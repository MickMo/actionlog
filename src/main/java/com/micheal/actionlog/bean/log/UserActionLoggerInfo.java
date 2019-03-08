package com.micheal.actionlog.bean.log;


import com.micheal.actionlog.bean.user.BaseUserInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * UserActionLogger 信息
 *
 * @author Monan
 * created on 2018/12/10 12:01
 */
public class UserActionLoggerInfo {

    public UserActionLoggerInfo(BaseUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserActionLoggerInfo() {
    }

    /**
     * 调用的方法名
     */
    private String methodName;
    /**
     * 拦截SQL类型
     *
     * @see com.micheal.actionlog.constant.SQLTypeConstant
     */
    private String sqlType;

    /**
     * 日志记录等级(DB,Log,Etc.)
     *
     * @see com.micheal.actionlog.constant.log.LogLevelConstant
     */
    private String logLevel;

    /**
     * 业务描述
     */
    private String description;

    /**
     * 用户信息
     */
    private BaseUserInfo userInfo;

    /**
     * 对应的Select语句,在复杂修改语句下需要指定
     */
    private String selectSQLName;

    /**
     * logs
     */
    private List<UserActionLog> logs = new ArrayList<>();

    /**
     * 是否有效<p>
     * sqlType 不为空<p>
     * logLevel
     *
     * @return
     */
    public boolean isValid() {
        return (StringUtils.isNotBlank(sqlType) && StringUtils.isNotBlank(logLevel));
    }

    /**
     * Sets new 日志记录等级DB,Log,Etc.
     *
     * @param logLevel New value of 日志记录等级DB,Log,Etc.
     * @see com.micheal.actionlog.constant.log.LogLevelConstant
     */
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * Gets 日志记录等级DB,Log,Etc.
     *
     * @return Value of 日志记录等级DB,Log,Etc.
     * @see com.micheal.actionlog.constant.log.LogLevelConstant
     */
    public String getLogLevel() {
        return logLevel;
    }

    /**
     * Sets new 拦截SQL类型
     *
     * @param sqlType New value of 拦截SQL类型
     * @see com.micheal.actionlog.constant.SQLTypeConstant
     */
    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    /**
     * Gets 拦截SQL类型
     *
     * @return Value of 拦截SQL类型
     * @see com.micheal.actionlog.constant.SQLTypeConstant
     */
    public String getSqlType() {
        return sqlType;
    }

    /**
     * Sets new 业务描述.
     *
     * @param description New value of 业务描述.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets 业务描述.
     *
     * @return Value of 业务描述.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets 对应的Select语句,在复杂修改语句下需要指定.
     *
     * @return Value of 对应的Select语句,在复杂修改语句下需要指定.
     */
    public String getSelectSQLName() {
        return selectSQLName;
    }

    /**
     * Sets new 对应的Select语句,在复杂修改语句下需要指定.
     *
     * @param selectSQLName New value of 对应的Select语句,在复杂修改语句下需要指定.
     */
    public void setSelectSQLName(String selectSQLName) {
        this.selectSQLName = selectSQLName;
    }

    /**
     * Gets 调用的方法名.
     *
     * @return Value of 调用的方法名.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Sets new 调用的方法名.
     *
     * @param methodName New value of 调用的方法名.
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public BaseUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(BaseUserInfo userInfo) {
        this.userInfo = userInfo;
    }


    public UserActionLog getUserActionLog(int index) {
        if (index > -1) {
            return logs.get(index);
        }
        return null;
    }

    public void setUserActionLog(int index, UserActionLog userActionLog) {
        if (index > -1 && null != userActionLog) {
            logs.set(index, userActionLog);
        }
    }

    public void addUserActionLog(UserActionLog userActionLog) {
        if (null != userActionLog) {
            logs.add(userActionLog);
        }
    }

    /**
     * Gets logs.
     *
     * @return Value of logs.
     */
    public List<UserActionLog> getLogs() {
        return logs;
    }


    public int getUserActionLogsSize() {
        return logs.size();
    }

    @Override
    public String toString() {
        return "UserActionLoggerInfo{" +
                "sqlType='" + sqlType + '\'' +
                ", logLevel='" + logLevel + '\'' +
                ", description='" + description + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }



}
