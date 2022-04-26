package com.revature.account;

import java.util.ArrayList;

public interface AccountDAO {
    Account getAccount(int id);
    ArrayList<Account> getUserAccounts(int userId);
    Account createAccount(int userId);
    void updateAccount(Account account);
    void deleteAccount(Account account);
}
