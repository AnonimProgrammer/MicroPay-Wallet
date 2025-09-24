package com.micropay.wallet.exception;

public class WalletNotFoundException extends WalletException {

    public WalletNotFoundException(String message) {
        super(message);
    }

    public WalletNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
