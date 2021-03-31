/*
 * 
 * 创建日期 2006-1-23
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.system.dbimpl;
/**
 * zhaixf(3412) 2008-05-14
 * req:NJLC_HG-BUG-ZHOUMF-20080508-001
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.linkage.commons.db.DBUtil;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.Role;
import com.linkage.litms.system.RoleManager;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;

public class RoleManagerSyb implements RoleManager {
    private Cursor cursor = null;

    private Map fields = null;

    private PrepareSQL pSQL = null;

    /**
     * 根据角色父ID获得此角色下面所有配置角色
     */
    private String m_Roles_ByRolePid_SQL = "select * from tab_role where role_pid=? or role_id=?";

    /**
     * 删除角色对象
     */
    private String m_DelRole_SQL = "delete from tab_role where role_id =?";

    /**
     * 删除角色权限
     */
    private String m_RoleQuora_Del_SQL = "delete from tab_role_operator where role_id=?";

    /**
     * 获得所有角色对象
     */
    private String m_RolesAll_Cursor_SQL = "select * from tab_role";

    public RoleManagerSyb() {
        super();
        pSQL = new PrepareSQL();
    }

    /**
     * 创建角色对象并返回
     * 
     * @param m_RoleId
     * @param m_RoleName
     * @param m_RoleDesc
     * @param m_Role_Pid
     * @param m_Acc_Oid
     * @return Role
     */
    public Role createRole(int m_RoleId, String m_RoleName, String m_RoleDesc, int m_Role_Pid, int m_Acc_Oid) {
        Role role = getRole(m_RoleId);
        // 角色对象ID若为零，则对象不存在
        if (role == null) {
            role = new RoleSyb(m_RoleId, m_RoleName, m_RoleDesc, m_Role_Pid, m_Acc_Oid);
        }

        return role;
    }

    /**
     * 根据角色ID获取角色对象
     * 
     * @param m_RoleId
     * @return Role
     */
    public Role getRole(int m_RoleId) {
        return new RoleSyb(m_RoleId);
    }

    /**
     * 删除角色对象以及权限
     * 
     * @param m_RoleId
     * @return boolean
     */
    public boolean delRole(int m_RoleId) {
        ArrayList list = new ArrayList();
        list.clear();

        pSQL.setSQL(m_DelRole_SQL);
        pSQL.setInt(1, m_RoleId);
        list.add(pSQL.getSQL());

        pSQL.setSQL(m_RoleQuora_Del_SQL);
        pSQL.setInt(1, m_RoleId);
        list.add(pSQL.getSQL());

        String[] arr_SQL = (String[]) list.toArray();
        list.clear();
        int[] iCode = DataSetBean.doBatch(arr_SQL);

        return (iCode != null) ? true : false;
    }

    /**
     * 删除指定角色对象
     * 
     * @param m_Role
     * @return boolean
     */
    public boolean delRole(Role m_Role) {
        return delRole(m_Role.getRoleId());
    }

    /**
     * 返回指定起点并顺延numResults个角色对象
     * 
     * @param startIndex
     * @param numResults
     * @return Cursor
     */
    public Cursor roles(int startIndex, int numResults) {
        Cursor temp_cursor = new Cursor();
        cursor = getAllRoles();
        fields = cursor.getNext();
        int mid = 0;
        // 到达startIndex
        while (fields != null && mid < startIndex) {
            mid++;

            fields = cursor.getNext();
        }

        mid = 0;
        // 提取数据
        while (fields != null && mid < numResults) {
            temp_cursor.add(fields);

            mid++;
            fields = cursor.getNext();
        }

        cursor = null;

        return temp_cursor;
    }

    /**
     * 获得所有角色对象
     * 
     * @return Cursor
     */
    public Cursor getAllRoles() {
    	pSQL.setSQL(m_RolesAll_Cursor_SQL);
        return DataSetBean.getCursor(pSQL.getSQL());
    }
    /**
     * 根据acc_oid从表tab_acc_role表中去role_id
     * @return
     */
    public Cursor getRolesFromAcc_Role(long acc_oid){
        String m_Roles = "select a.role_id,a.role_name,a.role_desc from tab_role a,tab_acc_role b where a.role_id = b.role_id and b.acc_oid = ?";
        pSQL.setSQL(m_Roles);
        pSQL.setLong(1, acc_oid);
        return DataSetBean.getCursor(pSQL.getSQL());
    }
    /**
     * 地市管理员能得到的权限roleid,不能将超级管理员的角色列出
     * @param role_id 超级管理员的role_id
     * @return
     */
    public Cursor getRoleByCityAdmin(long role_id){
        String m_Roles = "select * from tab_role where role_id != ?" ;
        pSQL.setSQL(m_Roles);
        pSQL.setLong(1, role_id);
        return DataSetBean.getCursor(pSQL.getSQL());
    }


    /**
     * 创建角色对象并返回
     * 
     * @param m_RoleId
     * @param m_RoleName
     * @param m_RoleDesc
     * @param m_Role_Pid
     * @param m_Acc_Oid
     * @param m_RoleQuoraInfo
     * @return Role
     */
    public Role createRole(int m_RoleId, String m_RoleName, String m_RoleDesc, int m_Role_Pid, int m_Acc_Oid, ArrayList m_RoleQuoraInfo) {
        Role role = getRole(m_RoleId);
        // if (role.getRoleId() == 0) {
        if (role == null) {
            role = new RoleSyb(m_RoleId, m_RoleName, m_RoleDesc, m_Role_Pid, m_Acc_Oid, m_RoleQuoraInfo);
        }

        return role;
    }

    /**
     * 刷新角色对象信息
     * 
     * @param m_Role
     * @return boolean
     */
    public boolean refRole(Role m_Role) {
        boolean flag = false;

        if (m_Role != null) {
            RoleSyb rSyb = new RoleSyb(m_Role);
            flag = rSyb.updateRoleInfoToDb();

            rSyb = null;
        }

        return flag;
    }

    /**
     * 刷新角色对象信息
     * 
     * @param m_RoleId
     * @param m_RoleName
     * @param m_RoleDesc
     * @param m_Role_Pid
     * @param m_Acc_Oid
     * @return boolean
     */
    public boolean refRole(int m_RoleId, String m_RoleName, String m_RoleDescr, int m_Role_Pid, int m_Acc_Oid) {
        boolean flag = false;

        RoleSyb rSyb = new RoleSyb(m_RoleId, m_RoleName, m_RoleDescr, m_Role_Pid, m_Acc_Oid);
        flag = rSyb.updateRoleInfoToDb();

        rSyb = null;

        return flag;
    }

    /**
     * 刷新角色和权限信息
     * 
     * @param m_RoleId
     * @param m_RoleName
     * @param m_RoleDesc
     * @param m_Role_Pid
     * @param m_Acc_Oid
     * @param m_RoleQuoraInfo
     * @return boolean
     */
    public boolean refRole(int m_RoleId, String m_RoleName, String m_RoleDescr, int m_Role_Pid, int m_Acc_Oid, ArrayList m_RoleQuoraInfo) {
        boolean flag = false;

        RoleSyb rSyb = new RoleSyb(m_RoleId, m_RoleName, m_RoleDescr, m_Role_Pid, m_Acc_Oid, m_RoleQuoraInfo);
        flag = rSyb.updateRoleInfoToDb();

        rSyb = null;

        return flag;
    }

    /**
     * 根据角色父ID获得下面所有角色对象
     * 
     * @param m_RolePid
     * @return Cursor
     */
    public Cursor getRolesByRolePid(int m_RolePid) {
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            m_Roles_ByRolePid_SQL = "select role_id, role_name, role_desc from tab_role where role_pid=? or role_id=?";
        }
        pSQL.setSQL(m_Roles_ByRolePid_SQL);
        pSQL.setInt(1, m_RolePid);
        pSQL.setInt(2, m_RolePid);
        
        return DataSetBean.getCursor(pSQL.getSQL());
    }

	/**
	 * @功能描述:
	 * @TODO Auto-generated method stub
	 * @return Cursor;
	 * 
	 * @author zhaixf
	 * @date 2008-05-13
	 */
	public Cursor getAllRolesByRolePid(int rolePid) {
		// 表示已经统计过的父节点，不需要重复统计。数据库中存在子节点和父节点都为1的情况，会导致死循环
		Set<String> alreadyPidSet = new HashSet<String>();
		alreadyPidSet.add(String.valueOf(rolePid));
		//获取所有的角色
		Cursor allRoles = getAllRoles();
		//当前传入角色的所有子角色
		Cursor result = new Cursor();
		Map map = new HashMap();
		//当前传入角色的下一级的子角色
		Cursor sonRoles = getNext(allRoles, Arrays.asList(String.valueOf(rolePid)));
		while(null != sonRoles && sonRoles.getRecordSize() > 0){
			// 将子角色加入到结果集中
			result.addCursor(sonRoles);
			
			Set pidset = new HashSet();
			while((map = sonRoles.getNext()) != null){
				String roleId = String.valueOf(map.get("role_id"));
				if (!alreadyPidSet.contains(roleId))
				{
					pidset.add(roleId);
					alreadyPidSet.add(roleId);
				}
			}
			allRoles.Reset();
			sonRoles = getNext(allRoles, pidset);
		}
		//is_default = 0表示为系统提供角色，对所有用户可见，不可改
		String sql = "select * from tab_role where is_default = 0";
		Cursor sor3 = DataSetBean.getCursor(sql);
		boolean falg = true;
		sonRoles = new Cursor();
		if(null != sor3 && sor3.getRecordSize() > 0){
			Map rmap = sor3.getNext();
			while(map!=null && "".equals(map)){
				falg = true;
				if(null != result && result.getRecordSize() > 0){
					Map tmap = result.getNext();
					while(tmap!=null){
						if(StringUtil.getStringValue(tmap.get("role_id")).equals(StringUtil.getStringValue(rmap.get("role_id")))){
							falg = false;
							break;
						}
						tmap = result.getNext();
					}
				}	
				if(falg){
					sonRoles.add(rmap);
				}
				rmap = sor3.getNext();
			}			
		}
		result.addCursor(sonRoles);
		return result;
	}
	
	/**
	 * 
	 * RoleManagerSyb.java
	 * @param cor
	 * @param pidColtion
	 * @return
	 * Cursor
	 * 
	 * @author zhaixf
	 * @date 2008-05-13
	 */
	public Cursor getNext(Cursor cor, Collection pidColtion){
		
		Cursor resultSor = new Cursor();
		if(null != cor && cor.getRecordSize() > 0){
			Map hmap = null;
			while((hmap = cor.getNext()) != null){
				if(pidColtion.contains(String.valueOf(hmap.get("role_pid")))){
					resultSor.add(hmap);
				}
			}
		}
		return resultSor;
	}

}
