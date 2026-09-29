package com.github.egorkrylov.statisticsservice.storage;

import com.github.egorkrylov.statisticsservice.model.StatisticEntry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class StatisticStorage {

    private final ConcurrentLinkedDeque<StatisticEntry> entries = new ConcurrentLinkedDeque<>();
    private final AtomicLong sequence = new AtomicLong(0);

    private final AtomicLong totalRooms = new AtomicLong(2);
    private final AtomicLong totalGuests = new AtomicLong(3);
    private final AtomicLong totalBookings = new AtomicLong(1);
    private final AtomicLong cancelledBookings = new AtomicLong(0);

    private final Set<String> processedEventIds = ConcurrentHashMap.newKeySet();
    
    public boolean isDuplicate(String eventId) {
        return !processedEventIds.add(eventId);
    }

    public StatisticEntry save(StatisticEntry entry) {
        StatisticEntry numbered = new StatisticEntry(
                sequence.incrementAndGet(),
                entry.eventId(),
                entry.eventType(),
                entry.source(),
                entry.eventTimestamp(),
                entry.receivedAt(),
                entry.description()
        );
        entries.addFirst(numbered);
        return numbered;
    }


    public List<StatisticEntry> findLatest(int limit) {
        return entries.stream()
                .limit(limit)
                .toList();
    }


    public int count() {
        return entries.size();
    }

    public void incrTotalRooms() {
        totalRooms.incrementAndGet();
    }

    public void decrTotalRooms() {
        totalRooms.decrementAndGet();
    }

    public void incrTotalGuests() {
        totalGuests.incrementAndGet();
    }

    public void decrTotalGuests() {
        totalGuests.decrementAndGet();
    }

    public void incrTotalBookings() {
        totalBookings.incrementAndGet();
    }

    public void cancelBookings() {
        totalBookings.decrementAndGet();
        cancelledBookings.incrementAndGet();
    }

    public long getTotalRooms() {
        return totalRooms.get();
    }

    public long getTotalGuests() {
        return totalGuests.get();
    }

    public long getTotalBookings() {
        return totalBookings.get();
    }

    public long getCancelledBookings() {
        return cancelledBookings.get();
    }
}
