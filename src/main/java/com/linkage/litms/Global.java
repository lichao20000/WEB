/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.litms;

import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;

import ACS.RPCManager;

/**
 *
 * @author  Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Feb 28, 2008 
 * @see     
 * @since   1.0
 */
public class Global {
	/**ACS4H_CORBA_NAME*/
	public static String G_ACS4H_CORBA_NAME = null;
	/**ACS4E_CORBA_NAME*/
	public static String G_ACS4E_CORBA_NAME = null;
	/**业务类型map(serv_type_id,serv_type_name)*/
	public static Map<Object, Object> Serv_type_Map;

	/**操作类型map(oper_type_id,oper_type_name)*/
	public static Map<String, String> Oper_type_Id_Name_Map;

	/**服务类型名称map(service_id,service_name)*/
	public static Map<String, String> Service_Id_Name_Map;

	/**服务类型信息map(service_id,String[]{serv_type_id,oper_type_id,wan_type,flag,service_name})*/
	public static Map<String, String[]> Service_Id_Info_Map;
	
	/**服务ID map(serv_type_id#oper_type_id#wan_type, service_id)*/
	public static Map<String, String> G_Service_Info_Id_Map;
	
	/**用户业务 map(serv_type_id,user_type)*/
	public static Map<String, String> G_Usertype_Servtype_Map;
	
	/**HGW CORBA map(gather_id,RPCManager)*/
	public static Map<String, RPCManager> G_ACS4H_CORBA_Map;
	
	/**HGW CORBA map(gather_id,RPCManager)*/
	public static Map<String, RPCManager> G_ACS4E_CORBA_Map;
	
	/**新疆IPOSS与ITMS属地映射 */
	//public static Map<String, String> GW_CITY_MAP_INFO = null;
	
	public static Map<String, String> CPE_FAULT_CODE_MAP = null;
	
	/**设备类型ID、设备类型名Map<devicetype_id,devicetype_name>  table:gw_dev_type*/
	public static Map<String,String> G_DeviceTypeID_Name_Map = null;
	
	public static List<Map<String,String>> G_DeviceTypeID_Name_List_Map = null;
	
	/** curatorFramework */
	public static CuratorFramework curatorFramework = null;
}
