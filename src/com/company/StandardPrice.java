package com.company;

import java.io.Serializable;

public class StandardPrice implements Serializable {

    private double price;
    private int name;

    public StandardPrice(int name,double price) {
        if (price > 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException(
                    "price must be over zero");
        }
        this.name = name;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getName() {
        return name;
    }
}