package com.docker.mswallet.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends WalletException {

    private BigDecimal availableBalance;
    private BigDecimal requestedAmount;

    public InsufficientBalanceException(String message, BigDecimal availableBalance, BigDecimal requestedAmount) {
        super(message);
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }
    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
