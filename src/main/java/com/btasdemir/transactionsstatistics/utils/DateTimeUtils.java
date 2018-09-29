package com.btasdemir.transactionsstatistics.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DateTimeUtils {

    @Value("${stale.threshold.millis}")
    private Long staleThresholdMillis;

    private static final DateTimeZone DATE_TIME_ZONE = DateTimeZone.UTC;

    public DateTime parse(String date) {
        return new DateTime(date, DATE_TIME_ZONE);
    }

    public DateTime getCurrentDateTime() {
        return new DateTime(DATE_TIME_ZONE);
    }

    public String getCurrentDateTimeAsString() {
        return getCurrentDateTime().toString();
    }

    public boolean isInFuture(DateTime dateTime) {
        DateTime currentDateTime = getCurrentDateTime();
        return currentDateTime.getMillis() - dateTime.getMillis() < 0;
    }

    public boolean isExceedThreshold(DateTime dateTime) {
        DateTime currentDateTime = getCurrentDateTime();
        return currentDateTime.getMillis() - dateTime.getMillis() > staleThresholdMillis;
    }

}
