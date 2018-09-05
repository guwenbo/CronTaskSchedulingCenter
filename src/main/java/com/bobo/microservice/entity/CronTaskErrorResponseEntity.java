package com.bobo.microservice.entity;

/**
 * @author Gin Gu
 */
public class CronTaskErrorResponseEntity extends CronTaskResponseEntity {

    public CronTaskErrorResponseEntity() {
        setReturnCode("ERROR");
    }

    public CronTaskErrorResponseEntity(String returnMsg) {
        this();
        setReturnMsg(returnMsg);
    }

}
