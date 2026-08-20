package com.kodbank;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages transaction operations
 */
public class TransactionManager {
    private Map<String, Transaction> transactions;
    private AtomicInteger transactionCounter;

    public TransactionManager() {
        this.transactions = new HashMap<>();
        this.transactionCounter = new AtomicInteger(0);
    }

    public Transaction createTransaction(String fromAccountId, String toAccountId, double amount,
                                        TransactionType transactionType, String description) {
        int counter = transactionCounter.incrementAndGet();
        String transactionId = String.format("TXN%06d", counter);
        
        Transaction transaction = new Transaction(transactionId, fromAccountId, toAccountId,
                                                 amount, transactionType, description);
        transactions.put(transactionId, transaction);
        return transaction;
    }

    public Transaction getTransaction(String transactionId) {
        return transactions.get(transactionId);
    }

    public boolean executeTransaction(String transactionId) {
        Transaction transaction = getTransaction(transactionId);
        if (transaction != null) {
            return transaction.execute();
        }
        return false;
    }

    public boolean cancelTransaction(String transactionId) {
        Transaction transaction = getTransaction(transactionId);
        if (transaction != null) {
            return transaction.cancel();
        }
        return false;
    }

    public List<Transaction> getAccountTransactions(String accountId) {
        List<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction txn : transactions.values()) {
            if (accountId.equals(txn.getFromAccountId()) || accountId.equals(txn.getToAccountId())) {
                accountTransactions.add(txn);
            }
        }
        return accountTransactions;
    }

    public List<Transaction> getTransactionsByStatus(TransactionStatus status) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction txn : transactions.values()) {
            if (txn.getStatus() == status) {
                result.add(txn);
            }
        }
        return result;
    }

    public double getAccountBalanceChange(String accountId) {
        List<Transaction> accountTransactions = getAccountTransactions(accountId);
        double change = 0.0;
        
        for (Transaction txn : accountTransactions) {
            if (txn.getStatus() == TransactionStatus.COMPLETED) {
                if (accountId.equals(txn.getFromAccountId()) && txn.getTransactionType() != TransactionType.DEPOSIT) {
                    change -= txn.getAmount();
                } else if (accountId.equals(txn.getToAccountId()) || txn.getTransactionType() == TransactionType.DEPOSIT) {
                    change += txn.getAmount();
                }
            }
        }
        return change;
    }

    public int getTransactionCount() {
        return transactions.size();
    }
}
