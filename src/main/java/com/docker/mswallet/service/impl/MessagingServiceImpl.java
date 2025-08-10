package com.docker.mswallet.service.impl;

import com.docker.mswallet.dto.credit.CreditWalletEvent;
import com.docker.mswallet.dto.credit.WalletCreditFailedEvent;
import com.docker.mswallet.dto.credit.WalletCreditedEvent;
import com.docker.mswallet.dto.debit.DebitWalletEvent;
import com.docker.mswallet.dto.debit.WalletDebitFailedEvent;
import com.docker.mswallet.dto.debit.WalletDebitedEvent;
import com.docker.mswallet.dto.refund.RefundWalletEvent;
import com.docker.mswallet.dto.refund.WalletRefundedEvent;
import com.docker.mswallet.exception.WalletException;
import com.docker.mswallet.messaging.MessagePublisher;
import com.docker.mswallet.service.MessagingService;
import com.docker.mswallet.service.WalletService;
import com.docker.mswallet.util.EventFactory;
import org.springframework.stereotype.Service;

@Service
public class MessagingServiceImpl implements MessagingService {

    private final WalletService walletService;
    private final MessagePublisher messagePublisher;
    private final EventFactory eventFactory;

    public MessagingServiceImpl(WalletService walletService, MessagePublisher messagePublisher, EventFactory eventFactory) {
        this.walletService = walletService;
        this.messagePublisher = messagePublisher;
        this.eventFactory = eventFactory;
    }

    @Override
    public void handleDebitWalletEvent(DebitWalletEvent event) {
        try {
            walletService.debitBalance(event.getWalletId(), event.getAmount());
            WalletDebitedEvent walletDebitedEvent = eventFactory.createWalletDebitedEvent(
                    event.getWalletId(),
                    event.getAmount(),
                    event.getTransactionId()
            );
            messagePublisher.publishWalletDebitedEvent(walletDebitedEvent);
        } catch (WalletException exception) {
            WalletDebitFailedEvent walletDebitedFailedEvent = eventFactory.createWalletDebitFailedEvent(
                    event.getWalletId(),
                    event.getTransactionId(),
                    exception.getMessage()
            );
            messagePublisher.publishWalletDebitFailedEvent(walletDebitedFailedEvent);
        } catch (Exception exception) {
            logger.error("Unexpected error while handling DebitWalletEvent: {}", exception.getMessage());
        }
    }

    @Override
    public void handleCreditWalletEvent(CreditWalletEvent event) {
        try {
            walletService.creditBalance(event.getWalletId(), event.getAmount());
            Long userId = walletService.getUserIdByWalletId(event.getWalletId());
            WalletCreditedEvent walletCreditedEvent = eventFactory.createWalletCreditedEvent(
                    event.getWalletId(),
                    userId,
                    event.getAmount(),
                    event.getTransactionId()
            );
            messagePublisher.publishWalletCreditedEvent(walletCreditedEvent);
        } catch (WalletException exception) {
            WalletCreditFailedEvent walletCreditFailedEvent = eventFactory.createWalletCreditFailedEvent(
                    event.getWalletId(),
                    event.getTransactionId(),
                    exception.getMessage()
            );
            messagePublisher.publishWalletCreditFailedEvent(walletCreditFailedEvent);
        } catch (Exception exception) {
            logger.error("Unexpected error while handling CreditWalletEvent: {}", exception.getMessage());
        }
    }

    @Override
    public void handleRefundWalletEvent(RefundWalletEvent event) {
        try {
            walletService.creditBalance(event.getWalletId(), event.getAmount());
            Long userId = walletService.getUserIdByWalletId(event.getWalletId());
            WalletRefundedEvent walletRefundedEvent = eventFactory.createWalletRefundedEvent(
                    event.getWalletId(),
                    userId,
                    event.getAmount(),
                    event.getTransactionId()
            );
            messagePublisher.publishWalletRefundedEvent(walletRefundedEvent);
        } catch (Exception exception) {
            logger.error("Unexpected error while handling RefundWalletEvent: {}", exception.getMessage());
        }
    }
}
