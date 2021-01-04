import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Map<String, String> customersAccounts = new HashMap<>();
        runApplication(customersAccounts);
    }

    static void runApplication(Map<String, String> customersAccounts) {
        Scanner scanner = new Scanner(System.in);
        helloMessage();
        boolean exit = false;
        while (!exit) {
            int chosenNumber = scanner.nextInt();
            switch (chosenNumber) {
                case 1:
                    createAccount(customersAccounts);
                    runApplication(customersAccounts);
                    break;
                case 2:
                    logIn(customersAccounts);
                    break;
                case 0:
                    System.out.print("Bye!");
                    exit = true;
                    System.exit(0);
            }
        }
    }

    static void helloMessage() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    static void createAccount(Map<String, String> customersAccounts) {
        Random random = new Random();
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
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
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            pin.append(random.nextInt(10));
        }
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(pin);
        customersAccounts.put(cardNumber.toString(), pin.toString());
    }

    static void logIn(Map<String, String> customersAccounts) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        String number = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();
        boolean accept = false;
        for (Map.Entry<String, String> accountNumbers : customersAccounts.entrySet()) {
            if (accountNumbers.getKey().equals(number) && accountNumbers.getValue().equals(pin)) {
                accept = true;
                break;
            }
        }
        if (accept) {
            System.out.println("You have successfully logged in!");
            balanceSite(customersAccounts);
        } else {
            System.out.println("Wrong card number or PIN!");
            runApplication(customersAccounts);
        }
    }

    static void balanceSite(Map<String, String> customersAccounts) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Balance");
        System.out.println("2. Log out");
        System.out.println("0. Exit");
        int chosenNumber = scanner.nextInt();
        switch (chosenNumber) {
            case 1:
                System.out.println("Balance: 0");
                balanceSite(customersAccounts);
                break;
            case 2:
                System.out.println("You have successfully logged out!");
                runApplication(customersAccounts);
                break;
            case 0:
                System.out.print("Bye!");
                System.exit(0);
        }
    }
}
