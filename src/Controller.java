import org.sqlite.SQLiteDataSource;

import java.util.Random;
import java.util.Scanner;

public class Controller {

    static Jdbc jdbc;
    Account account;

    public void init(String dbName) {
        String url = "jdbc:sqlite:" + dbName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        jdbc = new Jdbc(dataSource);
        jdbc.init();
        runApplication();
    }

    public void runApplication() {
        Scanner scanner = new Scanner(System.in);
        helloMessage();
        boolean exit = false;
        while (!exit) {
            int chosenNumber = scanner.nextInt();
            switch (chosenNumber) {
                case 1:
                    createAccount();
                    runApplication();
                    break;
                case 2:
                    logIn();
                    break;
                case 0:
                    System.out.print("Bye!");
                    exit = true;
                    System.exit(0);
            }
        }
    }

    void helloMessage() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    void createAccount() {
        account = new Account();
        Random random = new Random();
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        String cardNumber = generateCardNumber(random);
        String pin = generatePin(random);
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(pin);
        account.setNumber(cardNumber);
        account.setPin(pin);
        account.setBalance(0);
        jdbc.addNewAccount(account);
    }

    String generateCardNumber(Random random) {
        int[] numberMII = new int[16];
        int[] temporaryNumber = new int[16];
        numberMII[0] = 4;
        temporaryNumber[0] = 4;
        for (int i = 6; i < numberMII.length - 1; i++) {
            int digit = random.nextInt(10);
            numberMII[i] = digit;
            temporaryNumber[i] = digit;
        }
        for (int i = 0; i < temporaryNumber.length - 1; i += 2) {   //multiply odd numbers by two
            temporaryNumber[i] *= 2;
        }

        for (int i = 0; i < temporaryNumber.length - 1; i++) {  //subtract 9 to numbers over 9
            if (temporaryNumber[i] > 9) {
                temporaryNumber[i] -= 9;
            }
        }
        int sum = 0;
        for (int i = 0; i < temporaryNumber.length - 1; i++) {
            sum += temporaryNumber[i];
        }
        int checksum = 0;
        if (sum % 10 != 0) {
            checksum = 10 - sum % 10;// sum 16 digits modulo 10
        }

        numberMII[15] = checksum;
        StringBuilder cardNumber = new StringBuilder();
        for (int number : numberMII) {
            cardNumber.append(number);
        }
        return cardNumber.toString();
    }

    String generatePin(Random random) {
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            pin.append(random.nextInt(10));
        }
        return pin.toString();
    }

    void logIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        String number = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();
        boolean accept = false;

        account = jdbc.findAccount(number, pin);
        if (account != null) {
            accept = true;
        }
        if (accept) {
            System.out.println("You have successfully logged in!");
            balanceSite();
        } else {
            System.out.println("Wrong card number or PIN!");
            runApplication();
        }
    }

    void balanceSite() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
        int chosenNumber = scanner.nextInt();
        switch (chosenNumber) {
            case 1:
                balanceValueSite();
                balanceSite();
                break;
            case 2:
                incomeSite();
                balanceSite();
                break;
            case 3:
                moneyTransferSite();
                balanceSite();
                break;
            case 4:
                closeAccount();
                helloMessage();
                break;
            case 5:
                System.out.println("You have successfully logged out!");
                runApplication();
                break;
            case 0:
                System.out.print("Bye!");
                System.exit(0);
        }
    }

    void incomeSite() {
        System.out.println("Enter income:");
        Scanner scanner = new Scanner(System.in);
        int income = scanner.nextInt();
        account.setBalance(account.getBalance() + income);
        jdbc.updateAccountBalance(account);
        System.out.println("Income was added!");
    }

    void balanceValueSite() {
        int balance = jdbc.getBalance(account);
        System.out.println("Balance: " + balance);
    }

    void closeAccount() {
        jdbc.closeAccount(account);
        System.out.println("The account has been closed!");
    }

    void moneyTransferSite() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String transferAccount = scanner.next();
        boolean checkLuhn = checkLuhnAlgoritm(transferAccount);
        if (!checkLuhn) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
            return;
        }
        boolean checkAccountExists = jdbc.findTransferAccount(transferAccount);
        if (!checkAccountExists) {
            System.out.println("Such a card does not exist.");
            return;
        }
        System.out.println("Enter how much money you want to transfer:");
        int money = scanner.nextInt();
        boolean checkMoney = jdbc.isMoneyEnough(money, account);
        if (!checkMoney) {
            System.out.println("Not enough money!");
            return;
        }
        jdbc.transfer(account, transferAccount, money);
        System.out.println("Success!");
    }

    boolean checkLuhnAlgoritm(String checkAccount) {
        int[] cardNumber = new int[16];
        for (int i = 0; i < cardNumber.length; i++) {
            cardNumber[i] = Integer.parseInt(String.valueOf(checkAccount.charAt(i)));
        }
        for (int i = 0; i < cardNumber.length; i += 2) {
            cardNumber[i] *= 2;
        }
        for (int i = 0; i < cardNumber.length; i += 2) {
            if (cardNumber[i] > 9) {
                cardNumber[i] -= 9;
            }
        }
        int sum = 0;
        for (int i = 0; i < cardNumber.length; i++) {
            sum += cardNumber[i];
        }
        if (sum % 10 == 0) {
            return true;
        } else {
            return false;
        }
    }
}