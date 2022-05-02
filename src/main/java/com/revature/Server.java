package com.revature;

import com.revature.account.AccountController;
import com.revature.transaction.TransactionController;
import com.revature.user.UserController;
import io.javalin.Javalin;

public class Server {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);

        UserController userController = new UserController(app);
        AccountController accountController = new AccountController(app);
        TransactionController transactionController = new TransactionController(app);
    }
}
