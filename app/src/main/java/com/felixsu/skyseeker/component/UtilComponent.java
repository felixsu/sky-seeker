package com.felixsu.skyseeker.component;

import com.felixsu.skyseeker.provider.AppModule;
import com.felixsu.skyseeker.provider.UtilModule;
import com.felixsu.skyseeker.ui.MainActivity;
import com.felixsu.skyseeker.ui.fragment.ForecastFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by felixsu on 03/06/2016.
 */

@Singleton
@Component(modules = {AppModule.class, UtilModule.class})
public interface UtilComponent {

    void inject(ForecastFragment forecastFragment);
    void inject(MainActivity mainActivity);

}
