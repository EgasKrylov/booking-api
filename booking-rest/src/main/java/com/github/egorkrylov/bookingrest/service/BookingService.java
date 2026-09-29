package com.github.egorkrylov.bookingrest.service;

import com.github.egorkrylov.bookingapicontract.dto.*;
import com.github.egorkrylov.bookingapicontract.exception.ResourceNotFoundException;
import com.github.egorkrylov.bookingrest.domain.Booking;
import com.github.egorkrylov.bookingrest.domain.Guest;
import com.github.egorkrylov.bookingrest.domain.Room;
import com.github.egorkrylov.bookingrest.event.BookingEventPublisher;
import com.github.egorkrylov.bookingrest.repository.BookingRepository;
import com.github.egorkrylov.bookingrest.repository.GuestRepository;
import com.github.egorkrylov.bookingrest.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final BookingEventPublisher publisher;

    public BookingService(GuestRepository guestRepository, RoomRepository roomRepository,
                          BookingRepository bookingRepository, BookingEventPublisher publisher) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.publisher = publisher;
    }

    @Transactional(readOnly = true)
    public PagedResponse<BookingResponse> findAll(UUID guestId, int page, int size) {
        List<Booking> bookings = bookingRepository.findAll();

        if(guestId != null) {
            bookings = bookings.stream().filter(b -> b.getGuest().getId().equals(guestId)).toList();
        }

        List<BookingResponse> bookingsAll = bookings.stream()
                .map(b -> new BookingResponse(
                        b.getId(),
                        b.getGuest().getId(),
                        b.getRoom().getId(),
                        b.getCheckInDate(),
                        b.getCheckOutDate(),
                        b.getGuestCount(),
                        b.getVersion(),
                        b.getCreatedAt(),
                        b.getUpdateAt()
                )).toList();
        int totalElements = bookingsAll.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) bookingsAll.size() / page) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);

        List<BookingResponse> bookingPage = from >= totalElements ? List.of() : bookingsAll.subList(from, to);
        return new PagedResponse<>(bookingPage, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    @Transactional(readOnly = true)
    public BookingResponse findById(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", id));

        return new BookingResponse(
                booking.getId(),
                booking.getGuest().getId(),
                booking.getRoom().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getGuestCount(),
                booking.getVersion(),
                booking.getCreatedAt(),
                booking.getUpdateAt()
        );
    }


    @Transactional
    public BookingResponse create(BookingRequest request) {
        Guest guest = guestRepository.findById(request.guestId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest", request.guestId()));

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", request.roomId()));

        Booking booking = Booking.builder()
                .id(UUID.randomUUID())
                .guest(guest)
                .room(room)
                .checkInDate(request.checkInDate())
                .checkOutDate(request.checkOutDate())
                .guestCount(request.guestCount())
                .createdAt(OffsetDateTime.now())
                .build();

        Booking saved = bookingRepository.save(booking);

        BookingResponse response = new BookingResponse(
                saved.getId(),
                saved.getGuest().getId(),
                saved.getRoom().getId(),
                saved.getCheckInDate(),
                saved.getCheckOutDate(),
                saved.getGuestCount(),
                saved.getVersion(),
                saved.getCreatedAt(),
                saved.getUpdateAt()
        );

        publisher.publishCreated(response);

        return response;
    }


    @Transactional
    public BookingResponse update(UUID id, UpdateBookingRequest request) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", id));


        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", request.roomId()));

        booking = Booking.builder()
                .id(booking.getId())
                .guest(booking.getGuest())
                .room(room)
                .checkInDate(request.checkInDate())
                .checkOutDate(request.checkOutDate())
                .guestCount(request.guestCount())
                .createdAt(booking.getCreatedAt())
                .updateAt(OffsetDateTime.now())
                .build();

        Booking saved = bookingRepository.save(booking);

        BookingResponse response = new BookingResponse(
                saved.getId(),
                saved.getGuest().getId(),
                saved.getRoom().getId(),
                saved.getCheckInDate(),
                saved.getCheckOutDate(),
                saved.getGuestCount(),
                saved.getVersion() + 1,
                saved.getCreatedAt(),
                saved.getUpdateAt()
        );

        publisher.publishUpdated(response);

        return response;
    }

    @Transactional
    public BookingResponse patch(UUID id, PatchBookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", id));

        Room room = booking.getRoom();

        if (request.roomId() != null) {
            room = roomRepository.findById(request.roomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Room", request.roomId()));
        }

        Booking updatedBooking = Booking.builder()
                .id(booking.getId())
                .guest(booking.getGuest())
                .room(room)
                .checkInDate(request.checkInDate() != null ? request.checkInDate() : booking.getCheckInDate())
                .checkOutDate(request.checkOutDate() != null ? request.checkOutDate() : booking.getCheckOutDate())
                .guestCount(request.guestCount() != null ? request.guestCount() : booking.getGuestCount())
                .createdAt(booking.getCreatedAt())
                .updateAt(OffsetDateTime.now())
                .build();

        Booking saved = bookingRepository.save(updatedBooking);

        return new BookingResponse(
                saved.getId(),
                saved.getGuest().getId(),
                saved.getRoom().getId(),
                saved.getCheckInDate(),
                saved.getCheckOutDate(),
                saved.getGuestCount(),
                saved.getVersion() + 1,
                saved.getCreatedAt(),
                saved.getUpdateAt()
        );
    }


    @Transactional
    public void delete(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", id));

        BookingResponse response = new BookingResponse(
                booking.getId(),
                booking.getGuest().getId(),
                booking.getRoom().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getGuestCount(),
                booking.getVersion(),
                booking.getCreatedAt(),
                booking.getUpdateAt()
        );

        bookingRepository.delete(booking);

        publisher.publishDeleted(response);
    }


}
