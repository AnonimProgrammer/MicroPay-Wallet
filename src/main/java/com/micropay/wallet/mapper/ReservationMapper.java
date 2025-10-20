package com.micropay.wallet.mapper;

import com.micropay.wallet.dto.request.ReservationRequest;
import com.micropay.wallet.dto.response.ReservationResponse;
import com.micropay.wallet.model.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "request.requestedAmount", target = "amount")
    Reservation toEntity(Long walletId, ReservationRequest request);

    @Mapping(source = "reservation.amount", target = "reservedAmount")
    ReservationResponse toReservationResponse(Long walletId, Reservation reservation, BigDecimal availableBalance);

}
