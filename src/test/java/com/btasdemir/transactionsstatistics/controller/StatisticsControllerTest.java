package com.btasdemir.transactionsstatistics.controller;

import com.btasdemir.transactionsstatistics.model.response.StatisticsResponse;
import com.btasdemir.transactionsstatistics.service.statistics.StatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;

    @Test
    public void it_should_get_statistics() throws Exception {
        //Given
        StatisticsResponse statisticsResponse = new StatisticsResponse();
        statisticsResponse.setAvg("1");
        statisticsResponse.setMin("2");
        statisticsResponse.setMax("3");
        statisticsResponse.setSum("5");
        statisticsResponse.setCount(4L);

        given(statisticsService.getStatistics()).willReturn(statisticsResponse);

        //When
        ResultActions result = mockMvc.perform(get("/statistics"));

        //Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.avg").value("1"))
                .andExpect(jsonPath("$.min").value("2"))
                .andExpect(jsonPath("$.max").value("3"))
                .andExpect(jsonPath("$.sum").value("5"))
                .andExpect(jsonPath("$.count").value(4))
                .andReturn();
    }
}