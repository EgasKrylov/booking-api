package com.github.egorkrylov.bookingapicontract.endpoints;


import com.github.egorkrylov.bookingapicontract.config.BookingApiContractConfig;
import com.github.egorkrylov.bookingapicontract.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Guests", description = "Упрвелние гостями отеля")
@RequestMapping(
        value = "/api/guests",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public interface GuestApi {

    @Operation(
            summary = "Список гостей",
            description = "Возвращает список гостей с HATEOAS-ссылками.",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список гостей")
    @GetMapping
    PagedModel<EntityModel<GuestResponse>> getAll(
            @Parameter(description = "Номер страницы (0..N)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20")
            @RequestParam(defaultValue = "20") int size

    );

    @Operation(
            summary = "Получить гостя по ID",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Гость найден")
    @ApiResponse(responseCode = "404", description = "Гость не найден")
    @GetMapping("/{id}")
    EntityModel<GuestResponse> getGuestById(
            @Parameter(description = "ID гостя", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id
    );

    @Operation(
            summary = "Добавить нового гостя",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Гость создан.")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<GuestResponse>> createGuest(@Valid @RequestBody GuestRequest request);


    @Operation(
            summary = "Полное обновление гостя (PUT)",
            description = "Обновляет все поля гостя. Для обновления отдельных полей используйте PATCH.",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Гость обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Гость не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<GuestResponse> updateGuest(
            @Parameter(description = "ID гостя", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id,
            @Valid @RequestBody GuestRequest request
    );

    @Operation(
            summary = "Частичное обновление информации о госте (PATCH)",
            description = """
                    Обновляет только переданные поля (семантика JSON Merge Patch, RFC 7396).
                    Непереданные поля остаются без изменений.
                    """,
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Гость обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Гость не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<GuestResponse> patchGuest(
            @Parameter(description = "ID гостя", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id,
            @Valid @RequestBody PatchGuestRequest request
    );

    @Operation(
            summary = "Удалить гостя",
            description = "Удаляет гостя и все его бронирования.",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Гость удален")
    @ApiResponse(responseCode = "404", description = "Гость не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteGuest(
            @Parameter(description = "ID гостя", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id
    );

    @Operation(
            summary = "Бронирования указанного гостя",
            description = """
                    Возвращает список бронированний указанного гостя.
                    Это суб-ресурс (концепция REST): /guests/{id}/bookings.
                    """,
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список бронированний гостя")
    @ApiResponse(responseCode = "404", description = "Гость не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}/bookings")
    PagedModel<EntityModel<BookingResponse>> getBookingsGuest(
            @Parameter(description = "ID гостя", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id,
            @Parameter(description = "Номер страницы (0..N)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );

}
