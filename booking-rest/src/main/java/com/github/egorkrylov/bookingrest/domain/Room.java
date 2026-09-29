package com.github.egorkrylov.bookingrest.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Rooms")
public class Room {

    @Id
    private UUID id;

    @Column(name = "room_number", nullable = false)
    private Integer roomNumber;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    private short capacity;

    private BigDecimal price;

    private String description;

    @Version
    private long version;

}
