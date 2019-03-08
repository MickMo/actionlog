package com.micheal.actionlog.enums.sql;

/**
 * SQL 类型
 *
 * @author Monan
 * created on 2018/12/10 12:06
 */
public enum SQLTypeEnum {

    /**
     * 所有数据操作
     */
    All("delete,update,insert,select"),
    /**
     * 所有更改数据的操作 {@code Delete} {@code Update} {@code Insert}
     */
    Modified("delete,update,insert"),
    /**
     * {@code Delete}
     */
    Delete("delete"),
    /**
     * {@code Insert}
     */
    Insert("insert"),
    /**
     * {@code Select}
     */
    Select("select"),
    /**
     * {@code Update}
     */
    Update("update")
    ;


    private String type;

    /**
     * 构造器
     * @param desc 描述
     */
    SQLTypeEnum(String desc) {
        this.type = desc;
    }

    /**
     * 获取描述
     * @return 描述
     */
    public String getValue() {
        return this.type;
    }
}
