package com.revature.account;

import com.revature.jdbc.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;

public class AccountDAOImpl implements AccountDAO {

    @Override
    public Account getAccount(int id) {
        Connection connection = ConnectionManager.getConnection();

        Account account = null;
        try {
            String sql = "SELECT * FROM \"Account\" " +
                    "INNER JOIN \"UserAccount\" ON \"Account\".id = \"UserAccount\".account_id " +
                    "WHERE \"Account\".id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Timestamp accountCreated = rs.getTimestamp("account_created");
                AccountStatus status = AccountStatus.valueOf(rs.getString("status"));
                double balance = rs.getDouble("balance");
                String description = rs.getString("description");

                account = new Account(id, accountCreated, status, balance, description);
            }

        } catch (Exception e) {
            //TODO set up log4j logging
            e.printStackTrace();
        }

        return account;
    }

    @Override
    public ArrayList<Account> getAllAccountsOfType(AccountStatus status) {
        Connection connection = ConnectionManager.getConnection();

        ArrayList<Account> accounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM \"UserAccount\" INNER JOIN \"Account\" " +
                    "ON \"Account\".id = account_id WHERE status = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(status));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                Timestamp accountCreated = rs.getTimestamp("account_created");
                double balance = rs.getDouble("balance");
                String description = rs.getString("description");

                accounts.add(new Account(id, accountCreated, status, balance, description));
            }

        } catch (Exception e) {
            //TODO set up log4j logging
            e.printStackTrace();
        }

        return accounts;
    }

    @Override
    public ArrayList<Account> getUserAccounts(int userId) {
        Connection connection = ConnectionManager.getConnection();

        ArrayList<Account> accounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM \"UserAccount\" INNER JOIN \"Account\" " +
                    "ON \"Account\".id = account_id WHERE user_id = ?" +
                    "ORDER BY account_id";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                Timestamp accountCreated = rs.getTimestamp("account_created");
                AccountStatus status = AccountStatus.valueOf(rs.getString("status"));
                double balance = rs.getDouble("balance");
                String description = rs.getString("description");

                accounts.add(new Account(id, accountCreated, status, balance, description));
            }

        } catch (Exception e) {
            //TODO set up log4j logging
            e.printStackTrace();
        }

        return accounts;
    }

    @Override
    public Account createAccount(int userId) {
        Connection connection = ConnectionManager.getConnection();

        Account account = null;
        final Timestamp CURRENT_TIME = new Timestamp(System.currentTimeMillis());
        try {
            String sql = "INSERT INTO \"Account\" (status, balance) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, String.valueOf(AccountStatus.PENDING_APPROVAL));
            pstmt.setDouble(2, 0);

            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt("id");
                account = new Account(id, CURRENT_TIME, AccountStatus.PENDING_APPROVAL, 0);

                String insertUserAccount = "INSERT INTO \"UserAccount\" (account_created, user_id, account_id) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertUserAccount);
                preparedStatement.setTimestamp(1, CURRENT_TIME);
                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, account.getId());

                preparedStatement.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return account;
    }

    @Override
    public Account createJointAccount(int userId, int otherUserId) {
        Connection connection = ConnectionManager.getConnection();

        Account account = null;
        final Timestamp CURRENT_TIME = new Timestamp(System.currentTimeMillis());
        try {
            String sql = "INSERT INTO \"Account\" (status, balance) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, String.valueOf(AccountStatus.PENDING_APPROVAL));
            pstmt.setDouble(2, 0);

            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt("id");
                account = new Account(id, CURRENT_TIME, AccountStatus.PENDING_APPROVAL, 0);

                String insertUserAccount = "INSERT INTO \"UserAccount\" (account_created, user_id, account_id) " +
                        "VALUES (?, ?, ?), (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertUserAccount);
                preparedStatement.setTimestamp(1, CURRENT_TIME);
                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, account.getId());
                preparedStatement.setTimestamp(4, CURRENT_TIME);
                preparedStatement.setInt(5, otherUserId);
                preparedStatement.setInt(6, account.getId());

                preparedStatement.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return account;
    }

    @Override
    public void updateAccount(Account account) {
        Connection connection = ConnectionManager.getConnection();

        try {
            String sql = "UPDATE \"Account\" SET status = ?, balance = ?, description = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(account.getStatus()));
            pstmt.setDouble(2, account.getBalance());
            pstmt.setString(3, account.getDescription());
            pstmt.setInt(4, account.getId());

            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAccount(Account account) {
        Connection connection = ConnectionManager.getConnection();

        try {
            String sql = "DELETE FROM \"Account\" WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, account.getId());

            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
