package com.konradplonka.fuelcalculator.fragments.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.DialogFragment;

import com.konradplonka.fuelcalculator.other.PetrolStation;
import com.konradplonka.fuelcalculator.other.PetrolStations;
import com.konradplonka.fuelcalculator.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SetStationDialog extends DialogFragment{
    ArrayList<PetrolStation> petrolStationsList;
    ArrayList <RadioButton> radioButtons;
    RadioGroup radioGroup;
    ImageButton setStationButton;
    OnSetStationDialogListener listener;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_set_station, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        petrolStationsList = PetrolStation.getPetrolStationList();
        radioButtons = new ArrayList<>();

        initializeElements(view);
        generateRadioButtons();
        handleRadioButtons();
        setStation();

        return view;
    }

    private void setStation() {
        setStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (RadioButton radioButton: radioButtons){
                    if(radioButton.isChecked()){
                        for(PetrolStation station:petrolStationsList){
                            if(station.getPetrolStations().toString().equals(radioButton.getText())){
                                Toast.makeText(getContext(), radioButton.getText(), Toast.LENGTH_LONG).show();
                                listener.onStationSelected(station);

                            }
                        }

                        dismiss();
                    }
                }
            }
        });
    }

    private void initializeElements(View view) {
        radioGroup = view.findViewById(R.id.set_station_radioGroup);
        setStationButton = view.findViewById(R.id.set_station_button);

    }

    private void generateRadioButtons() {
        radioGroup.removeAllViews();

        // Generate radio buttons
        int i = 0;

        for(PetrolStation petrolStation:petrolStationsList){

            RadioButton button = new AppCompatRadioButton(getContext());

            button.setId(i);
            button.setText(petrolStation.getPetrolStations().toString());

            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(getContext(),null);
            params.setMargins(0, getDpSize(8), 0, 0);
            params.gravity = Gravity.CENTER_HORIZONTAL;

            button.setLayoutParams(params);
            button.setBackground(getResources().getDrawable(R.drawable.radio_button_selector));
            button.setTextColor(getResources().getColor(R.color.colorAccent));
            button.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            button.setPadding(getDpSize(16), getDpSize(4), getDpSize(16), getDpSize(4));

            if(petrolStation.getPetrolStations().equals(PetrolStations.UNKNOWN)){
                button.setChecked(true);
                button.setTextColor(Color.WHITE);
            }

            radioGroup.addView(button);
            radioButtons.add(button);

            i++;
        }

    }

    private void handleRadioButtons(){
        for (final RadioButton radioButton: radioButtons){

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSelected = ((AppCompatRadioButton)v).isChecked();
                    if (isSelected){

                        radioButton.setTextColor(Color.WHITE);

                        for(RadioButton radioButton : radioButtons){
                            if(!radioButton.isChecked()){
                                radioButton.setTextColor(getResources().getColor(R.color.colorAccent));
                            }

                        }

                    }
                }
            });
        }

    }

    private int getDpSize(int dp){
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (dp*scale + 0.5f);

        return dpAsPixels;
    }


    public interface OnSetStationDialogListener{
            void onStationSelected(PetrolStation petrolStation);
    }
    @Override
    public void onAttach(Context context) {
        try{
            listener = (OnSetStationDialogListener) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " +e.getMessage());
        }

        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


}
