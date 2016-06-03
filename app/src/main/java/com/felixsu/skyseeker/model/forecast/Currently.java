
package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Currently implements Serializable {

    @JsonProperty("time")
    private Integer mTime;
    @JsonProperty("summary")
    private String mSummary;
    @JsonProperty("icon")
    private String mIcon;
    @JsonProperty("nearestStormDistance")
    private Integer mNearestStormDistance;
    @JsonProperty("nearestStormBearing")
    private Integer mNearestStormBearing;
    @JsonProperty("precipIntensity")
    private Integer mPrecipIntensity;
    @JsonProperty("precipProbability")
    private Integer mPrecipProbability;
    @JsonProperty("temperature")
    private Double mTemperature;
    @JsonProperty("apparentTemperature")
    private Double mApparentTemperature;
    @JsonProperty("dewPoint")
    private Double mDewPoint;
    @JsonProperty("humidity")
    private Double mHumidity;
    @JsonProperty("windSpeed")
    private Double mWindSpeed;
    @JsonProperty("windBearing")
    private Integer mWindBearing;
    @JsonProperty("visibility")
    private Double mVisibility;
    @JsonProperty("cloudCover")
    private Double mCloudCover;
    @JsonProperty("pressure")
    private Double mPressure;
    @JsonProperty("ozone")
    private Double mOzone;

    public Integer getTime() {
        return mTime;
    }

    public void setTime(Integer time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public Integer getNearestStormDistance() {
        return mNearestStormDistance;
    }

    public void setNearestStormDistance(Integer nearestStormDistance) {
        mNearestStormDistance = nearestStormDistance;
    }

    public Integer getNearestStormBearing() {
        return mNearestStormBearing;
    }

    public void setNearestStormBearing(Integer nearestStormBearing) {
        mNearestStormBearing = nearestStormBearing;
    }

    public Integer getPrecipIntensity() {
        return mPrecipIntensity;
    }

    public void setPrecipIntensity(Integer precipIntensity) {
        mPrecipIntensity = precipIntensity;
    }

    public Integer getPrecipProbability() {
        return mPrecipProbability;
    }

    public void setPrecipProbability(Integer precipProbability) {
        mPrecipProbability = precipProbability;
    }

    public Double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(Double temperature) {
        mTemperature = temperature;
    }

    public Double getApparentTemperature() {
        return mApparentTemperature;
    }

    public void setApparentTemperature(Double apparentTemperature) {
        mApparentTemperature = apparentTemperature;
    }

    public Double getDewPoint() {
        return mDewPoint;
    }

    public void setDewPoint(Double dewPoint) {
        mDewPoint = dewPoint;
    }

    public Double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(Double humidity) {
        mHumidity = humidity;
    }

    public Double getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        mWindSpeed = windSpeed;
    }

    public Integer getWindBearing() {
        return mWindBearing;
    }

    public void setWindBearing(Integer windBearing) {
        mWindBearing = windBearing;
    }

    public Double getVisibility() {
        return mVisibility;
    }

    public void setVisibility(Double visibility) {
        mVisibility = visibility;
    }

    public Double getCloudCover() {
        return mCloudCover;
    }

    public void setCloudCover(Double cloudCover) {
        mCloudCover = cloudCover;
    }

    public Double getPressure() {
        return mPressure;
    }

    public void setPressure(Double pressure) {
        mPressure = pressure;
    }

    public Double getOzone() {
        return mOzone;
    }

    public void setOzone(Double ozone) {
        mOzone = ozone;
    }
}
