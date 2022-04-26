package com.revature.account;

import com.revature.exception.NegativeBalanceException;
import com.revature.transaction.Transaction;
import com.revature.transaction.TransactionDAOImpl;
import com.revature.transaction.TransactionType;

import java.sql.Timestamp;
import java.util.List;

public class Account {
    private static final AccountDAOImpl accountDAO = new AccountDAOImpl();
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

    public List<Transaction> getTransactions() {
        return transactions;
    }
    public Transaction getTransaction(int index) {
        return transactions.get(index);
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

    public void deposit(double amount, String description) {
        balance += amount;
        transactions.add(transactionDAO.createTransaction(0, id, amount, TransactionType.DEPOSIT, description));
        accountDAO.updateAccount(this);
    }

    public void withdraw(double amount, String description) throws NegativeBalanceException {
        if (balance - amount < 0) {
            throw new NegativeBalanceException("ERROR"); //TODO Add error message
        }
        balance -= amount;
        transactions.add(transactionDAO.createTransaction(id, 0, amount, TransactionType.WITHDRAWAL, description));
        accountDAO.updateAccount(this);
    }

    public void transfer(Account receiver, double amount, String description) throws NegativeBalanceException {
        if (balance - amount < 0) {
            throw new NegativeBalanceException("ERROR MESSAGE"); //TODO Add error message
        }
        Transaction transaction = transactionDAO.createTransaction(id, receiver.id, amount, TransactionType.TRANSFER, description);
        balance -= amount;
        transactions.add(transaction);

        receiver.setBalance(receiver.getBalance()+amount);
        List<Transaction> receiverTransactions = receiver.getTransactions();
        receiverTransactions.add(transaction);
    }
}
