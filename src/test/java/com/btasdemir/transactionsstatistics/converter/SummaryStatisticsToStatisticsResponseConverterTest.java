package com.btasdemir.transactionsstatistics.converter;

import com.btasdemir.transactionsstatistics.model.response.StatisticsResponse;
import com.btasdemir.transactionsstatistics.statistics.BigDecimalSummaryStatistics;
import com.btasdemir.transactionsstatistics.utils.DecimalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SummaryStatisticsToStatisticsResponseConverterTest {

    @InjectMocks
    private SummaryStatisticsToStatisticsResponseConverter converter;

    @Mock
    private DecimalUtils decimalUtils;

    @Test
    public void it_should_convert_big_decimal_summar_statistics_to_statistics_response() {
        //Given
        BigDecimalSummaryStatistics summaryStatistics = Arrays.asList(new BigDecimal("10.00"),
                new BigDecimal("20.00")).stream()
                .collect(BigDecimalSummaryStatistics.collect());

        ArgumentCaptor<BigDecimal> bigDecimalArgumentCaptor = ArgumentCaptor.forClass(BigDecimal.class);

        given(decimalUtils.convert(any(BigDecimal.class))).willReturn("10.00", "20.00", "15.00", "30.00");

        //When
        StatisticsResponse statisticsResponse = converter.convert(summaryStatistics);

        //Then
        assertThat(statisticsResponse.getMin()).isEqualTo("10.00");
        assertThat(statisticsResponse.getMax()).isEqualTo("20.00");
        assertThat(statisticsResponse.getAvg()).isEqualTo("15.00");
        assertThat(statisticsResponse.getSum()).isEqualTo("30.00");
        assertThat(statisticsResponse.getCount()).isEqualTo(2);

        verify(decimalUtils, times(4)).convert(bigDecimalArgumentCaptor.capture());
        List<BigDecimal> capturedBigDecimalValues = bigDecimalArgumentCaptor.getAllValues();
        assertThat(capturedBigDecimalValues.get(0)).isEqualTo("10.00");
        assertThat(capturedBigDecimalValues.get(1)).isEqualTo("20.00");
        assertThat(capturedBigDecimalValues.get(2)).isEqualTo("15.00");
        assertThat(capturedBigDecimalValues.get(3)).isEqualTo("30.00");
    }
}