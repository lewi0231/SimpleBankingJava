package banking;

import java.util.Objects;
import java.util.Random;

public class Account {
    public static int id = 0;
    Random random = new Random();

    String pin;

    String cardNumber;

    int balance = 0;

    public Account() {
        id++;
        int BIN = 400000;
        int accountIdentificationNumber = (random.nextInt(900000000) + 100000000);
        this.pin = "" + (random.nextInt(9000) + 1000);
        String number = "" + BIN + accountIdentificationNumber;
        int checksum = CardChecking.generateChecksum(number);
        this.cardNumber = number + checksum;
    }

    public Account(int id, String cardNumber, String pin, int balance) {
        id++;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    public void withdrawAmount(int amount) {
        balance = balance - amount;
    }

    public void depositAmount(int amount) {
        balance += amount;
    }

    public boolean comparePin(String pin) {
        return Objects.equals(this.pin, pin);
    }

    public int getId() {
        return id;
    }


    public void displayCreditCardInfo() {
        System.out.println("\nYour card has been created\nYour card number:\n" + getCardNumber() +
                "\nYour card PIN:\n" + pin);
    }

    public void addIncome(int income) {
        balance += income;
    }


}
