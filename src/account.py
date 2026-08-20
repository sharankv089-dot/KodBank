"""
Account management module for KodBank
"""

from datetime import datetime
from typing import Optional, List
from enum import Enum


class AccountType(Enum):
    """Types of bank accounts"""
    SAVINGS = "savings"
    CHECKING = "checking"
    MONEY_MARKET = "money_market"
    CD = "certificate_of_deposit"


class Account:
    """Represents a bank account"""
    
    def __init__(self, account_id: str, user_id: str, account_type: AccountType, 
                 initial_balance: float = 0.0):
        self.account_id = account_id
        self.user_id = user_id
        self.account_type = account_type
        self.balance = initial_balance
        self.created_at = datetime.now()
        self.is_active = True
        self.transaction_history = []
    
    def __repr__(self):
        return f"Account({self.account_id}, {self.account_type.value}, Balance: ${self.balance})"
    
    def deposit(self, amount: float) -> bool:
        """Deposit money into account"""
        if amount <= 0:
            raise ValueError("Deposit amount must be positive")
        
        self.balance += amount
        self.transaction_history.append({
            'type': 'deposit',
            'amount': amount,
            'timestamp': datetime.now(),
            'balance_after': self.balance
        })
        return True
    
    def withdraw(self, amount: float) -> bool:
        """Withdraw money from account"""
        if amount <= 0:
            raise ValueError("Withdrawal amount must be positive")
        
        if amount > self.balance:
            raise ValueError("Insufficient funds")
        
        self.balance -= amount
        self.transaction_history.append({
            'type': 'withdrawal',
            'amount': amount,
            'timestamp': datetime.now(),
            'balance_after': self.balance
        })
        return True
    
    def transfer(self, amount: float, target_account: 'Account') -> bool:
        """Transfer money to another account"""
        if not self.is_active or not target_account.is_active:
            raise ValueError("Both accounts must be active")
        
        self.withdraw(amount)
        target_account.deposit(amount)
        
        return True
    
    def get_balance(self) -> float:
        """Get current account balance"""
        return self.balance
    
    def close_account(self):
        """Close the account"""
        if self.balance != 0:
            raise ValueError("Cannot close account with non-zero balance")
        self.is_active = False
    
    def get_transaction_history(self) -> List[dict]:
        """Get account transaction history"""
        return self.transaction_history.copy()


class AccountManager:
    """Manages account operations"""
    
    def __init__(self):
        self.accounts: dict = {}
    
    def create_account(self, account_id: str, user_id: str, account_type: AccountType,
                      initial_balance: float = 0.0) -> Account:
        """Create a new account"""
        if account_id in self.accounts:
            raise ValueError(f"Account {account_id} already exists")
        
        account = Account(account_id, user_id, account_type, initial_balance)
        self.accounts[account_id] = account
        return account
    
    def get_account(self, account_id: str) -> Optional[Account]:
        """Retrieve account by ID"""
        return self.accounts.get(account_id)
    
    def get_user_accounts(self, user_id: str) -> List[Account]:
        """Get all accounts for a user"""
        return [acc for acc in self.accounts.values() if acc.user_id == user_id]
    
    def close_account(self, account_id: str) -> bool:
        """Close an account"""
        account = self.get_account(account_id)
        if account:
            account.close_account()
            return True
        return False
    
    def delete_account(self, account_id: str) -> bool:
        """Delete an account"""
        if account_id in self.accounts:
            del self.accounts[account_id]
            return True
        return False
    
    def get_total_balance(self, user_id: str) -> float:
        """Get total balance across all accounts for a user"""
        accounts = self.get_user_accounts(user_id)
        return sum(acc.get_balance() for acc in accounts)
