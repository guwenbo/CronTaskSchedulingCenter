package com.bobo.microservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author Gin Gu
 */
@Configuration
public class ThreadPoolTaskSchedulerConfiguration {

    @Bean
    public ThreadPoolTaskScheduler registerTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

}
