package com.konradplonka.fuelcalculator.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import com.konradplonka.fuelcalculator.R;

import static android.content.ContentValues.TAG;

public class SettingsDialog extends DialogFragment{
    OnSettingsDialogListener listener;
    Switch themeSwitch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_settings, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        themeSwitch = (Switch)view.findViewById(R.id.themeSwitch);

        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    listener.setNightMode();

                }
                else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        return view;
    }
    public interface OnSettingsDialogListener{
        void setNightMode();
    }

    @Override
    public void onAttach(Context context) {
        try{
            listener = (OnSettingsDialogListener) context;
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " +e.getMessage());
        }

        super.onAttach(context);
    }
}
