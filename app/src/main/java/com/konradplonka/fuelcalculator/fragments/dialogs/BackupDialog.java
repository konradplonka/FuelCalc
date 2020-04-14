package com.konradplonka.fuelcalculator.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.fragments.DictionaryTab;
import com.konradplonka.fuelcalculator.other.DatabaseHelper;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.FileNotFoundException;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.konradplonka.fuelcalculator.other.DatabaseHelper.EXPORT_DICTIONARY;

public class BackupDialog extends DialogFragment {
    private Button importButton;
    private Button exportButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_backup, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initializeViewElements(view);
        onExportButtonClick();
        onImportButtonClick();


        return view;
    }

    private void onImportButtonClick() {
        importButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ImportDialog importDialog = new ImportDialog();
                importDialog.show(getFragmentManager(), "ImportDialog");
                importDialog.setTargetFragment(getTargetFragment(), 1);

            }
        });
    }

    private void onExportButtonClick() {

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExportDialog exportDialog = new ExportDialog();
                exportDialog.show(getFragmentManager(), "ExportDialog");

            }
        });
    }
    


    private void initializeViewElements(View view) {
        importButton = view.findViewById(R.id.import_button);
        exportButton = (Button) view.findViewById(R.id.export_button);

    }

}
