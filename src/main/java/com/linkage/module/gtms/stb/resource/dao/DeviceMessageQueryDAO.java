package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 *
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-3-21
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class DeviceMessageQueryDAO extends SuperDAO
{
	public List<Map> querymessage(String oui,String device_model,int curPage_splitPage, int num_splitPage )
	{
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append(" select d.vendor_add, c.device_model,b.specversion,b.hardwareversion, b.softwareversion,a.oui,b.rela_dev_type_id,f.type_name," +
				"b.access_style_relay_id, e.spec_name  from tab_gw_device a,tab_devicetype_info b  left join tab_bss_dev_port e on b.spec_id = e.id " +
				"left join gw_access_type f on b.access_style_relay_id = f.type_id,gw_device_model c,tab_vendor d where a.devicetype_id = b.devicetype_id " +
				"and b.vendor_id = d.vendor_id and b.device_model_id = c.device_model_id ");
		if(!StringUtil.IsEmpty(oui))
		{
			sql.append(" and a.oui='"+oui+"'");
		}
		if(!StringUtil.IsEmpty(device_model))
		{
			sql.append(" and c.device_model like '%"+device_model+"%'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage+1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("vendor_add", rs.getString("vendor_add"));
						map.put("device_model", rs.getString("device_model"));
						map.put("specversion", rs.getString("specversion"));
						map.put("hardwareversion", rs.getString("hardwareversion"));
						map.put("softwareversion", rs.getString("softwareversion"));
						map.put("oui", rs.getString("oui"));
						map.put("rela_dev_type_id", Global.G_DeviceTypeID_Name_Map.get(rs.getString("rela_dev_type_id")));
						map.put("type_name", rs.getString("type_name"));
						map.put("access_style_relay_id", rs.getString("access_style_relay_id"));
						map.put("spec_name", rs.getString("spec_name"));
						return map;
					}
				});
		return list;
	}

	public int getquerypaging(String oui,String device_model,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select count(*) from tab_gw_device a,tab_devicetype_info b  left join tab_bss_dev_port e on b.spec_id = e.id " +
				"left join gw_access_type f on b.access_style_relay_id = f.type_id,gw_device_model c,tab_vendor d where a.devicetype_id = b.devicetype_id " +
				"and b.vendor_id = d.vendor_id and b.device_model_id = c.device_model_id ");
		if(!StringUtil.IsEmpty(oui))
		{
			sql.append(" and a.oui='"+oui+"'");
		}
		if(!StringUtil.IsEmpty(device_model))
		{
			sql.append(" and c.device_model like '%"+device_model+"%'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
}
