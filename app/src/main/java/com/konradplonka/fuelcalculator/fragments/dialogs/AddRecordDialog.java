package com.konradplonka.fuelcalculator.fragments.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.DialogFragment;

import com.konradplonka.fuelcalculator.fragments.DatePickerFragment;
import com.konradplonka.fuelcalculator.other.PetrolStation;
import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.other.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class AddRecordDialog extends DialogFragment implements SetStationDialog.OnSetStationDialogListener, DatePickerFragment.OnDatePickerFragmentListener {

    private ImageView stationImageView;
    private Button setStationButton;
    private TextView distanceTextView;
    private RadioButton counterRadioButton;
    private RadioButton dailyCounterRadioButton;
    private EditText distanceEditText;
    private EditText amountOfFuelEditText;
    private EditText costEditText;
    private RadioButton totalCostRadioButton;
    private RadioButton pricePerLRadioButton;
    private EditText dateEditText;
    private EditText descriptionEditText;
    private ImageButton addRecordButton;
    private DatabaseHelper db;
    private int vehicleId;
    private int distance;
    private OnAddRecordDialogListener listener;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_record, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        db = new DatabaseHelper(getContext());
        savedInstanceState = getArguments();
        vehicleId = savedInstanceState.getInt("vehicleId");
        distance = (savedInstanceState.getInt("distance"));



        initializeAddRecordDialogElements(view);

        distanceTextView.setText(formatDistance(String.valueOf(distance)) + " km");


        handleCostRadioButtons();
        handleCounterRadioButtons();
        openSetStationDialog();
        addRecordToList();

        handleDateEditText();

        return view;
    }


    private void handleDateEditText() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateEditText.setText(simpleDateFormat.format(new Date()));

        dateEditText.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("date", dateEditText.getText().toString());
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getFragmentManager(), "datePicker");
                datePickerFragment.setArguments(bundle);
                datePickerFragment.setTargetFragment(AddRecordDialog.this, 1);
            }
        });
    }

    private void openSetStationDialog() {
        setStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetStationDialog setStationDialog = new SetStationDialog();
                setStationDialog.show(getFragmentManager(), "setStation");
                setStationDialog.setTargetFragment(AddRecordDialog.this, 1);
            }
        });
    }



    private void initializeAddRecordDialogElements(View view) {

        stationImageView = view.findViewById(R.id.station_addRecord_imageView);
        setStationButton = view.findViewById(R.id.set_station_button);
        distanceTextView = view.findViewById(R.id.distance_addRecord_textView);
        counterRadioButton = view.findViewById(R.id.counter_radioButton);
        dailyCounterRadioButton = view.findViewById(R.id.daily_counter_radioButton);
        distanceEditText = view.findViewById(R.id.distance_addRecord_editText);
        amountOfFuelEditText = view.findViewById(R.id.amount_of_fuel_addRecord_editText);
        costEditText = view.findViewById(R.id.cost_addRecord_editText);
        totalCostRadioButton = view.findViewById(R.id.total_cost_radioButton);
        pricePerLRadioButton= view.findViewById(R.id.price_per_l_radioButton);
        dateEditText = view.findViewById(R.id.date_addRecord_editText);
        descriptionEditText = view.findViewById(R.id.description_addRecord_editText);
        addRecordButton = view.findViewById(R.id.add_record_button);

    }



    private void handleCostRadioButtons() {

        totalCostRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = ((AppCompatRadioButton)v).isChecked();
                if(isSelected){
                    totalCostRadioButton.setTextColor(Color.WHITE);
                    pricePerLRadioButton.setTextColor(getContext().getResources().getColor(R.color.colorAccent));

                    costEditText.setHint(R.string.total_cost);
                }
            }
        });

        pricePerLRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = ((AppCompatRadioButton)v).isChecked();
                if(isSelected){
                    totalCostRadioButton.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                    pricePerLRadioButton.setTextColor(Color.WHITE);

                    costEditText.setHint(R.string.price_per_liter);

                }
            }
        });
    }

    private void handleCounterRadioButtons() {

        counterRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = ((AppCompatRadioButton)v).isChecked();
                if(isSelected){
                    counterRadioButton.setTextColor(Color.WHITE);
                    dailyCounterRadioButton.setTextColor(getContext().getResources().getColor(R.color.colorAccent));

                    distanceEditText.setHint(R.string.vehicle_mileage_km);
                }
            }
        });

        dailyCounterRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = ((AppCompatRadioButton)v).isChecked();
                if(isSelected){
                    counterRadioButton.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                    dailyCounterRadioButton.setTextColor(Color.WHITE);

                    distanceEditText.setHint(R.string.daily_counter_km);

                }
            }
        });
    }


    private void addRecordToList() {

            addRecordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isEmptyField(distanceEditText) && !isNull(distanceEditText) && !isEmptyField(amountOfFuelEditText) && !isNull(amountOfFuelEditText) && !isEmptyField(costEditText) && !isNull(costEditText)) {
                        double totalCost;
                        int counter;
                        if (totalCostRadioButton.isChecked()) {
                            totalCost = Double.parseDouble(costEditText.getText().toString());

                        } else {
                            totalCost = Double.parseDouble(costEditText.getText().toString()) * Double.parseDouble(amountOfFuelEditText.getText().toString());

                        }

                        if(counterRadioButton.isChecked()){
                            counter = Integer.parseInt(distanceEditText.getText().toString());

                        }
                        else {
                            counter = distance + Integer.parseInt(distanceEditText.getText().toString());
                        }

                        db.addData(
                                vehicleId,
                                stationImageView.getTag().toString(),
                                counter,
                                Double.parseDouble(amountOfFuelEditText.getText().toString()),
                                totalCost,
                                dateEditText.getText().toString(),
                                descriptionEditText.getText().toString()
                        );

                        listener.refreshRecyclerView();
                        listener.handleVisibilityOfEmptyListMessage();
                        dismiss();
                    }

                }
            });




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

    public void updateDateEditText(String date){
        dateEditText.setText(date);
    }

    public void updateStationImageView(PetrolStation petrolStation){
        stationImageView.setImageResource(petrolStation.getImageSourceResource());
        stationImageView.setBackgroundResource(petrolStation.getLayoutBackgroundResource());
        stationImageView.setTag(petrolStation.getPetrolStations().toString());
    }

    private String formatDistance(String distance){
        StringBuilder distanceReverse = new StringBuilder(distance).reverse();
        distance = distanceReverse.toString();
        StringBuilder formattedDistance = new StringBuilder();
        for(int i = 0; i < distance.length(); i++) {
            formattedDistance.append(distance.charAt(i));
            if ((i + 1) % 3 == 0) {
                formattedDistance.append(" ");
            }
        }

        return formattedDistance.reverse().toString();
    }


    @Override
    public void onDateChange(String date) {
        updateDateEditText(date);
    }

    @Override
    public void onStationSelected(PetrolStation petrolStation) {
        updateStationImageView(petrolStation);
    }



    public interface OnAddRecordDialogListener{
        void refreshRecyclerView();
        void handleVisibilityOfEmptyListMessage();

    }

    @Override
    public void onAttach(Context context) {
        try{
            listener = (OnAddRecordDialogListener) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " +e.getMessage());
        }

        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }
}
