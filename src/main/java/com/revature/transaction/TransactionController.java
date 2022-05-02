package com.revature.transaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.revature.account.Account;
import com.revature.account.AccountDAOImpl;
import com.revature.user.UserDAOImpl;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionController {

    UserDAOImpl userDAO;
    AccountDAOImpl accountDAO;
    TransactionDAOImpl transactionDAO;
    public TransactionController(Javalin app) {

        userDAO = new UserDAOImpl();
        accountDAO = new AccountDAOImpl();
        transactionDAO = new TransactionDAOImpl();

        app.get("/accounts/{id}/transactions", getTransactions);
        app.get("/transactions/{id}", getTransaction);
        app.post("/accounts/{id}/transactions", createTransaction);
        app.put("/transactions/{id}", updateTransaction);
        app.delete("/transactions/{id}", deleteTransaction);
    }

    public Handler getTransactions = ctx -> {
        String id = ctx.pathParam("id");
        Account account = accountDAO.getAccount(Integer.parseInt(id));
        if (account == null) {
            System.out.println("404 Not Found - Account with account_id \""+id+"\" does not exist");
            ctx.status(404);
            return;
        }

        ArrayList<Transaction> transactions = transactionDAO.getAllTransactions(account.getId());

        System.out.println("200 OK - Transactions for account with account_id "+id+" found");
        ctx.status(200).json(transactions);
    };

    public Handler getTransaction = ctx -> {
        String id = ctx.pathParam("id");
        Transaction transaction = transactionDAO.getTransaction(Integer.parseInt(id));
        if (transaction == null) {
            System.out.println("404 Not Found - Transaction with transaction_id \""+id+"\" does not exist");
            ctx.status(404);
            return;
        }

        System.out.println("200 OK - Transaction with transaction_id "+id+" found");
        ctx.status(200).json(transaction);
    };

    /*
     * Expects { senderId, receiverId, amount, description } in body
     * */
    public Handler createTransaction = ctx -> {
        String id = ctx.pathParam("id");
        Account account = accountDAO.getAccount(Integer.parseInt(id));
        if (account == null) {
            System.out.println("404 Not Found - Account with account_id \""+id+"\" does not exist");
            ctx.status(404);
            return;
        }
        String jsonString = ctx.body();
        HashMap<String,String> body = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, String>>(){}.getType());
        int senderId = Integer.parseInt(body.get("senderId"));
        int receiverId = Integer.parseInt(body.get("receiverId"));
        double amount = Double.parseDouble(body.get("amount"));
        String description = body.get("description");

        if (senderId != account.getId() && receiverId != account.getId()) {
            System.out.println("403 Unauthorized - Cannot make a transaction involving other accounts");
            ctx.status(403);
            return;
        }

        if (senderId == 0 && receiverId != 0) {
            account.deposit(amount,description);
        } else if (senderId != 0 && receiverId == 0) {
            account.withdraw(amount,description);
        } else if (senderId != 0 && receiverId != 0) {
            account.transfer(accountDAO.getAccount(receiverId),amount,description);
        } else {
            System.out.println("400 Bad Request - Something went wrong with senderId and/or receiverId");
            ctx.status(400);
            return;
        }

        Transaction transaction = account.getTransaction(account.getTransactions().size()-1);

        System.out.println("200 OK - Transaction with transaction_id "+id+" created");
        ctx.status(200).json(transaction);
    };

    /*
     * Expects { description }
     * */
    public Handler updateTransaction = ctx -> {
        String id = ctx.pathParam("id");
        Transaction transaction = transactionDAO.getTransaction(Integer.parseInt(id));
        if (transaction == null) {
            System.out.println("404 Not Found - Transaction with transaction_id \""+id+"\" does not exist");
            ctx.status(404);
            return;
        }
        String jsonString = ctx.body();
        HashMap<String,String> body = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, String>>(){}.getType());
        String description = body.get("description");

        transaction.setDescription(description);

        transactionDAO.updateTransaction(transaction);

        System.out.println("200 OK - Transaction with transaction_id "+id+" updated");
        ctx.status(200).json(transaction);
    };

    public Handler deleteTransaction = ctx -> {
        String id = ctx.pathParam("id");
        Transaction transaction = transactionDAO.getTransaction(Integer.parseInt(id));
        if (transaction == null) {
            System.out.println("404 Not Found - Transaction with transaction_id \""+id+"\" does not exist");
            ctx.status(404);
            return;
        }

        transactionDAO.deleteTransaction(transaction);

        System.out.println("200 OK - Transaction with transaction_id "+id+" deleted");
        ctx.status(200).json(transaction);
    };
}
