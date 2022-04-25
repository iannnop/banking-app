package com.revature.account;

import com.revature.user.User;
import com.revature.user.UserRole;

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
            String sql = "SELECT * FROM \"Account\" WHERE id = ?";
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
    public ArrayList<Account> getAllAccounts(int userId) {
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
    public Account createAccount(int userId, double startingBalance) {
        Account account = null;
        final Timestamp CURRENT_TIME = new Timestamp(System.currentTimeMillis());
        try {
            String sql = "INSERT INTO \"Account\" " +
                    "(account_created, status, balance) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, CURRENT_TIME);
            pstmt.setString(2, String.valueOf(AccountStatus.PENDING_APPROVAL));
            pstmt.setDouble(3, startingBalance);

            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt("id");
                account = new Account(id, CURRENT_TIME, AccountStatus.PENDING_APPROVAL, startingBalance);

                String insertUserAccount = "INSERT INTO \"UserAccount\" (user_id, account_id) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertUserAccount);
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, account.getId());

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
            String sql = "DELETE FROM \"UserAccount\" WHERE id = ?;DELETE FROM \"User\" WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, account.getId());

            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
