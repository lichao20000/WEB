/**
 * @(#)com.linkage.liposs.vipms.flux.ManagerFluxConfig.java
 * Liposs
 * DATE: 20060930
 * Copyright 2006 联创科技.版权所有
 */
package com.linkage.litms.vipms.flux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.interfacecontrol.FluxManagerInterface;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.Log;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.webtopo.snmpgather.ReadFluxConfigPortInfo;
import com.linkage.module.gwms.Global;

/**
 * <p>
 * Title: config flux
 * </p>
 * <p>
 * Description: config flux of device for webtopo.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Linkage Corporation.
 * </p>
 * 
 * @author Yan.HaiJian, Network Management Product Department, ID Card No.5126
 * @version 2.1
 */
public class ManagerFluxConfig
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ManagerFluxConfig.class);
	private HttpServletRequest request = null;

	private String mysql = "select * from flux_oid_map where model=? and snmpversion=?";

	private String snmpSQL = "select distinct snmpversion from flux_oid_map where model=?";

	private String m_strSQLSnmp = "select distinct snmpversion from flux_deviceportconfig where device_id=?";

	private String myport = "select ifindex,ifdescr,ifname,ifnamedefined,ifportip "
					+ "from flux_deviceportinfo where device_id=?";

	private String sql = "select count(1) as num from flux_deviceportconfig where device_id=? and gatherflag=1 and ifconfigflag=1";

	private PrepareSQL pSQL = null;

	private Cursor cursor = null;

	private HashMap typeOid = new HashMap();

	private ArrayList snmpList = new ArrayList();

	public ArrayList NoPorts = null;

	private String model = null;

	private String counternum = null;

	private Map filterMap = new HashMap();

	// 查询设备的设备IP、设备名称
	private String deviceInfoSql = "select device_name,loopback_ip from tab_deviceresource where device_id=?";

	// 查询设备上已经配置的端口
	private String deviceConfigPortSql = "select a.port_info,a.ifindex,a.ifdescr,a.ifname,a.ifnamedefined,a.ifportip,a.getway,b.gatherflag,b.ifconfigflag from flux_interfacedeviceport a, flux_deviceportconfig b where a.device_id=b.device_id and a.getway=b.getway and a.port_info=b.port_info and a.device_id=? order by a.ifindex";

	// 查询设备上某个端口的详细信息
	private String devicePortInfoSql = "select a.intodb,a.gatherflag,b.* from flux_deviceportconfig a,flux_interfacedeviceport b where a.device_id=b.device_id and a.getway=b.getway and a.port_info=b.port_info and a.device_id=? and a.getway=? and a.port_info=?";

	// 更新flux_interfacedeviceport中的端口信息的sql
	private String update_flux_interfacedeviceport_Sql = "update flux_interfacedeviceport set ifinoctetsbps_max=?,ifoutoctetsbps_max=?,ifindiscardspps_max=?,ifoutdiscardspps_max=?,"
					+ "ifinerrorspps_max=?,ifouterrorspps_max=?,warningnum=?,warninglevel=?,reinstatelevel=?,overper=?,overnum=?,overlevel=?,reinoverlevel=?,com_day=?,intbflag=?,ifinoctets=?,"
					+ "inwarninglevel=?,inoperation=?,inreinstatelevel=?,outtbflag=?,ifoutoctets=?,outwarninglevel=?,outoperation=?,outreinstatelevel=? where device_id=? and getway=? and port_info=?";
	
	//更新江苏flux_interfacedeviceport中的端口信息的sql
	private String update_flux_interfacedeviceport_js_Sql = "update flux_interfacedeviceport set ifinoct_maxtype=?,ifinoctetsbps_max=?,ifoutoct_maxtype=?,ifoutoctetsbps_max=?,ifindiscardspps_max=?,ifoutdiscardspps_max=?,"
					+ "ifinerrorspps_max=?,ifouterrorspps_max=?,warningnum=?,warninglevel=?,reinstatelevel=?,ifinoct_mintype=?,ifinoctetsbps_min=?,ifoutoct_mintype=?,ifoutoctetsbps_min=?,warningnum_min=?,warninglevel_min=?,reinlevel_min=?,"
					+"overmax=?,overper=?,overnum=?,overlevel=?,reinoverlevel=?,com_day=?,overmin=?,overper_min=?,overnum_min=?,overlevel_min=?,reinoverlevel_min=?,"
					+ "intbflag=?,ifinoctets=?,inwarninglevel=?,inoperation=?,inreinstatelevel=?,outtbflag=?,ifoutoctets=?,outwarninglevel=?,outoperation=?,outreinstatelevel=? where device_id=? and getway=? and port_info=?";

	// 更新flux_deviceportconfig中的端口信息的sql
	private String update_flux_deviceportconfig_Sql = "update flux_deviceportconfig set intodb=?,gatherflag=? where device_id=? and getway=? and port_info=?";

	// 删除flux_deviceportconfig的端口配置信息
	private String delete_flux_deviceportconfig_Sql = "delete from flux_deviceportconfig where device_id=? and getway=? and port_info=?";

	// 删除flux_interfacedeviceport的端口配置信息
	private String delete_flux_interfacedeviceport_Sql = "delete from flux_deviceportconfig where device_id=? and getway=? and port_info=?";

	// 装载flux_oid_map中的oid
	private static HashMap deviceOIDMap = new HashMap();

	// deviceOIDMap的锁
	private static Object deviceOIDMapObject = new Object();

	private String load_oid_Sql = "select * from flux_oid_map order by model,snmpversion,counternum,oid_type,oid_order";

	public ManagerFluxConfig()
	{
	}

	public ManagerFluxConfig(HttpServletRequest request)
	{
		this.request = request;
		if (pSQL == null)
		{
			pSQL = new PrepareSQL();
		}

		synchronized (deviceOIDMapObject)
		{
			if (0 == deviceOIDMap.size())
			{
				loadOID();
			}
		}
	}

	/**
	 * 获取指定模型的OID
	 * 
	 * @param serial
	 * @param snmpversion
	 * @param counternum
	 * @param oidtype
	 * @return
	 */
	public static ArrayList getOIDList(String serial, String snmpversion,
					String counternum, String oidtype)
	{
		HashMap tempSerialMap = null;
		;
		ArrayList list = new ArrayList();
		HashMap tempVersionAndCounterMap = null;

		// 有这个设备型号的OID
		if (null != deviceOIDMap.get(serial)
						&& 0 != ((Map) deviceOIDMap.get(serial)).size())
		{
			tempSerialMap = (HashMap) deviceOIDMap.get(serial);

			// 有这个版本计数器的OID
			if (null != tempSerialMap.get(snmpversion + "_" + counternum))
			{
				tempVersionAndCounterMap = (HashMap) tempSerialMap
								.get(snmpversion + "_" + counternum);

				// 有这个类型的OID
				if (null != tempVersionAndCounterMap.get(oidtype))
				{
					list = (ArrayList) tempVersionAndCounterMap.get(oidtype);
				}
			}
		}
		// 没有这个设备型号的OID，取用默认的OID(即serial为0)
		else if (!"0".equals(serial))
		{
			serial = "0";
			// 获取默认的oid
			tempSerialMap = (HashMap) deviceOIDMap.get(serial);

			// 有这个版本计数器的OID
			if (null != tempSerialMap.get(snmpversion + "_" + counternum))
			{
				tempVersionAndCounterMap = (HashMap) tempSerialMap
								.get(snmpversion + "_" + counternum);

				// 有这个类型的OID
				if (null != tempVersionAndCounterMap.get(oidtype))
				{
					list = (ArrayList) tempVersionAndCounterMap.get(oidtype);
				}
			}
		}

		// clear
		tempVersionAndCounterMap = null;
		tempSerialMap = null;

		// logger.debug("end getOIDList serial:"+serial+"
		// snmpversion:"+snmpversion+" counternum:"+counternum+"
		// oidtype:"+oidtype+" size:"+list.size());
		// for(int i=0;i<list.size();i++)
		// {
		// logger.debug("oid:"+((PortJudgeAttr)list.get(i)).getValue());
		// }
		return list;
	}

	/**
	 * 装载系统中的OID
	 * 
	 */
	private void loadOID()
	{
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			load_oid_Sql = "select model, snmpversion, counternum, oid_type, oid_desc, oid_name, oid_order, oid_value " +
					"from flux_oid_map order by model,snmpversion,counternum,oid_type,oid_order";
		}
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(load_oid_Sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(load_oid_Sql);
		Map fields = cursor.getNext();
		String model = "";
		String snmpversion = "";
		String counternum = "";
		String oid_type = "";
		String oid_desc = "";
		String oid_name = "";
		String oid_order = "";
		String oid_value = "";
		String temp_model = "";
		String temp_snmpversion = "";
		String temp_counternum = "";
		// 存放相同设备型号的不同版本、计数器的oid容器
		HashMap deviceModelOID = null;
		// 存放相同设备型号、相同版本、计数器的oid容器
		HashMap deviceModelVersionCounterOID = null;

		ArrayList typeOID = null;
		PortJudgeAttr pja = null;
		while (null != fields)
		{
			model = (String) fields.get("model");
			snmpversion = (String) fields.get("snmpversion");
			counternum = (String) fields.get("counternum");
			oid_type = (String) fields.get("oid_type");
			oid_desc = (String) fields.get("oid_desc");
			oid_name = (String) fields.get("oid_name");
			oid_order = (String) fields.get("oid_order");
			oid_value = (String) fields.get("oid_value");

			pja = new PortJudgeAttr();
			pja.setName(oid_name);
			pja.setDesc(oid_desc);
			pja.setValue(oid_value);
			pja.setOrder(oid_order);
			pja.setModel(model);

			// 不相同的serial
			if (!temp_model.equals(model))
			{
				temp_model = model;
				temp_snmpversion = snmpversion;
				temp_counternum = counternum;
				deviceModelOID = new HashMap();
				deviceModelVersionCounterOID = new HashMap();
				typeOID = new ArrayList();
				typeOID.add(pja);
				deviceModelVersionCounterOID.put(oid_type, typeOID);
				deviceModelOID.put(snmpversion + "_" + counternum,
								deviceModelVersionCounterOID);
				deviceOIDMap.put(temp_model, deviceModelOID);
			}
			// 相同的设备serial，不同的版本或计数器
			else if (temp_model.equals(model)
							&& (!temp_snmpversion.equals(snmpversion) || (!temp_counternum
											.equals(counternum))))
			{
				// 版本
				if (!temp_snmpversion.equals(snmpversion))
				{
					temp_snmpversion = snmpversion;
				}

				// 计数器
				if (!temp_counternum.equals(counternum))
				{
					temp_counternum = counternum;
				}

				deviceModelOID = (HashMap) deviceOIDMap.get(temp_model);
				deviceModelVersionCounterOID = new HashMap();
				typeOID = new ArrayList();
				typeOID.add(pja);
				deviceModelVersionCounterOID.put(oid_type, typeOID);
				deviceModelOID.put(snmpversion + "_" + counternum,
								deviceModelVersionCounterOID);
				deviceOIDMap.put(temp_model, deviceModelOID);
			}
			// 相同的设备serial，相同的版本，相同计数器的情况
			else if (temp_model.equals(model)
							&& temp_snmpversion.equals(snmpversion)
							&& temp_counternum.equals(counternum))
			{
				deviceModelOID = (HashMap) deviceOIDMap.get(temp_model);
				deviceModelVersionCounterOID = (HashMap) deviceModelOID
								.get(snmpversion + "_" + counternum);
				// 包含这个类型
				if (deviceModelVersionCounterOID.containsKey(oid_type))
				{
					typeOID = (ArrayList) deviceModelVersionCounterOID
									.get(oid_type);
				}
				else
				{
					typeOID = new ArrayList();
				}
				typeOID.add(pja);

				deviceModelVersionCounterOID.put(oid_type, typeOID);
				deviceModelOID.put(snmpversion + "_" + counternum,
								deviceModelVersionCounterOID);
				deviceOIDMap.put(temp_model, deviceModelOID);
			}

			fields = cursor.getNext();
		}
	}

	/**
	 * 删除端口信息
	 * 
	 * @return 0:成功，-1:参数错误,-2:数据库操作失败，-3：通知后台失败
	 */
	public int deleteFluxportConfig()
	{
		int resultCode = 0;
		String device_id = request.getParameter("device_id");
		String[] port_infos = request.getParameterValues("port");
		if (device_id == null || "".equals(device_id) || null == port_infos
						|| 0 == port_infos.length)
		{
			logger.warn("deleteFluxportConfig param error!");
			return -1;
		}

		String getway = "";
		String port_info = "";
		ArrayList sqlList = new ArrayList();
		String logname = "fluxConfig" + new DateTimeUtil().getShortDate()
						+ ".sql";

		// 准备删除sql
		for (int i = 0; i < port_infos.length; i++)
		{
			if (port_infos[i].split("\\|\\|\\|").length != 2)
			{
				sqlList.clear();
				resultCode = -1;
				break;
			}
			getway = port_infos[i].split("\\|\\|\\|")[0];
			port_info = port_infos[i].split("\\|\\|\\|")[1];
			pSQL.setSQL(delete_flux_deviceportconfig_Sql);
			pSQL.setString(1, device_id);
			pSQL.setStringExt(2, getway, false);
			pSQL.setString(3, port_info);
			Log.writeLog("delete_flux_deviceportconfig_Sql:" + pSQL.getSQL(),
							logname);
			sqlList.add(pSQL.getSQL());
			pSQL.setSQL(delete_flux_interfacedeviceport_Sql);
			pSQL.setString(1, device_id);
			pSQL.setStringExt(2, getway, false);
			pSQL.setString(3, port_info);
			Log.writeLog(
							"delete_flux_interfacedeviceport_Sql:"
											+ pSQL.getSQL(), logname);
			sqlList.add(pSQL.getSQL());
		}

		if (sqlList.size() > 0)
		{
			int[] code = DataSetBean.doBatch(sqlList);
			if (null == code)
			{
				resultCode = -2;
			}

			// 通知后台
			if (0 == resultCode)
			{
				String[] device_ids = new String[1];
				device_ids[0] = device_id;
				if (!informMCFluxConfig(device_ids))
				{
					resultCode = -3;
				}
			}
		}

		// clear
		sqlList = null;

		return resultCode;
	}

	/**
	 * 通知MC配置信息改变
	 * 
	 * @param device_ids
	 * @return
	 */
	public boolean informMCFluxConfig(String[] device_ids)
	{
		boolean result = true;
		try
		{
			FluxManagerInterface.GetInstance().readDevices(device_ids);
		}
		catch(Exception e)
		{
			result = false;
			e.printStackTrace();
		}		

		return result;
	}

	/**
	 * 删除设备的流量配置信息
	 * 
	 * @return 0:成功，-1:参数错误,-2:数据库操作失败，-3：通知后台失败
	 */
	public int deleteDeviceFluxConfig()
	{
		int resultCode = 0;
		String device_id = request.getParameter("device_id");
		if (null == device_id || "".equals(device_id))
		{
			return -1;
		}

		String sql = "delete from flux_interfacedeviceport where device_id='"
						+ device_id + "'";
		ArrayList sqlList = new ArrayList();
		sqlList.add(sql);
		sql = "delete from flux_deviceportconfig where device_id='" + device_id
						+ "'";
		sqlList.add(sql);
		int[] code = DataSetBean.doBatch(sqlList);
		if (null == code)
		{
			resultCode = -2;
		}

		// 通知后台
		if (0 == resultCode)
		{
			String[] device_ids = new String[1];
			device_ids[0] = device_id;
			if (!informMCFluxConfig(device_ids))
			{
				resultCode = -3;
			}
		}

		// clear
		sqlList = null;

		return resultCode;
	}
	
	/**
	 * 更新江苏流量配置中的端口信息
	 * 
	 * @return 0:成功，-1:参数错误,-2:数据库操作失败，-3：通知后台失败
	 */
	public int updateJSDevicePortInfo()
	{
		int resultCode = 0;
		String device_id = request.getParameter("device_id");
		String port_infos = request.getParameter("port_info");
		if (device_id == null || "".equals(device_id) || port_infos == null
						|| "".equals(port_infos))
		{
			logger.warn("updateDevicePortInfo param error!");
			return -1;
		}

		String[] port_infoArray = port_infos.split("\\$\\$\\$");
		String getway = "";
		String port_info = "";
		ArrayList sqlList = new ArrayList();

		FluxConfigInfo fluxport = new FluxConfigInfo();

		/**
		 * 设置端口参数
		 */

		// 数据是否入库
		String intodb = request.getParameter("intodb");
		fluxport.setIntodb(Integer.parseInt(intodb));

		// 是否采集端口数据
		String gatherflag = request.getParameter("gatherflag");
		fluxport.setGatherflag(Integer.parseInt(gatherflag));	
		
		/**
		 * 带宽利用率阈值一设置
		 */

		//设置流入带宽利用率阈值一
		int ifinoct_maxtype =Integer.parseInt(request.getParameter("ifinoct_maxtype"));
		fluxport.setIfinoct_maxtype(ifinoct_maxtype);		
		if (ifinoct_maxtype>0)
		{
			fluxport.setIfinoctetsbps_max(Double.parseDouble(request
							.getParameter("ifinoctetsbps_max")));
		}
		else
		{
			fluxport.setIfinoctetsbps_max(-1.00);
		}

		//设置了流出带宽利用率阈值一
		int ifoutoct_maxtype=Integer.parseInt(request.getParameter("ifoutoct_maxtype"));
		fluxport.setIfoutoct_maxtype(ifoutoct_maxtype);		
		if (ifoutoct_maxtype>0)
		{
			fluxport.setIfoutoctetsbps_max(Double.parseDouble(request
							.getParameter("ifoutoctetsbps_max")));
		}
		else
		{
			fluxport.setIfoutoctetsbps_max(-1.00);
		}

		// 选中端口流入丢包率阈值checkbox
		if (null != request.getParameter("ifindiscardspps_max_checkbox"))
		{
			fluxport.setIfindiscardspps_max(Double.parseDouble(request
							.getParameter("ifindiscardspps_max")));
		}
		else
		{
			fluxport.setIfindiscardspps_max(-1.00);
		}

		// 选中端口流出丢包率阈值checkbox
		if (null != request.getParameter("ifoutdiscardspps_max_checkbox"))
		{
			fluxport.setIfoutdiscardspps_max(Double.parseDouble(request
							.getParameter("ifoutdiscardspps_max")));
		}
		else
		{
			fluxport.setIfoutdiscardspps_max(-1.00);
		}

		// 选中端口流入错包率阈值checkbox
		if (null != request.getParameter("ifinerrorspps_max_checkbox"))
		{
			fluxport.setIfinerrorspps_max(Double.parseDouble(request
							.getParameter("ifinerrorspps_max")));
		}
		else
		{
			fluxport.setIfinerrorspps_max(-1.00);
		}

		// 选中端口流入错包率阈值checkbox
		if (null != request.getParameter("ifouterrorspps_max_checkbox"))
		{
			fluxport.setIfouterrorspps_max(Double.parseDouble(request
							.getParameter("ifouterrorspps_max")));
		}
		else
		{
			fluxport.setIfouterrorspps_max(-1.00);
		}

		// 超出阈值的次数（发告警）
		String warningnum = request.getParameter("warningnum");
		fluxport.setWarningnum(Integer.parseInt(warningnum));

		// 发出阈值告警时的告警级别
		String warninglevel = request.getParameter("warninglevel");
		fluxport.setWarninglevel(Integer.parseInt(warninglevel));

		// 发出恢复告警级别
		String reinstatelevel = request.getParameter("reinstatelevel");
		fluxport.setReinstatelevel(Integer.parseInt(reinstatelevel));
		
		/**
		 * 带宽利用率阈值二设置
		 */
		//设置流入带宽利用率阈值二
		int ifinoct_mintype =Integer.parseInt(request.getParameter("ifinoct_mintype"));
		fluxport.setIfinoct_mintype(ifinoct_mintype);		
		if (ifinoct_mintype>0)
		{
			fluxport.setIfinoctetsbps_min(Double.parseDouble(request
							.getParameter("ifinoctetsbps_min")));
		}
		else
		{
			fluxport.setIfinoctetsbps_min(-1.00);
		}

		//设置了流出带宽利用率阈值二
		int ifoutoct_mintype=Integer.parseInt(request.getParameter("ifoutoct_mintype"));
		fluxport.setIfoutoct_mintype(ifoutoct_mintype);		
		if (ifoutoct_mintype>0)
		{
			fluxport.setIfoutoctetsbps_min(Double.parseDouble(request
							.getParameter("ifoutoctetsbps_min")));
		}
		else
		{
			fluxport.setIfoutoctetsbps_min(-1.00);
		}
		
		//超出阈值二的次数（发告警）
		String warningnum_min = request.getParameter("warningnum_min");
		fluxport.setWarningnum_min(Integer.parseInt(warningnum_min));
		
		//超出阈值二的故障告警级别
		String warninglevel_min = request.getParameter("warninglevel_min");
		fluxport.setWarninglevel_min(Integer.parseInt(warninglevel_min));
		
		//阈值二恢复告警级别
		String reinlevel_min = request.getParameter("reinlevel_min");
		fluxport.setReinlevel_min(Integer.parseInt(reinlevel_min));
		

		/**
		 * 动态阈值一
		 */
		int overmax = Integer.parseInt(request.getParameter("overmax"));
		fluxport.setOvermax(overmax);		
		if (overmax>0)
		{
			String overper = request.getParameter("overper");
			fluxport.setOverper(Integer.parseInt(overper));
			String overnum = request.getParameter("overnum");
			fluxport.setOvernum(Integer.parseInt(overnum));			
			String overlevel = request.getParameter("overlevel");
			fluxport.setOverlevel(Integer.parseInt(overlevel));
			String reinoverlevel = request.getParameter("reinoverlevel");
			fluxport.setReinoverlevel(Integer.parseInt(reinoverlevel));
			String com_day = request.getParameter("com_day");
			fluxport.setCom_day(Integer.parseInt(com_day));
		}
		
		/**
		 * 动态阈值二
		 */
		int overmin=Integer.parseInt(request.getParameter("overmin"));
		fluxport.setOvermin(overmin);
		if(overmin>0)
		{
			fluxport.setOverper_min(Integer.parseInt(request.getParameter("overper_min")));
			fluxport.setOvernum_min(Integer.parseInt(request.getParameter("overnum_min")));
			fluxport.setOverlevel_min(Integer.parseInt(request.getParameter("overlevel_min")));
			fluxport.setReinoverlevel_min(Integer.parseInt(request.getParameter("reinoverlevel_min")));
			fluxport.setCom_day(Integer.parseInt(request.getParameter("com_day")));
		}

		// 启用流入流量突变告警配置
		if (null != request.getParameter("intbflag"))
		{
			String intbflag = request.getParameter("intbflag");
			fluxport.setIntbflag(Integer.parseInt(intbflag));
			String ifinoctets = request.getParameter("ifinoctets");
			fluxport.setIfinoctets(Double.parseDouble(ifinoctets));
			String inoperation = request.getParameter("inoperation");
			fluxport.setInoperation(Integer.parseInt(inoperation));
			String inwarninglevel = request.getParameter("inwarninglevel");
			fluxport.setInwarninglevel(Integer.parseInt(inwarninglevel));
			String inreinstatelevel = request.getParameter("inreinstatelevel");
			fluxport.setInreinstatelevel(Integer.parseInt(inreinstatelevel));
		}

		// 启用流出流量突变告警配置
		if (null != request.getParameter("outtbflag"))
		{
			String outtbflag = request.getParameter("outtbflag");
			fluxport.setOuttbflag(Integer.parseInt(outtbflag));
			String ifoutoctets = request.getParameter("ifoutoctets");
			fluxport.setIfoutoctets(Double.parseDouble(ifoutoctets));
			String outoperation = request.getParameter("outoperation");
			fluxport.setOutoperation(Integer.parseInt(outoperation));
			String outwarninglevel = request.getParameter("outwarninglevel");
			fluxport.setOutwarninglevel(Integer.parseInt(outwarninglevel));
			String outreinstatelevel = request
							.getParameter("outreinstatelevel");
			fluxport.setOutreinstatelevel(Integer.parseInt(outreinstatelevel));
		}

		String logname = "fluxConfig" + new DateTimeUtil().getShortDate()
						+ ".sql";

		// 准备sql
		for (int i = 0; i < port_infoArray.length; i++)
		{
			if (port_infoArray[i].split("\\|\\|\\|").length != 2)
			{
				sqlList.clear();
				resultCode = -1;
				break;
			}
			getway = port_infoArray[i].split("\\|\\|\\|")[0];
			port_info = port_infoArray[i].split("\\|\\|\\|")[1].replaceAll("#\\$!","''").replaceAll("\\$#!","\"");

			fluxport.setDevice_id(device_id);
			fluxport.setGetway(Integer.parseInt(getway));
			fluxport.setPort_info(port_info);

			pSQL.setSQL(update_flux_deviceportconfig_Sql);
			pSQL.setInt(1, fluxport.getIntodb());
			pSQL.setInt(2, fluxport.getGatherflag());
			pSQL.setString(3, fluxport.getDevice_id());
			pSQL.setInt(4, fluxport.getGetway());
			pSQL.setString(5, fluxport.getPort_info());
			Log.writeLog("update_flux_deviceportconfig_Sql:" + pSQL.getSQL(),
							logname);
			sqlList.add(pSQL.getSQL());

			pSQL.setSQL(update_flux_interfacedeviceport_js_Sql);
			/**
			 * 带宽利用率阈值一
			 */
			pSQL.setInt(1,fluxport.getIfinoct_maxtype());
			pSQL.setDouble(2, fluxport.getIfinoctetsbps_max());
			pSQL.setInt(3,fluxport.getIfoutoct_maxtype());
			pSQL.setDouble(4, fluxport.getIfoutoctetsbps_max());
			pSQL.setDouble(5, fluxport.getIfindiscardspps_max());
			pSQL.setDouble(6, fluxport.getIfoutdiscardspps_max());
			pSQL.setDouble(7, fluxport.getIfinerrorspps_max());
			pSQL.setDouble(8, fluxport.getIfouterrorspps_max());
			pSQL.setInt(9, fluxport.getWarningnum());
			pSQL.setInt(10, fluxport.getWarninglevel());
			pSQL.setInt(11, fluxport.getReinstatelevel());
			/**
			 * 带宽利用率阈值二
			 */
			pSQL.setInt(12,fluxport.getIfinoct_mintype());
			pSQL.setDouble(13,fluxport.getIfinoctetsbps_min());
			pSQL.setInt(14,fluxport.getIfoutoct_mintype());
			pSQL.setDouble(15,fluxport.getIfoutoctetsbps_min());
			pSQL.setInt(16,fluxport.getWarningnum_min());
			pSQL.setInt(17,fluxport.getWarninglevel_min());
			pSQL.setInt(18,fluxport.getReinlevel_min());
			/**
			 * 动态阈值一
			 */
			pSQL.setInt(19,fluxport.getOvermax());			
			pSQL.setInt(20, fluxport.getOverper());
			pSQL.setInt(21, fluxport.getOvernum());
			pSQL.setInt(22, fluxport.getOverlevel());
			pSQL.setInt(23, fluxport.getReinoverlevel());
			pSQL.setInt(24, fluxport.getCom_day());
			/**
			 * 动态阈值二
			 */
			pSQL.setInt(25,fluxport.getOvermin());
			pSQL.setInt(26,fluxport.getOverper_min());
			pSQL.setInt(27,fluxport.getOvernum_min());
			pSQL.setInt(28,fluxport.getOverlevel_min());
			pSQL.setInt(29,fluxport.getReinoverlevel_min());
			
			/**
			 * 突变阈值
			 */
			pSQL.setInt(30, fluxport.getIntbflag());
			pSQL.setDouble(31, fluxport.getIfinoctets());
			pSQL.setInt(32, fluxport.getInwarninglevel());
			pSQL.setInt(33, fluxport.getInoperation());
			pSQL.setInt(34, fluxport.getInreinstatelevel());
			pSQL.setInt(35, fluxport.getOuttbflag());
			pSQL.setDouble(36, fluxport.getIfoutoctets());
			pSQL.setInt(37, fluxport.getOutwarninglevel());
			pSQL.setInt(38, fluxport.getOutoperation());
			pSQL.setInt(39, fluxport.getInreinstatelevel());
			pSQL.setString(40, fluxport.getDevice_id());
			pSQL.setInt(41, fluxport.getGetway());
			pSQL.setString(42, fluxport.getPort_info());
			Log.writeLog(
							"update_flux_interfacedeviceport_js_Sql:"
											+ pSQL.getSQL(), logname);
			sqlList.add(pSQL.getSQL());
		}

		if (sqlList.size() > 0)
		{
			int[] code = DataSetBean.doBatch(sqlList);

			if (code == null)
			{
				resultCode = -2;
			}

			// 通知后台
			if (0 == resultCode)
			{
				String[] device_ids = new String[1];
				device_ids[0] = device_id;
				if (!informMCFluxConfig(device_ids))
				{
					resultCode = -3;
				}
			}
		}

		// clear
		sqlList = null;

		return resultCode;
	}

	/**
	 * 更新流量配置中的端口信息
	 * 
	 * @return 0:成功，-1:参数错误,-2:数据库操作失败，-3：通知后台失败
	 */
	public int updateDevicePortInfo()
	{
		int resultCode = 0;
		String device_id = request.getParameter("device_id");
		String port_infos = request.getParameter("port_info");
		if (device_id == null || "".equals(device_id) || port_infos == null
						|| "".equals(port_infos))
		{
			logger.warn("updateDevicePortInfo param error!");
			return -1;
		}

		String[] port_infoArray = port_infos.split("\\$\\$\\$");
		String getway = "";
		String port_info = "";
		ArrayList sqlList = new ArrayList();

		FluxConfigInfo fluxport = new FluxConfigInfo();

		/**
		 * 设置端口参数
		 */

		// 数据是否入库
		String intodb = request.getParameter("intodb");
		fluxport.setIntodb(Integer.parseInt(intodb));

		// 是否采集端口数据
		String gatherflag = request.getParameter("gatherflag");
		fluxport.setGatherflag(Integer.parseInt(gatherflag));

		// 选中端口流入带宽利用率阈值checkbox
		if (null != request.getParameter("ifinoctetsbps_max_checkbox"))
		{
			fluxport.setIfinoctetsbps_max(Double.parseDouble(request
							.getParameter("ifinoctetsbps_max")));
		}
		else
		{
			fluxport.setIfinoctetsbps_max(-1.00);
		}

		// 选中端口流出带宽利用率阈值checkbox
		if (null != request.getParameter("ifoutoctetsbps_max_checkbox"))
		{
			fluxport.setIfoutoctetsbps_max(Double.parseDouble(request
							.getParameter("ifoutoctetsbps_max")));
		}
		else
		{
			fluxport.setIfoutoctetsbps_max(-1.00);
		}

		// 选中端口流入丢包率阈值checkbox
		if (null != request.getParameter("ifindiscardspps_max_checkbox"))
		{
			fluxport.setIfindiscardspps_max(Double.parseDouble(request
							.getParameter("ifindiscardspps_max")));
		}
		else
		{
			fluxport.setIfindiscardspps_max(-1.00);
		}

		// 选中端口流出丢包率阈值checkbox
		if (null != request.getParameter("ifoutdiscardspps_max_checkbox"))
		{
			fluxport.setIfoutdiscardspps_max(Double.parseDouble(request
							.getParameter("ifoutdiscardspps_max")));
		}
		else
		{
			fluxport.setIfoutdiscardspps_max(-1.00);
		}

		// 选中端口流入错包率阈值checkbox
		if (null != request.getParameter("ifinerrorspps_max_checkbox"))
		{
			fluxport.setIfinerrorspps_max(Double.parseDouble(request
							.getParameter("ifinerrorspps_max")));
		}
		else
		{
			fluxport.setIfinerrorspps_max(-1.00);
		}

		// 选中端口流入错包率阈值checkbox
		if (null != request.getParameter("ifouterrorspps_max_checkbox"))
		{
			fluxport.setIfouterrorspps_max(Double.parseDouble(request
							.getParameter("ifouterrorspps_max")));
		}
		else
		{
			fluxport.setIfouterrorspps_max(-1.00);
		}

		// 超出阈值的次数（发告警）
		String warningnum = request.getParameter("warningnum");
		fluxport.setWarningnum(Integer.parseInt(warningnum));

		// 发出阈值告警时的告警级别
		String warninglevel = request.getParameter("warninglevel");
		fluxport.setWarninglevel(Integer.parseInt(warninglevel));

		// 发出恢复告警级别
		String reinstatelevel = request.getParameter("reinstatelevel");
		fluxport.setReinstatelevel(Integer.parseInt(reinstatelevel));

		// 启用动态阈值告警
		if (null != request.getParameter("isover"))
		{
			String overper = request.getParameter("overper");
			fluxport.setOverper(Integer.parseInt(overper));
			String overnum = request.getParameter("overnum");
			fluxport.setOvernum(Integer.parseInt(overnum));
			String com_day = request.getParameter("com_day");
			fluxport.setCom_day(Integer.parseInt(com_day));
			String overlevel = request.getParameter("overlevel");
			fluxport.setOverlevel(Integer.parseInt(overlevel));
			String reinoverlevel = request.getParameter("reinoverlevel");
			fluxport.setReinoverlevel(Integer.parseInt(reinoverlevel));
		}

		// 启用流入流量突变告警配置
		if (null != request.getParameter("intbflag"))
		{
			String intbflag = request.getParameter("intbflag");
			fluxport.setIntbflag(Integer.parseInt(intbflag));
			String ifinoctets = request.getParameter("ifinoctets");
			fluxport.setIfinoctets(Double.parseDouble(ifinoctets));
			String inoperation = request.getParameter("inoperation");
			fluxport.setInoperation(Integer.parseInt(inoperation));
			String inwarninglevel = request.getParameter("inwarninglevel");
			fluxport.setInwarninglevel(Integer.parseInt(inwarninglevel));
			String inreinstatelevel = request.getParameter("inreinstatelevel");
			fluxport.setInreinstatelevel(Integer.parseInt(inreinstatelevel));
		}

		// 启用流出流量突变告警配置
		if (null != request.getParameter("outtbflag"))
		{
			String outtbflag = request.getParameter("outtbflag");
			fluxport.setOuttbflag(Integer.parseInt(outtbflag));
			String ifoutoctets = request.getParameter("ifoutoctets");
			fluxport.setIfoutoctets(Double.parseDouble(ifoutoctets));
			String outoperation = request.getParameter("outoperation");
			fluxport.setOutoperation(Integer.parseInt(outoperation));
			String outwarninglevel = request.getParameter("outwarninglevel");
			fluxport.setOutwarninglevel(Integer.parseInt(outwarninglevel));
			String outreinstatelevel = request
							.getParameter("outreinstatelevel");
			fluxport.setOutreinstatelevel(Integer.parseInt(outreinstatelevel));
		}

		String logname = "fluxConfig" + new DateTimeUtil().getShortDate()
						+ ".sql";

		// 准备sql
		for (int i = 0; i < port_infoArray.length; i++)
		{
			if (port_infoArray[i].split("\\|\\|\\|").length != 2)
			{
				sqlList.clear();
				resultCode = -1;
				break;
			}
			getway = port_infoArray[i].split("\\|\\|\\|")[0];
			port_info = port_infoArray[i].split("\\|\\|\\|")[1].replaceAll("#\\$!","''").replaceAll("\\$#!","\"");
			fluxport.setDevice_id(device_id);
			fluxport.setGetway(Integer.parseInt(getway));
			fluxport.setPort_info(port_info);

			pSQL.setSQL(update_flux_deviceportconfig_Sql);
			pSQL.setInt(1, fluxport.getIntodb());
			pSQL.setInt(2, fluxport.getGatherflag());
			pSQL.setString(3, fluxport.getDevice_id());
			pSQL.setInt(4, fluxport.getGetway());
			pSQL.setString(5, fluxport.getPort_info());
			Log.writeLog("update_flux_deviceportconfig_Sql:" + pSQL.getSQL(),
							logname);
			sqlList.add(pSQL.getSQL());

			pSQL.setSQL(update_flux_interfacedeviceport_Sql);
			pSQL.setDouble(1, fluxport.getIfinoctetsbps_max());
			pSQL.setDouble(2, fluxport.getIfoutoctetsbps_max());
			pSQL.setDouble(3, fluxport.getIfindiscardspps_max());
			pSQL.setDouble(4, fluxport.getIfoutdiscardspps_max());
			pSQL.setDouble(5, fluxport.getIfinerrorspps_max());
			pSQL.setDouble(6, fluxport.getIfouterrorspps_max());
			pSQL.setInt(7, fluxport.getWarningnum());
			pSQL.setInt(8, fluxport.getWarninglevel());
			pSQL.setInt(9, fluxport.getReinstatelevel());
			pSQL.setInt(10, fluxport.getOverper());
			pSQL.setInt(11, fluxport.getOvernum());
			pSQL.setInt(12, fluxport.getOverlevel());
			pSQL.setInt(13, fluxport.getReinoverlevel());
			pSQL.setInt(14, fluxport.getCom_day());
			pSQL.setInt(15, fluxport.getIntbflag());
			pSQL.setDouble(16, fluxport.getIfinoctets());
			pSQL.setInt(17, fluxport.getInwarninglevel());
			pSQL.setInt(18, fluxport.getInoperation());
			pSQL.setInt(19, fluxport.getInreinstatelevel());
			pSQL.setInt(20, fluxport.getOuttbflag());
			pSQL.setDouble(21, fluxport.getIfoutoctets());
			pSQL.setInt(22, fluxport.getOutwarninglevel());
			pSQL.setInt(23, fluxport.getOutoperation());
			pSQL.setInt(24, fluxport.getInreinstatelevel());
			pSQL.setString(25, fluxport.getDevice_id());
			pSQL.setInt(26, fluxport.getGetway());
			pSQL.setString(27, fluxport.getPort_info());
			Log.writeLog(
							"update_flux_interfacedeviceport_Sql:"
											+ pSQL.getSQL(), logname);
			sqlList.add(pSQL.getSQL());
		}

		if (sqlList.size() > 0)
		{
			int[] code = DataSetBean.doBatch(sqlList);

			if (code == null)
			{
				resultCode = -2;
			}

			// 通知后台
			if (0 == resultCode)
			{
				String[] device_ids = new String[1];
				device_ids[0] = device_id;
				if (!informMCFluxConfig(device_ids))
				{
					resultCode = -3;
				}
			}
		}

		// clear
		sqlList = null;

		return resultCode;
	}

	/**
	 * 获取设备某个端口的详细信息
	 * 
	 * @return
	 */
	public HashMap getDevicePortInfo()
	{
		HashMap portInfo = new HashMap();
		String device_id = request.getParameter("device_id");
		String port = request.getParameter("port");
		if (null == device_id || "".equals(device_id) || null == port
						|| "".equals(port)
						|| 2 != port.split("\\|\\|\\|").length)
		{
			return portInfo;
		}
		pSQL.setSQL(devicePortInfoSql);
		pSQL.setString(1, device_id);
		pSQL.setStringExt(2, port.split("\\|\\|\\|")[0], false);
		pSQL.setStringExt(3, port.split("\\|\\|\\|")[1].replaceAll("#\\$!","''").replaceAll("\\$#!","\""), true);
		logger.debug("getDevicePortInfo_SQL:" + pSQL.getSQL());
		portInfo = DataSetBean.getRecord(pSQL.getSQL());
		if (portInfo == null)
		{
			portInfo = new HashMap();
		}

		return portInfo;
	}

	/**
	 * 获取设备上已经配置的所有端口信息
	 * 
	 * @param device_id
	 * @return
	 */
	public Cursor getDeviceConfigPort(String device_id)
	{
		Cursor cursor = new Cursor();
		pSQL.setSQL(deviceConfigPortSql);
		pSQL.setString(1, device_id);
		logger.debug("getDeviceConfigPort_SQL:" + pSQL.getSQL());
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		return cursor;
	}

	/**
	 * 根据设备ID获取设备信息
	 * 
	 * @param device_id
	 * @return
	 */
	public HashMap getDeviceInfo(String device_id)
	{
		pSQL.setSQL(deviceInfoSql);
		pSQL.setString(1, device_id);
		logger.debug("getDeviceInfo_SQL:" + pSQL.getSQL());
		HashMap deviceMap = DataSetBean.getRecord(pSQL.getSQL());
		if (deviceMap == null)
		{
			deviceMap = new HashMap();
		}
		return deviceMap;
	}

	// 获取采集版本
	public ArrayList getSnmpVersion()
	{
		String serial = request.getParameter("serial");
		String snmpversion = request.getParameter("snmp");
		pSQL.setSQL(snmpSQL);
		pSQL.setString(1, serial);
		pSQL.setStringExt(2, snmpversion, false);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		if (cursor.getRecordSize() == 0)
		{
			pSQL.setString(1, "0");
			pSQL.setStringExt(2, snmpversion, false);
			cursor = DataSetBean.getCursor(pSQL.getSQL());
		}
		Map map = cursor.getNext();
		while (map != null)
		{
			snmpList.add((String) map.get("snmpversion"));
			map = cursor.getNext();
		}

		return snmpList;
	}

	public String getConfigedSnmpVersion()
	{
		String snmpVersion = null;

		String device_id = request.getParameter("device_id");
		pSQL.setSQL(snmpSQL);
		pSQL.setString(1, device_id);
		Map map = DataSetBean.getRecord(pSQL.getSQL());
		if (null != map)
		{
			snmpVersion = (String) map.get("snmpversion");
		}

		return snmpVersion;
	}

	// 初始化要采集的oid
	public void setSerial()
	{
		String serial = request.getParameter("serial");
		String snmp = request.getParameter("snmp");
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			mysql = "select model, counternum, oid_name, oid_desc, oid_value, oid_type, oid_order " +
					"from flux_oid_map where model=? and snmpversion=?";
		}
		pSQL.setSQL(mysql);
		pSQL.setString(1, serial);
		pSQL.setStringExt(2, snmp, false);
		// logger.debug("wp_setSerial:"+pSQL.getSQL());
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		// 如果有特殊处理的设备类型
		if (cursor.getRecordSize() == 0)
		{
			pSQL.setString(1, "0");
			pSQL.setStringExt(2, snmp, false);
			// logger.debug(pSQL.getSQL());
			cursor = DataSetBean.getCursor(pSQL.getSQL());
			// logger.debug("wp_setSerial_getRecordSize:"+pSQL.getSQL());
		}
		ArrayList list1 = new ArrayList();
		ArrayList list2 = new ArrayList();
		ArrayList list3 = new ArrayList();
		Map map = cursor.getNext();

		String oid_type = "";
		PortJudgeAttr pja = null;
		while (map != null)
		{
			if (model == null)
			{
				model = (String) map.get("model");
			}

			if (counternum == null)
			{
				counternum = (String) map.get("counternum");
			}

			pja = new PortJudgeAttr();
			pja.setName((String) map.get("oid_name"));
			pja.setDesc((String) map.get("oid_desc"));
			pja.setValue((String) map.get("oid_value"));
			oid_type = (String) map.get("oid_type");
			pja.setOrder((String) map.get("oid_order"));
			if (oid_type.compareTo("1") == 0)
			{
				list1.add(pja);
			}
			else if (oid_type.compareTo("2") == 0)
			{
				list2.add(pja);
			}
			else if (oid_type.compareTo("3") == 0)
			{
				list3.add(pja);
			}
			map = cursor.getNext();
		}
		typeOid.put("1", list1);
		typeOid.put("2", list2);
		typeOid.put("3", list3);
		// logger.debug("wp_setSerial_end");
	}

	public ArrayList getoidList(String type)
	{
		return (ArrayList) typeOid.get(type);
	}

	/**
	 * 判断该设备是否已经配置
	 * 
	 * @return
	 */
	public boolean isConfig()
	{
		boolean flag = false;
		String device_id = request.getParameter("device_id");
		pSQL.setSQL(sql);
		pSQL.setString(1, device_id);
		// logger.debug(pSQL.getSQL());
		Map map = DataSetBean.getRecord(pSQL.getSQL());
		if (map != null && Integer.parseInt((String) map.get("num")) > 0)
		{
			flag = true;
		}
		return flag;

	}

	// 获取端口的基本信息表
	public ArrayList getPortBaseInfo()
	{
		ArrayList retList = new ArrayList();
		String device_id = request.getParameter("device_id");
		String serial = request.getParameter("serial");
		pSQL.setSQL(myport);
		pSQL.setString(1, device_id);
		// logger.debug("wp_getPortBaseInfo:"+pSQL.getSQL());
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		// 如果里面有记录的话则不从后台取
		if (cursor.getRecordSize() > 0)
		{
			Map map = cursor.getNext();
			FluxPortInfo fpi = null;
			while (map != null)
			{
				fpi = new FluxPortInfo();
				fpi.setIfdescr((String) map.get("ifdescr"));
				fpi.setIfindex((String) map.get("ifindex"));
				fpi.setIfname((String) map.get("ifname"));
				fpi.setIfnamedefined((String) map.get("ifnamedefined"));
				fpi.setIfportip((String) map.get("ifportip"));
				retList.add(fpi);
				map = cursor.getNext();
			}
		}
		else
		{
			// 从后台获取相关数据
			ArrayList list = getoidList("2");
			HttpSession session = request.getSession();
			UserRes userRes = (UserRes) session.getAttribute("curUser");
			ReadFluxConfigPortInfo rcp = new ReadFluxConfigPortInfo();
			rcp.setOidList(list);
			rcp.setSerial(serial);
			rcp.setDevice_ID(device_id);
			String srcType = request.getParameter("type");
			if (null != srcType && !"".equals(srcType))
			{
				int type = Integer.parseInt(srcType);
				if (2 == type)
				{
					rcp.setType(type);
				}
				// logger.debug("wp_getPortBaseInfo_type:"+type);
			}

			retList = rcp.getDeviceInfo();
		}
		return retList;
	}

	public ArrayList getPortBaseInfo(ArrayList oidList)
	{

		ArrayList retList = new ArrayList();
		String device_id = request.getParameter("device_id");
		String serial = request.getParameter("serial");
//		pSQL.setSQL(myport);
//		pSQL.setString(1, device_id);
//		// logger.debug("wp_getPortBaseInfo:"+pSQL.getSQL());
//		cursor = DataSetBean.getCursor(pSQL.getSQL());
//		// 如果里面有记录的话则不从后台取
//		if (cursor.getRecordSize() > 0)
//		{
//			Map map = cursor.getNext();
//			FluxPortInfo fpi = null;
//			while (map != null)
//			{
//				fpi = new FluxPortInfo();
//				fpi.setIfdescr((String) map.get("ifdescr"));
//				fpi.setIfindex((String) map.get("ifindex"));
//				fpi.setIfname((String) map.get("ifname"));
//				fpi.setIfnamedefined((String) map.get("ifnamedefined"));
//				fpi.setIfportip((String) map.get("ifportip"));
//				retList.add(fpi);
//				map = cursor.getNext();
//			}
//		}
//		else
//		{
//			// 从后台获取相关数据
//			HttpSession session = request.getSession();
//			UserRes userRes = (UserRes) session.getAttribute("curUser");
//			ReadFluxConfigPortInfo rcp = new ReadFluxConfigPortInfo();
//			rcp.setOidList(oidList);
//			rcp.setSerial(serial);
//			rcp.setDevice_ID(device_id);
////			rcp.setAccountInfo(userRes.getUser().getAccount(), userRes
////							.getUser().getPasswd());
////			String srcType = request.getParameter("type");
////			if (null != srcType && !"".equals(srcType))
////			{
////				int type = Integer.parseInt(srcType);
////				if (2 == type)
////				{
////					rcp.setType(type);
////				}
////				// logger.debug("wp_getPortBaseInfo_type:"+type);
////			}
//
//			retList = rcp.getDeviceInfo();
//		}
		
        //从后台获取相关数据
		HttpSession session = request.getSession();
		UserRes userRes = (UserRes) session.getAttribute("curUser");
		ReadFluxConfigPortInfo rcp = new ReadFluxConfigPortInfo();
		rcp.setOidList(oidList);
		rcp.setSerial(serial);
		rcp.setDevice_ID(device_id);
		retList = rcp.getDeviceInfo();
		return retList;

	}

	/**
	 * 返回这个端口对应的采集方式
	 * 
	 * @param fpi
	 * @param list
	 * @return getWay(5;端口ip,4:端口别名,3:端口名字,2:端口描述)
	 */
	public int getGetway(FluxPortInfo fpi, ArrayList list)
	{
		int getway = 1;
		int ip_num = 0;
		int name_num = 0;
		int desc_num = 0;
		int namedefined_num = 0;
		FluxPortInfo temp = null;
		for (int i = 0; i < list.size(); i++)
		{
			temp = (FluxPortInfo) list.get(i);
			// 端口ip
			if (fpi.getIfportip() != null
							&& temp.getIfportip() != null
							&& fpi.getIfportip().compareTo(temp.getIfportip()) == 0
							&& fpi.getIfportip().compareTo("") != 0
							&& fpi.getIfportip().compareTo("null") != 0
							&& !"NODATA".equalsIgnoreCase(fpi.getIfportip())
							&& IsIp(fpi.getIfportip())
							&& fpi.getIfportip().compareTo("0.0.0.0") != 0)
			{
				ip_num++;				
			}

			// 端口名字
			if ((fpi.getIfname() != null && temp.getIfname() != null
							&& fpi.getIfname().compareTo(temp.getIfname()) == 0
							&& fpi.getIfname().compareTo("") != 0
							&& fpi.getIfname().compareTo("null") != 0
							&& !"NODATA".equalsIgnoreCase(fpi.getIfname())
							))
			{
				name_num++;
			}

			// 端口描述
			if ((fpi.getIfdescr() != null
							&& temp.getIfdescr() != null
							&& fpi.getIfdescr().compareTo(temp.getIfdescr()) == 0
							&& fpi.getIfdescr().compareTo("") != 0 
							&& fpi.getIfdescr().compareTo("null") != 0
							&& !"NODATA".equalsIgnoreCase(fpi.getIfdescr())
							))
			{
				desc_num++;
			}

			// 端口别名
			if ((fpi.getIfnamedefined() != null
							&& temp.getIfnamedefined() != null
							&& fpi.getIfnamedefined().compareTo(
											temp.getIfnamedefined()) == 0
							&& fpi.getIfnamedefined().compareTo("") != 0 
							&& fpi.getIfnamedefined().compareTo("null") != 0
							&& !"NODATA".equalsIgnoreCase(fpi.getIfnamedefined())
							))
			{
				namedefined_num++;
			}

		}

		if (ip_num == 1)
		{
			getway = 5;
		}
		else if (desc_num == 1)
		{
			getway = 2;
		}
		else if (name_num == 1)
		{
			getway = 3;
		}
		else if (namedefined_num == 1)
		{
			getway = 4;
		}

		return getway;

	}

	/**
	 * 检验ip是否合法
	 * 
	 * @param text
	 * @return
	 */
	private static boolean IsIp(String text)
	{
		IsNull(text);
		String ip = text.trim();
		if (ip.length() > 15)
		{
			return false;
		}
		if (ip.length() < 7)
		{
			return false;
		}
		int length = ip.length();
		char ch[] = new char[length];
		for (int i = 0; i < length; i++)
		{
			ch[i] = ip.charAt(i);
		}
		int pointNum = 0;
		for (int i = 0; i < length; i++)
		{
			if (ch[i] == '.')
			{
				pointNum++;
			}
		}
		if (pointNum != 3)
		{
			return false;
		}
		int x1 = ip.indexOf('.', 0);
		String str = ip.substring(0, x1);
		if (IsNumer(str) == false)
		{
			return false;
		}
		int n = Integer.parseInt(str);
		if (n < 1 || n > 255)
		{
			return false;
		}
		int x2 = ip.indexOf('.', x1 + 1);
		str = ip.substring(x1 + 1, x2);
		if (IsNumer(str) == false)
		{
			return false;
		}
		n = Integer.parseInt(str);
		if (n < 0 || n > 255)
		{
			return false;
		}
		int x3 = ip.indexOf('.', x2 + 1);
		str = ip.substring(x2 + 1, x3);
		if (IsNumer(str) == false)
		{
			return false;
		}
		n = Integer.parseInt(str);
		if (n < 0 || n > 255)
		{
			return false;
		}
		str = ip.substring(x3 + 1);
		if (IsNumer(str) == false)
		{
			return false;
		}
		n = Integer.parseInt(str);
		if (n < 1 || n > 255)
		{
			return false;
		}
		return true;
	}

	/**
	 * is int.
	 * 
	 * @param text
	 *            String
	 * @return boolean
	 */
	private static boolean IsNumer(String text)
	{

		IsNull(text);
		for (int i = 0; i < text.length(); i++)
		{
			if (text.charAt(i) < '0' || text.charAt(i) > '9')
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * is null
	 * 
	 * @param str
	 *            String
	 * @return boolean
	 */
	private static boolean IsNull(String str)
	{
		if (str == null || str.length() <= 0)
		{
			return false;
		}
		return true;
	}
	
	
	/**
	 * 根据device_id获取设备snmpversion
	 * @param device_id
	 * @return
	 */
	public static String getSnmpVersion(String device_id)
	{
		String snmp_version="";
		String sql="select snmp_version from sgw_security where device_id='"+device_id+"'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		HashMap record =DataSetBean.getRecord(sql);
		if(null!=record)
		{
			snmp_version=(String)record.get("snmp_version");
			if("v1".equalsIgnoreCase(snmp_version))
			{
				snmp_version="1";
			}
			else if("v2".equalsIgnoreCase(snmp_version))
			{
				snmp_version="2";
			}
			else
			{
				snmp_version="3";
			}
		}
		
		return snmp_version;		
	}
	
	/**
	 * 获取设备信息
	 * @param device_id
	 * @return
	 */
	private HashMap getDeviceInfoMap(String device_id)
	{
//		String sql="select a.*,b.device_model from tab_gw_device a,tab_devicetype_info b  where a.devicetype_id=b.devicetype_id and a.device_id='"+device_id+"'";
		String sql="select a.*,b.device_model from tab_gw_device a,gw_device_model b  where a.device_model_id=b.device_model_id and a.device_id='"+device_id+"'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql="select b.device_model from tab_gw_device a,gw_device_model b  where a.device_model_id=b.device_model_id and a.device_id='"+device_id+"'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return DataSetBean.getRecord(sql);
	}

	/**
	 * -1:设备没有配置读口令，0：成功,-2:设备不存在
	 * @return
	 */
	public int  saveDeviceOperator()
	{
		logger.debug("wp_SaveDeviceOperator_begin:"+new DateTimeUtil().getLongDate());
		String serial = request.getParameter("serial");
		String istotal = request.getParameter("istotal");
		String polltime = request.getParameter("polltime");
		String device_id = request.getParameter("device_id");
		String isdb = request.getParameter("isdb");
		String gatherflag =getSnmpVersion(device_id);
		if("".equals(gatherflag))
		{
			return -1;
		}
		NoPorts = new ArrayList();
		this.filterMap = InitFilterMap();		
		
		//获取所有的端口信息
		ArrayList listAll = new ArrayList();
		ArrayList oidList = null;
		//采集端口详细信息，使用v1版本的32位计数器来采（据说采端口描述等信息，随便用什么版本采都一样，要验证）
		oidList = getOIDList(serial, gatherflag, "32", "2");
		logger.debug("gatherflag:"+gatherflag+" oidList_size:"+oidList.size());		
		listAll = getPortBaseInfo(oidList);			
		logger.debug("end getPortBaseInfo time:"+new DateTimeUtil().getLongDate());
		logger.debug("采集到的所有端口数：" + listAll.size());
		
		// filter port.
		//DeviceAct deviceAct = new DeviceAct();
		//Map deviceInfo = deviceAct.getDeviceInfoMap(device_id);
		HashMap deviceInfo=getDeviceInfoMap(device_id);
		if(null==deviceInfo)
		{
			return -2;
		}
		String device_model = (String) deviceInfo.get("device_model");
		ArrayList list = filterPort(filterMap, device_model, listAll);
		logger.debug("end filterPort time:"+new DateTimeUtil().getLongDate());
		logger.debug("过滤后的端口数是:" + list.size());

		// 不是总体配置
		if (istotal.compareTo("0") == 0)
		{
			logger.debug("wp_不是总体配置:begin");
			// 首先要获取用户选择的端口号
			String[] ports = request.getParameterValues("ifindex");
			FluxConfigInfo fci = null;
			FluxPortInfo fpi = null;
			String[] attrs = null;
			String[] values = null;
			String ifindex = "";
			int num = 0;
			
			//遍历过滤后的端口中挑出用户选择的端口
			for (int i = 0; i < ports.length; i++)
			{
				ifindex = ports[i].split("\\|\\|\\|")[0];
				
				for(int j=0;j<list.size();j++)
				{
					fpi =(FluxPortInfo)list.get(j);
					if(ifindex.equals(fpi.getIfindex()))
					{
						fci = new FluxConfigInfo();
						if (null != isdb)
						{
							fci.setIntodb(Integer.parseInt(isdb));
						}
						fci.setPolltime(Integer.parseInt(polltime));
						fci.setDevice_id(device_id);
						
						//端口信息
						fci.setIfindex(fpi.getIfindex());
						if(null!=fpi.getIfportip()&&!"NODATA".equalsIgnoreCase(fpi.getIfportip()))
						{
							fci.setIfportip(fpi.getIfportip());
						}
						if(null!=fpi.getIfdescr()&&!"NODATA".equalsIgnoreCase(fpi.getIfdescr()))
						{
							fci.setIfdescr(fpi.getIfdescr());
						}
						if(null!=fpi.getIfname()&&!"NODATA".equalsIgnoreCase(fpi.getIfname()))
						{
							fci.setIfname(fpi.getIfname());
						}
						if(null!=fpi.getIfnamedefined()&&!"NODATA".equalsIgnoreCase(fpi.getIfnamedefined()))
						{
							fci.setIfnamedefined(fpi.getIfnamedefined());
						}						
						
						fci.setModel(model);
						fci.setCounternum(counternum);
						fci.setGetway(getGetway(fpi, list));
						
						//采集属性值
						attrs = request.getParameterValues("checkbox" + ifindex);
						// perfList = new ArrayList();
						String getarg = "0000000000000";
						for (int k = 0; k < attrs.length; k++)
						{
							values = attrs[k].split("\\|\\|\\|");
							num = Integer.parseInt(values[1]);
							getarg = getarg.substring(0, num) + "1"
											+ getarg.substring(num + 1);							
						}
						fci.setGetarg(getarg);
						fci.setSerial(serial);						
						
						synchronized (NoPorts)
						{
							NoPorts.add(fci);
						}
						
						break;
					}					
				}				
			}
			
			logger.debug("wp_不是总体配置:end   用户选择的端口数为："+NoPorts.size());
		}
		// 是总体配置
		else
		{
			FluxConfigInfo fci = null;
			FluxPortInfo fpi = null;
			logger.debug("wp_总体配置:begin");				

			// 确定采集参数
			String getarg = "0000000000000";
			oidList = getOIDList(serial, "2", "64", "1");
			PortJudgeAttr pja = null;
			for (int j = 0; j < oidList.size(); j++)
			{
				pja = (PortJudgeAttr) oidList.get(j);
				int num = Integer.parseInt(pja.getOrder());
				getarg = getarg.substring(0, num) + "1"
								+ getarg.substring(num + 1);
			}

			for (int i = 0; i < list.size(); i++)
			{
				fpi = (FluxPortInfo) list.get(i);
				// logger.debug("portIP:" + fpi.getIfportip());
				// 首先以ip地址为唯一标识进行端口的判断
				fci = new FluxConfigInfo();
				if (null != isdb)
				{
					fci.setIntodb(Integer.parseInt(isdb));
				}
				fci.setPolltime(Integer.parseInt(polltime));
				fci.setDevice_id(device_id);
				
				//端口信息
				fci.setIfindex(fpi.getIfindex());
				if(null!=fpi.getIfportip()&&!"NODATA".equalsIgnoreCase(fpi.getIfportip()))
				{					
					fci.setIfportip(fpi.getIfportip());
				}
				if(null!=fpi.getIfdescr()&&!"NODATA".equalsIgnoreCase(fpi.getIfdescr()))
				{
					fci.setIfdescr(fpi.getIfdescr());
				}
				if(null!=fpi.getIfname()&&!"NODATA".equalsIgnoreCase(fpi.getIfname()))
				{
					fci.setIfname(fpi.getIfname());
				}
				if(null!=fpi.getIfnamedefined()&&!"NODATA".equalsIgnoreCase(fpi.getIfnamedefined()))
				{
					fci.setIfnamedefined(fpi.getIfnamedefined());
				}	
				
				fci.setModel(model);
				fci.setCounternum(counternum);
				fci.setGetway(getGetway(fpi, list));
				fci.setGetarg(getarg);
				fci.setSerial(serial);
				synchronized (NoPorts)
				{
					NoPorts.add(fci);
				}

			}
			logger.debug("wp_总体配置:end");
		}
		logger.debug("NoPorts'size:" + NoPorts.size());
		try
		{
			FluxConfigThread pct = new FluxConfigThread(5);
			pct.setPortsList(NoPorts);			
			pct.setFilterMap(filterMap);
			//pct.setIsAuto(Integer.parseInt(isauto));
//			pct.setGatherFlag(gatherflag);
			pct.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;

	}

	// 同一入口，进行保存操作
	/*public void SaveDeviceOperator1()
	{
		logger.debug("wp_SaveDeviceOperator_begin");
		String serial = request.getParameter("serial");
		String snmp = request.getParameter("snmp");
		String istotal = request.getParameter("istotal");
		String polltime = request.getParameter("polltime");
		String device_id = request.getParameter("device_id");
		String isdb = request.getParameter("isdb");
		NoPorts = new ArrayList();
		ArrayList perfList = null;
		ArrayList baseList = new ArrayList();
		baseList.addAll(getoidList("2"));
		baseList.addAll(getoidList("3"));
		this.filterMap = InitFilterMap();
		// 不是总体配置
		if (istotal.compareTo("0") == 0)
		{
			logger.debug("wp_不是总体配置:begin");
			// 首先要获取用户选择的端口号
			String[] ports = request.getParameterValues("ifindex");
			FluxConfigInfo fci = null;
			String[] attrs = null;
			String[] values = null;
			String ifindex = "";
			String getway = "";

			int num = 0;
			for (int i = 0; i < ports.length; i++)
			{
				ifindex = ports[i].split("\\|\\|\\|")[0];
				getway = ports[i].split("\\|\\|\\|")[1];
				fci = new FluxConfigInfo();
				if (null != isdb)
				{
					fci.setIntodb(Integer.parseInt(isdb));
				}
				fci.setPolltime(Integer.parseInt(polltime));
				fci.setDevice_id(device_id);
				fci.setIfindex(ifindex);
				fci.setSnmpversion(Integer.parseInt(snmp));
				fci.setModel(model);
				fci.setCounternum(counternum);
				// 采集方式
				fci.setGetway(Integer.parseInt(getway));
				// 采集属性值
				attrs = request.getParameterValues("checkbox" + ifindex);
				perfList = new ArrayList();
				String getarg = "0000000000000";
				for (int j = 0; j < attrs.length; j++)
				{
					values = attrs[j].split("\\|\\|\\|");
					num = Integer.parseInt(values[1]);
					getarg = getarg.substring(0, num) + "1"
									+ getarg.substring(num + 1);
					perfList.add(values[0]);
				}
				fci.setPerList(perfList);
				fci.setGetarg(getarg);
				fci.setBaseList(baseList);
				synchronized (NoPorts)
				{
					NoPorts.add(fci);
				}
			}
			logger.debug("wp_不是总体配置:end");
		}
		// 是总体配置
		else
		{
			logger.debug("wp_总体配置:begin");
			// 获取所有的端口信息
			ArrayList listAll = getPortBaseInfo();
			logger.debug("采集到的所有端口数：" + listAll.size());
			FluxConfigInfo fci = null;
			FluxPortInfo fpi = null;
			// 获取所要采集的默认属性
			ArrayList oidList = getoidList("1");
			String getarg = "0000000000000";
			PortJudgeAttr pja = null;
			int num = 0;
			perfList = new ArrayList();
			for (int i = 0; i < oidList.size(); i++)
			{
				pja = (PortJudgeAttr) oidList.get(i);
				perfList.add(pja.getValue());
				num = Integer.parseInt(pja.getOrder());
				getarg = getarg.substring(0, num) + "1"
								+ getarg.substring(num + 1);
			}
			// filter port.
			DeviceAct deviceAct = new DeviceAct();
			Map deviceInfo = deviceAct.getDeviceInfoMap(device_id);
			String device_model = (String) deviceInfo.get("device_model");
			ArrayList list = filterPort(filterMap, device_model, listAll);
			logger.debug("过滤后的端口数是:" + list.size());
			for (int i = 0; i < list.size(); i++)
			{
				fpi = (FluxPortInfo) list.get(i);
				// logger.debug("portIP:" + fpi.getIfportip());
				// 首先以ip地址为唯一标识进行端口的判断
				fci = new FluxConfigInfo();
				if (null != isdb)
				{
					fci.setIntodb(Integer.parseInt(isdb));
				}
				fci.setPolltime(Integer.parseInt(polltime));
				fci.setDevice_id(device_id);
				fci.setIfindex(fpi.getIfindex());
				fci.setSnmpversion(Integer.parseInt(snmp));
				fci.setModel(model);
				fci.setCounternum(counternum);
				fci.setGetway(getGetway(fpi, list));
				fci.setGetarg(getarg);
				// 流量指标（流入流出等）的oid
				fci.setPerList(perfList);
				// 端口信息、端口网络指标的oid
				fci.setBaseList(baseList);
				synchronized (NoPorts)
				{
					NoPorts.add(fci);
				}

			}
			logger.debug("wp_总体配置:end");
		}
		logger.debug("NoPorts'size:" + NoPorts.size());
		try
		{
			FluxConfigThread pct = new FluxConfigThread(5);
			pct.setPortsList(NoPorts);
			pct.setSession(request.getSession());
			pct.setFilterMap(filterMap);
			String srcType = request.getParameter("type");
			if (null != srcType && !"".equals(srcType))
			{
				int type = Integer.parseInt(srcType);
				if (2 == type)
				{
					pct.setType(type);
				}
			}
			pct.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}*/

	/**
	 * 端口过滤(tab_model_port_filter)
	 * 
	 * @param _map
	 *            Map
	 * @param _list
	 *            ArrayList
	 * @return ArrayList
	 */
	public ArrayList filterPort(Map _map, String _device_model, ArrayList _list)
	{
		if (null == _list || _list.size() == 0)
			return null;

		ArrayList list = new ArrayList();

		for (int i = 0; i < _list.size(); i++)
		{
			FluxPortInfo fpi = (FluxPortInfo) _list.get(i);
			String ifname = fpi.getIfname();
			if (filterPortType(_map, _device_model, 1, ifname))
				continue;
			String ifdescr = fpi.getIfdescr();
			if (filterPortType(_map, _device_model, 2, ifdescr))
				continue;
			String ifnamedefined = fpi.getIfnamedefined();
			if (filterPortType(_map, _device_model, 3, ifnamedefined))
				continue;
			String ifindex = fpi.getIfindex();
			if (filterPortType(_map, _device_model, 4, ifindex))
				continue;

			list.add(fpi);
		}

		return list;
	}

	/**
	 * filter port info from tab_model_port_filter
	 * 
	 * @param _device_model
	 *            String
	 * @param _type
	 *            int
	 * @param _value
	 *            String
	 * @return boolean
	 *         <LI>true: the port will be filtered</LI>
	 *         <LI>false: the port will not be filtered</LI>
	 */
	public static boolean filterPortType(Map map, String _device_model,
					int _type, String _value)
	{

		ArrayList list = (ArrayList) map.get(_device_model + "#" + _type);
		if (null != list && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String tem = (String) list.get(i);
				switch (_type)
				{
					case 5:
						if (tem.toLowerCase().equals(_value.toLowerCase()))
						{
							logger.debug("端口类型过滤成功:" + _value);
							return true;
						}
						break;
					case 4:
						String[] arr = new String[2];
						arr = tem.split(",");
						long start = 0,
						end = 0,
						value = 0;
						try
						{
							start = Long.parseLong(arr[0]);
						}
						catch (NumberFormatException ex)
						{
						}

						try
						{
							end = Long.parseLong(arr[1]);
						}
						catch (NumberFormatException ex1)
						{
						}

						try
						{
							value = Long.parseLong(_value);
						}
						catch (NumberFormatException ex2)
						{
						}

						if (value >= start && value <= end)
						{
							logger.debug("端口索引过滤成功:" + _value);
							return true;
						}

						break;

					case 1:

					case 2:

					case 3:

					default:
						if (_value.toLowerCase().indexOf(tem.toLowerCase()) > -1)
						{
							logger.debug("端口名字、描述、别名过滤成功:" + _value);
							return true;
						}

						break;
				}
			}
		}

		return false;
	}

	/**
	 * get map of model filter.
	 * 
	 * @return Map
	 *         <LI>Key: device_model + "#" + type</LI>
	 *         <LI>Value: value</LI>
	 */
	public static Map InitFilterMap()
	{
		Map filterMap = new HashMap();
		filterMap.clear();
		//String sql = "select device_model,type,value from tab_model_port_filter";
		String sql = "";
		//Cursor cursor = DataSetBean.getCursor(sql);
		Cursor cursor = null;
		//Map map = cursor.getNext();
		Map map = null;
		if (null == map)
		{
			logger.debug("tab_model_port_filter,the table is empty.");
		}
		else
		{
			while (map != null)
			{
				String device_model = (String) map.get("device_model");
				String type = (String) map.get("type");
				String value = (String) map.get("value");
				for (int j = 1; j <= 5; j++)
				{
					if (type.equals("" + j))
					{
						if (null == filterMap.get(device_model + "#" + j))
						{
							ArrayList listTem = new ArrayList();
							listTem.add(value);
							filterMap.put(device_model + "#" + j, listTem);
						}
						else
						{
							ArrayList listTem = (ArrayList) filterMap
											.get(device_model + "#" + j);
							listTem.add(value);
							filterMap.put(device_model + "#" + j, listTem);
						}
					}
				}
				map = cursor.getNext();
			}
		}

		return filterMap;
	}
}