package com.docker.mswallet.controller;

import com.docker.mswallet.model.WalletModel;
import com.docker.mswallet.model.WalletStatus;
import com.docker.mswallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
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

    @GetMapping("/internal/filter")
    public ResponseEntity<List<WalletModel>> getWalletsByFilters(
            @RequestParam WalletStatus status
    ) {
        List<WalletModel> wallets = walletService.getWalletsByFilters(status);
        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/internal")
    public ResponseEntity<List<WalletModel>> getAllWallets() {
        List<WalletModel> wallets = walletService.getAllWallets();
        return ResponseEntity.ok(wallets);
    }

}
