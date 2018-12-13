package com.company;

public class BedPrice {

    private double constant;
    private int numberOfBeds;

    public BedPrice(int numberOfBeds, double constant) {
        if (constant >= 1) {
            this.constant = constant;
        } else {
            throw new IllegalArgumentException (
            "constant value must me atleast one");
        }
        this.numberOfBeds = numberOfBeds;
    }

    public double getConstant() {
        return constant;
    }

    public void setConstant(double constant) {
        this.constant = constant;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }
}

