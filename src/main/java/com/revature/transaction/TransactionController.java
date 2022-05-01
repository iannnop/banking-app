package com.revature.transaction;

import com.revature.account.Account;
import com.revature.account.AccountDAOImpl;
import com.revature.user.UserDAOImpl;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.ArrayList;

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
     * Expects { senderId, receiverId, amount, type, description } in body
     * */
    public Handler createTransaction = ctx -> {
        String id = ctx.pathParam("id");
        Account account = accountDAO.getAccount(Integer.parseInt(id));
        if (account == null) {
            System.out.println("404 Not Found - Account with account_id \""+id+"\" does not exist");
            ctx.status(404);
            return;
        }
        int senderId = Integer.parseInt(ctx.req.getParameter("senderId"));
        int receiverId = Integer.parseInt(ctx.req.getParameter("receiverId"));
        double amount = Double.parseDouble(ctx.req.getParameter("amount"));
        TransactionType type = TransactionType.valueOf(ctx.req.getParameter("type"));
        String description = ctx.req.getParameter("description");

        Transaction transaction = transactionDAO.createTransaction(senderId,receiverId,amount,type,description);

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
        String description = ctx.req.getParameter("description");

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
        ctx.status(200);
    };
}
