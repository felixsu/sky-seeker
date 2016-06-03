package com.felixsu.skyseeker.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Alert implements Serializable {

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

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Integer getTime() {
        return mTime;
    }

    public void setTime(Integer time) {
        mTime = time;
    }

    public Integer getExpires() {
        return mExpires;
    }

    public void setExpires(Integer expires) {
        mExpires = expires;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getUri() {
        return mUri;
    }

    public void setUri(String uri) {
        mUri = uri;
    }
}
