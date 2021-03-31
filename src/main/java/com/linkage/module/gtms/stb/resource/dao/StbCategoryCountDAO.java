
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
 * 湖南联通机顶盒按类别统计
 */
@SuppressWarnings("rawtypes")
public class StbCategoryCountDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(StbCategoryCountDAO.class);

	private String category;
	/**
	 * 由于stb_dev_supplement跟设备表不是完全同步的，目前只统计出‘串货’的设备，不是串货的设备默认为行货
	 */
	public List countCategoryDevByCity(String city_id,int category)
	{
		logger.debug("countCategoryDevByCity({})",city_id);
		PrepareSQL psql = new PrepareSQL();
		// 统计表stb_dev_supplement存在设备数据的
		psql.append(" select a.city_id,count(*) as num ");
		psql.append(" from stb_tab_gw_device a,stb_dev_supplement b ");
		psql.append(" where a.device_status=1 and a.device_id=b.device_id and b.category = " + category);
		psql.append(pinCity(city_id));
		psql.append(" group by a.city_id ");
		psql.append(" order by a.city_id ");
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 由于stb_dev_supplement跟设备表不是完全同步的，目前只统计出‘串货’的设备，不是串货的设备默认为行货
	 */
	public List countCategoryDevByVendor(String vendor_id,int category)
	{
		logger.debug("countCategoryDevByVendor({})",vendor_id);
		PrepareSQL psql = new PrepareSQL();
		// 统计表stb_dev_supplement存在设备数据的
		psql.append(" select a.vendor_id,count(*) as c_num ");
		psql.append(" from stb_tab_gw_device a,stb_dev_supplement b ");
		psql.append(" where a.device_status=1 and a.device_id=b.device_id and b.category = " + category);
		if(!"00".equals(vendor_id) && !StringUtil.IsEmpty(vendor_id))
		{
			psql.append(" and vendor_id = '" + vendor_id +"'");
		}
		psql.append(" group by a.vendor_id ");
		psql.append(" order by a.vendor_id ");
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 按厂商统计设备总数
	 */
	public List countResultByVendor(String vendor_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append(" select vendor_id, count(*) as d_cum ");
		psql.append(" from stb_tab_gw_device where device_status=1 ");
		if(!"00".equals(vendor_id) && !StringUtil.IsEmpty(vendor_id))
		{
			psql.append(" and vendor_id = '" + vendor_id +"'");
		}
		psql.append(" group by vendor_id ");
		psql.append(" order by vendor_id ");
		return DBOperation.getRecords(psql.getSQL());
	}
	/**
	 * 按属地统计设备的数据
	 */
	@SuppressWarnings("unchecked")
	public List countResultByCity(String city_id)
	{
		logger.debug("countResultByCity({})",city_id);
		List result  = new ArrayList ();
		if ("00".equals(city_id))
		{
			ArrayList cityArray = getNextCityList(city_id);
			for (Object obj : cityArray)
			{
				Map<String, String> map = new HashMap();
				HashMap city = (HashMap) obj;
				String city_id1 = StringUtil.getStringValue(city, "city_id");
				PrepareSQL psql = new PrepareSQL();
				psql.append(" select count(*) as d_cum ");
				psql.append(" from stb_tab_gw_device a ");
				psql.append(" where a.device_status=1 ");
				psql.append(pinCity(city_id1));
				Map<String, String> tmp = jt.queryForMap(psql.getSQL());
				map.put("city_id", city_id1);
				map.put("d_cum", StringUtil.getStringValue(tmp, "d_cum", "0"));
				result.add(map);
			}
			//省中心需要查询一次属于省中心的设备
			PrepareSQL psq = new PrepareSQL();
			psq.append(" select count(*) as d_cum ");
			psq.append(" from stb_tab_gw_device a ");
			psq.append(" where a.device_status=1 and city_id = '00'");
			Map<String, String> temp = jt.queryForMap(psq.getSQL());
			Map<String, String> centerMap = new HashMap();
			centerMap.put("city_id", "00");
			centerMap.put("d_cum", StringUtil.getStringValue(temp, "d_cum", "0"));
			result.add(centerMap);
		}
		else
		{
			PrepareSQL psql = new PrepareSQL();
			psql.append(" select a.city_id ,count(*) as d_cum ");
			psql.append(" from stb_tab_gw_device a ");
			psql.append(" where a.device_status=1 ");
			psql.append(pinCity(city_id));
			psql.append(" group by city_id ");
			psql.append(" order by city_id ");
			result = jt.queryForList(psql.getSQL());
		}
		return result;
	}

	/**
	 * 查询指定属地、版本的设备详细
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> countResultByCityCategory(final String type,String city_id,String cityType,
			final String version,int curPage_splitPage, int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		if("串".equals(version))
		{
			category = "1";
		}else if ("行".equals(version))
		{
			category = "0";
		}
		if("city".equals(type))
		{
			if("1".equals(category) || "0".equals(category))
			{// TODO wait (more table related)
				psql.append(" select a.device_id, b.category, a.city_id, a.device_serialnumber, a.serv_account, ");
				psql.append(" a.cpe_mac, a.vendor_id,  a.device_model_id, c.hardwareversion, ");
				psql.append(" c.softwareversion from stb_tab_gw_device a, stb_dev_supplement b, stb_tab_devicetype_info c ");
				psql.append(" where a.device_id = b.device_id and b.category =" + category + " and a.devicetype_id=c.devicetype_id ");
				if("1".equals(cityType)){
					psql.append(pinCity(city_id));
				}else{
					psql.append(" and a.city_id='"+city_id+"' ");
				}

			}else if ("小计".equals(version))
			{
				psql.append(" select x.*, b.category from (select a.device_id, a.city_id, ");
				psql.append(" a.device_serialnumber, a.serv_account, a.cpe_mac, a.vendor_id, ");
				psql.append(" a.device_model_id, c.hardwareversion, c.softwareversion");
				psql.append(" from stb_tab_gw_device a, stb_tab_devicetype_info c where a.device_status= 1 ");
				psql.append(" and a.devicetype_id=c.devicetype_id ");
				if("1".equals(cityType)){
					psql.append(pinCity(city_id));
				}else{
					psql.append(" and a.city_id='"+city_id+"' ");
				}
				psql.append(")x left join stb_dev_supplement b on x.device_id = b.device_id ");

			}else
			{
				psql.append(" select a.device_id, '2' category, a.city_id, a.device_serialnumber, ");
				psql.append(" a.serv_account, a.cpe_mac, a.vendor_id, a.device_model_id, c.hardwareversion, ");
				psql.append(" c.softwareversion  from stb_tab_gw_device a, stb_tab_devicetype_info c ");
				psql.append(" where a.devicetype_id=c.devicetype_id ");
				if("1".equals(cityType)){
					psql.append(pinCity(city_id));
				}else{
					psql.append(" and a.city_id='"+city_id+"' ");
				}
				psql.append(" and a.device_id not in ");
				psql.append(" (select device_id from stb_dev_supplement where category = '1' or category = '0') ");
			}
		}else if("vendor".equals(type))
		{
			if("1".equals(category) || "0".equals(category))
			{// TODO wait (more table related)
				psql.append(" select a.device_id, b.category, a.city_id, a.device_serialnumber, a.serv_account, ");
				psql.append(" a.cpe_mac, a.vendor_id,  a.device_model_id, c.hardwareversion, ");
				psql.append(" c.softwareversion from stb_tab_gw_device a, stb_dev_supplement b, stb_tab_devicetype_info c ");
				psql.append(" where a.device_id = b.device_id and b.category =" + category + " and a.devicetype_id=c.devicetype_id ");
				if(!"00".equals(city_id) && !StringUtil.IsEmpty(city_id))
				{
					psql.append(" and a.vendor_id = '" + city_id +"'");
				}

			}else if ("小计".equals(version))
			{
				psql.append(" select x.*, b.category from (select a.device_id, a.city_id, ");
				psql.append(" a.device_serialnumber, a.serv_account, a.cpe_mac, a.vendor_id, ");
				psql.append(" a.device_model_id, c.hardwareversion, c.softwareversion");
				psql.append(" from stb_tab_gw_device a, stb_tab_devicetype_info c where a.device_status= 1 ");
				psql.append(" and a.devicetype_id=c.devicetype_id ");
				if(!"00".equals(city_id) && !StringUtil.IsEmpty(city_id))
				{
					psql.append(" and a.vendor_id = '" + city_id +"'");
				}
				psql.append(")x left join stb_dev_supplement b on x.device_id = b.device_id ");

			}else
			{
				psql.append(" select a.device_id, '2' category, a.city_id, a.device_serialnumber, ");
				psql.append(" a.serv_account, a.cpe_mac, a.vendor_id, a.device_model_id, c.hardwareversion, ");
				psql.append(" c.softwareversion  from stb_tab_gw_device a, stb_tab_devicetype_info c ");
				psql.append(" where a.devicetype_id=c.devicetype_id ");
				if(!"00".equals(city_id) && !StringUtil.IsEmpty(city_id))
				{
					psql.append(" and a.vendor_id = '" + city_id +"'");
				}
				psql.append(" and a.device_id not in ");
				psql.append(" (select device_id from stb_dev_supplement where category = '1' or category = '0') ");
			}
		}
		final Map<String,String> vendorMap=getVendorName();
		final Map<String,String> deviceModelMap=getDeviceModle();
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("cityName",CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
						String cate = rs.getString("category");
						if ("0".equals(cate))
						{
							map.put("category", "行");
						}
						else if ("1".equals(cate))
						{
							map.put("category", "串");
						}
						else
						{
							map.put("category", "未知");
						}
						map.put("deviceSerialnumber", rs.getString("device_serialnumber"));
						map.put("servAccount", rs.getString("serv_account"));
						map.put("cpeMac", rs.getString("cpe_mac"));
						map.put("vendorName", vendorMap.get(rs.getString("vendor_id")));
						map.put("deviceModel",deviceModelMap.get(rs.getString("device_model_id")));
						map.put("softwareversion", rs.getString("softwareversion"));
						map.put("hardwareversion", rs.getString("hardwareversion"));
						return map;
					}
				});
		return list;
	}

	/**
	 * 获取指定属地、串货行货的数据,由于stb_dev_supplement表中的数据跟stb_tab_gw_device并不统一，设备默认为2：未知
	 * 所以需要先统计出stb_tab_gw_device的行、串货数量，其余设备默认为未知
	 */
	public int getCountResultByCityVersion(String type,String city_id,String cityType,String version)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) ");
		psql.append("from stb_tab_gw_device a, stb_dev_supplement b where a.device_status=1 ");
		psql.append("and a.device_id = b.device_id and b.category = 1");
		if("city".equals(type))
		{
			if ("1".equals(cityType))
			{
				psql.append(pinCity(city_id));
			}
			else
			{
				psql.append(" and a.city_id='" + city_id + "' ");
			}
		}else if ("vendor".equals(type) && !"00".equals(city_id))
		{
			psql.append(" and a.vendor_id='" + city_id + "' ");
		}
		int cate_sum = jt.queryForInt(psql.getSQL());
		psql = null;
		psql = new PrepareSQL();
		psql.append("select count(*) ");
		psql.append("from stb_tab_gw_device a, stb_dev_supplement b where a.device_status=1 ");
		psql.append("and a.device_id = b.device_id and b.category = 0");
		if("city".equals(type))
		{
			if ("1".equals(cityType))
			{
				psql.append(pinCity(city_id));
			}
			else
			{
				psql.append(" and a.city_id='" + city_id + "' ");
			}
		}else if ("vendor".equals(type) && !"00".equals(city_id))
		{
			psql.append(" and a.vendor_id='" + city_id + "' ");
		}

		int unCate_sum = jt.queryForInt(psql.getSQL());
		psql = null;
		psql = new PrepareSQL();
		psql.append("select count(*) ");
		psql.append("from stb_tab_gw_device a where a.device_status=1 ");
		if("city".equals(type))
		{
			if ("1".equals(cityType))
			{
				psql.append(pinCity(city_id));
			}
			else
			{
				psql.append(" and a.city_id='" + city_id + "' ");
			}
		}else if ("vendor".equals(type) && !"00".equals(city_id) )
		{
			psql.append(" and a.vendor_id='" + city_id + "' ");
		}
		int sum = jt.queryForInt(psql.getSQL());

		if (!StringUtil.IsEmpty(version) && "小计".equals(version))
		{
			return sum;
		}
		else if (!StringUtil.IsEmpty(version) && "串".equals(version))
		{
			return cate_sum;
		}
		else if (!StringUtil.IsEmpty(version) && "行".equals(version))
		{
			return unCate_sum;

		}else if (!StringUtil.IsEmpty(version) && "未知".equals(version))
		{
			return sum - cate_sum - unCate_sum;
		}
		return 0;
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

	public ArrayList<HashMap<String, String>> getNextCityList(String city_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select city_id from tab_city where parent_id = ?  order by city_id ");
		psql.setString(1, city_id);
		return DBOperation.getRecords(psql.getSQL());
	}

	/**
	 * 获取所有厂商
	 */
	public ArrayList<HashMap<String, String>> getVendorList(String vendor_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select vendor_id,vendor_name,vendor_add from stb_tab_vendor ");
		if(!StringUtil.IsEmpty(vendor_id) && !"00".equals(vendor_id))
		{
			psql.append(" where vendor_id = '" + vendor_id +"'");
		}
		psql.append(" order by vendor_id ");
		return DBOperation.getRecords(psql.getSQL());
	}
}
