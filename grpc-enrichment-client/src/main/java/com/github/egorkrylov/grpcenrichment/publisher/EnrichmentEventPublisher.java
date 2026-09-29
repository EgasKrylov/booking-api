package com.github.egorkrylov.grpcenrichment.publisher;

import com.github.egorkrylov.bookingeventcontract.EventEnvelope;
import com.github.egorkrylov.bookingeventcontract.RoomEvent;
import com.github.egorkrylov.bookingeventcontract.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EnrichmentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EnrichmentEventPublisher.class);
    private static final String SOURCE = "grpc-enrichment-client";

    private final RabbitTemplate rabbitTemplate;

    public EnrichmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEnriched(RoomEvent.Enriched enrichedEvent) {
        try {
            EventEnvelope<RoomEvent> envelope = EventEnvelope.wrap(
                    enrichedEvent,
                    SOURCE,
                    RoutingKeys.ROOM_ENRICHED
            );

            rabbitTemplate.convertAndSend(
                    RoutingKeys.EXCHANGE,
                    RoutingKeys.ROOM_ENRICHED,
                    envelope);

            log.info("Событие отправлено: {} [bookId={}, eventId={}]",
                    RoutingKeys.ROOM_ENRICHED,
                    enrichedEvent.roomId(),
                    envelope.metadata().eventId());

        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}",
                    RoutingKeys.ROOM_ENRICHED, e.getMessage());
        }
    }
}
