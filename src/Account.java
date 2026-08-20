package com.kodbank;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a bank account
 */
public class Account {
    private String accountId;
    private String userId;
    private AccountType accountType;
    private double balance;
    private LocalDateTime createdAt;
    private boolean isActive;
    private List<Map<String, Object>> transactionHistory;

    public Account(String accountId, String userId, AccountType accountType, double initialBalance) {
        this.accountId = accountId;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = initialBalance;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public String getUserId() {
        return userId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        
        balance += amount;
        recordTransaction("deposit", amount);
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        balance -= amount;
        recordTransaction("withdrawal", amount);
        return true;
    }

    public boolean transfer(double amount, Account targetAccount) {
        if (!isActive || !targetAccount.isActive) {
            throw new IllegalArgumentException("Both accounts must be active");
        }
        
        this.withdraw(amount);
        targetAccount.deposit(amount);
        return true;
    }

    public boolean closeAccount() {
        if (balance != 0) {
            throw new IllegalArgumentException("Cannot close account with non-zero balance");
        }
        this.isActive = false;
        return true;
    }

    public List<Map<String, Object>> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    private void recordTransaction(String type, double amount) {
        Map<String, Object> transaction = new HashMap<>();
        transaction.put("type", type);
        transaction.put("amount", amount);
        transaction.put("timestamp", LocalDateTime.now());
        transaction.put("balance_after", balance);
        transactionHistory.add(transaction);
    }

    @Override
    public String toString() {
        return "Account(" + accountId + ", " + accountType.getValue() + ", Balance: $" + balance + ")";
    }
}
