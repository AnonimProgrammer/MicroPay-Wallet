package com.micropay.wallet.service.wallet.impl;

import com.micropay.wallet.dto.request.ReservationRequest;
import com.micropay.wallet.dto.response.ReservationResponse;
import com.micropay.wallet.mapper.ReservationMapper;
import com.micropay.wallet.model.entity.Reservation;
import com.micropay.wallet.repo.ReservationRepository;
import com.micropay.wallet.service.wallet.BalanceManagementService;
import com.micropay.wallet.service.wallet.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReservationServiceImplTest {

    private ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;
    private BalanceManagementService balanceManagementService;
    private ReservationServiceImpl reservationService;

    private final Long WALLET_ID = 1L;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        reservationMapper = mock(ReservationMapper.class);
        balanceManagementService = mock(BalanceManagementService.class);

        reservationService = new ReservationServiceImpl(
                reservationRepository, reservationMapper, balanceManagementService
        );
    }

    @Test
    void reserveBalance_ShouldMapAndSaveReservationAndReturnResponse() {
        ReservationRequest request = mock(ReservationRequest.class);
        Reservation reservationEntity = mock(Reservation.class);
        Reservation savedReservation = mock(Reservation.class);
        ReservationResponse response = mock(ReservationResponse.class);

        when(reservationMapper.toEntity(WALLET_ID, request)).thenReturn(reservationEntity);
        when(balanceManagementService.reserveBalance(WALLET_ID, request.requestedAmount()))
                .thenReturn(BigDecimal.valueOf(100));
        when(reservationRepository.save(reservationEntity)).thenReturn(savedReservation);
        when(reservationMapper.toReservationResponse(WALLET_ID, savedReservation, BigDecimal.valueOf(100)))
                .thenReturn(response);

        ReservationResponse result = reservationService.reserveBalance(WALLET_ID, request);

        assertEquals(response, result);
        verify(reservationMapper, times(1)).toEntity(WALLET_ID, request);
        verify(balanceManagementService, times(1)).reserveBalance(WALLET_ID, request.requestedAmount());
        verify(reservationRepository, times(1)).save(reservationEntity);
        verify(reservationMapper, times(1))
                .toReservationResponse(WALLET_ID, savedReservation, BigDecimal.valueOf(100));
    }
}
