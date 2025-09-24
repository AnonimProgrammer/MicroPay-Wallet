package com.micropay.wallet.exception;

public class ReservationNotFoundException extends WalletException {

    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
