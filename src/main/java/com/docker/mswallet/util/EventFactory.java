package com.docker.mswallet.util;

import com.docker.mswallet.dto.credit.*;
import com.docker.mswallet.dto.debit.*;
import com.docker.mswallet.dto.refund.*;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class EventFactory {

    public WalletDebitedEvent createWalletDebitedEvent(Long walletId, BigDecimal amount, UUID transactionId) {
        WalletDebitedEvent event = new WalletDebitedEvent();
        event.setWalletId(walletId);
        event.setAmount(amount);
        event.setTransactionId(transactionId);
        return event;
    }

    public WalletDebitFailedEvent createWalletDebitFailedEvent(Long walletId, UUID transactionId, String failureReason) {
        WalletDebitFailedEvent event = new WalletDebitFailedEvent();
        event.setWalletId(walletId);
        event.setTransactionId(transactionId);
        event.setFailureReason(failureReason);
        return event;
    }

    public WalletCreditedEvent createWalletCreditedEvent(Long walletId, Long userId, BigDecimal amount, UUID transactionId) {
        WalletCreditedEvent event = new WalletCreditedEvent();
        event.setWalletId(walletId);
        event.setUserId(userId);
        event.setAmount(amount);
        event.setTransactionId(transactionId);
        return event;
    }

    public WalletCreditFailedEvent createWalletCreditFailedEvent(Long walletId, UUID transactionId, String failureReason) {
        WalletCreditFailedEvent event = new WalletCreditFailedEvent();
        event.setWalletId(walletId);
        event.setTransactionId(transactionId);
        event.setFailureReason(failureReason);
        return event;
    }

    public WalletRefundedEvent createWalletRefundedEvent(Long walletId, Long userId, BigDecimal amount, UUID transactionId) {
        WalletRefundedEvent event = new WalletRefundedEvent();
        event.setWalletId(walletId);
        event.setUserId(userId);
        event.setAmount(amount);
        event.setTransactionId(transactionId);
        return event;
    }
}

