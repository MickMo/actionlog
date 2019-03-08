package com.micheal.actionlog.util.sqlutil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ResultSetUtil
 *
 * @author Monan
 * created on 2018/12/19 9:01
 */
public class ResultSetUtil {

    /**
     * ResultSetToList
     *
     * @param rs ResultSet
     * @return List Map key:tableName.columnName value:value
     * @throws SQLException
     */
    public static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        List<String> colNameList = new ArrayList<String>();
        for (int i = 0; i < colCount; i++) {
            String tableName = rsmd.getTableName(i + 1);
            String columnLabel = rsmd.getColumnLabel(i + 1);
//            String columnName = rsmd.getColumnName(i + 1);
            colNameList.add(tableName + "." + columnLabel);
        }
        while (rs.next()) {
            Map map = new HashMap<String, Object>();
            for (int i = 0; i < colCount; i++) {
                String key = colNameList.get(i);
                String[] split = key.split("\\.");
                if (split.length < 2) {
                    continue;
                }
                String colName = split[1];
                Object value = rs.getString(colName);
                map.put(key, value);
            }
            results.add(map);
        }
        return results;
    }

    /**
     * ResultSetToList
     *
     * @param rs ResultSet
     * @return List Map
     * @throws SQLException
     */
    public static List<Map<String, String>> resultSetToStringList(ResultSet rs) throws SQLException {
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        List<String> colNameList = new ArrayList<String>();
        for (int i = 0; i < colCount; i++) {
            String tableName = rsmd.getTableName(i + 1);
            String columnName = rsmd.getColumnLabel(i + 1);
            colNameList.add(tableName + "." + columnName);
        }
        while (rs.next()) {
            for (int i = 0; i < colCount; i++) {
                Map map = new HashMap<String, Object>();
                String key = colNameList.get(i);
                String[] split = key.split("\\.");
                if (split.length < 2) {
                    continue;
                }
                String colName = split[1];
                Object value = rs.getString(colName);
                map.put(key, value);
                results.add(map);
            }
        }
        return results;
    }

    /**
     * ResultSetToList
     *
     * @param rs ResultSet
     * @return List Map
     * @throws SQLException
     */
    public static String resultSetColumnToString(ResultSet rs) throws SQLException {
//        List<Map<String, String>> results = new ArrayList<Map<String, String>>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        StringBuilder columnNameBuilder = new StringBuilder();
        for (int i = 0; i < colCount; i++) {
            String tableName = rsmd.getTableName(i + 1);
            String columnName = rsmd.getColumnLabel(i + 1);
            columnNameBuilder.append(columnName + ",");
        }
        return columnNameBuilder.toString();
    }
}
