package com.linkage.module.gtms.stb.resource.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.dao.BatchRebootDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings("rawtypes")
public class BatchRebootBIO 
{
	private static Logger LOG = LoggerFactory.getLogger(BatchRebootBIO.class);
	private BatchRebootDAO dao;
	
	
	/**
	 * 获取厂商
	 * @return
	 */
	public List getVendor()
	{
		return dao.getVendor();
	}
	
	/**
	 * 根据厂商获取目标版本
	 * @param vendorId
	 * @return
	 */
	public String getTargetVersion(String vendorId) 
	{
		List list = dao.getTargetVersion(vendorId);
		
		StringBuffer bf = new StringBuffer();
		String deviceModel = "";
		String tempDM = "";
		String tempDTD = "";
		String softwareversion = "";
		Map map;
		for (int i = 0; i < list.size(); i++) 
		{
			map = (Map) list.get(i);
			tempDM = StringUtil.getStringValue(map.get("device_model"));
			tempDTD = StringUtil.getStringValue(map.get("devicetype_id"));
			softwareversion = StringUtil.getStringValue(map,"softwareversion","");
			
			if (!deviceModel.equalsIgnoreCase(tempDM)) {
				deviceModel = tempDM;
				if (i > 0) {
					bf.append("|");
				}
				bf.append(tempDM + '\1');
				bf.append(tempDTD + "$" + softwareversion);
			} else {
				bf.append("#" + tempDTD + "$" + softwareversion);
			}
			
			map=null;
		}
		list=null;
		return bf.toString();
	}
	
	/**
	 * 查询任务
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param cityId
	 * @param vendorId
	 * @param status
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getTask(int curPage_splitPage, int num_splitPage,
			String cityId, String vendorId, String status) 
	{
		int start=(curPage_splitPage - 1) * num_splitPage;
		List<Map> list=dao.getTask(start,num_splitPage,cityId,vendorId,status);
		
		if(list!=null && !list.isEmpty())
		{
			List vendorList=dao.getVendor();
			Map<String,String> vendorMap=new HashMap<String,String>();
			
			if(vendorList!=null && !vendorList.isEmpty()){
				for(int i=0;i<vendorList.size();i++){
					Map m=(Map)vendorList.get(i);
					vendorMap.put(StringUtil.getStringValue(m,"vendor_id"),
							StringUtil.getStringValue(m,"vendor_add"));
				}
			}
			vendorList=null;
			
			for(Map map:list)
			{
				if("0".equals(StringUtil.getStringValue(map,"data_type"))){
					map.put("data_desc","范围选择");
				}else{
					map.put("data_desc","批量导入");
				}
				
				map.put("city_name",CityDAO.getCityName(StringUtil.getStringValue(map,"city_id")));
				map.put("vendor_name",vendorMap.get(StringUtil.getStringValue(map,"vendor_id")));
				map.put("add_time",getTime(StringUtil.getLongValue(map,"add_time")));
				map.put("update_time",getTime(StringUtil.getLongValue(map,"update_time")));
			}
			vendorMap=null;
		}
		return list;
	}

	/**
	 * 统计任务
	 * @param num_splitPage
	 * @param cityId
	 * @param vendorId
	 * @param status
	 * @return
	 */
	public int countTask(int num_splitPage,String cityId, String vendorId, String status) 
	{
		return dao.countTask(num_splitPage,cityId,vendorId,status);
	}
	
	/**
	 * 异常账号数
	 * @param taskId
	 * @return
	 */
	public String getErrData(String taskId) 
	{
		int err_num=dao.getDataCount(taskId,-2);
		int all_num=dao.getDataCount(taskId,100);
		return err_num+","+all_num;
	}
	
	/**
	 * 激活、失效、删除任务
	 * @param taskId
	 * @param status
	 * @return
	 */
	public String updateTask(String taskId,String status) 
	{
		int task_status=StringUtil.getIntegerValue(status);
		int ier = dao.updateTask(taskId,task_status);
		LOG.warn("updateTask({},{}) result:{}",taskId,status,ier);
		
		String res="0,操作失败！";
		if(task_status==-2){
			res = ier >= 1 ? "1,失效任务成功！" : "0,失效任务失败！";
		}else if(task_status==1){
			res = ier >= 1 ? "1,激活任务成功！" : "0,激活任务失败！";
		}else if(task_status==2){
			res = ier >= 1 ? "1,删除任务成功！" : "0,删除任务失败！";
		}
		return res;
	}
	
