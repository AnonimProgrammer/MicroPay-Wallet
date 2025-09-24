package com.micropay.wallet.repo;

import com.micropay.wallet.model.entity.Wallet;
import com.micropay.wallet.model.WalletStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id = :walletId")
    Optional<Wallet> findByIdForUpdate(@Param("walletId") Long walletId);

    @Query("SELECT w FROM Wallet w WHERE w.userId = :userId")
    List<Wallet> findByUserId(@Param("userId")Long userId);

    @Query("SELECT w FROM Wallet w WHERE w.status = :status")
    List<Wallet> findByFilters(@Param("status") WalletStatus status);

}
