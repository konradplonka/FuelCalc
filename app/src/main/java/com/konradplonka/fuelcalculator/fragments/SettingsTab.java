package com.konradplonka.fuelcalculator.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.fragments.dialogs.AboutApplicationDialog;
import com.konradplonka.fuelcalculator.fragments.dialogs.ExportDialog;
import com.konradplonka.fuelcalculator.fragments.dialogs.ImportDialog;
import com.konradplonka.fuelcalculator.other.DatabaseHelper;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class SettingsTab extends Fragment {
    OnSettingsTabListener listener;
    private Switch themeSwitch;
    private Switch fuelConsumptionSwitch;
    private Button importButton;
    private Button exportButton;
    private Button aboutAppButton;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_tab, container, false);


        initializeViewElements(view);
        handleThemeSwitch();
        handleFuelConsumptionSwitch();

        onImportButtonClick();
        onExportButtonClick();
        onAboutAppButtonClick();
        return view;
    }

    private void onAboutAppButtonClick() {
        aboutAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutApplicationDialog aboutApplicationDialog = new AboutApplicationDialog();
                aboutApplicationDialog.show(getFragmentManager(), "AboutApplicationDialog");
            }
        });
    }


    private void onExportButtonClick() {
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExportDialog exportDialog = new ExportDialog();
                exportDialog.show(getFragmentManager(), "ExportDialog");
            }
        });

    }

    private void onImportButtonClick() {
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportDialog importDialog = new ImportDialog();
                importDialog.show(getFragmentManager(), "ImportDialog");
            }
        });
    }

    private void handleThemeSwitch() {
        themeSwitch.setChecked(getThemeStatePref());
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    listener.setDarkMode();
                }
                else listener.setLightMode();
            }
        });
    }
    private void handleFuelConsumptionSwitch() {
        fuelConsumptionSwitch.setChecked(getFuelConsumptionCalcStatePref());
        fuelConsumptionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    saveFuelConsumptionStatePref(true);
                }
                else{
                    saveFuelConsumptionStatePref(false);
                }
            }
        });

    }

    private void initializeViewElements(View view) {
        themeSwitch = view.findViewById(R.id.themeSwitch);
        fuelConsumptionSwitch = view.findViewById(R.id.fuel_consumption_switch);
        importButton = view.findViewById(R.id.import_button);
        exportButton = view.findViewById(R.id.export_button);
        aboutAppButton = view.findViewById(R.id.about_app_button);
        
    }

    public boolean getThemeStatePref(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPref", MODE_PRIVATE);
        boolean isDark = sharedPreferences.getBoolean("isDark", false);

        return isDark;
    }
    public boolean getFuelConsumptionCalcStatePref(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPref", MODE_PRIVATE);
        boolean isDark = sharedPreferences.getBoolean("fuelConsumptionCalc", true);

        return isDark;
    }
    private void saveFuelConsumptionStatePref(boolean fuelConsumptionCalc){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("fuelConsumptionCalc", fuelConsumptionCalc);
        editor.commit();
    }


    public interface OnSettingsTabListener{
        void setDarkMode();
        void setLightMode();

    }

    @Override
    public void onAttach(Context context) {
        try{
            listener = (OnSettingsTabListener) context;
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " +e.getMessage());
        }

        super.onAttach(context);
    }
}
