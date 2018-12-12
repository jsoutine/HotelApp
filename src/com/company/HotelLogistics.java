package com.company;


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
    private ArrayList<BedPrices> bedConstantList = new ArrayList<>();
    private ArrayList<StandardPrice> standardList = new ArrayList<>();
    private Scanner input = new Scanner(System.in);


    //2.1.
    public void logIn(String id, String password) {
        boolean match = false;
        if (id.matches("C\\d+") || id.matches("c\\d+")) {
            for (AccountCustomer customer : customerList) {
                if (customer.getAccountID().equalsIgnoreCase(id) && customer.getPassword().equals(password) && !customer.isCancelledAccount()) {
                    match = true;
                    System.out.println("\nWelcome " + customer.getName() + "\n");
                    customerMainMenu(customer);
                    return;
                } else {
                    match = false;
                }
            }
        } else if (id.matches("A\\d+") || id.matches("a\\d+")) {
            for (AccountAdmin admin : adminList) {
                if (admin.getAccountID().equalsIgnoreCase(id) && admin.getPassword().equals(password) && !admin.isCancelledAccount()) {
                    match = true;
                    adminMainMenu(admin);
                    return;
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
    public boolean logOut() {   //This method returns true if user choose to log out, and false if not.
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
    public void adminMainMenu(Account loggedInAccount) {
        String menuChoice;
        boolean logout = false;
        do {
            System.out.printf("%n%s%n%s%s%s%n%s%n%s%n%s%n%s%n%s%n",
                    "3. ADMIN MAIN MENU",
                    "Logged in as admin (name: ", loggedInAccount.getName(), ")",
                    "1. Customers",
                    "2. Rooms",
                    "3. Bookings",
                    "4. Edit account access",
                    "0. Log out");
            do {
                menuChoice = input.nextLine();
                switch (menuChoice) {
                    case "1":
                        adminCustomers(loggedInAccount);
                        break;
                    case "2":
                        adminRooms(loggedInAccount);
                        break;
                    case "3":
                        System.out.println("Method still under construction");
                        viewBookings(loggedInAccount);
                        break;
                    case "4":
                        System.out.println("Method still under construction");
                        //adminEditAccess();
                        break;
                    case "0":
                        logout = logOut();
                        break;
                    default:
                        System.out.println("Invalid option. Type a choice 0-4:");
                        break;
                }
            }
            while (!menuChoice.equals("1") && !menuChoice.equals("2") && !menuChoice.equals("3") && !menuChoice.equals("4") && !menuChoice.equals("0"));
        } while (!logout);
    }

    //3.1.
    public void adminCustomers(Account loggedInAccount) {  //UNDER CONSTRUCTION
        ArrayList<AccountCustomer> methodList = new ArrayList<>();
        String menuChoice;
        boolean validateInput;
        int intChoice = 0;  //for choosing a customer

        do {
            System.out.println("3.1. CUSTOMERS");

            int countElements = 0;
            for (AccountCustomer x : customerList) {
                if (!x.isCancelledAccount()) {     //If account is not admin, and nor cancelled; add to new ArrayList (methodList)
                    methodList.add(x);
                    countElements++;
                    System.out.printf("%-3s%s%n", Integer.toString(countElements).concat("."), x);
                }
            }
            if (methodList.isEmpty()) {
                System.out.println("Customer list is empty.");

            } else {
                System.out.printf("%s%n",
                        "1-n. Choose customer");
            }
            System.out.printf("%s%n%s%n%s%n",
                    "A.   Add customer",
                    "C.   Cancelled accounts",
                    "0.   Back");
            do { // do this while input is not numeric, or while input does not match accounts (1-n) or 0, A or C.
                menuChoice = input.nextLine();
                //=============================== ADD CUSTOMER ====================================================
                if (menuChoice.equalsIgnoreCase("A")) {
                    System.out.println("INVOKE ADD CUSTOMER METHOD??");
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
                        for (int i = 0; i < customerList.size(); i++) {
                            if (methodList.get(intChoice - 1).getAccountID().equalsIgnoreCase(customerList.get(i).getAccountID())) {    //Find the corresponding account in the original list.
                                adminCustomer(customerList.get(i));   //Method call
                            }
                        }
                    }
                }
            } while (!validateInput);
        } while (true); //Always loop, until menuChoice = 0 -> Return

    }

    //3.1.2.  Ev bara använda 4. istället (Då krävs att metoden känner av om customer/admin)
    public void adminCustomer(AccountCustomer customer) {   //UNDER CONSTRUCTION
        System.out.printf("%s%n%s%n%s%n%s%n",
                "3.1.2. CUSTOMER (Admin level)",
                customer,
                "NOT COMPLETE METHOD, 'Enter' will for now lead directly to 'Make booking' for this customer",
                "Back (Enter)");
        input.nextLine();
        makeBooking(customer);
    }

    //3.1.3.
    public void adminCancelledAccounts(Account loggedInAccount) {  //UNDER CONSTRUCTION
        ArrayList<Account> cancelledAccounts = new ArrayList<>();
        System.out.println("3.1.3. CANCELLED ACCOUNTS");
        int countElements = 0;
        for (AccountCustomer x : customerList) {
            if (x.isCancelledAccount()) {  //If account is cancelled
                cancelledAccounts.add(x);
                countElements++;
                System.out.printf("%-3s%s%n", Integer.toString(countElements).concat("."), x);
            }
        }
        if (cancelledAccounts.isEmpty()) {
            System.out.println("No cancelled accounts.");

        }
        System.out.println("Back (Enter)");
        input.nextLine();
    }

    public void editCustomerInfo(AccountCustomer loggedInAccount) {
        int choice;

        System.out.println("---EDIT USER INFORMATION---");
        System.out.println("1. Name");
        System.out.println("2. Phonenumber");
        System.out.println("3. Password");
        System.out.println("4. Remove account");
        System.out.println("0. Back");
        choice = input.nextInt();

        switch (choice) {
            case 1:
                changeName(loggedInAccount);
                break;
            case 2:
                changePhoneNr(loggedInAccount);
                break;
            case 3:
                changePassword(loggedInAccount);
                break;
            case 4:
                removeAccount(loggedInAccount);
                break;
            default:
                System.out.println("Faulty input recognized. Try Again!");
                editCustomerInfo(loggedInAccount);
                break;
        }
    }

    private void changeName(AccountCustomer loggedInAccount) {
        int choice;

        System.out.println("---CHANGE NAME---");
        System.out.println("Name currently assigned: " + loggedInAccount.getName());
        System.out.println("1. Change name");
        System.out.println("0. Back");
        choice = input.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Enter new name: ");
                String newName = input.nextLine();
                newName = input.nextLine();
                loggedInAccount.setName(newName);
                System.out.println("New name: " + loggedInAccount.getName());
                System.out.println("Are you happy with the newly entered name?");
                System.out.println("(Y)es or (N)o");
                String yesOrNo = input.next();
                if (yesOrNo.equalsIgnoreCase("Y")) {
                    System.out.println("Very well, then let's return to the previous menu");
                    editCustomerInfo(loggedInAccount);
                } else if (yesOrNo.equalsIgnoreCase("N")) {
                    System.out.println("Returning to previous menu!");
                    changeName(loggedInAccount);
                }
                break;
            case 0:
                editCustomerInfo(loggedInAccount);
                break;
            default:
                System.out.println("Faulty input recognized. Let's try again!");
                changeName(loggedInAccount);
                break;
        }
    }

    private void changePhoneNr(AccountCustomer loggedInAccount) {

        String choice;

        System.out.println("---CHANGE PHONENUMBER---");
        System.out.println("Phonenumber currently assigned: " + loggedInAccount.getPhoneNumber());
        System.out.println("1. Change current phonenumber");
        System.out.println("0. Back");

        choice = input.nextLine();
        choice = input.nextLine();

        switch (choice) {
            case "1":
                System.out.println("Required format: Start with 0, then enter a 9-digit number sequence.");
                System.out.println("Enter new phonenumber: ");
                String newNr = input.nextLine();
                String regexStr = "^[0-9]{10}$";

                if (newNr.matches(regexStr)) {
                    loggedInAccount.setPhoneNumber(newNr);
                    System.out.println("New phonenumber: " + loggedInAccount.getPhoneNumber());
                    System.out.println("Are you happy with your newly entered phonenumber?");
                    System.out.println("(Y)es or (N)o");
                    String yesOrNo = input.next();
                    if (yesOrNo.equalsIgnoreCase("Y")) {
                        System.out.println("Very well, then lets return to the previous menu");
                        editCustomerInfo(loggedInAccount);
                    } else if (yesOrNo.equalsIgnoreCase("N")) {
                        changePhoneNr(loggedInAccount);
                    }
                } else {
                    System.out.println("Format not followed. Try again!");
                    //changePhoneNr(loggedInAccount);
                }
                break;

            case "0":
                editCustomerInfo(loggedInAccount);
                break;

            default:
                System.out.println("Faulty input recognized. Let's try again!");
                changePhoneNr(loggedInAccount);
                break;
        }
    }

    private void changePassword(AccountCustomer loggedInAccount) {
        System.out.println("---CHANGE PASSWORD---");
        System.out.println("Current password: " + loggedInAccount.getPassword());
        System.out.println("1. Change current password");
        System.out.println("0. Back");
        int choice = input.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Enter new password: ");
                String newPwd = input.nextLine();
                newPwd = input.nextLine();

                System.out.println("New password: " + newPwd);
                System.out.println("Are you happy with your newly entered password?");
                System.out.println("(Y)es or (N)o");
                String yesOrNo = input.next();

                if (yesOrNo.equalsIgnoreCase("Y")) {
                    System.out.println("Very well, then let's return to the previous menu");
                    editCustomerInfo(loggedInAccount);
                    loggedInAccount.setPassword(newPwd);
                } else if (yesOrNo.equalsIgnoreCase("N")) {
                    changePassword(loggedInAccount);
                }
                break;

            case 0:
                editCustomerInfo(loggedInAccount);
                break;
            default:
                System.out.println("Faulty input recognized. Let's try again!");
                changePassword(loggedInAccount);
                break;
        }
    }

    private void removeAccount(AccountCustomer loggedInAccount) {

        System.out.println("Do you truly wish to delete your account?");
        System.out.println("(Y)es or (N)o");
        String yesOrNo = input.next();
        if (yesOrNo.equalsIgnoreCase("Y")) {
            System.out.println("Enter your password to verify deletion of account: ");
            String pwCheck = input.nextLine();
            pwCheck = input.nextLine();
            if (pwCheck.matches(loggedInAccount.getPassword())) {
                loggedInAccount.setCancelledAccount(true);
            } else {
                System.out.println("Wrong password has been entered. Try again!");
                removeAccount(loggedInAccount);
            }
        } else if (yesOrNo.equalsIgnoreCase("N")) {
            System.out.println("Returning to previous menu!");
            editCustomerInfo(loggedInAccount);
        } else {
            System.out.println("(Y)es or (N)o hasn't been entered. Try again!");
        }
    }

    //3.2. (listRooms)

    public void adminRooms(Account loggedInAccount) {
        String menuChoice;
        boolean validateInput = false;
        int roomSelect;  // selects room

        do {
            System.out.println("3.2 ALL ROOMS IN THE SYSTEM");

            for (Room room : roomList) {
                System.out.println(room);
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
                        System.out.println("This method does not exist yet. Press 0 to go back.");
                        validateInput = true;
                        break;

                    case "E":
                        System.out.println("This method does not exist yet. Press 0 to go back.");
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
                            }
                        } catch (NumberFormatException e) {
                            System.out.printf("Please enter an option, or valid number between 1 and %d. Try again or press \"0\" to go back.%n", roomList.size());
                            validateInput = true;
                        }
                }

            } while (validateInput); // loops the room menu

        } while (true); //Always loop, until menuChoice = 0 -> Return

    }

    //3.2.4 (edit price)

   /*
   public void editprices() {


        int choice;

        do {
            System.out.printf("%s%n%s%n%s%n%s%n%s%n",
                    "Edit price for",
                    "Type a choice:",
                    "1. standards",
                    "2. beds.",
                    "0. Back.");
            choice = input.nextInt();

            do {
                switch (choice) {
                    case 1:

                        System.out.printf("%s%n%s%n",
                                "choose standard 1-5",
                                "Type a choice:");

                        break;
                    case 2:
                        System.out.printf("%s%n%s%n",
                                "choose beds 1, 2 or 4",
                                "Type a choice:");

                        break;

                    case 0:
                        return;
                    break;

                    default:
                        System.out.println("Please enter 0-2");
                        break;


                }

            }

        }
    }


    }
*/

    //4. Ev slå ihop med 3.1.2. (Då krävs att 4. känner av om customer/admin)
    public void customerMainMenu(AccountCustomer loggedInAccount) {
        String menuChoice;
        boolean logout = false;
        do {
            System.out.printf("%s%n%s%s%n%s%n%s%n%s%n%s%n",
                    "4. CUSTOMER MAIN MENU",
                    "Logged in as: ", loggedInAccount.getName(),
                    "1. Make a booking, or view available",
                    "2. View your bookings",
                    "3. Edit account info",
                    "0. Log out.");
            do {
                menuChoice = input.nextLine();
                switch (menuChoice) {
                    case "1":
                        //System.out.println("4.1.");
                        makeBooking(loggedInAccount);
                        break;
                    case "2":
                        //System.out.println("4.2.");
                        viewBookings(loggedInAccount);
                        break;
                    case "3":
                        //System.out.println("4.3.");
                        editCustomerInfo(loggedInAccount);
                        break;
                    case "0":
                        logout = logOut();
                        break;
                    default:
                        System.out.println("Invalid option. Type a a choice 0-3:");
                        break;
                }
            }
            while (!menuChoice.equals("1") && !menuChoice.equals("2") && !menuChoice.equals("3") && !menuChoice.equals("0"));
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

        /*System.out.printf("%s%n%s%n%s%n%s%n",
                "Are you planning on booking one room, or more than one room?",
                "1. One room",
                "2. More than one room",
                "0. Back");
        do{
            cancel = false;
            answer = input.nextLine();
            if (answer.equals("1")) {
                oneRoom = true;
                System.out.println("You wish to book one room.");
                validateInput = true;
            } else if (answer.equals("2")) {
                oneRoom = false;
                validateInput = true;
                System.out.println("You wish to book more than one room.");
            } else if (answer.equals("0") || answer.equalsIgnoreCase("O")) {
                cancel = true;
                validateInput = true;
            } else {
                System.out.println("Invalid input. Please try again:");
                validateInput = false;
            }
        }while (!validateInput);*/

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
                    System.out.println(e.getMessage() + ". Please try again:");
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
                        System.out.println(e.getMessage() + ". Please try again:");
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
                    if (beds < 1 || beds > 4) {   //WHAT SHOULD THE MAX NUMBER OF BEDS FOR A ROOM BE???
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
                    if (standard < 1 || standard > 5) {   //WHAT SHOULD THE MAX NUMBER OF BEDS FOR A ROOM BE???
                        System.out.println("Invalid standard. Must be 1-5. Try again.");
                        validateInput = false;
                    } else {
                        validateInput = true;
                    }
                }
            } while (!validateInput);
        }
        if (!cancel) {     // SE ÖVER IGEN!!!!!!!!!!!!!!!!!!!!!!!!
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
                if (!oneRoom || matchingResults.isEmpty()) {
                    if (oneRoom) {
                        System.out.println("Unfortunately no perfect matches. Here are some other alternatives that might be of interest: ");
                    }
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
    public void makeBooking(AccountCustomer concernedAccount) {
        System.out.println("4.1. MAKE BOOKING, OR VIEW AVAILABLE");
        ArrayList<BookingSearch> matchingResults = new ArrayList<>();
        ArrayList<BookingSearch> addedBookings = new ArrayList<>();
        String answer;
        int countElements;
        int bookingChoice = 0;
        boolean validateInput;
        boolean proceed = false;
        boolean cancel = false;
        //boolean lastMinute;
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
                lastMinute(booking);                 //Determine if last minute. If so; adjust to lat minute prices.
            }
        }

        if (!cancel) {
            do {

                if (matchingResults.isEmpty()) {
                    System.out.println("No results" + "\n0. Back");
                } else {
                    countElements = 0;
                    for (BookingSearch booking : matchingResults) {
                        System.out.printf("%-4s%s%n", Integer.toString(++countElements).concat("."), booking);
                    }
                    if (oneRoom) {   //If booking one room
                        System.out.println("1-n: Make a booking from the list. \n0. Cancel. No booking will be made.");
                    } else {      //If booking more than one room
                        System.out.printf("%s%s%s%s%s%n%s%n%s%s%s%n%s%n",
                                "Added rooms: ", addedBookings.size(), ". Sum: ", calculateSumBookingPrice(addedBookings), " SEK.",
                                "1-n: Add a room from the list to your booking.",
                                "P. Proceed to make booking of ", addedBookings.size(), " added rooms.",
                                "0. Cancel. No booking will be made.");
                    }
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
                                System.out.println("This room is alredy added to your booking. \nBack (Enter)");
                                input.nextLine();
                                validateInput = true;
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

                                        matchingResults.get(bookingChoice - 1).setAdded(true);  //Could add to addedList directly.
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
            } while (!proceed); //Maybe add !cancel
        }

        if (!cancel) {   //Confirm booking
            if (oneRoom) {
                matchingResults.get(bookingChoice - 1).setAdded(true);
            }
            for (BookingSearch booking : addedBookings) {  //For nicer display, don't show added here, because every item is added at this step.
                booking.setAdded(false);
            }

            System.out.println("Make booking for:");
            countElements = 0;
            for (BookingSearch booking : addedBookings) {
                //lastMinute(booking);
                System.out.printf("%-4s%s%n", Integer.toString(++countElements).concat("."), booking);
            }
            System.out.printf("%s%n%s%n", "Y. Yes, confirm this booking.", "N. No. Cancel booking process.");

            //System.out.printf("%s%n%s%n%s%n%s%n", "Make a booking for: ", matchingResults.get(bookingChoice - 1), "Y. Yes", "N. No. Cancel booking process.");
            do {
                answer = input.nextLine();
                switch (answer) {
                    case "Y":
                    case "y":
                        try {    // Maybe simplify, since basically: bookingDates(matchingResults)

                            for (BookingSearch booking : addedBookings) {
                                bookingDates(booking.getRoom(), booking.getFromDate(), booking.getToDate(), concernedAccount, booking.getPrice());
                            }
                           /* bookingDates(matchingResults.get(bookingChoice - 1).getRoom(),
                                    matchingResults.get(bookingChoice - 1).getFromDate(), matchingResults.get(bookingChoice - 1).getToDate(), concernedAccount, matchingResults.get(bookingChoice - 1).getPrice());*/
                            validateInput = true;
                            System.out.println("Booking succesful! BABABABAB");
                        } catch (IllegalArgumentException e) {
                            System.out.println("BOOKING FAILED!\n" + e.getMessage());
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
            System.out.println("Booking stage cancelled. No booking made" + "\nBack (Enter)");
            input.nextLine();
        }
    }

    //Part of 4.1.
    public boolean checkDates(Room room, LocalDate fromDate, LocalDate toDate) {  //Kan användas för att boka, eller för att sortera bokningar i kronologisk tids-ordning.
        boolean match = false;
        if (room.getRoomBookingList().isEmpty()) {                                                  //Om bokningslistan för rummet är tom.
            match = true;
        } else if (room.getRoomBookingList().size() == 1) {                                        //Om bara finns en bokning i listan
            if (toDate.isEqual(room.getRoomBookingList().get(0).getFromDate()) ||                      // Om utchek är samma dag som existerande incheck
                    toDate.isBefore(room.getRoomBookingList().get(0).getFromDate())) {                 //Om utcheck är innan existerande incheck
                match = true;
            } else if (fromDate.isEqual(room.getRoomBookingList().get(0).getToDate()) ||      // Om inchek är samma dag som existerande utcheck.
                    fromDate.isAfter(room.getRoomBookingList().get(0).getToDate())) {          //Om incheck är efter existerande utcheck.
                match = true;
            } else {
                match = false;
            }

        } else {
            for (int i = 0; i < room.getRoomBookingList().size(); i++) {
                
                if (i == 0) {
                    if (toDate.equals(room.getRoomBookingList().get(i).getFromDate()) ||    // Om index är 0 && Om utchek är samma dag som existerande incheck || utcheck är innan existerande incheck
                            toDate.isBefore(room.getRoomBookingList().get(i).getFromDate())) {
                     match = true;
                     break;
                    } else if ((fromDate.equals(room.getRoomBookingList().get(i).getToDate())   ||    fromDate.isAfter(room.getRoomBookingList().get(i).getToDate()))    &&
                            (toDate.equals(room.getRoomBookingList().get(1).getFromDate())  ||    toDate.isBefore(room.getRoomBookingList().get(1).getFromDate()))) {
                        match = true;
                        break;
                    }

                } else if ((i > 0) && (i < room.getRoomBookingList().size() - 1)) {                      // Om index är mer än 0 && index inte pekar på det sista objektet i listan.
                    if ((fromDate.equals(room.getRoomBookingList().get(i).getToDate()) ||                // Om inchek är samma dag som existerande utcheck.
                            fromDate.isAfter(room.getRoomBookingList().get(i).getToDate())) &&           // Om inckeck är efter existerande utceck &&
                            (toDate.equals(room.getRoomBookingList().get(i + 1).getFromDate()) ||        // Om utchek är samma dag som nästa existerande incheck
                                    toDate.isBefore(room.getRoomBookingList().get(i + 1).getFromDate()))) {  //Om utcheck är före nästa existerande incheck
                        match = true;
                        break;
                    }
                } else if (i == room.getRoomBookingList().size() - 1) {                      // If index points to last item in list.
                    if (fromDate.equals(room.getRoomBookingList().get(i).getToDate()) ||      // Om inchek är samma dag som existerande utcheck.
                            fromDate.isAfter(room.getRoomBookingList().get(i).getToDate())) {   //Om incheckning är efter existerande utcheck i.
                        match = true;
                    } else {
                        match = false;
                    }
                }
            }
        }
        return match;
    }

    //Part of 4.1.
    public void bookingDates(Room room, LocalDate fromDate, LocalDate toDate, AccountCustomer customer, double price) {  //Kan användas för att boka, eller för att sortera bokningar i kronologisk tids-ordning.
        if (room.getRoomBookingList().isEmpty()) {                                                  //Om bokningslistan för rummet är tom.
            room.getRoomBookingList().add(new BookingConfirm(room, fromDate, toDate, customer, price));
            System.out.println("Booking successful. Code 1");
            return;
        } else if (room.getRoomBookingList().size() == 1) {                                        //Om bara finns en bokning i listan
            if (toDate.isEqual(room.getRoomBookingList().get(0).getFromDate()) ||                      // Om utchek är samma dag som existerande incheck
                    toDate.isBefore(room.getRoomBookingList().get(0).getFromDate())) {                 //Om utcheck är innan existerande incheck
                room.getRoomBookingList().add(0, new BookingConfirm(room, fromDate, toDate, customer, price));     //Lägg till innan existerande bokning i listan
                System.out.println("Booking successful. Code 2");
                return;
            } else if (fromDate.isEqual(room.getRoomBookingList().get(0).getToDate()) ||      // Om inchek är samma dag som existerande utcheck.
                    fromDate.isAfter(room.getRoomBookingList().get(0).getToDate())) {          //Om incheck är efter existerande utcheck.
                room.getRoomBookingList().add(new BookingConfirm(room, fromDate, toDate, customer, price));             //Lägg till efter existerande bokning i listan.
                System.out.println("Booking successful. Code 3");
                return;
            } else {
                throw new IllegalArgumentException(
                        "Room " + room.getRoomNumber() + " Dates: " + fromDate + " to " + toDate + ": Not available at chosen date/dates. \nOnly one booking for this room in system, which collides with booking.");
            }
        } else {
            for (int i = 0; i < room.getRoomBookingList().size(); i++) {

                if (i == 0) {
                    if (toDate.equals(room.getRoomBookingList().get(i).getFromDate()) ||    // Om index är 0 && Om utchek är samma dag som existerande incheck || utcheck är innan existerande incheck
                            toDate.isBefore(room.getRoomBookingList().get(i).getFromDate())) {
                        room.getRoomBookingList().add(0, new BookingConfirm(room, fromDate, toDate, customer, price));       //Lägg till innan existerande bokning i listan
                        System.out.println("Booking successful. Code 4 " + " Iteration " + i);
                        return;
                    //} else if (i == 0 && room.getRoomBookingList().size() > 1) {
                    } else if ((fromDate.equals(room.getRoomBookingList().get(i).getToDate())   ||    fromDate.isAfter(room.getRoomBookingList().get(i).getToDate()))    &&
                            (toDate.equals(room.getRoomBookingList().get(1).getFromDate())  ||    toDate.isBefore(room.getRoomBookingList().get(1).getFromDate()))) {
                        room.getRoomBookingList().add(i +1, new BookingConfirm(room, fromDate, toDate, customer, price));
                        return;
                    }
                    /*else {
                        throw new IllegalArgumentException(
                                "Room " + room.getRoomNumber() + " Dates: " + fromDate + " to " + toDate + ": In conflict with other booking. Iteration: " + i + " Code A");
                    }*/
                    //START
                } else if ((i > 0) && (i < room.getRoomBookingList().size() - 1)) {                                                                 // Om index är mer än 0 && index nite pekar på det sista objektet i listan.
                    if ((fromDate.equals(room.getRoomBookingList().get(i).getToDate()) ||      // Om inchek är samma dag som existerande utcheck.
                            fromDate.isAfter(room.getRoomBookingList().get(i).getToDate())) &&
                            (toDate.equals(room.getRoomBookingList().get(i + 1).getFromDate()) ||                      // Om utchek är samma dag som existerande incheck
                                    toDate.isBefore(room.getRoomBookingList().get(i + 1).getFromDate()))) {  //Om incheck är är efter existerande utcheck i, och före existerande incheck i+1.)
                        room.getRoomBookingList().add(i + 1, new BookingConfirm(room, fromDate, toDate, customer, price));                       //Lägg till efter bokning "i" (Finns ledigt mellan bokning i och bokning i+1
                        System.out.println("Booking successful. Code 5 " + " Iteration " + i);
                        return;
                    } /*else {
                        throw new IllegalArgumentException(
                                "Room " + room.getRoomNumber() + " Dates: " + fromDate + " to " + toDate + ": In conflict with other booking. Iteration: " + i + " Code B");
                    }*/
                } else if (i == room.getRoomBookingList().size() - 1) {                      // If index points to last item in list.
                    if (fromDate.equals(room.getRoomBookingList().get(i).getToDate()) ||      // Om inchek är samma dag som existerande utcheck.
                            fromDate.isAfter(room.getRoomBookingList().get(i).getToDate())) {   //Om incheckning är efter existerande utcheck i.
                        room.getRoomBookingList().add(new BookingConfirm(room, fromDate, toDate, customer, price));
                        System.out.println("Booking successful. Code 6 " + " Iteration " + i);
                        return;
                    } else {
                        throw new IllegalArgumentException(
                                "Room " + room.getRoomNumber() + " Dates: " + fromDate + " to " + toDate + ": In conflict with other booking. Iteration: " + i + " Code C");
                    }
                }
            }
        }
    }

    public double calculateSingleBookingPrice(LocalDate fromDate, LocalDate toDate, Room room) {  //Used to determine the price of a specific room item booking.
        double price;
        double bedsConstant = 1;
        long periodDays = ChronoUnit.DAYS.between(fromDate, toDate);
        double standardPrice = standardList.get(room.getStandard() - 1).getPrice();  //May throw IndexOutOfBoundsException if no match??
        for (BedPrices beds : bedConstantList) {
            if (room.getBeds() == beds.getNumberOfBeds()) {  //If number of beds in the room equals
                bedsConstant = beds.getConstant();
                break;
            }
        }
        price = periodDays * standardPrice * bedsConstant;   //nights x standard x beds
        return price;
    }

    public double calculateSumBookingPrice(ArrayList<BookingSearch> bookingList) {  //Used to calculate the sum of an entire booking.
        int sum = 0;
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
    public void viewBookings(Account loggedIn) {  //3 displaying options: 1: Admin sees all booking 2: Admin sees customer specific bookings 3: Customer sees customer specific bookings
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
                    if (loggedIn instanceof AccountAdmin) {
                        methodList.add(booking);
                    } else if (booking.getCustomer().getAccountID().equalsIgnoreCase(loggedIn.getAccountID())) {
                        methodList.add(booking);
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
                System.out.printf("%-4s%s%n", "0.", "Back (Enter)");
                do {
                    menuChoice = input.nextLine();
                    if (menuChoice.equals("0") || menuChoice.equalsIgnoreCase("O")) {
                        return;
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
                            for (Room room : roomList) {
                                for (int i = 0; i < room.getRoomBookingList().size(); i++) {
                                    if (methodList.get(intChoice - 1).getBookingID() == room.getRoomBookingList().get(i).getBookingID()) {    //Find the corresponding account in the original list.
                                        viewBooking(room.getRoomBookingList().get(i));   //Method call
                                    }
                                }
                            }
                        }
                    }
                } while (!validateInput);
            }
        } while (true);
    }

    //4.2.1.
    public void viewBooking(Booking booking) {

        //Ska länka till metod removeBooking, när den metoden är klar.

        String cancel;
        boolean validate = false;


        System.out.println("4.2.1. BOOKING");
        System.out.println(booking);

        do {
            System.out.printf("%n%s%n%s%n%s%n", "Would you like to cancel this booking?", "1. Yes", "2. No");
            cancel = input.nextLine();

            switch (cancel) {
                case "1":
                    System.out.println("Your booking would now have been cancelled if the method was complete");
                    validate = true;
                    break;
                case "2":
                    System.out.println("Booking still valid.");
                    validate = true;
                    break;
                default:
                    System.out.println("Please enter an option of \"1\" or \"2\". ");
                    break;
            }
        } while (!validate);

        System.out.println("Back (Enter)");
        input.nextLine();
    }

    // 3.2.3.
    public void adminEditRoomInfo(Room room) {
        System.out.println("3.2.3 EDIT ROOM: " + room);

        String answer;
        int intAnwser;
        boolean validate = false;

        do {
            System.out.printf("%s%n%s%n%s%n%s%n%s%n%s%n",
                    "Edit room info: ",
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
                        System.out.println("Are you sure that you would like to remove this room? y/n");
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
                                System.out.println("Invalid anwser. Type y/n");
                                validate = false;
                            }

                        } while (validate == false);

                        if (acceptRemove) {

                            if (room.getRoomBookingList().isEmpty()) {
                                acceptRemove = true;

                            } else if (acceptRemove) {
                                for (BookingConfirm booking : room.getRoomBookingList()) {
                                    if (booking.getToDate().equals(LocalDate.now()) || booking.getToDate().isAfter(LocalDate.now()))
                                        ;
                                    acceptRemove = false;
                                    System.out.println("There are current or future bookings for this room.\n" +
                                            " Please remomve all current or future bookings for this room before removing it. ");
                                    break;
                                }
                            }
                        }
                        if (!acceptRemove) {
                            System.out.println("Removing room cancelled.");
                        } else {
                            for (int i = 0; i < roomList.size(); i++) {
                                if (roomList.get(i).getRoomNumber() == room.getRoomNumber()) {
                                    roomList.remove(roomList.get(i));
                                    System.out.printf("%s%d%s", "Room ", room.getRoomNumber(), " removed succesfully.");
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

    public void viewBookingsForRoom(Room room) {
        System.out.println("3.2.3.3. BOOKINGS FOR ROOM NUMBER " + room.getRoomNumber());

        do {
            String menuChoice;
            boolean validateInput;
            int intChoice = 0;

            ArrayList<BookingConfirm> metodlist = new ArrayList<>();

            for (BookingConfirm booking : room.getRoomBookingList()) {
                if (booking.getToDate().equals(LocalDate.now()) || booking.getToDate().isAfter(LocalDate.now())) {
                    metodlist.add(booking);
                }
            }

            if (metodlist.isEmpty()) {
                System.out.println("No current or future bookings found for room " + room.getRoomNumber() +
                        ". \nBack (Enter)"
                );
                input.nextLine();
                return;


            } else {
                for (int i = 0; i < metodlist.size(); i++) {
                    System.out.printf("%-4s%s%n", Integer.toString(i + 1).concat(". "), metodlist.get(i));
                }
                System.out.printf("%-4s%s%n", "0.", "Back (Enter)");
                do {
                    menuChoice = input.nextLine();
                    if (menuChoice.equals("0") || menuChoice.equalsIgnoreCase("O")) {
                        return;
                    } else {
                        try {
                            intChoice = Integer.parseInt(menuChoice);  // String -> int
                            validateInput = true;
                            if (intChoice < 1 || intChoice > metodlist.size()) {
                                validateInput = false;
                                System.out.println("Choice did not match an alternative. Try again:");


                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Choice did not match an alternative. Try again:");
                            validateInput = false;
                        }
                        if (validateInput) {
                            for (int i = 0; i < room.getRoomBookingList().size(); i++) {
                                if (metodlist.get(intChoice - 1).getBookingID() == room.getRoomBookingList().get(i).getBookingID()) {    //Find the corresponding account in the original list.
                                    viewBooking(room.getRoomBookingList().get(i));   //Method call
                                }
                            }
                        }
                    }
                } while (!validateInput);
            }

        } while (true);
    }

    public void createObjects() {
        //=================================== ADDING CUSTOMERS =====================================================

        customerList.add(new AccountCustomer("Ron Burgundy", "custom", "045125033"));
        customerList.add(new AccountCustomer("Anton Göransson", "custom", "0703545036"));
        customerList.add(new AccountCustomer("Arnold Svensson", "custom", "0705421876"));
        customerList.add(new AccountCustomer("Erik Larsson", "custom", "0704576556"));
        customerList.add(new AccountCustomer("Elin Hansson", "custom", "0707676768"));
        customerList.add(new AccountCustomer("Lena Karlsson", "custom", "0707676768"));

        //customerList.get(0).setCancelledAccount(true); //Set account to cancelled

        //=================================== ADDING ADMINS =====================================================

        adminList.add(new AccountAdmin("Admin", "admin"));

        //=========================== EXAMPLES OF ADDING ROOMS =====================================================
        //============================ EXAMPLES OF ADDING ROOMS =====================================================


        roomList.add(new Room(1, 1));               //1
        roomList.add(new Room(1, 1));               //2
        roomList.add(new Room(1, 2));               //3
        roomList.add(new Room(1, 2));               //4

        //================================== 4 ST SINGELROOM. STANDARD 1-2 =========================================
        //===================================2 ST STANDARD 1 & 2ST STANDARD 2=======================================

        roomList.add(new Room(2, 1));               //5
        roomList.add(new Room(2, 1));               //6
        roomList.add(new Room(2, 1));               //7
        roomList.add(new Room(2, 2));               //8
        roomList.add(new Room(2, 2));               //9
        roomList.add(new Room(2, 2));               //10
        roomList.add(new Room(2, 2));               //11
        roomList.add(new Room(2, 2));               //12
        roomList.add(new Room(2, 3));               //13
        roomList.add(new Room(2, 3));               //14
        roomList.add(new Room(2, 3));               //15
        roomList.add(new Room(2, 4));               //16
        roomList.add(new Room(2, 4));               //17
        roomList.add(new Room(2, 5));               //18
        roomList.add(new Room(2, 5));               //19

        //===================================== 15 ST DOUBLE ROOM STANDARD 1-5=======================================
        //========== 3 ST STANDARD 1, 5 ST STANDARD 2, 3 ST STANDARD 3, 2 ST STANDARD 4, 2 ST STANDARD 5=============

        roomList.add(new Room(4, 1));               //20
        roomList.add(new Room(4, 2));               //21
        roomList.add(new Room(4, 2));               //22
        roomList.add(new Room(4, 3));               //23
        roomList.add(new Room(4, 4));               //24
        roomList.add(new Room(4, 5));               //25

        //===================================== 6 ST 4 BEDS ROOM STANDARD 2-4========================================
        //========= 1 ST STANDARD 1, 2 ST STANDARD 2, 1 ST STANDARD 3, 1 ST STANDARD 4, 1 ST STANDARD 5 =============
        //======================================SUM ROOMS = 25 =====================================================

        //============================ CREATE STANDARD PRICE OBJECT ============================================

        standardList.add(new StandardPrice(1, 999));
        standardList.add(new StandardPrice(2, 1499));
        standardList.add(new StandardPrice(3, 1999));
        standardList.add(new StandardPrice(4, 2999));
        standardList.add(new StandardPrice(5, 4999));

        //============================ CREATE BEDS OBJECT =======================================================

        bedConstantList.add(new BedPrices(1, 1));
        bedConstantList.add(new BedPrices(2, 1.2));
        bedConstantList.add(new BedPrices(4, 1.7));

        //============================ EXAMPLE OF ADDING BOOKINGS ======================================================

        LocalDate fromDate1 = LocalDate.of(2019, 3, 12);
        LocalDate toDate1 = LocalDate.of(2019, 4, 11);

        try {    //                room       ,   customer
            double price1 = calculateSingleBookingPrice(fromDate1, toDate1, roomList.get(0));
            bookingDates(roomList.get(0), fromDate1, toDate1, customerList.get(1), price1);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED!1 " + e.getMessage());
        }

        LocalDate fromDate2 = LocalDate.of(2019, 2, 12);
        LocalDate toDate2 = LocalDate.of(2019, 3, 11);
        try {
            double price2 = calculateSingleBookingPrice(fromDate2, toDate2, roomList.get(0));
            bookingDates(roomList.get(0), fromDate2, toDate2, customerList.get(2), price2);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate3 = LocalDate.of(2019, 7, 12);
        LocalDate toDate3 = LocalDate.of(2019, 7, 17);
        try {
            double price3 = calculateSingleBookingPrice(fromDate3, toDate3, roomList.get(0));
            bookingDates(roomList.get(0), fromDate3, toDate3, customerList.get(3), price3);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate4 = LocalDate.of(2019, 5, 12);
        LocalDate toDate4 = LocalDate.of(2019, 5, 18);
        try {
            double price4 = calculateSingleBookingPrice(fromDate4, toDate4, roomList.get(2));
            bookingDates(roomList.get(2), fromDate4, toDate4, customerList.get(4), price4);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate5 = LocalDate.of(2019, 6, 12);
        LocalDate toDate5 = LocalDate.of(2019, 6, 17);
        try {
            double price5 = calculateSingleBookingPrice(fromDate4, toDate4, roomList.get(2));
            bookingDates(roomList.get(2), fromDate5, toDate5, customerList.get(5), price5);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate6 = LocalDate.of(2019, 5, 18);
        LocalDate toDate6 = LocalDate.of(2019, 5, 25);
        try {
            double price6 = calculateSingleBookingPrice(fromDate6, toDate6, roomList.get(0));
            bookingDates(roomList.get(0), fromDate6, toDate6, customerList.get(4), price6);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate7 = LocalDate.of(2019, 2, 1);
        LocalDate toDate7 = LocalDate.of(2019, 2, 6);
        try {
            double price7 = calculateSingleBookingPrice(fromDate7, toDate7, roomList.get(0));
            bookingDates(roomList.get(0), fromDate7, toDate7, customerList.get(4), price7);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        //=====================================================================================

        LocalDate fromDate8 = LocalDate.of(2018, 12, 12);
        LocalDate toDate8 = LocalDate.of(2018, 12, 13);
        try {
            double price8 = calculateSingleBookingPrice(fromDate8, toDate8, roomList.get(9));
            bookingDates(roomList.get(9), fromDate8, toDate8, customerList.get(0), price8);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate9 = LocalDate.of(2019, 2, 15);
        LocalDate toDate9 = LocalDate.of(2019, 2, 16);
        try {
            double price9 = calculateSingleBookingPrice(fromDate9, toDate9, roomList.get(9));
            bookingDates(roomList.get(9), fromDate9, toDate9, customerList.get(0), price9);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate10 = LocalDate.of(2019, 1, 1);
        LocalDate toDate10 = LocalDate.of(2019, 1, 2);
        try {
            double price10 = calculateSingleBookingPrice(fromDate10, toDate10, roomList.get(9));
            bookingDates(roomList.get(9), fromDate10, toDate10, customerList.get(0), price10);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate11 = LocalDate.of(2018, 12, 20);
        LocalDate toDate11 = LocalDate.of(2018, 12, 21);
        try {
            double price11 = calculateSingleBookingPrice(fromDate11, toDate11, roomList.get(9));
            bookingDates(roomList.get(9), fromDate11, toDate11, customerList.get(0), price11);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }

        LocalDate fromDate12 = LocalDate.of(2019, 2, 20);
        LocalDate toDate12 = LocalDate.of(2019, 2, 21);
        try {
            double price12 = calculateSingleBookingPrice(fromDate12, toDate12, roomList.get(9));
            bookingDates(roomList.get(9), fromDate12, toDate12, customerList.get(0), price12);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED! " + e.getMessage());
        }


        //====================================================================================
    }
}
