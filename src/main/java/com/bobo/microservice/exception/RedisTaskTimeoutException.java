package com.bobo.microservice.exception;

/**
 * @author Gin Gu
 */
public class RedisTaskTimeoutException extends Exception {

    public RedisTaskTimeoutException() {

    }

    public RedisTaskTimeoutException(String msg) {
        super(msg);
    }

}
