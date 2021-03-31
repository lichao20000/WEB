package com.linkage.module.gtms.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class SetLoidDAOImpl extends SuperDAO implements SetLoidDAO {
	private static Logger logger = LoggerFactory
			.getLogger(SetLoidDAOImpl.class);

	private Map<String, String> cityMap = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List queryDeviceDetail(String loid, String deviceNumber,
			String startTime, String endTime, String statu) {
		PrepareSQL sb = new PrepareSQL();// TODO wait (more table related)
		sb.append(" select a.city_id,c.oui,a.order_time,b.loid,b.device_serialnumber,b.status,b.update_time from gw_serv_setloid a , gw_setloid_device b,tab_gw_device c ");
		sb.append(" where a.task_id=b.task_id ");
		sb.append(" and b.device_id=c.device_id  ");
		sb.append(" and a.order_time>=? ");
		sb.append(" and a.order_time<? ");
		if (!StringUtil.IsEmpty(statu)) {
			sb.append(" and b.status= " + statu);
		}
		if (!StringUtil.IsEmpty(deviceNumber)) {
			sb.append(" and b.device_serialnumber like '%" + deviceNumber
					+ "%'");
		}
		if (!StringUtil.IsEmpty(loid)) {
			sb.append(" and b.loid like '%" + loid + "%'");
		}
		sb.setInt(1, StringUtil.getIntegerValue(startTime));
		sb.setInt(2, StringUtil.getIntegerValue(endTime));
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = jt.query(sb.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				map.put("oui", rs.getString("oui"));
				map.put("loid", rs.getString("loid"));
				String status = rs.getString("status");
				if ("0".equals(status)) {
					status = "未做";
				} else if ("1".equals(status)) {
					status = "成功";
				} else {
					status = "失败";
				}
				map.put("status", status);
				map.put("device_serialnumber",
						rs.getString("device_serialnumber"));
				try {
					long update_time = StringUtil.getLongValue(rs
							.getString("update_time"));
					DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
					map.put("update_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("update_time", "");
				} catch (Exception e) {
					map.put("update_time", "");
				}
				return map;
			}

		});
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List queryDeviceDetail(String loid, String deviceNumber,
			String startTime, String endTime, String statu,
			int curPage_splitPage, int num_splitPage) {
		PrepareSQL sb = new PrepareSQL();// TODO wait (more table related)
		sb.append(" select a.city_id,c.oui,a.order_time,b.loid,b.device_serialnumber,b.status,b.update_time from gw_serv_setloid a , gw_setloid_device b,tab_gw_device c ");
		sb.append(" where a.task_id=b.task_id ");
		sb.append(" and b.device_id=c.device_id  ");
		sb.append(" and a.order_time>=? ");
		sb.append(" and a.order_time<? ");
		if (!StringUtil.IsEmpty(statu)) {
			sb.append(" and b.status= " + statu);
		}
		if (!StringUtil.IsEmpty(deviceNumber)) {
			sb.append(" and b.device_serialnumber like '%" + deviceNumber
					+ "%'");
		}
		if (!StringUtil.IsEmpty(loid)) {
			sb.append(" and b.loid like '%" + loid + "%'");
		}
		sb.setInt(1, StringUtil.getIntegerValue(startTime));
		sb.setInt(2, StringUtil.getIntegerValue(endTime));
		cityMap = CityDAO.getCityIdCityNameMap();

		List<Map> list = querySP(sb.getSQL(),
				(curPage_splitPage - 1) * num_splitPage + 1, num_splitPage,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {

				Map<String, String> map = new HashMap<String, String>();
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				map.put("oui", rs.getString("oui"));
				map.put("loid", rs.getString("loid"));
				String status = rs.getString("status");
				if ("0".equals(status)) {
					status = "未做";
				} else if ("1".equals(status)) {
					status = "成功";
				} else {
					status = "失败";
				}
				map.put("status", status);
				map.put("device_serialnumber",
						rs.getString("device_serialnumber"));
				try {
					long update_time = StringUtil.getLongValue(rs
							.getString("update_time"));
					DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
					map.put("update_time", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("update_time", "");
				} catch (Exception e) {
					map.put("update_time", "");
				}
				return map;
			}

		});
		return list;
	}

	@Override
	public int getCount(String loid, String deviceNumber, String startTime,
			String endTime, String statu, int curPage_splitPage,
			int num_splitPage) {
		PrepareSQL sb = new PrepareSQL();// TODO wait (more table related)
		sb.append(" select count(*) from gw_serv_setloid a , gw_setloid_device b,tab_gw_device c ");
		sb.append(" where a.task_id=b.task_id ");
		sb.append(" and b.device_id=c.device_id  ");
		sb.append(" and a.order_time>=? ");
		sb.append(" and a.order_time<? ");
		if (!StringUtil.IsEmpty(statu)) {
			sb.append(" and b.status= " + statu);
		}
		if (!StringUtil.IsEmpty(deviceNumber)) {
			sb.append(" and b.device_serialnumber like '%" + deviceNumber
					+ "%'");
		}
		if (!StringUtil.IsEmpty(loid)) {
			sb.append(" and b.loid like '%" + loid + "%'");
		}
		sb.setInt(1, StringUtil.getIntegerValue(startTime));
		sb.setInt(2, StringUtil.getIntegerValue(endTime));

		int total = jt.queryForInt(sb.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 *
	 */
	@Override
	public String getDeviceId(String oui, String device_serialnumber) {
		logger.debug("getDeviceId()");
		PrepareSQL psql = new PrepareSQL(
				"select device_id,device_serialnumber from tab_gw_device where oui=? and device_serialnumber=?");
		psql.setString(1, oui);
		psql.setString(2, device_serialnumber);
		return StringUtil.getStringValue(jt.queryForMap(psql.getSQL()).get(
				"device_id"));
	}

	/**
	 * 查询device_id和device_serialnumber
	 *
	 * @param deviceStrs
	 * @return
	 */
	public List getDeviceList(String deviceStrs) {
		logger.debug("getDeviceId()");
		PrepareSQL psql = new PrepareSQL(
				"select device_id ,device_serialnumber from tab_gw_device where device_serialnumber in "
						+ deviceStrs);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 插入一条定制任务
	 *
	 */
	public void addSetLoidTask(Long taskId, String cityId, long userId) {
		String sql = "";
		sql = "insert into gw_serv_setloid values (" + taskId + "," + taskId
				+ ",'" + cityId + "'," + userId + ",0)";
		PrepareSQL psql = new PrepareSQL(sql);
		jt.update(psql.getSQL());
	}

	/**
	 * 批量插入定制的设备
	 */
	public void doInsertDevices(ArrayList<String> sqllist) {
		try {
			jt.batchUpdate((String[]) sqllist.toArray(new String[0]));
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

}
