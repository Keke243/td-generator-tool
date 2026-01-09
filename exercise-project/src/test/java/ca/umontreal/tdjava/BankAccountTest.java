package ca.umontreal.tdjava;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void constructor_setsOwnerAndBalance() {
        BankAccount account = new BankAccount("Keren", 100.0);
        assertEquals("Keren", account.getOwner());
        assertEquals(100.0, account.getBalance(), 0.0001);
    }

    @Test
    void deposit_increasesBalance() {
        BankAccount account = new BankAccount("Keren", 100.0);
        account.deposit(50.0);
        assertEquals(150.0, account.getBalance(), 0.0001);
    }

    @Test
    void withdraw_decreasesBalance() {
        BankAccount account = new BankAccount("Keren", 100.0);
        account.withdraw(40.0);
        assertEquals(60.0, account.getBalance(), 0.0001);
    }

    @Test
    void withdraw_moreThanBalance_throwsException() {
        BankAccount account = new BankAccount("Keren", 100.0);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(200.0));
    }

    @Test
    void negativeInitialBalance_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("Keren", -10.0));
    }
}
