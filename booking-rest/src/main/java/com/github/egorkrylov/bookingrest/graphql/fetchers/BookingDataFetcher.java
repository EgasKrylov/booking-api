package com.github.egorkrylov.bookingrest.graphql.fetchers;

import com.github.egorkrylov.bookingapicontract.dto.*;
import com.github.egorkrylov.bookingrest.graphql.types.*;
import com.github.egorkrylov.bookingrest.service.BookingService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.UUID;

@DgsComponent
public class BookingDataFetcher {

    private final BookingService bookingService;

    public BookingDataFetcher(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @DgsQuery
    public BookingResponse booking(@InputArgument String id) {
        return bookingService.findById(UUID.fromString(id));
    }

    @DgsQuery
    public BookingConnectionGql bookings(@InputArgument Integer page, @InputArgument Integer size) {

        page = page == null ? 0 : page;
        size = size == null ? 20 : size;

        PagedResponse<BookingResponse> paged = bookingService.findAll(null, page, size);

        return new BookingConnectionGql(
                paged.content(),
                new PagedInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements()
        );
    }

    @DgsMutation
    public BookingResponse createBooking(@InputArgument CreateBookingInputGql input) {
        BookingRequest request = new BookingRequest(
                input.guestId(),
                input.roomId(),
                input.checkInDate(),
                input.checkOutDate(),
                input.guestCount()
        );

        return bookingService.create(request);
    }

    @DgsMutation
    public BookingResponse updateBooking(@InputArgument String id, UpdateBookingInputGql input) {
        UpdateBookingRequest updateReq = new UpdateBookingRequest(
                input.roomId(),
                input.checkInDate(),
                input.checkOutDate(),
                input.guestCount()
        );

        return bookingService.update(UUID.fromString(id), updateReq);
    }

    @DgsMutation
    public BookingResponse patchBooking(@InputArgument String id, PatchBookingInputGql input) {
        PatchBookingRequest patchReq = new PatchBookingRequest(
                input.roomId(),
                input.checkInDate(),
                input.checkOutDate(),
                input.guestCount()
        );

        return bookingService.patch(UUID.fromString(id), patchReq);
    }

    @DgsMutation
    public boolean deleteBooking(@InputArgument String id) {
        bookingService.delete(UUID.fromString(id));
        return true;
    }
}
