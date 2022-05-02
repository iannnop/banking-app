package com.revature.account;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.revature.user.User;
import com.revature.user.UserDAOImpl;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.HashMap;

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
            System.out.println("404 Not Found - Account with account_id \""+id+"\" does not exist");
            ctx.status(404);
            return;
        }
        System.out.println("200 OK - Account with account_id "+id+" found");
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

        String jsonString = ctx.body();
        HashMap<String,String> body = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, String>>(){}.getType());
        double startingBalance = Double.parseDouble(body.get("startingBalance"));
        user.applyForAccount(startingBalance);
        Account account = user.getAccount(user.getAccounts().size()-1);

        System.out.println("200 OK - Account with account_id "+account.getId()+" created");
        ctx.status(201).json(account);
    };

    /*
    * Expects { status, balance, description } in body
    * */
    public Handler updateAccount = ctx -> {
        String id = ctx.pathParam("id");
        Account account = accountDAO.getAccount(Integer.parseInt(id));
        if (account == null) {
            System.out.println("404 Not Found - Account with id \""+id+"\" does not exist");
            ctx.status(404);
            return;
        }
        String jsonString = ctx.body();
        HashMap<String,String> body = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, String>>(){}.getType());

        AccountStatus status = AccountStatus.valueOf(body.get("status"));
        double balance = Double.parseDouble(body.get("balance"));
        String description = body.get("description");

        account.setStatus(status);
        account.setBalance(balance);
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
        ctx.status(200).json(account);
    };
}
