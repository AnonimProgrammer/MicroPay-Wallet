package com.micropay.wallet.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(
        Long id,
        UUID userId,
        BigDecimal availableBalance
) {}
