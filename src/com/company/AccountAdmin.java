package com.company;

public class AccountAdmin extends Account {
    private boolean cancelledAccount;
    private String phoneNumber;
    private String password;
    private String accountID;  //??
    private static int accountCounter;
    private String phoneValidate = "0\\d\\d\\d\\d\\d\\d\\d\\d+"; //Regular expression; At least 9 numeric digits, starting with 0 (Should use Pattern instead?)

    public AccountAdmin(String name, String phoneNumber, String password) {
        super(name);
        if (phoneNumber.matches(phoneValidate)) {
            this.phoneNumber = phoneNumber;
        }
        else{
            throw new IllegalArgumentException(
                    "Invalid phone number. Must be numeric, start with '0' and contain at least 9 digits.");
        }
        this.password = password;
        accountID = "A".concat(Integer.toString(++accountCounter));
        cancelledAccount = false;
    }

    public String getAccountID() {
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

    public boolean isCancelledAccount() {
        return cancelledAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public void setCancelledAccount (boolean cancelledAccount) {
        this.cancelledAccount = cancelledAccount;
    }

    @Override
    public String toString() {
        return String.format("%s%-4d%s%-10s", "Account id: ", accountID, "Password: ", password);
    }
}
