package com.github.egorkrylov.notificationservice.listener;

import com.github.egorkrylov.bookingeventcontract.BookingEvent;
import com.github.egorkrylov.bookingeventcontract.EventMetadata;
import com.github.egorkrylov.bookingeventcontract.GuestEvent;
import com.github.egorkrylov.bookingeventcontract.RoomEvent;
import com.github.egorkrylov.notificationservice.websocket.NotificationWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventNotificationListener {

    private final Logger log = LoggerFactory.getLogger(EventNotificationListener.class);
    private final Set<String> processedEventIds = ConcurrentHashMap.newKeySet();
    private final NotificationWebSocketHandler webSocketHandler;
    private final JsonMapper mapper;

    public EventNotificationListener(JsonMapper mapper, NotificationWebSocketHandler webSocketHandler) {
        this.mapper = mapper;
        this.webSocketHandler = webSocketHandler;
    }

    @RabbitListener(queues = "q.notifications.all", messageConverter = "")
    public void handleEvent(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode node = mapper.readTree(body);
            JsonNode metadata = node.get("metadata");
            EventMetadata eventMetadata = mapper.treeToValue(metadata, EventMetadata.class);
            if(!processedEventIds.add(eventMetadata.eventId())) {
                log.warn("Дубликат уведомления пропущен: eventId={}", eventMetadata.eventId());
                return;
            }
            JsonNode payload = node.get("payload");
            String title = buildTitle(eventMetadata.eventType());
            String description = buildDescription(payload, eventMetadata.eventType());
            String icon = resolveIcon(eventMetadata.eventType());
            String level = resolveLevel(eventMetadata.eventType());

            String notificationJson = mapper.writeValueAsString(
                    new NotificationPayload(
                            "NOTIFICATION",
                            eventMetadata.eventId(),
                            eventMetadata.eventType(),
                            title,
                            description,
                            icon,
                            level,
                            eventMetadata.source(),
                            eventMetadata.timestamp().toString(),
                            Instant.now().toString()
                    )
            );

            webSocketHandler.broadcast(notificationJson);

            log.info("[NOTIFY] {} | {} (клиентов: {})",
                    eventMetadata.eventType(), description, webSocketHandler.getActiveConnectionCount());

        } catch (Exception ex) {
            log.error("Ошибка обработки события для уведомлений: {}", ex.getMessage(), ex);
            throw new RuntimeException("Не удалось обработать событие", ex);
        }
    }

    private String buildTitle(String type) {
        return switch (type) {
            case "guest.created" -> "Новый гость";
            case "guest.updated" -> "Гость обновлен";
            case "guest.deleted" -> "Гость удален";
            case "room.created" -> "Новая комната";
            case "room.updated" -> "Комната обновлена";
            case "room.deleted" -> "Комната удалена";
            case "room.enriched" -> "Аналитика комнаты";
            case "booking.created" -> "Бронирование создано";
            case "booking.updated" -> "Бронирование обновлено";
            case "booking.deleted" -> "Бронирование отменено";
            default -> "Событие: " + type;
        };
    }

    private String buildDescription(JsonNode node, String type) {
        try {
            return switch (type) {
                case "guest.created" -> {
                    GuestEvent.Created e = mapper.treeToValue(node, GuestEvent.Created.class);
                    yield "Создан гость «%s %s» (email: %s, дата рождения: %s)"
                            .formatted(e.firstName(), e.lastName(), e.email(), e.birthDate());
                }

                case "guest.updated" -> {
                    GuestEvent.Updated e = mapper.treeToValue(node, GuestEvent.Updated.class);
                    yield "Обновлен гость «%s %s» (email: %s, дата рождения: %s)"
                            .formatted(e.firstName(), e.lastName(), e.email(), e.birthDate());
                }

                case "guest.deleted" -> {
                    GuestEvent.Deleted e = mapper.treeToValue(node, GuestEvent.Deleted.class);
                    yield "Удален гость «%s %s» (email: %s)".formatted(
                            e.firstName(), e.lastName(), e.email());
                }

                case "room.created" -> {
                    RoomEvent.Created e = mapper.treeToValue(node, RoomEvent.Created.class);
                    yield "Создана комната №%s (тип: %s, вместимость: %d, цена: %.2f)".formatted(
                            e.roomNumber(), e.roomType(), e.capacity(), e.price());
                }

                case "room.updated" -> {
                    RoomEvent.Updated e = mapper.treeToValue(node, RoomEvent.Updated.class);
                    yield "Обновлена комната №%s (тип: %s, вместимость: %d, цена: %.2f)".formatted(
                            e.roomNumber(), e.roomType(), e.capacity(), e.price());
                }

                case "room.deleted" -> {
                    RoomEvent.Deleted e = mapper.treeToValue(node, RoomEvent.Deleted.class);
                    yield "Удалена комната №%s (тип: %s)".formatted(
                            e.roomNumber(), e.roomType());
                }

                case "room.enriched" -> {
                    RoomEvent.Enriched e = mapper.treeToValue(node, RoomEvent.Enriched.class);
                    yield "Обогащена комната №%s (итоговая цена: %.2f, категория: %s, цена со скидкой: %.2f)".formatted(
                            e.roomNumber(), e.finalPrice(), e.priceCategory(), e.discountPrice());
                }

                case "booking.created" -> {
                    BookingEvent.Created e = mapper.treeToValue(node, BookingEvent.Created.class);
                    yield "Создано бронирование (заезд: %s, выезд: %s, гостей: %d)".formatted(
                            e.checkInDate(), e.checkOutDate(), e.guestCount());
                }

                case "booking.updated" -> {
                    BookingEvent.Updated e = mapper.treeToValue(node, BookingEvent.Updated.class);
                    yield "Обновлено бронирование (заезд: %s, выезд: %s, гостей: %d)".formatted(
                            e.checkInDate(), e.checkOutDate(), e.guestCount());
                }

                case "booking.deleted" -> {
                    BookingEvent.Deleted e = mapper.treeToValue(node, BookingEvent.Deleted.class);
                    yield "Отменено бронирование (заезд: %s, выезд: %s)".formatted(
                            e.checkInDate(), e.checkOutDate());
                }
                default -> "Неизвестное событие: " + type;
            };
        } catch (Exception ex) {
            return "Событие " + type + " (ошибка парсинга)";
        }
    }

    private String resolveLevel(String type) {
        return switch (type) {
            case "booking.deleted", "guest.deleted", "room.deleted" -> "warning";
            case "room.enriched"                  -> "info";
            default                               -> "success";
        };
    }

    private String resolveIcon(String type) {
        return switch (type) {
            case "guest.created" -> "guest-plus";
            case "guest.updated" -> "guest-edit";
            case "guest.deleted" -> "guest-remove";
            case "room.created" -> "room-plus";
            case "room.updated" -> "room-edit";
            case "room.deleted" -> "room-remove";
            case "room.enriched" -> "analytics";
            case "booking.created" -> "booking-plus";
            case "booking.updated" -> "booking-edit";
            case "booking.deleted" -> "booking-remove";
            default -> "bell";
        };
    }

    record NotificationPayload(
            String type,
            String eventId,
            String eventType,
            String title,
            String description,
            String icon,
            String level,
            String source,
            String eventTimestamp,
            String receivedAt
    ) {}
}
