package com.github.egorkrylov.bookingrest.graphql.types;

import com.github.egorkrylov.bookingapicontract.dto.GuestResponse;

import java.util.List;

public record GuestConnectionGql (
        List<GuestResponse> content,
        PagedInfoGql pagedInfoGql,
        int totalElements
)
{ }
