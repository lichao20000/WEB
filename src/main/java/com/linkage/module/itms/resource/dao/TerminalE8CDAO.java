package com.linkage.module.itms.resource.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-12-17
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class TerminalE8CDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(TerminalE8CDAO.class);
	
	/**
	 * 查询总e8-c终端数
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String,String> getTerminalAll(String city_id){
		logger.debug("TerminalE8CDAO=>getTerminalAll{}",city_id);
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.city_id,count(1) num from tab_gw_device a where a.device_type='e8-c'  ");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		} 
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}
	
	/**
	 * 查询新增终端总数
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String,String> getNewTerminal(String city_id, String start_time, String end_time){
		logger.debug("TerminalE8CDAO=>getNewTerminal{}");
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.city_id,count(1) num from tab_gw_device a where a.device_type='e8-c'  ");
		if(!StringUtil.IsEmpty(start_time)){
			sql.append(" and a.complete_time>=").append(start_time);
		}
		if(!StringUtil.IsEmpty(end_time)){
			sql.append(" and a.complete_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		} 
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}
	
    
	/**
	 * 查询2+1规格总数
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String,String> getTwoAndOneTerminal(String city_id, String start_time, String end_time){
		logger.debug("TerminalE8CDAO=>getTwoAndOneTerminal()");
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.city_id,count(1) num from tab_gw_device a,tab_devicetype_info b  where a.devicetype_id=b.devicetype_id  and b.spec_id=3 and a.device_type='e8-c'  ");
		if(!StringUtil.IsEmpty(start_time)){
			sql.append(" and a.complete_time>=").append(start_time);
		}
		if(!StringUtil.IsEmpty(end_time)){
			sql.append(" and a.complete_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		} 
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}
	
	/**
	 * 查询4+2规格总数
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String,String> getFourAndTwoTerminal(String city_id, String start_time, String end_time){
		logger.debug("TerminalE8CDAO=>getFourAndTwoTerminal()");
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.city_id,count(1) num from tab_gw_device a,tab_devicetype_info b  where a.devicetype_id=b.devicetype_id  and b.spec_id=1 and a.device_type='e8-c' ");
		if(!StringUtil.IsEmpty(start_time)){
			sql.append(" and a.complete_time>=").append(start_time);
		}
		if(!StringUtil.IsEmpty(end_time)){
			sql.append(" and a.complete_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		} 
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}
	
	/**
	 * 查询政企终端总数
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String,String> getEgwTerminal(String city_id, String start_time, String end_time){
		logger.debug("TerminalE8CDAO=>getEgwTerminal()");
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.city_id,count(1) num from tab_gw_device a, ");
		sql.append("(select m.devicetype_id,n.gw_type from  tab_devicetype_info m left join tab_bss_dev_port n on m.spec_id=n.id) b ");
		sql.append(" where a.devicetype_id=b.devicetype_id and  a.device_type='e8-c' and b.gw_type='2' ");
		if(!StringUtil.IsEmpty(start_time)){
			sql.append(" and a.complete_time>=").append(start_time);
		}
		if(!StringUtil.IsEmpty(end_time)){
			sql.append(" and a.complete_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		} 
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}
	
	
}
