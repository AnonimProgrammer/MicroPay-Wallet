package com.micropay.wallet.service.wallet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public interface BalanceManagementService {

    Logger logger = LoggerFactory.getLogger(BalanceManagementService.class);

    BigDecimal reserveBalance(Long walletId, BigDecimal amount);

    void debitBalance(Long walletId, Long paymentId);

    void creditBalance(Long walletId, BigDecimal amount);
}
