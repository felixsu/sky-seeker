package com.felixsu.skyseeker.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.felixsu.skyseeker.BuildConfig;

public class Util {

    public static String base64Encode(byte[] input, int len) {
        return Base64.encodeToString(input, 0, len, Base64.CRLF);
    }

    public static byte[] base64Decode(String input) {
        return Base64.decode(input, Base64.CRLF);
    }


    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static int percentValue(double value) {
        return (int) (value * 100);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        //String phrase = "";
        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                //phrase += Character.toUpperCase(c);
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            //phrase += c;
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public static String getAndroidVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }
}