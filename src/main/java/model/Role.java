/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Legion
 */
public class Role {

    private int roleId;
    private String roleName;
    private Boolean manageUser;
    private Boolean manageFinance;
    private Boolean manageCourse;

    public Role() {
    }

    public Role(int roleId, String roleName, Boolean manageUser, Boolean manageFinance, Boolean manageCourse) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.manageUser = manageUser;
        this.manageFinance = manageFinance;
        this.manageCourse = manageCourse;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getManageUser() {
        return manageUser;
    }

    public void setManageUser(Boolean manageUser) {
        this.manageUser = manageUser;
    }

    public Boolean getManageFinance() {
        return manageFinance;
    }

    public void setManageFinance(Boolean manageFinance) {
        this.manageFinance = manageFinance;
    }

    public Boolean getManageCourse() {
        return manageCourse;
    }

    public void setManageCourse(Boolean manageCourse) {
        this.manageCourse = manageCourse;
    }

    @Override
    public String toString() {
        return "Role{" + "roleId=" + roleId + ", roleName=" + roleName + ", manageUser=" + manageUser + ", manageFinance=" + manageFinance + ", manageCourse=" + manageCourse + '}';
    }
}
