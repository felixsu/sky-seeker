package com.felixsu.skyseeker.provider;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by felixsu on 02/06/2016.
 */

@Module
public class GeneralModule {

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
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
}