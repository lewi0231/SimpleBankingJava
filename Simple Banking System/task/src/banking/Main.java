package banking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String filename = args[1];

        Scanner scanner = new Scanner(System.in);
        String mainMenu = "1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit";

        AccountManagement accountManagement = new AccountManagement(filename);

        System.out.println(mainMenu);
        int option = scanner.nextInt();
        boolean exit = false;

        while (option != 0) {
            switch (option) {
                case 1:
                    Account account = new Account();
                    account.displayCreditCardInfo();
                    accountManagement.addAccount(account);
                    System.out.println();
                    System.out.println(mainMenu);
                    break;
                case 2:
                    System.out.println("Log into account");
                    System.out.println("Enter your card number");
                    String accountNumber = scanner.next();
                    System.out.println("Enter PIN");
                    String pin = String.valueOf(scanner.nextInt());

                    Account customerAccount = accountManagement.accountLogin(accountNumber, pin);
                    if (customerAccount == null) {
                        System.out.println("Wrong card number or PIN!");
                        System.out.println();
                        System.out.println(mainMenu);
                        break;
                    } else {
                        System.out.println("You have successfully logged in!");
                        System.out.println();
                        accountManagement.displaySubMenu();
                        int accountOption = scanner.nextInt();

                        exit = accountManagement.serviceAccount(accountOption,
                                customerAccount, scanner);
                        if (!exit) {
                            System.out.println(mainMenu);
                            break;
                        }
                    }
                    break;
                case 0:
                    exit = true;
                    break;
            }
            if (exit) {
                System.out.println("Bye!");
                option = 0;
            } else {
                option = scanner.nextInt();
            }
        }

    }
}
