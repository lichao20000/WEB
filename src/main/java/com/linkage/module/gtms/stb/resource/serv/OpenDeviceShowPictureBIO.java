package com.linkage.module.gtms.stb.resource.serv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.ftp.FtpClient;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.resource.dao.OpenDeviceShowPictureDAO;
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class OpenDeviceShowPictureBIO {
	private static Logger logger = LoggerFactory
			.getLogger(OpenDeviceShowPictureBIO.class);

	private OpenDeviceShowPictureDAO dao;
	
	private String remoteAbsoluteUrl = ""; // 图片存放的目录
	private String remoteAbsoluteIP = "";  // 图片服务器的IP地址
	private String port ="";  //图片服务器的端口
	public List getVendorList() {
		logger.debug("dao.getVendor({})"+dao.getVendor());
		return dao.getVendor();
	}
	/**
	 * 增加任务
	 * @param taskId
	 * @param cityId
	 * @param vendorId
	 * @param ipCheck
	 * @param macCheck
	 * @param taskName
	 * @param acc_oid
	 * @param addTime
	 * @param ipSG
	 * @param macSG
	 * @param deviceModelIds
	 * @param deviceTypeIds
	 * @param custCheck
	 * @param custSG
	 * @param uploadIP
	 * @param uploadFileName4IP
	 * @param uploadMAC
	 * @param uploadFileName4MAC
	 * @param uploadCustomer
	 * @param uploadFileName4Customer
	 * @param bootFile
	 * @param startFile
	 * @param authFile
	 * @param bootFileName
	 * @param startFileName
	 * @param authFileName
	 * @return
	 */
	public String OpenDeviceShowPicConfig(long taskId, String cityId,
			String vendorId, String ipCheck, String macCheck, String taskName,
			long acc_oid, long addTime, String ipSG, String macSG,
			String deviceModelIds, String deviceTypeIds, String custCheck,
			String custSG, File uploadIP, String uploadFileName4IP,
			File uploadMAC, String uploadFileName4MAC, File uploadCustomer,
			String uploadFileName4Customer, File bootFile, File startFile,
			File authFile, String bootFileName, String startFileName,
			String authFileName, String isLocked) {
		
		    logger.warn("OpenDeviceShowPicConfig(booFile={},startFile={},authFile={},bootFileName={},startFileName={},authFileName={},isLocked={})",
		    		new Object[]{bootFile,startFile,authFile,bootFileName,startFileName,authFileName,isLocked});
		    String ftpRemoteIp = LipossGlobals.getLipossProperty("attachmentsDir.ftpremoteip") ;
		    String bootFilePath  = "" ;
		    String startFilePath = "" ;
		    String authFilePath  = "" ;
		    
			String upLoadServerIP   = "" ;    // FTP服务器的IP地址
			String upLoadServerUser = "" ;   // 上传服务器用户名
			String upLoadServerPwd  = "" ;    // 上传服务器密码
			
			String bootFilePath1 = "";
			String startFilePath1 = "";
			String authFilePath1 = "";
			
			DateTimeUtil dt = new DateTimeUtil();
		// 获取上传服务器，下载服务器相关参数
			Map<String, String> serverMap = dao.getServerParameter();
			
			// 图片服务器相关信息
			String server_url = serverMap.get("server_url");
			upLoadServerUser = serverMap.get("access_user");
			upLoadServerPwd = serverMap.get("access_passwd");
			
			if (null == server_url || "".equals(server_url) || 
				null == upLoadServerUser || "".equals(upLoadServerUser) || 
				null == upLoadServerPwd || "".equals(upLoadServerPwd)) {
				logger.warn("图片服务器维护不正确！");
				return "图片服务器维护不正确，请确认！";
			}
			String [] arrayStr = serverMap.get("server_url").split("/");
			
			upLoadServerIP = arrayStr[2].split(":")[0];
			if(StringUtil.IsEmpty(ftpRemoteIp)){
				ftpRemoteIp = upLoadServerIP;
			}
			port = arrayStr[2].split(":")[1];
			
			// 拼装图片服务器上的图片存放目录
			remoteAbsoluteUrl = "/"+arrayStr[3]+"/"+arrayStr[4]+"/"+arrayStr[5];
			remoteAbsoluteIP = "http://"+upLoadServerIP;
			
			//启动图片
			if (null != bootFile && bootFile.isFile() && bootFile.length()>0) {
				logger.warn("bootFile");
				
				String result = checkPicture("bootFile", bootFile, bootFileName, dt.getYYYYMMDDHHMMSS());
				
				if (result.charAt(0) == '0') {
					logger.warn("开机图片上传至WEB服务器成功");
					bootFilePath = result.substring(2);
					bootFilePath1 = bootFilePath.split("/")[3];
				}else if(result.charAt(0) == '1'){
					logger.warn("开机图片不合格，任务定制失败");
					return "开机图片不合格，任务定制失败";
				}else if(result.charAt(0) == '2'){
					logger.warn("开机图片上传失败，任务定制失败");
					return "开机图片上传失败，任务定制失败";
				}else if(result.charAt(0) == '3'){
					logger.warn("任务定制失败");
					return "任务定制失败";
				}
				
				// 将刚刚上传至WEB服务器上的图片FTP到图片服务器
				String upLoadResult = picUploadToFTPServer(ftpRemoteIp, upLoadServerUser, upLoadServerPwd, bootFilePath);
				
				if ("1".equals(upLoadResult)) {
					logger.warn("将开机图片FTP至图片服务器失败，任务定制失败");
					return "将开机图片FTP至图片服务器失败，任务定制失败";
				}
			}
			if (null != startFile && startFile.isFile() && startFile.length()>0) {
				logger.warn("startFile");
				
				String result = checkPicture("startFile", startFile, startFileName, dt.getYYYYMMDDHHMMSS());
				
				if (result.charAt(0) == '0') {
					logger.warn("启动图片上传至WEB服务器成功");
					startFilePath = result.substring(2);
					startFilePath1 = startFilePath.split("/")[3];
				}else if(result.charAt(0) == '1'){
					logger.warn("启动图片不合格，任务定制失败");
					return "启动图片不合格，任务定制失败";
				}else if(result.charAt(0) == '2'){
					logger.warn("启动图片上传失败，任务定制失败");
					return "启动图片上传失败，任务定制失败";
				}else if(result.charAt(0) == '3'){
					logger.warn("任务定制失败");
					return "任务定制失败";
				}
				
				// 将刚刚上传至WEB服务器上的图片FTP到图片服务器
				String upLoadResult = picUploadToFTPServer(ftpRemoteIp, upLoadServerUser, upLoadServerPwd, startFilePath);
				
				if ("1".equals(upLoadResult)) {
					logger.warn("将启动图片FTP至图片服务器失败，任务定制失败");
					return "将启动图片FTP至图片服务器失败，任务定制失败";
				}
			}
			//认证如图片上传
			if (null != authFile && authFile.isFile() && authFile.length()>0) {
				
				logger.warn("authFile");
				String result = checkPicture("authFile", authFile, authFileName, dt.getYYYYMMDDHHMMSS());
				
				if (result.charAt(0) == '0') {
					logger.warn("认证图片上传至WEB服务器成功");
					authFilePath = result.substring(2);
					authFilePath1 = authFilePath.split("/")[3];
				}else if(result.charAt(0) == '1'){
					logger.warn("认证图片不合格，任务定制失败");
					return "认证图片不合格，任务定制失败";
				}else if(result.charAt(0) == '2'){
					logger.warn("认证图片上传失败，任务定制失败");
					return "认证图片上传失败，任务定制失败";
				}else if(result.charAt(0) == '3'){
					logger.warn("任务定制失败");
					return "任务定制失败";
				}
				
				// 将刚刚上传至WEB服务器上的图片FTP到图片服务器
				String upLoadResult = picUploadToFTPServer(ftpRemoteIp, upLoadServerUser, upLoadServerPwd, authFilePath);
				if ("1".equals(upLoadResult)) {
					logger.warn("将标清启动图片FTP至图片服务器失败，任务定制失败");
					return "将标清启动图片FTP至图片服务器失败，任务定制失败";
				}
			}
			
			String url = remoteAbsoluteIP+":"+port+remoteAbsoluteUrl+"/";
			String filePath="";
			if (null != uploadCustomer && uploadCustomer.isFile() && uploadCustomer.length()>0) {
				//如果是导入帐号，将导入帐号的文件放到指定的目录底下
				 String targetDirectory="";
				 filePath = "/accountFile";
				 String targetFileName ="";
				 HttpServletRequest request = null;
				 try {
				 //将文件存放到指定的路径中
				 request = ServletActionContext.getRequest();
				 targetDirectory = ServletActionContext.getServletContext().getRealPath(filePath);
				 //由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
		         targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS() +"_"+ uploadFileName4Customer;
		         File target = new File(targetDirectory, targetFileName);
				 FileUtils.copyFile(uploadCustomer, target);
				} catch (IOException e) {
					logger.error("批量导入升级，上传文件时出错");
				}
		        filePath = "http://"+request.getLocalAddr()+":"+request.getServerPort()+"/lims"+filePath +"/"+targetFileName;
				}
			int ier = dao.OpenDeviceShowPicConfig(taskId, cityId, vendorId, ipCheck,
				macCheck, taskName, acc_oid, addTime, ipSG, macSG,
				deviceModelIds, deviceTypeIds, custCheck, custSG,
				bootFilePath1,startFilePath1,authFilePath1,filePath,url,isLocked);
		 if (ier == 1)
		{
			return "任务定制成功!";
		}
		else
		{
			return "任务定制失败！";
		}
	}
	
	/**
	 * 检查将要上传的图片是否合格， 将合格的图片上传至WEB服务器<br>
	 * 
	 * @return
	 * 0 表示上传图片至WEB服务器成功<br>
	 * 1 表示上传的图片属性不合格
	 * 2 表示上传图片至WEB服务器失败<br>
	 * 3 表示分析图片分辨率及大小时异常<br>
	 */
	public String checkPicture(String picAttribute, File sourceFile, String fileName, String timeString) {
		
		logger.debug("checkPicture()");
		try {
			
			// 将符合条件的图片上传至WEB服务器
			String retrunMsg = upLoadPicture(picAttribute, fileName, sourceFile, timeString);
			if (retrunMsg.startsWith("1")) {
				return "2;上传图片失败!";
			}else {
				return retrunMsg;
			}
		} catch (Exception e) {
			return "3;分析图片属性时异常，任务定制失败！";
		}
	}
	/**
	 * 将合格的图片上传至服务器指定的目录
	 * 
	 * @param pictureFilePath
	 * @param picAttribute
	 * @return
	 */
	public String upLoadPicture(String picAttribute, String fileName, File sourceFile, String timeString) {
		
		logger.warn("upLoadPicture(picAttribute={},fileName={})",picAttribute,fileName);
		
		String returnMsg = "";   // 回参
		
		String newFileUrl = "";  // 图片在WEB服务器上的URL
		
		try {
			// 新文件名
			String newFileName = timeString + "_" + fileName;
			
	        // 将符合条件的图片上传至WEB服务器指定的目录,然后再从WEB服务器FTP到指定文件服务器的指定目录
	        // LipossGlobals.getLipossHome() : /export/home/data2/web/lims
	        
	        // 获取WEB服务器上的图片文件路径，此路径将会被存到数据库中，用于图片的展示
	        if("bootFile".equals(picAttribute)){
	        	newFileUrl = LipossGlobals.getLipossProperty("attachmentsDir.boot");
	        }else if("startFile".equals(picAttribute)){
	        	newFileUrl = LipossGlobals.getLipossProperty("attachmentsDir.start");
	        }else if("authFile".equals(picAttribute)){
	        	newFileUrl = LipossGlobals.getLipossProperty("attachmentsDir.auth");
	        }
	        
	        File target = new File(LipossGlobals.getLipossHome()+newFileUrl, newFileName);
	        FileUtils.copyFile(sourceFile, target);  // 实现文件的复制功能
	        
	        returnMsg = "0;"+newFileUrl+newFileName;
	        
		} catch (Exception e) {
			logger.warn("上传图片失败!");
			logger.error("上传图片出错，msg:({})", e.getMessage());
			returnMsg = "1;上传图片出错";
		}
		
		return returnMsg;
	}
	/**
	 * 将WEB服务器上的图片FTP至图片服务器，如果FTP失败，则定制任务失败，需要重新定制<br>
	 * 
	 * @param ip
	 * @param user
	 * @param pwd
	 * @param localFileUrl
	 * @return
	 * 0 表示上传成功<br>
	 * 1 表示上传失败<br>
	 */
	public String picUploadToFTPServer(String ip, String user, String pwd, String localFileUrl){
		
		logger.debug("==>picUploadToFTPServer()");
		
		String ftpPath = LipossGlobals.getLipossProperty("attachmentsDir.ftp");
		String localAbsoluteFile = LipossGlobals.getLipossHome()+localFileUrl;
//		String localFileUrlTemp = "";
		
//		try {
//			/**
//			 * 在测试时，是往江苏141测试机上FTP，发现上传后中文文件名出现乱码，
//			 * 所以使用下面的方法进行了转码
//			 */
//			localFileUrlTemp = new String(localFileUrl.getBytes("GBK"), "iso-8859-1");
//		} catch (Exception e) {
//			logger.warn("字符串截取异常");
//		}
		
		// 远程图片服务器的URL  remoteAbsoluteUrl =/FileServer/SOFT/KJHM/20140701152015_示例图片_01.jpg
		///export/home/process/lims/picture/boot/20140701152015_示例图片_01.jpg
		//String remoteAbsoluteFile = remoteAbsoluteUrl + localFileUrlTemp;
		String fileName = localFileUrl.split("/")[3];
		String remoteAbsoluteFile = ftpPath+remoteAbsoluteUrl +"/" +fileName;
		
		
		try {
			
			boolean result = false;
			
			FtpClient ftpClient = new FtpClient(ip, user, pwd);
			boolean connectResult = ftpClient.connect();  // 连接FTP服务器
			
			if(connectResult){
				/** remoteAbsoluteFile 远程路径   localAbsoluteFile 本地路劲*/
				result = ftpClient.put(remoteAbsoluteFile, localAbsoluteFile, false);
			}
			
			if (!result) {
				logger.warn("文件\""+localFileUrl+"\"FTP至图片服务器失败！");
				return "1";
			}else {
				logger.warn("文件\""+localFileUrl+"\"FTP至图片服务器成功！");
				return "0";
			}
		} catch (Exception e) {
			logger.warn("FTP图片文件时异常，msg=({})", e.getMessage());
			return "1";
		}
	}
	/**
	 * 
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel(String vendorId) {
		List list = dao.getDeviceModel(vendorId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0)
				bf.append("#");
			bf.append(map.get("device_model_id"));
			bf.append("$");
			bf.append(map.get("device_model"));
		}

		return bf.toString();
	}
	
	
	/**
	 * 
	 * @param cityId
	 * @return
	 */
	public String getNextCity(String cityId) {
		List list = CityDAO.getAllNextCityListByCityPidCore(cityId);
		
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0)
				bf.append("#");
			bf.append(map.get("city_id"));
			bf.append("$");
			bf.append(map.get("city_name"));
		}

		return bf.toString();
	}

	public String getSoftVersion(String deviceModelId) {
		List list = dao.getVersionList(deviceModelId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0)
				bf.append("#");
			bf.append(map.get("devicetype_id"));
			bf.append("$");
			bf.append(map.get("softwareversion"));
		}

		return bf.toString();
	}

	public String getIPString(String targetDirectory, String uploadFileName4IP,
			File uploadIP) {
		String reString = "";
		try {
			String targetFileName = new Date().getTime() + uploadFileName4IP;
			File target = new File(targetDirectory, targetFileName);
			FileUtils.copyFile(uploadIP, target);
			if (null != target && target.exists()) {
				// 创建工作表
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
						target));
				// 获得sheet
				HSSFSheet sheet = workbook.getSheetAt(0);
				// 获得sheet的总行数
				int rowCount = sheet.getLastRowNum();
				// 循环解析每一行，第一行不取
				for (int i = 1; i <= rowCount; i++) {
					// 获得行对象
					HSSFRow row = sheet.getRow(i);

					HSSFCell cell_start = row.getCell(0);
					String ip_start = getCellString(cell_start);

					HSSFCell cell_end = row.getCell(1);
					String ip_end = getCellString(cell_end);
					if (null != ip_start && null != ip_end) {
						reString += ip_start.trim() + "," + ip_end.trim() + ";";
					}
				}
				if (reString != "") {
					reString = reString.substring(0, reString.length() - 1);
				}
			} else {
				logger.warn("复制文件失败");
			}
		} catch (Exception e) {
			logger.warn("批量导入时异常");
		}
		return reString;
	}

	private String getCellString(HSSFCell cell) {
		// TODO Auto-generated method stub
		String result = null;
		if (cell != null) {
			// 单元格类型：Numeric:0,String:1,Formula:2,Blank:3,Boolean:4,Error:5
			int cellType = cell.getCellType();
			switch (cellType) {
			case HSSFCell.CELL_TYPE_STRING:
				result = cell.getRichStringCellValue().getString();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				result = cell.getNumericCellValue() + "";
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				result = cell.getCellFormula();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				break;
			default:
				break;
			}
		}
		return result;
	}

	public void setDao(OpenDeviceShowPictureDAO dao) {
		this.dao = dao;
	}
	public List getOrderTaskList(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName,String isLocked) {
		
		return dao.getOrderTaskList(curPage_splitPage, num_splitPage,
				 startTime, endTime, cityId, taskName,acc_oid,accName,isLocked);
	}
	public int countOrderTask(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName,String isLocked) {
		// TODO Auto-generated method stub
		return dao.countOrderTask(curPage_splitPage,
				num_splitPage,startTime, endTime, cityId, taskName,acc_oid,accName,isLocked);
	}
	public int countDeviceTask(int curPage_splitPage, int num_splitPage,String taskId) {
		return dao.countTaskResult(curPage_splitPage, num_splitPage, taskId);
	}
	public List getTaskResult(String taskId ,int curPage_splitPage,int num_splitPage) {
		return dao.getTaskResult(taskId,curPage_splitPage, num_splitPage);
	}
	public Map<String, String> getTaskDetail(String taskId) {
		
		Map<String,String> taskMap = dao.getTaskDetail(taskId);
		List verList = dao.getVerList(taskId);
		StringBuffer venBuf = new StringBuffer();
		StringBuffer modelBuf = new StringBuffer();
		StringBuffer typeBuf = new StringBuffer();
		List<String> venList = new ArrayList<String>();
		List<String> venListNew = new ArrayList<String>();
		String ven = "";
		String model = "";
		String type = "";
		Set<String> tempDeviceModelSet = new HashSet<String>();
		for (int i = 0; i < verList.size(); i++)
		{
			Map tempMap = (Map) verList.get(i);
//			ven += DeviceTypeUtil.getVendorName(StringUtil.getStringValue(tempMap
//					.get("vendor_id")))+"--";
			venList.add(DeviceTypeUtil.getVendorName(StringUtil.getStringValue(tempMap
					.get("vendor_id"))));
			model = DeviceTypeUtil.getDeviceModel(StringUtil.getStringValue(tempMap
					.get("device_model_id")));
			tempDeviceModelSet.add(model);
			type = DeviceTypeUtil.getDeviceSoftVersion(StringUtil.getStringValue(tempMap
					.get("devicetype_id")));
			typeBuf.append(type + "   ");
		}
		for (String str : tempDeviceModelSet)
		{
			modelBuf.append(str + "   ");
		}
		
		for(String v:venList){  
	        if(!venListNew.contains(v)){  
	        	venListNew.add(v);  
	        	ven += v + "   ";
	        }  
	    }  
		
		taskMap.put("vendorName", ven);
		taskMap.put("deviceModelName", modelBuf.toString());
		taskMap.put("deviceTypeName", typeBuf.toString());
		
		//    http://192.168.4.73:7070/lims/accountFile/20140710180642_account.xls
		String webAddress = LipossGlobals.getLipossProperty("attachmentsDir.web");
		
		String file_path = taskMap.get("file_path");
		String[] arrayStr = new String[6];
		if(!StringUtil.IsEmpty(file_path)){
			arrayStr = file_path.split("/");
		}
		
		if(!StringUtil.IsEmpty(file_path)){
			 file_path  = "http://"+webAddress+arrayStr[3]+"/"+arrayStr[4]+"/"+arrayStr[5];
			 taskMap.put("file_path",file_path);
		}else{
			taskMap.put("file_path","否");
		}
		return taskMap;
	}
	public List getIpList(String taskId) {
		// TODO Auto-generated method stub
		return dao.getIpList(taskId);
	}
	public List getMacList(String taskId) {
		// TODO Auto-generated method stub
		return dao.getMacList(taskId);
	}
	public List exportShowDeviceExcelList(String taskId) {
		// TODO Auto-generated method stub
		return dao.exportShowDeviceExcelList(taskId);
	}
	public String doDelete(String taskId) {
		// TODO Auto-generated method stub
		return dao.doDelete(taskId);
	}

}
