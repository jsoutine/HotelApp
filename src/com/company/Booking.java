package com.company;

import java.time.LocalDate;

public class Booking {
    private LocalDate fromDate;
    private LocalDate toDate;
    private Room room;
    private double price;
    private static LocalDate today = LocalDate.now();

    public Booking(Room room, LocalDate fromDate, LocalDate toDate, double price) {
        if (fromDate.isAfter(toDate) || fromDate.isBefore(today)) {
            throw new IllegalArgumentException(
                    "Unable to book: Your arrival date must be today or day to come, and your departure date must be after your arrival date.");
        }else {
            this.fromDate = fromDate;
            this.toDate = toDate;
        }
        this.room =room;
        this.price =price;
}

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate (LocalDate toDate) {   //Used when checking out earlier, where new toDate is set to today.
            this.toDate = toDate;
    }

    public Room getRoom() {
        return room;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice (double price) {
        this.price = price;
    }

    public String getDates() {
        StringBuilder dates = new StringBuilder().append(fromDate).append(" to ").append(toDate); //String concatenation using StringBuilder
        return String.format("%s", dates);
    }

    @Override
    public String toString() {                                       //Integer.toString(countElements2).concat(".")
        String priceString = String.format("%.02f%s", price, " SEK ");
        return String.format("%-28s%s%-4d%s%-4d%s%-4d%s%13s", getDates(), "Room: ", room.getRoomNumber(), "Beds: ", room.getBeds(), "Standard: ", room.getStandard(), "Price: ", priceString);
    }
}
