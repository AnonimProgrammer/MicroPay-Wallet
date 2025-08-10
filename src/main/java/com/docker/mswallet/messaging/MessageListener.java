package com.docker.mswallet.messaging;

import com.docker.mswallet.config.RabbitConstants;
import com.docker.mswallet.dto.credit.CreditWalletEvent;
import com.docker.mswallet.dto.debit.DebitWalletEvent;
import com.docker.mswallet.dto.refund.RefundWalletEvent;
import com.docker.mswallet.service.impl.MessagingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private final MessagingServiceImpl messagingService;
    private final static Logger logger = LoggerFactory.getLogger(MessageListener.class);

    public MessageListener(MessagingServiceImpl messagingService) {
        this.messagingService = messagingService;
    }

    @RabbitListener(queues = RabbitConstants.WALLET_DEBIT_QUEUE)
    public void listenToDebitWalletEvent(DebitWalletEvent debitWalletEvent) {
        logger.info("[MessageListener] - Listening to DebitWalletEvent: {}", debitWalletEvent);
        messagingService.handleDebitWalletEvent(debitWalletEvent);
    }

    @RabbitListener(queues = RabbitConstants.WALLET_CREDIT_QUEUE)
    public void listenToCreditWalletEvent(CreditWalletEvent creditWalletEvent) {
        logger.info("[MessageListener] - Listening to CreditWalletEvent: {}", creditWalletEvent);
        messagingService.handleCreditWalletEvent(creditWalletEvent);
    }

    @RabbitListener(queues = RabbitConstants.WALLET_REFUND_QUEUE)
    public void listenToRefundWalletEvent(RefundWalletEvent refundWalletEvent) {
        logger.info("[MessageListener] - Listening to RefundWalletEvent: {}", refundWalletEvent);
        messagingService.handleRefundWalletEvent(refundWalletEvent);
    }

}
