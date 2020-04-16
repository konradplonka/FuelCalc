package com.konradplonka.fuelcalculator.fragments.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.konradplonka.fuelcalculator.R;

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
