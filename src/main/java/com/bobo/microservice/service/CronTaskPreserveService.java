package com.bobo.microservice.service;

import com.bobo.microservice.exception.RedisTaskTimeoutException;
import com.bobo.microservice.entity.CronTaskMsgEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Gin Gu
 */
@Service
public class CronTaskPreserveService {

    private static final Logger logger = LoggerFactory.getLogger(CronTaskPreserveService.class);

    @Value("${cron.task.redis.hash.name}")
    private String cronTaskRedisHashName;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * save cron task into redis , using redis hash as data structure
     *
     * hash : ${cron.task.redis.hash.name}
     * hashKey : systemName + "_" + taskName
     * hashValue : taskEntity
     *
     * @param taskEntity
     * @throws RedisTaskTimeoutException
     */
    @SuppressWarnings("unchecked")
    public void saveTaskIntoRedis(CronTaskMsgEntity taskEntity) throws RedisTaskTimeoutException {

        logger.info("Begin to save cron task into redis");

        try {

            HashOperations operations = redisTemplate.opsForHash();

            // send data to redis
            operations.put(cronTaskRedisHashName, taskEntity.getSystemName() + "_" + taskEntity.getTaskName(), taskEntity);

        } catch (Exception e) {

            logger.error("Failed to save cron task into redis");

            throw new RedisTaskTimeoutException("Failed to save cron task into redis");

        }

        logger.info("End to save cron task into redis");

    }

}
