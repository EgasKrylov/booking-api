//package com.github.egorkrylov.bookingrest.storage;
//
//import com.github.egorkrylov.bookingapicontract.dto.BookingResponse;
//import com.github.egorkrylov.bookingapicontract.dto.GuestResponse;
//import com.github.egorkrylov.bookingapicontract.dto.RoomResponse;
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.OffsetDateTime;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicLong;
//
//@Component
//public class InMemoryStorage {
//    public final Map<Long, GuestResponse> guests = new ConcurrentHashMap<>();
//    public final Map<Long, BookingResponse> bookings = new ConcurrentHashMap<>();
//    public final Map<Long, RoomResponse> rooms = new ConcurrentHashMap<>();
//
//    public final AtomicLong guestSequence = new AtomicLong(0);
//    public final AtomicLong bookingSequence = new AtomicLong(0);
//    public final AtomicLong roomSequence = new AtomicLong(0);
//
//
//
//    @PostConstruct
//    public void init() {
//        long guestId1 = guestSequence.incrementAndGet();
//        GuestResponse guest1 = GuestResponse.builder()
//                .id(guestId1)
//                .firstName("Иван")
//                .lastName("Иванов")
//                .email("ivan@mail.ru")
//                .birthDate(LocalDate.of(2000, 2, 13))
//                .passportSeries("4617")
//                .passportNumber("321523")
//                .build();
//
//        long guestId2 = guestSequence.incrementAndGet();
//        GuestResponse guest2 = GuestResponse.builder()
//                .id(guestId2)
//                .firstName("Максим")
//                .lastName("Максимов")
//                .email("max@mail.ru")
//                .birthDate(LocalDate.of(1994, 4, 21))
//                .passportSeries("4314")
//                .passportNumber("311723")
//                .build();
//
//        long guestId3 = guestSequence.incrementAndGet();
//        GuestResponse guest3 = GuestResponse.builder()
//                .id(guestId3)
//                .firstName("Иван")
//                .lastName("Иванов")
//                .email("ivan@mail.ru")
//                .birthDate(LocalDate.of(2001, 3, 11))
//                .passportSeries("4418")
//                .passportNumber("331923")
//                .build();
//
//        guests.put(guestId1, guest1);
//        guests.put(guestId2, guest2);
//        guests.put(guestId3, guest3);
//
//
//        long roomId1 = roomSequence.incrementAndGet();
//        RoomResponse room1 = RoomResponse.builder()
//                .id(roomId1)
//                .roomNumber(201)
//                .roomType("Люкс")
//                .capacity(4)
//                .price(BigDecimal.valueOf(5000))
//                .description("Просторный номер с красивым видом.")
//                .build();
//
//        long roomId2 = roomSequence.incrementAndGet();
//        RoomResponse room2 = RoomResponse.builder()
//                .id(roomId2)
//                .roomNumber(123)
//                .roomType("Стандарт")
//                .capacity(3)
//                .price(BigDecimal.valueOf(4000))
//                .description("Стандартный номер для семьи из трех человек.")
//                .build();
//
//        rooms.put(roomId1, room1);
//        rooms.put(roomId2, room2);
//
//
//        long bookingId1 = bookingSequence.incrementAndGet();
//        BookingResponse booking1 = BookingResponse.builder()
//                .id(bookingId1)
//                .guest(guest1)
//                .room(room2)
//                .checkInDate(LocalDate.of(2026, 8, 14))
//                .checkOutDate(LocalDate.of(2026, 8, 20))
//                .guestCount(3)
//                .createdAt(OffsetDateTime.now())
//                .build();
//
//        bookings.put(bookingId1, booking1);
//
//    }
//
//
//}
