package com.micheal.actionlog.bean.user;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
public class User {
    private Integer userid;

    private String username;

    private String loginname;

    private String password;

    private String channelCode;

    private int usersex;

    private String userphone;

    private String userEmail;

    private String userBirthday;

    private Roles roles;

    private UserRelRoles userRelRoles;

    private int status;

    private long updateUserId;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public int getUsersex() {
        return usersex;
    }

    public void setUsersex(int usersex) {
        this.usersex = usersex;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public UserRelRoles getUserRelRoles() {
        return userRelRoles;
    }

    public void setUserRelRoles(UserRelRoles userRelRoles) {
        this.userRelRoles = userRelRoles;
    }

    public long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(long updateUserId) {
        this.updateUserId = updateUserId;
    }
}
