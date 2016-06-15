package com.felixsu.skyseeker.listener;

import android.support.v4.app.DialogFragment;

/**
 * Created by felixsu on 15/06/2016.
 */
public interface CityPickerDialogListener {
    void onDialogPositiveClick(DialogFragment fragment);
    void onDialogNegativeClick(DialogFragment fragment);

}
