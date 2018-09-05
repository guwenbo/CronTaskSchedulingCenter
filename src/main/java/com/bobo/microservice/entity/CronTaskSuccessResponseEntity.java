package com.bobo.microservice.entity;

/**
 * @author Gin Gu
 */
public class CronTaskSuccessResponseEntity extends CronTaskResponseEntity {

    private static final long serialVersionUID = -4893614852534784636L;

    public CronTaskSuccessResponseEntity() {
        setReturnCode("SUCCESS");
    }

    public CronTaskSuccessResponseEntity(String returnMsg) {
        this();
        setReturnMsg(returnMsg);
    }

}
