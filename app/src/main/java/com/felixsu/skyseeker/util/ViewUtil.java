package com.felixsu.skyseeker.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

/**
 * Created by felixsoewito on 5/22/16.
 */
public class ViewUtil {

    public static void tintDrawable(@NonNull Drawable d, int color) {
        d.setTint(color);
    }

    public static void hideView(View v) {
        v.setVisibility(View.INVISIBLE);
    }

    public static void showView(View v) {
        v.setVisibility(View.VISIBLE);
    }

    public static void showTextView(TextView v, String text) {
        if (v.getVisibility() != View.VISIBLE) {
            v.setVisibility(View.VISIBLE);
        }
        v.setText(text);
    }

    public static void disableOnClickListener(View view) {
        view.setOnClickListener(null);
    }

    public static void enableOnClickListener(View view, View.OnClickListener onClickListener) {
        view.setOnClickListener(onClickListener);
    }
}
