package com.github.egorkrylov.bookingrest.graphql.types;

public record PagedInfoGql (
        int pageNumber,
        int pageSize,
        int totalPages,
        boolean last
) {
}

