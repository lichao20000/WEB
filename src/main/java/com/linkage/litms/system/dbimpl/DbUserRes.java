/*
 * @(#)DbUserRes.java	1.00 1/21/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.system.dbimpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.PrepareSQL;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;

/**
 * 对保存登录用户各种信息接口的数据库实现，如用户账号、用户资料、用户访问资源范围、 用户访问功能模块权限等
 * 
 * @author yuht
 * @version 1.00, 1/21/2006
 * @see UserRes
 * @since Liposs 2.1
 */
public class DbUserRes implements UserRes{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger m_logger = LoggerFactory.getLogger(DbUserRes.class);
	
	private User user = null;

	private ArrayList devList = null;

	private ArrayList permList = null;

	private ArrayList procList = null;

	private ArrayList vpnList = null;

	private ArrayList hostList = null;

	// 载入资源范围
	private String LOAD_RESAREA = "select res_id from tab_gw_res_area where area_id=?"
			+ " and res_type=?";

	// 载入菜单（权限）范围
	private String LOAD_PERMISSIONS = "select operator_name from tab_operator "
			+ "where operator_id in(select operator_id from tab_role_operator where "
			+ "role_id in (select role_id  from tab_acc_role where acc_oid =?))";
	
	private String LOAD_PERMISSIONS2 = "select operator_name from tab_operator "
		+ "where operator_id in(select operator_id from tab_role_operator where "
		+ "role_id in (?))";
	
	/**
	 * 获取所有的设备ID  
	 */
	private static String m_selectDevicesSQL = "select device_id from tab_gw_device";

	private static final int DEV_RES = 1;

	private static final int PROC_RES = 2;

	private static final int VPN_RES = 8;

	private static final int HOST_RES = 6;

	// private static final int REPORT_RES = 3;
	private PrepareSQL pSQL = null;

	/**
	 * 带参数构造函数
	 * 
	 * @param _user
	 *            用户对象
	 */
	public DbUserRes(User _user) {
		this.user = _user;
		if (pSQL == null)
			pSQL = new PrepareSQL();
		// devList = loadResArea(DEV_RES);//暂时注销初始化设备资源权限代码，判断是否此比较影响速度
		// procList = loadResArea(PROC_RES);
		// vpnList = loadResArea(VPN_RES);
		// hostList = loadResArea(HOST_RES);
		
		/**
		 * modify by qixueqi
		 */
//		if(LipossGlobals.SystemType()==2){
//			permList = loadPermissions();
//		}else{
//			permList = new ArrayList();
//		}
		permList = loadPermissions();
		
	}

	public User getUser() {
		return user;
	}

	public List getUserDevRes() {
		// 在第一次使用时加载
		if (devList == null) {
			devList = loadResArea(DEV_RES);
		}

		return devList;
	}
	
	public List getUserDevRes(User user) {
		this.user = user;
		
		// 在第一次使用时加载
		if (devList == null) {
			devList = loadResArea(DEV_RES);
		}

		return devList;
	}

	public List getUserPermissions() {
		return permList;
	}

	public List getUserVpnRes() {
		if (vpnList == null) {
			vpnList = loadResArea(VPN_RES);
		}

		return vpnList;
	}

	public List getHostRes() {
		if (hostList == null) {
			hostList = loadResArea(HOST_RES);
		}

		return hostList;
	}

	public List getUserProcesses() {
		if (procList == null) {
			procList = loadResArea(PROC_RES);
		}

		return procList;
	}

	public String getCityId() {
		Map map = user.getUserInfo();

		return (String) map.get("per_city");
	}

	/**
	 * 通过资源类型<code>_res_type</code>载入用户各种资源访问范围
	 * 
	 * @param _res_type
	 *            资源类型
	 * @return 资源范围列表
	 */
	private ArrayList loadResArea(int _res_type) {
		ArrayList list = new ArrayList();
		Cursor cursor = null;
		Map fields = null;
		
		//如果是admin用户装载设备，就把tab_gw_res_area中的所有设备装载进来
        if(user.isAdmin()&&_res_type==DEV_RES)
        {
        	PrepareSQL psql = new PrepareSQL(m_selectDevicesSQL);
            psql.getSQL();
        	cursor = DataSetBean.getCursor(m_selectDevicesSQL);
        	fields = cursor.getNext();

    		while (fields != null) {
    			list.add((String) fields.get("device_id"));

    			fields = cursor.getNext();
    		}
        }
        else
        {
        	pSQL.setSQL(LOAD_RESAREA);		
    		pSQL.setLong(1, user.getAreaId());
    		pSQL.setInt(2, _res_type);

    		cursor = DataSetBean.getCursor(pSQL.getSQL());
    		fields = cursor.getNext();

    		while (fields != null) {
    			list.add((String) fields.get("res_id"));

    			fields = cursor.getNext();
    		}   		
        }
        
        //clear
		fields = null;
		cursor = null;

		return list;
	}

	/**
	 * 载入用户对资源的操作权限
	 * 
	 * @return 菜单权限列表
	 */
	private ArrayList loadPermissions() {
		ArrayList list = new ArrayList();
		
		pSQL.setSQL(LOAD_PERMISSIONS);
		pSQL.setLong(1, user.getId());
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map fields = cursor.getNext();
		
		while (fields != null) {
			list.add((String) fields.get("operator_name"));

			fields = cursor.getNext();
		}

		// clear
		fields = null;
		cursor = null;

		return list;
	}
	/**
	 * 载入用户对资源的操作权限(角色可以是多个)
	 * 
	 * @return list （菜单权限列表）
	 * add wangfeng 2006-10-22 获得用户对应资源操作列表
	 */
	private ArrayList loadPermissions2() {
		ArrayList list = new ArrayList();
		ArrayList role_list = (ArrayList)user.getRole_list();
		String role_temp = role_list.toString();
		role_temp = role_temp.substring(1,role_temp.length()-1);
		pSQL.setSQL(LOAD_PERMISSIONS2);
		pSQL.setStringExt(1, role_temp,false);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map fields = cursor.getNext();
		while (fields != null) {
			list.add((String) fields.get("operator_name"));
			fields = cursor.getNext();
		}
		
		return list;
	}

	public long getAreaId() {
		// TODO 自动生成方法存根
		return user.getAreaId();
	}
}
