package com.docker.mswallet.dto.refund;

import java.math.BigDecimal;
import java.util.UUID;

public class RefundWalletEvent {

    private UUID transactionId;
    private Long walletId;
    private BigDecimal amount;

    public RefundWalletEvent() {}
    public RefundWalletEvent(UUID transactionId, Long walletId, BigDecimal amount) {
        this.transactionId = transactionId;
        this.walletId = walletId;
        this.amount = amount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
    public Long getWalletId() {
        return walletId;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }
    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "RefundWalletEvent {" +
                "transactionId = " + transactionId +
                ", walletId = " + walletId +
                ", amount = " + amount +
                '}';
    }
}
