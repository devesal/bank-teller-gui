package persistence;

import model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private final List<Customer> customers = new ArrayList<>();

    public void add(Customer customer) {
        customers.add(customer);
    }

    public List<Customer> getAllCustomers() {
        return customers;
    }

    public void closeCustomer(Customer customer) {
        customer.setStatus(Customer.CLOSED);
    }

    public void openCustomer(Customer customer) {
        customer.setStatus(Customer.ACTIVE);
    }
}
