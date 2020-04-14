package com.konradplonka.fuelcalculator.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.konradplonka.fuelcalculator.activities.MainActivity;
import com.konradplonka.fuelcalculator.fragments.dialogs.AddRecordDialog;
import com.konradplonka.fuelcalculator.fragments.dialogs.BackupDialog;
import com.konradplonka.fuelcalculator.fragments.dialogs.ImportDialog;
import com.konradplonka.fuelcalculator.fragments.dialogs.SettingsDialog;
import com.konradplonka.fuelcalculator.other.ListItem;
import com.konradplonka.fuelcalculator.other.MyAdapter;
import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.other.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class DictionaryTab extends Fragment implements AddRecordDialog.OnAddRecordDialogListener, ImportDialog.OnImportDialogListener {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ListItem> list;
    private DatabaseHelper db;
    private FloatingActionButton addRecordFabButton;
    private FloatingActionButton backupFabButton;
    private FloatingActionButton settingsFabButton;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dictionary_tab, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<ListItem>();
        db = new DatabaseHelper(getContext());

        adapter = new MyAdapter(list, getContext(), getFragmentManager());
        recyclerView.setAdapter(adapter);





        loadDatabase();
        initializeElements(view);
        handleFabButtons();

        OnAddRecordClick();
        OnSettingsClick();
        OnBackupClick();

        return view;
    }

    private void OnAddRecordClick() {
        addRecordFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecordDialog addRecordFragment = new AddRecordDialog();
                addRecordFragment.show(getFragmentManager(), "AddRecordDialog");
                addRecordFragment.setTargetFragment(DictionaryTab.this, 1);
            }
       });
    }
    private void OnSettingsClick(){
        settingsFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsDialog settingsDialog = new SettingsDialog();
                settingsDialog.show(getFragmentManager(), "SettingsDialog");

            }
        });
    }
    private void OnBackupClick(){
        backupFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackupDialog backupDialog = new BackupDialog();
                backupDialog.show(getFragmentManager(), "BackupDialog");
                backupDialog.setTargetFragment(DictionaryTab.this, 1);
            }
        });
    }

    private void initializeElements(View view) {
        addRecordFabButton = (FloatingActionButton) view.findViewById(R.id.addRecordButton);
        backupFabButton = (FloatingActionButton) view.findViewById(R.id.backupButton);
        settingsFabButton = (FloatingActionButton) view.findViewById(R.id.settingsButton);
    }

    public void loadDatabase(){
        // Read from database and add to recycler view
        adapter.addRecordsFromDatabase();
        adapter.sortData();
    }


    private void handleFabButtons() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && (addRecordFabButton.isShown() && backupFabButton.isShown() && settingsFabButton.isShown())){
                    addRecordFabButton.hide();
                    backupFabButton.hide();
                    settingsFabButton.hide();
                }

                else if (dy < 0 && (!addRecordFabButton.isShown() && !backupFabButton.isShown() && !settingsFabButton.isShown())){
                    addRecordFabButton.show();
                    backupFabButton.show();
                    settingsFabButton.show();
                }
            }
        });

    }


    @Override
    public void refreshRecyclerView() {
        adapter.addLatestRecordFromDatabase();
        adapter.sortData();
    }


    public void updateItemData(int position, String stationTag, int distance, double amountOfFuel, double totalCost, String date, String description){
        adapter.updateRecord(position, stationTag, distance, amountOfFuel, totalCost, date, description);
        Toast.makeText(getContext(), "Zaktualizowano!", Toast.LENGTH_SHORT);
    }



    @Override
    public void refreshRecyclerViewAfterImport() {
        loadDatabase();
    }

}
