package com.github.egorkrylov.statisticsservice.controller;

import com.github.egorkrylov.statisticsservice.model.StatisticEntry;
import com.github.egorkrylov.statisticsservice.storage.StatisticStorage;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistic")
public class StatisticController {

    private final StatisticStorage storage;

    public StatisticController(StatisticStorage storage) {
        this.storage = storage;
    }

    @GetMapping()
    public Map<String, Object> getLog(@RequestParam(defaultValue = "100")  int limit) {
        List<StatisticEntry> entries = storage.findLatest(limit);

        return Map.of(
                "totalEntries", storage.count(),
                "showing", entries.size(),
                "entries", entries
        );
    }
}
