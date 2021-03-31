package com.linkage.module.gtms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class ConfigRateReportDaoImpl implements ConfigRateReportDao {

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(ConfigRateReportDaoImpl.class);

	private JdbcTemplate jt;

	/**
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}

	private Map<String, String> cityMap = null;

	@SuppressWarnings("unchecked")
	public List<Map> countSuccessRate(String endtime1,
			String cityId) {
		logger.debug("countSuccessRate({},{},{},{},{},{})", new Object[] {
				cityId, endtime1, cityId });
		
		List<String> allCityList = CityDAO.getAllNextCityIdsByCityPid(cityId);//00 011 012 0111 0121 

		Collections.sort(allCityList);
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sb = new StringBuffer(
				"Select city_id, count_time, total_count as total, succ_count as succ  "
						+ "from gw_serv_strategy_succ_report where city_id in (" 
						+ StringUtils.weave(allCityList) + ")");
						
		if (false == StringUtil.IsEmpty(endtime1)) {
			sb.append(" and  count_time =").append(endtime1);
		}
		//group by 后面新增total_count,succ_count 字段，兼容oracle语句，对sybase语句无影响
		sb.append(" group by city_id, count_time,total_count,succ_count");
		psql.append(sb.toString());
		List<Map> list = jt.query(psql.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1)
					throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("cityId", rs.getString("city_id"));
				map.put("total", rs.getString("total"));
				map.put("succ", rs.getString("succ"));
				return map;
			}
		});
		return list;
	}
}
