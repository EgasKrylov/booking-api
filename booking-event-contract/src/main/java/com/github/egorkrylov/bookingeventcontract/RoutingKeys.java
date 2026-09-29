package com.github.egorkrylov.bookingeventcontract;

public final class RoutingKeys {

    public static final String EXCHANGE = "booking.events";

    public static final String GUEST_CREATED = "guest.created";
    public static final String GUEST_UPDATED = "guest.updated";
    public static final String GUEST_DELETED = "guest.deleted";

    public static final String BOOKING_CREATED = "booking.created";
    public static final String BOOKING_UPDATED = "booking.updated";
    public static final String BOOKING_DELETED = "booking.deleted";

    public static final String ROOM_CREATED = "room.created";
    public static final String ROOM_UPDATED = "room.updated";
    public static final String ROOM_DELETED = "room.deleted";
    public static final String ROOM_ENRICHED = "room.enriched";


    public static final String ALL_GUEST_EVENTS = "guest.*";
    public static final String ALL_BOOKING_EVENTS = "booking.*";
    public static final String ALL_ROOM_EVENTS = "room.*";
    public static final String ALL_EVENTS = "#";



}
