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
import com.konradplonka.fuelcalculator.computations.FuelConsumptionComputations;
import com.konradplonka.fuelcalculator.computations.RangeComputations;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CalcTab3Fragment extends Fragment {
    private EditText fuelConsumptioneEditText;
    private EditText pricePerLiterEditText;
    private EditText amountOfFuelEditText;
    private LinearLayout results;
    private LinearLayout totalCostContainerLinearLayout;

    private Button calculateButton;
    private ImageButton clearImageButton;
    private TextView rangeTextView;
    private TextView totalCostTextView;

    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_calc_tab3, container, false);
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
                fuelConsumptioneEditText.setText("");
                pricePerLiterEditText.setText("");
                amountOfFuelEditText.setText("");
                results.setVisibility(View.GONE);
                fuelConsumptioneEditText.requestFocus();
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

    private void calculate() {
        int fuelConsumption = Integer.parseInt(fuelConsumptioneEditText.getText().toString());
        double pricePerLiter = 0;
        double amountOfFuel = Double.parseDouble(amountOfFuelEditText.getText().toString());

        if(pricePerLiterEditText.getText().toString().length() > 0){
            pricePerLiter = Double.parseDouble(pricePerLiterEditText.getText().toString());
            totalCostContainerLinearLayout.setVisibility(View.VISIBLE);
        }
        else{
            totalCostContainerLinearLayout.setVisibility(View.GONE);
        }
        DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        df.setMaximumFractionDigits(2);

        RangeComputations rangeComputations = new RangeComputations(fuelConsumption, pricePerLiter, amountOfFuel);
        rangeTextView.setText(df.format(rangeComputations.calculateRange()) + " km");
        totalCostTextView.setText(df.format(rangeComputations.calculateTotalCost()) + " zł");

    }
    private boolean checkEditTexts() {
        if(isEmptyField(fuelConsumptioneEditText) || isNull(fuelConsumptioneEditText)) return false;
        else if(isEmptyField(amountOfFuelEditText) || isNull(amountOfFuelEditText)) return false;
        else return true;
    }

    private void initializeViewElements(View view) {
        fuelConsumptioneEditText = view.findViewById(R.id.fuel_consumption);
        pricePerLiterEditText = view.findViewById(R.id.price_per_liter);
        amountOfFuelEditText = view.findViewById(R.id.amount_of_fuel);
        results = view.findViewById(R.id.results);
        rangeTextView = view.findViewById(R.id.range_result);
        totalCostTextView = view.findViewById(R.id.total_cost_result);
        totalCostContainerLinearLayout = view.findViewById(R.id.total_cost_result_container);

        calculateButton = view.findViewById(R.id.calculate_button);
        clearImageButton = view.findViewById(R.id.clear_button);

    }

    private Animation getAnimation(){
        return AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
