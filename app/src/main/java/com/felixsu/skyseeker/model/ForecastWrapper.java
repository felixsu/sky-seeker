package com.felixsu.skyseeker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.felixsu.skyseeker.model.forecast.Forecast;

import java.io.Serializable;

/**
 * Created by felixsoewito on 6/9/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastWrapper implements Serializable {

    private String mUuid;
    private int mViewId;
    private String mName;
    private Forecast mForecast;
    private String mPrimaryLocation;
    private String mSecondaryLocation;
    private String mSubAdministrativeLocation;
    private String mAdministrativeLocation;
    private String mCountry;
    private double mLatitude;
    private double mLongitude;
    private boolean mPrimary;

    public ForecastWrapper(String uuid, String name, Forecast forecast, String primaryLocation, String secondaryLocation, String subAdministrativeLocation, String administrativeLocationName, String country, double latitude, double longitude, boolean primary) {
        mUuid = uuid;
        mName = name;
        mForecast = forecast;
        mPrimaryLocation = primaryLocation;
        mSecondaryLocation = secondaryLocation;
        mSubAdministrativeLocation = subAdministrativeLocation;
        mAdministrativeLocation = administrativeLocationName;
        mCountry = country;
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

    public int getViewId() {
        return mViewId;
    }

    public void setViewId(int viewId) {
        mViewId = viewId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
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

    public String getSubAdministrativeLocation() {
        return mSubAdministrativeLocation;
    }

    public void setSubAdministrativeLocation(String subAdministrativeLocation) {
        mSubAdministrativeLocation = subAdministrativeLocation;
    }

    public String getAdministrativeLocation() {
        return mAdministrativeLocation;
    }

    public void setAdministrativeLocation(String administrativeLocation) {
        mAdministrativeLocation = administrativeLocation;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
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
