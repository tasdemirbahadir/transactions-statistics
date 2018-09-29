package com.btasdemir.transactionsstatistics.validator;

import com.btasdemir.transactionsstatistics.model.exception.TransactionExceedTimeThresholdException;
import com.btasdemir.transactionsstatistics.model.exception.TransactionRequestFieldsAreNotParsableException;
import com.btasdemir.transactionsstatistics.model.exception.TransactionRequestIsInFutureException;
import com.btasdemir.transactionsstatistics.model.request.TransactionRequest;
import com.btasdemir.transactionsstatistics.util.DateTimeTestUtils;
import com.btasdemir.transactionsstatistics.utils.DateTimeUtils;
import com.btasdemir.transactionsstatistics.utils.DecimalUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class TransactionRequestValidatorTest {

    @InjectMocks
    private TransactionRequestValidator transactionRequestValidator;

    @Mock
    private DateTimeUtils dateTimeUtils;

    @Mock
    private DecimalUtils decimalUtils;

    private DateTimeTestUtils dateTimeTestUtils = new DateTimeTestUtils();

    @Test
    public void it_should_throw_fields_not_parsable_exception_if_timestamp_field_is_not_parsable() {
        //Given
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("12.90");
        transactionRequest.setTimestamp("222");

        given(decimalUtils.parse("12.90")).willReturn(new BigDecimal("12.90"));
        given(dateTimeUtils.parse("222")).willThrow(IllegalArgumentException.class);

        //When
        TransactionRequestFieldsAreNotParsableException exception =
                (TransactionRequestFieldsAreNotParsableException) catchThrowable(() ->
                        transactionRequestValidator.validate(transactionRequest));

        //Then
        assertThat(exception).isNotNull();
    }

    @Test
    public void it_should_throw_fields_not_parsable_exception_if_amount_is_not_a_number() {
        //Given
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("not_a_number");
        transactionRequest.setTimestamp("2018-01-01");

        given(decimalUtils.parse("not_a_number")).willThrow(NumberFormatException.class);

        //When
        TransactionRequestFieldsAreNotParsableException exception =
                (TransactionRequestFieldsAreNotParsableException) catchThrowable(() ->
                        transactionRequestValidator.validate(transactionRequest));

        //Then
        assertThat(exception).isNotNull();
    }

    @Test
    public void it_should_throw_exception_if_transaction_is_in_the_future() {
        //Given
        DateTime currentDateTime = dateTimeTestUtils.getCurrentDateTime();

        TransactionRequest transactionRequest = getTransactionRequest();

        given(dateTimeUtils.parse(transactionRequest.getTimestamp())).willReturn(currentDateTime);
        given(dateTimeUtils.isInFuture(currentDateTime)).willReturn(true);

        //When
        TransactionRequestIsInFutureException exception = (TransactionRequestIsInFutureException)
                catchThrowable(() -> transactionRequestValidator.validate(transactionRequest));

        //Then
        assertThat(exception).isNotNull();
    }

    @Test
    public void it_should_throw_exception_if_given_date_exceeds_stale_threshold() {
        //Given
        DateTime currentDateTime = dateTimeTestUtils.getCurrentDateTime();

        TransactionRequest transactionRequest = getTransactionRequest();

        given(dateTimeUtils.parse(transactionRequest.getTimestamp())).willReturn(currentDateTime);
        given(dateTimeUtils.isExceedThreshold(currentDateTime)).willReturn(true);

        //When
        TransactionExceedTimeThresholdException exception =
                (TransactionExceedTimeThresholdException) catchThrowable(() -> transactionRequestValidator
                        .validate(transactionRequest));

        //Then
        assertThat(exception).isNotNull();
    }

    @Test
    public void it_should_not_throw_any_exception_when_the_request_is_valid() {
        //Given

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("12.12");
        transactionRequest.setTimestamp("2018-07-17T09:59:51.312Z");

        DateTime currentDateTime = dateTimeTestUtils.getCurrentDateTime();

        given(dateTimeUtils.isInFuture(currentDateTime)).willReturn(false);
        given(dateTimeUtils.isExceedThreshold(currentDateTime)).willReturn(false);
        given(dateTimeUtils.parse("2018-07-17T09:59:51.312Z")).willReturn(currentDateTime);
        given(decimalUtils.parse("12.12")).willReturn(new BigDecimal("12.12"));

        //When
        transactionRequestValidator.validate(transactionRequest);

        //Then
    }

    private TransactionRequest getTransactionRequest() {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("10.00");
        transactionRequest.setTimestamp("2018-09-23");
        return transactionRequest;
    }
}