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
 * @since 2013-12-18
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class TerminalSpecMatchDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(TerminalSpecMatchDAO.class);
	
	/**
	 * 总开户数
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String,String> getTerminalSpecAll(String city_id, String start_time, String end_time){
		logger.debug("TerminalSpecMatchDAO=>getTerminalSpecAll()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id, count(1) num from tab_hgwcustomer a where a.device_id is not null and a.opmode='1'  ");
		if(!StringUtil.IsEmpty(start_time)){
			sql.append(" and a.onlinedate>=").append(start_time);
		}
		if(!StringUtil.IsEmpty(end_time)){
			sql.append(" and a.onlinedate<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
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
	 * 设备2+1，用户4+2
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 *       
	 *       1 e8cp42                    
      2 e8cp11                    
      3 e8cp21                    
      4 EA0204                    
      5 EA0404                    
      6 EA0804                    
      7 EA0808                    
      8 GA0204                    
      9 GA0404                    
     10 GA0804                    
     11 GA0808    
	 * @return
	 */
	public Map<String,String> getTerminalSpectNoMactchOne(String city_id, String start_time, String end_time){
		logger.debug("TerminalSpecMatchDAO=>getTerminalSpectNoMactchOne()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(1) num from tab_hgwcustomer a,tab_gw_device b,tab_devicetype_info c ");
		sql.append(" where a.device_id = b.device_id and b.devicetype_id = c.devicetype_id and a.spec_id in (1,12) and c.spec_id = 3 ");
		if(!StringUtil.IsEmpty(start_time)){
			sql.append(" and a.onlinedate>=").append(start_time);
		}
		if(!StringUtil.IsEmpty(end_time)){
			sql.append(" and a.onlinedate<=").append(end_time);
		}
		
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
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
	 * 设备2+1，用户政企4+2
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String,String> getTerminalSpectNoMactchTwo(String city_id, String start_time, String end_time){
		logger.debug("TerminalSpecMatchDAO=>getTerminalSpectNoMactchTwo()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(1) num from  tab_hgwcustomer a,tab_gw_device b,tab_devicetype_info c ");
		sql.append(" where a.device_id = b.device_id and b.devicetype_id = c.devicetype_id and a.spec_id in(4,8) and c.spec_id = 3 ");
		if(!StringUtil.IsEmpty(start_time)){
			sql.append(" and a.onlinedate>=").append(start_time);
		}
		if(!StringUtil.IsEmpty(end_time)){
			sql.append(" and a.onlinedate<=").append(end_time);
		}
		
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
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
	 * 设备4+2，用户2+1
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String,String> getTerminalSpectNoMactchThree(String city_id, String start_time, String end_time){
		logger.debug("TerminalSpecMatchDAO=>getTerminalSpectNoMactchThree()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(1) num from tab_hgwcustomer a,tab_gw_device b,tab_devicetype_info c ");
		sql.append(" where a.device_id = b.device_id and b.devicetype_id = c.devicetype_id and a.spec_id = 3 and c.spec_id in (1,12) ");
		if(!StringUtil.IsEmpty(start_time)){
			sql.append(" and a.onlinedate>=").append(start_time);
		}
		if(!StringUtil.IsEmpty(end_time)){
			sql.append(" and a.onlinedate<=").append(end_time);
		}
		
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
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
	 * 设备政企4+8，用户4+2
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String,String> getTerminalSpectNoMactchFour(String city_id, String start_time, String end_time){
		logger.debug("TerminalSpecMatchDAO=>getTerminalSpectNoMactchTwo()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(1) num from tab_hgwcustomer a,tab_gw_device b,tab_devicetype_info c ");
		sql.append(" where a.device_id = b.device_id and b.devicetype_id = c.devicetype_id and a.spec_id in (1,12) and c.spec_id in (6,10) ");
		if(!StringUtil.IsEmpty(start_time)){
			sql.append(" and a.onlinedate>=").append(start_time);
		}
		if(!StringUtil.IsEmpty(end_time)){
			sql.append(" and a.onlinedate<=").append(end_time);
		}
		
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		} 
		sql.append(" group by a.city_id ");
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
