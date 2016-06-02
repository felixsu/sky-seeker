
package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Daily {

    @JsonProperty("summary")
    private String mSummary;
    @JsonProperty("icon")
    private String mIcon;
    @JsonProperty("data")
    private List<Data> mData = new ArrayList<Data>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Data {

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

    }
}
