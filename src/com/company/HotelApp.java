package com.company;

import java.util.Scanner;

public class HotelApp {
    private Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        //Main myApp = new Main();
        HotelLogistics logistics = new HotelLogistics();

        //================================= INITIALIZE OBJECTS ======================================================

        logistics.createObjects();

        //============================ EXAMPLE OF LOG IN STRUCTURE =====================================================

        int id = 0;                     //Account identifier
        String password = "admin";     //Account password
        logistics.logIn(id, password);  //Method call

        System.out.println("Just a print to see if log out returns to main.");
    }
}
