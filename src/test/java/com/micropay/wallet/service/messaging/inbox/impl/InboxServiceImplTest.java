package com.micropay.wallet.service.messaging.inbox.impl;

import com.micropay.wallet.model.entity.InboxEntity;
import com.micropay.wallet.repo.InboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class InboxServiceImplTest {

    @Mock
    private InboxRepository inboxRepository;

    @InjectMocks
    private InboxServiceImpl inboxService;

    private final static UUID EVENT_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveNewEventWhenNotPresent() {
        when(inboxRepository.findById(EVENT_ID)).thenReturn(Optional.empty());

        inboxService.checkInbox(EVENT_ID);

        ArgumentCaptor<InboxEntity> inboxCaptor = ArgumentCaptor.forClass(InboxEntity.class);
        verify(inboxRepository).findById(EVENT_ID);
        verify(inboxRepository).save(inboxCaptor.capture());

        InboxEntity savedEntity = inboxCaptor.getValue();
        assertEquals(EVENT_ID, savedEntity.getEventId());
    }

    @Test
    void shouldThrowExceptionWhenEventAlreadyExists() {
        InboxEntity existingInbox = new InboxEntity();
        existingInbox.setEventId(EVENT_ID);
        when(inboxRepository.findById(EVENT_ID)).thenReturn(Optional.of(existingInbox));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> inboxService.checkInbox(EVENT_ID));
        assertEquals("Inbox already contains such event.", exception.getMessage());

        verify(inboxRepository, never()).save(any());
    }

}
