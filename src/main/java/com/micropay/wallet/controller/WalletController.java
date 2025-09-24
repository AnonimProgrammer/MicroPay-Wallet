package com.micropay.wallet.controller;

import com.micropay.wallet.model.WalletModel;
import com.micropay.wallet.model.WalletStatus;
import com.micropay.wallet.service.WalletDataAccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletDataAccessService walletService;

    public WalletController(WalletDataAccessService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<WalletModel> createWallet(
            @RequestHeader("X-User-Id") Long userId
    ) {
        WalletModel createdWallet = walletService.createWallet(userId);
        return ResponseEntity.status(201).body(createdWallet);
    }

    @GetMapping("/user")
    public ResponseEntity<List<WalletModel>> getWalletsByUserId(
            @RequestHeader("X-User-Id") Long userId
    ) {
        List<WalletModel> wallets = walletService.getWalletsByUserId(userId);
        return ResponseEntity.ok(wallets);
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateWallet(@PathVariable Long id) {
        walletService.updateWalletStatus(id, WalletStatus.DEACTIVATED);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<Void> activateWallet(@PathVariable Long id) {
        walletService.updateWalletStatus(id, WalletStatus.ACTIVE);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/close/{id}")
    public ResponseEntity<Void> closeWallet(@PathVariable Long id) {
        walletService.updateWalletStatus(id, WalletStatus.CLOSED);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletModel> getWalletById(@PathVariable Long id) {
        return ResponseEntity.ok(walletService.getWalletById(id));
    }

}
