package com.docker.mswallet.model.entity;

import com.docker.mswallet.model.WalletStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal totalBalance;

    @Column(nullable = false)
    private BigDecimal availableBalance;

    @Column(nullable = false)
    private BigDecimal reservedBalance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WalletStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public Wallet() {}
    private Wallet(Builder builder) {
        this.userId = builder.userId;
        this.totalBalance = builder.totalBalance;
        this.availableBalance = builder.availableBalance;
        this.reservedBalance = builder.reservedBalance;
        this.status = builder.status;
    }

    public static class Builder {
        private Long userId;
        private BigDecimal totalBalance;
        private BigDecimal availableBalance;
        private BigDecimal reservedBalance;
        private WalletStatus status;

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

        public Wallet build() {
            return new Wallet(this);
        }
    }

    @PrePersist
    public void prePersist() {
        this.totalBalance = BigDecimal.ZERO;
        this.availableBalance = BigDecimal.ZERO;
        this.reservedBalance = BigDecimal.ZERO;
        this.status = WalletStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
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
