
package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Minutely {

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
        @JsonProperty("precipIntensity")
        public Integer precipIntensity;
        @JsonProperty("precipProbability")
        public Integer precipProbability;

    }

}
