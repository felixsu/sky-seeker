package com.felixsu.skyseeker.model.request;

/**
 * Created by felixsu on 01/06/2016.
 */
public class ForecastRequest {

    public static final String SI_UNIT = "si";
    public static final String US_UNIT = "us";

    private double mLatitude;
    private double mLongitude;
    private String mUnit;

    public ForecastRequest(double latitude, double longitude, String unit) {
        mLatitude = latitude;
        mLongitude = longitude;
        mUnit = unit;
    }

    public ForecastRequest() {
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

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String unit) {
        mUnit = unit;
    }
}
