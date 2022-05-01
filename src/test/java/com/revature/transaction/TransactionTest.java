package com.revature.transaction;

import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionTest {

    @Test
    public void getTransaction() {
        Transaction transaction = new Transaction(
                1,
                0,
                1,
                new Timestamp(System.currentTimeMillis()),
                99.99,
                TransactionType.DEPOSIT,
                "My first transaction"
        );

        TransactionDAOImpl transactionDAO = mock(TransactionDAOImpl.class);
        when(transactionDAO.getTransaction(1))
            .thenReturn(transaction);

        Transaction searchedTransaction = transactionDAO.getTransaction(1);

        assertEquals(transaction, searchedTransaction);

    }

    @Test
    public void getAllTransactions() {
        Transaction transactionOne = new Transaction(
                1,
                0,
                1,
                new Timestamp(System.currentTimeMillis()),
                99.99,
                TransactionType.DEPOSIT,
                "My first deposit"
        );
        Transaction transactionTwo = new Transaction(
                2,
                1,
                0,
                new Timestamp(System.currentTimeMillis()),
                0.99,
                TransactionType.WITHDRAWAL,
                "My first withdrawal"
        );
        Transaction transactionThree = new Transaction(
                3,
                1,
                2,
                new Timestamp(System.currentTimeMillis()),
                9.00,
                TransactionType.TRANSFER,
                "My first transfer"
        );

        ArrayList<Transaction> databaseTransactions = new ArrayList<>(Arrays.asList(transactionOne,transactionTwo,transactionThree));

        TransactionDAOImpl transactionDAO = mock(TransactionDAOImpl.class);
        when(transactionDAO.getAllTransactions(1))
            .thenReturn(new ArrayList<>(Arrays.asList(transactionOne,transactionTwo,transactionThree)));

        assertEquals(databaseTransactions, transactionDAO.getAllTransactions(1));
    }

    @Test
    public void createTransaction() {
        Transaction transaction = new Transaction(
                1,
                0,
                1,
                new Timestamp(System.currentTimeMillis()),
                99.99,
                TransactionType.DEPOSIT,
                "My first transaction"
        );

        TransactionDAOImpl transactionDAO = mock(TransactionDAOImpl.class);
        when(transactionDAO.createTransaction(0,1,99.99,TransactionType.DEPOSIT,"My first transaction"))
                .thenReturn(transaction);

        Transaction createdTransaction = transactionDAO.createTransaction(0,1,99.99,TransactionType.DEPOSIT,"My first transaction");

        assertEquals(transaction, createdTransaction);
    }

    @Test
    public void updateTransaction() {
        Transaction transaction = new Transaction(
                1,
                0,
                1,
                new Timestamp(System.currentTimeMillis()),
                99.99,
                TransactionType.DEPOSIT,
                "My first transaction"
        );
        transaction.setDescription("My first deposit!");

        assertEquals("My first deposit!", transaction.getDescription());
    }
}
