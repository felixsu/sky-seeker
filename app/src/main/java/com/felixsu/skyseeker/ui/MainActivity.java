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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.felixsu.skyseeker.R;
import com.felixsu.skyseeker.SkySeekerApp;
import com.felixsu.skyseeker.constant.Constants;
import com.felixsu.skyseeker.constant.LogConstants;
import com.felixsu.skyseeker.listener.ActivityCallbackListener;
import com.felixsu.skyseeker.model.ForecastWrapper;
import com.felixsu.skyseeker.model.forecast.Forecast;
import com.felixsu.skyseeker.model.request.ForecastRequest;
import com.felixsu.skyseeker.receiver.GeoCoderReceiver;
import com.felixsu.skyseeker.receiver.Receiver;
import com.felixsu.skyseeker.service.ForecastService;
import com.felixsu.skyseeker.service.GeoCoderService;
import com.felixsu.skyseeker.ui.fragment.ForecastFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
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

    private static final int UPDATE_INTERVAL = 30000;
    private static final int FASTEST_INTERVAL = 5000;
    private static final int DISPLACEMENT = 10;

    @Inject
    protected ForecastService mForecastService;
    @Inject protected SharedPreferences mSharedPreferences;
    @Inject protected ObjectMapper mObjectMapper;

    protected List<ForecastWrapper> mWrapperList = new ArrayList<>();

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private FragmentManager mFragmentManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private boolean mIsRequestingPermission = false;
    private GeoCoderReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((SkySeekerApp)getApplication()).getUtilComponent().inject(this);

        if (isGooglePlayServiceAvailable()) {
            Log.d(TAG, "google play service is available");
            initData();
            initFragment();
            initDrawer();
        }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signout) {
            Log.d(TAG, "Sign out pressed");
            FirebaseAuth.getInstance().signOut();
            startLoginActivity();
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

        Toast.makeText(this, "location changed !", Toast.LENGTH_SHORT).show();
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
        Log.d(TAG, "receiving back from geocoderService");
        String uuid = resultData.getString(GeoCoderService.RESULT_UUID);
        String primaryAddress;
        String secondaryAddress;

        switch (resultCode) {
            case Constants.RETURN_OK:
                ArrayList<String> listOfAddress = resultData.getStringArrayList(GeoCoderService.RESULT_ADDRESSES);
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
                break;
            case Constants.RETURN_ERROR:
            default:
                Log.e(TAG, LogConstants.UNEXPECTED_ERROR);
                primaryAddress = "error";
                secondaryAddress = "error";
                break;
        }

        Bundle bundle = new Bundle();
        ForecastWrapper wrapper = getWrapperWithId(uuid);

        wrapper.setPrimaryLocation(primaryAddress);
        wrapper.setSecondaryLocation(secondaryAddress);

        bundle.putSerializable(Constants.BUNDLE_FORECAST, wrapper);
        ((ForecastFragment) mFragmentManager.findFragmentByTag(uuid)).onForecastUpdated(bundle);
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
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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
        mFragmentManager.beginTransaction().add(R.id.fragmentContainer, ForecastFragment.newInstance(primaryWrapper), primaryWrapper.getUuid()).commit();

    }

    private void initData(){
        initGoogleApiClient();
        initLocationRequest();
        loadWrapperList();
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

    private void loadWrapperList() {
        try {
            if (mSharedPreferences.contains(Constants.PREFERENCE_WRAPPER_LIST)) {
                String storedWrapper = mSharedPreferences.getString(Constants.PREFERENCE_WRAPPER_LIST, null);
                if (storedWrapper != null) {
                    mWrapperList = mObjectMapper.readValue(storedWrapper, TypeFactory.defaultInstance().constructCollectionType(List.class, ForecastWrapper.class));
                    Log.d(TAG, "forecast wrapper loaded succesfully");

                    if (mWrapperList.isEmpty()) {
                        UUID uuid = UUID.fromString(TAG);
                        ForecastWrapper wrapper = new ForecastWrapper(uuid.toString(), null, null, null, 0, 0, true);
                        mWrapperList.add(wrapper);
                    }
                }
            } else {
                UUID uuid = UUID.randomUUID();
                ForecastWrapper wrapper = new ForecastWrapper(uuid.toString(), null, null, null, 0, 0, true);
                mWrapperList.add(wrapper);
            }
        } catch (IOException e) {
            Log.w(TAG, "failed deserialize wrapper list");
            Log.e(TAG, e.getMessage(), e);
        } catch (Exception e) {
            Log.e(TAG, "unexpected exception", e);
        }
    }

    private void storeData() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        try {
            if (mWrapperList != null) {
                String wrapperJson = mObjectMapper.writeValueAsString(mWrapperList);
                editor.putString(Constants.PREFERENCE_WRAPPER_LIST, wrapperJson).apply();
            }
        } catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
            Toast.makeText(this, "Unexpected error, contact dev", Toast.LENGTH_SHORT).show();
        }


        Log.d(TAG, "forecast stored successfully");
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

    private void startGeoCoderService(String uuid) {
        Intent intent = new Intent(this, GeoCoderService.class);
        intent.putExtra(GeoCoderService.TAG, mReceiver);
        intent.putExtra(GeoCoderService.EXTRA_UUID, uuid);
        intent.putExtra(GeoCoderService.EXTRA_LATITUDE, getWrapperWithId(uuid).getLatitude());
        intent.putExtra(GeoCoderService.EXTRA_LONGITUDE, getWrapperWithId(uuid).getLongitude());
        startService(intent);

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
                startGeoCoderService(mUuid);
            } else {
                Log.e(TAG, "got response status" + response.code() + "-" + response.body().string());
            }
        }
    }


}
