"""
Unit tests for KodBank application
"""

import unittest
from src.bank import Bank
from src.account import AccountType
from src.transaction import TransactionType


class TestBank(unittest.TestCase):
    """Test cases for Bank class"""
    
    def setUp(self):
        """Set up test fixtures"""
        self.bank = Bank("TestBank")
    
    def test_create_customer(self):
        """Test customer creation"""
        customer = self.bank.create_customer("U001", "Test User", "test@test.com",
                                             "555-0000", "Test St")
        self.assertIsNotNone(customer)
        self.assertEqual(customer.user_id, "U001")
        self.assertEqual(customer.name, "Test User")
    
    def test_open_account(self):
        """Test account opening"""
        self.bank.create_customer("U001", "Test User", "test@test.com", "555-0000", "Test St")
        account = self.bank.open_account("ACC001", "U001", AccountType.CHECKING, 1000.0)
        self.assertIsNotNone(account)
        self.assertEqual(account.get_balance(), 1000.0)
    
    def test_deposit(self):
        """Test deposit operation"""
        self.bank.create_customer("U001", "Test User", "test@test.com", "555-0000", "Test St")
        self.bank.open_account("ACC001", "U001", AccountType.CHECKING, 1000.0)
        
        self.bank.deposit("ACC001", 500)
        account = self.bank.get_account("ACC001")
        self.assertEqual(account.get_balance(), 1500.0)
    
    def test_withdraw(self):
        """Test withdrawal operation"""
        self.bank.create_customer("U001", "Test User", "test@test.com", "555-0000", "Test St")
        self.bank.open_account("ACC001", "U001", AccountType.CHECKING, 1000.0)
        
        self.bank.withdraw("ACC001", 300)
        account = self.bank.get_account("ACC001")
        self.assertEqual(account.get_balance(), 700.0)
    
    def test_transfer(self):
        """Test transfer operation"""
        self.bank.create_customer("U001", "User 1", "user1@test.com", "555-0001", "St 1")
        self.bank.create_customer("U002", "User 2", "user2@test.com", "555-0002", "St 2")
        self.bank.open_account("ACC001", "U001", AccountType.CHECKING, 1000.0)
        self.bank.open_account("ACC002", "U002", AccountType.CHECKING, 500.0)
        
        self.bank.transfer("ACC001", "ACC002", 200)
        
        acc1 = self.bank.get_account("ACC001")
        acc2 = self.bank.get_account("ACC002")
        self.assertEqual(acc1.get_balance(), 800.0)
        self.assertEqual(acc2.get_balance(), 700.0)
    
    def test_insufficient_funds(self):
        """Test insufficient funds error"""
        self.bank.create_customer("U001", "Test User", "test@test.com", "555-0000", "Test St")
        self.bank.open_account("ACC001", "U001", AccountType.CHECKING, 100.0)
        
        with self.assertRaises(ValueError):
            self.bank.withdraw("ACC001", 200)
    
    def test_customer_summary(self):
        """Test customer summary"""
        self.bank.create_customer("U001", "Test User", "test@test.com", "555-0000", "Test St")
        self.bank.open_account("ACC001", "U001", AccountType.CHECKING, 1000.0)
        self.bank.open_account("ACC002", "U001", AccountType.SAVINGS, 2000.0)
        
        summary = self.bank.get_customer_summary("U001")
        self.assertEqual(summary['total_accounts'], 2)
        self.assertEqual(summary['total_balance'], 3000.0)
    
    def test_bank_statistics(self):
        """Test bank statistics"""
        self.bank.create_customer("U001", "User 1", "user1@test.com", "555-0001", "St 1")
        self.bank.create_customer("U002", "User 2", "user2@test.com", "555-0002", "St 2")
        self.bank.open_account("ACC001", "U001", AccountType.CHECKING, 1000.0)
        self.bank.open_account("ACC002", "U002", AccountType.SAVINGS, 2000.0)
        
        stats = self.bank.get_bank_statistics()
        self.assertEqual(stats['total_customers'], 2)
        self.assertEqual(stats['total_accounts'], 2)
        self.assertEqual(stats['total_deposits'], 3000.0)


class TestAccount(unittest.TestCase):
    """Test cases for Account class"""
    
    def setUp(self):
        """Set up test fixtures"""
        from src.account import Account
        self.account = Account("ACC001", "U001", AccountType.CHECKING, 1000.0)
    
    def test_account_creation(self):
        """Test account creation"""
        self.assertEqual(self.account.account_id, "ACC001")
        self.assertEqual(self.account.get_balance(), 1000.0)
    
    def test_deposit_operation(self):
        """Test deposit"""
        self.account.deposit(500)
        self.assertEqual(self.account.get_balance(), 1500.0)
    
    def test_withdrawal_operation(self):
        """Test withdrawal"""
        self.account.withdraw(300)
        self.assertEqual(self.account.get_balance(), 700.0)


if __name__ == "__main__":
    unittest.main()
