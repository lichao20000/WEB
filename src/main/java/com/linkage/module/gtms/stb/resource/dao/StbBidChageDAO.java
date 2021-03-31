/**
 *
 */
package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.DataSourceContextHolder;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.litms.common.util.JdbcTemplateExtend;

/**
 *
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年6月11日
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
@SuppressWarnings("rawtypes")
public class StbBidChageDAO extends SuperDAO {
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(StbBidChageDAO.class);

	// 查询多个最多只能查询这么多条
	private int maxNum = 10000;

	/**
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}

	private void setDataSourceType(String isRealtimeQuery, String key) {
		DataSourceContextHolder.clearDBType();
		if ("false".equals(isRealtimeQuery)) {
			String type = null;
			type = DataSourceTypeCfgPropertiesManager.getInstance()
					.getConfigItem(key + "ChangeDB");
			if (!StringUtil.IsEmpty(type)) {
				logger.warn("类：" + this.getClass().getName() + "的数据源类型配置为："
						+ type);
				DataSourceContextHolder.setDBType(type);
			}
		}
	}

	/**
	 * 模拟匹配提示
	 */
	public List getDeviceSn(String cityId, String searchTxt,
			String gwShare_queryField) {

		PrepareSQL pSQL = new PrepareSQL();

        List<Map> list = null;

        if (null != searchTxt) {
			if ("username".equals(gwShare_queryField)) {
				String tableName = "tab_egwcustomer";
				if (LipossGlobals.IsITMS()) {
					tableName = "tab_hgwcustomer";
				}
				if(DBUtil.GetDB() == 3)
				{// mysql
					pSQL.setSQL("select ");
				}else{
					pSQL.setSQL("select top 5 ");
				}
				pSQL.append(" b.device_serialnumber, a.username as username from "+ tableName +" a,tab_gw_device b where a.device_id=b.device_id and a.user_state = '1' ");
				pSQL.append(" and a.username like '%");
				pSQL.append(searchTxt);
				pSQL.append("%' ");
				if (null != cityId && !"00".equals(cityId)) {
					ArrayList cityArray = CityDAO
							.getAllNextCityIdsByCityPid(cityId);
					pSQL.append(" and a.city_id in ('"
							+ StringUtils.weave(cityArray, "','") + "')");
					cityArray = null;
				}
				pSQL.append(" order by b.complete_time");
			} else {
				if(DBUtil.GetDB() == 3)
				{// mysql
					pSQL.setSQL("select ");
				}else{
					pSQL.setSQL("select top 5 ");
				}
				pSQL.append(" device_serialnumber, loopback_ip as username from tab_gw_device where device_status =1 ");
				if ("deviceSn".equals(gwShare_queryField)) {
					pSQL.append(" and device_serialnumber like '%");
					pSQL.append(searchTxt);
					pSQL.append("%' ");
				} else if ("deviceIp".equals(gwShare_queryField)) {
					pSQL.append(" and loopback_ip like '%");
					pSQL.append(searchTxt);
					pSQL.append("%' ");
				} else {
					pSQL.append(" and ( device_serialnumber like '%");
					pSQL.append(searchTxt);
					pSQL.append("%' or loopback_ip like '%");
					pSQL.append(searchTxt);
					pSQL.append("%') ");
				}
				if (null != cityId && !"00".equals(cityId)) {
					ArrayList cityArray = CityDAO
							.getAllNextCityIdsByCityPid(cityId);
					pSQL.append(" and a.city_id in ('"
							+ StringUtils.weave(cityArray, "','") + "')");
					cityArray = null;
				}
				pSQL.append(" order by complete_time");
			}
			if(DBUtil.GetDB() == 3)
			{// mysql
				pSQL.setSQL(" limit 5 ");
			}

            list = jt.queryForList(pSQL.getSQL());
			if(list == null || list.isEmpty()){
			    return null;
            }
            for (Map map : list) {
                String device_serialnumber = StringUtil.getStringValue(map, "device_serialnumber");
                String username = StringUtil.getStringValue(map, "username");
                map.put("device_sn", device_serialnumber + "|" + username);
            }
		}
        return list;
	}

	public int getTmpList() {
		logger.debug("gwDevicQueryDAO({})");
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from tab_softwareup_tmp");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		return jt.queryForInt(psql.getSQL());
	}

	public int[] insertImportDataTmp(List<String> dataList, int type) {
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = new PrepareSQL();
		String sql = "";
		for (int i = 0; i < dataList.size(); i++) {
			sql = "insert into tab_softwareup_tmp values ( '" + dataList.get(i)
					+ "' , " + type + ")";
			psql.setSQL(sql);
			sqlList.add(psql.getSQL());
		}
		return DataSetBean.doBatch(sqlList);
	}

	public void insertTmp(String fileName, List<String> dataList,
			String importQueryField) {
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		if ("username".equals(importQueryField)
				|| "kdusername".equals(importQueryField)) {
			for (int i = 0; i < dataList.size(); i++) {
				psql = new PrepareSQL();
				psql.append("insert into stb_tab_tmp_accounts "
						+ "(filename,username)" + " values ('" + fileName
						+ "','" + dataList.get(i) + "')");
				sqlList.add(psql.getSQL());
			}
		}
		int res;
		if (LipossGlobals.inArea(Global.JSDX)) {
			res = DBOperation.executeUpdate(sqlList, "proxool.xml-report");
		} else {
			res = DBOperation.executeUpdate(sqlList);
		}
	}

	public void insertTmpForDoubleParam(String fileName, List<String> dataList,
			List<String> paramList, String importQueryField) {
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		if ("username".equals(importQueryField)) {
			for (int i = 0; i < dataList.size(); i++) {
				psql = new PrepareSQL();
				psql.append("insert into tab_seniorquery_tmp"
						+ "(filename,username,paramvalue)" + " values ('"
						+ fileName + "','" + dataList.get(i) + "','"
						+ paramList.get(i) + "')");
				sqlList.add(psql.getSQL());
			}
		} else {
			for (int i = 0; i < dataList.size(); i++) {
				psql = new PrepareSQL();
				psql.append("insert into tab_seniorquery_tmp"
						+ "(filename,devicesn,paramvalue)" + " values ('"
						+ fileName + "','" + dataList.get(i) + "','"
						+ paramList.get(i) + "')");
				sqlList.add(psql.getSQL());
			}
		}
		int res;
		if (LipossGlobals.inArea(Global.JSDX)) {
			res = DBOperation.executeUpdate(sqlList, "proxool.xml-report");
		} else {
			res = DBOperation.executeUpdate(sqlList);
		}
	}

	public void insertTmp4Wireless(String fileName, List<String> dataList,
			String importQueryField, String currencyType) {
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		if ("username".equals(importQueryField)) {
			if ("1".equals(currencyType)) {
				for (int i = 0; i < dataList.size(); i++) {
					String tmps = "'" + dataList.get(i).replace(",", "','")
							+ "'";
					psql = new PrepareSQL();
					psql.append("insert into tab_seniorquery_tmp"
							+ "(filename,username,ssid,vlanid,priority,channel,wlanport)"
							+ " values ('" + fileName + "'," + tmps + ")");
					sqlList.add(psql.getSQL());
				}
			} else if ("0".equals(currencyType)) {
				for (int i = 0; i < dataList.size(); i++) {
					String tmps = "'" + dataList.get(i).replace(",", "','")
							+ "'";
					psql = new PrepareSQL();
					psql.append("insert into tab_seniorquery_tmp"
							+ "(filename,username,ssid,vlanid,priority,channel)"
							+ " values ('" + fileName + "'," + tmps + ")");
					sqlList.add(psql.getSQL());
				}
			}

		} else {
			if ("1".equals(currencyType)) {
				for (int i = 0; i < dataList.size(); i++) {
					String tmps = "'" + dataList.get(i).replace(",", "','")
							+ "'";
					psql = new PrepareSQL();
					psql.append("insert into tab_seniorquery_tmp"
							+ "(filename,devicesn,ssid,vlanid,priority,channel,wlanport)"
							+ " values ('" + fileName + "'," + tmps + ")");
					sqlList.add(psql.getSQL());
				}
			} else if ("0".equals(currencyType)) {
				for (int i = 0; i < dataList.size(); i++) {
					String tmps = "'" + dataList.get(i).replace(",", "','")
							+ "'";
					psql = new PrepareSQL();
					psql.append("insert into tab_seniorquery_tmp"
							+ "(filename,devicesn,ssid,vlanid,priority,channel)"
							+ " values ('" + fileName + "'," + tmps + ")");
					sqlList.add(psql.getSQL());
				}
			} else {
				logger.warn("GwDeviceQueryDAO==>insertTmp4Wireless()devicesn==currencyType++null");
			}

		}
		int res;
		if (LipossGlobals.inArea(Global.JSDX)) {
			res = DBOperation.executeUpdate(sqlList, "proxool.xml-report");
		} else {
			res = DBOperation.executeUpdate(sqlList);
		}
	}

	public List<Map> newDeviceQueryInfo4Simple(String mac, String username, int curPage_splitPage,
			int num_splitPage) {
		StringBuffer sql = new StringBuffer();
		// TODO wait (more table related)
		sql.append(" select b.oper_type,b.username,b.old_devsn,b.new_devsn,b.new_mac,b.old_mac,b.unbinddate,b.binddate,"
				+ " c.vendor_name as old_vendor_name,d.device_model as old_device_model,"
				+ " e.hardwareversion as old_hardwareversion,e.softwareversion as old_softwareversion "
				+ " from stb_tab_dev_bindrecord b,"
				+ " stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e where"
				+ " b.old_vendor_id=c.vendor_id and b.old_model_id=d.device_model_id "
				+ " and b.old_devicetype_id=e.devicetype_id  ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		if (null != mac && !"null".equals(mac)
				&& !"".equals(mac) && !"-1".equals(mac)) {
			//psql.appendAndString("b.new_mac", PrepareSQL.EQUEAL, mac);
			psql.append(" and (b.new_mac='"+mac+"' or b.old_mac='"+mac+"') ");
		}
		if (null != username && !"null".equals(username)
				&& !"".equals(username) && !"-1".equals(username)) {
			psql.appendAndString("b.username", PrepareSQL.EQUEAL,
					username);
		}

		List<Map> list = new ArrayList<Map>();
		list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("old_vendor_name", StringUtil.getStringValue(rs
						.getString("old_vendor_name")));
				map.put("old_device_model", StringUtil.getStringValue(rs
						.getString("old_device_model")));
				map.put("old_hardwareversion", StringUtil.getStringValue(rs
						.getString("old_hardwareversion")));
				map.put("old_devsn",
						StringUtil.getStringValue(rs.getString("old_devsn")));
				map.put("new_devsn",
						StringUtil.getStringValue(rs.getString("new_devsn")));
				map.put("old_softwareversion", StringUtil.getStringValue(rs
						.getString("old_softwareversion")));
				//map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				String a = StringUtil.getStringValue(rs.getString("username"));
				String new_mac = StringUtil.getStringValue(rs.getString("new_mac"));
				String old_mac = StringUtil.getStringValue(rs.getString("old_mac"));
				if (!"0".equals(a)) {
					map.put("username", a);
				} else {
					map.put("username", "");
				}
				map.put("new_mac", new_mac);
				map.put("old_mac", old_mac);
				map.put("unbinddate", DateUtil.transTime(
						rs.getLong("unbinddate"), "yyyy-MM-dd HH:mm:ss"));
				long b = rs.getLong("binddate");
				if (b != 0) {
					map.put("binddate",
							DateUtil.transTime(b, "yyyy-MM-dd HH:mm:ss"));
				} else {
					map.put("binddate", "");
				}
				return map;
			}
		});
		// TODO wait (more table related)
		String sql1 = " select c.vendor_name,d.device_model, e.hardwareversion,e.softwareversion "
				+ " from stb_tab_dev_bindrecord b,"
				+ " stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e where"
				+ " b.new_vendor_id=c.vendor_id and b.new_model_id=d.device_model_id "
				+ " and b.new_devicetype_id=e.devicetype_id ";

		Map a = null;
		for (Map map : list) {
			PrepareSQL psql1 = new PrepareSQL(sql1.toString());
			psql1.appendAndString("b.username", PrepareSQL.EQUEAL,
					(String) map.get("username"));
			a = queryForMap(psql1.getSQL());
			if (null != a) {
				map.put("vendor_name",
						StringUtil.getStringValue(a.get("vendor_name")));
				map.put("device_model",
						StringUtil.getStringValue(a.get("device_model")));
				map.put("hardwareversion",
						StringUtil.getStringValue(a.get("hardwareversion")));
				map.put("softwareversion",
						StringUtil.getStringValue(a.get("softwareversion")));
			} else {
				map.put("vendor_name", "");
				map.put("device_model", "");
				map.put("hardwareversion", "");
				map.put("softwareversion", "");
			}
			a = null;
		}

		return list;
	}
	public List<Map> newDeviceQueryInfo(String start_time, String end_time,
			String gw_type, long areaId, String queryType, String queryParam,
			String queryField, String cityId, String vendorId,
			String deviceModelId, String devicetypeId, int curPage_splitPage,
			int num_splitPage,String isNewNeed) {
		logger.debug("newDeviceQueryInfo()");
		StringBuffer sql = new StringBuffer();
		String condition = "";
		logger.warn("newDeviceQueryInfo isNewNeed :"+isNewNeed);
		boolean flag = false;
		if(!StringUtil.IsEmpty(isNewNeed) && "yes".equals(isNewNeed)){// TODO wait (more table related)
			String sqlstr = " select b.oper_type,b.loid,b.username,b.old_devsn,b.new_devsn,b.new_mac,b.old_mac,b.unbinddate,b.binddate,"
					+ " c.vendor_name as old_vendor_name,d.device_model as old_device_model,"
					+ " e.hardwareversion as old_hardwareversion,e.softwareversion as old_softwareversion "
					+ " from stb_tab_dev_bindrecord b, stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e ,"
					+ " tab_device_version_attribute f ,tab_device_version_attribute g where"
					+ " b.old_vendor_id=c.vendor_id and b.old_model_id=d.device_model_id "
					+ " and b.old_devicetype_id=e.devicetype_id  "
					+ " and e.devicetype_id=f.devicetype_id "
					+ " and b.old_devicetype_id=f.devicetype_id and f.is_tygate=0 "
					+ " and b.new_devicetype_id=g.devicetype_id and g.is_tygate=1 "
					+ " and b.new_devsn is not null ";
			sql.append(sqlstr);
			flag = true;
		}else{
			// TODO wait (more table related)
			sql.append(" select b.oper_type,b.username,b.old_devsn,b.new_devsn,b.new_mac,b.old_mac,b.unbinddate,b.binddate,"
					+ " c.vendor_name as old_vendor_name,d.device_model as old_device_model,"
					+ " e.hardwareversion as old_hardwareversion,e.softwareversion as old_softwareversion "
					+ " from stb_tab_dev_bindrecord b,"
					+ " stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e where"
					+ " b.old_vendor_id=c.vendor_id and b.old_model_id=d.device_model_id "
					+ " and b.old_devicetype_id=e.devicetype_id  ");
		}

		if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.cityid in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(start_time)) {
			//condition = " and b.updatetime";
			//if(flag){
				condition = " and b.binddate";
			//}
			sql.append(condition).append(">=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			//condition = " and b.updatetime";
			//if(flag){
				condition = " and b.binddate";
			//}
			sql.append(condition).append("<=").append(end_time);
		}if (null != vendorId && !"null".equals(vendorId)
				&& !"".equals(vendorId) && !"-1".equals(vendorId)) {
			sql.append(" and (b.old_vendor_id="+"'"+vendorId+"'"+" or b.new_vendor_id="+"'"+vendorId+"'"+")");
		}if (null != deviceModelId && !"null".equals(deviceModelId)
				&& !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			sql.append(" and (b.old_model_id="+"'"+deviceModelId+"'"+" or b.new_model_id="+"'"+deviceModelId+"'"+")");
		}if (null != devicetypeId && !"null".equals(devicetypeId)
				&& !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
			sql.append(" and (b.old_devicetype_id="+devicetypeId+" or b.new_devicetype_id="+devicetypeId+")");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("old_vendor_name", StringUtil.getStringValue(rs
						.getString("old_vendor_name")));
				map.put("old_device_model", StringUtil.getStringValue(rs
						.getString("old_device_model")));
				map.put("old_hardwareversion", StringUtil.getStringValue(rs
						.getString("old_hardwareversion")));
				map.put("old_devsn",
						StringUtil.getStringValue(rs.getString("old_devsn")));
				map.put("new_devsn",
						StringUtil.getStringValue(rs.getString("new_devsn")));
				map.put("old_softwareversion", StringUtil.getStringValue(rs
						.getString("old_softwareversion")));
				//map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				String a = StringUtil.getStringValue(rs.getString("username"));
				if (!"0".equals(a)) {
					map.put("username", a);
				} else {
					map.put("username", "");
				}
				map.put("unbinddate", DateUtil.transTime(
						rs.getLong("unbinddate"), "yyyy-MM-dd HH:mm:ss"));
				long b = rs.getLong("binddate");
				if (b != 0) {
					map.put("binddate",
							DateUtil.transTime(b, "yyyy-MM-dd HH:mm:ss"));
				} else {
					map.put("binddate", "");
				}

				String new_mac = StringUtil.getStringValue(rs.getString("new_mac"));
				map.put("new_mac", new_mac);
				String old_mac = StringUtil.getStringValue(rs.getString("old_mac"));
				map.put("old_mac", old_mac);
				return map;
			}
		});
		// TODO wait (more table related)
		String sql1 = " select c.vendor_name,d.device_model, e.hardwareversion,e.softwareversion "
				+ " from stb_tab_dev_bindrecord b,"
				+ " stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e where"
				+ " b.new_vendor_id=c.vendor_id and b.new_model_id=d.device_model_id "
				+ " and b.new_devicetype_id=e.devicetype_id ";

		Map a = null;
		for (Map map : list) {
			PrepareSQL psql1 = new PrepareSQL(sql1.toString());
			psql1.appendAndString("b.username", PrepareSQL.EQUEAL,
					(String) map.get("username"));
			a = queryForMap(psql1.getSQL());
			if (null != a) {
				map.put("vendor_name",
						StringUtil.getStringValue(a.get("vendor_name")));
				map.put("device_model",
						StringUtil.getStringValue(a.get("device_model")));
				map.put("hardwareversion",
						StringUtil.getStringValue(a.get("hardwareversion")));
				map.put("softwareversion",
						StringUtil.getStringValue(a.get("softwareversion")));
			} else {
				map.put("vendor_name", "");
				map.put("device_model", "");
				map.put("hardwareversion", "");
				map.put("softwareversion", "");
			}
			a = null;
		}

		return list;
	}

	public int countNewDeviceQueryInfo4Simple(String mac, String username,
			int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append(" select count(*) from stb_tab_dev_bindrecord b,"
				+ " stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e where"
				+ " b.old_vendor_id=c.vendor_id and b.old_model_id=d.device_model_id "
				+ " and b.old_devicetype_id=e.devicetype_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		if (null != mac && !"null".equals(mac) && !"".equals(mac)
				&& !"-1".equals(mac))
		{
			psql.append(" and (b.new_mac='"+mac+"' or b.old_mac='"+mac+"') ");
			//psql.appendAndString("b.loid", PrepareSQL.EQUEAL, mac);s
		}
		if (null != username && !"null".equals(username) && !"".equals(username)
				&& !"-1".equals(username))
		{
			psql.appendAndString("b.username", PrepareSQL.EQUEAL, username);
		}
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
	public int countNewDeviceQueryInfo(String start_time, String end_time,
			String gw_type, long areaId, String queryType, String queryParam,
			String queryField, String cityId, String vendorId,
			String deviceModelId, String devicetypeId, int curPage_splitPage,
			int num_splitPage, String isNewNeed) {
		StringBuffer sql = new StringBuffer();
		String condition = "";
		boolean flag = false;
		logger.warn("countNewDeviceQueryInfo isNewNeed :"+isNewNeed);
		if(!StringUtil.IsEmpty(isNewNeed) && "yes".equals(isNewNeed)){
			// TODO wait (more table related)
			String sqlstr = " select count(*) "
					+ " from stb_tab_dev_bindrecord b, stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e ,"
					+ " tab_device_version_attribute f ,tab_device_version_attribute g where"
					+ " b.old_vendor_id=c.vendor_id and b.old_model_id=d.device_model_id "
					+ " and b.old_devicetype_id=e.devicetype_id  "
					+ " and e.devicetype_id=f.devicetype_id "
					+ " and b.old_devicetype_id=f.devicetype_id and f.is_tygate=0 "
					+ " and b.new_devicetype_id=g.devicetype_id and g.is_tygate=1 "
					+ " and b.new_devsn is not null ";
			sql.append(sqlstr);
			flag = true;
		}else{// TODO wait (more table related)
			sql.append(" select count(*) from stb_tab_dev_bindrecord b,"
					+ " stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e where"
					+ " b.old_vendor_id=c.vendor_id and b.old_model_id=d.device_model_id "
					+ " and b.old_devicetype_id=e.devicetype_id ");
		}

		if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.cityid in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(start_time)) {
			//condition = " and b.updatetime";
			//if(flag){
				condition = " and b.binddate";
			//}
			sql.append(condition).append(">=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			//condition = " and b.updatetime";
			//if(flag){
				condition = " and b.binddate";
			//}
			sql.append(condition).append("<=").append(end_time);
		}if (null != vendorId && !"null".equals(vendorId)
				&& !"".equals(vendorId) && !"-1".equals(vendorId)) {
			sql.append(" and (b.old_vendor_id="+"'"+vendorId+"'"+" or b.new_vendor_id="+"'"+vendorId+"'"+")");
		}if (null != deviceModelId && !"null".equals(deviceModelId)
				&& !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			sql.append(" and (b.old_model_id="+"'"+deviceModelId+"'"+" or b.new_model_id="+"'"+deviceModelId+"'"+")");
		}if (null != devicetypeId && !"null".equals(devicetypeId)
				&& !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
			sql.append(" and (b.old_devicetype_id="+devicetypeId+" or b.new_devicetype_id="+devicetypeId+")");
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());


		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	@SuppressWarnings("unchecked")
	public List<Map> NewDeviceQueryExcel(String start_time, String end_time,
			String gw_type, long areaId, String queryType, String queryParam,
			String queryField, String cityId, String vendorId,
			String deviceModelId, String devicetypeId,String isNewNeed) {
		logger.debug("newDeviceQueryInfo()");

		StringBuffer sql = new StringBuffer();
		String condition = "";
		boolean flag = false;

		logger.warn("NewDeviceQueryExcel isNewNeed : "+isNewNeed);
		if(!StringUtil.IsEmpty(isNewNeed) && "yes".equals(isNewNeed)){
			// TODO wait (more table related)
			String sqlstr = " select b.oper_type,b.loid,b.username,b.old_devsn,b.new_devsn,b.unbinddate,b.binddate,"
					+ " c.vendor_name as old_vendor_name,d.device_model as old_device_model,"
					+ " e.hardwareversion as old_hardwareversion,e.softwareversion as old_softwareversion "
					+ " from stb_tab_dev_bindrecord b, stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e ,"
					+ " tab_device_version_attribute f ,tab_device_version_attribute g where"
					+ " b.old_vendor_id=c.vendor_id and b.old_model_id=d.device_model_id "
					+ " and b.old_devicetype_id=e.devicetype_id  "
					+ " and e.devicetype_id=f.devicetype_id "
					+ " and b.old_devicetype_id=f.devicetype_id and f.is_tygate=0 "
					+ " and b.new_devicetype_id=g.devicetype_id and g.is_tygate=1 "
					+ " and b.new_devsn is not null ";
			sql.append(sqlstr);
			flag = true;
		}else{// TODO wait (more table related)
			sql.append(" select b.oper_type,b.username,b.old_devsn,b.new_devsn,b.new_mac,b.old_mac,b.unbinddate,b.binddate,"
					+ " c.vendor_name as old_vendor_name,d.device_model as old_device_model,"
					+ " e.hardwareversion as old_hardwareversion,e.softwareversion as old_softwareversion "
					+ " from stb_tab_dev_bindrecord b,"
					+ " stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e where"
					+ " b.old_vendor_id=c.vendor_id and b.old_model_id=d.device_model_id "
					+ " and b.old_devicetype_id=e.devicetype_id  ");
		}

		if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)
				&& !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.cityid in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(start_time)) {
		//	condition = " and b.updatetime";
			//if(flag){
				condition = " and b.binddate";
			//}
			sql.append(condition).append(">=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time)) {
			//condition = " and b.updatetime";
			//if(flag){
				condition = " and b.binddate";
			//}
			sql.append(condition).append("<=").append(end_time);
		}if (null != vendorId && !"null".equals(vendorId)
				&& !"".equals(vendorId) && !"-1".equals(vendorId)) {
			sql.append(" and (b.old_vendor_id="+"'"+vendorId+"'"+" or b.new_vendor_id="+"'"+vendorId+"'"+")");
		}if (null != deviceModelId && !"null".equals(deviceModelId)
				&& !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			sql.append(" and (b.old_model_id="+"'"+deviceModelId+"'"+" or b.new_model_id="+"'"+deviceModelId+"'"+")");
		}if (null != devicetypeId && !"null".equals(devicetypeId)
				&& !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
			sql.append(" and (b.old_devicetype_id="+devicetypeId+" or b.new_devicetype_id="+devicetypeId+")");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());

		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put(
						"old_vendor_name",
						StringUtil.getStringValue(list.get(i).get(
								"old_vendor_name")));
				list.get(i).put(
						"old_device_model",
						StringUtil.getStringValue(list.get(i).get(
								"old_device_model")));
				list.get(i).put(
						"old_hardwareversion",
						StringUtil.getStringValue(list.get(i).get(
								"old_hardwareversion")));
				list.get(i)
						.put("old_devsn",
								StringUtil.getStringValue(list.get(i).get(
										"old_devsn")));
				list.get(i)
						.put("new_devsn",
								StringUtil.getStringValue(list.get(i).get(
										"new_devsn")));
				list.get(i).put(
						"old_softwareversion",
						StringUtil.getStringValue(list.get(i).get(
								"old_softwareversion")));
				/*list.get(i).put("loid",
						StringUtil.getStringValue(list.get(i).get("loid")));*/
				String a = StringUtil.getStringValue(list.get(i)
						.get("username"));
				if (!"0".equals(a)) {
					list.get(i).put("username", a);
				} else {
					list.get(i).put("username", "");
				}
				long b = StringUtil.getLongValue(list.get(i).get("binddate"));
				if (b != 0) {
					list.get(i).put("binddate",
							DateUtil.transTime(b, "yyyy-MM-dd HH:mm:ss"));
				} else {
					list.get(i).put("binddate", "");
				}
				list.get(i).put("username",
						StringUtil.getStringValue(list.get(i).get("username")));
				if(!false){
					list.get(i).put(
							"unbinddate",
							DateUtil.transTime(
									StringUtil.getLongValue(list.get(i).get(
											"unbinddate")), "yyyy-MM-dd HH:mm:ss"));
				}
				list.get(i).put("new_mac", StringUtil.getStringValue(list.get(i).get("new_mac")));
				list.get(i).put("old_mac", StringUtil.getStringValue(list.get(i).get("old_mac")));
			}
		}
		// TODO wait (more table related)
		String sql1 = " select c.vendor_name,d.device_model, e.hardwareversion,e.softwareversion "
				+ " from stb_tab_dev_bindrecord b,"
				+ " stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e where"
				+ " b.new_vendor_id=c.vendor_id and b.new_model_id=d.device_model_id "
				+ " and b.new_devicetype_id=e.devicetype_id ";

		Map a = null;
		for (Map map : list) {
			PrepareSQL psql1 = new PrepareSQL(sql1.toString());
			String username = StringUtil.getStringValue(map, "username");
			if(StringUtil.IsEmpty(username)){
				continue;
			}
			psql1.appendAndString("b.username", PrepareSQL.EQUEAL,username);
			a = queryForMap(psql1.getSQL());
			if (null != a) {
				map.put("vendor_name",
						StringUtil.getStringValue(a.get("vendor_name")));
				map.put("device_model",
						StringUtil.getStringValue(a.get("device_model")));
				map.put("hardwareversion",
						StringUtil.getStringValue(a.get("hardwareversion")));
				map.put("softwareversion",
						StringUtil.getStringValue(a.get("softwareversion")));
			} else {
				map.put("vendor_name", "");
				map.put("device_model", "");
				map.put("hardwareversion", "");
				map.put("softwareversion", "");
			}
		}

		return list;
	}

	/**
	 *
	 *
	 * @param areaId
	 * @param cityId
	 * @param userList
	 * @return
	 */
	public List queryDeviceByImportUsername(String gw_type, long areaId,
			String cityId, List<String> userList, String fileName) {
		logger.debug("queryDeviceByImportUsername()");
		// 江苏查报表库
		setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true",
				this.getClass().getName());
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		// pSQL.setSQL(" select a.*,b.vendor_add,c.device_model,d.softwareversion,f.* from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f,");
		pSQL.setSQL(" select b.username,b.old_devsn,b.new_devsn,b.new_mac,b.old_mac,b.binddate,"
				+ "c.vendor_name,d.device_model,e.hardwareversion,e.softwareversion,b.cityname from "
				+ " stb_tab_tmp_accounts a,stb_tab_dev_bindrecord b,stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e "
				+ " where a.username=b.username "
				+ " and b.old_vendor_id=c.vendor_id and b.old_model_id=d.device_model_id "
				+ " and b.old_devicetype_id=e.devicetype_id");
		pSQL.append(" and a.filename = '" + fileName + "'");
		return jt.query(pSQL.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map4Wireless(map, rs);
			}
		});
	}

	public List queryDeviceByImportUsername1(int curPage_splitPage,
			int num_splitPage, String gw_type, long areaId, String cityId,
			String fileName) {
		logger.debug("queryDeviceByImportUsername()");
		// 江苏查报表库
		setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true",
				this.getClass().getName());
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		// pSQL.setSQL(" select a.*,b.vendor_add,c.device_model,d.softwareversion,f.* from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f,");
		pSQL.setSQL(" select b.oper_type,b.username,b.old_devsn,b.new_devsn,b.new_mac,b.old_mac,b.unbinddate,b.binddate,"
				+ " c.vendor_name as old_vendor_name,d.device_model as old_device_model,"
				+ " e.hardwareversion as old_hardwareversion,e.softwareversion as old_softwareversion from "
				+ " stb_tab_tmp_accounts a,stb_tab_dev_bindrecord b,stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e  where a.username=b.username "
				+ " and b.old_vendor_id=c.vendor_id and b.old_model_id=d.device_model_id "
				+ " and b.old_devicetype_id=e.devicetype_id");
		pSQL.append(" and a.filename = '" + fileName + "'");
		if(curPage_splitPage != -1){
			List<Map> list = new ArrayList<Map>();
			list = querySP(pSQL.getSQL(), (curPage_splitPage - 1) * num_splitPage
					+ 1, num_splitPage, new RowMapper() {

				@Override
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					map.put("old_vendor_name", StringUtil.getStringValue(rs
							.getString("old_vendor_name")));
					map.put("old_device_model", StringUtil.getStringValue(rs
							.getString("old_device_model")));
					map.put("old_hardwareversion", StringUtil.getStringValue(rs
							.getString("old_hardwareversion")));
					map.put("old_devsn",
							StringUtil.getStringValue(rs.getString("old_devsn")));
					map.put("new_devsn",
							StringUtil.getStringValue(rs.getString("new_devsn")));
					map.put("old_softwareversion", StringUtil.getStringValue(rs
							.getString("old_softwareversion")));
					//map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
					String a = StringUtil.getStringValue(rs.getString("username"));
					if (!"0".equals(a)) {
						map.put("username", a);
					} else {
						map.put("username", "");
					}
					map.put("unbinddate", DateUtil.transTime(
							rs.getLong("unbinddate"), "yyyy-MM-dd HH:mm:ss"));
					long b = rs.getLong("binddate");
					if (b != 0) {
						map.put("binddate",
								DateUtil.transTime(b, "yyyy-MM-dd HH:mm:ss"));
					} else {
						map.put("binddate", "");
					}

					String new_mac = StringUtil.getStringValue(rs.getString("new_mac"));
					map.put("new_mac", new_mac);
					String old_mac = StringUtil.getStringValue(rs.getString("old_mac"));
					map.put("old_mac", old_mac);
					return map;
				}
			});
			// TODO wait (more table related)
			String sql1 = " select c.vendor_name,d.device_model, e.hardwareversion,e.softwareversion "
					+ " from stb_tab_dev_bindrecord b,"
					+ " stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e where"
					+ " b.new_vendor_id=c.vendor_id and b.new_model_id=d.device_model_id "
					+ " and b.new_devicetype_id=e.devicetype_id ";

			Map a = null;
			for (Map map : list) {
				PrepareSQL psql1 = new PrepareSQL(sql1.toString());
				psql1.appendAndString("b.username", PrepareSQL.EQUEAL,
						(String) map.get("username"));
				a = queryForMap(psql1.getSQL());
				if (null != a) {
					map.put("vendor_name",
							StringUtil.getStringValue(a.get("vendor_name")));
					map.put("device_model",
							StringUtil.getStringValue(a.get("device_model")));
					map.put("hardwareversion",
							StringUtil.getStringValue(a.get("hardwareversion")));
					map.put("softwareversion",
							StringUtil.getStringValue(a.get("softwareversion")));
				} else {
					map.put("vendor_name", "");
					map.put("device_model", "");
					map.put("hardwareversion", "");
					map.put("softwareversion", "");
				}
				a = null;
			}

			return list;
		}else{
			List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSQL.getSQL());
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put(
						"old_vendor_name",
						StringUtil.getStringValue(list.get(i).get(
								"old_vendor_name")));
				list.get(i).put(
						"old_device_model",
						StringUtil.getStringValue(list.get(i).get(
								"old_device_model")));
				list.get(i).put(
						"old_hardwareversion",
						StringUtil.getStringValue(list.get(i).get(
								"old_hardwareversion")));
				list.get(i)
						.put("old_devsn",
								StringUtil.getStringValue(list.get(i).get(
										"old_devsn")));
				list.get(i)
						.put("new_devsn",
								StringUtil.getStringValue(list.get(i).get(
										"new_devsn")));
				list.get(i).put(
						"old_softwareversion",
						StringUtil.getStringValue(list.get(i).get(
								"old_softwareversion")));
				//list.get(i).put("loid",StringUtil.getStringValue(list.get(i).get("loid")));
				String a = StringUtil.getStringValue(list.get(i)
						.get("username"));
				if (!"0".equals(a)) {
					list.get(i).put("username", a);
				} else {
					list.get(i).put("username", "");
				}
				long b = StringUtil.getLongValue(list.get(i).get("binddate"));
				if (b != 0) {
					list.get(i).put("binddate",
							DateUtil.transTime(b, "yyyy-MM-dd HH:mm:ss"));
				} else {
					list.get(i).put("binddate", "");
				}
				list.get(i).put("username",
						StringUtil.getStringValue(list.get(i).get("username")));
				list.get(i).put(
						"unbinddate",
						DateUtil.transTime(
								StringUtil.getLongValue(list.get(i).get(
										"unbinddate")), "yyyy-MM-dd HH:mm:ss"));
				list.get(i).put("new_mac", StringUtil.getStringValue(list.get(i).get("new_mac")));
				list.get(i).put("old_mac", StringUtil.getStringValue(list.get(i).get("old_mac")));
			}
		}
		// TODO wait (more table related)
		String sql1 = " select c.vendor_name,d.device_model, e.hardwareversion,e.softwareversion "
				+ " from stb_tab_dev_bindrecord b,"
				+ " stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e where"
				+ " b.new_vendor_id=c.vendor_id and b.new_model_id=d.device_model_id "
				+ " and b.new_devicetype_id=e.devicetype_id ";

		Map a = null;
		for (Map map : list) {
			PrepareSQL psql1 = new PrepareSQL(sql1.toString());
			psql1.appendAndString("b.username", PrepareSQL.EQUEAL,
					(String) map.get("username"));
			a = queryForMap(psql1.getSQL());
			if (null != a) {
				map.put("vendor_name",
						StringUtil.getStringValue(a.get("vendor_name")));
				map.put("device_model",
						StringUtil.getStringValue(a.get("device_model")));
				map.put("hardwareversion",
						StringUtil.getStringValue(a.get("hardwareversion")));
				map.put("softwareversion",
						StringUtil.getStringValue(a.get("softwareversion")));
			} else {
				map.put("vendor_name", "");
				map.put("device_model", "");
				map.put("hardwareversion", "");
				map.put("softwareversion", "");
			}
		}

		return list;}
	}

	// 数据
	public int queryDeviceBynumber(String gw_type, long areaId, String cityId,
			List<String> userList, String fileName) {
		logger.debug("queryDeviceByImportUsername()");
		// 江苏查报表库
		setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true",
				this.getClass().getName());
		String tableName = "tab_tmp_accounts";
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		// pSQL.setSQL(" select a.*,b.vendor_add,c.device_model,d.softwareversion,f.* from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f,");
		pSQL.setSQL(" select count(*) from tab_tmp_accounts a,tab_dev_bindrecord b,tab_vendor c,"
				+ " gw_device_model d,tab_devicetype_info e  where a.username=b.username "
				+ " and b.old_vendor_id=c.vendor_id and b.old_model_id=d.device_model_id"
				+ " and b.old_devicetype_id=e.devicetype_id ");
		pSQL.append(" and a.filename = '" + fileName + "'");

		return jt.queryForInt(pSQL.toString());
	}

	/**
	 *
	 *
	 * @param areaId
	 * @param cityId
	 * @param userList
	 * @return
	 */
	public List queryDeviceByImportUsernameForDoubleParam(String gw_type,
			long areaId, String cityId, List<String> userList, String fileName) {
		logger.debug("queryDeviceByImportUsername()");
		// 江苏查报表库
		setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true",
				this.getClass().getName());
		String tableName = "tab_tmp_accounts";
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		// pSQL.setSQL(" select a.*,b.vendor_add,c.device_model,d.softwareversion,f.* from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f,");
		pSQL.setSQL(" select b.username, b.cityname, b.old_devsn, b.new_devsn, b.binddate, "
			+  "c.vendor_name,d.device_model,e.hardwareversion,e.softwareversion "
			+"from tab_tmp_accounts a,tab_dev_bindrecord b,tab_vendor c, "
			+ "gw_device_model d,tab_devicetype_info e  where a.username=b.username"
			+ " and b.old_vendor_id=c.vendor_id and b.old_model_id=d.device_model_id and "
			+ "b.old_devicetype_id=e.devicetype_id ");
		pSQL.append(" and a.filename = '" + fileName + "'");

		return jt.query(pSQL.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map4Wireless(map, rs);
			}
		});
	}

	/**
	 *
	 *
	 * @param areaId
	 * @param cityId
	 * @param userList
	 * @return
	 */
	// 没有被调用 注释 2020/10/21
	/*public List queryDeviceByImportKDUsername(String gw_type, long areaId,
			String cityId, List<String> userList, String fileName) {
		logger.debug("queryDeviceByImportUsername()");
		// 江苏查报表库
		setDataSourceType(LipossGlobals.inArea("js_dx") ? "false" : "true",
				this.getClass().getName());
		String tableName = "tab_hgwcustomer";
		String tableServ = "hgwcust_serv_info";
		if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
			tableName = "tab_egwcustomer";
			tableServ = "egwcust_serv_info";
		}
		PrepareSQL pSQL = new PrepareSQL();
		// pSQL.setSQL(" select a.*,b.vendor_add,c.device_model,d.softwareversion,f.* from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f,");
		pSQL.setSQL(" select a.*,b.vendor_add,c.device_model,d.softwareversion,f.*,g.username from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f,");
		pSQL.append(tableName);
		pSQL.append(" e, ");
		pSQL.append(tableServ);
		pSQL.append(" g where a.device_id=e.device_id and a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		pSQL.append(" and e.user_state in ('1','2') ");
		if (null != cityId && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		pSQL.append(" and g.username = f.username and g.user_id = e.user_id and g.serv_type_id=10 and f.filename = '"
				+ fileName + "'");
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		pSQL.append(" order by complete_time");
		return jt.query(pSQL.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map4Wireless(map, rs);
			}
		});
	}*/

	// 数据
	public int queryDeviceByKDnumber(String gw_type, long areaId,
			String cityId, List<String> userList, String fileName) {
		logger.debug("queryDeviceByImportUsername()");
		// 江苏查报表库
		setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true",
				this.getClass().getName());
		String tableName = "tab_hgwcustomer";
		String tableServ = "hgwcust_serv_info";
		if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
			tableName = "tab_egwcustomer";
			tableServ = "egwcust_serv_info";
		}
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		// pSQL.setSQL(" select a.*,b.vendor_add,c.device_model,d.softwareversion,f.* from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f,");
		pSQL.setSQL(" select count(*) from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f,");
		pSQL.append(tableName);
		pSQL.append(" e, ");
		pSQL.append(tableServ);
		pSQL.append(" g where a.device_id=e.device_id and a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		pSQL.append(" and e.user_state in ('1','2') ");
		if (null != cityId && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		pSQL.append(" and g.username = f.username and g.user_id = e.user_id and g.serv_type_id=10 and f.filename = '"
				+ fileName + "'");
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		pSQL.append(" order by complete_time");
		return jt.queryForInt(pSQL.toString());
	}

	/**
	 *
	 *
	 * @param areaId
	 * @param cityId
	 * @param userList
	 * @return
	 */
	public List queryDeviceByImportDevicesnForDoubleParam(String gw_type,
			long areaId, String cityId, List<String> devidesnList,
			String fileName) {
		logger.debug("queryDeviceByImportDevicesn()");
		// 江苏查报表库
		setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true",
				this.getClass().getName());
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL(" select b.vendor_add,c.device_model,d.softwareversion,f.username from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f ");
		pSQL.append(" where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != cityId && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		pSQL.append(" and a.device_serialnumber = f.devicesn and f.filename ='"
				+ fileName + "'");
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		pSQL.append(" order by complete_time");
		return jt.query(pSQL.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map4Wireless(map, rs);
			}
		});
	}

	/**
	 *
	 *
	 * @param areaId
	 * @param cityId
	 * @param userList
	 * @return
	 */

	public List queryDeviceByImportDevicesn(String gw_type, long areaId,
			String cityId, List<String> devidesnList, String fileName) {
		logger.debug("queryDeviceByImportDevicesn()");
		// 江苏查报表库
		setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true",
				this.getClass().getName());
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL(" select b.vendor_add,c.device_model,d.softwareversion,f.username from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f ");
		pSQL.append(" where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != cityId && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		pSQL.append(" and a.device_serialnumber = f.devicesn and f.filename ='"
				+ fileName + "'");
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		pSQL.append(" order by complete_time");
		return jt.query(pSQL.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map4Wireless(map, rs);
			}
		});
	}

	public int queryDeviceByDevicesn(String gw_type, long areaId,
			String cityId, List<String> devidesnList, String fileName) {
		logger.debug("queryDeviceByImportDevicesn()");
		// 江苏查报表库
		setDataSourceType(LipossGlobals.inArea(Global.JSDX) ? "false" : "true",
				this.getClass().getName());
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL(" select count(*) from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d, tab_seniorquery_tmp f ");
		pSQL.append(" where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != cityId && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		pSQL.append(" and a.device_serialnumber = f.devicesn and f.filename ='"
				+ fileName + "'");
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		pSQL.append(" order by complete_time");
		return jt.queryForInt(pSQL.toString());
	}

	public int ByFieldOr(String gw_type, long areaId, String queryParam,
			String cityId) {
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select count(*) from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != queryParam && !"".equals(queryParam)
				&& !"-1".equals(queryParam)) {
			if (queryParam.length() > 5) {
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(queryParam.substring(queryParam.length() - 6,
						queryParam.length()));
				pSQL.append("' ");
			}
			pSQL.append(" and (a.device_serialnumber like '%");
			pSQL.append(queryParam);
			pSQL.append("%' or a.loopback_ip like '%");
			pSQL.append(queryParam);
			pSQL.append("%') ");
		}
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		return jt.queryForInt(pSQL.toString());
	}

	public List<Map> isawifiDeviceByFieldOr(String gw_type, long areaId,
			String queryParam, String cityId) {
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select d.is_awifi from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != queryParam && !"".equals(queryParam)
				&& !"-1".equals(queryParam)) {
			if (queryParam.length() > 5) {
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(queryParam.substring(queryParam.length() - 6,
						queryParam.length()));
				pSQL.append("' ");
			}
			pSQL.append(" and (a.device_serialnumber like '%");
			pSQL.append(queryParam);
			pSQL.append("%' or a.loopback_ip like '%");
			pSQL.append(queryParam);
			pSQL.append("%') ");
		}
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		List list = jt.queryForList(pSQL.getSQL());
		return list;
	}

	/**
	 * 查询设备列表(根据设备序列号和设备IP用or以及like来匹配)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDeviceByFieldOr(String gw_type, long areaId,
			String queryParam, String cityId) {
		logger.debug(
				"GwDeviceQueryDAO=>queryDeviceByFieldOr(areaId:{},queryParam:{})",
				areaId, queryParam);
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id," +
				"a.office_id, a.complete_time, a.zone_id, a.buy_time, a.staff_id, " +
				"a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id, " +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, " +
				"a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus, a.cpe_username, " +
				"a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, " +
				"a.x_com_passwd, a.gw_type, a.device_model_id, a.customer_id, " +
				"a.device_url, a.x_com_passwd_old, a.vendor_id, " +
				"b.vendor_add,c.device_model,d.softwareversion,d.is_awifi " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
				"where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != queryParam && !"".equals(queryParam)) {
			pSQL.append(" and (a.device_serialnumber like '%");
			pSQL.append(queryParam);
			pSQL.append("%' or a.loopback_ip like '%");
			pSQL.append(queryParam);
			pSQL.append("%') ");
		}
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		// if( 1!=areaId ) {
		// pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
		// pSQL.append(String.valueOf(areaId));
		// pSQL.append(") ");
		// }
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		pSQL.append(" order by a.complete_time");
		return jt.query(pSQL.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(根据设备序列号和设备IP用or以及like来匹配)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDeviceByFieldOr(String gw_type, int curPage_splitPage,
			int num_splitPage, long areaId, String queryParam, String cityId) {
		logger.debug(
				"GwDeviceQueryDAO=>queryDeviceByFieldOr(areaId:{},queryParam:{})",
				areaId, queryParam);
		PrepareSQL pSQL = new PrepareSQL();
		if (DBUtil.GetDB() == 2) {
			pSQL.append("select top " + maxNum);
		} else {
			pSQL.append("select");
		}// TODO wait (more table related)
		pSQL.append(" a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id," +
				"a.office_id, a.complete_time, a.zone_id, a.buy_time, a.staff_id, " +
				"a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id, " +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, " +
				"a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus, a.cpe_username, " +
				"a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, " +
				"a.x_com_passwd, a.gw_type, a.device_model_id, a.customer_id, " +
				"a.device_url, a.x_com_passwd_old, a.vendor_id, " +
				"b.vendor_add,c.device_model,d.softwareversion,d.is_awifi " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
				"where a.device_status =1 and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != queryParam && !"".equals(queryParam)) {
			pSQL.append(" and (a.device_serialnumber like '%");
			pSQL.append(queryParam);
			pSQL.append("%' or a.loopback_ip like '%");
			pSQL.append(queryParam);
			pSQL.append("%') ");
		}
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		// if( 1!=areaId ) {
		// pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
		// pSQL.append(String.valueOf(areaId));
		// pSQL.append(") ");
		// }
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		if (DBUtil.GetDB() == 1) {
			pSQL.append(" and rownum< " + maxNum);
		}
		pSQL.append(" order by a.complete_time");

		if (DBUtil.GetDB() == 3)
		{// mysql
			pSQL.append(" limit " + maxNum);
		}

		return querySP(pSQL.toString(),
				(curPage_splitPage - 1) * num_splitPage, num_splitPage,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						return resultSet2Map(map, rs);
					}
				});
	}

	/**
	 * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public int queryDeviceByFieldOrCount(String gw_type, long areaId,
			String queryParam, String cityId) {
		logger.debug(
				"GwDeviceQueryDAO=>queryDeviceByFieldOrCount(areaId:{},queryParam:{})",
				areaId, queryParam);
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select count(*) from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != queryParam && !"".equals(queryParam)) {
			pSQL.append(" and (a.device_serialnumber like '%");
			pSQL.append(queryParam);
			pSQL.append("%' or a.loopback_ip like '%");
			pSQL.append(queryParam);
			pSQL.append("%') ");
		}
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		// if( 1!=areaId ) {
		// pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
		// pSQL.append(String.valueOf(areaId));
		// pSQL.append(") ");
		// }

		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		return jt.queryForInt(pSQL.toString());
	}

	public List<Map> isawifiDevice(String gw_type, long areaId, String cityId,
			String vendorId, String deviceModelId, String devicetypeId,
			String bindType, String deviceSerialnumber, String deviceIp) {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select d.is_awifi from tab_gw_device a,tab_devicetype_info d where a.device_status =1 and a.devicetype_id=d.devicetype_id ");
		if (null != deviceSerialnumber && !"".equals(deviceSerialnumber)
				&& !"-1".equals(deviceSerialnumber)) {
			if (deviceSerialnumber.length() > 5) {
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(
						deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber",
					PrepareSQL.L_LIKE, deviceSerialnumber, false);
		}
		if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL,
					deviceIp, false);
		}
		List list = jt.queryForList(pSQL.getSQL());
		return list;

	}

	public int queryaccount_number(String gw_type, long areaId, String cityId,
			String vendorId, String deviceModelId, String devicetypeId,
			String bindType, String deviceSerialnumber, String deviceIp) {
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select count(*) from tab_gw_device a where a.device_status =1");
		if (null != deviceSerialnumber && !"".equals(deviceSerialnumber)
				&& !"-1".equals(deviceSerialnumber)) {
			if (deviceSerialnumber.length() > 5) {
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(
						deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber",
					PrepareSQL.L_LIKE, deviceSerialnumber, false);
		}
		if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL,
					deviceIp, false);
		}
		return jt.queryForInt(pSQL.toString());
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDevice(String gw_type, long areaId, String cityId,
			String vendorId, String deviceModelId, String devicetypeId,
			String bindType, String deviceSerialnumber, String deviceIp) {
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id," +
				"a.office_id, a.complete_time, a.zone_id, a.buy_time, a.staff_id, " +
				"a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id, " +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, " +
				"a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus, a.cpe_username, " +
				"a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, " +
				"a.x_com_passwd, a.gw_type, a.device_model_id, a.customer_id, " +
				"a.device_url, a.x_com_passwd_old, a.vendor_id, " +
				"b.vendor_add,c.device_model,d.softwareversion,d.is_awifi " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
				"where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != vendorId && !"null".equals(vendorId)
				&& !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (null != deviceModelId && !"null".equals(deviceModelId)
				&& !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL,
					deviceModelId);
		}
		if (null != devicetypeId && !"null".equals(devicetypeId)
				&& !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL,
					devicetypeId);
		}
		if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL,
					bindType);
		}
		if (null != deviceSerialnumber && !"".equals(deviceSerialnumber)
				&& !"-1".equals(deviceSerialnumber)) {
			if (deviceSerialnumber.length() > 5) {
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(
						deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber",
					PrepareSQL.L_LIKE, deviceSerialnumber, false);
		}
		if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL,
					deviceIp, false);
		}
		// if( 1!=areaId ) {
		// pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
		// pSQL.append(String.valueOf(areaId));
		// pSQL.append(") ");
		// }
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		pSQL.append(" order by a.complete_time");
		return jt.query(pSQL.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDeviceBySQL(String gwShare_matchSQL) {
		logger.debug("GwDeviceQueryDAO=>queryDeviceBySQL()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(gwShare_matchSQL);
		pSQL.append(" order by a.complete_time");
		return jt.query(pSQL.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 高级查询设备列表SQL(单独根据设备表的条件查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public String queryDeviceSQL(String gw_type, long areaId, String cityId,
			String vendorId, String deviceModelId, String devicetypeId,
			String bindType, String deviceSerialnumber, String deviceIp) {
		logger.debug("GwDeviceQueryDAO=>queryDeviceSQL()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id," +
				"a.office_id, a.complete_time, a.zone_id, a.buy_time, a.staff_id, " +
				"a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id, " +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, " +
				"a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus, a.cpe_username, " +
				"a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, " +
				"a.x_com_passwd, a.gw_type, a.device_model_id, a.customer_id, " +
				"a.device_url, a.x_com_passwd_old, a.vendor_id, " +
				"b.vendor_add,c.device_model,d.softwareversion " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
				"where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != vendorId && !"null".equals(vendorId)
				&& !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='" + vendorId + "'");
		}
		if (null != deviceModelId && !"null".equals(deviceModelId)
				&& !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append(" and a.device_model_id='" + deviceModelId + "'");
		}
		if (null != devicetypeId && !"null".equals(devicetypeId)
				&& !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
			pSQL.append("and a.devicetype_id=" + devicetypeId);
		}
		if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
			pSQL.append("and a.cpe_allocatedstatus=" + bindType);
		}
		if (null != deviceSerialnumber && !"".equals(deviceSerialnumber)
				&& !"-1".equals(deviceSerialnumber)) {
			if (deviceSerialnumber.length() > 5) {
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(
						deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber",
					PrepareSQL.L_LIKE, deviceSerialnumber, false);
		}
		if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL,
					deviceIp, false);
		}
		// if( 1!=areaId ) {
		// pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
		// pSQL.append(String.valueOf(areaId));
		// pSQL.append(") ");
		// }
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		return pSQL.getSQL().trim();
	}

	public String queryDeviceSQL2(String gw_type, long areaId, String cityId,
			String vendorId, String deviceModelId, String devicetypeId,
			String bindType, String sn) {
		logger.debug("GwDeviceQueryDAO=>queryDeviceSQL2()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select a.device_id,a.device_model_id,a.devicetype_id,b.username ");
		pSQL.append("from tab_gw_device a,tab_hgwcustomer b ");
		pSQL.append("where a.device_status=1 and a.device_id=b.device_id ");
		if (!StringUtil.IsEmpty(cityId) && !"null".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList<String> cityArray = CityDAO
					.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (!StringUtil.IsEmpty(vendorId) && !"null".equals(vendorId)
				&& !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='" + vendorId + "'");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"null".equals(deviceModelId)
				&& !"-1".equals(deviceModelId)) {
			pSQL.append(" and a.device_model_id='" + deviceModelId + "'");
		}
		if (!StringUtil.IsEmpty(devicetypeId) && !"null".equals(devicetypeId)
				&& !"-1".equals(devicetypeId)) {
			pSQL.append("and a.devicetype_id=" + devicetypeId);
		}
		if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType)) {
			pSQL.append("and a.cpe_allocatedstatus=" + bindType);
		}
		if (!StringUtil.IsEmpty(sn) && !"-1".equals(sn)) {
			if (sn.length() > 5) {
				pSQL.append(" and a.dev_sub_sn='"
						+ sn.substring(sn.length() - 6, sn.length()) + "' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber",
					PrepareSQL.L_LIKE, sn, false);
		}

		if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type=" + gw_type);
		}
		return pSQL.getSQL().trim();
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDevice(String gw_type, int curPage_splitPage,
			int num_splitPage, long areaId, String cityId, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber, String deviceIp) {
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();
		if (DBUtil.GetDB() == 2) {
			pSQL.append("select top " + maxNum);
		} else {
			pSQL.append("select");
		}// TODO wait (more table related)
		pSQL.append(" a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id," +
				"a.office_id, a.complete_time, a.zone_id, a.buy_time, a.staff_id, " +
				"a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id, " +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, " +
				"a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus, a.cpe_username, " +
				"a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, " +
				"a.x_com_passwd, a.gw_type, a.device_model_id, a.customer_id, " +
				"a.device_url, a.x_com_passwd_old, a.vendor_id, " +
				"b.vendor_add,c.device_model,d.softwareversion,d.is_awifi " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d " +
				"where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != vendorId && !"null".equals(vendorId)
				&& !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (null != deviceModelId && !"null".equals(deviceModelId)
				&& !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL,
					deviceModelId);
		}
		if (null != devicetypeId && !"null".equals(devicetypeId)
				&& !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL,
					devicetypeId);
		}
		if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL,
					bindType);
		}
		if (null != deviceSerialnumber && !"".equals(deviceSerialnumber)
				&& !"-1".equals(deviceSerialnumber)) {
			if (deviceSerialnumber.length() > 5) {
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(
						deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber",
					PrepareSQL.L_LIKE, deviceSerialnumber, false);
		}
		if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL,
					deviceIp, false);
		}
		// if( 1!=areaId ) {
		// pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
		// pSQL.append(String.valueOf(areaId));
		// pSQL.append(") ");
		// }
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		if (DBUtil.GetDB() == 1) {
			pSQL.append(" and rownum< " + maxNum);
		}
		pSQL.append(" order by a.complete_time");

		if (DBUtil.GetDB() == 3)
		{// mysql
			pSQL.append(" limit " + maxNum);
		}

		return querySP(pSQL.toString(),
				(curPage_splitPage - 1) * num_splitPage, num_splitPage,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						return resultSet2Map(map, rs);
					}
				});
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDeviceBySQL(int curPage_splitPage, int num_splitPage,
			String gwShare_matchSQL) {
		logger.debug("GwDeviceQueryDAO=>queryDeviceBySQL()");
		PrepareSQL pSQL = new PrepareSQL();
		if (DBUtil.GetDB() == 2) {
			pSQL.setSQL(gwShare_matchSQL.replace("select", "select top "
					+ maxNum));
		} else {
			pSQL.setSQL(gwShare_matchSQL);
			pSQL.append(" and rownum< " + maxNum);
		}
		pSQL.append(" order by a.complete_time");

		if(DBUtil.GetDB() == 3)
		{// mysql
			pSQL.append(" limit " + maxNum);
		}

		return querySP(pSQL.toString(),
				(curPage_splitPage - 1) * num_splitPage, num_splitPage,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						return resultSet2Map(map, rs);
					}
				});
	}

	/**
	 * 查询设备列表(条数)(根据设备序列号和设备IP用or以及like来匹配)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public int queryDeviceCount(String gw_type, long areaId, String cityId,
			String vendorId, String deviceModelId, String devicetypeId,
			String bindType, String deviceSerialnumber, String deviceIp) {
		logger.debug("GwDeviceQueryDAO=>queryDeviceCount()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select count(*) from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != vendorId && !"null".equals(vendorId)
				&& !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (null != deviceModelId && !"null".equals(deviceModelId)
				&& !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL,
					deviceModelId);
		}
		if (null != devicetypeId && !"null".equals(devicetypeId)
				&& !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL,
					devicetypeId);
		}
		if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL,
					bindType);
		}
		if (null != deviceSerialnumber && !"".equals(deviceSerialnumber)
				&& !"-1".equals(deviceSerialnumber)) {
			if (deviceSerialnumber.length() > 5) {
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(
						deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber",
					PrepareSQL.L_LIKE, deviceSerialnumber, false);
		}
		if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL,
					deviceIp, false);
		}
		// if( 1!=areaId ) {
		// pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
		// pSQL.append(String.valueOf(areaId));
		// pSQL.append(") ");
		// }
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		return jt.queryForInt(pSQL.toString());
	}

	/**
	 * 根据自定义的SQL查询设备
	 *
	 * @param gwShare_matchSQL
	 * @return
	 */
	public int queryDeviceCountBySQL(String gwShare_matchSQL) {
		logger.debug("GwDeviceQueryDAO=>queryDeviceCountBySQL()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select count(*) from ");
		pSQL.append(gwShare_matchSQL.split("from")[1]);
		return jt.queryForInt(pSQL.toString());
	}

	/**
	 * 查询设备列表(关联设备状态表查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDeviceByLikeStatus(String gw_type, long areaId,
			String cityId, String vendorId, String deviceModelId,
			String devicetypeId, String bindType, String deviceSerialnumber,
			String deviceIp, String onlineStatus) {
		logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatus()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id," +
				"a.office_id, a.complete_time, a.zone_id, a.buy_time, a.staff_id, " +
				"a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id, " +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, " +
				"a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus, a.cpe_username, " +
				"a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, " +
				"a.x_com_passwd, a.gw_type, a.device_model_id, a.customer_id, " +
				"a.device_url, a.x_com_passwd_old, a.vendor_id, " +
				"b.vendor_add,c.device_model,d.softwareversion " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e " +
				"where a.device_status =1 and  a.device_id=e.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != onlineStatus && !"".equals(onlineStatus)
				&& !"-1".equals(onlineStatus)) {
			pSQL.appendAndNumber("e.online_status", PrepareSQL.EQUEAL,
					onlineStatus);
		}
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != vendorId && !"null".equals(vendorId)
				&& !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (null != deviceModelId && !"null".equals(deviceModelId)
				&& !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL,
					deviceModelId);
		}
		if (null != devicetypeId && !"null".equals(devicetypeId)
				&& !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL,
					devicetypeId);
		}
		if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL,
					bindType);
		}
		if (null != deviceSerialnumber && !"".equals(deviceSerialnumber)
				&& !"-1".equals(deviceSerialnumber)) {
			if (deviceSerialnumber.length() > 5) {
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(
						deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber",
					PrepareSQL.L_LIKE, deviceSerialnumber, false);
		}
		if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL,
					deviceIp, false);
		}
		// if( 1!=areaId ) {
		// pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
		// pSQL.append(String.valueOf(areaId));
		// pSQL.append(") ");
		// }
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		pSQL.append(" order by a.complete_time");
		return jt.query(pSQL.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 高级查询设备列表SQL(关联设备状态表查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public String queryDeviceByLikeStatusSQL(String gw_type, long areaId,
			String cityId, String vendorId, String deviceModelId,
			String devicetypeId, String bindType, String deviceSerialnumber,
			String deviceIp, String onlineStatus) {
		logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatusSQL()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id," +
				"a.office_id, a.complete_time, a.zone_id, a.buy_time, a.staff_id, " +
				"a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id, " +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, " +
				"a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus, a.cpe_username, " +
				"a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, " +
				"a.x_com_passwd, a.gw_type, a.device_model_id, a.customer_id, " +
				"a.device_url, a.x_com_passwd_old, a.vendor_id, " +
				"b.vendor_add,c.device_model,d.softwareversion " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e " +
				"where a.device_status =1 and  a.device_id=e.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != onlineStatus && !"".equals(onlineStatus)
				&& !"-1".equals(onlineStatus)) {
			pSQL.appendAndNumber("e.online_status", PrepareSQL.EQUEAL,
					onlineStatus);
		}
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != vendorId && !"null".equals(vendorId)
				&& !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='" + vendorId + "'");
		}
		if (null != deviceModelId && !"null".equals(deviceModelId)
				&& !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.append(" and a.device_model_id='" + deviceModelId + "'");
		}
		if (null != devicetypeId && !"null".equals(devicetypeId)
				&& !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
			pSQL.append("and a.devicetype_id=" + devicetypeId);
		}
		if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
			pSQL.append("and a.cpe_allocatedstatus=" + bindType);
		}
		if (null != deviceSerialnumber && !"".equals(deviceSerialnumber)
				&& !"-1".equals(deviceSerialnumber)) {
			if (deviceSerialnumber.length() > 5) {
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(
						deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber",
					PrepareSQL.L_LIKE, deviceSerialnumber, false);
		}
		if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL,
					deviceIp, false);
		}
		// if( 1!=areaId ) {
		// pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
		// pSQL.append(String.valueOf(areaId));
		// pSQL.append(") ");
		// }
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		return pSQL.getSQL().trim();
	}

	public String queryDeviceByLikeStatusSQL2(String gw_type, long areaId,
			String cityId, String vendorId, String deviceModelId,
			String devicetypeId, String bindType, String sn, String onlineStatus) {
		logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatusSQL2()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.append("select a.device_id,a.device_model_id,a.devicetype_id,b.username ");
		pSQL.append("from tab_gw_device a,tab_hgwcustomer b,gw_devicestatus e ");
		pSQL.append("where a.device_status=1 and a.device_id=e.device_id and a.device_id=b.device_id ");
		if (!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus)) {
			pSQL.appendAndNumber("e.online_status", PrepareSQL.EQUEAL,
					onlineStatus);
		}
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList<String> cityArray = CityDAO
					.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (!StringUtil.IsEmpty(vendorId) && !"null".equals(vendorId)
				&& !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='" + vendorId + "'");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"null".equals(deviceModelId)
				&& !"-1".equals(deviceModelId)) {
			pSQL.append(" and a.device_model_id='" + deviceModelId + "'");
		}
		if (!StringUtil.IsEmpty(devicetypeId) && !"null".equals(devicetypeId)
				&& !"-1".equals(devicetypeId)) {
			pSQL.append("and a.devicetype_id=" + devicetypeId);
		}
		if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType)) {
			pSQL.append("and a.cpe_allocatedstatus=" + bindType);
		}
		if (!StringUtil.IsEmpty(sn) && !"-1".equals(sn)) {
			if (sn.length() > 5) {
				pSQL.append(" and a.dev_sub_sn='"
						+ sn.substring(sn.length() - 6, sn.length()) + "' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber",
					PrepareSQL.L_LIKE, sn, false);
		}
		if (!StringUtil.IsEmpty(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type=" + gw_type);
		}
		return pSQL.getSQL().trim();
	}

	/**
	 * 查询设备列表(关联设备状态表查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDeviceByLikeStatus(String gw_type, int curPage_splitPage,
			int num_splitPage, long areaId, String cityId, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String deviceSerialnumber, String deviceIp, String onlineStatus) {
		logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatus()");
		PrepareSQL pSQL = new PrepareSQL();
		if (DBUtil.GetDB() == 2) {
			pSQL.append("select top " + maxNum);
		} else {
			pSQL.append("select");
		}// TODO wait (more table related)
		pSQL.append(" a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id," +
				"a.office_id, a.complete_time, a.zone_id, a.buy_time, a.staff_id, " +
				"a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id, " +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, " +
				"a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus, a.cpe_username, " +
				"a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, " +
				"a.x_com_passwd, a.gw_type, a.device_model_id, a.customer_id, " +
				"a.device_url, a.x_com_passwd_old, a.vendor_id, " +
				"b.vendor_add,c.device_model,d.softwareversion " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e " +
				"where a.device_status =1 and  a.device_id=e.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != onlineStatus && !"".equals(onlineStatus)
				&& !"-1".equals(onlineStatus)) {
			pSQL.appendAndNumber("e.online_status", PrepareSQL.EQUEAL,
					onlineStatus);
		}
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != vendorId && !"null".equals(vendorId)
				&& !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (null != deviceModelId && !"null".equals(deviceModelId)
				&& !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL,
					deviceModelId);
		}
		if (null != devicetypeId && !"null".equals(devicetypeId)
				&& !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL,
					devicetypeId);
		}
		if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL,
					bindType);
		}
		if (null != deviceSerialnumber && !"".equals(deviceSerialnumber)
				&& !"-1".equals(deviceSerialnumber)) {
			if (deviceSerialnumber.length() > 5) {
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(
						deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber",
					PrepareSQL.L_LIKE, deviceSerialnumber, false);
		}
		if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL,
					deviceIp, false);
		}
		// if( 1!=areaId ) {
		// pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
		// pSQL.append(String.valueOf(areaId));
		// pSQL.append(") ");
		// }
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		if (DBUtil.GetDB() == 1) {
			pSQL.append(" and rownum< " + maxNum);
		}
		pSQL.append(" order by a.complete_time");

		if (DBUtil.GetDB() == 3)
		{// mysql
			pSQL.append(" limit " + maxNum);
		}

		return querySP(pSQL.toString(),
				(curPage_splitPage - 1) * num_splitPage, num_splitPage,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						return resultSet2Map(map, rs);
					}
				});
	}

	/**
	 * 查询设备列表(关联设备状态表查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public int queryDeviceByLikeStatusCount(String gw_type, long areaId,
			String cityId, String vendorId, String deviceModelId,
			String devicetypeId, String bindType, String deviceSerialnumber,
			String deviceIp, String onlineStatus) {
		logger.debug("GwDeviceQueryDAO=>queryDeviceByLikeStatus()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.setSQL("select count(*) from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,gw_devicestatus e where a.device_status =1 and  a.device_id=e.device_id and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != onlineStatus && !"".equals(onlineStatus)
				&& !"-1".equals(onlineStatus)) {
			pSQL.appendAndNumber("e.online_status", PrepareSQL.EQUEAL,
					onlineStatus);
		}
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != vendorId && !"null".equals(vendorId)
				&& !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.appendAndString("a.vendor_id", PrepareSQL.EQUEAL, vendorId);
		}
		if (null != deviceModelId && !"null".equals(deviceModelId)
				&& !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
			pSQL.appendAndString("a.device_model_id", PrepareSQL.EQUEAL,
					deviceModelId);
		}
		if (null != devicetypeId && !"null".equals(devicetypeId)
				&& !"".equals(devicetypeId) && !"-1".equals(devicetypeId)) {
			pSQL.appendAndNumber("a.devicetype_id", PrepareSQL.EQUEAL,
					devicetypeId);
		}
		if (null != bindType && !"".equals(bindType) && !"-1".equals(bindType)) {
			pSQL.appendAndNumber("a.cpe_allocatedstatus", PrepareSQL.EQUEAL,
					bindType);
		}
		if (null != deviceSerialnumber && !"".equals(deviceSerialnumber)
				&& !"-1".equals(deviceSerialnumber)) {
			if (deviceSerialnumber.length() > 5) {
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSerialnumber.substring(
						deviceSerialnumber.length() - 6,
						deviceSerialnumber.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber",
					PrepareSQL.L_LIKE, deviceSerialnumber, false);
		}
		if (null != deviceIp && !"".equals(deviceIp) && !"-1".equals(deviceIp)) {
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL,
					deviceIp, false);
		}
		// if( 1!=areaId ) {
		// pSQL.append(" and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id =");
		// pSQL.append(String.valueOf(areaId));
		// pSQL.append(") ");
		// }
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}

		return jt.queryForInt(pSQL.toString());
	}

	public int account_number(String gw_type, long areaId, String cityId,
			String username) {
		PrepareSQL pSQL = new PrepareSQL();
		String tableName = "tab_hgwcustomer";
		if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
			tableName = "tab_egwcustomer";
		}
		pSQL.setSQL("select count(*) from tab_gw_device a,tab_hgwcustomer e where a.device_id=e.device_id");
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != username && !"".equals(username)) {
			pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
		}

		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		return jt.queryForInt(pSQL.toString());
	}

	public List<Map> isawifiDevice(String gw_type, long areaId, String cityId,
			String username) {
		PrepareSQL pSQL = new PrepareSQL();
		String tableName = "tab_hgwcustomer";
		if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
			tableName = "tab_egwcustomer";
		}// TODO wait (more table related)
		pSQL.setSQL("select d.is_awifi from tab_gw_device a,tab_devicetype_info d,");
		pSQL.append(tableName);
		pSQL.append(" e where a.device_id=e.device_id and a.device_status =1 and a.devicetype_id=d.devicetype_id ");
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != username && !"".equals(username)) {
			pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
		}

		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		List list = jt.queryForList(pSQL.getSQL());
		return list;
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDevice(String gw_type, long areaId, String cityId,
			String username) {
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();
		String tableName = "tab_hgwcustomer";
		if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
			tableName = "tab_egwcustomer";
		}// TODO wait (more table related)
		pSQL.setSQL("select a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id," +
				"a.office_id, a.complete_time, a.zone_id, a.buy_time, a.staff_id, " +
				"a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id, " +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, " +
				"a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus, a.cpe_username, " +
				"a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, " +
				"a.x_com_passwd, a.gw_type, a.device_model_id, a.customer_id, " +
				"a.device_url, a.x_com_passwd_old, a.vendor_id, " +
				"b.vendor_add,c.device_model,d.softwareversion , d.is_awifi " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,");
		pSQL.append(tableName);
		pSQL.append(" e where a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != username && !"".equals(username)) {
			if ("1".equalsIgnoreCase(LipossGlobals
					.getLipossProperty("isLikeQuery"))) {
				pSQL.append(" and e.username like '%");
				pSQL.append(username);
				pSQL.append("' ");
				String user_sub_name = username;
				if (username.length() > 6) {
					user_sub_name = username.substring(username.length() - 6);
				}
				pSQL.append(" and e.user_sub_name ='");
				pSQL.append(user_sub_name);
				pSQL.append("'");
			} else {
				pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
			}
		}

		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		pSQL.append(" order by a.complete_time");
		return jt.query(pSQL.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public List queryDevice(String gw_type, int curPage_splitPage,
			int num_splitPage, long areaId, String cityId, String username) {
		logger.debug("GwDeviceQueryDAO=>queryDevice()");
		PrepareSQL pSQL = new PrepareSQL();
		String tableName = "tab_hgwcustomer";
		if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
			tableName = "tab_egwcustomer";
		}
		if (DBUtil.GetDB() == 2) {
			pSQL.append("select top " + maxNum);
		} else {
			pSQL.append("select");
		}// TODO wait (more table related)
		pSQL.append(" a.device_id, a.oui, a.device_serialnumber, a.device_name, a.city_id," +
				"a.office_id, a.complete_time, a.zone_id, a.buy_time, a.staff_id, " +
				"a.remark, a.loopback_ip, a.interface_id, a.device_status, a.gather_id, " +
				"a.devicetype_id, a.maxenvelopes, a.cr_port, a.cr_path, " +
				"a.cpe_mac, a.cpe_currentupdatetime, a.cpe_allocatedstatus, a.cpe_username, " +
				"a.cpe_passwd, a.acs_username, a.acs_passwd, a.device_type, a.x_com_username, " +
				"a.x_com_passwd, a.gw_type, a.device_model_id, a.customer_id, " +
				"a.device_url, a.x_com_passwd_old, a.vendor_id, " +
				"b.vendor_add,c.device_model,d.softwareversion,d.is_awifi " +
				"from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,");
		pSQL.append(tableName);
		pSQL.append(" e where a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != username && !"".equals(username)) {
			if ("1".equalsIgnoreCase(LipossGlobals
					.getLipossProperty("isLikeQuery"))) {
				pSQL.append(" and e.username like '%");
				pSQL.append(username);
				pSQL.append("' ");
				String user_sub_name = username;
				if (username.length() > 6) {
					user_sub_name = username.substring(username.length() - 6);
				}
				pSQL.append(" and e.user_sub_name ='");
				pSQL.append(user_sub_name);
				pSQL.append("'");
			} else {
				pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
			}
		}

		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		if (DBUtil.GetDB() == 1) {
			pSQL.append(" and rownum< " + maxNum);
		}
		pSQL.append(" order by a.complete_time");

		if (DBUtil.GetDB() == 3)
		{// mysql
			pSQL.append(" limit " + maxNum);
		}

		return querySP(pSQL.toString(),
				(curPage_splitPage - 1) * num_splitPage, num_splitPage,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						return resultSet2Map(map, rs);
					}
				});
	}

	/**
	 * 宁夏 按VOIP电话号码查询设备 根据VOIP电话号码以及属地统计有多少设备
	 *
	 * add by zhangchy 2012-02-23
	 *
	 * @param city_id
	 * @param voipPhoneNum
	 * @return
	 */
	public int queryDeviceCount(String gw_type, String city_id,
			String voipPhoneNum) {

		logger.debug("queryDeviceCount({},{})", new Object[] { city_id,
				voipPhoneNum });

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)

		psql.append("select count(*) ");
		psql.append("  from tab_hgwcustomer a, tab_voip_serv_param b, tab_gw_device c, tab_vendor d, gw_device_model e, tab_devicetype_info f ");
		psql.append(" where 1 = 1 ");
		psql.append("   and a.user_id = b.user_id");
		psql.append("   and a.device_id = c.device_id");
		psql.append("   and c.vendor_id = d.vendor_id");
		psql.append("   and c.device_model_id = e.device_model_id ");
		psql.append("   and c.devicetype_id = f.devicetype_id");
		psql.append("   and c.device_status = 1"); // 设备已确认

		// 根据VOIP电话号码
		if (null != voipPhoneNum && !"".equals(voipPhoneNum)) {
			psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
		}

		if (null != city_id && !"null".equals(city_id) && !"".equals(city_id)
				&& !"-1".equals(city_id) && !"00".equals(city_id)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
			psql.append("   and c.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			psql.append(" and c.gw_type = " + gw_type);
		}

		return jt.queryForInt(psql.getSQL());
	}

	public int number(String gw_type, String city_id, String voipPhoneNum) {
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select count(*) from tab_hgwcustomer a, tab_voip_serv_param b, tab_gw_device c ");
		psql.append(" where 1 = 1 ");
		psql.append("   and a.user_id = b.user_id");
		psql.append("   and a.device_id = c.device_id");
		// psql.append("   and c.device_status = 1"); // 设备已确认
		// 根据VOIP电话号码
		if (null != voipPhoneNum && !"".equals(voipPhoneNum)) {
			psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
		}

		if (null != city_id && !"null".equals(city_id) && !"".equals(city_id)
				&& !"-1".equals(city_id) && !"00".equals(city_id)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
			psql.append("   and c.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			psql.append(" and c.gw_type = " + gw_type);
		}
		return jt.queryForInt(psql.toString());
	}

	public List<Map> isawifiDevice(String gw_type, String city_id,
			String voipPhoneNum) {
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select f.is_awifi from tab_hgwcustomer a, tab_voip_serv_param b, tab_gw_device c, tab_vendor d, gw_device_model e, tab_devicetype_info f ");
		psql.append(" where 1 = 1 ");
		psql.append("   and a.user_id = b.user_id");
		psql.append("   and a.device_id = c.device_id");
		psql.append("   and c.vendor_id = d.vendor_id");
		psql.append("   and c.device_model_id = e.device_model_id ");
		psql.append("   and c.devicetype_id = f.devicetype_id");
		psql.append("   and c.device_status = 1"); // 设备已确认

		// 根据VOIP电话号码
		if (null != voipPhoneNum && !"".equals(voipPhoneNum)) {
			psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
		}

		if (null != city_id && !"null".equals(city_id) && !"".equals(city_id)
				&& !"-1".equals(city_id) && !"00".equals(city_id)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
			psql.append("   and c.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			psql.append(" and c.gw_type = " + gw_type);
		}
		List list = jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 宁夏 按VOIP电话号码查询设备
	 *
	 * 不分页
	 *
	 * add by zhangchy 2012-02-23
	 *
	 * @param city_id
	 * @param voipPhoneNum
	 * @return
	 */
	public List queryDevice(String gw_type, String city_id, String voipPhoneNum) {

		logger.debug("queryDevice({},{})",
				new Object[] { city_id, voipPhoneNum });

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)

		psql.append("select c.device_id, c.oui, c.device_serialnumber, c.device_name, c.city_id," +
				"c.office_id, c.complete_time, c.zone_id, c.buy_time, c.staff_id, " +
				"c.remark, c.loopback_ip, c.interface_id, c.device_status, c.gather_id, " +
				"c.devicetype_id, c.maxenvelopes, c.cr_port, c.cr_path, " +
				"c.cpe_mac, c.cpe_currentupdatetime, c.cpe_allocatedstatus, c.cpe_username, " +
				"c.cpe_passwd, c.acs_username, c.acs_passwd, c.device_type, c.x_com_username, " +
				"c.x_com_passwd, c.gw_type, c.device_model_id, c.customer_id, " +
				"c.device_url, c.x_com_passwd_old, c.vendor_id, " +
				"d.vendor_add, e.device_model, f.softwareversion,f.is_awifi ");
		psql.append("  from tab_hgwcustomer a, tab_voip_serv_param b, tab_gw_device c, tab_vendor d, gw_device_model e, tab_devicetype_info f ");
		psql.append(" where 1 = 1 ");
		psql.append("   and a.user_id = b.user_id");
		psql.append("   and a.device_id = c.device_id");
		psql.append("   and c.vendor_id = d.vendor_id");
		psql.append("   and c.device_model_id = e.device_model_id ");
		psql.append("   and c.devicetype_id = f.devicetype_id");
		psql.append("   and c.device_status = 1"); // 设备已确认

		// 根据VOIP电话号码
		if (null != voipPhoneNum && !"".equals(voipPhoneNum)) {
			psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
		}

		if (null != city_id && !"null".equals(city_id) && !"".equals(city_id)
				&& !"-1".equals(city_id) && !"00".equals(city_id)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
			psql.append("   and c.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			psql.append(" and c.gw_type = " + gw_type);
		}
		return jt.query(psql.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 宁夏 按VOIP电话号码查询设备
	 *
	 * 分页
	 *
	 * add by zhangchy 2012-02-23
	 *
	 * @param city_id
	 * @param voipPhoneNum
	 * @return
	 */
	public List queryDevice(String gw_type, int curPage_splitPage,
			int num_splitPage, String city_id, String voipPhoneNum) {

		logger.debug("queryDevice({},{},{},{})", new Object[] {
				curPage_splitPage, num_splitPage, city_id, voipPhoneNum });

		PrepareSQL psql = new PrepareSQL();
		if (DBUtil.GetDB() == 2) {
			psql.append("select top " + maxNum);
		} else {
			psql.append("select");
		}// TODO wait (more table related)
		psql.append(" c.device_id, c.oui, c.device_serialnumber, c.device_name, c.city_id," +
				"c.office_id, c.complete_time, c.zone_id, c.buy_time, c.staff_id, " +
				"c.remark, c.loopback_ip, c.interface_id, c.device_status, c.gather_id, " +
				"c.devicetype_id, c.maxenvelopes, c.cr_port, c.cr_path, " +
				"c.cpe_mac, c.cpe_currentupdatetime, c.cpe_allocatedstatus, c.cpe_username, " +
				"c.cpe_passwd, c.acs_username, c.acs_passwd, c.device_type, c.x_com_username, " +
				"c.x_com_passwd, c.gw_type, c.device_model_id, c.customer_id, " +
				"c.device_url, c.x_com_passwd_old, c.vendor_id, " +
				"d.vendor_add, e.device_model, f.softwareversion ,f.is_awifi");
		psql.append("  from tab_hgwcustomer a, tab_voip_serv_param b, tab_gw_device c, tab_vendor d, gw_device_model e, tab_devicetype_info f ");
		psql.append(" where 1 = 1 ");
		psql.append("   and a.user_id = b.user_id");
		psql.append("   and a.device_id = c.device_id");
		psql.append("   and c.vendor_id = d.vendor_id");
		psql.append("   and c.device_model_id = e.device_model_id ");
		psql.append("   and c.devicetype_id = f.devicetype_id");
		psql.append("   and c.device_status = 1"); // 设备已确认

		// 根据VOIP电话号码
		if (null != voipPhoneNum && !"".equals(voipPhoneNum)) {
			psql.append("   and b.voip_phone = '" + voipPhoneNum + "'");
		}

		if (null != city_id && !"null".equals(city_id) && !"".equals(city_id)
				&& !"-1".equals(city_id) && !"00".equals(city_id)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
			psql.append("   and c.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}

		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			psql.append(" and c.gw_type = " + gw_type);
		}
		if (DBUtil.GetDB() == 1) {
			psql.append(" and rownum< " + maxNum);
		}else if (DBUtil.GetDB() == 3)
		{// mysql
			psql.append(" limit " + maxNum);
		}
		return querySP(psql.toString(),
				(curPage_splitPage - 1) * num_splitPage, num_splitPage,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						return resultSet2Map(map, rs);
					}
				});
	}

	public int Kd(String gw_type, String cityId, String kdname) {
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append(" select count(*) from tab_hgwcustomer a, hgwcust_serv_info b, tab_gw_device c where a.device_id=c.device_id and a.user_id = b.user_id ");
		// psql.append("   and c.device_status = 1"); // 设备已确认

		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append("   and c.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}

		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			psql.append(" and c.gw_type = " + gw_type);
		}

		// 宽带账号
		if (!StringUtil.IsEmpty(kdname)) {
			psql.append(" and b.serv_type_id = 10 and b.username='" + kdname
					+ "'");
		}
		List list = jt.queryForList(psql.getSQL());

		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append("   and c.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}

		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			psql.append(" and c.gw_type = " + gw_type);
		}

		// 宽带账号
		if (!StringUtil.IsEmpty(kdname)) {
			psql.append(" and b.serv_type_id = 10 and b.username='" + kdname
					+ "'");
		}
		return jt.queryForInt(psql.toString());
	}

	public List<Map> isawifiDeviceByKdname(String gw_type, String cityId,
			String kdname) {
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select f.is_awifi ");
		psql.append(" from tab_hgwcustomer a, hgwcust_serv_info b, tab_gw_device c, tab_vendor d, ");
		psql.append(" gw_device_model e, tab_devicetype_info f");
		psql.append(" where a.device_id=c.device_id and a.user_id = b.user_id ");
		psql.append("   and c.vendor_id = d.vendor_id");
		psql.append("   and c.device_model_id = e.device_model_id ");
		psql.append("   and c.devicetype_id = f.devicetype_id");
		psql.append("   and c.device_status = 1"); // 设备已确认

		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append("   and c.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}

		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			psql.append(" and c.gw_type = " + gw_type);
		}

		// 宽带账号
		if (!StringUtil.IsEmpty(kdname)) {
			psql.append(" and b.serv_type_id = 10 and b.username='" + kdname
					+ "'");
		}
		List list = jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 查询设备页面增加按照宽带账号查询(不分页)
	 *
	 * @param gw_type
	 * @param cityId
	 * @param kdname
	 *
	 * @author chenjie
	 * @date 2012-4-18
	 * @return
	 */
	public List queryDeviceByKdname(String gw_type, String cityId, String kdname) {
		logger.debug("queryDeviceByKdname({},{},{})", new Object[] { gw_type,
				cityId, kdname });
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select c.device_id, c.oui, c.device_serialnumber, c.device_name, c.city_id," +
				"c.office_id, c.complete_time, c.zone_id, c.buy_time, c.staff_id, " +
				"c.remark, c.loopback_ip, c.interface_id, c.device_status, c.gather_id, " +
				"c.devicetype_id, c.maxenvelopes, c.cr_port, c.cr_path, " +
				"c.cpe_mac, c.cpe_currentupdatetime, c.cpe_allocatedstatus, c.cpe_username, " +
				"c.cpe_passwd, c.acs_username, c.acs_passwd, c.device_type, c.x_com_username, " +
				"c.x_com_passwd, c.gw_type, c.device_model_id, c.customer_id, " +
				"d.vendor_add, e.device_model, f.softwareversion,f.is_awifi ");
		psql.append(" from tab_hgwcustomer a, hgwcust_serv_info b, tab_gw_device c, tab_vendor d, ");
		psql.append(" gw_device_model e, tab_devicetype_info f");
		psql.append(" where a.device_id=c.device_id and a.user_id = b.user_id ");
		psql.append("   and c.vendor_id = d.vendor_id");
		psql.append("   and c.device_model_id = e.device_model_id ");
		psql.append("   and c.devicetype_id = f.devicetype_id");
		psql.append("   and c.device_status = 1"); // 设备已确认

		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append("   and c.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}

		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			psql.append(" and c.gw_type = " + gw_type);
		}

		// 宽带账号
		if (!StringUtil.IsEmpty(kdname)) {
			psql.append(" and b.serv_type_id = 10 and b.username='" + kdname
					+ "'");
		}

		return jt.query(psql.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}

	/**
	 * 查询设备页面增加按照宽带账号统计总数
	 *
	 * @param gw_type
	 * @param cityId
	 * @param kdname
	 *
	 * @author chenjie
	 * @date 2012-4-18
	 * @return
	 */
	public int queryDeviceCountByKdname(String gw_type, String cityId,
			String kdname) {
		logger.debug("queryDeviceByKdname({},{},{})", new Object[] { gw_type,
				cityId, kdname });
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select count(*)");
		psql.append(" from tab_hgwcustomer a, hgwcust_serv_info b, tab_gw_device c, tab_vendor d, ");
		psql.append(" gw_device_model e, tab_devicetype_info f");
		psql.append(" where a.device_id=c.device_id and a.user_id = b.user_id ");
		psql.append("   and c.vendor_id = d.vendor_id");
		psql.append("   and c.device_model_id = e.device_model_id ");
		psql.append("   and c.devicetype_id = f.devicetype_id");
		psql.append("   and c.device_status = 1"); // 设备已确认

		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append("   and c.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}

		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			psql.append(" and c.gw_type = " + gw_type);
		}

		// 宽带账号
		if (!StringUtil.IsEmpty(kdname)) {
			psql.append(" and b.serv_type_id = 10 and b.username='" + kdname
					+ "'");
		}

		return jt.queryForInt(psql.getSQL());
	}

	/**
	 * 查询设备页面增加按照宽带账号查询(分页)
	 *
	 * @param gw_type
	 * @param cityId
	 * @param kdname
	 * @param curPage_splitPage
	 * @param num_splitPage
	 *
	 * @author chenjie
	 * @date 2012-4-18
	 * @return
	 */
	public List queryDeviceByKdname(String gw_type, String cityId,
			String kdname, int curPage_splitPage, int num_splitPage) {
		logger.debug("queryDeviceByKdname({},{},{})", new Object[] { gw_type,
				cityId, kdname });
		PrepareSQL psql = new PrepareSQL();
		if (DBUtil.GetDB() == 2) {
			psql.append("select top " + maxNum);
		} else {
			psql.append("select");
		}// TODO wait (more table related)
		psql.append(" c.device_id, c.oui, c.device_serialnumber, c.device_name, c.city_id," +
				"c.office_id, c.complete_time, c.zone_id, c.buy_time, c.staff_id, " +
				"c.remark, c.loopback_ip, c.interface_id, c.device_status, c.gather_id, " +
				"c.devicetype_id, c.maxenvelopes, c.cr_port, c.cr_path, " +
				"c.cpe_mac, c.cpe_currentupdatetime, c.cpe_allocatedstatus, c.cpe_username, " +
				"c.cpe_passwd, c.acs_username, c.acs_passwd, c.device_type, c.x_com_username, " +
				"c.x_com_passwd, c.gw_type, c.device_model_id, c.customer_id,  " +
				"d.vendor_add, e.device_model, f.softwareversion,f.is_awifi ");
		psql.append(" from tab_hgwcustomer a, hgwcust_serv_info b, tab_gw_device c, tab_vendor d, ");
		psql.append(" gw_device_model e, tab_devicetype_info f");
		psql.append(" where a.device_id=c.device_id and a.user_id = b.user_id ");
		psql.append("   and c.vendor_id = d.vendor_id");
		psql.append("   and c.device_model_id = e.device_model_id ");
		psql.append("   and c.devicetype_id = f.devicetype_id");
		psql.append("   and c.device_status = 1"); // 设备已确认

		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append("   and c.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}

		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			psql.append(" and c.gw_type = " + gw_type);
		}

		// 宽带账号
		if (!StringUtil.IsEmpty(kdname)) {
			psql.append(" and b.serv_type_id = 10 and b.username='" + kdname
					+ "'");
		}
		if (DBUtil.GetDB() == 1) {
			psql.append(" and rownum< " + maxNum);
		}else if (DBUtil.GetDB() == 3)
		{// mysql
			psql.append(" limit " + maxNum);
		}
		return querySP(psql.toString(),
				(curPage_splitPage - 1) * num_splitPage, num_splitPage,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						return resultSet2Map(map, rs);
					}
				});
	}

	/**
	 * 查询设备列表(单独根据设备表的条件查询)
	 *
	 * @param areaId
	 * @param queryParam
	 * @return
	 */
	public int queryDeviceCount(String gw_type, long areaId, String cityId,
			String username) {
		logger.debug("GwDeviceQueryDAO=>queryDeviceCount()");
		PrepareSQL pSQL = new PrepareSQL();
		String tableName = "tab_hgwcustomer";
		if (!StringUtil.IsEmpty(gw_type) && "2".equals(gw_type)) {
			tableName = "tab_egwcustomer";
		}// TODO wait (more table related)
		pSQL.setSQL("select count(*) from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,");
		pSQL.append(tableName);
		pSQL.append(" e where a.device_id=e.device_id and a.device_status =1 and  a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (null != cityId && !"null".equals(cityId) && !"".equals(cityId)
				&& !"-1".equals(cityId) && !"00".equals(cityId)) {
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('"
					+ StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		if (null != username && !"".equals(username)) {
			if ("1".equalsIgnoreCase(LipossGlobals
					.getLipossProperty("isLikeQuery"))) {
				pSQL.append(" and e.username like '%");
				pSQL.append(username);
				pSQL.append("' ");
				String user_sub_name = username;
				if (username.length() > 6) {
					user_sub_name = username.substring(username.length() - 6);
				}
				pSQL.append(" and e.user_sub_name ='");
				pSQL.append(user_sub_name);
				pSQL.append("'");
			} else {
				pSQL.appendAndString("e.username", PrepareSQL.EQUEAL, username);
			}
		}

		if (null != gw_type && !"".equals(gw_type) && !"null".equals(gw_type)) {
			pSQL.append(" and a.gw_type = " + gw_type);
		}
		return jt.queryForInt(pSQL.toString());
	}

	/**
	 * 数据转换
	 *
	 * @param map
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> resultSet2Map(Map<String, String> map,
			ResultSet rs) {
		try {
			map.put("device_id", rs.getString("device_id"));
			map.put("oui", rs.getString("oui"));
			map.put("device_serialnumber", rs.getString("device_serialnumber"));
			map.put("device_name", rs.getString("device_name"));
			map.put("city_id", rs.getString("city_id"));
			map.put("city_name",
					CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
			map.put("office_id", rs.getString("office_id"));
			map.put("complete_time",
					new DateTimeUtil(rs.getLong("complete_time") * 1000)
							.getYYYY_MM_DD_HH_mm_ss());
			map.put("zone_id", rs.getString("zone_id"));
			map.put("buy_time", new DateTimeUtil(rs.getLong("buy_time") * 1000)
					.getYYYY_MM_DD_HH_mm_ss());
			map.put("staff_id", rs.getString("staff_id"));
			map.put("remark", rs.getString("remark"));
			map.put("loopback_ip", rs.getString("loopback_ip"));
			map.put("interface_id", rs.getString("interface_id"));
			map.put("device_status", rs.getString("device_status"));
			map.put("gather_id", rs.getString("gather_id"));
			map.put("devicetype_id", rs.getString("devicetype_id"));
			map.put("softwareversion", rs.getString("softwareversion"));
			map.put("maxenvelopes", rs.getString("maxenvelopes"));
			map.put("cr_port", rs.getString("cr_port"));
			map.put("cr_path", rs.getString("cr_path"));
			map.put("cpe_mac", rs.getString("cpe_mac"));
			map.put("cpe_currentupdatetime",
					new DateTimeUtil(rs.getLong("cpe_currentupdatetime") * 1000)
							.getYYYY_MM_DD_HH_mm_ss());
			map.put("cpe_allocatedstatus", rs.getString("cpe_allocatedstatus"));
			map.put("cpe_username", rs.getString("cpe_username"));
			map.put("cpe_passwd", rs.getString("cpe_passwd"));
			map.put("acs_username", rs.getString("acs_username"));
			map.put("acs_passwd", rs.getString("acs_passwd"));
			map.put("device_type", rs.getString("device_type"));
			map.put("x_com_username", rs.getString("x_com_username"));
			map.put("x_com_passwd", rs.getString("x_com_passwd"));
			map.put("gw_type", rs.getString("gw_type"));
			map.put("device_model_id", rs.getString("device_model_id"));
			map.put("device_model", rs.getString("device_model"));
			map.put("customer_id", rs.getString("customer_id"));
			map.put("device_url", rs.getString("device_url"));
			map.put("x_com_passwd_old", rs.getString("x_com_passwd_old"));
			map.put("vendor_id", rs.getString("vendor_id"));
			map.put("vendor_add", rs.getString("vendor_add"));
			map.put("is_awifi", rs.getString("is_awifi"));
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return map;
	}

	/**
	 * 数据转换
	 *
	 * @param map
	 * @param rs
	 * @return
	 * @throws SQLException
	 *
	 */
	public Map<String, String> resultSet2Map4Wireless(Map<String, String> map,
			ResultSet rs) {
		try {
			//map.put("loid", rs.getString("loid"));
			//map.put("old_oui", rs.getString("old_oui"));
			map.put("username",StringUtil.getStringValue(rs.getString("username")));
			map.put("city_name",StringUtil.getStringValue(rs.getString("cityname")));
			map.put("old_devsn", rs.getString("old_devsn"));
			map.put("new_devsn", rs.getString("new_devsn"));
			map.put("new_mac", rs.getString("new_mac"));
			map.put("old_mac", rs.getString("old_mac"));
			map.put("old_vendor_name", rs.getString("vendor_name"));
			map.put("old_device_model", rs.getString("device_model"));

			long b = rs.getLong("binddate");
			if (b != 0) {
				map.put("binddate",
						DateUtil.transTime(b, "yyyy-MM-dd HH:mm:ss"));
			} else {
				map.put("binddate", "");
			}
			// TODO wait (more table related)
			String sql1 = " select c.vendor_name,d.device_model, e.hardwareversion,e.softwareversion "
					+ " from stb_tab_dev_bindrecord b,"
					+ " stb_tab_vendor c, stb_gw_device_model d,stb_tab_devicetype_info e where"
					+ " b.new_vendor_id=c.vendor_id and b.new_model_id=d.device_model_id "
					+ " and b.new_devicetype_id=e.devicetype_id ";

			PrepareSQL psql1 = new PrepareSQL(sql1.toString());
			psql1.appendAndString("b.username", PrepareSQL.EQUEAL,
					(String) map.get("username"));
			Map a = queryForMap(psql1.getSQL());
			if (null != a) {
				map.put("vendor_name",
						StringUtil.getStringValue(a.get("vendor_name")));
				map.put("device_model",
						StringUtil.getStringValue(a.get("device_model")));
				map.put("hardwareversion",
						StringUtil.getStringValue(a.get("hardwareversion")));
				map.put("softwareversion",
						StringUtil.getStringValue(a.get("softwareversion")));
			} else {
				map.put("vendor_name", "");
				map.put("device_model", "");
				map.put("hardwareversion", "");
				map.put("softwareversion", "");
			}
			a = null;

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return map;
	}

}
