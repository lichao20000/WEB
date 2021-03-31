/**
 *
 */
package com.linkage.module.gtms.stb.report.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 工单报表
 * fanjm
 * @author HP (AILK No.)
 * @version 1.0
 * @since 2018-4-20
 * @category com.linkage.module.gtms.stb.report.dao
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class SheetDailyCustReportDAO extends SuperDAO{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SheetDailyCustReportDAO.class);

	/**
	 * 根据属地分组获取全部时间范围内的用户
	 * @param cityId
	 * @param startOpenDate
	 * @param endOpenDate
	 * @return
	 */
	public List<Map<String,String>> getStatsGroupbyCity(String cityId, String startOpenDate, String endOpenDate)
	{
		logger.debug("getStatsGroupByCity({},{},{})", new Object[]{cityId, startOpenDate, endOpenDate});

		StringBuffer sb = new StringBuffer();
		if(StringUtil.IsEmpty(cityId)||"00".equals(cityId)){
			sb.append("select city_id, count(*) as num from stb_tab_customer where user_status=1 ");
		}
		else{
			sb.append("select city_id, count(*) as num from stb_tab_customer where city_id='"+cityId+"' and user_status=1 ");
		}

		if (!StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and opendate>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and opendate<=").append(endOpenDate);
		}

		sb.append(" group by city_id");

		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());

		return list;
	}


	/*public Map<String, String> getErrorMap(){


		String strSQL = "select error_code, error_desc from tab_bss_sheet_error order by error_code";
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		Map map = DataSetBean.getMap(strSQL);
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}*/


	public int getStats(String cityId, String startOpenDate, String endOpenDate)
	{
		logger.debug("getStats({},{},{})", new Object[]{cityId, startOpenDate, endOpenDate});

		StringBuffer sb = new StringBuffer();
		if(StringUtil.IsEmpty(cityId)||"00".equals(cityId)){
			sb.append("select count(*) as num from stb_tab_customer where user_status=1 ");
		}
		else{
			sb.append("select count(*) as num from stb_tab_customer where city_id='"+cityId+"' and user_status=1 ");
		}

		if (!StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and opendate>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and opendate<=").append(endOpenDate);
		}

		PrepareSQL psql = new PrepareSQL(sb.toString());
		return jt.queryForInt(psql.getSQL());
	}


}
