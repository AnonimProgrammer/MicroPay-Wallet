package com.micropay.wallet.service.impl;

import com.micropay.wallet.dto.internal.ReservationRequest;
import com.micropay.wallet.dto.internal.ReservationResponse;
import com.micropay.wallet.model.entity.Reservation;
import com.micropay.wallet.repo.ReservationRepository;
import com.micropay.wallet.service.WalletDataAccessService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReservationServiceImpl {

    private final ReservationRepository reservationRepository;
    private final WalletDataAccessService walletDataAccessService;

    public ReservationServiceImpl(ReservationRepository reservationRepository, WalletDataAccessService walletDataAccessService) {
        this.reservationRepository = reservationRepository;
        this.walletDataAccessService = walletDataAccessService;
    }

    @Transactional
    public ReservationResponse reserveBalance(Long id, ReservationRequest request) {
        Reservation reservation = new Reservation.Builder()
                .setWalletId(id)
                .setPaymentId(request.getPaymentId())
                .setAmount(request.getRequestedAmount())
                .build();
        BigDecimal availableBalance = walletDataAccessService.reserveBalance(id, request.getRequestedAmount());
        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse.Builder()
                .setWalletId(id)
                .setPaymentId(savedReservation.getPaymentId())
                .setReservedAmount(savedReservation.getAmount())
                .setAvailableBalance(availableBalance)
                .setStatus(savedReservation.getStatus())
                .build();
    }

}
