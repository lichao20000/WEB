package com.linkage.litms.paramConfig;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.SysManagerClient;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.software.VersionManage;
import com.linkage.module.gwms.Global;

public class ScheludeJsp {
	private static Logger m_logger = LoggerFactory.getLogger(ScheludeJsp.class);
	
	private PrepareSQL pSQL = null;

	private static Map iorMap = null;

	/**
	 * 根据sheet_id获取gather_id
	 */
	private String gatherIdFromSheetId = "select gather_id from tab_gw_device where device_id in(select device_id from tab_sheet where sheet_id=?)";
	
	private String protIDFromSheetId = "select prot_id from tab_sheet where sheet_id=?";

	public ScheludeJsp() {
		pSQL = new PrepareSQL();
		if (iorMap == null) {
			loadIorMap();
		}
	}

	/**
	 * 获取IOR映射关系
	 * 
	 * @return Map
	 */
	private Map loadIorMap() {
		iorMap = new HashMap();

		String sql = "select * from tab_ior";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select object_name, object_poa, ior from tab_ior";
		}
		pSQL.setSQL(sql);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map fields = cursor.getNext();

		if (fields != null) {
			while (fields != null) {
				String object_name = (String) fields.get("object_name");
				String object_poa = (String) fields.get("object_poa");
				String ior = (String) fields.get("ior");
				String mapKey = object_name + "," + object_poa;
				iorMap.put(mapKey, ior);
				fields = cursor.getNext();
			}

		}

		return iorMap;
	}

	/**
	 * 调用ACS系统维护corba接口
	 * 
	 * @param sys_item
	 * @return boolean
	 */
	public void systemManage(String sys_item) {
		SysManagerClient client = null;
		int[] taskArr = new int[] { Integer.parseInt(sys_item) };

		try {
			client = new SysManagerClient();
			client.manager.SysManager(taskArr);
		} catch (Exception e) {
			m_logger.error("system manage do error :" + e);
			client.rebindManagerCorbaInterface();

			client.manager.SysManager(taskArr);
		}
	}

	/**
	 * 根据sheet_id获取gather_id
	 * 
	 * @param sheet_id
	 * @return String
	 */
	public String getGatherId(String sheet_id) {
		pSQL.setSQL(gatherIdFromSheetId);
		pSQL.setString(1, sheet_id);
		m_logger.debug(pSQL.getSQL());
		Map field = DataSetBean.getRecord(pSQL.getSQL());

		if (field != null) {
			return (String) field.get("gather_id");
		}

		return null;
	}
	
	/**
	 * 根据sheet_id获取协议类型prot_id
	 * @param sheet_id  工单编号
	 * @return 协议类型prot_id    1:TR069 2:SNMP
	 */
	public String getSheetType(String sheet_id){
		pSQL.setSQL(protIDFromSheetId);
		pSQL.setString(1, sheet_id);
		m_logger.debug(pSQL.getSQL());
		Map field = DataSetBean.getRecord(pSQL.getSQL());

		if (field != null) {
			return (String) field.get("prot_id");
		}

		return null;
	}
	
	/**
	 * 批量工单通知后台（SNMP设备）
	 * 
	 * @param strGatherId  采集点
	 * @param sheet_id_array   工单编号
	 * @param receive_time_array   工单接收时间
	 * @return boolean  成功标志  false：失败  true：成功
	 */
	public boolean sheetInformSNMP(String strGatherId, String[] sheet_id_array,String[] receive_time_array) {
		boolean flag = false;
		
		//调用SNMP设备的处理方法
		VersionManage versionManage = new VersionManage();
		versionManage.transferCorbaForVersionUpdate(sheet_id_array);

		return flag;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
