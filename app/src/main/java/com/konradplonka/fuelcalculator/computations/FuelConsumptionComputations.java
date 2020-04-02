package com.konradplonka.fuelcalculator.computations;

public class FuelConsumptionComputations {
    private double distance;
    private double fuelPrice;
    private double amountOfFuel;

    public FuelConsumptionComputations(double distance, double fuelPrice, double amountOfFuel){
        this.distance = distance;
        this.fuelPrice = fuelPrice;
        this.amountOfFuel = amountOfFuel;
    }

    public double calculateAverageFuelConsumpion(){
        return (amountOfFuel / distance) * 100;
    }

    public double calculateTotalCost(){
        return fuelPrice * amountOfFuel;
    }
}
