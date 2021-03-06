package com.company;

public class AccountAdmin extends Account {
    private String accountID;

    public AccountAdmin(int accountCount, String name, String password) {
        super(name, password);
        accountID = "A".concat(Integer.toString(accountCount));
    }

    public AccountAdmin(String name, String password, boolean cancelledAccount, String accountID) {
        super(name, password, cancelledAccount);
        this.accountID = accountID;
    }

    public String getAccountID() {
        return accountID;
    }

    @Override
    public String toString() {
        return String.format("%s%-4s", "Account id: ", accountID);
    }
}
