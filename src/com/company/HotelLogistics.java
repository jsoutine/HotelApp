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
        boolean match = true;
        if (id.matches("C\\d+") || id.matches("c\\d+")) {
            for (AccountCustomer customer : customerList) {
                if (customer.getAccountID().equalsIgnoreCase(id) && customer.getPassword().equals(password) && !customer.isCancelledAccount()) {
                    System.out.println("\nWelcome " + customer.getName() + "\n");
                    customerMainMenu(customer);
                    match = true;
                }else{
                    match = false;
                }
            }
        } else if (id.matches("A\\d+") || id.matches("a\\d+")) {
            for (AccountAdmin admin : adminList) {
                if (admin.getAccountID().equalsIgnoreCase(id) && admin.getPassword().equals(password) && !admin.isCancelledAccount()) {
                    adminMainMenu(admin);
                    match = true;
                }else {
                    match = false;
                }
            }
        } else {
            match = false;
        }
        if(!match){
            System.out.println("Login failed. Check user id and password.\n");
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
                        System.out.println("3.2. Rooms");
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
        ArrayList<Account> methodList = new ArrayList<>();
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
        System.out.println("NOT COMPLETE METHOD\n" + "Back (Enter)");
        input.nextLine();
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



   /*
   //3.4. (Eventuellt lägga till: if index 0; not able to change -> En permanent admin.
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
        boolean validateInput;
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
            }
            while (!menuChoice.equals("1") && !menuChoice.equals("2"));  // Loop if haven't chosen to exit method in 3.4.

            if (customerList.isEmpty()) {
                System.out.println("Account list is empty." + "\nBack (Enter)");
                input.nextLine();
                return;
            } else {
                int countElements = 0;
                if (admin) {
                    for (Account x : customerList) {
                        if (x.isFullRights() == true) {     //Add all admin accounts into a new ArrayList
                            methodList.add(x);              //Add all admin accounts into a new ArrayList
                            countElements++;                                 // count number of elements
                            System.out.printf("%-3s%s%n", Integer.toString(countElements).concat("."), x);    ////Print admin accounts with identifying numbers
                        } //                              for representation: concat two String objects
                    }
                } else {
                    for (Account x : customerList) {
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
                            validateInput = true;
                            answer = input.nextLine();
                            switch (answer) {
                                case "y":
                                case "Y":
                                    for (int i = 0; i < customerList.size(); i++) {
                                        if (methodList.get(intChoice - 1).getAccountID() == customerList.get(i).getAccountID()) {    //Find the corresponding account in the original list.
                                            if (admin) {
                                                customerList.get(i).setFullRights(false);                                                // make the changes in the account in the original list.
                                            } else {
                                                customerList.get(i).setFullRights(true);
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
                                    validateInput = false;
                                    break;
                            }
                        } while (!validateInput);
                    }
                }
            }
        } while (loopEntireMethod);  // If chosen Back to 3.4.
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
                        System.out.println("Invalid option. Type a a choice 0-3:");
                        break;
                }
            }
            while (!menuChoice.equals("1") && !menuChoice.equals("2") && !menuChoice.equals("3") && !menuChoice.equals("0"));
        } while (!logout);
    }


    //4.1.
    public void makeBooking(Account concernedAccount) {
        System.out.println("4.1. MAKE BOOKING, OR VIEW AVAILABLE");
        ArrayList<Booking> matchingResults = new ArrayList<>();
        LocalDate fromDate = LocalDate.of(2000, 1, 1);
        LocalDate toDate = LocalDate.of(2000, 1, 1);
        String answer;
        int year;
        int month;
        int day;
        int beds = 0;
        int standard = 0;
        int bookingChoice = 0;
        boolean validateInput;
        boolean cancel;
        System.out.println("Step 1/4. Enter date of desired arrival: (YY-MM-DD)" + "\n0. Cancel");
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
            System.out.println("Step 2/4. Enter date of desired departure: (YY-MM-DD)" + "\n0. Cancel");
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
        if (!cancel) {
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
        if (!cancel) {
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
        if (!cancel) {
            System.out.println("SEARCH RESULT:");
            for (Room room : roomList) {
                if (room.getBeds() == beds && room.getStandard() == standard) {
                    if (checkDates(room, fromDate, toDate)) {
                        double price = calculateBookingPrice(fromDate, toDate, room);
                        matchingResults.add(new Booking(room, fromDate, toDate, price));
                        //break;
                    }
                }
            }
            if (matchingResults.isEmpty()) {
                System.out.println("Unfortunately no perfect matches. Here are some other alternatives that might be of interest: ");
                for (Room room : roomList) {
                    if (room.getBeds() >= beds) {      //HOW SHOULD WE FILTER SECOND HAND MATCHES?
                        if (checkDates(room, fromDate, toDate)) {
                            double price = calculateBookingPrice(fromDate, toDate, room);
                            matchingResults.add(new Booking(room, fromDate, toDate, price));
                        }
                    }
                }
            }
            if (matchingResults.isEmpty()) {
                System.out.println("No results" + "\n0. Back");
            } else {
                int countElements = 0;
                for (Booking booking : matchingResults) {
                    System.out.printf("%-4s%s%n", Integer.toString(++countElements).concat("."), booking);
                }
                System.out.println("1-n: Make a booking from the list" + "\n0. Back");
            }
            do {
                answer = input.nextLine();
                if (answer.equals("0") || answer.equalsIgnoreCase("O")) {
                    validateInput = true;
                    cancel = true;
                } else {
                    try {
                        bookingChoice = Integer.parseInt(answer);  // String -> int
                        validateInput = true;
                        if (bookingChoice < 1 || bookingChoice > matchingResults.size()) {
                            validateInput = false;
                            System.out.println("Choice did not match an alternative. Try again:");
                            //} else {
                            //    validateNumeric = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Choice did not match an alternative. Try again:");
                        validateInput = false;
                    }
                }
            } while (!validateInput);

        }
        if (!cancel) {
            System.out.printf("%s%n%s%n%s%n%s%n", "Make a booking for: ", matchingResults.get(bookingChoice - 1), "Y. Yes", "N. No. Cancel booking process.");
            do {
                answer = input.nextLine();
                switch (answer) {
                    case "Y":
                    case "y":
                        try {    // Maybe simplify, since basically: bookingDates(matchingResults)
                            bookingDates(matchingResults.get(bookingChoice - 1).getRoom(),
                                    matchingResults.get(bookingChoice - 1).getFromDate(), matchingResults.get(bookingChoice - 1).getToDate(), concernedAccount, matchingResults.get(bookingChoice - 1).getPrice());
                            validateInput = true;
                        } catch (IllegalArgumentException e) {
                            System.out.println("BOOKING FAILED!1 " + e.getMessage());
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
        }
    }


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

                if ((i == 0) && (toDate.equals(room.getRoomBookingList().get(0).getFromDate()) ||    // Om index är 0 && Om utchek är samma dag som existerande incheck || utcheck är innan existerande incheck
                        toDate.isBefore(room.getRoomBookingList().get(0).getFromDate()))) {
                    match = true;
                    break;
                } else if ((i > 0) && (i < room.getRoomBookingList().size() - 1)) {                                                                 // Om index är mer än 0 && index nite pekar på det sista objektet i listan.
                    if ((fromDate.equals(room.getRoomBookingList().get(i).getToDate()) ||      // Om inchek är samma dag som existerande utcheck.
                            fromDate.isAfter(room.getRoomBookingList().get(i).getToDate())) &&
                            (toDate.equals(room.getRoomBookingList().get(i + 1).getFromDate()) ||                      // Om utchek är samma dag som existerande incheck
                                    toDate.isBefore(room.getRoomBookingList().get(i + 1).getFromDate()))) {  //Om incheck är är efter existerande utcheck i, och före existerande incheck i+1.)
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
                        "Room not available at chosen date/dates. Only one booking in system, which collides with this.");
            }
        } else {
            for (int i = 0; i < room.getRoomBookingList().size(); i++) {

                if ((i == 0) && (toDate.equals(room.getRoomBookingList().get(0).getFromDate()) ||    // Om index är 0 && Om utchek är samma dag som existerande incheck || utcheck är innan existerande incheck
                        toDate.isBefore(room.getRoomBookingList().get(0).getFromDate()))) {
                    room.getRoomBookingList().add(0, new BookingConfirm(room, fromDate, toDate, customer, price));       //Lägg till innan existerande bokning i listan
                    System.out.println("Booking successful. Code 4 " + " Iteration " + i);
                    return;
                } else if ((i > 0) && (i < room.getRoomBookingList().size() - 1)) {                                                                 // Om index är mer än 0 && index nite pekar på det sista objektet i listan.
                    if ((fromDate.equals(room.getRoomBookingList().get(i).getToDate()) ||      // Om inchek är samma dag som existerande utcheck.
                            fromDate.isAfter(room.getRoomBookingList().get(i).getToDate())) &&
                            (toDate.equals(room.getRoomBookingList().get(i + 1).getFromDate()) ||                      // Om utchek är samma dag som existerande incheck
                                    toDate.isBefore(room.getRoomBookingList().get(i + 1).getFromDate()))) {  //Om incheck är är efter existerande utcheck i, och före existerande incheck i+1.)
                        room.getRoomBookingList().add(i + 1, new BookingConfirm(room, fromDate, toDate, customer, price));                       //Lägg till efter bokning "i" (Finns ledigt mellan bokning i och bokning i+1
                        System.out.println("Booking successful. Code 5 " + " Iteration " + i);
                        return;
                    }
                } else if (i == room.getRoomBookingList().size() - 1) {                      // If index points to last item in list.
                    if (fromDate.equals(room.getRoomBookingList().get(i).getToDate()) ||      // Om inchek är samma dag som existerande utcheck.
                            fromDate.isAfter(room.getRoomBookingList().get(i).getToDate())) {   //Om incheckning är efter existerande utcheck i.
                        room.getRoomBookingList().add(new BookingConfirm(room, fromDate, toDate, customer, price));
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

    public double calculateBookingPrice(LocalDate fromDate, LocalDate toDate, Room room) {
        double price;
        double bedsConstant = 1;
        long periodDays = ChronoUnit.DAYS.between(fromDate, toDate); // - 1 för antal nätter
        double standardPrice = standardList.get(room.getStandard() - 1).getPrice();  //May throw IndexOutOfBoundsException if no match??
        for (BedPrices beds : bedConstantList) {
            if (room.getBeds() == beds.getNumberOfBeds()) {  //If number of beds in the room equals
                bedsConstant = beds.getConstant();
                break;
            }
        }
        price = (periodDays - 1) * standardPrice * bedsConstant;   //nights x standard x beds
        return price;
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
        System.out.println("4.2.1. BOOKING");
        System.out.println(booking);
        System.out.println("Back (Enter)");
        input.nextLine();
    }


    public void createObjects() {
        //=================================== ADDING CUSTOMERS =====================================================

        customerList.add(new AccountCustomer("Ron Burgundy", "045125033", "custom"));
        customerList.add(new AccountCustomer("Anton Göransson", "0703545036", "custom"));
        customerList.add(new AccountCustomer("Arnold Svensson", "0704565656", "custom"));
        customerList.add(new AccountCustomer("Erik Larsson", "0704576556", "custom"));
        customerList.add(new AccountCustomer("Elin Hansson", "0707676768", "custom"));
        customerList.add(new AccountCustomer("Lena Karlsson", "044343434", "custom"));

        //=================================== ADDING ADMINS =====================================================

        adminList.add(new AccountAdmin("Admin", "044545454", "admin"));

        //=================================== ADDING GUEST =====================================================

        AccountGuest Guest = new AccountGuest("Guest");

        //============================ EXAMPLES OF ADDING ROOMS =====================================================



        roomList.add(new Room(1, 1));               //1
        roomList.add(new Room(1, 1));               //2
        roomList.add(new Room(1, 2));               //3
        roomList.add(new Room(1, 2));               //4

        //================================== 4 ST SINGELROOM. STANDARD 1-3 =========================================

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

        roomList.add(new Room(4, 1));               //20
        roomList.add(new Room(4, 2));               //21
        roomList.add(new Room(4, 2));               //22
        roomList.add(new Room(4, 3));               //23
        roomList.add(new Room(4, 4));               //24
        roomList.add(new Room(4, 5));               //25

        //===================================== 6 ST 4 BEDS ROOM STANDARD 2-4========================================
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
            double price1 = calculateBookingPrice(fromDate1, toDate1, roomList.get(0));
            bookingDates(roomList.get(0), fromDate1, toDate1, customerList.get(1), price1);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED!1 " + e.getMessage());
        }

        LocalDate fromDate2 = LocalDate.of(2019, 2, 12);
        LocalDate toDate2 = LocalDate.of(2019, 3, 11);
        try {
            double price2 = calculateBookingPrice(fromDate2, toDate2, roomList.get(0));
            bookingDates(roomList.get(0), fromDate2, toDate2, customerList.get(2), price2);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED!2 " + e.getMessage());
        }

        LocalDate fromDate3 = LocalDate.of(2019, 7, 12);
        LocalDate toDate3 = LocalDate.of(2019, 7, 17);
        try {
            double price3 = calculateBookingPrice(fromDate3, toDate3, roomList.get(0));
            bookingDates(roomList.get(0), fromDate3, toDate3, customerList.get(3), price3);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED!3 " + e.getMessage());
        }

        LocalDate fromDate4 = LocalDate.of(2019, 5, 12);
        LocalDate toDate4 = LocalDate.of(2019, 5, 18);
        try {
            double price4 = calculateBookingPrice(fromDate4, toDate4, roomList.get(2));
            bookingDates(roomList.get(2), fromDate4, toDate4, customerList.get(4), price4);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED!4 " + e.getMessage());
        }

        LocalDate fromDate5 = LocalDate.of(2019, 6, 12);
        LocalDate toDate5 = LocalDate.of(2019, 6, 17);
        try {
            double price5 = calculateBookingPrice(fromDate4, toDate4, roomList.get(2));
            bookingDates(roomList.get(2), fromDate5, toDate5, customerList.get(5), price5);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED!5 " + e.getMessage());
        }

        LocalDate fromDate6 = LocalDate.of(2019, 5, 18);
        LocalDate toDate6 = LocalDate.of(2019, 5, 25);
        try {
            double price6 = calculateBookingPrice(fromDate6, toDate6, roomList.get(0));
            bookingDates(roomList.get(0), fromDate6, toDate6, customerList.get(4), price6);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED!6 " + e.getMessage());
        }

        LocalDate fromDate7 = LocalDate.of(2019, 2, 1);
        LocalDate toDate7 = LocalDate.of(2019, 2, 6);
        try {
            double price7 = calculateBookingPrice(fromDate7, toDate7, roomList.get(0));
            bookingDates(roomList.get(0), fromDate7, toDate7, customerList.get(4), price7);
        } catch (IllegalArgumentException e) {
            System.out.println("BOOKING FAILED!7 " + e.getMessage());
        }


    }

}
