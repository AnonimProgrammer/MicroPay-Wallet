package com.micropay.wallet.mapper;

import com.micropay.wallet.dto.credit.CreditWalletEvent;
import com.micropay.wallet.dto.debit.DebitWalletEvent;
import com.micropay.wallet.dto.refund.RefundWalletEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class EventMapperTest {

    private EventMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EventMapperImpl();
    }

    @Test
    void mapToWalletDebitedEvent_ShouldMapCorrectly() {
        DebitWalletEvent event = new DebitWalletEvent(null, 1L, 2L, new BigDecimal("100"));
        var mapped = mapper.mapToWalletDebitedEvent(event);

        assertThat(mapped).isNotNull();
        assertThat(mapped.eventId()).isNotNull();
        assertThat(mapped.paymentId()).isEqualTo(1L);
        assertThat(mapped.walletId()).isEqualTo(2L);
        assertThat(mapped.amount()).isEqualTo(new BigDecimal("100"));
    }

    @Test
    void mapToWalletDebitFailedEvent_ShouldMapCorrectly() {
        DebitWalletEvent event = new DebitWalletEvent(null, 1L, 2L, new BigDecimal("50"));
        String reason = "Insufficient funds";

        var mapped = mapper.mapToWalletDebitFailedEvent(event, reason);

        assertThat(mapped).isNotNull();
        assertThat(mapped.eventId()).isNotNull();
        assertThat(mapped.paymentId()).isEqualTo(1L);
        assertThat(mapped.walletId()).isEqualTo(2L);
        assertThat(mapped.failureReason()).isEqualTo(reason);
    }

    @Test
    void mapToWalletDebitFailedEvent_ShouldReturnNullIfBothNull() {
        assertThat(mapper.mapToWalletDebitFailedEvent(null, null)).isNull();
    }

    @Test
    void mapToWalletCreditedEvent_ShouldMapCorrectly() {
        CreditWalletEvent event = new CreditWalletEvent(null, 10L, 20L, new BigDecimal("75"));
        var mapped = mapper.mapToWalletCreditedEvent(event);

        assertThat(mapped).isNotNull();
        assertThat(mapped.eventId()).isNotNull();
        assertThat(mapped.paymentId()).isEqualTo(10L);
        assertThat(mapped.walletId()).isEqualTo(20L);
        assertThat(mapped.amount()).isEqualTo(new BigDecimal("75"));
    }

    @Test
    void mapToWalletCreditFailedEvent_ShouldMapCorrectly() {
        CreditWalletEvent event = new CreditWalletEvent(null, 10L, 20L, new BigDecimal("75"));
        String reason = "Network error";

        var mapped = mapper.mapToWalletCreditFailedEvent(event, reason);

        assertThat(mapped).isNotNull();
        assertThat(mapped.eventId()).isNotNull();
        assertThat(mapped.paymentId()).isEqualTo(10L);
        assertThat(mapped.walletId()).isEqualTo(20L);
        assertThat(mapped.failureReason()).isEqualTo(reason);
    }

    @Test
    void mapToWalletCreditFailedEvent_ShouldReturnNullIfBothNull() {
        assertThat(mapper.mapToWalletCreditFailedEvent(null, null)).isNull();
    }

    @Test
    void mapToWalletRefundedEvent_ShouldMapCorrectly() {
        RefundWalletEvent event = new RefundWalletEvent(null, 5L, 6L, new BigDecimal("25"));
        var mapped = mapper.mapToWalletRefundedEvent(event);

        assertThat(mapped).isNotNull();
        assertThat(mapped.eventId()).isNotNull();
        assertThat(mapped.paymentId()).isEqualTo(5L);
        assertThat(mapped.walletId()).isEqualTo(6L);
        assertThat(mapped.amount()).isEqualTo(new BigDecimal("25"));
    }

    @Test
    void mapMethods_ShouldReturnNullForNullInput() {
        assertThat(mapper.mapToWalletDebitedEvent(null)).isNull();
        assertThat(mapper.mapToWalletCreditedEvent(null)).isNull();
        assertThat(mapper.mapToWalletRefundedEvent(null)).isNull();
    }
}
