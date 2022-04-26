package com.revature.account;

import com.revature.db.ConnectionManager;

import java.sql.Connection;
import java.util.ArrayList;

public interface AccountDAO {
    Connection connection = ConnectionManager.getConnection();

    Account getAccount(int id);
    ArrayList<Account> getUserAccounts(int userId);
    Account createAccount(int userId);
    void updateAccount(Account account);
    void deleteAccount(Account account);
}
