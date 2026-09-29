package com.github.egorkrylov.bookingapicontract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Частичное обновление. Передайте только те поля, которые нужно изменить.")
public record PatchRoomRequest(

        @Schema(description = "Номер комнаты", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
        @Positive(message = "Номер комнты не может быть отрицательным")
        @Max(value = 100000, message = "Номер комнаты не может быть больше 100000")
        Integer roomNumber,

        @Schema(description = "Тип номера", example = "Люкс", requiredMode = Schema.RequiredMode.REQUIRED)
        String roomType,

        @Schema(description = "Максимальная вместимость номера", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
        @Positive(message = "Вместимоть комнаты не может быть отрицательной")
        @Max(value = 10, message = "Вместимость номера не может быть больше 10")
        Short capacity,

        @Schema(description = "Стоимость номера в рублях", example = "5000", requiredMode = Schema.RequiredMode.REQUIRED)
        @Positive(message = "Стоимость комнаты не может быть отрицательной")
        BigDecimal price,

        @Schema(description = "Описание номера", requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(max = 5000, message = "Описание не может превышать 5000 символов")
        String description
) {}
