package com.company;

import java.io.Serializable;
import java.time.LocalDate;

public class BookingConfirm extends Booking implements Serializable {
    private AccountCustomer customer;
    private int bookingID;
    private int uniqueID;  //The unique room bookingID, not dependent on bookingID
    private static int uniqueIDcounter;
    private boolean checkedIn;
    private boolean checkedOut;

    public BookingConfirm (Room room, LocalDate fromDate, LocalDate toDate, double price, int bookingID, int uniqueID, AccountCustomer customer) {
        super(room, fromDate, toDate, price);
        this.customer = customer;
        this.bookingID = bookingID;
        this.uniqueID = uniqueID;
        checkedIn = false;
        checkedOut = false;
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

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    @Override
    public String toString() {
        return String.format("%s%15s%-4d%s%-5s%s%-25s", super.toString(), "Booking ID: ", bookingID, "Customer ID: ", customer.getAccountID(), "Customer name: ", customer.getName()) ;
    }
}
