package com.micropay.wallet.service.impl;

import com.micropay.wallet.dto.credit.CreditWalletEvent;
import com.micropay.wallet.dto.credit.WalletCreditFailedEvent;
import com.micropay.wallet.dto.credit.WalletCreditedEvent;
import com.micropay.wallet.dto.debit.DebitWalletEvent;
import com.micropay.wallet.dto.debit.WalletDebitFailedEvent;
import com.micropay.wallet.dto.debit.WalletDebitedEvent;
import com.micropay.wallet.dto.refund.RefundWalletEvent;
import com.micropay.wallet.dto.refund.WalletRefundedEvent;
import com.micropay.wallet.exception.WalletException;
import com.micropay.wallet.messaging.MessagePublisher;
import com.micropay.wallet.service.MessagingService;
import com.micropay.wallet.service.WalletDataAccessService;
import com.micropay.wallet.util.EventFactory;
import org.springframework.stereotype.Service;

@Service
public class MessagingServiceImpl implements MessagingService {

    private final WalletDataAccessService walletService;
    private final MessagePublisher messagePublisher;
    private final EventFactory eventFactory;

    public MessagingServiceImpl(WalletDataAccessService walletService, MessagePublisher messagePublisher, EventFactory eventFactory) {
        this.walletService = walletService;
        this.messagePublisher = messagePublisher;
        this.eventFactory = eventFactory;
    }

    @Override
    public void handleDebitWalletEvent(DebitWalletEvent event) {
        Long userId = walletService.getUserIdByWalletId(event.getWalletId());
        try {
            walletService.debitBalance(event.getWalletId(), event.getPaymentId());
            WalletDebitedEvent walletDebitedEvent = eventFactory.createWalletDebitedEvent(
                    event.getWalletId(),
                    userId,
                    event.getAmount(),
                    event.getPaymentId()
            );
            messagePublisher.publishWalletDebitedEvent(walletDebitedEvent);
        } catch (WalletException exception) {
            WalletDebitFailedEvent walletDebitedFailedEvent = eventFactory.createWalletDebitFailedEvent(
                    event.getWalletId(),
                    userId,
                    event.getPaymentId(),
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
                    event.getPaymentId()
            );
            messagePublisher.publishWalletCreditedEvent(walletCreditedEvent);
        } catch (WalletException exception) {
            WalletCreditFailedEvent walletCreditFailedEvent = eventFactory.createWalletCreditFailedEvent(
                    event.getWalletId(),
                    event.getPaymentId(),
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
                    event.getPaymentId()
            );
            messagePublisher.publishWalletRefundedEvent(walletRefundedEvent);
        } catch (Exception exception) {
            logger.error("Unexpected error while handling RefundWalletEvent: {}", exception.getMessage());
        }
    }
}
