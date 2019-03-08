package com.micheal.actionlog.util.sqlutil;

import com.micheal.actionlog.enums.sql.SQLTypeEnum;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

/**
 * SQL Type Judge Util
 *
 * @author Monan
 * created on 2018/12/19 16:25
 */
public class SQLTypeUtil {

    /**
     * Judge SQL Type<p>
     * support {@code Delete} {@code Update} {@code Select} {@code Insert}
     *
     * @param sqlStmt SQL Statement String
     * @return SQL Type
     * @see
     */
    public static SQLTypeEnum getSqlType(Statement sqlStmt) {
        if (sqlStmt instanceof Delete) {
            return SQLTypeEnum.Delete;
        } else if (sqlStmt instanceof Update) {
            return SQLTypeEnum.Update;
        } else if (sqlStmt instanceof Insert) {
            return SQLTypeEnum.Insert;
        } else if (sqlStmt instanceof Select) {
            return SQLTypeEnum.Select;
        }
//        else if (sqlStmt instanceof Alter) {
//            return SqlType.ALTER;
//        }
//        else if (sqlStmt instanceof CreateIndex) {
//            return SqlType.CREATEINDEX;
//        }
//        else if (sqlStmt instanceof CreateTable) {
//            return SqlType.CREATETABLE;
//        }
//        else if (sqlStmt instanceof CreateView) {
//            return SqlType.CREATEVIEW;
//        }
//        else if (sqlStmt instanceof Drop) {
//            return SqlType.DROP;
//        }
//        else if (sqlStmt instanceof Execute) {
//            return SqlType.EXECUTE;
//        }
//        else if (sqlStmt instanceof Merge) {
//            return SqlType.MERGE;
//        }
//        else if (sqlStmt instanceof Replace) {
//            return SqlType.REPLACE;
//        }
//        else if (sqlStmt instanceof Truncate) {
//            return SqlType.TRUNCATE;
//        }
//        else if (sqlStmt instanceof Upsert) {
//            return SqlType.UPSERT;
//        }
        return null;
    }

}
