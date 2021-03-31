
package com.linkage.module.gtms.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class VoipChangeCountDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(VoipChangeCountDAO.class);

	public List<Map> queryVoipChangeInfo(String serviceId)
	{
		logger.debug("VoipChangeCountDAO--queryVoipChangeInfo({})", serviceId);
		StringBuffer sql = new StringBuffer();
		sql.append("select t.city_id,t.set_result,count(*) as total from tab_voip_change_report T group by  t.city_id,t.set_result");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		logger.warn("psql:[]",psql.getSQL());
		return jt.queryForList(psql.getSQL());
	}

	public List<Map> getDevList(String serviceId, String status, String cityId, int curPage_splitPage, int num_splitPage)
	{
		String sql = appendSqlMethod(serviceId, status, cityId);
		PrepareSQL psql = new PrepareSQL(sql);

		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, num_splitPage);

		return list;
	}

	private String appendSqlMethod(String serviceId, String status, String cityId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.username, t.prox_serv, t.prox_port, t.stand_prox_serv, t.stand_prox_port, t.voip_port, t.rtp_prefix, ");
		sql.append("        T1.CITY_NAME, ");
		sql.append("        CASE t.reg_id_type ");
		sql.append("          WHEN 1 THEN ");
		sql.append("           'DomainName' ");
		sql.append("          WHEN 0 THEN ");
		sql.append("           'IP' 　　ELSE '' ");
		sql.append("        END AS reg_id_type_itms ");
		sql.append("   from tab_voip_change_report T ");
		sql.append("   LEFT JOIN TAB_CITY T1 ");
		sql.append("     ON T.CITY_ID = T1.CITY_ID ");
		sql.append("  where 1=1 ");
		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId)))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and T.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!"".equals(status)&&status!=null)
		{
			sql.append(" and T.set_result= "+status);
		}

		return sql.toString();
	}

	public int getDevCount(String serviceId, String status, String cityId, int curPage_splitPage, int num_splitPage)
	{
		logger.warn("getDevCount(serviceId:{},status:{},cityId:{})", new Object[] { serviceId, status, cityId });
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from tab_voip_change_report T LEFT JOIN TAB_CITY T1 ON T.CITY_ID = T1.CITY_ID WHERE 1=1 ");
		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId)))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and T.city_id in (").append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!"".equals(status)&&status!=null)
		{
			sql.append(" and T.set_result= "+status);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = this.jt.queryForInt(psql.getSQL());
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

	public List<Map> getDevExcel(String serviceId1, String status, String cityId)
	{
		String sql = appendSqlMethod(serviceId1, status, cityId);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = this.jt.queryForList(psql.getSQL());
		logger.debug("devList:" + list.toString());
		return list;
	}
}
