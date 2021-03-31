package com.linkage.module.liposs.performance.dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.litms.LipossGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.Log;
import com.linkage.module.gwms.Global;
import com.linkage.module.liposs.performance.bio.Flux_Map_Instance;
import com.linkage.module.liposs.performance.bio.snmpGather.FluxPortInfo;
import com.linkage.module.liposs.system.basesupport.BaseSupportDAO;
import com.linkage.system.utils.DateTimeUtil;
/**
 * 配置流量DAO通用类
 * @author BENYP(5260)
 * *********************************************修改记录&说明*********************************************
 * ----------------------------------------------------------------------------------------------------
 *  ID   时间       修改人         单子编号              修改内容                 备注
 * ----------------------------------------------------------------------------------------------------
 *   1 2009-01-19 BENYP(5260)                  BBMS设备资源表改为tab_gw_device，设备型号改为显示为设备序列号，
 *                                             使用为device_model_id字段【与表gw_device_model关联】，厂商改为oui
 * -----------------------------------------------------------------------------------------------------
 *   2
 * *****************************************************************************************************
 *
 */
public abstract class ConfigFluxDao extends BaseSupportDAO
{
	private Logger log = LoggerFactory.getLogger(ConfigFluxDao.class);
	private String logFileName = "fluxConfig" + new DateTimeUtil().getShortDate()+ ".log";
	/**
	 * 编辑流量端口
	 * @param device_id:设备ID
	 * @param port_info：端口信息：getway|||port_info
	 * @param intodb:是否入库 0:不入库 1：入库
	 * @param gatherflag:是否采集 0：不采集 1：采集
	 * @param fmi:告警数据类
	 * @return true:成功 false 失败
	 */
	public abstract boolean editFluxPort(String device_id,String[] port_info,int intodb,int gatherflag,Flux_Map_Instance fmi);

	/**
	 * 装载这个设备的告警配置信息
	 *
	 * @param device_id
	 * @return key:device_id||getway||port_info value:Flux_Map_Instance对象
	 */
	public abstract Map<String, Flux_Map_Instance> loadDeviceAlarm(String device_id);

	/**
	 * 配置设备端口
	 * @param param  放端口的配置参数
	 * （device_id、snmpversion、model、counternum、polltime、gatherflag、ifconfigflag、intodb、getarg）
	 * @param fpi
	 * @param alarmParam
	 * @return
	 */
	public abstract boolean configDevicePortFlux(Map<String,String> param,FluxPortInfo fpi,Flux_Map_Instance alarmParam);

	/**
	 * 判断这个设备这个端口类型是否能过滤
	 * @param device_id
	 * @param iftype
	 * @return
	 */
	public boolean isFilterPortByPortType(String device_id,int iftype)
	{
		boolean result=false;// TODO wait (more table related)
		String  sql="select count(*) from tab_gw_device a,tab_model_port_filter b,gw_device_model c "
			+"where a.device_model_id=c.device_model_id and c.device_model=b.device_model and b.type=5 and b.value='"
			+iftype+"' and a.device_id='"+device_id+"'";
		PrepareSQL psql = new PrepareSQL(sql);
		int count=0;
		try
		{
			count=jt.queryForInt(psql.getSQL());
		}
		catch(Exception e)
		{
			count=0;
		}
		if(count>0)
		{
			result=true;
		}
		return result;
	}


	/**
	 * 删除设备流量配置
	 *
	 * @param device_id:设备ID
	 * @return true:删除成功 false 删除失败
	 */
	public boolean delFluxConfig(String device_id)
	{
		boolean result=false;
		String[] sqlArray = new String[2];
		sqlArray[0] = "delete from flux_deviceportconfig where device_id='" + device_id
				+ "'";
		PrepareSQL psql = new PrepareSQL(sqlArray[0]);
		psql.getSQL();
		log(true,sqlArray[0]);
		sqlArray[1] = "delete from flux_interfacedeviceport where device_id='"
				+ device_id + "'";
		psql = new PrepareSQL(sqlArray[1]);
		psql.getSQL();
		log(true,sqlArray[1]);
		try
		{			jt.batchUpdate(sqlArray);
			result=true;
			log(true,"－－－－－－删除"+device_id+"－－－－－－－执行成功－－－－－－－－－－－－－－－－－");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log(true,"－－－－－－删除"+device_id+"－－－执行失败－－－－－－－－－－－－－－－－－");
		}

		return result;
	}

