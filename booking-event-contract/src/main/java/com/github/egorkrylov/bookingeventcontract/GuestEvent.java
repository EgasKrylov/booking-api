package com.github.egorkrylov.bookingeventcontract;

import java.time.LocalDate;

public sealed interface GuestEvent {

    record Created(
            String firstName,
            String lastName,
            String email,
            LocalDate birthDate
    ) implements GuestEvent {}

    record Updated(
            String firstName,
            String lastName,
            String email,
            LocalDate birthDate
    ) implements GuestEvent {}



    record Deleted(
            String firstName,
            String lastName,
            String email
    ) implements GuestEvent {}

}

