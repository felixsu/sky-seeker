package com.felixsu.skyseeker.model;

import com.felixsu.skyseeker.model.forecast.Forecast;

import java.io.Serializable;

/**
 * Created by felixsoewito on 6/9/16.
 */
public class ForecastWrapper implements Serializable {

    private String mUuid;
    private Forecast mForecast;
    private String mPrimaryLocation;
    private String mSecondaryLocation;
    private double mLatitude;
    private double mLongitude;
    private boolean mPrimary;

    public ForecastWrapper(String uuid, Forecast forecast, String primaryLocation, String secondaryLocation, double latitude, double longitude, boolean primary) {
        mUuid = uuid;
        mForecast = forecast;
        mPrimaryLocation = primaryLocation;
        mSecondaryLocation = secondaryLocation;
        mLatitude = latitude;
        mLongitude = longitude;
        mPrimary = primary;
    }

    public ForecastWrapper() {
    }

    public String getUuid() {
        return mUuid;
    }

    public void setUuid(String uuid) {
        mUuid = uuid;
    }

    public Forecast getForecast() {
        return mForecast;
    }

    public void setForecast(Forecast forecast) {
        mForecast = forecast;
    }

    public String getPrimaryLocation() {
        return mPrimaryLocation;
    }

    public void setPrimaryLocation(String primaryLocation) {
        mPrimaryLocation = primaryLocation;
    }

    public String getSecondaryLocation() {
        return mSecondaryLocation;
    }

    public void setSecondaryLocation(String secondaryLocation) {
        mSecondaryLocation = secondaryLocation;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public boolean isPrimary() {
        return mPrimary;
    }

    public void setPrimary(boolean primary) {
        mPrimary = primary;
    }
}
