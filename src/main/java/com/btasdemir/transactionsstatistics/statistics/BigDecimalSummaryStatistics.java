package com.btasdemir.transactionsstatistics.statistics;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Collector;

public class BigDecimalSummaryStatistics implements Consumer<BigDecimal> {

    public static Collector<BigDecimal, ?, BigDecimalSummaryStatistics> collect() {
        return Collector.of(BigDecimalSummaryStatistics::new, BigDecimalSummaryStatistics::accept,
                BigDecimalSummaryStatistics::merge);
    }

    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal min = BigDecimal.ZERO;
    private BigDecimal max = BigDecimal.ZERO;
    private long count = 0;

    public void accept(BigDecimal value) {
        if (count == 0) {
            initValues(value);
        } else {
            calculateStep(value);
        }
    }

    private void initValues(BigDecimal value) {
        count = 1;
        sum = value;
        min = value;
        max = value;
    }

    private void calculateStep(BigDecimal value) {
        sum = sum.add(value);
        if (min.compareTo(value) > 0) {
            min = value;
        }
        if (max.compareTo(value) < 0) {
            max = value;
        }
        count++;
    }

    public BigDecimalSummaryStatistics merge(BigDecimalSummaryStatistics summaryStatistics) {
        sum = sum.add(summaryStatistics.sum);
        if (min.compareTo(summaryStatistics.min) > 0) {
            min = summaryStatistics.min;
        }
        if (max.compareTo(summaryStatistics.max) < 0) {
            max = summaryStatistics.max;
        }
        count += summaryStatistics.count;
        return this;
    }

    public long getCount() {
        return count;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public BigDecimal getAverage() {
        return count < 2 ? sum : sum.divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getMax() {
        return max;
    }
}
