package com.micropay.wallet.repo.impl;

import com.micropay.wallet.model.WalletStatus;
import com.micropay.wallet.model.entity.Wallet;
import com.micropay.wallet.repo.WalletRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WalletRepositoryExtensionImpl implements WalletRepositoryExtension {

    private final EntityManager entityManager;

    @Override
    public List<Wallet> findWallets(WalletStatus status, Integer limit, Long cursorId, String sortOrder) {
        StringBuilder jpql = new StringBuilder("SELECT w FROM Wallet w WHERE 1=1");

        if (status != null) {
            jpql.append(" AND w.status = :status");
        }
        if (cursorId != null) {
            if (sortOrder.equals("ASC")) {
                jpql.append(" AND w.id > :cursorId");
            } else {
                jpql.append(" AND w.id < :cursorId");
            }
        }
        jpql.append(" ORDER BY w.id ").append(sortOrder);

        TypedQuery<Wallet> query = entityManager.createQuery(jpql.toString(), Wallet.class)
                .setMaxResults(limit + 1);

        if (status != null) {
            query.setParameter("status", status);
        }
        if (cursorId != null) {
            query.setParameter("cursorId", cursorId);
        }
        return query.getResultList();
    }

}
