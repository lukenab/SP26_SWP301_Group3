/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Setting;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class SettingDAO extends DBContext {

    public List<Setting> getAllSetting() {
        List<Setting> list = new ArrayList<>();
        String sql = "SELECT * FROM [SystemSetting]";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Setting(
                        rs.getString("SettingKey"),
                        rs.getString("SettingValue"),
                        rs.getString("Description"),
                        rs.getString("SettingGroup")));
            }
        } catch (Exception e) {
            System.out.println("Fail to get settings: " +e.getMessage());
        }
        return list;
    }
    
    public boolean updateSetting(String key, String value){
        String sql = "UPDATE [SystemSetting] SET SettingValue = ?, LastUpdated = GETDATE() WHERE SettingKey = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, value);
            ps.setString(2, key);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to update setting: " +e.getMessage());
        }
        return false;
    }
    
    public String getSettingValue(String key){
        String sql = "SELECT SettingValue FROM [SystemSetting] WHERE SettingKey = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
             return rs.getString("SettingValue");
            }
        } catch (Exception e) {
            System.out.println("Fail to get setting value: "+e.getMessage());
        }
    return null;
    }
    
    public static void main(String[] args) {
        SettingDAO dao = new SettingDAO();
//        List<Setting> list = dao.getAllSetting();
//        for (Setting setting : list) {
//            System.out.println(setting);
//        }
        System.out.println(dao.getSettingValue("MAX_LOGIN_ATTEMPTS"));
    }
}
