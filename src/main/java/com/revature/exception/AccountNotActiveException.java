package com.revature.exception;

public class AccountNotActiveException extends Exception {
    public AccountNotActiveException(String message) {
        super(message);
    }
}
