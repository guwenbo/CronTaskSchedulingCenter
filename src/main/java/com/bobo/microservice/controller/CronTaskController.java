package com.bobo.microservice.controller;

import com.bobo.microservice.entity.CronTaskErrorResponseEntityFactory;
import com.bobo.microservice.entity.CronTaskMsgEntity;
import com.bobo.microservice.entity.CronTaskResponseEntity;
import com.bobo.microservice.entity.CronTaskSuccessResponseEntityFactory;
import com.bobo.microservice.exception.RedisTaskTimeoutException;
import com.bobo.microservice.service.CronTaskPreserveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gin Gu
 */
@RestController
@RequestMapping("/task")
public class CronTaskController {

    private static final Logger logger = LoggerFactory.getLogger(CronTaskController.class);

    @Autowired
    private CronTaskSuccessResponseEntityFactory successResponseEntityFactory;

    @Autowired
    private CronTaskErrorResponseEntityFactory errorResponseEntityFactory;

    @Autowired
    private CronTaskPreserveService cronTaskPreserveService;

    @PostMapping
    public CronTaskResponseEntity handleCronTaskPostRequest(@RequestBody CronTaskMsgEntity cronTaskMsgEntity) {

        logger.info("Begin to handle POST request for creating a new cron task");

        CronTaskResponseEntity cronTaskResponseEntity;

        try {

            // todo check request is valid ?

            cronTaskPreserveService.saveTaskIntoRedis(cronTaskMsgEntity);

            cronTaskResponseEntity = successResponseEntityFactory.getResponseEntity("Create Cron Task successfully");

        } catch (RedisTaskTimeoutException e) {

            cronTaskResponseEntity = errorResponseEntityFactory.getResponseEntity(e.getMessage());

        }

        logger.info("End to handle POST request for creating a new cron task");

        return cronTaskResponseEntity;
    }

}
