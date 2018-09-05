package com.bobo.microservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Gin Gu
 */
@Configuration
public class CronTaskMappingBeanConfiguration {

    /**
     * autowired a HashMap to save the mapping of name -> task
     *
     * @return
     */
    @Bean(name = "cronTaskMap")
    public HashMap<String, ScheduledFuture> registerCronTaskMappingBean() {
        return new HashMap<>(20);
    }

}
