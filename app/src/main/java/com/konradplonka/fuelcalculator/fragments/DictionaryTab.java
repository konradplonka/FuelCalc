package com.konradplonka.fuelcalculator.fragments;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.fragments.dialogs.AddRecordDialog;
import com.konradplonka.fuelcalculator.fragments.dialogs.AddVehicleDialog;
import com.konradplonka.fuelcalculator.fragments.dialogs.SettingsDialog;
import com.konradplonka.fuelcalculator.other.DatabaseHelper;
import com.konradplonka.fuelcalculator.other.ListItem;
import com.konradplonka.fuelcalculator.other.MyAdapter;
import com.konradplonka.fuelcalculator.other.VehicleListItem;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DictionaryTab extends Fragment implements AddVehicleDialog.OnAddVehicleDialogListener, AddRecordDialog.OnAddRecordDialogListener {

    private RecyclerView.LayoutManager layoutManager;
    private MyAdapter adapter;
    private ArrayAdapter<String> vehicleAdapter;
    private DatabaseHelper db;

    private List<ListItem> dataList;
    private List<VehicleListItem> vehiclesList;
    private List<String> vehiclesListString;

    private int selectedVehicleId;
    private int selectedVehiclePosition;

    private RecyclerView recyclerView;
    private Spinner spinner;
    private ImageButton vehicleMenu;
    private RelativeLayout vehicleSelectorRelativeLayout;
    private FloatingActionButton addRecordFabButton;
    private FloatingActionButton settingsFabButton;
    private FloatingActionButton backupFabButton;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dictionary_tab, container, false);

        selectedVehicleId = getSelectedVehicleIdStatePref();
        selectedVehiclePosition = getSelectedVehiclePositionStatePref();
        db = new DatabaseHelper(getContext());

        initializeViewElements(view);
        initializeLists();
        handleRecyclerView();
        handleSpinner();

        onAddRecordClick();
        onSettingsClick();
        handleFabButtons();

        return view;
    }

    private void onAddRecordClick() {
        addRecordFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vehiclesListString.size() >1){
                    Bundle bundle = new Bundle();
                    bundle.putInt("vehicleId", selectedVehicleId);
                    AddRecordDialog addRecordFragment = new AddRecordDialog();
                    addRecordFragment.setArguments(bundle);
                    addRecordFragment.show(getFragmentManager(), "AddRecordDialog");
                    addRecordFragment.setTargetFragment(DictionaryTab.this, 1);
                }
                else Toast.makeText(getContext(), "Aby dodać rekord musisz utworzyć profil samochodu!", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void onSettingsClick(){
        settingsFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsDialog settingsDialog = new SettingsDialog();
                settingsDialog.show(getFragmentManager(), "SettingsDialog");
            }
        });


    }


    private void initializeViewElements(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        spinner = view.findViewById(R.id.car_selector_spinner);
        vehicleMenu = view.findViewById(R.id.vehicle_handle_menu_imageButton);
        addRecordFabButton = view.findViewById(R.id.addRecordButton);
        settingsFabButton = view.findViewById(R.id.settingsButton);
        backupFabButton = view.findViewById(R.id.backupButton);
        vehicleSelectorRelativeLayout = view.findViewById(R.id.vehicle_selector_spinner_RelativeLayout);
    }

    private void initializeLists() {
        dataList = new ArrayList<ListItem>();
        vehiclesList = new ArrayList<VehicleListItem>();
        vehiclesListString = new ArrayList<String>();
    }

    private void handleRecyclerView() {
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyAdapter(dataList, getContext(), getFragmentManager());
        recyclerView.setAdapter(adapter);

        loadDatabase();
    }
    private void handleSpinner() {
        onVehicleMenuClick();
        getSpinnerVehiclesList();

        vehicleAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_xml, vehiclesListString);
        vehicleAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(vehicleAdapter);
        spinner.setBackgroundResource(R.drawable.card_bg);
        spinner.setSelection(selectedVehiclePosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    selectedVehiclePosition = position;
                    for(VehicleListItem vehicle: vehiclesList){
                        if(vehicle.toString().equals(vehiclesListString.get(position))){
                            selectedVehicleId = vehicle.getId();
                            break;
                        }

                    }
                    saveSelectedVehicleStatePref(selectedVehicleId, selectedVehiclePosition);
                    adapter.filterDataByVehicleId(selectedVehicleId);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onVehicleMenuClick() {
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
                                spinner.setSelection(0);
                                break;


                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });
    }



    private void addVehicle() {
        AddVehicleDialog addVehicleDialog = new AddVehicleDialog();
        addVehicleDialog.show(getFragmentManager(), "AddVehicleDialog");
        addVehicleDialog.setTargetFragment(DictionaryTab.this, 1);
    }

    private void removeVehicle() {
        String selectedItem = spinner.getSelectedItem().toString();
        for(VehicleListItem vehicle: vehiclesList){
            if(selectedItem.equals(vehicle.toString())){
                if(db.deleteVehicle(vehicle.getId()) &&  db.deleteSpecifyVehicleData(vehicle.getId())){
                    saveSelectedVehicleStatePref(0,0);
                    vehiclesListString.remove(selectedItem);
                    vehiclesList.remove(vehicle);
                    adapter.clearList();
                    adapter.notifyDataSetChanged();

                    Toast.makeText(getContext(), "Usunięto pojazd!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getContext(), "Błąd w usuwaniu pojazdu!", Toast.LENGTH_SHORT).show();



                break;
            }
        }
    }

    private void getSpinnerVehiclesList() {
        vehiclesListString.add("Wybierz pojazd");
        for(VehicleListItem vehicle: vehiclesList){
            vehiclesListString.add(vehicle.toString());
        }
    }


    public void loadDatabase(){
        loadVehiclesDatabase();
        adapter.addRecordsFromDatabase();
        adapter.sortData();
    }

    private void loadVehiclesDatabase(){
        SQLiteCursor cursor = db.getVehicles();
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


    @Override
    public void refreshSpinner() {
        SQLiteCursor cursor = db.getVehicles();
        cursor.moveToLast();
        int id = cursor.getInt(0);
        String brand = cursor.getString(1);
        String model = cursor.getString(2);

        vehiclesList.add(new VehicleListItem(id, brand, model));
        vehiclesListString.clear();
        getSpinnerVehiclesList();

        spinner.setSelection(vehiclesListString.size()-1);
    }

    @Override
    public void refreshRecyclerView() {
        adapter.addLatestRecordFromDatabase();
        adapter.filterDataByVehicleId(selectedVehicleId);
        adapter.sortData();
    }

    private void saveSelectedVehicleStatePref(int selectedVehicleId, int selectedVehiclePosition){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedVehicleId", selectedVehicleId);
        editor.putInt("selectedVehiclePosition", selectedVehiclePosition);
        editor.commit();
    }

    public int getSelectedVehicleIdStatePref(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPref", MODE_PRIVATE);
        int selectedVehicleId = sharedPreferences.getInt("selectedVehicleId", 0);

        return selectedVehicleId;
    }

    public int getSelectedVehiclePositionStatePref(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPref", MODE_PRIVATE);
        int selectedVehiclePosition = sharedPreferences.getInt("selectedVehiclePosition", 0);

        return selectedVehiclePosition;
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

    public void updateItemData(int position, String stationTag, int distance, double amountOfFuel, double totalCost, String date, String description){
        adapter.updateRecord(position, stationTag, distance, amountOfFuel, totalCost, date, description);
    }
}