/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.litms.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.RPCManager;
import ACS.RPCManagerHelper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.Global;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

/**
 * 读取数据库中数据，初始化数据
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Feb 28, 2008
 * @see
 * @since 1.0
 */
public class DBInit {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(DBInit.class);
	/** instance */
	private static DBInit instance = null;
	/** temp map */
	private Map fields = null;
	/** sql */
	private String sql = null;

	private static ORB orb;
	
	private static final int DB_MYSQL=com.linkage.module.gwms.Global.DB_MYSQL;
	
	/** get instance */
	public static DBInit GetInstance() {
		if (instance == null) {
			instance = new DBInit();
			initORB();
		}
		return instance;
	}

	/**
	 * 初始化ORB
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2010-4-12
	 * @return ORB
	 */
	private static ORB initORB(){
		String[] args = null;
		return ORB.init(args, null);
	}
	
	/**
	 * 初始化业务类型
	 */
	public void initServ_Type_Map() {
		logger.debug("初始化业务类型");
		if (Global.Serv_type_Map == null) {
			Global.Serv_type_Map = new HashMap<Object, Object>();
		}
		sql = "select * from tab_gw_serv_type";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select serv_type_id, serv_type_name from tab_gw_serv_type";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		fields = cursor.getNext();
		while (fields != null) {
			Global.Serv_type_Map.put(fields.get("serv_type_id"), fields
					.get("serv_type_name"));
			fields = cursor.getNext();
		}
		// clear
		fields = null;
		cursor = null;
		sql = null;
	}

	/**
	 * 初始化操作类型
	 */
	public void initOper_Type() {
		logger.debug("初始化操作类型");
		if (Global.Oper_type_Id_Name_Map == null) {
			Global.Oper_type_Id_Name_Map = new HashMap<String, String>();
		}
		sql = "select * from tab_gw_oper_type";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select oper_type_id, oper_type_name from tab_gw_oper_type";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		fields = cursor.getNext();
		while (fields != null) {
			Global.Oper_type_Id_Name_Map
					.put((String) fields.get("oper_type_id"), (String) fields
							.get("oper_type_name"));
			fields = cursor.getNext();
		}
		// clear
		fields = null;
		cursor = null;
		sql = null;
	}

	/**
	 * 初始化服务
	 */
	public void initService() {
		if (Global.Service_Id_Name_Map == null) {
			Global.Service_Id_Name_Map = new HashMap<String, String>();
		}
		if (Global.Service_Id_Info_Map == null) {
			Global.Service_Id_Info_Map = new HashMap<String, String[]>();
		}
		if (Global.G_Service_Info_Id_Map == null) {
			Global.G_Service_Info_Id_Map = new HashMap<String, String>();
		}
		sql = "select * from tab_service";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select service_id, serv_type_id, oper_type_id, wan_type, flag, service_name from tab_service";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		fields = cursor.getNext();
		while (fields != null) {
			Global.Service_Id_Name_Map.put((String) fields.get("service_id"),
					(String) fields.get("service_name"));
			Global.Service_Id_Info_Map.put((String) fields.get("service_id"),
					new String[] { (String) fields.get("serv_type_id"),
							(String) fields.get("oper_type_id"),
							(String) fields.get("wan_type"),
							(String) fields.get("flag"),
							(String) fields.get("service_name") });
			Global.G_Service_Info_Id_Map.put((String) fields
					.get("serv_type_id")
					+ "#"
					+ (String) fields.get("oper_type_id")
					+ "#"
					+ (String) fields.get("wan_type"), (String) fields
					.get("service_id"));
			fields = cursor.getNext();
		}
		// clear
		fields = null;
		cursor = null;
		sql = null;
	}

