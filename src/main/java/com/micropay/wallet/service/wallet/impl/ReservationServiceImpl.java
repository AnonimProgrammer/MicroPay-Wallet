package com.micropay.wallet.service.wallet.impl;

import com.micropay.wallet.dto.request.ReservationRequest;
import com.micropay.wallet.dto.response.ReservationResponse;
import com.micropay.wallet.mapper.ReservationMapper;
import com.micropay.wallet.model.entity.Reservation;
import com.micropay.wallet.repo.ReservationRepository;
import com.micropay.wallet.service.wallet.BalanceManagementService;
import com.micropay.wallet.service.wallet.ReservationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final BalanceManagementService balanceManagementService;

    @Transactional
    @Override
    public ReservationResponse reserveBalance(Long id, ReservationRequest request) {
        Reservation reservation = reservationMapper.toEntity(id, request);

        BigDecimal availableBalance = balanceManagementService.reserveBalance(id, request.requestedAmount());
        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toReservationResponse(id, savedReservation, availableBalance);
    }

}
