package com.felixsu.skyseeker.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.felixsu.skyseeker.constant.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by felixsu on 04/06/2016.
 */
public class GeoCoderService extends IntentService {

    public static final String TAG = GeoCoderService.class.getName();

    public static final String EXTRA_UUID = "geo-coder-service-uuid";
    public static final String EXTRA_LATITUDE = "geo-coder-service-latitude";
    public static final String EXTRA_LONGITUDE = "geo-coder-service-longitude";

    public static final String RESULT_UUID = "geo-coder-service-result-uuid";
    public static final String RESULT_ADDRESSES = "geo-coder-service-result-addresses";

    public GeoCoderService() {
        super(GeoCoderService.TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "GeoCoderService start");

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        final ResultReceiver receiver = intent.getParcelableExtra(TAG);
        final double latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 0.0);
        final double longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0);
        final String uuid = intent.getStringExtra(EXTRA_UUID);
        List<Address> addresses;

        final Bundle result = new Bundle();
        result.putString(RESULT_UUID, uuid);

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 10);

            if (addresses == null || addresses.isEmpty()){
                receiver.send(Constants.RETURN_NOT_FOUND, result);
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressCollection = new ArrayList<>();

                for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
                    addressCollection.add(address.getAddressLine(i));
                }
                result.putStringArrayList(RESULT_ADDRESSES, addressCollection);
                receiver.send(Constants.RETURN_OK, result);
            }

        } catch (IOException e) {
            Log.e(TAG, "service not available", e);
            receiver.send(Constants.RETURN_ERROR, result);
        } catch (IllegalArgumentException e){
            Log.e(TAG, "bad input format", e);
            receiver.send(Constants.RETURN_ERROR, result);
        } catch (Exception e){
            receiver.send(Constants.RETURN_ERROR, result);
        }


    }

}
