package com.bobo.microservice.redis;

import static com.bobo.microservice.utils.StringUtils.isEmpty;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Gin Gu
 */
public class DistributedLockFactory {

    public DistributedLock getDistributedLock(String resourceName, RedisTemplate redisTemplate) {
        if (isEmpty(resourceName)) {
            throw new IllegalArgumentException("resource name can not be null");
        }
        return new DistributedLock(resourceName, redisTemplate);
    }

}
