/**
 * 
 */

package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.bio.DeviceTypeInfoBIO;

/**
 * @author chenjie
 */
public class DeviceTypeInfoExpDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DeviceTypeInfoExpDAO.class);

	
	@SuppressWarnings("unchecked")
	public List<Map> queryDeviceList(int vendor, int device_model, String hard_version,
			String soft_version, int is_check, int rela_dev_type, int curPage_splitPage,
			int num_splitPage, String startTime, String endTime,int access_style_relay_id,int spec_id)
	{
		logger.debug("queryDeviceList({},{},{},{},{},{},{},{})", new Object[] { vendor,
				device_model, hard_version, soft_version, is_check, rela_dev_type,
				curPage_splitPage, num_splitPage });
		StringBuffer sb = new StringBuffer();
		// sql修改采用左链接实现，部分地市如江西电信tab_devicetype_info.spec_id可以为空
		String sql = "select a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
				+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName, "
				+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, a.rela_dev_type_id,a.ip_type,is_normal"
				+ "  from tab_devicetype_info_exp a left join tab_bss_dev_port d on a.spec_id = d.id, gw_device_model b,tab_vendor c"
				+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
		sb.append(sql);
		if (vendor != -1)
		{
			sb.append(" and c.vendor_id='" + vendor + "'");
		}
		if (device_model != -1)
		{
			sb.append(" and b.device_model_id='" + device_model + "'");
		}
		if (hard_version != null && !"".equals(hard_version))
		{
			sb.append(" and a.hardwareversion='" + hard_version + "'");
		}
		// 软件版本后模糊匹配
		if (soft_version != null && !"".equals(soft_version))
		{
			sb.append(" and a.softwareversion like '" + soft_version + "%'");
		}
		if (is_check != -2)
		{
			sb.append(" and a.is_check=" + is_check);
		}
		if (rela_dev_type != -1)
		{
			sb.append(" and a.rela_dev_type_id=" + rela_dev_type);
		}
		if (startTime != null && !"".equals(startTime))
		{
			sb.append(" and a.add_time > " + startTime);
		}
		if (endTime != null && !"".equals(endTime))
		{
			sb.append(" and a.add_time < " + endTime);
		}
		if (access_style_relay_id != -1)
		{
			sb.append(" and a.access_style_relay_id=" + access_style_relay_id);
		}
		if (spec_id != -1)
		{
			sb.append(" and spec_id = " + spec_id);
		}
		sb.append(" order by a.devicetype_id");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		final Map accessTypeMap = getAccessType();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				String accessStyleRelayId = StringUtil.getStringValue(rs
						.getString("access_style_relay_id"));
				map.put("devicetype_id", rs.getString("devicetype_id"));
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
				map.put("spec_id", rs.getString("spec_id"));
				map.put("specversion", rs.getString("specversion"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("is_check", rs.getString("is_check"));
				map.put("rela_dev_type_id", rs.getString("rela_dev_type_id"));
				map.put("vendor_id", rs.getString("vendor_id"));
				map.put("device_model_id", rs.getString("device_model_id"));
				map.put("ip_type", rs.getString("ip_type"));
				map.put("is_normal", rs.getString("is_normal"));
				map.put("specName", rs.getString("specName"));
				if (!"".equals(accessStyleRelayId))
				{
					// String type_name = getTypeNameByTypeId(accessStyleRelayId);
					map.put("type_id", accessStyleRelayId);
					map.put("type_name", (String) accessTypeMap.get(accessStyleRelayId));
				}
				else
				{
					map.put("type_id", "");
					map.put("type_name", "");
				}
				return map;
			}
		});
		return list;
	}
	
	public List<Map<String,String>> queryExeclDeviceList(int vendor, int device_model, String hard_version,
			String soft_version, int is_check, int rela_dev_type,int access_style_relay_id,int spec_id)
	{
		StringBuffer sb = new StringBuffer();
		// sql修改采用左链接实现，部分地市如江西电信tab_devicetype_info.spec_id可以为空
		String sql = "select a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
				+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name , "
				+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, a.rela_dev_type_id,a.ip_type,is_normal"
				+ "  from tab_devicetype_info_exp a left join tab_bss_dev_port d on a.spec_id = d.id, gw_device_model b,tab_vendor c"
				+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
		sb.append(sql);
		if (vendor != -1)
		{
			sb.append(" and c.vendor_id='" + vendor + "'");
		}
		if (device_model != -1)
		{
			sb.append(" and b.device_model_id='" + device_model + "'");
		}
		if (hard_version != null && !"".equals(hard_version))
		{
			sb.append(" and a.hardwareversion='" + hard_version + "'");
		}
		// 软件版本后模糊匹配
		if (soft_version != null && !"".equals(soft_version))
		{
			sb.append(" and a.softwareversion like '" + soft_version + "%'");
		}
		if (is_check != -2)
		{
			sb.append(" and a.is_check=" + is_check);
		}
		if (rela_dev_type != -1)
		{
			sb.append(" and a.rela_dev_type_id=" + rela_dev_type);
		}
		if (access_style_relay_id != -1)
		{
			sb.append(" and a.access_style_relay_id=" + access_style_relay_id);
		}
		if (spec_id != -1)
		{
			sb.append(" and spec_id = " + spec_id);
		}
		sb.append(" order by a.devicetype_id");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	public List<Map<String,String>> querySpecList()
	{
		String specSql = "select id,spec_name from tab_bss_dev_port";
		List<Map<String,String>> specList = new ArrayList<Map<String,String>>();
		PrepareSQL specPsql = new PrepareSQL(specSql.toString());
		specList = jt.queryForList(specPsql.getSQL());
		return specList;
	}
	
	public String getSpecName(String specId)
	{
		String specSql = "select spec_name from tab_bss_dev_port where id = " + specId ;
		List<Map> specList = new ArrayList<Map>();
		PrepareSQL specPsql = new PrepareSQL(specSql.toString());
		specList = jt.queryForList(specPsql.getSQL());
		String specName = "";
		if (specList.size() > 0)
		{
			specName = (String) specList.get(0).get("spec_name");
		}
		return specName;
	}
	
	
	public String getPortAndType(long deviceTypeId){
	String portSql = "select port_name,port_dir,port_type,port_desc from tab_devicetype_info_expport where devicetype_id="
			+ deviceTypeId;
	List<Map> portList = new ArrayList<Map>();
	PrepareSQL portPsql = new PrepareSQL(portSql.toString());
	portList = jt.queryForList(portPsql.getSQL());
	String name="";
	String dir="";
	String type="";
	String desc="";
	if(portList.size()>0){
		
		for(int i=0;i<portList.size();i++){
			name = name + StringUtil.getStringValue(portList.get(i).get("port_name"))
			+ "#";
			dir = dir + StringUtil.getStringValue(portList.get(i).get("port_dir"))
			+ "#";
			type = type + StringUtil.getStringValue(portList.get(i).get("port_type"))
			+ "#";
			desc = desc + StringUtil.getStringValue(portList.get(i).get("port_desc"))
			+ "#";
		}
		
	}

	String typeSql = "select server_type from tab_devicetype_expservertype where devicetype_id="
		+ deviceTypeId;
	List<Map> typeList = new ArrayList<Map>();
	PrepareSQL typePsql = new PrepareSQL(typeSql.toString());
	typeList = jt.queryForList(typePsql.getSQL());
	String serverType="";
		if(typeList.size()>0){
			for(int i=0;i<typeList.size();i++){
				
				serverType = serverType + StringUtil.getStringValue(typeList.get(i).get("server_type"))
				+ ",";
				
			}
			serverType = serverType.substring(0, serverType.length() - 1);
		}
		String result="";
		result=name+dir+type+desc+"&"+serverType;
		return result;
		
	}
	public Map getAccessType(){
		String sql="select * from gw_access_type ";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		Map<String,String> AccessMap=new HashMap<String,String>();
	list=jt.queryForList(sql);
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
	{
		String infoSql = "select a.devicetype_id, c.vendor_add, b.device_model, a.access_style_relay_id,a.spec_id," 
			    +" a.specversion, a.hardwareversion, a.softwareversion, a.is_check, a.rela_dev_type_id,"
				+ " a.access_style_relay_id,a.ip_type ,is_normal from tab_devicetype_info_exp a, gw_device_model b,tab_vendor c "
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
		String specName = getSpecName(String.valueOf(spec_id));
		infoList.get(0).put("specName", specName);
		String portSql = "select port_name,port_dir,port_type,port_desc from tab_devicetype_info_expport where devicetype_id="
				+ deviceTypeId;
		List<Map> portList = new ArrayList<Map>();
		PrepareSQL portPsql = new PrepareSQL(portSql.toString());
		portList = jt.queryForList(portPsql.getSQL());
		if (portList.size() > 0 && infoList.size() > 0)
		{
			String port = "";
			for (int i = 0; i < portList.size(); i++)
			{
				port = port + StringUtil.getStringValue(portList.get(i).get("port_name"))
						+ ",";
			}
			port = port.substring(0, port.length() - 1);
			infoList.get(0).put("port_name", port);
		}
		String typeSql = "select server_type from tab_devicetype_expservertype where devicetype_id="
				+ deviceTypeId;
		List<Map> typeList = new ArrayList<Map>();
		PrepareSQL typePsql = new PrepareSQL(typeSql.toString());
		typeList = jt.queryForList(typePsql.getSQL());
		if (typeList.size() > 0 && infoList.size() > 0)
		{
			String type = "";
			for (int i = 0; i < typeList.size(); i++)
			{
				String tempType = StringUtil.getStringValue(typeList.get(i).get(
						"server_type"));
				if (tempType.equals("0"))
				{
					type = type + "IMS SIP" + ",";
				}
				if (tempType.equals("1"))
				{
					type = type + "软交换SIP" + ",";
				}
				if (tempType.equals("2"))
				{
					type = type + "H248" + ",";
				}
			}
			logger.warn("@@@@@@@");
			
			type = type.substring(0, type.length() - 1);
			infoList.get(0).put("server_type", type);
		}
		return infoList;
	}

	/**
	 * 根据上行方式ID获取上行方式名称 add by zhangchy 2011-10-25
	 * 
	 * @param accessStyleRelayId
	 * @return
	 */
	public String getTypeNameByTypeId(String accessStyleRelayId)
	{
		StringBuffer sb = new StringBuffer(
				"select distinct type_name from gw_access_type where type_id = "
						+ accessStyleRelayId);
		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map> list = jt.queryForList(psql.getSQL());
		if (list.size() < 1)
		{
			return "";
		}
		if (StringUtil.getStringValue(list.get(0), "type_name") == null)
		{
			return "";
		}
		return StringUtil.getStringValue(list.get(0), "type_name");
	}

	public String getTypeNameList(String typeId)
	{
		StringBuffer sb = new StringBuffer(
				"select distinct type_id, type_name from gw_access_type");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		psql.getSQL();
		return DeviceTypeInfoBIO.createListBox(DataSetBean.getCursor(sb.toString()),
				"type_id", "type_name", typeId, true);
	}

	public int getDeviceListCount(int vendor, int device_model, String hard_version,
			String soft_version, int is_check, int rela_dev_type, int curPage_splitPage,
			int num_splitPage, String startTime, String endTime,int access_style_relay_id,int spec_id)
	{
		logger.debug("getDeviceListCount({},{},{},{},{},{},{},{})", new Object[] {
				vendor, device_model, hard_version, soft_version, is_check,
				rela_dev_type, curPage_splitPage, num_splitPage });
		StringBuffer sb = new StringBuffer();
		String sql = "select count(1) from tab_devicetype_info_exp a, gw_device_model b,tab_vendor c"
				+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id ";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select count(*) from tab_devicetype_info_exp a, gw_device_model b,tab_vendor c"
					+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id ";
		}
		sb.append(sql);
		if (vendor != -1)
		{
			sb.append(" and c.vendor_id='" + vendor + "'");
		}
		if (device_model != -1)
		{
			sb.append(" and b.device_model_id='" + device_model + "'");
		}
		if (hard_version != null && !"".equals(hard_version))
		{
			sb.append(" and a.hardwareversion='" + hard_version + "'");
		}
		// 软件版本后模糊匹配
		if (soft_version != null && !"".equals(soft_version))
		{
			sb.append(" and a.softwareversion like '" + soft_version + "%'");
		}
		if (is_check != -2)
		{
			sb.append(" and a.is_check=" + is_check);
		}
		if (rela_dev_type != -1)
		{
			sb.append(" and a.rela_dev_type_id=" + rela_dev_type);
		}
		if (startTime != null && !"".equals(startTime))
		{
			sb.append(" and a.add_time > " + startTime);
		}
		if (endTime != null && !"".equals(endTime))
		{
			sb.append(" and a.add_time < " + endTime);
		}
		
		if (access_style_relay_id != -1)
		{
			sb.append(" and a.access_style_relay_id=" + access_style_relay_id);
		}
		if (spec_id != -1)
		{
			sb.append(" and spec_id = " + spec_id);
		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public void deleteDevice(int id)
	{
		logger.debug("deleteDevice({})", new Object[] { id });
		String [] sql=new String[3];
		 sql[0] = "delete from tab_devicetype_info_exp where devicetype_id=" + id;
		 sql[1] ="delete from tab_devicetype_expservertype where devicetype_id=" + id;
		 sql[2]="delete from tab_devicetype_info_expport where devicetype_id=" + id;
		 for(int i=0;i<3;i++){
			 PrepareSQL psql = new PrepareSQL(sql[i]);
			 psql.setLong(1,id);
			 sql[i]=psql.getSQL();
		 }
		
		jt.batchUpdate(sql);
	}

	/**
	 * 根据devicetype_id获取该版本编号下有多少设备
	 * 
	 * @param id
	 *            devicetype_id
	 * @return
	 */
	public int getDeviceListCount(int id)
	{
		String sql = "select count(1) from tab_gw_device where devicetype_id=" + id;
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select count(*) from tab_gw_device where devicetype_id=" + id;
		}
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForInt(psql.getSQL());
	}

	/**
	 * 增加设备版本
	 * 
	 * @param vendor
	 *            厂商
	 * @param device_model
	 *            设备型号
	 * @param specversion
	 *            特定版本
	 * @param hard_version
	 *            硬件型号
	 * @param soft_version
	 *            软件型号
	 * @param is_check
	 *            是否审核
	 * @param rela_dev_type
	 *            设备类型
	 * @return
	 */
	public Object[] addDevTypeInfo(int vendor, int device_model, String specversion,
			String hard_version, String soft_version, int is_check, int rela_dev_type,
			String typeId,String  ipType,long spec_id)
	{
		String sql = "insert into tab_devicetype_info_exp(devicetype_id,vendor_id,device_model_id,specversion,hardwareversion,softwareversion,add_time,is_check,rela_dev_type_id,access_style_relay_id,ip_type,spec_id) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		long max_id = DataSetBean.getMaxId("tab_devicetype_info_exp", "devicetype_id");
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

	public String batchImportDevice(List<String> elsData)
	{
		String msg = "设备版本入库失败";
		ArrayList<String> tempList = new ArrayList<String>();
		long max_id = DataSetBean.getMaxId("tab_devicetype_info_exp", "devicetype_id");

		for (String data : elsData)
		{
			String[] dataAry = data.split(";");
			String sql = "insert into tab_devicetype_info_exp(devicetype_id,vendor_id,device_model_id,specversion,hardwareversion,softwareversion,add_time,is_check,rela_dev_type_id,access_style_relay_id,spec_id) values(?,?,?,?,?,?,?,?,?,?,?)";
			PrepareSQL psql = new PrepareSQL(sql.toString());
			psql.setLong(1, max_id);
			psql.setString(2, dataAry[0]);
			psql.setString(3, dataAry[1]);
			psql.setString(4, dataAry[2]);

			psql.setString(5, dataAry[3]);
			psql.setString(6, dataAry[4]);
			psql.setLong(7, TimeUtil.getCurrentTime());
			psql.setInt(8, StringUtil.getIntegerValue(dataAry[5]));
			psql.setInt(9, StringUtil.getIntegerValue(dataAry[6]));
			psql.setInt(10, StringUtil.getIntegerValue(dataAry[7]));
			psql.setInt(11, StringUtil.getIntegerValue(dataAry[8]));
			tempList.add(psql.getSQL());
			max_id++;
		}
		int[] ier = doBatch(tempList);
		if (ier != null && ier.length > 0) {
			msg = "设备版本入库成功";
		} else {
			msg = "设备版本入库失败";
		}
		logger.warn(msg);
		return msg;
	}
	
	/**
	 * 修改设备版本
	 * 
	 * @param deviceTypeId
	 *            ID
	 * @param vendor
	 *            厂商
	 * @param device_model
	 *            设备型号
	 * @param specversion
	 *            特定版本
	 * @param hard_version
	 *            硬件型号
	 * @param soft_version
	 *            软件型号
	 * @param is_check
	 *            是否审核
	 * @param rela_dev_type
	 *            设备类型
	 * @return 操作结果
	 */
	public int updateDevTypeInfo(long deviceTypeId, int vendor, int device_model,
			String specversion, String hard_version, String soft_version, int is_check,
			int rela_dev_type, String typeId,String ipType,String isNormal,long spec_id)
	{
		// 更新设备版本表
		String sql = "update tab_devicetype_info_exp set is_check=?,rela_dev_type_id=?, " +
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
		
		
		String sql2 = "update tab_gw_device set device_type=(select type_name from gw_dev_type where type_id=?) where devicetype_id=?";
		PrepareSQL psql2 = new PrepareSQL(sql2);
//		if (rela_dev_type == 1)
//		{
			psql2.setString(1, StringUtil.getStringValue(rela_dev_type));
			
//			
//		}
//		
//		else{
//			
//			if (rela_dev_type == 2)
//			{
//				psql2.setString(1, "e8-c");
//			}
//			else
//			{
//				psql2.setString(1, "");
//			}
//		}
		
		psql2.setLong(2, deviceTypeId);
		return jt.batchUpdate(new String[] { psql.getSQL(), psql2.getSQL() })[0];
	}

	public void updateIsCheck(long id)
	{
		logger.debug("updateIsCheck({})", new Object[] { id });
		String sql = "update tab_devicetype_info_exp set is_check=1 where devicetype_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, id);
		jt.update(psql.getSQL());
	}

	/**
	 * 添加端口和设备协议
	 * 
	 * @param
	 * @return 操作结果
	 */
	public int addPortAddServertype(String portInfo, long acc_oid, String servertype)
	{
		long max_id = DataSetBean.getMaxId("tab_devicetype_info_exp", "devicetype_id");
		if (!servertype.equals(""))
		{
			String[] allServertype = servertype.split("@");
			String[] sql = new String[allServertype.length];
			for (int i = 0; i < allServertype.length; i++)
			{
				sql[i] = "insert into tab_devicetype_expservertype (devicetype_id,server_type,time,acc_oid) values(?,?,?,?)";
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
				sql[i] = "insert into tab_devicetype_info_expport(devicetype_id,port_name,port_dir,port_type,port_desc,add_time,acc_oid) values(?,?,?,?,?,?,?)";
				PrepareSQL psq = new PrepareSQL(sql[i]);
				psq.setLong(1, max_id - 1);
				psq.setString(2, port[0]);
				psq.setString(3, port[1]);
				psq.setInt(4, Integer.parseInt(port[2]));
				if (port.length > 3)
				{
					psq.setString(5, port[3]);
				}
				else
				{
					psq.setString(5, "");
				}
				psq.setLong(6, System.currentTimeMillis() / 1000);
				psq.setLong(7, acc_oid);
				sql[i] = psq.getSQL();
			}
			return jt.batchUpdate(sql)[0];
		}
		else
		{
			return 0;
		}
	}

	/**
	 * 修改端口和设备协议
	 * 
	 * @param
	 * @return 操作结果
	 */
	public int updatePortAddServertype(long deviceTypeId, String portInfo, long acc_oid,
			String servertype)
	{
		//long max_id = DataSetBean.getMaxId("tab_devicetype_info", "devicetype_id") - 1;
		String delSqlForPort = "delete  from tab_devicetype_info_expport  where devicetype_id="+ deviceTypeId;
		String delSqlForType = "delete  from tab_devicetype_expservertype   where devicetype_id="+ deviceTypeId;
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
				sql[i] = "insert into tab_devicetype_expservertype (devicetype_id,server_type,time,acc_oid) values(?,?,?,?)";
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
				sql[i] = "insert into tab_devicetype_info_expport(devicetype_id,port_name,port_dir,port_type,port_desc,add_time,acc_oid) values(?,?,?,?,?,?,?)";
				PrepareSQL psql = new PrepareSQL(sql[i]);
				psql.setLong(1, deviceTypeId);
				psql.setString(2, port[0]);
				psql.setString(3, port[1]);
				psql.setInt(4, Integer.parseInt(port[2]));
				
				if (port.length > 3)
				{
					psql.setString(5, port[3]);
				}
				else
				{
					psql.setString(5, "");
				}
				psql.setLong(6, System.currentTimeMillis() / 1000);
				psql.setLong(7, acc_oid);
				sql[i] = psql.getSQL();
			}
			return jt.batchUpdate(sql)[0];
		}
		else
		{
			return 0;
		}
	}

 /**
  * 
  * @param deviceModel
  * @return
  */
	 public int isNormalVersion(int deviceModel) {
		StringBuffer sb = new StringBuffer(
		"select count(1)  from tab_devicetype_info_exp a where a.is_normal = 1 and a.device_model_id='"+deviceModel+"'");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		return jt.queryForInt(psql.getSQL());
	 }
	 
	 /**
	  * 查询设备类型
	  * 
	  */
	 public List<Map<String,String>> getGwDevType(){
		 PrepareSQL psql = new PrepareSQL("select type_id,type_name from gw_dev_type");
		 return  jt.queryForList(psql.getSQL());
	 }
}
