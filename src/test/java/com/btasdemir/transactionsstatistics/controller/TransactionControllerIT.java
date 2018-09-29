package com.btasdemir.transactionsstatistics.controller;

import com.btasdemir.transactionsstatistics.configuration.SchedulerConfiguration;
import com.btasdemir.transactionsstatistics.converter.TransactionRequestToTransactionConverter;
import com.btasdemir.transactionsstatistics.model.request.TransactionRequest;
import com.btasdemir.transactionsstatistics.service.TransactionService;
import com.btasdemir.transactionsstatistics.util.DateTimeTestUtils;
import com.btasdemir.transactionsstatistics.util.JsonTestUtils;
import com.btasdemir.transactionsstatistics.utils.DateTimeUtils;
import com.btasdemir.transactionsstatistics.utils.DecimalUtils;
import com.btasdemir.transactionsstatistics.validator.TransactionRequestValidator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
@Import({TransactionService.class,
        TransactionRequestValidator.class,
        GlobalControllerExceptionHandler.class,
        DateTimeUtils.class,
        DecimalUtils.class,
        TransactionRequestToTransactionConverter.class,
        SchedulerConfiguration.class})
public class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Value("${stale.threshold.millis}")
    private Long staleThresholdMillis;

    private JsonTestUtils jsonTestUtils = new JsonTestUtils();
    private DateTimeTestUtils dateTimeTestUtils = new DateTimeTestUtils();

    @Test
    public void it_should_return_204_if_the_transaction_is_older_than_staling_threshold() throws Exception {
        //Given
        DateTime staledDateTime = dateTimeTestUtils.getCurrentDateTime()
                .withDurationAdded(staleThresholdMillis + 1000, -1);

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setTimestamp(staledDateTime.toString());
        transactionRequest.setAmount("26.26");

        //When
        ResultActions result = mockMvc.perform(
                post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTestUtils.serializeToJson(transactionRequest))
        );

        //Then
        result.andExpect(status().isNoContent()).andReturn();
    }

    @Test
    public void it_should_return_400_if_the_json_is_invalid() throws Exception {
        //Given
        String invalidJson = "{\"this_json_is_invalid}";

        //When
        ResultActions result = mockMvc.perform(
                post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
        );

        //Then
        result.andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void it_should_return_422_when_timestamp_field_is_not_parsable() throws Exception {
        //Given
        String notParsableDateTime = "2018_09_23";

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setTimestamp(notParsableDateTime);
        transactionRequest.setAmount("26.26");

        //When
        ResultActions result = mockMvc.perform(
                post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTestUtils.serializeToJson(transactionRequest))
        );

        //Then
        result.andExpect(status().isUnprocessableEntity()).andReturn();
    }

    @Test
    public void it_should_return_422_when_amount_field_is_not_parsable() throws Exception {
        //Given
        DateTime dateTime = dateTimeTestUtils.getCurrentDateTime();

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setTimestamp(dateTime.toString());
        transactionRequest.setAmount("not_parsable_amount");

        //When
        ResultActions result = mockMvc.perform(
                post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTestUtils.serializeToJson(transactionRequest))
        );

        //Then
        result.andExpect(status().isUnprocessableEntity()).andReturn();
    }

    @Test
    public void it_should_return_422_when_the_transaction_date_is_in_the_future() throws Exception {
        //Given
        DateTime dateTime = dateTimeTestUtils.getCurrentDateTime().withDurationAdded(60000, 1);

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setTimestamp(dateTime.toString());
        transactionRequest.setAmount("26.26");

        //When
        ResultActions result = mockMvc.perform(
                post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTestUtils.serializeToJson(transactionRequest))
        );

        //Then
        result.andExpect(status().isUnprocessableEntity()).andReturn();
    }
}