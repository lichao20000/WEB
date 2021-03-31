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
public class SheetDeviceReportDAO extends SuperDAO{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SheetDeviceReportDAO.class);





	/**
	 * 属地分组、查询全部设备
	 * @param cityId
	 * @param startOpenDate
	 * @param endOpenDate
	 * @return
	 */
	public List<Map<String,String>> getAllStatsGroupByCity(String cityId, String startOpenDate, String endOpenDate)
	{
		logger.debug("getStatsGroupByCity({},{},{})", new Object[]{cityId, startOpenDate, endOpenDate});

		StringBuffer sb = new StringBuffer();
		if(StringUtil.IsEmpty(cityId)||"00".equals(cityId)){
			sb.append("select city_id, count(*) as num from stb_tab_gw_device where 1=1 ");
		}
		else{
			sb.append("select city_id, count(*) as num from stb_tab_gw_device where city_id='"+cityId+"'");
		}

		if (!StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and cpe_currentupdatetime>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and cpe_currentupdatetime<=").append(endOpenDate);
		}

		sb.append(" group by city_id");
		sb.append(" order by city_id");

		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		print(list);

		return list;
	}



	/**
	 * 查询全部设备
	 * @param cityId
	 * @param startOpenDate
	 * @param endOpenDate
	 * @return
	 */
	public int getAllStats(String cityId, String startOpenDate, String endOpenDate)
	{
		logger.debug("getAllStats({},{},{})", new Object[]{cityId, startOpenDate, endOpenDate});

		StringBuffer sb = new StringBuffer();
		if(StringUtil.IsEmpty(cityId)||"00".equals(cityId)){
			sb.append("select count(*) as num from stb_tab_gw_device where 1=1 ");
		}
		else{
			sb.append("select count(*) as num from stb_tab_gw_device where city_id='"+cityId+"'");
		}

		if (!StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and cpe_currentupdatetime>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and cpe_currentupdatetime<=").append(endOpenDate);
		}

		PrepareSQL psql = new PrepareSQL(sb.toString());
		int num = jt.queryForInt(psql.getSQL());

		return num;
	}


	/**
	 * 根据属地、时间查询符合条件工单数目
	 *
	 * @param cityId
	 * @param endOpenDate
	 * @param startOpenDate
	 * @return <00(city_id), <succNum=1, failNum=2, notNum=3>>
	 */
	public List<Map<String,String>> getOnlineStatsGroupByCity(String cityId, String startOpenDate, String endOpenDate)
	{
		logger.debug("getStatsGroupByCity({},{},{})", new Object[]{cityId, startOpenDate, endOpenDate});

		StringBuffer sb = new StringBuffer();
		if(StringUtil.IsEmpty(cityId)||"00".equals(cityId)){
			sb.append("select d.city_id, count(*) as num from stb_tab_gw_device d,stb_gw_devicestatus s where d.device_id=s.device_id and s.online_status=1 ");
		}
		else{
			sb.append("select d.city_id, count(*) as num from stb_tab_gw_device d,stb_gw_devicestatus s where d.device_id=s.device_id and s.online_status=1 and d.city_id='"+cityId+"'");
		}

		if (!StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and d.cpe_currentupdatetime>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and d.cpe_currentupdatetime<=").append(endOpenDate);
		}

		sb.append(" group by d.city_id");
		sb.append(" order by d.city_id");

		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		print(list);

		return list;
	}




	/**
	 * 查询全部设备
	 * @param cityId
	 * @param startOpenDate
	 * @param endOpenDate
	 * @return
	 */
	public int getOnlineStats(String cityId, String startOpenDate, String endOpenDate)
	{
		logger.debug("getOnlineStats({},{},{})", new Object[]{cityId, startOpenDate, endOpenDate});

		StringBuffer sb = new StringBuffer();
		if(StringUtil.IsEmpty(cityId)||"00".equals(cityId)){
			sb.append("select count(*) as num from stb_tab_gw_device d,stb_gw_devicestatus s where d.device_id=s.device_id and s.online_status=1 ");
		}
		else{
			sb.append("select count(*) as num from stb_tab_gw_device d,stb_gw_devicestatus s where d.device_id=s.device_id and s.online_status=1 and d.city_id='"+cityId+"'");
		}

		if (!StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and d.cpe_currentupdatetime>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and d.cpe_currentupdatetime<=").append(endOpenDate);
		}

		PrepareSQL psql = new PrepareSQL(sb.toString());
		int num = jt.queryForInt(psql.getSQL());

		return num;
	}



	/**
	 * 根据属地、时间查询符合条件工单数目
	 *
	 * @param cityId
	 * @param endOpenDate
	 * @param startOpenDate
	 * @return <00(city_id), <succNum=1, failNum=2, notNum=3>>
	 */
	public List<Map<String,String>> getServStatsGroupByCity(String cityId, String startOpenDate, String endOpenDate)
	{
		logger.debug("getStatsGroupByCity({},{},{})", new Object[]{cityId, startOpenDate, endOpenDate});

		StringBuffer sb = new StringBuffer();
		if(StringUtil.IsEmpty(cityId)||"00".equals(cityId)){
			sb.append("select d.city_id, count(*) as num from stb_tab_gw_device d,stb_tab_customer s where d.customer_id=s.customer_id and s.user_status=1 ");
		}
		else{
			sb.append("select d.city_id, count(*) as num from stb_tab_gw_device d,stb_tab_customer s where d.customer_id=s.customer_id and s.user_status=1 and d.city_id='"+cityId+"'");
		}

		if (!StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and d.cpe_currentupdatetime>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and d.cpe_currentupdatetime<=").append(endOpenDate);
		}

		sb.append(" group by d.city_id");
		sb.append(" order by d.city_id");

		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		print(list);

		return list;
	}




	/**
	 * 查询全部开通业务数
	 * @param cityId
	 * @param startOpenDate
	 * @param endOpenDate
	 * @return
	 */
	public int getServStats(String cityId, String startOpenDate, String endOpenDate)
	{
		logger.debug("getServStats({},{},{})", new Object[]{cityId, startOpenDate, endOpenDate});

		StringBuffer sb = new StringBuffer();
		if(StringUtil.IsEmpty(cityId)||"00".equals(cityId)){
			sb.append("select count(*) as num from stb_tab_gw_device d,stb_tab_customer s where d.customer_id=s.customer_id and s.user_status=1 ");
		}
		else{
			sb.append("select count(*) as num from stb_tab_gw_device d,stb_tab_customer s where d.customer_id=s.customer_id and s.user_status=1 and d.city_id='"+cityId+"'");
		}

		if (!StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and d.cpe_currentupdatetime>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and d.cpe_currentupdatetime<=").append(endOpenDate);
		}

		PrepareSQL psql = new PrepareSQL(sb.toString());
		int num = jt.queryForInt(psql.getSQL());

		return num;
	}



	private void print(List<Map<String,String>> s){
		try{
			for(Map<String,String> one:s){
		    	for (String ss : one.keySet()) {
		    		logger.warn("key:" + ss);
		    		logger.warn("values:" + one.get(ss).toString());
		    	}
		    }
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
	}



}
