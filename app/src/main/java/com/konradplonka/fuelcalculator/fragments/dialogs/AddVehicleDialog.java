package com.konradplonka.fuelcalculator.fragments.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.other.DatabaseHelper;

import static android.content.ContentValues.TAG;

public class AddVehicleDialog extends DialogFragment {
    private EditText brandEditText;
    private EditText modelEditText;
    private ImageButton addVehicleImageButton;
    private DatabaseHelper databaseHelper;

    private OnAddVehicleDialogListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_vehicle, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        databaseHelper = new DatabaseHelper(getContext());

        initializeViewElements(view);
        onAddVehicleClick();
        return view;

    }

    private void onAddVehicleClick() {
        addVehicleImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand = brandEditText.getText().toString();
                String model = modelEditText.getText().toString();

                databaseHelper.addVehicle(brand, model);
                listener.refreshSpinner();
                dismiss();

            }
        });
    }

    private void initializeViewElements(View view) {
        brandEditText = (EditText) view.findViewById(R.id.brand_addVehicle_editText);
        modelEditText = (EditText) view.findViewById(R.id.model_addVehicle_editText);
        addVehicleImageButton = (ImageButton) view.findViewById(R.id.add_vehicle_button);


    }

    public interface OnAddVehicleDialogListener{
        void refreshSpinner();
    }

    @Override
    public void onAttach(Context context) {
        try{
            listener = (OnAddVehicleDialogListener) getTargetFragment();
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
