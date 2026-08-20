"""
Main Bank class for KodBank
"""

from typing import Optional, List
from .user import UserManager, User
from .account import AccountManager, Account, AccountType
from .transaction import TransactionManager, Transaction, TransactionType


class Bank:
    """Main Bank class that orchestrates all banking operations"""
    
    def __init__(self, bank_name: str = "KodBank"):
        self.bank_name = bank_name
        self.user_manager = UserManager()
        self.account_manager = AccountManager()
        self.transaction_manager = TransactionManager()
    
    def __repr__(self):
        return f"Bank({self.bank_name}, Users: {len(self.user_manager.users)}, Accounts: {len(self.account_manager.accounts)})"
    
    # ==================== User Operations ====================
    
    def create_customer(self, user_id: str, name: str, email: str, phone: str, address: str) -> User:
        """Create a new bank customer"""
        return self.user_manager.create_user(user_id, name, email, phone, address)
    
    def get_customer(self, user_id: str) -> Optional[User]:
        """Get customer information"""
        return self.user_manager.get_user(user_id)
    
    def update_customer(self, user_id: str, name: str = None, email: str = None,
                       phone: str = None, address: str = None) -> bool:
        """Update customer information"""
        user = self.get_customer(user_id)
        if user:
            user.update_profile(name, email, phone, address)
            return True
        return False
    
    # ==================== Account Operations ====================
    
    def open_account(self, account_id: str, user_id: str, account_type: AccountType,
                    initial_balance: float = 0.0) -> Account:
        """Open a new bank account"""
        if not self.user_manager.user_exists(user_id):
            raise ValueError(f"User {user_id} does not exist")
        
        return self.account_manager.create_account(account_id, user_id, account_type, initial_balance)
    
    def get_account(self, account_id: str) -> Optional[Account]:
        """Get account information"""
        return self.account_manager.get_account(account_id)
    
    def get_customer_accounts(self, user_id: str) -> List[Account]:
        """Get all accounts for a customer"""
        return self.account_manager.get_user_accounts(user_id)
    
    def close_account(self, account_id: str) -> bool:
        """Close a bank account"""
        return self.account_manager.close_account(account_id)
    
    # ==================== Transaction Operations ====================
    
    def deposit(self, account_id: str, amount: float, description: str = "Deposit") -> bool:
        """Deposit money into an account"""
        account = self.get_account(account_id)
        if not account:
            raise ValueError(f"Account {account_id} not found")
        
        txn = self.transaction_manager.create_transaction(
            account_id, account_id, amount, TransactionType.DEPOSIT, description
        )
        account.deposit(amount)
        txn.execute()
        return True
    
    def withdraw(self, account_id: str, amount: float, description: str = "Withdrawal") -> bool:
        """Withdraw money from an account"""
        account = self.get_account(account_id)
        if not account:
            raise ValueError(f"Account {account_id} not found")
        
        if amount > account.get_balance():
            raise ValueError("Insufficient funds")
        
        txn = self.transaction_manager.create_transaction(
            account_id, None, amount, TransactionType.WITHDRAWAL, description
        )
        account.withdraw(amount)
        txn.execute()
        return True
    
    def transfer(self, from_account_id: str, to_account_id: str, amount: float,
                description: str = "Transfer") -> bool:
        """Transfer money between accounts"""
        from_account = self.get_account(from_account_id)
        to_account = self.get_account(to_account_id)
        
        if not from_account or not to_account:
            raise ValueError("One or both accounts not found")
        
        if amount > from_account.get_balance():
            raise ValueError("Insufficient funds")
        
        txn = self.transaction_manager.create_transaction(
            from_account_id, to_account_id, amount, TransactionType.TRANSFER, description
        )
        from_account.transfer(amount, to_account)
        txn.execute()
        return True
    
    # ==================== Reporting ====================
    
    def get_account_statement(self, account_id: str) -> dict:
        """Get account statement"""
        account = self.get_account(account_id)
        if not account:
            return {}
        
        return {
            'account_id': account.account_id,
            'account_type': account.account_type.value,
            'balance': account.get_balance(),
            'created_at': account.created_at,
            'is_active': account.is_active,
            'transactions': account.get_transaction_history()
        }
    
    def get_customer_summary(self, user_id: str) -> dict:
        """Get complete summary for a customer"""
        user = self.get_customer(user_id)
        if not user:
            return {}
        
        accounts = self.get_customer_accounts(user_id)
        total_balance = sum(acc.get_balance() for acc in accounts)
        
        return {
            'user_id': user.user_id,
            'name': user.name,
            'email': user.email,
            'phone': user.phone,
            'address': user.address,
            'total_accounts': len(accounts),
            'total_balance': total_balance,
            'accounts': [
                {
                    'account_id': acc.account_id,
                    'type': acc.account_type.value,
                    'balance': acc.get_balance()
                }
                for acc in accounts
            ]
        }
    
    def get_bank_statistics(self) -> dict:
        """Get overall bank statistics"""
        return {
            'bank_name': self.bank_name,
            'total_customers': len(self.user_manager.users),
            'total_accounts': len(self.account_manager.accounts),
            'total_transactions': len(self.transaction_manager.transactions),
            'total_deposits': sum(acc.get_balance() for acc in self.account_manager.accounts.values())
        }
