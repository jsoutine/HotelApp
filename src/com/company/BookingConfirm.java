package com.company;

import java.time.LocalDate;

public class BookingConfirm extends Booking {
    private AccountCustomer customer;
    private int bookingID;
    private static int bookingIdCount = 0;
    private boolean sameBookingID;
    private int uniqueID;  //The unique room bookingID, not dependent on bookingID
    private int uniqueIDcounter;

    public BookingConfirm (Room room, LocalDate fromDate, LocalDate toDate, AccountCustomer customer, double price, boolean sameBookingID) {
        super(room, fromDate, toDate, price);
        this.customer = customer;
        if (!sameBookingID) {    //The first room in a booking has sameBookingID = false.
            bookingID = ++bookingIdCount;
        }else {
            bookingID = bookingIdCount;
        }
        uniqueID = ++uniqueIDcounter;
    }

    public int getBookingID() {
        return bookingID;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public AccountCustomer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return String.format("%s%15s%-4d%s%-25s%s%s", super.toString(), "Booking ID: ", bookingID, "Customer name: ", customer.getName(), "Customer ID: ", customer.getAccountID()) ;
    }
}
