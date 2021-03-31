/**
 *
 */
package com.linkage.module.gtms.stb.resource.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 工单报表
 * fanjm
 * @author HP (AILK No.)
 * @version 1.0
 * @since 2018-4-20
 * @category com.linkage.module.gtms.stb.report.dao
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class GwDeviceVenCityQueryDAO extends SuperDAO{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(GwDeviceVenCityQueryDAO.class);





	/**
	 * 属地分组，查询确定型号、版本的设备数量
	 * @param cityId
	 * @param startOpenDate
	 * @param endOpenDate
	 * @param endOpenDate2
	 * @param startOpenDate2
	 * @param cityId2
	 * @return
	 */
	public List<Map<String,String>> getAllStatsGroupByCity(String devicetypeId,String deviceModelId,String vendorId,String cityId, String startOpenDate, String endOpenDate)
	{
		logger.debug("getStatsGroupByCity({})", new Object[]{devicetypeId, deviceModelId, vendorId,cityId, startOpenDate, endOpenDate});
		List list = null;
		StringBuffer sb = new StringBuffer();
		if(!"00".equals(cityId)){// TODO wait (more table related)
			sb.append("select v.vendor_name,m.device_model,t.softwareversion,d.city_id, count(*) as num from stb_tab_gw_device d inner join stb_tab_devicetype_info t on d.devicetype_id=t.devicetype_id left join stb_tab_vendor v on " +
					" v.vendor_id=t.vendor_id left join stb_gw_device_model m on m.device_model_id=t.device_model_id where d.customer_id is not null ");
			if(!StringUtil.IsEmpty(vendorId)&&!"-1".equals(vendorId)){
				sb.append(" and t.vendor_id='").append(vendorId).append("' ");
			}

			if(!StringUtil.IsEmpty(deviceModelId)&&!"-1".equals(deviceModelId)){
				sb.append(" and t.device_model_id='").append(deviceModelId).append("' ");
			}

			if(!StringUtil.IsEmpty(devicetypeId)&&!"-1".equals(devicetypeId)){
				sb.append(" and t.softwareversion='").append(devicetypeId).append("' ");
			}

			if( null!=cityId &&!"-1".equals(cityId)&&!"00".equals(cityId)) {
				ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sb.append(" and d.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
				cityArray = null;
			}

			if (!StringUtil.IsEmpty(startOpenDate))
			{
				sb.append(" and d.complete_time>=").append(startOpenDate);
			}
			if (false == StringUtil.IsEmpty(endOpenDate))
			{
				sb.append(" and d.complete_time<=").append(endOpenDate);
			}

			sb.append(" group by v.vendor_name,m.device_model,t.softwareversion,d.city_id");
			sb.append(" order by v.vendor_name,m.device_model,t.softwareversion,d.city_id");

			PrepareSQL psql = new PrepareSQL(sb.toString());
			if(Global.NXDX.equals(Global.instAreaShortName))
			{
				list =  DBOperation.getRecords(psql.getSQL(),"xml-stb");
			}
			else {
				list = jt.queryForList(psql.getSQL());
			}
		}
		else{// TODO wait (more table related)
			sb.append("select v.vendor_name,m.device_model,t.softwareversion,c.parent_id as city_id, count(*) as num " +
					" from stb_tab_gw_device d inner join stb_tab_devicetype_info t on d.devicetype_id=t.devicetype_id " +
					" left join stb_tab_vendor v on v.vendor_id=t.vendor_id left join stb_gw_device_model m on m.device_model_id=t.device_model_id " +
					" inner join (select city_id,parent_id from tab_city where parent_id in (select city_id from tab_city where parent_id='00') " +
					" union all select city_id,city_id from tab_city where parent_id ='00' " +
					" union all select city_id,city_id from tab_city where city_id='00') c on d.city_id = c.city_id where d.customer_id is not null ");
			if(!StringUtil.IsEmpty(vendorId)&&!"-1".equals(vendorId)){
				sb.append(" and t.vendor_id='").append(vendorId).append("' ");
			}

			if(!StringUtil.IsEmpty(deviceModelId)&&!"-1".equals(deviceModelId)){
				sb.append(" and t.device_model_id='").append(deviceModelId).append("' ");
			}

			if(!StringUtil.IsEmpty(devicetypeId)&&!"-1".equals(devicetypeId)){
				sb.append(" and t.softwareversion='").append(devicetypeId).append("' ");
			}

			if (!StringUtil.IsEmpty(startOpenDate))
			{
				sb.append(" and d.complete_time>=").append(startOpenDate);
			}
			if (false == StringUtil.IsEmpty(endOpenDate))
			{
				sb.append(" and d.complete_time<=").append(endOpenDate);
			}

			sb.append(" group by v.vendor_name,m.device_model,t.softwareversion,c.parent_id");
			sb.append(" order by v.vendor_name,m.device_model,t.softwareversion,c.parent_id");

			PrepareSQL psql = new PrepareSQL(sb.toString());
			if(Global.NXDX.equals(Global.instAreaShortName))
			{
				list =  DBOperation.getRecords(psql.getSQL(),"xml-stb");
			}
			else {
				list = jt.queryForList(psql.getSQL());
			}
			//list = jt.queryForList(psql.getSQL());
		}

		return list;
	}




}
