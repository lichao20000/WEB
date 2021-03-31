package com.linkage.module.ids.bio;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.ids.dao.HTTPDownloadDAO;
import com.linkage.module.ids.dao.HTTPUploadDAO;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class HTTPUploadBIO {
	private static Logger logger = LoggerFactory
			.getLogger(HTTPUploadBIO.class);
	private HTTPUploadDAO dao;
	
	public String addTask4NX(String name,String taskid,long accoid,String addtime,String starttime,String endtime,
			String enddate,String deviceIds,String param,String url,String testUserName, String testPWD){
		try {
			List<String> netAccounts = new ArrayList<String>();
			
			if(StringUtil.IsEmpty(deviceIds) || "0".equals(deviceIds)){
				String sqlSpell = null;
				if(!StringUtil.IsEmpty(param)){
					String[] paramArr = param.split("\\|");
					if(null!=paramArr &&  paramArr.length>=11){
						sqlSpell = paramArr[10];
					}
				}
				
				if(StringUtil.IsEmpty(sqlSpell)){
					logger.warn("==[{}][{}]设备为空，查询sql为空，程序结束==", taskid, name);
					return "false";
				}
				
				ArrayList<HashMap<String, String>> devList = dao.getDevIds4NX(sqlSpell);
				if(null==devList || devList.size()==0){
					logger.warn("==[{}][{}]设备为空，程序结束==", taskid, name);
					return "false";
				}
				
				int num = dao.addTask4NX(name, taskid, accoid, addtime, starttime, endtime, enddate,url,testUserName,testPWD, sqlSpell, 2);
				if(num>0){
					return "true";
				}else{
					return "false";
				}
			}
			else
			{
			
				String[] deviceIdsArr = null==deviceIds ? null : deviceIds.split(",");
				if(null==deviceIdsArr || 0==deviceIdsArr.length){
					logger.warn("==[{}][{}]设备为空，程序结束==", taskid, name);
					return "false";
				}
			
				for(String deviceId : deviceIdsArr){
					if(!StringUtil.IsEmpty(deviceId)){
						netAccounts.add(deviceId);
					}
				}
			}
			
			if(null==netAccounts || netAccounts.size()==0){
				logger.warn("==[{}][{}]设备查询结果为空，程序结束==", taskid, name);
				return "false";
			}
			// 批量插入设备ID到临时表
			String filenameTmp = taskid+"-"+name;
			dao.insertTmp(filenameTmp, netAccounts);
			// 根据设备序列号获取设备信息
			ArrayList<HashMap<String, String>> devList = dao.getTaskDevList4NX(filenameTmp);
			List<HashMap<String, String>> devListNew = new ArrayList<HashMap<String, String>>();
			
			if(null!=devList && devList.size()>0){
				for(HashMap<String, String> map : devList)
				{
					String aDeviceId = StringUtil.getStringValue(map, "a_device_id", "");
					
					if(StringUtil.IsEmpty(map.get("device_id")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到设备", taskid, aDeviceId);
					}
					else if(StringUtil.IsEmpty(map.get("wan_type")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到上网方式", taskid, aDeviceId);
					}
					else if(!"1".equals(map.get("wan_type")) && !"2".equals(map.get("wan_type")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]既不是路由也不是桥接", taskid, aDeviceId);
					}
					else if(StringUtil.IsEmpty(map.get("username")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到loid", taskid, aDeviceId);
					}
					else{
						devListNew.add(map);
					}
				}
				if(null==devListNew || devListNew.size()==0)
				{
					logger.warn("taskid[{}] devListNew is null", taskid);
					return "false";
				}
			}else{
				logger.warn("taskid[{}] devList is null", taskid);
				return "false";
			}
			
			logger.warn("taskid[{}]-devListNew.size[{}]", taskid, devListNew.size());
			if(devListNew.size()>10000){
				logger.warn("taskid[{}] 定制设备超过10000条，程序结束==", taskid);
				return "false10000";
			}
			// 获取sqlList
			ArrayList<String> sqlList = dao.sqlList(devListNew, name, taskid);
			ArrayList<String> sqlListTmp = new ArrayList<String>();
			int count = 0;
			
			// 批量插入设备信息
			if(null!=sqlList && sqlList.size()>0){
				for(String sql : sqlList){
					sqlListTmp.add(sql);
					if(sqlListTmp.size()>=200){
						int res = dao.insertTaskDev4NX(sqlListTmp);
						if(res>0){
							count += sqlListTmp.size();
						}
						sqlListTmp.clear();
					}
				}
			}
			if(sqlListTmp.size()>0){
				int res = dao.insertTaskDev4NX(sqlListTmp);
				if(res>0){
					count += sqlListTmp.size();
				}
				sqlListTmp.clear();
			}
			
			logger.warn("==[{}]插入tab_ids_task表[{}]条数据==", taskid, count);
			
			int num = dao.addTask4NX(name, taskid, accoid, addtime, starttime, endtime, enddate,url,testUserName,testPWD, "", 1);
			if(num>0){
				return "true";
			}else{
				return "false";
			}
			
		} catch (Exception e) {
			logger.warn("批量定制任务执行失败e[{}]", e);
			return "false";
		}
	}
	
	/**
	 * 查询任务
	 */
	public List<Map> queryTask(String name,String acc_loginname,String starttime,String endtime,int curPage_splitPage, int num_splitPage){
		return dao.queryTask(name, acc_loginname, starttime, endtime, curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 查询任务总数
	 */
	public int queryTaskCount(String name,String acc_loginname,String starttime,String endtime,int curPage_splitPage, int num_splitPage){
		return dao.queryTaskCount(name, acc_loginname, starttime, endtime, curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 删除任务
	 */
	public String delTask(String taskid){
		 dao.delTask(taskid);
		 return "删除成功";
	}
	
	/**
	 * 查看详细
	 */
	public Map queryTaskInfo(String taskid){
		return dao.queryTaskInfo(taskid);
	}
	
	/**
	 * 设备列表
	 */
	public List<Map> getTaskDevList(String taskid,int curPage_splitPage, int num_splitPage) {
		return dao.getTaskDevList(taskid, curPage_splitPage, num_splitPage);
	}
	/**
	 * 设备列表总数
	 */
	public int getTaskCount(String taskid,int curPage_splitPage, int num_splitPage) {
		return dao.getTaskDevCount(taskid, curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 结果导出
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getTaskDevResToExcel(String taskId){
		List<Map> list = dao.getTaskDevResToExcel(taskId);
		if(null != list && list.size()>0){
			
			for(int i=0;i<list.size();i++){
				String bomtime = String.valueOf(list.get(i).get("bom_time"));
				String eomtime = String.valueOf(list.get(i).get("eom_time"));
				String upload_pert = String.valueOf(list.get(i).get("upload_pert"));
				String totalbytessent =  String.valueOf(list.get(i).get("totalbytessent"));
				String totalbytesreceived = String.valueOf(list.get(i).get("totalbytesreceived"));
				String tasknameTmp = String.valueOf(list.get(i).get("task_name"));
				
				if(bomtime.length()>10){
					bomtime = (bomtime.replace("T", " ")).substring(0,19);
					eomtime = (eomtime.replace("T", " ")).substring(0,19);
					list.get(i).put("bomtime", bomtime);
					list.get(i).put("eomtime", eomtime);
				}
				list.get(i).put("upload_pert", upload_pert);
				list.get(i).put("totalbytessent", totalbytessent);
				list.get(i).put("totalbytesreceived", totalbytesreceived);
				list.get(i).put("task_name", tasknameTmp);
				
			}
		}
		return list;
	}
	
	/**
	 * 导出任务列表
	 */
	public List<Map> queryTaskExcel(String name,String acc_loginname,String starttime,String endtime){
		return dao.queryTaskExcel(name, acc_loginname, starttime, endtime);
	}
	
	/**
	 * 查询结果
	 */
	@SuppressWarnings("unchecked")
	public List<Map> queryDevBySerialnumber(String taskname,String serialnumber,String starttime,String endtime,int curPage_splitPage, int num_splitPage){
		List<Map> list = dao.queryDevBySerialnumber(taskname,serialnumber,starttime,endtime, curPage_splitPage, num_splitPage);
		if(null != list && list.size()>0){
			for(int i=0;i<list.size();i++){
				String bomtime = String.valueOf(list.get(i).get("bom_time"));
				String eomtime = String.valueOf(list.get(i).get("eom_time"));
				String upload_pert = String.valueOf(list.get(i).get("upload_pert"));
				String totalbytessent =  String.valueOf(list.get(i).get("totalbytessent"));
				String totalbytesreceived = String.valueOf(list.get(i).get("totalbytesreceived"));
				
				if(bomtime.length()>10){
					bomtime = (bomtime.replace("T", " ")).substring(0,19);
					eomtime = (eomtime.replace("T", " ")).substring(0,19);
					list.get(i).put("bomtime", bomtime);
					list.get(i).put("eomtime", eomtime);
				}
				list.get(i).put("upload_pert", upload_pert);
				list.get(i).put("totalbytessent", totalbytessent);
				list.get(i).put("totalbytesreceived", totalbytesreceived);
			}
		}
		return list;
	}
	
	/**
	 * 查询结果总数
	 */
	public int queryDevBySerialnumberCount(String taskname, String serialnumber,String starttime,String endtime,int curPage_splitPage, int num_splitPage){
		return dao.queryDevBySerialnumberCount(taskname,serialnumber,starttime,endtime, curPage_splitPage, num_splitPage);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map> queryDevBySerialnumberExcel(String taskname,String serialnumber,String starttime,String endtime){
		List<Map> list = dao.queryDevBySerialnumberExcel(taskname,serialnumber, starttime, endtime);
		if(null != list && list.size()>0){
			
			for(int i=0;i<list.size();i++){
				String bomtime = String.valueOf(list.get(i).get("bom_time"));
				String eomtime = String.valueOf(list.get(i).get("eom_time"));
				String upload_pert = String.valueOf(list.get(i).get("upload_pert"));
				String totalbytessent =  String.valueOf(list.get(i).get("totalbytessent"));
				String totalbytesreceived = String.valueOf(list.get(i).get("totalbytesreceived"));
				String tasknameTmp = String.valueOf(list.get(i).get("task_name"));
				
				if(bomtime.length()>10){
					bomtime = (bomtime.replace("T", " ")).substring(0,19);
					eomtime = (eomtime.replace("T", " ")).substring(0,19);
					list.get(i).put("bomtime", bomtime);
					list.get(i).put("eomtime", eomtime);
				}
				list.get(i).put("upload_pert", upload_pert);
				list.get(i).put("totalbytessent", totalbytessent);
				list.get(i).put("totalbytesreceived", totalbytesreceived);
				list.get(i).put("task_name", tasknameTmp);
			}
			
			
		}
		return list;
	}
	
	
	public HTTPUploadDAO getDao() {
		return dao;
	}

	public void setDao(HTTPUploadDAO dao) {
		this.dao = dao;
	}
	
	
}
