package com.konradplonka.fuelcalculator.fragments;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.konradplonka.fuelcalculator.fragments.dialogs.AddRecordDialog;
import com.konradplonka.fuelcalculator.fragments.dialogs.AddVehicleDialog;
import com.konradplonka.fuelcalculator.fragments.dialogs.BackupDialog;
import com.konradplonka.fuelcalculator.fragments.dialogs.ImportDialog;
import com.konradplonka.fuelcalculator.fragments.dialogs.SettingsDialog;
import com.konradplonka.fuelcalculator.other.VehicleListItem;
import com.konradplonka.fuelcalculator.other.ListItem;
import com.konradplonka.fuelcalculator.other.MyAdapter;
import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.other.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DictionaryTab extends Fragment implements AddRecordDialog.OnAddRecordDialogListener, ImportDialog.OnImportDialogListener, AddVehicleDialog.OnAddVehicleDialogListener {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayAdapter<String> vehicleAdapter;

    private RecyclerView.LayoutManager layoutManager;
    private List<ListItem> list;
    private List<VehicleListItem> vehiclesList;
    private List<String> vehicles;

    private DatabaseHelper db;
    private FloatingActionButton addRecordFabButton;
    private FloatingActionButton backupFabButton;
    private FloatingActionButton settingsFabButton;

    private Spinner spinner;
    private ImageButton vehicleMenu;
    private int selectedVehicleId;
    private int selectedVehiclePosition;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dictionary_tab, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<ListItem>();
        vehiclesList = new ArrayList<VehicleListItem>();
        vehicles = new ArrayList<String>();
        db = new DatabaseHelper(getContext());

        adapter = new MyAdapter(list, getContext(), getFragmentManager());
        recyclerView.setAdapter(adapter);



        selectedVehicleId = getSelectedVehicleIdStatePref();
        selectedVehiclePosition = getSelectedVehiclePositionStatePref();



        loadDatabase();
        initializeElements(view);
        initializeSpinner();

        handleSpinnerChange();


        handleFabButtons();

        OnAddRecordClick();
        OnSettingsClick();
        OnBackupClick();

        vehicleMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), vehicleMenu);
                popupMenu.inflate(R.menu.vehicle_options);


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                addVehicle();

                                break;
                            case R.id.menu2:
                                removeVehicle();
                                break;


                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });





        return view;
    }

    private void handleSpinnerChange() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    Toast.makeText(getContext(), String.valueOf(vehicles.get(position)), Toast.LENGTH_SHORT).show();

                    for(VehicleListItem vehicle: vehiclesList){
                        if(vehicle.toString().equals(vehicles.get(position))){
                            selectedVehicleId = vehicle.getId();
                            Log.e("ID: ", String.valueOf(selectedVehicleId));
                            saveSelectedVehicleIdStatePref(selectedVehicleId);
                            saveSelectedVehiclePositionStatePref(position);

                            adapter.notifyDataSetChanged();


                        }
                    }
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addVehicle() {
        AddVehicleDialog addVehicleDialog = new AddVehicleDialog();
        addVehicleDialog.show(getFragmentManager(), "AddVehicleDialog");
        addVehicleDialog.setTargetFragment(DictionaryTab.this, 1);
    }

    private void initializeSpinner() {
        loadSpinnerData();

        spinner.setSelection(selectedVehiclePosition);

    }

    private void OnAddRecordClick() {
        addRecordFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("vehicleId", selectedVehicleId);
                AddRecordDialog addRecordFragment = new AddRecordDialog();
                addRecordFragment.setArguments(bundle);
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
        spinner = (Spinner) view.findViewById(R.id.car_selector_spinner);
        vehicleMenu = (ImageButton) view.findViewById(R.id.vehicle_handle_menu_imageButton);
    }

    public void loadDatabase(){
        // Read vehicles from database

        loadVehicles();

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

    private void loadVehicles(){
        SQLiteCursor cursor = db.getVehicles();
        Log.e("Cursor count ", String.valueOf(cursor.getCount()));
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String brand = cursor.getString(1);
                String model = cursor.getString(2);
                Log.e("brand", brand);

                vehiclesList.add(new VehicleListItem(id, brand, model));
            }
        }
    }
    private void addLatestVehicle(){
        SQLiteCursor cursor = db.getVehicles();
        cursor.moveToLast();
        int id = cursor.getInt(0);
        String brand = cursor.getString(1);
        String model = cursor.getString(2);

        vehiclesList.add(new VehicleListItem(id, brand, model));
        vehicles.add(brand + " " + model);
    }
    private void loadSpinnerData(){
        vehicles.add("Wybierz pojazd");
        for(VehicleListItem vehicle: vehiclesList){
            vehicles.add(vehicle.toString());
        }
        vehicleAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_xml,vehicles);
        vehicleAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(vehicleAdapter);
        spinner.setBackgroundResource(R.drawable.card_bg);


    }
    private void removeVehicle(){

                final String text = spinner.getSelectedItem().toString();
                for(VehicleListItem vehicle: vehiclesList){
                    if(text.equals(vehicle.toString())){
                        vehicles.remove(vehicle.toString());
                        vehiclesList.remove(vehicle);
                        db.deleteVehicle(vehicle.getId());
                        adapter.notifyDataSetChanged();
                        spinner.setSelection(0);
                        break;
                    }
                }

    }


    private void saveSelectedVehicleIdStatePref(int selectedVehicleId){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedVehicleId", selectedVehicleId);
        editor.commit();
    }
    private void saveSelectedVehiclePositionStatePref(int position){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("position", position);
        editor.commit();
    }

    public int getSelectedVehicleIdStatePref(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPref", MODE_PRIVATE);
        int selectedVehicleId = sharedPreferences.getInt("selectedVehicleId", 0);

        return selectedVehicleId;
    }
    public int getSelectedVehiclePositionStatePref(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPref", MODE_PRIVATE);
        int position = sharedPreferences.getInt("position", 0);

        return position;
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

    @Override
    public void refreshSpinner() {
        addLatestVehicle();
        adapter.notifyDataSetChanged();
        spinner.setSelection(vehicles.size()-1);


    }
}
