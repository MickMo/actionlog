package com.micheal.actionlog.bean.user;

import java.util.Date;

/**
 * @description 角色
 **/
public class Roles {
    private String rolesid;
    private String rolesname;
    private String rolescode;
    private Integer deleteflag;
    private Integer createuserid;
    private Date createtime;
    private Integer updateuserid;
    private Date updatetime;
    private String remark;

    public String getRolesid() {
        return rolesid;
    }

    public void setRolesid(String rolesid) {
        this.rolesid = rolesid;
    }

    public String getRolesname() {
        return rolesname;
    }

    public void setRolesname(String rolesname) {
        this.rolesname = rolesname;
    }

    public String getRolescode() {
        return rolescode;
    }

    public void setRolescode(String rolescode) {
        this.rolescode = rolescode;
    }

    public Integer getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

    public Integer getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(Integer createuserid) {
        this.createuserid = createuserid;
    }

    public Date getCreatetime() {
        if (createtime != null) {
            return new Date(createtime.getTime());
        } else {
            return null;
        }
    }

    public void setCreatetime(Date createtime) {
        if (createtime != null) {
            this.createtime = (Date) createtime.clone();
        } else {
            this.createtime = null;
        }
    }

    public Integer getUpdateuserid() {
        return updateuserid;
    }

    public void setUpdateuserid(Integer updateuserid) {
        this.updateuserid = updateuserid;
    }

    public Date getUpdatetime() {
        if (updatetime != null) {
            return new Date(updatetime.getTime());
        } else {
            return null;
        }
    }

    public void setUpdatetime(Date updatetime) {
        if (updatetime != null) {
            this.updatetime = (Date) updatetime.clone();
        } else {
            this.updatetime = null;
        }
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
