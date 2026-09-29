package com.github.egorkrylov.bookingrest.event;

import com.github.egorkrylov.bookingapicontract.dto.RoomResponse;
import com.github.egorkrylov.bookingeventcontract.EventEnvelope;
import com.github.egorkrylov.bookingeventcontract.RoomEvent;
import com.github.egorkrylov.bookingeventcontract.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RoomEventPublisher {

    private final Logger log = LoggerFactory.getLogger(RoomEventPublisher.class);
    private final String SOURCE = "booking-rest";

    private final RabbitTemplate rabbitTemplate;

    public RoomEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(RoomResponse response) {
        var event = new RoomEvent.Created(
                response.getId(),
                response.getRoomNumber(),
                response.getRoomType(),
                response.getCapacity(),
                response.getPrice()
        );

        send(RoutingKeys.ROOM_CREATED, event);
    }

    public void publishUpdated(RoomResponse response) {
        var event = new RoomEvent.Updated(
                response.getRoomNumber(),
                response.getRoomType(),
                response.getCapacity(),
                response.getPrice()
        );

        send(RoutingKeys.ROOM_UPDATED, event);
    }

    public void publishDeleted(RoomResponse response) {
        var event = new RoomEvent.Deleted(
                response.getRoomNumber(),
                response.getRoomType()
        );

        send(RoutingKeys.ROOM_DELETED, event);
    }

    private void send(String routingKey, RoomEvent event) {
        try {
            EventEnvelope<RoomEvent> eventEnvelope = EventEnvelope.wrap(event, SOURCE, routingKey);
            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, routingKey, eventEnvelope);
            log.info("Событие отправлено: {} [eventId={}]", routingKey, eventEnvelope.metadata().eventId());
        } catch (Exception ex) {
            log.error("Не удалось отправить событие {}: {}", routingKey, ex.getMessage());
        }
    }



}
