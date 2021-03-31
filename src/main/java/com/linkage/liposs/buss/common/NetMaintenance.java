package com.linkage.liposs.buss.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;

/**
 * @author liuw(5153)
 * @version 1.0
 * @since 2007-8-9
 * @category 首页显示逻辑处理类
 * 
 */
public class NetMaintenance {
	private DataSource dao;

	private JdbcTemplate jt;

	private ArrayList list = null;


	/**
	 * 首页显示告警信息内容的方法.此方法还响应查询操作,如页面不设置参数即设备名称,将查询所有设备,如填写,将模糊匹配填写的内容进行查询。
	 * 
	 * @param areaid
	 *            根据域过滤出当前帐号所属设备查询范围
	 * @param deviceName
	 *            根据设备名称可以模糊匹配查询告警信息
	 * @return ArrayList list中存放的是map
	 */

	public ArrayList getNetMaintenanceContext(long areaid, String deviceName) {
		Date currDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String today = formatter.format(currDate);
		java.sql.Date start = java.sql.Date.valueOf(today);
		String queryDate = start.toString().replaceAll("-", "");
		String queryDevName = "";
		if (!deviceName.equals("")) {
			queryDevName = "and device_name like '%" + deviceName + "%'";
		}
		jt = new JdbcTemplate(dao);
		String strSQL = "select * from tab_taskplan_data where device_id in(select res_id from tab_gw_res_area where area_id="
				+ areaid
				+ " and res_type=1) and table_id like '%"
				+ queryDate
				+ "%' " + queryDevName + "";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select device_id, device_ip, device_name, resource_type_id, table_id, createtime, cpu_util, mem_util, " +
					" ping_value, temp_value, severity " +
					" from tab_taskplan_data where device_id in(select res_id from tab_gw_res_area where area_id="
					+ areaid
					+ " and res_type=1) and table_id like '%"
					+ queryDate
					+ "%' " + queryDevName + "";
		}
		list = new ArrayList();
		PrepareSQL psql = new PrepareSQL(strSQL);
		jt.query(psql.getSQL(), new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {

				HashMap nameMap = getDeviceResName();
				HashMap map = new HashMap();
				map.put("device_id", rs.getString("device_id"));
				map.put("device_ip", rs.getString("device_ip"));
				map.put("device_name", rs.getString("device_name"));
				map.put("resource_type_id", (String) nameMap.get(String
						.valueOf(rs.getInt("resource_type_id"))));
				map.put("table_id", rs.getString("table_id"));
				map.put("createtime", rs.getInt("createtime"));
				map.put("cpu_util", rs.getInt("cpu_util"));
				map.put("mem_util", rs.getInt("mem_util"));
				map.put("ping_value", rs.getInt("ping_value"));
				map.put("temp_value", rs.getInt("temp_value"));
				map.put("severity", rs.getInt("severity"));
				map.put("gather_id", getDeviceGatherId(rs
						.getString("device_id")));
				list.add(map);
			}
		});
		return list;
	}
	/**
	 * @author suixz
	 * 返回采集区域,显示在首页采集区域下拉列表中
	 * @param areaId
	 * @return
	 */
	public ArrayList getAreaList(String areaId) {
		String sql = "";
		if ("1".equals(areaId)) {
			sql = "select area_id,remark from tab_area where area_id in(select distinct area_id from tab_gw_res_area where res_type=1)";
		} else {
			sql = "select area_id,remark from tab_area where area_id="
					+ areaId
					+ " or area_pid="
					+ areaId
					+ " and area_id in(select distinct area_id from tab_gw_res_area where res_type=1)";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		list = (ArrayList) jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 根据设备的deviceID获取采集点gatherID
	 * 
	 * @param deviceID
	 * @return String
	 */
	public String getDeviceGatherId(String deviceID) {

		String gatherid = "";
		List list;
		String strSQL = "select gather_id from tab_gw_device where device_id='"
				+ deviceID + "'";
		PrepareSQL psql = new PrepareSQL(strSQL);
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				gatherid = (String) ((Map) list.get(i)).get("gather_id");
			}
		} else {
			gatherid = "";
		}

		return gatherid;
	}

	/**
	 * 所有资源名称
	 * 
	 * @return HashMap key：resource_type_id value：resource_name
	 */
	public HashMap getDeviceResName() {
		List list;
		HashMap resMap = null;
		String resNameSQL = "select resource_type_id,resource_name from tab_resourcetype";
		PrepareSQL psql = new PrepareSQL(resNameSQL);
		list = jt.queryForList(psql.getSQL());
		resMap = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			resMap.put(((Map) list.get(i)).get("resource_type_id").toString(),
					((Map) list.get(i)).get("resource_name").toString());
		}
		return resMap;

	}

	public void setDao(DataSource dao) {
		this.dao = dao;
	}

}
