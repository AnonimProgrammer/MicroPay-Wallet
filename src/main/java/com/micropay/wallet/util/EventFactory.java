package com.micropay.wallet.util;

import com.micropay.wallet.dto.credit.WalletCreditFailedEvent;
import com.micropay.wallet.dto.credit.WalletCreditedEvent;
import com.micropay.wallet.dto.debit.WalletDebitFailedEvent;
import com.micropay.wallet.dto.debit.WalletDebitedEvent;
import com.micropay.wallet.dto.refund.WalletRefundedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EventFactory {

    public WalletDebitedEvent createWalletDebitedEvent(
            Long walletId, Long userId, BigDecimal amount, Long paymentId
    ) {
        return new WalletDebitedEvent(paymentId, walletId, userId, amount);
    }

    public WalletDebitFailedEvent createWalletDebitFailedEvent(
            Long walletId, Long userId, Long paymentId, String failureReason
    ) {
        return new WalletDebitFailedEvent(paymentId, walletId, userId, failureReason);
    }

    public WalletCreditedEvent createWalletCreditedEvent(
            Long walletId, Long userId, BigDecimal amount, Long paymentId
    ) {
        return new WalletCreditedEvent(paymentId, walletId, userId, amount);
    }

    public WalletCreditFailedEvent createWalletCreditFailedEvent(
            Long walletId, Long paymentId, String failureReason
    ) {
        return new WalletCreditFailedEvent(paymentId, walletId, failureReason);
    }

    public WalletRefundedEvent createWalletRefundedEvent(
            Long walletId, Long userId, BigDecimal amount, Long paymentId
    ) {
        return new WalletRefundedEvent(paymentId, walletId, userId, amount);
    }
}

