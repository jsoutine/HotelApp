package com.company;

public class AccountAdmin extends Account {
    //private boolean cancelledAccount;
    //private String password;
    private String accountID;
    private static int accountCounter;

    public AccountAdmin(String name, String password) {
        super(name, password);
        accountID = "A".concat(Integer.toString(++accountCounter));
    }

    public String getAccountID() {
        return accountID;
    }


    @Override
    public String toString() {
        return String.format("%s%-4s", "Account id: ", accountID);
    }
}
