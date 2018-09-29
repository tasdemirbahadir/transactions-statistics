package com.btasdemir.transactionsstatistics.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulerConfigurationIT {

    @Autowired
    private ApplicationContext context;

    @Test
    public void it_should_load_scheduler_configuration() {
        //Given

        //When
        Map<String, TaskScheduler> taskSchedulerBeans = context.getBeansOfType(TaskScheduler.class);

        //Then
        assertThat(taskSchedulerBeans.values()).hasSize(1);
        TaskScheduler taskScheduler = taskSchedulerBeans.values().iterator().next();
        assertThat(taskScheduler).isInstanceOf(ConcurrentTaskScheduler.class);
    }
}