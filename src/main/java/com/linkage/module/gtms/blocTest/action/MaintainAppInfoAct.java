package com.linkage.module.gtms.blocTest.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.blocTest.serv.MaintainAppInfoBIO;
import com.linkage.module.itms.resource.act.DeviceTypeInfoACT;
import com.linkage.module.itms.resource.bio.DevVendorDataUploadBIO;

/**
 * 
 * @author zzd (Ailk No.)
 * @version 1.0
 * @since 2016-8-9
 * @category com.linkage.module.gtms.blocTest.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MaintainAppInfoAct  extends splitPageAction implements ServletRequestAware, ServletResponseAware, SessionAware
{
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(MaintainAppInfoAct.class);

	/** 新增-文件路径 **/
	private File fileAppPath = null;
	/** 新增-文件名称 **/
	private String fileName = null;
	/**
	 * 执行结果 0:错误 1:正确
	 */
	private String excuteResult;
	/** 软件升级记录查询 用service */
	private MaintainAppInfoBIO bio;
	private String device_model;
	private String appuuid;
	private String app_name;
	private String app_desc; 
	private String app_vendor; 
	private String app_version; 
	private String  app_publish_status; 
	private String app_publish_time_start;
	private String app_publish_time_end;
	private String delId;
	private String pubId;
	private String detailid;
	private String delName;
	private String upfilepath;
	private String updateId;
	private List<Map> appInfoList;
	private List appDetailList;
	private String ajax;
	private Map session;
	/** HttpServletRequest */
	private HttpServletRequest request;
	/** HttpServletResponse */
	private HttpServletResponse response;
	
	/** 存放在WEB工程的路径 **/
	public static String SYSTEM_PATH = "app-version-file" + File.separator;
	
	public String queryDetailAppInfo(){
		appDetailList = bio.queryDetailList(detailid);
		return "detail";
	}
	
	public String pubAppInfo(){
		long app_publish_time = System.currentTimeMillis()/1000;
		try {
			bio.pubAppInfo(StringUtil.getLongValue(pubId),app_publish_time);
			ajax = "1";
		} catch (Exception e) {
			ajax = "0";
		}
		excuteResult = "6";
		return "ajax";
	}
	
	public String updateAppInfo()
	{
		ServletRequest servletRequest = (ServletRequest) request;
		// 保存的路径
		String storePath = servletRequest.getRealPath("/") + SYSTEM_PATH;
		if(null != fileAppPath&&null != fileName){
			if(1 == checkFileName(fileName)){
				//存在同名文件
				excuteResult = "4";
				return "appUploadResponse";
			}
			if(fileAppPath.length()>(200*1024)){
				//上传文件大于200M
				excuteResult = "5";
				return "appUploadResponse";
			}
			// 删除文件结果
			String delfileResult = bio.deleteFile(upfilepath);
			// 上传文件结果
			String fileResult = bio.saveFile(fileAppPath, storePath, fileName);
		}
		
		UserRes curUser = (UserRes) session.get("curUser");
		User user = curUser.getUser();
		String create_user = user.getId()+"";
		long create_time = System.currentTimeMillis()/1000;
		if(null == app_desc){
			app_desc ="";
		}
		long app_publish_time =0L;
		if("1".equals(app_publish_status)){
			app_publish_time = create_time;
		}
		int num = bio.updAppInfo(updateId, appuuid, app_name, app_desc, app_vendor, app_version, app_publish_status,create_time ,create_user,app_publish_time,storePath+fileName);
		if(1 == num){
			excuteResult = "1";
		}else{
			excuteResult = "fail";
		}
		return "appUploadResponse";
	}
	public String delAppInfo(){
		String dbresult;
		Map map = bio.querydelPath(StringUtil.getLongValue(delId));
		try {
			bio.delAppInfo(delId);
			dbresult = "1";
		} catch (Exception e) {
			dbresult = "0";
		}
		ServletRequest servletRequest = (ServletRequest) request;
		String storePath = servletRequest.getRealPath("/") + SYSTEM_PATH;
		// 删除文件结果
		String deleteName = (String)map.get("file_path");
		String fileResult = bio.deleteFile(deleteName);
		ajax = dbresult+"|"+fileResult;
		if("1".equals(dbresult)&&"1".equals(fileResult)){
			querymaintainAppInfo();
		}
		return "ajax";
	}
	
	public String querymaintainAppInfo() {
	
		if (app_publish_time_start != null && !"".equals(app_publish_time_start)) {
			app_publish_time_start = dealTime(app_publish_time_start);
		}
		if (app_publish_time_end != null && !"".equals(app_publish_time_end)) {
			app_publish_time_end = dealTime(app_publish_time_end);
		}
		
		appInfoList = bio.queryDevList(curPage_splitPage, num_splitPage, appuuid, app_name, app_vendor, app_version,
												app_publish_status, app_publish_time_start, app_publish_time_end);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "appInfoList";
	}
	public String dealTime(String time) {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date str = new Date();
		try {
			str = date.parse(time);
		} catch (ParseException e) {

			logger.warn("选择开始或者结束的时间格式不对:" + time);
		}

		return str.getTime() / 1000 + "";
	}
	/**
	 * 新增软件升级记录
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String maintainAppInfo()
	{
		ServletRequest servletRequest = (ServletRequest) request;
		// 保存的路径
		String storePath = servletRequest.getRealPath("/") + SYSTEM_PATH;
		
		if(1 == checkFileName(fileName)){
			excuteResult = "4";
			return "appUploadResponse";
		}
		if(fileAppPath.length()>(200*1024)){
			excuteResult = "5";
			return "appUploadResponse";
		}
		// 上传文件结果
		String fileResult = bio.saveFile(fileAppPath, storePath, fileName);
		
		UserRes curUser = (UserRes) session.get("curUser");
		User user = curUser.getUser();
		String create_user = user.getId()+"";
		long create_time = System.currentTimeMillis()/1000;
		if("0".equals(fileResult)){
			excuteResult = "0";
		}else{
			String idstr = create_time+""+(int)(Math.random()*1000);
			long id = StringUtil.getLongValue(idstr);
			if(null == app_desc){
				app_desc ="";
			}
			long app_publish_time =0L;
			if("1".equals(app_publish_status)){
				app_publish_time = create_time;
			}
			if(null == create_user){
				create_user ="";
			}   
			excuteResult = bio.addMaintainAppInfo(id, appuuid, app_name,app_desc, app_vendor, app_version,
					app_publish_time, app_publish_status, create_user, create_time, storePath+fileName);

		}
		return "appUploadResponse";
	}
	public int checkFileName(String filename){
		
		return bio.checkFilename(filename);
	}



	public File getFileAppPath()
	{
		return fileAppPath;
	}


	public void setFileAppPath(File fileAppPath)
	{
		this.fileAppPath = fileAppPath;
	}


	public String getFileName()
	{
		return fileName;
	}


	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}


	public String getExcuteResult()
	{
		return excuteResult;
	}


	public void setExcuteResult(String excuteResult)
	{
		this.excuteResult = excuteResult;
	}


	public MaintainAppInfoBIO getBio()
	{
		return bio;
	}


	public void setBio(MaintainAppInfoBIO bio)
	{
		this.bio = bio;
	}


	


	public String getApp_name()
	{
		return app_name;
	}


	public void setApp_name(String app_name)
	{
		this.app_name = app_name;
	}


	public String getApp_desc()
	{
		return app_desc;
	}


	public void setApp_desc(String app_desc)
	{
		this.app_desc = app_desc;
	}


	public String getApp_vendor()
	{
		return app_vendor;
	}


	public void setApp_vendor(String app_vendor)
	{
		this.app_vendor = app_vendor;
	}


	public String getApp_version()
	{
		return app_version;
	}


	public void setApp_version(String app_version)
	{
		this.app_version = app_version;
	}


	


	public String getApp_publish_status()
	{
		return app_publish_status;
	}


	public void setApp_publish_status(String app_publish_status)
	{
		this.app_publish_status = app_publish_status;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}


	public Map getSession()
	{
		return session;
	}


	public void setSession(Map session)
	{
		this.session = session;
	}




	
	public HttpServletRequest getRequest()
	{
		return request;
	}


	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response)
	{
		this.response = response;
	}
	
	
	
	public String getDevice_model()
	{
		return device_model;
	}
	
	public void setDevice_model(String device_model)
	{
		this.device_model = device_model;
	}
	
	public String getApp_publish_time_start()
	{
		return app_publish_time_start;
	}
	
	public void setApp_publish_time_start(String app_publish_time_start)
	{
		this.app_publish_time_start = app_publish_time_start;
	}
	
	public String getApp_publish_time_end()
	{
		return app_publish_time_end;
	}
	
	public void setApp_publish_time_end(String app_publish_time_end)
	{
		this.app_publish_time_end = app_publish_time_end;
	}
	
	public List<Map> getAppInfoList()
	{
		return appInfoList;
	}
	
	public void setAppInfoList(List<Map> appInfoList)
	{
		this.appInfoList = appInfoList;
	}
	
	public String getAppuuid()
	{
		return appuuid;
	}
	
	public void setAppuuid(String appuuid)
	{
		this.appuuid = appuuid;
	}

	
	public String getDelId()
	{
		return delId;
	}

	
	public void setDelId(String delId)
	{
		this.delId = delId;
	}

	
	public String getDelName()
	{
		return delName;
	}

	
	public void setDelName(String delName)
	{
		this.delName = delName;
	}
	
	public String getUpdateId()
	{
		return updateId;
	}
	
	public void setUpdateId(String updateId)
	{
		this.updateId = updateId;
	}

	
	public String getPubId()
	{
		return pubId;
	}

	
	public void setPubId(String pubId)
	{
		this.pubId = pubId;
	}

	
	public String getDetailid()
	{
		return detailid;
	}

	
	public void setDetailid(String detailid)
	{
		this.detailid = detailid;
	}

	
	public List getAppDetailList()
	{
		return appDetailList;
	}

	
	public void setAppDetailList(List appDetailList)
	{
		this.appDetailList = appDetailList;
	}

	
	public String getUpfilepath()
	{
		return upfilepath;
	}

	
	public void setUpfilepath(String upfilepath)
	{
		this.upfilepath = upfilepath;
	}

	
	

}
