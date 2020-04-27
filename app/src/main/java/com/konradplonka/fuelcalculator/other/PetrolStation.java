package com.konradplonka.fuelcalculator.other;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import com.konradplonka.fuelcalculator.R;

import java.util.ArrayList;

public class PetrolStation{
    private PetrolStations petrolStations;
    private int layoutBackgroundResource;
    private int imageSourceResource;


    public PetrolStation(PetrolStations petrolStations, int layoutBackgroundResource, int imageSourceResource) {
        this.petrolStations = petrolStations;
        this.layoutBackgroundResource = layoutBackgroundResource;
        this.imageSourceResource = imageSourceResource;
    }

    public PetrolStations getPetrolStations() {
        return petrolStations;
    }

    public void setPetrolStations(PetrolStations petrolStations) {
        this.petrolStations = petrolStations;
    }

    public int getLayoutBackgroundResource() {
        return layoutBackgroundResource;
    }

    public void setLayoutBackgroundResource(int layoutBackgroundResource) {
        this.layoutBackgroundResource = layoutBackgroundResource;
    }

    public int getImageSourceResource() {
        return imageSourceResource;
    }

    public void setImageSourceResource(int imageSourceResource) {
        this.imageSourceResource = imageSourceResource;
    }

    public static ArrayList getPetrolStationList(){
        ArrayList petrolStationList = new ArrayList();

        petrolStationList.add(new PetrolStation(PetrolStations.UNKNOWN, R.drawable.oval_unknown, R.drawable.ic_unknown));
        petrolStationList.add(new PetrolStation(PetrolStations.AMIC, R.drawable.oval_station_bg, R.drawable.ic_amic));
        petrolStationList.add(new PetrolStation(PetrolStations.AUCHAN, R.drawable.oval_auchan, R.drawable.ic_auchan));
        petrolStationList.add(new PetrolStation(PetrolStations.BP, R.drawable.oval_bp, R.drawable.ic_bp));
        petrolStationList.add(new PetrolStation(PetrolStations.CIRCLEK, R.drawable.oval_circlek, R.drawable.ic_circlek));
        petrolStationList.add(new PetrolStation(PetrolStations.LOTOS, R.drawable.oval_lotos, R.drawable.ic_lotos));
        petrolStationList.add(new PetrolStation(PetrolStations.MOYA, R.drawable.oval_moya, R.drawable.ic_moya));
        petrolStationList.add(new PetrolStation(PetrolStations.ORLEN, R.drawable.oval_orlen, R.drawable.ic_orlen));
        petrolStationList.add(new PetrolStation(PetrolStations.SHELL, R.drawable.oval_shell, R.drawable.ic_shell));
        return petrolStationList;
    }
}
