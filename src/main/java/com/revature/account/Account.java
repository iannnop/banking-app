package com.revature.account;

import com.revature.exception.NegativeBalanceException;
import com.revature.transaction.Transaction;
import com.revature.transaction.TransactionDAOImpl;

import java.sql.Timestamp;
import java.util.List;

public class Account {
    private static final TransactionDAOImpl transactionDAO = new TransactionDAOImpl();

    private final int id;
    private final Timestamp accountCreated;
    private AccountStatus status;
    private double balance;
    private String description;
    private final List<Transaction> transactions;

    /*
    * Account constructor with all required fields
    * */
    public Account(int id, Timestamp accountCreated, AccountStatus status, double balance) {
        this.id = id;
        this.accountCreated = accountCreated;
        this.status = status;
        this.balance = balance;
        transactions = transactionDAO.getAllTransactions(id);
    }

    /*
    * Account constructor with all fields
    * */
    public Account(int id, Timestamp accountCreated, AccountStatus status, double balance, String description) {
        this.id = id;
        this.accountCreated = accountCreated;
        this.status = status;
        this.balance = balance;
        this.description = description;
        transactions = transactionDAO.getAllTransactions(id);
    }

    public int getId() {
        return id;
    }

    public Timestamp getAccountCreated() {
        return accountCreated;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountCreated=" + accountCreated +
                ", status=" + status +
                ", balance=" + balance +
                ", description='" + description + '\'' +
                ", transactions=" + transactions +
                '}';
    }

    public void addTransaction(Transaction t) {
        try {
            transactionDAO.createTransaction(t);
            transactions.add(t);
        } catch (NegativeBalanceException e) {
            e.printStackTrace();
        }
    }
    public void updateTransaction(Transaction oldTransaction, Transaction newTransaction) {
        try {
            transactionDAO.updateTransaction(oldTransaction, newTransaction);
            int oldIndex = transactions.indexOf(oldTransaction);
            transactions.set(oldIndex, newTransaction);
        } catch (NegativeBalanceException e) {
            e.printStackTrace();
        }
    }

    public void removeTransaction(Transaction t) {
        try {
            transactionDAO.deleteTransaction(t);
            transactions.remove(t);
        } catch (NegativeBalanceException e) {
            e.printStackTrace();
        }
    }
}
