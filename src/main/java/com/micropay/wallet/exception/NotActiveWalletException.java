package com.micropay.wallet.exception;

public class NotActiveWalletException extends RuntimeException {

    public NotActiveWalletException() {
        super("Wallet is not active.");
    }
}
