package com.linkage.module.itms.resource.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author Reno (Ailk NO.)
 * @version 1.0
 * @since 2015年1月28日
 * @category dao.resource
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class QueryDeviceByVoipIpDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(QueryDeviceByVoipIpDAO.class);
	
	private Map<String, String> cityMap = null;
	/**
	 * 清空临时表
	 */
	public void truncateTempTable(){
		String sql = "truncate table tmp_ipaddress";
		PrepareSQL psql = new PrepareSQL(sql);
		jt.execute(psql.getSQL());
	}
	
	/**
	 * 批量将IP列表插入到临时表tmp_ipaddress
	 * @param list
	 */
	public void insertTempTable(List<String> list){
		if(list != null &&!list.isEmpty()){
			String[] sqlArray = new String[list.size()];
			for(int i = 0;i<list.size();i++){
				String sql = "insert into tmp_ipaddress (ipaddress) values('"+list.get(i)+"')";
				sqlArray[i] = sql;
			}
			jt.batchUpdate(sqlArray);
		}
	}
	
/**
 * 分页查询家庭网关语音信息
 * @param curPage_splitPage 页码
 * @param num_splitPage 每页展示多少条
 * @return
 */
	public List<Map> queryPaged(int curPage_splitPage, int num_splitPage){
		String sql = "select a.ipaddress, c.username, c.city_id,d.device_serialnumber, d.vendor_id, d.device_model_id, e.softwareversion "+
		"from tmp_ipaddress a "+
		"left join hgwcust_serv_info b on a.ipaddress = b.ipaddress "+
		"left join tab_hgwcustomer c on b.user_id = c.user_id "+
		"left join tab_gw_device d on c.device_id = d.device_id "+
		"left join tab_devicetype_info e on d.devicetype_id = e.devicetype_id "+
		"where b.serv_type_id = 14 ";
		PrepareSQL psql = new PrepareSQL(sql);
		return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, num_splitPage);
	}
	
	public Integer queryCount(){
		String sql = "select COUNT(1) "+
		"from tmp_ipaddress a "+
		"left join hgwcust_serv_info b on a.ipaddress = b.ipaddress "+
		"left join tab_hgwcustomer c on b.user_id = c.user_id "+
		"left join tab_gw_device d on c.device_id = d.device_id "+
		"left join tab_devicetype_info e on d.devicetype_id = e.devicetype_id "+
		"where b.serv_type_id = 14 ";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForInt(psql.getSQL());
	}
	
	/**
	 * 分页查询家庭网关语音信息，并获得厂商、设备型号等信息，用于页面展示。
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map<String,Object>> dealQueryPaged(int curPage_splitPage, int num_splitPage){
		List<Map> queryPaged = this.queryPaged(curPage_splitPage, num_splitPage);
		return this.convert(queryPaged);
	}
	
	/**
	 * 家庭网关语音信息查询，用于生成excel，查询所有，不分页
	 * @return
	 */
	public List<Map> query(){
			String sql = "select a.ipaddress, c.username, c.city_id,d.device_serialnumber, d.vendor_id, d.device_model_id, e.softwareversion "+
			"from tmp_ipaddress a "+
			"left join hgwcust_serv_info b on a.ipaddress = b.ipaddress "+
			"left join tab_hgwcustomer c on b.user_id = c.user_id "+
			"left join tab_gw_device d on c.device_id = d.device_id "+
			"left join tab_devicetype_info e on d.devicetype_id = e.devicetype_id "+
			"where b.serv_type_id = 14 ";
			PrepareSQL psql = new PrepareSQL(sql);
			return jt.queryForList(psql.getSQL());
		}
	
	public List<Map<String,Object>> dealQuery(){
		List<Map> list = this.query();
		return this.convert(list);
	}
	
	public List<Map<String,Object>> convert(List<Map> queryList){
		logger.warn("分页查询的结果：", queryList);
		cityMap = CityDAO.getCityIdCityNameMap();
		if(queryList!=null && !queryList.isEmpty()){
			List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
			for(Map row:queryList){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ipaddress", row.get("ipaddress"));
				map.put("username", row.get("username"));
				String cityId = (String)row.get("city_id");
				String cityName = cityId==null?null:cityMap.get(cityId);
				map.put("city_name", cityName);
				map.put("device_serialnumber", (String)row.get("device_serialnumber"));
				String vendor_id= (String)row.get("vendor_id"); 
				// 以下获取厂商
				String vendor_name=null;
				if(vendor_id !=null){
					String sql = "select vendor_name from tab_vendor where vendor_id = '"+vendor_id+"'";
					List<Map> vendorNameList = jt.queryForList(sql);
					if(vendorNameList!= null && !vendorNameList.isEmpty()){
						Map vObject = vendorNameList.get(0);
						if(vObject != null){
							vendor_name = (String) vObject.get("vendor_name");
						}
					}
				}
				map.put("vendor_name", vendor_name);
				// 以下获取终端型号
				String device_model_id = (String)row.get("device_model_id");
				String device_model = null;
				if(device_model_id!=null){
					String sql = "select device_model from gw_device_model where device_model_id = '"+device_model_id+"'";
					List<Map> deviceModelList = jt.queryForList(sql);
					if(deviceModelList!= null && !deviceModelList.isEmpty()){
						Map dObject = deviceModelList.get(0);
						if(dObject != null){
							device_model = (String)dObject.get("device_model");
						}
					}
				}
				map.put("device_model", device_model);
				map.put("softwareversion", row.get("softwareversion"));
				list.add(map);
			}
			logger.warn("准备显示到页面上的数据：list:{}", list);
			return list;
		}
		return null;
	}
	
	
}
