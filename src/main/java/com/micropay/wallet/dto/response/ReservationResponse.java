package com.micropay.wallet.dto.response;

import com.micropay.wallet.model.ReservationStatus;

import java.math.BigDecimal;

public record ReservationResponse(
        Long walletId,
        Long paymentId,
        BigDecimal reservedAmount,
        BigDecimal availableBalance,
        ReservationStatus status
) {}
