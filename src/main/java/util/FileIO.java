package util;

import model.Customer;
import model.BankAccount;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIO {
    private static final String CUSTOMERS_FILE = "customers.dat";
    private static final String ACCOUNTS_FILE = "accounts.dat";

    // --- SAVE ALL CUSTOMERS ---
    public static void saveAllCustomers(List<Customer> customers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CUSTOMERS_FILE))) {
            oos.writeObject(customers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- LOAD ALL CUSTOMERS ---
    @SuppressWarnings("unchecked")
    public static List<Customer> loadAllCustomers() {
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Customer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // --- SAVE ALL ACCOUNTS ---
    public static void saveAllAccounts(List<BankAccount> accounts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ACCOUNTS_FILE))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- LOAD ALL ACCOUNTS ---
    @SuppressWarnings("unchecked")
    public static List<BankAccount> loadAllAccounts() {
        File file = new File(ACCOUNTS_FILE);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<BankAccount>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
