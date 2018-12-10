package com.company;

public abstract class Account {
    private String name;
    private String accountID;

    public Account (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

   public String getAccountID() {
       return accountID;
    }

    public void setName (String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s%-20s",  "Name: ", name);
    }

}
