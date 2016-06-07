package com.felixsu.skyseeker.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
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
import com.felixsu.skyseeker.R;
import com.felixsu.skyseeker.SkySeekerApp;
import com.felixsu.skyseeker.constant.Constants;
import com.felixsu.skyseeker.listener.OnForecastUpdatedListener;
import com.felixsu.skyseeker.model.forecast.Forecast;
import com.felixsu.skyseeker.ui.fragment.MainFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnForecastUpdatedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String TAG = MainActivity.class.getName();
    private static final int PLAY_SERVICE_RESOLUTION_REQUEST = 1000;
    private static final int PERMISSION_LOCATION_COARSE_REQUEST = 1002;
    private static final int PERMISSION_LOCATION_FINE_REQUEST = 1003;

    private static final int UPDATE_INTERVAL = 10000;
    private static final int FASTEST_INTERVAL = 5000;
    private static final int DISPLACEMENT = 10;
    @Inject protected SharedPreferences mSharedPreferences;
    @Inject protected ObjectMapper mObjectMapper;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FragmentManager mFragmentManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Forecast mForecast;
    private Location mLastLocation;
    private boolean mRequestLocationUpdate = true;
    private boolean mIsRequestingPermission = false;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        isGooglePlayServiceAvailable();
        if (mGoogleApiClient.isConnected() && mRequestLocationUpdate) {
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

        storeForecastData();
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
    public void onUpdate(Forecast forecast) {
        mForecast = forecast;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "connection failed " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "google api connected");
        displayLocation();

        if (mRequestLocationUpdate) {
            startLocationUpdates();
        }
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

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.fragmentContainer, MainFragment.newInstance(mForecast), MainFragment.TAG).commit();

    }

    private void initData(){
        initGoogleApiClient();
        initLocationRequest();
        loadForecastData();
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

    private void loadForecastData() {
        try {
            if (mSharedPreferences.contains(Constants.PREFERENCE_FORECAST)) {
                String storedForecast = mSharedPreferences.getString(Constants.PREFERENCE_FORECAST, null);
                if (storedForecast != null) {
                    mForecast = mObjectMapper.readValue(storedForecast, Forecast.class);
                    Log.d(TAG, "forecast loaded successfully");
                } else {
                    mForecast = null;
                }
            } else {
                mForecast = null;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void storeForecastData() {
        String forecastJson = null;
        try {
            if (mForecast != null) {
                forecastJson = mObjectMapper.writeValueAsString(mForecast);
            } else {
                Log.i(TAG, "forecast data null");
            }
        } catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.PREFERENCE_FORECAST, forecastJson).apply();
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


}
