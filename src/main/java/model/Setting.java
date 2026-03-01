/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Legion
 */
public class Setting {
    private String settingKey;
    private String settingValue;
    private String description;
    private String settingGroup;

    public Setting() {
    }

    public Setting(String settingKey, String settingValue, String description, String settingGroup) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.description = description;
        this.settingGroup = settingGroup;
    }

    public String getSettingGroup() {
        return settingGroup;
    }

    public void setSettingGroup(String settingGroup) {
        this.settingGroup = settingGroup;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Setting{" + "settingKey=" + settingKey + ", settingValue=" + settingValue + ", description=" + description + ", settingGroup=" + settingGroup + '}';
    }
}
