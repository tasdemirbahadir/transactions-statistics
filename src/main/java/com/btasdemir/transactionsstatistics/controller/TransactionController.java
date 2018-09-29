package com.btasdemir.transactionsstatistics.controller;

import com.btasdemir.transactionsstatistics.model.request.TransactionRequest;
import com.btasdemir.transactionsstatistics.service.TransactionService;
import com.btasdemir.transactionsstatistics.validator.TransactionRequestValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("transactions")
public class TransactionController {

    private final TransactionRequestValidator transactionRequestValidator;
    private final TransactionService transactionService;

    public TransactionController(TransactionRequestValidator transactionRequestValidator,
                                 TransactionService transactionService) {
        this.transactionRequestValidator = transactionRequestValidator;
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void postTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        transactionRequestValidator.validate(transactionRequest);
        transactionService.save(transactionRequest);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public void deleteAllTransactions() {
        transactionService.deleteAll();
    }

}
