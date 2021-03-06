package com.company;

import com.company.file_management.DeleteData;
import com.company.file_management.LoadData;
import com.company.file_management.SaveData;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class HotelLogistics {

    private ArrayList<AccountCustomer> customerList = new ArrayList<>();  //Lista över kunder.
    private ArrayList<AccountAdmin> adminList = new ArrayList<>();
    private ArrayList<Room> roomList = new ArrayList<>();        //Lista över rummen
    private ArrayList<BedPrice> bedPriceList = new ArrayList<>();
    private ArrayList<StandardPrice> standardList = new ArrayList<>();
    private Scanner input = new Scanner(System.in);
    private DeleteData delete = new DeleteData();
    private SaveData save = new SaveData();
    private LoadData load = new LoadData();

    //2.1.
    public void logIn(String id, String password) {
        loadAllCustomers();
        loadAllAdmins();
        boolean match = false;
        if (id.matches("C\\d+") || id.matches("c\\d+")) {
            for (AccountCustomer customer : customerList) {
                if (customer.getAccountID().equalsIgnoreCase(id) && customer.getPassword().equals(password)) {
                    if (customer.isCancelledAccount()) {
                        System.out.printf("%s%n%s%n", "This account is cancelled. You need to create a new account.", "Back (Enter)");
                        input.nextLine();
                        return;
                    } else {
                        match = true;
                        System.out.println("\nWelcome " + customer.getName() + "\n");
                        customerMainMenu(customer, false);
                        break;
                    }
                } else {
                    match = false;
                }
            }
        } else if (id.matches("A\\d+") || id.matches("a\\d+")) {
            for (AccountAdmin admin : adminList) {
                if (admin.getAccountID().equalsIgnoreCase(id) && admin.getPassword().equals(password) && !admin.isCancelledAccount()) {
                    match = true;
                    adminMainMenu(admin);
                    break;
                } else {
                    match = false;
                }
            }
        } else {
            match = false;
        }
        if (!match) {
            System.out.println("Login failed. Check user id and password.\nBack (Enter)");
            input.nextLine();
        }
    }

    //3.4. & 4.5.
    private boolean logOut() {   //This method returns true if user choose to log out, and false if not.
        String menuChoice;
        boolean logout = false;
        System.out.println("Log out? y/n");
        do {
            menuChoice = input.nextLine();
            if (menuChoice.equalsIgnoreCase("y")) {
                System.out.println("Logged out" + "\nBack (Enter)");
                input.nextLine();
                logout = true;
            } else if (menuChoice.equalsIgnoreCase("n")) {
                System.out.println("Log out cancelled" + "\nBack (Enter)");
                logout = false;
            } else {
                System.out.println("Invalid input. Type y/n:");
            }
        } while (!menuChoice.equalsIgnoreCase("y") && !menuChoice.equalsIgnoreCase("n"));
        return logout;
    }

    //3.
    private void adminMainMenu(Account loggedInAccount) {
        String menuChoice;
        boolean logout = false;
        boolean validateInput;
        do {
            int toCheckInToday = 0;
            int checkedInToday = 0;
            int toCheckOutToday = 0;
            int checkedOutToday = 0;
            int lateCheckOut = 0;
            int lateCheckIn = 0;

            System.out.printf("%s%n%s%n%s%n%s%n%s%n%s%n%s%n",
                    "3.\n====ADMIN MAIN MENU====",
                    "1. Customers",
                    "2. Rooms",
                    "3. Bookings",
                    "4. Check in",
                    "5. Check out",
                    "0. Log out");
            loadAllRooms();
            loadAllCustomers();
            for (Room room : roomList) {  //Count expected check in & check out for today, including late ones (gives warning)
                for (BookingConfirm booking : room.getRoomBookingList()) {
                    if (booking.getFromDate().equals(LocalDate.now())) {
                        toCheckInToday++;
                        if (booking.isCheckedIn()) {
                            checkedInToday++;
                        }
                    } else if (booking.getFromDate().isBefore(LocalDate.now()) && !booking.isCheckedIn()) {
                        toCheckInToday++;
                        lateCheckIn++;
                    } else if (booking.getToDate().equals(LocalDate.now())) {
                        toCheckOutToday++;
                        if (booking.isCheckedOut()) {
                            checkedOutToday++;
                        }
                    } else if (booking.getToDate().isBefore(LocalDate.now()) && !booking.isCheckedOut()) {  //if overdue stay
                        toCheckOutToday++;
                        lateCheckOut++;
                    }
                }
            }
            System.out.printf("%-18s%d%s%d%s%n%-18s%d%s%d%s%n",
                    "Check in today:", checkedInToday, "/", toCheckInToday, (lateCheckIn > 0 ? ("  ").concat(Integer.toString(lateCheckIn)).concat(" late check ins!") : ""),      //Warns if any late check ins
                    "Check out today:", checkedOutToday, "/", toCheckOutToday, (lateCheckOut > 0 ? ("  ").concat(Integer.toString(lateCheckOut)).concat(" overdue stays!") : "")); //Warns if any overdue stays
            do {
                menuChoice = input.nextLine();
                switch (menuChoice) {
                    case "1":
                        adminCustomers(loggedInAccount);
                        validateInput = true;
                        break;
                    case "2":
                        adminRooms(loggedInAccount);
                        validateInput = true;
                        break;
                    case "3":
                        //System.out.println("Method still under construction");
                        viewBookings(loggedInAccount);
                        validateInput = true;
                        break;
                    case "4":
                        adminCheckIn();
                        validateInput = true;
                        break;
                    case "5":
                        adminCheckOut();
                        validateInput = true;
                        break;
                    case "0":
                        logout = logOut();
                        validateInput = true;
                        break;
                    default:
                        System.out.println("Invalid option. Type a choice 0-4:");
                        validateInput = false;
                        break;
                }
            }
            while (!validateInput);
        } while (!logout);
    }

    //3.1.
    private void adminCustomers(Account loggedInAccount) {  //UNDER CONSTRUCTION
        ArrayList<AccountCustomer> methodList = new ArrayList<>();
        String menuChoice;
        boolean validateInput;
        int intChoice = 0;  //for choosing a customer

        do {
            updateCustomerList();
            System.out.println("3.1. CUSTOMERS");

            int countElements = 0;
            for (AccountCustomer x : customerList) {
                if (!x.isCancelledAccount()) {     //If account is not admin, and nor cancelled; add to new ArrayList (methodList)
                    methodList.add(x);
                    System.out.printf("%-4s%s%n", Integer.toString(++countElements).concat("."), x);
                }
            }
            if (methodList.isEmpty()) {
                System.out.println("Customer list is empty.");

            } else {
                System.out.printf("%-4s%s%n",
                        ("1-").concat(Integer.toString(countElements)).concat("."), " Choose customer");
            }
            System.out.printf("%s%n%s%n%s%n",
                    "A.   Add customer",
                    "C.   Cancelled accounts",
                    "0.   Back");
            do { // do this while input is not numeric, or while input does not match accounts (1-n) or 0, A or C.
                menuChoice = input.nextLine();
                //=============================== ADD CUSTOMER ====================================================
                if (menuChoice.equalsIgnoreCase("A")) {
                    addCustomer();
                    validateInput = true;
                    //=========================== CANCELLED ACCOUNTS ===================================================
                } else if (menuChoice.equalsIgnoreCase("C")) {
                    adminCancelledAccounts(loggedInAccount);
                    validateInput = true;
                    //============================== BACK =============================================================
                } else if (menuChoice.equals("0") || menuChoice.equalsIgnoreCase("O")) {
                    return;
                    //========================== SPECIFIC CUSTOMER ===================================================
                } else {
                    try {
                        intChoice = Integer.parseInt(menuChoice);  // String -> int
                        validateInput = true;
                        if (intChoice < 1 || intChoice > methodList.size()) {
                            validateInput = false;
                            System.out.println("Choice did not match an alternative. Try again:");
                            //} else {
                            //    validateNumeric = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Choice did not match an alternative. Try again:");
                        validateInput = false;
                    }
                    if (validateInput) {
                        for (AccountCustomer customer : customerList) {
                            if (methodList.get(intChoice - 1).getAccountID().equalsIgnoreCase(customer.getAccountID())) {    //Find the corresponding account in the original list.
                                customerMainMenu(customer, true);   //Method call
                            }
                        }
                    }
                }
            } while (!validateInput);
        } while (true); //Always loop, until menuChoice = 0 -> Return
    }

    //3.1.1.
    public void addCustomer() {

        String menu;
        String firstName;
        String lastName;
        String name;
        String phoneNumber;
        String password;
        String passwordCheck;
        String choice;
        String phoneValidate = "0\\d\\d\\d\\d\\d\\d\\d\\d+"; // checks that phone number is in correct format
        boolean checkAll = false;
        boolean checkSwitch;

        System.out.printf("%s%n%s%n%s%n%s%n",
                "====NEW GUEST====",
                "Welcome to Hotel Gittan. Register your account here.",
                "1. Proceed",
                "0. Back");

        do {
            menu = input.nextLine();
            menu = menu.toUpperCase();

            switch (menu) {
                case "1":
                    System.out.println("====ACCOUNT CREATION====" +
                            "\nYou will be assigned a unique user ID." +
                            "\nPlease fill in the following information below.");
                    checkSwitch = true;
                    break;
                case "O":
                case "0":
                    System.out.println();
                    return;
                default:
                    System.out.println("Invalid input. Try again.");
                    checkSwitch = false;
            }
        } while (!checkSwitch);

        do {
            firstName = ""; // resets information for loop.
            lastName = "";
            phoneNumber = "";

            while (firstName.matches(".*\\d+.*") || firstName.isEmpty()) {
                System.out.print("First name: ");
                firstName = input.nextLine();

                if (firstName.matches(".*\\d+.*")) {
                    System.out.println("Your name can't contain numbers. Try again!\n");
                } else if (firstName.isEmpty()) {
                    System.out.println("No letters has been entered. Try again!\n");
                }
            }
            while (lastName.matches(".*\\d+.*") || lastName.isEmpty()) {
                System.out.print("Last name: ");
                lastName = input.nextLine();

                if (lastName.matches(".*\\d+.*")) {
                    System.out.println("Your name can't contain numbers. Try again. \n");
                } else if (lastName.isEmpty()) {
                    System.out.println("No letters has been entered. Try again!\n");
                }
            }
            while (!phoneNumber.matches(phoneValidate)) {
                System.out.print("Phone number: ");
                phoneNumber = input.nextLine();

                if (!phoneNumber.matches(phoneValidate)) {
                    System.out.println("Invalid phone number. It must be numeric, start with '0' and contain at least 9 digits.\n");
                }
            }
            name = firstName + " " + lastName; // two variables for user friendliness, combined to pass on to customerAccount.

            do {
                System.out.print("Password: ");
                password = input.nextLine();
                System.out.print("Repeat your password: ");
                passwordCheck = input.nextLine();

                if (!password.equals(passwordCheck)) {
                    System.out.println("\nYour password didn't match. Try again.");
                }
            } while (!password.equals(passwordCheck));

            do {
                System.out.printf("%n%s%n%s%n%s%n%s%n%s%n%s%n%s%n%s%n%s%n%s%n%s",
                        "====ACCOUNT VERIFICATION====",
                        "The information you have entered is: ",
                        "First name: ", firstName,
                        "Last name: ", lastName,
                        "Phone number: ", phoneNumber,
                        "Password: ", password,
                        "Is this information correct?",
                        "Y. Yes.",
                        "N. No, fill it in again.",
                        "0. Back");

                choice = input.nextLine();
                choice = choice.toUpperCase();
                checkSwitch = true;

                switch (choice) {
                    case "Y":
                        System.out.printf("Thank you %s.%n", name);
                        checkAll = true;
                        break;
                    case "N":
                        System.out.println("\nPlease fill in your information again.");
                        break;
                    case "O":
                    case "0":
                        System.out.println("Cancelled. No account created.");
                        return;
                    default:
                        System.out.println("\nInvalid input. Try again.");
                        checkSwitch = false;
                }
            } while (!checkSwitch);
        } while (!checkAll);
        try {
            AccountCustomer newDude = new AccountCustomer(getEntityCounts("Customer"), name, password, phoneNumber);
            customerList.add(newDude);
            save.saveCustomers(customerList);
            System.out.printf("You can now log in with your unique user ID: %s, and your chosen password. %n%n", newDude.getAccountID());
        }catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Back (Enter)");
        input.nextLine();
    }

    //3.1.3.
    private void adminCancelledAccounts(Account loggedInAccount) {  //UNDER CONSTRUCTION
        ArrayList<Account> cancelledAccounts = new ArrayList<>();
        String menuChoice;
        int intChoice = 0;
        boolean validateInput;
        System.out.println("3.1.3. CANCELLED ACCOUNTS");
        int countElements = 0;
        updateCustomerList();
        for (AccountCustomer customer : customerList) {
            if (customer.isCancelledAccount()) {  //If account is cancelled
                cancelledAccounts.add(customer);
                System.out.printf("%-3s%s%n", Integer.toString(++countElements).concat("."), customer);
            }
        }
        if (cancelledAccounts.isEmpty()) {
            System.out.printf("%s%n%s%n", "No cancelled accounts.", "Back (Enter)");
            input.nextLine();
        } else {
            if (cancelledAccounts.size() == 1) { //If only one cancelled account in list
                System.out.printf("%s%n%s%n", "Press 1 to view historic bookings for this customer.", "0. Back");
            } else {
                System.out.printf("%-4s%s%n%s%n",
                        ("1-").concat(Integer.toString(countElements)).concat("."), " View historic bookings for selected customer.",
                        "0. Back");
            }
            do {
                menuChoice = input.nextLine();
                if (menuChoice.equals("0") || menuChoice.equalsIgnoreCase("O")) {
                    return;
                } else {
                    try {
                        intChoice = Integer.parseInt(menuChoice);  // String -> int
                        validateInput = true;
                        if (intChoice < 1 || intChoice > cancelledAccounts.size()) {
                            validateInput = false;
                            System.out.println("Choice did not match an alternative. Try again:");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Choice did not match an alternative. Try again:");
                        validateInput = false;
                    }
                    if (validateInput) {
                        for (AccountCustomer customer : customerList) {
                            if (cancelledAccounts.get(intChoice - 1).getAccountID().equalsIgnoreCase(customer.getAccountID())) {    //Find the corresponding account in the original list.
                                viewBookingsHistoric(customer);   //Method call
                            }
                        }
                    }
                }
            } while (!validateInput);
        }
    }

    // 3.5
    private void adminCheckIn() {
        ArrayList<BookingConfirm> checkInList = new ArrayList<>();
        String menuChoice;
        int intChoice = 0;
        int countElements;
        int countBookingID = 0;
        int bookingID = 0;
        int countValidCheckIns = 0;
        //int uniqueID = 0;
        boolean validateInput;
        boolean proceed = false;
        boolean cancel;
        updateAllRooms();

        do {
            System.out.println("CHECK IN");
            countElements = 0;
            countBookingID = 0;
            cancel = false;
            for (Room room : roomList) {
                for (BookingConfirm booking : room.getRoomBookingList()) {
                    if (!booking.isCheckedIn() && (booking.getFromDate().equals(LocalDate.now()) || booking.getFromDate().isBefore(LocalDate.now()))) {
                        checkInList.add(booking);
                        System.out.printf("%-3s%s%7s%n", Integer.toString(++countElements).concat("."), booking, (booking.getFromDate().isBefore(LocalDate.now()) ? "LATE!" : "")); //Conditional operator: Warns if hasn't checked in at check in date.
                    }
                }
            }
            if (checkInList.isEmpty()) {
                System.out.println("No remaining check in:s today. \nBack (Enter)");
                proceed = true;
                input.nextLine();
            } else {
                System.out.printf("%s%n%s%n", "1-n: Select a booking to check in.", "0. Back");
                do {
                    menuChoice = input.nextLine();
                    if (menuChoice.equals("0") || menuChoice.equalsIgnoreCase("O")) {
                        validateInput = true;
                        proceed = true;
                        cancel = true;
                    } else {
                        try {
                            intChoice = Integer.parseInt(menuChoice);  // String -> int
                            validateInput = true;
                            if (intChoice < 1 || intChoice > checkInList.size()) {
                                validateInput = false;
                                System.out.println("Choice did not match an alternative. Try again:");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Choice did not match an alternative. Try again:");
                            validateInput = false;
                        }
                    }
                } while (!validateInput);
                if (!cancel) {

                    for (Room room : roomList) {
                        for (BookingConfirm booking : room.getRoomBookingList()) {
                            if (checkInList.get(intChoice - 1).getBookingID() == booking.getBookingID()
                                    && !booking.isCheckedIn()) {
                                countBookingID++;
                                bookingID = booking.getBookingID();
                                //uniqueID = booking.getUniqueID();
                            }
                        }
                    }
                    if (countBookingID > 1) {
                        countElements = 0;
                        System.out.println("There are more than one room reservations linked to this booking:");
                        for (Room room : roomList) {
                            for (BookingConfirm booking : room.getRoomBookingList()) {
                                if (booking.getBookingID() == bookingID && !booking.isCheckedIn()) {
                                    System.out.printf("%-3s%s%n", Integer.toString(++countElements).concat("."), booking);
                                }
                            }
                        }
                        System.out.println("A. Check in all.");
                    }
                    System.out.printf("%s%d%s%d%s%d%s%n%s%n",
                            "C. Check in chosen room (Room nr: ", checkInList.get(intChoice - 1).getRoom().getRoomNumber(),
                            ". Standard: ", checkInList.get(intChoice - 1).getRoom().getStandard(), ". Beds: ", checkInList.get(intChoice - 1).getRoom().getBeds(), ")",
                            "0. Cancel.");
                    do {
                        menuChoice = input.nextLine();
                        if (countBookingID > 1 && menuChoice.equalsIgnoreCase("A")) {
                            for (Room room : roomList) {
                                for (int i = 0; i < room.getRoomBookingList().size(); i++) {
                                    if (room.getRoomBookingList().get(i).getBookingID() == bookingID) {
                                        if (i != 0 && !room.getRoomBookingList().get(i - 1).isCheckedOut()) {  //if prevoius booking is not checked out
                                            System.out.printf("%s%d%s%n", "Unable to check in room nr: ", room.getRoomNumber(), ". Previous booking not checked out.");
                                        } else {
                                            room.getRoomBookingList().get(i).setCheckedIn(true);
                                            save.saveRoom(room);
                                            System.out.printf("%s%d%n", "Checked in room nr: ", room.getRoomNumber());
                                            countValidCheckIns++;

                                        }
                                    }
                                }
                            }
                            if (countValidCheckIns == countBookingID) {
                                System.out.printf("%s%d%s%d%n", "Successfully checked in all ", countBookingID, " rooms of booking ID: ", bookingID);
                            } else {
                                System.out.printf("%s%d%s%d%s%d%n", "Note: Only able to check in ", countValidCheckIns, " of the ", countBookingID, " rooms of booking ID: ", bookingID);
                            }
                            System.out.println("Back (Enter)");
                            validateInput = true;
                            input.nextLine();
                        } else if (menuChoice.equalsIgnoreCase("C")) {
                            for (Room room : roomList) {
                                for (int i = 0; i < room.getRoomBookingList().size(); i++) {
                                    if (room.getRoomBookingList().get(i).getUniqueID() == checkInList.get(intChoice - 1).getUniqueID()) {
                                        if (i != 0 && !room.getRoomBookingList().get(i - 1).isCheckedOut()) {
                                            System.out.printf("%s%d%s%n", "Unable to check in room nr: ", room.getRoomNumber(), ". Previous booking not checked out.");
                                        } else {
                                            room.getRoomBookingList().get(i).setCheckedIn(true);
                                            save.saveRoom(room);
                                            System.out.printf("%s%d%n", "Successfully checked in room nr: ", room.getRoomNumber());
                                        }
                                        break;
                                    }
                                }
                            }
                            System.out.println("Back (Enter)");
                            validateInput = true;
                            input.nextLine();
                        } else if (menuChoice.equals("0") || menuChoice.equalsIgnoreCase("O")) {
                            validateInput = true;
                            cancel = true;
                        } else {
                            System.out.println("Invalid input. Try again:");
                            validateInput = false;
                        }
                    } while (!validateInput);
                }
                if (cancel) {
                    System.out.println("Back(Enter)");
                    input.nextLine();
                }
            }
            checkInList.clear();
        } while (!proceed);
    }

    //  3.6
    private void adminCheckOut() {
        ArrayList<BookingConfirm> checkOutList = new ArrayList<>();
        String menuChoice;
        int intChoice = 0;
        int countElements;
        int countBookingID = 0;
        int bookingID = 0;
        int uniqueID = 0;
        boolean validateInput;
        boolean proceed = false;
        boolean cancel;
        updateAllRooms();

        do {
            System.out.println("CHECK OUT");
            countElements = 0;
            countBookingID = 0;
            cancel = false;
            System.out.println("Check out today:");
            for (Room room : roomList) {
                for (BookingConfirm booking : room.getRoomBookingList()) {
                    if (booking.isCheckedIn() && !booking.isCheckedOut() && (booking.getToDate().equals(LocalDate.now())) || booking.getToDate().isBefore(LocalDate.now())) {
                        checkOutList.add(booking);
                        countElements++;
                        System.out.printf("%-3s%s%10s%n", Integer.toString(countElements).concat("."), booking, (booking.getToDate().isBefore(LocalDate.now()) ? "OVERDUE!" : "")); //Conditional operator: Warns if overdue stay
                    }
                }
            }
            if (checkOutList.isEmpty()) {
                System.out.println("No expected check outs today.");
                //proceed = true;
            }
            System.out.println("Early check out:");
            for (Room room : roomList) {
                for (BookingConfirm booking : room.getRoomBookingList()) {
                    if (booking.isCheckedIn() && !booking.isCheckedOut() && booking.getToDate().isAfter(LocalDate.now())) {
                        checkOutList.add(booking);
                        countElements++;
                        System.out.printf("%-3s%s%n", Integer.toString(countElements).concat("."), booking);
                    }
                }
            }
            if (checkOutList.isEmpty()) {
                System.out.println("No reservations to check out. \nBack (Enter)");
                proceed = true;
                input.nextLine();

            } else {
                System.out.printf("%s%n%s%n", "1-n: Select a booking to check out.", "0. Back.");
                do {
                    menuChoice = input.nextLine();
                    if (menuChoice.equals("0") || menuChoice.equalsIgnoreCase("O")) {
                        validateInput = true;
                        proceed = true;
                        cancel = true;
                    } else {
                        try {
                            intChoice = Integer.parseInt(menuChoice);  // String -> int
                            validateInput = true;
                            if (intChoice < 1 || intChoice > checkOutList.size()) {
                                validateInput = false;
                                System.out.println("Choice did not match an alternative. Try again:");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Choice did not match an alternative. Try again:");
                            validateInput = false;
                        }
                    }
                } while (!validateInput);
                if (!cancel) {
                    for (Room room : roomList) {
                        for (BookingConfirm booking : room.getRoomBookingList()) {
                            if (checkOutList.get(intChoice - 1).getBookingID() == booking.getBookingID()
                                    && booking.isCheckedIn() && !booking.isCheckedOut()) {
                                countBookingID++;
                                bookingID = booking.getBookingID();
                                uniqueID = booking.getUniqueID();
                            }
                        }
                    }
                    if (countBookingID > 1) {
                        countElements = 0;
                        System.out.println("There are more than one room reservation linked to this booking:");
                        for (Room room : roomList) {
                            for (BookingConfirm booking : room.getRoomBookingList()) {
                                if (booking.getBookingID() == bookingID && !booking.isCheckedOut()) {                    // + && !booking.isCheckedOut()
                                    System.out.printf("%-3s%s%n", Integer.toString(++countElements).concat("."), booking);
                                }
                            }
                        }
                        System.out.println("A. Check out all.");
                    }
                    System.out.printf("%s%d%s%d%s%d%s%n%s%n",
                            "C. Check out chosen room (Room nr: ", checkOutList.get(intChoice - 1).getRoom().getRoomNumber(),
                            ". Standard: ", checkOutList.get(intChoice - 1).getRoom().getStandard(), ". Beds: ", checkOutList.get(intChoice - 1).getRoom().getBeds(), ")",
                            "0. Cancel.");
                    do {
                        menuChoice = input.nextLine();
                        if (countBookingID > 1 && menuChoice.equalsIgnoreCase("A")) {
                            for (Room room : roomList) {
                                for (BookingConfirm booking : room.getRoomBookingList()) {
                                    if (booking.getBookingID() == bookingID) {
                                        booking.setCheckedOut(true);
                                        if (booking.getToDate().isAfter(LocalDate.now())) {  //If early checkout: Set new toDate to today.
                                            booking.setToDate(LocalDate.now());
                                        }
                                        save.saveRoom(room);
                                    }
                                }
                            }
                            System.out.printf("%s%d%s%d%n%s%n", "Successfully checked out all ", countBookingID, " rooms of booking ID: ", bookingID, "Back (Enter)");
                            validateInput = true;
                            //proceed = true;
                            input.nextLine();
                        } else if (menuChoice.equalsIgnoreCase("C")) {
                            for (Room room : roomList) {
                                for (BookingConfirm booking : room.getRoomBookingList()) {
                                    if (booking.getUniqueID() == uniqueID) {
                                        booking.setCheckedOut(true);
                                        if (booking.getToDate().isAfter(LocalDate.now())) {  //If early checkout: Set new toDate to today.
                                            booking.setToDate(LocalDate.now());
                                        }
                                        save.saveRoom(room);
                                        break;
                                    }
                                }
                            }
                            System.out.printf("%s%d%n%s%n", "Successfully checked out room nr: ", checkOutList.get(intChoice - 1).getRoom().getRoomNumber(), "Back (Enter)");
                            validateInput = true;
                            input.nextLine();

                        } else if (menuChoice.equals("0") || menuChoice.equalsIgnoreCase("O")) {
                            validateInput = true;
                            cancel = true;
                        } else {
                            System.out.println("Invalid input. Try again:");
                            validateInput = false;
                        }
                    } while (!validateInput);
                }
                if (cancel) {
                    System.out.println("Back(Enter)");
                    input.nextLine();
                }
            }
            checkOutList.clear();
        } while (!proceed);
    }

    private void editCustomerInfo(AccountCustomer concernedAccount) {
        boolean validateInput = false;
        String choice;
        updateCustomerList();

        do {
            if (concernedAccount.isCancelledAccount()) {
                validateInput = true;
            } else {
                System.out.println("4.3\n====EDIT USER INFORMATION====");
                System.out.println("Name:        " + concernedAccount.getName());
                System.out.println("Phonenumber: " + concernedAccount.getPhoneNumber());
                System.out.println("Password:    " + concernedAccount.getPassword());
                System.out.println("=============================");
                System.out.println("1. Edit name");
                System.out.println("2. Edit phone number");
                System.out.println("3. Edit Password");
                System.out.println("4. Remove account");
                System.out.println("0. Back");

                do {
                    System.out.println("Enter choice: ");
                    choice = input.nextLine();
                    switch (choice) {
                        case "1":
                            editAccountName(concernedAccount);
                            break;
                        case "2":
                            editAccountPhoneNr(concernedAccount);
                            break;
                        case "3":
                            editAccountPassword(concernedAccount);
                            break;
                        case "4":
                            cancelAccount(concernedAccount);
                            break;
                        case "0":
                            validateInput = true;
                            break;
                        default:
                            System.out.println("Invalid input. Try Again:");
                            break;
                    }
                } while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4") && !choice.equals("0"));
            }
        } while (!validateInput);
    }

    private void editAccountName(AccountCustomer loggedInAccount) {
        boolean validateinput = true;
        boolean validateYorN;
        boolean validateExitToChangeName = true;
        boolean cancel = false;
        boolean exitChange = false;

        do {
            System.out.println("4.3.1. Edit name");
            System.out.println("Name currently assigned: " + loggedInAccount.getName());
            System.out.println("1. Edit current name");
            System.out.println("0. Back");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    do {
                        String fullName;
                        String firstName;
                        String lastName = "";
                        do {
                            do {
                                System.out.println("Enter new first name: ");
                                firstName = input.nextLine();

                                if (firstName.equals("0")) {
                                    cancel = true;
                                    exitChange = true;
                                } else {
                                    if (firstName.matches(".*\\d+.*") && !firstName.equals("0")) {
                                        System.out.println("Your first name can't contain numbers. Try again. \n");
                                        cancel = false;
                                    } else if (firstName.isEmpty()) {
                                        System.out.println("No digits entered. Try again.\n");
                                        cancel = false;
                                    } else {
                                        cancel = true;
                                        exitChange = true;
                                    }
                                }
                            } while (!cancel);

                            do {
                                if (firstName.equals("0")) {
                                    cancel = true;
                                    exitChange = true;
                                } else {
                                    System.out.println("Enter new last name: ");
                                    lastName = input.nextLine();
                                    if (lastName.matches(".*\\d+.*") && !lastName.equals("0")) {
                                        System.out.println("Your name can't contain numbers. Try again.");
                                        cancel = false;
                                    } else if (lastName.isEmpty()) {
                                        System.out.println("No digits entered. Try again.");
                                        cancel = false;
                                    } else if (lastName.equals("0")) {
                                        cancel = true;
                                        exitChange = true;
                                    }
                                }
                            } while (!cancel);
                        } while (!exitChange);

                        fullName = firstName + " " + lastName;

                        if (loggedInAccount.getName().matches(fullName)) {
                            System.out.println("Name entered matches the previous one. Try again.");
                            validateExitToChangeName = false;
                            validateinput = false;
                        }

                        if (firstName.equals("0") || lastName.equals("0")) {
                            validateinput = true;
                            validateExitToChangeName = false;
                            System.out.println("Edit of name cancelled. \nBack (Enter)");
                            input.nextLine();

                        } else if (!loggedInAccount.getName().matches(fullName)) {
                            System.out.println("New name: " + fullName);

                            do {
                                System.out.println("Keep new name?");
                                System.out.println("y/n");
                                String yesOrNo = input.nextLine();

                                if (yesOrNo.equals("0")) {
                                    System.out.println("Changes cancelled. Returning to menu.\n");
                                    validateExitToChangeName = false;
                                    validateinput = true;
                                    validateYorN = true;

                                } else {
                                    if (yesOrNo.equalsIgnoreCase("Y")) {
                                        loggedInAccount.setName(fullName);
                                        save.saveCustomers(customerList);
                                        validateinput = true;
                                        validateYorN = true;
                                        validateExitToChangeName = true;

                                    } else if (yesOrNo.equalsIgnoreCase("N")) {
                                        validateinput = false;
                                        validateYorN = true;
                                        validateExitToChangeName = false;

                                    } else {
                                        System.out.println("Invalid input. Try again.");
                                        validateinput = false;
                                        validateYorN = false;
                                    }
                                }
                            } while (!validateYorN);
                        }
                    } while (!validateinput);
                    break;

                case "0":
                case "o":
                case "O":
                    validateExitToChangeName = true;
                    System.out.println("No changes made.\nBack (Enter)");
                    input.nextLine();
                    break;
                default:
                    System.out.println("Invalid input. Try again.");
                    validateExitToChangeName = false;
                    break;
            }
        } while (!validateExitToChangeName);
    }


    private void editAccountPhoneNr(AccountCustomer loggedInAccount) {
        boolean validateInput;
        boolean cancel = false;

        do {
            System.out.println("4.3.2 Edit phone number");
            System.out.println("Phone number currently assigned: " + loggedInAccount.getPhoneNumber());
            System.out.println("1. Edit current phone number");
            System.out.println("0. Back");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Please enter new phone number:\n0. Back");
                    do {
                        String newNr = input.nextLine();
                        if (newNr.equals("0")) {
                            cancel = true;
                            validateInput = true;

                        } else {
                            try {
                                loggedInAccount.setPhoneNumber(newNr);
                                save.saveCustomers(customerList);
                                validateInput = true;
                                System.out.println("New phone number set: " + loggedInAccount.getPhoneNumber() + "\nBack (Enter)");
                                input.nextLine();
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage() + " Try again:");
                                validateInput = false;
                            }
                        }
                    } while (!validateInput);
                    break;
                case "O":
                case "o":
                case "0":
                    validateInput = true;
                    cancel = true;
                    break;
                default:
                    System.out.println("Invalid input. Try again:");
                    validateInput = false;
                    break;
            }
            if (cancel) {
                System.out.println("Cancelled. No new phone number set.");
            }
        } while (!validateInput);
    }

    private void editAccountPassword(AccountCustomer loggedInAccount) {
        boolean validateInput = true;
        boolean validateChangePW = true;
        do {
            System.out.println("4.3.3. Edit password");
            System.out.println("Current password: " + loggedInAccount.getPassword());
            System.out.println("1. Edit current password");
            System.out.println("0. Back");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    do {
                        System.out.println("Enter new password: ");
                        String newPwd = input.nextLine();

                        if (newPwd.equals(loggedInAccount.getPassword())) {
                            System.out.println("Password entered is the same as your old one. Try a new one! \nPress (Enter)");
                            validateInput = false;
                            input.nextLine();

                        } else if (!newPwd.equals(loggedInAccount.getPassword())) {
                            System.out.println("Are you happy with your newly entered password?");
                            System.out.println("y/n or press 0 to go back to menu!");
                            String yesOrNo = input.nextLine();

                            if (yesOrNo.equalsIgnoreCase("Y")) {
                                System.out.println("Very well, then lets return to the previous menu \nPress (Enter)");
                                loggedInAccount.setPassword(newPwd);
                                save.saveCustomers(customerList);
                                validateInput = true;
                                validateChangePW = true;
                                input.nextLine();

                            } else if (yesOrNo.equalsIgnoreCase("N")) {
                                System.out.println("Then lets try again!\nPress (Enter)");
                                validateInput = false;
                                validateChangePW = false;
                                input.nextLine();

                            } else if (yesOrNo.equalsIgnoreCase("0")) {
                                System.out.println("Back option (0) chosen. Returning to 'Change password' menu!\nPress (Enter)");
                                validateChangePW = false;
                                input.nextLine();

                            } else {
                                System.out.println("Invalid input. Try again:");
                                validateInput = false;
                                validateChangePW = false;
                            }
                        }
                    } while (!validateInput);
                    break;

                case "O":
                case "o":
                case "0":
                    validateInput = true;
                    validateChangePW = true;
                    break;
                default:
                    System.out.println("Invalid input. Try again:");
                    validateInput = false;
                    validateChangePW = false;
                    break;
            }
        } while (!validateChangePW);
    }

    private void cancelAccount(AccountCustomer loggedInAccount) {
        boolean validateInput;
        boolean validatePW;

        System.out.println("4.3.4\n====REMOVE ACCOUNT====");
        do {
            System.out.printf("%s%n%s%n%s%n%s%n", "Do you truly wish to remove this account?",
                    "Note: Any future bookings made will de cancelled. Present bookings that are not checked in will also be cancelled",
                    "Y. Yes, remove account.",
                    "N. No, do not remove account.");
            System.out.println("y/n");
            String yesOrNo = input.nextLine();

            if (yesOrNo.equalsIgnoreCase("Y")) {
                do {
                    System.out.println("Enter password to verify deletion of account: ");
                    String pwCheck = input.nextLine();
                    if (pwCheck.matches(loggedInAccount.getPassword()) || pwCheck.equals(adminList.get(0).getPassword())) {
                        loggedInAccount.setCancelledAccount(true);
                        //Do: Update room
                        for(Room room : roomList) {
                            for(int i = 0 ; i < room.getRoomBookingList().size() ; i++) {
                                if ((room.getRoomBookingList().get(i).getCustomer().getAccountID().equals(loggedInAccount.getAccountID())) &&
                                        !room.getRoomBookingList().get(i).isCheckedIn()) {
                                    room.getRoomBookingList().remove(i);           //Cancel all unchecked in bookings for this customer
                                    i -= 1;
                                }
                            }
                            save.saveRoom(room);
                        }
                        save.saveCustomers(customerList);
                        validateInput = true;
                        validatePW = true;
                        System.out.println("Account cancelled.\nBack (Enter)");
                        input.nextLine();

                    } else {
                        System.out.println("Invalid password. Try again:");
                        validateInput = false;
                        validatePW = false;
                        input.nextLine();

                    }
                } while (!validatePW);
            } else if (yesOrNo.equalsIgnoreCase("N")) {
                System.out.println("4.3.4.1\nReturning to previous menu! \nPress (Enter)");
                validateInput = true;
                input.nextLine();

            } else {
                System.out.println("(Y)es or (N)o hasn't been entered. Try again!");
                validateInput = false;
            }
        } while (!validateInput);
    }

    private void adminAddRoom() {
        String choice;
        int beds = 0;
        String answer;
        int standard = 0;
        boolean validateInput;

        do {
            System.out.println("====ADD ROOM======");
            System.out.println("1. Create new room");
            System.out.println("0. Back");
            System.out.println("==================");
            choice = input.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Enter number of beds (1,2 or 4): ");
                    do {
                        answer = input.nextLine();
                        try {
                            beds = Integer.parseInt(answer);
                            validateInput = true;
                        } catch (NumberFormatException e) {
                            validateInput = false;
                        }

                        if (beds == 1 || beds == 2 || beds == 4) {
                            validateInput = true;
                        } else {
                            validateInput = false;
                            System.out.println("Invalid input. Amount of beds must be entered accordingly: 1, 2 or 4. \nTry again:");
                        }
                    } while (!validateInput);

                    System.out.println("Enter room standard (1-5): ");
                    do {
                        answer = input.nextLine();
                        try {
                            standard = Integer.parseInt(answer);
                            validateInput = true;
                        } catch (NumberFormatException e) {
                            validateInput = false;
                        }
                        if (standard == 1 || standard == 2 || standard == 3 || standard == 4 || standard == 5) {
                            validateInput = true;
                            try {
                                Room newRoom = new Room(getEntityCounts("Room"), beds, standard);
                                roomList.add(newRoom);
                                save.saveRoom(newRoom);
                                System.out.println("New room has been created!");
                            }catch(IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                            System.out.println("Back (Enter)");
                            input.nextLine();
                        } else {
                            validateInput = false;
                            System.out.println("Invalid input. Standard must be entered accordingly: 1-5.\nTry again:");
                        }

                    } while (!validateInput);

                    break;

                case "0":
                    System.out.println("Returning to the previous menu!");
                    break;

                default:
                    System.out.println("Faulty input recognized. Try again!\nPress (Enter)");
                    input.nextLine();
                    break;
            }
        } while (!choice.equalsIgnoreCase("1") && !choice.equalsIgnoreCase("0"));
    }

    //3.2. (listRooms)
    private void adminRooms(Account loggedInAccount) {
        String menuChoice;
        boolean validateInput;
        int roomSelect;  // selects room

        do {
            updateAllRooms();
            int counter = 1;

            System.out.println("3.2 ALL ROOMS IN THE SYSTEM");

            for (Room room : roomList) {
                System.out.printf("%-4s%s%n", Integer.toString(counter).concat(". "), room);
                counter++;
            }

            System.out.printf("%n1-%s. Select room from above%n", roomList.size());
            System.out.printf("%s%n%s%n%s%n",
                    "A.    Add a room",
                    "E.    Edit prices",
                    "0.    Back");
            do {
                menuChoice = input.nextLine();
                menuChoice = menuChoice.toUpperCase();

                switch (menuChoice) {
                    case "A":
                        adminAddRoom();
                        validateInput = true;
                        break;
                    case "E":
                        adminEditPrices();
                        validateInput = true;
                        break;
                    case "0":
                    case "O":
                        return;
                    default:
                        try {
                            roomSelect = Integer.parseInt(menuChoice);  // String -> int
                            validateInput = true;
                            if (roomSelect < 1 || roomSelect > roomList.size()) {
                                System.out.printf("There are only %d rooms to select from. Try again or press \"0\" to go back.%n", roomList.size());
                            } else {
                                adminEditRoomInfo(roomList.get(roomSelect - 1));
                                validateInput = true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Try again.");
                            validateInput = false;
                        }
                }
            } while (!validateInput); // loops the room menu
        } while (true); //Always loop, until menuChoice = 0 -> Return
    }

    // 3.2.3.
    private void adminEditRoomInfo(Room room) {
        String answer;
        int intAnwser;
        boolean validate;

        do {
            updateRoom(room.getRoomNumber());
            System.out.printf("%s%d%n%-10s%d%n%-10s%d%n%s%n%s%n%s%n%s%n%s%n",
                    "3.2.3. EDIT ROOM NUMBER ", room.getRoomNumber(),
                    "Beds: ", room.getBeds(),
                    "Standard:", room.getStandard(),
                    "1: Edit beds",
                    "2: Edit standard ",
                    "3: Remove room",
                    "4: View bookings for this room",
                    "0. Back");
            do {
                answer = input.nextLine();

                switch (answer) {
                    case "1":
                        System.out.println("Edit the number of beds for room " + room.getRoomNumber() +
                                ", or press \"0\" to cancel. ");

                        do {
                            System.out.println("Type in the new number of beds for this room: ");

                            answer = input.nextLine();
                            if (answer.equals("0") || answer.equalsIgnoreCase("o")) {
                                System.out.println("Edit number has been cancelled. \n " +
                                        "Back (enter)");
                                input.nextLine();
                                validate = true;
                            } else {

                                try {
                                    intAnwser = Integer.parseInt(answer);
                                    room.setBeds(intAnwser);
                                    save.saveRoom(room);
                                    validate = true;

                                } catch (NumberFormatException e) {
                                    System.out.println("Answer must be digits. ");
                                    validate = false;

                                } catch (IllegalArgumentException b) {
                                    System.out.println(b.getMessage());
                                    validate = false;
                                }
                            }
                        }
                        while (!validate);
                        System.out.println("The new number of bed(s) in room " + room.getRoomNumber()
                                + " is now " + answer + ".");
                        break;
                    case "2":
                        System.out.println("Edit standard for room number " + room.getRoomNumber());

                        do {
                            System.out.println("Type in new standard for the room, or press \"0\" to cancel ");
                            answer = input.nextLine();

                            if (answer.equals("0") || answer.equalsIgnoreCase("o")) {
                                System.out.println("Edit number has been cancelled. \n " +
                                        "Back (enter).");
                                input.nextLine();
                                validate = true;
                            } else {
                                try {
                                    intAnwser = Integer.parseInt(answer);
                                    room.setStandard(intAnwser);
                                    save.saveRoom(room);
                                    validate = true;

                                } catch (NumberFormatException a) {
                                    System.out.println("Answer must be digits.");
                                    validate = false;

                                } catch (IllegalArgumentException b) {
                                    System.out.println(b.getMessage());
                                    validate = false;
                                }
                            }
                        } while (!validate);
                        System.out.println("The new standard for room number " + room.getRoomNumber()
                                + " is now: " + answer + ".");
                        break;
                    case "3":
                        boolean acceptRemove = false;
                        System.out.println("Remove this room? y/n");
                        do {
                            validate = true;

                            answer = input.nextLine();
                            if (answer.equalsIgnoreCase("y")) {
                                acceptRemove = true;
                                validate = true;
                            } else if (answer.equalsIgnoreCase("n")) {
                                acceptRemove = false;
                                validate = true;

                            } else {
                                System.out.println("Invalid anwser. ");
                                validate = false;
                            }

                        } while (!validate);

                        if (acceptRemove) {

                            if (room.getRoomBookingList().isEmpty()) {
                                acceptRemove = true;

                            } else if (acceptRemove) {
                                for (BookingConfirm booking : room.getRoomBookingList()) {
                                    if (booking.getToDate().equals(LocalDate.now()) || booking.getToDate().isAfter(LocalDate.now())) {
                                        acceptRemove = false;
                                        System.out.println("There are current or future bookings for this room.\n" +
                                                " Please remomve all current or future bookings for this room before removing it. ");
                                        break;
                                    }
                                }
                            }
                        }
                        if (!acceptRemove) {
                            System.out.println("Removing room cancelled.");
                        } else {
                            for (int i = 0; i < roomList.size(); i++) {
                                if (roomList.get(i).getRoomNumber() == room.getRoomNumber()) {
                                    delete.deleteRoom(roomList.get(i).getRoomNumber());
                                    loadAllRooms();
                                    System.out.printf("%s%d%s", "Room ", room.getRoomNumber(), " removed succesfully.");
                                    System.out.println("Back (Enter) ");
                                    input.nextLine();
                                    return;
                                }
                            }
                        }
                        System.out.println("Back (Enter) ");
                        input.nextLine();
                        break;
                    case "4": // view bookings.
                        viewBookingsForRoom(room);
                        validate = true;
                        break;
                    case "0":
                        System.out.println("Back (Enter) ");
                        input.nextLine();
                        return;
                    default:
                        System.out.println("Not correct anwser, please enter 0-4");
                        validate = false;
                        break;
                }

            } while (!validate);
        } while (true);
    }

    //3.2.4 (edit price)
    private void adminEditPrices() {
        String menuChoice;
        String answer;
        int intAnwser = 0;
        double newDoubleValue = 0;
        boolean validate = false;
        boolean exitMethod = false;
        boolean cancel;
        boolean validMenuChoice;
        loadAllStandards();
        loadAllBedPrices();

        do {
            cancel = false;
            System.out.printf("%s%n%s%n%s%n%s%n%s%n",
                    "Edit price for:",
                    "1. Standards",
                    "2. Beds",
                    "0. Back",
                    "Type a choice:");
            do {
                validMenuChoice = true;
                menuChoice = input.nextLine();

                switch (menuChoice) {
                    case "1":
                        updateStandardList();
                        System.out.printf("%s%n",
                                "Choose standard 1-5, " +
                                        "or 0 to cancel.");
                        for (StandardPrice standard : standardList) {
                            System.out.println(standard.getName() + " standard/room. Price: " + standard.getPrice() + "SEK");
                        }
                        do {
                            answer = input.nextLine();

                            if (answer.equals("0") || answer.equalsIgnoreCase("O")) {
                                validate = true;
                                exitMethod = false;
                                cancel = true;
                            } else {
                                try {
                                    intAnwser = Integer.parseInt(answer);  // String -> int
                                    validate = true;

                                } catch (NumberFormatException e) {
                                    System.out.println("Input must be numeric. Try again:");
                                    validate = false;
                                }
                            }

                            if (!cancel && validate) {
                                validate = false;
                                for (int i = 0; i < standardList.size(); i++) {
                                    if (standardList.get(i).getName() == intAnwser) {
                                        intAnwser = i;
                                        validate = true;
                                        break;
                                    }
                                }

                                if (!validate) {
                                    System.out.println("Input did not match an alternative. Try again:");
                                } else {
                                    System.out.println("Enter new price for standard " + standardList.get(intAnwser).getName());
                                    do {
                                        answer = input.nextLine();
                                        try {
                                            newDoubleValue = Double.parseDouble(answer);
                                            validate = true;
                                        } catch (NumberFormatException e) {
                                            System.out.println("input must be numneric");
                                            validate = false;
                                        }

                                        if (validate) {
                                            if (newDoubleValue >= 10000 || newDoubleValue < 300) {
                                                System.out.println("Not a valid value. Must be 300-10.000 . Try again");
                                                validate = false;

                                            } else {
                                                standardList.get(intAnwser).setPrice(newDoubleValue);
                                                save.saveStandards(standardList);
                                                System.out.println("Room standard " + standardList.get(intAnwser).getName() +
                                                        ". New price is:  " + standardList.get(intAnwser).getPrice() + " SEK  \nBack (Enter)");
                                                validate = true;
                                                input.nextLine();
                                            }
                                        }
                                    } while (!validate);
                                }
                            }
                        } while (!validate);
                        break;
                    case "2":
                        updateBedPriceList();
                        System.out.printf("%s%n",
                                "Choose the beds/room constant price to edit, or 0 to cancel:");
                        for (BedPrice beds : bedPriceList) {
                            System.out.println(beds.getNumberOfBeds() + " beds/room. Constant: " + beds.getConstant() +
                                    " x standard price per night and room.");
                        }
                        do {
                            answer = input.nextLine();
                            if (answer.equals("0") || answer.equalsIgnoreCase("O")) {
                                validate = true;
                                exitMethod = false;
                                cancel = true;
                            } else {
                                try {
                                    intAnwser = Integer.parseInt(answer);  // String -> int
                                    validate = true;

                                } catch (NumberFormatException e) {
                                    System.out.println("Input must be numeric. Try again:");
                                    validate = false;
                                }
                            }

                            if (!cancel && validate) {
                                validate = false;
                                for (int i = 0; i < bedPriceList.size(); i++) {
                                    if (bedPriceList.get(i).getNumberOfBeds() == intAnwser) {
                                        intAnwser = i;
                                        validate = true;
                                        break;
                                    }
                                }

                                if (!validate) {
                                    System.out.println("Input did not match an alternative. Try again:");
                                } else {

                                    System.out.println("Enter new constant value for " + bedPriceList.get(intAnwser).getNumberOfBeds() +
                                            " beds/room: (X.X)" +
                                            "\nCurrent constant value: "
                                            + bedPriceList.get(intAnwser).getConstant() + " SEK.");
                                    do {
                                        answer = input.nextLine();

                                        try {
                                            newDoubleValue = Double.parseDouble(answer);
                                            validate = true;
                                        } catch (NumberFormatException e) {
                                            System.out.println("input must be numneric, and decimals seperated by a dot (X.X). Try again:");
                                            validate = false;
                                        }

                                        if (validate) {
                                            if (newDoubleValue < 1.00 || newDoubleValue > 4.00) {
                                                System.out.println("Not a valid value. Must be 1.00 - 4.00. Try again:");
                                                validate = false;
                                            } else {
                                                bedPriceList.get(intAnwser).setConstant(newDoubleValue);
                                                save.saveBedPrices(bedPriceList);
                                                System.out.println("New beds constant price for " + bedPriceList.get(intAnwser).getNumberOfBeds() +
                                                        " per room is: " + bedPriceList.get(intAnwser).getConstant() + " SEK \nBack (Enter)");
                                                validate = true;
                                                input.nextLine();
                                            }
                                        }

                                    } while (!validate);
                                }

                            }

                        } while (!validate);
                        break;
                    case "0":
                    case "O":
                    case "o":
                        System.out.println("Back (Enter) ");
                        exitMethod = true;
                        validMenuChoice = true;
                        input.nextLine();
                        break;
                    default:
                        System.out.println("Invalid input. Please type a choice 0-2: ");
                        validMenuChoice = false;
                        break;
                }
                if (cancel) {
                    System.out.println("Back (Enter)");
                    input.nextLine();
                }
            } while (!validMenuChoice);
        } while (!exitMethod);
    }

    private void customerMainMenu(AccountCustomer customerAccount, boolean adminView) {
        String menuChoice;
        boolean logout = false;
        do {
            if (customerAccount.isCancelledAccount()) {
                logout = true;
            } else {
                if (adminView) {
                    System.out.printf("%s%n%-6s%s%n%-6s%s%n%s%n%s%n%s%n%s%n%s%n",
                            "4. CUSTOMER",
                            "Name:", customerAccount.getName(),
                            "ID:", customerAccount.getAccountID(),
                            "1. Make a booking",
                            "2. View bookings",
                            "3. View historic bookings",
                            "4. Edit account info",
                            "0. Back");
                } else {
                    System.out.printf("%s%n%s%s%n%s%n%s%n%s%n%s%n%s%n",
                            "========== MAIN MENU ===========",
                            "Logged in as: ", customerAccount.getName(),
                            "1. Make a booking, or view available",
                            "2. View your bookings",
                            "3. View your historic bookings",
                            "4. Edit account info",
                            "0. Log out");
                }
                loadAllRooms();
                do {
                    menuChoice = input.nextLine();
                    switch (menuChoice) {
                        case "1":
                            //System.out.println("4.1.");
                            makeBooking(customerAccount);
                            break;
                        case "2":
                            //System.out.println("4.2.");
                            viewBookings(customerAccount);
                            break;
                        case "3":
                            viewBookingsHistoric(customerAccount);
                            break;
                        case "4":
                            //System.out.println("4.3.");
                            editCustomerInfo(customerAccount);
                            break;
                        case "0":
                            if (adminView) {
                                logout = true;
                            } else {
                                logout = logOut();
                            }
                            break;
                        default:
                            System.out.println("Invalid option. Type a a choice 0-3:");
                            break;
                    }
                }
                while (!menuChoice.equals("1") && !menuChoice.equals("2") && !menuChoice.equals("3") && !menuChoice.equals("4") && !menuChoice.equals("0"));
            }
        } while (!logout);
    }

    public int numberOfRoomsBooking() {  //This method is needed as a separate method since it's return value are used in different methods in the booking chain.
        String answer;
        int intAnswer = 0;
        boolean validateInput;
        System.out.printf("%s%n%s%n%s%n%s%n",
                "Are you planning on booking one room, or more than one room?",
                "1. One room",
                "2. More than one room",
                "0. Back");
        do {
            answer = input.nextLine();
            if (answer.equals("1")) {
                intAnswer = Integer.parseInt(answer);
                System.out.println("You wish to book one room.");
                validateInput = true;
            } else if (answer.equals("2")) {
                intAnswer = Integer.parseInt(answer);
                System.out.println("You wish to book more than one room.");
                validateInput = true;
            } else if (answer.equals("0") || answer.equalsIgnoreCase("O")) {
                intAnswer = Integer.parseInt(answer);
                validateInput = true;
            } else {
                System.out.println("Invalid input. Please try again:");
                validateInput = false;
            }
        } while (!validateInput);
        return intAnswer;
    }

    //Part of 4.1.
    public ArrayList<BookingSearch> searchBooking(boolean oneRoom) {
        ArrayList<BookingSearch> matchingResults = new ArrayList<>();
        LocalDate fromDate = LocalDate.of(2000, 1, 1);
        LocalDate toDate = LocalDate.of(2000, 1, 1);
        String answer;
        int year;
        int month;
        int day;
        int beds = 0;
        int standard = 0;
        boolean validateInput;
        boolean cancel;

        System.out.printf("%s%s%n%s%n", ((oneRoom) ? "Step 1/4." : "Step 1/2."),
                " Enter date of desired arrival: (YY-MM-DD)", "0. Cancel");
        do {
            cancel = false;
            answer = input.nextLine();
            if (answer.matches("\\d\\d-\\d\\d-\\d\\d")) {
                String[] splitDate = answer.split("-");    //Could also use charAt. T.ex. YYMMDD
                try {
                    year = Integer.parseInt("20" + splitDate[0]);
                    month = Integer.parseInt(splitDate[1]);
                    day = Integer.parseInt(splitDate[2]);
                    fromDate = LocalDate.of(year, month, day);
                    validateInput = true;
                    //}catch (NumberFormatException e) {
                    //    System.out.println("Invalid input. Please type numeric values:");
                } catch (DateTimeParseException e) {     //Or DateTimeException
                    System.out.println("Invalid date. Please try again:");
                    validateInput = false;
                } catch (DateTimeException e) {     //Or DateTimeException
                    System.out.println("The date does not exist. Please try again:");
                    validateInput = false;
                }
                if (validateInput == true && fromDate.isBefore(LocalDate.now())) {
                    System.out.println("Arrival date may not be a past date. Try again.");
                    validateInput = false;
                }
            } else if (answer.equals("0") || answer.equalsIgnoreCase("0")) {
                validateInput = true;
                cancel = true;
            } else {
                System.out.println("Invalid input. Please try again:");
                validateInput = false;
            }
        } while (!validateInput);
        if (!cancel) {
            System.out.printf("%s%s%n%s%n", ((oneRoom) ? "Step 2/4." : "Step 2/2."),
                    " Enter date of desired departure: (YY-MM-DD)", "0. Cancel");
            do {
                //cancel = false;
                answer = input.nextLine();
                if (answer.matches("\\d\\d-\\d\\d-\\d\\d")) {
                    String[] splitDate = answer.split("-");    //Could also use charAt. T.ex. YYMMDD
                    try {
                        year = Integer.parseInt("20" + splitDate[0]);
                        month = Integer.parseInt(splitDate[1]);
                        day = Integer.parseInt(splitDate[2]);
                        toDate = LocalDate.of(year, month, day);
                        validateInput = true;
                        //}catch (NumberFormatException e) {
                        //    System.out.println("Invalid input. Please type numeric values:");
                    } catch (DateTimeParseException e) {     //Or DateTimeException
                        System.out.println("Invalid date. Please try again:");
                        validateInput = false;
                    } catch (DateTimeException e) {     //Or DateTimeException
                        System.out.println("The date does not exist. Please try again:");
                        validateInput = false;
                    }
                    if (validateInput == true && (toDate.isBefore(fromDate)) || toDate.equals(fromDate)) {
                        System.out.println("Departure may not be same day as arrival, and not before arrival. Try again:");
                        validateInput = false;
                    }
                } else if (answer.equals("0") || answer.equalsIgnoreCase("O")) {
                    validateInput = true;
                    cancel = true;
                } else {
                    System.out.println("Invalid input. Please try again:");
                    validateInput = false;
                }
            } while (!validateInput);
        }
        if (!cancel && oneRoom) {
            System.out.println("Step 3/4. Enter number of beds" + "\n0. Cancel");
            do {
                //cancel = false;
                answer = input.nextLine();
                if (answer.equals("0") || answer.equalsIgnoreCase("O")) {
                    validateInput = true;
                    cancel = true;
                } else {
                    try {
                        beds = Integer.parseInt(answer);
                        validateInput = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please type numeric values:");
                        validateInput = false;
                    }
                    if (beds < 1 || beds > 4) {
                        System.out.println("Invalid number of beds. Must be 1-4. Try again.");
                        validateInput = false;
                    } else {
                        validateInput = true;
                    }
                }
            } while (!validateInput);
        }
        if (!cancel && oneRoom) {
            System.out.println("Step 4/4. Enter desired standard (1-5)" + "\n0. Cancel");
            do {
                //cancel = false;
                answer = input.nextLine();
                if (answer.equals("0") || answer.equalsIgnoreCase("O")) {
                    validateInput = true;
                    cancel = true;
                } else {
                    try {
                        standard = Integer.parseInt(answer);
                        validateInput = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please type numeric values:");
                        validateInput = false;
                    }
                    if (standard < 1 || standard > 5) {
                        System.out.println("Invalid standard. Must be 1-5. Try again.");
                        validateInput = false;
                    } else {
                        validateInput = true;
                    }
                }
            } while (!validateInput);
        }
        if (!cancel) {
            updateAllRooms();
            if (oneRoom) {
                System.out.println("SEARCH RESULT:");
                for (Room room : roomList) {
                    if (room.getBeds() == beds && room.getStandard() == standard) {
                        if (checkDates(room, fromDate, toDate)) {
                            double price = calculateSingleBookingPrice(fromDate, toDate, room);
                            matchingResults.add(new BookingSearch(room, fromDate, toDate, price));
                            //break;
                        }
                    }
                }
                if (matchingResults.isEmpty()) {
                    System.out.println("Unfortunately no perfect matches. Here are some other alternatives that might be of interest: ");
                    for (Room room : roomList) {
                        if (room.getBeds() >= beds) {
                            if (checkDates(room, fromDate, toDate)) {
                                double price = calculateSingleBookingPrice(fromDate, toDate, room);
                                matchingResults.add(new BookingSearch(room, fromDate, toDate, price));
                            }
                        }
                    }
                }
            } else {
                for (Room room : roomList) {
                    if (checkDates(room, fromDate, toDate)) {
                        double price = calculateSingleBookingPrice(fromDate, toDate, room);
                        matchingResults.add(new BookingSearch(room, fromDate, toDate, price));
                    }
                }
            }
        }
        if (cancel) {
            System.out.println("Search cancelled." + "\nBack (Enter)");
            input.nextLine();
        }
        return matchingResults;
    }

    //4.1.
    private void makeBooking(AccountCustomer concernedAccount) {
        System.out.println("4.1. MAKE BOOKING, OR VIEW AVAILABLE");
        ArrayList<BookingSearch> matchingResults = new ArrayList<>();
        ArrayList<BookingSearch> addedBookings = new ArrayList<>();
        String answer;
        int countElements;
        int bookingChoice = 0;
        boolean validateInput;
        boolean proceed = false;
        boolean cancel = false;
        boolean oneRoom = false;

        int numberOfRooms = numberOfRoomsBooking(); //Method call
        if (numberOfRooms == 0) {
            cancel = true;
        } else if (numberOfRooms == 1) {
            oneRoom = true;
        }

        if (!cancel) {
            matchingResults = searchBooking(oneRoom);  //Call to method searchBooking to add matching booking object to ArrayList, depending on one room, or more than one room.
            for (BookingSearch booking : matchingResults) {
                lastMinute(booking);                 //Determine if last minute. If so; adjust to last minute prices.
            }
            do {
                if (matchingResults.isEmpty()) {
                    System.out.println("No results" + "\nBack (Enter)");
                    cancel = true;
                    proceed = true;
                    input.nextLine();
                } else {
                    countElements = 0;
                    for (BookingSearch booking : matchingResults) {
                        System.out.printf("%-4s%s%n", Integer.toString(++countElements).concat("."), booking);
                    }
                    if (oneRoom) {   //If booking one room
                        if (matchingResults.size() == 1) {
                            System.out.printf("%s%n", "Press 1 to make the suggested booking above.");
                        } else {
                            System.out.printf("%-6s%s%n%-6s%n",
                                    ("1-").concat(Integer.toString(countElements)).concat(":"), "Make a booking from the list.", "0. Cancel. No booking will be made.");
                        }
                    } else {      //If booking more than one room
                        System.out.printf("%s%d%s%.02f%s%n",
                                "Added rooms: ", addedBookings.size(), ". Sum: ", calculateSumBookingPrice(addedBookings), " SEK.");
                        if (matchingResults.size() == 1) {
                            System.out.printf("%s%n", "Press 1 to add the suggested room to your booking.");
                        } else {
                            System.out.printf("%-6s%s%n",
                                    ("1-").concat(Integer.toString(countElements)).concat(":"), "Add a room from the list to your booking.");
                        }
                        System.out.printf("%-6s%s%d%s%n%-6s%s%n", "P.", "Proceed to make booking of ", addedBookings.size(), " added rooms.",
                                "0.", "Cancel. No booking will be made." );
                    }
                    do {
                        answer = input.nextLine();
                        if (answer.equals("0") || answer.equalsIgnoreCase("O")) {
                            validateInput = true;
                            cancel = true;
                            proceed = true;
                        } else if (!oneRoom && answer.equalsIgnoreCase("P")) {
                            if (addedBookings.isEmpty()) {
                                System.out.println("No rooms have been added to your booking. \nContinue (Enter)");
                                input.nextLine();
                                validateInput = true;
                            } else {
                                System.out.printf("%s%n%s%n%s%n", "Proceed to confirm and finish this booking?",
                                        "Y. Yes, proceed.",
                                        "N. No, continue to add rooms to booking, or cancel booking.");
                                do {
                                    answer = input.nextLine();
                                    switch (answer) {
                                        case "Y":
                                        case "y":
                                            countElements = 0;
                                            for (BookingSearch booking : matchingResults) {
                                                if (booking.isAdded()) {
                                                    countElements++;
                                                }
                                            }
                                            if (countElements == 0) {
                                                System.out.println("No rooms are added to this booking. Booking will not be possible.");
                                                validateInput = true;
                                                proceed = false;
                                            } else {
                                                validateInput = true;
                                                proceed = true;
                                            }
                                            break;
                                        case "N":
                                        case "n":
                                            validateInput = true;
                                            proceed = false;
                                            break;
                                        default:
                                            System.out.println("Invalid input. Try again:");
                                            validateInput = false;
                                            break;
                                    }
                                } while (!validateInput);
                            }
                        } else {
                            try {
                                bookingChoice = Integer.parseInt(answer);  // String -> int
                                validateInput = true;
                                proceed = true;
                                if (bookingChoice < 1 || bookingChoice > matchingResults.size()) {
                                    validateInput = false;
                                    System.out.println("Choice did not match an alternative. Try again:");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Choice did not match an alternative. Try again:");
                                validateInput = false;
                            }

                            if (!oneRoom && validateInput) {
                                if (matchingResults.get(bookingChoice - 1).isAdded()) {
                                    System.out.printf("%s%n%s%n%s%n",
                                            "This room is already added to your booking. Remove from your list?",
                                            "Y. Yes, remove this room",
                                            "N. No, don't remove this room.");
                                    do {
                                        answer = input.nextLine();
                                        if (answer.equalsIgnoreCase("Y")) {
                                            validateInput = true;
                                            matchingResults.get(bookingChoice - 1).setAdded(false);
                                            for (int i = 0; i < addedBookings.size(); i++) {
                                                if (matchingResults.get(bookingChoice - 1).getRoom() == addedBookings.get(i).getRoom()) {
                                                    addedBookings.remove(i);
                                                    break;
                                                }
                                            }
                                        } else if (answer.equalsIgnoreCase("N")) {
                                            validateInput = true;
                                        } else {
                                            validateInput = false;
                                            System.out.println("Invalid input. Try again:");
                                        }
                                    } while (!validateInput);
                                    proceed = false;
                                } else {
                                    System.out.printf("%s%n%s%n%s%n%s%n%s%n%s%n",
                                            "ADDING TO BOOKING:", matchingResults.get(bookingChoice - 1),
                                            "Add this to booking?",
                                            "Y. Yes, add this to my booking.",
                                            "N. No, do not add this to my booking. Go back to booking suggestions.",
                                            "O. Cancel booking. No booking will be made.");
                                    do {
                                        answer = input.nextLine();
                                        if (answer.equalsIgnoreCase("Y")) {
                                            proceed = false;
                                            validateInput = true;

                                            matchingResults.get(bookingChoice - 1).setAdded(true);
                                            addedBookings.add(matchingResults.get(bookingChoice - 1));  //Added bookings transfered to list addedBookings, for the confirmation step later.
                                            System.out.println("Booking added.");

                                        } else if (answer.equalsIgnoreCase("N")) {
                                            proceed = false;
                                            validateInput = true;
                                            System.out.println("Booking not added.");

                                        } else if (answer.equals("0") || answer.equalsIgnoreCase("O")) {
                                            proceed = true;
                                            cancel = true;
                                            validateInput = true;
                                        } else {
                                            proceed = false;
                                            validateInput = false;
                                            System.out.println("Invalid input. Please try again:");
                                        }
                                    } while (!validateInput);
                                }
                            }
                        }
                    } while (!validateInput);
                }
            } while (!proceed);
        }

        if (!cancel) {   //Confirm booking
            if (oneRoom) {
                addedBookings.add(matchingResults.get(bookingChoice - 1));
            }
            for (BookingSearch booking : addedBookings) {  //For nicer display, don't show added here, because every item is added at this step.
                booking.setAdded(false);
            }

            System.out.println("Make booking for:");
            countElements = 0;
            for (BookingSearch booking : addedBookings) {
                System.out.printf("%-4s%s%n", Integer.toString(++countElements).concat("."), booking);
            }
            System.out.printf("%s%.2f%s%n%s%n%s%n", "Sum: ", calculateSumBookingPrice(addedBookings), " SEK.", "Y. Yes, confirm this booking.", "N. No. Cancel booking process.");

            do {
                answer = input.nextLine();
                switch (answer) {
                    case "Y":
                    case "y":
                        int bookingID = 0;
                        try {
                            bookingID = getEntityCounts("BookingID");
                            try {
                                for (BookingSearch booking : addedBookings) {
                                    bookingDates(booking.getRoom(), booking.getFromDate(), booking.getToDate(),
                                            concernedAccount, booking.getPrice(), bookingID, getEntityCounts("UniqueBookingID"));
                                }
                                //validateInput = true;
                                System.out.println("Booking succesful!");
                            } catch (IllegalArgumentException f) {
                                System.out.println("BOOKING FAILED!\n" + f.getMessage());
                            }
                        }catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        validateInput = true;
                        System.out.println("Back (Enter)");
                        input.nextLine();
                        break;
                    case "N":
                    case "n":
                        validateInput = true;
                        cancel = true;
                        break;
                    default:
                        validateInput = false;
                        System.out.println("Invalid input. Try again:");
                        break;
                }
            } while (!validateInput);
        }
        if (cancel) {
            System.out.println("Booking stage cancelled. No booking made." + "\nBack (Enter)");
            input.nextLine();
        }
    }

    //Part of 4.1.
    private boolean checkDates(Room room, LocalDate fromDate, LocalDate toDate) {
        boolean match = false;
        updateAllRooms();
        if (room.getRoomBookingList().isEmpty()) {
            match = true;
        } else if (room.getRoomBookingList().size() == 1) {
            if (toDate.isEqual(room.getRoomBookingList().get(0).getFromDate()) ||
                    toDate.isBefore(room.getRoomBookingList().get(0).getFromDate())) {
                match = true;
            } else
                match = fromDate.isEqual(room.getRoomBookingList().get(0).getToDate()) ||
                        fromDate.isAfter(room.getRoomBookingList().get(0).getToDate());

        } else {
            for (int i = 0; i < room.getRoomBookingList().size(); i++) {

                if (i == 0) {
                    if (toDate.equals(room.getRoomBookingList().get(i).getFromDate()) ||
                            toDate.isBefore(room.getRoomBookingList().get(i).getFromDate())) {
                        match = true;
                        break;
                    } else if ((fromDate.equals(room.getRoomBookingList().get(i).getToDate()) || fromDate.isAfter(room.getRoomBookingList().get(i).getToDate())) &&
                            (toDate.equals(room.getRoomBookingList().get(1).getFromDate()) || toDate.isBefore(room.getRoomBookingList().get(1).getFromDate()))) {
                        match = true;
                        break;
                    }

                } else if ((i > 0) && (i < room.getRoomBookingList().size() - 1)) {
                    if ((fromDate.equals(room.getRoomBookingList().get(i).getToDate()) ||
                            fromDate.isAfter(room.getRoomBookingList().get(i).getToDate())) &&
                            (toDate.equals(room.getRoomBookingList().get(i + 1).getFromDate()) ||
                                    toDate.isBefore(room.getRoomBookingList().get(i + 1).getFromDate()))) {
                        match = true;
                        break;
                    }
                } else if (i == room.getRoomBookingList().size() - 1) {
                    match = fromDate.equals(room.getRoomBookingList().get(i).getToDate()) ||
                            fromDate.isAfter(room.getRoomBookingList().get(i).getToDate());
                }
            }
        }
        return match;
    }

    //Part of 4.1.
    private void bookingDates(Room room, LocalDate fromDate, LocalDate toDate, AccountCustomer customer, double price, int bookingID, int uniqueBookingID) {  //Can be used to book, or sort bookings in cronological time order.
        updateRoom(room.getRoomNumber());
        if (room.getRoomBookingList().isEmpty()) {                                                  //If booking list for room is empty.
            room.getRoomBookingList().add(new BookingConfirm(room, fromDate, toDate, price, bookingID, uniqueBookingID, customer));
            save.saveRoom(room);
            //System.out.println("Booking successful. Code 1");
            return;
        } else if (room.getRoomBookingList().size() == 1) {                                        //If only one booking in list.
            if (toDate.isEqual(room.getRoomBookingList().get(0).getFromDate()) ||                      // If check out is same day as existing check in.
                    toDate.isBefore(room.getRoomBookingList().get(0).getFromDate())) {                 // If check out is before existing check in.
                room.getRoomBookingList().add(0, new BookingConfirm(room, fromDate, toDate, price, bookingID, uniqueBookingID, customer));     //Add before existing booking in list.
                save.saveRoom(room);
                //System.out.println("Booking successful. Code 2");
                return;
            } else if (fromDate.isEqual(room.getRoomBookingList().get(0).getToDate()) ||      // If check in is same day as existing check out.
                    fromDate.isAfter(room.getRoomBookingList().get(0).getToDate())) {         // If check in is after existing check out.
                room.getRoomBookingList().add(new BookingConfirm(room, fromDate, toDate, price, bookingID, uniqueBookingID, customer));             // Add after existing booking in list.
                save.saveRoom(room);
                //System.out.println("Booking successful. Code 3");
                return;
            } else {
                throw new IllegalArgumentException(
                        "Room " + room.getRoomNumber() + " Dates: " + fromDate + " to " + toDate + ": Not available at chosen date/dates. \nOnly one booking for this room in system, which collides with booking.");
            }
        } else {
            for (int i = 0; i < room.getRoomBookingList().size(); i++) {

                if (i == 0) {
                    if (toDate.equals(room.getRoomBookingList().get(i).getFromDate()) ||    // If index is 0 && if check out is same day as existing check in || check out is before existing check in.
                            toDate.isBefore(room.getRoomBookingList().get(i).getFromDate())) {
                        room.getRoomBookingList().add(0, new BookingConfirm(room, fromDate, toDate, price, bookingID, uniqueBookingID, customer));       //Add before existing booking in room booking list.
                        save.saveRoom(room);
                        //System.out.println("Booking successful. Code 4 " + " Iteration " + i);
                        return;
                    } else if ((fromDate.equals(room.getRoomBookingList().get(i).getToDate()) || fromDate.isAfter(room.getRoomBookingList().get(i).getToDate())) &&
                            (toDate.equals(room.getRoomBookingList().get(1).getFromDate()) || toDate.isBefore(room.getRoomBookingList().get(1).getFromDate()))) {
                        room.getRoomBookingList().add(i + 1, new BookingConfirm(room, fromDate, toDate, price, bookingID, uniqueBookingID, customer));
                        save.saveRoom(room);
                        return;
                    }
                    /*else {
                        throw new IllegalArgumentException(
                                "Room " + room.getRoomNumber() + " Dates: " + fromDate + " to " + toDate + ": In conflict with other booking. Iteration: " + i + " Code A");
                    }*/
                } else if ((i > 0) && (i < room.getRoomBookingList().size() - 1)) {                            // If index is more than 0 && index doesn't point at the last object in the room booking list.
                    if ((fromDate.equals(room.getRoomBookingList().get(i).getToDate()) ||                      // If check in is same day as existing check out.
                            fromDate.isAfter(room.getRoomBookingList().get(i).getToDate())) &&
                            (toDate.equals(room.getRoomBookingList().get(i + 1).getFromDate()) ||              // If check out is same day as next check in.
                                    toDate.isBefore(room.getRoomBookingList().get(i + 1).getFromDate()))) {    // If check out is before next check in
                        room.getRoomBookingList().add(i + 1, new BookingConfirm(room, fromDate, toDate, price, bookingID, uniqueBookingID, customer));       // Add after booking i. (Available between existing bookings i & i+1)
                        save.saveRoom(room);
                        //System.out.println("Booking successful. Code 5 " + " Iteration " + i);
                        return;
                    } /*else {
                        throw new IllegalArgumentException(
                                "Room " + room.getRoomNumber() + " Dates: " + fromDate + " to " + toDate + ": In conflict with other booking. Iteration: " + i + " Code B");
                    }*/
                } else if (i == room.getRoomBookingList().size() - 1) {                         // If index points to last item in list.
                    if (fromDate.equals(room.getRoomBookingList().get(i).getToDate()) ||        // If check in is same day as existing check out.
                            fromDate.isAfter(room.getRoomBookingList().get(i).getToDate())) {   // If check in is after existing check ou (i).
                        room.getRoomBookingList().add(new BookingConfirm(room, fromDate, toDate, price, bookingID, uniqueBookingID, customer));
                        save.saveRoom(room);
                        //System.out.println("Booking successful. Code 6 " + " Iteration " + i);
                        return;
                    } else {
                        throw new IllegalArgumentException(
                                "Room " + room.getRoomNumber() + " Dates: " + fromDate + " to " + toDate + ": In conflict with other booking. Iteration: " + i + " Code C");
                    }
                }
            }
        }
    }

    private double calculateSingleBookingPrice(LocalDate fromDate, LocalDate toDate, Room room) {  //Used to determine the price of a specific room item booking.
        double price;
        double standardPrice = 1;
        double bedsConstant = 1;
        loadAllStandards();
        loadAllBedPrices();
        long periodDays = ChronoUnit.DAYS.between(fromDate, toDate);
        try {
            standardPrice = standardList.get(room.getStandard() - 1).getPrice();  //May throw IndexOutOfBoundsException if no match??
        }catch (IndexOutOfBoundsException e) {
            System.out.println("ERROR! The expected room standard was not found. The given price is invalid.");
        }
            for (BedPrice beds : bedPriceList) {
                if (room.getBeds() == beds.getNumberOfBeds()) {  //If number of beds in the room equals
                    bedsConstant = beds.getConstant();
                    break;
                }
            }
        price = periodDays * standardPrice * bedsConstant;   //nights x standard x beds
        if(price == 1) {
            System.out.println("ERROR! Price calculation failed. The given price is invalid.");
        }
        return price;
    }

    private double calculateSumBookingPrice(ArrayList<BookingSearch> bookingList) {  //Used to calculate the sum of an entire booking.
        double sum = 0;
        for (Booking booking : bookingList) {
            sum += booking.getPrice();
        }
        return sum;
    }

    public void lastMinute(BookingSearch booking) {
        long periodDays;
        long daysUntil;
        periodDays = ChronoUnit.DAYS.between(booking.getFromDate(), booking.getToDate());
        daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), booking.getFromDate());
        if (daysUntil < 6 && periodDays < 10) {  //If last minute
            booking.setLastMinute(true);
            booking.setPrice(booking.getPrice() * 0.75);
        }
    }

    //4.2.  &&  3.3.)
    private void viewBookings(Account loggedIn) {  //3 displaying options: 1: Admin sees all booking 2: Admin sees customer specific bookings 3: Customer sees customer specific bookings
        updateAllRooms();
        do {
            if (loggedIn instanceof AccountAdmin) {
                System.out.println("4.2. ALL BOOKINGS");
            } else {
                System.out.println("4.2. BOOKINGS FOR: " + loggedIn.getName());
            }
            ArrayList<BookingConfirm> methodList = new ArrayList<>();
            String menuChoice;
            boolean validateInput;
            int intChoice = 0;
            for (Room room : roomList) {
                for (BookingConfirm booking : room.getRoomBookingList()) {
                    if (booking.getToDate().equals(LocalDate.now()) || booking.getToDate().isAfter(LocalDate.now())) {
                        if (loggedIn instanceof AccountAdmin) {
                            methodList.add(booking);
                        } else if (booking.getCustomer().getAccountID().equalsIgnoreCase(loggedIn.getAccountID())) {
                            methodList.add(booking);
                        }
                    }
                }
            }  //MAYBE ADD: SORTING DEPENDING ON FROM WHICH METHOD THIS METHOD IS INVOKED (compareTo). t.ex. by date rather than room->date
            if (methodList.isEmpty()) {
                System.out.println("No bookings found.\n" + "Back (Enter)");
                input.nextLine();
                return;
            } else {
                for (int i = 0; i < methodList.size(); i++) {
                    System.out.printf("%-4s%s%n", Integer.toString(i + 1).concat(". "), methodList.get(i));
                }
                if (methodList.size() == 1) { //If only one booking in list.
                    System.out.printf("%s%n%n", "Press 1 to cancel this booking.");
                } else {
                    System.out.printf("%-6s%s%n",
                            ("1-").concat(Integer.toString(methodList.size())).concat("."), "Cancel booking");
                }
                System.out.printf("%-6s%s%n%-6s%s%n", "H.", "Historic bookings", "0.", "Back");
                do {
                    menuChoice = input.nextLine();
                    if (menuChoice.equals("0") || menuChoice.equalsIgnoreCase("O")) {
                        return;
                    } else if (menuChoice.equalsIgnoreCase("H")) {
                        viewBookingsHistoric(loggedIn);
                        return;
                    } else {
                        try {
                            intChoice = Integer.parseInt(menuChoice);  // String -> int
                            validateInput = true;
                            if (intChoice < 1 || intChoice > methodList.size()) {
                                validateInput = false;
                                System.out.println("Choice did not match an alternative. Try again:");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Choice did not match an alternative. Try again:");
                            validateInput = false;
                        }
                        if (validateInput) {
                            boolean skip = false;
                            for (Room room : roomList) {
                                for (int i = 0; i < room.getRoomBookingList().size(); i++) {
                                    if (methodList.get(intChoice - 1).getUniqueID() == room.getRoomBookingList().get(i).getUniqueID()) {    //Find the corresponding account in the original list.
                                        cancelBooking(room.getRoomBookingList().get(i));   //Method call
                                        skip = true;
                                        break;
                                    }
                                }
                                if(skip) {
                                    break;
                                }
                            }
                        }
                    }
                } while (!validateInput);
            }
        } while (true);
    }

    //4.4.
    private void viewBookingsHistoric(Account concernedAccount) {
        ArrayList<BookingConfirm> historicBookings = new ArrayList<>();
        updateAllRooms();
        System.out.printf("%s%n%s%s%n", "4.4. HISTORIC BOOKINGS",
                "For customer: ", concernedAccount.getName());
        int countElements = 0;
        for (Room room : roomList) {
            for (BookingConfirm booking : room.getRoomBookingList()) {
                if (booking.getToDate().isBefore(LocalDate.now())) {  //If booking is historic
                    if (concernedAccount instanceof AccountAdmin) {
                        historicBookings.add(booking);
                    } else if (booking.getCustomer().getAccountID().equalsIgnoreCase(concernedAccount.getAccountID())) {
                        historicBookings.add(booking);
                    }
                    historicBookings.add(booking);
                    System.out.printf("%-3s%s%n", Integer.toString(++countElements).concat("."), booking);
                }
            }
        }
        if (historicBookings.isEmpty()) {
            System.out.println("No historic bookings.");
        }
        System.out.println("Back (Enter)");
        input.nextLine();
    }

    //4.2.1.1.
    private void cancelBooking(BookingConfirm thisBooking) {
        int countElements = 0;
        int bookingID = thisBooking.getBookingID();
        int uniqueID = thisBooking.getUniqueID();
        boolean validate;
        String confirm;
        String menu;
        updateAllRooms();

        System.out.printf("%n%s%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s%n",
                "CANCEL BOOKING",
                "Dates: ", thisBooking.getDates(),
                "Name: ", thisBooking.getCustomer().getName(),
                "Room number: ", thisBooking.getRoom().getRoomNumber(),
                "Room standard: ", thisBooking.getRoom().getStandard(),
                "Number of beds: ", thisBooking.getRoom().getBeds());

        do {
            System.out.printf("%n%s%n%s%n%s%n",
                    "Would you like to cancel this booking?",
                    "Y. Yes",
                    "N. No");

            confirm = input.nextLine();
            confirm = confirm.toUpperCase();

            switch (confirm) {
                case "Y":
                    if (thisBooking.isCheckedIn() && !thisBooking.isCheckedOut()) {
                        System.out.println("This booking is checked in. Please check out if you want to cancel this booking in advance. \nBack (Enter)");
                        input.nextLine();
                        return;
                    } else if (thisBooking.getFromDate().isBefore(LocalDate.now())) {
                        System.out.println("You can only cancel a future booking. Contact hotel admin personal to cancel this booking. \nBack (Enter)");
                        input.nextLine();
                        return;
                    }
                    for (Room room : roomList) {
                        for (int i = 0; i < room.getRoomBookingList().size(); i++) {
                            if (thisBooking.getBookingID() == room.getRoomBookingList().get(i).getBookingID()) {
                                countElements++;
                            }
                        }
                    }
                    if (countElements > 1) {
                        countElements = 0;
                        System.out.println("\nThere are more than one room reservations linked to this booking ");
                        for (Room room : roomList) {
                            for (BookingConfirm booking : room.getRoomBookingList()) {
                                if (booking.getBookingID() == bookingID) {
                                    System.out.printf("%-3s%s%n", Integer.toString(++countElements).concat("."), booking);
                                }
                            }
                        }
                        System.out.print("\nA. Cancel all\n");
                    }

                    System.out.printf("%s%n%s%n",
                            "C. Confirm cancel booking for selected room.",
                            "0. Back");
                    do {
                        menu = input.nextLine();
                        if (countElements > 1 && menu.equalsIgnoreCase("A")) {
                            for (Room room : roomList) {
                                for (int i = 0; i < room.getRoomBookingList().size(); i++) {
                                    if (room.getRoomBookingList().get(i).getBookingID() == bookingID) {
                                        room.getRoomBookingList().remove(i);
                                        i -= 1;
                                        save.saveRoom(room);
                                    }
                                }
                            }

                            System.out.printf("%s%d%s%d%n%s%n", "Successfully cancelled all ", countElements, " bookings of booking ID: ", bookingID, "Back (Enter)");
                            validate = true;
                            //proceed = true;
                            input.nextLine();
                        } else if (menu.equalsIgnoreCase("C")) {
                            for (Room room : roomList) {
                                for (int i = 0; i < room.getRoomBookingList().size(); i++) {
                                    if (room.getRoomBookingList().get(i).getUniqueID() == uniqueID) {
                                        room.getRoomBookingList().remove(i);
                                        save.saveRoom(room);
                                        break;
                                    }
                                }
                            }
                            System.out.printf("%s%s%n%s%n", "Successfully cancelled booking: ", thisBooking, "Back (Enter)");
                            validate = true;
                            input.nextLine();
                        } else if (menu.equals("0") || menu.equalsIgnoreCase("O")) {
                            System.out.println("Booking still valid.\n");
                            //validate = true;
                            return;
                        } else {
                            System.out.println("Invalid input. Try again:");
                            validate = false;
                        }
                    } while (!validate);

                    validate = true;
                    break;
                case "N":
                case "n":
                case "O":
                case "0":
                    System.out.println("Booking still valid.\n");
                    validate = true;
                    break;
                default:
                    System.out.println("Invalid input. Try again.");
                    validate = false;
                    break;
            }
        } while (!validate);
        loadAllRooms(); //Since booking(s) may have been removed
    }

    private void viewBookingsForRoom(Room room) {
        updateRoom(room.getRoomNumber());
        do {
            System.out.println("3.2.3.3. BOOKINGS FOR ROOM NUMBER " + room.getRoomNumber());
            String menuChoice;
            boolean validateInput = false;
            int intChoice = 0;

            ArrayList<BookingConfirm> methodList = new ArrayList<>();

            for (BookingConfirm booking : room.getRoomBookingList()) {
                if (booking.getToDate().equals(LocalDate.now()) || booking.getToDate().isAfter(LocalDate.now())) {
                    methodList.add(booking);
                }
            }
            if (methodList.isEmpty()) {
                System.out.println("No present or future bookings for this room.");
            } else {
                for (int i = 0; i < methodList.size(); i++) {
                    System.out.printf("%-4s%s%n", Integer.toString(i + 1).concat(". "), methodList.get(i));
                }
                if (methodList.size() == 1) { //If only one booking in list.
                    System.out.printf("%s%n%n", "Press 1 to cancel this booking.");
                } else {
                    System.out.printf("%-6s%s%n",
                            ("1-").concat(Integer.toString(methodList.size())).concat("."), "Cancel booking");
                }
            }
            System.out.printf("%-6s%s%n%-6s%s%n", "H.", "Historic bookings", "0.", "Back");
            do {
                menuChoice = input.nextLine();
                if (menuChoice.equals("0") || menuChoice.equalsIgnoreCase("O")) {
                    return;
                } else if (menuChoice.equalsIgnoreCase("H")) {
                    System.out.printf("%s%s%n", "HISTORIC BOOKINGS FOR ROOM NR: ", room.getRoomNumber());
                    int countHistoricElements = 0;
                    for (BookingConfirm booking : room.getRoomBookingList()) {
                        if (booking.getToDate().isBefore(LocalDate.now())) {  //If booking is historic
                            System.out.printf("%-3s%s%n", Integer.toString(++countHistoricElements).concat("."), booking);
                        }
                    }
                    if (countHistoricElements == 0) {
                        System.out.println("No historic bookings.");
                    }
                    validateInput = true;
                    System.out.println("Back (Enter)");
                    input.nextLine();
                } else if (methodList.size() > 0) {
                    try {
                        intChoice = Integer.parseInt(menuChoice);  // String -> int
                        validateInput = true;
                        if (intChoice < 1 || intChoice > methodList.size()) {
                            validateInput = false;
                            System.out.println("Choice did not match an alternative. Try again:");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Choice did not match an alternative. Try again:");
                        validateInput = false;
                    }
                    if (validateInput) {
                        for (BookingConfirm booking : room.getRoomBookingList()) {
                            if (methodList.get(intChoice - 1).getUniqueID() == booking.getUniqueID()) {
                                cancelBooking(booking);   //Method call
                                break;
                            }
                        }
                    }
                }

            } while (!validateInput);

        } while (true);
    }

    public void createObjectsSaveToFile() {
        //=============================== INITIALIZE OBJECT COUNT ID'S ================================================
        int[] entityCount = new int[5];
        entityCount[0] = 1; //Rooms
        entityCount[1] = 1; //Customers
        entityCount[2] = 1; //Admins
        entityCount[3] = 1; //BookingID
        entityCount[4] = 1; //Unique booking ID
        save.saveEntityCounts(entityCount);

        //==================================== CREATE ROOMS =====================================================
        roomList.add(new Room(getEntityCounts("Room"), 1, 1));               //1
        roomList.add(new Room(getEntityCounts("Room"), 1, 1));               //2
        roomList.add(new Room(getEntityCounts("Room"), 1, 2));               //3
        roomList.add(new Room(getEntityCounts("Room"), 1, 2));               //4
        roomList.add(new Room(getEntityCounts("Room"), 2, 1));               //5
        roomList.add(new Room(getEntityCounts("Room"), 2, 1));               //6
        roomList.add(new Room(getEntityCounts("Room"), 2, 1));               //7
        roomList.add(new Room(getEntityCounts("Room"), 2, 2));               //8
        roomList.add(new Room(getEntityCounts("Room"), 2, 2));               //9
        roomList.add(new Room(getEntityCounts("Room"), 2, 2));               //10
        roomList.add(new Room(getEntityCounts("Room"), 2, 2));               //11
        roomList.add(new Room(getEntityCounts("Room"), 2, 2));               //12
        roomList.add(new Room(getEntityCounts("Room"), 2, 3));               //13
        roomList.add(new Room(getEntityCounts("Room"), 2, 3));               //14
        roomList.add(new Room(getEntityCounts("Room"), 2, 3));               //15
        roomList.add(new Room(getEntityCounts("Room"), 2, 4));               //16
        roomList.add(new Room(getEntityCounts("Room"), 2, 4));               //17
        roomList.add(new Room(getEntityCounts("Room"), 2, 5));               //18
        roomList.add(new Room(getEntityCounts("Room"), 2, 5));               //19
        roomList.add(new Room(getEntityCounts("Room"), 4, 1));               //20
        roomList.add(new Room(getEntityCounts("Room"), 4, 2));               //21
        roomList.add(new Room(getEntityCounts("Room"), 4, 2));               //22
        roomList.add(new Room(getEntityCounts("Room"), 4, 3));               //23
        roomList.add(new Room(getEntityCounts("Room"), 4, 4));               //24
        roomList.add(new Room(getEntityCounts("Room"), 4, 5));               //25
        for (Room room : roomList) {
            save.saveRoom(room);
        }//========================== CREATE STANDARD PRICE OBJECTS ============================================
        standardList.add(new StandardPrice(1, 999));
        standardList.add(new StandardPrice(2, 1499));
        standardList.add(new StandardPrice(3, 1999));
        standardList.add(new StandardPrice(4, 2999));
        standardList.add(new StandardPrice(5, 4999));
        save.saveStandards(standardList);
        //============================ CREATE BEDS OBJECTS =======================================================
        bedPriceList.add(new BedPrice(1, 1));
        bedPriceList.add(new BedPrice(2, 1.2));
        bedPriceList.add(new BedPrice(4, 1.7));
        save.saveBedPrices(bedPriceList);
        //=============================== CREATE ACCOUNTS =======================================================
        customerList.add(new AccountCustomer(getEntityCounts("Customer"),"Ron Burgundy", "custom", "045125033"));
        customerList.add(new AccountCustomer(getEntityCounts("Customer"),"Anton Göransson", "custom", "0703545036"));
        customerList.add(new AccountCustomer(getEntityCounts("Customer"),"Arnold Svensson", "custom", "0705421876"));
        customerList.add(new AccountCustomer(getEntityCounts("Customer"),"Erik Larsson", "custom", "0704576556"));
        customerList.add(new AccountCustomer(getEntityCounts("Customer"),"Elin Hansson", "custom", "0707676768"));
        customerList.add(new AccountCustomer(getEntityCounts("Customer"),"Lena Karlsson", "custom", "0707676768"));
        adminList.add(new AccountAdmin(getEntityCounts("Admin"),"Admin", "admin")); //Create admin
        customerList.get(5).setCancelledAccount(true); //Set account to cancelled
        save.saveCustomers(customerList); //customerList -> File
        save.saveAdmins(adminList);
        //================================ CREATE BOOKINGS =======================================================
        loadAllRooms();

        LocalDate fromDate1 = LocalDate.of(2019, 3, 12);
        LocalDate toDate1 = LocalDate.of(2019, 3, 13);

        try {    //                room       ,   customer
            double price1 = calculateSingleBookingPrice(fromDate1, toDate1, roomList.get(0));
            bookingDates(roomList.get(0), fromDate1, toDate1, customerList.get(1), price1, getEntityCounts("BookingID"), getEntityCounts("UniqueBookingID"));
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED!1 " + e.getMessage());
        }

        LocalDate fromDate2 = LocalDate.of(2019, 2, 12);
        LocalDate toDate2 = LocalDate.of(2019, 2, 15);
        try {
            double price2 = calculateSingleBookingPrice(fromDate2, toDate2, roomList.get(0));
            bookingDates(roomList.get(0), fromDate2, toDate2, customerList.get(2), price2, getEntityCounts("BookingID"), getEntityCounts("UniqueBookingID"));
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate3 = LocalDate.of(2019, 7, 12);
        LocalDate toDate3 = LocalDate.of(2019, 7, 13);
        try {
            double price3 = calculateSingleBookingPrice(fromDate3, toDate3, roomList.get(0));
            bookingDates(roomList.get(0), fromDate3, toDate3, customerList.get(3), price3, getEntityCounts("BookingID"), getEntityCounts("UniqueBookingID"));
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate4 = LocalDate.of(2019, 5, 12);
        LocalDate toDate4 = LocalDate.of(2019, 5, 18);
        try {
            double price4 = calculateSingleBookingPrice(fromDate4, toDate4, roomList.get(2));
            bookingDates(roomList.get(2), fromDate4, toDate4, customerList.get(4), price4, getEntityCounts("BookingID"), getEntityCounts("UniqueBookingID"));
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate5 = LocalDate.of(2019, 6, 12);
        LocalDate toDate5 = LocalDate.of(2019, 6, 14);
        try {
            double price5 = calculateSingleBookingPrice(fromDate4, toDate4, roomList.get(2));
            bookingDates(roomList.get(2), fromDate5, toDate5, customerList.get(5), price5, getEntityCounts("BookingID"), getEntityCounts("UniqueBookingID"));
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate6 = LocalDate.of(2019, 5, 18);
        LocalDate toDate6 = LocalDate.of(2019, 5, 24);
        try {
            double price6 = calculateSingleBookingPrice(fromDate6, toDate6, roomList.get(0));
            bookingDates(roomList.get(0), fromDate6, toDate6, customerList.get(4), price6, getEntityCounts("BookingID"), getEntityCounts("UniqueBookingID"));
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate7 = LocalDate.of(2019, 2, 1);
        LocalDate toDate7 = LocalDate.of(2019, 2, 5);
        try {
            double price7 = calculateSingleBookingPrice(fromDate7, toDate7, roomList.get(0));
            bookingDates(roomList.get(0), fromDate7, toDate7, customerList.get(4), price7, getEntityCounts("BookingID"), getEntityCounts("UniqueBookingID"));
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate8 = LocalDate.of(2019, 1, 10);
        LocalDate toDate8 = LocalDate.of(2019, 1, 12);
        try {
            double price8 = calculateSingleBookingPrice(fromDate8, toDate8, roomList.get(9));
            bookingDates(roomList.get(9), fromDate8, toDate8, customerList.get(0), price8, getEntityCounts("BookingID"), getEntityCounts("UniqueBookingID"));
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate9 = LocalDate.of(2019, 2, 15);
        LocalDate toDate9 = LocalDate.of(2019, 2, 16);
        try {
            double price9 = calculateSingleBookingPrice(fromDate9, toDate9, roomList.get(9));
            bookingDates(roomList.get(9), fromDate9, toDate9, customerList.get(0), price9, getEntityCounts("BookingID"), getEntityCounts("UniqueBookingID"));
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate10 = LocalDate.of(2019, 1, 17);
        LocalDate toDate10 = LocalDate.of(2019, 1, 18);
        try {
            double price10 = calculateSingleBookingPrice(fromDate10, toDate10, roomList.get(9));
            bookingDates(roomList.get(9), fromDate10, toDate10, customerList.get(0), price10, getEntityCounts("BookingID"), getEntityCounts("UniqueBookingID"));
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate11 = LocalDate.of(2019, 2, 16);
        LocalDate toDate11 = LocalDate.of(2019, 2, 18);
        try {
            double price11 = calculateSingleBookingPrice(fromDate11, toDate11, roomList.get(9));
            bookingDates(roomList.get(9), fromDate11, toDate11, customerList.get(0), price11, getEntityCounts("BookingID"), getEntityCounts("UniqueBookingID"));
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate12 = LocalDate.of(2019, 1, 11); //19-2-20
        LocalDate toDate12 = LocalDate.of(2019, 1, 12);   //19-2-21
        try {
            double price12 = calculateSingleBookingPrice(fromDate12, toDate12, roomList.get(10));
            bookingDates(roomList.get(10), fromDate12, toDate12, customerList.get(0), price12, getEntityCounts("BookingID"), getEntityCounts("UniqueBookingID"));
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }
        for (Room room : roomList) {
            save.saveRoom(room);
        }
    }

    private void loadAllRooms() {  // File -> roomList (Creates entire new roomList from file)
        ArrayList<Object> data = new ArrayList<>();
        roomList.clear();
        //data.clear();
        int roomNumber = 1;
        int standard;
        int beds;
        ArrayList<BookingConfirm> roomBookingList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            try {
                data.clear();
                data = load.loadRoom(roomNumber);
                if (data.size() == 4) {
                    standard = (int) data.get(1);
                    beds = (int) data.get(2);
                    roomBookingList = (ArrayList<BookingConfirm>) data.get(3);
                    roomList.add(new Room(roomNumber, beds, standard, roomBookingList));
                }
            } catch (NullPointerException e) {
                //System.out.println(e.getMessage());
            }
            roomNumber++;
        }
    }

    private void updateAllRooms() { // File -> roomList (Updates the rooms in roomList)
        ArrayList<Object> data = new ArrayList<>();
        int standard;
        int beds;
        ArrayList<BookingConfirm> roomBookingList = new ArrayList<>();

        for (Room room : roomList) {
            try {
                data.clear();
                data = load.loadRoom(room.getRoomNumber());
                if (data.size() == 4) {
                    standard = (int) data.get(1);
                    beds = (int) data.get(2);
                    roomBookingList = (ArrayList<BookingConfirm>) data.get(3);
                    room.setBeds(beds);
                    room.setStandard(standard);
                    room.setRoomBookingList(roomBookingList);
                }
            } catch (NullPointerException e) {
                //System.out.println(e.getMessage());
            }
        }
    }

    private void updateRoom(int roomNumber) {  // File -> roomList (Updates specific room in roomList)
        ArrayList<Object> data = new ArrayList<>();
        int standard;
        int beds;
        ArrayList<BookingConfirm> roomBookingList = new ArrayList<>();

        data = load.loadRoom(roomNumber);
        if (data.size() == 4) {
            standard = (int) data.get(1);
            beds = (int) data.get(2);
            roomBookingList = (ArrayList<BookingConfirm>) data.get(3);
            for (Room room : roomList) {
                if (room.getRoomNumber() == roomNumber) {
                    room.setBeds(beds);
                    room.setStandard(standard);
                    room.setRoomBookingList(roomBookingList);
                }
            }
        }
    }

    private void loadAllCustomers() { // File -> customerList (Creates entire new customerList from file)
        ArrayList<AccountCustomer> customersFromFile = new ArrayList<>();
        customerList.clear();
        try {
            customersFromFile.clear();
            customersFromFile = load.loadCustomers();
            if (!customersFromFile.isEmpty()) {
                for (AccountCustomer customer : customersFromFile) {
                    customerList.add(new AccountCustomer(customer.getName(), customer.getPassword(), customer.isCancelledAccount(),
                            customer.getAccountID(), customer.getPhoneNumber()));
                }
            } else {
                System.out.println("No customers in the file system.");
            }
        } catch (NullPointerException e) {
            //System.out.println(e.getMessage());
        }
    }

    private void updateCustomerList() { // File -> customerList (Updates the existing customer's info in customerList)
        ArrayList<AccountCustomer> customersFromFile = new ArrayList<>();
        try {
            customersFromFile.clear();
            customersFromFile = load.loadCustomers();
            if (!customersFromFile.isEmpty()) {
                for (AccountCustomer custFromFile : customersFromFile) {
                    for (AccountCustomer custFromApp : customerList) {
                        if (custFromFile.getAccountID().equals(custFromApp.getAccountID())) {
                            custFromApp.setName(custFromFile.getName());
                            custFromApp.setPassword(custFromFile.getPassword());
                            custFromApp.setCancelledAccount(custFromFile.isCancelledAccount());
                            custFromApp.setPhoneNumber(custFromFile.getPhoneNumber());
                        }
                    }
                }
            } else {
                System.out.println("No customers in the file system.");
            }
        } catch (NullPointerException e) {
            //System.out.println(e.getMessage());
        }
    }

    public void loadAllAdmins() { // File -> adminList (Creates entire new adminList from file)
        ArrayList<AccountAdmin> adminsFromFile = new ArrayList<>();
        adminList.clear();
        try {
            adminsFromFile.clear();
            adminsFromFile = load.loadAdmins();
            if (!adminsFromFile.isEmpty()) {
                for (AccountAdmin admin : adminsFromFile) {
                    adminList.add(new AccountAdmin(admin.getName(), admin.getPassword(), admin.isCancelledAccount(),
                            admin.getAccountID()));
                }
            } else {
                System.out.println("No admins in the file system.");
            }
        } catch (NullPointerException e) {
            //System.out.println(e.getMessage());
        }
    }

    private void loadAllBedPrices() { // File -> bedPriceList (Creates entire new bedPriceList from file)
        ArrayList<BedPrice> bedPricesFromFile = new ArrayList<>();
        bedPriceList.clear();
        try {
            bedPricesFromFile.clear();
            bedPricesFromFile = load.loadBedPrices();
            if (!bedPricesFromFile.isEmpty()) {
                for (BedPrice bedPrice : bedPricesFromFile) {
                    bedPriceList.add(new BedPrice(bedPrice.getNumberOfBeds(), bedPrice.getConstant()));
                }
            } else {
                System.out.println("No bed prices in the file system.");
            }
        } catch (NullPointerException e) {
            //System.out.println(e.getMessage());
        }
    }

    private void updateBedPriceList() { // File -> bedPriceList (Updates the existing bed prices)
        ArrayList<BedPrice> bedPricesFromFile = new ArrayList<>();
        try {
            bedPricesFromFile.clear();
            bedPricesFromFile = load.loadBedPrices();
            if (!bedPricesFromFile.isEmpty()) {
                for (BedPrice bedPriceFromFile : bedPricesFromFile) {
                    for (BedPrice bedPriceFromApp : bedPriceList) {
                        if (bedPriceFromFile.getNumberOfBeds() == bedPriceFromApp.getNumberOfBeds()) {
                            bedPriceFromApp.setConstant(bedPriceFromFile.getConstant());
                        }
                    }
                }
            } else {
                System.out.println("No bed prices in the file system.");
            }
        } catch (NullPointerException e) {
            //System.out.println(e.getMessage());
        }
    }

    private void loadAllStandards() { // File -> standardList (Creates entire new standardList from file)
        ArrayList<StandardPrice> standardsFromFile = new ArrayList<>();
        standardList.clear();
        try {
            standardsFromFile.clear();
            standardsFromFile = load.loadStandards();
            if (!standardsFromFile.isEmpty()) {
                for (StandardPrice standard : standardsFromFile) {
                    standardList.add(new StandardPrice(standard.getName(), standard.getPrice()));
                }
            } else {
                System.out.println("No room standards in the file system.");
            }
        } catch (NullPointerException e) {
            //System.out.println(e.getMessage());
        }
    }

    private void updateStandardList() { // File -> standarList (Updates the existing room standard's price constant values)
        ArrayList<StandardPrice> standardsFromFile = new ArrayList<>();
        try {
            standardsFromFile.clear();
            standardsFromFile = load.loadStandards();
            if (!standardsFromFile.isEmpty()) {
                for (StandardPrice standardFromFile : standardsFromFile) {
                    for (StandardPrice standardFromApp : standardList) {
                        if (standardFromFile.getName() == standardFromApp.getName()) {
                            standardFromApp.setPrice(standardFromFile.getPrice());
                        }
                    }
                }
            } else {
                System.out.println("No room standards in the file system.");
            }
        } catch (NullPointerException e) {
            //System.out.println(e.getMessage());
        }
    }

    private int getEntityCounts (String ID) { //Entity counts from file. Increments and saves after every use.
        int[] theEntities = new int[5];
        int theID = 0;

        theEntities = load.loadEntityCounts();
        switch (ID) {
            case "Room":
                theID = theEntities[0]++;
                break;
            case "Customer":
                theID = theEntities[1]++;
                break;
            case "Admin":
                theID = theEntities[2]++;
                break;
            case "BookingID":
                theID = theEntities[3]++;
                break;
            case "UniqueBookingID":
                theID = theEntities[4]++;
                break;
            default:
                throw new IllegalArgumentException(
                        "Unable to create " + ID + ". Error when loading data from server files.");
        }
        save.saveEntityCounts(theEntities);
        return theID;
    }
}