
package com.linkage.module.itms.report.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;
/**
 * 重庆21和23端口关闭dao
 * 涉及：批量下发任务表tab_batchconfig_task、下发结果表tab_batch_result_telnet、设备表tab_gw_device
 */
public class PortCloseCountDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(PortCloseCountDAO.class);


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
	public List<Map> portCloseListByCity() 
	{
		List<Map> list = new ArrayList<Map>();
		Map<String,String> resultMap = null;

		long totalNum = 0;//总数
		
		//总计
		long allTotalNum =0;
		long allSuccNum =0;
		long allUnDoneNum =0;
		long allFailNum =0;

		List<Map<String,String>> totalList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> successList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> notDoneList = new ArrayList<Map<String,String>>();

		//结果表中相同设备id 取最新的结果
		String tab_batch_result_telnet_temp = "(select device_id,task_id,status,rn from (select device_id,task_id,status,row_number() over (partition by device_id order by settime desc) rn from tab_batch_result_telnet) where rn = 1)" ; 
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		
		//每个城市总配置数
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("select count(*) as num,c.city_id ");
		}else{
			psql.append("select count(*) as num,c.city_id ");
		}
		psql.append("from tab_batchconfig_task a,"+tab_batch_result_telnet_temp+" b,tab_gw_device c ");
		psql.append("where a.task_id=b.task_id and b.device_id=c.device_id ");
		psql.append("and (a.parampath='"+parampath1+"' or a.parampath='"+parampath2 +"') ");
		psql.append("and a.paramvalue='"+ paramvalue +"' group by c.city_id");
		totalList = jt.queryForList(psql.getSQL());
		
		//每个城市成功数
		PrepareSQL psql1 = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql1.append("select count(*) as num,c.city_id ");
		}else{
			psql1.append("select count(*) as num,c.city_id ");
		}
		psql1.append("from tab_batchconfig_task a,"+tab_batch_result_telnet_temp+" b,tab_gw_device c ");
		psql1.append("where a.task_id = b.task_id and b.device_id = c.device_id and b.status = 1 ");
		psql1.append("and (a.parampath = '"+parampath1+"' or a.parampath = '"+parampath2 +"') ");
		psql1.append("and a.paramvalue = '"+ paramvalue +"' group by c.city_id");
		
		successList = jt.queryForList(psql1.getSQL());
		
		//每个城市未触发数
		PrepareSQL psql2 = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql2.append("select count(*) as num,c.city_id ");
		}else{
			psql2.append("select count(*) as num,c.city_id ");
		}
		psql2.append("from tab_batchconfig_task a,"+tab_batch_result_telnet_temp+" b,tab_gw_device c ");
		psql2.append("where a.task_id=b.task_id and b.device_id=c.device_id and (b.status=0 or b.status=7) ");
		psql2.append("and (a.parampath='"+parampath1+"' or a.parampath='"+parampath2 +"')  ");
		psql2.append("and a.paramvalue = '"+ paramvalue +"' group by c.city_id");
		
		notDoneList = jt.queryForList(psql2.getSQL());

		logger.warn("totalListSQL:"+totalList.toString());
		logger.warn("successListSQL:"+successList.toString());
		logger.warn("notDoneListSQL:"+notDoneList.toString());

		for (Map<String, String> map : totalList) 
		{
			long succNum = 0;//成功数
			long unDoneNum = 0;//未触发数
			long failNum = 0;//失败数

			resultMap = new HashMap();
			totalNum = StringUtil.getLongValue(map.get("num"));
			if(map != null){
				String cityId = map.get("city_id");
				for (Map<String, String> map2 : successList) {
					if(cityId.equals(map2.get("city_id"))) {
						succNum = StringUtil.getLongValue(map2.get("num"));
						break;
					}
				}
				for (Map<String, String> map3 : notDoneList) {
					if(cityId.equals(map3.get("city_id"))) {
						unDoneNum = StringUtil.getLongValue(map3.get("num"));
						break;
					}
				}
				//失败数
				failNum = totalNum - succNum - unDoneNum;
				
				resultMap.put("city_id",cityId);
				resultMap.put("city_name", Global.G_CityId_CityName_Map.get(cityId));
				resultMap.put("totalNum", totalNum+"");
				resultMap.put("succNum", succNum+"");
				resultMap.put("unDoneNum", unDoneNum+"");
				resultMap.put("failNum", failNum+"");
				
				list.add(resultMap);

				//总数
				allTotalNum = allTotalNum +totalNum;
				allSuccNum = allSuccNum + succNum;
				allUnDoneNum = allUnDoneNum + unDoneNum;
				allFailNum = allFailNum + failNum;
			}
		}
		//添加总数
		resultMap = new HashMap();
		resultMap.put("all", "总计");
		resultMap.put("allTotalNum", allTotalNum +"");
		resultMap.put("allSuccNum", allSuccNum +"");
		resultMap.put("allUnDoneNum", allUnDoneNum +"");
		resultMap.put("allFailNum", allFailNum +"");
		list.add(resultMap);
		resultMap = null;
		return list;
	}
	
	/** 统计列表导出
	 * 为了在导出文件里面有总计那一行，所以重新写个类返回
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
				tempMap.put("totalNum", map.get("totalNum"));
				tempMap.put("succNum", map.get("succNum"));
				tempMap.put("unDoneNum", map.get("unDoneNum"));
				tempMap.put("failNum", map.get("failNum"));
			}else {
				tempMap.put("city_name", "总数");
				tempMap.put("totalNum", map.get("allTotalNum"));
				tempMap.put("succNum", map.get("allSuccNum"));
				tempMap.put("unDoneNum", map.get("allUnDoneNum"));
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

		//结果表中相同设备id 取最新的结果
		String tab_batch_result_telnet_temp = "(select device_id,task_id,status,settime,rn from (select device_id,task_id,status,settime,row_number() over (partition by device_id order by settime desc) rn from tab_batch_result_telnet) where rn = 1)" ; 
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		
		PrepareSQL psql = new PrepareSQL();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		
		psql.append("select d.vendor_id,d.city_id,d.device_model_id,d.device_serialnumber,");
		psql.append("d.devicetype_id,s.status,s.settime ");
		psql.append("from tab_batchconfig_task a,"+tab_batch_result_telnet_temp+" s,tab_gw_device d ");
		psql.append("where a.task_id = s.task_id and d.device_id=s.device_id ");
		psql.append("and (a.parampath='"+parampath1+"' or a.parampath='"+parampath2 +"') ");
		psql.append("and a.paramvalue='"+ paramvalue +"'");
		if(!StringUtil.IsEmpty(city_id)){
			psql.append(" and d.city_id='"+city_id+"'");
		}
		if("unDone".equals(type)){
			psql.append(" and (s.status = 0 or s.status = 7) ");
		}
		else if("succ".equals(type)){
			psql.append(" and s.status=1 ");
		}
		else if("fail".equals(type)){
			psql.append(" and (s.status!=1 and s.status != 0 and s.status != 7) ");
		}
		psql.append(" order by s.status desc,s.settime desc");

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

			//执行时间
			long start_time = StringUtil.getLongValue(map.get("settime"));
			if(start_time!=0){
				map.put("settime", new DateTimeUtil(start_time*1000).getLongDate());
			}
			else{
				map.put("settime", "");
			}

			//区域
			if(null!=Global.G_CityId_CityName_Map){
				map.put("city_name", Global.G_CityId_CityName_Map.get(city_id));
			}

			//结果
			int status = StringUtil.getIntegerValue(map.get("status"));
			if(null!=Global.G_Fault_Map && null!=Global.G_Fault_Map.get(status) 
					&& !StringUtil.IsEmpty(Global.G_Fault_Map.get(status).getFaultDesc())){
				map.put("result_desc", Global.G_Fault_Map.get(status).getFaultDesc());
			}
			else{
				map.put("result_desc", map.get("fault_desc"));
			}

			if(status==0||status==7){
				map.put("result_id", "未做");
				map.put("result_desc", "/");
			}
			else if(status==1){
				map.put("result_id", "成功");
			}
			else{
				map.put("result_id", "失败");
			}
		}
		return list;
	}

}
