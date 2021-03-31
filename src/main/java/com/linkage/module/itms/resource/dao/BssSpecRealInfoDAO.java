package com.linkage.module.itms.resource.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.IdentityHashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


public class BssSpecRealInfoDAO extends SuperDAO {
	
	
	/**
	 * BSS终端规格
	 * @param startOpenDate1 开始时间
	 * @param endOpenDate1 结束时间
	 * @param city_id 属地编码
	 * @param devicetype 终端类型
	 * @param cust_type_id 客户类型
	 * @return
	 */
	
	public Map getBssSpecInfo(String startOpenDate1,String endOpenDate1, String city_id, String devicetype,String spec_id){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.spec_id,d.city_id,count(1) total  from tab_gw_device d,tab_devicetype_info t,tab_hgwcustomer a where a.device_id=d.device_id and t.devicetype_id = d.devicetype_id ");
		
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and d.complete_time>").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and d.complete_time<").append(endOpenDate1);
		}
		
		if(!StringUtil.IsEmpty(city_id) && !"00".equals(city_id) && !"-1".equals(city_id)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and d.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		
		if(!StringUtil.IsEmpty(spec_id) && !"0".equals(spec_id)){
			sql.append(" and t.spec_id=").append(spec_id);
		}
		
		sql.append(" group by  t.spec_id,d.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if(false == list.isEmpty()){
			for(int i=0; i<list.size(); i++ ){
				Map rmap = (Map)list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), StringUtil
						.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}
	
	/**
	 * 用户实际终端规格
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param city_id
	 * @param devicetype
	 * @param cust_type_id
	 * @param spec_id
	 * @return
	 */
	public Map getUserSpecInfo(String startOpenDate1,String endOpenDate1, String city_id, String devicetype, String spec_id){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select h.spec_id,h.city_id,count(1) total  from tab_hgwcustomer h,tab_gw_device d,tab_devicetype_info t ");
		sql.append("  where t.devicetype_id = d.devicetype_id and h.device_id = d.device_id ");
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and d.complete_time>").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and d.complete_time<").append(endOpenDate1);
		}
		if(!StringUtil.IsEmpty(city_id) && !"00".equals(city_id) && !"-1".equals(city_id)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and h.city_id  in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		
		if(!StringUtil.IsEmpty(spec_id) && !"0".equals(spec_id)){
			sql.append(" and t.spec_id=").append(spec_id);
		}
		sql.append(" group by h.spec_id,h.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		Map map = new HashMap<String, String>();
		if(!list.isEmpty()){
			for(int i=0; i<list.size(); i++){
				Map rmap = (Map)list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id"))+":"+StringUtil.getStringValue(rmap.get("spec_id")),StringUtil.getStringValue(rmap.get("total")));	
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
