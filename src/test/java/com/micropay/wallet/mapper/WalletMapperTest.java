package com.micropay.wallet.mapper;

import com.micropay.wallet.dto.response.WalletResponse;
import com.micropay.wallet.model.WalletModel;
import com.micropay.wallet.model.WalletStatus;
import com.micropay.wallet.model.entity.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class WalletMapperTest {

    private WalletMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new WalletMapperImpl();
    }

    @Test
    void toModel_ShouldMapAllFields() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setUserId(UUID.randomUUID());
        wallet.setTotalBalance(new BigDecimal("1000"));
        wallet.setAvailableBalance(new BigDecimal("800"));
        wallet.setReservedBalance(new BigDecimal("200"));
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setUpdatedAt(LocalDateTime.now());

        WalletModel model = mapper.toModel(wallet);

        assertThat(model).isNotNull();
        assertThat(model.getId()).isEqualTo(wallet.getId());
        assertThat(model.getUserId()).isEqualTo(wallet.getUserId());
        assertThat(model.getTotalBalance()).isEqualTo(wallet.getTotalBalance());
        assertThat(model.getAvailableBalance()).isEqualTo(wallet.getAvailableBalance());
        assertThat(model.getReservedBalance()).isEqualTo(wallet.getReservedBalance());
        assertThat(model.getStatus()).isEqualTo(wallet.getStatus());
        assertThat(model.getCreatedAt()).isEqualTo(wallet.getCreatedAt());
        assertThat(model.getUpdatedAt()).isEqualTo(wallet.getUpdatedAt());
    }

    @Test
    void toResponse_ShouldMapCorrectFields() {
        Wallet wallet = new Wallet();
        wallet.setId(2L);
        wallet.setUserId(UUID.randomUUID());
        wallet.setAvailableBalance(new BigDecimal("500"));

        WalletResponse response = mapper.toResponse(wallet);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(wallet.getId());
        assertThat(response.userId()).isEqualTo(wallet.getUserId());
        assertThat(response.availableBalance()).isEqualTo(wallet.getAvailableBalance());
    }

    @Test
    void mappingMethods_ShouldReturnNullForNullInput() {
        assertThat(mapper.toModel(null)).isNull();
        assertThat(mapper.toResponse(null)).isNull();
    }
}
