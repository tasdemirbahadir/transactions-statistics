package com.btasdemir.transactionsstatistics.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateTimeTestUtils {

    public DateTime getCurrentDateTime() {
        return new DateTime(DateTimeZone.UTC);
    }

}
