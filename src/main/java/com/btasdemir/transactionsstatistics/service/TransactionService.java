package com.btasdemir.transactionsstatistics.service;

import com.btasdemir.transactionsstatistics.converter.TransactionRequestToTransactionConverter;
import com.btasdemir.transactionsstatistics.domain.Transaction;
import com.btasdemir.transactionsstatistics.model.request.TransactionRequest;
import com.btasdemir.transactionsstatistics.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TransactionService {
    private final TransactionRequestToTransactionConverter converter;
    private final DateTimeUtils dateTimeUtils;

    private final List<Transaction> transactions;

    public TransactionService(TransactionRequestToTransactionConverter converter,
                              DateTimeUtils dateTimeUtils) {
        this.converter = converter;
        this.dateTimeUtils = dateTimeUtils;
        this.transactions = Collections.synchronizedList(new ArrayList<>());
    }

    public void save(TransactionRequest transactionRequest) {
        transactions.add(converter.convert(transactionRequest));
    }

    public void deleteAll() {
        transactions.clear();
    }

    public Stream<BigDecimal> getNotStaledTransactionsAmountsStreamAndDiscardRest() {
        List<Transaction> transactionsStaled = transactions.stream()
                .filter(this::isTransactionStaled)
                .collect(Collectors.toList());
        this.transactions.removeAll(transactionsStaled);
        return this.transactions.stream().map(Transaction::getAmount);
    }

    private boolean isTransactionStaled(Transaction transaction) {
        return dateTimeUtils.isExceedThreshold(transaction.getDateTime());
    }

}