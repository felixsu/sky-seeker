package com.felixsu.skyseeker.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixsu.skyseeker.R;
import com.felixsu.skyseeker.SkySeekerApp;
import com.felixsu.skyseeker.constant.Constants;
import com.felixsu.skyseeker.listener.OnForecastUpdatedListener;
import com.felixsu.skyseeker.model.forecast.Forecast;
import com.felixsu.skyseeker.ui.fragment.MainFragment;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnForecastUpdatedListener {

    public static final String TAG = MainActivity.class.getName();

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FragmentManager mFragmentManager;

    @Inject protected SharedPreferences mSharedPreferences;
    @Inject protected ObjectMapper mObjectMapper;

    private Forecast mForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((SkySeekerApp)getApplication()).getUtilComponent().inject(this);
        initDrawer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
        initFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    private void initFragment(){

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.fragmentContainer, MainFragment.newInstance(mForecast), MainFragment.TAG).commit();

    }

    private void initData(){
        try {
            if (mSharedPreferences.contains(Constants.PREFERENCE_FORECAST)) {
                String storedForecast = mSharedPreferences.getString(Constants.PREFERENCE_FORECAST, null);
                if (storedForecast != null) {
                    mForecast = mObjectMapper.readValue(storedForecast, Forecast.class);
                } else {
                    mForecast = null;
                }
            } else {
                mForecast = null;
            }
        } catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void storeData(){
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
        editor.putString(Constants.PREFERENCE_FORECAST, forecastJson);
    }
}
