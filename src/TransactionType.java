package com.kodbank;

/**
 * Types of transactions
 */
public enum TransactionType {
    DEPOSIT("deposit"),
    WITHDRAWAL("withdrawal"),
    TRANSFER("transfer"),
    PAYMENT("payment"),
    INTEREST("interest");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
