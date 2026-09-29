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


@Tag(name = "Rooms", description = "Управление номерами")
@RequestMapping(
        value = "/api/rooms",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public interface RoomApi {

    @Operation(
            summary = "Список всех номеров",
            description = "Возвращает список номеров с HATEOAS-ссылками.",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список номеров")
    @GetMapping
    PagedModel<EntityModel<RoomResponse>> getAll(
            @Parameter(description = "Номер страницы (0..N)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20")
            @RequestParam(defaultValue = "20") int size
    );


    @Operation(
            summary = "Получить номер по ID",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Номер найден")
    @ApiResponse(responseCode = "404", description = "Номер не найден")
    @GetMapping("/{id}")
    EntityModel<RoomResponse> getRoomById(
            @Parameter(description = "ID номера", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id
    );


    @Operation(
            summary = "Добавить новый номер",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Номер создан")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<RoomResponse>> createRoom(@Valid @RequestBody RoomRequest request);


    @Operation(
            summary = "Полное обновление номера (PUT)",
            description = "Обновляет все поля номера. Для обновления отдельных полей используйте PATCH.",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Номер обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Номер не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<RoomResponse> updateRoom(
            @Parameter(description = "ID номера", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id,
            @Valid @RequestBody RoomRequest request
    );


    @Operation(
            summary = "Частичное обновление информации о номере (PATCH)",
            description = """
                    Обновляет только переданные поля (семантика JSON Merge Patch, RFC 7396).
                    Непереданные поля остаются без изменений.
                    """,
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Номер обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Номер не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<RoomResponse> patchRoom(
            @Parameter(description = "ID номера", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id,
            @Valid @RequestBody PatchRoomRequest request
    );


    @Operation(
            summary = "Удалить номер",
            security = @SecurityRequirement(name = BookingApiContractConfig.SECURITY_SHEMA_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Номер удален")
    @ApiResponse(responseCode = "404", description = "Номер не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRoom(
            @Parameter(description = "ID номера", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id
    );


}
