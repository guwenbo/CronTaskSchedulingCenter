package com.bobo.microservice.enums;

import static com.bobo.microservice.utils.StringUtils.isEmpty;

public enum RedisEnums {

    /**
     * lock
     */
    LOCK;

    /**
     * get prefix of redis distributed lock
     *
     * @return
     */
    private String getPrefix() {
        return this.name() + ".";
    }


    public String generateKey(String key) {

        if (isEmpty(key)) {
            throw new IllegalArgumentException();
        }

        return this.getPrefix() + key;

    }

}
