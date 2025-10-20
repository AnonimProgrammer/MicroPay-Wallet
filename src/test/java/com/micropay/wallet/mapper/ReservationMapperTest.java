package com.micropay.wallet.mapper;

import com.micropay.wallet.dto.request.ReservationRequest;
import com.micropay.wallet.dto.response.ReservationResponse;
import com.micropay.wallet.model.ReservationStatus;
import com.micropay.wallet.model.entity.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationMapperTest {

    private ReservationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ReservationMapperImpl();
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        ReservationRequest request = new ReservationRequest(100L, new BigDecimal("50"));
        Long walletId = 10L;

        Reservation reservation = mapper.toEntity(walletId, request);

        assertThat(reservation).isNotNull();
        assertThat(reservation.getWalletId()).isEqualTo(walletId);
        assertThat(reservation.getPaymentId()).isEqualTo(100L);
        assertThat(reservation.getAmount()).isEqualTo(new BigDecimal("50"));
    }

    @Test
    void toEntity_ShouldReturnNullIfBothNull() {
        assertThat(mapper.toEntity(null, null)).isNull();
    }

    @Test
    void toEntity_ShouldHandleNullRequest() {
        Long walletId = 20L;
        Reservation reservation = mapper.toEntity(walletId, null);

        assertThat(reservation).isNotNull();
        assertThat(reservation.getWalletId()).isEqualTo(walletId);
        assertThat(reservation.getPaymentId()).isNull();
        assertThat(reservation.getAmount()).isNull();
    }

    @Test
    void toReservationResponse_ShouldMapCorrectly() {
        Reservation reservation = new Reservation();
        reservation.setWalletId(5L);
        reservation.setPaymentId(200L);
        reservation.setAmount(new BigDecimal("75"));
        reservation.setStatus(ReservationStatus.RESERVED);

        BigDecimal availableBalance = new BigDecimal("500");

        ReservationResponse response = mapper.toReservationResponse(5L, reservation, availableBalance);

        assertThat(response).isNotNull();
        assertThat(response.walletId()).isEqualTo(5L);
        assertThat(response.paymentId()).isEqualTo(200L);
        assertThat(response.reservedAmount()).isEqualTo(new BigDecimal("75"));
        assertThat(response.availableBalance()).isEqualTo(new BigDecimal("500"));
        assertThat(response.status()).isEqualTo(ReservationStatus.RESERVED);
    }

    @Test
    void toReservationResponse_ShouldReturnNullIfAllNull() {
        assertThat(mapper.toReservationResponse(null, null, null)).isNull();
    }

    @Test
    void toReservationResponse_ShouldHandleNullReservation() {
        BigDecimal availableBalance = new BigDecimal("300");
        ReservationResponse response = mapper.toReservationResponse(10L, null, availableBalance);

        assertThat(response).isNotNull();
        assertThat(response.walletId()).isNull();
        assertThat(response.paymentId()).isNull();
        assertThat(response.reservedAmount()).isNull();
        assertThat(response.availableBalance()).isEqualTo(availableBalance);
        assertThat(response.status()).isNull();
    }
}
