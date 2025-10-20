package com.micropay.wallet.service.wallet.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.micropay.wallet.dto.response.CursorPage;
import com.micropay.wallet.dto.response.WalletResponse;
import com.micropay.wallet.exception.NotActiveWalletException;
import com.micropay.wallet.model.entity.Wallet;
import com.micropay.wallet.exception.WalletNotFoundException;
import com.micropay.wallet.model.WalletModel;
import com.micropay.wallet.model.WalletStatus;
import com.micropay.wallet.repo.WalletRepository;
import com.micropay.wallet.service.cache.CacheService;
import com.micropay.wallet.mapper.WalletMapper;
import com.micropay.wallet.service.wallet.WalletManagementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletManagementServiceImpl implements WalletManagementService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final CacheService cacheService;

    private final static int DEFAULT_PAGE_SIZE = 20;

    @Override
    @Transactional
    public WalletResponse createWallet(UUID userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);

        Wallet savedWallet = walletRepository.save(wallet);
        cacheService.evictAll("wallets");

        return walletMapper.toResponse(savedWallet);
    }

    @Override
    @Transactional
    public void updateWalletStatus(UUID userId, WalletStatus status) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for User ID: " + userId));

        wallet.setStatus(status);
        walletRepository.save(wallet);

        cacheService.evict("walletData", userId.toString());
        cacheService.evictAll("wallets");
    }

    @Override
    public Long getWalletIdByUserId(UUID userId) {
        String cacheKey = "walletId_" + userId;
        return cacheService.getOrPut(
                "walletIds", cacheKey, new TypeReference<Long>() {},
                () -> walletRepository.findWalletIdByUserId(userId)
                        .orElseThrow(() -> new WalletNotFoundException("Wallet not found for User ID: " + userId))
        );
    }

    @Override
    public WalletResponse getWalletByUserId(UUID userId) {
        String cacheKey = userId.toString();
        return cacheService.getOrPut(
                "walletData", cacheKey, new TypeReference<WalletResponse>() {}, () -> {
                    Wallet wallet = walletRepository.findByUserId(userId)
                            .orElseThrow(() -> new WalletNotFoundException("Wallet not found for User ID: " + userId));
                    isActive(wallet);
                    return walletMapper.toResponse(wallet);
                }
        );
    }

    private void isActive(Wallet wallet) {
        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new NotActiveWalletException();
        }
    }

    @Override
    public CursorPage<WalletModel> getWallets(
            WalletStatus status, Integer pageSize, Long cursorId, String sortBy
    ) {
        final int size = (pageSize == null || pageSize > 100) ? DEFAULT_PAGE_SIZE : pageSize;

        String sortOrder = (sortBy != null && sortBy.equalsIgnoreCase("asc")) ? "ASC" : "DESC";

        String cacheKey = buildCacheKey(status, cursorId, sortOrder, size);
        return cacheService.getOrPut(
                "wallets", cacheKey, new TypeReference<CursorPage<WalletModel>>() {}, () -> {
                    List<Wallet> wallets = walletRepository.findWallets(status, size, cursorId, sortOrder);
                    checkIfEmpty(wallets);

                    boolean hasNext = wallets.size() > size;
                    Long nextCursor = hasNext ? wallets.get(size).getId() : null;

                    if (hasNext) {
                        wallets = wallets.subList(0, size);
                    }
                    List<WalletModel> walletModels = wallets.stream()
                            .map(walletMapper::toModel)
                            .toList();

                    return new CursorPage<>(walletModels, nextCursor, hasNext);
                }
        );
    }

    private String buildCacheKey(WalletStatus status, Long cursorId, String sortOrder, Integer pageSize) {
        return String.format("%s_%s_%s_%s",
                status != null ? status.name() : "null",
                cursorId != null ? cursorId : "null",
                sortOrder,
                pageSize
        );
    }

    private void checkIfEmpty(List<Wallet> wallets) {
        if (wallets.isEmpty()) {
            throw new WalletNotFoundException("No wallets found in the system.");
        }
    }

}
