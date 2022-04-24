package com.revature.transaction;

import java.sql.Timestamp;

public class Transaction {
    private final int id;
    private final int senderId;
    private final int receiverId;
    private final Timestamp timestamp;
    private final double amount;
    private final TransactionType type;
    private String description;
    public Transaction(int id, int senderId, int receiverId, Timestamp timestamp,
                       double amount, TransactionType type, String description) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
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
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public int getSenderId() {
        return senderId;
    }
    public int getReceiverId() {
        return receiverId;
    }
}
