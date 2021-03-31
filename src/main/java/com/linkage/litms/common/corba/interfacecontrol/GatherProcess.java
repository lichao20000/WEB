package com.linkage.litms.common.corba.interfacecontrol;

import java.util.ArrayList;
import java.util.HashMap;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.Log;

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

public class GatherProcess
{
	private static Logger log = LoggerFactory.getLogger(GatherProcess.class);
	private static String fileName = "corba.log";

	// 如果对象是组对象，则此编号为组对象的编号
	// 如果对象组对象的子对象，则此编号为子对象的编号
	private String ProcessName = null;

	public String GatherId = null;

	public int ProcessCount = 1;

	private int Sub_ID = 0;

	private String LocalName = null;

	private String LocalPoaName = null;

	private ArrayList ProcessList = null;

	public org.omg.CORBA.Object Manager = null;

	// 进行服务定位
	static NameComponent path[] = { new NameComponent("", "PoaName"),
									new NameComponent("", "ObjectName") };
	private static NamingContext ncRef = null;

	public GatherProcess()
	{
	}

	public void SetManager(org.omg.CORBA.Object manager)
	{
		Manager = manager;
	}

	public void SetProcessName(String name)
	{
		ProcessName = name;
	}

	public ArrayList GetProcessList()
	{
		return ProcessList;
	}

	public GatherProcess SearchProcessByID(String UniqueID)
	{
		if (UniqueID == null)
			return null;

		int serial = Integer.parseInt(UniqueID);
		int location = serial % ProcessCount;
		if (ProcessList == null)
			return null;
		for (int i = 0; i < ProcessList.size(); i++)
		{
			GatherProcess process = (GatherProcess) ProcessList.get(i);
			if (process.Sub_ID == location)
			{
				return process;
			}
		}
		return null;
	}

	public GatherProcess SearchProcessByLocation(int location)
	{
		if (ProcessList == null)
			return null;
		for (int i = 0; i < ProcessList.size(); i++)
		{
			GatherProcess process = (GatherProcess) ProcessList.get(i);
			if (process.Sub_ID == location)
			{
				return process;
			}
		}
		return null;
	}

	private void InitSingleProcess()
	{
		String sql ="select para_context from tab_process_config where gather_id='"
			+ GatherId
			+ "' and process_name='"
			+ ProcessName
			+ "' and location='"+Sub_ID+"' and para_item='LocalName'";
//		log.debug("查询LocalNameSQL:"+sql);
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		// 查询LocalName
		HashMap record = DataSetBean
						.getRecord(sql);
		if (null != record)
		{
			LocalName = (String) record.get("para_context");
		}
		else
		{
			Log.writeLog("LocalName was not set for process " + ProcessName
							+ " gather_id " + GatherId, fileName);
			return;
		}
		
		sql="select para_context from tab_process_config where gather_id='"
										+ GatherId
										+ "' and process_name='"
										+ ProcessName
										+ "' and location='"+Sub_ID+"' and para_item='LocalPoaName'";
//		log.debug("查询LocalPoaName:"+sql);
		PrepareSQL psql2 = new PrepareSQL(sql);
		psql2.getSQL();
		// 查询LocalPoaName
		record = DataSetBean.getRecord(sql);
		if (null != record)
		{
			LocalPoaName = (String) record.get("para_context");
		}
		else
		{
			Log.writeLog("LocalPoaName was not set for process " + ProcessName
							+ " gather_id " + GatherId, fileName);
			return;
		}
	}

