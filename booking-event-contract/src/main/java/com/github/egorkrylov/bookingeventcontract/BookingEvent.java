package com.github.egorkrylov.bookingeventcontract;

import java.time.LocalDate;

public sealed interface BookingEvent {

    record Created(
            LocalDate checkInDate,
            LocalDate checkOutDate,
            short guestCount
    ) implements BookingEvent {}

    record Updated(
            LocalDate checkInDate,
            LocalDate checkOutDate,
            short guestCount
    ) implements BookingEvent {}

    record Deleted(
            LocalDate checkInDate,
            LocalDate checkOutDate
    ) implements BookingEvent {}
}
