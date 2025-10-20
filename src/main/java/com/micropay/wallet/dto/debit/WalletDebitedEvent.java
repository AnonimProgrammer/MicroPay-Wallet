package com.micropay.wallet.dto.debit;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDebitedEvent (
        UUID eventId,
        Long paymentId,
        Long walletId,
        BigDecimal amount
) {}
