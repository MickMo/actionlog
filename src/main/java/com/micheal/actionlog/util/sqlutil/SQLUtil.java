package com.micheal.actionlog.util.sqlutil;


import org.apache.commons.lang3.StringUtils;

/**
 * Common SQL Util
 *
 * @author Monan
 * created on 2018/12/19 23:56
 */
public class SQLUtil {

    private static final String GetPrimaryKeyColumnNameByTableNameSQLStr = "select column_name from information_schema.columns " +
            "where table_name=? and table_schema='ec' and column_key='PRI'";

    private static final String GetAllCommonColumnNamesByTableNameSQLStr = "select column_name from information_schema.columns " +
            "where table_name=? and table_schema='ec' and column_key <> 'PRI'";

    /**
     * Generate a SQL String to get primary key's column name from DB base on table's name<p>
     * will add suffix to primary key's column
     *
     * @param tableName table name
     * @return SQL
     */
    public static String generateGetPrimaryColumnsByTableNameSQL(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        return GetPrimaryKeyColumnNameByTableNameSQLStr.replace("?", "'" + tableName + "'");
    }

    /**
     * Generate a SQL String to get all NONE primary key's column name from DB base on table's name
     *
     * @param tableName table name
     * @return SQL
     */
    public static String generateGetAllCommonColumnsByTableNameSQL(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        return GetAllCommonColumnNamesByTableNameSQLStr.replace("?", "'" + tableName + "'");
    }
}
