
package com.linkage.module.itms.resource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class StbSheetSuccDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(StbSheetSuccDAO.class);

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getCountNum(String cityId, String starttime,
			String endtime, String queryType)
	{
		logger.debug("StbSheetSuccDAO({},{},{},{})", new Object[] { cityId, starttime,
				endtime, queryType });
		PrepareSQL psql = new PrepareSQL(
				"select a.city_id,a.stbuptyle,count(1) as num from stb_tab_customer a "
						+ "where a.stbuptyle is not null");
		if ("1".equals(queryType))
		{
			psql.append(" and a.user_status = 1");
		}
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			psql.append(" and a.openuserdate >= "
					+ new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			psql.append(" and a.openuserdate <= "
					+ new DateTimeUtil(endtime).getLongTime());
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			ArrayList<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and a.city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		psql.append(" group by a.city_id,a.stbuptyle ");
		return jt.queryForList(psql.getSQL());
	}
}
