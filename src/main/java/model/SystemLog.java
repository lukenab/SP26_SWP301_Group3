/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author phuct
 */
public class SystemLog {
    private int logId;
    private String actorName;
    private String actionType;
    private String description;
    private LocalDateTime logDate;

    public SystemLog() {
    }

    public SystemLog(int logId, String actorName, String actionType, String description, LocalDateTime logDate) {
        this.logId = logId;
        this.actorName = actorName;
        this.actionType = actionType;
        this.description = description;
        this.logDate = logDate;
    }

    public LocalDateTime getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDateTime logDate) {
        this.logDate = logDate;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SystemLog{" + "logId=" + logId + ", actorName=" + actorName + ", actionType=" + actionType + ", description=" + description + ", logDate=" + logDate + '}';
    }
    
}
