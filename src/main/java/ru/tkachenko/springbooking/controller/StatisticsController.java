package ru.tkachenko.springbooking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springbooking.service.StatisticService;

@RestController
@RequestMapping("api/stats")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticService statisticService;

    @GetMapping
    public ResponseEntity<Void> exportToCsv(@RequestParam String pathToFolder) {
        statisticService.exportDataToCsv(pathToFolder);
        return ResponseEntity.noContent().build();
    }
}
