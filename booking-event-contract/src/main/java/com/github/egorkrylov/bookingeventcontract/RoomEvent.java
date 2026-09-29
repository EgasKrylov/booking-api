package com.github.egorkrylov.bookingeventcontract;

import java.math.BigDecimal;
import java.util.UUID;

public sealed interface RoomEvent {

    record Created(
            UUID roomId,
            Integer roomNumber,
            String roomType,
            Short capacity,
            BigDecimal price
    ) implements RoomEvent {}

    record Updated(
            Integer roomNumber,
            String roomType,
            Short capacity,
            BigDecimal price
    ) implements RoomEvent {}

    record Deleted(
            Integer roomNumber,
            String roomType
    ) implements RoomEvent {}

    record Enriched(
            UUID roomId,
            Integer roomNumber,
            BigDecimal finalPrice,
            String priceCategory,
            BigDecimal discountPrice
    ) implements RoomEvent {}

}
