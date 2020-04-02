package com.konradplonka.fuelcalculator.computations;

public class RangeComputations {
    private double fuelConsumption;
    private double fuelPrice;
    private double amountOfFuel;

    public RangeComputations(double fuelConsumption, double fuelPrice, double amountOfFuel) {
        this.fuelConsumption = fuelConsumption;
        this.fuelPrice = fuelPrice;
        this.amountOfFuel = amountOfFuel;
    }

    public double calculateRange(){
        return (amountOfFuel / fuelConsumption) * 100;
    }

    public double calculateTotalCost(){
        return amountOfFuel * fuelPrice;
    }

}
