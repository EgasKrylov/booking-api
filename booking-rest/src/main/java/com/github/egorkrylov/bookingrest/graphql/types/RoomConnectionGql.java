package com.github.egorkrylov.bookingrest.graphql.types;

import com.github.egorkrylov.bookingapicontract.dto.RoomResponse;

import java.util.List;

public record RoomConnectionGql(
        List<RoomResponse> content,
        PagedInfoGql pagedInfoGql,
        int totalElements
) {
}
