public class Account {
    private int id;
    private String number;
    private String pin;
    private int balance;

    Account(String number, String pin, int balance){
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }
    Account(){}

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}