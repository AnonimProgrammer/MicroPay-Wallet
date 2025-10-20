package com.micropay.wallet.service.wallet;

import com.micropay.wallet.dto.request.ReservationRequest;
import com.micropay.wallet.dto.response.ReservationResponse;

public interface ReservationService {

    ReservationResponse reserveBalance(Long id, ReservationRequest request);

}
