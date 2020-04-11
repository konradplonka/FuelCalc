package com.konradplonka.fuelcalculator.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.konradplonka.fuelcalculator.R;

import com.konradplonka.fuelcalculator.computations.CostPerPersonComputations;
import com.konradplonka.fuelcalculator.computations.FuelConsumptionComputations;
import com.konradplonka.fuelcalculator.computations.RangeComputations;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CalculatorTab extends Fragment {

    private Button costsPerPersonButton;
    private Button fuelUsageButton;
    private Button rangeButton;
    private FloatingActionButton calculateButton;

    private CardView costsPerPersonGetDataView;
    private LinearLayout costsPerPersonResultsView;

    private CardView fuelConsumptionGetDataView;
    private LinearLayout fuelCosnumptionResultsView;

    private CardView rangeGetDataView;
    private LinearLayout rangeResultsView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculator_tab, container, false);

        initializeViewElements(view);

        handleTopMenu(view);


        return view;
    }

    private void handleTopMenu(final View view) {


        setVisibilityOfViews(1);


       costsPerPersonButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setVisibilityOfViews(1);
           }
       });

       fuelUsageButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setVisibilityOfViews(2);
           }
       });

       rangeButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setVisibilityOfViews(3);
           }
       });




       calculateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(costsPerPersonGetDataView.getVisibility() == View.VISIBLE && costsPerPersonGetDataView.getVisibility() == View.VISIBLE){
                    makePricePerPersonComputations(view);

               }
               else if(fuelConsumptionGetDataView.getVisibility() == View.VISIBLE && fuelCosnumptionResultsView.getVisibility() == View.VISIBLE){
                   makeFuelConsumptionComputatuons(view);
               }
               else if(rangeGetDataView.getVisibility() == View.VISIBLE && rangeResultsView.getVisibility() == View.VISIBLE){
                   makeRangeComputations(view);
               }
           }
       });

    }

    private void initializeViewElements(View view) {

        costsPerPersonButton = (Button) view.findViewById(R.id.costs_per_person_button);
        fuelUsageButton = (Button) view.findViewById(R.id.fuel_usage_button);
        rangeButton = (Button) view.findViewById(R.id.range_button);

        calculateButton = (FloatingActionButton) view.findViewById(R.id.calculate_floatingButton);

        costsPerPersonGetDataView = (CardView) view.findViewById(R.id.costs_per_person_get_data_view);
        costsPerPersonResultsView = (LinearLayout) view.findViewById(R.id.costs_per_person_results_view);

        fuelConsumptionGetDataView = (CardView) view.findViewById(R.id.fuel_consumption_get_data_view);
        fuelCosnumptionResultsView = (LinearLayout) view.findViewById(R.id.fuel_consumption_results_view);

        rangeGetDataView = (CardView) view.findViewById(R.id.range_get_data_view);
        rangeResultsView = (LinearLayout) view.findViewById(R.id.range_results_view);
    }


    public void setVisibilityOfViews(int tab){
        if(tab == 1){
            costsPerPersonButton.setSelected(true);
            costsPerPersonButton.setPressed(false);

            fuelUsageButton.setSelected(false);

            rangeButton.setSelected(false);


            costsPerPersonGetDataView.setVisibility(View.VISIBLE);
            costsPerPersonResultsView.setVisibility(View.VISIBLE);

            fuelConsumptionGetDataView.setVisibility(View.GONE);
            fuelCosnumptionResultsView.setVisibility(View.GONE);

            rangeGetDataView.setVisibility(View.GONE);
            rangeResultsView.setVisibility(View.GONE);
        }

        if(tab == 2){
            costsPerPersonButton.setSelected(false);

            fuelUsageButton.setSelected(true);
            fuelUsageButton.setPressed(false);

            rangeButton.setSelected(false);


            costsPerPersonGetDataView.setVisibility(View.GONE);
            costsPerPersonResultsView.setVisibility(View.GONE);

            fuelConsumptionGetDataView.setVisibility(View.VISIBLE);
            fuelCosnumptionResultsView.setVisibility(View.VISIBLE);

            rangeGetDataView.setVisibility(View.GONE);
            rangeResultsView.setVisibility(View.GONE);
        }

        if(tab == 3){
            costsPerPersonButton.setSelected(false);

            fuelUsageButton.setSelected(false);

            rangeButton.setSelected(true);
            rangeButton.setPressed(false);



            costsPerPersonGetDataView.setVisibility(View.GONE);
            costsPerPersonResultsView.setVisibility(View.GONE);

            fuelConsumptionGetDataView.setVisibility(View.GONE);
            fuelCosnumptionResultsView.setVisibility(View.GONE);

            rangeGetDataView.setVisibility(View.VISIBLE);
            rangeResultsView.setVisibility(View.VISIBLE);
        }
    }

    private void makePricePerPersonComputations(View view){

        // Initialize view elements

        EditText distanceEditText = (EditText) view.findViewById(R.id.distance_price_per_person_editText);
        EditText fuelPriceEditText = (EditText) view.findViewById(R.id.fuel_price_price_per_person_editText);
        EditText fuelConsumptionEditText = (EditText) view.findViewById(R.id.fuel_consumption_price_per_person_editText);
        EditText numberOfPeopleEditText = (EditText) view.findViewById(R.id.number_of_people_price_per_person_editText);

        TextView costPerPersonTextView = (TextView) view.findViewById(R.id.cost_per_person_result_textView);
        TextView totalCostTextView = (TextView) view.findViewById(R.id.total_cost_price_per_person_result_textView);
        TextView fuelUsageTextView = (TextView) view.findViewById(R.id.fuel_usage_result_textView);


        // Check editTexts

        if(isEmptyField(distanceEditText) || isNull(distanceEditText)) return;
        else if(isEmptyField(fuelPriceEditText) || isNull(fuelPriceEditText)) return;
        else if(isEmptyField(fuelConsumptionEditText) || isNull(fuelConsumptionEditText)) return;
        else if(isEmptyField(numberOfPeopleEditText) || isNull(numberOfPeopleEditText)) return;

        // Parse data

        double distance = Double.parseDouble(distanceEditText.getText().toString());
        double fuelPrice = Double.parseDouble(fuelPriceEditText.getText().toString());
        double fuelConsumpiton = Double.parseDouble(fuelConsumptionEditText.getText().toString());
        double numberOfPeople = Integer.parseInt(numberOfPeopleEditText.getText().toString());

        CostPerPersonComputations costPerPersonComputations = new CostPerPersonComputations(distance,fuelPrice,fuelConsumpiton,numberOfPeople);

        DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        df.setMaximumFractionDigits(2);

        // Set results data

        costPerPersonTextView.setText(df.format(costPerPersonComputations.calculatePricePerPerson())+ "zł");
        totalCostTextView.setText(df.format(costPerPersonComputations.calculateTotalCost()) + " zł");
        fuelUsageTextView.setText(df.format(costPerPersonComputations.calculateUsedFuel()) + " l");

        LinearLayout costsPerPersonResultsView = (LinearLayout) view.findViewById(R.id.costs_per_person_results_view);
        TextView resultsTextTextView = (TextView) view.findViewById(R.id.results_text_textView);


    }

    private void makeFuelConsumptionComputatuons(View view){

        // Initialize view elements

        EditText distanceEditText = (EditText) view.findViewById(R.id.distance_fuel_consumption_editText);
        EditText fuelPriceEditText = (EditText) view.findViewById(R.id.fuel_price_fuel_consumption_editText);
        EditText amountOfFuelEditText =(EditText) view.findViewById(R.id.amount_of_fuel_fuel_consumption_editText);

        TextView averageFuelConsumptionTextView = (TextView) view.findViewById(R.id.average_fuel_consumption_result_textView);
        TextView totalCostTextView = (TextView) view.findViewById(R.id.total_cost_fuel_consumption_result_textView);

        // Check editTexts

        if(isEmptyField(distanceEditText) || isNull(distanceEditText)) return;
        else if(isEmptyField(distanceEditText) || isNull(fuelPriceEditText)) return;
        else if(isEmptyField(amountOfFuelEditText) || isNull(amountOfFuelEditText)) return;

        // Parse data

        double distance = Double.parseDouble(distanceEditText.getText().toString());
        double fuelPrice = Double.parseDouble(fuelPriceEditText.getText().toString());
        double amountOfFuel = Double.parseDouble(amountOfFuelEditText.getText().toString());

        FuelConsumptionComputations fuelConsumptionComputations = new FuelConsumptionComputations(distance, fuelPrice, amountOfFuel);

        DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        df.setMaximumFractionDigits(2);


        // Set results data

        averageFuelConsumptionTextView.setText(df.format(fuelConsumptionComputations.calculateAverageFuelConsumpion()) + " l/100 km");
        totalCostTextView.setText(df.format(fuelConsumptionComputations.calculateTotalCost()) + " zł");


    }

    private void makeRangeComputations(View view) {

        // Initialize view elements

        EditText fuelConsumptionEditText = (EditText) view.findViewById(R.id.fuel_consumption_range_editText);
        EditText fuelPriceEditText = (EditText) view.findViewById(R.id.fuel_price_range_editText);
        EditText amountOfFuelEditText = (EditText) view.findViewById(R.id.amount_of_fuel_range_editText);

        TextView rangeTextView = (TextView) view.findViewById(R.id.range_result_textView);
        TextView totalCost = (TextView) view.findViewById(R.id.total_cost_range_result_textView);

        // Check editTexts

        if(isEmptyField(fuelConsumptionEditText) || isNull(fuelConsumptionEditText)) return;
        else if(isEmptyField(fuelPriceEditText) || isNull(fuelPriceEditText)) return;
        else if(isEmptyField(amountOfFuelEditText) || isNull(amountOfFuelEditText)) return;

        // Parse data

        double fuelConsumption  = Double.parseDouble(fuelConsumptionEditText.getText().toString());
        double fuelPrice = Double.parseDouble(fuelPriceEditText.getText().toString());
        double amountOfFuel = Double.parseDouble(amountOfFuelEditText.getText().toString());

        RangeComputations rangeComputations = new RangeComputations(fuelConsumption, fuelPrice, amountOfFuel);

        DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        df.setMaximumFractionDigits(2);

        // Set results data

        rangeTextView.setText(df.format(rangeComputations.calculateRange()) + " km");
        totalCost.setText(df.format(rangeComputations.calculateTotalCost()) + " zł");


    }

    private boolean isEmptyField (EditText editText){
        boolean result = editText.getText().toString().length() <= 0;
        if (result)
            Toast.makeText(getContext(), "Uzupełnij wszystkie pola!", Toast.LENGTH_SHORT).show();
        return result;
    }
    private boolean isNull(EditText editText){
        String value=editText.getText().toString();
        boolean result = Double.parseDouble(value) == 0;
        if(result)
            Toast.makeText(getContext(), "Pola nie mogą mieć wartości 0!", Toast.LENGTH_SHORT).show();
        return result;

    }

}
