package com.revature.account;

import com.revature.db.ConnectionManager;

import java.sql.Connection;
import java.util.ArrayList;

public interface AccountDAO {
    Connection connection = ConnectionManager.connect();

    Account getAccount(int id);
    ArrayList<Account> getAllAccounts();
    void createAccount(Account account);
    void updateAccount(Account account);
    void deleteAccount(Account account);
}
