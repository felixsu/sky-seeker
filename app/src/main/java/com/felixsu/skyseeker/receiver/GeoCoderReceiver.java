package com.felixsu.skyseeker.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by felixsoewito on 6/9/16.
 */
public class GeoCoderReceiver extends ResultReceiver {

    private Receiver mReceiver;

    public GeoCoderReceiver(Handler handler) {
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
