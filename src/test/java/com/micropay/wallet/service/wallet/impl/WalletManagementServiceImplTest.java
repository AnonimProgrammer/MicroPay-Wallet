package com.micropay.wallet.service.wallet.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.micropay.wallet.dto.response.CursorPage;
import com.micropay.wallet.dto.response.WalletResponse;
import com.micropay.wallet.exception.NotActiveWalletException;
import com.micropay.wallet.exception.WalletNotFoundException;
import com.micropay.wallet.model.WalletModel;
import com.micropay.wallet.model.WalletStatus;
import com.micropay.wallet.model.entity.Wallet;
import com.micropay.wallet.repo.WalletRepository;
import com.micropay.wallet.service.cache.CacheService;
import com.micropay.wallet.mapper.WalletMapper;
import com.micropay.wallet.service.wallet.impl.WalletManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletManagementServiceImplTest {

    private WalletRepository walletRepository;
    private WalletMapper walletMapper;
    private CacheService cacheService;
    private WalletManagementServiceImpl walletService;

    private static final UUID USER_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        walletRepository = mock(WalletRepository.class);
        walletMapper = mock(WalletMapper.class);
        cacheService = mock(CacheService.class);
        walletService = new WalletManagementServiceImpl(walletRepository, walletMapper, cacheService);
    }

    @Test
    void createWallet_ShouldSaveAndEvictCache() {
        Wallet wallet = new Wallet();
        wallet.setUserId(USER_ID);
        WalletResponse response = mock(WalletResponse.class);

        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletMapper.toResponse(wallet)).thenReturn(response);

        WalletResponse result = walletService.createWallet(USER_ID);

        assertEquals(response, result);
        verify(walletRepository, times(1)).save(any(Wallet.class));
        verify(cacheService, times(1)).evictAll("wallets");
        verify(walletMapper, times(1)).toResponse(wallet);
    }

    @Test
    void updateWalletStatus_ShouldUpdateAndEvictCache() {
        Wallet wallet = new Wallet();
        wallet.setStatus(WalletStatus.DEACTIVATED);

        when(walletRepository.findByUserId(USER_ID)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);

        walletService.updateWalletStatus(USER_ID, WalletStatus.ACTIVE);

        assertEquals(WalletStatus.ACTIVE, wallet.getStatus());
        verify(walletRepository, times(1)).save(wallet);
        verify(cacheService, times(1)).evict("walletData", USER_ID.toString());
        verify(cacheService, times(1)).evictAll("wallets");
    }

    @Test
    void updateWalletStatus_ShouldThrowException_WhenWalletNotFound() {
        when(walletRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class,
                () -> walletService.updateWalletStatus(USER_ID, WalletStatus.ACTIVE));
    }

    @Test
    void getWalletIdByUserId_ShouldReturnFromCacheOrRepository() {
        when(cacheService.getOrPut(eq("walletIds"), anyString(),
                any(TypeReference.class), any())).thenReturn(123L);

        Long walletId = walletService.getWalletIdByUserId(USER_ID);

        assertEquals(123L, walletId);
        verify(cacheService, times(1))
                .getOrPut(eq("walletIds"), anyString(), any(TypeReference.class), any());
    }

    @Test
    void getWalletByUserId_ShouldReturnActiveWallet() {
        Wallet wallet = new Wallet();
        wallet.setStatus(WalletStatus.ACTIVE);
        WalletResponse response = mock(WalletResponse.class);

        when(cacheService.getOrPut(eq("walletData"), anyString(), any(TypeReference.class), any()))
                .thenAnswer(invocation -> {
                    Supplier<WalletResponse> supplier = invocation.getArgument(3);
                    return supplier.get();
                });
        when(walletRepository.findByUserId(USER_ID)).thenReturn(Optional.of(wallet));
        when(walletMapper.toResponse(wallet)).thenReturn(response);

        WalletResponse result = walletService.getWalletByUserId(USER_ID);

        assertEquals(response, result);
    }

    @Test
    void getWalletByUserId_ShouldThrowNotActiveWalletException_WhenWalletInactive() {
        Wallet wallet = new Wallet();
        wallet.setStatus(WalletStatus.CLOSED);

        when(cacheService.getOrPut(eq("walletData"), anyString(), any(TypeReference.class), any()))
                .thenAnswer(invocation -> {
                    Supplier<WalletResponse> supplier = invocation.getArgument(3);
                    return supplier.get();
                });
        when(walletRepository.findByUserId(USER_ID)).thenReturn(Optional.of(wallet));

        assertThrows(NotActiveWalletException.class, () -> walletService.getWalletByUserId(USER_ID));
    }

    @Test
    void getWalletByUserId_ShouldThrowWalletNotFoundException_WhenWalletMissing() {
        when(cacheService.getOrPut(eq("walletData"), anyString(), any(TypeReference.class), any()))
                .thenAnswer(invocation -> {
                    Supplier<WalletResponse> supplier = invocation.getArgument(3);
                    return supplier.get();
                });
        when(walletRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.getWalletByUserId(USER_ID));
    }

    @Test
    void getWallets_ShouldReturnCursorPageFromCacheService() {
        CursorPage<WalletModel> cursorPage = new CursorPage<>(List.of(), null, false);

        when(cacheService.getOrPut(
                eq("wallets"), anyString(),
                any(TypeReference.class), any()))
                .thenReturn(cursorPage);

        CursorPage<WalletModel> result = walletService.getWallets(
                WalletStatus.ACTIVE, 10, 1L, "asc"
        );
        assertEquals(cursorPage, result);
        verify(cacheService, times(1)).getOrPut(eq("wallets"), anyString(),
                any(TypeReference.class), any());
    }
}
