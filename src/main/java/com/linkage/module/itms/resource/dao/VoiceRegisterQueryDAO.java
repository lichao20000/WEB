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
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-11-12
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class VoiceRegisterQueryDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory
			.getLogger(VoiceRegisterQueryDAO.class);
	private Map<String,String> reasonMap = new HashMap<String, String>();
	private Map<String, String> modelMap;
	public VoiceRegisterQueryDAO()
	{
		reasonMap.put("0", "成功");
		reasonMap.put("1", "IAD模块错误");
		reasonMap.put("2", "访问路由不通");
		reasonMap.put("3", "访问服务器无响应");
		reasonMap.put("4", "帐号、密码错误");
		reasonMap.put("5", "未知错误");
	}

	public List<Map> VoiceRegisterQueryInfo(String device_sn,String modelId,String loid, String device_type,
			String enabled, String voip_phone, String status,String reason, String start_time,String end_time,int curPage_splitPage, int num_splitPage){
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select  a.device_serialnumber,a.loid,a.enabled,a.status,a.reason,a.line_id,b.device_model_id,b.device_type,d.voip_phone from ");
		sql.append("  tab_voicestatus_info a,tab_gw_device b, tab_hgwcustomer c ,tab_voip_serv_param d ");
		sql.append("  where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber  and a.loid=c.username and  c.user_id=d.user_id ");
		if(!StringUtil.IsEmpty(device_sn)){
			sql.append(" and a.device_serialnumber='").append(device_sn).append("' ");
		}
		if(!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId)){
			sql.append(" and b.device_model_id='").append(modelId).append("' ");
		}
		if(!StringUtil.IsEmpty(loid)){
			sql.append(" and a.loid='").append(loid).append("' ");
		}
		if(!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type)){
			sql.append(" and b.device_type='").append(device_type).append("' ");
		}
		if(!StringUtil.IsEmpty(enabled) && !"-1".equals(enabled)){
			sql.append(" and a.enabled='").append(enabled).append("' ");
		}
		if(!StringUtil.IsEmpty(voip_phone)){
			sql.append(" and d.voip_phone='").append(voip_phone).append("' ");
		}
		if(!StringUtil.IsEmpty(status) && !"-1".equals(status)){
			sql.append(" and a.status='").append(status).append("' ");
		}
		if(!StringUtil.IsEmpty(reason) && !"-1".equals(reason)){
			sql.append(" and a.reason='").append(reason).append("' ");
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
		modelMap = this.getDeviceModel();
		List<Map> list = new ArrayList<Map>();
		list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					@Override
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("line_id", StringUtil.getStringValue(rs.getString("line_id")));
						map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
						map.put("device_serialnumber", StringUtil.getStringValue(rs
								.getString("device_serialnumber")));
						String device_model_id = StringUtil.getStringValue(rs.getString("device_model_id"));
						map.put("device_id", device_model_id);
						map.put("device_model", modelMap.get(device_model_id));
						map.put("status", StringUtil.getStringValue(rs.getString("status")));
						map.put("device_type", StringUtil.getStringValue(rs.getString("device_type")));
						map.put("voip_phone",StringUtil.getStringValue(rs.getString("voip_phone")));
						String enabled = StringUtil.getStringValue(rs.getString("enabled"));
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
						map.put("enabled", message2);
						map.put("reason",reasonMap.get(StringUtil.getStringValue(rs.getString("reason"))));
						return map;
					}
				});
		return list;
	}
	
	public int countVoiceRegisterQueryInfo(String device_sn,String modelId,String loid, String device_type,
			String enabled, String voip_phone, String status,String reason, String start_time,String end_time,int curPage_splitPage, int num_splitPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from ");
		sql.append("  tab_voicestatus_info a,tab_gw_device b, tab_hgwcustomer c ,tab_voip_serv_param d ");
		sql.append("  where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber  and a.loid=c.username and  c.user_id=d.user_id ");
		if(!StringUtil.IsEmpty(device_sn)){
			sql.append(" and a.device_serialnumber='").append(device_sn).append("' ");
		}
		if(!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId)){
			sql.append(" and b.device_model_id='").append(modelId).append("' ");
		}
		if(!StringUtil.IsEmpty(loid)){
			sql.append(" and a.loid='").append(loid).append("' ");
		}
		if(!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type)){
			sql.append(" and b.device_type='").append(device_type).append("' ");
		}
		if(!StringUtil.IsEmpty(enabled) && !"-1".equals(enabled)){
			sql.append(" and a.enabled='").append(enabled).append("' ");
		}
		if(!StringUtil.IsEmpty(voip_phone)){
			sql.append(" and d.voip_phone='").append(voip_phone).append("' ");
		}
		if(!StringUtil.IsEmpty(status) && !"-1".equals(status)){
			sql.append(" and a.status='").append(status).append("' ");
		}
		if(!StringUtil.IsEmpty(reason) && !"-1".equals(reason)){
			sql.append(" and a.reason='").append(reason).append("' ");
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
	private Map<String, String> getDeviceModel()
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
}
