package ca.umontreal.tdjava;

public class BankAccount {

    private final String owner;

    private double balance;

    public BankAccount(String owner, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.owner = owner;
        this.balance = initialBalance;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        throw new UnsupportedOperationException("TODO");
    }

    public void withdraw(double amount) {
        throw new UnsupportedOperationException("TODO");
    }
}
