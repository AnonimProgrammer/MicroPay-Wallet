package com.micropay.wallet.service.impl;

import com.micropay.wallet.exception.ReservationNotFoundException;
import com.micropay.wallet.model.ReservationStatus;
import com.micropay.wallet.model.entity.Reservation;
import com.micropay.wallet.model.entity.Wallet;
import com.micropay.wallet.exception.InsufficientBalanceException;
import com.micropay.wallet.exception.WalletNotFoundException;
import com.micropay.wallet.model.WalletModel;
import com.micropay.wallet.model.WalletStatus;
import com.micropay.wallet.repo.ReservationRepository;
import com.micropay.wallet.repo.WalletRepository;
import com.micropay.wallet.service.WalletDataAccessService;
import com.micropay.wallet.util.WalletMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletDataAccessServiceImpl implements WalletDataAccessService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final ReservationRepository reservationRepository;

    public WalletDataAccessServiceImpl(WalletRepository walletRepository, WalletMapper walletMapper, ReservationRepository reservationRepository) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional
    public WalletModel createWallet(Long userId) {
        Wallet wallet = new Wallet.Builder()
                .userId(userId).build();
        Wallet savedWallet = walletRepository.save(wallet);
        return walletMapper.toModel(savedWallet);
    }

    @Override
    @Transactional
    public BigDecimal reserveBalance(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for ID: " + walletId));
        BigDecimal availableBalance = wallet.getAvailableBalance();

        if (availableBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available Balance: " + availableBalance,
                    availableBalance, amount
            );
        }
        wallet.setAvailableBalance(wallet.getAvailableBalance().subtract(amount));
        wallet.setReservedBalance(wallet.getReservedBalance().add(amount));
        walletRepository.save(wallet);

        logger.info("Reserved {} from wallet ID {}. New available balance: {}, New reserved balance: {}",
                amount, walletId, wallet.getAvailableBalance(), wallet.getReservedBalance());

        return wallet.getAvailableBalance();
    }

    @Override
    @Transactional
    public void debitBalance(Long walletId, Long paymentId) {
        Wallet wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for ID: " + walletId));

        Reservation reservation = reservationRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found for Payment ID: " + paymentId));
        if (reservation.getStatus().compareTo(ReservationStatus.DEBITED) == 0) { return; }

        wallet.setReservedBalance(wallet.getReservedBalance().subtract(reservation.getAmount()));
        wallet.setTotalBalance(wallet.getTotalBalance().subtract(reservation.getAmount()));
        reservation.setStatus(ReservationStatus.DEBITED);

        walletRepository.save(wallet);
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void creditBalance(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for ID: " + walletId));

        wallet.setTotalBalance(wallet.getTotalBalance().add(amount));
        wallet.setAvailableBalance(wallet.getAvailableBalance().add(amount));
        walletRepository.save(wallet);

        logger.info("Credited {} to wallet ID {}. New balance: {}", amount, walletId, wallet.getTotalBalance());
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
