package com.felixsu.skyseeker;

import android.app.Application;

import com.felixsu.skyseeker.component.DaggerUtilComponent;
import com.felixsu.skyseeker.component.UtilComponent;
import com.felixsu.skyseeker.provider.AppModule;
import com.felixsu.skyseeker.provider.UtilModule;

/**
 * Created by felixsu on 03/06/2016.
 */
public class SkySeekerApp extends Application {

    private UtilComponent mUtilComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mUtilComponent = DaggerUtilComponent.builder()
                .appModule(new AppModule(this))
                .utilModule(new UtilModule())
                .build();
    }

    public UtilComponent getUtilComponent() {
        return mUtilComponent;
    }
}
