package com.micheal.actionlog.bean.user;


import org.apache.commons.lang3.StringUtils;

/**
 * 用户基本信息,用于记录操作
 *
 * @author Monan
 * created on 2018/12/11 19:35
 */

public class BaseUserInfo {
    /**
     * 用户ID
     */
    private Integer userid;

    /**
     * 登陆名
     */
    private String loginname;

    /**
     * Sets new 登陆名.
     *
     * @param loginname New value of 登陆名.
     */
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    /**
     * Sets new 用户ID.
     *
     * @param userid New value of 用户ID.
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * Gets 用户ID.
     *
     * @return Value of 用户ID.
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * Gets 登陆名.
     *
     * @return Value of 登陆名.
     */
    public String getLoginname() {
        return loginname;
    }



    public boolean isValid() {
        return StringUtils.isNotBlank(loginname) && userid != 0;
    }
}
