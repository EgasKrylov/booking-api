CREATE TABLE rooms (
    id UUID PRIMARY KEY,
    room_number INT UNIQUE NOT NULL ,
    room_type VARCHAR(32) NOT NULL ,
    capacity SMALLINT NOT NULL ,
    price NUMERIC(10, 2) NOT NULL ,
    description TEXT,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE guests (
    id UUID PRIMARY KEY,
    first_name VARCHAR(128) NOT NULL ,
    last_name VARCHAR(128) NOT NULL,
    email VARCHAR(255) UNIQUE,
    birth_date DATE NOT NULL,
    passport_series VARCHAR(4) NOT NULL,
    passport_number VARCHAR(6) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE bookings (
    id UUID PRIMARY KEY,
    guest_id UUID NOT NULL REFERENCES guests(id),
    room_id UUID NOT NULL REFERENCES rooms(id),
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    guest_count SMALLINT NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    update_at TIMESTAMP
);

CREATE INDEX idx_bookings_guest_id ON bookings(guest_id);

CREATE INDEX idx_bookings_room_id ON bookings(room_id);