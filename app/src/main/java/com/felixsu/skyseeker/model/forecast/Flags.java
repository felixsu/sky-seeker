package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Flags {

    @JsonProperty("sources")
    public List<String> sources = new ArrayList<String>();
    @JsonProperty("darksky-stations")
    public List<String> darkskyStations = new ArrayList<String>();
    @JsonProperty("lamp-stations")
    public List<String> lampStations = new ArrayList<String>();
    @JsonProperty("isd-stations")
    public List<String> isdStations = new ArrayList<String>();
    @JsonProperty("madis-stations")
    public List<String> madisStations = new ArrayList<String>();
    @JsonProperty("units")
    public String units;

}