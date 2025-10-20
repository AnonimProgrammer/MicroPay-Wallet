package com.micropay.wallet.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class WalletModel {

    private Long id;
    private UUID userId;
    private BigDecimal totalBalance;
    private BigDecimal availableBalance;
    private BigDecimal reservedBalance;
    private WalletStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

