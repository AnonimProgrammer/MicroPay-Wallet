package com.micropay.wallet.exception;

public class WalletNotFoundException extends WalletException {

    public WalletNotFoundException(String message) {
        super(message);
    }

    public WalletNotFoundException(Long walletId) {
        super("Wallet not found for ID: " + walletId);
    }

    public WalletNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
