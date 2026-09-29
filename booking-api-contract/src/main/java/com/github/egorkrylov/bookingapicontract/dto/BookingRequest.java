package com.github.egorkrylov.bookingapicontract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Запрос на создание бронирования")
public record BookingRequest (

        @Schema(description = "Уникальный идентификатор гостя", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "ID гостя не может быть пустым")
        UUID guestId,

        @Schema(description = "Уникальный идентификатор номера", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "ID номера не может быть пустым")
        UUID roomId,

        @Schema(description = "Дата заезда", example = "2026-11-09", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Дата заезда не может быть пустой")
        @Future(message = "Дата должна быть в будущем!")
        LocalDate checkInDate,

        @Schema(description = "Дата выезда", example = "2026-11-14", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Дата выезда не может быть пустой")
        @Future(message = "Дата должна быть в будущем!")
        LocalDate checkOutDate,

        @Schema(description = "Количество гостей, которые будут проживать", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        @Positive(message = "Количество гостей не может быть отрицательным")
        @Max(value = 10, message = "Количетсво гостей не может быть больше 10")
        short guestCount


) {}
