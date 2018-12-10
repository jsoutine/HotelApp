package com.company;

public class AccountGuest extends Account {
    private String accountID;
    private static int accountCounter;

    public AccountGuest(String name) {
        super(name);
        accountID = "B".concat(Integer.toString(++accountCounter));
    }
}