	/**
	 * 获取单个端口信息
	 *
	 * @param device_id:设备ID
	 * @param port_info：单个端口信息【getway|||port_info】
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getFluxPortInfo(String device_id, String port_info)
	{
		String[] tmp = port_info.split("\\|\\|\\|");
		String[] port=new String[2];
		if (tmp.length < 2)
		{
			port[0]=tmp[0];
			port[1]="";
		}else{
			port=tmp;
		}

		String port_tmp=port[1];
		String sql="";

		// b表字段
		String bColunm = "b.ifindex, b.ifdescr, b.ifname, b.ifnamedefined, b.iftype, b.ifspeed, b.ifmtu, b.ifhighspeed, " +
				"b.ifinoctetsbps_max, b.ifoutoctetsbps_max, b.ifindiscardspps_max, b.ifoutdiscardspps_max, " +
				"b.ifinerrorspps_max, b.ifouterrorspps_max, b.warningnum, b.warninglevel, b.reinstatelevel, b.overper, " +
				"b.overnum, b.com_day, b.overlevel, b.reinoverlevel, b.intbflag, b.ifinoctets, b.inoperation, b.inwarninglevel, " +
				"b.inreinstatelevel, b.outtbflag, b.ifoutoctets, b.outwarninglevel, b.outreinstatelevel, b.outoperation";
		// 江苏专用
		String jsColunm = "b.ifinoct_maxtype, b.ifoutoct_maxtype, b.ifinoct_mintype, b.ifinoctetsbps_min, b.ifoutoct_mintype, " +
				"b.ifoutoctetsbps_min, b.warningnum_min, b.warninglevel_min, b.reinlevel_min, b.overmax, b.overmin, " +
				"b.overper_min, b.overnum_min, b.overlevel_min, b.reinoverlevel_min";

		sql = "select a.intodb,a.gatherflag," + bColunm;

		if(Global.JSDX.equals(Global.instAreaShortName)){
			sql += "," + jsColunm;
		}

		if(port_tmp==null || "null".equals(port_tmp)){
			sql += "from flux_deviceportconfig a,flux_interfacedeviceport b"
				+ " where a.device_id=b.device_id and a.getway=b.getway and a.port_info=b.port_info and a.device_id='"
				+ device_id
				+ "'"
				+ " and a.getway="
				+ port[0]
				+ " and (a.port_info is null or a.port_info='null')";
		}else{
			sql += "from flux_deviceportconfig a,flux_interfacedeviceport b"
				+ " where a.device_id=b.device_id and a.getway=b.getway and a.port_info=b.port_info and a.device_id='"
				+ device_id
				+ "'"
				+ " and a.getway="
				+ port[0]
				+ " and a.port_info='"
				+ port[1] + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 删除端口信息
	 *
	 * @param device_id:设备ID
	 * @param ports：端口信息【getway|||port_info】
	 * @return true：删除成功 false:删除失败
	 */
	public boolean delFluxPort(String device_id, String[] ports)
	{
		String sql = "";
		ArrayList<String> list=new ArrayList<String>();
		int n = ports.length;
		String[] tmp;
		for (int i = 0; i < n; i++)
		{
			tmp = ports[i].split("\\|\\|\\|");
			if (tmp.length < 2)
			{
				continue;
			}
			sql = " delete from flux_deviceportconfig where device_id='" + device_id
					+ "' and getway=" + tmp[0] + " and port_info='" + tmp[1] + "' ";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			list.add(sql);
			sql = " delete from flux_interfacedeviceport where device_id='" + device_id
					+ "' and getway=" + tmp[0] + " and port_info='" + tmp[1] + "' ";
			psql = new PrepareSQL(sql);
			list.add(psql.getSQL());
		}

		if(list==null || list.isEmpty()){
			return false;
		}
		n=list.size();
		tmp=new String[n];
		for(int i=0;i<n;i++){
			tmp[i]=list.get(i);
			log(true,list.get(i));
		}
		try
		{
			jt.batchUpdate(tmp);
			log(true,"－删除设备"+device_id+" 端口信息－－－－－－－－－－－－执行成功－－－－－－－－－－－－－－－－－");
		}
		catch (Exception e)
		{
			log(true,"－删除设备"+device_id+" 端口信息----------------－－执行失败－－－－－－－－－－－－－－－－－");
			return false;
		}
		return true;
	}

