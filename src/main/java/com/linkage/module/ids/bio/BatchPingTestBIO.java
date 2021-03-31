package com.linkage.module.ids.bio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.commons.db.DBUtil;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.ids.dao.BatchPingTestDAO;

public class BatchPingTestBIO 
{
	private Logger logger = LoggerFactory.getLogger(BatchPingTestBIO.class);
	private BatchPingTestDAO dao = null;
	
	
	public String addTaskInfo(String taskname, String taskid, long accoid,
			String addtime, String starttime, String endtime, String enddate,
			String pingtype, String packetsize, String packetnum,
			String timeout, String url,String filename) 
	{
		try {
			//从xls中解析出设备序列号
			List<String> list = getImportDataByXLS(filename);
			//批量插入设备序列号到临时表
			dao.insertTmp(filename, list);
			//根据设备序列号获取设备信息
			List<Map> devList = dao.getTaskDevList(filename);
			//logger.warn("devList:{}",devList.size());
			//获取sqlList
			ArrayList<String> sqlList = sqlList(devList,taskname,taskid);
			//批量插入设备信息
			dao.insertTaskDev(sqlList);
			
		} catch (BiffException e) {
			logger.warn("{}文件解析出错！",filename);
			return "false";
		} catch (IOException e) {
			logger.warn("{}文件解析出错！",filename);
			return "false";
		}catch(Exception e){
			logger.warn("批量定制任务执行失败");
			return "false";
		}
		int num = dao.addTaskInfo(taskname, taskid, accoid, addtime, starttime, endtime, 
						enddate, pingtype, packetsize, packetnum, timeout, url);
		return "批量定制任务执行成功！";
	}
	
	public String getExcelRow(String fileName)
	{
		String msg="true";
		File f = new File(getFilePath()+fileName);
		int rowCount=0;
		try {
			Workbook wwb = Workbook.getWorkbook(f);;
			Sheet ws = null;
			ws=wwb.getSheet(0);
			rowCount = ws.getRows();
		} catch (BiffException e) {
			logger.warn("BiffException");
		} catch (IndexOutOfBoundsException e) {
			logger.warn("IndexOutOfBoundsException");
		} catch (IOException e) {
			logger.warn("IOException");
		}
		if(rowCount>2000){
			msg="false";
			f.delete();
		}
		return msg;
	}
	
