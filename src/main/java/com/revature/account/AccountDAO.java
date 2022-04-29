package com.revature.account;

import java.util.ArrayList;

public interface AccountDAO {
    Account getAccount(int id);
    ArrayList<Account> getAllAccountsOfType(AccountStatus status);
    ArrayList<Account> getUserAccounts(int userId);
    Account createAccount(int userId);
    Account createJointAccount(int userId, int otherUserId);
    void updateAccount(Account account);
    void deleteAccount(Account account);
}
