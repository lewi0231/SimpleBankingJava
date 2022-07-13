package banking;

import java.util.Scanner;

public class AccountManagement {


    public AccountManagement(String filename) {
        DatabaseManager.setFilename(filename);
        DatabaseManager.initializeTable();
    }

    public void displaySubMenu() {
        System.out.println("1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit\n");
    }

    public Account accountLogin(String cardNumber, String pin) {
        Account account = DatabaseManager.getAccount(cardNumber);
        if (account != null && account.comparePin(pin)) {
            return account;
        }

        return null;
    }


    public void addAccount(Account account) {
        int id = account.getId();
        String card = account.getCardNumber();
        String pin = account.getPin();
        int balance = account.getBalance();

        DatabaseManager.addAccount(new Account(id, card, pin, balance));
    }

    public boolean serviceAccount(int accountOption, Account customerAccount, Scanner scanner) {
        int option = accountOption;
        while (option != 0) {
            switch (option) {
                case 1:
                    System.out.println();
                    System.out.println("Balance: " + customerAccount.getBalance());
                    System.out.println();
                    this.displaySubMenu();
                    break;
                case 2:
                    System.out.println("Enter income:\n");
                    int income = scanner.nextInt();
                    customerAccount.addIncome(income);
                    DatabaseManager.updateAccountBalance(customerAccount);
                    this.displaySubMenu();
                    break;
                case 3:
                    System.out.println("Transfer");
                    System.out.println("Enter card number:\n");
                    String otherCard = scanner.next();

                    boolean isValid = CardChecking.isValidCardNumber(otherCard);
                    Account destinationAccount;

                    if (!isValid) {
                        System.out.println("Probably you made a mistake in the card number. " +
                                "\nPlease try again!");
                        this.displaySubMenu();
                        break;
                    }

                    destinationAccount = DatabaseManager.getAccount(otherCard);
                    int amount;

                    if (destinationAccount != null) {
                        System.out.println("Enter how much money you want to transfer:\n");
                        amount = scanner.nextInt();
                    } else {
                        System.out.println("Such a card does not exist.");
                        this.displaySubMenu();
                        break;
                    }

                    if (customerAccount.getBalance() < amount) {
                        System.out.println("Not enough money!");
                    } else {
                        customerAccount.withdrawAmount(amount);
                        destinationAccount.depositAmount(amount);
                        DatabaseManager.transferAmountTransaction(amount, customerAccount, destinationAccount);

                        System.out.println("Success!");
                        this.displaySubMenu();
                    }
                    break;

                case 4:
                    DatabaseManager.deleteAccount(customerAccount);
                    System.out.println("The account has been closed!");
                    return false;
                case 5:
                    System.out.println("You have successfully logged out!");
                    return false;
            }
            option = scanner.nextInt();
        }
        return true;
    }

    public static boolean isValidCardNumber(String cardNumber) {
        char last = cardNumber.charAt(cardNumber.length() - 1);
        String number = cardNumber.substring(0, cardNumber.length() - 1);
        int tally = 0;
        int num;
        for (int i = 0; i < number.length(); i++) {
            num = Integer.parseInt(String.valueOf(number.charAt(i)));

            if (i % 2 == 0) {
                num *= 2;
                if (num > 9) {
                    num -= 9;
                }
            }
            tally += num;
        }

        return (tally + Integer.parseInt(String.valueOf(last))) % 10 == 0;
    }
}
