package interfaces;

import model.*;

import java.util.ArrayList;

public interface Transferrable {
    void transferMoney(int accountNumber, double amount, ArrayList<BankAccount> bankAccounts);
}
