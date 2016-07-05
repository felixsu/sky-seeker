package com.felixsu.skyseeker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    private TextView mPlaceNameLabel;
    private EditText mPlaceNameField;
    private Button mSubmitButton;

    private Place mSelectedPlace;

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

        initAutoCompletePlaceFragment();

        mPlaceNameLabel = (TextView) findViewById(R.id.label_place_name);
        mPlaceNameField = (EditText) findViewById(R.id.field_place_name);
        mSubmitButton = (Button) findViewById(R.id.button_submit_place);

        mPlaceNameLabel.setText("-");
        mSubmitButton.setEnabled(false);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitResult();
            }
        });
    }

    private void initAutoCompletePlaceFragment() {
        mPlaceFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        mPlaceFragment.setFilter(typeFilter);

        mPlaceFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mSelectedPlace = place;
                mPlaceNameLabel.setText(place.getName());
                mSubmitButton.setEnabled(true);
                Log.i(TAG, "Place: " + place.getName());//get place details here
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void submitResult() {
        Intent intent = getIntent();
        if (intent != null) {
            intent.putExtra(RESULT_LONGITUDE, mSelectedPlace.getLatLng().longitude);
            intent.putExtra(RESULT_LATITUDE, mSelectedPlace.getLatLng().latitude);
            intent.putExtra(RESULT_PLACE_NAME, mSelectedPlace.getName());

            setResult(RESULT_OK, intent);
        } else {
            Log.e(TAG, "unexpected error. intent null");
            setResult(RESULT_CANCELED);
        }
        finish();
    }

}
