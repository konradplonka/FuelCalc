package com.konradplonka.fuelcalculator.fragments.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.other.DatabaseHelper;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.FileNotFoundException;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.konradplonka.fuelcalculator.other.DatabaseHelper.EXPORT_DICTIONARY;

public class ImportDialog extends DialogFragment {
    private AppCompatRadioButton clearDatabaseRadioButton;
    private  AppCompatRadioButton addToCurrentDatabaseRadioButton;
    private ImageButton importImageButton;
    private RadioGroup importRadioGroup;
    private LinearLayout progressBarLinearLayout;
    private TextView progressBarTextView;
    private ImageView progressBarImageView;

    private OnImportDialogListener listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_import, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initializeViewElements(view);
        handleRadioButtons();
        onClickImportImageButton();
        return view;
    }

    private void onClickImportImageButton() {
        importImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clearDatabaseRadioButton.isChecked()){
                    DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                    databaseHelper.clearData();
                }
                new MaterialFilePicker()
                        .withActivity((Activity) getContext())
                        .withRequestCode(1)
                        .withFilter(Pattern.compile(".*\\.csv$")) // Filtering files and directories by file name using regexp
                        .withFilterDirectories(true) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .withPath(getContext().getExternalFilesDir(null) + "/" + EXPORT_DICTIONARY)
                        .start();

            }

        });
    }

    private void initializeViewElements(View view) {
        clearDatabaseRadioButton = (AppCompatRadioButton) view.findViewById(R.id.clear_database_radioButton);
        addToCurrentDatabaseRadioButton = (AppCompatRadioButton) view.findViewById(R.id.add_to_current_database_radioButton);
        importImageButton = (ImageButton) view.findViewById(R.id.import_imageButton);
        importRadioGroup = (RadioGroup) view.findViewById(R.id.import_radioGroup);
        progressBarLinearLayout = (LinearLayout) view.findViewById(R.id.progressBar_linearLayout);
        progressBarTextView = (TextView) view.findViewById(R.id.progressBar_textView);
        progressBarImageView = (ImageView) view.findViewById(R.id.progressBar_imageView);
    }

    private void handleRadioButtons() {
        clearDatabaseRadioButton.setChecked(true);
        clearDatabaseRadioButton.setTextColor(Color.WHITE);

        clearDatabaseRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = ((AppCompatRadioButton)v).isChecked();
                if(isSelected){
                    clearDatabaseRadioButton.setTextColor(Color.WHITE);
                    addToCurrentDatabaseRadioButton.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });

        addToCurrentDatabaseRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = ((AppCompatRadioButton)v).isChecked();
                if(isSelected){
                    addToCurrentDatabaseRadioButton.setTextColor(Color.WHITE);
                    clearDatabaseRadioButton.setTextColor(getResources().getColor(R.color.colorAccent));

                }
            }
        });
    }
    private void buttonFinished(){
        progressBarLinearLayout.setAnimation(getAnimation());
        progressBarImageView.setAnimation(getAnimation());
        progressBarTextView.setAnimation(getAnimation());

        progressBarLinearLayout.setVisibility(View.VISIBLE);
        progressBarImageView.setVisibility(View.VISIBLE);
        progressBarTextView.setText("Zakończono!");
        importRadioGroup.setVisibility(View.GONE);


    }

    private Animation getAnimation(){
        return AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            String backupPath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            try{
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                databaseHelper.importDatabase(backupPath);
                listener.refreshRecyclerViewAfterImport();
                buttonFinished();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }


    }
    public interface OnImportDialogListener{
        void refreshRecyclerViewAfterImport();
    }

    @Override
    public void onAttach(Context context) {
        try{
            listener = (OnImportDialogListener) getTargetFragment();
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
