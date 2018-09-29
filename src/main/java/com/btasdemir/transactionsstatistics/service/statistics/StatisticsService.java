package com.btasdemir.transactionsstatistics.service.statistics;

import com.btasdemir.transactionsstatistics.converter.SummaryStatisticsToStatisticsResponseConverter;
import com.btasdemir.transactionsstatistics.model.response.StatisticsResponse;
import com.btasdemir.transactionsstatistics.service.TransactionService;
import com.btasdemir.transactionsstatistics.statistics.BigDecimalSummaryStatistics;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    private final TransactionService transactionService;
    private final SummaryStatisticsToStatisticsResponseConverter converter;

    public StatisticsService(TransactionService transactionService,
                             SummaryStatisticsToStatisticsResponseConverter converter) {
        this.transactionService = transactionService;
        this.converter = converter;
    }

    public StatisticsResponse calculateStatistics() {
        return converter.convert(transactionService.getNotStaledTransactionsAmountsStreamAndDiscardRest()
                .collect(BigDecimalSummaryStatistics.collect()));
    }

}
