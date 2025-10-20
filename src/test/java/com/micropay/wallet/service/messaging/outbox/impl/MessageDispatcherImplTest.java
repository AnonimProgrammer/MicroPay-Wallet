package com.micropay.wallet.service.messaging.outbox.impl;

import com.micropay.wallet.config.RabbitConstants;
import com.micropay.wallet.dto.credit.WalletCreditFailedEvent;
import com.micropay.wallet.dto.credit.WalletCreditedEvent;
import com.micropay.wallet.dto.debit.WalletDebitFailedEvent;
import com.micropay.wallet.dto.debit.WalletDebitedEvent;
import com.micropay.wallet.dto.refund.WalletRefundedEvent;
import com.micropay.wallet.model.entity.BaseEvent;
import com.micropay.wallet.service.messaging.outbox.OutboxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class MessageDispatcherImplTest {

    private OutboxService outboxService;
    private MessageDispatcherImpl dispatcher;

    @BeforeEach
    void setUp() {
        outboxService = mock(OutboxService.class);
        dispatcher = new MessageDispatcherImpl(outboxService);
    }

    @Test
    void publishWalletCreditedEvent_ShouldSaveAndPublish() {
        WalletCreditedEvent event = mock(WalletCreditedEvent.class);
        BaseEvent baseEvent = new BaseEvent();
        when(outboxService.saveBaseEvent(
                "WALLET_CREDITED",
                RabbitConstants.WALLET_CREDIT_EXCHANGE,
                RabbitConstants.WALLET_CREDITED_ROUTING_KEY,
                event
        )).thenReturn(baseEvent);

        dispatcher.publishWalletCreditedEvent(event);

        verify(outboxService, times(1)).saveBaseEvent(
                "WALLET_CREDITED",
                RabbitConstants.WALLET_CREDIT_EXCHANGE,
                RabbitConstants.WALLET_CREDITED_ROUTING_KEY,
                event
        );
        verify(outboxService, times(1)).publish(baseEvent);
    }

    @Test
    void publishWalletCreditFailedEvent_ShouldSaveAndPublish() {
        WalletCreditFailedEvent event = mock(WalletCreditFailedEvent.class);
        BaseEvent baseEvent = new BaseEvent();
        when(outboxService.saveBaseEvent(
                "WALLET_CREDIT_FAILED",
                RabbitConstants.WALLET_CREDIT_EXCHANGE,
                RabbitConstants.WALLET_CREDIT_FAILED_ROUTING_KEY,
                event
        )).thenReturn(baseEvent);

        dispatcher.publishWalletCreditFailedEvent(event);

        verify(outboxService).saveBaseEvent(
                "WALLET_CREDIT_FAILED",
                RabbitConstants.WALLET_CREDIT_EXCHANGE,
                RabbitConstants.WALLET_CREDIT_FAILED_ROUTING_KEY,
                event
        );
        verify(outboxService).publish(baseEvent);
    }

    @Test
    void publishWalletDebitedEvent_ShouldSaveAndPublish() {
        WalletDebitedEvent event = mock(WalletDebitedEvent.class);
        BaseEvent baseEvent = new BaseEvent();
        when(outboxService.saveBaseEvent(
                "WALLET_DEBITED",
                RabbitConstants.WALLET_DEBIT_EXCHANGE,
                RabbitConstants.WALLET_DEBITED_ROUTING_KEY,
                event
        )).thenReturn(baseEvent);

        dispatcher.publishWalletDebitedEvent(event);

        verify(outboxService).saveBaseEvent(
                "WALLET_DEBITED",
                RabbitConstants.WALLET_DEBIT_EXCHANGE,
                RabbitConstants.WALLET_DEBITED_ROUTING_KEY,
                event
        );
        verify(outboxService).publish(baseEvent);
    }

    @Test
    void publishWalletDebitFailedEvent_ShouldSaveAndPublish() {
        WalletDebitFailedEvent event = mock(WalletDebitFailedEvent.class);
        BaseEvent baseEvent = new BaseEvent();
        when(outboxService.saveBaseEvent(
                "WALLET_DEBIT_FAILED",
                RabbitConstants.WALLET_DEBIT_EXCHANGE,
                RabbitConstants.WALLET_DEBIT_FAILED_ROUTING_KEY,
                event
        )).thenReturn(baseEvent);

        dispatcher.publishWalletDebitFailedEvent(event);

        verify(outboxService).saveBaseEvent(
                "WALLET_DEBIT_FAILED",
                RabbitConstants.WALLET_DEBIT_EXCHANGE,
                RabbitConstants.WALLET_DEBIT_FAILED_ROUTING_KEY,
                event
        );
        verify(outboxService).publish(baseEvent);
    }

    @Test
    void publishWalletRefundedEvent_ShouldSaveAndPublish() {
        WalletRefundedEvent event = mock(WalletRefundedEvent.class);
        BaseEvent baseEvent = new BaseEvent();
        when(outboxService.saveBaseEvent(
                "WALLET_REFUNDED",
                RabbitConstants.WALLET_REFUND_EXCHANGE,
                RabbitConstants.WALLET_REFUNDED_ROUTING_KEY,
                event
        )).thenReturn(baseEvent);

        dispatcher.publishWalletRefundedEvent(event);

        verify(outboxService).saveBaseEvent(
                "WALLET_REFUNDED",
                RabbitConstants.WALLET_REFUND_EXCHANGE,
                RabbitConstants.WALLET_REFUNDED_ROUTING_KEY,
                event
        );
        verify(outboxService).publish(baseEvent);
    }
}
