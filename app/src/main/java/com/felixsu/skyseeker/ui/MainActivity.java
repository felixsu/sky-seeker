package com.felixsu.skyseeker.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixsu.skyseeker.R;
import com.felixsu.skyseeker.SkySeekerApp;
import com.felixsu.skyseeker.constant.Constants;
import com.felixsu.skyseeker.constant.LogConstants;
import com.felixsu.skyseeker.listener.ActivityCallbackListener;
import com.felixsu.skyseeker.model.ForecastWrapper;
import com.felixsu.skyseeker.model.GlobalData;
import com.felixsu.skyseeker.model.User;
import com.felixsu.skyseeker.model.forecast.Forecast;
import com.felixsu.skyseeker.model.request.ForecastRequest;
import com.felixsu.skyseeker.receiver.GeoCoderReceiver;
import com.felixsu.skyseeker.receiver.Receiver;
import com.felixsu.skyseeker.service.ForecastService;
import com.felixsu.skyseeker.service.GeocoderService;
import com.felixsu.skyseeker.ui.fragment.ForecastFragment;
import com.felixsu.skyseeker.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ActivityCallbackListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        Receiver {

    public static final String TAG = MainActivity.class.getName();
    private static final int PLAY_SERVICE_RESOLUTION_REQUEST = 1000;
    private static final int PERMISSION_LOCATION_COARSE_REQUEST = 2001;
    private static final int PERMISSION_LOCATION_FINE_REQUEST = 2002;
    private static final int LOCATION_REQUEST = 1001;

    private static final int UPDATE_INTERVAL = 30000;
    private static final int FASTEST_INTERVAL = 5000;
    private static final int DISPLACEMENT = 10;

    @Inject
    protected ForecastService mForecastService;
    @Inject protected SharedPreferences mSharedPreferences;
    @Inject protected ObjectMapper mObjectMapper;

    protected List<ForecastWrapper> mWrapperList = new ArrayList<>();
    protected GlobalData mGlobalData;
    protected User mUser;
    private boolean mFromSignIn = false;
    private Bundle mSignInBundle;

    //View
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Menu mNavigationMenu;
    private CircularImageView mNavProfilePictureIcon;
    private TextView mNavGreetingsLabel;
    private TextView mNavNameLabel;
    private TextView mNavEmailLabel;

    private FragmentManager mFragmentManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private boolean mIsRequestingPermission = false;
    private GeoCoderReceiver mReceiver;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((SkySeekerApp)getApplication()).getUtilComponent().inject(this);

        if (isGooglePlayServiceAvailable()) {
            Log.d(TAG, "google play service is available");
            //initData will load all available forecast stored before
            initView();
            initData();
            initFragment();
            //forecast is filled, available to be read.
            initDrawer();
        }
    }

    private void initView() {
        mNavProfilePictureIcon = (CircularImageView) findViewById(R.id.icon_navigationHeader);
        mNavGreetingsLabel = (TextView) findViewById(R.id.label_navigationHeaderGreetings);
        mNavNameLabel = (TextView) findViewById(R.id.label_navigationHeaderName);
        mNavEmailLabel = (TextView) findViewById(R.id.label_navigationHeaderEmail);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        attachReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();

        isGooglePlayServiceAvailable();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        deAttachReceiver();
        storeData();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST) {
            if (resultCode == RESULT_OK) {

                Double longitude = data.getDoubleExtra(SelectPlaceActivity.RESULT_LONGITUDE, 0.0);
                Double latitude = data.getDoubleExtra(SelectPlaceActivity.RESULT_LATITUDE, 0.0);
                String placeName = data.getStringExtra(SelectPlaceActivity.RESULT_PLACE_NAME);
                Log.i(TAG, "long: " + longitude + " lat: " + latitude);

                UUID uuid = UUID.randomUUID();
                ForecastWrapper wrapper = new ForecastWrapper(uuid.toString(), placeName, null, null, null, null, null, null, latitude, longitude, false);

                int id = View.generateViewId();
                wrapper.setViewId(id);

                MenuItem menuItem = mNavigationMenu.findItem(R.id.nav_item_location_holder).getSubMenu().add(R.id.nav_group_location, id, 0, wrapper.getName());
                menuItem.setIcon(R.drawable.ic_location_on_black_24dp);

                mWrapperList.add(wrapper);
                showForecast(wrapper);
            } else {
                Log.i(TAG, "location result cancelled/failed");
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        ForecastWrapper wrapper = getWrapperWithViewId(id);
        if (wrapper != null) {
            showForecast(wrapper);
        } else {
            if (id == R.id.nav_signout) {
                Log.d(TAG, "Sign out pressed");
                signOut();
            }

            if (id == R.id.nav_item_add_location) {
                addNewLocation();
            }
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestForecast(ForecastRequest request, String uuid) {
        ForecastWrapper wrapper = getWrapperWithId(uuid);
        wrapper.setLatitude(request.getLatitude());
        wrapper.setLongitude(request.getLongitude());
        mForecastService.getForecast(request, new ForecastRequestListener(uuid));
    }

    @Override
    public Location getLocation() {
        return mLastLocation;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "connection failed " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "google api connected");
        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Log.i(TAG, "location changes " + location.getLongitude() + ", " + location.getLatitude());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mIsRequestingPermission = false;
        switch (requestCode) {
            case PERMISSION_LOCATION_COARSE_REQUEST: {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    requestPermission();
                } else {
                    Toast.makeText(this, "App will not working, please close", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case PERMISSION_LOCATION_FINE_REQUEST: {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    requestPermission();
                } else {
                    Toast.makeText(this, "App will not working, please close", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Log.d(TAG, "receiving back from GeocoderService");
        String uuid = resultData.getString(GeocoderService.RESULT_UUID);
        String primaryAddress;
        String secondaryAddress;
        String countryCode;
        String subAdmin;
        String admin;

        switch (resultCode) {
            case Constants.RETURN_OK:
                ArrayList<String> listOfAddress = resultData.getStringArrayList(GeocoderService.RESULT_ADDRESSES);
                countryCode = resultData.getString(GeocoderService.RESULT_COUNTRY_CODE);
                subAdmin = resultData.getString(GeocoderService.RESULT_SUB_ADMINISTRATIVE_NAME);
                admin = resultData.getString(GeocoderService.RESULT_ADMINISTRATIVE_NAME);
                int size = listOfAddress.size();
                if (size > 0) {
                    primaryAddress = listOfAddress.get(0);
                    if (size > 1) {
                        secondaryAddress = listOfAddress.get(1);
                    } else {
                        secondaryAddress = "not found";
                    }
                } else {
                    primaryAddress = "not found";
                    secondaryAddress = "not found";
                }
                break;
            case Constants.RETURN_NOT_FOUND:
                primaryAddress = "not found";
                secondaryAddress = "not found";
                countryCode = "not found";
                subAdmin = "not found";
                admin = "not found";
                break;
            case Constants.RETURN_ERROR:
            default:
                Log.e(TAG, LogConstants.UNEXPECTED_ERROR);
                primaryAddress = "error";
                secondaryAddress = "error";
                countryCode = "error";
                subAdmin = "error";
                admin = "error";

                break;
        }

        Bundle bundle = new Bundle();
        ForecastWrapper wrapper = getWrapperWithId(uuid);

        wrapper.setPrimaryLocation(primaryAddress);
        wrapper.setSecondaryLocation(secondaryAddress);
        wrapper.setCountry(countryCode);
        wrapper.setSubAdministrativeLocation(subAdmin);
        wrapper.setAdministrativeLocation(admin);

        //FORECAST UPDATED
        bundle.putSerializable(Constants.BUNDLE_FORECAST, wrapper);
        updateUserData();
        pushUpdateInfo();
        ((ForecastFragment) mFragmentManager.findFragmentByTag(uuid)).onForecastUpdated(bundle);
    }

    private void pushUpdateInfo() {
        Bundle b = new Bundle();
        b.putString(LogConstants.FA_KEY_INFO_WEATHER_UPDATE, "weather updated");
        mFirebaseAnalytics.logEvent(LogConstants.FA_TITLE_INFO, b);
    }

    private void attachReceiver() {
        mReceiver = new GeoCoderReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    private void deAttachReceiver() {
        mReceiver = null;
    }

    private boolean isGooglePlayServiceAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(this, "device not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!mIsRequestingPermission) {
                requestPermission();
            }
            Log.i(TAG, "some permission not granted");
            return;
        }
        Log.d(TAG, "Location updates started");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

    }

    private void initDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        mNavigationMenu = mNavigationView.getMenu();

        MenuItem locationHolder = mNavigationMenu.findItem(R.id.nav_item_location_holder);
        for (ForecastWrapper wrapper : mWrapperList) {
            int id = View.generateViewId();
            wrapper.setViewId(id);

            MenuItem menuItem = locationHolder.getSubMenu().add(R.id.nav_group_location, id, 0, wrapper.getName());
            menuItem.setIcon(R.drawable.ic_location_on_black_24dp);
        }
    }

    private void displayLocation() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            Log.i(TAG, "some permission not granted");
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            Log.i(TAG, "last location received " + latitude + " " + longitude);
        } else {
            Log.i(TAG, "couldn't get last location");
        }
    }

    private void initFragment(){
        ForecastWrapper primaryWrapper = null;
        for (ForecastWrapper wrapper : mWrapperList) {
            if (wrapper.isPrimary()) {
                primaryWrapper = wrapper;
                break;
            }
        }

        if (primaryWrapper == null) {
            Toast.makeText(this, LogConstants.UNEXPECTED_ERROR, Toast.LENGTH_SHORT).show();
            Log.e(TAG, LogConstants.UNEXPECTED_ERROR);
            throw new RuntimeException(LogConstants.UNEXPECTED_ERROR);
        }
        mFragmentManager = getSupportFragmentManager();
        showForecast(primaryWrapper);
        mGlobalData.setCurrentActiveUuid(null);

    }

    private void initData(){
        initGoogleApiClient();
        initLocationRequest();
        initFirebaseAnalytics();
        initFirebaseDatabase();
        initFirebaseAuthAndUser();
        fromSignInValidation();
        loadAppData();
    }

    private void initFirebaseAuthAndUser() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth != null) {
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            if (mFirebaseUser == null) {
                Toast.makeText(this, "Auth Not Valid", Toast.LENGTH_SHORT).show();
                signOut();
            }
        }
    }

    private void initFirebaseDatabase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    private void fromSignInValidation() {
        Intent i = getIntent();
        if (i != null && i.hasExtra(User.BUNDLE_NAME)) {
            Bundle b = new Bundle();
            b.putString(LogConstants.FA_KEY_INFO_FROM_SIGN_IN, "user come from sign in");
            mFirebaseAnalytics.logEvent(LogConstants.FA_TITLE_INFO, b);
            mFromSignIn = true;
            mSignInBundle = i.getBundleExtra(User.BUNDLE_NAME);
        }
    }

    private void initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void loadAppData() {
        try {
            if (mSharedPreferences.contains(Constants.PREFERENCE_GLOBAL_DATA)) {
                String globalData = mSharedPreferences.getString(Constants.PREFERENCE_GLOBAL_DATA, null);
                if (globalData != null) {
                    mGlobalData = mObjectMapper.readValue(globalData, GlobalData.class);
                    Log.d(TAG, "global data loaded successfully");
                    mWrapperList = mGlobalData.getForecastWrapperList();
                    mUser = mGlobalData.getUser();
                    if (mWrapperList.isEmpty()) {
                        Log.w(TAG, "forecast data not found on stored data");
                        UUID uuid = UUID.randomUUID();
                        ForecastWrapper wrapper = new ForecastWrapper(uuid.toString(), Constants.FRAGMENT_NAME_DEFAULT, null, null, null, null, null, null, 0, 0, true);
                        mWrapperList.add(wrapper);
                    }
                    if (mUser == null) {
                        Log.w(TAG, "user data not found on stored data");
                        if (!mFromSignIn) {
                            Toast.makeText(this, "re-Authenticate", Toast.LENGTH_SHORT).show();
                            signOut();
                        } else {
                            mUser = new User();
                            setupUserData();
                            mGlobalData.setUser(mUser);
                        }
                    }
                }
            } else {
                Log.i(TAG, "first run detected");
                mGlobalData = new GlobalData();
                mWrapperList = new ArrayList<>();
                mUser = new User();

                setupUserData();
                updateUserData();

                mGlobalData.setForecastWrapperList(mWrapperList);
                mGlobalData.setUser(mUser);

                //generate first forecast location
                UUID uuid = UUID.randomUUID();
                ForecastWrapper wrapper = new ForecastWrapper(uuid.toString(), Constants.FRAGMENT_NAME_DEFAULT, null, null, null, null, null, null, 0, 0, true);
                mWrapperList.add(wrapper);
            }
        } catch (IOException e) {
            Log.w(TAG, "failed deserialize wrapper list");
            Log.e(TAG, e.getMessage(), e);
        } catch (Exception e) {
            Log.e(TAG, "unexpected exception", e);
        }
    }

    private void setupUserData() {
        if (mSignInBundle != null) {
            mUser.setName(mSignInBundle.getString(User.KEY_NAME));
            mUser.setEmail(mSignInBundle.getString(User.KEY_EMAIL));
            mUser.setPictureUrl(mSignInBundle.getString(User.KEY_PICTURE_URL));
        } else {
            mUser.setName("not initialize");
            mUser.setEmail("not initialize");
            mUser.setPictureUrl("not initialize");
        }
        mUser.setJoinDate(new Date().getTime());
    }

    private void storeData() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        try {
            if (mGlobalData != null) {
                String globalDataJson = mObjectMapper.writeValueAsString(mGlobalData);
                editor.putString(Constants.PREFERENCE_GLOBAL_DATA, globalDataJson).apply();
            }
        } catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
            Toast.makeText(this, "Unexpected error, contact dev", Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "global data stored successfully");
    }

    private void requestPermission() {
        mIsRequestingPermission = true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_LOCATION_COARSE_REQUEST);
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_LOCATION_FINE_REQUEST);
            return;
        }
        displayLocation();
        startLocationUpdates();
        Log.i(TAG, "all permission granted");
    }

    private ForecastWrapper getWrapperWithId(String uuid) {
        ForecastWrapper result = null;
        boolean found = false;

        for (ForecastWrapper wrapper : mWrapperList) {
            if (wrapper.getUuid().equals(uuid)) {
                result = wrapper;
                found = true;
                break;
            }
        }
        if (!found) {
            Log.e(TAG, LogConstants.UNEXPECTED_ERROR);
            throw new RuntimeException(LogConstants.UNEXPECTED_ERROR);
        }
        return result;
    }

    private ForecastWrapper getWrapperWithViewId(int viewId) {
        ForecastWrapper result = null;
        Log.d(TAG, "try finding wrapper with view Id " + viewId);
        for (ForecastWrapper wrapper : mWrapperList) {
            if (wrapper.getViewId() == viewId) {
                result = wrapper;
                break;
            }
        }
        return result;
    }

    private ForecastWrapper getPrimaryWrapper() {
        ForecastWrapper result = null;
        for (ForecastWrapper wrapper : mWrapperList) {
            if (wrapper.isPrimary()) {
                result = wrapper;
                break;
            }
        }
        return result;
    }

    private void startGeocoderService(String uuid) {
        Intent intent = new Intent(this, GeocoderService.class);
        intent.putExtra(GeocoderService.TAG, mReceiver);
        intent.putExtra(GeocoderService.EXTRA_UUID, uuid);
        intent.putExtra(GeocoderService.EXTRA_REQUEST_CODE, GeocoderService.REQUEST_WITH_LONG_LAT);
        intent.putExtra(GeocoderService.EXTRA_LATITUDE, getWrapperWithId(uuid).getLatitude());
        intent.putExtra(GeocoderService.EXTRA_LONGITUDE, getWrapperWithId(uuid).getLongitude());
        startService(intent);
    }

    private void addNewLocation() {
        Intent intent = new Intent(this, SelectPlaceActivity.class);
        startActivityForResult(intent, LOCATION_REQUEST);
    }

    private void showForecast(ForecastWrapper wrapper) {
        String currentUuid = mGlobalData.getCurrentActiveUuid();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (currentUuid != null) {
            Log.i(TAG, "trying to replace fragment with uuid " + currentUuid + " with " + wrapper.getUuid());
            ft.replace(R.id.fragmentContainer, ForecastFragment.newInstance(wrapper), wrapper.getUuid());
        } else {
            ft.add(R.id.fragmentContainer, ForecastFragment.newInstance(wrapper), wrapper.getUuid());
        }
        ft.commit();
        mGlobalData.setCurrentActiveUuid(wrapper.getUuid());
    }

    private void updateUserData() {
        if (mUser != null) {
            mUser.setAndroidOs(Util.getAndroidVersion());
            mUser.setAppVersion(Util.getAppVersion());
            mUser.setPhoneModel(Util.getDeviceName());

            ForecastWrapper primaryWrapper = getPrimaryWrapper();
            if (primaryWrapper != null) {
                mUser.setLocation(primaryWrapper.getSubAdministrativeLocation());
                mUser.setCountry(primaryWrapper.getCountry());
            }
            Log.d(TAG, "updating database " + mFirebaseUser.getUid());
            DatabaseReference reference = mFirebaseDatabase.getReference(User.FIREBASE_USER_REFERENCE);
            reference.child(mFirebaseUser.getUid()).setValue(mUser, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.e(TAG, databaseError.getMessage());
                    } else {
                        Log.d(TAG, "update db success");
                    }
                }
            });
        } else {
            Log.e(TAG, "user null");
            Bundle b = new Bundle();
            b.putString(LogConstants.FA_KEY_ERR_USER, "fatal, error user null");
            mFirebaseAnalytics.logEvent(LogConstants.FA_TITLE_ERROR, b);
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startLoginActivity();
    }

    private class ForecastRequestListener implements Callback {

        private String mUuid;

        public ForecastRequestListener(String uuid) {
            mUuid = uuid;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(TAG, e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code() == HttpURLConnection.HTTP_OK) {

                Forecast forecast = mObjectMapper.readValue(response.body().string(), Forecast.class);
                ForecastWrapper wrapper = getWrapperWithId(mUuid);
                wrapper.setForecast(forecast);
                startGeocoderService(mUuid);
            } else {
                Log.e(TAG, "got response status" + response.code() + "-" + response.body().string());
            }
        }
    }


}
