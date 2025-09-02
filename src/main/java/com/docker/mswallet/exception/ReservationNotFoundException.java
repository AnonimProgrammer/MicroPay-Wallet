package com.docker.mswallet.exception;

public class ReservationNotFoundException extends WalletException {

    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
