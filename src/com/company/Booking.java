package com.company;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class Booking {
    private LocalDate fromDate;
    private LocalDate toDate;
    //private Account customer;
    private Room room;
    private double price;
    //private boolean lastMinute;
    //private boolean confirmedBooking;  //To separate a confirmed booking from suggested bookings from search.
    //private int bookingID;
    //private static int bookingIdCount = 0;
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

   /* public Account getCustomer(){
        return customer;
    }*/

    public Room getRoom() {
        return room;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice (double price) {
        this.price = price;
    }

    /*public int getBookingID() {
        return bookingID;
    }*/

    public String getPeriod() {
        //Period diff = Period.between(fromdate, toDate);  //Gets period divided in yy,mm,dd
        long periodDays = ChronoUnit.DAYS.between(fromDate, toDate);  //ChronoUnit. To get time in only one unit, t.ex. days.
        return String.format("%d%s", periodDays, " days");
    }

    public String getTimeUntil() {
        Period diff2 = Period.between(today, fromDate);
        return String.format("%d%s%d%s%d%s", diff2.getYears(), " years, ", diff2.getMonths(), " months and ", diff2.getDays(), " days.");
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
