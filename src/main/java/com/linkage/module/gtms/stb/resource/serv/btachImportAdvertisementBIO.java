package com.linkage.module.gtms.stb.resource.serv;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.ftp.FtpClient;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.resource.dao.btachImportAdvertisementDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.StringUtil;


/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-2-12
 * @category com.linkage.module.gtms.stb.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class btachImportAdvertisementBIO
{
	private static Logger logger = LoggerFactory
			.getLogger(btachImportAdvertisementBIO.class);
	private btachImportAdvertisementDAO dao;
	private String remoteAbsoluteUrl = ""; // 图片存放的目录
	private String remoteAbsoluteIP = "";  // 图片服务器的IP地址
	private String port ="";  //图片服务器的端口
	public int insert(List<Map<String,String>> dataList,String group_id,long taskId)
	{
		return dao.insertServAccount(dataList, group_id,taskId);
	}
	/**
	 * 查询所有分组
	 * @return
	 */
	public String getGroupId()
	{
		List list=dao.getGroupId();
		StringBuffer bf=new StringBuffer();
		for(int i=0;i<list.size();i++)
		{
			Map map=(Map)list.get(i);
			if(i>0)
				bf.append("#");
			bf.append(map.get("group_id"));
			bf.append("$");
			bf.append(map.get("group_name"));
		}
		return bf.toString();
	}
	
	public String OpenDeviceShowPicConfig(long taskId,String taskName,long acc_oid,File bootFile, File startFile,
			File authFile, String bootFileName, String startFileName,String gwShare_fileName,
			String authFileName,String groupids, String filePath,String priority ,long add_time,String Invalid_time) {
		logger.warn("btachImportAdvertisementBIO(booFile={},startFile={},authFile={},bootFileName={},startFileName={},authFileName={})",
	    		new Object[]{bootFile,startFile,authFile,bootFileName,startFileName,authFileName});
		 
		    String bootFilePath  = "" ;
		    String startFilePath = "" ;
		    String authFilePath  = "" ;
		    
			String upLoadServerIP   = "" ;    // FTP服务器的IP地址
			String upLoadServerUser = "" ;   // 上传服务器用户名
			String upLoadServerPwd  = "" ;    // 上传服务器密码
			
			String bootFilePath1 = "";
			String startFilePath1 = "";
			String authFilePath1 = "";
			//开机
			String starturl="";
			//启动
			String booturl="";
			//认证
			String authturl="";
			DateTimeUtil dt = new DateTimeUtil();
			String path_accountFile_web=LipossGlobals.getLipossProperty("attachmentsDir.accountFile");
			//开机路径
			String path_start_web=LipossGlobals.getLipossProperty("attachmentsDir.start");
			//启动路径
			String path_boot_web=LipossGlobals.getLipossProperty("attachmentsDir.boot");
			//认证路径
			String path_auth_web=LipossGlobals.getLipossProperty("attachmentsDir.auth");
			//需要ftp的其他web ip
			String ip_otherweb=LipossGlobals.getLipossProperty("attachmentsDir.otherweb");
			//需要ftp的其他web 的账号密码
			String name_ftp_otherweb=LipossGlobals.getLipossProperty("nodeftp.username");
			String pwd_ftp_otherweb=LipossGlobals.getLipossProperty("nodeftp.password");
			
			// 获取上传服务器，下载服务器相关参数
			List<Map<String, String>> serverMap = dao.getServerParameter();
			String url="";
			int ier=0;
			String timeString = dt.getYYYYMMDDHHMMSS();
			for(int i=0;i<serverMap.size();i++)
			{
				String ftpRemoteIp = LipossGlobals.getLipossProperty("attachmentsDir.ftpremoteip") ;
				// 图片服务器相关信息
				String server_url = serverMap.get(i).get("server_url");
				upLoadServerUser = serverMap.get(i).get("access_user");
				upLoadServerPwd = serverMap.get(i).get("access_passwd");
				if (null == server_url || "".equals(server_url) || 
						null == upLoadServerUser || "".equals(upLoadServerUser) || 
						null == upLoadServerPwd || "".equals(upLoadServerPwd)) {
						logger.warn("图片服务器维护不正确！");
						return "图片服务器维护不正确，请确认！";
					}
				String [] arrayStr = serverMap.get(i).get("server_url").split("/");
				
				upLoadServerIP = arrayStr[2].split(":")[0];
				if(StringUtil.IsEmpty(ftpRemoteIp)){
					ftpRemoteIp = upLoadServerIP;
				}
				port = arrayStr[2].split(":")[1];
				// 拼装图片服务器上的图片存放目录
				remoteAbsoluteUrl = "/"+arrayStr[3]+"/"+arrayStr[4]+"/"+arrayStr[5]+"/"+arrayStr[6];
				remoteAbsoluteIP = "http://"+upLoadServerIP;
				//启动图片
				if (null != bootFile && bootFile.isFile() && bootFile.length()>0) {
					logger.warn("bootFile");
					
					String result = checkPicture("bootFile", bootFile, bootFileName, timeString);
					
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
					if(Global.CQDX.equals(Global.instAreaShortName)){
					if(i==0){
						String newFileName = timeString + "_" + bootFileName;
						String ftpWEBResult = picUploadToOtherWEB(ip_otherweb, name_ftp_otherweb, pwd_ftp_otherweb, path_boot_web+newFileName);
						if ("1".equals(ftpWEBResult)) {
							logger.warn("将开机图片FTP至web服务器失败，可能会影响审核时预览图片.");
						}
					}
					}
					//booturl="/"+arrayStr[3]+"/"+arrayStr[4]+"/"+arrayStr[5]+"/"+arrayStr[6]+"/"+bootFileName+"_"+dt.getYYYYMMDDHHMMSS();
				}
				if (null != startFile && startFile.isFile() && startFile.length()>0) {
					logger.warn("startFile");
					
					String result = checkPicture("startFile", startFile, startFileName, timeString);
					
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
					
					if(Global.CQDX.equals(Global.instAreaShortName)){
						
					if(i==0){
						String newFileName = timeString + "_" + startFileName;
						String ftpWEBResult = picUploadToOtherWEB(ip_otherweb, name_ftp_otherweb, pwd_ftp_otherweb, path_start_web+newFileName);
						if ("1".equals(ftpWEBResult)) {
							logger.warn("将开机图片FTP至web服务器失败，可能会影响审核时预览图片.");
						}
					}
					}
					//starturl="/"+arrayStr[3]+"/"+arrayStr[4]+"/"+arrayStr[5]+"/"+arrayStr[6]+"/"+startFileName+"_"+dt.getYYYYMMDDHHMMSS();

				}
				
				//认证如图片上传
				if (null != authFile && authFile.isFile() && authFile.length()>0) {
					
					logger.warn("authFile");
					String result = checkPicture("authFile", authFile, authFileName, timeString);
					
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
					if(Global.CQDX.equals(Global.instAreaShortName)){
					if(i==0){
						String newFileName = timeString + "_" + authFileName;
						String ftpWEBResult = picUploadToOtherWEB(ip_otherweb, name_ftp_otherweb, pwd_ftp_otherweb, path_auth_web+newFileName);
						if ("1".equals(ftpWEBResult)) {
							logger.warn("将开机图片FTP至web服务器失败，可能会影响审核时预览图片.");
						}
					}
					}
					//authturl="/"+arrayStr[3]+"/"+arrayStr[4]+"/"+arrayStr[5]+"/"+arrayStr[6]+"/"+authFileName+"_"+dt.getYYYYMMDDHHMMSS();
				}
				
				//url = remoteAbsoluteIP+":"+port+remoteAbsoluteUrl+"/";
				
			}
			
			
			// 文件服务器下发地址
			if("1".equals(LipossGlobals.getLipossProperty("fileServer.enable"))){
				url = LipossGlobals.getLipossProperty("fileServer.path") + remoteAbsoluteUrl+"/";
			}else{
				url = remoteAbsoluteIP+":"+port+remoteAbsoluteUrl+"/";
			}
			
			File uploadCustomer=new File(getFilePath()+ gwShare_fileName);
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
			        targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS() +"_"+ gwShare_fileName;
			        File target = new File(targetDirectory, targetFileName);
					FileUtils.copyFile(uploadCustomer, target);
					if(Global.CQDX.equals(Global.instAreaShortName)){
						String ftpWEBResult = picUploadToOtherWEB(ip_otherweb, name_ftp_otherweb, pwd_ftp_otherweb, path_accountFile_web+targetFileName);
						if ("1".equals(ftpWEBResult)) {
							logger.warn("将导入业务账号文件FTP至web服务器失败，可能会影响审核时预览图片.");
						}
					}
				} catch (IOException e) {
					logger.error("批量导入升级，上传文件时出错");
				}
		       // filePath = "http://"+request.getLocalAddr()+":"+request.getServerPort()+"/lims"+filePath +"/"+targetFileName;
		        filePath = "http://" + request.getLocalAddr() + ":" + 
		                request.getServerPort() + request.getContextPath() + filePath + "/" + 
		                targetFileName;
			}
			
			ier = dao.OpenDeviceShowPicConfig(taskId, taskName, groupids, filePath, bootFilePath1, startFilePath1, authFilePath1, acc_oid, priority, add_time, Invalid_time,url);
			if (ier > 0)
			{
				return "任务定制成功!";
			}
			else
			{
				return "任务定制失败！";
			}
	}
	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath()
	{
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try
		{
			lipossHome = java.net.URLDecoder.decode(
					a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
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
		String fileName = localFileUrl.split("/")[3];
		String remoteAbsoluteFile = ftpPath+remoteAbsoluteUrl +"/" +fileName;
		
		
		try {
			
			boolean result = false;
			
			FtpClient ftpClient = new FtpClient(ip, user, pwd);
			boolean connectResult = ftpClient.connect();  // 连接FTP服务器
			
			if(connectResult){
				/** remoteAbsoluteFile 远程路径   localAbsoluteFile 本地路劲*/
				logger.warn(localFileUrl+"从"+localAbsoluteFile+"ftp至"+ip+"的"+remoteAbsoluteFile);
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
	 * 将WEB服务器上的图片FTP至另一个web，若失败，不影响定制，审核预览图片可能会失败(有一半概率)<br>
	 * 
	 * @param ip
	 * @param user
	 * @param pwd
	 * @param localFileUrl
	 * @return
	 * 0 表示上传成功<br>
	 * 1 表示上传失败<br>
	 */
	public String picUploadToOtherWEB(String ip, String user, String pwd, String localFileUrl){
		logger.debug("==>picExcelUploadToOtherWEB()");
		String localAbsoluteFile = LipossGlobals.getLipossHome()+localFileUrl;
		String remoteAbsoluteFile = localAbsoluteFile;
		
		
		try {
			
			boolean result = false;
			FtpClient ftpClient = new FtpClient(ip, user, pwd);
			boolean connectResult = ftpClient.connect();  // 连接ftp服务器
			logger.warn("connectResult="+connectResult);
			if(connectResult){
				/** remoteAbsoluteFile 远程路径   localAbsoluteFile 本地路劲*/
				logger.warn("将"+localFileUrl+"从本地"+localAbsoluteFile+"上传至"+ip+"的"+remoteAbsoluteFile);
				result = ftpClient.put(remoteAbsoluteFile, localAbsoluteFile, false);
			}
			
			if (!result) {
				logger.warn("文件\""+localFileUrl+"\"FTP至另一个web服务器失败！");
				return "1";
			}else {
				logger.warn("文件\""+localFileUrl+"\"FTP至另一个web服务器成功！");
				return "0";
			}
		} catch (Exception e) {
			logger.warn("FTP至另一个web服务器时异常，msg=({})", e.getMessage());
			return "1";
		}
	}
	
	public Map<String, String> getDeviceInfo(String param)
	{
		return dao.getDeviceInfo(param);
	}
	public btachImportAdvertisementDAO getDao()
	{
		return dao;
	}
	
	public void setDao(btachImportAdvertisementDAO dao)
	{
		this.dao = dao;
	}
	
	public String getRemoteAbsoluteUrl()
	{
		return remoteAbsoluteUrl;
	}
	
	public void setRemoteAbsoluteUrl(String remoteAbsoluteUrl)
	{
		this.remoteAbsoluteUrl = remoteAbsoluteUrl;
	}
	
	public String getRemoteAbsoluteIP()
	{
		return remoteAbsoluteIP;
	}
	
	public void setRemoteAbsoluteIP(String remoteAbsoluteIP)
	{
		this.remoteAbsoluteIP = remoteAbsoluteIP;
	}
	
	public String getPort()
	{
		return port;
	}
	
	public void setPort(String port)
	{
		this.port = port;
	}
	
	
}
