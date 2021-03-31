package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowCallbackHandler;
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
 * @since 2013-10-31
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class VoiceRegisterFailQueryDAO extends SuperDAO
{
	
	private static Logger logger = LoggerFactory
			.getLogger(VoiceRegisterFailQueryDAO.class);
	private Map<String, String> cityMap;
	private Map<String, String> modelMap;
	private Map<String,String> reasonMap = new HashMap<String, String>();

	private static final String CENTER_CITY = "00";
	// 无效的city_id
	private static final String INVALID_CITY = "-1";

	public VoiceRegisterFailQueryDAO(){
		reasonMap.put("0", "成功");
		reasonMap.put("1", "IAD模块错误");
		reasonMap.put("2", "访问路由不通");
		reasonMap.put("3", "访问服务器无响应");
		reasonMap.put("4", "帐号、密码错误");
		reasonMap.put("5", "未知错误");
	}
	/**
	 * 语音线路一失败统计
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String,String> voiceRegisterFailOne(String city_id,String start_time,String end_time){
		logger.debug("voiceRegisterFailOne()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,count(1) num from (select distinct oui,device_serialnumber from tab_voicestatus_info  where line_id=1 and reason!='0' ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(" ) a,tab_gw_device b  where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber ");
		if (!StringUtil.IsEmpty(city_id) && !INVALID_CITY.equals(city_id) && !CENTER_CITY.equals(city_id))
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
	 * 语音线路二失败统计
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String,String> voiceRegisterFailTwo(String city_id,String start_time,String end_time){
		logger.debug("voiceRegisterFailOne()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,count(1) num from (select distinct oui,device_serialnumber from tab_voicestatus_info  where line_id=2 and reason!='0' ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(" ) a,tab_gw_device b  where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber ");
		if (!StringUtil.IsEmpty(city_id) && !INVALID_CITY.equals(city_id) && !CENTER_CITY.equals(city_id))
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
	
	
	public Map<String,String> voiceRegisterFailOneAndTwo(String city_id,String start_time,String end_time){
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,count(1) num from (select m.oui,m.device_serialnumber,m.line_id ,n.line_id line_id2 from ");
		sql.append(" (select distinct oui,device_serialnumber,line_id from tab_voicestatus_info where line_id=1  and reason!='0' ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(" ) m,");
		sql.append(" (select distinct oui,device_serialnumber,line_id from tab_voicestatus_info where line_id=2 and reason!='0' ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(" ) n");
		sql.append(" where m.oui=n.oui and m.device_serialnumber=n.device_serialnumber ) a,");
		sql.append(" tab_gw_device b where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and  a.line_id=1 and a.line_id2=2 ");
		if (!StringUtil.IsEmpty(city_id) && !INVALID_CITY.equals(city_id) && !CENTER_CITY.equals(city_id))
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
	
	
	
	public List<Map> voiceRegisterDeviceQueryInfo(String city_id,String line_id, String start_time,
			String end_time, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("voiceDeviceQueryInfo()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,a.loid,a.device_serialnumber,a.reason,a.line_id,a.enabled,b.device_type,a.status,d.voip_phone,a.add_time");
		sql.append(" from tab_voicestatus_info a,tab_gw_device b, tab_hgwcustomer c ,tab_voip_serv_param d ");
		sql.append(" where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.loid=c.username and  c.user_id=d.user_id and a.reason!='0'  ");
		if(!StringUtil.IsEmpty(line_id)){
			sql.append(" and a.line_id=").append(line_id);
		}
		if (!StringUtil.IsEmpty(city_id) && !INVALID_CITY.equals(city_id) && !CENTER_CITY.equals(city_id))
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
						map.put("reason",
								reasonMap.get(StringUtil.getStringValue(rs.getString("reason"))));
						map.put("add_time", DateUtil.transTime(rs.getLong("add_time"), "yyyy-MM-dd HH:mm:ss"));
						return map;
					}
				});
		return list;
	}

	public int countVoiceRegisterDeviceQueryInfo(String city_id,String line_id, String start_time,
			String end_time, int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1)  from ");
		sql.append(" tab_voicestatus_info a,tab_gw_device b,tab_hgwcustomer  c ,tab_voip_serv_param d ");
		sql.append(" where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.loid=c.username and c.user_id=d.user_id and a.reason!='0' ");
		if(!StringUtil.IsEmpty(line_id)){
			sql.append(" and a.line_id=").append(line_id);
		}
		if (!StringUtil.IsEmpty(city_id) && !INVALID_CITY.equals(city_id) && !CENTER_CITY.equals(city_id))
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
	private Map<String, String> getDeviceModel()
	{
		if (modelMap == null)
		{
			modelMap = new HashMap<String, String>();
			PrepareSQL psql = new PrepareSQL(
					"select device_model_id,device_model from gw_device_model");
			jt.query(psql.getSQL(), new RowCallbackHandler()
			{

				@Override
				public void processRow(ResultSet rs) throws SQLException
				{
					modelMap.put(rs.getString("device_model_id"), rs.getString("device_model"));
				}});
		}
		return modelMap;
	}
	public List<Map> voiceRegisterDeviceQueryExcel(String city_id,String line_id, String start_time,String end_time){
		logger.debug("voiceDeviceQueryInfo()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.city_id,a.loid,a.device_serialnumber,a.reason,a.line_id,a.enabled,b.device_type,b.device_model_id,a.status,d.voip_phone,a.add_time");
		sql.append(" from tab_voicestatus_info a,tab_gw_device b, tab_hgwcustomer c ,tab_voip_serv_param d ");
		sql.append(" where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.loid=c.username and  c.user_id=d.user_id ");
		if(!StringUtil.IsEmpty(line_id)){
			sql.append(" and a.line_id=").append(line_id);
		}
		if (!StringUtil.IsEmpty(city_id) && !INVALID_CITY.equals(city_id) && !CENTER_CITY.equals(city_id))
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
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if(null!=list && list.size()>0){
			for (int i = 0; i < list.size(); i++)
			{
				String cityId = StringUtil.getStringValue(list.get(i).get("city_id"));
				String lineId = StringUtil.getStringValue(list.get(i).get("line_id"));
				list.get(i).put("line_id", lineId);
				list.get(i).put("city_id", cityId);
				list.get(i).put("city_name", cityMap.get(cityId));
				String device_model_id = StringUtil.getStringValue(list.get(i).get("device_model_id"));
				list.get(i).put("device_id", device_model_id);
//				list.get(i).put("device_model", getDeviceModel().get(device_model_id));
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
				list.get(i).put("enabled", "端口[" + lineId + "]:" + message2);
				String reason_id = StringUtil.getStringValue(list.get(i).get("reason"));
				list.get(i).put("reason",reasonMap.get(reason_id));
				list.get(i).put("add_time", DateUtil.transTime(StringUtil.getLongValue(list.get(i).get("add_time")), "yyyy-MM-dd HH:mm:ss"));
			}
		}
		return list;
	}
	
}
