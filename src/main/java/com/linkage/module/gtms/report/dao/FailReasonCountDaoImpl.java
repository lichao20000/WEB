package com.linkage.module.gtms.report.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gtms.report.serv.FailReasonCountServImpl;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class FailReasonCountDaoImpl extends SuperDAO implements FailReasonCountDao {

	private static Logger logger = LoggerFactory.getLogger(FailReasonCountServImpl.class);

	@SuppressWarnings("unchecked")
	public Map<String,Map<String,Integer>> getFailReasonCount(String cityId, String starttime,
			String endtime) {
		logger.warn("FailReasonCount({},{},{})",new Object[]{cityId,starttime,endtime});
		PrepareSQL sb = new PrepareSQL();
		sb.append(" select a.result_id,b.city_id,count(*) as num  ");
		sb.append(" from gw_serv_strategy a,tab_gw_device b");
		sb.append(" where a.device_id=b.device_id ");
		sb.append("  and a.time>=?");
		sb.append(" and a.time<=?");
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append(" and b.city_id in (");
			sb.append(StringUtils.weave(cityIdList));
			sb.append(")");
			cityIdList = null;
		}
		sb.append(" group by a.result_id,b.city_id");
		sb.setInt(1, StringUtil.getIntegerValue(starttime));
		sb.setInt(2, StringUtil.getIntegerValue(endtime));
		List<Map<String,String>> list = jt.queryForList(sb.getSQL());
		Map<String,Map<String,Integer>> reMap = new HashMap<String,Map<String,Integer>>();
		Map temp = null;
		for (int i = 0; i < list.size(); i++) {
			temp = list.get(i);
			Map map1  = reMap.get(StringUtil.getStringValue(temp.get("result_id")));
			if(map1==null||map1.isEmpty()){
				map1 = new HashMap<String,Integer>();
			}
			map1.put(StringUtil.getStringValue(temp.get("city_id")), StringUtil.getIntegerValue(temp.get("num")));
			reMap.put(StringUtil.getStringValue(temp.get("result_id")), map1);
		}

		return reMap;
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> getFailCode(){

		logger.debug("getFailCode()");

		String sql = "select fault_code, fault_desc, fault_reason from tab_cpe_faultcode";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map> list = jt.queryForList(sql);
		Map<String, String> failCodeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			failCodeMap.put(StringUtil.getStringValue(map.get("fault_code")),
					StringUtil.getStringValue(map.get("fault_desc"))+"("+StringUtil.getStringValue(map.get("fault_reason"))+")");
		}
		return failCodeMap;
	}
}