	public List<Map> queryTask(String name,String acc_loginname,
			String starttime,String endtime,int curPage_splitPage, int num_splitPage)
	{
		return dao.queryTask(name, acc_loginname, starttime, endtime, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> queryTaskExcel(String name,String acc_loginname,String starttime,String endtime)
	{
		return dao.queryTaskExcel(name, acc_loginname, starttime, endtime);
	}
	
	public int queryTaskCount(String name,String acc_loginname,String starttime,
			String endtime,int curPage_splitPage, int num_splitPage)
	{
		return dao.queryTaskCount(name, acc_loginname, starttime, endtime, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> getTaskDevList(String taskid,int curPage_splitPage, int num_splitPage) 
	{
		return dao.getTaskDevList(taskid, curPage_splitPage, num_splitPage);
	}
	
	public int getTaskCount(String taskid,int curPage_splitPage, int num_splitPage) 
	{
		return dao.getTaskDevCount(taskid, curPage_splitPage, num_splitPage);
	}
	
	public String delTask(String taskid){
		 dao.delTask(taskid);
		 return "删除成功";
	}
	
	public Map queryTaskInfo(String taskid)
	{
		return dao.queryTaskInfo(taskid);
	}
	
	public List<Map> queryPingResult(String serialnumber, String starttime,
			String endtime, int curPage_splitPage, int num_splitPage) 
	{
		return dao.queryPingResult(serialnumber, starttime, endtime, curPage_splitPage, num_splitPage);
	}
	
	public int queryPingResultCount(String serialnumber, String starttime,
			String endtime, int curPage_splitPage, int num_splitPage) 
	{
		return dao.queryPingResultCount(serialnumber, starttime, endtime, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> queryPingResultExcel(String serialnumber, String starttime,String endtime)		
	{
		List<Map> list=dao.queryPingResultExcel(serialnumber, starttime, endtime);
		
		List<Map> returnList=new ArrayList<Map>();
		if(list!=null && !list.isEmpty())
		{
			Map<String,String> vendorNameMap=null;
			Map<String,String> deviceModelMap=null;
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				vendorNameMap=dao.getVendorName();
				deviceModelMap=dao.getDeviceModel();
			}
			
			for(Map m:list)
			{
				Map<String,String> map = new HashMap<String,String>();
				
				String cityName,vendorName,deviceModel;
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					cityName=CityDAO.getCityName(StringUtil.getStringValue(m,"city_id"));
					vendorName=vendorNameMap.get(StringUtil.getStringValue(m,"vendor_id"));
					deviceModel=deviceModelMap.get(StringUtil.getStringValue(m,"device_model_id"));
					if(StringUtil.IsEmpty(cityName) 
							|| StringUtil.IsEmpty(vendorName) 
							|| StringUtil.IsEmpty(deviceModel)){
						continue;
					}
				}else{
					cityName=StringUtil.getStringValue(m,"city_name");
					vendorName=StringUtil.getStringValue(m,"vendor_name");
					deviceModel=StringUtil.getStringValue(m,"device_model");
				}
				
				map.put("city_name",cityName);
				map.put("vendor_name",vendorName);
				map.put("device_model",deviceModel);
				map.put("device_serialnumber",StringUtil.getStringValue(m,"device_serialnumber"));
				long test_time = StringUtil.getLongValue(m,"test_time");
				map.put("test_time", DateUtil.transTime(test_time, "yyyy-MM-dd HH:mm:ss"));
				String ping_type = StringUtil.getStringValue(m,"wan_interface");
				if(ping_type.equals("1")){
					ping_type="TR069";
				}else if(ping_type.equals("2")){
					ping_type="宽带上网";
				}
				map.put("ping_type", ping_type);
				map.put("success_count", StringUtil.getStringValue(m,"success_count"));
				map.put("failure_count", StringUtil.getStringValue(m,"failure_count"));
				map.put("avg_resp_time", StringUtil.getStringValue(m,"avg_resp_time"));
				map.put("min_resp_time", StringUtil.getStringValue(m,"min_resp_time"));
				map.put("max_resp_time", StringUtil.getStringValue(m,"max_resp_time"));
				map.put("packet_loss_rate", StringUtil.getStringValue(m,"packet_loss_rate"));
				map.put("url", StringUtil.getStringValue(m,"test_ip"));
				
				returnList.add(map);
				map=null;
				m=null;
			}
		}
		
		list.clear();
		list=null;
		return returnList;
	}
	
	private ArrayList<String> sqlList(List<Map> list,String name,String taskid)
	{
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			String oui = String.valueOf(list.get(i).get("oui"));
			String username = String.valueOf(list.get(i).get("username"));
			String device_serialnumber = String.valueOf(list.get(i).get("device_serialnumber"));
			String device_id = String.valueOf(list.get(i).get("device_id"));
			StringBuffer sql = new StringBuffer();
			sql.append("insert into tab_ids_task_dev(task_name,task_id,device_id,oui,device_serialnumber,loid,statu)");
			sql.append(" values('"+name+"','"+taskid+"',"+device_id+",'"+oui+"','"+device_serialnumber+"','"+username+"',0)");
			logger.info(sql.toString());
			sqlList.add(sql.toString());
		}
		return sqlList;
	}
	
	private List<String> getImportDataByXLS(String fileName) throws IOException, BiffException
	{
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath()+fileName);
		Workbook wwb = Workbook.getWorkbook(f);;
		Sheet ws = null;
		//总sheet数
		//int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m=0;m<sheetNumber;m++){
			ws = wwb.getSheet(m);
			
			//当前页总记录行数和列数
			int rowCount = ws.getRows();
			int columeCount = ws.getColumns();
			//取当前页所有值放入list中
			for (int i=1;i<rowCount;i++){
				String temp = ws.getCell(0, i).getContents();
				if(null!=temp && !"".equals(temp)){
					if(!"".equals(ws.getCell(0, i).getContents().trim())){
						list.add(ws.getCell(0, i).getContents().trim());
					}
				}
			}
		}
		f.delete();
		return list;
	}
	
	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath() 
	{
		//获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径",lipossHome);
		return lipossHome + "/temp/";
	}
	
	
	public BatchPingTestDAO getDao() {
		return dao;
	}
	
	public void setDao(BatchPingTestDAO dao) {
		this.dao = dao;
	}
}
