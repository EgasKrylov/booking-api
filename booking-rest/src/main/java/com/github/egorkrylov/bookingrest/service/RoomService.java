package com.github.egorkrylov.bookingrest.service;

import com.github.egorkrylov.bookingapicontract.dto.PagedResponse;
import com.github.egorkrylov.bookingapicontract.dto.PatchRoomRequest;
import com.github.egorkrylov.bookingapicontract.dto.RoomRequest;
import com.github.egorkrylov.bookingapicontract.dto.RoomResponse;
import com.github.egorkrylov.bookingapicontract.exception.ResourceNotFoundException;
import com.github.egorkrylov.bookingrest.domain.Room;
import com.github.egorkrylov.bookingrest.event.RoomEventPublisher;
import com.github.egorkrylov.bookingrest.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomEventPublisher publisher;

    public RoomService(RoomRepository roomRepository, RoomEventPublisher publisher) {
        this.roomRepository = roomRepository;
        this.publisher = publisher;
    }

    public PagedResponse<RoomResponse> findAll(int page, int size) {
        List<RoomResponse> all = roomRepository.findAll().stream()
                .map(room -> RoomResponse.builder()
                        .id(room.getId())
                        .roomNumber(room.getRoomNumber())
                        .roomType(room.getRoomType())
                        .capacity(room.getCapacity())
                        .price(room.getPrice())
                        .description(room.getDescription())
                        .version(room.getVersion())
                        .build())
                .toList();

        int totalElements = all.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<RoomResponse> content = from >=totalElements ? List.of() : all.subList(from, to);
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    @Transactional(readOnly = true)
    public RoomResponse findById(UUID id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id));

        return RoomResponse.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .capacity(room.getCapacity())
                .price(room.getPrice())
                .description(room.getDescription())
                .version(room.getVersion())
                .build();
    }

    @Transactional
    public RoomResponse create(RoomRequest request) {
        Room room = Room.builder()
                .id(UUID.randomUUID())
                .roomNumber(request.roomNumber())
                .roomType(request.roomType())
                .capacity(request.capacity())
                .price(request.price())
                .description(request.description())
                .build();

        Room saved = roomRepository.save(room);

        RoomResponse response = RoomResponse.builder()
                .id(saved.getId())
                .roomNumber(saved.getRoomNumber())
                .roomType(saved.getRoomType())
                .capacity(saved.getCapacity())
                .price(saved.getPrice())
                .description(saved.getDescription())
                .version(saved.getVersion())
                .build();
        publisher.publishCreated(response);
        return response;
    }

    @Transactional
    public RoomResponse update(UUID id, RoomRequest request) {
        Room existing = roomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Room", id));

        Room room = Room.builder()
                .id(existing.getId())
                .roomNumber(request.roomNumber())
                .roomType(request.roomType())
                .capacity(request.capacity())
                .price(request.price())
                .description(request.description())
                .version(existing.getVersion())
                .build();

        Room saved = roomRepository.save(room);

        RoomResponse response = RoomResponse.builder()
                .id(saved.getId())
                .roomNumber(saved.getRoomNumber())
                .roomType(saved.getRoomType())
                .capacity(saved.getCapacity())
                .price(saved.getPrice())
                .description(saved.getDescription())
                .version(saved.getVersion() + 1)
                .build();

        publisher.publishUpdated(response);

        return response;
    }

    @Transactional
    public RoomResponse patch(UUID id, PatchRoomRequest request) {
        Room existing = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id));

        Room room = Room.builder()
                .id(existing.getId())
                .roomNumber(request.roomNumber() == null ? existing.getRoomNumber() : request.roomNumber())
                .roomType(request.roomType() == null ? existing.getRoomType() : request.roomType())
                .capacity(request.capacity() == null ? existing.getCapacity() : request.capacity())
                .price(request.price() == null ? existing.getPrice() : request.price())
                .description(request.description() == null ? existing.getDescription() : request.description())
                .version(existing.getVersion())
                .build();

        Room saved = roomRepository.save(room);

        return RoomResponse.builder()
                .id(saved.getId())
                .roomNumber(saved.getRoomNumber())
                .roomType(saved.getRoomType())
                .capacity(saved.getCapacity())
                .price(saved.getPrice())
                .description(saved.getDescription())
                .version(saved.getVersion() + 1)
                .build();
    }



    @Transactional
    public void delete(UUID id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id));

        RoomResponse response = RoomResponse.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .capacity(room.getCapacity())
                .price(room.getPrice())
                .description(room.getDescription())
                .version(room.getVersion())
                .build();

        roomRepository.delete(room);

        publisher.publishDeleted(response);
    }
}
