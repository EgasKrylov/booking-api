package com.github.egorkrylov.statisticsservice.listener;

import com.github.egorkrylov.bookingeventcontract.BookingEvent;
import com.github.egorkrylov.bookingeventcontract.EventMetadata;
import com.github.egorkrylov.bookingeventcontract.GuestEvent;
import com.github.egorkrylov.bookingeventcontract.RoomEvent;
import com.github.egorkrylov.statisticsservice.model.StatisticEntry;
import com.github.egorkrylov.statisticsservice.storage.StatisticStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.amqp.core.Message;

import java.time.Instant;

@Component
public class StatisticEventListener {

    private static final Logger log = LoggerFactory.getLogger(StatisticEventListener.class);

    private final StatisticStorage storage;
    private final JsonMapper jsonMapper;

    public StatisticEventListener(StatisticStorage storage, JsonMapper jsonMapper) {
        this.storage = storage;
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(queues = "q.statistic.events", messageConverter = "")
    public void handleEvent(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            if(storage.isDuplicate(metadata.eventId())) {
                log.warn("Дубликат события пропущен: eventId={}", metadata.eventId());
                return;
            }

            JsonNode payloadNode = root.get("payload");
            String description = buildDescription(payloadNode, metadata.eventType());

            storage.save(new StatisticEntry(
                    0,
                    metadata.eventId(),
                    metadata.eventType(),
                    metadata.source(),
                    metadata.timestamp(),
                    Instant.now(),
                    description
            ));

            updateStatistic(metadata.eventType());

            log.info("\n{}\n{}", description, printStatistic());
        } catch (Exception ex) {
            log.error("Ошибка обработки события: {}", ex.getMessage(), ex);

            throw new RuntimeException("Не удалось обработать событие", ex);
        }
    }

    private String buildDescription(JsonNode node, String type) {
        return switch (type) {
            case "guest.created" -> {
                GuestEvent.Created guestCreated = jsonMapper.treeToValue(node, GuestEvent.Created.class);
                yield String.format(
                        "Создан новый гость!\n" +
                        "Имя: %s\n" +
                        "Фамилия: %s\n" +
                        "Email: %s\n" +
                        "Дата рждения: %s",
                        guestCreated.firstName(), guestCreated.lastName(), guestCreated.email(), guestCreated.birthDate().toString()
                );
            }
            case "guest.updated" -> {
                GuestEvent.Updated guestUpdated = jsonMapper.treeToValue(node, GuestEvent.Updated.class);
                yield String.format(
                        "Данные гостя обновлены!\n" +
                        "Имя: %s\n" +
                        "Фамилия: %s\n" +
                        "Email: %s\n" +
                        "Дата рждения: %s",
                        guestUpdated.firstName(), guestUpdated.lastName(), guestUpdated.email(), guestUpdated.birthDate().toString()
                );
            }
            case "guest.deleted" -> {
                GuestEvent.Deleted guestDeleted = jsonMapper.treeToValue(node, GuestEvent.Deleted.class);
                yield String.format(
                        "Гость удален!\n" +
                        "Имя: %s\n" +
                        "Фамилия: %s\n" +
                        "Email: %s",
                        guestDeleted.firstName(), guestDeleted.lastName(), guestDeleted.email()
                );
            }
            case "room.created" -> {
                RoomEvent.Created roomCreated = jsonMapper.treeToValue(node, RoomEvent.Created.class);
                yield String.format(
                        "Создан новая комната!\n" +
                        "Номер: %s\n" +
                        "Тип: %s\n" +
                        "Вместимость человек: %s\n" +
                        "Цена: %.1f",
                        roomCreated.roomNumber(), roomCreated.roomType(), roomCreated.capacity(), roomCreated.price()
                );
            }
            case "room.updated" -> {
                RoomEvent.Updated roomUpdated = jsonMapper.treeToValue(node, RoomEvent.Updated.class);
                yield String.format(
                        "Данные комнаты обновлены!\n" +
                        "Номер: %s\n" +
                        "Тип: %s\n" +
                        "Вместимость человек: %s\n" +
                        "Цена: %f",
                        roomUpdated.roomNumber(), roomUpdated.roomType(), roomUpdated.capacity(), roomUpdated.price()
                );
            }
            case "room.deleted" -> {
                RoomEvent.Deleted roomDeleted = jsonMapper.treeToValue(node, RoomEvent.Deleted.class);
                yield String.format(
                        "Данные комнаты обновлены!\n" +
                        "Номер: %s\n" +
                        "Тип: %s",
                        roomDeleted.roomNumber(), roomDeleted.roomType()
                );
            }
            case "room.enriched" -> {
                RoomEvent.Enriched roomEnriched = jsonMapper.treeToValue(node, RoomEvent.Enriched.class);
                yield String.format("Комната обогащена id=%d, номер комнаты=%d (итоговая цена=%.1f руб, категория=%s, " +
                        "цена со скидкой=%.1f)",
                        roomEnriched.roomId(), roomEnriched.roomNumber(), roomEnriched.finalPrice(),
                        roomEnriched.priceCategory(), roomEnriched.discountPrice());
            }
            case "booking.created" -> {
                BookingEvent.Created bookingCreated = jsonMapper.treeToValue(node, BookingEvent.Created.class);
                yield String.format(
                        "Создано новое бронирвание!\n" +
                        "Дата заезда: %s\n" +
                        "Дата выезда: %s\n" +
                        "Количетсво гостей: %d",
                        bookingCreated.checkInDate().toString(), bookingCreated.checkOutDate().toString(), bookingCreated.guestCount()
                );
            }
            case "booking.updated" -> {
                BookingEvent.Updated bookingUpdated = jsonMapper.treeToValue(node, BookingEvent.Updated.class);
                yield String.format(
                        "Бронирвание обновлено!\n" +
                        "Дата заезда: %s\n" +
                        "Дата выезда: %s\n" +
                        "Количетсво гостей: %d",
                        bookingUpdated.checkInDate().toString(), bookingUpdated.checkOutDate().toString(), bookingUpdated.guestCount()
                );
            }
            case "booking.deleted" -> {
                BookingEvent.Deleted bookingDeleted = jsonMapper.treeToValue(node, BookingEvent.Deleted.class);
                yield String.format(
                        "Бронирвание отменено!\n" +
                        "Дата заезда: %s\n" +
                        "Дата выезда: %s\n",
                        bookingDeleted.checkInDate().toString(), bookingDeleted.checkOutDate().toString()
                );
            }
            default -> "Неизвестное событие: " + type;
        };
    }

    private void updateStatistic(String eventType) {
        switch (eventType) {
            case "guest.created" -> storage.incrTotalGuests();
            case "guest.deleted" -> storage.decrTotalGuests();
            case "room.created" -> storage.incrTotalRooms();
            case "room.deleted" -> storage.decrTotalRooms();
            case "booking.created" -> storage.incrTotalBookings();
            case "booking.deleted" -> storage.cancelBookings();
        }
    }

    private String printStatistic() {
        return String.format(
                "\nТекущая статистика:\n" +
                        "-Количество гостей: %d\n" +
                        "-Количество номеров: %d\n" +
                        "-Количество бронированний: %d\n" +
                        "-Количество отмененных бронированний: %d\n",
                storage.getTotalGuests(), storage.getTotalRooms(),
                storage.getTotalBookings(), storage.getCancelledBookings()
        );
    }
}
