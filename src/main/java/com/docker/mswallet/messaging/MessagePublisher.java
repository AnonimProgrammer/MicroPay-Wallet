package com.docker.mswallet.messaging;

import com.docker.mswallet.config.RabbitConstants;
import com.docker.mswallet.dto.credit.WalletCreditFailedEvent;
import com.docker.mswallet.dto.credit.WalletCreditedEvent;
import com.docker.mswallet.dto.debit.WalletDebitFailedEvent;
import com.docker.mswallet.dto.debit.WalletDebitedEvent;
import com.docker.mswallet.dto.refund.WalletRefundedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final static Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    public MessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishWalletCreditedEvent(WalletCreditedEvent event) {
        logger.info("[MessagePublisher] - Publishing WalletCreditedEvent: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitConstants.WALLET_CREDIT_EXCHANGE,
                RabbitConstants.WALLET_CREDITED_ROUTING_KEY, event
        );
    }

    public void publishWalletCreditFailedEvent(WalletCreditFailedEvent event) {
        logger.info("[MessagePublisher] - Publishing WalletCreditFailedEvent: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitConstants.WALLET_CREDIT_EXCHANGE,
                RabbitConstants.WALLET_CREDIT_FAILED_ROUTING_KEY, event
        );
    }

    public void publishWalletDebitedEvent(WalletDebitedEvent event) {
        logger.info("[MessagePublisher] - Publishing WalletDebitedEvent: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitConstants.WALLET_DEBIT_EXCHANGE,
                RabbitConstants.WALLET_DEBITED_ROUTING_KEY, event
        );
    }

    public void publishWalletDebitFailedEvent(WalletDebitFailedEvent event) {
        logger.info("[MessagePublisher] - Publishing WalletDebitFailedEvent: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitConstants.WALLET_DEBIT_EXCHANGE,
                RabbitConstants.WALLET_DEBIT_FAILED_ROUTING_KEY,
                event);
    }

    public void publishWalletRefundedEvent(WalletRefundedEvent event) {
        logger.info("[MessagePublisher] - Publishing WalletRefundedEvent: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitConstants.WALLET_REFUND_EXCHANGE,
                RabbitConstants.WALLET_REFUNDED_ROUTING_KEY, event);
    }

}
