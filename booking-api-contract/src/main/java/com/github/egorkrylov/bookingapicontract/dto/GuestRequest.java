package com.github.egorkrylov.bookingapicontract.dto;

import com.github.egorkrylov.bookingapicontract.validation.ValidNumber;
import com.github.egorkrylov.bookingapicontract.validation.ValidSeries;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Запрос на создание гостя")
public record GuestRequest(

        @Schema(description = "Имя гостя", example = "Иван", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя гостя не может быть пустым")
        @Size(max = 128, message = "Имя не может быть больше 128 символов")
        String firstName,

        @Schema(description = "Фамилия гостя", example = "Иванов", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Фамилия не может быть пустой")
        @Size(max = 128, message = "Фамилия не может быть больше 128 символов")
        String lastName,

        @Schema(description = "Email гостя", example = "ivan@example.com")
        @Email(message = "Некорректный формат email")
        @Size(max = 255, message = "Email не может превышать 255 символов")
        String email,

        @Schema(description = "Дата рождения гостя", example = "1995-03-06")
        @Past(message = "Дата рожденя должна быть в прошлом")
        LocalDate birthDate,

        @Schema(description = "Серия паспорта гостя", example = "4621")
        @NotBlank(message = "Серия паспорта не может быть пустой")
        @ValidSeries(message = "Серия паспорта строго 4 символа")
        String passportSeries,

        @Schema(description = "Номер паспорта гостя", example = "662134")
        @NotBlank(message = "Номер паспорта не может быть пустым")
        @ValidNumber(message = "Серия паспорта строго 6 символа")
        String passportNumber

) {}
