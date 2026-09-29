package com.github.egorkrylov.bookingrest.graphql.types;

import com.github.egorkrylov.bookingapicontract.dto.BookingResponse;

import java.util.List;

public record BookingConnectionGql(
        List<BookingResponse> content,
        PagedInfoGql pagedInfo,
        int totalElements
) {
}
