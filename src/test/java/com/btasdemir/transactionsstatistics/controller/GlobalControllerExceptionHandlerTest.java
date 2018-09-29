package com.btasdemir.transactionsstatistics.controller;

import com.btasdemir.transactionsstatistics.model.exception.TransactionExceedTimeThresholdException;
import com.btasdemir.transactionsstatistics.model.exception.TransactionRequestFieldsAreNotParsableException;
import com.btasdemir.transactionsstatistics.model.exception.TransactionRequestIsInFutureException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

@RunWith(MockitoJUnitRunner.class)
public class GlobalControllerExceptionHandlerTest {

    @InjectMocks
    private GlobalControllerExceptionHandler globalControllerExceptionHandler;

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Test
    public void it_should_handle_transaction_exceed_time_threshold_exception() {
        //Given
        TransactionExceedTimeThresholdException exception =
                new TransactionExceedTimeThresholdException();

        //When
        ResponseEntity<Void> responseEntity = globalControllerExceptionHandler
                .handleTransactionExceedTimeThresholdException(exception);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        outputCapture.expect(containsString(
                "TransactionExceedTimeThresholdException exception happened."));
    }

    @Test
    public void it_should_handle_transaction_request_fields_are_not_parsable_exception() {
        //Given
        TransactionRequestFieldsAreNotParsableException exception =
                new TransactionRequestFieldsAreNotParsableException();

        //When
        ResponseEntity<Void> responseEntity = globalControllerExceptionHandler
                .handleTransactionRequestFieldsAreNotParsableException(exception);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        outputCapture.expect(containsString(
                "TransactionRequestFieldsAreNotParsableException exception happened."));
    }

    @Test
    public void it_should_handle_transaction_request_is_in_future_exception() {
        //Given
        TransactionRequestIsInFutureException exception =
                new TransactionRequestIsInFutureException();

        //When
        ResponseEntity<Void> responseEntity = globalControllerExceptionHandler
                .handleTransactionRequestIsInFutureException(exception);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        outputCapture.expect(containsString(
                "TransactionRequestIsInFutureException exception happened."));
    }
}