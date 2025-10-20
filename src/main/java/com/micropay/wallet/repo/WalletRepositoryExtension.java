package com.micropay.wallet.repo;

import com.micropay.wallet.model.WalletStatus;
import com.micropay.wallet.model.entity.Wallet;

import java.util.List;

public interface WalletRepositoryExtension {

    List<Wallet> findWallets(WalletStatus status, Integer pageSize, Long cursorId, String sortOrder);
}
