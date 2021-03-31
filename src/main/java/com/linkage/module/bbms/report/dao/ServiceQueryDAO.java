
package com.linkage.module.bbms.report.dao;

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

@SuppressWarnings("unchecked")
public class ServiceQueryDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ServiceQueryDAO.class);
	

	/**
	 * 取的所有的业务类型List
	 * 
	 * @author wangsenbo
	 * @date Mar 12, 2010
	 * @param
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getServiceTypeList()
	{
		String sql = "select id as service_id,service_name from cpe_gather_service_desc";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return jt.queryForList(sql);
	}

	/**
	 * 取的所有的业务类型Map
	 * 
	 * @author wangsenbo
	 * @date Mar 12, 2010
	 * @param
	 * @return List<Map<String,String>>
	 */
	public Map<String, String> getServiceTypeMap()
	{
		List<Map<String, String>> list = getServiceTypeList();
		Map<String, String> map = new HashMap<String, String>();
		for (Map<String, String> tmap : list)
		{
			map.put(StringUtil.getStringValue(tmap.get("service_id")), tmap.get("service_name"));
		}
		return map;
	}

	public List<Map<String,String>> queryService(String cityId, String userName,
			String device_serialnumber, String serviceId)
	{
		logger.debug("queryService");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,a.username,b.device_serialnumber,b.oui,");
		sql.append("b.vendor_id,b.devicetype_id,b.device_model_id,c.service_result,");
		sql.append("c.id,c.gather_time from tab_gw_device b left join tab_egwcustomer a ");
		sql.append(" on a.device_id=b.device_id and a.user_state='1',");
		sql.append(" cpe_gather_result c  where b.device_id=c.device_id ");
		
//		sql
//				.append(
//						"select b.city_id,a.username,a.device_serialnumber,a.oui,b.vendor_id,b.devicetype_id,b.device_model_id,c.service_result,c.id,c.gather_time ")
//				.append("from tab_egwcustomer a,tab_gw_device b,cpe_gather_result c ")
//				.append(
//						"where a.device_id=b.device_id and a.device_id=c.device_id and a.user_state='1' ");
		if (false == StringUtil.IsEmpty(cityId)&&!"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (false == StringUtil.IsEmpty(serviceId) && !"-1".equals(serviceId))
		{
			sql.append(" and c.id=").append(serviceId);
		}
		if (false == StringUtil.IsEmpty(userName))
		{
			sql.append(" and a.username='").append(userName).append("'");
		}
		if (false == StringUtil.IsEmpty(device_serialnumber))
		{
			sql.append(" and b.device_serialnumber like'%").append(
					device_serialnumber).append("%'");
		}
		sql.append(" order by a.user_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		return jt.queryForList(sql.toString());
	}
}
