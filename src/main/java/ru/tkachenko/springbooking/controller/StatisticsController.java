package ru.tkachenko.springbooking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springbooking.service.StatisticService;

@RestController
@RequestMapping("api/stats")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = "Statistics API")
public class StatisticsController {
    private final StatisticService statisticService;

    @Operation(
            summary = "Get statistics",
            description = "Get files with statistics"
    )
    @Parameter(
            name = "pathToFolder",
            description = "Path to the folder for exporting the statistics in CSV format"
    )
    @GetMapping
    public ResponseEntity<Void> exportToCsv(@RequestParam String pathToFolder) {
        statisticService.exportDataToCsv(pathToFolder);
        return ResponseEntity.noContent().build();
    }
}
