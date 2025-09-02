package com.docker.mswallet.service.impl;

import com.docker.mswallet.dto.internal.ReservationRequest;
import com.docker.mswallet.dto.internal.ReservationResponse;
import com.docker.mswallet.model.ReservationStatus;
import com.docker.mswallet.model.entity.Reservation;
import com.docker.mswallet.repo.ReservationRepository;
import com.docker.mswallet.service.WalletDataAccessService;
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
