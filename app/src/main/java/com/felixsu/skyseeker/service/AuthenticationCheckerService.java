package com.felixsu.skyseeker.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.felixsu.skyseeker.constant.Constants;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by felixsoewito on 5/22/16.
 */
public class AuthenticationCheckerService extends IntentService {

    public static final String TAG = AuthenticationCheckerService.class.getName();

    public AuthenticationCheckerService() {
        super(AuthenticationCheckerService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "AuthenticationCheckerService start");

        final ResultReceiver receiver = intent.getParcelableExtra(TAG);
        final Bundle result = new Bundle();

        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            Thread.sleep(900);

            if (auth.getCurrentUser() == null) {
                receiver.send(Constants.RETURN_UNAUTHORIZED, result);
            } else {
                receiver.send(Constants.RETURN_OK, result);
            }

        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            Log.d(TAG, "AuthenticationCheckerService end");
        }
    }
}
