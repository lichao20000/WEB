
package com.linkage.module.ids.dao;

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
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2014-2-18
 * @category com.linkage.module.ids.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class IdsAlarmInfoDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(IdsAlarmInfoDAO.class);
	private Map<String, String> cityMap = new HashMap<String, String>();
	private Map<String, String> alarmTypeMap = new HashMap<String, String>();

	public IdsAlarmInfoDAO()
	{
		alarmTypeMap.put("1", "当前告警");
		alarmTypeMap.put("2", "历史告警");
		alarmTypeMap.put("3", "预解除告警");
	}

	/**
	 * 预检预修告警信息查询
	 * 
	 * @param start_time
	 *            开始时间
	 * @param end_time
	 *            结束时间
	 * @param alarm_type
	 *            告警类别
	 * @param alarm_code
	 *            告警代码
	 * @param alarm_count
	 *            告警次数
	 * @param is_send_sheet
	 *            是否派单
	 * @param is_pre_release
	 *            告警预解除
	 * @param is_release
	 *            告警解除
	 * @param device_id
	 *            设备序列号
	 * @param loid
	 *            LOID
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getIdsarmInfoList(String start_time, String end_time,
			String alarm_type, String alarm_code, String alarm_count,
			String is_send_sheet, String is_pre_release, String is_release,
			String device_id, String loid,String city_id, int curPage_splitPage, int num_splitPage)
	{
		logger.info("IdsAlarmInfoDAO=>getIdsarmInfoList()");
		StringBuffer sql = new StringBuffer();
		List<Map> list = new ArrayList<Map>();
		//TODO wait
		sql.append("select max(area_name) area_name,max(city_id) city_id,max(loid) loid,"
					+ "max(device_serialnumber) device_serialnumber,max(alarm_type) alarm_type,"
					+ "max(alarm_name) alarm_name,max(alarm_code) alarm_code,max(alarm_content) alarm_content,"
					+ "max(first_up_time) first_up_time,max(last_up_time) last_up_time,"
					+ "max(alarm_count) alarm_count,max(is_pre_release) is_pre_release,"
					+ "max(is_release) is_release,max(release_time) release_time,"
					+ "max(duration_time) duration_time,max(is_send_sheet) is_send_sheet,"
					+ "max(send_sheet_obj) send_sheet_obj,max(olt_name1) olt_name1,max(olt_ip1) olt_ip1,"
					+ "max(pon_id1) pon_id1,max(olt_name) olt_name,max(olt_ip) olt_ip,max(pon_intf) pon_intf "
					+ "from ( ");
		
		sql.append("select d.city_id,d.loid,d.device_serialnumber,d.alarm_type,d.alarm_name,"
				+ "d.alarm_code,d.alarm_content,d.first_up_time,d.last_up_time,d.alarm_count,"
				+ "d.is_pre_release,d.is_release,d.release_time,d.duration_time,d.is_send_sheet,"
				+ "d.send_sheet_obj,d.olt_ip,d.pon_intf,d.onu_id,"
				+ "c.olt_name olt_name1,c.olt_ip olt_ip1,c.pon_id pon_id1,c.onu_id onu_id1,"
				+ "d.olt_name,c.area_name from ( ");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select a.city_id,a.loid,a.device_serialnumber,a.alarm_type,a.alarm_name,"
						+ "a.alarm_code,a.alarm_content,a.first_up_time,a.last_up_time,a.alarm_count,"
						+ "a.is_pre_release,a.is_release,a.release_time,a.duration_time,a.is_send_sheet,"
						+ "a.send_sheet_obj,a.olt_ip,a.pon_intf,a.onu_id,b.device_serialnumber ");
		}else{
			sql.append("select a.*,b.device_serialnumber ");
		}
			
			sql.append(" from tab_ids_alarm  a left join tab_gw_device b on a.device_id=b.device_id ");
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" where a.first_up_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and a.last_up_time<=").append(end_time);
			}
			if (!StringUtil.IsEmpty(alarm_type) && !"-1".equals(alarm_type))
			{
				sql.append(" and a.alarm_type=").append(alarm_type);
			}
			if (!StringUtil.IsEmpty(alarm_code))
			{
				sql.append(" and a.alarm_code='").append(alarm_code.trim()).append("'");
			}
			if (!StringUtil.IsEmpty(alarm_count))
			{
				sql.append(" and a.alarm_count>=").append(alarm_count.trim());
			}
			if (!StringUtil.IsEmpty(is_send_sheet) && !"-1".equals(is_send_sheet))
			{
				sql.append(" and a.is_send_sheet=").append(is_send_sheet);
			}
			if (!StringUtil.IsEmpty(is_pre_release) && !"-1".equals(is_pre_release))
			{
				sql.append(" and a.is_pre_release=").append(is_pre_release);
			}
			if (!StringUtil.IsEmpty(is_release) && !"-1".equals(is_release))
			{
				sql.append(" and a.is_release=").append(is_release);
			}
			if (!StringUtil.IsEmpty(device_id))
			{
				sql.append(" and b.device_serialnumber='").append(device_id.trim())
						.append("' ");
			}
			if (!StringUtil.IsEmpty(loid))
			{
				sql.append(" and a.loid='").append(loid.trim()).append("'");
			}
			if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
					&& !"-1".equals(city_id))
			{
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
				sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
						.append(")");
				cityIdList = null;
			}
			sql.append(" )");
			//连接第三张表
			sql.append(" d left join t_pon_info c on d.loid=c.loid ");
			sql.append(" ) t ");
			sql.append("group by city_id,loid,device_serialnumber,alarm_type,alarm_name,alarm_code,"
					+ "alarm_content,first_up_time,last_up_time,alarm_count,is_pre_release,"
					+ "is_release,release_time,duration_time,is_send_sheet,send_sheet_obj,"
					+ "olt_name1,olt_ip1,pon_id1,olt_name,olt_ip,pon_intf ");
			PrepareSQL psql = new PrepareSQL(sql.toString());
			cityMap = Global.G_CityId_CityName_Map;
			list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
					num_splitPage, new RowMapper()
					{

						@Override
						public Object mapRow(ResultSet rs, int arg1) throws SQLException
						{
							Map<String, String> map = new HashMap<String, String>();
							String cityid = rs.getString("city_id");
							if(null == cityid)
							{
								cityid = "";
								map.put("city_name",
										StringUtil.getStringValue(rs.getString("area_name")));
							}
							else
							{
								map.put("city_name",
										StringUtil.getStringValue(cityMap.get(cityid)));
							}
							map.put("cityid", cityid);
							
							logger.warn("CityId :"+cityid);
							map.put("loid", rs.getString("loid"));
							map.put("device_serialnumber",
									rs.getString("device_serialnumber"));
							map.put("alarm_type", StringUtil.getStringValue(alarmTypeMap
									.get(rs.getString("alarm_type"))));
							map.put("alarm_name", rs.getString("alarm_name"));
							map.put("alarm_code", rs.getString("alarm_code"));
							map.put("alarm_content", rs.getString("alarm_content"));
							map.put("first_up_time",
									getFormatDate(rs.getString("first_up_time")));
							map.put("last_up_time",
									getFormatDate(rs.getString("last_up_time")));
							map.put("alarm_count", rs.getString("alarm_count"));
							map.put("is_pre_release", "1".equals(rs.getString("is_pre_release"))?"是":"否");
							map.put("is_release", "1".equals(rs.getString("is_release"))?"是":"否");
							map.put("release_time",
									getFormatDate(rs.getString("release_time")));
							long time = StringUtil.getLongValue(rs.getString("duration_time"));
							map.put("duration_time", DateUtil.formatTime(time));
							map.put("is_send_sheet", "1".equals(rs.getString("is_send_sheet"))?"是":"否");
							map.put("send_sheet_obj", rs.getString("send_sheet_obj"));
							//如果是500001-500006 那么PONID之类的用C表中的
							if(!"500007".equals(rs.getString("alarm_code")))
							{
								map.put("olt_name", rs.getString("olt_name1"));
								map.put("olt_ip", rs.getString("olt_ip1"));
								map.put("pon_intf", rs.getString("pon_id1"));
								
							}
							else
							{
								
								map.put("olt_name", rs.getString("olt_name"));
								map.put("olt_ip", rs.getString("olt_ip"));
								map.put("pon_intf", rs.getString("pon_intf"));
							}
							
							return map;
						}
					});
		
		return list;
	}

	/**
	 * 分页统计最大数
	 * 
	 * @param start_time
	 *            开始时间
	 * @param end_time
	 *            结束时间
	 * @param alarm_type
	 *            告警类别
	 * @param alarm_code
	 *            告警代码
	 * @param alarm_count
	 *            告警次数
	 * @param is_send_sheet
	 *            是否派单
	 * @param is_pre_release
	 *            告警预解除
	 * @param is_release
	 *            告警解除
	 * @param device_id
	 *            设备序列号
	 * @param loid
	 *            LOID
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countIdsarmInfoList(String start_time, String end_time, String alarm_type,
			String alarm_code, String alarm_count, String is_send_sheet,
			String is_pre_release, String is_release, String device_id, String loid,String city_id,
			int curPage_splitPage, int num_splitPage)
	{
		logger.info("IdsAlarmInfoDAO=>getIdsarmInfoList()");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		
		sql.append(" from tab_ids_alarm a left join tab_gw_device b on a.device_id=b.device_id ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" where a.first_up_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.last_up_time<=").append(end_time);
		}
		if (!StringUtil.IsEmpty(alarm_type) && !"-1".equals(alarm_type))
		{
			sql.append(" and a.alarm_type=").append(alarm_type);
		}
		if (!StringUtil.IsEmpty(alarm_code))
		{
			sql.append(" and a.alarm_code='").append(alarm_code.trim()).append("'");
		}
		if (!StringUtil.IsEmpty(alarm_count))
		{
			sql.append(" and a.alarm_count>=").append(alarm_count.trim());
		}
		if (!StringUtil.IsEmpty(is_send_sheet) && !"-1".equals(is_send_sheet))
		{
			sql.append(" and a.is_send_sheet=").append(is_send_sheet);
		}
		if (!StringUtil.IsEmpty(is_pre_release) && !"-1".equals(is_pre_release))
		{
			sql.append(" and a.is_pre_release=").append(is_pre_release);
		}
		if (!StringUtil.IsEmpty(is_release) && !"-1".equals(is_release))
		{
			sql.append(" and a.is_release=").append(is_release);
		}
		if (!StringUtil.IsEmpty(device_id))
		{
			sql.append(" and b.device_serialnumber='").append(device_id.trim())
					.append("' ");
		}
		if (!StringUtil.IsEmpty(loid))
		{
			sql.append(" and a.loid='").append(loid.trim()).append("'");
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
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

	public List<Map> getIdsarmInfoListExcel(String start_time, String end_time,
			String alarm_type, String alarm_code, String alarm_count,
			String is_send_sheet, String is_pre_release, String is_release,
			String device_id, String loid,String city_id)
	{
		logger.info("IdsAlarmInfoDAO=>getIdsarmInfoListExcel()");
		StringBuffer sql = new StringBuffer();
//		sql.append("select a.city_id,c.username,b.device_serialnumber,a.alarm_type,a.alarm_name,a.alarm_code,a.alarm_content,a.first_up_time,a.last_up_time,a.alarm_count,a.is_pre_release,a.is_release,a.release_time,a.duration_time,a.is_send_sheet,a.send_sheet_obj,a.olt_name,a.olt_ip,a.pon_intf,a.onu_id ");
		
		//TODO wait
		sql.append("select max(city_id) city_id,max(loid) loid,max(device_serialnumber) device_serialnumber,"
					+ "max(alarm_type) alarm_type,max (alarm_name) alarm_name,max(alarm_code) alarm_code,"
					+ "max(alarm_content) alarm_content,max(first_up_time) first_up_time,"
					+ "max(last_up_time) last_up_time,max(alarm_count) alarm_count,"
					+ "max(is_pre_release) is_pre_release,max(is_release) is_release,"
					+ "max(release_time) release_time,max(duration_time) duration_time,"
					+ "max(is_send_sheet) is_send_sheet,max(send_sheet_obj) send_sheet_obj,max(olt_name1) olt_name1,"
					+ "max(olt_ip1)olt_ip1,max(pon_id1) pon_id1,max(olt_name)olt_name,max(olt_ip)olt_ip,"
					+ "max(pon_intf)pon_intf  from ( ");
		
		sql.append("select d.city_id,d.loid,d.device_serialnumber,d.alarm_type,d.alarm_name,d.alarm_code,"
					+ "d.alarm_content,d.first_up_time,d.last_up_time,d.alarm_count,d.is_pre_release,"
					+ "d.is_release,d.release_time,d.duration_time,d.is_send_sheet,d.send_sheet_obj,"
					+ "d.olt_name,d.olt_ip,d.pon_intf,d.onu_id,"
					+ "c.olt_name olt_name1,c.olt_ip olt_ip1,c.pon_id pon_id1,c.onu_id onu_id1 "
					+ "from ( ");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select a.city_id,a.loid,a.device_serialnumber,a.alarm_type,a.alarm_name,a.alarm_code,"
						+ "a.alarm_content,a.first_up_time,a.last_up_time,a.alarm_count,a.is_pre_release,"
						+ "a.is_release,a.release_time,a.duration_time,a.is_send_sheet,a.send_sheet_obj,"
						+ "a.olt_name,a.olt_ip,a.pon_intf,a.onu_id,b.device_serialnumber ");
		}else{
			sql.append("select a.*, b.device_serialnumber ");
		}
		
		
		sql.append(" from tab_ids_alarm a left join tab_gw_device b on a.device_id=b.device_id ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" where a.first_up_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and a.last_up_time<=").append(end_time); 
		}
		if (!StringUtil.IsEmpty(alarm_type) && !"-1".equals(alarm_type))
		{
			sql.append(" and a.alarm_type=").append(alarm_type);
		}
		if (!StringUtil.IsEmpty(alarm_code))
		{
			sql.append(" and a.alarm_code='").append(alarm_code.trim()).append("'");
		}
		if (!StringUtil.IsEmpty(alarm_count))
		{
			sql.append(" and a.alarm_count>=").append(alarm_count.trim());
		}
		if (!StringUtil.IsEmpty(is_send_sheet) && !"-1".equals(is_send_sheet))
		{
			sql.append(" and a.is_send_sheet=").append(is_send_sheet);
		}
		if (!StringUtil.IsEmpty(is_pre_release) && !"-1".equals(is_pre_release))
		{
			sql.append(" and a.is_pre_release=").append(is_pre_release);
		}
		if (!StringUtil.IsEmpty(is_release) && !"-1".equals(is_release))
		{
			sql.append(" and a.is_release=").append(is_release);
		}
		if (!StringUtil.IsEmpty(device_id))
		{
			sql.append(" and b.device_serialnumber='").append(device_id.trim())
					.append("' ");
		}
		if (!StringUtil.IsEmpty(loid))
		{
			sql.append(" and a.loid='").append(loid.trim()).append("'");
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" )");
		//连接第三张表
		sql.append(" d left join t_pon_info c on d.loid = c.loid ");
		
		sql.append(" ) t group by  ");
		sql.append(" city_id,loid,device_serialnumber,alarm_type,alarm_name,alarm_code,alarm_content,first_up_time,last_up_time,alarm_count,is_pre_release,is_release,release_time,duration_time,is_send_sheet,send_sheet_obj,"
				+ " olt_name1, olt_ip1, pon_id1,"
				+ "olt_name,olt_ip,pon_intf  ");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String cityid = StringUtil.getStringValue(list.get(i).get("city_id"));
				list.get(i).put("cityid", cityid);
				list.get(i).put("city_name",
						StringUtil.getStringValue(cityMap.get(cityid)));
				list.get(i).put("loid",
						StringUtil.getStringValue(list.get(i).get("loid")));
				list.get(i)
						.put("device_serialnumber",
								StringUtil.getStringValue(list.get(i).get(
										"device_serialnumber")));
				list.get(i).put(
						"alarm_type",
						StringUtil.getStringValue(alarmTypeMap.get(StringUtil
								.getStringValue(list.get(i).get("alarm_type")))));
				list.get(i).put("alarm_name",
						StringUtil.getStringValue(list.get(i).get("alarm_name")));
				list.get(i).put("alarm_code",
						StringUtil.getStringValue(list.get(i).get("alarm_code")));
				list.get(i).put("alarm_content", list.get(i).get("alarm_content"));
				list.get(i).put(
						"first_up_time",
						getFormatDate(StringUtil.getStringValue(list.get(i).get(
								"first_up_time"))));
				list.get(i).put(
						"last_up_time",
						getFormatDate(StringUtil.getStringValue(list.get(i).get(
								"last_up_time"))));
				list.get(i).put("alarm_count", list.get(i).get("alarm_count"));
				list.get(i).put("is_pre_release", "1".equals(StringUtil.getStringValue(list.get(i).get("is_pre_release")))?"是":"否");
				list.get(i).put("is_release", "1".equals(StringUtil.getStringValue(list.get(i).get("is_release")))?"是":"否");
				list.get(i).put(
						"release_time",
						getFormatDate(StringUtil.getStringValue(list.get(i).get(
								"release_time"))));
				long time = StringUtil.getLongValue(list.get(i).get("duration_time"));
				list.get(i).put("duration_time", time > 0 ? DateUtil.formatTime(time) : "");
				list.get(i).put("is_send_sheet", "1".equals(StringUtil.getStringValue(list.get(i).get("is_send_sheet")))?"是":"否");
				list.get(i).put("send_sheet_obj", list.get(i).get("send_sheet_obj"));
				if(!"500007".equals(StringUtil.getStringValue(list.get(i).get("alarm_code"))))
				{
					list.get(i).put("olt_name", list.get(i).get("olt_name1"));
					list.get(i).put("olt_ip", list.get(i).get("olt_ip1"));
					list.get(i).put("pon_intf", list.get(i).get("pon_id1"));
				}
				else
				{
					list.get(i).put("olt_name", list.get(i).get("olt_name"));
					list.get(i).put("olt_ip", list.get(i).get("olt_ip"));
					list.get(i).put("pon_intf",list.get(i).get("pon_intf"));
				}
			}
		}
		return list;
	}

	private String getFormatDate(String first_up_time)
	{
		if(StringUtil.IsEmpty(first_up_time)){
			return "";
		}else{
			return StringUtil.formatDate("yyyy-MM-dd HH:mm",
					StringUtil.getLongValue(first_up_time));
		}
	}
}
