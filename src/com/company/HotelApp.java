package com.company;

import java.awt.print.Book;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class HotelApp {
    private Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        HotelApp myApp = new HotelApp();
        HotelLogistics logistics = new HotelLogistics();

        //================================= INITIALIZE OBJECTS ======================================================

        logistics.createObjects();

        //============================ EXAMPLE OF LOG IN STRUCTURE ===================================================

        String selection;
        String answerID;
        String password;
        boolean validMenu;

        System.out.println("\n====WELCOME=====");
        do {
            System.out.printf("%s%n%s%n%s%n%s%n",
                    "1. Log in",
                    "2. Create Account",
                    "3. View available rooms",
                    "4. Exit");

            selection = myApp.input.nextLine();

            switch (selection) {
                case "1":
                    System.out.print("Please enter your user ID: ");
                    answerID = myApp.input.nextLine();
                    System.out.print("Please enter your password: ");
                    password = myApp.input.nextLine();
                    logistics.logIn(answerID, password);
                    validMenu = false;
                    break;
                case "2":
                    logistics.addCustomer();
                    validMenu = false;
                    break;

                case "3":
                    boolean cancel = false;
                    int numberOfRooms;
                    boolean oneRoom = false;
                    System.out.println("2.3. SEARCH FOR AVAILABLE ROOMS. Note: Need to register to book.");
                    numberOfRooms = logistics.numberOfRoomsBooking(); //Method call
                    if (numberOfRooms == 0) {
                        cancel = true;
                    } else if (numberOfRooms == 1) {
                        oneRoom = true;
                    } else {
                        oneRoom = false;
                    }
                    if (!cancel) {
                        ArrayList<BookingSearch> matchingResults = logistics.searchBooking(oneRoom); //Method call
                        if (matchingResults.isEmpty()) {
                            System.out.println("No results" + "\nBack (Enter)");
                        } else {
                            int countElements = 0;
                            boolean lastMinute;
                            for (BookingSearch booking : matchingResults) {
                                logistics.lastMinute(booking);
                                System.out.printf("%-4s%s%n", Integer.toString(++countElements).concat("."), booking);
                            }
                            System.out.println("You need to register to make any of the suggested bookings above. \nBack(Enter)");
                        }
                    } else {
                        System.out.println("Back (Enter)");
                    }
                    myApp.input.nextLine();
                    validMenu = false;
                    break;
                case "4":
                    System.out.println("Exits program.");
                    validMenu = true;
                    break;
                default:
                    System.out.println("Please enter a valid selection");
                    validMenu = false;
                    break;
            }
        } while (!validMenu);
    }
}
