package com.micropay.wallet.service;

import com.micropay.wallet.model.WalletModel;
import com.micropay.wallet.model.WalletStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public interface WalletDataAccessService {

    Logger logger = LoggerFactory.getLogger(WalletDataAccessService.class);

    WalletModel createWallet(Long userId);

    BigDecimal reserveBalance(Long walletId, BigDecimal amount);

    void debitBalance(Long walletId, Long paymentId);

    void creditBalance(Long walletId, BigDecimal amount);

    WalletModel getWalletById(Long walletId);

    List<WalletModel> getWalletsByUserId(Long userId);

    void updateWalletStatus(Long walletId, WalletStatus status);

    List<WalletModel> getAllWallets();

    List<WalletModel> getWalletsByFilters(WalletStatus status);

    Long getUserIdByWalletId(Long walletId);

}
