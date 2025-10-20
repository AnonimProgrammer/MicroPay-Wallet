package com.micropay.wallet.service.wallet;

import com.micropay.wallet.dto.response.CursorPage;
import com.micropay.wallet.dto.response.WalletResponse;
import com.micropay.wallet.model.WalletModel;
import com.micropay.wallet.model.WalletStatus;
import java.util.UUID;

public interface WalletManagementService {

    WalletResponse createWallet(UUID userId);

    WalletResponse getWalletByUserId(UUID userId);

    void updateWalletStatus(UUID userId, WalletStatus status);

    CursorPage<WalletModel> getWallets(WalletStatus status, Integer pageSize, Long cursorId, String sortBy);

    Long getWalletIdByUserId(UUID userId);
}
