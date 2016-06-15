package com.felixsu.skyseeker.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.felixsu.skyseeker.R;
import com.felixsu.skyseeker.listener.CityPickerDialogListener;

/**
 * Created by felixsu on 15/06/2016.
 */
public class CityPickerDialog extends DialogFragment {

    private CityPickerDialogListener mDialogListener;
    private Location mLocation;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_city_picker, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Enter location name")
                .setView(view)
                .setPositiveButton("OK", mPositiveListener)
                .setNegativeButton("Cancel", mNegativeListener);

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mDialogListener = (CityPickerDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CityPickerDialogListener");
        }
    }

    private DialogInterface.OnClickListener mPositiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mDialogListener != null) {
                mDialogListener.onDialogPositiveClick(CityPickerDialog.this);
            }
            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener mNegativeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mDialogListener != null) {
                mDialogListener.onDialogNegativeClick(CityPickerDialog.this);
            }
            dialog.cancel();
        }
    };
}
