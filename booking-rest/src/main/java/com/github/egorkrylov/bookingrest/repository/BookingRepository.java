package com.github.egorkrylov.bookingrest.repository;

import com.github.egorkrylov.bookingrest.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
}
