package com.micropay.wallet.service.messaging.inbox;

import com.micropay.wallet.config.RabbitConstants;
import com.micropay.wallet.dto.credit.CreditWalletEvent;
import com.micropay.wallet.dto.debit.DebitWalletEvent;
import com.micropay.wallet.dto.refund.RefundWalletEvent;
import com.micropay.wallet.service.coordinator.CoordinatorServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

    private final CoordinatorServiceImpl messagingService;
    private final InboxService inboxService;

    @RabbitListener(queues = RabbitConstants.WALLET_DEBIT_QUEUE)
    public void listenToDebitWalletEvent(DebitWalletEvent event) {
        log.info("Listening to DebitWalletEvent: {}", event);

        try {
            inboxService.checkInbox(event.eventId());
        }  catch (Exception exception) { return; }
        messagingService.handleDebitWalletEvent(event);
    }

    @RabbitListener(queues = RabbitConstants.WALLET_CREDIT_QUEUE)
    public void listenToCreditWalletEvent(CreditWalletEvent event) {
        log.info("Listening to CreditWalletEvent: {}", event);

        try {
            inboxService.checkInbox(event.eventId());
        }  catch (Exception exception) { return; }
        messagingService.handleCreditWalletEvent(event);
    }

    @RabbitListener(queues = RabbitConstants.WALLET_REFUND_QUEUE)
    public void listenToRefundWalletEvent(RefundWalletEvent event) {
        log.info("Listening to RefundWalletEvent: {}", event);

        try {
            inboxService.checkInbox(event.eventId());
        }  catch (Exception exception) { return; }
        messagingService.handleRefundWalletEvent(event);
    }

}
