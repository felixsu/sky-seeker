package com.felixsu.skyseeker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felixsoewito on 6/9/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalData {
    private List<ForecastWrapper> mForecastWrapperList = new ArrayList<>();
    private String mCurrentActiveUuid;

    public GlobalData() {
    }

    public List<ForecastWrapper> getForecastWrapperList() {
        return mForecastWrapperList;
    }

    public void setForecastWrapperList(List<ForecastWrapper> forecastWrapperList) {
        mForecastWrapperList = forecastWrapperList;
    }

    public String getCurrentActiveUuid() {
        return mCurrentActiveUuid;
    }

    public void setCurrentActiveUuid(String currentActiveUuid) {
        mCurrentActiveUuid = currentActiveUuid;
    }
}
