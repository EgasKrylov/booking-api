package com.github.egorkrylov.bookingrest.service;

import com.github.egorkrylov.bookingapicontract.dto.GuestRequest;
import com.github.egorkrylov.bookingapicontract.dto.GuestResponse;
import com.github.egorkrylov.bookingapicontract.dto.PagedResponse;
import com.github.egorkrylov.bookingapicontract.dto.PatchGuestRequest;
import com.github.egorkrylov.bookingapicontract.exception.ResourceNotFoundException;
import com.github.egorkrylov.bookingrest.domain.Guest;
import com.github.egorkrylov.bookingrest.event.GuestEventPublisher;
import com.github.egorkrylov.bookingrest.repository.GuestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class GuestService {


    private final GuestRepository guestRepository;
    private final GuestEventPublisher publisher;

    public GuestService(GuestRepository guestRepository, GuestEventPublisher publisher) {
        this.guestRepository = guestRepository;
        this.publisher = publisher;
    }

    public PagedResponse<GuestResponse> findAll(int page, int size) {
        List<GuestResponse> all = guestRepository.findAll().stream()
                .map(g -> new GuestResponse(
                        g.getId(),
                        g.getFirstName(),
                        g.getLastName(),
                        g.getEmail(),
                        g.getBirthDate(),
                        g.getPassportSeries(),
                        g.getPassportNumber(),
                        g.getVersion()
                )).toList();

        int totalElements = all.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<GuestResponse> content = (from >= totalElements) ? List.of() : all.subList(from, to);

        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

//    public GuestResponse findById(Long id) {
//        return Optional.ofNullable(storage.guests.get(id))
//                .orElseThrow(() -> new ResourceNotFoundException("Guest", id));
//    }

    @Transactional(readOnly = true)
    public GuestResponse findById(UUID id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", id));

        return GuestResponse.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .email(guest.getEmail())
                .birthDate(guest.getBirthDate())
                .passportSeries(guest.getPassportSeries())
                .passportNumber(guest.getPassportNumber())
                .version(guest.getVersion())
                .build();
    }


    @Transactional
    public GuestResponse create(GuestRequest request) {

        Guest guest = Guest.builder()
                .id(UUID.randomUUID())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .birthDate(request.birthDate())
                .passportSeries(request.passportSeries())
                .passportNumber(request.passportNumber())
                .build();

        Guest saved = guestRepository.save(guest);

        GuestResponse response = GuestResponse.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .birthDate(saved.getBirthDate())
                .passportSeries(saved.getPassportSeries())
                .passportNumber(saved.getPassportNumber())
                .version(saved.getVersion())
                .build();

        publisher.publishCreated(response);
        return response;
    }



    @Transactional
    public GuestResponse update(UUID id, GuestRequest request) {
        Guest existing = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", id));

        Guest guest = Guest.builder()
                .id(id)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .birthDate(request.birthDate())
                .passportSeries(request.passportSeries())
                .passportNumber(request.passportNumber())
                .build();

        Guest saved = guestRepository.save(guest);

        GuestResponse response = GuestResponse.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .birthDate(saved.getBirthDate())
                .passportSeries(saved.getPassportSeries())
                .passportNumber(saved.getPassportNumber())
                .version(saved.getVersion() + 1)
                .build();

        publisher.publishUpdated(response);

        return response;

    }


    @Transactional
    public GuestResponse patch(UUID id, PatchGuestRequest request) {
        Guest existing = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", id));

        Guest guest = Guest.builder()
                .id(existing.getId())
                .firstName(request.firstName() != null ? request.firstName() : existing.getFirstName())
                .lastName(request.lastName() != null ? request.lastName() : existing.getLastName())
                .email(request.email() != null ? request.email() : existing.getEmail())
                .birthDate(request.birthDate() != null ? request.birthDate() : existing.getBirthDate())
                .passportSeries(request.passportSeries() != null ? request.passportSeries() : existing.getPassportSeries())
                .passportNumber(request.passportNumber() != null ? request.passportNumber() : existing.getPassportNumber())
                .version(existing.getVersion())
                .build();

        Guest saved = guestRepository.save(guest);

        return GuestResponse.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .birthDate(saved.getBirthDate())
                .passportSeries(saved.getPassportSeries())
                .passportNumber(saved.getPassportNumber())
                .version(saved.getVersion() + 1)
                .build();
    }


    @Transactional
    public void delete(UUID id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", id));

        GuestResponse response = GuestResponse.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .email(guest.getEmail())
                .birthDate(guest.getBirthDate())
                .passportSeries(guest.getPassportSeries())
                .passportNumber(guest.getPassportNumber())
                .version(guest.getVersion())
                .build();

        guestRepository.delete(guest);
        publisher.publishDeleted(response);

    }


}
