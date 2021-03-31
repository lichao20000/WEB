
package com.linkage.module.itms.resource.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;
/**
 * CQDX-REQ-ITMS-20190225-YJ-001(安全端口关闭统计)-修改(1)
 * 涉及：端口关闭参数采集表tab_result_telnetftp  设备表 tab_gw_device  用户表tab_hgwcustomer 
 */
public class SafePortCloseCountDAO extends SuperDAO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(SafePortCloseCountDAO.class);


	/** 查询节点的名称 */
	private String port21 = "InternetGatewayDevice.DeviceInfo.X_CT-COM_ServiceManage.FtpEnable";//21端口
	private String port23 = "InternetGatewayDevice.DeviceInfo.X_CT-COM_ServiceManage.TelnetEnable";//23端口
	String parampath1 = port21+","+port23;
	String parampath2 = port23+","+port21;
	/** 节点value */
	String paramvalue = "0,0";
	/**
	 * 重庆21和23端口关闭
	 * 统计方法
	 */
	public List<Map> portCloseListByCity() {

		List<Map> list = new ArrayList<Map>();
		Map<String,String> resultMap = null;

		long totalNum = 0;//总数
		
		//总计
		long allTotalNum =0;
		long allSuccNum21 =0;
		long allSuccNum23 =0;
		long allFailNum =0;

		List<Map<String,String>> totalList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> successList21 = new ArrayList<Map<String,String>>();
		List<Map<String,String>> successList23 = new ArrayList<Map<String,String>>();
		List<Map<String,String>> failList = new ArrayList<Map<String,String>>();
		
		//每个城市总配置数
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(1) as num,b.city_id from tab_result_telnetftp a,tab_gw_device b,tab_hgwcustomer c where a.device_id = b.device_id and a.device_id = c.device_id group by b.city_id");
		totalList = jt.queryForList(psql.getSQL());
		
		//每个城市21端口关闭成功数
		PrepareSQL psql21 = new PrepareSQL();
		psql21.append("select count(1) as num,b.city_id from tab_result_telnetftp a,tab_gw_device b,tab_hgwcustomer c where a.device_id = b.device_id and (a.status = 1 or a.status = 0 ) and a.FtpEnable='0' and a.device_id = c.device_id group by b.city_id");
		successList21 = jt.queryForList(psql21.getSQL());

		//每个城市23端口关闭成功数
		PrepareSQL psql23 = new PrepareSQL();
		psql23.append("select count(1) as num,b.city_id from tab_result_telnetftp a,tab_gw_device b,tab_hgwcustomer c where a.device_id = b.device_id and (a.status = 1 or a.status = 0 ) and a.TelnetEnable='0' and a.device_id = c.device_id group by b.city_id");
		successList23 = jt.queryForList(psql23.getSQL());
		
		
		//每个城市采集失败数
		PrepareSQL psql2 = new PrepareSQL();
		psql2.append("select count(1) as num,b.city_id from tab_result_telnetftp a,tab_gw_device b,tab_hgwcustomer c where a.device_id = b.device_id and a.status != 1 and a.status != 0 and a.device_id = c.device_id group by b.city_id");
		failList = jt.queryForList(psql2.getSQL());

		logger.warn("totalListSQL:"+totalList.toString());
		logger.warn("successList21SQL:"+successList21.toString());
		logger.warn("successList23SQL:"+successList23.toString());
		logger.warn("failList:"+failList.toString());

		for (Map<String, String> map : totalList) {
			long succNum21 = 0;//21端口关闭成功数
			long succNum23 = 0;//23端口关闭成功数
			long failNum = 0;//失败数

			resultMap = new HashMap();
			totalNum = StringUtil.getLongValue(map.get("num"));
			if(map != null){
				String cityId = map.get("city_id");
				for (Map<String, String> map2 : successList21) {
					if(cityId.equals(map2.get("city_id"))) {
						succNum21 = StringUtil.getLongValue(map2.get("num"));
						break;
					}
				}
				for (Map<String, String> map2 : successList23) {
					if(cityId.equals(map2.get("city_id"))) {
						succNum23 = StringUtil.getLongValue(map2.get("num"));
						break;
					}
				}
				for (Map<String, String> map3 : failList) {
					if(cityId.equals(map3.get("city_id"))) {
						failNum = StringUtil.getLongValue(map3.get("num"));
						break;
					}
				}
				
				resultMap.put("city_id", cityId);
				resultMap.put("city_name", Global.G_CityId_CityName_Map.get(cityId));
				resultMap.put("succNum21", succNum21+"");
				resultMap.put("succNum23", succNum23+"");
				resultMap.put("totalNum", totalNum+"");
				resultMap.put("failNum", failNum+"");
				
				list.add(resultMap);

				//总数
				allTotalNum = allTotalNum +totalNum;
				allSuccNum21 = allSuccNum21 + succNum21;
				allSuccNum23 = allSuccNum23 + succNum23;
				allFailNum = allFailNum + failNum;
			}
		}
		//添加总数
		resultMap = new HashMap();
		resultMap.put("all", "小计");
		resultMap.put("allSuccNum21", allSuccNum21 +"");
		resultMap.put("allSuccNum23", allSuccNum23 +"");
		resultMap.put("allTotalNum", allTotalNum +"");
		resultMap.put("allFailNum", allFailNum +"");
		list.add(resultMap);
		resultMap = null;
		return list;
	}
	
	/** 统计列表导出
	 * 为了在导出文件里面有小计那一行，所以重新写个方法返回
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> reportCountExcel() {
		List<Map> list = new ArrayList<Map>();
		List<Map> tempList = this.portCloseListByCity();
		Map tempMap = null;
		for (Map map : tempList) {
			tempMap = new HashMap();
			if(map.get("all") == null) {
				tempMap.put("city_name", map.get("city_name"));
				tempMap.put("succNum21", map.get("succNum21"));
				tempMap.put("succNum23", map.get("succNum23"));
				tempMap.put("totalNum", map.get("totalNum"));
				tempMap.put("failNum", map.get("failNum"));
			}else {
				tempMap.put("city_name", "小计");
				tempMap.put("succNum21", map.get("allSuccNum21"));
				tempMap.put("succNum23", map.get("allSuccNum23"));
				tempMap.put("totalNum", map.get("allTotalNum"));
				tempMap.put("failNum", map.get("allFailNum"));
			}
			list.add(tempMap);
		}
		return list;
	}

	/** 根据条件查找 */
	public List<Map> queryDevList(String city_id, String type,
			int curPage_splitPage, int num_splitPage)
	{
		List<Map> list = new ArrayList<Map>();
		List<Map> descList = new ArrayList<Map>();
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select c.vendor_id,c.device_model_id,c.devicetype_id,c.city_id,c.device_serialnumber,b.username,a.gathertime from tab_result_telnetftp a,tab_hgwcustomer b,tab_gw_device c where a.device_id = b.device_id and a.device_id = c.device_id ");
		if(!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)){
			psql.append(" and c.city_id='"+city_id+"' ");
		}
		if("succ21".equals(type)){
			psql.append(" and a.FtpEnable = 0 and (a.status = 1 or a.status = 0) ");
		}
		else if("succ23".equals(type)){
			psql.append(" and a.TelnetEnable = 0 and (a.status = 1 or a.status = 0) ");
		}
		else if("fail".equals(type)){
			psql.append(" and a.status != 1 and a.status != 0 ");
		}
		psql.append(" order by a.gathertime desc");

		//不传分页信息查询全部
		if(curPage_splitPage==0 && num_splitPage==0){
			list = jt.queryForList(psql.getSQL());
		}
		else{
			list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
					+ 1, num_splitPage);
		}

		for(int i=0;i<list.size();i++){
			Map<String,String> map  = list.get(i);
			String vendor_id = map.get("vendor_id");
			//厂商
			if(null!=DeviceTypeUtil.vendorMap && !StringUtil.IsEmpty(vendor_id)){
				String vendor = DeviceTypeUtil.vendorMap.get(vendor_id);
				map.put("vendor_id", vendor);
			}

			//型号
			String device_model_id = map.get("device_model_id");
			if(null!=DeviceTypeUtil.deviceModelMap && !StringUtil.IsEmpty(device_model_id)){
				String model = DeviceTypeUtil.deviceModelMap.get(device_model_id);
				map.put("device_model_id", model);
			}

			//版本
			String devicetype_id = StringUtil.getStringValue(map.get("devicetype_id"));
			if(null!=DeviceTypeUtil.softVersionMap && !StringUtil.IsEmpty(devicetype_id)){
				String version = DeviceTypeUtil.softVersionMap.get(devicetype_id);
				map.put("version", version);
			}
			
			//属地
			if(null != Global.G_CityId_CityName_Map){
				map.put("city_name", Global.G_CityId_CityName_Map.get(map.get("city_id")));
			}

			
			//执行时间
			long gathertime = StringUtil.getLongValue(map.get("gathertime"));
			if(gathertime!=0){
				map.put("gathertime", new DateTimeUtil(gathertime*1000).getLongDate());
			}
			else{
				map.put("gathertime", "");
			}
		}
		return list;
	}

}
