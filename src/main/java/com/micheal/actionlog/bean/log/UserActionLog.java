package com.micheal.actionlog.bean.log;

import java.util.Date;

/**
 * 用户操作日志
 *
 * @author Monan
 * created on 2018/12/18 14:24
 */
public class UserActionLog {

    public static final String PrimeKeysSeparator = ",";
    /**
     * 主键
     */
    private int id;

    /**
     * 业务描述
     */
    private String description;

    /**
     * 记录ID
     */
    private String recordId;

    /**
     * 主键ID(可以有多个字段,','号分隔)
     */
    private String effectedPrimeKey;

    /**
     * 被影响的数据表的主键字段名
     */
    private String  effectedPrimeKeyName ;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 修改前的值
     */
    private String originalValue;

    /**
     * 修改后的值
     */
    private String modifiedValue;

    /**
     * 用户ID
     */
    private int userId;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * SQL
     */
    private String SQLStatement;

    /**
     * 调用方法名
     */
    private String methodName;


    /**
     * Gets SQLStatement.
     *
     * @return Value of SQLStatement.
     */
    public String getSQLStatement() {
        return SQLStatement;
    }

    /**
     * Gets 修改后的值.
     *
     * @return Value of 修改后的值.
     */
    public String getModifiedValue() {
        return modifiedValue;
    }

    /**
     * Sets new 修改前的值.
     *
     * @param originalValue New value of 修改前的值.
     */
    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    /**
     * Gets 主键.
     *
     * @return Value of 主键.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets new 用户ID.
     *
     * @param userId New value of 用户ID.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Sets new 主键.
     *
     * @param id New value of 主键.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets new SQLStatement.
     *
     * @param SQLStatement New value of SQLStatement.
     */
    public void setSQLStatement(String SQLStatement) {
        this.SQLStatement = SQLStatement;
    }

    /**
     * Gets 表名.
     *
     * @return Value of 表名.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets new 修改时间.
     *
     * @param modifyDate New value of 修改时间.
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * Sets new 主键ID可以有多个字段','号分隔.
     *
     * @param effectedPrimeKey New value of 主键ID可以有多个字段','号分隔.
     */
    public void setEffectedPrimeKey(String effectedPrimeKey) {
        this.effectedPrimeKey = effectedPrimeKey;
    }

    /**
     * Gets 字段名.
     *
     * @return Value of 字段名.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets new 修改后的值.
     *
     * @param modifiedValue New value of 修改后的值.
     */
    public void setModifiedValue(String modifiedValue) {
        this.modifiedValue = modifiedValue;
    }

    /**
     * Gets 主键ID可以有多个字段','号分隔.
     *
     * @return Value of 主键ID可以有多个字段','号分隔.
     */
    public String getEffectedPrimeKey() {
        return effectedPrimeKey;
    }

    /**
     * Sets new 表名.
     *
     * @param tableName New value of 表名.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Gets 修改时间.
     *
     * @return Value of 修改时间.
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * Gets 修改前的值.
     *
     * @return Value of 修改前的值.
     */
    public String getOriginalValue() {
        return originalValue;
    }

    /**
     * Gets 记录ID.
     *
     * @return Value of 记录ID.
     */
    public String getRecordId() {
        return recordId;
    }

    /**
     * Sets new 记录ID.
     *
     * @param recordId New value of 记录ID.
     */
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    /**
     * Sets new 字段名.
     *
     * @param columnNames New value of 字段名.
     */
    public void setColumnName(String columnNames) {
        this.columnName = columnNames;
    }

    /**
     * Gets 用户ID.
     *
     * @return Value of 用户ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets 调用方法名.
     *
     * @return Value of 调用方法名.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Sets new 调用方法名.
     *
     * @param methodName New value of 调用方法名.
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
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

    public String getEffectedPrimeKeyName() {
        return effectedPrimeKeyName;
    }

    public void setEffectedPrimeKeyName(String effectedPrimeKeyName) {
        this.effectedPrimeKeyName = effectedPrimeKeyName;
    }

    @Override
    public String toString() {
        return "UserActionLog{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", recordId='" + recordId + '\'' +
                ", effectedPrimeKey='" + effectedPrimeKey + '\'' +
                ", effectedPrimeKeyName='" + effectedPrimeKeyName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", originalValue='" + originalValue + '\'' +
                ", modifiedValue='" + modifiedValue + '\'' +
                ", userId=" + userId +
                ", modifyDate=" + modifyDate +
                ", SQLStatement='" + SQLStatement + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}
