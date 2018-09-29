package com.btasdemir.transactionsstatistics.controller;

import com.btasdemir.transactionsstatistics.model.request.TransactionRequest;
import com.btasdemir.transactionsstatistics.service.TransactionService;
import com.btasdemir.transactionsstatistics.util.JsonTestUtils;
import com.btasdemir.transactionsstatistics.validator.TransactionRequestValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRequestValidator transactionRequestValidator;

    @MockBean
    private TransactionService transactionService;

    private JsonTestUtils jsonTestUtils = new JsonTestUtils();

    @Test
    public void it_should_post_transaction_request() throws Exception {
        //Given
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("34.25");
        transactionRequest.setTimestamp("2018-09-23");

        willDoNothing().given(transactionRequestValidator).validate(transactionRequest);
        willDoNothing().given(transactionService).save(transactionRequest);

        //When
        ResultActions result = mockMvc.perform(
                post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTestUtils.serializeToJson(transactionRequest))
        );

        //Then
        result.andExpect(status().isCreated()).andReturn();
    }

    @Test
    public void it_should_delete_all_transactions() throws Exception {
        //Given
        willDoNothing().given(transactionService).deleteAll();

        //When
        ResultActions result = mockMvc.perform(delete("/transactions"));

        //Then
        result.andExpect(status().isNoContent()).andReturn();
    }
}