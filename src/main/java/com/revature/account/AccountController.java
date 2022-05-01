package com.revature.account;

import com.revature.user.User;
import com.revature.user.UserDAOImpl;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class AccountController {
    UserDAOImpl userDAO;
    AccountDAOImpl accountDAO;

    public AccountController(Javalin app) {

        userDAO = new UserDAOImpl();
        accountDAO = new AccountDAOImpl();

        app.get("/users/{username}/accounts", getAccounts);
        app.get("/accounts/{id}", getAccount);
        app.post("/users/{username}/accounts", createAccount);
        app.put("/accounts/{id}", updateAccount);
        app.delete("/accounts/{id}", deleteAccount);
    }

    public Handler getAccounts = ctx -> {
        String username = ctx.pathParam("username");
        User user = userDAO.getUser(username);
        if (user == null) {
            System.out.println("404 Not Found - User \""+username+"\" does not exist");
            ctx.status(404);
            return;
        }
        ArrayList<Account> accounts = accountDAO.getUserAccounts(user.getId());

        System.out.println("200 OK - Accounts for "+username+" found");
        ctx.status(200).json(accounts);
    };

    public Handler getAccount = ctx -> {
        String id = ctx.pathParam("id");
        Account account = accountDAO.getAccount(Integer.parseInt(id));
        if (account == null) {
            System.out.println("404 Not Found - Account with id \""+id+"\" does not exist");
            ctx.status(404);
            return;
        }
        System.out.println("200 OK - Account with id "+id+" found");
        ctx.status(200).json(account);
    };

    /*
     * Expects { startingBalance } in body
     * */
    public Handler createAccount = ctx -> {
        if (ctx.req.getParameter("startingBalance") == null) {
            System.out.println("400 Bad Request - Expected startingBalance in body");
            ctx.status(400);
        }

        String username = ctx.pathParam("username");
        User user = userDAO.getUser(username);
        if (user == null) {
            System.out.println("404 Not Found - User \""+username+"\" does not exist");
            ctx.status(404);
            return;
        }

        double startingBalance = Double.parseDouble(ctx.req.getParameter("startingBalance"));
        user.applyForAccount(startingBalance);

        System.out.println("200 OK - Account created");
        ctx.status(201).json(user);
    };

    /*
    * Expects { status, balance, description }
    * */
    public Handler updateAccount = ctx -> {
        String id = ctx.pathParam("id");
        Account account = accountDAO.getAccount(Integer.parseInt(id));
        if (account == null) {
            System.out.println("404 Not Found - Account with id \""+id+"\" does not exist");
            ctx.status(404);
            return;
        }
        String status = ctx.req.getParameter("status");
        String balance = ctx.req.getParameter("balance");
        String description = ctx.req.getParameter("description");

        account.setStatus(AccountStatus.valueOf(status));
        account.setBalance(Double.parseDouble(balance));
        account.setDescription(description);

        accountDAO.updateAccount(account);

        System.out.println("200 OK - Account with id "+id+" updated");
        ctx.status(200).json(account);
    };

    public Handler deleteAccount = ctx -> {
        String id = ctx.pathParam("id");
        Account account = accountDAO.getAccount(Integer.parseInt(id));
        if (account == null) {
            System.out.println("404 Not Found - Account with id \""+id+"\" does not exist");
            ctx.status(404);
            return;
        }

        accountDAO.deleteAccount(account);

        System.out.println("200 OK - Account with id "+id+" deleted");
        ctx.status(200);
    };
}
