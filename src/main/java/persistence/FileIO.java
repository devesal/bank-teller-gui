package persistence;
import model.BankAccount;

import java.io.*;
import java.util.ArrayList;
public class FileIO {

    private static final String ACCOUNTS_DATA_FILE = "all_accounts.dat";


    public static void saveAllAccounts(ArrayList<BankAccount> accounts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ACCOUNTS_DATA_FILE))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }


    public static ArrayList<BankAccount> loadAllAccounts() {
        File file = new File(ACCOUNTS_DATA_FILE);
        if (!file.exists()) {
            return new ArrayList<>(); // Return empty list if file doesn't exist
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ACCOUNTS_DATA_FILE))) {
            return (ArrayList<BankAccount>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
