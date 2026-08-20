package com.kodbank;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a financial transaction
 */
public class Transaction {
    private String transactionId;
    private String fromAccountId;
    private String toAccountId;
    private double amount;
    private TransactionType transactionType;
    private String description;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public Transaction(String transactionId, String fromAccountId, String toAccountId,
                      double amount, TransactionType transactionType, String description) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.description = description;
        this.status = TransactionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getDescription() {
        return description;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public boolean execute() {
        try {
            this.status = TransactionStatus.COMPLETED;
            this.completedAt = LocalDateTime.now();
            return true;
        } catch (Exception e) {
            this.status = TransactionStatus.FAILED;
            return false;
        }
    }

    public boolean cancel() {
        if (status == TransactionStatus.PENDING) {
            this.status = TransactionStatus.CANCELLED;
            return true;
        }
        return false;
    }

    public Map<String, Object> getDetails() {
        Map<String, Object> details = new HashMap<>();
        details.put("transaction_id", transactionId);
        details.put("from_account", fromAccountId);
        details.put("to_account", toAccountId);
        details.put("amount", amount);
        details.put("type", transactionType.getValue());
        details.put("status", status.getValue());
        details.put("description", description);
        details.put("created_at", createdAt);
        details.put("completed_at", completedAt);
        return details;
    }

    @Override
    public String toString() {
        return "Transaction(" + transactionId + ", " + transactionType.getValue() + ", $" + amount + ")";
    }
}
