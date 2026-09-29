package com.github.egorkrylov.bookingrest.graphql.fetchers;

import com.github.egorkrylov.bookingapicontract.dto.GuestRequest;
import com.github.egorkrylov.bookingapicontract.dto.GuestResponse;
import com.github.egorkrylov.bookingapicontract.dto.PagedResponse;
import com.github.egorkrylov.bookingapicontract.dto.PatchGuestRequest;
import com.github.egorkrylov.bookingrest.graphql.types.*;
import com.github.egorkrylov.bookingrest.service.GuestService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.UUID;

@DgsComponent
public class GuestDataFetcher {

    private final GuestService guestService;

    public GuestDataFetcher(GuestService guestService) {
        this.guestService = guestService;
    }


    @DgsQuery
    public GuestResponse guest(@InputArgument String id) {
        UUID guestId = UUID.fromString(id);

        return guestService.findById(guestId);
    }

    @DgsQuery
    public GuestConnectionGql guests(@InputArgument Integer page, @InputArgument Integer size) {

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PagedResponse<GuestResponse> guests = guestService.findAll(pageNum, pageSize);

        return new GuestConnectionGql(
                guests.content(),
                new PagedInfoGql(guests.pageNumber(), guests.pageSize(), guests.totalPages(), guests.last()),
                (int) guests.totalElements()
        );
    }

    @DgsMutation
    public GuestResponse createGuest(@InputArgument CreateGuestInputGql input) {
        GuestRequest request = new GuestRequest(
                input.firstName(),
                input.lastName(),
                input.email(),
                input.birthDate(),
                input.passportSeries(),
                input.passportNumber()
        );

        return guestService.create(request);
    }

    @DgsMutation
    public GuestResponse updateGuest(
            @InputArgument String id,
            @InputArgument UpdateGuestInputGql input
    ) {
        GuestRequest guestRequest = new GuestRequest(
                input.firstName(),
                input.lastName(),
                input.email(),
                input.birthDate(),
                input.passportSeries(),
                input.passportNumber()
        );

        return guestService.update(UUID.fromString(id), guestRequest);
    }

    @DgsMutation
    public GuestResponse patchGuest(
            @InputArgument String id,
            @InputArgument PatchGuestInputGql input
            ) {
        PatchGuestRequest guestRequest = new PatchGuestRequest(
                input.firstName(),
                input.lastName(),
                input.email(),
                input.birthDate(),
                input.passportSeries(),
                input.passportNumber()
        );

        return guestService.patch(UUID.fromString(id), guestRequest);
    }

    @DgsMutation
    public boolean deleteGuest(@InputArgument String id) {
        guestService.delete(UUID.fromString(id));
        return true;
    }


}
