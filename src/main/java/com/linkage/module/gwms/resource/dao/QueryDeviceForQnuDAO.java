
package com.linkage.module.gwms.resource.dao;

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
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.DeviceDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author yinlei3 (73167)
 * @version 1.0
 * @since 2016年1月5日
 * @category com.linkage.module.gwms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class QueryDeviceForQnuDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(DeviceDAO.class);

	/**
	 * 获取社会化管控设备
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param device_serialnumber
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getImpDeviceList(int curPage_splitPage, int num_splitPage,
			String device_serialnumber)
	{
		logger.debug("getImpDeviceList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select device_id,device_serialnumber,oui,city_id,vendor_name,model_name,remark,add_date from tab_gw_device_init where 1=1 ");
		if (!StringUtil.IsEmpty(device_serialnumber))
		{
			sql.append("and dev_sub_sn = '"
					+ device_serialnumber.substring(device_serialnumber.length() - 6)
					+ "' and device_serialnumber like '%" + device_serialnumber + "'");
		}
		sql.append(" order by add_date desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		List<Map> list = querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("model_name", rs.getString("model_name"));
				map.put("remark", rs.getString("remark"));
				map.put("city_id", CityDAO.getCityName(rs.getString("city_id")));
				if (!StringUtil.IsEmpty(rs.getString("add_date")))
				{
					DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue(rs
							.getString("add_date")) * 1000l);
					map.put("add_date", dt.getDate());
				}
				else
				{
					map.put("add_date", "");
				}
				return map;
			}
		});
		return list;
	}

	public int getImpDeviceCount(int curPage_splitPage, int num_splitPage,
			String device_serialnumber)
	{
		logger.debug("getImpDeviceCount()");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append(" select count(*) from tab_gw_device_init where 1=1 ");
		}else{
			sql.append(" select count(1) from tab_gw_device_init where 1=1 ");
		}
		
		if (!StringUtil.IsEmpty(device_serialnumber))
		{
			sql.append("and dev_sub_sn = '"
					+ device_serialnumber.substring(device_serialnumber.length() - 6)
					+ "' and device_serialnumber like '%" + device_serialnumber + "'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		int total = jt.queryForInt(sql.toString());
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
