package com.github.egorkrylov.bookingrest.assemblers;

import com.github.egorkrylov.bookingapicontract.dto.BookingResponse;
import com.github.egorkrylov.bookingrest.controllers.BookingController;
import com.github.egorkrylov.bookingrest.controllers.GuestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookingModelAssembler implements RepresentationModelAssembler<BookingResponse, EntityModel<BookingResponse>> {

    @Override
    public EntityModel<BookingResponse> toModel(BookingResponse booking) {
        return EntityModel.of(booking,
                linkTo(methodOn(BookingController.class).getBookingById(booking.getId())).withSelfRel(),
                linkTo(methodOn(GuestController.class).getGuestById(booking.getGuestId())).withRel("guest"),
                linkTo(methodOn(BookingController.class).getAll(0, 20)).withRel("collection")
        );
    }
}
