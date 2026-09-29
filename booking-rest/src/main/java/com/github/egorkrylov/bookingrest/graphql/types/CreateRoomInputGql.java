package com.github.egorkrylov.bookingrest.graphql.types;

import java.math.BigDecimal;

public record CreateRoomInputGql(
        Integer roomNumber,
        String roomType,
        Short capacity,
        BigDecimal price,
        String description
) {}
