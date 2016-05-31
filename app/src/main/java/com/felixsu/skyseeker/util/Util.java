package com.felixsu.skyseeker.util;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
    private final static ObjectMapper mapper = new ObjectMapper();

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
}