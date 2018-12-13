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
        int intAnswer = 0;
        String answerID;
        String password;
        boolean validateInput;
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
                    //myApp.input.nextLine();
                    System.out.printf("%s%n%s", "Welcome new guest. You will be assigned a unique user ID.", "Enter your desired password: ");
                    myApp.input.nextLine();
                    System.out.println("SYNTAX ERROR. YOU HAVE OVERLOADED THE MAINFRAME.\n");
                    validMenu = false;
                    break;

                case "3":
                    System.out.println("2.3. Search for available rooms. Note: Need to register to book.");
                    ArrayList<BookingSearch> matchingResults = logistics.searchBooking(); //Method call
                    if (matchingResults.isEmpty()) {
                        System.out.println("No results" + "\n Back (Enter)");
                    } else {
                        int countElements = 0;
                        boolean lastMinute;
                        for (BookingSearch booking : matchingResults) {
                            logistics.lastMinute(booking);
                                System.out.printf("%-4s%s%n", Integer.toString(++countElements).concat("."), booking);
                        }
                        System.out.println("You need to register to make any of the suggested bookings. \nBack(Enter)");
                    }
                    myApp.input.nextLine();
                    validMenu = false;
                    break;
                case "4":
                    validateInput = true;
                    return;

                default:
                    System.out.println("Please enter a valid selection");
                    validMenu = false;
            }

        } while (!validMenu);


    }
}
