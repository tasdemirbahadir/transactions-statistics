package com.btasdemir.transactionsstatistics.validator;

import com.btasdemir.transactionsstatistics.model.exception.TransactionExceedTimeThresholdException;
import com.btasdemir.transactionsstatistics.model.exception.TransactionRequestFieldsAreNotParsableException;
import com.btasdemir.transactionsstatistics.model.exception.TransactionRequestIsInFutureException;
import com.btasdemir.transactionsstatistics.model.request.TransactionRequest;
import com.btasdemir.transactionsstatistics.utils.DateTimeUtils;
import com.btasdemir.transactionsstatistics.utils.DecimalUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
public class TransactionRequestValidator {

    private final DateTimeUtils dateTimeUtils;
    private final DecimalUtils decimalUtils;

    public TransactionRequestValidator(DateTimeUtils dateTimeUtils, DecimalUtils decimalUtils) {
        this.dateTimeUtils = dateTimeUtils;
        this.decimalUtils = decimalUtils;
    }

    public void validate(TransactionRequest transactionRequest) {
        validateIfFieldsAreParsable(transactionRequest);
        validateIfTransactionDateIsNotInFuture(transactionRequest);
        validateIfRequestNotExceedTimeThreshold(transactionRequest);
    }

    private void validateIfFieldsAreParsable(TransactionRequest transactionRequest) {
        validateAmountIfParsable(transactionRequest.getAmount());
        validateTimestampIfParsable(transactionRequest.getTimestamp());
    }

    private void validateAmountIfParsable(String transactionAmount) {
        try {
            decimalUtils.parse(transactionAmount);
        } catch (NumberFormatException ex) {
            throw new TransactionRequestFieldsAreNotParsableException();
        }
    }

    private void validateTimestampIfParsable(String transactionTimestamp) {
        try {
            dateTimeUtils.parse(transactionTimestamp);
        } catch (IllegalArgumentException ex) {
            throw new TransactionRequestFieldsAreNotParsableException();
        }
    }

    private void validateIfTransactionDateIsNotInFuture(TransactionRequest transactionRequest) {
        DateTime dateTime = dateTimeUtils.parse(transactionRequest.getTimestamp());
        if (dateTimeUtils.isInFuture(dateTime)) {
            throw new TransactionRequestIsInFutureException();
        }
    }

    private void validateIfRequestNotExceedTimeThreshold(TransactionRequest transactionRequest) {
        DateTime date = dateTimeUtils.parse(transactionRequest.getTimestamp());
        if (dateTimeUtils.isExceedThreshold(date)) {
            throw new TransactionExceedTimeThresholdException();
        }
    }

}
