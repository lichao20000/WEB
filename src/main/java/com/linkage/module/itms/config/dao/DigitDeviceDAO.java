
package com.linkage.module.itms.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-9-19 上午10:10:36
 * @category com.linkage.module.gwms.share.dao
 * @copyright 南京联创科技 网管科技部
 */
public class DigitDeviceDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(DigitDeviceDAO.class);

	public List<Map> query(String cityId, String vendorId, String deviceModelId,
			String deviceTypeId, String device_serialnumber, String loopback_ip,
			String device_id_ex, String task_name, int curPage_splitPage,
			int num_splitPage)
	{
		PrepareSQL pSql = new PrepareSQL();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		StringBuffer sql = new StringBuffer();
		String str = "select c.city_name,d.device_serialnumber,v.vendor_name," +
				"m.device_model,dd.starttime,dd.endtime,dd.result_id,vd.map_name, dd.enable,dk.task_name " +
				"from "
				+ " gw_voip_digit_map vd,gw_voip_digit_task dk,tab_gw_device d"
				+ " left join gw_voip_digit_device dd   on d.device_id=dd.device_id"
				+ " left join tab_city c             on d.city_id=c.city_id"
				+ " left join tab_vendor v           on d.vendor_id=v.vendor_id"
				+ " left join gw_device_model m      on d.device_model_id=m.device_model_id"
				+ " left join tab_devicetype_info dc on d.devicetype_id=dc.devicetype_id"
				+ " where vd.map_id =dd.map_id and dk.task_id=dd.task_id";
		if (!"-1".equals(vendorId))
		{
			str = str + " and v.vendor_id='" + vendorId + "'";
		}
		if (!"-1".equals(deviceModelId))
		{
			str = str + " and m.device_model_id='" + deviceModelId + "'";
		}
		if (!"-1".equals(deviceTypeId))
		{
			str = str + " and dc.devicetype_id=" + Integer.parseInt(deviceTypeId);
		}
		if (device_serialnumber != null && !"".equals(device_serialnumber))
		{
			str = str + " and device_serialnumber='" + device_serialnumber + "'";
		}
		if (loopback_ip != null && !"".equals(loopback_ip))
		{
			str = str + " and d.loopback_ip='" + loopback_ip + "'";
		}
		if (device_id_ex != null && !"".equals(device_id_ex))
		{
			str = str + " and d.device_id_ex='" + device_id_ex + "'";
		}
		if (task_name != null && !"".equals(task_name))
		{
			str = str + " and dk.task_name='" + task_name + "'";
		}
		if (!"-1".equals(cityId))
		{
			str = str + " and ( c.city_id='" + cityId + "' or c.parent_id='" + cityId
					+ "')";
		}
		
	/**	
	  if (!"-1".equals(cityId))
	 
				{
					str = str + " and  c.city_id='" + cityId + "' or c.parent_id='" + cityId
							+ "'";
				}
				*/
		sql.append(str);
		pSql.setSQL(sql.toString());
		List<Map> list = querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("city_name", rs.getString("city_name"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("device_model", rs.getString("device_model"));
				map.put("starttime", rs.getString("starttime"));
				map.put("endtime", rs.getString("endtime"));
				map.put("result_id", rs.getString("result_id"));
				map.put("map_name", rs.getString("map_name"));
				map.put("enable", rs.getString("enable"));
				map.put("task_name", rs.getString("task_name"));
				return map;
			}
		});
		return list;
	}

	public int getListCount(String cityId, String vendorId, String deviceModelId,
			String deviceTypeId, String device_serialnumber, String loopback_ip,
			String device_id_ex, String task_name, int curPage_splitPage,
			int num_splitPage)
	{
		PrepareSQL pSql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		String str;
		if(DBUtil.GetDB()==3){
			//TODO wait
			str = "select count(*) from ";
		}else{
			str = "select count(1) from ";
		}
		str += " gw_voip_digit_map vd,gw_voip_digit_task dk,tab_gw_device d"
				+ " left join gw_voip_digit_device dd   on d.device_id=dd.device_id"
				+ " left join tab_city c             on d.city_id=c.city_id"
				+ " left join tab_vendor v           on d.vendor_id=v.vendor_id"
				+ " left join gw_device_model m      on d.device_model_id=m.device_model_id"
				+ " left join tab_devicetype_info dc on d.devicetype_id=dc.devicetype_id"
				+ " where vd.map_id =dd.map_id and dk.task_id=dd.task_id";
		if (!"-1".equals(vendorId))
		{
			str = str + " and v.vendor_id='" + vendorId + "'";
		}
		if (!"-1".equals(deviceModelId))
		{
			str = str + " and m.device_model_id='" + deviceModelId + "'";
		}
		if (!"-1".equals(deviceTypeId))
		{
			str = str + " and dc.devicetype_id=" + Integer.parseInt(deviceTypeId);
		}
		if (device_serialnumber != null && !"".equals(device_serialnumber))
		{
			str = str + " and device_serialnumber='" + device_serialnumber + "'";
		}
		if (loopback_ip != null && !"".equals(loopback_ip))
		{
			str = str + " and d.loopback_ip='" + loopback_ip + "'";
		}
		if (device_id_ex != null && !"".equals(device_id_ex))
		{
			str = str + " and d.device_id_ex='" + device_id_ex + "'";
		}
		if (task_name != null && !"".equals(task_name))
		{
			str = str + " and dk.task_name='" + task_name + "'";
		}
		if (!"-1".equals(cityId))
		{
			str = str + " and ( c.city_id='" + cityId + "' or c.parent_id='" + cityId
					+ "')";
		}
		
		
		//if (!"-1".equals(cityId))
	///	{
		//	str = str + " and  c.city_id='" + cityId + "' or c.parent_id='" + cityId
		//			+ "'";
		//}
		sql.append(str);
		pSql.setSQL(sql.toString());
		int total = jt.queryForInt(pSql.getSQL());
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
}
