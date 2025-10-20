package com.micropay.wallet.service.wallet.impl;

import com.micropay.wallet.exception.InsufficientBalanceException;
import com.micropay.wallet.exception.NotActiveWalletException;
import com.micropay.wallet.exception.ReservationNotFoundException;
import com.micropay.wallet.model.ReservationStatus;
import com.micropay.wallet.model.WalletStatus;
import com.micropay.wallet.model.entity.Reservation;
import com.micropay.wallet.model.entity.Wallet;
import com.micropay.wallet.repo.ReservationRepository;
import com.micropay.wallet.repo.WalletRepository;
import com.micropay.wallet.service.cache.CacheService;
import com.micropay.wallet.service.wallet.impl.BalanceManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BalanceManagementServiceImplTest {

    private WalletRepository walletRepository;
    private ReservationRepository reservationRepository;
    private CacheService cacheService;
    private BalanceManagementServiceImpl balanceService;

    private final Long WALLET_ID = 1L;
    private final Long PAYMENT_ID = 100L;

    @BeforeEach
    void setUp() {
        walletRepository = mock(WalletRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        cacheService = mock(CacheService.class);

        balanceService = new BalanceManagementServiceImpl(walletRepository, reservationRepository, cacheService);
    }

    @Test
    void reserveBalance_ShouldReserveAndReturnAvailableBalance() {
        Wallet wallet = new Wallet();
        wallet.setId(WALLET_ID);
        wallet.setUserId(UUID.randomUUID());
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setAvailableBalance(BigDecimal.valueOf(200));
        wallet.setReservedBalance(BigDecimal.ZERO);

        when(walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);

        BigDecimal reservedAmount = BigDecimal.valueOf(50);
        BigDecimal result = balanceService.reserveBalance(WALLET_ID, reservedAmount);

        assertEquals(BigDecimal.valueOf(150), result);
        assertEquals(BigDecimal.valueOf(50), wallet.getReservedBalance());
        verify(walletRepository).save(wallet);
        verify(cacheService).evict("walletData", wallet.getUserId().toString());
        verify(cacheService).evictAll("wallets");
    }

    @Test
    void reserveBalance_ShouldThrowNotActiveWalletException() {
        Wallet wallet = new Wallet();
        wallet.setStatus(WalletStatus.DEACTIVATED);
        when(walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));

        assertThrows(NotActiveWalletException.class, () ->
                balanceService.reserveBalance(WALLET_ID, BigDecimal.TEN));
    }

    @Test
    void reserveBalance_ShouldThrowInsufficientBalanceException() {
        Wallet wallet = new Wallet();
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setAvailableBalance(BigDecimal.valueOf(20));
        when(walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));

        assertThrows(InsufficientBalanceException.class, () ->
                balanceService.reserveBalance(WALLET_ID, BigDecimal.valueOf(50)));
    }

    @Test
    void debitBalance_ShouldDebitReservedBalanceAndUpdateReservation() {
        Wallet wallet = new Wallet();
        wallet.setReservedBalance(BigDecimal.valueOf(100));
        wallet.setTotalBalance(BigDecimal.valueOf(200));

        Reservation reservation = new Reservation();
        reservation.setAmount(BigDecimal.valueOf(100));
        reservation.setStatus(ReservationStatus.RESERVED);

        when(walletRepository.findByIdForUpdate(WALLET_ID)).thenReturn(Optional.of(wallet));
        when(reservationRepository.findByPaymentId(PAYMENT_ID)).thenReturn(Optional.of(reservation));

        balanceService.debitBalance(WALLET_ID, PAYMENT_ID);

        assertEquals(BigDecimal.ZERO, wallet.getReservedBalance());
        assertEquals(BigDecimal.valueOf(100), wallet.getTotalBalance());
        assertEquals(ReservationStatus.DEBITED, reservation.getStatus());

        verify(walletRepository).save(wallet);
        verify(reservationRepository).save(reservation);
        verify(cacheService).evictAll("wallets");
    }

    @Test
    void debitBalance_ShouldThrowReservationNotFoundException() {
        when(walletRepository.findByIdForUpdate(WALLET_ID)).thenReturn(Optional.of(new Wallet()));
        when(reservationRepository.findByPaymentId(PAYMENT_ID)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () ->
                balanceService.debitBalance(WALLET_ID, PAYMENT_ID));
    }

    @Test
    void creditBalance_ShouldAddAmountToWallet() {
        Wallet wallet = new Wallet();
        wallet.setId(WALLET_ID);
        wallet.setUserId(UUID.randomUUID());
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setTotalBalance(BigDecimal.valueOf(100));
        wallet.setAvailableBalance(BigDecimal.valueOf(50));

        when(walletRepository.findByIdForUpdate(WALLET_ID)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);

        balanceService.creditBalance(WALLET_ID, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), wallet.getTotalBalance());
        assertEquals(BigDecimal.valueOf(100), wallet.getAvailableBalance());

        verify(walletRepository).save(wallet);
        verify(cacheService).evict("walletData", wallet.getUserId().toString());
        verify(cacheService).evictAll("wallets");
    }

    @Test
    void creditBalance_ShouldThrowNotActiveWalletException() {
        Wallet wallet = new Wallet();
        wallet.setStatus(WalletStatus.CLOSED);
        when(walletRepository.findByIdForUpdate(WALLET_ID)).thenReturn(Optional.of(wallet));

        assertThrows(NotActiveWalletException.class, () ->
                balanceService.creditBalance(WALLET_ID, BigDecimal.TEN));
    }
}
