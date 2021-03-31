package com.linkage.module.ids.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.elasticsearch.bio.ElasticsearchInitBIO;
import com.linkage.commons.elasticsearch.util.ElasticDataUtil;
import com.linkage.commons.elasticsearch.util.MySearchOption;
import com.linkage.commons.elasticsearch.util.MySearchOption.OperType;
import com.linkage.commons.elasticsearch.util.MySearchOption.SearchLogic;
import com.linkage.commons.elasticsearch.util.MySearchOption.SearchType;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;
		
@SuppressWarnings({ "unchecked", "rawtypes" })
public class QueryUserInfoDAO extends SuperDAO
{
	private static Logger logger=LoggerFactory.getLogger(AlarmQueryDAO.class);
	private ElasticDataUtil edu  = null;
	
	/**
	 * 
	 * 查设备信息
	 * @param device_serialnumber
	 * @return
	 */
	public List<HashMap<String,String>> getDeviceInfo(String device_serialnumber)
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==3)
		{
			sql.append("select device_id,oui,device_serialnumber,loopback_ip,");
			sql.append("cpe_mac,vendor_id,device_model_id,devicetype_id ");
			sql.append("from tab_gw_device where device_serialnumber= ?");
			
			sql.setString(1, device_serialnumber);
			List<HashMap<String,String>> list=DBOperation.getRecords(sql.getSQL());
			List<HashMap<String,String>> l=new ArrayList<HashMap<String,String>>();
			if(list!=null && list.isEmpty())
			{
				Map<String,String> vn=getVendorName();
				Map<String,String> dm=getDeviceModel();
				Map<String,List<String>> dti=getDeviceTypeInfo();
				
				for(HashMap<String,String> map:list)
				{
					String vadd=vn.get(StringUtil.getStringValue(map,"vendor_id"));
					String model=dm.get(StringUtil.getStringValue(map,"device_model_id"));
					List<String> ver=dti.get(StringUtil.getStringValue(map,"devicetype_id"));
					if(StringUtil.IsEmpty(vadd) || StringUtil.IsEmpty(model) || ver==null || ver.isEmpty()){
						continue;
					}
					
					map.put("vendor_add",vadd);
					map.put("device_model",model);
					map.put("hardwareversion",ver.get(0));
					map.put("softwareversion",ver.get(1));
					map.put("ip_model_type",ver.get(2));
					
					vadd=null;
					model=null;
					ver=null;
				}
				
				vn=null;
				dm=null;
				dti=null;
			}
			
			return l;
		}
		else
		{
			sql.append("select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip,");
			sql.append(" b.vendor_add,c.device_model,d.hardwareversion,d.ip_model_type ,");
			sql.append(" d.softwareversion,a.cpe_mac from tab_gw_device a,tab_vendor b ");
			sql.append(" ,gw_device_model c, tab_devicetype_info d where a.device_serialnumber= ?");
			sql.append(" and a.vendor_id=b.vendor_id and a.device_model_id=");
			sql.append(" c.device_model_id and a.devicetype_id=d.devicetype_id");
			
			sql.setString(1, device_serialnumber);
			return DBOperation.getRecords(sql.getSQL());
		}
	}

	/**
	 * 根据设备序列号查询用户
	 * 
	 * @param device_serialnumber
	 * @return
	 */
	public List<HashMap<String,String>> getDeviceHUserInfo(String device_serialnumber)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.user_id,a.username,a.bandwidth,a.linkman,a.linkaddress,a.credno,a.linkphone,a.device_port,a.macaddress,b.username as kdname");
		psql.append(" from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id and b.serv_type_id=10 ");
		psql.append(" where a.user_state='1' and a.device_serialnumber=? ");
		psql.setString(1, device_serialnumber);
		return DBOperation.getRecords(psql.getSQL());
	}

	/**
	 *       查设备信息
			* @param loid
			* @return
	 */
	public List<HashMap<String,String>> getDeviceInfoByLoid(String loid)
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==3)
		{
			sql.append("select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip,");
			sql.append("a.vendor_id,a.device_model_id,a.devicetype_id,a.cpe_mac ");
			sql.append("from tab_hgwcustomer e,tab_gw_device a ");
			sql.append("where e.username=? and e.device_serialnumber=a.device_serialnumber ");
			sql.setString(1,loid);
			
			List<HashMap<String,String>> list=DBOperation.getRecords(sql.getSQL());
			List<HashMap<String,String>> l=new ArrayList<HashMap<String,String>>();
			if(list!=null && list.isEmpty())
			{
				Map<String,String> vn=getVendorName();
				Map<String,String> dm=getDeviceModel();
				Map<String,List<String>> dti=getDeviceTypeInfo();
				
				for(HashMap<String,String> map:list)
				{
					String vadd=vn.get(StringUtil.getStringValue(map,"vendor_id"));
					String model=dm.get(StringUtil.getStringValue(map,"device_model_id"));
					List<String> ver=dti.get(StringUtil.getStringValue(map,"devicetype_id"));
					if(StringUtil.IsEmpty(vadd) || StringUtil.IsEmpty(model) || ver==null || ver.isEmpty()){
						continue;
					}
					
					map.put("vendor_add",vadd);
					map.put("device_model",model);
					map.put("hardwareversion",ver.get(0));
					map.put("softwareversion",ver.get(1));
					map.put("ip_model_type",ver.get(2));
					
					vadd=null;
					model=null;
					ver=null;
				}
				
				vn=null;
				dm=null;
				dti=null;
			}
			
			return l;
		}
		else
		{
			sql.append("select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip,");
			sql.append(" b.vendor_add,c.device_model,d.hardwareversion,d.ip_model_type ,");
			sql.append(" d.softwareversion,a.cpe_mac from tab_hgwcustomer e,tab_gw_device a,tab_vendor b ");
			sql.append(" ,gw_device_model c, tab_devicetype_info d where e.username= ?");
			sql.append(" and e.device_serialnumber=a.device_serialnumber and a.vendor_id=b.vendor_id and a.device_model_id=");
			sql.append(" c.device_model_id and a.devicetype_id=d.devicetype_id");
			sql.setString(1,loid);
			return DBOperation.getRecords(sql.toString());	
		}
	}
	
	/**
	 *      查用户信息
			* @param loid
			* @return
	 */
	public List<HashMap<String,String>> getDeviceHUserInfoByLoid(String loid)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.user_id,a.username,a.bandwidth,a.linkman,a.linkaddress,a.credno,a.linkphone,a.device_port,a.macaddress,b.username as kdname");
		psql.append(" from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id = b.user_id and b.serv_type_id = 10 ");
		psql.append(" where a.user_state='1' and a.username=? ");
		psql.setString(1, loid);
		return DBOperation.getRecords(psql.getSQL());
			
	}

	/**
	 * 根据loid或设备sn查询最近一次设备记录
	 */
	public Map<String, String> queryDevCurrentStatus(String indexName, 
			  String indexType,String paramType,String param)
	{
		 edu = ElasticsearchInitBIO.getInstance().getElasticDataMap().get(indexName);
	     ArrayList<MySearchOption> searchOptionList = new ArrayList<MySearchOption>();
	     MySearchOption searchOption= new MySearchOption(paramType, SearchType.term,SearchLogic.must, param);
	     searchOptionList.add(searchOption);
	     List<Map<String, Object>> list=edu.simpleSearch(indexType, searchOptionList, 0, 1, "upload_time", "desc");
	     Map<String, String> map = null;
	     if (null != list) {
				for (Map<String, Object> remap : list) {

					if (null != remap) {
						map = new HashMap<String, String>();
						map.put("status", StringUtil.getStringValue(remap,"status"));
						map.put("tx_power", StringUtil.getStringValue(remap,"tx_power"));
						map.put("rx_power", StringUtil.getStringValue(remap,"rx_power"));
						map.put("temperature", StringUtil.getStringValue(remap,"temperature"));
						map.put("vottage",this.transformDecimal(StringUtil.getDoubleValue(remap,"vottage") * 100 / 1000 / 1000));
						map.put("bais_current", this.transformDecimal(StringUtil.getDoubleValue(remap,"bais_current") * 2 / 1000));
						long upload_time = StringUtil.getLongValue(remap,"upload_time");
						DateTimeUtil dt = new DateTimeUtil(upload_time * 1000);
						map.put("upload_time", dt.getLongDate());
						long add_time = StringUtil.getLongValue(remap,"add_time");
						DateTimeUtil dateTimeUtil = new DateTimeUtil(
								add_time * 1000);
						map.put("add_time", dateTimeUtil.getLongDate());
					}
				}
			}
		return map;
	}
	/**
	 * 根据loid或设备sn查询最近一次宽带记录
	 */
	public Map<String, String> queryNetCurrentStatus(String indexName, 
			  String indexType,String paramType,String param)
	{
		 edu = ElasticsearchInitBIO.getInstance().getElasticDataMap().get(indexName);
	     ArrayList<MySearchOption> searchOptionList = new ArrayList<MySearchOption>();
	     MySearchOption searchOption= new MySearchOption(paramType, SearchType.term,SearchLogic.must, param);
	     searchOptionList.add(searchOption);
	     List<Map<String, Object>> list=edu.simpleSearch(indexType, searchOptionList, 0, 1, "upload_time", "desc");
	     Map<String, String> map = null;
	     if (null != list) {
				for (Map<String, Object> remap : list) {

					if (null != remap) {
						map = new HashMap<String, String>();
						map.put("netStatus", StringUtil.getStringValue(remap,"status"));
						map.put("reason",StringUtil.getStringValue(remap,"reason"));
					}
				}
			}
		return map;
	}
	/**
	 * 根据loid或设备sn查询最近一次语音记录
	 */
	public Map<String, String> queryVoipCurrentStatus(String indexName, 
			  String indexType,String paramType,String param)
	{
		 edu = ElasticsearchInitBIO.getInstance().getElasticDataMap().get(indexName);
	     ArrayList<MySearchOption> searchOptionList = new ArrayList<MySearchOption>();
	     MySearchOption searchOption= new MySearchOption(paramType, SearchType.term,SearchLogic.must, param);
	     MySearchOption searchOptionline1= new MySearchOption("line_id", SearchType.term,SearchLogic.must, 1);
	     MySearchOption searchOptionline2= new MySearchOption("line_id", SearchType.term,SearchLogic.must, 2);
	     
	     searchOptionList.add(searchOption);
	     searchOptionList.add(searchOptionline1);
	     List<Map<String, Object>> listLine1=edu.simpleSearch(indexType, searchOptionList, 0, 1, "upload_time", "desc");
	     Map<String, String> map = new HashMap<String, String>();
	     if (null != listLine1) {
				for (Map<String, Object> remap : listLine1) {
					if (null != remap) {
						String enabled = "";
						if ("Enabled".equals(StringUtil.getStringValue(remap,"enabled"))) {
							enabled = "启用";
						} else {
							enabled = "未启用";
						}
						map.put("enabledLine1", enabled);
						String reason = "";
						logger.warn("reason is:"+StringUtil.getStringValue(remap, "reason"));
						int reType = StringUtil.getIntValue(remap,"reason");
						switch (reType) {
						case 0:
							reason = "成功";
							break;
						case 1:
							reason = "IAD模块错误";
							break;
						case 2:
							reason = "访问路由不通";
							break;
						case 3:
							reason = "访问服务器无响应";
							break;
						case 4:
							reason = "帐号、密码错误";
							break;
						case 5:
							reason = "未知错误";
							break;
						default:
							break;
						}
						map.put("reasonLine1", reason);
						}
				}
			}
	     searchOptionList.remove(1);
	     searchOptionList.add(searchOptionline2);
	     List<Map<String, Object>> listLine2=edu.simpleSearch(indexType, searchOptionList, 0, 1, "upload_time", "desc");
	     if (null != listLine2) {
				for (Map<String, Object> remap : listLine2) {
					if (null != remap) {
						String enabled = "";
						if ("Enabled".equals(StringUtil.getStringValue(remap,"enabled"))) {
							enabled = "启用";
						} else {
							enabled = "未启用";
						}
						map.put("enabledLine2", enabled);
						String reason = "";
						int reType = StringUtil.getIntValue(remap,"reason");
						switch (reType) {
						case 0:
							reason = "成功";
							break;
						case 1:
							reason = "IAD模块错误";
							break;
						case 2:
							reason = "访问路由不通";
							break;
						case 3:
							reason = "访问服务器无响应";
							break;
						case 4:
							reason = "帐号、密码错误";
							break;
						case 5:
							reason = "未知错误";
							break;
						default:
							break;
						}
						map.put("reasonLine2", reason);
						}
				}
			}
		return map;
	}
	/**
	 * 查询Http拨测结果根据设备sn
	 */
	public Map<String,String> getHttpInfo(String sn)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select test_time,bom_time,eom_time,total_bytes_rece from ");
			psql.append("(select test_time,bom_time,eom_time,total_bytes_rece from tab_http_diag_result ");
			psql.append("where device_serialnumber = ? order by test_time desc) limit 1");
		}else{
			psql.append("select test_time,bom_time,eom_time,total_bytes_rece from ");
			psql.append("(select test_time,bom_time,eom_time,total_bytes_rece from tab_http_diag_result ");
			psql.append("where device_serialnumber = ? order by test_time desc) where rownum=1");
		}
		
		psql.setString(1, sn);
		return  DBOperation.getRecord(psql.getSQL());
	}
	/**
	 * 查询Http拨测结果根据loid
	 */
	public Map<String,String> getHttpInfoByLoid(String loid)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select test_time,bom_time,eom_time,total_bytes_rece from ");
			psql.append("(select b.test_time,b.bom_time,b.eom_time,b.total_bytes_rece ");
			psql.append("from tab_hgwcustomer a,tab_http_diag_result b ");
			psql.append("where a.username=? and a.device_serialnumber=b.device_serialnumber order by b.test_time desc) limit 1");
		}else{
			psql.append("select test_time,bom_time,eom_time,total_bytes_rece from ");
			psql.append("(select b.test_time,b.bom_time,b.eom_time,b.total_bytes_rece ");
			psql.append("from tab_hgwcustomer a,tab_http_diag_result b ");
			psql.append("where a.username=? and a.device_serialnumber=b.device_serialnumber order by b.test_time desc) where rownum=1");
		}
		
		psql.setString(1, loid);
		return  DBOperation.getRecord(psql.getSQL());
	}
	
	/**
	 * 根据sn能查到多少设备
	 */
	public List<HashMap<String,String>> getDevQuantityBySn(String sn)
	{
		
		PrepareSQL sql = new PrepareSQL();
		sql.append("select device_serialnumber from tab_gw_device where device_serialnumber like '%"+sn+"'");
		return  DBOperation.getRecords(sql.getSQL());
	}
	
	/**
	 *      查询loid存在不存在
			* @param loid
			* @return
	 */
	public Map<String,String> getUserByLoid(String loid)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select username from tab_hgwcustomer where username= ? ");
		psql.setString(1, loid);
		return DBOperation.getRecord(psql.getSQL());
	}
   
    /**
     *     查询图表数据
    		* @return
     */
	public List<Map<String,String>> getChartData(String indexName,String indexType,Long startTime, Long endTime,
			String paramType, String param)
	{
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap().get(indexName);

		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(startTime, endTime,
				paramType, param);
         Long count=StringUtil.getLongValue(edu.getCount(indexType, searchOptionList));

		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

		List<Map<String, Object>> list = edu.simpleSearch(indexType,
				searchOptionList, 0, count.intValue(), "upload_time", "asc");

		if (null != list) {
			Map<String, String> map = null;
			for (Map<String, Object> remap : list) {

				if (null != remap) {
					map = new HashMap<String, String>();
					map.put("tx_power", StringUtil.getStringValue(remap,"tx_power"));
					map.put("rx_power", StringUtil.getStringValue(remap,"rx_power"));
					map.put("temperature", StringUtil.getStringValue(remap,"temperature"));
					map.put("vottage", this.transformDecimal(StringUtil.getDoubleValue(remap,"vottage") * 100 / 1000 / 1000));
					map.put("bais_current", this.transformDecimal(StringUtil.getDoubleValue(remap,"bais_current") * 2 / 1000));
					long upload_time = StringUtil.getLongValue(remap,"upload_time");
					map.put("upload_time", upload_time * 1000+"");
				}
				returnList.add(map);
			}
		}

		return returnList;
	}
    /**
     *     查询pon口图表数据
    		* @return
     */
	public List<Map<String,String>> getPonData(String indexName,String indexType,Long startTime, Long endTime,
			String paramType, String param)
	{
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap().get(indexName);

		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(startTime, endTime,
				paramType, param);
		
		Long count=StringUtil.getLongValue(edu.getCount(indexType, searchOptionList));

		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

		List<Map<String, Object>> list = edu.simpleSearch(indexType,
				searchOptionList, 0, count.intValue(), "upload_time", "asc");

		if (null != list) {
			Map<String, String> map = null;
			for (Map<String, Object> remap : list) {

				if (null != remap) {
					map = new HashMap<String, String>();
					map.put("bytes_sent", StringUtil.getDoubleValue(remap, "bytes_sent")/1024/1024+"");
					map.put("bytes_received", StringUtil.getDoubleValue(remap, "bytes_received")/1024/1024+"");
					long upload_time = StringUtil.getLongValue(remap,"upload_time");
					map.put("upload_time", upload_time * 1000+"");
				}
				returnList.add(map);
			}
		}

		return returnList;
	}
	
	 /**
     *     查询lan口图表数据
    		* @return
     */
	public List<Map<String,String>> getLanData(String indexName,String indexType,Long startTime, Long endTime,
			String paramType, String param)
	{
		edu = ElasticsearchInitBIO.getInstance().getElasticDataMap().get(indexName);

		ArrayList<MySearchOption> searchOptionList = this.getQueryParam(startTime, endTime,
				paramType, param);
		
		Long count=StringUtil.getLongValue(edu.getCount(indexType, searchOptionList));

		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

		List<Map<String, Object>> list = edu.simpleSearch(indexType,
				searchOptionList, 0, count.intValue(), "upload_time", "asc");

		if (null != list) {
			Map<String, String> map = null;
			for (Map<String, Object> remap : list) {

				if (null != remap) {
					map = new HashMap<String, String>();
					map.put("lan_interface_config_id", StringUtil.getStringValue(remap,"lan_interface_config_id"));
					map.put("status", StringUtil.getStringValue(remap,"status"));
					map.put("bytes_sent", this.transformDecimal(StringUtil.getDoubleValue(remap, "bytes_sent")/1024/1024)+"");
					map.put("bytes_received", this.transformDecimal(StringUtil.getDoubleValue(remap, "bytes_received")/1024/1024)+"");
					long upload_time = StringUtil.getLongValue(remap,"upload_time");
					map.put("upload_time", upload_time * 1000+"");
				}
				returnList.add(map);
			}
		}
		return returnList;
	}
	
	/**
	 *       生成参数
			* @param startTime
			* @param endTime
			* @param paramType
			* @param param
			* @return
	 */
	public ArrayList<MySearchOption> getQueryParam(Long startTime, Long endTime,String paramType, String param)
	{
		ArrayList<MySearchOption> searchOptionList = new ArrayList<MySearchOption>();

		MySearchOption mySearchOption = new MySearchOption(
				paramType, SearchType.term, SearchLogic.must,param);

			MySearchOption mySearchOption2 = null;
			MySearchOption mySearchOption3 = null;

			mySearchOption2 = new MySearchOption("upload_time", SearchType.range,
					SearchLogic.must, OperType.gt,
					StringUtil.getStringValue(startTime));
			mySearchOption3 = new MySearchOption("upload_time", SearchType.range,
					SearchLogic.must, OperType.lt,
					StringUtil.getStringValue(endTime));
			searchOptionList.add(mySearchOption);
			searchOptionList.add(mySearchOption2);
			searchOptionList.add(mySearchOption3);
			
		return searchOptionList;
	}
	
	/**
	 * 查询一个设备有几个lan口根据设备sn
	 */
	public Map<String,String> getLanNumByDevSn(String sn)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select a.lan_num from tab_bss_dev_port a,");
			psql.append("(select b.spec_id from tab_devicetype_info b,tab_gw_device c ");
			psql.append("where c.device_id=? and b.devicetype_id=c.devicetype_id) t ");
			psql.append("where a.id=t.spec_id");
		}else{
			psql.append("select a.lan_num from tab_bss_dev_port a,tab_devicetype_info b,tab_gw_device c where c.device_id=? and b.devicetype_id=c.devicetype_id and  a.id=b.spec_id");
		}
		
		psql.setString(1, sn);
		return  DBOperation.getRecord(psql.getSQL());
	}
	
	/**
	 * 查询一个设备有几个lan口根据loid
	 */
	public Map<String,String> getLanNumByLoid(String loid)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select c.devicetype_id from tab_hgwcustomer d,tab_gw_device c ");
			psql.append("where d.username=? and d.device_id=c.device_id ");
			psql.setString(1, loid);
			Map<String,String> m=DBOperation.getRecord(psql.getSQL());
			if(m==null || m.isEmpty()){
				return null;
			}
			
			psql=new PrepareSQL();
			psql.append("select a.lan_num from tab_bss_dev_port a,tab_devicetype_info b ");
			psql.append("where b.devicetype_id="+StringUtil.getStringValue(m,"devicetype_id")+" and a.id=b.spec_id ");
		}else{
			psql.append("select a.lan_num from tab_hgwcustomer d,tab_bss_dev_port a,tab_devicetype_info b,tab_gw_device c ");
			psql.append("where d.username=? and d.device_id=c.device_id and b.devicetype_id=c.devicetype_id and a.id=b.spec_id ");
			psql.setString(1, loid);
		}
		
		return  DBOperation.getRecord(psql.getSQL());
	}
	
	/**
	 *       保留两位小数
			* @param number
			* @return
	 */
	private String transformDecimal(double number){
		return new DecimalFormat("0.00").format(new Double(number));
	}
	
	/**
	 * 获取所有厂商
	 */
	private Map<String,String> getVendorName()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select vendor_id,vendor_add from tab_vendor order by vendor_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				map.put(StringUtil.getStringValue(m,"vendor_id"),
						StringUtil.getStringValue(m,"vendor_add"));
			}
		}
		
		return map;
	}
	
	/**
	 * 获取所有型号
	 */
	private Map<String,String> getDeviceModel()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_model_id,device_model from gw_device_model order by device_model_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				map.put(StringUtil.getStringValue(m,"device_model_id"),
						StringUtil.getStringValue(m,"device_model"));
			}
		}
		
		return map;
	}
	
	/**
	 * 获取所有版本
	 */
	private Map<String,List<String>> getDeviceTypeInfo()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select devicetype_id,hardwareversion,softwareversion,ip_model_type ");
		psql.append("from tab_devicetype_info order by devicetype_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,List<String>> map=new HashMap<String,List<String>>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				List<String> l=new ArrayList<String>();
				l.add(StringUtil.getStringValue(m,"hardwareversion",""));
				l.add(StringUtil.getStringValue(m,"softwareversion",""));
				l.add(StringUtil.getStringValue(m,"ip_model_type",""));
				map.put(StringUtil.getStringValue(m,"devicetype_id"),l);
			}
		}
		
		return map;
	}
}

	