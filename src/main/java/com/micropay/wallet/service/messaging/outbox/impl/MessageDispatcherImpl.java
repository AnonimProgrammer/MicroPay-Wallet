package com.micropay.wallet.service.messaging.outbox.impl;

import com.micropay.wallet.config.RabbitConstants;
import com.micropay.wallet.dto.credit.WalletCreditFailedEvent;
import com.micropay.wallet.dto.credit.WalletCreditedEvent;
import com.micropay.wallet.dto.debit.WalletDebitFailedEvent;
import com.micropay.wallet.dto.debit.WalletDebitedEvent;
import com.micropay.wallet.dto.refund.WalletRefundedEvent;
import com.micropay.wallet.model.entity.BaseEvent;
import com.micropay.wallet.service.messaging.outbox.MessageDispatcher;
import com.micropay.wallet.service.messaging.outbox.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageDispatcherImpl implements MessageDispatcher {

    private final OutboxService outboxService;

    @Override
    public void publishWalletCreditedEvent(WalletCreditedEvent event) {
        log.info("Publishing WalletCreditedEvent: {}", event);
        BaseEvent baseEvent = outboxService.saveBaseEvent(
                "WALLET_CREDITED",
                RabbitConstants.WALLET_CREDIT_EXCHANGE,
                RabbitConstants.WALLET_CREDITED_ROUTING_KEY, event
        );
        outboxService.publish(baseEvent);
    }

    @Override
    public void publishWalletCreditFailedEvent(WalletCreditFailedEvent event) {
        log.info("Publishing WalletCreditFailedEvent: {}", event);
        BaseEvent baseEvent = outboxService.saveBaseEvent(
                "WALLET_CREDIT_FAILED",
                RabbitConstants.WALLET_CREDIT_EXCHANGE,
                RabbitConstants.WALLET_CREDIT_FAILED_ROUTING_KEY, event
        );
        outboxService.publish(baseEvent);
    }

    @Override
    public void publishWalletDebitedEvent(WalletDebitedEvent event) {
        log.info("Publishing WalletDebitedEvent: {}", event);
        BaseEvent baseEvent = outboxService.saveBaseEvent(
                "WALLET_DEBITED",
                RabbitConstants.WALLET_DEBIT_EXCHANGE,
                RabbitConstants.WALLET_DEBITED_ROUTING_KEY, event
        );
        outboxService.publish(baseEvent);
    }

    @Override
    public void publishWalletDebitFailedEvent(WalletDebitFailedEvent event) {
        log.info("Publishing WalletDebitFailedEvent: {}", event);
        BaseEvent baseEvent = outboxService.saveBaseEvent(
                "WALLET_DEBIT_FAILED",
                RabbitConstants.WALLET_DEBIT_EXCHANGE,
                RabbitConstants.WALLET_DEBIT_FAILED_ROUTING_KEY, event
        );
        outboxService.publish(baseEvent);
    }

    @Override
    public void publishWalletRefundedEvent(WalletRefundedEvent event) {
        log.info("Publishing WalletRefundedEvent: {}", event);
        BaseEvent baseEvent = outboxService.saveBaseEvent(
                "WALLET_REFUNDED",
                RabbitConstants.WALLET_REFUND_EXCHANGE,
                RabbitConstants.WALLET_REFUNDED_ROUTING_KEY, event
        );
        outboxService.publish(baseEvent);
    }

}
