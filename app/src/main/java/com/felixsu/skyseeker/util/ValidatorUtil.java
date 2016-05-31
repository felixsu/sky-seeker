package com.felixsu.skyseeker.util;

/**
 * Created by felixsoewito on 5/22/16.
 */
public class ValidatorUtil {
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
