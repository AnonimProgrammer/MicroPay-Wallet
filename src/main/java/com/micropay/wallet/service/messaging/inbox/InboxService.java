package com.micropay.wallet.service.messaging.inbox;

import java.util.UUID;

public interface InboxService {

    void checkInbox(UUID eventId);
}
