package com.github.egorkrylov.bookingrest.event;


import com.github.egorkrylov.bookingapicontract.dto.GuestResponse;
import com.github.egorkrylov.bookingeventcontract.EventEnvelope;
import com.github.egorkrylov.bookingeventcontract.GuestEvent;
import com.github.egorkrylov.bookingeventcontract.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;



@Component
public class GuestEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(GuestEventPublisher.class);
    private static final String SOURCE = "booking-rest";

    private final RabbitTemplate rabbitTemplate;

    public GuestEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(GuestResponse response) {
        var event = new GuestEvent.Created(
                response.getFirstName(),
                response.getLastName(),
                response.getEmail(),
                response.getBirthDate()
        );
        send(RoutingKeys.GUEST_CREATED, event);

    }

    public void publishDeleted(GuestResponse response) {
        var event = new GuestEvent.Deleted(
                response.getFirstName(),
                response.getLastName(),
                response.getEmail()
        );

        send(RoutingKeys.GUEST_DELETED, event);
    }

    public void publishUpdated(GuestResponse response) {
        var event = new GuestEvent.Updated(
                response.getFirstName(),
                response.getLastName(),
                response.getEmail(),
                response.getBirthDate()
        );

        send(RoutingKeys.GUEST_UPDATED, event);
    }



    private void send(String routingKey, GuestEvent event) {
        try {
            EventEnvelope<GuestEvent> eventEnvelope = EventEnvelope.wrap(event, SOURCE, routingKey);

            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, routingKey, eventEnvelope);
            log.info("Событие отправлено: {} [eventId={}]", routingKey, eventEnvelope.metadata().eventId());
        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}", routingKey, e.getMessage());
        }
    }
}
