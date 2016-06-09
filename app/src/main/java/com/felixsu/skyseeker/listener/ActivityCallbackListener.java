package com.felixsu.skyseeker.listener;

import android.location.Location;

import com.felixsu.skyseeker.model.request.ForecastRequest;

/**
 * Created by felixsu on 02/06/2016.
 */
public interface ActivityCallbackListener {
    void onRequestForecast(ForecastRequest request, String uuid);

    Location getLocation();

}
