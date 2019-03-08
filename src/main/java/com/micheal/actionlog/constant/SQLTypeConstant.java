package com.micheal.actionlog.constant;

/**
 * SQL 类型
 *
 * @author Monan
 * created on 2018/12/10 12:06
 */
public class SQLTypeConstant {

    /**
     * 所有数据操作
     */
    public final static String ALL = "delete,update,insert,select";
    /**
     * 所有更改数据的操作 {@code Delete} {@code Update} {@code Insert}
     */
    public final static String Modified = "delete,update,insert";
    /**
     * 部分更改数据的操作 {@code Delete} {@code Update}
     */
    public final static String Effected = "delete,update";
    /**
     * {@code Delete}
     */
    public final static String Delete = "delete";
    /**
     * {@code Insert}
     */
    public final static String Insert = "insert";
    /**
     * {@code Update}
     */
    public final static String Update = "update";


}
