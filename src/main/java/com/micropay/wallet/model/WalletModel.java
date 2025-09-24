package com.micropay.wallet.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletModel {

    private Long id;
    private Long userId;
    private BigDecimal totalBalance;
    private BigDecimal availableBalance;
    private BigDecimal reservedBalance;
    private WalletStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WalletModel() {}
    private WalletModel(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.totalBalance = builder.totalBalance;
        this.availableBalance = builder.availableBalance;
        this.reservedBalance = builder.reservedBalance;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static class Builder {
        private Long id;
        private Long userId;
        private BigDecimal totalBalance;
        private BigDecimal availableBalance;
        private BigDecimal reservedBalance;
        private WalletStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }
        public Builder totalBalance(BigDecimal totalBalance) {
            this.totalBalance = totalBalance;
            return this;
        }
        public Builder availableBalance(BigDecimal availableBalance) {
            this.availableBalance = availableBalance;
            return this;
        }
        public Builder reservedBalance(BigDecimal reservedBalance) {
            this.reservedBalance = reservedBalance;
            return this;
        }
        public Builder status(WalletStatus status) {
            this.status = status;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public WalletModel build() {
            return new WalletModel(this);
        }
    }

    // Getters
    public Long getId() {
        return id;
    }
    public Long getUserId() {
        return userId;
    }
    public BigDecimal getTotalBalance() {
        return totalBalance;
    }
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }
    public BigDecimal getReservedBalance() {
        return reservedBalance;
    }
    public WalletStatus getStatus() {
        return status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }
    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }
    public void setReservedBalance(BigDecimal reservedBalance) {
        this.reservedBalance = reservedBalance;
    }
    public void setStatus(WalletStatus status) {
        this.status = status;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

