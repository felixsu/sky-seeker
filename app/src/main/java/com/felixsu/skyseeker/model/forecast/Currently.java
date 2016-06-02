
package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Currently {

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

}