	/**
	 * 查询已配置端口列表
	 *
	 * @param device_id:设备ID
	 * @return List
	 *         <ul>
	 *         <li><font color='blue'>port_info:</font>端口信息</li>
	 *         <li><font color='blue'>ifindex:</font>端口索引</li>
	 *         <li><font color='blue'>ifdescr:</font>端口描述</li>
	 *         <li><font color='blue'>ifname:</font>端口名字</li>
	 *         <li><font color='blue'>ifnamedefined:</font>端口别名</li>
	 *         <li><font color='blue'>ifportip:</font>端口IP</li>
	 *         <li><font color='blue'>getway:</font>采集方式</li>
	 *         <li><font color='blue'>getwayStr:</font>索引</li>
	 *         <li><font color='blue'>confignum:</font>已配阈值数量<font
	 *         color='red'>(注：江苏使用字段)</font></li>
	 *         <li><font color='blue'>gatherflag</font>采集标志</li>
	 *         <li><font color='blue'>ifconfigflag</font>是否已经配置</li>
	 *         <li><font color='blue'>polltime</font>采集时间间隔</li>
	 *         <li><font color='blue'>intodb</font>是否入库</li>
	 *         <li><font color='blue'>snmpversion</font>SNMP版本</li>
	 *         </ul>
	 */
	public abstract List<Map<String, String>> getConfigPortList(String device_id);

	protected String changeString(String str)
	{
		if (str == null || str.equalsIgnoreCase("null"))
		{
			return "";
		}
		return str;
	}

