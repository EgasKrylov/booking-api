package com.github.egorkrylov.bookingrest.assemblers;

import com.github.egorkrylov.bookingapicontract.dto.GuestResponse;
import com.github.egorkrylov.bookingrest.controllers.GuestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GuestModelAssembler implements RepresentationModelAssembler<GuestResponse, EntityModel<GuestResponse>> {


    @Override
    public EntityModel<GuestResponse> toModel(GuestResponse guest) {
        return EntityModel.of(guest,
                linkTo(methodOn(GuestController.class).getGuestById(guest.getId())).withSelfRel(),
                linkTo(methodOn(GuestController.class).getAll(0, 20)).withRel("collection"),
                linkTo(methodOn(GuestController.class).getBookingsGuest(guest.getId(), 0, 20)).withRel("bookings")
        );
    }
}
