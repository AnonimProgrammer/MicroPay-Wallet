package com.docker.mswallet.service.impl;

import com.docker.mswallet.entity.Wallet;
import com.docker.mswallet.exception.InsufficientBalanceException;
import com.docker.mswallet.exception.WalletNotFoundException;
import com.docker.mswallet.model.WalletModel;
import com.docker.mswallet.model.WalletStatus;
import com.docker.mswallet.repo.WalletRepository;
import com.docker.mswallet.service.WalletService;
import com.docker.mswallet.util.WalletMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    public WalletServiceImpl(WalletRepository walletRepository, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    @Override
    @Transactional
    public WalletModel createWallet(Long userId) {
        Wallet wallet = new Wallet.Builder()
                .userId(userId)
                .balance(BigDecimal.ZERO)
                .status(WalletStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
        Wallet savedWallet = walletRepository.save(wallet);
        return walletMapper.toModel(savedWallet);
    }

    @Override
    @Transactional
    public void debitBalance(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for ID: " + walletId));
        BigDecimal balance = wallet.getBalance();

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance. Balance: " + balance);
        }
        wallet.setBalance(balance.subtract(amount));
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        logger.info("Debited {} from wallet ID {}. New balance: {}", amount, walletId, wallet.getBalance());
    }

    @Override
    @Transactional
    public void creditBalance(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for ID: " + walletId));
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        logger.info("Credited {} to wallet ID {}. New balance: {}", amount, walletId, wallet.getBalance());
    }

    @Override
    public WalletModel getWalletById(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for ID: " + walletId));
        return walletMapper.toModel(wallet);
    }

    @Override
    public List<WalletModel>  getWalletsByUserId(Long userId) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        if (wallets.isEmpty()) {
            throw new WalletNotFoundException("No wallets found for user ID: " + userId);
        }

        return wallets.stream()
                .map(walletMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateWalletStatus(Long walletId, WalletStatus status) {
        Wallet wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for ID: " + walletId));
        wallet.setStatus(status);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        logger.info("Updated wallet ID {} status to {}", walletId, status);
    }

    @Override
    public List<WalletModel> getWalletsByFilters(WalletStatus status) {
        List<Wallet> filteredWallets = walletRepository
                .findByFilters(status);
        if (filteredWallets.isEmpty()) {
            throw new WalletNotFoundException("No wallets found with the specified filters.");
        }
        return filteredWallets.stream()
                .map(walletMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<WalletModel> getAllWallets() {
        List<Wallet> wallets = walletRepository.findAll();
        if (wallets.isEmpty()) {
            throw new WalletNotFoundException("No wallets found in the system.");
        }
        return wallets.stream()
                .map(walletMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Long getUserIdByWalletId(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for ID: " + walletId));
        return wallet.getUserId();
    }
}
