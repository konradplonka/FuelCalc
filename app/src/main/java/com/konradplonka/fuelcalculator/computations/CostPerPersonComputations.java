package com.konradplonka.fuelcalculator.computations;

public class CostPerPersonComputations {
    private double distance;
    private double fuelPrice;
    private double fuelConsumption;
    private double numberOfPeople;

    public CostPerPersonComputations(double distance, double fuelPrice, double fuelConsumption, double numberOfPeople) {
        this.distance = distance;
        this.fuelPrice = fuelPrice;
        this.fuelConsumption = fuelConsumption;
        this.numberOfPeople = numberOfPeople;
    }

    public double calculatePricePerPerson() {
        return  ((fuelConsumption * distance) / 100) * fuelPrice / numberOfPeople;
    }

    public double calculateTotalCost(){
        return ((fuelConsumption * distance) / 100) * fuelPrice;
    }
    public double calculateUsedFuel(){
        return (fuelConsumption * distance) / 100;
    }
}

