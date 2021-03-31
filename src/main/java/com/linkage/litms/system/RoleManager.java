/*
 * 
 * 创建日期 2006-1-23
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.system;

import java.util.ArrayList;

import com.linkage.litms.common.database.Cursor;

public interface RoleManager {

    /**
     * 创建角色对象
     * 
     * @param m_RoleId
     *            角色ID
     * @param m_RoleName
     *            角色名称
     * @param m_RoleDescr
     *            角色描述
     * @param m_Role_Pid
     *            角色父ID
     * @param m_Acc_Oid
     *            角色操作员
     * @return Role
     */
    public Role createRole(int m_RoleId, String m_RoleName, String m_RoleDesc, int m_Role_Pid, int m_Acc_Oid);

    /**
     * 创建角色对象
     * 
     * @param m_RoleId
     *            角色ID
     * @param m_RoleName
     *            角色名称
     * @param m_RoleDescr
     *            角色描述
     * @param m_Role_Pid
     *            角色父ID
     * @param m_Acc_Oid
     *            角色操作员ID
     * @param m_RoleInfo
     *            角色信息
     * @return Role
     */
    public Role createRole(int m_RoleId, String m_RoleName, String m_RoleDescr, int m_Role_Pid, int m_Acc_Oid, ArrayList m_RoleQuoraInfo);

    /**
     * 获得角色对象
     * 
     * @param m_RoleId
     * @return Role
     */
    public Role getRole(int m_RoleId);

    /**
     * 删除角色对象
     * 
     * @param m_RoleId
     * @return boolean
     */
    public boolean delRole(int m_RoleId);

    /**
     * 删除角色对象
     * 
     * @param m_Role
     * @return boolean
     */
    public boolean delRole(Role m_Role);
    
    /**
     * 刷新角色对象
     * @param m_Role
     * @return  boolean
     */
    public boolean refRole(Role m_Role);
    
    /**
     * 刷新角色对象
     * @param m_RoleId
     * @param m_RoleName
     * @param m_RoleDescr
     * @param m_Role_Pid
     * @param m_Acc_Oid
     * @return boolean
     */
    public boolean refRole(int m_RoleId, String m_RoleName, String m_RoleDescr, int m_Role_Pid, int m_Acc_Oid);
    
    /**
     * 刷新角色对象
     * @param m_RoleId
     * @param m_RoleName
     * @param m_RoleDescr
     * @param m_Role_Pid
     * @param m_Acc_Oid
     * @param m_RoleQuoraInfo
     * @return boolean
     */
    public boolean refRole(int m_RoleId, String m_RoleName, String m_RoleDescr, int m_Role_Pid, int m_Acc_Oid, ArrayList m_RoleQuoraInfo);

    /**
     * 获取角色列表
     * 
     * @param startIndex
     * @param numResults
     * @return Cursor
     */
    public Cursor roles(int startIndex, int numResults);
    
    /**
     * 根据role_id得到级联出的所有的子孙role_id
     * RoleManager.java
     * @param m_RolePid
     * @return
     * Cursor
     */
    public Cursor getAllRolesByRolePid(int m_RolePid);
    
}
