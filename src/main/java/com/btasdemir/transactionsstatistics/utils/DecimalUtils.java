package com.btasdemir.transactionsstatistics.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Component
public class DecimalUtils {

    private static final int DECIMAL_PLACES_COUNT = 2;

    public BigDecimal parse(String value) {
        return new BigDecimal(value);
    }

    public String convert(BigDecimal value) {
        return value.setScale(DECIMAL_PLACES_COUNT, ROUND_HALF_UP).toString();
    }

}
