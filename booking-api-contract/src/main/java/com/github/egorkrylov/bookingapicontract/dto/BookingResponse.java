package com.github.egorkrylov.bookingapicontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "bookings", itemRelation = "booking")
@Schema(description = "Информация о бронировании")
public class BookingResponse extends RepresentationModel<BookingResponse> {

    @Schema(description = "ID бронирования", example = "1")
    private final UUID id;

    @Schema(description = "Гость")
    private final UUID guestId;

    @Schema(description = "Бронируемый номер")
    private final UUID roomId;

    @Schema(description = "Дата заезда", example = "2026-11-09")
    private final LocalDate checkInDate;

    @Schema(description = "Дата выезда", example = "2026-11-14")
    private final LocalDate checkOutDate;

    @Schema(description = "Количество гостей, которые будут проживать", example = "3")
    private final short guestCount;

    @Schema(description = "Версия", example = "1")
    private final Long version;

    @Schema(description = "Момент создания бронирования")
    private final OffsetDateTime createdAt;

    @Schema(description = "Момент последнего обновления бронирования")
    private final OffsetDateTime updatedAt;

}
