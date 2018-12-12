package com.company;

import java.time.LocalDate;

public class BookingSearch extends Booking {
    boolean lastMinute;
    boolean added;

    public BookingSearch (Room room, LocalDate fromDate, LocalDate toDate, double price) {
        super(room, fromDate, toDate, price);
        lastMinute = false;
        added = false;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public boolean isLastMinute() {
        return lastMinute;
    }

    public void setLastMinute(boolean lastMinute) {
        this.lastMinute = lastMinute;
    }

    @Override
    public String toString() {
        return String.format("%s%s%s", super.toString(), ((lastMinute) ? " (Last minute price!)" : ""), ((added) ? "ADDED" : ""));
    }
}