	/**
	 * 获取任务详细
	 * @param taskId
	 * @return
	 */
	public Map getTaskInfo(String taskId) 
	{
		List list=dao.getTaskInfo(taskId);
		
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty())
		{
			Map m=(Map)list.get(0);
			
			map.put("task_id",taskId);
			map.put("task_desc",StringUtil.getStringValue(m,"task_desc"));
			map.put("task_detail",StringUtil.getStringValue(m,"task_detail"));
			map.put("data_type",StringUtil.getStringValue(m,"data_type"));
			map.put("acc_loginname",StringUtil.getStringValue(m,"acc_loginname"));
			map.put("add_time",getTime(StringUtil.getLongValue(m,"add_time")));
			map.put("update_time",getTime(StringUtil.getLongValue(m,"update_time")));
			
			int data_type=StringUtil.getIntValue(m,"data_type",0);
			if(1==data_type){
				map.put("data_type_desc","批量导入业务账号");
			}else if(2==data_type){
				map.put("data_type_desc","批量导入MAC地址");
			}else{
				map.put("data_type_desc","范围定制");
			}
			
			if(data_type==1 || data_type==2)
			{
				String file_path=StringUtil.getStringValue(m,"file_path","");
				map.put("file_name",file_path.replaceAll("/export/home/itms/WEB/temp/",""));
				
				int err_num=dao.getDataCount(taskId,-2);
				int	all_num=dao.getDataCount(taskId,100);
				map.put("all_num",all_num+"");
				map.put("err_num",err_num+"");
			}
			else
			{
				map.put("city_name",CityDAO.getCityName(StringUtil.getStringValue(m,"city_id")));
				
				List vendorList=dao.getVendor();
				Map<String,String> vendorMap=new HashMap<String,String>();
				
				if(vendorList!=null && !vendorList.isEmpty()){
					for(int i=0;i<vendorList.size();i++){
						Map vm=(Map)vendorList.get(i);
						vendorMap.put(StringUtil.getStringValue(vm,"vendor_id"),
								StringUtil.getStringValue(vm,"vendor_add"));
					}
				}
				vendorList=null;
				map.put("vendor_add",StringUtil.getStringValue(vendorMap,
										StringUtil.getStringValue(m,"vendor_id"),"所有厂商"));
				
				String device_type="所有版本";
				
				String device_type_id=StringUtil.getStringValue(m,"device_type_id");
				if(!StringUtil.IsEmpty(device_type_id))
				{
					List deviceTypeList=dao.getTaskDeviceTypeData(device_type_id);
					if(deviceTypeList!=null && !deviceTypeList.isEmpty()){
						StringBuffer st=new StringBuffer();
						for(int i=0;i<deviceTypeList.size();i++){
							Map dm=(Map)deviceTypeList.get(i);
							st.append(StringUtil.getStringValue(dm,"softwareversion")
										+"("+StringUtil.getStringValue(dm,"device_model")+"),");
							dm=null;
						}
						device_type=st.deleteCharAt(st.length()-1).toString();
					}
				}
				map.put("device_type",device_type);
			}
			
			if(1==StringUtil.getIntValue(m,"status") 
					|| 4==StringUtil.getIntValue(m,"status") ){
				map.put("status","激活");
			}else{
				map.put("status","失效");
			}
		}
		list=null;
		return map;
	}
	
	/**
	 * 导入的账号数据
	 * @param taskId
	 * @param data_type
	 * @param status
	 * @return
	 */
	public List exportTaskServ(String taskId, String data_type, String status) 
	{
		return dao.exportTaskServ(StringUtil.getLongValue(taskId),
									StringUtil.getIntegerValue(data_type),
									StringUtil.getIntegerValue(status));
	}
	
	/**
	 * 任务结果报表统计
	 * @param taskId
	 * @return
	 */
	public List getCount(String taskId)
	{
		List list=dao.getCount(taskId);
		if(list==null || list.isEmpty()){
			return null;
		}
		
		Map<String,String> cityNumMap=new HashMap<String,String>();
		List<String> cityList=new ArrayList<String>();
		int allNum=0;
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			if("2".equals(StringUtil.getIntValue(map,"result_status"))){
				continue;
			}
			
			allNum+=StringUtil.getIntValue(map,"num");
			String ci=StringUtil.getStringValue(map,"city_id");
			long n=StringUtil.getLongValue(map,"num");
			if(StringUtil.IsEmpty(cityNumMap.get(ci))){
				cityNumMap.put(ci,n+"");
			}else{
				cityNumMap.put(ci,StringUtil.getLongValue(cityNumMap,ci)+n+"");
			}
			
			if(!cityList.contains(ci)){
				cityList.add(ci);
			}
		}
		
		List<Map<String,String>> dealList=new ArrayList<Map<String,String>>();
		for(int i=0;i<list.size();i++)
		{
			Map map=(Map)list.get(i);
			
			Map<String,String> rm=new HashMap<String,String>();
			rm.put("task_id",taskId);
			
			String city_id=StringUtil.getStringValue(map,"city_id");
			rm.put("city_id",city_id);
			rm.put("city_name",CityDAO.getCityName(city_id));

			long count=StringUtil.getIntValue(cityNumMap, city_id);
			rm.put("count",count+"");
			rm.put("countPerc",getPercent(count,allNum));
			
			int result_status=StringUtil.getIntValue(map,"result_status");
			long num=StringUtil.getLongValue(map,"num");
			
			rm.put("unDo","0");
			rm.put("suss","0");
			rm.put("sussPerc","0.00%");
			rm.put("noDo","0");
			//-2：无设备，-1：失效，0:未操作，1：成功 ,2:不支持
			switch(result_status)
			{
				case 0:
					rm.put("unDo",num+"");
					break;
				case 1:
					rm.put("suss",num+"");
					break;
				case 2:
					rm.put("noDo",num+"");
					break;
			}
			dealList.add(rm);
			
			map=null;
			rm=null;
			city_id=null;
		}
		list=null;
		
		List<Map<String,String>> resultList=new ArrayList<Map<String,String>>();
		for(String city:cityList)
		{
			Map<String,String> rm=new HashMap<String,String>();
			rm.put("unDo","0");
			rm.put("suss","0");
			rm.put("noDo","0");
			long suss=0;
			for(Map<String,String> map:dealList)
			{
				if(city.equals(map.get("city_id"))){
					rm.put("task_id",taskId);
					rm.put("city_id",city);
					rm.put("city_name",CityDAO.getCityName(city));
					rm.put("count",map.get("count"));
					rm.put("countPerc",map.get("countPerc"));
					
					suss=StringUtil.getLongValue(map.get("suss"));
					if(StringUtil.getLongValue(map.get("unDo"))>0){
						rm.put("unDo",map.get("unDo"));
					}
					
					if(suss>0){
						rm.put("sussPerc",getPercent(suss,StringUtil.getLongValue(map.get("count"))));
						rm.put("suss",map.get("suss"));
					}
					
					if(StringUtil.getLongValue(map.get("noDo"))>0){
						rm.put("noDo",map.get("noDo"));
					}
				}
			}
			
			if(StringUtil.IsEmpty(rm.get("sussPerc"))){
				rm.put("sussPerc","0.00%");
			}
			resultList.add(rm);
		}
		cityList=null;
		
		int sussAllNum=0;
		int undoAllNum=0;
		int nodoAllNum=0;
		for(Map<String,String> map:resultList){
			sussAllNum+=StringUtil.getIntValue(map,"suss");
			undoAllNum+=StringUtil.getIntValue(map,"unDo");
			nodoAllNum+=StringUtil.getIntValue(map,"noDo");
		}
		
		Map<String,String> rm1=new HashMap<String,String>();
		rm1.put("task_id",taskId);
		rm1.put("city_id","allCity");
		rm1.put("city_name","小计");
		rm1.put("suss",sussAllNum+"");
		rm1.put("unDo",undoAllNum+"");
		rm1.put("noDo",nodoAllNum+"");
		rm1.put("count",allNum+"");
		resultList.add(rm1);
		rm1=null;
		
		Map<String,String> rm2=new HashMap<String,String>();
		rm2.put("task_id",taskId);
		rm2.put("city_id","-1");
		rm2.put("city_name","占比");
		rm2.put("suss",getPercent(sussAllNum,allNum));
		rm2.put("unDo",getPercent(undoAllNum,allNum));
		rm2.put("noDo",getPercent(nodoAllNum,allNum));
		resultList.add(rm2);
		rm2=null;
		
		return resultList;
	}
	
	/**
	 * 设备详细
	 * @param task_id
	 * @param city_id
	 * @param status
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDevList(String task_id,String city_id,String status,
			int curPage_splitPage, int num_splitPage) 
	{
		List list;
		if(curPage_splitPage==-1 && num_splitPage==-1){
			list=dao.getDevList(StringUtil.getLongValue(task_id), city_id, 
					StringUtil.getIntegerValue(status),0,0);
		}else{
			int start=(curPage_splitPage - 1) * num_splitPage;
			list=dao.getDevList(StringUtil.getLongValue(task_id), city_id, 
								StringUtil.getIntegerValue(status), start, num_splitPage);
		}
		
		if(list!=null && !list.isEmpty())
		{
			List vendorList=dao.getVendor();
			Map<String,String> vendorMap=new HashMap<String,String>();
			
			if(vendorList!=null && !vendorList.isEmpty()){
				for(int i=0;i<vendorList.size();i++){
					Map m=(Map)vendorList.get(i);
					vendorMap.put(StringUtil.getStringValue(m,"vendor_id"),
							StringUtil.getStringValue(m,"vendor_add"));
				}
			}
			vendorList=null;
			
			List deviceModelList=dao.getDeviceModle();
			Map<String,String> deviceModelMap=new HashMap<String,String>();
			
			if(deviceModelList!=null && !deviceModelList.isEmpty()){
				for(int i=0;i<deviceModelList.size();i++){
					Map m=(Map)deviceModelList.get(i);
					deviceModelMap.put(StringUtil.getStringValue(m,"device_model_id"),
							StringUtil.getStringValue(m,"device_model"));
				}
			}
			deviceModelList=null;
			
			List deviceTypeList=dao.getDeviceType();
			Map<String,String> deviceTypeMap=new HashMap<String,String>();
			
			if(deviceTypeList!=null && !deviceTypeList.isEmpty()){
				for(int i=0;i<deviceTypeList.size();i++){
					Map m=(Map)deviceTypeList.get(i);
					deviceTypeMap.put(StringUtil.getStringValue(m,"devicetype_id"),
							StringUtil.getStringValue(m,"hardwareversion")
							+"|"+StringUtil.getStringValue(m,"softwareversion"));
				}
			}
			deviceTypeList=null;
			
			for(int i=0;i<list.size();i++)
			{
				Map m=(Map)list.get(i);
				
				m.put("cityName",CityDAO.getCityName(StringUtil.getStringValue(m,"city_id")));
				m.put("vendorName", vendorMap.get(StringUtil.getStringValue(m,"vendor_id")));
				m.put("deviceModel", deviceModelMap.get(StringUtil.getStringValue(m,"device_model_id")));
				
				String deviceType=deviceTypeMap.get(StringUtil.getStringValue(m,"devicetype_id"));
				if(StringUtil.IsEmpty(deviceType)){
					m.put("hardwareversion","");
					m.put("softwareversion","");
				}else{
					m.put("hardwareversion",deviceType.split("\\|")[0]);
					m.put("softwareversion",deviceType.split("\\|")[1]);
				}
				
			}
			
			vendorMap=null;
			deviceModelMap=null;
			deviceTypeMap=null;
		}

		return list;
	}
	
	
	/**
	 * 设备总量
	 * @param task_id
	 * @param city_id
	 * @param status
	 * @param num_splitPage
	 * @return
	 */
	public int countDevList(String task_id,String city_id,String status, int num_splitPage)
	{
		return dao.countDevList(StringUtil.getLongValue(task_id), city_id, 
								StringUtil.getIntegerValue(status), num_splitPage);
	}
	
	/**
	 * 定制任务
	 * @param user_id
	 * @param taskId
	 * @param cityId
	 * @param vendorId
	 * @param deviceTypeIds
	 * @param data_type
	 * @param filePath
	 * @param taskDesc
	 * @param taskDetail
	 * @return
	 */
	public int addTask(long user_id,long taskId,String cityId, String vendorId,
			String deviceTypeIds, String data_type,
			String filePath, String taskDesc, String taskDetail) 
	{
		vendorId="-1".equals(vendorId)? null:vendorId;
		if(!StringUtil.IsEmpty(deviceTypeIds)){
			deviceTypeIds=deviceTypeIds.substring(0,deviceTypeIds.length()-1);
		}
		
		return dao.addTask(user_id,taskId,cityId,vendorId,deviceTypeIds,
							StringUtil.getIntegerValue(data_type),
							filePath,taskDesc,taskDetail);
	}
	

	/**
	 * 日志记录
	 * @param user_id
	 * @param user_ip
	 * @param operation_content
	 * @param result
	 */
	public void addLog(long user_id, String user_ip, String operation_content,String result) 
	{
		dao.addLog(user_id,user_ip,operation_content,result);
	}
	
	/**
	 * 保留两位小数
	 * @return
	 */
	private String getPercent(long a,long b)
	{
		if(a==0 || b==0){
			return "0.00%";
		}
		
		String n=StringUtil.getStringValue((double) a*100/b)+"000";
		return n.substring(0,n.indexOf(".")+3)+"%";
    }
	
	/**
	 * 秒数转日期
	 * @param time
	 * @return
	 */
	private String getTime(long time)
	{
		if(time==0){
			return "";
		}
		
		try
		{
			DateTimeUtil dt = new DateTimeUtil(time * 1000);
			return dt.getLongDate();
		}
		catch (Exception e)
		{
			return "";
		}
	}

	
	
	public BatchRebootDAO getDao() {
		return dao;
	}

	public void setDao(BatchRebootDAO dao) {
		this.dao = dao;
	}

}
