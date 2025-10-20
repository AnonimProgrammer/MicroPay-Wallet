package com.micropay.wallet.dto.request;

import java.math.BigDecimal;

public record ReservationRequest(Long paymentId, BigDecimal requestedAmount) {}
