package com.micropay.wallet.controller;

import com.micropay.wallet.dto.response.CursorPage;
import com.micropay.wallet.model.WalletModel;
import com.micropay.wallet.model.WalletStatus;
import com.micropay.wallet.service.wallet.WalletManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/wallets")
@RequiredArgsConstructor
public class AdminCallsController {

    private final WalletManagementService walletService;

    @GetMapping
    public ResponseEntity<CursorPage<WalletModel>> getWallets(
            @RequestParam (required = false) WalletStatus status,
            @RequestParam (required = false) Integer pageSize,
            @RequestParam (required = false) Long cursorId,
            @RequestParam (required = false) String sortBy
    ) {
        CursorPage<WalletModel> walletList = walletService.getWallets(status, pageSize, cursorId, sortBy);

        return ResponseEntity.ok(walletList);
    }

}
