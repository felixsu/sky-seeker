
package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Minutely implements Serializable {

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
    public static class Data implements Serializable {

        @JsonProperty("time")
        public Integer time;
        @JsonProperty("precipIntensity")
        public Integer precipIntensity;
        @JsonProperty("precipProbability")
        public Integer precipProbability;

        public Integer getTime() {
            return time;
        }

        public void setTime(Integer time) {
            this.time = time;
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
    }

}
