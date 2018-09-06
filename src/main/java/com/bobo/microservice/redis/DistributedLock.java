package com.bobo.microservice.redis;

import static com.bobo.microservice.utils.StringUtils.isNotEmpty;

import com.bobo.microservice.enums.RedisEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Gin Gu
 */
public class DistributedLock {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    /**
     * expire time of redis distributed lock
     */
    private long lockExpireTime = 3 * 1000;

    /**
     * value of distributed lock
     */
    private final String uuid = UUID.randomUUID().toString();


    /**
     * resource which is going to be locked
     */
    private final String resourceName;


    /**
     * redis template
     */
    private RedisTemplate redisTemplate;

    public DistributedLock(String resourceName, RedisTemplate redisTemplate) {
        this.resourceName = resourceName;
        this.redisTemplate = redisTemplate;
    }


    /**
     * custom time of milliseconds for expire time
     *
     * @param lockExpireTime
     */
    public void setLockExpireTime(long lockExpireTime) {
        this.lockExpireTime = lockExpireTime;
    }

    /**
     * lock resource by using setNX
     *
     * @return if resource has been locked
     */
    @SuppressWarnings("unchecked")
    public boolean lockResource() {

        logger.info("Prepare to get lock");

        ValueOperations operations = redisTemplate.opsForValue();

        String resourceKey = RedisEnums.LOCK.generateKey(this.resourceName);

        if (operations.setIfAbsent(resourceKey, uuid)) {

            // set expire time for lock
            operations.set(resourceKey, uuid, lockExpireTime, TimeUnit.MILLISECONDS);

            logger.info(Thread.currentThread().getName() + " get lock for " + resourceKey);

            return true;

        } else {

            logger.warn("Lock " + resourceKey + " is existed");

            return false;
        }

    }

    /**
     * release lock of resource
     * <p>
     * Use lua to implement transaction
     */
    @SuppressWarnings("unchecked")
    public void releaseLock() {

        logger.info("Prepare to release lock");

        String lua = "if redis.call('get', KEYS[1]) == ARGV[1]\n" +
                "then\n" +
                " return redis.call('del', KEYS[1])\n" +
                "else\n" +
                " return 0\n" +
                "end";

        RedisScript<Long> script = new DefaultRedisScript<Long>(lua, Long.class);

        String resourceKey = RedisEnums.LOCK.generateKey(this.resourceName);

        List<String> keys = new ArrayList<>();

        keys.add(resourceKey);

        if (isNotEmpty(resourceName)) {

            redisTemplate.execute(script, keys, uuid);
        }

        logger.info("Lock for " + resourceKey + " has been released");

    }

}
