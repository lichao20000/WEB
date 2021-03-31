package com.linkage.module.itms.resource.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-12-23
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */

public class TerminalReplaceDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(TerminalReplaceDAO.class);
	
	 /**
	  * 终端更换量
	  * @param city_id
	  * @param start_time
	  * @param end_time
	  * @param edtionIdBuffer
	  * @return
	  */
	public Map<String,String> TerminalReplaceOperInfo(String city_id, String start_time, String end_time , String edtionIdBuffer){
		logger.debug("TerminalReplaceDAO=>TerminalReplaceInfo()");
		StringBuffer sql = new StringBuffer();
		sql.append("select c.vendor_id,count(a.device_id) oper_num from tab_gw_device a,tab_devicetype_info b,tab_vendor c ,bind_log d ");
		sql.append(" where a.devicetype_id=b.devicetype_id and b.vendor_id=c.vendor_id and a.device_id=d.device_id and  d.oper_type=2 ");
		sql.append("  and c.vendor_id in ").append(edtionIdBuffer);
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.complete_time >=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.complete_time <=").append(end_time);
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
		{
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
				sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
						.append(")");
				cityIdList = null;
		}
		sql.append("  group by c.vendor_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = jt.queryForList(psql.getSQL());
		if (null!=list && !list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("vendor_id")),StringUtil.getStringValue(list.get(i).get("oper_num")));
			}
		}
		return map;
	}
	
	/**
	 * 终端总数
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @param edtionIdBuffer
	 * @return
	 */
	public Map<String,String> TerminalReplaceAllInfo(String city_id, String start_time, String end_time , String edtionIdBuffer){
		logger.debug("TerminalReplaceDAO=>TerminalReplaceInfo()");
		StringBuffer sql = new StringBuffer();
		sql.append("select c.vendor_id,count(a.device_id) all_num from tab_gw_device a,tab_devicetype_info b,tab_vendor c ,bind_log d ");
		sql.append(" where a.devicetype_id=b.devicetype_id and b.vendor_id=c.vendor_id and a.device_id=d.device_id  ");
		sql.append("  and c.vendor_id in ").append(edtionIdBuffer);
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.complete_time >=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.complete_time <=").append(end_time);
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
			{
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
				sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
						.append(")");
				cityIdList = null;
			}
		sql.append("  group by c.vendor_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = jt.queryForList(psql.getSQL());
		if (null!=list && !list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("vendor_id")),StringUtil.getStringValue(list.get(i).get("all_num")));
			}
		}
		return map;
	}
	
	
//	public List<Map> test(String city_id, String start_time, String end_time , String edtionIdBuffer){
//		StringBuffer sql = new StringBuffer();
//		sql.append(" select c.vendor_id,c.vendor_name,d.oper_type from tab_gw_device a,tab_devicetype_info b,tab_vendor c ,bind_log d ");
//		sql.append("where a.devicetype_id=b.devicetype_id and b.vendor_id=c.vendor_id and a.device_id=d.device_id ");
//		sql.append(" and c.vendor_id in ").append(edtionIdBuffer);
//		if (!StringUtil.IsEmpty(start_time))
//		{
//			sql.append(" and a.complete_time >=").append(start_time);
//		}
//		if (!StringUtil.IsEmpty(end_time))
//		{
//			sql.append(" and a.complete_time <=").append(end_time);
//		}
//		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
//		{
//				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
//				sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
//						.append(")");
//				cityIdList = null;
//		}
//		PrepareSQL psql = new PrepareSQL(sql.toString());
//		List<Map> list = jt.queryForList(psql.getSQL());
//		return list;
//	}
	
	
	
	/**
	 * 终端类型e8-c，总数大于100的厂商
	 * @return
	 */
	public List<String> getVendorList(){
		List<String> tempList = new ArrayList<String>();
		String sql = "select c.vendor_id,count(1) num from tab_gw_device a ,tab_devicetype_info b,tab_vendor c " +
					 "  where a.devicetype_id=b.devicetype_id and b.vendor_id=c.vendor_id and a.device_type='e8-c' group by c.vendor_id";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		for (int i = 0; i < list.size(); i++)
		{
			String num = StringUtil.getStringValue(list.get(i).get("num"));
			if (Integer.parseInt(num) > 100)
			{
				String vendor_id = StringUtil.getStringValue(list.get(i).get(
						"vendor_id"));
				tempList.add(vendor_id);
			}
		}
		return tempList;
	}
	
	/**
	 * @category getVendor 获取所有的厂商
	 * @return
	 */
	public Map<String, String> getVendor()
	{
		logger.debug("getVendor()");
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL pSQL = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from tab_vendor");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSQL.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String tempValue = StringUtil.getStringValue(list.get(i)
						.get("vendor_add"))
						+ "("
						+ StringUtil.getStringValue(list.get(i).get("vendor_name")) + ")";
				map.put(StringUtil.getStringValue(list.get(i).get("vendor_id")),
						tempValue);
			}
		}
		return map;
	}
}
	