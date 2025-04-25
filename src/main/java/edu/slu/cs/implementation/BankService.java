package edu.slu.cs.implementation;

import edu.slu.cs.model.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BankService {

    private final Map<Integer, BankAccount> accounts = new HashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();

    public BankService(Collection<BankAccount> initialAccounts) {
        initialAccounts.forEach(acct -> accounts.put(acct.getAccountNo(), acct));
    }

    /**
     * Inquire the balance of an active account.
     */
    public Optional<Double> inquireBalance(int accountNo) {
        BankAccount acct = accounts.get(accountNo);
        if (acct != null && "Active".equals(acct.getStatus())) {
            return Optional.of(acct.inquireBalance());
        }
        return Optional.empty();
    }

    public boolean deposit(int accountNo, double amount) {
        BankAccount acct = accounts.get(accountNo);
        if (acct != null && "Active".equals(acct.getStatus())) {
            acct.deposit(amount);
            record(accountNo, accountNo, amount, Transaction.Type.DEPOSIT);
            return true;
        }
        return false;
    }

    public boolean withdraw(int accountNo, double amount) {
        BankAccount acct = accounts.get(accountNo);
        if (acct != null && "Active".equals(acct.getStatus())) {
            acct.withdraw(amount);
            record(accountNo, accountNo, amount, Transaction.Type.WITHDRAWAL);
            return true;
        }
        return false;
    }

    public boolean transfer(int fromAcc, int toAcc, double amount) {
        BankAccount src = accounts.get(fromAcc);
        BankAccount dst = accounts.get(toAcc);
        if (src != null && dst != null
                && "Active".equals(src.getStatus())
                && "Active".equals(dst.getStatus())) {
            src.transferMoney(toAcc, amount, new ArrayList<>(accounts.values()));
            record(fromAcc, toAcc, amount, Transaction.Type.TRANSFER);
            return true;
        }
        return false;
    }

    public boolean closeAccount(int accountNo) {
        BankAccount acct = accounts.get(accountNo);
        if (acct != null) {
            acct.closeAccount();
            record(accountNo, accountNo, acct.inquireBalance(), Transaction.Type.WITHDRAWAL);
            return true;
        }
        return false;
    }

    public boolean reopenAccount(int accountNo) {
        BankAccount acct = accounts.get(accountNo);
        if (acct != null && "Closed".equals(acct.getStatus())) {
            acct.reopenAccount();
            return true;
        }
        return false;
    }

    /**
     * Search accounts by various criteria: account number, name, type, or transaction history.
     */
    public List<BankAccount> searchAccounts(String term, SearchCriteria criteria) {
        String lc = term.toLowerCase();
        switch (criteria) {
            case ACCOUNT_NUMBER:
                try {
                    int no = Integer.parseInt(term);
                    return accounts.containsKey(no)
                            ? Collections.singletonList(accounts.get(no))
                            : Collections.emptyList();
                } catch (NumberFormatException e) {
                    return Collections.emptyList();
                }
            case NAME:
                return accounts.values().stream()
                        .filter(a -> (a.getFirstName() + " " + a.getLastName()).toLowerCase().contains(lc))
                        .collect(Collectors.toList());
            case TYPE:
                return accounts.values().stream()
                        .filter(a -> a.displayAccountType().toLowerCase().contains(lc))
                        .collect(Collectors.toList());
            case TRANSACTION_HISTORY:
                // Return accounts involved in transactions matching the term
                return transactions.stream()
                        .filter(tx -> tx.toString().toLowerCase().contains(lc))
                        .flatMap(tx -> Stream.of(accounts.get(tx.getFromAccount()), accounts.get(tx.getToAccount())))
                        .distinct()
                        .collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }

    /**
     * Modify account details. Cannot change account number.
     */
    public boolean modifyAccount(int accountNo, String newFirstName, String newLastName, String newStatus) {
        BankAccount acct = accounts.get(accountNo);
        if (acct != null) {
            acct.setFirstName(newFirstName);
            acct.setLastName(newLastName);
            acct.setStatus(newStatus);
            return true;
        }
        return false;
    }


    // Reporting utilities using Streams
    public List<BankAccount> getActiveAccounts() {
        return accounts.values().stream()
                .filter(a -> "Active".equals(a.getStatus()))
                .collect(Collectors.toList());
    }

    public double getTotalAssets() {
        return accounts.values().stream()
                .mapToDouble(BankAccount::inquireBalance)
                .sum();
    }

    public Optional<BankAccount> getRichestAccount() {
        return accounts.values().stream()
                .max(Comparator.comparingDouble(BankAccount::inquireBalance));
    }

    public Map<Transaction.Type, List<Transaction>> getTransactionsByType() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getType));
    }

    public List<Transaction> getTransactionsForAccount(int accountNo) {
        return transactions.stream()
                .filter(t -> t.getFromAccount() == accountNo || t.getToAccount() == accountNo)
                .collect(Collectors.toList());
    }

    public double getAverageBalance() {
        return accounts.values().stream()
                .mapToDouble(BankAccount::inquireBalance)
                .average()
                .orElse(0.0);
    }

    private void record(int from, int to, double amt, Transaction.Type type) {
        transactions.add(new Transaction(from, to, amt, type));
    }

    public enum SearchCriteria { ACCOUNT_NUMBER, NAME, TYPE, TRANSACTION_HISTORY }
}

