package com.micropay.wallet.service.coordinator;

import com.micropay.wallet.dto.credit.CreditWalletEvent;
import com.micropay.wallet.dto.credit.WalletCreditFailedEvent;
import com.micropay.wallet.dto.credit.WalletCreditedEvent;
import com.micropay.wallet.dto.debit.DebitWalletEvent;
import com.micropay.wallet.dto.debit.WalletDebitFailedEvent;
import com.micropay.wallet.dto.debit.WalletDebitedEvent;
import com.micropay.wallet.dto.refund.RefundWalletEvent;
import com.micropay.wallet.dto.refund.WalletRefundedEvent;
import com.micropay.wallet.exception.WalletException;
import com.micropay.wallet.mapper.EventMapper;
import com.micropay.wallet.service.messaging.outbox.MessageDispatcher;
import com.micropay.wallet.service.wallet.BalanceManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoordinatorServiceImpl implements CoordinatorService {

    private final BalanceManagementService balanceService;
    private final MessageDispatcher messageDispatcher;
    private final EventMapper eventMapper;

    @Override
    public void handleDebitWalletEvent(DebitWalletEvent event) {
        try {
            balanceService.debitBalance(event.walletId(), event.paymentId());
            WalletDebitedEvent walletDebitedEvent = eventMapper.mapToWalletDebitedEvent(event);

            messageDispatcher.publishWalletDebitedEvent(walletDebitedEvent);
        } catch (WalletException exception) {
            WalletDebitFailedEvent walletDebitedFailedEvent = eventMapper
                    .mapToWalletDebitFailedEvent(event, exception.getMessage());

            messageDispatcher.publishWalletDebitFailedEvent(walletDebitedFailedEvent);
        } catch (Exception exception) {
            logger.error("Unexpected error while handling DebitWalletEvent: {}", exception.getMessage());
        }
    }

    @Override
    public void handleCreditWalletEvent(CreditWalletEvent event) {
        try {
            balanceService.creditBalance(event.walletId(), event.amount());
            WalletCreditedEvent walletCreditedEvent = eventMapper.mapToWalletCreditedEvent(event);

            messageDispatcher.publishWalletCreditedEvent(walletCreditedEvent);
        } catch (WalletException exception) {
            WalletCreditFailedEvent walletCreditFailedEvent = eventMapper
                    .mapToWalletCreditFailedEvent(event, exception.getMessage());

            messageDispatcher.publishWalletCreditFailedEvent(walletCreditFailedEvent);
        } catch (Exception exception) {
            logger.error("Unexpected error while handling CreditWalletEvent: {}", exception.getMessage());
        }
    }

    @Override
    public void handleRefundWalletEvent(RefundWalletEvent event) {
        try {
            balanceService.creditBalance(event.walletId(), event.amount());
            WalletRefundedEvent walletRefundedEvent = eventMapper.mapToWalletRefundedEvent(event);

            messageDispatcher.publishWalletRefundedEvent(walletRefundedEvent);
        } catch (Exception exception) {
            logger.error("Unexpected error while handling RefundWalletEvent: {}", exception.getMessage());
        }
    }
}
