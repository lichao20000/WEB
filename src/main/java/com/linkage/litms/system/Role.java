/*
 * 
 * 创建日期 2006-1-20
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.system;

import java.util.ArrayList;
import java.util.Map;

public interface Role {
    /**
     * 设置角色名称
     * 
     * @param m_RoleName
     */
    public void setRoleName(String m_RoleName);

    /**
     * 设置角色描述
     * 
     * @param m_RoleDesc
     */
    public void setRoleDesc(String m_RoleDesc);

    /**
     * 设置角色PID
     * 
     * @param m_Role_Pid
     */
    public void setRolePid(int m_Role_Pid);

    /**
     * 设置操作人员ID
     * 
     * @param m_Acc_Oid
     */
    public void setAccOid(int m_Acc_Oid);

    /**
     * 获取角色名称
     * 
     * @return String
     */
    public String getRoleName();

    /**
     * 获取角色描述
     * 
     * @return String
     */
    public String getRoleDesc();

    /**
     * 获取角色PID
     * 
     * @return int
     */
    public int getRolePid();

    /**
     * 获取角色操作人员ID
     * 
     * @return int
     */
    public int getAccoid();

    /**
     * 获取角色对象详细信息
     * 
     * @return Map
     */
    public Map getRoleInfo();

    /**
     * 设置角色对象详细信息
     * 
     * @param m_RoleInfo
     */
    public void setRoleInfo(Map m_RoleInfo);

    /**
     * 设置角色ID
     * 
     * @param m_RoleId
     */
    public void setRoleId(int m_RoleId);

    /**
     * 获取角色ID
     * 
     * @return int
     */
    public int getRoleId();

    /**
     * 获取角色权限
     * 
     * @return ArrayList
     */
    public ArrayList getRoleQuora();

    /**
     * 设置角色权限
     * 
     * @param m_RoleQuora
     */
    public void setRoleQuora(ArrayList m_RoleQuora);
}
