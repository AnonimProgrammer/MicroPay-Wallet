package com.docker.mswallet.exception;

public abstract class WalletException extends RuntimeException {

    WalletException(String message) {
        super(message);
    }

    WalletException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
