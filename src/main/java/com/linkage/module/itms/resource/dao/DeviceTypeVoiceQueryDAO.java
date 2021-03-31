
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-29
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DeviceTypeVoiceQueryDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory
			.getLogger(DeviceTypeVoiceQueryDAO.class);
	private Map<String, String> cityMap;
	private Map<String, String> modelMap;
	private Map<String, String> reasonMap = new HashMap<String, String>();

	public DeviceTypeVoiceQueryDAO()
	{
		reasonMap.put("0", "成功");
		reasonMap.put("1", "IAD模块错误");
		reasonMap.put("2", "访问路由不通");
		reasonMap.put("3", "访问服务器无响应");
		reasonMap.put("4", "帐号、密码错误");
		reasonMap.put("5", "未知错误");
	}

	/**
	 * @category getVendor 获取所有的厂商
	 * @return
	 */
	public Map<String, String> getVendor()
	{
		logger.debug("getVendor()");
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL pSQL = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from tab_vendor");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSQL.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String tempValue = StringUtil.getStringValue(list.get(i)
						.get("vendor_add"))
						+ "("
						+ StringUtil.getStringValue(list.get(i).get("vendor_name")) + ")";
				map.put(StringUtil.getStringValue(list.get(i).get("vendor_id")),
						tempValue);
			}
		}
		return map;
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

	/**
	 * 设备型号信息
	 * 
	 * @return
	 */
	public List<String> getModel(String vendorId, String modelId)
	{
		logger.warn("DeviceTypeVoiceQueryDAO=>getModel()");
		List<String> tempList = new ArrayList<String>();
		PrepareSQL pSQL = new PrepareSQL(
				" select a.device_model_id from gw_device_model a where 1=1 ");
		pSQL.append(" and a.vendor_id='");
		pSQL.append(vendorId);
		pSQL.append("' ");
		if (!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId))
		{
			pSQL.append(" and a.device_model_id='");
			pSQL.append(modelId);
			pSQL.append("'");
		}
		List<Map> modellist = jt.queryForList(pSQL.getSQL());
		if (null != modellist && modellist.size() > 0)
		{
			for (int i = 0; i < modellist.size(); i++)
			{
				String device_model_id = StringUtil.getStringValue(modellist.get(i).get(
						"device_model_id"));
				tempList.add(device_model_id);
			}
		}
		return tempList;
	}

	/**
	 * 语音端口1设备型号统计
	 * 
	 * @param edtionIdBuffer
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String, String> getDeviceOneInfo(String edtionIdBuffer, String start_time,
			String end_time)
	{
		logger.debug("DeviceTypeVoiceQueryDAO=>getDeviceTypeVoiceQueryList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.device_model_id, count(1) num from ");
		sql.append(" (select distinct loid,oui,device_serialnumber  from tab_voicestatus_info where line_id=1 ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(" ) a , tab_gw_device b  where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and  ");
		sql.append(" b.device_model_id in ").append(edtionIdBuffer);
		sql.append(" group by b.device_model_id");
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("device_model_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	/**
	 * 语音端口1未启动的设备型号统计
	 * 
	 * @param edtionIdBuffer
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String, String> getDeviceOneNoInfo(String edtionIdBuffer,
			String start_time, String end_time)
	{
		logger.debug("DeviceTypeVoiceQueryDAO=>getDeviceTypeVoiceQueryList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.device_model_id, count(1) num from ");
		sql.append(" (select distinct loid,oui,device_serialnumber  from tab_voicestatus_info where line_id=1  and status='Disabled' ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(") a , tab_gw_device b where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and ");
		sql.append(" b.device_model_id in ").append(edtionIdBuffer);
		sql.append(" group by b.device_model_id");
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("device_model_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	/**
	 * 语音端口2设备型号统计
	 * 
	 * @param edtionIdBuffer
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String, String> getDeviceTwoInfo(String edtionIdBuffer, String start_time,
			String end_time)
	{
		logger.debug("DeviceTypeVoiceQueryDAO=>getDeviceTypeVoiceQueryList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.device_model_id, count(1) num from  ");
		sql.append(" (select distinct loid,oui,device_serialnumber  from tab_voicestatus_info where line_id=2 ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(" ) a , tab_gw_device b where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and ");
		sql.append(" b.device_model_id in ").append(edtionIdBuffer);
		sql.append(" group by b.device_model_id");
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("device_model_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	/**
	 * 语音端口2未启动的设备型号统计
	 * 
	 * @param edtionIdBuffer
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String, String> getDeviceTwoNoInfo(String edtionIdBuffer,
			String start_time, String end_time)
	{
		logger.debug("DeviceTypeVoiceQueryDAO=>getDeviceTypeVoiceQueryList()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.device_model_id, count(1) num from ");
		sql.append(" (select distinct loid,oui,device_serialnumber  from tab_voicestatus_info where line_id=2  and status='Disabled' ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(" )  a, tab_gw_device b  where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and  ");
		sql.append(" b.device_model_id in ").append(edtionIdBuffer);
		sql.append(" group by b.device_model_id");
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("device_model_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	/**
	 * 语音端口1和语音端口2同时未使用总数
	 * 
	 * @param edtionIdBuffer
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Map<String, String> getDeviceOneTwoNoInfo(String edtionIdBuffer,
			String start_time, String end_time)
	{
		logger.debug("voiceOrderQueryInfo()");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.device_model_id,count(1) num from (select m.oui,m.device_serialnumber from ");
		sql.append(" (select distinct loid,oui,device_serialnumber  from tab_voicestatus_info where  line_id=1 and status='Disabled'  ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(") m, ");
		sql.append(" (select distinct loid,oui,device_serialnumber  from tab_voicestatus_info where  line_id=2  and status='Disabled' ");
		if (!StringUtil.IsEmpty(start_time))
		{
			sql.append(" and upload_time>=").append(start_time);
		}
		if (!StringUtil.IsEmpty(end_time))
		{
			sql.append(" and upload_time<=").append(end_time);
		}
		sql.append(") n ");
		sql.append(" where m.loid=n.loid ) a, ");
		sql.append(" tab_gw_device b where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and  ");
		sql.append(" b.device_model_id in ").append(edtionIdBuffer);
		sql.append(" group by b.device_model_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("device_model_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	public List<Map> voiceDeviceQueryInfo(String edtionIdBuffer, String start_time,
			String end_time,String numInfo, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("voiceDeviceQueryInfo()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.loid,a.device_serialnumber,a.reason,a.line_id,a.enabled,b.device_type,b.device_model_id,a.status,d.voip_phone  from ");
		if("5".equals(numInfo)){
			sql.append("(select  m.loid,m.oui,m.device_serialnumber,m.reason,m.line_id,m.enabled,m.status from tab_voicestatus_info m, tab_voicestatus_info n   ");
			sql.append(" where m.loid=n.loid and m.line_id=1 and m.status='Disabled' and n.line_id=2 and n.status='Disabled' ");
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and m.upload_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and m.upload_time<=").append(end_time);
			}
			sql.append(" ) a, ");
		}else{
			sql.append(" tab_voicestatus_info a,  ");
		}
		
		sql.append("  tab_gw_device b, tab_hgwcustomer c ,tab_voip_serv_param d");
		sql.append(" where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.loid=c.username and  c.user_id=d.user_id and ");
		sql.append(" b.device_model_id in ").append(edtionIdBuffer.toString());
		int num = StringUtil.getIntegerValue(numInfo);
		switch (num)
		{
			case 1:
				sql.append(" and a.line_id=1 ");
				break;
			case 2:
				sql.append(" and a.line_id=1  and a.status='Disabled' ");
				break;
			case 3:
				sql.append(" and a.line_id=2  ");
				break;
			case 4:
				sql.append(" and a.line_id=2  and a.status='Disabled' ");
				break;
			case 5:
				break;
			default:
				break;
		}
		
		if (!"5".equals(numInfo))
		{
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and a.upload_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and a.upload_time<=").append(end_time);
			}
		}
		
		sql.append(" order by c.username,b.device_model_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		modelMap = getDeviceModel();
		List<Map> list = new ArrayList<Map>();
		list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					@Override
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String device_model_id = StringUtil.getStringValue(rs
								.getString("device_model_id"));
						map.put("line_id",
								StringUtil.getStringValue(rs.getString("line_id")));
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
						map.put("reason", reasonMap.get(StringUtil.getStringValue(rs
								.getString("reason"))));
						return map;
					}
				});
		return list;
	}

	public int countVoiceDeviceQueryInfo(String edtionIdBuffer, String start_time,
			String end_time, String numInfo,int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql.append("select count(*)  from ");
		}
		else {
			sql.append("select count(1)  from ");
		}
		if("5".equals(numInfo)){
			sql.append("(select  m.loid,m.oui,m.device_serialnumber,m.reason,m.line_id,m.enabled,m.status from tab_voicestatus_info m, tab_voicestatus_info n   ");
			sql.append(" where m.loid=n.loid and m.line_id=1 and m.status='Disabled' and n.line_id=2 and n.status='Disabled' ");
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and m.upload_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and m.upload_time<=").append(end_time);
			}
			sql.append(" ) a, ");
		}else{
			sql.append(" tab_voicestatus_info a,  ");
		}
		
		sql.append("  tab_gw_device b, tab_hgwcustomer c ,tab_voip_serv_param d");
		sql.append(" where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.loid=c.username and  c.user_id=d.user_id and ");
		sql.append(" b.device_model_id in ").append(edtionIdBuffer.toString());
		int num = StringUtil.getIntegerValue(numInfo);
		switch (num)
		{
			case 1:
				sql.append(" and a.line_id=1 ");
				break;
			case 2:
				sql.append(" and a.line_id=1  and a.status='Disabled' ");
				break;
			case 3:
				sql.append(" and a.line_id=2  ");
				break;
			case 4:
				sql.append(" and a.line_id=2  and a.status='Disabled' ");
				break;
			case 5:
				break;
			default:
				break;
		}
		
		if(!"5".equals(numInfo)){
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and a.upload_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and a.upload_time<=").append(end_time);
			}
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

	public List<Map> voiceDeviceQueryExcel(String edtionIdBuffer, String start_time,
			String end_time, String numInfo)
	{
		logger.debug("voiceDeviceQueryInfo()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.loid,a.device_serialnumber,a.reason,a.line_id,a.enabled,b.device_type,b.device_model_id,a.status,d.voip_phone  from ");
		if("5".equals(numInfo)){
			sql.append("(select distinct m.loid,m.oui,m.device_serialnumber,m.reason,m.line_id,m.enabled,m.status  from tab_voicestatus_info m, tab_voicestatus_info n ");
			sql.append(" where m.loid=n.loid and m.line_id=1 and m.status='Disabled' and n.line_id=2 and n.status='Disabled' ");
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and m.upload_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and m.upload_time<=").append(end_time);
			}
			sql.append(" ) a, ");
		}else{
			sql.append(" tab_voicestatus_info  a,");
		}
		
		sql.append("  tab_gw_device b, tab_hgwcustomer c ,tab_voip_serv_param d where ");
		sql.append(" a.oui=b.oui and a.device_serialnumber=b.device_serialnumber and a.loid=c.username and  c.user_id=d.user_id and ");
		sql.append(" b.device_model_id in ").append(edtionIdBuffer.toString());
		int num = StringUtil.getIntegerValue(numInfo);
		switch (num)
		{
			case 1:
				sql.append(" and a.line_id=1 ");
				break;
			case 2:
				sql.append(" and a.line_id=1  and a.status='Disabled' ");
				break;
			case 3:
				sql.append(" and a.line_id=2  ");
				break;
			case 4:
				sql.append(" and a.line_id=2  and a.status='Disabled' ");
				break;
			case 5:
				break;
			default:
				break;
		}
		if(!"5".equals(numInfo)){
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and a.upload_time>=").append(start_time);
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and a.upload_time<=").append(end_time);
			}
		}
		sql.append("  order by c.username,b.device_model_id");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		modelMap = getDeviceModel();
		List<Map> list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String device_model_id = StringUtil.getStringValue(list.get(i).get(
						"device_model_id"));
				String line_id = StringUtil.getStringValue(list.get(i).get("line_id"));
				list.get(i).put("line_id", line_id);
				list.get(i).put("device_id", device_model_id);
				list.get(i).put("device_model", modelMap.get(device_model_id));
				list.get(i).put("loid",
						StringUtil.getStringValue(list.get(i).get("loid")));
				list.get(i)
						.put("device_serialnumber",
								StringUtil.getStringValue(list.get(i).get(
										"device_serialnumber")));
				list.get(i).put("device_type",
						StringUtil.getStringValue(list.get(i).get("device_type")));
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
				list.get(i).put("enabled", "端口[" + line_id + "]:" + message2);
				list.get(i).put(
						"reason",
						reasonMap.get(StringUtil
								.getStringValue(list.get(i).get("reason"))));
			}
		}
		return list;
	}
}
