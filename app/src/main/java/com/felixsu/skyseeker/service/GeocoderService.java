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
public class GeocoderService extends IntentService {

    public static final String TAG = GeocoderService.class.getName();

    public static final int REQUEST_WITH_LONG_LAT = 2000;
    public static final int REQUEST_WITH_NAME = 2001;

    public static final String EXTRA_UUID = "geo-coder-service-uuid";
    public static final String EXTRA_REQUEST_CODE = "geo-coder-request-code";
    public static final String EXTRA_LATITUDE = "geo-coder-service-latitude";
    public static final String EXTRA_LONGITUDE = "geo-coder-service-longitude";
    public static final String EXTRA_LOCATION_NAME = "geo-coder-service-name";

    public static final String RESULT_UUID = "geo-coder-service-result-uuid";
    public static final String RESULT_ADDRESSES = "geo-coder-service-result-addresses";
    public static final String RESULT_SUB_ADMINISTRATIVE_NAME = "geo-coder-service-administration-name";
    public static final String RESULT_ADMINISTRATIVE_NAME = "geo-coder-service-administration-name";
    public static final String RESULT_COUNTRY_CODE = "geo-coder-service-result-country-code";

    public GeocoderService() {
        super(GeocoderService.TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "GeoCoderService start");

        final ResultReceiver receiver = intent.getParcelableExtra(TAG);
        final int requestCode = intent.getIntExtra(EXTRA_REQUEST_CODE, 0);
        final String uuid = intent.getStringExtra(EXTRA_UUID);
        final Bundle result = new Bundle();
        result.putString(RESULT_UUID, uuid);

        try {

            List<Address> addresses;
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            switch (requestCode) {
                case REQUEST_WITH_LONG_LAT:
                    addresses = startLongLatService(intent, geocoder);
                    break;
                case REQUEST_WITH_NAME:
                    addresses = startLocationNameService(intent, geocoder);
                    break;
                default:
                    throw new RuntimeException("Request not supported: " + requestCode);
            }

            if (addresses == null || addresses.isEmpty()){
                receiver.send(Constants.RETURN_NOT_FOUND, result);
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressCollection = new ArrayList<>();

                for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
                    addressCollection.add(address.getAddressLine(i));
                }
                result.putStringArrayList(RESULT_ADDRESSES, addressCollection);
                result.putString(RESULT_ADMINISTRATIVE_NAME, address.getAdminArea());
                result.putString(RESULT_SUB_ADMINISTRATIVE_NAME, address.getSubAdminArea());
                result.putString(RESULT_COUNTRY_CODE, address.getCountryCode());
                receiver.send(Constants.RETURN_OK, result);
            }
        } catch (IOException e) {
            Log.e(TAG, "service not available", e);
            receiver.send(Constants.RETURN_ERROR, result);
        } catch (IllegalArgumentException e){
            Log.e(TAG, "bad input format", e);
            receiver.send(Constants.RETURN_ERROR, result);
        } catch (Exception e){
            Log.e(TAG, "unexpected exception", e);
            receiver.send(Constants.RETURN_ERROR, result);
        }


    }

    private List<Address> startLongLatService(Intent intent, Geocoder geocoder) throws Exception {
        final double latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 0.0);
        final double longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0);

        return geocoder.getFromLocation(latitude, longitude, 5);


    }

    private List<Address> startLocationNameService(Intent intent, Geocoder geocoder) throws Exception {
        final String locationName = intent.getStringExtra(EXTRA_LOCATION_NAME);

        return geocoder.getFromLocationName(locationName, 5);
    }

}
