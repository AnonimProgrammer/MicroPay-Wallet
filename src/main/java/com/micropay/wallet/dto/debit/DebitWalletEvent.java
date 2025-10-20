package com.micropay.wallet.dto.debit;

import java.math.BigDecimal;
import java.util.UUID;

public record DebitWalletEvent (
        UUID eventId,
        Long paymentId,
        Long walletId,
        BigDecimal amount
) {}
