package com.linkage.module.ids.bio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.ids.dao.HTTPDownloadDAO;

public class HTTPDownloadBIO {
	private static Logger logger = LoggerFactory
			.getLogger(HTTPDownloadBIO.class);
	private HTTPDownloadDAO dao;
	
	public String addTask(String name,String taskid,long accoid,String addtime,String starttime,String endtime,String enddate,String filename,String url,String testUserName, String testPWD){
		
		try {
			//从xls中解析出设备序列号
			List<String> list = getImportDataByXLS(filename);
			//批量插入设备序列号到临时表
			dao.insertTmp(filename, list);
			//根据设备序列号获取设备信息
			List<Map> devList = dao.getTaskDevList(filename);
			//logger.warn("devList:{}",devList.size());
			//获取sqlList
			ArrayList<String> sqlList = sqlList(devList,name,taskid);
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
//		int num = dao.addTask(name, taskid, accoid, addtime, starttime, endtime, enddate,url,httpType);
		int num = dao.addTask(name, taskid, accoid, addtime, starttime, endtime, enddate,url,testUserName,testPWD);
		return "批量定制任务执行成功！";
	}

	public String addTask4JX(String name,String taskid,long accoid,String addtime,String starttime,String endtime,String enddate,String fileName){
		FileInputStream in=null;
		try {
			//将文件上传至后台模块ftp
			String ftpIp = LipossGlobals.getLipossProperty("batchHttp.ftpHost");
			String ftpPort = LipossGlobals.getLipossProperty("batchHttp.ftpPort");
			String ftpUserName = LipossGlobals.getLipossProperty("batchHttp.ftpUserName");
			String ftpPwd = LipossGlobals.getLipossProperty("batchHttp.ftpPwd");
			String ftpPath = LipossGlobals.getLipossProperty("batchHttp.ftpPath");
			//获取文件
			String filePath = getFilePath() + fileName;
			logger.warn("addTask4JX with filePath:{}",filePath);
			in = new FileInputStream(new File(filePath));
			if(FtpUtil.uploadFile(ftpIp,ftpUserName,ftpPwd,Integer.parseInt(ftpPort),ftpPath,fileName,in)){
				//上传成功 写入任务表
				int num = dao.addTask4JX(name, taskid, accoid, addtime, starttime, endtime, enddate,fileName);
				return num > 0 ? "true" : "false";
			}
			logger.warn("addTask4JX with uploadFile fail,taskName:{}",name);
			return "false";
		}catch (Exception e){
			logger.warn("addTask4JX with uploadFile error,taskName:{}",name,e);
			return "false";
		}finally{
			try {
				if(in!=null){
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

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
	
	public List<Map> queryTask(String name,String acc_loginname,String starttime,String endtime,int curPage_splitPage, int num_splitPage){
		return dao.queryTask(name, acc_loginname, starttime, endtime, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> queryTaskExcel(String name,String acc_loginname,String starttime,String endtime){
		return dao.queryTaskExcel(name, acc_loginname, starttime, endtime);
	}
	
	public int queryTaskCount(String name,String acc_loginname,String starttime,String endtime,int curPage_splitPage, int num_splitPage){
		return dao.queryTaskCount(name, acc_loginname, starttime, endtime, curPage_splitPage, num_splitPage);
	}
	
	public String delTask(String taskid){
		 dao.delTask(taskid);
		 return "删除成功";
	}
	
	public List<Map> getTaskDevList(String taskid,int curPage_splitPage, int num_splitPage) {
		return dao.getTaskDevList(taskid, curPage_splitPage, num_splitPage);
	}
	
	public int getTaskCount(String taskid,int curPage_splitPage, int num_splitPage) {
		return dao.getTaskDevCount(taskid, curPage_splitPage, num_splitPage);
	}

	public Map queryTaskInfo(String taskid){
		return dao.queryTaskInfo(taskid);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map> queryDevBySerialnumber(String taskname,String serialnumber,String starttime,String endtime,int curPage_splitPage, int num_splitPage){
		List<Map> list = dao.queryDevBySerialnumber(taskname,serialnumber,starttime,endtime, curPage_splitPage, num_splitPage);
		if(null != list && list.size()>0){
			for(int i=0;i<list.size();i++){
				String bomtime = String.valueOf(list.get(i).get("bom_time"));
				String eomtime = String.valueOf(list.get(i).get("eom_time"));
				String downPert = String.valueOf(list.get(i).get("down_pert"));
				String totalDownPert =  String.valueOf(list.get(i).get("total_down_pert"));
				String receivebyte = String.valueOf(list.get(i).get("total_bytes_rece"));
				
				if(bomtime.length()>10){
					bomtime = (bomtime.replace("T", " ")).substring(0,19);
					eomtime = (eomtime.replace("T", " ")).substring(0,19);
					list.get(i).put("bomtime", bomtime);
					list.get(i).put("eomtime", eomtime);
				}
				list.get(i).put("downPert", downPert);
				list.get(i).put("totalDownPert", totalDownPert);
				list.get(i).put("total_bytes_rece", receivebyte);
				
				
				/**
				String downPert ="0.0";
				String receivebyte = String.valueOf(list.get(i).get("total_bytes_rece"));
				if(bomtime.length()>10){
					downPert = getDownPert(bomtime, eomtime, receivebyte);
					bomtime = (bomtime.replace("T", " ")).substring(0,19);
					eomtime = (eomtime.replace("T", " ")).substring(0,19);
					list.get(i).put("bomtime", bomtime);
					list.get(i).put("eomtime", eomtime);
				}
				list.get(i).put("downPert", downPert);
				*/
			}
			
			
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map> queryDevBySerialnumberExcel(String taskname,String serialnumber,String starttime,String endtime){
		List<Map> list = dao.queryDevBySerialnumberExcel(taskname,serialnumber, starttime, endtime);
		if(null != list && list.size()>0){
			
			for(int i=0;i<list.size();i++){
				String bomtime = String.valueOf(list.get(i).get("bom_time"));
				String eomtime = String.valueOf(list.get(i).get("eom_time"));
				String downPert = String.valueOf(list.get(i).get("down_pert"));
				String totalDownPert =  String.valueOf(list.get(i).get("total_down_pert"));
				String receivebyte = String.valueOf(list.get(i).get("total_bytes_rece"));
				String tasknameTmp = String.valueOf(list.get(i).get("task_name"));
				
				if(bomtime.length()>10){
					bomtime = (bomtime.replace("T", " ")).substring(0,19);
					eomtime = (eomtime.replace("T", " ")).substring(0,19);
					list.get(i).put("bomtime", bomtime);
					list.get(i).put("eomtime", eomtime);
				}
				list.get(i).put("downPert", downPert);
				list.get(i).put("totalDownPert", totalDownPert);
				list.get(i).put("total_bytes_rece", receivebyte);
				list.get(i).put("task_name", tasknameTmp);
				
				/**
				String downPert ="0.0";
				String receivebyte = String.valueOf(list.get(i).get("total_bytes_rece"));
				if(bomtime.length()>10){
					downPert = getDownPert(bomtime, eomtime, receivebyte);
					bomtime = (bomtime.replace("T", " ")).substring(0,19);
					eomtime = (eomtime.replace("T", " ")).substring(0,19);
					list.get(i).put("bomtime", bomtime);
					list.get(i).put("eomtime", eomtime);
				}
				list.get(i).put("downPert", downPert);
				*/
			}
			
			
		}
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map> getTaskDevResToExcel(String taskId){
		List<Map> list = dao.getTaskDevResToExcel(taskId);
		if(null != list && list.size()>0){
			
			for(int i=0;i<list.size();i++){
				String bomtime = String.valueOf(list.get(i).get("bom_time"));
				String eomtime = String.valueOf(list.get(i).get("eom_time"));
				String downPert = String.valueOf(list.get(i).get("down_pert"));
				String totalDownPert =  String.valueOf(list.get(i).get("total_down_pert"));
				String receivebyte = String.valueOf(list.get(i).get("total_bytes_rece"));
				String tasknameTmp = String.valueOf(list.get(i).get("task_name"));
				
				if(bomtime.length()>10){
					bomtime = (bomtime.replace("T", " ")).substring(0,19);
					eomtime = (eomtime.replace("T", " ")).substring(0,19);
					list.get(i).put("bomtime", bomtime);
					list.get(i).put("eomtime", eomtime);
				}
				list.get(i).put("downPert", downPert);
				list.get(i).put("totalDownPert", totalDownPert);
				list.get(i).put("total_bytes_rece", receivebyte);
				list.get(i).put("task_name", tasknameTmp);
				
			}
			
			
		}
		return list;
	}
	
	
	public int queryDevBySerialnumberCount(String taskname, String serialnumber,String starttime,String endtime,int curPage_splitPage, int num_splitPage){
		return dao.queryDevBySerialnumberCount(taskname,serialnumber,starttime,endtime, curPage_splitPage, num_splitPage);
	}
	
	public String getExcelRow(String fileName){
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
		if((LipossGlobals.inArea(Global.JXDX) && rowCount > 25000)
				|| (!LipossGlobals.inArea(Global.JXDX) && rowCount > 10001)){
			msg="false";
			f.delete();
		}
		return msg;
		
	}
	
	private List<String> getImportDataByXLS(String fileName) throws IOException, BiffException{
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
			
//			if(null!=ws.getCell(0,0).getContents()){
//				String line = ws.getCell(0,0).getContents().trim();
//				
//				if(null!=line && "用户LOID".equals(line)){
//					this.importQueryField = "loid";
//				}else if(null!=line && "宽带账号".equals(line)){
//					this.importQueryField = "net_account";
//				}else if(null!=line && "ITV账号".equals(line)){
//					this.importQueryField = "itv_account";
//				}else if(null!=line && "语音账号".equals(line)){
//					this.importQueryField = "voip_account";
//				}
//			}
			
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
	
	private ArrayList<String> sqlList(List<Map> list,String name,String taskid){
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			String oui = String.valueOf(list.get(i).get("oui"));
			String username = String.valueOf(list.get(i).get("username"));
			String device_serialnumber = String.valueOf(list.get(i).get("device_serialnumber"));
			String device_id = String.valueOf(list.get(i).get("device_id"));
			StringBuffer sql = new StringBuffer();
			sql.append("insert into tab_ids_task_dev(task_name,task_id,device_id,oui,device_serialnumber,loid,statu)");
			sql.append(" values('"+name+"','"+taskid+"',"+device_id+",'"+oui+"','"+device_serialnumber+"','"+username+"',0)");
			sqlList.add(sql.toString());
		}
		return sqlList;
	}
	
	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		//获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径",lipossHome  );
		return lipossHome + "/temp/";
	}

	/**
	 * 下载速率
	 * @param TransportStartTime 开始时间
	 * @param TransportEndTime 结束时间
	 * @param ReceiveByte 字节数
	 * @return
	 */
	private  String getDownPert(String transportStartTime,
			String transportEndTime, String receiveByte) 
	{
		float ff = 0;
		String strtime = transportStartTime;
		String endtime = transportEndTime;
		if( !StringUtil.IsEmpty(strtime) && !StringUtil.IsEmpty(endtime))
		{
			BigDecimal strTime = new BigDecimal(strtime.split(":")[2].split("[.]")[0]).add(new BigDecimal(strtime.split(":")[2].split("[.]")[1]).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP));
			BigDecimal endTime = new BigDecimal(endtime.split(":")[2].split("[.]")[0]).add(new BigDecimal(endtime.split(":")[2].split("[.]")[1]).divide(new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP));
			BigDecimal receiveBytes = new BigDecimal(receiveByte).divide(new BigDecimal("1024") ,6,BigDecimal.ROUND_HALF_UP);//k
			
			BigDecimal mintue = (new BigDecimal(endtime.split(":")[1]).subtract(new BigDecimal(strtime.split(":")[1]))).multiply(new BigDecimal("60"));
			BigDecimal period = endTime.subtract(strTime).add(mintue);
			//k/s
			ff = receiveBytes.divide(period,6, BigDecimal.ROUND_HALF_UP).floatValue();
			DecimalFormat df = new DecimalFormat("#0.00");
			return StringUtil.getStringValue(df.format(ff));	
		}
		return StringUtil.getStringValue(ff);
	}
	
	public HTTPDownloadDAO getDao() {
		return dao;
	}

	public void setDao(HTTPDownloadDAO dao) {
		this.dao = dao;
	}
	
	public static void main(String[] args) {
		
		String s1 = "3|		  |00|		  ||		  ||		  ||		  |select a.device_id,a.devicetype_id,a.device_model_id,b.username from tab_gw_device a,tab_hgwcustomer b,tab_seniorquery_tmp f where a.device_status=1 and a.device_id=b.device_id  and a.device_serialnumber=f.devicesn and f.filename='1540536035307.txt' and a.gw_type=1 order by complete_time|79|linkage";
		String s2 = "3|		  |00|		  ||		  ||		  ||		  |select a.device_id,a.devicetype_id,a.device_model_id,b.username from tab_gw_device a,tab_hgwcustomer b,tab_seniorquery_tmp f where a.device_status=1 and a.device_id=b.device_id  and b.username=f.username and f.filename='1540536919294.txt' and a.gw_type=1 order by complete_time|88|linkage";
		String s3 = "2|		  |00|		  |0|2		  |21|-1		  |-1|		  |select a.device_id,a.device_model_id,a.devicetype_id,b.username from tab_gw_device a,tab_hgwcustomer b,gw_devicestatus e where a.device_status=1 and a.device_id=e.device_id and a.device_id=b.device_id  and (e.online_status=0) and a.vendor_id='2' and a.device_model_id='21' and a.gw_type=1|7067|linkage";
			
		String[] paramArr1 = s1.split("\\|");
		String[] paramArr2 = s2.split("\\|");
		String[] paramArr3 = s3.split("\\|");
		
		System.out.println("paramArr1[10]==="+paramArr1[10]);
		System.out.println("paramArr2[10]==="+paramArr2[10]);
		System.out.println("paramArr3[10]==="+paramArr3[10]);
			
	}

	public List<Map> querySingleHttpList(String httpType, String taskid, String taskname, String starttime, String endtime, int curPage_splitPage, int num_splitPage) {
		return dao.querySingleHttpList(httpType,taskid,taskname,starttime,endtime,curPage_splitPage,num_splitPage);
	}


	public int querySingleHttpListCount(String httpType, String taskid, String taskname, String starttime, String endtime, int curPage_splitPage, int num_splitPage) {
		return dao.querySingleHttpListCount(httpType,taskid,taskname,starttime,endtime,curPage_splitPage,num_splitPage);
	}

	public List<Map> querySingleHttpExcel(String httpType, String taskid, String taskname, String starttime, String endtime) {
		return dao.querySingleHttpExcel(httpType,taskid,taskname,starttime,endtime);
	}
}
