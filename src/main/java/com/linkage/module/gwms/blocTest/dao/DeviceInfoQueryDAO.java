
package com.linkage.module.gwms.blocTest.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-7-21 下午04:37:12
 * @category resource.dao
 * @copyright 南京联创科技 网管科技部
 */
public class DeviceInfoQueryDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(DeviceInfoQueryDAO.class);

	// jdbc
	// private JdbcTemplateExtend jt;
	/**
	 * @param username
	 *            逻辑ID
	 * @param user
	 *            宽带帐号
	 * @param telephone
	 *            电话号码
	 * @return 查询的所有数据
	 */
	public List<Map> queryDeviceList(String username, String user, String telephone,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryDeviceList({},{},{},{},{},{},{},{})", new Object[] { username,
				user, telephone, curPage_splitPage, num_splitPage });
		StringBuffer sb = new StringBuffer();
		/**
		 if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		}
		*/
		String sql = "select distinct d.devicetype_id,d.device_serialnumber,d.oui, v.vendor_name as 'vendor_name',m.device_model as 'device_model',t.hardwareversion as 'hardwareversion',"
				+ "t.softwareversion as 'softwareversion' ,dc.device_type as 'device_type', dc.is_card_apart as 'is_card_apart',dc.wan_name as'wan_name',"
				+ "dc.wan_num as 'wan_num',dc.wan_can as 'wan_can',dc.lan_name as 'lan_name',dc.lan_num as'lan_num',dc.lan_can as'lan_can',"
				+ "dc.wlan_name as 'wlan_name',dc.wlan_num as'wlan_num',dc.wlan_can as'wlan_can',dc.voip_name as'voip_name',dc.voip_num as 'voip_num',"
				+ "dc.voip_can as 'voip_can',dc.voip_protocol as'voip_protocol',dc.wireless_type as'wireless_type',dc.wireless_num as'wireless_num',dc.wireless_size as 'wireless_size' "
				+ " from tab_gw_device d"
				+ " left join  tab_hgwcustomer c  on c.device_id = d.device_id"
				+ " left join tab_vendor v             on  v.vendor_id=d.vendor_id"
				+ " left join gw_device_model m        on m.device_model_id= d.device_model_id"
				+ " left join tab_devicetype_info  t    on t.devicetype_id=d.devicetype_id"
				+ " left join hgwcust_serv_info cs   on  cs.user_id=c.user_id"
				+ " left join tab_devicetype_config_info dc     on  dc.devicetype_id=d.devicetype_id where 1=1";
		sb.append(sql);
		if (username != null && !"".equals(username))
		{
			sb.append(" and c.username='" + username + "'");
		}
		if (user != null && !"".equals(user))
		{
			sb.append(" and cs.username='" + user + "'");
		}
		if (telephone != null && !"".equals(telephone))
		{
			sb.append(" and c.linkphone='" + telephone + "'");
		}
		
		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				int type = rs.getInt("device_type");
				map.put("device_type", "");
				if (type == 1)
				{
					map.put("device_type", "标准型e8-B网关");
				}
				if (type == 2)
				{
					map.put("device_type", "普及型e8-B网关");
				}
				if (type == 3)
				{
					map.put("device_type", "标准型e8-C网关");
				}
				if (type == 4)
				{
					map.put("device_type", "AP外置型e8-C网关");
				}
				if (type == 5)
				{
					map.put("device_type", "其他ITMS管理的终端类型");
				}
				int is_card_apart = rs.getInt("is_card_apart");
				if (is_card_apart == 1)
				{
					map.put("is_card_apart", "是");
				}
				else
				{
					map.put("is_card_apart", "否");
				}
				if (rs.getString("wan_name") != null
						&& !"".equals(rs.getString("wan_name")))
				{
					map.put("wan_name", rs.getString("wan_name"));
					map.put("wan_num", rs.getString("wan_num"));
					map.put("wan_can", rs.getString("wan_can"));
				}
				if (rs.getString("lan_name") != null
						&& !"".equals(rs.getString("wan_name")))
				{
					map.put("lan_name", rs.getString("lan_name"));
					map.put("lan_num", rs.getString("lan_num"));
					map.put("lan_can", rs.getString("lan_can"));
				}
				if (rs.getString("wlan_name") != null
						&& !"".equals(rs.getString("wlan_name")))
				{
					map.put("wlan_name", rs.getString("wlan_name"));
					map.put("wlan_num", rs.getString("wlan_num"));
					map.put("wlan_can", rs.getString("wlan_can"));
				}
				if (rs.getString("voip_name") != null
						&& !"".equals(rs.getString("voip_name")))
				{
					map.put("voip_name", rs.getString("voip_name"));
					map.put("voip_num", rs.getString("voip_num"));
					map.put("voip_can", rs.getString("voip_can"));
					int voip_protocol = rs.getInt("voip_protocol");
					if (voip_protocol == 0)
					{
						map.put("voip_protocol", "IMS SIP\\软交换SIP\\H.248");
					}
					if (voip_protocol == 1)
					{
						map.put("voip_protocol", "IMS SIP");
					}
					if (voip_protocol == 2)
					{
						map.put("voip_protocol", "软交换");
					}
					if (voip_protocol == 3)
					{
						map.put("voip_protocol", "H.248");
					}
					if (!"".equals(rs.getInt("wireless_type")))
					{
						if (rs.getInt("wireless_type") == 0)
						{
							map.put("wireless_type", "内置");
						}
						else
						{
							map.put("wireless_type", "外置");
						}
					}
					if (!"".equals(rs.getInt("wireless_num")))
					{
						map.put("wireless_num", rs.getInt("wireless_num") + "");
					}
					if ("".equals(rs.getInt("wireless_size")))
					{
						map.put("wireless_size", rs.getInt("wireless_size") + "");
					}
				}
				// map.put("bind_port", rs.getString("bind_port"));
				// map.put("serv_type_name", rs.getString("serv_type_name"));
				// map.put("serv_type_name", rs.getString("serv_type_name"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("oui", rs.getString("oui"));
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("device_model", rs.getString("device_model"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				return map;
			}
		});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List getHgwcustServInfo(String username, String user, String telephone)
	{
		StringBuffer sb = new StringBuffer();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		String sql = "select gs.serv_type_name as 'serv_type_name',cs.bind_port as 'bind_port',c.user_id from tab_hgwcustomer c"
				+ " left join hgwcust_serv_info cs   on  cs.user_id=c.user_id"
				+ " left join tab_gw_serv_type gs  on gs.serv_type_id=cs.serv_type_id where 1=1";
		sb.append(sql);
		if (username != null && !"".equals(username))
		{
			sb.append(" and c.username='" + username + "'");
		}
		if (user != null && !"".equals(user))
		{
			sb.append(" and cs.username='" + user + "'");
		}
		if (telephone != null && !"".equals(telephone))
		{
			sb.append(" and c.linkphone='" + telephone + "'");
		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map<String,String>> list =  jt.queryForList(psql.getSQL());
		for (Map<String, String> tmap : list)
		{
			if("VoIP".equals(tmap.get("serv_type_name"))){
				tmap.put("bind_port", getVoipBindPort(StringUtil.getStringValue(tmap.get("user_id"))));
			}
		}
		return list;
	}

	private String getVoipBindPort(String userId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select line_id from tab_voip_serv_param voip where user_id=").append(userId);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map<String,String>> list =  jt.queryForList(psql.getSQL());
		String bind_port = "";
		for (Map<String, String> map : list)
		{
			bind_port = "line"+ StringUtil.getStringValue(map.get("line_id"))+",";
		}
		bind_port = bind_port.substring(0,bind_port.length()-1);
		return bind_port;
	}

	public int getDeviceListCount(String username, String user, String telephone,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getDeviceListCount({},{},{},{},{},{},{},{})", new Object[] {
				username, user, telephone, curPage_splitPage, num_splitPage });
		StringBuffer sb = new StringBuffer();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		String sql = "select count(distinct d.devicetype_id)"
				+ "from tab_gw_device d"
				+ " left join  tab_hgwcustomer c  on c.device_id = d.device_id"
				+ " left join tab_vendor v             on  v.vendor_id=d.vendor_id"
				+ " left join gw_device_model m        on m.device_model_id= d.device_model_id"
				+ " left join tab_devicetype_info  t    on t.devicetype_id=d.devicetype_id"
				+ " left join hgwcust_serv_info cs        on  cs.user_id=c.user_id"
				+ " left join tab_gw_serv_type gs         on gs.serv_type_id=cs.serv_type_id"
				+ " left join tab_devicetype_config_info dc     on  dc.devicetype_id=d.devicetype_id where 1=1";
		sb.append(sql);
		if (username != null && !"".equals(username))
		{
			sb.append(" and c.username='" + username + "'");
		}
		if (user != null && !"".equals(user))
		{
			sb.append(" and cs.username='" + user + "'");
		}
		if (telephone != null && !"".equals(telephone))
		{
			sb.append(" and c.linkphone='" + telephone + "'");
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

	// 查询设备版本配置信息
	public List<Map> queryList(int vendor, int device_model, String hard_version,
			String soft_version, int is_check, int rela_dev_type, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("queryDeviceList({},{},{},{},{},{},{},{})", new Object[] { vendor,
				device_model, hard_version, soft_version, is_check, rela_dev_type,
				curPage_splitPage, num_splitPage });
		StringBuffer sb = new StringBuffer();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		String sql = "select devicetype_id, a.vendor_id, a.device_model_id, vendor_add," +
				"device_model, specversion, hardwareversion, softwareversion, is_check, rela_dev_type_id " +
				"from tab_devicetype_info a, gw_device_model b,tab_vendor c"
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
		if (is_check != -1)
		{
			sb.append(" and a.is_check=" + is_check);
		}
		if (rela_dev_type != -1)
		{
			sb.append(" and a.rela_dev_type_id=" + rela_dev_type);
		}
		sb.append(" order by vendor_add desc");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		logger.info("sql===" + psql.getSQL());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("devicetype_id", rs.getString("devicetype_id"));
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
				map.put("specversion", rs.getString("specversion"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("is_check", rs.getString("is_check"));
				map.put("rela_dev_type_id", rs.getString("rela_dev_type_id"));
				map.put("vendor_id", rs.getString("vendor_id"));
				map.put("device_model_id", rs.getString("device_model_id"));
				return map;
			}
		});
		return list;
	}

	public int getListCount(int vendor, int device_model, String hard_version,
			String soft_version, int is_check, int rela_dev_type, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("getDeviceListCount({},{},{},{},{},{},{},{})", new Object[] {
				vendor, device_model, hard_version, soft_version, is_check,
				rela_dev_type, curPage_splitPage, num_splitPage });
		
		int total=0;
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL)
		{
			psql.append("select a.vendor_id from tab_devicetype_info a, gw_device_model b ");
			psql.append("where a.device_model_id=b.device_model_id");
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
			if (is_check != -1){
				psql.append(" and a.is_check=" + is_check);
			}
			if (rela_dev_type != -1){
				psql.append(" and a.rela_dev_type_id=" + rela_dev_type);
			}
			
			List l=jt.queryForList(psql.getSQL());
			if(l!=null && !l.isEmpty()){
				total=l.size();
				List<String> vendorids=getVendorIds();
				for(int i=0;i<l.size();i++){
					if(!vendorids.contains(StringUtil.getStringValue((Map)l.get(i),"vendor_id")) 
							|| (vendor!=-1 && vendor!=StringUtil.getIntValue((Map)l.get(i),"vendor_id"))){
						total--;
					}
				}
			}
		}
		else
		{
			psql.append("select count(1) from tab_devicetype_info a, gw_device_model b,tab_vendor c ");
			psql.append("where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id");
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
			if (is_check != -1){
				psql.append(" and a.is_check=" + is_check);
			}
			if (rela_dev_type != -1){
				psql.append(" and a.rela_dev_type_id=" + rela_dev_type);
			}
			total = jt.queryForInt(psql.getSQL());
		}
		
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 查询指定设备的版本配置信息
	 * 
	 * @param id
	 * @return
	 */
	public Map queryConfig(String devicetype_id)
	{
		logger.debug("getCustomerInfo({})", devicetype_id);
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select is_card_apart,access_type,device_type,wan_name,wan_num,wan_can,");
			psql.append("lan_name,lan_num,lan_can,voip_name,voip_num,voip_can,voip_protocol,");
			psql.append("wlan_name,wlan_num,wlan_can,wireless_num,wireless_size,wireless_type ");
		}else{
			psql.append("select * ");
		}
		psql.append("from tab_devicetype_config_info where devicetype_id="+devicetype_id);
		List list = jt.queryForList(psql.getSQL());
		if (list != null && list.size() > 0)
		{
			logger.info("into map");
			return (Map) list.get(0);
		}
		return null;
	}

	public List<Map> queryDeviceTypeConfigList(int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryDeviceTypeConfigList()", new Object[] { curPage_splitPage,num_splitPage });
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select access_type,device_type,wan_name,wan_num,wan_can,lan_name,lan_num,lan_can,");
			psql.append("wlan_name,wlan_num,wlan_can,voip_name,voip_num,voip_can,voip_protocol,");
			psql.append("wireless_type,wireless_num,wireless_size ");
		}else{
			psql.append("select * ");
		}
		psql.append("from tab_devicetype_config_info");
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				int access_type = rs.getInt("access_type");
				map.put("access_type", "");
				if (access_type == 1){
					map.put("access_type", "DSL");
				}else if (access_type == 2){
					map.put("access_type", "Ethernet");
				}else if (access_type == 3){
					map.put("access_type", "PON");
				}else if (access_type == 4){
					map.put("access_type", "GPON");
				}
				int type = rs.getInt("device_type");
				map.put("device_type", "");
				if (type == 1){
					map.put("device_type", "标准型e8-B网关");
				}else if (type == 2){
					map.put("device_type", "普及型e8-B网关");
				}else if (type == 3){
					map.put("device_type", "标准型e8-C网关");
				}else if (type == 4){
					map.put("device_type", "AP外置型e8-C网关");
				}else if (type == 5){
					map.put("device_type", "其他ITMS管理的终端类型");
				}
				int is_card_apart = rs.getInt("is_card_apart");
				map.put("access_type", "");
				if (is_card_apart == 1)
				{
					map.put("is_card_apart", "是");
				}
				else
				{
					map.put("is_card_apart", "否");
				}
				if (rs.getString("wan_name") != null
						&& !"".equals(rs.getString("wan_name")))
				{
					map.put("wan_name", rs.getString("wan_name"));
					map.put("wan_num", rs.getString("wan_num"));
					map.put("wan_can", rs.getString("wan_can"));
				}
				if (rs.getString("lan_name") != null
						&& !"".equals(rs.getString("wan_name")))
				{
					map.put("lan_name", rs.getString("lan_name"));
					map.put("lan_num", rs.getString("lan_num"));
					map.put("lan_can", rs.getString("lan_can"));
				}
				if (rs.getString("wlan_name") != null
						&& !"".equals(rs.getString("wlan_name")))
				{
					map.put("wlan_name", rs.getString("wlan_name"));
					map.put("wlan_num", rs.getString("wlan_num"));
					map.put("wlan_can", rs.getString("wlan_can"));
				}
				if (rs.getString("voip_name") != null
						&& !"".equals(rs.getString("voip_name")))
				{
					map.put("voip_name", rs.getString("voip_name"));
					map.put("voip_num", rs.getString("voip_num"));
					map.put("voip_can", rs.getString("voip_can"));
					int voip_protocol = rs.getInt("voip_protocol");
					if (voip_protocol == 0)
					{
						map.put("voip_protocol", "IMS SIP\\软交换SIP\\H.248");
					}
					if (voip_protocol == 1)
					{
						map.put("voip_protocol", "IMS SIP");
					}
					if (voip_protocol == 2)
					{
						map.put("voip_protocol", "软交换");
					}
					if (voip_protocol == 3)
					{
						map.put("voip_protocol", "H.248");
					}
				}
				if (!"".equals(rs.getInt("wireless_type")))
				{
					if (rs.getInt("wireless_type") == 0){
						map.put("wireless_type", "内置");
					}else{
						map.put("wireless_type", "外置");
					}
				}
				if (!"".equals(rs.getInt("wireless_num"))){
					map.put("wireless_num", rs.getInt("wireless_num") + "");
				}
				if ("".equals(rs.getInt("wireless_size"))){
					map.put("wireless_size", rs.getInt("wireless_size") + "");
				}
				return map;
			}
		});
		return list;
	}

	public int getDeviceTypeConfigListCount(int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getDeviceTypeConfigListCount()", new Object[] { curPage_splitPage,num_splitPage });
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select count(*) from tab_devicetype_config_info");
		}else{
			psql.append("select count(1) from tab_devicetype_config_info");
		}
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	// 添加设备版本配置信息
	public int addDeviceTypeConfig(String devicetype_id, int accessType, int deviceType,
			int isCardApart, String wan_name, int wan_num, String wan_can,
			String lan_name, int lan_num, String lan_can, String wlan_name, int wlan_num,
			String wlan_can, String voip_name, int voip_num, String voip_can,
			int voipProtocol, int wirelessType, int wireless_num, int wireless_size)
	{
		// 生成入库语句
		String s = "select count(*) from tab_devicetype_config_info where devicetype_id ="
				+ Integer.parseInt(devicetype_id);
		String updateSql = "update tab_devicetype_config_info set access_type="
				+ accessType + ",device_type=" + deviceType + ",is_card_apart="
				+ isCardApart + ",wan_name='" + wan_name + "',wan_num=" + wan_num
				+ ",wan_can='" + wan_can + "',lan_name='" + lan_name + "',lan_num="
				+ lan_num + ",lan_can='" + lan_can + "',wlan_name='" + wlan_name
				+ "',wlan_num=" + wlan_num + ",wlan_can='" + wlan_can + "',voip_name='"
				+ voip_name + "',voip_num=" + voip_num + ",voip_can='" + voip_can
				+ "',voip_protocol=" + voipProtocol + ",wireless_type=" + wirelessType
				+ ",wireless_num=" + wireless_num + ",wireless_size=" + wireless_size
				+ " where devicetype_id=" + Integer.parseInt(devicetype_id) + "";
		String addSql = "insert into tab_devicetype_config_info (devicetype_id,access_type,device_type,is_card_apart,wan_name,"
				+ "wan_num,wan_can,lan_name,lan_num,lan_can,wlan_name,wlan_num,wlan_can,voip_name,voip_num,voip_can,"
				+ "voip_protocol,wireless_type,wireless_num,wireless_size)" + "values ("
				+ Integer.parseInt(devicetype_id)
				+ ","+ accessType + ","+ deviceType
				+ ","+ isCardApart + ",'"+ wan_name
				+ "',"+ wan_num + ",'"+ wan_can
				+ "','"+ lan_name + "',"+ lan_num
				+ ",'"+ lan_can + "','"+ wlan_name
				+ "',"+ wlan_num + ",'"+ wlan_can
				+ "','"+ voip_name + "',"+ voip_num
				+ ",'"+ voip_can + "',"+ voipProtocol
				+ ","+ wirelessType + "," + wireless_num 
				+ "," + wireless_size + ")";
		PrepareSQL psql = new PrepareSQL(s);
		int result = jt.queryForInt(psql.getSQL());
		if (result > 0)
		{
			PrepareSQL update = new PrepareSQL(updateSql);
			return jt.update(update.getSQL());
		}
		else
		{
			PrepareSQL add = new PrepareSQL(addSql);
			logger.info(add.getSQL());
			return jt.update(addSql);
		}
	}
	
	/**
	 * 获取所有厂商
	 */
	private List<String> getVendorIds()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select vendor_id from tab_vendor order by vendor_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		List<String> l=new ArrayList<String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				l.add(StringUtil.getStringValue(m,"vendor_id"));
			}
		}
		
		return l;
	}
	
}
