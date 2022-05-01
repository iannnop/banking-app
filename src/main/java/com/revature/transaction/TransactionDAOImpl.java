package com.revature.transaction;

import com.revature.jdbc.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public Transaction getTransaction(int id) {
        Connection connection = ConnectionManager.getConnection();

        Transaction transaction = null;
        try {
            String query = "SELECT * FROM \"Transaction\" " +
                    "INNER JOIN \"AccountTransaction\" ON \"Transaction\".id = \"AccountTransaction\".transaction_id " +
                    "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int senderId = rs.getInt("sender_id");
                int receiverId = rs.getInt("receiver_id");
                Timestamp transactionCreated = rs.getTimestamp("transaction_created");
                double amount = rs.getDouble("amount");
                TransactionType type = TransactionType.valueOf(rs.getString("type"));
                String description = rs.getString("description");

                transaction = new Transaction(id, senderId, receiverId, transactionCreated, amount, type, description);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return transaction;
    }

    @Override
    public ArrayList<Transaction> getAllTransactions(int accountId) {
        Connection connection = ConnectionManager.getConnection();

        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM \"Transaction\" " +
                    "INNER JOIN \"AccountTransaction\" ON \"Transaction\".id = \"AccountTransaction\".transaction_id " +
                    "WHERE sender_id = ? OR receiver_id = ?" +
                    "ORDER BY transaction_id";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, accountId);
            statement.setInt(2, accountId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("transaction_id");
                int senderId = rs.getInt("sender_id");
                int receiverId = rs.getInt("receiver_id");
                Timestamp transactionCreated = rs.getTimestamp("transaction_created");
                double amount = rs.getDouble("amount");
                TransactionType type = TransactionType.valueOf(rs.getString("type"));
                String description = rs.getString("description");

                transactions.add(new Transaction(id, senderId, receiverId, transactionCreated, amount, type, description));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public Transaction createTransaction(int senderId, int receiverId, double amount, TransactionType type, String description) {
        Connection connection = ConnectionManager.getConnection();

        Transaction transaction = null;
        final Timestamp CURRENT_TIME = new Timestamp(System.currentTimeMillis());
        try {
            String sql = "INSERT INTO \"Transaction\" (amount, type, description) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setDouble(1, amount);
            pstmt.setString(2, String.valueOf(type));
            pstmt.setString(3, description);

            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt("id");
                transaction = new Transaction(id, senderId, receiverId, CURRENT_TIME, amount, type, description);

                String insertAccountTransaction = "INSERT INTO \"AccountTransaction\" " +
                        "(transaction_id, transaction_created, sender_id, receiver_id) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertAccountTransaction);
                preparedStatement.setInt(1, id);
                preparedStatement.setTimestamp(2, CURRENT_TIME);
                if (senderId == 0) {
                    preparedStatement.setNull(3, Types.INTEGER);
                } else {
                    preparedStatement.setInt(3, senderId);
                }
                if (receiverId == 0) {
                    preparedStatement.setNull(4, Types.INTEGER);
                } else {
                    preparedStatement.setInt(4, receiverId);
                }

                preparedStatement.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return transaction;
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        Connection connection = ConnectionManager.getConnection();

        try {
            String sql = "UPDATE \"Transaction\" amount = ?, type = ?, description = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, transaction.getAmount());
            pstmt.setString(2, String.valueOf(transaction.getType()));
            pstmt.setString(3, transaction.getDescription());

            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        Connection connection = ConnectionManager.getConnection();

        try {
            String sql = "DELETE FROM \"Transaction\" WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, transaction.getId());

            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
