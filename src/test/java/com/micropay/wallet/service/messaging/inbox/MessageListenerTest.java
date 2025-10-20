package com.micropay.wallet.service.messaging.inbox;

import com.micropay.wallet.dto.credit.CreditWalletEvent;
import com.micropay.wallet.dto.debit.DebitWalletEvent;
import com.micropay.wallet.dto.refund.RefundWalletEvent;
import com.micropay.wallet.service.coordinator.CoordinatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

class MessageListenerTest {

    private CoordinatorServiceImpl coordinatorService;
    private InboxService inboxService;
    private MessageListener listener;
    private final static UUID EVENT_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        coordinatorService = mock(CoordinatorServiceImpl.class);
        inboxService = mock(InboxService.class);
        listener = new MessageListener(coordinatorService, inboxService);
    }

    @Test
    void listenToDebitWalletEvent_ShouldCallInboxAndHandleEvent() {
        DebitWalletEvent event = new DebitWalletEvent(EVENT_ID, 1L, 2L, null);

        listener.listenToDebitWalletEvent(event);

        verify(inboxService, times(1)).checkInbox(EVENT_ID);
        verify(coordinatorService, times(1)).handleDebitWalletEvent(event);
    }

    @Test
    void listenToDebitWalletEvent_ShouldSkipIfInboxThrows() {
        DebitWalletEvent event = new DebitWalletEvent(EVENT_ID, 1L, 2L, null);

        doThrow(new RuntimeException("Inbox error")).when(inboxService).checkInbox(EVENT_ID);

        listener.listenToDebitWalletEvent(event);

        verify(inboxService, times(1)).checkInbox(EVENT_ID);
        verify(coordinatorService, never()).handleDebitWalletEvent(any());
    }

    @Test
    void listenToCreditWalletEvent_ShouldCallInboxAndHandleEvent() {
        CreditWalletEvent event = new CreditWalletEvent(EVENT_ID, 1L, 2L, null);

        listener.listenToCreditWalletEvent(event);

        verify(inboxService, times(1)).checkInbox(EVENT_ID);
        verify(coordinatorService, times(1)).handleCreditWalletEvent(event);
    }

    @Test
    void listenToCreditWalletEvent_ShouldSkipIfInboxThrows() {
        CreditWalletEvent event = new CreditWalletEvent(EVENT_ID, 1L, 2L, null);

        doThrow(new RuntimeException("Inbox error")).when(inboxService).checkInbox(EVENT_ID);

        listener.listenToCreditWalletEvent(event);

        verify(inboxService, times(1)).checkInbox(EVENT_ID);
        verify(coordinatorService, never()).handleCreditWalletEvent(any());
    }

    @Test
    void listenToRefundWalletEvent_ShouldCallInboxAndHandleEvent() {
        RefundWalletEvent event = new RefundWalletEvent(EVENT_ID, 1L, 2L, null);

        listener.listenToRefundWalletEvent(event);

        verify(inboxService, times(1)).checkInbox(EVENT_ID);
        verify(coordinatorService, times(1)).handleRefundWalletEvent(event);
    }

    @Test
    void listenToRefundWalletEvent_ShouldSkipIfInboxThrows() {
        RefundWalletEvent event = new RefundWalletEvent(EVENT_ID, 1L, 2L, null);

        doThrow(new RuntimeException("Inbox error")).when(inboxService).checkInbox(EVENT_ID);

        listener.listenToRefundWalletEvent(event);

        verify(inboxService, times(1)).checkInbox(EVENT_ID);
        verify(coordinatorService, never()).handleRefundWalletEvent(any());
    }
}
