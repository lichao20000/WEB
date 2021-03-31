package com.linkage.module.gtms.stb.resource.dao;

import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.system.utils.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings({ "deprecation","rawtypes" })
public class StbSetParamDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(StbSetParamDAO.class);


	/**
	 * 获取设备信息
	 */
	public Map getDeviceInfo(String device_id)
	{
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select a.device_serialnumber,a.city_id,a.cpe_mac,a.device_model_id,");
		psql.append("a.complete_time,a.cpe_currentupdatetime,a.loopback_ip,");
		psql.append("b.vendor_add,c.device_model,d.serv_account,");
		psql.append("e.hardwareversion,e.softwareversion,e.epg_version,e.net_type,f.category,");
		psql.append("f.apk_version_name,f.network_type,f.addressing_type,f.public_ip,f.ip_type ");
		psql.append("from stb_tab_gw_device a ");
		psql.append("left join stb_tab_vendor b on a.vendor_id=b.vendor_id ");
		psql.append("left join stb_gw_device_model c on a.device_model_id=c.device_model_id ");
		psql.append("left join stb_tab_customer d on a.customer_id=d.customer_id ");
		psql.append("left join stb_tab_devicetype_info e on a.devicetype_id=e.devicetype_id ");
		psql.append("left join stb_dev_supplement f on a.device_id=f.device_id ");
		psql.append("where a.device_status=1 and a.device_id=? ");
		psql.setString(1, device_id);

		return jt.queryForMap(psql.getSQL());
	}

	/**
	 * 获取所有策略
	 */
	public List<Map> getStrategyResult(final String device_id,int curPage_splitPage,int num_splitPage)
	{
		if (!StringUtil.IsEmpty(device_id))
		{
			//查询设备信息
			final Map devMap=getDeviceInfo(device_id);

			//查询策略信息
			PrepareSQL psql = new PrepareSQL();
			psql.append("select id,service_id,sheet_para,time,start_time,end_time,result_id,status ");
			psql.append("from stb_gw_strategy_soft_log ");
			psql.append("where service_id in(5,8,9) and device_id=? ");
			psql.append("order by time desc ");

			psql.setString(1, device_id);

			return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,num_splitPage,
					new RowMapper()
					{
						public Object mapRow(ResultSet rs, int arg1) throws SQLException
						{
							Map<String, String> resMap = new HashMap<String, String>();
							resMap.put("device_id",device_id);
							resMap.put("device_serialnumber",StringUtil.getStringValue(devMap,"device_serialnumber",""));
							resMap.put("device_model", StringUtil.getStringValue(devMap,"device_model",""));
							resMap.put("vendor_name", StringUtil.getStringValue(devMap,"vendor_add",""));
							resMap.put("city_name", CityDAO.getCityName(StringUtil.getStringValue(devMap,"city_id","")));
							resMap.put("cpe_mac", StringUtil.getStringValue(devMap,"cpe_mac",""));
							resMap.put("serv_account", StringUtil.getStringValue(devMap,"serv_account",""));
							resMap.put("hardwareversion", StringUtil.getStringValue(devMap,"hardwareversion",""));
							resMap.put("softwareversion", StringUtil.getStringValue(devMap,"softwareversion",""));

							resMap.put("apk_version_name", StringUtil.getStringValue(devMap,"apk_version_name",""));
							resMap.put("epg_version", StringUtil.getStringValue(devMap,"epg_version",""));
							resMap.put("network_type", StringUtil.getStringValue(devMap,"network_type",""));
							resMap.put("addressing_type", StringUtil.getStringValue(devMap,"addressing_type",""));
							resMap.put("loopback_ip", StringUtil.getStringValue(devMap,"loopback_ip",""));
							resMap.put("public_ip", StringUtil.getStringValue(devMap,"public_ip",""));

							resMap.put("strategy_id",rs.getString("id"));
							if("9".equals(rs.getString("service_id"))){
								resMap.put("strategy_type","设备重启");
								resMap.put("strategy_info","设备重启");
							}else if("8".equals(rs.getString("service_id"))){
								resMap.put("strategy_type","恢复出厂设置");
								resMap.put("strategy_info","恢复出厂设置");
							}else{
								resMap.put("strategy_type","软件升级");
								resMap.put("strategy_info", getVersion_path(rs.getString("sheet_para")));
							}

							Integer result = StringUtil.getIntegerValue(rs.getString("result_id"));
							if (-1 == result) {
								if(0 == StringUtil.getIntegerValue(rs.getString("status"))){
									resMap.put("strategy_status", "策略已失效");
								}else{
									resMap.put("strategy_status", "策略未下发");
								}
							} else if (1 == result) {
								resMap.put("strategy_status", "策略已下发");
							} else if (2 == result) {
								resMap.put("strategy_status", "策略已执行成功");
							}

							String complete_time = StringUtil.getStringValue(devMap,"complete_time","");
							if (!StringUtil.IsEmpty(complete_time)) {
								DateTimeUtil dateTimeUtil = new DateTimeUtil(Long.parseLong(complete_time) * 1000);
								complete_time = dateTimeUtil.getLongDate();
								dateTimeUtil = null;
							}
							resMap.put("complete_time", complete_time);

							String cpe_currentupdatetime = StringUtil.getStringValue(devMap,"cpe_currentupdatetime","");
							if (!StringUtil.IsEmpty(cpe_currentupdatetime)) {
								DateTimeUtil dateTimeUtil = new DateTimeUtil(Long.parseLong(cpe_currentupdatetime) * 1000);
								cpe_currentupdatetime = dateTimeUtil.getLongDate();
								dateTimeUtil = null;
							}
							resMap.put("cpe_currentupdatetime", cpe_currentupdatetime);

							String time = rs.getString("time");
							if (!StringUtil.IsEmpty(time)) {
								DateTimeUtil dateTimeUtil = new DateTimeUtil(Long.parseLong(time) * 1000);
								time = dateTimeUtil.getLongDate();
								dateTimeUtil = null;
							}
							resMap.put("time", time);

							String startTime = rs.getString("start_time");
							if (!StringUtil.IsEmpty(startTime)) {
								DateTimeUtil dateTimeUtil = new DateTimeUtil(Long.parseLong(startTime) * 1000);
								startTime = dateTimeUtil.getLongDate();
								dateTimeUtil = null;
							}
							resMap.put("start_time", startTime);

							String endTime = rs.getString("end_time");
							if (!StringUtil.IsEmpty(endTime)) {
								DateTimeUtil dateTimeUtil = new DateTimeUtil(Long.parseLong(endTime) * 1000);
								endTime = dateTimeUtil.getLongDate();
								dateTimeUtil = null;
							}
							resMap.put("end_time", endTime);
							return resMap;
						}
					});
		}

		return null;
	}

	/**
	 * 获取最大页数
	 */
	public int getCountStrategyResult(String device_id,int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) ");
		psql.append("from stb_gw_strategy_soft_log ");
		psql.append("where service_id in(5,8,9) and device_id=? ");
		psql.setString(1, device_id);
		int total = jt.queryForInt(psql.getSQL());

		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 删除策略
	 */
	public int delete(String id)
	{
		PrepareSQL sql1 = new PrepareSQL();
		sql1.append("delete from stb_gw_strategy_soft_log where id=? ");
		sql1.setLong(1, StringUtil.getLongValue(id));

		PrepareSQL sql2 = new PrepareSQL();
		sql2.append("delete from stb_gw_strategy_soft where id=? ");
		sql2.setLong(1, StringUtil.getLongValue(id));

		int[] result=jt.batchUpdate(new String[]{sql1.getSQL(),sql2.getSQL()});

		logger.debug("[{}] delete,result:[{}]",Arrays.toString(result));
		if(result!=null && result.length>0){
			return 1;
		}
		return 0;
	}

	/**
	 * 新增策略，重启或恢复出厂设置
	 */
	public int insertStrategy(long accOId,String deviceId,long service_id)
	{
		PrepareSQL sql0 = new PrepareSQL();
		sql0.append("update stb_gw_strategy_soft_log set status=0 where device_id=? and service_id=? ");
		sql0.setString(1,deviceId);
		sql0.setLong(2,service_id);

		PrepareSQL sql1 = new PrepareSQL();

		sql1.append("delete from stb_gw_strategy_soft where device_id=? and service_id=? ");
		sql1.setString(1,deviceId);
		sql1.setLong(2,service_id);

		long sid=Math.round(Math.random() * 100000L);
		PrepareSQL sql2=new PrepareSQL();
		sql2.append("insert into stb_gw_strategy_soft (id,status,result_id,acc_oid,time,");
		sql2.append("type,service_id,redo,sheet_para,device_id) ");
		sql2.append("values (?,?,?,?,?,?,?,?,?,?) ");
		sql2.setLong(1,sid);
		sql2.setLong(2,1);
		sql2.setLong(3,-1);//默认未做
		sql2.setLong(4,accOId);
		sql2.setLong(5,System.currentTimeMillis()/1000L);
		sql2.setLong(6,4);
		sql2.setLong(7,service_id);
		sql2.setLong(8,0);
		sql2.setString(9,"noSheet");
		sql2.setString(10,deviceId);

		PrepareSQL sql3=new PrepareSQL();
		sql3.append("insert into stb_gw_strategy_soft_log (id,status,result_id,acc_oid,time,");
		sql3.append("type,service_id,redo,sheet_para,device_id) ");
		sql3.append("values (?,?,?,?,?,?,?,?,?,?) ");
		sql3.setLong(1,sid);
		sql3.setLong(2,1);
		sql3.setLong(3,-1);//默认未做
		sql3.setLong(4,accOId);
		sql3.setLong(5,System.currentTimeMillis()/1000L);
		sql3.setLong(6,4);
		sql3.setLong(7,service_id);
		sql3.setLong(8,0);
		sql3.setString(9,"noSheet");
		sql3.setString(10,deviceId);

		int[] result=jt.batchUpdate(new String[]{sql0.getSQL(),sql1.getSQL(),sql2.getSQL(),sql3.getSQL()});

		if(result!=null && result.length>0){
			return 1;
		}
		return 0;
	}


	private String getVersion_path(String sheet_para) {
		SAXReader reader = new SAXReader();
		Document document = null;
		String version_path=null;
		try
		{
			document = reader.read(new StringReader(sheet_para));
			Element root = document.getRootElement();
			version_path=root.elementText("version_path");
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		return version_path;
	}
}
