/**
 * 
 */
package com.linkage.litms.common.corba;

import java.util.Map;

import org.omg.CORBA.ORB;

import SysManager.Manager;
import SysManager.ManagerHelper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;

/**
 * 完成定时调度工作：调度系统维护接口和数据库备份接口
 * 
 * @author lizhaojun
 */
public class SysManagerClient {
	/**
	 * 系统维护和数据库备份corba对象
	 */
	public static Manager manager = null;

	/**
	 * 获得系统维护和数据库备份corba对象ior串
	 */
	private String to_ior = "select ior from tab_ior where object_name='SysManager' and object_poa='SysManager_Poa'";

	private static String ior = null;
	
	/**
	 * 
	 */
	public SysManagerClient() {
		getManagerCorbaInterface();
	}

	/**
	 * 获得系统维护corba对象
	 */
	private void getManagerCorbaInterface() {
		if (manager == null) {
			ior = getIor();

			try {
				String[] args = null;
				ORB orb = ORB.init(args, null);
				org.omg.CORBA.Object objRef = orb.string_to_object(ior);
				manager = ManagerHelper.narrow(objRef);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 重新绑定系统维护corba对象
	 */
	public void rebindManagerCorbaInterface() {
		ior = getIor();

		try {
			String[] args = null;
			ORB orb = ORB.init(args, null);
			org.omg.CORBA.Object objRef = orb.string_to_object(ior);
			manager = ManagerHelper.narrow(objRef);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取ior
	 * 
	 * @return String
	 */
	private String getIor() {
		PrepareSQL psql = new PrepareSQL(to_ior);
		psql.getSQL();
		Map map = DataSetBean.getRecord(to_ior);

		if (null != map) {
			return (String) map.get("ior");
		}

		return null;
	}

	/**
	 * 
	 * @param backup_id
	 * @param type
	 * @return int
	 */
	public int dbManager(int backup_id, short type) {
		int arr_tab = 0;
		try {
			arr_tab = manager.DBManager(backup_id, type);
		} catch (Exception e) {
			e.printStackTrace();
			try{
				getManagerCorbaInterface();
				arr_tab = manager.DBManager(backup_id, type);
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return arr_tab;
	}

}
