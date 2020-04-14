package com.konradplonka.fuelcalculator.other;


import android.content.Context;
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
import com.konradplonka.fuelcalculator.fragments.dialogs.EditRecordDialog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private List<ListItem> listItems;
    private Context context;
    private FragmentManager fragmentManager;




    public MyAdapter(List<ListItem> listItems, Context context, FragmentManager fragmentManager) {
        this.listItems = listItems;
        this.context = context;
        this.fragmentManager = fragmentManager;


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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = (TextView) itemView.findViewById(R.id.date_item_textView);
            distanceTextView = (TextView) itemView.findViewById(R.id.distance_item_textView);
            totalCostTextView = (TextView) itemView.findViewById(R.id.total_cost_item_textView);
            costPerLTextView = (TextView) itemView.findViewById(R.id.cost_per_l_item_textView);
            amountOfFuelTextView = (TextView) itemView.findViewById(R.id.amount_of_fuel_item_textView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.description_item_textView);
            petrolStationImgImageView = (ImageView) itemView.findViewById(R.id.petrol_station_image);
            itemContainerRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_container_relativeLayout);
            optionsImageButton = (ImageButton) itemView.findViewById(R.id.options_imageButton);
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
        holder.distanceTextView.setText(listItem.getDistance() + " km");
        holder.totalCostTextView.setText(df.format(listItem.getTotalCost()) + " zł");
        holder.costPerLTextView.setText(df.format(listItem.getTotalCost()/listItem.getAmountOfFuel()) + " zł");
        holder.amountOfFuelTextView.setText(df.format(listItem.getAmountOfFuel()) + " l");

        if(listItem.getDescription().length() == 0){
            holder.descriptionTextView.setVisibility(View.GONE);
        }
        else holder.descriptionTextView.setText(listItem.getDescription());

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
                popupMenu.inflate(R.menu.options_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                DatabaseHelper db = new DatabaseHelper(context);
                                if(db.deleteData(listItem.getId())){
                                    deleteRecord(listItem);
                                    Toast.makeText(context, "Usunięto!", Toast.LENGTH_SHORT);
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
        listItems.add(listItem);
        notifyDataSetChanged();


    }
    private void deleteRecord(ListItem listItem){
       int position = listItems.indexOf(listItem);
       listItems.remove(position);
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
                PetrolStations petrolStations = PetrolStations.valueOf(cursor.getString(1));
                int distance = cursor.getInt(2);
                double amountOfFuel = cursor.getDouble(3);
                double totalCost = cursor.getDouble(4);
                String date = cursor.getString(5);
                String description = cursor.getString(6);
                Log.e("DATE: ", date);

                addRecord(new ListItem(id, petrolStations, distance, amountOfFuel, totalCost, date, description));
            }
        }
        sortData();
    }

    public void addLatestRecordFromDatabase(){
        DatabaseHelper db = new DatabaseHelper(context);
        SQLiteCursor cursor = db.getData();
        cursor.moveToLast();

        int id = cursor.getInt(0);
        PetrolStations petrolStations = PetrolStations.valueOf(cursor.getString(1));
        int distance = cursor.getInt(2);
        double amountOfFuel = cursor.getDouble(3);
        double totalCost = cursor.getDouble(4);
        String date = cursor.getString(5);
        String description = cursor.getString(6);

        addRecord(new ListItem(id, petrolStations, distance, amountOfFuel, totalCost, date, description));
        notifyItemInserted(getItemCount()+1);

    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void sortData(){
        Collections.sort(listItems, ListItem.BY_DATE_DESCENDING);
        notifyDataSetChanged();
    }




}
