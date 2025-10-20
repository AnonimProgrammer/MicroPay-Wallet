package com.micropay.wallet.repo;

import com.micropay.wallet.model.entity.InboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InboxRepository extends JpaRepository<InboxEntity, UUID> {

}
