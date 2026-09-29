package com.github.egorkrylov.bookingrest.graphql.fetchers;

import com.github.egorkrylov.bookingapicontract.dto.BookingResponse;
import com.github.egorkrylov.bookingapicontract.dto.GuestResponse;
import com.github.egorkrylov.bookingapicontract.dto.PagedResponse;
import com.github.egorkrylov.bookingrest.graphql.types.BookingConnectionGql;
import com.github.egorkrylov.bookingrest.graphql.types.PagedInfoGql;
import com.github.egorkrylov.bookingrest.service.BookingService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.InputArgument;


@DgsComponent
public class GuestBookingsDataFetcher {

    private final BookingService bookingService;

    public GuestBookingsDataFetcher(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @DgsData(parentType = "Guest", field = "bookings")
    public BookingConnectionGql bookings (
            DgsDataFetchingEnvironment dfe,
            @InputArgument Integer page,
            @InputArgument Integer size) {

        GuestResponse guest = dfe.getSource();

        page = page == null ? 0 : page;
        size = size == null ? 20 : size;

        PagedResponse<BookingResponse> bookings = bookingService.findAll(guest.getId(), page, size);

        return new BookingConnectionGql(
                bookings.content(),
                new PagedInfoGql(bookings.pageNumber(), bookings.pageSize(), bookings.totalPages(), bookings.last()),
                (int) bookings.totalElements()
        );
    }
}
