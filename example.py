"""
Example usage of KodBank application
"""

from src.bank import Bank
from src.account import AccountType


def main():
    """Demonstrate KodBank functionality"""
    
    # Initialize the bank
    bank = Bank("KodBank")
    
    # Create customers
    print("=== Creating Customers ===")
    customer1 = bank.create_customer("C001", "John Doe", "john@example.com", 
                                    "555-1234", "123 Main St")
    customer2 = bank.create_customer("C002", "Jane Smith", "jane@example.com",
                                    "555-5678", "456 Oak Ave")
    print(f"Created: {customer1}")
    print(f"Created: {customer2}")
    
    # Open accounts
    print("\n=== Opening Accounts ===")
    account1 = bank.open_account("ACC001", "C001", AccountType.CHECKING, 1000.0)
    account2 = bank.open_account("ACC002", "C001", AccountType.SAVINGS, 5000.0)
    account3 = bank.open_account("ACC003", "C002", AccountType.CHECKING, 2000.0)
    print(f"Opened: {account1}")
    print(f"Opened: {account2}")
    print(f"Opened: {account3}")
    
    # Perform transactions
    print("\n=== Performing Transactions ===")
    bank.deposit("ACC001", 500, "Salary Deposit")
    print(f"Deposited $500 to ACC001. Balance: ${account1.get_balance()}")
    
    bank.withdraw("ACC002", 1000, "ATM Withdrawal")
    print(f"Withdrew $1000 from ACC002. Balance: ${account2.get_balance()}")
    
    bank.transfer("ACC001", "ACC003", 300, "Payment to Jane")
    print(f"Transferred $300 from ACC001 to ACC003")
    print(f"ACC001 Balance: ${account1.get_balance()}")
    print(f"ACC003 Balance: ${account3.get_balance()}")
    
    # Get statements
    print("\n=== Account Statements ===")
    stmt1 = bank.get_account_statement("ACC001")
    print(f"Account: {stmt1['account_id']}")
    print(f"Balance: ${stmt1['balance']}")
    print(f"Transactions: {len(stmt1['transactions'])}")
    
    # Get customer summary
    print("\n=== Customer Summary ===")
    summary = bank.get_customer_summary("C001")
    print(f"Customer: {summary['name']}")
    print(f"Total Accounts: {summary['total_accounts']}")
    print(f"Total Balance: ${summary['total_balance']}")
    
    # Get bank statistics
    print("\n=== Bank Statistics ===")
    stats = bank.get_bank_statistics()
    print(f"Bank: {stats['bank_name']}")
    print(f"Total Customers: {stats['total_customers']}")
    print(f"Total Accounts: {stats['total_accounts']}")
    print(f"Total Transactions: {stats['total_transactions']}")
    print(f"Total Deposits: ${stats['total_deposits']}")


if __name__ == "__main__":
    main()
