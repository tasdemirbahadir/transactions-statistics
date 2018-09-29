package com.btasdemir.transactionsstatistics.util;

import com.btasdemir.transactionsstatistics.utils.DecimalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(MockitoJUnitRunner.class)
public class DecimalUtilsTest {

    @InjectMocks
    private DecimalUtils decimalUtils;

    @Test
    public void it_should_parse_big_decimal() {
        //Given
        String bigDecimalValue1 = "10.345";
        String bigDecimalValue2 = "10.8";

        //When
        BigDecimal parsedBigDecimalValue1 = decimalUtils.parse(bigDecimalValue1);
        BigDecimal parsedBigDecimalValue2 = decimalUtils.parse(bigDecimalValue2);

        //Then
        assertThat(parsedBigDecimalValue1).isEqualTo("10.345");
        assertThat(parsedBigDecimalValue2).isEqualTo("10.8");
    }

    @Test
    public void it_should_throw_number_format_exception_when_given_value_is_not_a_valid_big_decimal() {
        //Given
        String bigDecimalValue1 = "10a345";

        //When
        NumberFormatException exception = (NumberFormatException) catchThrowable(() -> decimalUtils.parse(bigDecimalValue1));

        //Then
        assertThat(exception).isNotNull();
    }

    @Test
    public void it_should_convert_big_decimal_to_string_with_rounded_half_up_and_with_two_decimal_places() {
        //Given
        BigDecimal bigDecimal1 = new BigDecimal("12.34789");
        BigDecimal bigDecimal2 = new BigDecimal("12.344123");
        BigDecimal bigDecimal3 = new BigDecimal("12");

        //When
        String bigDecimalStringValue1 = decimalUtils.convert(bigDecimal1);
        String bigDecimalStringValue2 = decimalUtils.convert(bigDecimal2);
        String bigDecimalStringValue3 = decimalUtils.convert(bigDecimal3);

        //Then
        assertThat(bigDecimalStringValue1).isEqualTo("12.35");
        assertThat(bigDecimalStringValue2).isEqualTo("12.34");
        assertThat(bigDecimalStringValue3).isEqualTo("12.00");
    }
}