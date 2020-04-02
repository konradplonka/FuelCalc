package com.konradplonka.fuelcalculator.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.konradplonka.fuelcalculator.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    OnDatePickerFragmentListener listener;
    SimpleDateFormat simpleDateFormat;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        int year;
        int month;
        int day;

        savedInstanceState = getArguments();
        String dateString = savedInstanceState.getString("date");

        try {
            Date date = simpleDateFormat.parse(dateString);
            c.setTime(date);
            day = c.get(Calendar.DAY_OF_MONTH);
            month = c.get(Calendar.MONTH);
            year = c.get(Calendar.YEAR);


        } catch (ParseException e) {
            day = c.get(Calendar.DAY_OF_MONTH);
            month = c.get(Calendar.MONTH);
            year = c.get(Calendar.YEAR);
        }


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        try {
            Date date = simpleDateFormat.parse(String.format("%d-%d-%d",day, month+1, year));
            listener.onDateChange(simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public interface OnDatePickerFragmentListener {
        void onDateChange(String date);
    }

    @Override
    public void onAttach(Context context) {
        try{
            listener = (OnDatePickerFragmentListener)getTargetFragment();
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