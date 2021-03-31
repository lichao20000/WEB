/**
 * 
 */
package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author chenjie
 * @date 2011-8-26
 * 工单统计报表
 */
public class SheetStatsReportDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(SheetStatsReportDAO.class);
	
	/**
	 * 根据CITY和其他条件统计出所有地市的数据(已做/未做/失败)
	 * 
	 * @param deviceType
	 * @param cityId
	 * @param servTypeId
	 * @param startOpenDate
	 * @param endOpenDate
	 * @return <00(city_id), <succNum=1, failNum=2, notNum=3>>
	 */
	public Map<String, Map<String, Integer>> getStatsMapByCityId(String deviceType, String cityId, 
			String servTypeId, String startOpenDate, String endOpenDate) 
	{
		logger.debug("getStatsMapByCityId({},{},{},{},{})", 
				new Object[]{deviceType, cityId, servTypeId, startOpenDate, endOpenDate});
		
		// 所有的子地市
		List<String> cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		Map<String, Map<String, Integer>> result = new LinkedHashMap<String, Map<String, Integer>>();
		
		// 所有地市的成功数据统计
		Map<String,Integer> succMap = getDataByOpenStatus(deviceType, cityList, 
										servTypeId, startOpenDate, endOpenDate, 1);
		// 所有地市的失败数据统计
		Map<String,Integer> failMap = getDataByOpenStatus(deviceType, cityList, 
										servTypeId, startOpenDate, endOpenDate, -1);
		// 所有地市的未做数据统计
		Map<String,Integer> notMap =  getDataByOpenStatus(deviceType, cityList,
										servTypeId, startOpenDate, endOpenDate, 0);
		
		int succNum = 0;
		int failNum = 0;
		int notNum = 0;
		
		// 循环地市,组装数据
		for(String city : cityList)
		{
			succNum = getIntValue(succMap.get(city)); 
			failNum = getIntValue(failMap.get(city));
			notNum = getIntValue(notMap.get(city));
			
			Map<String,Integer> temp = new HashMap<String,Integer>();
			temp.put("succNum", succNum);
			temp.put("failNum", failNum);
			temp.put("notNum", notNum);
			
			result.put(city, temp);
		}
		
		return result;
	}
	
	/**
	 * 根据条件+openStatus获取所有地市数据
	 * 
	 * @param deviceType
	 * @param cityList
	 * @param servTypeId
	 * @param startOpenDate
	 * @param endOpenDate
	 * @param openStatus
	 * @return <001(地市), 12(数字)> 
	 */
	public Map<String,Integer> getDataByOpenStatus(String deviceType, List<String> cityList, 
			String servTypeId, String startOpenDate, String endOpenDate, int openStatus)
	{
		logger.debug("getDataByOpenStatus({},{},{},{},{},{})", 
				new Object[]{deviceType, cityList, servTypeId, startOpenDate, endOpenDate, openStatus});
		
		StringBuffer sb = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sb.append("select b.city_id, count(*) as num ");
		}else{
			sb.append("select b.city_id, count(1) as num ");
		}
		sb.append("from hgwcust_serv_info a, tab_hgwcustomer b, gw_cust_user_dev_type d")
		  .append(" where a.user_id=b.user_id and a.user_id = d.user_id")
		  .append(" and a.open_status=" + openStatus);
		
