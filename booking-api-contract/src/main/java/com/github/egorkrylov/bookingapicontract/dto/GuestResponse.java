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
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "guests", itemRelation = "guest")
@Schema(description = "Информация о госте")
public class GuestResponse extends RepresentationModel<GuestResponse> {

    @Schema(description = "Уникальный идентификатор гостя", example = "1")
    private final UUID id;

    @Schema(description = "Имя гостя", example = "Иван")
    private final String firstName;

    @Schema(description = "Фамилия гостя", example = "Иванов")
    private final String lastName;

    @Schema(description = "Email гостя", example = "ivan@example.com")
    private final String email;

    @Schema(description = "Дата рождения гостя", example = "1995-03-06")
    private final LocalDate birthDate;

    @Schema(description = "Серия паспорта гостя", example = "4621")
    private final String passportSeries;

    @Schema(description = "Номер паспорта гостя", example = "662134")
    private final String passportNumber;

    @Schema(description = "Версия", example = "1")
    private final Long version;

}
