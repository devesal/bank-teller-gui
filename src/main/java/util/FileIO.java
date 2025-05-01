package util;

import model.BankAccount;
import model.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for reading and writing customer and bank account data to files.
 * This class handles serialization and deserialization of {@link Customer} and {@link BankAccount} objects.
 */
public class FileIO {

    private static final String ACCOUNTS_DATA_FILE = "all_accounts.dat";
    private static final String CUSTOMER_DATA_FILE = "all_customers.dat";

    /**
     * Saves a list of bank accounts to the account data file.
     *
     * @param accounts the list of bank accounts to save
     */
    public static void saveAllAccounts(ArrayList<BankAccount> accounts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ACCOUNTS_DATA_FILE))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    /**
     * Loads all bank accounts from the account data file.
     *
     * @return a list of {@link BankAccount} objects, or an empty list if the file does not exist or an error occurs
     */
    public static ArrayList<BankAccount> loadAllAccounts() {
        File file = new File(ACCOUNTS_DATA_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ACCOUNTS_DATA_FILE))) {
            return (ArrayList<BankAccount>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Loads all customers from the customer data file.
     *
     * @return a list of {@link Customer} objects, or an empty list if the file does not exist or an error occurs
     */
    public static ArrayList<Customer> loadAllCustomers() {
        File file = new File(CUSTOMER_DATA_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(CUSTOMER_DATA_FILE))) {
            return (ArrayList<Customer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading customers: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Saves a list of customers to the customer data file.
     *
     * @param customers the list of customers to save
     */
    public static void saveAllCustomers(List<Customer> customers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(CUSTOMER_DATA_FILE))) {
            oos.writeObject(customers);
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    /**
     * Adds a new customer to the stored data and updates the account data accordingly.
     *
     * @param customer the new customer to add
     */
    public static void addCustomer(Customer customer) {
        ArrayList<Customer> all = loadAllCustomers();
        all.add(customer);
        saveAllCustomers(all);

        ArrayList<BankAccount> allAccounts = all.stream()
                .flatMap(c -> c.getAccounts().stream())
                .collect(Collectors.toCollection(ArrayList::new));
        saveAllAccounts(allAccounts);
    }

    /**
     * Removes a customer from the stored data and updates the account data accordingly.
     *
     * @param customer the customer to remove
     */
    public static void removeCustomer(Customer customer) {
        ArrayList<Customer> customers = loadAllCustomers();
        customers.remove(customer);
        saveAllCustomers(customers);

        ArrayList<BankAccount> allAccounts = customers.stream()
                .flatMap(c -> c.getAccounts().stream())
                .collect(Collectors.toCollection(ArrayList::new));
        saveAllAccounts(allAccounts);
    }
}
