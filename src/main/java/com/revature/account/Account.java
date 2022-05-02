package com.revature.account;

import com.revature.exception.AccountNotActiveException;
import com.revature.exception.InvalidAmountException;
import com.revature.exception.NegativeBalanceException;
import com.revature.transaction.Transaction;
import com.revature.transaction.TransactionDAOImpl;
import com.revature.transaction.TransactionType;
import com.revature.user.User;
import com.revature.user.UserDAOImpl;

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

    public void setBalance(double balance) throws NegativeBalanceException {
        if (balance < 0) {
            throw new NegativeBalanceException("Balance cannot be less than 0");
        }
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
                ", transactions=" + transactions.size() +
                '}';
    }

    public void deposit(double amount, String description) throws InvalidAmountException, AccountNotActiveException {
        if (transactions.size() >= 1 && status != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException("ERROR: Cannot deposit to an account that is "+status);
        }
        if (amount <= 0) {
            throw new InvalidAmountException("ERROR: Deposit amount cannot be less than or equal to 0");
        }
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        TransactionDAOImpl transactionDAO = new TransactionDAOImpl();

        balance += amount;
        transactions.add(transactionDAO.createTransaction(0, id, amount, TransactionType.DEPOSIT, description));
        accountDAO.updateAccount(this);
    }

    public void withdraw(double amount, String description)
            throws NegativeBalanceException, InvalidAmountException, AccountNotActiveException {
        if (status != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException("ERROR: Cannot withdraw from an account that is "+status);
        }
        if (amount <= 0) {
            throw new InvalidAmountException("ERROR: Withdraw amount cannot be less than or equal to 0");
        }
        if (balance - amount < 0) {
            throw new NegativeBalanceException("ERROR: Withdraw amount is greater than available balance");
        }
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        TransactionDAOImpl transactionDAO = new TransactionDAOImpl();

        balance -= amount;
        transactions.add(transactionDAO.createTransaction(id, 0, amount, TransactionType.WITHDRAWAL, description));
        accountDAO.updateAccount(this);
    }

    public void transfer(Account receiver, double amount, String description)
            throws NegativeBalanceException, InvalidAmountException, AccountNotActiveException {
        if (status != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException("ERROR: Cannot transfer from an account that is "+status);
        }
        if (receiver.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException("ERROR: Cannot transfer to an account that is "+receiver.getStatus());
        }
        if (amount <= 0) {
            throw new InvalidAmountException("ERROR: Transfer amount cannot be less than or equal to 0");
        }
        if (balance - amount < 0) {
            throw new NegativeBalanceException("ERROR: Transfer amount is greater than available balance");
        }

        AccountDAOImpl accountDAO = new AccountDAOImpl();
        TransactionDAOImpl transactionDAO = new TransactionDAOImpl();

        Transaction transaction = transactionDAO.createTransaction(id, receiver.id, amount, TransactionType.TRANSFER, description);
        balance -= amount;
        transactions.add(transaction);

        receiver.setBalance(receiver.getBalance()+amount);
        List<Transaction> receiverTransactions = receiver.getTransactions();
        receiverTransactions.add(transaction);

        accountDAO.updateAccount(this);
        accountDAO.updateAccount(receiver);
    }

    public void printTransactions() {
        UserDAOImpl userDAO = new UserDAOImpl();

        System.out.printf("LIST OF ALL TRANSACTIONS FOR ACCOUNT WITH account_id: %d%n", id);
        System.out.printf("======================================%n");
        if (transactions.size() == 0) {
            System.out.println("\nNO TRANSACTIONS FOUND");
        }
        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            User sender = userDAO.getUserFromAccount(transaction.getSenderId());
            User receiver = userDAO.getUserFromAccount(transaction.getReceiverId());
            System.out.println("\nTRANSACTION " + i);
            System.out.println("DESCRIPTION: " + transaction.getDescription()+"\n");
            System.out.println("transaction_number: " + i);
            System.out.println("transaction_id: " + transaction.getId());
            System.out.println("transaction_created: " + transaction.getTransactionCreated());
            System.out.println("sender_id: " + transaction.getSenderId());
            if (sender == null) {
                System.out.println("Personal deposit!");
            } else {
                System.out.println("sender_username: " + sender.getUsername());
            }
            System.out.println("receiver_id: " + transaction.getReceiverId());
            if (receiver == null) {
                System.out.println("Personal withdrawal!");
            } else {
                System.out.println("receiver_username: " + receiver.getUsername());
            }
            System.out.println("transaction_amount: " + transaction.getAmount());
            System.out.println("transaction_type: " + transaction.getType());
            System.out.printf("%n======================================%n");
        }
        System.out.println("Total number of transactions: " + transactions.size());
        System.out.printf("END OF TRANSACTIONS REPORT FOR ACCOUNT WITH account_id: %d%n", id);
    }
}
