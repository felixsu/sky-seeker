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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felixsu.skyseeker.R;
import com.felixsu.skyseeker.constant.Constants;
import com.felixsu.skyseeker.constant.LogConstants;
import com.felixsu.skyseeker.listener.ActivityCallbackListener;
import com.felixsu.skyseeker.model.ForecastWrapper;
import com.felixsu.skyseeker.model.forecast.Forecast;
import com.felixsu.skyseeker.model.request.ForecastRequest;
import com.felixsu.skyseeker.util.ForecastUtil;
import com.felixsu.skyseeker.util.Util;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class ForecastFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = ForecastFragment.class.getName();
    Button mGetForecastButton;
    Button mToggleDetailButton;
    private ImageView mWeatherIcon;
    private TextView mLocationNameLabel;
    private TextView mTemperatureLabel;
    private TextView mApparentTemperatureLabel;
    private TextView mWeatherDetailLabel;
    private TextView mWindDirectionLabel;
    private TextView mWindSpeedLabel;
    private TextView mHumidityLabel;
    private TextView mPrecipChanceLabel;
    private ForecastWrapper mForecastWrapper;
    private Forecast mForecast;
    private ExpandableRelativeLayout mExpandableLayout;
    private ActivityCallbackListener mListener;

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_getForecast:
                updateWeather();
                break;
            case R.id.button_toggleDetail:
                toggleDetailView();
                break;
            default:
                Log.w(TAG, "clicking unregistered object");
                break;
        }
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
        mWeatherIcon = (ImageView) rootView.findViewById(R.id.ic_weather);
        mLocationNameLabel = (TextView) rootView.findViewById(R.id.label_locationName);
        mTemperatureLabel = (TextView) rootView.findViewById(R.id.label_temperature);
        mApparentTemperatureLabel = (TextView) rootView.findViewById(R.id.label_apparentTemperature);
        mWeatherDetailLabel = (TextView) rootView.findViewById(R.id.label_weatherDetail);
        mWindDirectionLabel = (TextView) rootView.findViewById(R.id.label_windDirection);
        mWindSpeedLabel = (TextView) rootView.findViewById(R.id.label_windSpeed);
        mHumidityLabel = (TextView) rootView.findViewById(R.id.label_humidity);
        mPrecipChanceLabel = (TextView) rootView.findViewById(R.id.label_precipChance);

        mGetForecastButton = (Button) rootView.findViewById(R.id.button_getForecast);
        mToggleDetailButton = (Button) rootView.findViewById(R.id.button_toggleDetail);

        mGetForecastButton.setOnClickListener(this);
        mToggleDetailButton.setOnClickListener(this);

        mExpandableLayout = (ExpandableRelativeLayout) rootView.findViewById(R.id.expandable_detail);
    }

    private void updateViewValue(){
        //todo not complete yet
        Log.i(TAG, "i am " + getTag());
        final String temperatureCelsiusText = "%d " + "°C";
        final String feelsLikeCelsiusText = "Feels like %d " + "°C";
        final String windSpeedText = "%d " + "m/s";
        final String humidityText = "%d " + "%%";
        final String precipChanceText = "%d " + "%%";

        if (mForecastWrapper != null) {
            mLocationNameLabel.setText(mForecastWrapper.getSubAdministrativeLocation() + ", " + mForecastWrapper.getCountry());
        } else {
            mLocationNameLabel.setText("not available");
        }

        if (mForecast != null){
            mWeatherIcon.setImageResource(ForecastUtil.getIcon(mForecast.getCurrently().getIcon()));
            mTemperatureLabel.setText(String.format(temperatureCelsiusText, mForecast.getCurrently().getTemperature().intValue()));
            mApparentTemperatureLabel.setText(String.format(feelsLikeCelsiusText, mForecast.getCurrently().getApparentTemperature().intValue()));
            mWeatherDetailLabel.setText(mForecast.getHourly().getSummary());
            mWindDirectionLabel.setText(ForecastUtil.directionValue(mForecast.getCurrently().getWindBearing()));
            mWindSpeedLabel.setText(String.format(windSpeedText, mForecast.getCurrently().getWindSpeed().intValue()));
            mHumidityLabel.setText(String.format(humidityText, Util.percentValue(mForecast.getCurrently().getHumidity())));
            mPrecipChanceLabel.setText(String.format(precipChanceText, Util.percentValue(mForecast.getCurrently().getPrecipProbability())));
        } else {
            mWeatherIcon.setImageResource(R.drawable.ic_weather_sunny);
            mTemperatureLabel.setText("-");
            mApparentTemperatureLabel.setText("Feels like -");
            mWeatherDetailLabel.setText("please refresh");
            mWindDirectionLabel.setText("-");
            mWindSpeedLabel.setText("-");
            mHumidityLabel.setText("-");
            mPrecipChanceLabel.setText("-");
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

    public void toggleDetailView() {
        mExpandableLayout.toggle();
    }


}