	private void InitMultiProcess()
	{
		HashMap record = null;
		for (int i = 0; i < ProcessCount; i++)
		{
			PrepareSQL psql = new PrepareSQL("select para_context from tab_process_config where gather_id='"
					+ GatherId
					+ "' and process_name='"
					+ ProcessName
					+ "' and location='"+i+"' and para_item='LocalName'");
			psql.getSQL();
			// 查询LocalName
			record = DataSetBean
							.getRecord("select para_context from tab_process_config where gather_id='"
											+ GatherId
											+ "' and process_name='"
											+ ProcessName
											+ "' and location='"+i+"' and para_item='LocalName'");
			if (null != record)
			{
				LocalName = (String) record.get("para_context");
			}
			else
			{
				Log.writeLog("LocalName was not set for process " + ProcessName
								+ " gather_id " + GatherId, fileName);
				return;
			}
			PrepareSQL psql2 = new PrepareSQL("select para_context from tab_process_config where gather_id='"
					+ GatherId
					+ "' and process_name='"
					+ ProcessName
					+ "' and location='"+i+"' and para_item='LocalPoaName'");
			psql2.getSQL();
			// 查询LocalPoaName
			record = DataSetBean
							.getRecord("select para_context from tab_process_config where gather_id='"
											+ GatherId
											+ "' and process_name='"
											+ ProcessName
											+ "' and location='"+i+"' and para_item='LocalPoaName'");
			if (null != record)
			{
				LocalPoaName = (String) record.get("para_context");
			}
			else
			{
				Log.writeLog("LocalPoaName was not set for process "
								+ ProcessName + " gather_id " + GatherId,
								fileName);
				return;
			}
			GatherProcess process = new GatherProcess();
			process.GatherId = GatherId;
			process.Sub_ID = i;
			process.ProcessCount = 1;
			process.SetProcessName(ProcessName);
			if(null==ProcessList)
			{
				ProcessList = new ArrayList();
			}
			ProcessList.add(process);
			process.InitProcess();
		}

	}

	public void InitProcess()
	{
		if (ProcessCount == 1)
		{
			InitSingleProcess();
		}
		else if (ProcessCount > 1)
		{
			InitMultiProcess();
		}
		else
		{
			Log.writeLog("ProcessNumber <=0 for process " + ProcessName
							+ "Serial " + GatherId, fileName);
		}

	}

	public void InitManager()
	{
		if (ProcessName == null)
		{
			Log.writeLog("Process Name was not set in group " + GatherId,
							fileName);
			return;
		}
		try
		{
			org.omg.CORBA.Object ref = GetObjectRef(LocalPoaName,
							LocalName);
			if (ProcessName.compareTo("Pmee") == 0)
			{
				Manager = PMEE.PMEEManagerHelper.narrow(ref);
			}
			else if (ProcessName.compareTo("SnmpGather") == 0)
			{
				Manager = Performance.PerformanceManagerHelper.narrow(ref);
			}else if (ProcessName.compareTo("SnmpPing") == 0)
			{
				Manager = I_Snmp_Ping.I_ConfigModifiedHelper.narrow(ref);
			}
			else if(ProcessName.compareTo("SnmpGwCheck") == 0){
				Manager = SnmpGwCheck.I_ConfigModifiedHelper.narrow(ref);
			}
			 else if (ProcessName.compareTo("IpCheck") == 0) {
		          Manager = I_Ip_Check.I_ObjectManagerHelper.narrow(ref);
		    }
			 else if (ProcessName.compareTo("FluxManager") == 0)
			{
					Manager = FluxControl.FluxDeviceConfHelper.narrow(ref);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Manager = null;
		}

	}

	static public org.omg.CORBA.Object GetObjectRef(String PoaName,
					String ObjectName)
	{
		org.omg.CORBA.Object ref = null;		
		try
		{
			PrepareSQL psql = new PrepareSQL("select ior from tab_ior where object_name='NameService' and object_poa='/rootPoa'and object_port=0");
			psql.getSQL();
			//获取ior
			HashMap record = DataSetBean.getRecord("select ior from tab_ior where object_name='NameService' and object_poa='/rootPoa'and object_port=0");
			String m_Ior="";
			if(null!=record)
			{
				m_Ior = (String)record.get("ior");
			}
			ORB orb = ORB.init((String[]) null, null);
			org.omg.CORBA.Object objRef = orb.string_to_object(m_Ior);
			ncRef = NamingContextHelper.narrow(objRef);
			path[0].id = PoaName;
			path[1].id = ObjectName;
			ref = ncRef.resolve(path);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ref = null;
		}		
		return ref;
	}

	public void print()
	{
		log.debug("ProcessName=" + this.ProcessName);
		log.debug("LocalName=" + this.LocalName + "  size:"
						+ this.LocalName.length());
		log.debug("LocalPoaName=" + this.LocalPoaName + "  size:"
						+ this.LocalPoaName.length());
		log.debug("GatherID=" + this.GatherId);
		log.debug("ProcessCount=" + this.ProcessCount);
		log.debug("Sub_ID=" + this.Sub_ID);

	}

}
