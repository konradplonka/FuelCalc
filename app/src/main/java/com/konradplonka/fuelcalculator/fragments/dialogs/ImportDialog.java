package com.konradplonka.fuelcalculator.fragments.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.other.DatabaseHelper;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.FileNotFoundException;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.konradplonka.fuelcalculator.other.DatabaseHelper.EXPORT_DICTIONARY;

public class ImportDialog extends DialogFragment {
    private ImageButton importButton;
    private LinearLayout doneLinearLayout;
    private TextView importMessageTextView;
    private ImageButton backImageButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_import, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initializeViewElements(view);
        onImportImageButtonClick();
        onBackImageButtonClick();

        return view;
    }

    private void onBackImageButtonClick() {
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void onImportImageButtonClick() {
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity((Activity) getContext())
                        .withRequestCode(1)
                        .withFilter(Pattern.compile(".*\\.db$")) // Filtering files and directories by file name using regexp
                        .withFilterDirectories(true) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .withPath(getContext().getExternalFilesDir(null) + "/" + EXPORT_DICTIONARY)
                        .start();
            }
        });
    }


    private void initializeViewElements(View view) {
        importButton = view.findViewById(R.id.import_imageButton);
        doneLinearLayout = view.findViewById(R.id.done_linearLayout);
        importMessageTextView = view.findViewById(R.id.import_message);
        backImageButton = view.findViewById(R.id.back_button);
    }
    private void setImportFinishedState(String backupName){
        importMessageTextView.setAnimation(getAnimation());
        doneLinearLayout.setAnimation(getAnimation());

        doneLinearLayout.setVisibility(View.VISIBLE);
        importMessageTextView.setText(String.format("Baza danych o nazwie \"%s\" została pomyślnie zaimportowana!", backupName));




    }

    private Animation getAnimation(){
        return AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String backupPath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
            if(databaseHelper.importDatabase(backupPath, getContext())){
                getBackupFileName(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                setImportFinishedState(getBackupFileName(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)));
                saveSelectedVehicleStatePref(0,0);

            }

        }
    }

    private String getBackupFileName(String filePath) {
        String[] splitFilePath = filePath.split("/");
        return splitFilePath[splitFilePath.length - 1];
    }

    private void saveSelectedVehicleStatePref(int selectedVehicleId, int selectedVehiclePosition){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedVehicleId", selectedVehicleId);
        editor.putInt("selectedVehiclePosition", selectedVehiclePosition);
        editor.commit();
    }
}
