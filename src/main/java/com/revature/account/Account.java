package com.revature.account;

import com.revature.transaction.Transaction;
import com.revature.transaction.TransactionDAOImpl;

import java.util.List;

public class Account {
    private static final TransactionDAOImpl transactionDao = new TransactionDAOImpl();

    private final int id;
    private AccountStatus status;
    private String type;
    private double balance;
    private final List<Transaction> transactions = transactionDao.getAllTransactions(this);

    public Account(int id, AccountStatus status, String type, int balance) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addTransaction(Transaction t) {
        transactionDao.addTransaction(t);
        transactions.add(t);
    }
    public void updateTransaction(Transaction oldTransaction, Transaction newTransaction) {
        transactionDao.updateTransaction(oldTransaction, newTransaction);
        int oldIndex = transactions.indexOf(oldTransaction);
        transactions.set(oldIndex, newTransaction);
    }

    public void removeTransaction(Transaction t) {
        transactionDao.deleteTransaction(t);
        transactions.remove(t);
    }
}
