package com.revature.transaction;

import com.revature.db.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;

public class TransactionDAOImpl implements TransactionDAO {

    private Connection connection = ConnectionManager.getConnection();

    @Override
    public Transaction getTransaction(int id) {
        Transaction transaction = null;
        try {
            String query = "SELECT * FROM \"Transaction\" WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int senderId = rs.getInt("sender_id");
                int receiverId = rs.getInt("receiver_id");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                double amount = rs.getDouble("amount");
                TransactionType type = TransactionType.valueOf(rs.getString("type"));
                String description = rs.getString("description");
                double newBalance = rs.getDouble("new_balance");

                transaction = new Transaction(id, senderId, receiverId, timestamp, amount, type, description, newBalance);
            }

        } catch (Exception e) {
            //TODO set up log4j logging
            e.printStackTrace();
        }

        return transaction;
    }

    @Override
    public ArrayList<Transaction> getAllTransactions(int accountId) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM \"Transaction\" WHERE sender_id = ? OR receiver_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, accountId);
            statement.setInt(2, accountId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int senderId = rs.getInt("sender_id");
                int receiverId = rs.getInt("receiver_id");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                double amount = rs.getDouble("amount");
                TransactionType type = TransactionType.valueOf(rs.getString("type"));
                String description = rs.getString("description");
                double newBalance = rs.getDouble("new_balance");

                transactions.add(new Transaction(id, senderId, receiverId, timestamp, amount, type, description, newBalance));
            }

        } catch (Exception e) {
            //TODO set up log4j logging
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public Transaction createTransaction(int senderId, int receiverId, double amount, TransactionType type, String description, double newBalance) {
        Transaction transaction = null;
        final Timestamp CURRENT_TIME = new Timestamp(System.currentTimeMillis());
        try {
            String sql = "INSERT INTO \"Transaction\" " +
                    "(sender_id, receiver_id, timestamp, amount, type, description) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (senderId == 0) {
                pstmt.setNull(1, Types.INTEGER);
            } else {
                pstmt.setInt(1, senderId);
            }
            if (senderId == 0) {
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(2, receiverId);
            }
            pstmt.setTimestamp(3, CURRENT_TIME);
            pstmt.setDouble(4, amount);
            pstmt.setString(5, String.valueOf(type));
            pstmt.setString(6, description);

            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt("id");
                transaction = new Transaction(id, senderId, receiverId, CURRENT_TIME, amount, type, description, newBalance);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return transaction;
    }

    @Override
    public void updateTransaction(Transaction oldTransaction, Transaction newTransaction) {

    }

    @Override
    public void deleteTransaction(Transaction transaction) {

    }
}
