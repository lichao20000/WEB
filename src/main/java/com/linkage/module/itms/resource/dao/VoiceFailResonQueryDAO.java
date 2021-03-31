package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-30
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class VoiceFailResonQueryDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(VoiceFailResonQueryDAO.class);
	
	private Map<String, String> cityMap;
	private Map<String, String> modelMap;
	private Map<String,String> reasonMap = new HashMap<String, String>();
	
	public VoiceFailResonQueryDAO(){
		reasonMap.put("0", "成功");
		reasonMap.put("1", "IAD模块错误");
		reasonMap.put("2", "访问路由不通");
		reasonMap.put("3", "访问服务器无响应");
		reasonMap.put("4", "帐号、密码错误");
		reasonMap.put("5", "未知错误");
	}
	/**
	 * 失败原因一
	 * @param start_time
	 * @param end_time
	 * @param city_id
	 * @return
	 */
	public Map<String,String> voiceFailOneInfo(String start_time,String end_time,String city_id){
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,count(1) num from ");
		sql.append(" (select distinct loid,reason,oui,device_serialnumber  from tab_voicestatus_info where 1=1 ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(" ) a , tab_gw_device b where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.reason='1'");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" group by b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}
	
	/**
	 * 失败原因2
	 * @param start_time
	 * @param end_time
	 * @param city_id
	 * @return
	 */
	public Map<String,String> voiceFailTwoInfo(String start_time,String end_time,String city_id){
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,count(1) num from ");
		sql.append(" (select distinct loid,reason,oui,device_serialnumber  from tab_voicestatus_info where 1=1 ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(" ) a , tab_gw_device b where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.reason='2'");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" group by b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}
	
	/**
	 * 失败原因3
	 * @param start_time
	 * @param end_time
	 * @param city_id
	 * @return
	 */
	public Map<String,String> voiceFailThreeInfo(String start_time,String end_time,String city_id){
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,count(1) num from ");
		sql.append(" (select distinct loid,reason,oui,device_serialnumber  from tab_voicestatus_info where 1=1 ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(" ) a , tab_gw_device b where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.reason='3'");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" group by b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}
	
	/**
	 * 失败原因4
	 * @param start_time
	 * @param end_time
	 * @param city_id
	 * @return
	 */
	public Map<String,String> voiceFailFourInfo(String start_time,String end_time,String city_id){
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,count(1) num from ");
		sql.append(" (select distinct loid,reason,oui,device_serialnumber  from tab_voicestatus_info where 1=1 ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(" ) a , tab_gw_device b where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.reason='4'");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" group by b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}
	
	
	/**
	 * 失败原因5
	 * @param start_time
	 * @param end_time
	 * @param city_id
	 * @return
	 */
	public Map<String,String> voiceFailFiveInfo(String start_time,String end_time,String city_id){
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,count(1) num from ");
		sql.append(" (select distinct loid,reason,oui,device_serialnumber  from tab_voicestatus_info where 1=1 ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(" ) a , tab_gw_device b where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.reason='5'");
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" group by b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}
	
	public List<Map> voiceFailDeviceQueryInfo(String city_id, String reason, String start_time,
			String end_time, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("voiceDeviceQueryInfo()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,a.loid,a.device_serialnumber,a.reason,a.line_id,a.enabled,b.device_type,b.device_model_id,a.status,d.voip_phone,a.add_time");
		sql.append(" from tab_voicestatus_info a,tab_gw_device b, tab_hgwcustomer c  ,tab_voip_serv_param d ");
		sql.append(" where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.loid=c.username and c.user_id=d.user_id ");
		if(!StringUtil.IsEmpty(reason)){
			sql.append(" and a.reason='").append(reason).append("'  ");
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.upload_time<=").append(end_time);
		}
		sql.append(" order by a.line_id,c.username,b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		modelMap = getDeviceModel();
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list = new ArrayList<Map>();
		list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					@Override
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String cityId = StringUtil.getStringValue(rs.getString("city_id"));
						map.put("line_id", StringUtil.getStringValue(rs.getString("line_id")));
						map.put("city_id", cityId);
						map.put("city_name", cityMap.get(cityId));
						String device_model_id = StringUtil.getStringValue(rs
								.getString("device_model_id"));
						map.put("device_id", device_model_id);
						map.put("device_model", modelMap.get(device_model_id));
						map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
						map.put("device_serialnumber", StringUtil.getStringValue(rs
								.getString("device_serialnumber")));
						map.put("device_type",
								StringUtil.getStringValue(rs.getString("device_type")));
						String status = StringUtil.getStringValue(rs.getString("status"));
						map.put("status", status);
						map.put("voip_phone",
								StringUtil.getStringValue(rs.getString("voip_phone")));
						String enabled = StringUtil.getStringValue(rs
								.getString("enabled"));
						String message2 = "";
						if (!StringUtil.IsEmpty(enabled))
						{
							if ("Enabled".equals(enabled))
							{
								message2 = "启用";
							}
							else if ("Disabled".equals(enabled))
							{
								message2 = "未启用";
							}
						}
						map.put("enabled", message2);
						String reason_id = StringUtil.getStringValue(rs.getString("reason"));
						map.put("reason_id", reason_id);
						map.put("reason",reasonMap.get(reason_id));
						map.put("add_time", DateUtil.transTime(rs.getLong("add_time"), "yyyy-MM-dd HH:mm:ss"));
						return map;
					}
				});
		return list;
	}

	public int countVoiceFailDeviceQueryInfo(String city_id,String reason, String start_time,
			String end_time, int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1)  from ");
		sql.append(" tab_voicestatus_info a,tab_gw_device b,tab_hgwcustomer  c ,tab_voip_serv_param d ");
		sql.append(" where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.loid=c.username and c.user_id=d.user_id ");
		if(!StringUtil.IsEmpty(reason)){
			sql.append(" and a.reason='").append(reason).append("'  ");
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.upload_time<=").append(end_time);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
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
	
	/**
	 * @category getDevicetype 获取所有的设备型号
	 * @param vendorId
	 * @return List
	 */
	public Map<String, String> getDeviceModel()
	{
		logger.debug("DeviceServiceDAO=>getDeviceModel()");
		Map<String, String> modelMap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(
				"select device_model_id,device_model from gw_device_model");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				modelMap.put(
						StringUtil.getStringValue(list.get(i).get("device_model_id")),
						StringUtil.getStringValue(list.get(i).get("device_model")));
			}
		}
		return modelMap;
	}
	
	
	public List<Map> voiceFailDeviceQueryExcel(String city_id, String reason, String start_time, String end_time)
	{
		logger.debug("voiceDeviceQueryInfo()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,a.loid,a.device_serialnumber,a.reason,a.line_id,a.enabled,b.device_type,b.device_model_id,a.status,d.voip_phone  from ");
		sql.append(" tab_voicestatus_info a,tab_gw_device b, tab_hgwcustomer c  ,tab_voip_serv_param d  ");
		sql.append(" where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.loid=c.username and c.user_id=d.user_id ");
		if(!StringUtil.IsEmpty(reason)){
			sql.append(" and a.reason='").append(reason).append("'  ");
		}
		if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id) && !"00".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and a.upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.upload_time<=").append(end_time);
		}
		sql.append(" order by a.line_id,c.username,b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		modelMap = getDeviceModel();
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list = jt.queryForList(psql.getSQL());
		if(null!=list && list.size()>0){
			for (int i = 0; i < list.size(); i++)
			{
				String cityId = StringUtil.getStringValue(list.get(i).get("city_id"));
				String line_id = StringUtil.getStringValue(list.get(i).get("line_id"));
				list.get(i).put("line_id", line_id);
				list.get(i).put("city_id", cityId);
				list.get(i).put("city_name", cityMap.get(cityId));
				String device_model_id = StringUtil.getStringValue(list.get(i).get("device_model_id"));
				list.get(i).put("device_id", device_model_id);
				list.get(i).put("device_model", modelMap.get(device_model_id));
				list.get(i).put("loid", StringUtil.getStringValue(list.get(i).get("loid")));
				list.get(i).put("device_serialnumber", StringUtil.getStringValue(list.get(i)
						.get("device_serialnumber")));
				list.get(i).put("device_type",StringUtil.getStringValue(list.get(i).get("device_type")));
				String status = StringUtil.getStringValue(list.get(i).get("status"));
				list.get(i).put("status", status);
				list.get(i).put("voip_phone",
						StringUtil.getStringValue(list.get(i).get("voip_phone")));
				String enabled = StringUtil.getStringValue(list.get(i).get("enabled"));
				String message2 = "";
				if (StringUtil.IsEmpty(enabled))
				{
					if ("Enabled".equals(enabled))
					{
						message2 = "启用";
					}
					else if ("Disabled".equals(enabled))
					{
						message2 = "未启用";
					}
				}
				list.get(i).put("enabled", "端口[" + line_id + "]:" + message2);
				String reason_id = StringUtil.getStringValue(list.get(i).get("reason"));
				list.get(i).put("reason",reasonMap.get(reason_id));
			}
		}
		return list;
	}
	
	
}
