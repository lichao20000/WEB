package com.linkage.module.bbms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import dao.util.JdbcTemplateExtend;

/**
 * EVDO模块：EVDO数据卡绑定日志
 *
 * @author 漆学启
 * @date 2009-10-29
 */
public class EVDOBindLogDAO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(EVDOBindLogDAO.class);

	private JdbcTemplateExtend jt;

	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}

	/**
	 * 查询记录
	 *
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @param deviceNo
	 * @return
	 */
	public List getEVDOBindLog(int curPage_splitPage, int num_splitPage,
			String cityId, long startDate, long endDate, String deviceNo) {

		logger.debug("EVDOBindLogDAO=>getEVDOBindLog()");
		StringBuffer sql = new StringBuffer();

		sql.append(" select a.username,b.device_serialnumber,a.oper_type,");
		sql.append(" a.bind_type,a.binddate from bind_log a,tab_gw_device b ");
		sql.append(" where a.device_id=b.device_id and a.binddate>");
		sql.append(startDate);
		sql.append(" and a.binddate<");
		sql.append(endDate);
		if(null!=deviceNo && !"".equals(deviceNo)){
			sql.append(" and b.device_serialnumber='");
			sql.append(deviceNo);
			sql.append("' ");
		}
		if(!"00".equals(cityId)){
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in ('");
			sql.append(StringUtils.weave(list,"','"));
			sql.append("') ");
			list = null;
		}
		sql.append(" order by a.binddate ");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();

		return jt.querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> map = new HashMap<String, String>();

						map.put("esn", rs.getString("username"));
						map.put("device_serialnumber", rs.getString("device_serialnumber"));
						String oper_type = String.valueOf(rs.getString("oper_type"));
						String bind_type = String.valueOf(rs.getString("bind_type"));
						if("1".equals(oper_type)){
							map.put("oper_type","绑定");
						}else{
							map.put("oper_type","解绑");
						}
						if("2".equals(bind_type)){
							map.put("bind_type","无线卡绑定设备");
						}else{
							map.put("bind_type","用户绑定设备");
						}
						map.put("binddate", new DateTimeUtil(Long.valueOf(rs.getString("binddate")+"000")).getLongDate());

						return map;
					}
		});
	}

	/**
	 * 查询记录显示页总数
	 *
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @param deviceNo
	 * @return
	 */
	public int getEVDOBindLogCount(int curPage_splitPage, int num_splitPage,
			String cityId, long startDate, long endDate, String deviceNo) {

		logger.debug("EVDOBindLogDAO=>getEVDOBindLogCount()");
		StringBuffer sql = new StringBuffer();

		sql.append(" select count(*) from bind_log a,tab_gw_device b ");
		sql.append(" where a.device_id=b.device_id and a.binddate>");
		sql.append(startDate);
		sql.append(" and a.binddate<");
		sql.append(endDate);
		if(null!=deviceNo && !"".equals(deviceNo)){
			sql.append(" and b.device_serialnumber='");
			sql.append(deviceNo);
			sql.append("' ");
		}
		if(!"00".equals(cityId)){
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in ('");
			sql.append(StringUtils.weave(list,"','"));
			sql.append("') ");
			list = null;
		}

		logger.debug("getEVDOBindLogCount=>sql:{}",sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		int total = jt.queryForInt(sql.toString());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}

		return maxPage;
	}
}
