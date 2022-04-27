package com.revature.account;

import com.revature.exception.InvalidInputException;
import com.revature.exception.NegativeBalanceException;
import com.revature.transaction.Transaction;
import com.revature.transaction.TransactionDAOImpl;
import com.revature.transaction.TransactionType;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Account implements Serializable {

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
        TransactionDAOImpl transactionDAO = new TransactionDAOImpl();
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
        TransactionDAOImpl transactionDAO = new TransactionDAOImpl();
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

    public void deposit(double amount, String description) throws InvalidInputException {
        if (amount <= 0) {
            throw new InvalidInputException("Deposit amount cannot be less than or equal to 0");
        }
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        TransactionDAOImpl transactionDAO = new TransactionDAOImpl();

        balance += amount;
        transactions.add(transactionDAO.createTransaction(0, id, amount, TransactionType.DEPOSIT, description));
        accountDAO.updateAccount(this);
    }

    public void withdraw(double amount, String description) throws NegativeBalanceException, InvalidInputException {
        if (amount <= 0) {
            throw new InvalidInputException("Withdraw amount cannot be less than or equal to 0");
        }
        if (balance - amount < 0) {
            throw new NegativeBalanceException("Withdraw amount is greater than available balance");
        }
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        TransactionDAOImpl transactionDAO = new TransactionDAOImpl();

        balance -= amount;
        transactions.add(transactionDAO.createTransaction(id, 0, amount, TransactionType.WITHDRAWAL, description));
        accountDAO.updateAccount(this);
    }

    public void transfer(Account receiver, double amount, String description)
            throws NegativeBalanceException, InvalidInputException {
        if (amount <= 0) {
            throw new InvalidInputException("Transfer amount cannot be less than or equal to 0");
        }
        if (balance - amount < 0) {
            throw new NegativeBalanceException("Transfer amount is greater than available balance");
        }
        TransactionDAOImpl transactionDAO = new TransactionDAOImpl();

        Transaction transaction = transactionDAO.createTransaction(id, receiver.id, amount, TransactionType.TRANSFER, description);
        balance -= amount;
        transactions.add(transaction);

        receiver.setBalance(receiver.getBalance()+amount);
        List<Transaction> receiverTransactions = receiver.getTransactions();
        receiverTransactions.add(transaction);
    }
}