	/**
	 * 用户业务初始化
	 * 
	 */
	public void initUsertype() {

		if (Global.G_Usertype_Servtype_Map == null) {
			Global.G_Usertype_Servtype_Map = new HashMap<String, String>();
		}
		String sql = "select * from gw_usertype_servtype";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select serv_type_id, user_type from gw_usertype_servtype";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		fields = cursor.getNext();
		while (fields != null) {

			Global.G_Usertype_Servtype_Map.put((String) fields
					.get("serv_type_id"), (String) fields.get("user_type"));
			fields = cursor.getNext();
		}
		// clear
		fields = null;
		cursor = null;
		sql = null;
	}
	/**
	 * 设备类型初始化
	 * 
	 */
	public void initDeviceType() {

		if (Global.G_DeviceTypeID_Name_Map == null) {
			Global.G_DeviceTypeID_Name_Map = new HashMap<String, String>();
		}
		if (Global.G_DeviceTypeID_Name_List_Map == null) {
			Global.G_DeviceTypeID_Name_List_Map = new ArrayList<Map<String,String>>();
		}
		String sql = "select * from gw_dev_type";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select type_name, type_id from gw_dev_type";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		fields = cursor.getNext();
		while (fields != null) {
			Global.G_DeviceTypeID_Name_Map.put((String) fields
					.get("type_id"), (String) fields.get("type_name"));
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("type_id", (String) fields.get("type_id"));
			map.put("type_name", (String) fields.get("type_name"));
			Global.G_DeviceTypeID_Name_List_Map.add(map);
			fields = cursor.getNext();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("type_id", "0");
		map.put("type_name", "全部");
		Global.G_DeviceTypeID_Name_List_Map.add(map);
		// clear
		fields = null;
		cursor = null;
		sql = null;
	}
	/**
	 * init corba.
	 */
	public void initMidwareCorba() {
		// init server home.
		String sql = "select * from tab_ior";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select object_name, object_poa, ior from tab_ior";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		String ior = "";
		String object_name = "";
		String object_poa = "";
		org.omg.CORBA.Object objRef = null;
		ORB orb = null;
		if (fields != null) {
			while (fields != null) {

				object_name = (String) fields.get("object_name");
				object_poa = (String) fields.get("object_poa");
				ior = (String) fields.get("ior");

				if(null == orb){
					orb = initORB();
				}
				
				objRef = orb.string_to_object(ior);

				LipossGlobals.G_corbaObject.put(object_name + "," + object_poa,
						objRef);

				fields = cursor.getNext();
			}

		}
	}

	/**
	 * init corba.
	 */
	public void initACS4HCorba() {
		// init server home.
		String sql = "select * from tab_ior where object_name like '"
				+ Global.G_ACS4H_CORBA_NAME + "%'";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select object_name, ior from tab_ior where object_name like '"
					+ Global.G_ACS4H_CORBA_NAME + "%'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		String gather_id = "";
		String ior = "";
		org.omg.CORBA.Object objRef = null;
		ORB orb = null;
		RPCManager rpc = null;
		if (fields != null) {
			while (fields != null) {
				gather_id = (String) fields.get("object_name");
				gather_id = gather_id.substring(Global.G_ACS4H_CORBA_NAME
						.length());
				ior = (String) fields.get("ior");

				if(null == orb){
					orb = initORB();
				}
				
				objRef = orb.string_to_object(ior);
				rpc = RPCManagerHelper.narrow(objRef);
				Global.G_ACS4H_CORBA_Map.put(gather_id, rpc);

				fields = cursor.getNext();
			}

		}
	}

	/**
	 * init corba.
	 */
	public void initACS4ECorba() {
		// init server home.
		String sql = "select * from tab_ior where object_name like '"
				+ Global.G_ACS4E_CORBA_NAME + "%'";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select object_name, ior from tab_ior where object_name like '"
					+ Global.G_ACS4E_CORBA_NAME + "%'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		String gather_id = "";
		String ior = "";
		org.omg.CORBA.Object objRef = null;
		ORB orb = null;
		RPCManager rpc = null;
		if (fields != null) {
			while (fields != null) {
				gather_id = (String) fields.get("object_name");
				gather_id = gather_id.substring(Global.G_ACS4E_CORBA_NAME
						.length());
				ior = (String) fields.get("ior");

				if(null == orb){
					orb = initORB();
				}
				
				objRef = orb.string_to_object(ior);
				rpc = RPCManagerHelper.narrow(objRef);
				Global.G_ACS4E_CORBA_Map.put(gather_id, rpc);

				fields = cursor.getNext();
			}

		}
	}
}
