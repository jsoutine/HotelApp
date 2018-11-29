package com.company;

import java.util.ArrayList;

public class Room {
    private int roomNumber;
    private static int roomCounter;
    private int beds;
    private int standard;
    private ArrayList<Booking> roomBookingList = new ArrayList<>();
    //private int pricePerNight; Use enums for standard??

    public Room(int beds, int standard) {
        if (beds <= 0 || beds > 8) {
            throw new IllegalArgumentException(
                    "Number of beds in a room can only be 1-8.");
        }else {
            this.beds = beds;
        }
        if (standard < 1 || standard > 5) {
            throw new IllegalArgumentException(
                    "Standards only rank 1-5.");
        }else {
            this.standard = standard;
        }
        roomNumber = ++roomCounter;
    }

    public ArrayList<Booking> getRoomBookingList(){
        return roomBookingList;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    @Override
    public String toString(){
        return String.format ("%s%-4d%s%-2d%s%-2d", "Room number: ", roomNumber, "Beds: ", beds, "Standard: ", standard);
    }

}
