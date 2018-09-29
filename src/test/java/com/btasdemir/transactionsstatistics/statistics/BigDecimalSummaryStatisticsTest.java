package com.btasdemir.transactionsstatistics.statistics;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BigDecimalSummaryStatisticsTest {

    @Test
    public void it_should_calculate_big_decimal_summary_statistics_properly() {
        //Given
        List<BigDecimal> bigDecimalValues = Arrays.asList(new BigDecimal("45.83"), new BigDecimal("837.82"),
                new BigDecimal("926.45"), new BigDecimal("6128.36"), new BigDecimal("1.749"));

        //When
        BigDecimalSummaryStatistics summaryStatistics = bigDecimalValues.stream()
                .collect(BigDecimalSummaryStatistics.collect());

        //Then
        assertThat(summaryStatistics.getMin()).isEqualTo("1.749");
        assertThat(summaryStatistics.getMax()).isEqualTo("6128.36");
        assertThat(summaryStatistics.getSum()).isEqualTo("7940.209");
        assertThat(summaryStatistics.getAverage()).isEqualTo("1588.04");
        assertThat(summaryStatistics.getCount()).isEqualTo(5);
    }

    @Test
    public void it_should_calculate_big_decimal_summary_statistics_with_parallel_streaming() {
        //Given
        List<BigDecimal> bigDecimalValues = Arrays.asList(new BigDecimal("45.83"), new BigDecimal("837.82"),
                new BigDecimal("926.45"), new BigDecimal("6128.36"), new BigDecimal("1.749"));

        //When
        BigDecimalSummaryStatistics summaryStatistics = bigDecimalValues.parallelStream()
                .collect(BigDecimalSummaryStatistics.collect());

        //Then
        assertThat(summaryStatistics.getMin()).isEqualTo("1.749");
        assertThat(summaryStatistics.getMax()).isEqualTo("6128.36");
        assertThat(summaryStatistics.getSum()).isEqualTo("7940.209");
        assertThat(summaryStatistics.getAverage()).isEqualTo("1588.04");
        assertThat(summaryStatistics.getCount()).isEqualTo(5);
    }

    @Test
    public void it_should_calculate_big_decimal_summary_statistics_with_average_rounded_up() {
        //Given
        List<BigDecimal> bigDecimalValues = Arrays.asList(new BigDecimal("45.846"), new BigDecimal("837.82"),
                new BigDecimal("926.45"), new BigDecimal("6128.36"), new BigDecimal("1.749"));

        //When
        BigDecimalSummaryStatistics summaryStatistics = bigDecimalValues.stream()
                .collect(BigDecimalSummaryStatistics.collect());

        //Then
        assertThat(summaryStatistics.getMin()).isEqualTo("1.749");
        assertThat(summaryStatistics.getMax()).isEqualTo("6128.36");
        assertThat(summaryStatistics.getSum()).isEqualTo("7940.225");
        assertThat(summaryStatistics.getAverage()).isEqualTo("1588.05");
        assertThat(summaryStatistics.getCount()).isEqualTo(5);
    }

    @Test
    public void it_should_return_zero_values_when_the_list_is_empty() {
        //Given
        List<BigDecimal> bigDecimalValues = Arrays.asList();

        //When
        BigDecimalSummaryStatistics summaryStatistics = bigDecimalValues.stream()
                .collect(BigDecimalSummaryStatistics.collect());

        //Then
        assertThat(summaryStatistics.getMin()).isEqualTo("0");
        assertThat(summaryStatistics.getMax()).isEqualTo("0");
        assertThat(summaryStatistics.getSum()).isEqualTo("0");
        assertThat(summaryStatistics.getAverage()).isEqualTo("0");
        assertThat(summaryStatistics.getCount()).isEqualTo(0);
    }
}