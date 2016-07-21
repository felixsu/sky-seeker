package com.felixsu.skyseeker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by felixsoewito on 7/21/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    public static final String FIREBASE_USER_REFERENCE = "user";
    public static final String BUNDLE_NAME = "bundle-user";
    public static final String KEY_NAME = "user-name";
    public static final String KEY_EMAIL = "user-email";
    public static final String KEY_PICTURE_URL = "user-picture-url";

    private String mName;
    private String mEmail;
    private String mPictureUrl;
    private String mLocation;
    private String mCountry;
    private Long mJoinDate;
    private String mAppVersion;
    private String mAndroidOs;
    private String mPhoneModel;

    public User() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        mPictureUrl = pictureUrl;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public Long getJoinDate() {
        return mJoinDate;
    }

    public void setJoinDate(Long joinDate) {
        mJoinDate = joinDate;
    }

    public String getAppVersion() {
        return mAppVersion;
    }

    public void setAppVersion(String appVersion) {
        mAppVersion = appVersion;
    }

    public String getAndroidOs() {
        return mAndroidOs;
    }

    public void setAndroidOs(String androidOs) {
        mAndroidOs = androidOs;
    }

    public String getPhoneModel() {
        return mPhoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        mPhoneModel = phoneModel;
    }
}
