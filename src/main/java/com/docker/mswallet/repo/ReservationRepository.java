package com.docker.mswallet.repo;

import com.docker.mswallet.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.paymentId = :paymentId")
    Reservation findByPaymentId(@Param("paymentId")Long paymentId);
}
