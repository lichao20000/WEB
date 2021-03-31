
package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.utils.StringUtils;

/**
 * 湖南联通机顶盒按EPG或APK版本统计
 */
@SuppressWarnings("rawtypes")
public class StbCountReportDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(StbCountReportDAO.class);

	/**
	 * 获取所有epg/apk版本数据
	 */
	public List getVersion(String type)
	{
		logger.debug("getVersion()");
		PrepareSQL psql = new PrepareSQL();
		if("epg".equals(type)){
			psql.append("select epg_version as version from stb_tab_devicetype_info group by epg_version order by epg_version");
		}else if("apk".equals(type)){
			psql.append("select apk_version_name as version from stb_dev_supplement group by apk_version_name order by apk_version_name");
		}

		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 按属地、epg/apk版本统计
	 */
	public List countResultByCity(String type,String city_id)
	{
		logger.debug("countResultByCity({})",city_id);
		PrepareSQL psql = new PrepareSQL();
		if("epg".equals(type))
		{
			psql.append("select b.epg_version as version,a.city_id,count(*) as c_num ");
			psql.append("from stb_tab_devicetype_info b ");
			psql.append("left join stb_tab_gw_device a on a.devicetype_id=b.devicetype_id ");
			psql.append("where a.device_status=1 ");
			psql.append(pinCity(city_id));
			psql.append("group by b.epg_version,a.city_id ");
			psql.append("order by b.epg_version,a.city_id ");
		}
		else if("apk".equals(type))
		{
			//统计表stb_dev_supplement存在设备数据的
			psql.append("select b.apk_version_name as version,a.city_id,count(*) as c_num ");
			psql.append("from stb_tab_gw_device a,stb_dev_supplement b ");
			psql.append("where a.device_status=1 and a.device_id=b.device_id ");
			psql.append(pinCity(city_id));
			psql.append("group by b.apk_version_name,a.city_id ");
			psql.append("order by b.apk_version_name,a.city_id ");
		}

		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 按属地统计表stb_dev_supplement不存在设备的数据
	 */
	public List countResultByCityNull(String city_id)
	{
		logger.debug("countResultByCityNull({})",city_id);
		PrepareSQL psql = new PrepareSQL();
		psql.append("select 'Unknown' as version,a.city_id,count(*) as c_num ");
		psql.append("from stb_tab_gw_device a ");
		psql.append("where a.device_status=1 ");
		psql.append("and not exists(select 1 from stb_dev_supplement b where a.device_id=b.device_id) ");
		psql.append(pinCity(city_id));
		psql.append("group by a.city_id ");
		psql.append("order by a.city_id ");

		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 查询指定属地、版本的设备详细
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> countResultByCityVersion(final String type,String city_id,String cityType,
			final String version,int curPage_splitPage, int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		if("epg".equals(type))
		{// TODO wait (more table related)
			psql.append("select x.*,b.serv_account from (");
			psql.append("select a.city_id,a.device_serialnumber,a.vendor_id,a.cpe_mac,a.customer_id,");
			psql.append("a.device_model_id,d.epg_version as version,d.hardwareversion,d.softwareversion ");
			psql.append("from stb_tab_gw_device a,stb_tab_devicetype_info d ");
			psql.append("where a.device_status=1 and a.devicetype_id=d.devicetype_id ");

			if("1".equals(cityType)){
				psql.append(pinCity(city_id));
			}else{
				psql.append(" and a.city_id='"+city_id+"' ");
			}

			if(!StringUtil.IsEmpty(version) && !"小计".equals(version)){
				psql.append(" and d.epg_version='"+version+"'");
			}

			psql.append(") x left join stb_tab_customer b on x.customer_id=b.customer_id ");
		}
		else if("apk".equals(type))
		{
			psql.append("select x.*,b.serv_account from (");

			if("Unknown".equals(version))
			{// TODO wait (more table related)
				psql.append("select a.city_id,a.device_serialnumber,a.vendor_id,a.cpe_mac,");
				psql.append("a.customer_id,c.softwareversion,c.hardwareversion,a.device_id,");
				psql.append("a.device_model_id,'Unknown' as version ");
				psql.append("from stb_tab_gw_device a,stb_tab_devicetype_info c ");
				psql.append("where a.device_status=1 and a.devicetype_id=c.devicetype_id ");
				psql.append("and not exists(select 1 from stb_dev_supplement b where a.device_id=b.device_id) ");

				if("1".equals(cityType)){
					psql.append(pinCity(city_id));
				}else{
					psql.append(" and a.city_id='"+city_id+"' ");
				}
			}
			else if(!"小计".equals(version))
			{// TODO wait (more table related)
				psql.append("select a.city_id,a.device_serialnumber,a.vendor_id,a.cpe_mac,");
				psql.append("a.customer_id,c.softwareversion,c.hardwareversion,");
				psql.append("a.device_model_id,f.apk_version_name as version,f.device_id ");
				psql.append("from stb_tab_gw_device a,stb_tab_devicetype_info c,stb_dev_supplement f ");
				psql.append("where a.device_status=1 and a.device_id=f.device_id ");
				psql.append("and a.devicetype_id=c.devicetype_id ");
				if(StringUtil.IsEmpty(version)){
					psql.append(" and f.apk_version_name is null ");
				}else{
					psql.append(" and f.apk_version_name='"+version+"' ");
				}

				if("1".equals(cityType)){
					psql.append(pinCity(city_id));
				}else{
					psql.append(" and a.city_id='"+city_id+"' ");
				}
			}
			else
			{// TODO wait (more table related)
				psql.append("select z.*,f.apk_version_name as version,f.device_id as f_id from (");
				psql.append("select a.city_id,a.device_serialnumber,a.vendor_id,a.cpe_mac,a.device_id,");
				psql.append("a.customer_id,c.softwareversion,c.hardwareversion,a.device_model_id ");
				psql.append("from stb_tab_gw_device a,stb_tab_devicetype_info c ");
				psql.append("where a.device_status=1 and a.devicetype_id=c.devicetype_id ");
				if("1".equals(cityType)){
					psql.append(pinCity(city_id));
				}else{
					psql.append(" and a.city_id='"+city_id+"' ");
				}
				psql.append(") z left join stb_dev_supplement f on z.device_id=f.device_id");
			}

			psql.append(") x left join stb_tab_customer b on x.customer_id=b.customer_id ");
		}

		final Map<String,String> vendorMap=getVendorName();
		final Map<String,String> deviceModelMap=getDeviceModle();

		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage+1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("cityName",CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));

						if("apk".equals(type) && "小计".equals(version)
								&& StringUtil.IsEmpty(rs.getString("f_id"))){
							map.put("version","Unknown");
						}else{
							map.put("version", rs.getString("version"));
						}

						map.put("deviceSerialnumber", rs.getString("device_serialnumber"));
						map.put("servAccount", rs.getString("serv_account"));
						map.put("cpeMac", rs.getString("cpe_mac"));
						map.put("vendorName", vendorMap.get(rs.getString("vendor_id")));
						map.put("deviceModel", deviceModelMap.get(rs.getString("device_model_id")));
						map.put("softwareversion",rs.getString("softwareversion"));
						map.put("hardwareversion",rs.getString("hardwareversion"));

						return map;
					}
				});

		return list;
	}

	/**
	 * 获取指定属地、epg/apk版本的设备总量
	 */
	public int getCountResultByCityVersion(String type,String city_id,String cityType,String version)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) ");

		if("epg".equals(type))
		{// TODO wait (more table related)
			psql.append("from stb_tab_gw_device a,stb_gw_device_model c,stb_tab_devicetype_info d ");
			psql.append("where a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
			if("1".equals(cityType)){
				psql.append(pinCity(city_id));
			}else{
				psql.append(" and a.city_id='"+city_id+"' ");
			}

			if(!StringUtil.IsEmpty(version) && !"小计".equals(version)){
				psql.append(" and d.epg_version='"+version+"'");
			}
		}
		else if("apk".equals(type))
		{
			if("Unknown".equals(version))
			{
				psql.append("from stb_tab_gw_device a,stb_tab_devicetype_info c ");
				psql.append("where a.device_status=1 and a.devicetype_id=c.devicetype_id ");
				psql.append("and not exists(select 1 from stb_dev_supplement b where a.device_id=b.device_id) ");

				if("1".equals(cityType)){
					psql.append(pinCity(city_id));
				}else{
					psql.append(" and a.city_id='"+city_id+"' ");
				}
			}
			else if(!"小计".equals(version))
			{// TODO wait (more table related)
				psql.append("from stb_tab_gw_device a,stb_tab_devicetype_info c,stb_dev_supplement f ");
				psql.append("where a.device_status=1 and a.device_id=f.device_id ");
				psql.append("and a.devicetype_id=c.devicetype_id ");
				if(StringUtil.IsEmpty(version)){
					psql.append(" and f.apk_version_name is null ");
				}else{
					psql.append(" and f.apk_version_name='"+version+"' ");
				}

				if("1".equals(cityType)){
					psql.append(pinCity(city_id));
				}else{
					psql.append(" and a.city_id='"+city_id+"' ");
				}
			}
			else
			{
				psql.append("from stb_tab_gw_device a,stb_tab_devicetype_info c ");
				psql.append("where a.device_status=1 and a.devicetype_id=c.devicetype_id ");
				if("1".equals(cityType)){
					psql.append(pinCity(city_id));
				}else{
					psql.append(" and a.city_id='"+city_id+"' ");
				}
			}
		}

		return jt.queryForInt(psql.getSQL());
	}

	/**
	 * 拼接a表属地条件
	 */
	private String pinCity(String city_id)
	{
		StringBuffer sb=new StringBuffer();
		if(!StringUtil.IsEmpty(city_id) && !"00".equals(city_id) && !"-1".equals(city_id)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sb.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "') ");
			cityArray = null;
		}

		return sb.toString();
	}

	/**
	 * 获取所有厂商
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getVendorName()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select vendor_id,vendor_name,vendor_add from stb_tab_vendor ");
		List list=jt.queryForList(psql.getSQL());

		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				if(StringUtil.IsEmpty(StringUtil.getStringValue((Map<String,String>)list.get(i),"vendor_add"))){
					map.put(StringUtil.getStringValue((Map<String,String>)list.get(i),"vendor_id"),
							StringUtil.getStringValue((Map<String,String>)list.get(i),"vendor_name"));
				}else{
					map.put(StringUtil.getStringValue((Map<String,String>)list.get(i),"vendor_id"),
							StringUtil.getStringValue((Map<String,String>)list.get(i),"vendor_add"));
				}
			}
		}

		return map;
	}

	/**
	 * 获取所有型号
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getDeviceModle()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_model_id,device_model from stb_gw_device_model ");
		List list=jt.queryForList(psql.getSQL());

		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				map.put(StringUtil.getStringValue((Map<String,String>)list.get(i),"device_model_id"),
						StringUtil.getStringValue((Map<String,String>)list.get(i),"device_model"));
			}
		}

		return map;
	}

	/**
	 * @param city_id
	 * @return
	 * 2019-9-12

	 */
	public ArrayList<HashMap<String, String>> getNextCityListOrderByCityPid(String city_id)
	{
		// TODO Auto-generated method stub
		PrepareSQL psql = new PrepareSQL();
		psql.append("select city_id,city_name from tab_city where city_id = ? or parent_id = ? order by city_id ");
		psql.setString(1, city_id);
		psql.setString(2, city_id);

		return DBOperation.getRecords(psql.getSQL());
	}

}
