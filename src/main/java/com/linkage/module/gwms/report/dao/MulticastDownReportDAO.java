package com.linkage.module.gwms.report.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.litms.common.database.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class MulticastDownReportDAO extends SuperDAO
{
	Logger logger = LoggerFactory.getLogger(MulticastDownReportDAO.class);

	private Map<String, String> cityMap = null;


	public Map queryDevice(String loid) 
	{
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置格式
		Map resultMap = null;
		cityMap = CityDAO.getCityIdCityNameMap();
	//	Set<String> entry = cityMap.keySet();
		PrepareSQL psql = new PrepareSQL();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		psql.append("select a.city_id, a.username,b.device_serialnumber ,b.complete_time, f.last_time, d.vendor_name,e.device_model,     " +
				"  c.hardwareversion,c.softwareversion  " +
				" from tab_hgwcustomer a left join tab_gw_device b on a.device_id = b.device_id ,tab_devicetype_info c,tab_vendor d,gw_device_model e ,gw_devicestatus f " +
				"  where b.devicetype_id = c.devicetype_id and b.device_id = f.device_id " +
				" and c.vendor_id = d.vendor_id and c.device_model_id = e.device_model_id and   a.username = ? ");
		psql.setString(1,loid);
		logger.warn(psql.getSQL());
		resultMap = DBOperation.getRecord(psql.getSQL());

		if(null != resultMap)
		{
			resultMap.put("city_name",cityMap.get(StringUtil.getStringValue(resultMap.get("city_id"))));
			long completeTime = StringUtil.getLongValue(resultMap.get("complete_time"));
			long lastTime = StringUtil.getLongValue(resultMap.get("last_time"));
			resultMap.put("complete_time",format.format(completeTime * 1000));
			resultMap.put("last_time",format.format(lastTime * 1000));
			return resultMap;
		}
		psql = new PrepareSQL();
		psql.append("select city_id,username from tab_hgwcustomer where username = ? ");
		psql.setString(1,loid);
		logger.warn(psql.getSQL());
		resultMap = DBOperation.getRecord(psql.getSQL());
		if(null != resultMap)
		{
			resultMap.put("city_name",cityMap.get(StringUtil.getStringValue(resultMap.get("city_id"))));
			resultMap.put("username",loid);
			return resultMap;
		}

		PrepareSQL psql1 = new PrepareSQL();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		psql1.append("select a.city_id, a.username,b.device_serialnumber ,b.complete_time, f.last_time, d.vendor_name,e.device_model ,    " +
				"  c.hardwareversion,c.softwareversion     " +
				" from tab_egwcustomer a left join tab_gw_device b on a.device_id = b.device_id ,tab_devicetype_info c,tab_vendor d,gw_device_model e ,gw_devicestatus f " +
				"  where b.devicetype_id = c.devicetype_id and b.device_id = f.device_id " +
				" and c.vendor_id = d.vendor_id and c.device_model_id = e.device_model_id and   a.username = ? ");
		psql1.setString(1,loid);
		logger.warn(psql1.getSQL());
		resultMap = DBOperation.getRecord(psql1.getSQL());
		if(null != resultMap)
		{
			resultMap.put("city_name",cityMap.get(StringUtil.getStringValue(resultMap.get("city_id"))));
			long completeTime = StringUtil.getLongValue(resultMap.get("complete_time"));
			long lastTime = StringUtil.getLongValue(resultMap.get("last_time"));
			resultMap.put("complete_time",format.format(completeTime * 1000));
			resultMap.put("last_time",format.format(lastTime * 1000));
			return resultMap;
		}

		psql = new PrepareSQL();
		psql.append("select city_id,username from tab_egwcustomer where username = ? ");
		psql.setString(1,loid);
		logger.warn(psql.getSQL());
		resultMap = DBOperation.getRecord(psql.getSQL());
		if(null != resultMap)
		{
			resultMap.put("city_name",cityMap.get(StringUtil.getStringValue(resultMap.get("city_id"))));
			resultMap.put("username",loid);
			return resultMap;
		}


		resultMap = new HashMap();
		resultMap.put("username",loid);
		return resultMap;
	}

	
	public Map queryBusiness(String loid) {
		Map resultMap = new HashMap();
		cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		PrepareSQL psql1 = new PrepareSQL();
		psql1.append("select a.user_id,a.city_id,a.username as loid,b.serv_type_id,b.username," +
				"	b.vlanid,b.open_status " +
				" from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id = b.user_id " +
				"  where a.username = ?  ");
		psql1.setString(1,loid);
		list = DBOperation.getRecords(psql1.getSQL());

		if(null != list && !list.isEmpty())
		{
			resultMap.put("city_name",cityMap.get(StringUtil.getStringValue(list.get(0).get("city_id"))));
			resultMap.put("username",loid);
			for(Map map : list)
			{
				int servTypeId = StringUtil.getIntegerValue(map.get("serv_type_id"));
				if(10 == servTypeId)
				{
					resultMap.put("netAccount",StringUtil.getStringValue(map.get("username")));
					resultMap.put("netVlan",StringUtil.getStringValue(map.get("vlanid")));
					String openStatus = StringUtil.getStringValue(map.get("open_status"));
					if("0".equals(openStatus))
					{
						resultMap.put("netOpenStatus","未做");
					}
					else if("1".equals(openStatus))
					{
						resultMap.put("netOpenStatus","成功");
					}
					else
					{
						resultMap.put("netOpenStatus","失败");
					}
				}
				else if(11 == servTypeId)
				{
					resultMap.put("itvAccount",StringUtil.getStringValue(map.get("username")));
					resultMap.put("itvVlan",StringUtil.getStringValue(map.get("vlanid")));
					String openStatus = StringUtil.getStringValue(map.get("open_status"));
					if("0".equals(openStatus))
					{
						resultMap.put("itvOpenStatus","未做");
					}
					else if("1".equals(openStatus))
					{
						resultMap.put("itvOpenStatus","成功");
					}
					else
					{
						resultMap.put("itvOpenStatus","失败");
					}
				}
				else if(28 == servTypeId)
				{
					resultMap.put("vpdnAccount",StringUtil.getStringValue(map.get("username")));
					resultMap.put("vpdnVlan",StringUtil.getStringValue(map.get("vlanid")));
					String openStatus = StringUtil.getStringValue(map.get("open_status"));
					if("0".equals(openStatus))
					{
						resultMap.put("vpdnOpenStatus","未做");
					}
					else if("1".equals(openStatus))
					{
						resultMap.put("vpdnOpenStatus","成功");
					}
					else
					{
						resultMap.put("vpdnOpenStatus","失败");
					}
				}
				else if(14 == servTypeId)
				{
					resultMap.put("voipVlan",StringUtil.getStringValue(map.get("vlanid")));
					String openStatus = StringUtil.getStringValue(map.get("open_status"));
					if("0".equals(openStatus))
					{
						resultMap.put("voipOpenStatus","未做");
					}
					else if("1".equals(openStatus))
					{
						resultMap.put("voipOpenStatus","成功");
					}
					else
					{
						resultMap.put("voipOpenStatus","失败");
					}
					String userId = StringUtil.getStringValue(map.get("user_id"));
					PrepareSQL psql2 = new PrepareSQL();
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						psql2.append("select group_concat(a.voip_phone order by a.voip_port separator ',') as voip_phone,");
					}else{
						psql2.append("select listagg(a.voip_phone,',') within group (order by a.voip_port) as voip_phone,");
					}
					psql2.append("min(a.protocol) as protocol from tab_voip_serv_param a where a.user_id=? ");
					psql2.setString(1,userId);
					logger.warn(psql2.getSQL());
					Map result = DBOperation.getRecord(psql2.getSQL());

					if(null != result)
					{
						String voipPhone = StringUtil.getStringValue(result.get("voip_phone"));
						String protocol = StringUtil.getStringValue(result.get("protocol"));
						resultMap.put("voipPhone",voipPhone);
						if("0".equals(protocol))
						{
							resultMap.put("voipProtocol","IMS SIP");
						}
						else if("2".equals(protocol))
						{
							resultMap.put("voipProtocol","H248");
						}
						else
						{
							resultMap.put("voipProtocol","软交换");
						}
					}
				}
			}
			return  resultMap;
		}

		PrepareSQL psql3 = new PrepareSQL();
		psql3.append("select a.user_id,a.city_id,a.username as loid,b.serv_type_id,b.username,b.vlanid,b.open_status " +
				" from tab_egwcustomer a left join egwcust_serv_info b on a.user_id = b.user_id " +
				"  where a.username = ?  ");
		psql3.setString(1,loid);
		logger.warn(psql3.getSQL());
		list = DBOperation.getRecords(psql3.getSQL());

		if(null != list && !list.isEmpty())
		{
			resultMap.put("city_name",cityMap.get(StringUtil.getStringValue(list.get(0).get("city_id"))));
			resultMap.put("username",loid);
			for(Map map : list)
			{
				int servTypeId = StringUtil.getIntegerValue(map.get("serv_type_id"));
				if(10 == servTypeId)
				{
					resultMap.put("netAccount",StringUtil.getStringValue(map.get("username")));
					resultMap.put("netVlan",StringUtil.getStringValue(map.get("vlanid")));
					String openStatus = StringUtil.getStringValue(map.get("open_status"));
					if("0".equals(openStatus))
					{
						resultMap.put("netOpenStatus","未做");
					}
					else if("1".equals(openStatus))
					{
						resultMap.put("netOpenStatus","成功");
					}
					else
					{
						resultMap.put("netOpenStatus","失败");
					}
				}
				else if(11 == servTypeId)
				{
					resultMap.put("itvAccount",StringUtil.getStringValue(map.get("username")));
					resultMap.put("itvVlan",StringUtil.getStringValue(map.get("vlanid")));
					String openStatus = StringUtil.getStringValue(map.get("open_status"));
					if("0".equals(openStatus))
					{
						resultMap.put("itvOpenStatus","未做");
					}
					else if("1".equals(openStatus))
					{
						resultMap.put("itvOpenStatus","成功");
					}
					else
					{
						resultMap.put("itvOpenStatus","失败");
					}
				}
				else if(28 == servTypeId)
				{
					resultMap.put("vpdnAccount",StringUtil.getStringValue(map.get("username")));
					resultMap.put("vpdnVlan",StringUtil.getStringValue(map.get("vlanid")));
					String openStatus = StringUtil.getStringValue(map.get("open_status"));
					if("0".equals(openStatus))
					{
						resultMap.put("vpdnOpenStatus","未做");
					}
					else if("1".equals(openStatus))
					{
						resultMap.put("vpdnOpenStatus","成功");
					}
					else
					{
						resultMap.put("vpdnOpenStatus","失败");
					}
				}
				else if(14 == servTypeId)
				{
					resultMap.put("voipVlan",StringUtil.getStringValue(map.get("vlanid")));
					String openStatus = StringUtil.getStringValue(map.get("open_status"));
					if("0".equals(openStatus))
					{
						resultMap.put("voipOpenStatus","未做");
					}
					else if("1".equals(openStatus))
					{
						resultMap.put("voipOpenStatus","成功");
					}
					else
					{
						resultMap.put("voipOpenStatus","失败");
					}
					String userId = StringUtil.getStringValue(map.get("user_id"));
					PrepareSQL psql2 = new PrepareSQL();
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						psql2.append("select group_concat(a.voip_phone order by a.voip_port separator ',') as voip_phone,");
					}else{
						psql2.append("select listagg(a.voip_phone,',') within group (order by a.voip_port) as voip_phone,");
					}
					psql2.append("min(a.protocol) as protocol from tab_egw_voip_serv_param a where a.user_id=? ");
					psql2.setString(1,userId);
					Map result = DBOperation.getRecord(psql2.getSQL());

					if(null != result)
					{
						String voipPhone = StringUtil.getStringValue(result.get("voip_phone"));
						String protocol = StringUtil.getStringValue(result.get("protocol"));
						resultMap.put("voipPhone",voipPhone);
						if("0".equals(protocol))
						{
							resultMap.put("voipProtocol","IMS SIP");
						}
						else if("2".equals(protocol))
						{
							resultMap.put("voipProtocol","H248");
						}
						else
						{
							resultMap.put("voipProtocol","软交换");
						}
					}
				}
			}
			return  resultMap;
		}
		resultMap = new HashMap();
		resultMap.put("username",loid);
		return resultMap;
	}
}
