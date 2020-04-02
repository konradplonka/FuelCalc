package com.konradplonka.fuelcalculator.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.konradplonka.fuelcalculator.ListItem;
import com.konradplonka.fuelcalculator.MyAdapter;
import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.other.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class DictionaryTab extends Fragment implements AddRecordDialog.OnAddRecordDialogListener{
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

        addRecord();
        openSettings();

        return view;
    }

    private void addRecord() {
        addRecordFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecordDialog addRecordFragment = new AddRecordDialog();
                addRecordFragment.show(getFragmentManager(), "AddRecordDialog");
                addRecordFragment.setTargetFragment(DictionaryTab.this, 1);
            }
//                addRecordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                addRecordDialog.show();
//
//                final ImageView stationImageView = (ImageView) addRecordDialog.findViewById(R.id.station_addRecord_imageView);
//                final Button setStationButton = (Button) addRecordDialog.findViewById(R.id.set_station_button);
//                final EditText distanceEditText = (EditText)addRecordDialog.findViewById(R.id.distance_addRecord_editText);
//                final EditText amountOfFuelEditText = (EditText)addRecordDialog.findViewById(R.id.distance_addRecord_editText);
//                final EditText costEditText = (EditText)addRecordDialog.findViewById(R.id.cost_addRecord_editText);
//                final RadioButton totalCostRadioButton = (RadioButton) addRecordDialog.findViewById(R.id.total_cost_radioButton);
//                final RadioButton pricePerLRadioButton = (RadioButton) addRecordDialog.findViewById(R.id.price_per_l_radioButton);
//                final EditText dateEditText = (EditText) addRecordDialog.findViewById(R.id.date_addRecord_editText);
//                final EditText descriptionEditText = (EditText) addRecordDialog.findViewById(R.id.description_addRecord_editText);
//                ImageButton addRecordButton = (ImageButton) addRecordDialog.findViewById(R.id.add_record_button);
//
//                handleRadioButtons(addRecordDialog, view, costEditText, totalCostRadioButton, pricePerLRadioButton);
//                handleDateEditText(dateEditText);
//
//
//                setStationButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        setStation(stationImageView);
//                    }
//                });
//
//                addRecordButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        addRecordToList(stationImageView, distanceEditText, amountOfFuelEditText, costEditText, totalCostRadioButton, pricePerLRadioButton, dateEditText, descriptionEditText, adapter);
//                        adapter.addRecordsFromDatabase();
//                        addRecordDialog.dismiss();
//                    }
//                });
//
//            }
       });
    }
    private void openSettings(){
        settingsFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsDialog settingsDialog = new SettingsDialog();
                settingsDialog.show(getFragmentManager(), "SettingsDialog");
            }
        });
    }

    private void initializeElements(View view) {
        addRecordFabButton = (FloatingActionButton) view.findViewById(R.id.addRecordButton);
        backupFabButton = (FloatingActionButton) view.findViewById(R.id.backupButton);
        settingsFabButton = (FloatingActionButton) view.findViewById(R.id.settingsButton);
    }

    private void loadDatabase(){
        // Read from database and add to recycler view
        adapter.addRecordsFromDatabase();
        adapter.sortData();
    }


    private void handleFabButtons() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && (addRecordFabButton.isShown() && backupFabButton.isShown())){
                    addRecordFabButton.hide();
                    backupFabButton.hide();
                }

                else if (dy < 0 && (!addRecordFabButton.isShown() && !backupFabButton.isShown())){
                    addRecordFabButton.show();
                    backupFabButton.show();
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
}
