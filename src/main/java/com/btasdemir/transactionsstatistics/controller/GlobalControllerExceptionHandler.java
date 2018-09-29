package com.btasdemir.transactionsstatistics.controller;

import com.btasdemir.transactionsstatistics.model.exception.TransactionExceedTimeThresholdException;
import com.btasdemir.transactionsstatistics.model.exception.TransactionRequestFieldsAreNotParsableException;
import com.btasdemir.transactionsstatistics.model.exception.TransactionRequestIsInFutureException;
import com.btasdemir.transactionsstatistics.model.exception.TransactionsStatisticsApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(TransactionExceedTimeThresholdException.class)
    public ResponseEntity<Void> handleTransactionExceedTimeThresholdException(
            TransactionExceedTimeThresholdException exception) {
        return logErrorAndReturnResponse(exception, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(TransactionRequestFieldsAreNotParsableException.class)
    public ResponseEntity<Void> handleTransactionRequestFieldsAreNotParsableException(
            TransactionRequestFieldsAreNotParsableException exception) {
        return logErrorAndReturnResponse(exception, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TransactionRequestIsInFutureException.class)
    public ResponseEntity<Void> handleTransactionRequestIsInFutureException(
            TransactionRequestIsInFutureException exception) {
        return logErrorAndReturnResponse(exception, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private ResponseEntity<Void> logErrorAndReturnResponse(TransactionsStatisticsApplicationException exception,
                                                           HttpStatus status) {
        LOGGER.error("{} exception happened.", exception.getClass().getSimpleName());
        return new ResponseEntity<>(status);
    }

}
