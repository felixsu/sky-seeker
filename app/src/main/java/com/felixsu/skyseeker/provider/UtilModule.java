package com.felixsu.skyseeker.provider;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixsu.skyseeker.service.ForecastService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
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
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 1 * 1024 * 1024; // 1 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(cache);

        return builder.build();
    }

    @Provides
    @Singleton
    ForecastService provideForecastService(OkHttpClient client){
        ForecastService service = new ForecastService(client);
        return service;
    }
}
