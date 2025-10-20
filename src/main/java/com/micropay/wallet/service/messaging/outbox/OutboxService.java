package com.micropay.wallet.service.messaging.outbox;


import com.micropay.wallet.model.entity.BaseEvent;

public interface OutboxService {

    <T> BaseEvent saveBaseEvent(String eventType, String exchangeName, String routingKey, T payload);

    void publish(BaseEvent event);

}
