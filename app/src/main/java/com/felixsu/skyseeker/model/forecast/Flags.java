package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Flags implements Serializable {

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

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getDarkskyStations() {
        return darkskyStations;
    }

    public void setDarkskyStations(List<String> darkskyStations) {
        this.darkskyStations = darkskyStations;
    }

    public List<String> getLampStations() {
        return lampStations;
    }

    public void setLampStations(List<String> lampStations) {
        this.lampStations = lampStations;
    }

    public List<String> getIsdStations() {
        return isdStations;
    }

    public void setIsdStations(List<String> isdStations) {
        this.isdStations = isdStations;
    }

    public List<String> getMadisStations() {
        return madisStations;
    }

    public void setMadisStations(List<String> madisStations) {
        this.madisStations = madisStations;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}