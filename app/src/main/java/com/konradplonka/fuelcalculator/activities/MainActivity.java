package com.konradplonka.fuelcalculator.activities;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.tabs.TabLayout;
import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.fragments.CalculatorTab;
import com.konradplonka.fuelcalculator.fragments.DictionaryTab;
import com.konradplonka.fuelcalculator.fragments.dialogs.AddRecordDialog;
import com.konradplonka.fuelcalculator.fragments.dialogs.EditRecordDialog;
import com.konradplonka.fuelcalculator.fragments.dialogs.SettingsDialog;
import com.konradplonka.fuelcalculator.other.DatabaseHelper;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity implements EditRecordDialog.OnEditRecordDialogListener, SettingsDialog.OnSettingsDialogListener {
    DictionaryTab dictionaryTab;
    public boolean isDark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        isDark = getThemeStatePref();
        Log.e("DARK: ", String.valueOf(isDark));
        if(isDark){
            setTheme(R.style.DarkTheme);
        }
        else{
            setTheme(R.style.AppTheme);
        }

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            String backupPath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            try{
                DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
                databaseHelper.importDatabase(backupPath);
                dictionaryTab.loadDatabase();

            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }



}