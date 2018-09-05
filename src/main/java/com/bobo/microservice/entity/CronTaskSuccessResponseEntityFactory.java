package com.bobo.microservice.entity;

import org.springframework.stereotype.Component;

/**
 * @author Gin Gu
 */
@Component
public class CronTaskSuccessResponseEntityFactory implements CronTaskResponseEntityFactory {

    @Override
    public CronTaskResponseEntity getResponseEntity(String msg) {
        return new CronTaskSuccessResponseEntity(msg);
    }

}
