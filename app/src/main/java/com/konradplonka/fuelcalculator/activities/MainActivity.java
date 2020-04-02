package com.konradplonka.fuelcalculator.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;

import com.google.android.material.tabs.TabLayout;
import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.fragments.AddRecordDialog;
import com.konradplonka.fuelcalculator.fragments.CalculatorTab;
import com.konradplonka.fuelcalculator.fragments.DatePickerFragment;
import com.konradplonka.fuelcalculator.fragments.DictionaryTab;
import com.konradplonka.fuelcalculator.fragments.EditRecordDialog;
import com.konradplonka.fuelcalculator.fragments.SettingsDialog;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements EditRecordDialog.OnEditRecordDialogListener, SettingsDialog.OnSettingsDialogListener {
    DictionaryTab dictionaryTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        }
        else setTheme(R.style.AppTheme_NoActionBar);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.getTabAt(0).setIcon(R.drawable.calculator_tab_icon_24dp);
        tabs.getTabAt(1).setIcon(R.drawable.dictionary_tab_icon_24dp);



    }

    @Override
    public void refreshItem(int position, String stationTag, int distance, double amountOfFuel, double totalCost, String date, String description) {
        dictionaryTab.updateItemData(position, stationTag, distance, amountOfFuel, totalCost, date, description);
    }

    @Override
    public void setNightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        restartApp();
    }

    private void restartApp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    CalculatorTab calculatorTab = new CalculatorTab();
                    return calculatorTab;
                case 1:
                    dictionaryTab = new DictionaryTab();
                    return dictionaryTab;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }


    }

}