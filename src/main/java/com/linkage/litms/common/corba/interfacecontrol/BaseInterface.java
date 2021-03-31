package com.linkage.litms.common.corba.interfacecontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.Log;
import com.linkage.module.gwms.Global;



/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class BaseInterface
{
	private static String fileName ="corba.log";
	
	protected ArrayList ProcessList = new ArrayList();

	String ProcessName = null;

	public BaseInterface()
	{
	}

	public void SetProcessName(String name)
	{
		ProcessName = name;
	}

	public void InitProcessList()
	{
		PrepareSQL psql = new PrepareSQL("select gather_id,process_number from tab_process where process_name='"
				+ ProcessName + "'");
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor("select gather_id,process_number from tab_process where process_name='"
											+ ProcessName + "'");
		Map fields = cursor.getNext();
		if(null==fields)
		{
			Log.writeLog("serial was not set for process  "+ProcessName+"  "+new DateTimeUtil().getLongDate(),fileName);					
		}
		else
		{
			while(null!=fields)
			{
				GatherProcess process = new GatherProcess();
				process.GatherId = (String)fields.get("gather_id");
				process.ProcessCount = Integer.parseInt((String)fields.get("process_number"));
				process.SetProcessName(ProcessName);
				process.InitProcess();
				process.print();
				ProcessList.add(process);
				fields = cursor.getNext();
			}
		}
		
	}
	
	/**
	 * 根据device_id获取设备信息
	 * @param device_id
	 * @return
	 */
	public HashMap getDeviceInfo(String device_id)
	{
		String sql = "select a.gather_id,a.loopback_ip,a.snmp_udp,b.* from tab_gw_device a,sgw_security b where a.device_id=b.device_id and a.device_id='"+device_id+"'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select a.gather_id,a.loopback_ip,a.snmp_udp, " +
					"b.device_id,b.snmp_version,b.is_enable,b.security_username,b.security_model,b.engine_id," +
					"b.context_name,b.security_level,b.auth_protocol,b.auth_passwd,b.privacy_protocol," +
					"b.privacy_passwd,b.snmp_r_passwd,b.snmp_w_passwd " +
					" from tab_gw_device a,sgw_security b where a.device_id=b.device_id and a.device_id='"+device_id+"'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return DataSetBean.getRecord(sql);
		
	}
	
	
	/**
	 * 根据device_id获取企业网关设备信息
	 * @param device_id
	 * @return
	 */
	public HashMap getGWDeviceInfo(String device_id)
	{
		String sql = "select a.gather_id,a.loopback_ip,a.snmp_udp,b.* from tab_gw_device a,sgw_security b where a.device_id=b.device_id and a.device_id='"+device_id+"'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select a.gather_id,a.loopback_ip,a.snmp_udp, " +
					"b.device_id,b.snmp_version,b.is_enable,b.security_username,b.security_model,b.engine_id," +
					"b.context_name,b.security_level,b.auth_protocol,b.auth_passwd,b.privacy_protocol," +
					"b.privacy_passwd,b.snmp_r_passwd,b.snmp_w_passwd " +
					" from tab_gw_device a,sgw_security b where a.device_id=b.device_id and a.device_id='"+device_id+"'";
		}

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return DataSetBean.getRecord(sql);
	}

	// 根据采集点获得此采集点进程的数量 added by wanghaifeng at 20070607
	private int GetProcessNum(String gather_id)
	{
		if (gather_id == null)
			return -1;
		for (int i = 0; i < ProcessList.size(); i++)
		{
			GatherProcess process = (GatherProcess) ProcessList.get(i);
			if (process.GatherId.compareTo(gather_id) == 0)
			{
				if (process.ProcessCount == 1 || process.ProcessCount == 0)
					return 1;
				return process.ProcessCount;
			}
		}
		return -1;
	}

	// 根据process num和采集点编号算出位置信息 added by wanghaifeng at 20070607
	private int GetLocByDeviceIdAndProcessNum(String device_id, int process_num)
	{
		if (device_id == null)
			return -1;
		if (process_num <= 0)
			return -1;

		int serial = Integer.parseInt(device_id);
		int location = serial % process_num;
		return location;
	}

	public int GetLocByGatherIdAndDeviceId(String gather_id, String device_id)
	{
		int process_num = GetProcessNum(gather_id);
		return GetLocByDeviceIdAndProcessNum(device_id, process_num);
	}

	public GatherProcess SearchProcessByObject(HashMap deviceInfo)
	{
		if (deviceInfo == null)
			return null;
		for (int i = 0; i < ProcessList.size(); i++)
		{
			GatherProcess process = (GatherProcess) ProcessList.get(i);
			if (process.GatherId.equals((String)deviceInfo.get("gather_id")))
			{
				if (process.ProcessCount == 1 || process.ProcessCount == 0)
					return process;
				return process
								.SearchProcessByID((String)deviceInfo.get("device_id"));
			}
		}
		Log.writeLog("can not find GatherProcess for "
						+ this.ProcessName + "  GatherID : " + (String)deviceInfo.get("gather_id"),fileName);
		return null;
	}

	public GatherProcess SearchProcessByGatherID(String gather_id, String loc)
	{
		if (gather_id == null)
			return null;
		if (loc == null)
			return null;
		int location = Integer.parseInt(loc);
		for (int i = 0; i < ProcessList.size(); i++)
		{
			GatherProcess process = (GatherProcess) ProcessList.get(i);
			if (process.GatherId.compareTo(gather_id) == 0)
			{
				if (process.ProcessCount == 1 || process.ProcessCount == 0)
					return process;
				return process.SearchProcessByLocation(location);
			}
		}
		return null;
	}

	public GatherProcess SearchProcessByGatherID(String gather_id, int location)
	{
		if (gather_id == null)
			return null;
		for (int i = 0; i < ProcessList.size(); i++)
		{
			GatherProcess process = (GatherProcess) ProcessList.get(i);
			if (process.GatherId.compareTo(gather_id) == 0)
			{
				if (process.ProcessCount == 1 || process.ProcessCount == 0)
					return process;
				return process.SearchProcessByLocation(location);
			}
		}
		return null;
	}

	public GatherProcess SearchProcessByGatherID(String gather_id)
	{
		if (gather_id == null)
			return null;
		for (int i = 0; i < ProcessList.size(); i++)
		{
			GatherProcess process = (GatherProcess) ProcessList.get(i);
			if (process.GatherId.compareTo(gather_id) == 0)
			{
				return process;
			}
		}
		return null;
	}

}
