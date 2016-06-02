package com.felixsu.skyseeker.service;

import com.felixsu.skyseeker.constant.Constants;
import com.felixsu.skyseeker.model.request.ForecastRequest;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by felixsu on 01/06/2016.
 */

public class ForecastService {
    private static final String PARAM_UNIT = "unit";
    private static final OkHttpClient CLIENT = new OkHttpClient();

    public void getForecast(ForecastRequest forecastRequest, Callback callback){
        String latitude = String.valueOf(forecastRequest.getLatitude());
        String longitude = String.valueOf(forecastRequest.getLongitude());
        String unit = forecastRequest.getUnit();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .host(Constants.FORECAST_BASE_URL)
                .addPathSegment(latitude + "," + longitude)
                .addQueryParameter(PARAM_UNIT, unit)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();

        CLIENT.newCall(request).enqueue(callback);
    }

}
