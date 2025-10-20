package com.micropay.wallet.controller;

import com.micropay.wallet.dto.response.WalletResponse;
import com.micropay.wallet.service.wallet.WalletManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/wallets")
public class WalletController {

    private final WalletManagementService walletService;

    public WalletController(WalletManagementService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(
            @RequestHeader("X-User-Id") UUID userId
    ) {
        WalletResponse createdWallet = walletService.createWallet(userId);
        return ResponseEntity.status(201).body(createdWallet);
    }

    @GetMapping()
    public ResponseEntity<WalletResponse> getWalletById(
            @RequestHeader("X-User-Id") UUID userId
    ) {
        return ResponseEntity.ok(walletService.getWalletByUserId(userId));
    }

}
