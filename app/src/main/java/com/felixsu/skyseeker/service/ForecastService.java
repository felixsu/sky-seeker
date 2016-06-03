package com.felixsu.skyseeker.service;

import android.util.Log;

import com.felixsu.skyseeker.constant.Constants;
import com.felixsu.skyseeker.model.request.ForecastRequest;

import javax.inject.Inject;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by felixsu on 01/06/2016.
 */

public class ForecastService {
    private static final String TAG = ForecastService.class.getName();

    private static final String PARAM_UNIT = "units";

    private OkHttpClient mClient;

    public ForecastService(OkHttpClient client) {
        mClient = client;
    }

    public void getForecast(ForecastRequest forecastRequest, Callback callback){
        String latitude = String.valueOf(forecastRequest.getLatitude());
        String longitude = String.valueOf(forecastRequest.getLongitude());
        String unit = forecastRequest.getUnit();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(Constants.FORECAST_HOST)
                .addPathSegment(Constants.FORECAST_FC_SEGMENT)
                .addPathSegment(Constants.FORECAST_API_KEY)
                .addPathSegment(latitude + "," + longitude)
                .addQueryParameter(PARAM_UNIT, unit)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();
        Log.d(TAG, "HTTP request " + request.method() + " " + httpUrl.toString());
        mClient.newCall(request).enqueue(callback);
    }

}
