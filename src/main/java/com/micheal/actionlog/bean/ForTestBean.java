package com.micheal.actionlog.bean;

/**
 * @author Monan
 * created on 2/27/2019 9:42 AM
 */
public class ForTestBean {

    /**
     * ID
     */
    private Long id;


    /**
     * deleteFlag 1-是 0-否
     */
    private Integer deleteFlag;


    /**
     * Sets new ID.
     *
     * @param id New value of ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets deleteFlag 1-是 0-否.
     *
     * @return Value of deleteFlag 1-是 0-否.
     */
    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * Sets new deleteFlag 1-是 0-否.
     *
     * @param deleteFlag New value of deleteFlag 1-是 0-否.
     */
    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * Gets ID.
     *
     * @return Value of ID.
     */
    public Long getId() {
        return id;
    }
}
