package com.company;

public abstract class Account {
    private String name;
    private String accountID;
    private String password;
    private boolean cancelledAccount;

    public Account (String name, String password) {
        this.name = name;
        this.password = password;
        cancelledAccount = false;
    }

    public String getName() {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

   public String getAccountID() {
        return accountID;
    }

    public boolean isCancelledAccount() {
        return cancelledAccount;
    }

    public void setCancelledAccount (boolean cancelledAccount) {
        this.cancelledAccount = cancelledAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return String.format("%s%-20s",  "Name: ", name);
    }

}
