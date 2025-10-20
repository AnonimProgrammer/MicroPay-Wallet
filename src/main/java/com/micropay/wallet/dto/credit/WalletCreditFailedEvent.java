package com.micropay.wallet.dto.credit;

import java.util.UUID;

public record WalletCreditFailedEvent(
        UUID eventId,
        Long paymentId,
        Long walletId,
        String failureReason
) {}