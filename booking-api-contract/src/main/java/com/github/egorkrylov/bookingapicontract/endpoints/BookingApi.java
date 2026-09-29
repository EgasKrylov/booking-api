package com.github.egorkrylov.bookingapicontract.endpoints;

import com.github.egorkrylov.bookingapicontract.config.BookingApiContractConfig;
import com.github.egorkrylov.bookingapicontract.dto.*;
import com.github.egorkrylov.bookingapicontract.dto.BookingResponse;
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

@Tag(name = "Bookings", description = "Управление бронированиями отеля")
@RequestMapping(
        value = "/api/bookings",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public interface BookingApi {

    @Operation(
            summary = "Список всех бронированний",
            description = "Возвращает список бронированний с HATEOAS-ссылками.",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список бронированний")
    @GetMapping
    PagedModel<EntityModel<BookingResponse>> getAll(
            @Parameter(description = "Номер страницы (0..N)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20")
            @RequestParam(defaultValue = "20") int size
    );


    @Operation(
            summary = "Получить бронирование по ID",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Бронирование найдено")
    @ApiResponse(responseCode = "404", description = "Бронирование не найдено")
    @GetMapping("/{id}")
    EntityModel<BookingResponse> getBookingById(
            @Parameter(description = "ID бронирования", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id
    );


    @Operation(
            summary = "Добавить новое бронирование",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Бронирование создано")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<BookingResponse>> createBooking(@Valid @RequestBody BookingRequest request);


    @Operation(
            summary = "Полное обновление бронирования (PUT)",
            description = "Обновляет все поля бронирования. Для обновления отдельных полей используйте PATCH.",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Бронирование обновлено")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Бронирование не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<BookingResponse> updateBooking(
            @Parameter(description = "ID бронирования", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id,
            @Valid @RequestBody UpdateBookingRequest request
    );


    @Operation(
            summary = "Частичное обновление информации о бронировании (PATCH)",
            description = """
                    Обновляет только переданные поля (семантика JSON Merge Patch, RFC 7396).
                    Непереданные поля остаются без изменений.
                    """,
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Броинрование обновлено")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Бронирование не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<BookingResponse> patchBooking(
            @Parameter(description = "ID бронирования", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id,
            @Valid @RequestBody PatchBookingRequest request
    );


    @Operation(
            summary = "Удалить бронирование",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Бронирование удалено")
    @ApiResponse(responseCode = "404", description = "Бронирование не найдено",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteBooking(
            @Parameter(description = "ID бронирования", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id
    );










}
