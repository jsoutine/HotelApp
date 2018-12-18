package com.company;

import java.util.ArrayList;

public class Room {
    private int roomNumber;
    private static int roomCounter;
    private int beds;
    private int standard;
    private ArrayList<BookingConfirm> roomBookingList = new ArrayList<>();
    //private int pricePerNight; Use enums for standard??

    public Room(int beds, int standard) {
        if(beds == 1 || beds == 2 || beds == 4) {
            this.beds = beds;
        }else{
            throw new IllegalArgumentException(
                    "Number of beds in a room can only be 1, 2 or 4.");
        }
        if (standard > 0 && standard < 6) {
            this.standard = standard;
        }else {
            throw new IllegalArgumentException(
                "Standards only rank 1-5.");
        }
        roomNumber = ++roomCounter;
    }

    public ArrayList<BookingConfirm> getRoomBookingList(){
        return roomBookingList;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getBeds() {
        return beds;
    }

    public int getStandard() {
        return standard;
    }


    public void setBeds (int beds) {
        if(beds == 1 || beds == 2 || beds == 4) {
            this.beds = beds;
        }else{
            throw new IllegalArgumentException(
                    "Number of beds in a room can only be 1, 2 or 4.");
        }
    }

    public void setStandard (int standard) {
        if (standard > 0 && standard < 6) {
            this.standard = standard;
        } else {
            throw new IllegalArgumentException(
                    "Standards only rank 1-5.");
        }
    }

    @Override
    public String toString(){
        return String.format ("%s%-4d%s%-2d%s%-2d", "Room number: ", roomNumber, "Beds: ", beds, "Standard: ", standard);
    }
}