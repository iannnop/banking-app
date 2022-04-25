package com.revature.account;

import com.revature.db.ConnectionManager;

import java.sql.Connection;
import java.util.ArrayList;

public interface AccountDAO {
    Connection connection = ConnectionManager.getConnection();

    Account getAccount(int id);
    ArrayList<Account> getAllAccounts(int userId);
    Account createAccount(int userId, double startingBalance);
    void updateAccount(Account account);
    void deleteAccount(Account account);
}
