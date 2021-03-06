package com.konradplonka.fuelcalculator.other;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.computations.FuelConsumptionComputations;
import com.konradplonka.fuelcalculator.fragments.DictionaryTab;
import com.konradplonka.fuelcalculator.fragments.dialogs.EditRecordDialog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    public List<ListItem> getListItems() {
        return listItems;
    }

    private List<ListItem> listItems;
    private List<ListItem> listItemsFull;
    private Context context;
    private FragmentManager fragmentManager;
    private OnMyAdapterListener listener;
    private boolean fuelConsumptionCalc;




    public MyAdapter(List<ListItem> listItems , Context context, FragmentManager fragmentManager, OnMyAdapterListener listener) {
        this.listItems = listItems;
        this.listItemsFull = listItems;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
        fuelConsumptionCalc = getFuelConsumptionCalcStatePref();


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView dateTextView;
        public TextView distanceTextView;
        public TextView totalCostTextView;
        public TextView costPerLTextView;
        public TextView amountOfFuelTextView;
        public TextView descriptionTextView;
        public ImageView petrolStationImgImageView;
        public RelativeLayout itemContainerRelativeLayout;
        public ImageButton optionsImageButton;
        public TextView distanceIncreaseTextView;
        public TextView fuelConsumptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = (TextView) itemView.findViewById(R.id.date);
            distanceTextView = (TextView) itemView.findViewById(R.id.distance);
            totalCostTextView = (TextView) itemView.findViewById(R.id.total_cost);
            costPerLTextView = (TextView) itemView.findViewById(R.id.cost_per_liter);
            amountOfFuelTextView = (TextView) itemView.findViewById(R.id.amount_of_fuel);
            descriptionTextView = (TextView) itemView.findViewById(R.id.description);
            petrolStationImgImageView = (ImageView) itemView.findViewById(R.id.petrol_station_image);
            itemContainerRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_container_relativeLayout);
            optionsImageButton = (ImageButton) itemView.findViewById(R.id.options_imageButton);
            distanceIncreaseTextView = (TextView) itemView.findViewById(R.id.distance_increase);
            fuelConsumptionTextView = (TextView) itemView.findViewById(R.id.fuel_consumption);
        }
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);
        DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        df.setMaximumFractionDigits(2);

        holder.petrolStationImgImageView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.itemContainerRelativeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        holder.dateTextView.setText(listItem.getDate());
        holder.distanceTextView.setText(formatDistance(String.valueOf(listItem.getDistance())) + " km");
        holder.totalCostTextView.setText("-" + df.format(listItem.getTotalCost()) + " zł");
        holder.costPerLTextView.setText(df.format(listItem.getTotalCost()/listItem.getAmountOfFuel()) + " zł/l");
        holder.amountOfFuelTextView.setText("+" + df.format(listItem.getAmountOfFuel()) + " l");
        if(getItemCount() > 1 && position != listItems.size() - 1){
            Log.e("items", String.valueOf(getItemCount()));
            int distance = listItem.getDistance() - listItems.get(position + 1).getDistance();
            double amountOfFuel = listItems.get(position + 1).getAmountOfFuel();
            holder.distanceIncreaseTextView.setVisibility(View.VISIBLE);
            holder.distanceIncreaseTextView.setText("+" + distance + " km");

            if(fuelConsumptionCalc){
                holder.fuelConsumptionTextView.setVisibility(View.VISIBLE);
                df.setMaximumFractionDigits(1);
                holder.fuelConsumptionTextView.setText(df.format(new FuelConsumptionComputations(distance, amountOfFuel).calculateAverageFuelConsumpion()) + "l/100 km");
            }

        }
        else{
            holder.distanceIncreaseTextView.setVisibility(View.GONE);
            holder.fuelConsumptionTextView.setVisibility(View.GONE);
        }


        if(listItem.getDescription().length() == 0){
            holder.descriptionTextView.setVisibility(View.GONE);
        }
        else {
            holder.descriptionTextView.setVisibility(View.VISIBLE);
            holder.descriptionTextView.setText(listItem.getDescription());
        }

        ArrayList<PetrolStation> petrolStationsList = PetrolStation.getPetrolStationList();
        for(PetrolStation petrolStation: petrolStationsList){
            if(petrolStation.getPetrolStations().equals(listItem.getPetrolStations())){
                holder.petrolStationImgImageView.setImageResource(petrolStation.getImageSourceResource());
                holder.petrolStationImgImageView.setBackgroundResource(petrolStation.getLayoutBackgroundResource());
            }
        }
        handleOptionsMenuButton(holder, listItem);


}

    private void handleOptionsMenuButton(final ViewHolder holder, final ListItem listItem) {
        holder.optionsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.optionsImageButton);
                popupMenu.inflate(R.menu.item_options);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                DatabaseHelper db = new DatabaseHelper(context);
                                if(db.deleteData(listItem.getId())){
                                    deleteRecord(listItem);
                                    Toast.makeText(context, "Usunięto!", Toast.LENGTH_SHORT);
                                    listener.setVisibleAddButton();
                                    listener.handleVisibilityOfEmptyListMessage();
                                }


                                break;
                            case R.id.menu2:
                                Bundle bundle = new Bundle();

                                bundle.putInt("position", listItems.indexOf(listItem));
                                bundle.putInt("id", listItem.getId());
                                bundle.putString("petrolStation", listItem.getPetrolStations().toString());
                                bundle.putInt("distance", listItem.getDistance());
                                bundle.putDouble("amountOfFuel", listItem.getAmountOfFuel());
                                bundle.putDouble("totalCost", listItem.getTotalCost());
                                bundle.putString("date", listItem.getDate());
                                bundle.putString("description", holder.descriptionTextView.getText().toString());
                                EditRecordDialog editRecordDialog = new EditRecordDialog();
                                editRecordDialog.setArguments(bundle);
                                editRecordDialog.show(fragmentManager,"EditRecord");



                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    public void addRecord(ListItem listItem){
        listItemsFull.add(listItem);
        notifyDataSetChanged();


    }
    private void deleteRecord(ListItem listItem){
       int position = listItems.indexOf(listItem);
       listItems.remove(position);
       listItemsFull.remove(listItem);
       notifyItemRemoved(position);
    }
    public void updateRecord(int position, String stationTag, int distance, double amountOfFuel, double totalCost, String date, String description){
        ListItem listItem = listItems.get(position);

        if(!listItem.getPetrolStations().toString().equals(stationTag)){
            listItem.setPetrolStations(PetrolStations.valueOf(stationTag));
        }
        if(listItem.getDistance() != distance){
            listItem.setDistance(distance);
        }
        if(listItem.getAmountOfFuel() != amountOfFuel){
            listItem.setAmountOfFuel(amountOfFuel);
        }
        if(listItem.getTotalCost() != totalCost){
            listItem.setTotalCost(totalCost);
        }
        if(!listItem.getDate().equals(date))
        {
            listItem.setDate(date);
            sortData();
        }
        if(!listItem.getDescription().equals(description)){
            listItem.setDescription(description);
        }


        notifyItemChanged(position);


    }

    public void addRecordsFromDatabase(){
        listItems.clear();
        DatabaseHelper db = new DatabaseHelper(context);
        SQLiteCursor cursor = db.getData();
        Log.e("Cursor count ", String.valueOf(cursor.getCount()));
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                int vehicleId = cursor.getInt(1);
                PetrolStations petrolStations = PetrolStations.valueOf(cursor.getString(2));
                int distance = cursor.getInt(3);
                double amountOfFuel = cursor.getDouble(4);
                double totalCost = cursor.getDouble(5);
                String date = cursor.getString(6);
                String description = cursor.getString(7);
                Log.e("DATE: ", date);

                addRecord(new ListItem(id, vehicleId, petrolStations, distance, amountOfFuel, totalCost, date, description));
            }
        }
        sortData();


    }


    public void addLatestRecordFromDatabase(){
        DatabaseHelper db = new DatabaseHelper(context);
        SQLiteCursor cursor = db.getData();
        cursor.moveToLast();

        int id = cursor.getInt(0);
        int vehicleId = cursor.getInt(1);
        PetrolStations petrolStations = PetrolStations.valueOf(cursor.getString(2));
        int distance = cursor.getInt(3);
        double amountOfFuel = cursor.getDouble(4);
        double totalCost = cursor.getDouble(5);
        String date = cursor.getString(6);
        String description = cursor.getString(7);

        addRecord(new ListItem(id, vehicleId, petrolStations, distance, amountOfFuel, totalCost, date, description));
        notifyItemInserted(getItemCount()+1);

    }

    public void filterDataByVehicleId(int vehicleId){
        Log.e("Filtrowanie", String.valueOf(vehicleId));
        Log.e("Filtrowanie", String.valueOf(listItemsFull.size()));
        List<ListItem> listItemsFiltered = new ArrayList<>();
            for(ListItem item: listItemsFull){
                if(item.getVehicleId() == vehicleId){
                    listItemsFiltered.add(item);
                    Log.e("VehicleId: ", String.valueOf(item.getVehicleId()));
                }



        }
            listItems = listItemsFiltered;
            sortData();


    }

    public void clearList(){
        for(ListItem item: listItems){
            listItemsFull.remove(item);
        }
        listItems.clear();
    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void sortData(){
        Collections.sort(listItems, ListItem.BY_DATE_DESCENDING);
        notifyDataSetChanged();
    }
    private String formatDistance(String distance){
        StringBuilder distanceReverse = new StringBuilder(distance).reverse();
        distance = distanceReverse.toString();
        StringBuilder formattedDistance = new StringBuilder();
        for(int i = 0; i < distance.length(); i++) {
            formattedDistance.append(distance.charAt(i));
            if ((i + 1) % 3 == 0) {
                formattedDistance.append(" ");
            }
        }

        return formattedDistance.reverse().toString();
    }

    public boolean getFuelConsumptionCalcStatePref(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPref", MODE_PRIVATE);
        boolean isDark = sharedPreferences.getBoolean("fuelConsumptionCalc", true);

        return isDark;
    }

    public interface OnMyAdapterListener{
        void setVisibleAddButton();
        void handleVisibilityOfEmptyListMessage();
    }





}
