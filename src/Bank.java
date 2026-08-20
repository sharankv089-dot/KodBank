package com.kodbank;

import java.util.*;

/**
 * Main Bank class that orchestrates all banking operations
 */
public class Bank {
    private String bankName;
    private UserManager userManager;
    private AccountManager accountManager;
    private TransactionManager transactionManager;

    public Bank(String bankName) {
        this.bankName = bankName;
        this.userManager = new UserManager();
        this.accountManager = new AccountManager();
        this.transactionManager = new TransactionManager();
    }

    // ==================== User Operations ====================

    public User createCustomer(String userId, String name, String email, String phone, String address) {
        return userManager.createUser(userId, name, email, phone, address);
    }

    public User getCustomer(String userId) {
        return userManager.getUser(userId);
    }

    public boolean updateCustomer(String userId, String name, String email, String phone, String address) {
        User user = getCustomer(userId);
        if (user != null) {
            user.updateProfile(name, email, phone, address);
            return true;
        }
        return false;
    }

    // ==================== Account Operations ====================

    public Account openAccount(String accountId, String userId, AccountType accountType, double initialBalance) {
        if (!userManager.userExists(userId)) {
            throw new IllegalArgumentException("User " + userId + " does not exist");
        }
        return accountManager.createAccount(accountId, userId, accountType, initialBalance);
    }

    public Account getAccount(String accountId) {
        return accountManager.getAccount(accountId);
    }

    public List<Account> getCustomerAccounts(String userId) {
        return accountManager.getUserAccounts(userId);
    }

    public boolean closeAccount(String accountId) {
        return accountManager.closeAccount(accountId);
    }

    // ==================== Transaction Operations ====================

    public boolean deposit(String accountId, double amount, String description) {
        Account account = getAccount(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account " + accountId + " not found");
        }
        
        Transaction txn = transactionManager.createTransaction(
            accountId, accountId, amount, TransactionType.DEPOSIT, description
        );
        account.deposit(amount);
        txn.execute();
        return true;
    }

    public boolean withdraw(String accountId, double amount, String description) {
        Account account = getAccount(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account " + accountId + " not found");
        }
        
        if (amount > account.getBalance()) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        Transaction txn = transactionManager.createTransaction(
            accountId, null, amount, TransactionType.WITHDRAWAL, description
        );
        account.withdraw(amount);
        txn.execute();
        return true;
    }

    public boolean transfer(String fromAccountId, String toAccountId, double amount, String description) {
        Account fromAccount = getAccount(fromAccountId);
        Account toAccount = getAccount(toAccountId);
        
        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("One or both accounts not found");
        }
        
        if (amount > fromAccount.getBalance()) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        Transaction txn = transactionManager.createTransaction(
            fromAccountId, toAccountId, amount, TransactionType.TRANSFER, description
        );
        fromAccount.transfer(amount, toAccount);
        txn.execute();
        return true;
    }

    // ==================== Reporting ====================

    public Map<String, Object> getAccountStatement(String accountId) {
        Account account = getAccount(accountId);
        if (account == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> statement = new HashMap<>();
        statement.put("account_id", account.getAccountId());
        statement.put("account_type", account.getAccountType().getValue());
        statement.put("balance", account.getBalance());
        statement.put("created_at", account.getCreatedAt());
        statement.put("is_active", account.isActive());
        statement.put("transactions", account.getTransactionHistory());
        return statement;
    }

    public Map<String, Object> getCustomerSummary(String userId) {
        User user = getCustomer(userId);
        if (user == null) {
            return new HashMap<>();
        }
        
        List<Account> accounts = getCustomerAccounts(userId);
        double totalBalance = 0;
        for (Account account : accounts) {
            totalBalance += account.getBalance();
        }
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("user_id", user.getUserId());
        summary.put("name", user.getName());
        summary.put("email", user.getEmail());
        summary.put("phone", user.getPhone());
        summary.put("address", user.getAddress());
        summary.put("total_accounts", accounts.size());
        summary.put("total_balance", totalBalance);
        
        List<Map<String, Object>> accountsList = new ArrayList<>();
        for (Account account : accounts) {
            Map<String, Object> accMap = new HashMap<>();
            accMap.put("account_id", account.getAccountId());
            accMap.put("type", account.getAccountType().getValue());
            accMap.put("balance", account.getBalance());
            accountsList.add(accMap);
        }
        summary.put("accounts", accountsList);
        
        return summary;
    }

    public Map<String, Object> getBankStatistics() {
        double totalDeposits = 0;
        for (Account account : accountManager.getAllAccounts()) {
            totalDeposits += account.getBalance();
        }
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("bank_name", bankName);
        stats.put("total_customers", userManager.getUserCount());
        stats.put("total_accounts", accountManager.getAccountCount());
        stats.put("total_transactions", transactionManager.getTransactionCount());
        stats.put("total_deposits", totalDeposits);
        return stats;
    }

    @Override
    public String toString() {
        return "Bank(" + bankName + ", Users: " + userManager.getUserCount() + 
               ", Accounts: " + accountManager.getAccountCount() + ")";
    }
}
