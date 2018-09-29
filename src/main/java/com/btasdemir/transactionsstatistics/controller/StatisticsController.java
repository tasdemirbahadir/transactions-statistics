package com.btasdemir.transactionsstatistics.controller;

import com.btasdemir.transactionsstatistics.model.response.StatisticsResponse;
import com.btasdemir.transactionsstatistics.service.statistics.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public StatisticsResponse getStatistics() {
        return statisticsService.calculateStatistics();
    }

}
