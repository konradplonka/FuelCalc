package com.konradplonka.fuelcalculator;

import java.util.ArrayList;

public class PetrolStation {
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

        petrolStationList.add(new PetrolStation(PetrolStations.UNKNOWN, R.drawable.oval_unknown, R.drawable.unknown));
        petrolStationList.add(new PetrolStation(PetrolStations.ORLEN, R.drawable.oval_orlen, R.drawable.orlen));
        petrolStationList.add(new PetrolStation(PetrolStations.SHELL, R.drawable.oval_shell, R.drawable.shell));
        petrolStationList.add(new PetrolStation(PetrolStations.BP, R.drawable.oval_bp, R.drawable.bp));
        return petrolStationList;
    }
}
