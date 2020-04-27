package com.konradplonka.fuelcalculator.fragments.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.DialogFragment;

import com.konradplonka.fuelcalculator.fragments.DatePickerFragment;
import com.konradplonka.fuelcalculator.other.PetrolStation;
import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.other.DatabaseHelper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class EditRecordDialog extends DialogFragment implements SetStationDialog.OnSetStationDialogListener, DatePickerFragment.OnDatePickerFragmentListener {

    private ImageView stationImageView;
    private Button setStationButton;
    private TextView distanceTextView;
    private RadioGroup counterRadioGroup;
    private EditText distanceEditText;
    private EditText amountOfFuelEditText;
    private EditText costEditText;
    private RadioButton totalCostRadioButton;
    private RadioButton pricePerLRadioButton;
    private EditText dateEditText;
    private EditText descriptionEditText;
    private ImageButton addRecordButton;
    private DatabaseHelper db;
    private OnEditRecordDialogListener listener;
    private DecimalFormat df;

    private int id;
    private int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_record, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        df.setMaximumFractionDigits(2);
        savedInstanceState = getArguments();

        id = savedInstanceState.getInt("id");
        position = savedInstanceState.getInt("position");

        db = new DatabaseHelper(getContext());

        initializeAddRecordDialogElements(view);
        fillFieldsWithItemData(savedInstanceState);
        handleCostRadioButtons(savedInstanceState);
        openSetStationDialog();
        handleEditRecord(savedInstanceState);

        handleDateEditText();

        return view;
    }

    private void fillFieldsWithItemData(Bundle savedInstanceState) {

        distanceTextView.setVisibility(View.GONE);
        counterRadioGroup.setVisibility(View.GONE);

        ArrayList<PetrolStation> petrolStationsList = PetrolStation.getPetrolStationList();
        for(PetrolStation petrolStation: petrolStationsList){
            if(petrolStation.getPetrolStations().toString().equals(savedInstanceState.getString("petrolStation"))){
                stationImageView.setImageResource(petrolStation.getImageSourceResource());
                stationImageView.setBackgroundResource(petrolStation.getLayoutBackgroundResource());
                stationImageView.setTag(savedInstanceState.getString("petrolStation"));
            }
        }

        distanceEditText.setText(String.valueOf(savedInstanceState.getInt("distance")));
        amountOfFuelEditText.setText(df.format(savedInstanceState.getDouble("amountOfFuel")));
        costEditText.setText(df.format(savedInstanceState.getDouble("totalCost")));
        dateEditText.setText(savedInstanceState.getString("date"));
        descriptionEditText.setText(savedInstanceState.getString("description"));
    }

    private void handleDateEditText() {

        dateEditText.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("date", dateEditText.getText().toString());
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getFragmentManager(), "datePicker");
                datePickerFragment.setTargetFragment(EditRecordDialog.this, 1);
                datePickerFragment.setArguments(bundle);
            }
        });
    }

    private void openSetStationDialog() {
        setStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetStationDialog setStationDialog = new SetStationDialog();
                setStationDialog.show(getFragmentManager(), "setStation");
                setStationDialog.setTargetFragment(EditRecordDialog.this, 1);
//
            }
        });
    }



    private void initializeAddRecordDialogElements(View view) {


        stationImageView = view.findViewById(R.id.station_addRecord_imageView);
        setStationButton = view.findViewById(R.id.set_station_button);
        distanceTextView = view.findViewById(R.id.distance_addRecord_textView);
        counterRadioGroup = view.findViewById(R.id.counter_radioGroup);
        distanceEditText = view.findViewById(R.id.distance_addRecord_editText);
        amountOfFuelEditText = view.findViewById(R.id.amount_of_fuel_addRecord_editText);
        costEditText = view.findViewById(R.id.cost_addRecord_editText);
        totalCostRadioButton = view.findViewById(R.id.total_cost_radioButton);
        pricePerLRadioButton= view.findViewById(R.id.price_per_l_radioButton);
        dateEditText = view.findViewById(R.id.date_addRecord_editText);
        descriptionEditText = view.findViewById(R.id.description_addRecord_editText);
        addRecordButton = view.findViewById(R.id.add_record_button);

    }



    private void handleCostRadioButtons(final Bundle savedInstanceState) {

        totalCostRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = ((AppCompatRadioButton)v).isChecked();
                if(isSelected){
                    totalCostRadioButton.setTextColor(Color.WHITE);
                    pricePerLRadioButton.setTextColor(getContext().getResources().getColor(R.color.colorAccent));

                    costEditText.setHint("Całkowity koszt [zł]");
                    costEditText.setText(df.format(savedInstanceState.getDouble("totalCost")));
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

                    costEditText.setHint("Cena za l [zł]");
                    costEditText.setText(df.format(savedInstanceState.getDouble("totalCost")/savedInstanceState.getDouble("amountOfFuel")));

                }
            }
        });
    }


    private void handleEditRecord(final Bundle bundle) {



        addRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stationTag = stationImageView.getTag().toString();
                int distance = Integer.parseInt(distanceEditText.getText().toString());
                double amountOfFuel = Double.parseDouble(amountOfFuelEditText.getText().toString());
                double totalCost = 0;
                String date = dateEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                if(totalCostRadioButton.isChecked()){
                    totalCost = Double.parseDouble(costEditText.getText().toString());



                }
                else{
                    totalCost = Double.parseDouble(costEditText.getText().toString())*Double.parseDouble(amountOfFuelEditText.getText().toString());
                }

                if(db.updateData(id, stationTag, distance, amountOfFuel, totalCost, date, description)){
                    listener.refreshItem(position, stationTag, distance, amountOfFuel, totalCost, date, description);

                }
                dismiss();

            }
        });



    }

    public void updateDateEditText(String date){
        dateEditText.setText(date);
    }

    public void updateStationImageView(PetrolStation petrolStation){
        stationImageView.setImageResource(petrolStation.getImageSourceResource());
        stationImageView.setBackgroundResource(petrolStation.getLayoutBackgroundResource());
        stationImageView.setTag(petrolStation.getPetrolStations().toString());
    }


    @Override
    public void onDateChange(String date) {
        updateDateEditText(date);
    }

    @Override
    public void onStationSelected(PetrolStation petrolStation) {
        updateStationImageView(petrolStation);
    }


    public interface OnEditRecordDialogListener{
        void refreshItem(int position, String stationTag, int distance, double amountOfFuel, double totalCost, String date, String description);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  OnEditRecordDialogListener){
            listener = (OnEditRecordDialogListener) context;
        }
        else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }
}
