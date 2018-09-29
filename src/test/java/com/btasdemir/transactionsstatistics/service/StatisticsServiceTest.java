package com.btasdemir.transactionsstatistics.service;

import com.btasdemir.transactionsstatistics.converter.SummaryStatisticsToStatisticsResponseConverter;
import com.btasdemir.transactionsstatistics.model.response.StatisticsResponse;
import com.btasdemir.transactionsstatistics.service.statistics.StatisticsService;
import com.btasdemir.transactionsstatistics.statistics.BigDecimalSummaryStatistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceTest {

    @InjectMocks
    private StatisticsService statisticsService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private SummaryStatisticsToStatisticsResponseConverter converter;

    @Test
    public void it_should_calculate_statistics_correctly() {
        //Given
        Stream<BigDecimal> notStaledTransactionsAmountsStream = Arrays.asList(new BigDecimal("10.00"),
                new BigDecimal("20.00")).stream();
        StatisticsResponse statisticsResponse = new StatisticsResponse();

        ArgumentCaptor<BigDecimalSummaryStatistics> summaryStatisticsArgumentCaptor = ArgumentCaptor
                .forClass(BigDecimalSummaryStatistics.class);

        given(transactionService.getNotStaledTransactionsAmountsStreamAndDiscardRest())
                .willReturn(notStaledTransactionsAmountsStream);
        given(converter.convert(any(BigDecimalSummaryStatistics.class))).willReturn(statisticsResponse);

        //When
        StatisticsResponse retrievedStatistics = statisticsService.calculateStatistics();

        //Then
        assertThat(retrievedStatistics).isEqualTo(statisticsResponse);

        verify(converter).convert(summaryStatisticsArgumentCaptor.capture());
        BigDecimalSummaryStatistics summaryStatistics = summaryStatisticsArgumentCaptor.getValue();
        assertThat(summaryStatistics.getMin()).isEqualTo("10.00");
        assertThat(summaryStatistics.getMax()).isEqualTo("20.00");
        assertThat(summaryStatistics.getAverage()).isEqualTo("15.00");
        assertThat(summaryStatistics.getSum()).isEqualTo("30.00");
        assertThat(summaryStatistics.getCount()).isEqualTo(2);
    }
}