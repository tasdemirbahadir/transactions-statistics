package com.btasdemir.transactionsstatistics.domain;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class Transaction {

    private BigDecimal amount;
    private DateTime dateTime;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
