package com.company;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class Booking {
    private LocalDate fromDate;
    private LocalDate toDate;
    private Account customer;
    private Room room;
    //private int price;
    private int bookingID;
    private static int bookingIdCount = 0;
    private static LocalDate today = LocalDate.now();

    public Booking (Account customer, Room room, LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)   ||   fromDate.isBefore(today)) {
            throw new IllegalArgumentException(
                    "Unable to book: Your arrival date must be today or day to come, and your departure date must be after your arrival date.");
        } else {
            this.fromDate = fromDate;
            this.toDate = toDate;
        }
        this.customer = customer;
        this.room = room;
        bookingID = bookingIdCount++;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public String getPeriod() {
        //Period diff = Period.between(fromdate, toDate);  //Gets period divided in yy,mm,dd
        long periodDays = ChronoUnit.DAYS.between(fromDate, toDate);  //ChronoUnit. To get time in only one unit, t.ex. days.
        return String.format("%d%s", periodDays, " days");
    }

    public String getTimeUntil() {
        Period diff2 = Period.between(today, fromDate);
        return String.format("%d%s%d%s%d%s", diff2.getYears()," years, ", diff2.getMonths(), " months and ", diff2.getDays(), " days.");
    }

    @Override
    public String toString(){
        return String.format("%-10s%-20s%n%-10s%d%n%-10s%s%s%s%n%-10s%s%n%-10s%s%n",
                "Customer: ", customer.getName(),
                "Room: ", room.getRoomNumber(),
                "Dates: ", fromDate, " to ", toDate,
                "Period: ", getPeriod(),
                "Time until: ", getTimeUntil());
    }
}
