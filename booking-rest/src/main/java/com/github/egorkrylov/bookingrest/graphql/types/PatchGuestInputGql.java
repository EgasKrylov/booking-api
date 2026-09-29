package com.github.egorkrylov.bookingrest.graphql.types;

import java.time.LocalDate;

public record PatchGuestInputGql(
        String firstName,
        String lastName,
        String email,
        LocalDate birthDate,
        String passportSeries,
        String passportNumber
) {
}
