package com.company;

import java.io.Serializable;

public class AccountCustomer extends Account implements Serializable {
    private String phoneNumber;
    private String accountID;
    private String phoneValidate = "0\\d\\d\\d\\d\\d\\d\\d\\d+"; //Regular expression; At least 9 numeric digits, starting with 0 (Should use Pattern instead?)

    public AccountCustomer (String name, String password, boolean cancelledAccount, String accountID, String phoneNumber) {
        super(name, password, cancelledAccount);
        this.accountID = accountID;
        this.phoneNumber = phoneNumber;
    }

    public AccountCustomer(int accountCount, String name, String password, String phoneNumber) {
        super(name, password);
        if (phoneNumber.matches(phoneValidate)) {
            this.phoneNumber = phoneNumber;
        }
        else{
            throw new IllegalArgumentException(
                    "Invalid phone number. Must be numeric, start with '0' and contain at least 9 digits.");
        }
        accountID = "C".concat(Integer.toString(accountCount));
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

    public String getPhoneNumber() {
        return phoneNumber;
    }


    @Override
    public String toString() {
        return String.format("%s%s%-4s%s%-15s",super.toString(), "Account id: ", accountID, "Phone number: ", phoneNumber);
    }
}
