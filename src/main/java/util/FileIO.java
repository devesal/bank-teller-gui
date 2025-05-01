package util;
import model.BankAccount;
import model.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileIO {

    private static final String ACCOUNTS_DATA_FILE = "all_accounts.dat";
    private static final String CUSTOMER_DATA_FILE = "all_customers.dat";

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

    public static ArrayList<Customer> loadAllCustomers() {
        File file = new File(CUSTOMER_DATA_FILE);
        if (!file.exists()) {
            return new ArrayList<>(); // Return empty list if file doesn't exist
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(CUSTOMER_DATA_FILE))) {
            return (ArrayList<Customer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading customers: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveAllCustomers(List<Customer> customers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(CUSTOMER_DATA_FILE))) {
            oos.writeObject(customers);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    public static void addCustomer(Customer customer) {
        ArrayList<Customer> all = loadAllCustomers();
        all.add(customer);
        saveAllCustomers(all);

        // Also update the accounts file with the new accounts from this customer
        ArrayList<BankAccount> allAccounts = all.stream()
                .flatMap(c -> c.getAccounts().stream())
                .collect(Collectors.toCollection(ArrayList::new));
        saveAllAccounts(allAccounts);
    }
    public static void removeCustomer(Customer customer){
        ArrayList<Customer> customers = loadAllCustomers();
        customers.remove(customer);
        saveAllCustomers(customers);

        ArrayList<BankAccount> allAccounts = customers.stream()
                .flatMap(c -> c.getAccounts().stream())
                .collect(Collectors.toCollection(ArrayList::new));
        saveAllAccounts(allAccounts);

    }
}
