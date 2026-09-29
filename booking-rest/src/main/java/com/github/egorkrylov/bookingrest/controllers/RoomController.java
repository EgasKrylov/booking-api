package com.github.egorkrylov.bookingrest.controllers;

import com.github.egorkrylov.bookingapicontract.dto.PagedResponse;
import com.github.egorkrylov.bookingapicontract.dto.PatchRoomRequest;
import com.github.egorkrylov.bookingapicontract.dto.RoomRequest;
import com.github.egorkrylov.bookingapicontract.dto.RoomResponse;
import com.github.egorkrylov.bookingapicontract.endpoints.RoomApi;
import com.github.egorkrylov.bookingrest.assemblers.RoomModelAssembler;
import com.github.egorkrylov.bookingrest.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RoomController implements RoomApi {

    private final RoomService roomService;
    private final RoomModelAssembler roomModelAssembler;
    private final PagedResourcesAssembler<RoomResponse> pagedRoomAssembler;

    public RoomController(RoomService roomService,
                          RoomModelAssembler roomModelAssembler,
                          PagedResourcesAssembler<RoomResponse> pagedRoomAssembler) {
        this.roomService = roomService;
        this.roomModelAssembler = roomModelAssembler;
        this.pagedRoomAssembler = pagedRoomAssembler;
    }

    @Override
    public PagedModel<EntityModel<RoomResponse>> getAll(int page, int size) {
        PagedResponse<RoomResponse> roomResp = roomService.findAll(page, size);

        Page<RoomResponse> pageResp = new PageImpl<>(
                roomResp.content(),
                PageRequest.of(page, size),
                roomResp.totalElements()
        );

        return pagedRoomAssembler.toModel(pageResp, roomModelAssembler);
    }

    @Override
    public EntityModel<RoomResponse> getRoomById(UUID id) {
        return roomModelAssembler.toModel(roomService.findById(id));
    }

    @Override
    public ResponseEntity<EntityModel<RoomResponse>> createRoom(RoomRequest request) {
        EntityModel<RoomResponse> entityRoom = roomModelAssembler.toModel(roomService.create(request));

        return ResponseEntity
                .created(entityRoom.getRequiredLink("self").toUri())
                .body(entityRoom);
    }

    @Override
    public EntityModel<RoomResponse> updateRoom(UUID id, RoomRequest request) {
        return roomModelAssembler.toModel(roomService.update(id, request));
    }

    @Override
    public EntityModel<RoomResponse> patchRoom(UUID id, PatchRoomRequest request) {
        return roomModelAssembler.toModel(roomService.patch(id, request));
    }

    @Override
    public void deleteRoom(UUID id) {
        roomService.delete(id);
    }
}
