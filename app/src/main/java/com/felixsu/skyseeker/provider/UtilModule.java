package com.felixsu.skyseeker.provider;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixsu.skyseeker.service.ForecastService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by felixsu on 03/06/2016.
 */

@Module
public class UtilModule {

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreference(Application application){
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    public ObjectMapper provideObjectMapper(){
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.build();
    }

    @Provides
    @Singleton
    ForecastService provideForecastService(OkHttpClient client){
        ForecastService service = new ForecastService(client);
        return service;
    }
}
