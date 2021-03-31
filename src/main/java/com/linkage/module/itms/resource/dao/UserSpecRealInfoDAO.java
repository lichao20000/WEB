
package com.linkage.module.itms.resource.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-8-19
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class UserSpecRealInfoDAO extends SuperDAO
{


	public Map getBssSpecInfo(String startOpenDate1, String endOpenDate1, String city_id,
			String devicetype, String spec_id)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select t.spec_id,h.city_id,count(1) total  from tab_hgwcustomer h,tab_gw_device d,tab_devicetype_info t ");
		sql.append("  where t.devicetype_id = d.devicetype_id and h.device_id = d.device_id   ");
		 if (!StringUtil.IsEmpty(startOpenDate1))
		 {
		 sql.append(" and h.dealdate>").append(startOpenDate1);
		 }
		 if (!StringUtil.IsEmpty(endOpenDate1))
		 {
		 sql.append(" and h.dealdate<").append(endOpenDate1);
		 }
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id) && !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and h.city_id  in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(spec_id) && !"0".equals(spec_id))
		{
			sql.append(" and h.spec_id=").append(spec_id);
		}
		sql.append(" group by  t.spec_id,h.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		Map map = new HashMap<String, String>();
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id"))+":"+StringUtil.getStringValue(rmap.get("spec_id")),
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}

	/**
	 * 用户实际终端规格
	 * 
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param city_id
	 * @param devicetype
	 * @param cust_type_id
	 * @param spec_id
	 * @return
	 */
	public Map getUserSpecInfo(String startOpenDate1, String endOpenDate1,
			String city_id, String devicetype, String spec_id)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.spec_id,a.city_id,count(1) total from tab_hgwcustomer a,tab_gw_device d where a.device_id=d.device_id ");
		 if (!StringUtil.IsEmpty(startOpenDate1))
		 {
		 sql.append(" and a.dealdate>").append(startOpenDate1);
		 }
		 if (!StringUtil.IsEmpty(endOpenDate1))
		 {
		 sql.append(" and a.dealdate<").append(endOpenDate1);
		 }
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id) && !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(spec_id) && !"0".equals(spec_id))
		{
			sql.append(" and a.spec_id=").append(spec_id);
		}
		sql.append(" group by  a.spec_id,a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if (!list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}
	
	public Map getTabBssDevPort(String cust_type_id){
		PrepareSQL psql = new PrepareSQL("select id, spec_name from tab_bss_dev_port where gw_type=? order by id");
		psql.setString(1, cust_type_id);
		Map map = DataSetBean.getMap(psql.getSQL());
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map); 
		return resultMap;
	}
}
