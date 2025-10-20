package com.micropay.wallet.service.wallet.impl;

import com.micropay.wallet.exception.InsufficientBalanceException;
import com.micropay.wallet.exception.NotActiveWalletException;
import com.micropay.wallet.exception.ReservationNotFoundException;
import com.micropay.wallet.exception.WalletNotFoundException;
import com.micropay.wallet.model.ReservationStatus;
import com.micropay.wallet.model.WalletStatus;
import com.micropay.wallet.model.entity.Reservation;
import com.micropay.wallet.model.entity.Wallet;
import com.micropay.wallet.repo.ReservationRepository;
import com.micropay.wallet.repo.WalletRepository;
import com.micropay.wallet.service.cache.CacheService;
import com.micropay.wallet.service.wallet.BalanceManagementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceManagementServiceImpl implements BalanceManagementService {

    private final WalletRepository walletRepository;
    private final ReservationRepository reservationRepository;
    private final CacheService cacheService;

    @Override
    @Transactional
    public BigDecimal reserveBalance(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        isActive(wallet);
        checkIfEnoughBalance(wallet, amount);

        wallet.setAvailableBalance(wallet.getAvailableBalance().subtract(amount));
        wallet.setReservedBalance(wallet.getReservedBalance().add(amount));

        logger.info("Reserved {} from wallet ID {}. New available balance: {}, New reserved balance: {}",
                amount, walletId, wallet.getAvailableBalance(), wallet.getReservedBalance());

        walletRepository.save(wallet);

        cacheService.evict("walletData", wallet.getUserId().toString());
        cacheService.evictAll("wallets");

        return wallet.getAvailableBalance();
    }

    private void isActive(Wallet wallet) {
        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new NotActiveWalletException();
        }
    }

    private void checkIfEnoughBalance(Wallet wallet, BigDecimal amount) {
        if (wallet.getAvailableBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available Balance: " + wallet.getAvailableBalance(),
                    wallet.getAvailableBalance(), amount
            );
        }
    }

    @Override
    @Transactional
    public void debitBalance(Long walletId, Long paymentId) {
        Wallet wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        Reservation reservation = reservationRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found for Payment ID: " + paymentId));

        if (reservation.getStatus().compareTo(ReservationStatus.DEBITED) == 0) { return; }

        wallet.setReservedBalance(wallet.getReservedBalance().subtract(reservation.getAmount()));
        wallet.setTotalBalance(wallet.getTotalBalance().subtract(reservation.getAmount()));
        reservation.setStatus(ReservationStatus.DEBITED);

        walletRepository.save(wallet);
        reservationRepository.save(reservation);

        cacheService.evictAll("wallets");
    }

    @Override
    @Transactional
    public void creditBalance(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        isActive(wallet);
        wallet.setTotalBalance(wallet.getTotalBalance().add(amount));
        wallet.setAvailableBalance(wallet.getAvailableBalance().add(amount));

        logger.info("Credited {} to wallet ID {}. New balance: {}", amount, walletId, wallet.getTotalBalance());
        walletRepository.save(wallet);

        cacheService.evict("walletData", wallet.getUserId().toString());
        cacheService.evictAll("wallets");
    }
}
