package com.company;

public class Account {
    private boolean fullRights;
    private boolean cancelledAccount;
    private String name;
    private String phoneNumber;
    private String password;
    private int accountID;
    private static int accountCounter;
    private String phoneValidate = "0\\d\\d\\d\\d\\d\\d\\d\\d+"; //Regular expression; At least 9 numeric digits, starting with 0 (Should use Pattern instead?)

    public Account (String name, String phoneNumber, String password) {
        this.name = name;
        if (phoneNumber.matches(phoneValidate)) {
            this.phoneNumber = phoneNumber;
        }
        else{
            throw new IllegalArgumentException(
                    "Invalid phone number. Must be numeric, start with '0' and contain at least 9 digits.");
        }
        this.password = password;
        accountID = ++accountCounter;
        fullRights = false;
        cancelledAccount = false;
    }

    public String getName() {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setPhoneNumber (String phoneNumber) {
        if (phoneNumber.matches(phoneValidate)) {
            this.phoneNumber = phoneNumber;
        }
        else{
            throw new IllegalArgumentException(
                    "Invalid phone number. Must be numeric, start with '0' and contain at least 9 digits.");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public boolean isFullRights() {
        return fullRights;
    }

    public void setFullRights (boolean fullRights) {
        this.fullRights = fullRights;
    }

    public void setCancelledAccount (boolean cancelledAccount) {
        this.cancelledAccount = cancelledAccount;
    }

    @Override
    public String toString() {
        String rights;
        if (fullRights) {
            rights = "Admin";
        }else{
            rights = "Customer";
        }
        return String.format( "%-9s%s%-20s%s%-4d%s%-10s", rights, "Name: ", name, "Account id: ", accountID, "Password: ", password);
    }

}
