import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
public class Main {
    public static void main (String[] args){
        Map<String, String> customersAccounts = new HashMap<>();
        runApplication(customersAccounts);


    }

    static void runApplication(Map<String, String> customersAccounts){
        Scanner scanner = new Scanner(System.in);
        helloMessage();
        boolean exit = false;
        while(!exit){
            int chosenNumber = scanner.nextInt();
            switch(chosenNumber){
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

            }
        }
    }

    static void helloMessage(){
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    static void createAccount(Map<String, String> customersAccounts){
        Random random = new Random();
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        String numberMII = "400000";
        String numberAccount = "";
        for (int i = 0 ; i < 9; i++){
            numberAccount += random.nextInt(10);
        }
        String checksum = "";
        checksum += random.nextInt(10);
        String cardNumber = numberMII + numberAccount + checksum;
        String pin = "";
        for (int i = 0 ; i < 4; i++){
            pin += random.nextInt(10);
        }
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(pin);
        customersAccounts.put(cardNumber, pin);
    }

    static void logIn(Map<String, String> customersAccounts){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        String number = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();
        boolean accept = false;
        for(Map.Entry<String,String> accountNumbers : customersAccounts.entrySet()){
            if (accountNumbers.getKey().equals(number) && accountNumbers.getValue().equals(pin)) {
                accept = true;
            }
        }
        if (accept){
            System.out.println("You have successfully logged in!");
            balanceSite(customersAccounts);
        }else{
            System.out.println("Wrong card number or PIN!");
            runApplication(customersAccounts);
        }
    }

    static void balanceSite(Map<String, String> customersAccounts){
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Balance");
        System.out.println("2. Log out");
        System.out.println("0. Exit");
        int chosenNumber = scanner.nextInt();
        switch(chosenNumber){
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