	/**
	 * 获取系统中OID列表
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getOIDMapList()
	{
		String sql = "select model, snmpversion, counternum, oid_type, oid_desc, oid_name, oid_order, oid_value " +
				" from flux_oid_map order by model,snmpversion,counternum,oid_type,oid_order";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	public List getBandwidth(String deviceId){
		PrepareSQL psql = new PrepareSQL("select bandwidth from tab_egwcustomer where device_id='"+deviceId+"'");
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 查询已经配置的设备【支持批量配置，device_id间用,分隔】
	 *
	 * @param deviceList:设备ID链表
	 * @return
	 *            <li><font color='blue'>配置状态:</font>ifconfigflag</li>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDevConfigFluxResult(List<String> deviceList)
	{
		String sql = "select distinct device_id from flux_deviceportconfig where device_id in("
				+ dualDevID(deviceList) + ") and ifconfigflag=1";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 获取设备基本信息
	 *
	 * @param deviceList
	 *            设备ID链表
	 *            <ul>
	 *            <li><font color='blue'>设备ID:</font>device_id</li>
	 *            <li><font color='blue'>设备名称:</font>device_name</li>
	 *            <li><font color='blue'>设备IP:</font>loopback_ip</li>
	 *            <li><font color='blue'>设备厂商:</font>vendor_name</li>
	 *            <li><font color='blue'>设备型号:</font>device_model</li>
	 *            <li><font color='blue'>设备型号:</font>serial</li>
	 *            </ul>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getDevInfo(List<String> deviceList)
	{
		String devID=dualDevID(deviceList);
//		String sql="select a.device_id,a.device_name,a.loopback_ip,a.device_serialnumber as device_model,a.devicetype_id as serial,b.vendor_name "
//			      +" from tab_gw_device a left join tab_vendor b on a.oui=b.vendor_id "
//			      +" where device_id in("+devID+")";
		String sql="select a.device_id,a.device_name,a.loopback_ip,a.device_serialnumber as device_model,a.devicetype_id as serial,b.vendor_name "
		      +" from tab_gw_device a left join tab_vendor b on a.vendor_id=b.vendor_id "
		      +" where device_id in("+devID+")";

		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list=jt.queryForList(psql.getSQL());
		/*
		sql="select a.device_id,b.devicetype_id as serial from tab_gw_device a,tab_devicetype_info b where a.devicetype_id=b.devicetype_id"
		+" and a.oui=b.oui and a.os_version=b.softwareversion and a.device_id in("+devID+")";
		List<Map> serialList=jt.queryForList(sql);
		Map<String,String> map=new HashMap<String,String>();
		for(Map m:serialList){
			map.put(m.get("device_id")+"", m.get("serial")+"");
		}
		String serial;
		for(Map m:list){
			serial=map.get(m.get("device_id")+"");
			if(serial!=null){
				m.put("serial",serial);
			}else{
				m.put("serial",getSerial(""+m.get("device_id")));
			}
		}
		log.debug("设备信息:"+list);
		*/
		return list;
	}
	/*
	private String getSerial(String device_id){
		String sql="select serial from tab_gw_device a,tab_devicetype_info b"
			+" where a.device_model=b.device_name and a.vendor_id=b.vendor_id"
			+" and device_id='"+device_id+"'";
		List<Map> list=jt.queryForList(sql);
		if(list==null || list.isEmpty()){
			return "-1";
		}else{
			return list.get(0).get("serial")+"";
		}
	}
	*/
	/**
	 * 装载设备的端口过滤规则（内部捕获异常）
	 *
	 * @param device_id
	 * @return 返回map key:type value:这个型号这个过滤类型对应的值list 如果没有查到对应的过滤规则，则返回0长度的map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> initFilterRule(String device_id)
	{
		Map<String, List<String>> filterRule = new HashMap<String, List<String>>();
		String sql = "select device_model_id from tab_gw_device where device_id='"
				+ device_id + "'";
		try
		{
			PrepareSQL psql = new PrepareSQL(sql);
			Map deviceInfo = jt.queryForMap(psql.getSQL());
			String device_model = (String) deviceInfo.get("device_model");
			sql = "select type, value from tab_model_port_filter where device_model='"
					+ device_model + "' order by type,value";
			psql = new PrepareSQL(sql);
			List<Map> list = jt.queryForList(psql.getSQL());
			String type = "";
			List<String> tempList = null;
			for (Map record : list)
			{
				type = String.valueOf(record.get("type"));
				if (filterRule.containsKey(type))
				{
					tempList = filterRule.get(type);
				}
				else
				{
					tempList = new ArrayList<String>();
				}
				tempList.add(String.valueOf(record.get("value")));
				filterRule.put(type, tempList);
			}
		}
		catch (Exception e)
		{
			return filterRule;
		}
		return filterRule;
	}

	/**
	 * 拼接DEVICE_ID
	 *
	 * @param device_id
	 * @return deviceID
	 */
	protected String dualDevID(List<String> deviceList)
	{
		if (deviceList.isEmpty())
		{
			return "";
		}
		StringBuffer sb = new StringBuffer(512);
		for (String device_id : deviceList)
		{
			sb.append("'").append(device_id).append("',");
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 记录日志的方法
	 *
	 * @param isNeedWriteFile
	 *            是否需要写文件，true：刷屏＋写文件 false：只刷屏不写文件
	 * @param msg
	 *            日志内容
	 */
	protected void log(boolean isNeedWriteFile, String msg)
	{
		log.debug(msg);
		if (isNeedWriteFile)
		{
			Log.writeLog("【"+new DateTimeUtil().getLongDate()+"】"+msg, logFileName);
		}
	}

	/**
	 * 获取版本信息
	 * @param device_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getSnmpversion(String device_id) {
		String sql = "select snmp_version from sgw_security where device_id='" + device_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		Map<String, String> map = jt.queryForMap(psql.getSQL());

		if (null == map) {
			return "3";
		}
		log.debug("FFFFFFFFFFFFFFFF:" + map);
		if ("v3".equals(map.get("snmp_version"))) {
			return "3";
		} else if ("v2".equals(map.get("snmp_version"))) {
			return "2";
		} else {
			return "1";
		}
	}
}
