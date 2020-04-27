package com.konradplonka.fuelcalculator.activities;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.fragments.CalculatorTab;
import com.konradplonka.fuelcalculator.fragments.DictionaryTab;
import com.konradplonka.fuelcalculator.fragments.SettingsTab;
import com.konradplonka.fuelcalculator.fragments.dialogs.EditRecordDialog;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements SettingsTab.OnSettingsTabListener, EditRecordDialog.OnEditRecordDialogListener {
    private CalculatorTab calculatorTab;
    private DictionaryTab dictionaryTab;
    private SettingsTab settingsTab;
    public boolean isDark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        calculatorTab = new CalculatorTab();
        dictionaryTab = new DictionaryTab();
        settingsTab = new SettingsTab();


        handleTheme();
        initializeAds();
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        ChipNavigationBar chipNavigationBar = findViewById(R.id.bottom_navigation_bar);
        chipNavigationBar.setOnItemSelectedListener(onItemSelectedListener);

        chipNavigationBar.setItemSelected(R.id.calculator,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, calculatorTab).commit();



    }

    private void handleTheme() {
        isDark = getThemeStatePref();
        Log.e("DARK: ", String.valueOf(isDark));
        if(isDark){
            setTheme(R.style.DarkTheme);
        }
        else{
            setTheme(R.style.AppTheme);
        }
    }

    private void initializeAds() {

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }


    private ChipNavigationBar.OnItemSelectedListener onItemSelectedListener = new ChipNavigationBar.OnItemSelectedListener() {
        @Override
        public void onItemSelected(int i) {
            Fragment selectedFragment = new CalculatorTab();

            switch (i){
                case R.id.calculator:
                    selectedFragment = calculatorTab;
                    break;
                case R.id.dictionary:
                    selectedFragment = dictionaryTab;
                    break;
                case R.id.settings:
                    selectedFragment = settingsTab;
                    break;


            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();

        }
    };


    @Override
    public void refreshItem(int position, String stationTag, int distance, double amountOfFuel, double totalCost, String date, String description) {
        dictionaryTab.updateItemData(position, stationTag, distance, amountOfFuel, totalCost, date, description);

    }



    @Override
    public void setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        saveThemeStatePref(true);
        restartApp();
    }

    @Override
    public void setLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        saveThemeStatePref(false);
        restartApp();
    }

    private void saveThemeStatePref(boolean isDark){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isDark", isDark);
        editor.commit();
    }
    public boolean getThemeStatePref(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        boolean isDark = sharedPreferences.getBoolean("isDark", false);

        return isDark;
    }

    private void restartApp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            Fragment importDialog = getSupportFragmentManager().findFragmentByTag("ImportDialog");
            importDialog.onActivityResult(requestCode, resultCode, data);
        }


    }



}