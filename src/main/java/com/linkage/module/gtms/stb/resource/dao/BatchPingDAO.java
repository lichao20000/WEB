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
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.system.utils.StringUtils;


public class BatchPingDAO extends SuperDAO {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BatchPingDAO.class);

	/**
	 * 批量ping数据查询
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param cityId
	 * @param loopbackIp
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> queryData(int curPage_splitPage, int num_splitPage,
			String cityId, String deviceIp, String result, String startTime, String endTime) {
		PrepareSQL pSql = new PrepareSQL();

		pSql.setSQL("select a.device_ip, a.device_name, a.city_name, a.result, a.result_desc, a.operate_time" +
				" a.succes_num, a.fail_num, a.packet_loss_rate, a.min_response_time, a.avg_response_time, a.max_response_time, a.city_id " +
				" from stb_batch_ping a where 1 = 1");

		if (!CityDAO.isAdmin(cityId)) {
			ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSql.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}

		if (!StringUtil.IsEmpty(deviceIp) && !"-1".equals(deviceIp)) {
			pSql.append(" and a.device_ip = '" + deviceIp + "' ");
		}

		if (!StringUtil.IsEmpty(result)) {
			pSql.append(" and a.result = '" + result + "' ");
		}
		if (!StringUtil.IsEmpty(startTime)) {
			pSql.append(" and a.operate_time >= " + new DateTimeUtil(startTime).getLongTime() + "");
		}
		if (!StringUtil.IsEmpty(endTime)) {
			pSql.append(" and a.operate_time <= " + new DateTimeUtil(endTime).getLongTime() + "");
		}
		if (-1 != curPage_splitPage) {
			return querySP(pSql.getSQL(), (curPage_splitPage - 1)
					* num_splitPage, num_splitPage, new RowMapper() {

				public Object mapRow(ResultSet rs, int arg1)
						throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					return resultSet2Map(map, rs);
				}
			});
		} else {
			return jt.query(pSql.getSQL(), new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1)
						throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					return resultSet2Map(map, rs);
				}
			});
		}
	}

	/**
	 * 查询记录数
	 * @param cityId
	 * @param deviceIp
	 * @return
	 */
	public int queryCount(String cityId, String deviceIp, String result, String startTime, String endTime) {
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select count(*) from stb_batch_ping a where 1=1 ");

		if (!CityDAO.isAdmin(cityId)) {
			ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSql.append(" and a.city_id in ('" + StringUtils.weave(cityArray, "','")
					+ "')");
			cityArray = null;
		}
		if (!StringUtil.IsEmpty(deviceIp) && !"-1".equals(deviceIp)) {
			pSql.append(" and a.device_ip = '" + deviceIp + "' ");
		}
		if (!StringUtil.IsEmpty(result)) {
			pSql.append(" and a.result = '" + result + "' ");
		}
		if (!StringUtil.IsEmpty(startTime)) {
			pSql.append(" and a.operate_time >= " + new DateTimeUtil(startTime).getLongTime() + "");
		}
		if (!StringUtil.IsEmpty(endTime)) {
			pSql.append(" and a.operate_time <= " + new DateTimeUtil(endTime).getLongTime() + "");
		}
		return jt.queryForInt(pSql.getSQL());
	}

	/**
	 * 数据转换
	 *
	 * @param map
	 * @param rs
	 * @return
	 */
	public Map<String, String> resultSet2Map(Map<String, String> map,
			ResultSet rs) {
		try {
			map.put("device_ip", rs.getString("device_ip"));
			map.put("device_name", rs.getString("device_name"));
			if ("1".equals(rs.getString("result"))) {
				map.put("result", "成功");
			} else {
				map.put("result", "失败");
			}
			map.put("result_desc", rs.getString("result_desc"));
			map.put("operate_time", new DateTimeUtil(
					rs.getLong("operate_time") * 1000).getYYYY_MM_DD_HH_mm_ss());
			map.put("succes_num", rs.getString("succes_num"));
			map.put("fail_num", rs.getString("fail_num"));
			map.put("packet_loss_rate", rs.getString("packet_loss_rate"));
			map.put("min_response_time", rs.getString("min_response_time"));
			map.put("avg_response_time", rs.getString("avg_response_time"));
			map.put("max_response_time", rs.getString("max_response_time"));
			map.put("city_id", rs.getString("city_id"));
			map.put("city_name",
					CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		return map;
	}
}
