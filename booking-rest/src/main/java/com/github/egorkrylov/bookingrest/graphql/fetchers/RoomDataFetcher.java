package com.github.egorkrylov.bookingrest.graphql.fetchers;

import com.github.egorkrylov.bookingapicontract.dto.PagedResponse;
import com.github.egorkrylov.bookingapicontract.dto.PatchRoomRequest;
import com.github.egorkrylov.bookingapicontract.dto.RoomRequest;
import com.github.egorkrylov.bookingapicontract.dto.RoomResponse;
import com.github.egorkrylov.bookingrest.graphql.types.*;
import com.github.egorkrylov.bookingrest.service.RoomService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.UUID;

@DgsComponent
public class RoomDataFetcher {

    private final RoomService roomService;

    public RoomDataFetcher(RoomService roomService) {
        this.roomService = roomService;
    }

    @DgsQuery
    public RoomConnectionGql rooms(@InputArgument Integer page, @InputArgument Integer size) {

        page = page == null ? 0 : page;
        size = size == null ? 0 : size;

        PagedResponse<RoomResponse> roomResponse = roomService.findAll(page, size);

        return new RoomConnectionGql(
                roomResponse.content(),
                new PagedInfoGql(roomResponse.pageNumber(), roomResponse.pageSize(), roomResponse.totalPages(), roomResponse.last()),
                (int) roomResponse.totalElements()
        );
    }


    @DgsQuery
    public RoomResponse room(@InputArgument String id) {
        return roomService.findById(UUID.fromString(id));
    }

    @DgsMutation
    public RoomResponse createRoom(@InputArgument CreateRoomInputGql input) {
        RoomRequest request = new RoomRequest(
                input.roomNumber(),
                input.roomType(),
                input.capacity(),
                input.price(),
                input.description()
        );

        return roomService.create(request);
    }

    @DgsMutation
    public RoomResponse updateRoom(@InputArgument String id, @InputArgument UpdateRoomInputGql input) {
        RoomRequest request = new RoomRequest(
                input.roomNumber(),
                input.roomType(),
                input.capacity(),
                input.price(),
                input.description()
        );


        return roomService.update(UUID.fromString(id), request);
    }

    @DgsMutation
    public RoomResponse patchRoom(@InputArgument String id, @InputArgument PatchRoomInputGql input) {
        PatchRoomRequest request = new PatchRoomRequest(
                input.roomNumber(),
                input.roomType(),
                input.capacity(),
                input.price(),
                input.description()
        );

        return roomService.patch(UUID.fromString(id), request);
    }

    @DgsMutation
    public boolean deleteRoom(@InputArgument String id) {
        roomService.delete(UUID.fromString(id));
        return true;
    }

}
