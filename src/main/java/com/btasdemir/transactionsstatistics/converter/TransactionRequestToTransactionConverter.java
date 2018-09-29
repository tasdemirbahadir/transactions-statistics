package com.btasdemir.transactionsstatistics.converter;

import com.btasdemir.transactionsstatistics.domain.Transaction;
import com.btasdemir.transactionsstatistics.model.request.TransactionRequest;
import com.btasdemir.transactionsstatistics.utils.DateTimeUtils;
import com.btasdemir.transactionsstatistics.utils.DecimalUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionRequestToTransactionConverter {

    private final DecimalUtils decimalUtils;
    private final DateTimeUtils dateTimeUtils;

    public TransactionRequestToTransactionConverter(DecimalUtils decimalUtils, DateTimeUtils dateTimeUtils) {
        this.decimalUtils = decimalUtils;
        this.dateTimeUtils = dateTimeUtils;
    }

    public Transaction convert(TransactionRequest transactionRequest) {
        BigDecimal amount = decimalUtils.parse(transactionRequest.getAmount());
        DateTime transactionDateTime = dateTimeUtils.parse(transactionRequest.getTimestamp());

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDateTime(transactionDateTime);

        return transaction;
    }
}
