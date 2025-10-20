package com.micropay.wallet.service.coordinator;

import com.micropay.wallet.dto.credit.CreditWalletEvent;
import com.micropay.wallet.dto.credit.WalletCreditFailedEvent;
import com.micropay.wallet.dto.credit.WalletCreditedEvent;
import com.micropay.wallet.dto.debit.DebitWalletEvent;
import com.micropay.wallet.dto.debit.WalletDebitFailedEvent;
import com.micropay.wallet.dto.debit.WalletDebitedEvent;
import com.micropay.wallet.dto.refund.RefundWalletEvent;
import com.micropay.wallet.dto.refund.WalletRefundedEvent;
import com.micropay.wallet.exception.InsufficientBalanceException;
import com.micropay.wallet.exception.WalletNotFoundException;
import com.micropay.wallet.mapper.EventMapper;
import com.micropay.wallet.service.messaging.outbox.MessageDispatcher;
import com.micropay.wallet.service.wallet.BalanceManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;

class CoordinatorServiceImplTest {

    private BalanceManagementService balanceService;
    private MessageDispatcher messageDispatcher;
    private EventMapper eventMapper;
    private CoordinatorServiceImpl messagingService;

    private final static UUID EVENT_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        balanceService = mock(BalanceManagementService.class);
        messageDispatcher = mock(MessageDispatcher.class);
        eventMapper = mock(EventMapper.class);

        messagingService = new CoordinatorServiceImpl(balanceService, messageDispatcher, eventMapper);
    }

    @Test
    void handleDebitWalletEvent_ShouldDebitAndPublishEvent() {
        DebitWalletEvent event = new DebitWalletEvent(
                EVENT_ID, 1L, 100L, BigDecimal.valueOf(50));
        WalletDebitedEvent debitedEvent = new WalletDebitedEvent(
                EVENT_ID, 1L, 100L, BigDecimal.valueOf(50));

        doNothing().when(balanceService).debitBalance(event.walletId(), event.paymentId());
        when(eventMapper.mapToWalletDebitedEvent(event)).thenReturn(debitedEvent);

        messagingService.handleDebitWalletEvent(event);

        verify(balanceService).debitBalance(event.walletId(), event.paymentId());
        verify(eventMapper).mapToWalletDebitedEvent(event);
        verify(messageDispatcher).publishWalletDebitedEvent(debitedEvent);
    }

    @Test
    void handleDebitWalletEvent_ShouldPublishFailedEventOnWalletException() {
        DebitWalletEvent event = new DebitWalletEvent(
                EVENT_ID, 1L, 100L, BigDecimal.valueOf(50));
        WalletDebitFailedEvent failedEvent = new WalletDebitFailedEvent(
                EVENT_ID, 1L, 100L, "Insufficient funds");

        doThrow(new InsufficientBalanceException("Insufficient funds")).when(balanceService)
                .debitBalance(event.walletId(), event.paymentId());
        when(eventMapper.mapToWalletDebitFailedEvent(event, "Insufficient funds")).thenReturn(failedEvent);

        messagingService.handleDebitWalletEvent(event);

        verify(balanceService).debitBalance(event.walletId(), event.paymentId());
        verify(eventMapper).mapToWalletDebitFailedEvent(event, "Insufficient funds");
        verify(messageDispatcher).publishWalletDebitFailedEvent(failedEvent);
    }

    @Test
    void handleCreditWalletEvent_ShouldCreditAndPublishEvent() {
        CreditWalletEvent event = new CreditWalletEvent(
                EVENT_ID,1L, 100L, BigDecimal.valueOf(50));
        WalletCreditedEvent creditedEvent = new WalletCreditedEvent(
                EVENT_ID, 1L, 100L, BigDecimal.valueOf(50));

        doNothing().when(balanceService).creditBalance(event.walletId(), event.amount());
        when(eventMapper.mapToWalletCreditedEvent(event)).thenReturn(creditedEvent);

        messagingService.handleCreditWalletEvent(event);

        verify(balanceService).creditBalance(event.walletId(), event.amount());
        verify(eventMapper).mapToWalletCreditedEvent(event);
        verify(messageDispatcher).publishWalletCreditedEvent(creditedEvent);
    }

    @Test
    void handleCreditWalletEvent_ShouldPublishFailedEventOnWalletException() {
        CreditWalletEvent event = new CreditWalletEvent(
                EVENT_ID, 1L, 100L, BigDecimal.valueOf(50));
        WalletCreditFailedEvent failedEvent = new WalletCreditFailedEvent(
                EVENT_ID, 1L, 2L, "Error");

        doThrow(new WalletNotFoundException("Error")).when(balanceService)
                .creditBalance(event.walletId(), event.amount());
        when(eventMapper.mapToWalletCreditFailedEvent(event, "Error")).thenReturn(failedEvent);

        messagingService.handleCreditWalletEvent(event);

        verify(balanceService).creditBalance(event.walletId(), event.amount());
        verify(eventMapper).mapToWalletCreditFailedEvent(event, "Error");
        verify(messageDispatcher).publishWalletCreditFailedEvent(failedEvent);
    }

    @Test
    void handleRefundWalletEvent_ShouldCreditAndPublishRefundedEvent() {
        RefundWalletEvent event = new RefundWalletEvent(
                EVENT_ID, 1L, 100L, BigDecimal.valueOf(50));
        WalletRefundedEvent refundedEvent = new WalletRefundedEvent(
                EVENT_ID, 1L, 100L, BigDecimal.valueOf(50));

        doNothing().when(balanceService).creditBalance(event.walletId(), event.amount());
        when(eventMapper.mapToWalletRefundedEvent(event)).thenReturn(refundedEvent);

        messagingService.handleRefundWalletEvent(event);

        verify(balanceService).creditBalance(event.walletId(), event.amount());
        verify(eventMapper).mapToWalletRefundedEvent(event);
        verify(messageDispatcher).publishWalletRefundedEvent(refundedEvent);
    }
}
