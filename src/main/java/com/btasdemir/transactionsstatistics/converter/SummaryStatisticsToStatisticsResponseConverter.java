package com.btasdemir.transactionsstatistics.converter;

import com.btasdemir.transactionsstatistics.model.response.StatisticsResponse;
import com.btasdemir.transactionsstatistics.statistics.BigDecimalSummaryStatistics;
import com.btasdemir.transactionsstatistics.utils.DecimalUtils;
import org.springframework.stereotype.Component;

@Component
public class SummaryStatisticsToStatisticsResponseConverter {

    private final DecimalUtils decimalUtils;

    public SummaryStatisticsToStatisticsResponseConverter(DecimalUtils decimalUtils) {
        this.decimalUtils = decimalUtils;
    }

    public StatisticsResponse convert(BigDecimalSummaryStatistics summaryStatistics) {
        StatisticsResponse statisticsResponse = new StatisticsResponse();

        statisticsResponse.setMin(decimalUtils.convert(summaryStatistics.getMin()));
        statisticsResponse.setMax(decimalUtils.convert(summaryStatistics.getMax()));
        statisticsResponse.setAvg(decimalUtils.convert(summaryStatistics.getAverage()));
        statisticsResponse.setSum(decimalUtils.convert(summaryStatistics.getSum()));
        statisticsResponse.setCount(summaryStatistics.getCount());

        return statisticsResponse;
    }

}
