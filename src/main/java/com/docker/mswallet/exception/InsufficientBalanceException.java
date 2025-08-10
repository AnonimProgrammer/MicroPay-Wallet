package com.docker.mswallet.exception;

public class InsufficientBalanceException extends WalletException {

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
