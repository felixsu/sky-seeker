package com.felixsu.skyseeker.ui.fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.felixsu.skyseeker.R;
import com.felixsu.skyseeker.constant.Constants;
import com.felixsu.skyseeker.constant.LogConstants;
import com.felixsu.skyseeker.listener.ActivityCallbackListener;
import com.felixsu.skyseeker.model.ForecastWrapper;
import com.felixsu.skyseeker.model.forecast.Forecast;
import com.felixsu.skyseeker.model.request.ForecastRequest;

public class ForecastFragment extends Fragment {

    public static final String TAG = ForecastFragment.class.getName();

    TextView mTemperatureLabel;
    TextView mSummaryLabel;
    TextView mLatitudeLabel;
    TextView mLongitudeLabel;
    TextView mPrimaryAddressLabel;
    TextView mSecondaryAddressLabel;

    Button mGetForecastButton;
    private ForecastWrapper mForecastWrapper;
    private Forecast mForecast;
    private ActivityCallbackListener mListener;

    private View.OnClickListener getForecastButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateWeather();
        }
    };

    public ForecastFragment() {
    }

    public static ForecastFragment newInstance(ForecastWrapper forecastWrapper) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();

        if (forecastWrapper != null) {
            args.putSerializable(Constants.BUNDLE_FORECAST, forecastWrapper);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initForecastData(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        updateViewValue();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ActivityCallbackListener) {
            mListener = (ActivityCallbackListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ActivityCallbackListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initForecastData(Bundle bundle){
        if (bundle.containsKey(Constants.BUNDLE_FORECAST)){
            mForecastWrapper = (ForecastWrapper) bundle.getSerializable(Constants.BUNDLE_FORECAST);
            if (mForecastWrapper == null) {
                Toast.makeText(getActivity(), "something goes wrong, contact our support", Toast.LENGTH_SHORT).show();
                throw new RuntimeException("Forecast wrapper null");
            }

            mForecast = mForecastWrapper.getForecast();
            if (mForecast == null) {
                updateWeather();
            }

        }
    }

    private void updateWeather(){
        if (mListener != null) {
            if (mForecastWrapper.isPrimary()) {
                Location location = mListener.getLocation();
                if (location == null) {
                    Toast.makeText(getActivity(), "Location not available", Toast.LENGTH_SHORT).show();
                    return;
                }
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                Log.i(TAG, "requesting forecast for " + latitude + " " + longitude);
                mListener.onRequestForecast(new ForecastRequest(latitude, longitude, ForecastRequest.SI_UNIT), mForecastWrapper.getUuid());
            } else {
                double longitude = mForecastWrapper.getLongitude();
                double latitude = mForecastWrapper.getLatitude();
                mListener.onRequestForecast(new ForecastRequest(latitude, longitude, ForecastRequest.SI_UNIT), mForecastWrapper.getUuid());
            }
        }
    }

    private void initView(View rootView){
        mTemperatureLabel = (TextView) rootView.findViewById(R.id.label_temperature);
        mSummaryLabel = (TextView) rootView.findViewById(R.id.label_summary);
        mLatitudeLabel = (TextView) rootView.findViewById(R.id.label_latitude);
        mLongitudeLabel = (TextView) rootView.findViewById(R.id.label_longitude);
        mPrimaryAddressLabel = (TextView) rootView.findViewById(R.id.label_primaryAddress);
        mSecondaryAddressLabel = (TextView) rootView.findViewById(R.id.label_secondaryAddress);

        mGetForecastButton = (Button) rootView.findViewById(R.id.button_getForecast);

        mGetForecastButton.setOnClickListener(getForecastButtonListener);
    }

    private void updateViewValue(){
        Log.i(TAG, "i am " + getTag());
        if (mForecastWrapper != null) {
            mLatitudeLabel.setText(String.valueOf(mForecastWrapper.getLatitude()));
            mLongitudeLabel.setText(String.valueOf(mForecastWrapper.getLongitude()));
            mPrimaryAddressLabel.setText(mForecastWrapper.getPrimaryLocation());
            mSecondaryAddressLabel.setText(mForecastWrapper.getSecondaryLocation());
        }

        if (mForecast != null){
            mTemperatureLabel.setText(mForecast.getCurrently().getTemperature().toString());
            mSummaryLabel.setText(mForecast.getCurrently().getSummary());
        } else {
            mTemperatureLabel.setText("not available");
            mSummaryLabel.setText("not available");
        }
    }

    public void onForecastUpdated(Bundle bundle) {
        mForecastWrapper = (ForecastWrapper) bundle.getSerializable(Constants.BUNDLE_FORECAST);
        if (mForecastWrapper == null) {
            Log.e(TAG, LogConstants.UNEXPECTED_ERROR);
            throw new RuntimeException(LogConstants.UNEXPECTED_ERROR);
        }
        mForecast = mForecastWrapper.getForecast();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateViewValue();
            }
        });


    }
}
