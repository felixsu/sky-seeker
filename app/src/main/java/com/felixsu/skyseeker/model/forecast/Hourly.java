
package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Hourly {

    @JsonProperty("summary")
    public String summary;
    @JsonProperty("icon")
    public String icon;
    @JsonProperty("data")
    public List<Data> data = new ArrayList<Data>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Data {

        @JsonProperty("time")
        public Integer time;
        @JsonProperty("summary")
        public String summary;
        @JsonProperty("icon")
        public String icon;
        @JsonProperty("precipIntensity")
        public Integer precipIntensity;
        @JsonProperty("precipProbability")
        public Integer precipProbability;
        @JsonProperty("temperature")
        public Double temperature;
        @JsonProperty("apparentTemperature")
        public Double apparentTemperature;
        @JsonProperty("dewPoint")
        public Double dewPoint;
        @JsonProperty("humidity")
        public Double humidity;
        @JsonProperty("windSpeed")
        public Double windSpeed;
        @JsonProperty("windBearing")
        public Integer windBearing;
        @JsonProperty("visibility")
        public Double visibility;
        @JsonProperty("cloudCover")
        public Double cloudCover;
        @JsonProperty("pressure")
        public Double pressure;
        @JsonProperty("ozone")
        public Double ozone;

    }
}