//		if(!StringUtil.IsEmpty(openStatus))
//		{
//			sb.append(" a.open_status=" + openStatus);
//		}
		
		if (false == StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and a.dealdate>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and a.dealdate<=").append(endOpenDate);
		}
		
		if(false == StringUtil.IsEmpty(deviceType))
		{
			sb.append(" and d.type_id='").append(deviceType).append("'");
		}
		
		if(!StringUtil.IsEmpty(servTypeId))
		{
			sb.append(" and a.serv_type_id=" + servTypeId);
		}
		
		// 地市
		if(null == cityList || cityList.isEmpty())
		{
			sb.append(" and b.city_id in ('").append(StringUtils.weave(cityList)).append("')");
		}
		
		sb.append(" group by b.city_id");
		sb.append(" order by b.city_id");

		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map> list = jt.queryForList(psql.getSQL());
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		int value = 0;
		for(Map temp : list)
		{
			// 如果为null
			if(StringUtil.IsEmpty(StringUtil.getStringValue(temp.get("num"))))
			{
				value = 0;
			}
			else
			{
				value = Integer.parseInt(temp.get("num").toString());
			}
			map.put((String)temp.get("city_id"), value);
		}
		
		return map;
	}
	
	/**
	 * 查看详细
	 * @param cityId
	 * @param openStatus
	 * @param deviceType
	 * @param servTypeId
	 * @param startOpenDate
	 * @param endOpenDate
	 * @return
	 * @return
	 */
	public List<Map> getDetailReport(String cityId, String openStatus, String deviceType, 
			String servTypeId, String startOpenDate, String endOpenDate
			, int curPage_splitPage, int num_splitPage) 
	{
		logger.debug("getDetailReport({},{},{},{},{},{})", 
				new Object[]{cityId, openStatus, deviceType, servTypeId, startOpenDate, endOpenDate});
		List<String> cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		StringBuffer sb = new StringBuffer();
		sb.append("select a.username,a.city_id,a.device_serialnumber,c.serv_type_name,b.dealdate,b.open_status,");
		sb.append("a.user_id,a.oui,a.device_id,b.serv_type_id,b.serv_status,b.wan_type,e.city_name,d.type_id ");
		sb.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_serv_type c,gw_cust_user_dev_type d,tab_city e ");
		sb.append("where a.user_id=b.user_id and b.serv_type_id=c.serv_type_id and a.user_id=d.user_id "); 
		sb.append("and a.city_id=e.city_id and a.city_id in" + "(" + StringUtils.weave(cityList) + ") ");
		sb.append("and b.open_status=? ");
		
		if (false == StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and b.dealdate>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and b.dealdate<=").append(endOpenDate);
		}
		
		if(false == StringUtil.IsEmpty(deviceType))
		{
			sb.append(" and d.type_id='").append(deviceType).append("'");
		}
		
		if(!StringUtil.IsEmpty(servTypeId))
		{
			sb.append(" and b.serv_type_id=" + servTypeId);
		}
		
		PrepareSQL psql = new PrepareSQL(sb.toString());
		psql.setInt(1, Integer.parseInt(openStatus));
		
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("serv_type_name", rs.getString("serv_type_name"));
				// 将dealdate转换成时间
				try
				{
					long dealdate = StringUtil.getLongValue(rs.getString("dealdate"));
					DateTimeUtil dt = new DateTimeUtil(dealdate * 1000);
					map.put("dealdate", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("dealdate", "");
				}
				catch (Exception e)
				{
					map.put("dealdate", "");
				}
				map.put("open_status", rs.getString("open_status"));
				map.put("user_id", String.valueOf(rs.getInt("user_id")));
				map.put("oui", rs.getString("oui"));
				map.put("device_id", rs.getString("device_id"));
				String serv_type_id = rs.getString("serv_type_id");
				map.put("serv_type_id", serv_type_id);
				/**
				String tmp = "-";
				if (false == StringUtil.IsEmpty(serv_type_id))
				{
					tmp = servTypeMap.get(serv_type_id);
				}
				map.put("serv_type", tmp);
				**/
				map.put("serv_status", rs.getString("serv_status"));
				map.put("wan_type", rs.getString("wan_type"));
				map.put("username", rs.getString("username"));
				map.put("city_name", rs.getString("city_name"));
				map.put("city_id", rs.getString("city_id"));
				map.put("type_id", rs.getString("type_id"));
				return map;
			}
		});
		return list;
	}
	
	// 不分页
	@SuppressWarnings("unchecked")
	public List<Map> getDetailReport(String cityId, String openStatus, String deviceType,
			String servTypeId, String startOpenDate, String endOpenDate)
	{
		
		logger.debug("getDetailReport({},{},{},{},{},{})", 
				new Object[]{cityId, openStatus, deviceType, servTypeId, startOpenDate, endOpenDate});
		List<String> cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		StringBuffer sb = new StringBuffer();
		sb.append("select a.username,a.city_id,a.device_serialnumber,c.serv_type_name,b.dealdate,b.open_status,");
		sb.append("a.user_id,a.oui,a.device_id,b.serv_type_id,b.serv_status,b.wan_type,e.city_name,d.type_id ");
		sb.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_serv_type c,gw_cust_user_dev_type d,tab_city e ");
		sb.append("where a.user_id=b.user_id and b.serv_type_id=c.serv_type_id and a.user_id=d.user_id "); 
		sb.append("and a.city_id=e.city_id and a.city_id in(" + StringUtils.weave(cityList) + ") ");
		sb.append("and b.open_status=? ");
		
		if (false == StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and b.dealdate>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and b.dealdate<=").append(endOpenDate);
		}
		
		if(false == StringUtil.IsEmpty(deviceType))
		{
			sb.append(" and d.type_id='").append(deviceType).append("'");
		}
		
		if(!StringUtil.IsEmpty(servTypeId))
		{
			sb.append(" and b.serv_type_id=" + servTypeId);
		}
		
		PrepareSQL psql = new PrepareSQL(sb.toString());
		psql.setInt(1, Integer.parseInt(openStatus));
		
		List<Map> list = jt.query(psql.getSQL(),new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("serv_type_name", rs.getString("serv_type_name"));
				// 将dealdate转换成时间
				try
				{
					long dealdate = StringUtil.getLongValue(rs.getString("dealdate"));
					DateTimeUtil dt = new DateTimeUtil(dealdate * 1000);
					map.put("dealdate", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("dealdate", "");
				}
				catch (Exception e)
				{
					map.put("dealdate", "");
				}
				map.put("open_status", rs.getString("open_status"));
				map.put("user_id", String.valueOf(rs.getInt("user_id")));
				map.put("oui", rs.getString("oui"));
				map.put("device_id", rs.getString("device_id"));
				String serv_type_id = rs.getString("serv_type_id");
				map.put("serv_type_id", serv_type_id);
				/**
				String tmp = "-";
				if (false == StringUtil.IsEmpty(serv_type_id))
				{
					tmp = servTypeMap.get(serv_type_id);
				}
				map.put("serv_type", tmp);
				**/
				map.put("serv_status", rs.getString("serv_status"));
				map.put("wan_type", rs.getString("wan_type"));
				map.put("username", rs.getString("username"));
				map.put("city_name", rs.getString("city_name"));
				map.put("city_id", rs.getString("city_id"));
				map.put("type_id", rs.getString("type_id"));
				return map;
			}
		});
		return list;
	}
	
	public int countDetailReport(String cityId, String openStatus, String deviceType, 
			String servTypeId, String startOpenDate, String endOpenDate,
			int curPage_splitPage,int num_splitPage) 
	{
		logger.debug("countDetailReport:({},{},{},{},{},{})", 
				new Object[]{cityId, openStatus, deviceType, servTypeId, startOpenDate, endOpenDate});
		
		List<String> cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		StringBuffer sb = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sb.append("select count(*) as num ");
		}else{
			sb.append("select count(1) as num ");
		}
		sb.append("from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_serv_type c,gw_cust_user_dev_type d,tab_city e ");
		sb.append("where a.user_id=b.user_id and b.serv_type_id=c.serv_type_id and a.user_id=d.user_id "); 
		sb.append("and a.city_id=e.city_id and a.city_id in(" + StringUtils.weave(cityList) + ") ");
		sb.append("and b.open_status=? ");
		
		if (false == StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and b.dealdate>=").append(startOpenDate);
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and b.dealdate<=").append(endOpenDate);
		}
		
		if(false == StringUtil.IsEmpty(deviceType))
		{
			sb.append(" and d.type_id='").append(deviceType).append("'");
		}
		
		if(!StringUtil.IsEmpty(servTypeId))
		{
			sb.append(" and b.serv_type_id=" + servTypeId);
		}
		
		PrepareSQL psql = new PrepareSQL(sb.toString());
		psql.setInt(1, Integer.parseInt(openStatus));
		
		int total = jt.queryForInt(psql.getSQL());
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
	
	// 如果数据为空，即为0
	private int getIntValue(Integer value)
	{
		if(value == null)
		{
			return 0;
		}
		else
		{
			return value.intValue();
		}
	}
}
