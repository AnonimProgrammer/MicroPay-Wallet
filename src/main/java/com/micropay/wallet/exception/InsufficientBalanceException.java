package com.micropay.wallet.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class InsufficientBalanceException extends WalletException {

    private BigDecimal availableBalance;
    private BigDecimal requestedAmount;

    public InsufficientBalanceException(String message, BigDecimal availableBalance, BigDecimal requestedAmount) {
        super(message);
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }

}
