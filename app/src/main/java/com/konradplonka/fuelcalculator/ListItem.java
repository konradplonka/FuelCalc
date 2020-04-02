package com.konradplonka.fuelcalculator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class ListItem {
    private int id;
    private PetrolStations petrolStations;
    private int distance;
    private double amountOfFuel;
    private double totalCost;
    private String date;
    private String description;

    public ListItem(int id, PetrolStations petrolStations, int distance, double amountOfFuel, double totalCost, String date, String description) {
        this.id = id;
        this.petrolStations = petrolStations;
        this.distance = distance;
        this.amountOfFuel = amountOfFuel;
        this.totalCost = totalCost;
        this.date = date;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PetrolStations getPetrolStations() {
        return petrolStations;
    }

    public void setPetrolStations(PetrolStations petrolStations) {
        this.petrolStations = petrolStations;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getAmountOfFuel() {
        return amountOfFuel;
    }

    public void setAmountOfFuel(double amountOfFuel) {
        this.amountOfFuel = amountOfFuel;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final Comparator<ListItem> BY_DATE_DESCENDING = new Comparator<ListItem>() {
        String dateFormat = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        @Override
        public int compare(ListItem o1, ListItem o2) {
            Date date1 = null;
            Date date2 = null;
            try {
                date1 = simpleDateFormat.parse(o1.getDate());
                date2 = simpleDateFormat.parse(o2.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date2.compareTo(date1);
        }
    };
}
