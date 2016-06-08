package com.felixsu.skyseeker.listener;

import com.felixsu.skyseeker.model.request.ForecastRequest;

/**
 * Created by felixsu on 02/06/2016.
 */
public interface ActivityCallbackListener {
    void onRequest(ForecastRequest request, int id);
}
