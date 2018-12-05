package com.company;

import java.time.LocalDate;

public class BookingConfirm extends Booking {
    private AccountCustomer customer;
    private int bookingID;
    private static int bookingIdCount = 0;

    public BookingConfirm (Room room, LocalDate fromDate, LocalDate toDate, AccountCustomer customer, double price) {
        super(room, fromDate, toDate, price);
        this.customer = customer;
        bookingID = ++bookingIdCount;
    }

    public int getBookingID() {
        return bookingID;
    }

    public AccountCustomer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return String.format("%s%15s%-4d%s%-25s%s%s", super.toString(), "Booking ID: ", bookingID, "Customer name: ", customer.getName(), "Customer ID: ", customer.getAccountID()) ;
    }
}
