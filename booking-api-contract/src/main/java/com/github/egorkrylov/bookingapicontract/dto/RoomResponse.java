package com.github.egorkrylov.bookingapicontract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "rooms", itemRelation = "room")
@Schema(description = "Информация о комнате")
public class RoomResponse extends RepresentationModel<RoomResponse> {

    @Schema(description = "Уникальный идентификатор комнаты", example = "1")
    private final UUID id;

    @Schema(description = "Номер комнаты", example = "101")
    private final Integer roomNumber;

    @Schema(description = "Тип номера", example = "Люкс")
    private final String roomType;

    @Schema(description = "Максимальная вместимость номера", example = "4")
    private final Short capacity;

    @Schema(description = "Стоимость номера в рублях", example = "5000")
    @Positive(message = "Стоимость комнаты не может быть отрицательной")
    private final BigDecimal price;

    @Schema(description = "Описание номера")
    private final String description;

    @Schema(description = "Версия", example = "1")
    private final Long version;

}
