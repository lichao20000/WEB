package com.linkage.module.itms.resource.act;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.FtpUtil;
import com.linkage.module.itms.resource.bio.DeviceTestAccountBIO;

public class DeviceTestAccountACT extends splitPageAction implements SessionAware,ServletRequestAware,ServletResponseAware {
	private static Logger logger = LoggerFactory.getLogger(DeviceTestAccountACT.class);
	private DeviceTestAccountBIO bio;
	private Map session;
	private String ajax = null;
	private String gwShare_deviceModelId = null;
	
	private String gwShare_hardwareVersion = null;
	private String gwShare_fileName = "";
	private List<Map<String,String>> testAccountList;
	private String gwShare_devicetypeId = null;
	private String vendorId = "";
	private String device_serialnumber = "";
	private String deviceModelId = "";
	private String goal_devicetypeId = "";
	private static final long serialVersionUID = 572146812454l;
	private static final int BUFFER_SIZE = 16 * 1024 * 1024;
	private File myFile;
	private File myffFile;
	private String contentType;
	private String fileName;
	private String myFileFileName;
	private String imageFileName;
	private HttpServletRequest request;
	/** HttpServletResponse */
	private HttpServletResponse response;
	/** 注册时间 */
	private String starttime = null;
	
	private String starttime1 = null;
	
	/** 注册时间 */
	private String endtime = null;
	
	private String endtime1 = null;
	
