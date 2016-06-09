package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by felixsu on 01/06/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Forecast implements Serializable {

    @JsonProperty("latitude")
    public Double latitude;
    @JsonProperty("longitude")
    public Double longitude;
    @JsonProperty("timezone")
    public String timezone;
    @JsonProperty("offset")
    public Integer offset;
    @JsonProperty("currently")
    public Currently currently;
    @JsonProperty("minutely")
    public Minutely minutely;
    @JsonProperty("hourly")
    public Hourly hourly;
    @JsonProperty("daily")
    public Daily daily;
    @JsonProperty("alerts")
    public List<Alert> alerts = new ArrayList<Alert>();
    @JsonProperty("flags")
    public Flags flags;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Currently getCurrently() {
        return currently;
    }

    public void setCurrently(Currently currently) {
        this.currently = currently;
    }

    public Minutely getMinutely() {
        return minutely;
    }

    public void setMinutely(Minutely minutely) {
        this.minutely = minutely;
    }

    public Hourly getHourly() {
        return hourly;
    }

    public void setHourly(Hourly hourly) {
        this.hourly = hourly;
    }

    public Daily getDaily() {
        return daily;
    }

    public void setDaily(Daily daily) {
        this.daily = daily;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }

}