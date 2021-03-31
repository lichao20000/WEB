package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gtms.stb.resource.serv.DeviceTypeInfoBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings({"unchecked","rawtypes"})
public class DeviceTypeInfoDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(DeviceTypeInfoDAO.class);


	public List<Map<String,String>> querySpecList()
	{
		List<Map<String,String>> specList = new ArrayList<Map<String,String>>();
		PrepareSQL specPsql = new PrepareSQL("select id,spec_name from tab_bss_dev_port ");
		specList = jt.queryForList(specPsql.getSQL());
		return specList;
	}

	public String getSpecName(String specId)
	{
		List<Map> specList = new ArrayList<Map>();
		PrepareSQL specPsql = new PrepareSQL("select spec_name from tab_bss_dev_port where id=" + specId);
		specList = jt.queryForList(specPsql.getSQL());
		String specName = "";
		if (specList.size() > 0){
			specName = (String) specList.get(0).get("spec_name");
		}
		return specName;
	}


	public String getPortAndType(long deviceTypeId)
	{
		String portSql = "select port_name,port_dir,port_type,port_desc from tab_devicetype_info_port where devicetype_id="
				+ deviceTypeId;
		List<Map> portList = new ArrayList<Map>();
		PrepareSQL portPsql = new PrepareSQL(portSql.toString());
		portList = jt.queryForList(portPsql.getSQL());
		String name="";
		String dir="";
		String type="";
		String desc="";
		if(portList.size()>0)
		{
			for(int i=0;i<portList.size();i++)
			{
				name += StringUtil.getStringValue(portList.get(i).get("port_name"))+ "#";
				dir += StringUtil.getStringValue(portList.get(i).get("port_dir"))+ "#";
				type += StringUtil.getStringValue(portList.get(i).get("port_type"))+ "#";
				desc += StringUtil.getStringValue(portList.get(i).get("port_desc"))+ "#";
			}
		}

		String typeSql = "select server_type from tab_devicetype_info_servertype where devicetype_id="
			+ deviceTypeId;
		List<Map> typeList = new ArrayList<Map>();
		PrepareSQL typePsql = new PrepareSQL(typeSql.toString());
		typeList = jt.queryForList(typePsql.getSQL());
		String serverType="";
		if(typeList.size()>0){
			for(int i=0;i<typeList.size();i++){
				serverType += StringUtil.getStringValue(typeList.get(i).get("server_type"))+ ",";
			}
			serverType = serverType.substring(0, serverType.length() - 1);
		}
		return name+dir+type+desc+"&"+serverType;
	}

	public Map getAccessType()
	{
		PrepareSQL psql = new PrepareSQL("select type_id, type_name from gw_access_type ");
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		list=jt.queryForList(psql.getSQL());

		Map<String,String> AccessMap=new HashMap<String,String>();
		for(Map accessMap : list){
			AccessMap.put(accessMap.get("type_id").toString(), (String)accessMap.get("type_name"));
		}
		return AccessMap;
	}

	/**
	 * @param vendor
	 * @return 查询的特定数据
	 */
	public List<Map> queryDeviceDetail(long deviceTypeId,long spec_id)
	{// TODO wait (more table related)
		String infoSql = "select a.devicetype_id,c.vendor_add,b.device_model,a.access_style_relay_id,a.spec_id,"
			    +" a.specversion,a.hardwareversion,a.softwareversion,a.is_check,a.rela_dev_type_id,"
				+ "a.access_style_relay_id,a.ip_type,is_normal from stb_tab_devicetype_info a,stb_gw_device_model b,stb_tab_vendor c "
				+ "where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id and a.devicetype_id="+ deviceTypeId;
		PrepareSQL infoPsql = new PrepareSQL(infoSql);
		List<Map> infoList = new ArrayList<Map>();
		infoList = jt.queryForList(infoPsql.getSQL());
		if (infoList.size() > 0)
		{
			String access_style_relay_id = StringUtil.getStringValue(infoList.get(0).get(
					"access_style_relay_id"));
			if (!"".equals(access_style_relay_id))
			{
				String type_name = getTypeNameByTypeId(access_style_relay_id);
				infoList.get(0).put("type_id", access_style_relay_id);
				infoList.get(0).put("type_name", type_name);
			}
			else
			{
				infoList.get(0).put("type_id", "");
				infoList.get(0).put("type_name", "");
			}
		}
		String specName = getSpecName(String.valueOf(spec_id));
		infoList.get(0).put("specName", specName);
		String portSql = "select port_name,port_dir,port_type,port_desc from tab_devicetype_info_port "
						+ "where devicetype_id="+ deviceTypeId;
		List<Map> portList = new ArrayList<Map>();
		PrepareSQL portPsql = new PrepareSQL(portSql);
		portList = jt.queryForList(portPsql.getSQL());
		if (portList.size() > 0 && infoList.size() > 0)
		{
			String port = "";
			for (Map m:portList){
				port += StringUtil.getStringValue(m.get("port_name"))+ ",";
			}
			port = port.substring(0, port.length() - 1);
			infoList.get(0).put("port_name", port);
		}
		String typeSql = "select server_type from tab_devicetype_info_servertype where devicetype_id="
				+ deviceTypeId;
		List<Map> typeList = new ArrayList<Map>();
		PrepareSQL typePsql = new PrepareSQL(typeSql);
		typeList = jt.queryForList(typePsql.getSQL());
		if (typeList.size() > 0 && infoList.size() > 0)
		{
			String type = "";
			for (Map m:typeList)
			{
				String tempType = StringUtil.getStringValue(m.get("server_type"));
				if (tempType.equals("0")){
					type += "IMS SIP" + ",";
				}else if (tempType.equals("1")){
					type += "软交换SIP" + ",";
				}else if (tempType.equals("2")){
					type += "H248" + ",";
				}
			}

			type = type.substring(0, type.length() - 1);
			infoList.get(0).put("server_type", type);
		}
		return infoList;
	}

	/**
	 * 根据上行方式ID获取上行方式名称 add by zhangchy 2011-10-25
	 */
	public String getTypeNameByTypeId(String accessStyleRelayId)
	{
		StringBuffer sb = new StringBuffer(
				"select distinct type_name from gw_access_type where type_id = "
						+ accessStyleRelayId);
		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map> list = jt.queryForList(psql.getSQL());
		if (list.size() < 1){
			return "";
		}else if (StringUtil.getStringValue(list.get(0), "type_name") == null){
			return "";
		}

		return StringUtil.getStringValue(list.get(0), "type_name");
	}

	public String getTypeNameList(String typeId)
	{
		StringBuffer sb = new StringBuffer(
				"select distinct type_id, type_name from gw_access_type");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		return DeviceTypeInfoBIO.createListBox(DataSetBean.getCursor(psql.getSQL()),
				"type_id", "type_name", typeId, true);
	}

	public int getDeviceListCount(int vendor, int device_model, String hard_version,
			String soft_version, int is_check, int rela_dev_type, int curPage_splitPage,
			int num_splitPage, String startTime, String endTime,int access_style_relay_id,int spec_id)
	{
		logger.debug("getDeviceListCount({},{},{},{},{},{},{},{})", new Object[] {
				vendor, device_model, hard_version, soft_version, is_check,
				rela_dev_type, curPage_splitPage, num_splitPage });
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select count(*) from stb_tab_devicetype_info a,stb_gw_device_model b,");
		psql.append("stb_tab_vendor c where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id ");
		if (vendor != -1){
			psql.append(" and c.vendor_id='" + vendor + "'");
		}
		if (device_model != -1){
			psql.append(" and b.device_model_id='" + device_model + "'");
		}
		if (!StringUtil.IsEmpty(hard_version)){
			psql.append(" and a.hardwareversion='" + hard_version + "'");
		}
		// 软件版本后模糊匹配
		if (!StringUtil.IsEmpty(soft_version)){
			psql.append(" and a.softwareversion like '" + soft_version + "%'");
		}
		if (is_check != -2){
			psql.append(" and a.is_check=" + is_check);
		}
		if (rela_dev_type != -1){
			psql.append(" and a.rela_dev_type_id=" + rela_dev_type);
		}
		if (!StringUtil.IsEmpty(startTime)){
			psql.append(" and a.add_time > " + startTime);
		}
		if (!StringUtil.IsEmpty(endTime)){
			psql.append(" and a.add_time < " + endTime);
		}
		if (access_style_relay_id != -1){
			psql.append(" and a.access_style_relay_id=" + access_style_relay_id);
		}
		if (spec_id != -1){
			psql.append(" and spec_id = " + spec_id);
		}

		int total = jt.queryForInt(psql.getSQL());
		if (total % num_splitPage == 0){
			return total / num_splitPage;
		}
		return total / num_splitPage + 1;
	}

	public void deleteDevice(int id)
	{
		logger.debug("deleteDevice({})", new Object[] { id });
		String [] sql=new String[3];
		 sql[0] = "delete from stb_tab_devicetype_info where devicetype_id=" + id;
		 sql[1] ="delete from tab_devicetype_info_servertype where devicetype_id=" + id;
		 sql[2]="delete from tab_devicetype_info_port where devicetype_id=" + id;
		 for(int i=0;i<3;i++){
			 PrepareSQL psql = new PrepareSQL(sql[i]);
			 psql.setLong(1,id);
			 sql[i]=psql.getSQL();
		 }

		jt.batchUpdate(sql);
	}

	/**
	 * 根据devicetype_id获取该版本编号下有多少设备
	 */
	public int getDeviceListCount(int id)
	{
		String sql = "select count(*) from stb_tab_gw_device where devicetype_id=" + id;
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForInt(psql.getSQL());
	}

	/**
	 * 增加设备版本
	 *
	 * @param vendor 厂商
	 * @param device_model 设备型号
	 * @param specversion 特定版本
	 * @param hard_version 硬件型号
	 * @param soft_version 软件型号
	 * @param is_check 是否审核
	 * @param rela_dev_type 设备类型
	 */
	public Object[] addDevTypeInfo(int vendor, int device_model, String specversion,
			String hard_version, String soft_version, int is_check, int rela_dev_type,
			String typeId,String  ipType,long spec_id)
	{
		String sql = "insert into stb_tab_devicetype_info(devicetype_id,vendor_id,device_model_id,specversion,hardwareversion,softwareversion,add_time,is_check,rela_dev_type_id,access_style_relay_id,ip_type,spec_id) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		long max_id = DataSetBean.getMaxId("stb_tab_devicetype_info", "devicetype_id");
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, max_id);
		psql.setString(2, String.valueOf(vendor));
		psql.setString(3, String.valueOf(device_model));
		psql.setString(4, specversion);
		psql.setString(5, hard_version);
		psql.setString(6, soft_version);
		psql.setLong(7, TimeUtil.getCurrentTime());
		psql.setInt(8, is_check);
		psql.setInt(9, rela_dev_type);
		psql.setLong(10, new Long(typeId));
		psql.setInt(11, StringUtil.getIntegerValue(ipType));
		psql.setLong(12, spec_id);
		return new Object[] { jt.update(psql.getSQL()), max_id };
	}

	/**
	 * 修改设备版本
	 *
	 * @param deviceTypeId ID
	 * @param vendor 厂商
	 * @param device_model 设备型号
	 * @param specversion 特定版本
	 * @param hard_version 硬件型号
	 * @param soft_version 软件型号
	 * @param is_check 是否审核
	 * @param rela_dev_type 设备类型
	 */
	public int updateDevTypeInfo(long deviceTypeId, int vendor, int device_model,
			String specversion, String hard_version, String soft_version, int is_check,
			int rela_dev_type, String typeId,String ipType,String isNormal,long spec_id)
	{
		// 更新设备版本表
		String sql = "update stb_tab_devicetype_info set is_check=?,rela_dev_type_id=?, " +
				"access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=? where devicetype_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setInt(1, is_check);
		psql.setInt(2, rela_dev_type);
		psql.setLong(3, new Long(typeId));
		psql.setString(4, specversion);
		psql.setInt(5, StringUtil.getIntegerValue(ipType));
		psql.setInt(6, StringUtil.getIntegerValue(isNormal));
		psql.setLong(7, spec_id);
		psql.setLong(8, deviceTypeId);

		PrepareSQL psql2 = new PrepareSQL("update stb_tab_gw_device set device_type=? where devicetype_id=? ");
		if (rela_dev_type == 1){
			psql2.setString(1, "e8-b");
		}else{
			if (rela_dev_type == 2){
				psql2.setString(1, "e8-c");
			}else{
				psql2.setString(1, "");
			}
		}

		psql2.setLong(2, deviceTypeId);
		return jt.batchUpdate(new String[] { psql.getSQL(), psql2.getSQL() })[0];
	}

	public void updateIsCheck(long id)
	{
		logger.debug("updateIsCheck({})", new Object[] { id });
		String sql = "update stb_tab_devicetype_info set is_check=1 where devicetype_id=? ";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, id);
		jt.update(psql.getSQL());
	}

	/**
	 * 添加端口和设备协议
	 */
	public int addPortAddServertype(String portInfo, long acc_oid, String servertype)
	{
		long max_id = DataSetBean.getMaxId("stb_tab_devicetype_info", "devicetype_id");
		if (!servertype.equals(""))
		{
			String[] allServertype = servertype.split("@");
			String[] sql = new String[allServertype.length];
			for (int i = 0; i < allServertype.length; i++)
			{
				sql[i] = "insert into tab_devicetype_info_servertype (devicetype_id,server_type,time,acc_oid) values(?,?,?,?)";
				PrepareSQL psql = new PrepareSQL(sql[i]);
				psql.setLong(1, max_id - 1);
				psql.setInt(2, Integer.parseInt(allServertype[i]));
				psql.setLong(3, System.currentTimeMillis() / 1000);
				psql.setLong(4, acc_oid);
				sql[i] = psql.getSQL();
			}
			jt.batchUpdate(sql);
		}

		String[] port = new String[4];
		if (!"".equals(portInfo))
		{
			String[] AllPort = portInfo.split("#");
			String[] sql = new String[AllPort.length];
			for (int i = 0; i < AllPort.length; i++)
			{
				port = AllPort[i].split("@");
				sql[i] = "insert into tab_devicetype_info_port(devicetype_id,port_name,port_dir,port_type,port_desc,add_time,acc_oid) values(?,?,?,?,?,?,?)";
				PrepareSQL psq = new PrepareSQL(sql[i]);
				psq.setLong(1, max_id - 1);
				psq.setString(2, port[0]);
				psq.setString(3, port[1]);
				psq.setInt(4, Integer.parseInt(port[2]));
				if (port.length > 3){
					psq.setString(5, port[3]);
				}else{
					psq.setString(5, "");
				}
				psq.setLong(6, System.currentTimeMillis() / 1000);
				psq.setLong(7, acc_oid);
				sql[i] = psq.getSQL();
			}
			return jt.batchUpdate(sql)[0];
		}

		return 0;
	}

	/**
	 * 修改端口和设备协议
	 */
	public int updatePortAddServertype(long deviceTypeId, String portInfo, long acc_oid,
			String servertype)
	{
		//long max_id = DataSetBean.getMaxId("stb_tab_devicetype_info", "devicetype_id") - 1;
		String delSqlForPort = "delete  from tab_devicetype_info_port  where devicetype_id="+ deviceTypeId;
		String delSqlForType = "delete  from tab_devicetype_info_servertype   where devicetype_id="+ deviceTypeId;
		String[] del = new String[2];
		PrepareSQL psql1 = new PrepareSQL(delSqlForPort);
		PrepareSQL psql2 = new PrepareSQL(delSqlForType);
		del[0] = psql1.getSQL();
		del[1] = psql2.getSQL();
		jt.batchUpdate(del);;
		if (!servertype.equals(""))
		{
			String[] allServertype = servertype.split("@");
			String[] sql = new String[allServertype.length];
			for (int i = 0; i < allServertype.length; i++)
			{
				sql[i] = "insert into tab_devicetype_info_servertype (devicetype_id,server_type,time,acc_oid) values(?,?,?,?)";
				PrepareSQL psql = new PrepareSQL(sql[i]);
				psql.setLong(1, deviceTypeId);
				psql.setInt(2, Integer.parseInt(allServertype[i]));
				psql.setLong(3, System.currentTimeMillis() / 1000);
				psql.setLong(4, acc_oid);
				sql[i] = psql.getSQL();
			}
			jt.batchUpdate(sql);
		}

		if (!"".equals(portInfo))
		{
			String[] AllPort = portInfo.split("#");
			String[] port = new String[4];
			String[] sql = new String[AllPort.length];

			for (int i = 0; i < AllPort.length; i++)
			{
				port = AllPort[i].split("@");
				sql[i] = "insert into tab_devicetype_info_port(devicetype_id,port_name,port_dir,port_type,port_desc,add_time,acc_oid) values(?,?,?,?,?,?,?)";
				PrepareSQL psql = new PrepareSQL(sql[i]);
				psql.setLong(1, deviceTypeId);
				psql.setString(2, port[0]);
				psql.setString(3, port[1]);
				psql.setInt(4, Integer.parseInt(port[2]));

				if (port.length > 3){
					psql.setString(5, port[3]);
				}else{
					psql.setString(5, "");
				}
				psql.setLong(6, System.currentTimeMillis() / 1000);
				psql.setLong(7, acc_oid);
				sql[i] = psql.getSQL();
			}
			return jt.batchUpdate(sql)[0];
		}

		return 0;
	}

	 public int isNormalVersion(int deviceModel)
	 {
		StringBuffer sb = new StringBuffer(
		"select count(*)  from stb_tab_devicetype_info a where a.is_normal = 1 and a.device_model_id='"+deviceModel+"'");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		return jt.queryForInt(psql.getSQL());
	 }

	 /**
	  * 查询设备类型
	  */
	 public List<Map<String,String>> getGwDevType()
	 {
		 PrepareSQL psql = new PrepareSQL("select type_id,type_name from gw_dev_type");
		 return  jt.queryForList(psql.getSQL());
	 }

	public List<Map> querystbDeviceList(int vendor, int device_model,
			String hard_version, String soft_version,String bootadv, int curPage_splitPage,
			int num_splitPage, String startTime, String endTime)
	{
		logger.debug("querystbDeviceList({},{},{},{},{},{},{},{})", new Object[] { vendor,
				device_model, hard_version, soft_version,
				curPage_splitPage, num_splitPage });

		StringBuffer sb = new StringBuffer();
		// sql修改采用左链接实现，部分地市如江西电信tab_devicetype_info.spec_id可以为空
		sb.append("select a.devicetype_id,a.specversion,a.hardwareversion,");
		sb.append("a.softwareversion,a.vendor_id,a.device_model_id,");
		sb.append("c.vendor_add,b.device_model");

		if(LipossGlobals.inArea(Global.JXDX)){
			sb.append(",a.zeroconf,a.bootadv,a.category,a.is_probe");
		}else if(LipossGlobals.inArea(Global.HLJDX)){
			sb.append(",a.zeroconf,a.is_check,a.bootadv");
		}else if(LipossGlobals.inArea(Global.HNLT)){
			sb.append(",a.epg_version,a.net_type");
		}else if(!LipossGlobals.inArea(Global.JLDX)){
			sb.append(",a.zeroconf,a.bootadv");
		}else if(LipossGlobals.inArea(Global.SXLT)){
			sb.append(",a.is_check");
		}
		// TODO wait (more table related)
		sb.append(" from stb_tab_devicetype_info a, stb_gw_device_model b,stb_tab_vendor c ");
		sb.append("where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id ");
		if (vendor != -1){
			sb.append(" and c.vendor_id='" + vendor + "'");
		}
		if (device_model != -1){
			sb.append(" and b.device_model_id='" + device_model + "'");
		}
		if (!StringUtil.IsEmpty(hard_version)){
			 sb.append(" and a.hardwareversion='" + hard_version + "'");
		}
		// 软件版本后模糊匹配
		if (!StringUtil.IsEmpty(soft_version)){
			sb.append(" and a.softwareversion like '" + soft_version + "%'");
		}
		if (!StringUtil.IsEmpty(bootadv)){
			sb.append(" and a.bootadv='" + bootadv + "'");
		}
//		if (is_check != -2){
//			sb.append(" and a.is_check=" + is_check);
//		}
//		if (rela_dev_type != -1){
//			sb.append(" and a.rela_dev_type_id=" + rela_dev_type);
//		}
		if (!StringUtil.IsEmpty(startTime)){
			sb.append(" and a.add_time > " + startTime);
		}
		if (!StringUtil.IsEmpty(endTime)){
			sb.append(" and a.add_time < " + endTime);
		}
//		if (access_style_relay_id != -1){
//			sb.append(" and a.access_style_relay_id=" + access_style_relay_id);
//		}
//		if (spec_id != -1){
//			sb.append(" and spec_id = " + spec_id);
//		}
		sb.append(" order by a.devicetype_id");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		//final Map accessTypeMap = getAccessType();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage	+ 1,
				num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
//				String accessStyleRelayId = StringUtil.getStringValue(rs
//						.getString("access_style_relay_id"));

				map.put("devicetype_id", rs.getString("devicetype_id"));
				if(!LipossGlobals.inArea(Global.JLDX) && !LipossGlobals.inArea(Global.HNLT)){
					map.put("zeroconf", rs.getString("zeroconf"));
					map.put("bootadv", rs.getString("bootadv"));
				}
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
//				map.put("spec_id", rs.getString("spec_id"));
				map.put("specversion", rs.getString("specversion"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				if(LipossGlobals.inArea(Global.HLJDX) || LipossGlobals.inArea(Global.SXLT)){
					map.put("is_check", rs.getString("is_check"));
				}
//				map.put("rela_dev_type_id", rs.getString("rela_dev_type_id"));
				map.put("vendor_id", rs.getString("vendor_id"));
				map.put("device_model_id", rs.getString("device_model_id"));
				if(LipossGlobals.inArea(Global.JXDX)){
					map.put("category", rs.getString("category"));
					map.put("is_probe", rs.getString("is_probe"));
				}
				if(LipossGlobals.inArea(Global.HNLT)){
					map.put("epg_version", rs.getString("epg_version"));
					map.put("net_type", rs.getString("net_type"));
				}
//				map.put("ip_type", rs.getString("ip_type"));
//				map.put("is_normal", rs.getString("is_normal"));
//				map.put("specName", rs.getString("specName"));
//				if (!"".equals(accessStyleRelayId))
//				{
//					// String type_name = getTypeNameByTypeId(accessStyleRelayId);
//					map.put("type_id", accessStyleRelayId);
//					map.put("type_name", (String) accessTypeMap.get(accessStyleRelayId));
//				}
//				else
//				{
//					map.put("type_id", "");
//					map.put("type_name", "");
//				}
				return map;
			}
		});
		return list;
	}

	public int getstbDeviceListCount(int vendor, int device_model,
			String hard_version, String soft_version,String bootadv, int curPage_splitPage, int num_splitPage,
			String startTime, String endTime) {
		logger.debug("getstbDeviceListCount({},{},{},{},{},{},{},{})", new Object[] {
				vendor, device_model, hard_version, soft_version, curPage_splitPage, num_splitPage });
		StringBuffer sb = new StringBuffer();// TODO wait (more table related)
		String sql = "select count(*) from stb_tab_devicetype_info a, stb_gw_device_model b,stb_tab_vendor c"
				+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id ";
		sb.append(sql);
		if (vendor != -1){
			sb.append(" and c.vendor_id='" + vendor + "'");
		}
		if (device_model != -1){
			sb.append(" and b.device_model_id='" + device_model + "'");
		}
		if (!StringUtil.IsEmpty(hard_version)){
			 sb.append(" and a.hardwareversion='" + hard_version + "'");
		}
		// 软件版本后模糊匹配
		if (!StringUtil.IsEmpty(soft_version)){
			sb.append(" and a.softwareversion like '" + soft_version + "%'");
		}
		if (!StringUtil.IsEmpty(bootadv)){
			sb.append(" and a.bootadv='" + bootadv + "'");
		}
//		if (is_check != -2){
//			sb.append(" and a.is_check=" + is_check);
//		}
//		if (rela_dev_type != -1){
//			sb.append(" and a.rela_dev_type_id=" + rela_dev_type);
//		}
		if (!StringUtil.IsEmpty(startTime)){
			sb.append(" and a.add_time > " + startTime);
		}
		if (!StringUtil.IsEmpty(endTime)){
			sb.append(" and a.add_time < " + endTime);
		}
//		if (access_style_relay_id != -1){
//			sb.append(" and a.access_style_relay_id=" + access_style_relay_id);
//		}
//		if (spec_id != -1){
//			sb.append(" and spec_id = " + spec_id);
//		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
		int total = jt.queryForInt(psql.getSQL());
		if (total % num_splitPage == 0){
			return total / num_splitPage;
		}
		return total / num_splitPage + 1;
	}

	public int getstbDeviceListCount(int deleteID)
	{
		String sql = "select count(*) from stb_tab_gw_device where devicetype_id=" + deleteID;
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForInt(psql.getSQL());
	}

	public void deletestbDevice(int deleteID)
	{
		logger.warn("deletestbDevice({})", new Object[] { deleteID });
		String sql= "delete from stb_tab_devicetype_info where devicetype_id=" + deleteID;
		PrepareSQL psql = new PrepareSQL(sql);
		jt.update(psql.getSQL());
	}

	public List<Map> querystbDeviceDetail(long deviceTypeId, long detailSpecId)
	{// TODO wait (more table related)
		String infoSql = "select a.devicetype_id, c.vendor_add, b.device_model, a.access_style_relay_id,a.spec_id,"
			    +" a.specversion, a.hardwareversion, a.softwareversion, a.is_check, a.rela_dev_type_id,"
				+ " a.access_style_relay_id,a.ip_type ,is_normal from stb_tab_devicetype_info a, stb_gw_device_model b,stb_tab_vendor c "
				+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id and a.devicetype_id="
				+ deviceTypeId;
		PrepareSQL infoPsql = new PrepareSQL(infoSql.toString());
		List<Map> infoList = new ArrayList<Map>();
		infoList = jt.queryForList(infoPsql.getSQL());
		if (infoList.size() > 0)
		{
			String access_style_relay_id = StringUtil.getStringValue(infoList.get(0).get(
					"access_style_relay_id"));
			if (!"".equals(access_style_relay_id))
			{
				String type_name = getTypeNameByTypeId(access_style_relay_id);
				infoList.get(0).put("type_id", access_style_relay_id);
				infoList.get(0).put("type_name", type_name);
			}
			else
			{
				infoList.get(0).put("type_id", "");
				infoList.get(0).put("type_name", "");
			}
		}
		String specName = getSpecName(String.valueOf(detailSpecId));
		infoList.get(0).put("specName", specName);
		String portSql = "select port_name,port_dir,port_type,port_desc from stb_tab_devicetype_info_port where devicetype_id="
				+ deviceTypeId;
		List<Map> portList = new ArrayList<Map>();
		PrepareSQL portPsql = new PrepareSQL(portSql.toString());
		portList = jt.queryForList(portPsql.getSQL());
		if (portList.size() > 0 && infoList.size() > 0)
		{
			String port = "";
			for (int i = 0; i < portList.size(); i++)
			{
				port += StringUtil.getStringValue(portList.get(i).get("port_name"))+ ",";
			}
			port = port.substring(0, port.length() - 1);
			infoList.get(0).put("port_name", port);
		}
		String typeSql = "select server_type from stb_tab_devicetype_info_servertype where devicetype_id="
				+ deviceTypeId;
		List<Map> typeList = new ArrayList<Map>();
		PrepareSQL typePsql = new PrepareSQL(typeSql.toString());
		typeList = jt.queryForList(typePsql.getSQL());
		if (typeList.size() > 0 && infoList.size() > 0)
		{
			String type = "";
			for (Map m:typeList)
			{
				String tempType = StringUtil.getStringValue(m.get("server_type"));
				if (tempType.equals("0")){
					type += "IMS SIP" + ",";
				}else if (tempType.equals("1")){
					type += "软交换SIP" + ",";
				}else if (tempType.equals("2")){
					type += "H248" + ",";
				}
			}

			type = type.substring(0, type.length() - 1);
			infoList.get(0).put("server_type", type);
		}
		return infoList;
	}

	public Object[] addstbDevTypeInfo(int vendor, int device_model, String specversion,
			String hard_version, String soft_version, int is_check, int rela_dev_type,
			String typeId,String  zeroconf,String bootadv)
	{
		String sql = "insert into stb_tab_devicetype_info(devicetype_id,vendor_id,device_model_id,specversion,hardwareversion,softwareversion,add_time,is_check,zeroconf,bootadv) values(?,?,?,?,?,?,?,?,?,?)";
		long max_id = DataSetBean.getMaxId("stb_tab_devicetype_info", "devicetype_id");
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, max_id);
		psql.setString(2, String.valueOf(vendor));
		psql.setString(3, String.valueOf(device_model));
		psql.setString(4, specversion);
		psql.setString(5, hard_version);
		psql.setString(6, soft_version);
		psql.setLong(7, TimeUtil.getCurrentTime());
		psql.setInt(8, is_check);
		psql.setString(9,zeroconf);
		psql.setString(10,bootadv);
		return new Object[] { jt.update(psql.getSQL()), max_id };
	}

	public int updatestbDevTypeInfo(long deviceTypeId, int vendor, int device_model,
			String specversion, String hard_version, String soft_version, int is_check,
			int rela_dev_type, String typeId,String ipType,String isNormal,long spec_id,
			String zeroconf,String bootadv, int category,int is_probe)
	{
		// 更新设备版本表
		String sql = "update stb_tab_devicetype_info set zeroconf=?,bootadv=?,category=?,is_probe=? where devicetype_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, zeroconf);
		psql.setString(2, bootadv);
		psql.setLong(3, deviceTypeId);
		psql.setLong(3, category);
		psql.setLong(4, is_probe);
		psql.setLong(5, deviceTypeId);

		return jt.batchUpdate(new String[] { psql.getSQL() })[0];
	}

	public int updatestbDevTypeInfo(long deviceTypeId, int vendor, int device_model,
			String specversion, String hard_version, String soft_version, int is_check,
			int rela_dev_type, String typeId,String ipType,String isNormal,long spec_id,
			String zeroconf,String bootadv, int category)
	{
		// 更新设备版本表
		String sql = "update stb_tab_devicetype_info set zeroconf=?,bootadv=?,is_check=? where devicetype_id=?";
		if(LipossGlobals.inArea(Global.JXDX)){
			sql = "update stb_tab_devicetype_info set zeroconf=?,bootadv=?,category=? where devicetype_id=?";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, zeroconf);
		psql.setString(2, bootadv);
		psql.setLong(3, is_check);
		psql.setLong(4, deviceTypeId);
		if(LipossGlobals.inArea(Global.JXDX)){
			psql.setLong(3, category);
			psql.setLong(4, deviceTypeId);
		}

		return jt.batchUpdate(new String[] { psql.getSQL() })[0];
	}

	/**
	 * 修改版本型号EPG版本/适用网络类型 数据，湖南联通特有
	 */
	public int updatestbDevTypeInfo(long deviceTypeId,String epg_version,String net_type)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("update stb_tab_devicetype_info set epg_version=?,net_type=? ");
		psql.append("where devicetype_id=? ");
		psql.setString(1, epg_version);
		psql.setString(2, net_type);
		psql.setLong(3, deviceTypeId);

		return jt.batchUpdate(new String[] { psql.getSQL() })[0];
	}

	public int updatestbPortAddServertype(long deviceTypeId, String portInfo, long acc_oid,
			String servertype)
	{
		//long max_id = DataSetBean.getMaxId("stb_tab_devicetype_info", "devicetype_id") - 1;
		String delSqlForPort = "delete from stb_tab_devicetype_info_port where devicetype_id="+ deviceTypeId;
		String delSqlForType = "delete from stb_tab_devicetype_info_servertype where devicetype_id="+ deviceTypeId;
		String[] del = new String[2];
		PrepareSQL psql1 = new PrepareSQL(delSqlForPort);
		PrepareSQL psql2 = new PrepareSQL(delSqlForType);
		del[0] = psql1.getSQL();
		del[1] = psql2.getSQL();
		jt.batchUpdate(del);;
		if (!servertype.equals(""))
		{
			String[] allServertype = servertype.split("@");
			String[] sql = new String[allServertype.length];
			for (int i = 0; i < allServertype.length; i++)
			{
				sql[i] = "insert into tab_devicetype_info_servertype (devicetype_id,server_type,time,acc_oid) values(?,?,?,?)";
				PrepareSQL psql = new PrepareSQL(sql[i]);
				psql.setLong(1, deviceTypeId);
				psql.setInt(2, Integer.parseInt(allServertype[i]));
				psql.setLong(3, System.currentTimeMillis() / 1000);
				psql.setLong(4, acc_oid);
				sql[i] = psql.getSQL();
			}
			jt.batchUpdate(sql);
		}

		if (!"".equals(portInfo))
		{
			String[] AllPort = portInfo.split("#");
			String[] port = new String[4];
			String[] sql = new String[AllPort.length];

			for (int i = 0; i < AllPort.length; i++)
			{
				port = AllPort[i].split("@");
				sql[i] = "insert into tab_devicetype_info_port(devicetype_id,port_name,port_dir,port_type,port_desc,add_time,acc_oid) values(?,?,?,?,?,?,?)";
				PrepareSQL psql = new PrepareSQL(sql[i]);
				psql.setLong(1, deviceTypeId);
				psql.setString(2, port[0]);
				psql.setString(3, port[1]);
				psql.setInt(4, Integer.parseInt(port[2]));

				if (port.length > 3){
					psql.setString(5, port[3]);
				}else{
					psql.setString(5, "");
				}
				psql.setLong(6, System.currentTimeMillis() / 1000);
				psql.setLong(7, acc_oid);
				sql[i] = psql.getSQL();
			}
			return jt.batchUpdate(sql)[0];
		}

		return 0;
	}


}