	public String getMyFileFileName() {
		return myFileFileName;
	}

	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}

	/**
	 * 查询设备硬件版本
	 * 
	 * @return
	 */
	public String getDeviceHardVersion() {
		logger.debug("GwDeviceQueryACT=>getDeviceHardVersion()");
		this.ajax = bio.getDeviceHardVersion(gwShare_deviceModelId);
		return "ajax";
	}
	
	/**
	 * 获取软件/目标版本信息
	 * @return
	 */
	public String getSoftVersion(){
		ajax=bio.getSoftVersion(gwShare_hardwareVersion,gwShare_deviceModelId);
		return "ajax";
	}
	/**
	 * 时间转化
	 */
	private void setTime(){
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime)){
			starttime1 = null;
		}else{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime)){
			endtime1 = null;
		}else{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}
	public String getList(){
		this.setTime();
		testAccountList = bio.getList(vendorId,deviceModelId,goal_devicetypeId,starttime1,endtime1, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getListCount(vendorId,deviceModelId,goal_devicetypeId,starttime1,endtime1, curPage_splitPage, num_splitPage);
		return "testAccountList";
	}
	
	public String addTestAccountPath(){
		 fileupAccount();
		UserRes curUser = (UserRes) session.get("curUser");
		ajax = bio.addTestAccountPath(device_serialnumber,vendorId,deviceModelId,goal_devicetypeId,gwShare_fileName,curUser.getUser().getId());
		return "ajax";
	}
	/**
	 * 下载模板文件
	 * 
	 * @return
	 */
	public String download()
	{
			// 文件路径
		try {
			String a = FileUploadAction.class.getResource("/").getPath();
			//String lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
			String lipossHome ="/export/home/itms/WEB_GTMS";
			//File imageFile = new File(lipossHome + "/deviceTestAccount/" + imageFileName);
			//String ftpfilepath = LipossGlobals.getLipossProperty("TestDeviceAccFTP.FILEPATH");
				// lipossHome + "/deviceTestAccount/" +fileName;
			bio.download(lipossHome + "/deviceTestAccount/" +fileName,response);
		} catch (Exception e) {
			logger.error("下载文件错误:{}",ExceptionUtils.getStackTrace(e));
		}
		return null;
			
	}
		// 流拷贝，要保证目标文件的父文件夹存在，否则抛出FileNotFoundException
		private static int copy(File src, File dst) {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = new BufferedInputStream(new FileInputStream(src));
					out = new BufferedOutputStream(new FileOutputStream(dst));
					byte[] buffer = new byte[in.available()];
					while (in.read(buffer) > 0) {
						out.write(buffer);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return 2;
				}finally {
					if (null != in) {
						try {
							in.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (null != out) {
						try {
							out.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			return 1;
		}

		private static String getExtention(String fileName) {
			int pos = fileName.lastIndexOf(".");
			if(pos>-1){
				return fileName.substring(pos);
			}else{
				return "";
			}
		}
		/**
		 * 获取文件路径
		 * 
		 * @return
		 */
		public String getFilePath() {
			// 获取系统的绝对路径
			String lipossHome = "";
			String a = FileUploadAction.class.getResource("/").getPath();
			try {
				lipossHome = java.net.URLDecoder.decode(a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
			} catch (Exception e) {
				logger.error(e.getMessage());
				lipossHome = null;
			}
			logger.debug("{}待解析的文件路径", lipossHome);
			return lipossHome + "/temp/";
		}
		
		public String fileupAccount() {
			//获取系统的绝对路径
			String lipossHome = "";
			String a = FileUploadAction.class.getResource("/").getPath();
			try{
				lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
			}catch(Exception e){
				logger.error(e.getMessage());
				lipossHome = null;
			}
			this.imageFileName = gwShare_fileName;
			String ftpfilepath ="/export/home/itms/WEB_GTMS/deviceTestAccount";
			//String ftpfilepath =lipossHome + "/deviceTestAccount" ;
			//String ftpfilepath =lipossHome.substring(1, lipossHome.length()) + "/deviceTestAccount" ;
			File imageFile = new File(ftpfilepath+ "/"+imageFileName);
			if(!imageFile.getParentFile().exists()){
				imageFile.getParentFile().mkdirs();
			}
			logger.warn("文件名称:[{}]",imageFile.getName());
			myffFile = new File(getFilePath() + imageFileName);
			//拷贝到自己目录下
			copy(myffFile,imageFile);
			logger.warn("拷贝完成文件名称:[{}]",imageFile.getName());
			//同步ftp
			FileInputStream in=null;
			try{
				String host = LipossGlobals.getLipossProperty("TestDeviceAccFTP.HOST");
				int port = Integer.parseInt(LipossGlobals.getLipossProperty("TestDeviceAccFTP.PORT"));
				String username = LipossGlobals.getLipossProperty("TestDeviceAccFTP.USERNAME");
				String password = LipossGlobals.getLipossProperty("TestDeviceAccFTP.PASSWORD");
				String[] hosts = host.split(",");
				for (int i = 0; i < hosts.length; i++) {
						in=new FileInputStream(myffFile);
						logger.warn("开始同步文件到主机："+hosts[i]+"文件路径:"+ftpfilepath);
						boolean test = FtpUtil.uploadFile(hosts[i], username, password, port,ftpfilepath, imageFileName,in);
						logger.warn("同步文件到主机："+hosts[i]+"文件路径:"+ftpfilepath+",结果:"+test);
				}
			}catch (Exception e) {
				logger.error("文件上传或者ftp错误：{}",ExceptionUtils.getStackTrace(e));
			}finally{
				if(in!=null){
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return SUCCESS;
		}
		
	public String isfileexit(){
		String ftpfilepath = LipossGlobals.getLipossProperty("TestDeviceAccFTP.FILEPATH");
		// path是指欲下载的文件的路径
		
		String host = LipossGlobals.getLipossProperty("TestDeviceAccFTP.HOST");
		int port = Integer.parseInt(LipossGlobals.getLipossProperty("TestDeviceAccFTP.PORT"));
		String username = LipossGlobals.getLipossProperty("TestDeviceAccFTP.USERNAME");
		String password = LipossGlobals.getLipossProperty("TestDeviceAccFTP.PASSWORD");
		String a = FileUploadAction.class.getResource("/").getPath();
		try {
			String lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
			logger.warn("[{}]开始下载文件[{}], 目标位置[{}]",
					new Object[] { 0, ftpfilepath+fileName,lipossHome + "/deviceTestAccount/" +fileName });
			// 开始下载文件
			//FtpUtil.downloadFtpFile(host, username, password, port, ftpfilepath+fileName, lipossHome + "/deviceTestAccount/", fileName);
			ajax = "0";
		} catch (UnsupportedEncodingException e) {
			ajax = "1";
			logger.error("err:{}",ExceptionUtils.getStackTrace(e));
		}
		
		return "ajax";
		
	}
	public DeviceTestAccountBIO getBio() {
		return bio;
	}

	public void setBio(DeviceTestAccountBIO bio) {
		this.bio = bio;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getGwShare_deviceModelId() {
		return gwShare_deviceModelId;
	}

	public void setGwShare_deviceModelId(String gwShare_deviceModelId) {
		this.gwShare_deviceModelId = gwShare_deviceModelId;
	}

	public String getGwShare_hardwareVersion() {
		return gwShare_hardwareVersion;
	}

	public void setGwShare_hardwareVersion(String gwShare_hardwareVersion) {
		this.gwShare_hardwareVersion = gwShare_hardwareVersion;
	}

	public String getGwShare_devicetypeId() {
		return gwShare_devicetypeId;
	}

	public void setGwShare_devicetypeId(String gwShare_devicetypeId) {
		this.gwShare_devicetypeId = gwShare_devicetypeId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public String getGoal_devicetypeId() {
		return goal_devicetypeId;
	}

	public void setGoal_devicetypeId(String goal_devicetypeId) {
		this.goal_devicetypeId = goal_devicetypeId;
	}

	public List<Map<String, String>> getTestAccountList() {
		return testAccountList;
	}

	public void setTestAccountList(List<Map<String, String>> testAccountList) {
		this.testAccountList = testAccountList;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getGwShare_fileName() {
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getDevice_serialnumber() {
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber) {
		this.device_serialnumber = device_serialnumber;
	}

	

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
		public void setServletRequest(HttpServletRequest request) {
			this.request = request;
		}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getStarttime1() {
		return starttime1;
	}

	public void setStarttime1(String starttime1) {
		this.starttime1 = starttime1;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getEndtime1() {
		return endtime1;
	}

	public void setEndtime1(String endtime1) {
		this.endtime1 = endtime1;
	}

	public File getMyffFile() {
		return myffFile;
	}

	public void setMyffFile(File myffFile) {
		this.myffFile = myffFile;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
}
