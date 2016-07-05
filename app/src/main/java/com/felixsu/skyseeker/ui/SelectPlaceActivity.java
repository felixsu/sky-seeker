package com.felixsu.skyseeker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.felixsu.skyseeker.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class SelectPlaceActivity extends AppCompatActivity {

    public static final String TAG = SelectPlaceActivity.class.getName();

    public static final String RESULT_LONGITUDE = "result-select-place-activity-longitude";
    public static final String RESULT_LATITUDE = "result-select-place-activity-latitude";
    public static final String RESULT_PLACE_NAME = "result-select-place-activity-name";

    private PlaceAutocompleteFragment mPlaceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_place);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
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

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mPlaceFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        mPlaceFragment.setFilter(typeFilter);

        mPlaceFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());//get place details here
                Intent intent = getIntent();
                if (intent != null) {
                    intent.putExtra(RESULT_LONGITUDE, place.getLatLng().longitude);
                    intent.putExtra(RESULT_LATITUDE, place.getLatLng().latitude);
                    intent.putExtra(RESULT_PLACE_NAME, place.getName());

                    setResult(RESULT_OK, intent);
                } else {
                    Log.e(TAG, "unexpected error. intent null");
                    setResult(RESULT_CANCELED);
                }
                finish();

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

}
