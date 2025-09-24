package com.micropay.wallet.controller;

import com.micropay.wallet.dto.internal.ReservationRequest;
import com.micropay.wallet.dto.internal.ReservationResponse;
import com.micropay.wallet.model.WalletModel;
import com.micropay.wallet.model.WalletStatus;
import com.micropay.wallet.service.WalletDataAccessService;
import com.micropay.wallet.service.impl.ReservationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/wallets")
public class WalletInternalController {

    private final WalletDataAccessService walletService;
    private final ReservationServiceImpl reservationService;

    public WalletInternalController(WalletDataAccessService walletService, ReservationServiceImpl reservationService) {
        this.walletService = walletService;
        this.reservationService = reservationService;
    }

    @GetMapping("/filter")
    public ResponseEntity<List<WalletModel>> getWalletsByFilters(
            @RequestParam WalletStatus status
    ) {
        List<WalletModel> wallets = walletService.getWalletsByFilters(status);
        return ResponseEntity.ok(wallets);
    }

    @GetMapping
    public ResponseEntity<List<WalletModel>> getAllWallets() {
        List<WalletModel> wallets = walletService.getAllWallets();
        return ResponseEntity.ok(wallets);
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<ReservationResponse> reserveBalance(
            @PathVariable Long id,
            @RequestBody ReservationRequest request
    ){
        ReservationResponse response = reservationService.reserveBalance(id, request);
        return ResponseEntity.ok(response);
    }

}
