
package com.linkage.module.bbms.report.dao;

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
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 端口流量按设备查询
 *
 * @author onelinesky
 * @date 2011-1-15
 */
public class DeviceFluxQueryDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DeviceFluxQueryDAO.class);

	Map<String,Map<String,String>> deviceInfoMap = new HashMap<String,Map<String,String>>();

	/**
	 * 判断报表是否存在
	 *
	 * @author onelinesky
	 * @date 2011-1-15
	 * @return int
	 */
	public int isHaveTable(String tableName) {

		logger.debug("isHaveTable({})",tableName);

		PrepareSQL pSQL = new PrepareSQL();
		if (DBUtil.GetDB() == Global.DB_ORACLE)
		{// oracle
			pSQL.setSQL("select count(*) from user_tables where table_name=?");
			pSQL.setString(1, StringUtil.getUpperCase(tableName));
		}
		else if (DBUtil.GetDB() == Global.DB_SYBASE)
		{// sybase
			pSQL.setSQL("select count(*) from sysobjects where name =?");
			pSQL.setString(1, tableName);
		}else if (DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			pSQL.setSQL("select count(*) from information_schema.tables where table_name =?");
			pSQL.setString(1, tableName);
		}else{
			pSQL.setSQL("select count(*) from sysobjects where name =?");
			pSQL.setString(1, tableName);
		}

		return jt.queryForInt(pSQL.getSQL());

	}


	/**
	 * 查询设备流量报表
	 *
	 * @author onelinesky
	 * @date 2011-1-15
	 * @param
	 * @return List<Map>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getFlux(String tableName, List<String> deviceId,
			long startDate,long endDate) {
		logger.debug("getFlux()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL("select device_id,collecttime,sum(ifinoctets) as ifinoctets," +
				" sum(ifoutoctets) as ifoutoctets from flux_day_stat_2011_1 " +
				" where device_id in (?) group by device_id,collecttime " +
				" order by device_id,collecttime");
		pSQL.setString(1, StringUtils.weave(deviceId,"','"));

		return jt.query(pSQL.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				String _deviceId = rs.getString("device_id");
				long _collecttime = rs.getLong("collecttime");
				String _ifinoctets = rs.getString("ifinoctets");
				String _ifoutoctets = rs.getString("ifoutoctets");

				map.put("device_id", _deviceId);
				map.put("collecttime", new DateTimeUtil(_collecttime*1000).getYYYY_MM_DD());
				map.put("ifinoctets", _ifinoctets);
				map.put("ifoutoctets", _ifoutoctets);
				map.putAll(deviceInfoMap.get(_deviceId));
				return map;
			}
		});
	}

	/**
	 * 根据设备序列号或者设备IP查询设备
	 *
	 * @author onelinesky
	 * @date 2011-1-15
	 * @param
	 * @return List<String>
	 */
	public List<String> getInfoByDevice(String cityId, String deviceSn,String loopbackIp) {
		logger.debug("getInfoByDevice({},{},{})",new Object[]{cityId,deviceSn,loopbackIp});
		if((null==deviceSn || "".equals(deviceSn.trim()))
				&& (null==loopbackIp || "".equals(loopbackIp.trim()))){
			return new ArrayList<String>();
		}

		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(" select a.device_id,a.device_serialnumber,b.customer_id," +
				" b.customer_name,b.username from tab_gw_device a left " +
				" join (select m.customer_id,m.customer_name,n.username," +
				" n.device_id from tab_customerinfo m,tab_egwcustomer n " +
				" where m.customer_id=n.customer_id) b on " +
				" a.device_id=b.device_id where 1=1 ");

		if(null!=cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}

		if(null!=deviceSn && !"".equals(deviceSn) && !"-1".equals(deviceSn)){
			if(deviceSn.length()>5){
				pSQL.append(" and a.dev_sub_sn ='");
				pSQL.append(deviceSn.substring(deviceSn.length()-6, deviceSn.length()));
				pSQL.append("' ");
			}
			pSQL.append(PrepareSQL.AND, "a.device_serialnumber", PrepareSQL.L_LIKE, deviceSn, false);
		}
		if(null!=loopbackIp && !"".equals(loopbackIp) && !"-1".equals(loopbackIp)){
			pSQL.append(PrepareSQL.AND, "a.loopback_ip", PrepareSQL.EQUEAL, loopbackIp, false);
		}

		List list = jt.queryForList(pSQL.getSQL());
		List<String> deviceList = new ArrayList<String>();
		Map one = null;
		for(int i=0;i<list.size();i++){
			one = (Map)list.get(i);
			String _deviceId = String.valueOf(one.get("device_id"));
			String _deviceSn = String.valueOf(one.get("device_serialnumber"));
			String _customerId = String.valueOf(one.get("customer_id"));
			String _customerName = String.valueOf(one.get("customer_name"));
			String _username = String.valueOf(one.get("username"));
			if("null".equals(_customerId)){
				_customerId = "";
			}
			if("null".equals(_customerName)){
				_customerName = "";
			}
			if("null".equals(_username)){
				_username = "";
			}

			deviceList.add(_deviceId);
			Map<String,String> _tempMap = deviceInfoMap.get(_deviceId);
			if(null == _tempMap){
				_tempMap = new HashMap<String,String>();
			}
			_tempMap.put("device_id", _deviceId);
			_tempMap.put("device_serialnumber", _deviceSn);
			_tempMap.put("customer_id", _customerId);
			_tempMap.put("customer_name", _customerName);
			_tempMap.put("username", _username);
			deviceInfoMap.put(_deviceId,_tempMap);
		}
		return deviceList;
	}

	/**
	 * 根据用户帐号查询设备
	 *
	 * @author onelinesky
	 * @date 2011-1-15
	 * @param
	 * @return List<String>
	 */
	public List<String> getInfoByUser(String cityId, String userName) {
		logger.debug("getInfoByUser({},{})",cityId,userName);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(" select b.device_id,b.device_serialnumber,a.customer_id," +
				" a.customer_name,b.username from tab_customerinfo a," +
				" tab_egwcustomer b where a.customer_id=b.customer_id " +
				" and b.device_id!=null and b.username=? ");
		pSQL.setString(1, userName);
		if(null!=cityId && !"null".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSQL.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		List list = jt.queryForList(pSQL.getSQL());
		List<String> deviceList = new ArrayList<String>();
		Map one = null;
		for(int i=0;i<list.size();i++){
			one = (Map)list.get(i);
			String _deviceId = String.valueOf(one.get("device_id"));
			String _deviceSn = String.valueOf(one.get("device_serialnumber"));
			String _customerId = String.valueOf(one.get("customer_id"));
			String _customerName = String.valueOf(one.get("customer_name"));
			String _username = String.valueOf(one.get("username"));
			deviceList.add(_deviceId);
			Map<String,String> _tempMap = deviceInfoMap.get(_deviceId);
			if(null == _tempMap){
				_tempMap = new HashMap<String,String>();
			}
			_tempMap.put("device_id", _deviceId);
			_tempMap.put("device_serialnumber", _deviceSn);
			_tempMap.put("customer_id", _customerId);
			_tempMap.put("customer_name", _customerName);
			_tempMap.put("username", _username);
			deviceInfoMap.put(_deviceId,_tempMap);
		}
		return deviceList;
	}
}
