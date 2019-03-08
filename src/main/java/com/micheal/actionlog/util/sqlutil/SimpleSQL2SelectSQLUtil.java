package com.micheal.actionlog.util.sqlutil;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Simple SQL statement Util
 *
 * @author Monan
 * created on 2018/12/19 11:52
 */
public class SimpleSQL2SelectSQLUtil {
    /**
     * DB Keyword {@code SELECT}
     */
    public static final String SQL_KEYWORD_SELECT = "SELECT";
    /**
     * DB Keyword {@code FROM}
     */
    public static final String SQL_KEYWORD_FROM = "FROM";
    /**
     * DB Keyword {@code WHERE}
     */
    public static final String SQL_KEYWORD_WHERE = "WHERE";

    public static final String Pri_Key_Suffix = "_PriKey#";

    private static final Logger LOG = LoggerFactory.getLogger(SimpleSQL2SelectSQLUtil.class);

    /**
     * 根据Update语句构建对应的Select语句
     *
     * @param updateStatement  Update语句
     * @param priKeyColumnName 主键字段名(可变参数,仅第一个参数生效)
     * @return Select语句
     */
    public static String toSelectSQL(Update updateStatement, String... priKeyColumnName) {
        //构建Select语句
        String tablesNameStr = getTablesName(updateStatement);
        if (StringUtils.isBlank(tablesNameStr)) {
            return null;
        }

        String columnsStr = getColumns(updateStatement, priKeyColumnName);
        String joinsStr = getJoins(updateStatement);
        String whereClause = getWhereClause(updateStatement);

        StringBuilder selectStrBuilder = new StringBuilder();
        selectStrBuilder.append(SQL_KEYWORD_SELECT).append(columnsStr);
        selectStrBuilder.append(SQL_KEYWORD_FROM).append(tablesNameStr);
        selectStrBuilder.append(joinsStr);
        if (StringUtils.isNotBlank(whereClause)) {
            selectStrBuilder.append(SQL_KEYWORD_WHERE).append(whereClause);
        }
        return selectStrBuilder.toString();
    }

    /**
     * 根据Delete语句构建对应的Select语句
     *
     * @param deleteStatement Delete语句
     * @param columnsStr      字段名(逗号分隔)
     * @return Select语句
     */
    public static String toSelectSQL(Delete deleteStatement, String columnsStr) {
        //构建Select语句
        String tablesNameStr = getTablesName(deleteStatement);
        if (StringUtils.isBlank(tablesNameStr)) {
            return null;
        }

        String joinsStr = getJoins(deleteStatement);
        String whereClause = getWhereClause(deleteStatement);

        StringBuilder selectStrBuilder = new StringBuilder();
        selectStrBuilder.append(SQL_KEYWORD_SELECT).append(columnsStr);
        selectStrBuilder.append(SQL_KEYWORD_FROM).append(tablesNameStr);
        selectStrBuilder.append(joinsStr);
        if (StringUtils.isNotBlank(whereClause)) {
            selectStrBuilder.append(SQL_KEYWORD_WHERE).append(whereClause);
        }
        return selectStrBuilder.toString();
    }

    /**
     * 获取所有字段<p>
     *
     * @param updateStatement  Update语句
     * @param priKeyColumnName 主键字段名(可变参数,仅第一个参数生效)
     * @return Where子句
     */
    private static String getColumns(Update updateStatement, String... priKeyColumnName) {
        //获取所有Set字段
        List<Column> columns = updateStatement.getColumns();
        StringBuilder columnsStrBuilder = new StringBuilder();
        if (columns != null && columns.size() > 0) {
            columnsStrBuilder.append(" ");
            for (Column column : columns) {
                columnsStrBuilder.append(" ").append(column.getColumnName()).append(",");
            }
            if (ArrayUtils.isNotEmpty(priKeyColumnName)) {
                String priKeyAliasName = priKeyColumnName[0] + Pri_Key_Suffix;
                columnsStrBuilder.append(" ").append(priKeyColumnName[0]).append(" AS '").append(priKeyAliasName).append("',");
            }
            columnsStrBuilder.setLength(columnsStrBuilder.length() - 1);
            columnsStrBuilder.append(" ");
        }
        return columnsStrBuilder.toString();
    }

    /**
     * 获取表名
     *
     * @param updateStatement Update语句
     * @return Where子句
     */
    private static String getTablesName(Update updateStatement) {
        //获取表名
        List<Table> tables = updateStatement.getTables();
        String tablesNameCore = getTablesNameCore(tables);
        if (StringUtils.isBlank(tablesNameCore)) {
            LOG.error("Error SQL!Unable to retrieval table name from SQL:{}.", updateStatement.toString());
        }
        return tablesNameCore;
    }

    /**
     * 获取表名
     *
     * @param updateStatement Update语句
     * @return Where子句
     */
    private static String getTablesName(Delete updateStatement) {
        //获取表名
        List<Table> tables = updateStatement.getTables();
        String tablesNameCore = getTablesNameCore(tables);
        if (StringUtils.isBlank(tablesNameCore)) {
            LOG.error("Error SQL!Unable to retrieval table name from SQL:{}.", updateStatement.toString());
        }
        return tablesNameCore;
    }

    /**
     * 获取表名Core
     *
     * @param tables List<Table> tables
     * @return 表名, 用','分隔.
     */
    private static String getTablesNameCore(List<Table> tables) {
        StringBuilder tablesNameStrBuilder = new StringBuilder();
        if (tables == null || tables.size() < 1) {
            return null;
        } else if (tables.size() < 2) {
            tablesNameStrBuilder.append(" ");
            for (Table table : tables) {
                tablesNameStrBuilder.append(" " + table.getName() + ",");
            }
            tablesNameStrBuilder.setLength(tablesNameStrBuilder.length() - 1);
            tablesNameStrBuilder.append(" ");
        } else {
            return null;
        }
        return tablesNameStrBuilder.toString();
    }

    /**
     * 获取连接查询子句
     *
     * @param updateStatement Update语句
     * @return Where子句
     */
    private static String getJoins(Update updateStatement) {
        //获取所有join语句
        List<Join> joins = updateStatement.getJoins();
        StringBuilder joinStrBuilder = new StringBuilder();
        if (joins != null && joins.size() > 0) {
            joinStrBuilder.append(" ");
            for (Join join : joins) {
                joinStrBuilder.append(join.toString() + " ");
            }
        }

        return joinStrBuilder.toString();
    }

    /**
     * 获取Where子句
     *
     * @param updateStatement Update语句
     * @return Where子句
     */
    private static String getWhereClause(Update updateStatement) {
        //获取Where子句
        Expression where = updateStatement.getWhere();
        String whereStr = " " + where.toString() + " ";
        return whereStr;
    }

    /**
     * 获取连接查询子句
     *
     * @param updateStatement Update语句
     * @return Where子句
     */
    private static String getJoins(Delete updateStatement) {
        //获取所有join语句
        List<Join> joins = updateStatement.getJoins();
        StringBuilder joinStrBuilder = new StringBuilder();
        if (joins != null && joins.size() > 0) {
            joinStrBuilder.append(" ");
            for (Join join : joins) {
                joinStrBuilder.append(join.toString() + " ");
            }
        }

        return joinStrBuilder.toString();
    }

    /**
     * 获取Where子句
     *
     * @param updateStatement Update语句
     * @return Where子句
     */
    private static String getWhereClause(Delete updateStatement) {
        //获取Where子句
        Expression where = updateStatement.getWhere();
        String whereStr = " " + where.toString() + " ";
        return whereStr;
    }

}
