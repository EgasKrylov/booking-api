package com.github.egorkrylov.grpcenrichment.listener;

import com.github.egorkrylov.bookingeventcontract.EventMetadata;
import com.github.egorkrylov.bookingeventcontract.RoomEvent;
import com.github.egorkrylov.grpc.AnalyzeRoomRequest;
import com.github.egorkrylov.grpc.AnalyzeRoomResponse;
import com.github.egorkrylov.grpc.RoomAnalyticsGrpc;
import com.github.egorkrylov.grpcenrichment.publisher.EnrichmentEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class RoomCreatedListener {

    private final Logger log = LoggerFactory.getLogger(RoomCreatedListener.class);

    private final RoomAnalyticsGrpc.RoomAnalyticsBlockingStub analyticsStub;
    private final EnrichmentEventPublisher enrichmentPublisher;
    private final JsonMapper jsonMapper;

    public RoomCreatedListener(RoomAnalyticsGrpc.RoomAnalyticsBlockingStub analyticsStub,
                               EnrichmentEventPublisher enrichmentPublisher, JsonMapper jsonMapper
    ) {
        this.analyticsStub = analyticsStub;
        this.enrichmentPublisher = enrichmentPublisher;
        this.jsonMapper = jsonMapper;
    }


    @RabbitListener(queues = "q.enrichment.room-created")
    public void handleRoomCreated(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            JsonNode payloadNode = root.get("payload");
            RoomEvent.Created roomCreated = jsonMapper.treeToValue(payloadNode, RoomEvent.Created.class);

            log.info("Получено событие room.created: roomId={}, номер комнаты=«{}» [eventId={}]",
                    roomCreated.roomId(), roomCreated.roomNumber(), metadata.eventId());

            AnalyzeRoomRequest analyzeRoomRequest = AnalyzeRoomRequest.newBuilder()
                    .setRoomId(roomCreated.roomId().toString())
                    .setRoomType(roomCreated.roomType())
                    .setCapacity(roomCreated.capacity())
                    .setPrice(roomCreated.price().doubleValue())
                    .build();

            log.info("Вызов gRPC: RoomAnalytics.AnalyzeRoom(roomId={})", roomCreated.roomId());
            AnalyzeRoomResponse analyzeRoomResponse = analyticsStub.analyzeRoom(analyzeRoomRequest);

            log.info("gRPC ответ получен: roomId={}, итоговая цена={} руб, классификация={}, цена со скидкой={}",
                    analyzeRoomResponse.getRoomId(),
                    analyzeRoomResponse.getFinalPrice(),
                    analyzeRoomResponse.getPriceCategory(),
                    analyzeRoomResponse.getDiscountPrice());

            RoomEvent.Enriched enrichedEvent = new RoomEvent.Enriched(
                    UUID.fromString(analyzeRoomResponse.getRoomId()),
                    roomCreated.roomNumber(),
                    BigDecimal.valueOf(analyzeRoomResponse.getFinalPrice()),
                    analyzeRoomResponse.getPriceCategory(),
                    BigDecimal.valueOf(analyzeRoomResponse.getDiscountPrice())
            );

            enrichmentPublisher.publishEnriched(enrichedEvent);

            log.info("Комната обогащена: roomId={}, номер комнаты={} room.enriched отправлено",
                    roomCreated.roomId(), roomCreated.roomNumber());
        } catch (io.grpc.StatusRuntimeException e) {
            log.error("gRPC ошибка при обогащении комнаты: {} ({})",
                    e.getStatus().getDescription(), e.getStatus().getCode());
            throw new RuntimeException("gRPC-вызов завершился ошибкой", e);

        } catch (Exception e) {
            log.error("Ошибка обработки события room.created: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие room.created", e);
        }
    }

}
