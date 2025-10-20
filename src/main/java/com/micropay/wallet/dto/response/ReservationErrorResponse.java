package com.micropay.wallet.dto.response;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ReservationErrorResponse extends ErrorResponse {

    private BigDecimal availableBalance;
    private BigDecimal requestedAmount;

    public ReservationErrorResponse(Integer errorCode, String message, LocalDateTime timestamp, BigDecimal availableBalance, BigDecimal requestedAmount) {
        super(errorCode, message, timestamp);
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }

}
