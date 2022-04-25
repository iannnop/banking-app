package com.revature.transaction;

import java.util.ArrayList;

public interface TransactionDAO {
    Transaction getTransaction(int id);
    ArrayList<Transaction> getAllTransactions(int accountId);
    Transaction createTransaction(int senderId, int receiverId, double amount, TransactionType type, String description, double newBalance);
    void updateTransaction(Transaction oldTransaction, Transaction newTransaction);
    void deleteTransaction(Transaction transaction);
}
