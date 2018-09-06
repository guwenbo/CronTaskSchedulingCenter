package com.bobo.microservice.task;

import static com.bobo.microservice.redis.DistributedLockFactory.getDistributedLock;

import com.bobo.microservice.redis.DistributedLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author Gin Gu
 */
public class KafkaCronTask extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(KafkaCronTask.class);

    private static final String KAFKA_TASK_LOCK_NAME = "kafka_task";

    private String messageBody;

    private KafkaTemplate<String, String> kafkaTemplate;

    private RedisTemplate redisTemplate;

    public KafkaCronTask(String messageBody, KafkaTemplate<String, String> kafkaTemplate, RedisTemplate redisTemplate) {
        this.messageBody = messageBody;
        this.kafkaTemplate = kafkaTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run() {

        DistributedLock lock = null;

        try {

            logger.info("-----------------------");

            String msg = String.format("threadName: %s , threadId: %s , taskName: %s ...", Thread.currentThread().getName(), Thread.currentThread().getId(), messageBody);

            lock = getDistributedLock(KAFKA_TASK_LOCK_NAME, redisTemplate);

            boolean result = lock.lockResource();

            if (!result) {

                logger.warn("kafka task has been executed in other system");
            }

            logger.info(msg);

            logger.info("-----------------------");

            kafkaTemplate.send("test", messageBody);

        } finally {

            if (lock != null) {

                lock.releaseLock();

            }

        }
    }

}
