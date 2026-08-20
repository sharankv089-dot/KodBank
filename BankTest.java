import com.kodbank.*;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for KodBank application
 */
public class BankTest {
    
    private Bank bank;
    private int testCounter = 0;
    
    public BankTest() {
        this.bank = new Bank("TestBank");
    }
    
    private String getUniqueId(String prefix) {
        return prefix + (++testCounter);
    }
    
    public void testCreateCustomer() {
        System.out.println("Testing: Create Customer");
        String userId = getUniqueId("U");
        User customer = bank.createCustomer(userId, "Test User", "test@test.com", "555-0000", "Test St");
        assert customer != null : "Customer should not be null";
        assert customer.getUserId().equals(userId) : "User ID should match";
        assert customer.getName().equals("Test User") : "Name should be Test User";
        System.out.println("✓ PASSED: Create Customer");
    }
    
    public void testOpenAccount() {
        System.out.println("Testing: Open Account");
        String userId = getUniqueId("U");
        String accId = getUniqueId("ACC");
        bank.createCustomer(userId, "Test User", "test@test.com", "555-0000", "Test St");
        Account account = bank.openAccount(accId, userId, AccountType.CHECKING, 1000.0);
        assert account != null : "Account should not be null";
        assert account.getBalance() == 1000.0 : "Balance should be 1000.0";
        System.out.println("✓ PASSED: Open Account");
    }
    
    public void testDeposit() {
        System.out.println("Testing: Deposit");
        String userId = getUniqueId("U");
        String accId = getUniqueId("ACC");
        bank.createCustomer(userId, "Test User", "test@test.com", "555-0000", "Test St");
        bank.openAccount(accId, userId, AccountType.CHECKING, 1000.0);
        bank.deposit(accId, 500, "Test Deposit");
        Account account = bank.getAccount(accId);
        assert account.getBalance() == 1500.0 : "Balance should be 1500.0";
        System.out.println("✓ PASSED: Deposit");
    }
    
    public void testWithdraw() {
        System.out.println("Testing: Withdraw");
        String userId = getUniqueId("U");
        String accId = getUniqueId("ACC");
        bank.createCustomer(userId, "Test User", "test@test.com", "555-0000", "Test St");
        bank.openAccount(accId, userId, AccountType.CHECKING, 1000.0);
        bank.withdraw(accId, 300, "Test Withdrawal");
        Account account = bank.getAccount(accId);
        assert account.getBalance() == 700.0 : "Balance should be 700.0";
        System.out.println("✓ PASSED: Withdraw");
    }
    
    public void testTransfer() {
        System.out.println("Testing: Transfer");
        String userId1 = getUniqueId("U");
        String userId2 = getUniqueId("U");
        String accId1 = getUniqueId("ACC");
        String accId2 = getUniqueId("ACC");
        bank.createCustomer(userId1, "User 1", "user1@test.com", "555-0001", "St 1");
        bank.createCustomer(userId2, "User 2", "user2@test.com", "555-0002", "St 2");
        bank.openAccount(accId1, userId1, AccountType.CHECKING, 1000.0);
        bank.openAccount(accId2, userId2, AccountType.CHECKING, 500.0);
        bank.transfer(accId1, accId2, 200, "Test Transfer");
        
        Account acc1 = bank.getAccount(accId1);
        Account acc2 = bank.getAccount(accId2);
        assert acc1.getBalance() == 800.0 : "ACC001 balance should be 800.0";
        assert acc2.getBalance() == 700.0 : "ACC002 balance should be 700.0";
        System.out.println("✓ PASSED: Transfer");
    }
    
    public void testInsufficientFunds() {
        System.out.println("Testing: Insufficient Funds");
        String userId = getUniqueId("U");
        String accId = getUniqueId("ACC");
        bank.createCustomer(userId, "Test User", "test@test.com", "555-0000", "Test St");
        bank.openAccount(accId, userId, AccountType.CHECKING, 100.0);
        
        try {
            bank.withdraw(accId, 200, "Should Fail");
            assert false : "Should have thrown exception";
        } catch (IllegalArgumentException e) {
            System.out.println("✓ PASSED: Insufficient Funds");
        }
    }
    
    public void testCustomerSummary() {
        System.out.println("Testing: Customer Summary");
        String userId = getUniqueId("U");
        String accId1 = getUniqueId("ACC");
        String accId2 = getUniqueId("ACC");
        bank.createCustomer(userId, "Test User", "test@test.com", "555-0000", "Test St");
        bank.openAccount(accId1, userId, AccountType.CHECKING, 1000.0);
        bank.openAccount(accId2, userId, AccountType.SAVINGS, 2000.0);
        
        Map<String, Object> summary = bank.getCustomerSummary(userId);
        assert (int) summary.get("total_accounts") == 2 : "Should have 2 accounts";
        assert (double) summary.get("total_balance") == 3000.0 : "Total balance should be 3000.0";
        System.out.println("✓ PASSED: Customer Summary");
    }
    
    public void testBankStatistics() {
        System.out.println("Testing: Bank Statistics");
        String userId1 = getUniqueId("U");
        String userId2 = getUniqueId("U");
        String accId1 = getUniqueId("ACC");
        String accId2 = getUniqueId("ACC");
        bank.createCustomer(userId1, "User 1", "user1@test.com", "555-0001", "St 1");
        bank.createCustomer(userId2, "User 2", "user2@test.com", "555-0002", "St 2");
        bank.openAccount(accId1, userId1, AccountType.CHECKING, 1000.0);
        bank.openAccount(accId2, userId2, AccountType.SAVINGS, 2000.0);
        
        Map<String, Object> stats = bank.getBankStatistics();
        assert (int) stats.get("total_customers") == 2 : "Should have 2 customers";
        assert (int) stats.get("total_accounts") == 2 : "Should have 2 accounts";
        assert (double) stats.get("total_deposits") == 3000.0 : "Total deposits should be 3000.0";
        System.out.println("✓ PASSED: Bank Statistics");
    }
    
    public void runAllTests() {
        System.out.println("========== KodBank Test Suite ==========\n");
        try {
            testCreateCustomer();
            testOpenAccount();
            testDeposit();
            testWithdraw();
            testTransfer();
            testInsufficientFunds();
            testCustomerSummary();
            testBankStatistics();
            System.out.println("\n========== All Tests Passed! ==========");
        } catch (AssertionError e) {
            System.out.println("\n✗ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        BankTest test = new BankTest();
        test.runAllTests();
    }
}
