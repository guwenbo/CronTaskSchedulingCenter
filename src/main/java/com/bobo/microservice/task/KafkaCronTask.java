package com.bobo.microservice.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author Gin Gu
 */
public class KafkaCronTask extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(KafkaCronTask.class);

    private KafkaTemplate<String, String> kafkaTemplate;

    private String messageBody;

    public KafkaCronTask(KafkaTemplate<String, String> kafkaTemplate, String messageBody) {
        this.kafkaTemplate = kafkaTemplate;
        this.messageBody = messageBody;
    }

    @Override
    public void run() {
        String msg = String.format("threadName: %s , threadId: %s , taskName: %s ...", Thread.currentThread().getName(), Thread.currentThread().getId(), messageBody);

        logger.info(msg);

        //todo get redis distributed lock

        kafkaTemplate.send("test", messageBody);
    }

}
