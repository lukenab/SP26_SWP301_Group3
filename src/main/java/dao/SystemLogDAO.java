/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.SystemLog;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class SystemLogDAO extends DBContext {

    public void insertLog(String actorName, String actorRole, String actionType, String description) {
        String sql = "INSERT INTO SystemLog (ActorName, ActorRole, ActionType, Description, LogDate) VALUES (?, ?, ?, ?, GETDATE())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, actorName);
            ps.setString(2, actorRole);
            ps.setString(3, actionType);
            ps.setString(4, description);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Fail to write System Log: " + e.getMessage());
        }
    }

    public List<SystemLog> getRecentLogs(String filterAction) {
        List<SystemLog> list = new ArrayList<>();
        String sql = "SELECT TOP 200 LogID, ActorName, ActorRole, ActionType, Description, LogDate FROM SystemLog ";

        if (filterAction != null && !filterAction.isEmpty() && !filterAction.equals("ALL")) {
            sql += "WHERE ActionType LIKE ? ";
        }
        sql += "ORDER BY LogDate DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (filterAction != null && !filterAction.isEmpty() && !filterAction.equals("ALL")) {
                ps.setString(1, "%" + filterAction + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SystemLog log = new SystemLog();
                log.setLogId(rs.getInt("LogID"));
                log.setActorName(rs.getString("ActorName"));
                log.setActorRole(rs.getString("ActorRole"));
                log.setActionType(rs.getString("ActionType"));
                log.setDescription(rs.getString("Description"));
                log.setLogDate(rs.getTimestamp("LogDate"));
                list.add(log);
            }
        } catch (Exception e) {
            System.out.println("Fail to get System Log: " + e.getMessage());
        }
        return list;
    }

    public List<SystemLog> getLogsByActor(String actorName, int row) {
        List<SystemLog> list = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM SystemLog WHERE ActorName = ? ORDER BY LogDate DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, row);
            ps.setString(2, actorName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SystemLog log = new SystemLog();
                log.setLogId(rs.getInt("LogID"));
                log.setActorName(rs.getString("ActorName"));
                log.setActorRole(rs.getString("ActorRole"));
                log.setActionType(rs.getString("ActionType"));
                log.setDescription(rs.getString("Description"));
                log.setLogDate(rs.getTimestamp("LogDate"));
                list.add(log);
            }
        } catch (Exception e) {
            System.out.println("Fail to get Personal Logs: " + e.getMessage());
        }
        return list;
    }
}
