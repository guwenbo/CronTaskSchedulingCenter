package com.bobo.microservice.entity;

import java.io.Serializable;

/**
 * @author Gin Gu
 */
public class CronTaskResponseEntity implements Serializable {

    private static final long serialVersionUID = 7068237834552949815L;

    private String returnCode;

    private String returnMsg;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

}
