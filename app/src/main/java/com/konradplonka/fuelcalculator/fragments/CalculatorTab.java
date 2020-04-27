package com.konradplonka.fuelcalculator.fragments;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;
import com.konradplonka.fuelcalculator.R;


public class CalculatorTab extends Fragment {
    private CalcTab1Fragment calcTab1Fragment;
    private CalcTab2Fragment calcTab2Fragment;
    private CalcTab3Fragment calcTab3Fragment;

    private LinearLayout costsPerPersonButton;
    private LinearLayout fuelConsumptionButton;
    private LinearLayout rangeButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculator_tab, container, false);

        initializeViewElements(view);
        handleTopMenu();
        costsPerPersonButton.performClick();


        return view;
    }

    private void handleTopMenu() {
        costsPerPersonButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CalcTab1Fragment calcTab1Fragment = new CalcTab1Fragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, calcTab1Fragment);
                fragmentTransaction.addToBackStack("costsPerPerson");
                fragmentTransaction.commit();

                costsPerPersonButton.getBackground().setColorFilter(getResources().getColor(R.color.darkBlue), PorterDuff.Mode.SRC_ATOP);
                fuelConsumptionButton.getBackground().setColorFilter(getResources().getColor(R.color.lightOrange), PorterDuff.Mode.SRC_ATOP);
                rangeButton.getBackground().setColorFilter(getResources().getColor(R.color.lightGreen), PorterDuff.Mode.SRC_ATOP);
            }
        });

        fuelConsumptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcTab2Fragment calcTab2Fragment = new CalcTab2Fragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, calcTab2Fragment);
                fragmentTransaction.addToBackStack("fuelConsumption");
                fragmentTransaction.commit();

                costsPerPersonButton.getBackground().setColorFilter(getResources().getColor(R.color.lightBlue), PorterDuff.Mode.SRC_ATOP);
                fuelConsumptionButton.getBackground().setColorFilter(getResources().getColor(R.color.darkOrange), PorterDuff.Mode.SRC_ATOP);
                rangeButton.getBackground().setColorFilter(getResources().getColor(R.color.lightGreen), PorterDuff.Mode.SRC_ATOP);

            }
        });

        rangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcTab3Fragment calcTab3Fragment = new CalcTab3Fragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, calcTab3Fragment);

                fragmentTransaction.commit();

                costsPerPersonButton.getBackground().setColorFilter(getResources().getColor(R.color.lightBlue), PorterDuff.Mode.SRC_ATOP);
                fuelConsumptionButton.getBackground().setColorFilter(getResources().getColor(R.color.lightOrange), PorterDuff.Mode.SRC_ATOP);
                rangeButton.getBackground().setColorFilter(getResources().getColor(R.color.darkGreen), PorterDuff.Mode.SRC_ATOP);
            }
        });
    }

    private void initializeViewElements(View view) {
        costsPerPersonButton = view.findViewById(R.id.cost_per_person_button);
        fuelConsumptionButton = view.findViewById(R.id.fuel_consumption_button);
        rangeButton = view.findViewById(R.id.range_button);

    }


}
