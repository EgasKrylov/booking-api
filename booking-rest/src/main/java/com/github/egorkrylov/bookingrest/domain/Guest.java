package com.github.egorkrylov.bookingrest.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Guests")
public class Guest {

    @Id
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 128)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 128)
    private String lastName;

    @Column(length = 255)
    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "passport_series", nullable = false, length = 4)
    private String passportSeries;

    @Column(name = "passport_number", nullable = false, length = 6)
    private String passportNumber;

    @Version
    private long version;


}
