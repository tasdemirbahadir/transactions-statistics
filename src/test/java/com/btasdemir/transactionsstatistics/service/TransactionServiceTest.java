package com.btasdemir.transactionsstatistics.service;

import com.btasdemir.transactionsstatistics.converter.TransactionRequestToTransactionConverter;
import com.btasdemir.transactionsstatistics.domain.Transaction;
import com.btasdemir.transactionsstatistics.model.request.TransactionRequest;
import com.btasdemir.transactionsstatistics.util.DateTimeTestUtils;
import com.btasdemir.transactionsstatistics.utils.DateTimeUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRequestToTransactionConverter converter;

    @Mock
    private DateTimeUtils dateTimeUtils;

    private DateTimeTestUtils dateTimeTestUtils = new DateTimeTestUtils();

    private List<Transaction> transactionsDataList;

    @Before
    public void setup() {
        transactionsDataList = (List<Transaction>) ReflectionTestUtils.getField(transactionService, "transactions");
    }

    @Test
    public void it_should_save_transaction() {
        //Given
        TransactionRequest transactionRequest = new TransactionRequest();
        Transaction transaction = new Transaction();

        given(converter.convert(transactionRequest)).willReturn(transaction);

        //When
        transactionService.save(transactionRequest);

        //Then
        assertThat(transactionsDataList).contains(transaction);
    }

    @Test
    public void it_should_delete_all_transactions() {
        //Given

        //When
        transactionService.deleteAll();

        //Then
        assertThat(transactionsDataList).isEmpty();
    }

    @Test
    public void it_should_get_not_staled_transactions_and_discard_rest() {
        //Given
        long staleThresholdMillis = 60000;

        DateTime currentDateTime = dateTimeTestUtils.getCurrentDateTime();
        DateTime staledDateTime1 = currentDateTime.withDurationAdded(staleThresholdMillis + 1, -1);
        DateTime staledDateTime2 = currentDateTime.withDurationAdded(staleThresholdMillis + 10, -1);
        DateTime staledDateTime3 = currentDateTime.withDurationAdded(staleThresholdMillis * 2, -1);
        DateTime notStaledDateTime1 = currentDateTime.withDurationAdded(staleThresholdMillis, -1);
        DateTime notStaledDateTime2 = currentDateTime.withDurationAdded(staleThresholdMillis / 2, -1);
        DateTime notStaledDateTime3 = currentDateTime.withDurationAdded(staleThresholdMillis - 1, -1);
        DateTime notStaledDateTime4 = currentDateTime.withDurationAdded(staleThresholdMillis / 4, -1);

        Transaction transactionStaled1 = getTransactionWithDateTime(staledDateTime1);
        Transaction transactionStaled2 = getTransactionWithDateTime(staledDateTime2);
        Transaction transactionStaled3 = getTransactionWithDateTime(staledDateTime3);

        Transaction transactionNotStaled1 = getTransactionWith(notStaledDateTime1, new BigDecimal("10.00"));
        Transaction transactionNotStaled2 = getTransactionWith(notStaledDateTime2, new BigDecimal("20.00"));
        Transaction transactionNotStaled3 = getTransactionWith(notStaledDateTime3, new BigDecimal("30.00"));
        Transaction transactionNotStaled4 = getTransactionWith(notStaledDateTime4, new BigDecimal("40.00"));

        transactionsDataList.addAll(Arrays.asList(transactionNotStaled1, transactionNotStaled2, transactionNotStaled3,
                transactionNotStaled4, transactionStaled1, transactionStaled2, transactionStaled3));

        given(dateTimeUtils.isExceedThreshold(staledDateTime1)).willReturn(true);
        given(dateTimeUtils.isExceedThreshold(staledDateTime2)).willReturn(true);
        given(dateTimeUtils.isExceedThreshold(staledDateTime3)).willReturn(true);
        given(dateTimeUtils.isExceedThreshold(notStaledDateTime1)).willReturn(false);
        given(dateTimeUtils.isExceedThreshold(notStaledDateTime2)).willReturn(false);
        given(dateTimeUtils.isExceedThreshold(notStaledDateTime3)).willReturn(false);
        given(dateTimeUtils.isExceedThreshold(notStaledDateTime4)).willReturn(false);

        //When
        List<String> notStaledTransactionsAmounts = transactionService.getNotStaledTransactionsAmountsStreamAndDiscardRest()
                .map(String::valueOf)
                .collect(toList());

        //Then
        assertThat(notStaledTransactionsAmounts).containsExactlyInAnyOrder("10.00", "20.00", "30.00", "40.00");
        assertThat(transactionsDataList).containsExactlyInAnyOrder(transactionNotStaled1, transactionNotStaled2,
                transactionNotStaled3, transactionNotStaled4);
    }

    private Transaction getTransactionWithDateTime(DateTime dateTime) {
        Transaction transactionWithDateTime = new Transaction();
        transactionWithDateTime.setDateTime(dateTime);
        return transactionWithDateTime;
    }

    private Transaction getTransactionWith(DateTime dateTime, BigDecimal amount) {
        Transaction transaction = getTransactionWithDateTime(dateTime);
        transaction.setAmount(amount);
        return transaction;
    }
}