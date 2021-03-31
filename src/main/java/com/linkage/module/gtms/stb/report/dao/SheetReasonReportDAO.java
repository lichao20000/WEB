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
public class SheetReasonReportDAO extends SuperDAO{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SheetReasonReportDAO.class);
	
	/**
	 * 根据错误码分组
	 * 
	 * @param cityId
	 * @param endOpenDate
	 * @param startOpenDate
	 * @return <00(city_id), <succNum=1, failNum=2, notNum=3>>
	 */
	public List<Map<String,String>> getStatsGroupbyCode(String cityId, String startOpenDate, String endOpenDate) 
	{
		logger.debug("getStatsGroupByCity({},{},{})", new Object[]{cityId, startOpenDate, endOpenDate});
		
		StringBuffer sb = new StringBuffer();
		if(StringUtil.IsEmpty(cityId)||"00".equals(cityId)){
			sb.append("select error_code,city_id, count(*) as num from tab_bss_sheet where 1=1 ");
		}
		else{
			sb.append("select error_code,city_id, count(*) as num from tab_bss_sheet where city_id='"+cityId+"'");
		}
		
		if (!StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and RECEIVE_DATE>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and RECEIVE_DATE<=").append(endOpenDate);
		}
		
		sb.append(" group by error_code,city_id");

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
	
	
	public List<Map<String,String>> getStats(String cityId, String startOpenDate, String endOpenDate) 
	{
		logger.debug("getStats({},{},{})", new Object[]{cityId, startOpenDate, endOpenDate});
		
		StringBuffer sb = new StringBuffer();
		if(StringUtil.IsEmpty(cityId)||"00".equals(cityId)){
			sb.append("select result, count(*) as num from tab_bss_sheet where 1=1 ");
		}
		else{
			sb.append("select result, count(*) as num from tab_bss_sheet where city_id='"+cityId+"'");
		}
		
		if (!StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and RECEIVE_DATE>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and RECEIVE_DATE<=").append(endOpenDate);
		}
		
		sb.append(" group by result");

		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		return list;
	}
	
	
}
