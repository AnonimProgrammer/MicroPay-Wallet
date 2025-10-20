package com.micropay.wallet.service.messaging.outbox.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micropay.wallet.dto.debit.WalletDebitedEvent;
import com.micropay.wallet.model.EventStatus;
import com.micropay.wallet.model.entity.BaseEvent;
import com.micropay.wallet.repo.BaseEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class OutboxServiceImplTest {

    @Mock
    private BaseEventRepository baseEventRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private OutboxServiceImpl outboxService;

    private final static String EVENT_TYPE = "TEST_EVENT";
    private final static String EXCHANGE = "EXCHANGE";
    private final static String ROUTING_KEY = "ROUTING_KEY";
    private WalletDebitedEvent event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.event = mock(WalletDebitedEvent.class);
    }

    @Test
    void shouldSaveBaseEventSuccessfully() {
        BaseEvent savedEvent = new BaseEvent();
        when(baseEventRepository.save(any(BaseEvent.class))).thenReturn(savedEvent);

        BaseEvent result = outboxService.saveBaseEvent(EVENT_TYPE, EXCHANGE, ROUTING_KEY, event);

        assertNotNull(result);
        ArgumentCaptor<BaseEvent> captor = ArgumentCaptor.forClass(BaseEvent.class);
        verify(baseEventRepository).save(captor.capture());

        BaseEvent actual = captor.getValue();
        assertEquals(EVENT_TYPE, actual.getEventType());
        assertEquals(EXCHANGE, actual.getExchangeName());
        assertEquals(ROUTING_KEY, actual.getRoutingKey());
    }

    @Test
    void shouldPublishEventSuccessfully() throws Exception {
        BaseEvent event = new BaseEvent();
        event.setExchangeName(EXCHANGE);
        event.setRoutingKey(ROUTING_KEY);
        event.setPayload(this.event);
        event.setEventType(EVENT_TYPE);

        when(mapper.readValue(event.getPayload(), Object.class)).thenReturn(new Object());

        outboxService.publish(event);

        assertEquals(EventStatus.SENT, event.getStatus());
        verify(rabbitTemplate).convertAndSend(eq(EXCHANGE), eq(ROUTING_KEY), any(Object.class));
        verify(baseEventRepository).save(event);
    }

    @Test
    void shouldSetRetryingStatusWhenPublishFails() throws Exception {
        BaseEvent event = new BaseEvent();
        event.setExchangeName(EXCHANGE);
        event.setRoutingKey(ROUTING_KEY);
        event.setPayload(this.event);
        event.setRetryCount(2);
        doThrow(new RuntimeException("fail")).when(mapper).readValue(anyString(), eq(Object.class));

        outboxService.publish(event);

        assertEquals(EventStatus.RETRYING, event.getStatus());
        assertEquals(3, event.getRetryCount());
        verify(baseEventRepository).save(event);
    }

    @Test
    void shouldSetFailedStatusWhenMaxRetriesExceeded() throws Exception {
        BaseEvent event = new BaseEvent();
        event.setExchangeName(EXCHANGE);
        event.setRoutingKey(ROUTING_KEY);
        event.setPayload(this.event);
        event.setRetryCount(5);
        doThrow(new RuntimeException("fail")).when(mapper).readValue(anyString(), eq(Object.class));

        outboxService.publish(event);

        assertEquals(EventStatus.FAILED, event.getStatus());
        assertEquals(6, event.getRetryCount());
        verify(baseEventRepository).save(event);
    }

    @Test
    void shouldDoNothingWhenNoPendingEvents() {
        when(baseEventRepository.findPendingEvents()).thenReturn(Collections.emptyList());

        outboxService.retryPendingEvents();

        verify(baseEventRepository, never()).save(any());
        verifyNoInteractions(rabbitTemplate);
    }

    @Test
    void shouldRetryPendingEvents() {
        BaseEvent event1 = new BaseEvent();
        BaseEvent event2 = new BaseEvent();
        when(baseEventRepository.findPendingEvents()).thenReturn(List.of(event1, event2));

        OutboxServiceImpl spyService = Mockito.spy(outboxService);
        doNothing().when(spyService).publish(any(BaseEvent.class));

        spyService.retryPendingEvents();

        verify(spyService, times(2)).publish(any(BaseEvent.class));
    }
}
