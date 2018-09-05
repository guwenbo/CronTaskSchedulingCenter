package com.bobo.microservice.service;

import com.bobo.microservice.entity.CronTaskMsgEntity;
import com.bobo.microservice.task.KafkaCronTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Gin Gu
 */
@Service
public class CronTaskAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(CronTaskAnalysisService.class);

    @Value("${cron.task.redis.hash.name}")
    private String cronTaskHashName;

    @Resource(name = "cronTaskMap")
    private HashMap<String, ScheduledFuture> cronTaskLocal;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * fetch all values with keys in hash that in redis
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, CronTaskMsgEntity> fetchCronTask() {

        logger.info("Begin to fetch Cron Task in Redis Job ...");

        HashOperations<String, String, CronTaskMsgEntity> ops = redisTemplate.opsForHash();

        // fetch keys in hash
        Set<String> keys = ops.keys(cronTaskHashName);

        HashMap<String, CronTaskMsgEntity> remoteCronTask = new HashMap<>(20);

        // todo optimize
        redisTemplate.executePipelined(

                new RedisCallback<Object>() {

                    @Override
                    public HashMap doInRedis(RedisConnection connection) throws DataAccessException {

                        keys.forEach(key -> remoteCronTask.put(key, ops.get(cronTaskHashName, key)));

                        return null;
                    }
                }

        );

        logger.info("End to fetch Cron Task in Redis Job ...");

        return remoteCronTask;
    }


    public HashMap<String, CronTaskMsgEntity> analysisCronTask(HashMap<String, CronTaskMsgEntity> remoteCronTask) {

        logger.info("Begin to analysis Cron task ...");

        Set<String> remoteCronTaskKeys = remoteCronTask.keySet();

        Set<String> cronTaskLocalKeys = cronTaskLocal.keySet();

        remoteCronTaskKeys.removeAll(cronTaskLocalKeys);

        HashMap<String, CronTaskMsgEntity> latestCronTask = null;

        if (remoteCronTaskKeys.size() > 0) {

            latestCronTask = new HashMap<>(10);

            for (String s : remoteCronTaskKeys) {

                latestCronTask.put(s, remoteCronTask.get(s));

            }

        }

        logger.info("End to analysis Cron task ...");

        return latestCronTask;

    }


    public void loadLocalCronTask(HashMap<String, CronTaskMsgEntity> latestCronTaskMap) {

        logger.info("Begin load local cron task");

        for (String key : latestCronTaskMap.keySet()) {

            CronTaskMsgEntity entity = latestCronTaskMap.get(key);

            Runnable task = new KafkaCronTask(kafkaTemplate, entity.getTaskMsg());

            ScheduledFuture future = threadPoolTaskScheduler.schedule(task, new CronTrigger(entity.getCron()));

            cronTaskLocal.put(key, future);

        }

        logger.info("End load local cron task");

    }

}
