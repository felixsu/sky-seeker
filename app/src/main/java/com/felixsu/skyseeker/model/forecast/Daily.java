
package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Daily implements Serializable {

    @JsonProperty("summary")
    private String mSummary;
    @JsonProperty("icon")
    private String mIcon;
    @JsonProperty("data")
    private List<Data> mData = new ArrayList<Data>();

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

    public List<Data> getData() {
        return mData;
    }

    public void setData(List<Data> data) {
        mData = data;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data implements Serializable {

        @JsonProperty("time")
        private Integer mTime;
        @JsonProperty("summary")
        private String mSummary;
        @JsonProperty("icon")
        private String mIcon;
        @JsonProperty("sunriseTime")
        private Integer mSunriseTime;
        @JsonProperty("sunsetTime")
        private Integer mSunsetTime;
        @JsonProperty("moonPhase")
        private Double mMoonPhase;
        @JsonProperty("precipIntensity")
        private Integer mPrecipIntensity;
        @JsonProperty("precipIntensityMax")
        private Integer mPrecipIntensityMax;
        @JsonProperty("precipProbability")
        private Integer mPrecipProbability;
        @JsonProperty("temperatureMin")
        private Double mTemperatureMin;
        @JsonProperty("temperatureMinTime")
        private Integer mTemperatureMinTime;
        @JsonProperty("temperatureMax")
        private Double mTemperatureMax;
        @JsonProperty("temperatureMaxTime")
        private Integer mTemperatureMaxTime;
        @JsonProperty("apparentTemperatureMin")
        private Double mApparentTemperatureMin;
        @JsonProperty("apparentTemperatureMinTime")
        private Integer mApparentTemperatureMinTime;
        @JsonProperty("apparentTemperatureMax")
        private Double mApparentTemperatureMax;
        @JsonProperty("apparentTemperatureMaxTime")
        private Integer mApparentTemperatureMaxTime;
        @JsonProperty("dewPoint")
        private Double mDewPoint;
        @JsonProperty("humidity")
        private Double mHumidity;
        @JsonProperty("windSpeed")
        private Double mWindSpeed;
        @JsonProperty("windBearing")
        private Integer mWindBearing;
        @JsonProperty("visibility")
        private Integer mVisibility;
        @JsonProperty("cloudCover")
        private Double mCloudCover;
        @JsonProperty("pressure")
        private Double mPressure;
        @JsonProperty("ozone")
        private Double mOzone;
        @JsonProperty("precipIntensityMaxTime")
        private Integer mPrecipIntensityMaxTime;
        @JsonProperty("precipType")
        private String mPrecipType;

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

        public Integer getSunriseTime() {
            return mSunriseTime;
        }

        public void setSunriseTime(Integer sunriseTime) {
            mSunriseTime = sunriseTime;
        }

        public Integer getSunsetTime() {
            return mSunsetTime;
        }

        public void setSunsetTime(Integer sunsetTime) {
            mSunsetTime = sunsetTime;
        }

        public Double getMoonPhase() {
            return mMoonPhase;
        }

        public void setMoonPhase(Double moonPhase) {
            mMoonPhase = moonPhase;
        }

        public Integer getPrecipIntensity() {
            return mPrecipIntensity;
        }

        public void setPrecipIntensity(Integer precipIntensity) {
            mPrecipIntensity = precipIntensity;
        }

        public Integer getPrecipIntensityMax() {
            return mPrecipIntensityMax;
        }

        public void setPrecipIntensityMax(Integer precipIntensityMax) {
            mPrecipIntensityMax = precipIntensityMax;
        }

        public Integer getPrecipProbability() {
            return mPrecipProbability;
        }

        public void setPrecipProbability(Integer precipProbability) {
            mPrecipProbability = precipProbability;
        }

        public Double getTemperatureMin() {
            return mTemperatureMin;
        }

        public void setTemperatureMin(Double temperatureMin) {
            mTemperatureMin = temperatureMin;
        }

        public Integer getTemperatureMinTime() {
            return mTemperatureMinTime;
        }

        public void setTemperatureMinTime(Integer temperatureMinTime) {
            mTemperatureMinTime = temperatureMinTime;
        }

        public Double getTemperatureMax() {
            return mTemperatureMax;
        }

        public void setTemperatureMax(Double temperatureMax) {
            mTemperatureMax = temperatureMax;
        }

        public Integer getTemperatureMaxTime() {
            return mTemperatureMaxTime;
        }

        public void setTemperatureMaxTime(Integer temperatureMaxTime) {
            mTemperatureMaxTime = temperatureMaxTime;
        }

        public Double getApparentTemperatureMin() {
            return mApparentTemperatureMin;
        }

        public void setApparentTemperatureMin(Double apparentTemperatureMin) {
            mApparentTemperatureMin = apparentTemperatureMin;
        }

        public Integer getApparentTemperatureMinTime() {
            return mApparentTemperatureMinTime;
        }

        public void setApparentTemperatureMinTime(Integer apparentTemperatureMinTime) {
            mApparentTemperatureMinTime = apparentTemperatureMinTime;
        }

        public Double getApparentTemperatureMax() {
            return mApparentTemperatureMax;
        }

        public void setApparentTemperatureMax(Double apparentTemperatureMax) {
            mApparentTemperatureMax = apparentTemperatureMax;
        }

        public Integer getApparentTemperatureMaxTime() {
            return mApparentTemperatureMaxTime;
        }

        public void setApparentTemperatureMaxTime(Integer apparentTemperatureMaxTime) {
            mApparentTemperatureMaxTime = apparentTemperatureMaxTime;
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

        public Integer getVisibility() {
            return mVisibility;
        }

        public void setVisibility(Integer visibility) {
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

        public Integer getPrecipIntensityMaxTime() {
            return mPrecipIntensityMaxTime;
        }

        public void setPrecipIntensityMaxTime(Integer precipIntensityMaxTime) {
            mPrecipIntensityMaxTime = precipIntensityMaxTime;
        }

        public String getPrecipType() {
            return mPrecipType;
        }

        public void setPrecipType(String precipType) {
            mPrecipType = precipType;
        }
    }
}
