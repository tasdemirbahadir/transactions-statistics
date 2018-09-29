package com.btasdemir.transactionsstatistics.converter;

import com.btasdemir.transactionsstatistics.domain.Transaction;
import com.btasdemir.transactionsstatistics.model.request.TransactionRequest;
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
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class TransactionRequestToTransactionConverterTest {

    @InjectMocks
    private TransactionRequestToTransactionConverter converter;

    @Mock
    private DateTimeUtils dateTimeUtils;

    @Mock
    private DecimalUtils decimalUtils;

    @Test
    public void it_should_convert_transaction_request_to_transaction_properly() {
        //Given
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("11");
        transactionRequest.setTimestamp("2018-11-11");

        DateTime transactionDateTime = new DateTime();
        BigDecimal transactionAmount = new BigDecimal("11");

        given(dateTimeUtils.parse("2018-11-11")).willReturn(transactionDateTime);
        given(decimalUtils.parse("11")).willReturn(transactionAmount);

        //When
        Transaction transaction = converter.convert(transactionRequest);

        //Then
        assertThat(transaction.getAmount()).isEqualTo(transactionAmount);
        assertThat(transaction.getDateTime()).isEqualTo(transactionDateTime);
    }
}