package com.bobo.microservice.config;

import com.bobo.microservice.quartz.FetchAndLoadCronTaskJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gin Gu
 */
@Configuration
public class CronTaskJobsConfiguration {

    @Value("${cron.task.cron.expression}")
    private String cronTaskCronExpression;

    @Bean
    public JobDetail fetchAndLoadCronTaskJobDetail() {

        return JobBuilder.newJob(FetchAndLoadCronTaskJob.class)
                .withIdentity("FetchAndLoadCronTaskJob", "CronTaskGroup")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger fetchAndLoadCronTaskJobTrigger() {

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronTaskCronExpression);

        return TriggerBuilder.newTrigger().forJob(fetchAndLoadCronTaskJobDetail()).withSchedule(scheduleBuilder).build();

    }

}
