package com.revature.transaction;

import java.io.Serializable;
import java.sql.Timestamp;

public class Transaction implements Serializable {

    private final int id;
    private final int senderId;
    private final int receiverId;
    private final Timestamp transactionCreated;
    private final double amount;
    private final TransactionType type;
    private String description;
    public Transaction(int id, int senderId, int receiverId, Timestamp transactionCreated,
                       double amount, TransactionType type, String description) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.transactionCreated = transactionCreated;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }
    public int getId() {
        return id;
    }
    public double getAmount() {
        return amount;
    }
    public TransactionType getType() {
        return type;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Timestamp getTransactionCreated() {
        return transactionCreated;
    }
    public int getSenderId() {
        return senderId;
    }
    public int getReceiverId() {
        return receiverId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", timestamp=" + transactionCreated +
                ", amount=" + amount +
                ", type=" + type +
                ", description='" + description + '\'' +
                '}';
    }
}
