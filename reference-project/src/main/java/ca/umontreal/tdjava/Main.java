package ca.umontreal.tdjava;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Demo BankAccount (reference project) ===");

        BankAccount account = new BankAccount("Alice", 100.0);

        System.out.println("Owner: " + account.getOwner());
        System.out.println("Initial balance: " + account.getBalance());

        System.out.println("\nDepositing 50...");
        account.deposit(50.0);
        System.out.println("Balance after deposit: " + account.getBalance());

        System.out.println("\nWithdrawing 30...");
        account.withdraw(30.0);
        System.out.println("Balance after withdrawal: " + account.getBalance());

        System.out.println("\nDemo finished successfully.");
    }
}
