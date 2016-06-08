package com.felixsu.skyseeker.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.felixsu.skyseeker.R;
import com.felixsu.skyseeker.constant.Constants;
import com.felixsu.skyseeker.listener.ActivityCallbackListener;
import com.felixsu.skyseeker.model.forecast.Forecast;
import com.felixsu.skyseeker.model.request.ForecastRequest;

public class ForecastFragment extends Fragment {

    public static final String TAG = ForecastFragment.class.getName();

    TextView mTemperatureLabel;
    TextView mSummaryLabel;
    Button mGetForecastButton;
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

    // TODO: Rename and change types and number of parameters
    public static ForecastFragment newInstance(Forecast forecast) {
        ForecastFragment fragment = new ForecastFragment();
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
            mForecast = (Forecast) bundle.getSerializable(Constants.BUNDLE_FORECAST);
        }
    }

    private void updateWeather(){
        if (mListener != null) {
            mListener.onRequest(new ForecastRequest(-6.193, 106.87, ForecastRequest.SI_UNIT), getId());
        }
    }

    private void initView(View rootView){
        mTemperatureLabel = (TextView) rootView.findViewById(R.id.label_temperature);
        mSummaryLabel = (TextView) rootView.findViewById(R.id.label_summary);
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

    public void onForecastUpdated(Forecast forecast) {
        mForecast = forecast;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateViewValue();
            }
        });
    }
}
