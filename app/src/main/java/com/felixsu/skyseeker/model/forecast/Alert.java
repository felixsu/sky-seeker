package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Alert {

    @JsonProperty("title")
    private String mTitle;
    @JsonProperty("time")
    private Integer mTime;
    @JsonProperty("expires")
    private Integer mExpires;
    @JsonProperty("description")
    private String mDescription;
    @JsonProperty("uri")
    private String mUri;

}
