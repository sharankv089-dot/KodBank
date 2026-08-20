"""
Transaction management module for KodBank
"""

from datetime import datetime
from typing import Optional, List
from enum import Enum


class TransactionType(Enum):
    """Types of transactions"""
    DEPOSIT = "deposit"
    WITHDRAWAL = "withdrawal"
    TRANSFER = "transfer"
    PAYMENT = "payment"
    INTEREST = "interest"


class TransactionStatus(Enum):
    """Status of transactions"""
    PENDING = "pending"
    COMPLETED = "completed"
    FAILED = "failed"
    CANCELLED = "cancelled"


class Transaction:
    """Represents a financial transaction"""
    
    def __init__(self, transaction_id: str, from_account_id: str, to_account_id: Optional[str],
                 amount: float, transaction_type: TransactionType, description: str = ""):
        self.transaction_id = transaction_id
        self.from_account_id = from_account_id
        self.to_account_id = to_account_id
        self.amount = amount
        self.transaction_type = transaction_type
        self.description = description
        self.status = TransactionStatus.PENDING
        self.created_at = datetime.now()
        self.completed_at: Optional[datetime] = None
    
    def __repr__(self):
        return f"Transaction({self.transaction_id}, {self.transaction_type.value}, ${self.amount})"
    
    def execute(self) -> bool:
        """Execute the transaction"""
        try:
            self.status = TransactionStatus.COMPLETED
            self.completed_at = datetime.now()
            return True
        except Exception as e:
            self.status = TransactionStatus.FAILED
            return False
    
    def cancel(self):
        """Cancel the transaction"""
        if self.status == TransactionStatus.PENDING:
            self.status = TransactionStatus.CANCELLED
            return True
        return False
    
    def get_details(self) -> dict:
        """Get transaction details"""
        return {
            'transaction_id': self.transaction_id,
            'from_account': self.from_account_id,
            'to_account': self.to_account_id,
            'amount': self.amount,
            'type': self.transaction_type.value,
            'status': self.status.value,
            'description': self.description,
            'created_at': self.created_at,
            'completed_at': self.completed_at
        }


class TransactionManager:
    """Manages transaction operations"""
    
    def __init__(self):
        self.transactions: dict = {}
        self.transaction_counter = 0
    
    def create_transaction(self, from_account_id: str, to_account_id: Optional[str],
                          amount: float, transaction_type: TransactionType,
                          description: str = "") -> Transaction:
        """Create a new transaction"""
        self.transaction_counter += 1
        transaction_id = f"TXN{self.transaction_counter:06d}"
        
        transaction = Transaction(transaction_id, from_account_id, to_account_id,
                                 amount, transaction_type, description)
        self.transactions[transaction_id] = transaction
        return transaction
    
    def get_transaction(self, transaction_id: str) -> Optional[Transaction]:
        """Retrieve transaction by ID"""
        return self.transactions.get(transaction_id)
    
    def execute_transaction(self, transaction_id: str) -> bool:
        """Execute a transaction"""
        transaction = self.get_transaction(transaction_id)
        if transaction:
            return transaction.execute()
        return False
    
    def cancel_transaction(self, transaction_id: str) -> bool:
        """Cancel a transaction"""
        transaction = self.get_transaction(transaction_id)
        if transaction:
            return transaction.cancel()
        return False
    
    def get_account_transactions(self, account_id: str) -> List[Transaction]:
        """Get all transactions for an account"""
        return [txn for txn in self.transactions.values() 
                if txn.from_account_id == account_id or txn.to_account_id == account_id]
    
    def get_transactions_by_status(self, status: TransactionStatus) -> List[Transaction]:
        """Get transactions by status"""
        return [txn for txn in self.transactions.values() if txn.status == status]
    
    def get_account_balance_change(self, account_id: str) -> float:
        """Calculate net balance change for an account"""
        transactions = self.get_account_transactions(account_id)
        change = 0.0
        for txn in transactions:
            if txn.status == TransactionStatus.COMPLETED:
                if txn.from_account_id == account_id and txn.transaction_type != TransactionType.DEPOSIT:
                    change -= txn.amount
                elif txn.to_account_id == account_id or txn.transaction_type == TransactionType.DEPOSIT:
                    change += txn.amount
        return change
