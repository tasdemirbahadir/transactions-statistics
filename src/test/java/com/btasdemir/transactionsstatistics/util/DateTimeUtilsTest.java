package com.btasdemir.transactionsstatistics.util;

import com.btasdemir.transactionsstatistics.utils.DateTimeUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeUtilsTest {

    @InjectMocks
    private DateTimeUtils dateTimeUtils;

    private DateTimeTestUtils dateTimeTestUtils = new DateTimeTestUtils();

    @Test
    public void it_should_convert_to_date_time_properly() {
        //Given
        String dateValue = "2018-07-17T09:59:51.312Z";

        //When
        DateTime dateTime = dateTimeUtils.parse(dateValue);

        //Then
        assertThat(dateTime.getYear()).isEqualTo(2018);
        assertThat(dateTime.getMonthOfYear()).isEqualTo(7);
        assertThat(dateTime.getDayOfMonth()).isEqualTo(17);
        assertThat(dateTime.getHourOfDay()).isEqualTo(9);
        assertThat(dateTime.getMinuteOfHour()).isEqualTo(59);
        assertThat(dateTime.getSecondOfMinute()).isEqualTo(51);
        assertThat(dateTime.getMillisOfSecond()).isEqualTo(312);
    }

    @Test
    public void it_should_get_current_date_with_time_zone_in_utc() {
        //Given

        //When
        DateTime currentDateTime = dateTimeUtils.getCurrentDateTime();

        //Then
        assertThat(currentDateTime.getZone()).isEqualTo(DateTimeZone.UTC);
    }

    @Test
    public void it_should_get_current_date_time_as_string() {
        //Given

        //When
        String currentDateTimeAsString = dateTimeUtils.getCurrentDateTimeAsString();

        //Then
        assertThat(currentDateTimeAsString).isNotBlank();
    }

    @Test
    public void it_should_return_true_when_the_date_is_in_future() {
        //Given
        DateTime dateTimeInFuture = dateTimeTestUtils.getCurrentDateTime()
                .withDurationAdded(60000, 1);

        //When
        Boolean isDateTimeInFuture = dateTimeUtils.isInFuture(dateTimeInFuture);

        //Then
        assertThat(isDateTimeInFuture).isTrue();
    }

    @Test
    public void it_should_return_true_when_the_date_exceeds_threshold() {
        //Given
        long staleThresholdMillis = 60000L;
        ReflectionTestUtils.setField(dateTimeUtils, "staleThresholdMillis", staleThresholdMillis);
        DateTime dateTimeExceedsThreshold = dateTimeTestUtils.getCurrentDateTime()
                .withDurationAdded(staleThresholdMillis + 1000, -1);

        //When
        Boolean isDateExceedThreshold = dateTimeUtils.isExceedThreshold(dateTimeExceedsThreshold);

        //Then
        assertThat(isDateExceedThreshold).isTrue();
    }
}