package com.micropay.wallet.controller;

import com.micropay.wallet.dto.request.ReservationRequest;
import com.micropay.wallet.dto.response.ReservationResponse;
import com.micropay.wallet.model.WalletStatus;
import com.micropay.wallet.service.wallet.ReservationService;
import com.micropay.wallet.service.wallet.WalletManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/internal/wallets")
@RequiredArgsConstructor
public class InternalCallsController {

    private final ReservationService reservationService;
    private final WalletManagementService walletService;

    @PostMapping("/{id}/reserve")
    public ResponseEntity<ReservationResponse> reserveBalance(
            @PathVariable Long id,
            @RequestBody ReservationRequest request
    ){
        ReservationResponse response = reservationService.reserveBalance(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/deactivate")
    public ResponseEntity<Void> deactivateWallet(@RequestHeader("X-User-Id") UUID userId) {
        walletService.updateWalletStatus(userId, WalletStatus.DEACTIVATED);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/activate")
    public ResponseEntity<Void> activateWallet(@RequestHeader("X-User-Id") UUID userId) {
        walletService.updateWalletStatus(userId, WalletStatus.ACTIVE);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/close")
    public ResponseEntity<Void> closeWallet(@RequestHeader("X-User-Id") UUID userId) {
        walletService.updateWalletStatus(userId, WalletStatus.CLOSED);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Long> getWalletId(@RequestHeader("X-User-Id") UUID userId) {
        Long walletId = walletService.getWalletIdByUserId(userId);
        return ResponseEntity.ok(walletId);
    }

}
