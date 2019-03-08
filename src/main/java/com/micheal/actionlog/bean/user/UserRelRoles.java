package com.micheal.actionlog.bean.user;

import java.util.Date;

/**
 * <内容说明>
 *
 * @author liu.jiawei
 * created on 2018/11/22 23:34
 */
public class UserRelRoles {

    /**
     * 主键Id
     */
    private long id;
    /**
     * 用户Id
     */
    private long userId;
    /**
     * 角色Id
     */
    private long rolesId;
    /**
     * 是否删除
     */
    private Integer deleteFlag;
    /**
     * 创建用户id
     */
    private Integer createUserId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新userId
     */
    private Integer updateUserId;
    /**
     *更新时间
     */
    private Date updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRolesId() {
        return rolesId;
    }

    public void setRolesId(long rolesId) {
        this.rolesId = rolesId;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        if (createTime != null) {
            return new Date(createTime.getTime());
        } else {
            return null;
        }
    }

    public void setCreateTime(Date createTime) {
        if (createTime != null) {
            this.createTime = (Date) createTime.clone();
        } else {
            this.createTime = null;
        }
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateTime() {
        if (updateTime != null) {
            return new Date(updateTime.getTime());
        } else {
            return null;
        }
    }

    public void setUpdateTime(Date updateTime) {
        if (updateTime != null) {
            this.updateTime = (Date) updateTime.clone();
        } else {
            this.updateTime = null;
        }
    }

    @Override
    public String toString() {
        return "UserRelRoles{" +
                "id=" + id +
                ", userId=" + userId +
                ", rolesId=" + rolesId +
                ", deleteFlag=" + deleteFlag +
                ", createUserId=" + createUserId +
                ", createTime=" + createTime +
                ", updateUserId=" + updateUserId +
                ", updateTime=" + updateTime +
                '}';
    }
}
