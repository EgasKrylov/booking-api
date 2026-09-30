package com.github.egorkrylov.bookingrest.controllers;

import com.github.egorkrylov.bookingapicontract.dto.*;
import com.github.egorkrylov.bookingapicontract.endpoints.GuestApi;
import com.github.egorkrylov.bookingrest.assemblers.BookingModelAssembler;
import com.github.egorkrylov.bookingrest.assemblers.GuestModelAssembler;
import com.github.egorkrylov.bookingrest.service.BookingService;
import com.github.egorkrylov.bookingrest.service.GuestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GuestController implements GuestApi {

    private final GuestService guestService;
    private final BookingService bookingService;
    private final GuestModelAssembler guestModelAssembler;
    private final BookingModelAssembler bookingModelAssembler;
    private final PagedResourcesAssembler<GuestResponse> pagedGuestAssembler;
    private final PagedResourcesAssembler<BookingResponse> pagedBookingAssembler;

    public GuestController(GuestService guestService,
                           BookingService bookingService,
                           GuestModelAssembler guestModelAssembler,
                           BookingModelAssembler bookingModelAssembler,
                           PagedResourcesAssembler<GuestResponse> pagedGuestAssembler,
                           PagedResourcesAssembler<BookingResponse> pagedBookingAssembler) {
        this.guestService = guestService;
        this.bookingService = bookingService;
        this.guestModelAssembler = guestModelAssembler;
        this.bookingModelAssembler = bookingModelAssembler;
        this.pagedGuestAssembler = pagedGuestAssembler;
        this.pagedBookingAssembler = pagedBookingAssembler;
    }


    @Override
    @PreAuthorize("hasAnyRole('READER','EDITOR')")
    public PagedModel<EntityModel<GuestResponse>> getAll(int page, int size) {
        PagedResponse<GuestResponse> guests = guestService.findAll(page, size);
        Page<GuestResponse> guestsPage = new PageImpl<>(
                guests.content(),
                PageRequest.of(page, size),
                guests.totalElements()
        );

        return pagedGuestAssembler.toModel(guestsPage, guestModelAssembler);
    }

    @Override
    @PreAuthorize("hasAnyRole('READER','EDITOR')")
    public EntityModel<GuestResponse> getGuestById(UUID id) {
        GuestResponse guest = guestService.findById(id);

        return guestModelAssembler.toModel(guest);
    }

    @Override
    @PreAuthorize("hasAnyRole('EDITOR')")
    public ResponseEntity<EntityModel<GuestResponse>> createGuest(GuestRequest request) {
        GuestResponse createdGuest = guestService.create(request);
        EntityModel<GuestResponse> guestResponse = guestModelAssembler.toModel(createdGuest);

        return ResponseEntity
                .created(guestResponse.getRequiredLink("self").toUri())
                .body(guestResponse);
    }

    @Override
    @PreAuthorize("hasAnyRole('EDITOR')")
    public EntityModel<GuestResponse> updateGuest(UUID id, GuestRequest request) {
        GuestResponse guestResponse = guestService.update(id, request);

        return guestModelAssembler.toModel(guestResponse);
    }

    @Override
    @PreAuthorize("hasAnyRole('EDITOR')")
    public EntityModel<GuestResponse> patchGuest(UUID id, PatchGuestRequest request) {
        GuestResponse guestResponse = guestService.patch(id, request);

        return guestModelAssembler.toModel(guestResponse);
    }

    @Override
    @PreAuthorize("hasAnyRole('EDITOR')")
    public void deleteGuest(UUID id) {
        guestService.delete(id);
    }

    @Override
    @PreAuthorize("hasAnyRole('READER','EDITOR')")
    public PagedModel<EntityModel<BookingResponse>> getBookingsGuest(UUID id, int page, int size) {
        guestService.findById(id);
        PagedResponse<BookingResponse> bookings = bookingService.findAll(id, page, size);
        Page<BookingResponse> pageBookings = new PageImpl<>(
                bookings.content(),
                PageRequest.of(page, size),
                bookings.totalElements()
        );

        return pagedBookingAssembler.toModel(pageBookings, bookingModelAssembler);
    }
}
