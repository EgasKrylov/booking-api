package com.github.egorkrylov.bookingrest.event;

import com.github.egorkrylov.bookingapicontract.dto.BookingResponse;
import com.github.egorkrylov.bookingeventcontract.BookingEvent;
import com.github.egorkrylov.bookingeventcontract.EventEnvelope;
import com.github.egorkrylov.bookingeventcontract.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingEventPublisher {

    private final Logger log = LoggerFactory.getLogger(BookingEventPublisher.class);
    private final String SOURCE = "booking-rest";

    private final RabbitTemplate rabbitTemplate;

    public BookingEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(BookingResponse response) {
        var event = new BookingEvent.Created(
                response.getCheckInDate(),
                response.getCheckOutDate(),
                response.getGuestCount()
        );

        send(RoutingKeys.BOOKING_CREATED, event);
    }

    public void publishUpdated(BookingResponse response) {
        var event = new BookingEvent.Updated(
                response.getCheckInDate(),
                response.getCheckOutDate(),
                response.getGuestCount()
        );

        send(RoutingKeys.BOOKING_UPDATED, event);
    }

    public void publishDeleted(BookingResponse response) {
        var event = new BookingEvent.Deleted(
                response.getCheckInDate(),
                response.getCheckOutDate()
        );

        send(RoutingKeys.BOOKING_DELETED, event);
    }

    private void send(String routingKey, BookingEvent event) {
        try {
            EventEnvelope<BookingEvent> eventEnvelope = EventEnvelope.wrap(event, SOURCE, routingKey);
            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, routingKey, eventEnvelope);
            log.info("Событие отправлено: {} [eventId={}]", routingKey, eventEnvelope.metadata().eventId());
        } catch (Exception ex) {
            log.error("Не удалось отправить событие {}: {}", routingKey, ex.getMessage());
        }
    }
}
