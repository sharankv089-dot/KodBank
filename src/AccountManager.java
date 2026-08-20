package com.kodbank;

import java.util.*;

/**
 * Manages account operations
 */
public class AccountManager {
    private Map<String, Account> accounts;

    public AccountManager() {
        this.accounts = new HashMap<>();
    }

    public Account createAccount(String accountId, String userId, AccountType accountType, double initialBalance) {
        if (accounts.containsKey(accountId)) {
            throw new IllegalArgumentException("Account " + accountId + " already exists");
        }
        
        Account account = new Account(accountId, userId, accountType, initialBalance);
        accounts.put(accountId, account);
        return account;
    }

    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    public List<Account> getUserAccounts(String userId) {
        List<Account> userAccounts = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (account.getUserId().equals(userId)) {
                userAccounts.add(account);
            }
        }
        return userAccounts;
    }

    public boolean closeAccount(String accountId) {
        Account account = getAccount(accountId);
        if (account != null) {
            account.closeAccount();
            return true;
        }
        return false;
    }

    public boolean deleteAccount(String accountId) {
        return accounts.remove(accountId) != null;
    }

    public double getTotalBalance(String userId) {
        List<Account> userAccounts = getUserAccounts(userId);
        double total = 0;
        for (Account account : userAccounts) {
            total += account.getBalance();
        }
        return total;
    }

    public int getAccountCount() {
        return accounts.size();
    }

    public Collection<Account> getAllAccounts() {
        return accounts.values();
    }
}
