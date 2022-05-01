package com.revature.account;

import com.revature.exception.NegativeBalanceException;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountTest {
    @Test
    public void getAccount() {
        Account account = new Account(
                1,
                new Timestamp(System.currentTimeMillis()),
                AccountStatus.PENDING_APPROVAL,
                100.00,
                "My first account"
        );

        AccountDAOImpl accountDAO = mock(AccountDAOImpl.class);
        when(accountDAO.getAccount(1))
                .thenReturn(account);

        Account searchedAccount = accountDAO.getAccount(1);

        assertEquals(account, searchedAccount);
    }

    @Test
    public void getAllAccountsOfType() {
        Account accountOne = new Account(
                1,
                new Timestamp(System.currentTimeMillis()),
                AccountStatus.PENDING_APPROVAL,
                100.00,
                "My first account"
        );
        Account accountTwo = new Account(
                2,
                new Timestamp(System.currentTimeMillis()),
                AccountStatus.PENDING_APPROVAL,
                122.00,
                "My second account"
        );
        Account accountThree = new Account(
                3,
                new Timestamp(System.currentTimeMillis()),
                AccountStatus.PENDING_APPROVAL,
                211.00,
                "My third account"
        );

        ArrayList<Account> pendingAccounts = new ArrayList<>(Arrays.asList(accountOne,accountTwo,accountThree));

        AccountDAOImpl accountDAO = mock(AccountDAOImpl.class);
        when(accountDAO.getAllAccountsOfType(AccountStatus.PENDING_APPROVAL))
                .thenReturn(new ArrayList<>(Arrays.asList(accountOne,accountTwo,accountThree)));

        assertEquals(pendingAccounts, accountDAO.getAllAccountsOfType(AccountStatus.PENDING_APPROVAL));
    }

    @Test
    public void createAccount() {
        Account account = new Account(
                1,
                new Timestamp(System.currentTimeMillis()),
                AccountStatus.PENDING_APPROVAL,
                100.00,
                "My first account"
        );

        AccountDAOImpl accountDAO = mock(AccountDAOImpl.class);
        when(accountDAO.createAccount(1))
                .thenReturn(account);

        Account createdAccount = accountDAO.createAccount(1);

        assertEquals(account, createdAccount);
    }

    @Test
    public void setBalanceThrowException() {
        Account account = new Account(
                1,
                new Timestamp(System.currentTimeMillis()),
                AccountStatus.PENDING_APPROVAL,
                100.00,
                "My first account"
        );

        assertThrows(NegativeBalanceException.class, () -> { account.setBalance(-100.00); });
    }

    @Test
    public void updateAccount() {
        Account account = new Account(
                1,
                new Timestamp(System.currentTimeMillis()),
                AccountStatus.PENDING_APPROVAL,
                100.00,
                "My first account"
        );

        account.setStatus(AccountStatus.ACTIVE);
        assertEquals(AccountStatus.ACTIVE, account.getStatus());

        account.setDescription("New description");
        assertEquals("New description", account.getDescription());
    }

}
