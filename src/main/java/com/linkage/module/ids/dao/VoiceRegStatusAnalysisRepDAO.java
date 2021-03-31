package com.linkage.module.ids.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

@SuppressWarnings("unchecked")
public class VoiceRegStatusAnalysisRepDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(VoiceRegStatusAnalysisRepDAO.class);
	private Map<String, String> cityMap;
	private Map<String,String> reasonMap = new HashMap<String, String>();
	
	public VoiceRegStatusAnalysisRepDAO(){
		reasonMap.put("0", "成功");
		reasonMap.put("1", "IAD模块错误");
		reasonMap.put("2", "访问路由不通");
		reasonMap.put("3", "访问服务器无响应");
		reasonMap.put("4", "帐号、密码错误");
		reasonMap.put("5", "未知错误");
	}
	
	/**
	 * 查询语音注册注册状态(按地域)
	 * @param city_id
	 * @param start_time
	 * @param end_time
	 * @param reportType
	 * @return
	 */
	public List<Map<String,String>> voiceRegQueryInfo(String start_time,String end_time,String reportType) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select city_id,line1_count,line1_disabled,line2_count,line2_disabled,line1_2_disabled ");
		psql.append("from tab_voiceport_city ");
		psql.append("where report_time>=? and report_time<=? ");
		psql.append("and report_type=? order by city_id ");
		
		psql.setLong(1, StringUtil.getLongValue(start_time));
		psql.setLong(2, StringUtil.getLongValue(end_time));
		psql.setInt(3, StringUtil.getIntegerValue(reportType));
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		List<Map<String,String>> reList = new ArrayList<Map<String,String>>();
		Map<String,String> tempMap = null;
		Map<String,String> newMap = null;
		cityMap = Global.G_CityId_CityName_Map;
		if(null!=list&&list.size()>0){
			for( int i = 0;i<list.size();i++){
				newMap = new HashMap<String,String>();
				tempMap = list.get(i);
				String cityId = tempMap.get("city_id");
				String cityName = cityMap.get(cityId);
				newMap.put("city_id", cityId);
				newMap.put("cityName", cityName);
				newMap.put("line1_count", StringUtil.getStringValue(tempMap.get("line1_count")));
				newMap.put("line1_disabled", StringUtil.getStringValue(tempMap.get("line1_disabled")));
				newMap.put("line2_count", StringUtil.getStringValue(tempMap.get("line2_count")));
				newMap.put("line2_disabled", StringUtil.getStringValue(tempMap.get("line2_disabled")));
				newMap.put("line1_2_disabled", StringUtil.getStringValue(tempMap.get("line1_2_disabled")));
				reList.add(newMap);
			}
		}
		return  reList;
	}
	
	/**
	 * 查询语音注册注册状态(按型号)
	 * @param start_time
	 * @param end_time
	 * @param reportType
	 * @return
	 */
	public List<Map<String,String>> voiceRegQueryInfoByModel(String start_time,
			String end_time, String reportType) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_model_id,line1_count,line1_disabled,line2_count,line2_disabled,line1_2_disabled ");
		psql.append("from tab_voiceport_model ");
		psql.append("where report_time>=? and report_time<=? ");
		psql.append("and report_type=? order by device_model_id");
		
		psql.setLong(1, StringUtil.getLongValue(start_time));
		psql.setLong(2, StringUtil.getLongValue(end_time));
		psql.setInt(3, StringUtil.getIntegerValue(reportType));
		
	    List<Map<String,String>> list =   jt.queryForList(psql.getSQL());
	    
	    Map<String,String> deviceModelMap = this.getDeviceModelMap();
	    
		List<Map<String,String>> reList = new ArrayList<Map<String,String>>();
		Map<String,String> tempMap = null;
		Map<String,String> newMap = null;
		if(null!=list&&list.size()>0){
			for( int i = 0;i<list.size();i++){
				newMap = new HashMap<String,String>();
				tempMap = list.get(i);
				String deviceModelId = tempMap.get("device_model_id");
				String deviceModel = deviceModelMap.get(deviceModelId);
				newMap.put("device_model_id", deviceModelId);
				newMap.put("deviceModel", deviceModel);
				newMap.put("line1_count", StringUtil.getStringValue(tempMap.get("line1_count")));
				newMap.put("line1_disabled", StringUtil.getStringValue(tempMap.get("line1_disabled")));
				newMap.put("line2_count", StringUtil.getStringValue(tempMap.get("line2_count")));
				newMap.put("line2_disabled", StringUtil.getStringValue(tempMap.get("line2_disabled")));
				newMap.put("line1_2_disabled", StringUtil.getStringValue(tempMap.get("line1_2_disabled")));
				reList.add(newMap);
			}
		}
		return  reList;
	}
	/**
	 * id与name对照
	 * @return
	 */
	private Map<String,String> getDeviceModelMap()
	{
		HashMap<String, String> deviceModelMap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL("select device_model_id,device_model from gw_device_model");
		List<HashMap<String,String>> deviceModelList = jt.queryForList(psql.getSQL());
		for (HashMap<String,String> map:deviceModelList)
		{
			deviceModelMap.put(StringUtil.getStringValue(map.get("device_model_id")),
								map.get("device_model"));
		}
		return deviceModelMap;
	}
	
	/**
	 * PPPOE失败原因统计
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param reportType
	 * @return
	 */
	public List<Map<String,String>> getPPPOEFailStatus(String startOpenDate1,
			String endOpenDate1, String reportType) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select city_id,time_out,auth_failure,disconnect ");
		psql.append("from tab_pppoe_failstatus ");
		psql.append("where report_time>=? and report_time<=? ");
		psql.append("and report_type=? order by city_id");
		
		psql.setLong(1, StringUtil.getLongValue(startOpenDate1));
		psql.setLong(2, StringUtil.getLongValue(endOpenDate1));
		psql.setInt(3, StringUtil.getIntegerValue(reportType));
		
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
			
		List<Map<String,String>> reList = new ArrayList<Map<String,String>>();
		Map<String,String> tempMap = null;
		Map<String,String> newMap = null;
		cityMap = Global.G_CityId_CityName_Map;
		if(null!=list&&list.size()>0){
			for( int i = 0;i<list.size();i++){
				newMap = new HashMap<String,String>();
				tempMap = list.get(i);
				String cityId = tempMap.get("city_id");
				String cityName = cityMap.get(cityId);
				newMap.put("city_id", cityId);
				newMap.put("cityName", cityName);
				newMap.put("time_out", StringUtil.getStringValue(tempMap.get("time_out")));
				newMap.put("auth_failure", StringUtil.getStringValue(tempMap.get("auth_failure")));
				newMap.put("disconnect", StringUtil.getStringValue(tempMap.get("disconnect")));
				reList.add(newMap);
			}
		}
		return  reList;
	}
	
	@SuppressWarnings("rawtypes")
	public String getFilePath() 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select outter_url,server_dir ");
		}else{
			psql.append("select * ");
		}
		psql.append("from tab_file_server where server_name ='报表文件' ");
		
		Map map =   queryForMap(psql.getSQL());
		String outUrl  = StringUtil.getStringValue(map.get("outter_url"));
		String serverDir = StringUtil.getStringValue(map.get("server_dir"));
		return outUrl+"/"+serverDir;
	}
	
	/**
	 * 语音注册失败原因按区域分组统计。
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param reportType
	 * @return
	 */
	public List<Map<String, String>> getVoicRegFailedReasonQuery(String startOpenDate1,
			String endOpenDate1,String reportType) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,lad_error,route_error,server_error,account_error,unknown_error ");
		sql.append("from tab_voicestatus_report ");
		sql.append("where report_time>=? and report_time<=? ");
		sql.append("and report_type=? order by city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setLong(1, StringUtil.getLongValue(startOpenDate1));
		psql.setLong(2, StringUtil.getLongValue(endOpenDate1));
		psql.setInt(3, StringUtil.getIntegerValue(reportType));
		
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		
		List<Map<String,String>> reList = new ArrayList<Map<String,String>>();
		Map<String,String> tempMap = null;
		Map<String,String> newMap = null;
		cityMap  = Global.G_CityId_CityName_Map;
		if(null!=list&&list.size()>0){
			for( int i = 0;i<list.size();i++){
				newMap = new HashMap<String,String>();
				tempMap = list.get(i);
				String cityId = tempMap.get("city_id");
				String cityName = cityMap.get(cityId);
				newMap.put("city_id", cityId);
				newMap.put("cityName", cityName);
				newMap.put("lad_error", StringUtil.getStringValue(tempMap.get("lad_error")));
				newMap.put("route_error", StringUtil.getStringValue(tempMap.get("route_error")));
				newMap.put("server_error", StringUtil.getStringValue(tempMap.get("server_error")));
				newMap.put("account_error", StringUtil.getStringValue(tempMap.get("account_error")));
				newMap.put("unknown_error", StringUtil.getStringValue(tempMap.get("unknown_error")));
				reList.add(newMap);
			}
		}
		return  reList;
	}
	
	/**
	 * 终端环境
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param reportType
	 * @return
	 */
	public List<Map<String, String>> getDeviceTempQuery(String startOpenDate1,String endOpenDate1,String reportType)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("select city_id,device_count ");
		sql.append("from tab_ponstatus_report ");
		sql.append("where report_time>=? and report_time<=? ");
		sql.append("and report_type=? order by city_id");
		
		sql.setLong(1, StringUtil.getLongValue(startOpenDate1));
		sql.setLong(2, StringUtil.getLongValue(endOpenDate1));
		sql.setInt(3, StringUtil.getIntegerValue(reportType));
		
		List<Map<String,String>> list = jt.queryForList(sql.getSQL());
		
		List<Map<String,String>> reList = new ArrayList<Map<String,String>>();
		Map<String,String> tempMap = null;
		Map<String,String> newMap = null;
		cityMap = Global.G_CityId_CityName_Map;
		if(null!=list&&list.size()>0){
			for( int i = 0;i<list.size();i++){
				newMap = new HashMap<String,String>();
				tempMap = list.get(i);
				String cityId = tempMap.get("city_id");
				String cityName = cityMap.get(cityId);
				newMap.put("city_id", cityId);
				newMap.put("cityName", cityName);
				newMap.put("device_count", StringUtil.getStringValue(tempMap.get("device_count")));
				reList.add(newMap);
			}
		}
		return  reList;
	}
}
