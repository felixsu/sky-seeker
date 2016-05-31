package com.felixsu.skyseeker.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class AuthenticationCheckerReceiver extends ResultReceiver {

    private Receiver mReceiver;

    public AuthenticationCheckerReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}