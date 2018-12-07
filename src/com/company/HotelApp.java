package com.company;

import java.util.Scanner;

public class HotelApp {
    private Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        HotelApp myApp = new HotelApp();
        HotelLogistics logistics = new HotelLogistics();

        //================================= INITIALIZE OBJECTS ======================================================

        logistics.createObjects();

        //============================ EXAMPLE OF LOG IN STRUCTURE =====================================================

        int selection;
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

            selection = myApp.input.nextInt();
            myApp.input.nextLine();

            switch (selection) {
                case 1:
                    //do {
                        System.out.print("Please enter your user ID: ");
                        answerID = myApp.input.nextLine();
                       /* try {
                            intAnswer = Integer.parseInt(answerID);
                            validateInput = true;
                        } catch (NumberFormatException e) {
                            System.out.println("\nYour user ID is the number you received while creating your account. Try again. \n");
                            validateInput = false;
                        }
                    } while (!validateInput);*/

                    System.out.print("Please enter your password: ");
                    password = myApp.input.nextLine();
                    logistics.logIn(answerID, password);
                  /*  if (password.equals("custom")) { //This shit ain't pretty, vi ändrar det tillsammans. -J
                        validMenu = true;
                    } else {
                        validMenu = false;
                    }*/
                  validMenu = false;
                    break;

                case 2:
                    //myApp.input.nextLine();
                    System.out.printf("%s%n%s", "Welcome new guest. You will be assigned a unique user ID.", "Enter your desired password: ");
                    myApp.input.nextLine();
                    System.out.println("SYNTAX ERROR. YOU HAVE OVERLOADED THE MAINFRAME.\n");
                    validMenu = false;
                    break;

                case 3:
                    System.out.println("\nSorry, there are no available rooms at this time.\n");
                    validMenu = false;
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Please enter a valid selection");
                    validMenu = false;
            }

        } while (!validMenu);

        /*int id = 0;                     //Account identifier
        String password = "admin";     //Account password
        logistics.logIn(id, password);  //Method call

        System.out.println("Just a print to see if log out returns to main.");*/

    }
}
