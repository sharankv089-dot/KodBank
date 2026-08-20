package com.kodbank;

import java.util.List;
import java.util.Map;

/**
 * Example usage of KodBank application
 */
public class KodBankExample {
    
    public static void main(String[] args) {
        // Initialize the bank
        Bank bank = new Bank("KodBank");
        
        // Create customers
        System.out.println("=== Creating Customers ===");
        User customer1 = bank.createCustomer("C001", "John Doe", "john@example.com",
                                             "555-1234", "123 Main St");
        User customer2 = bank.createCustomer("C002", "Jane Smith", "jane@example.com",
                                             "555-5678", "456 Oak Ave");
        System.out.println("Created: " + customer1);
        System.out.println("Created: " + customer2);
        
        // Open accounts
        System.out.println("\n=== Opening Accounts ===");
        Account account1 = bank.openAccount("ACC001", "C001", AccountType.CHECKING, 1000.0);
        Account account2 = bank.openAccount("ACC002", "C001", AccountType.SAVINGS, 5000.0);
        Account account3 = bank.openAccount("ACC003", "C002", AccountType.CHECKING, 2000.0);
        System.out.println("Opened: " + account1);
        System.out.println("Opened: " + account2);
        System.out.println("Opened: " + account3);
        
        // Perform transactions
        System.out.println("\n=== Performing Transactions ===");
        bank.deposit("ACC001", 500, "Salary Deposit");
        System.out.println("Deposited $500 to ACC001. Balance: $" + account1.getBalance());
        
        bank.withdraw("ACC002", 1000, "ATM Withdrawal");
        System.out.println("Withdrew $1000 from ACC002. Balance: $" + account2.getBalance());
        
        bank.transfer("ACC001", "ACC003", 300, "Payment to Jane");
        System.out.println("Transferred $300 from ACC001 to ACC003");
        System.out.println("ACC001 Balance: $" + account1.getBalance());
        System.out.println("ACC003 Balance: $" + account3.getBalance());
        
        // Get statements
        System.out.println("\n=== Account Statements ===");
        Map<String, Object> stmt1 = bank.getAccountStatement("ACC001");
        System.out.println("Account: " + stmt1.get("account_id"));
        System.out.println("Balance: $" + stmt1.get("balance"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> transactions = (List<Map<String, Object>>) stmt1.get("transactions");
        System.out.println("Transactions: " + transactions.size());
        
        // Get customer summary
        System.out.println("\n=== Customer Summary ===");
        Map<String, Object> summary = bank.getCustomerSummary("C001");
        System.out.println("Customer: " + summary.get("name"));
        System.out.println("Total Accounts: " + summary.get("total_accounts"));
        System.out.println("Total Balance: $" + summary.get("total_balance"));
        
        // Get bank statistics
        System.out.println("\n=== Bank Statistics ===");
        Map<String, Object> stats = bank.getBankStatistics();
        System.out.println("Bank: " + stats.get("bank_name"));
        System.out.println("Total Customers: " + stats.get("total_customers"));
        System.out.println("Total Accounts: " + stats.get("total_accounts"));
        System.out.println("Total Transactions: " + stats.get("total_transactions"));
        System.out.println("Total Deposits: $" + stats.get("total_deposits"));
    }
}
