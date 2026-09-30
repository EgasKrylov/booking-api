package com.github.egorkrylov.bookingrest.controllers;

import com.github.egorkrylov.bookingapicontract.dto.*;
import com.github.egorkrylov.bookingapicontract.endpoints.BookingApi;
import com.github.egorkrylov.bookingrest.assemblers.BookingModelAssembler;
import com.github.egorkrylov.bookingrest.service.BookingService;
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
public class BookingController implements BookingApi {

    private final BookingService bookingService;
    private final BookingModelAssembler bookingModelAssembler;
    private final PagedResourcesAssembler<BookingResponse> pagedBookingAssembler;

    public BookingController(BookingService bookingService,
                             BookingModelAssembler bookingModelAssembler,
                             PagedResourcesAssembler<BookingResponse> pagedBookingAssembler) {
        this.bookingService = bookingService;
        this.bookingModelAssembler = bookingModelAssembler;
        this.pagedBookingAssembler = pagedBookingAssembler;
    }

    @Override
    @PreAuthorize("hasAnyRole('READER','EDITOR')")
    public PagedModel<EntityModel<BookingResponse>> getAll(int page, int size) {
        PagedResponse<BookingResponse> bookingsResp = bookingService.findAll(null, page, size);
        Page<BookingResponse> pageResp = new PageImpl<>(
                bookingsResp.content(),
                PageRequest.of(page, size),
                bookingsResp.totalElements()
        );

        return pagedBookingAssembler.toModel(pageResp, bookingModelAssembler);
    }

    @Override
    @PreAuthorize("hasAnyRole('READER','EDITOR')")
    public EntityModel<BookingResponse> getBookingById(UUID id) {
        BookingResponse bookingResponse = bookingService.findById(id);

        return bookingModelAssembler.toModel(bookingResponse);
    }

    @Override
    @PreAuthorize("hasAnyRole('EDITOR')")
    public ResponseEntity<EntityModel<BookingResponse>> createBooking(BookingRequest request) {
        BookingResponse bookingResponse = bookingService.create(request);
        EntityModel<BookingResponse> entityBooking = bookingModelAssembler.toModel(bookingResponse);

        return ResponseEntity
                .created(entityBooking.getRequiredLink("self").toUri())
                .body(entityBooking);
    }

    @Override
    @PreAuthorize("hasAnyRole('EDITOR')")
    public EntityModel<BookingResponse> updateBooking(UUID id, UpdateBookingRequest request) {
        return bookingModelAssembler.toModel(bookingService.update(id, request));
    }

    @Override
    @PreAuthorize("hasAnyRole('EDITOR')")
    public EntityModel<BookingResponse> patchBooking(UUID id, PatchBookingRequest request) {
        return bookingModelAssembler.toModel(bookingService.patch(id, request));
    }

    @Override
    @PreAuthorize("hasAnyRole('EDITOR')")
    public void deleteBooking(UUID id) {
        bookingService.delete(id);
    }
}
