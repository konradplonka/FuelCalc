package com.konradplonka.fuelcalculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.computations.CostPerPersonComputations;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CalcTab1Fragment extends Fragment {
    private Button calculateButton;
    private ImageButton clearImageButton;
    private LinearLayout results;
    private EditText distanceEditText;
    private EditText fuelPriceEditText;
    private EditText fuelConsumptionEditText;
    private EditText numberOfPeopleEditText;
    private TextView costPerPersonTextView;
    private TextView fuelUsageTextView;
    private TextView totalCostTextView;
    private AdView mAdView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calc_tab1, container, false);

        initializeViewElements(view);
        loadAd(view);
        onCalculateButtonClick(view);
        onClearButtonClick();
        return view;
    }

    private void loadAd(View view) {
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void onClearButtonClick() {
        clearImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distanceEditText.setText("");
                fuelPriceEditText.setText("");
                fuelConsumptionEditText.setText("");
                numberOfPeopleEditText.setText("");
                results.setVisibility(View.GONE);
                distanceEditText.requestFocus();

            }
        });

    }

    private void onCalculateButtonClick(final View view) {
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEditTexts()){
                    if(results.getVisibility() == View.GONE){
                        results.setAnimation(getAnimation());
                        results.setVisibility(View.VISIBLE);
                    }
                    hideKeyboard(view);
                    calculate();
                }


            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void calculate() {


        int distance = Integer.parseInt(distanceEditText.getText().toString());
        double fuelPrice = Double.parseDouble(fuelPriceEditText.getText().toString());
        double fuelConsumption = Double.parseDouble(fuelConsumptionEditText.getText().toString());
        int numberOfPeople = Integer.parseInt(numberOfPeopleEditText.getText().toString());


        DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        df.setMaximumFractionDigits(2);

        CostPerPersonComputations costPerPersonComputations = new CostPerPersonComputations(distance, fuelPrice, fuelConsumption, numberOfPeople);
        costPerPersonTextView.setText(df.format(costPerPersonComputations.calculatePricePerPerson()) + " zł");
        fuelUsageTextView.setText(df.format(costPerPersonComputations.calculateFuelUsage()) + " l");
        totalCostTextView.setText(df.format(costPerPersonComputations.calculateTotalCost()) + " zł");


    }

    private boolean checkEditTexts() {
        if(isEmptyField(distanceEditText) || isNull(distanceEditText)) return false;
        else if(isEmptyField(fuelPriceEditText) || isNull(fuelPriceEditText)) return false;
        else if(isEmptyField(fuelConsumptionEditText) || isNull(fuelConsumptionEditText)) return false;
        else if(isEmptyField(numberOfPeopleEditText) || isNull(numberOfPeopleEditText)) return false;
        else return true;
    }

    private void initializeViewElements(View view) {
        calculateButton = view.findViewById(R.id.calculate_button);
        clearImageButton = view.findViewById(R.id.clear_button);
        results = view.findViewById(R.id.results);
        distanceEditText = view.findViewById(R.id.distance);
        fuelPriceEditText = view.findViewById(R.id.fuel_price);
        fuelConsumptionEditText = view.findViewById(R.id.fuel_consumption);
        numberOfPeopleEditText = view.findViewById(R.id.number_of_people);
        costPerPersonTextView = view.findViewById(R.id.cost_per_person_result);
        fuelUsageTextView = view.findViewById(R.id.fuel_usage_result);
        totalCostTextView = view.findViewById(R.id.total_cost_result);


    }
    private Animation getAnimation(){
        return AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation);
    }

    private boolean isEmptyField (EditText editText){
        boolean result = editText.getText().toString().length() <= 0;
        if (result)
            Toast.makeText(getContext(), R.string.fill_all_required_fields, Toast.LENGTH_SHORT).show();
        return result;
    }
    private boolean isNull(EditText editText){
        String value=editText.getText().toString();
        boolean result = Double.parseDouble(value) == 0;
        if(result)
            Toast.makeText(getContext(), R.string.fiels_ccnnot_be_zero, Toast.LENGTH_SHORT).show();
        return result;

    }
}
