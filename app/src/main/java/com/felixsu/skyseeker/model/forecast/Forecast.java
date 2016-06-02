package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felixsu on 01/06/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Forecast {

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

}