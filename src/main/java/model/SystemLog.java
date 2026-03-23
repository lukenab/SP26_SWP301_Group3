/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;
/**
 *
 * @author phuct
 */
public class SystemLog {
    private int logId;
    private String actorName;
    private String actionType;
    private String actorRole;
    private String description;
    private Timestamp logDate;

    public SystemLog() {
    }

    public SystemLog(int logId, String actorName, String actionType, String actorRole, String description, Timestamp logDate) {
        this.logId = logId;
        this.actorName = actorName;
        this.actionType = actionType;
        this.actorRole = actorRole;
        this.description = description;
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

    public String getActorRole() {
        return actorRole;
    }

    public void setActorRole(String actorRole) {
        this.actorRole = actorRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getLogDate() {
        return logDate;
    }

    public void setLogDate(Timestamp logDate) {
        this.logDate = logDate;
    }

    @Override
    public String toString() {
        return "SystemLog{" + "logId=" + logId + ", actorName=" + actorName + ", actionType=" + actionType + ", actorRole=" + actorRole + ", description=" + description + ", logDate=" + logDate + '}';
    }
}
