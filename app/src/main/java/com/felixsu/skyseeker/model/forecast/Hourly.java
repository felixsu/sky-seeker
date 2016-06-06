
package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Hourly implements Serializable {

    @JsonProperty("summary")
    public String summary;
    @JsonProperty("icon")
    public String icon;
    @JsonProperty("data")
    public List<Data> data = new ArrayList<Data>();

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data implements Serializable {

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
        @JsonProperty("precipType")
        private String mPrecipType;

        public Integer getTime() {
            return time;
        }

        public void setTime(Integer time) {
            this.time = time;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Integer getPrecipIntensity() {
            return precipIntensity;
        }

        public void setPrecipIntensity(Integer precipIntensity) {
            this.precipIntensity = precipIntensity;
        }

        public Integer getPrecipProbability() {
            return precipProbability;
        }

        public void setPrecipProbability(Integer precipProbability) {
            this.precipProbability = precipProbability;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public Double getApparentTemperature() {
            return apparentTemperature;
        }

        public void setApparentTemperature(Double apparentTemperature) {
            this.apparentTemperature = apparentTemperature;
        }

        public Double getDewPoint() {
            return dewPoint;
        }

        public void setDewPoint(Double dewPoint) {
            this.dewPoint = dewPoint;
        }

        public Double getHumidity() {
            return humidity;
        }

        public void setHumidity(Double humidity) {
            this.humidity = humidity;
        }

        public Double getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(Double windSpeed) {
            this.windSpeed = windSpeed;
        }

        public Integer getWindBearing() {
            return windBearing;
        }

        public void setWindBearing(Integer windBearing) {
            this.windBearing = windBearing;
        }

        public Double getVisibility() {
            return visibility;
        }

        public void setVisibility(Double visibility) {
            this.visibility = visibility;
        }

        public Double getCloudCover() {
            return cloudCover;
        }

        public void setCloudCover(Double cloudCover) {
            this.cloudCover = cloudCover;
        }

        public Double getPressure() {
            return pressure;
        }

        public void setPressure(Double pressure) {
            this.pressure = pressure;
        }

        public Double getOzone() {
            return ozone;
        }

        public void setOzone(Double ozone) {
            this.ozone = ozone;
        }

        public String getPrecipType() {
            return mPrecipType;
        }

        public void setPrecipType(String precipType) {
            mPrecipType = precipType;
        }
    }
}
