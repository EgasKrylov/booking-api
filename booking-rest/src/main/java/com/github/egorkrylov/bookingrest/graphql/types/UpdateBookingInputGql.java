package com.github.egorkrylov.bookingrest.graphql.types;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateBookingInputGql(
        UUID roomId,

        LocalDate checkInDate,

        LocalDate checkOutDate,

        Short guestCount
) {
}
