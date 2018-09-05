package com.bobo.microservice.entity;

import java.io.Serializable;

/**
 * @author Gin Gu
 */
public class CronTaskMsgEntity implements Serializable {

    private static final long serialVersionUID = -8645009158539687625L;

    private String systemName;

    private String taskName;

    private String cron;

    private String taskMsg;

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskMsg() {
        return taskMsg;
    }

    public void setTaskMsg(String taskMsg) {
        this.taskMsg = taskMsg;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

}
