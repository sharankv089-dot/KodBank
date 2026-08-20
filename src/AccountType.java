package com.kodbank;

/**
 * Types of bank accounts
 */
public enum AccountType {
    SAVINGS("savings"),
    CHECKING("checking"),
    MONEY_MARKET("money_market"),
    CD("certificate_of_deposit");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
