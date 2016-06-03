package com.felixsu.skyseeker.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixsu.skyseeker.R;
import com.felixsu.skyseeker.SkySeekerApp;
import com.felixsu.skyseeker.constant.Constants;
import com.felixsu.skyseeker.listener.OnForecastUpdatedListener;
import com.felixsu.skyseeker.model.forecast.Forecast;
import com.felixsu.skyseeker.model.request.ForecastRequest;
import com.felixsu.skyseeker.service.ForecastService;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainFragment extends Fragment {

    public static final String TAG = MainFragment.class.getName();

    TextView mTemperatureLabel;
    TextView mSummaryLabel;
    EditText mLongitudeField;
    EditText mLatitudeField;
    Button mGetForecastButton;

    Forecast mForecast;

    @Inject
    protected ForecastService mForecastService;
    @Inject
    protected SharedPreferences mSharedPreferences;
    @Inject
    protected ObjectMapper mObjectMapper;

    private OnForecastUpdatedListener mListener;

    public MainFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(Forecast forecast) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();

        if (forecast != null){
            args.putSerializable(Constants.BUNDLE_FORECAST, forecast);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SkySeekerApp app = (SkySeekerApp) getActivity().getApplication();
        app.getUtilComponent().inject(this);

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
        if (context instanceof OnForecastUpdatedListener) {
            mListener = (OnForecastUpdatedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnForecastUpdatedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initForecastData(Bundle bundle){
        if (bundle.containsKey(Constants.BUNDLE_FORECAST)){
            mForecast = (Forecast) bundle.getSerializable(Constants.BUNDLE_FORECAST);
        }
    }

    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(TAG, e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code() == HttpURLConnection.HTTP_OK) {
                String body = response.body().string();
                mForecast = mObjectMapper.readValue(body, Forecast.class);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateViewValue();
                    }
                });

                if (mListener != null) {
                    mListener.onUpdate(mForecast);
                    Log.d(TAG, "forecast updated");
                }
            } else {
                Log.e(TAG, "got response status" + response.code() + "-" + response.body().string());
            }
        }
    };

    private View.OnClickListener getForecastButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateWeather();
        }
    };

    private void updateWeather(){
        mForecastService.getForecast(new ForecastRequest(-6.193, 106.87, ForecastRequest.SI_UNIT), mCallback);
    }

    private void initView(View rootView){
        mTemperatureLabel = (TextView) rootView.findViewById(R.id.label_temperature);
        mSummaryLabel = (TextView) rootView.findViewById(R.id.label_summary);
        mLongitudeField = (EditText) rootView.findViewById(R.id.field_longitude);
        mLatitudeField = (EditText) rootView.findViewById(R.id.field_latitude);
        mGetForecastButton = (Button) rootView.findViewById(R.id.button_getForecast);

        mGetForecastButton.setOnClickListener(getForecastButtonListener);
    }

    private void updateViewValue(){
        if (mForecast != null){
            mTemperatureLabel.setText(mForecast.getCurrently().getTemperature().toString());
            mSummaryLabel.setText(mForecast.getCurrently().getSummary());
        } else {
            mTemperatureLabel.setText("not available");
            mSummaryLabel.setText("not available");
        }
    }


}
