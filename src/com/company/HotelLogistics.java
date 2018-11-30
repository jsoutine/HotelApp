package com.company;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

    public class HotelLogistics {

        private ArrayList<Account> accountList = new ArrayList<>();  //Lista över accounts.
        private ArrayList<Room> roomList = new ArrayList<>();        //Lista över rummen
        private Scanner input = new Scanner(System.in);


        public void logIn(int id, String password) {
            if (accountList.get(id).getPassword().equals(password)) {
                if (accountList.get(id).isFullRights()){
                    adminMainMenu(accountList.get(id));
                }else{
                    System.out.println("Welcome " + accountList.get(id).getName());
                    customerMainMenu(accountList.get(id));
                }
            }else {
                System.out.println("Login failed. Check user id or password.");
            }
        }

        public boolean logOut(){   //This method returns true if user choose to log out, and false if not.
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
            }while(!menuChoice.equalsIgnoreCase("y") && !menuChoice.equalsIgnoreCase("n"));
            return logout;
        }

        public void adminMainMenu (Account loggedInAccount) {
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
                            System.out.println("3.2. Rooms");
                            break;
                        case "3":
                            System.out.println("3.3. Bookings");
                            break;
                        case "4":
                            System.out.println("Method still under construction");
                            adminEditAccess();
                            break;
                        case "0":
                            logout = logOut();
                            break;
                        default:
                            System.out.println("Invalid option. Type a choice 0-4:");
                            break;
                    }
                } while (!menuChoice.equals("1") && !menuChoice.equals("2") && !menuChoice.equals("3") && !menuChoice.equals("4") && !menuChoice.equals("0"));
            }while(!logout);
        }

        public void adminCustomers(Account loggedInAccount) {  //UNDER CONSTRUCTION
            ArrayList<Account> methodList = new ArrayList<>();
            String menuChoice;
            boolean validateInput;
            int intChoice = 0;  //for choosing a customer

            do {
                System.out.println("3.1. CUSTOMERS");

                int countElements = 0;
                for (Account x : accountList) {
                    if (!x.isFullRights() && !x.isCancelledAccount()) {     //If account is not admin, and nor cancelled; add to new ArrayList (methodList)
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
                            for (int i = 0; i < accountList.size(); i++) {
                                if (methodList.get(intChoice - 1).getAccountID() == accountList.get(i).getAccountID()) {    //Find the corresponding account in the original list.
                                    adminCustomer(accountList.get(i));   //Method call
                                }
                            }
                        }
                    }
                } while (!validateInput);
            }while(true); //Always loop, until menuChoice = 0 -> Return

        }

        public void adminCustomer(Account customer) {   //UNDER CONSTRUCTION
            System.out.printf("%s%n%s%n%s%n%s%n",
                    "3.1.2. CUSTOMER (Admin level)",
                    customer,
                    "NOT COMPLETE METHOD,",
                    "Back (Enter)");
            input.nextLine();
        }

        public void adminCancelledAccounts(Account loggedInAccount) {  //UNDER CONSTRUCTION
            ArrayList<Account> cancelledAccounts = new ArrayList<>();
            System.out.println("3.1.3. CANCELLED ACCOUNTS");
            int countElements2 = 0;
            for (Account x : accountList) {
                if (x.isCancelledAccount() && !x.isFullRights()) {  //If account is cancelled and not admin
                    cancelledAccounts.add(x);
                    countElements2++;
                    System.out.printf("%-3s%s%n", Integer.toString(countElements2).concat("."), x);
                }
            }
            if (cancelledAccounts.isEmpty()) {
                System.out.println("No cancelled accounts.");

            }
            System.out.println("NOT COMPLETE METHOD\n" + "Back (Enter)");
            input.nextLine();
        }

        public void adminEditAccess() {  //STILL UNDER CONSTRUCTION
            ArrayList<Account> methodList = new ArrayList<>();
            // Vi kan skapa nya objekt (t.ex. ArrayLists) inuti metoder hur mycket vi vill utan att bekymra oss om
            //hur det påverkar det stora programmet, eftersom objekt som skapas i metoder dör när metoden avslutas.
            // -> Det blir bara temporära objekt som används under metodens livstid.
            String menuChoice;
            String menuChoice2;
            boolean validateNumeric;
            int intChoice = 0;
            String answer;
            boolean validateInput2;
            boolean loopEntireMethod = false;
            boolean admin = false;  //Used in method to determine if we are edting admin account(s) or customer account(s)

            do {
                System.out.printf("%s%n%s%n%s%n%s%n%s%n",
                        "3.4. EDIT ACCOUNT ACCESS",
                        "Type a choice:",
                        "1. View admin accounts",
                        "2. View customer accounts.",
                        "0. Back.");
                do {
                    menuChoice = input.nextLine();
                    switch (menuChoice) {
                        case "1":
                            admin = true;  // Editing admin accounts
                            System.out.println("3.4.1. ADMIN ACCOUNTS");
                            break;
                        case "2":
                            admin = false;  //  Editing customer accounts
                            System.out.println("3.4.2. CUSTOMER ACCOUNTS");
                            break;
                        case "0":
                            return; //Exit method
                        default:
                            System.out.println("Please type an answer '0', '1' or '2'.");
                            break;
                    }
                } while (!menuChoice.equals("1") && !menuChoice.equals("2"));  // Loop if haven't chosen to exit method in 3.4.

                if (accountList.isEmpty()) {
                    System.out.println("Account list is empty." + "\nBack (Enter)");
                    input.nextLine();
                    return;
                } else {
                    int countElements = 0;
                    if (admin) {
                        for (Account x : accountList) {
                            if (x.isFullRights() == true) {     //Add all admin accounts into a new ArrayList
                                methodList.add(x);              //Add all admin accounts into a new ArrayList
                                countElements++;                                 // count number of elements
                                System.out.printf("%-3s%s%n", Integer.toString(countElements).concat("."), x);    ////Print admin accounts with identifying numbers
                            } //                              for representation: concat two String objects
                        }
                    } else {
                        for (Account x : accountList) {
                            if (x.isFullRights() == false) {     //Add all non-admin accounts into a new ArrayList
                                methodList.add(x);
                                countElements++;
                                System.out.printf("%-3s%s%n", Integer.toString(countElements).concat("."), x);
                            }
                        }
                    }
                    if (methodList.isEmpty()) {
                        if (admin) {
                            System.out.println("Admin list is empty." + "\nBack (Enter)");
                        } else {
                            System.out.println("Customer list is empty." + "\nBack (Enter)");
                        }
                        input.nextLine();
                        //loopEntireMethod = true;  Better???
                        return;
                    } else {
                        System.out.printf("%-3s%s%n", "0.", "Back (to 3.4.)");
                        do { // do this while input is not numeric, or while input does not match accounts (1-n) or 0 (Back)
                            menuChoice2 = input.nextLine();
                            try {
                                intChoice = Integer.parseInt(menuChoice2);  // String -> int
                                validateNumeric = true;
                                if (intChoice < 0 || intChoice > methodList.size()) {
                                    validateNumeric = false;
                                    System.out.println("Choice did not match an alternative. Try again:");
                                    //} else {
                                    //    validateNumeric = true;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Answer must be numeric. Try again:");
                                validateNumeric = false;
                            }
                        } while (!validateNumeric);
                        if (intChoice == 0) { // = Back to beginning of menu (3.4.)
                            loopEntireMethod = true;
                        } else {
                            if (admin) {
                                System.out.printf("%s%n%s%n", "3.4.1.1. Remove admin access? y/n", (methodList.get(intChoice - 1)));
                            } else {
                                System.out.printf("%s%n%s%n", "3.4.2.1. Set as admin? y/n", (methodList.get(intChoice - 1)));
                            }
                            do {  // do while input is not y/n
                                validateInput2 = true;
                                answer = input.nextLine();
                                switch (answer) {
                                    case "y":
                                    case "Y":
                                        for (int i = 0; i < accountList.size(); i++) {
                                            if (methodList.get(intChoice - 1).getAccountID() == accountList.get(i).getAccountID()) {    //Find the corresponding account in the original list.
                                                if (admin) {
                                                    accountList.get(i).setFullRights(false);                                                // make the changes in the account in the original list.
                                                } else {
                                                    accountList.get(i).setFullRights(true);
                                                }
                                            }
                                        }
                                        if (admin) {
                                            System.out.printf("%s%s%s%n%s%n", "3.4.1.1.1. '", methodList.get(intChoice - 1).getName(), "' now only have customer access.",
                                                    "Back (Enter)");
                                        } else {
                                            System.out.printf("%s%s%s%n%s%n", "3.4.2.1.1. '", methodList.get(intChoice - 1).getName(), "' now have admin access.",
                                                    "Back (Enter)");
                                        }
                                        input.nextLine();
                                        return;
                                    case "n":
                                    case "N":
                                        System.out.printf("%s%n%s%n",
                                                "3.4.1.1.2. Access edit cancelled",
                                                "Back (Enter)");
                                        input.nextLine();
                                        return;
                                    default:
                                        System.out.println("Type an answer 'y' or 'n'");
                                        validateInput2 = false;
                                        break;
                                }
                            } while (!validateInput2);
                        }
                    }
                }


            } while (!loopEntireMethod);  // If chosen Back to 3.4.
        }

        public void customerMainMenu (Account loggedInAccount) {
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
                            System.out.println("4.1.");
                            break;
                        case "2":
                            System.out.println("4.2.");
                            break;
                        case "3":
                            System.out.println("4.3.");
                            break;
                        case "0":
                            logout = logOut();
                            break;
                        default:
                            System.out.println("Invalid option. Typa a choice 0-3:");
                            break;
                    }
                } while (!menuChoice.equals("1") && !menuChoice.equals("2") && !menuChoice.equals("3") && !menuChoice.equals("0"));
            }while(!logout);
        }

        public void viewBookings(Account loggedIn) {
            ArrayList <Booking> methodList = new ArrayList<>();
            String menuChoice;
            for (Room room : roomList) {
                for (Booking booking : room.getRoomBookingList()) {
                    if(loggedIn.isFullRights()) {
                        methodList.add(booking);
                    }else if (booking.getCustomer().getAccountID() == loggedIn.getAccountID()) {
                        methodList.add(booking);
                    }
                }
            }  //MAYBE ADD: SORTING DEPENDING ON FROM WHICH METHOD THIS METHOD IS INVOKED (compareTo)
            if (methodList.isEmpty()) {
                System.out.println("No bookings found.\n" + "Back (Enter)");
                return;
            }else{
                for (int i = 0; i < methodList.size(); i++) {
                    System.out.printf("%-4s%s%n", Integer.toString(i+1).concat(". "), methodList.get(i));

                }
                System.out.printf("%-4s%s%n", "0.", "Back (Enter");
                menuChoice = input.nextLine();

            }
        }

        public void bookingDates(Room room, Account customer, LocalDate fromDate, LocalDate toDate) {  //Kan användas för att boka, eller för att sortera bokningar i kronologisk tids-ordning.
            if (room.getRoomBookingList().isEmpty()) {                                                  //Om bokningslistan för rummet är tom.
                room.getRoomBookingList().add(new Booking(customer, room, fromDate, toDate));
                System.out.println("Booking successful. Code 1");
                return;
            } else if (room.getRoomBookingList().size() == 1) {                                        //Om bara finns en bokning i listan
                if (toDate.isBefore(room.getRoomBookingList().get(0).getFromDate())) {                 //Om utcheck är innan existerande incheck
                    room.getRoomBookingList().add(0, new Booking(customer, room, fromDate, toDate));     //Lägg till innan existerande bokning i listan
                    System.out.println("Booking successful. Code 2");
                    return;
                } else if (fromDate.isAfter(room.getRoomBookingList().get(0).getToDate())) {          //Om incheck är efter existerande utcheck.
                    room.getRoomBookingList().add(new Booking(customer, room, fromDate, toDate));             //Lägg till efter existerande bokning i listan.
                    System.out.println("Booking successful. Code 3");
                    return;
                } else {
                    throw new IllegalArgumentException(
                            "Room not available at chosen date/dates. Only one booking in system, which collides with this.");
                }
            } else {
                for (int i = 0; i < room.getRoomBookingList().size(); i++) {

                    if ((i == 0) && (toDate.isBefore(room.getRoomBookingList().get(0).getFromDate()))) {      //Om index är 0 && Om utcheck är innan existerande incheck
                        room.getRoomBookingList().add(0, new Booking(customer, room, fromDate, toDate));       //Lägg till innan existerande bokning i listan
                        System.out.println("Booking successful. Code 4 " + " Iteration " + i);
                        return;
                    } else if ((i > 0) && (i < room.getRoomBookingList().size() - 1)) {                                                                 // Om index är mer än 0 && index nite pekar på det sista objektet i listan.
                        if (fromDate.isAfter(room.getRoomBookingList().get(i).getToDate()) && toDate.isBefore(room.getRoomBookingList().get(i + 1).getFromDate())) {  //Om incheck är är efter existerande utcheck i, och före existerande incheck i+1.)
                            room.getRoomBookingList().add(i + 1, new Booking(customer, room, fromDate, toDate));                       //Lägg till efter bokning "i" (Finns ledigt mellan bokning i och bokning i+1
                            System.out.println("Booking successful. Code 5 " + " Iteration " + i);
                            return;
                        }
                    } else if (i == room.getRoomBookingList().size() - 1) {                      // If index points to last item in list.
                        if (fromDate.isAfter(room.getRoomBookingList().get(i).getToDate())) {   //Om incheckning är efter existerande utcheck i.
                            room.getRoomBookingList().add(new Booking(customer, room, fromDate, toDate));
                            System.out.println("Booking successful. Code 6 " + " Iteration " + i);
                            return;
                        } else {
                            throw new IllegalArgumentException(
                                    "In conflict with other booking.");
                        }
                    }
                }
            }
        }


        public void createObjects(){
            //============================ EXAMPLES OF ADDING ACCOUNTS =====================================================

            accountList.add(new Account("Admin", "045125033", "admin"));  //index 0 = Admin
            accountList.add(new Account("Anton Göransson", "0703545036", "custom"));
            accountList.add(new Account("Arnold Svensson", "0704565656", "custom"));
            accountList.add(new Account("Erik Larsson", "0704576556", "custom"));
            accountList.add(new Account("Elin Hansson", "0707676768", "custom"));
            accountList.add(new Account("Lena Karlsson", "044343434", "custom"));

            //============================ EXAMPLES OF ADDING ROOMS ======================================================

            roomList.add(new Room(2, 3));
            roomList.add(new Room(1, 5));
            roomList.add(new Room(2, 2));

            //============================ EXAMPLE OF ADDING BOOKING ======================================================

            LocalDate fromDate1 = LocalDate.of(2019, 3, 12);
            LocalDate toDate1 = LocalDate.of(2019, 4, 11);

            try {    //                room       ,   customer
                bookingDates(roomList.get(0), accountList.get(0), fromDate1, toDate1);
            } catch (IllegalArgumentException e) {
                System.out.println("BOOKING FAILED! " + e.getMessage());
            }

            //============================ EXAMPLES OF SETTING ACCOUNT AS ADMIN ============================================

            accountList.get(0).setFullRights(true);

        /*============================ EXAMPLES OF PRINTING ============================================
        System.out.println();

        System.out.println("ACCOUNTS");
        for (Account x : accountList) {
            System.out.printf("%s%n", x);
        }
        System.out.println();

        System.out.println("ADMINS");
        for (Account x : accountList) {
            if (x.isFullRights() == true) {
                System.out.println(x);
            }
        }
        System.out.println();

        System.out.println("ROOMS");
        for (Room x : roomList) {
            System.out.printf("%s%n", x);
        }
        System.out.println();


        System.out.println("BOOKINGS");
        for (Booking x : roomList.get(0).getRoomBookingList()) {
            System.out.printf("%s%n", x);
        }


        System.out.println("ACCOUNTS");
        for (Account x : accountList) {
            System.out.printf("%s%n", x);
        }
        */
        }
    }
