package com.revature.account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class AccountDAOImpl implements AccountDAO {

    @Override
    public Account getAccount(int id) {
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
            //TODO Set up message for account not found

        } catch (Exception e) {
            //TODO set up log4j logging
            e.printStackTrace();
        }

        return account;
    }

    @Override
    public ArrayList<Account> getUserAccounts(int userId) {
        ArrayList<Account> accounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM \"UserAccount\" INNER JOIN \"Account\" " +
                    "ON \"Account\".id = account_id WHERE user_id = ?";
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
    public void updateAccount(Account account) {
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
    public void deleteAccount(Account account) { //TODO Fix error with this, deleting is complex
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
