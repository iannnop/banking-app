package com.revature;

import com.revature.account.Account;
import com.revature.account.AccountDAOImpl;
import com.revature.transaction.TransactionDAOImpl;
import com.revature.user.UserDAOImpl;
import com.revature.user.UserRole;
import com.revature.user.User;

public class App {
    public static void main(String[] args) {
        UserDAOImpl userDAO = new UserDAOImpl();
        TransactionDAOImpl transactionDAO = new TransactionDAOImpl();
        User admin = userDAO.getAdmin("admin");
        System.out.println(admin);
        Account adminAccount = admin.getAccount(0);
        System.out.println(adminAccount);
        adminAccount.deposit(200, "My first paycheck");
        System.out.println(adminAccount);
    }
}
