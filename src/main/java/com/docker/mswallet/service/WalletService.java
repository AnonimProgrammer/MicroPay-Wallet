package com.docker.mswallet.service;

import com.docker.mswallet.model.WalletModel;
import com.docker.mswallet.model.WalletStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    Logger logger = LoggerFactory.getLogger(WalletService.class);

    WalletModel createWallet(Long userId);

    void debitBalance(Long walletId, BigDecimal amount);

    void creditBalance(Long walletId, BigDecimal amount);

    WalletModel getWalletById(Long walletId);

    List<WalletModel> getWalletsByUserId(Long userId);

    void updateWalletStatus(Long walletId, WalletStatus status);

    List<WalletModel> getAllWallets();

    List<WalletModel> getWalletsByFilters(WalletStatus status);

    Long getUserIdByWalletId(Long walletId);

}
