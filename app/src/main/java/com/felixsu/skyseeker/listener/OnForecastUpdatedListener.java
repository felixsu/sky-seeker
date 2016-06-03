package com.felixsu.skyseeker.listener;

import android.net.Uri;

import com.felixsu.skyseeker.model.forecast.Forecast;

/**
 * Created by felixsu on 02/06/2016.
 */
public interface OnForecastUpdatedListener {
    void onUpdate(Forecast forecast);
}
