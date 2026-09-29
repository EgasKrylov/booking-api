package com.github.egorkrylov.statisticsservice.model;

import java.time.Instant;

public record StatisticEntry(

        long sequenceNumber,

        String eventId,

        String eventType,

        String source,

        Instant eventTimestamp,

        Instant receivedAt,

        String description

) {}
