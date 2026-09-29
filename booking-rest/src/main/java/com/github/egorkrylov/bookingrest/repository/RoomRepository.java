package com.github.egorkrylov.bookingrest.repository;

import com.github.egorkrylov.bookingrest.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    boolean existsByRoomNumber(Integer roomNumber);
}
