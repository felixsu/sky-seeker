package com.felixsu.skyseeker.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.felixsu.skyseeker.R;
import com.felixsu.skyseeker.constant.Constants;
import com.felixsu.skyseeker.constant.LogConstants;
import com.felixsu.skyseeker.receiver.AuthenticationCheckerReceiver;
import com.felixsu.skyseeker.receiver.Receiver;
import com.felixsu.skyseeker.service.AuthenticationCheckerService;
import com.felixsu.skyseeker.util.ViewUtil;

public class SplashScreenActivity extends Activity implements Receiver {

    public static final String TAG = SplashScreenActivity.class.getName();

    private ImageView mSunnyImage;

    private AuthenticationCheckerReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, LogConstants.ON_CREATE_ENTER);
        initView();
        decorateView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, LogConstants.ON_START_ENTER);
        attachReceiver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, LogConstants.ON_RESUME_ENTER);
        authenticate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, LogConstants.ON_PAUSE_ENTER);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, LogConstants.ON_STOP_ENTER);
        deAttachReceiver();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Log.d(TAG, String.valueOf(resultCode));
        switch (resultCode) {
            case Constants.RETURN_OK:
                startMainActivity();
                break;
            case Constants.RETURN_UNAUTHORIZED:
                startLoginActivity();
                break;
            default:
                startLoginActivity();
        }
    }

    private void authenticate() {
        Log.d(TAG, "entering authenticate");
        Intent intent = new Intent(this, AuthenticationCheckerService.class);
        intent.putExtra(AuthenticationCheckerService.TAG, mReceiver);
        startService(intent);
    }

    private void attachReceiver() {
        mReceiver = new AuthenticationCheckerReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    private void deAttachReceiver() {
        mReceiver = null;
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    private void initView() {
        setContentView(R.layout.activity_splash_screen);
        mSunnyImage = (ImageView) findViewById(R.id.image_sunny);
    }

    private void decorateView() {
        Log.d(TAG, "decorate view");
        Drawable sunnyLogo = getDrawable(R.drawable.ic_sunny);
        ViewUtil.tintDrawable(sunnyLogo, getResources().getColor(R.color.colorWhite));
        mSunnyImage.setImageDrawable(sunnyLogo);
    }
}
