package com.github.egorkrylov.bookingrest.assemblers;

import com.github.egorkrylov.bookingapicontract.dto.RoomResponse;
import com.github.egorkrylov.bookingrest.controllers.RoomController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RoomModelAssembler implements RepresentationModelAssembler<RoomResponse, EntityModel<RoomResponse>> {

    @Override
    public EntityModel<RoomResponse> toModel(RoomResponse room) {
        return EntityModel.of(room,
                linkTo(methodOn(RoomController.class).getRoomById(room.getId())).withSelfRel(),
                linkTo(methodOn(RoomController.class).getAll(0, 20)).withRel("collection")
        );
    }
}
