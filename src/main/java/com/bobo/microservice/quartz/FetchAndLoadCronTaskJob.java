package com.bobo.microservice.quartz;

import com.bobo.microservice.entity.CronTaskMsgEntity;
import com.bobo.microservice.service.CronTaskAnalysisService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashMap;

/**
 * @author Gin Gu
 */
public class FetchAndLoadCronTaskJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(FetchAndLoadCronTaskJob.class);

    @Autowired
    private CronTaskAnalysisService cronTaskAnalysisService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    protected void executeInternal(JobExecutionContext context) {

        logger.info("Begin to fetch and Load Cron Task in Redis Job");

        HashMap<String, CronTaskMsgEntity> remoteCronTask = cronTaskAnalysisService.fetchCronTask();

        HashMap<String, CronTaskMsgEntity> latestCronTaskMap = cronTaskAnalysisService.analysisCronTask(remoteCronTask);

        if (latestCronTaskMap != null) {

            cronTaskAnalysisService.loadLocalCronTask(latestCronTaskMap);

        }

        logger.info("End to fetch and Load Cron Task in Redis Job");

    }

}
