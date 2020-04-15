package com.konradplonka.fuelcalculator.other;

import androidx.annotation.NonNull;

public class VehicleListItem {
    private int id;
    private String brand;
    private String model;

    public VehicleListItem(int id, String brand, String model) {
        this.id = id;
        this.brand = brand;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s %s", brand, model);
    }
}
