package com.linkage.module.gtms.config.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.VoipChangeConfigBio;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.StringUtil;

public class VoipChangeConfigACT extends splitPageAction implements
		SessionAware, ServletResponseAware 
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(VoipChangeConfigACT.class);
	private Map<String, Object> session;
	private VoipChangeConfigBio bio;
	private String gwShare_fileName = "";
	private String gwShare_cityId = "";
	// 查询方式
	private String gwShare_queryField = "";
	private int total;
	// 查询参数
	private String gwShare_queryParam = "";
	private String gwShare_msg = "";
	/** 错误账号 */
	private String faultList = "";
	private String ajax = "";
	private long userId;
	private String taskid = "";
	private HttpServletResponse response;
	// 查询宽带帐号
	private List<HashMap<String, String>> deviceList;
	// 是否为通化页面
	private String gwShare_thpage = "";

	/**
	 * 分析导入文件
	 */
	public String queryDeviceList() 
	{
		logger.debug("VoipChangeConfigACT=>getDeviceList()");
		UserRes curUser = (UserRes) session.get("curUser");
		if (!StringUtil.IsEmpty(gwShare_cityId)) {
			gwShare_cityId.trim();
		} else {
			gwShare_cityId = curUser.getCityId();
		}
		if (!StringUtil.IsEmpty(gwShare_cityId)) {
			gwShare_fileName.trim();
		}
		taskid = StringUtil.getStringValue(new DateTimeUtil().getLongTime());
		List<HashMap<String, String>> deviceListTmp = bio.getDeviceList(gwShare_fileName, faultList,taskid, "1".equals(gwShare_thpage) ? true : false);
		if (null == deviceListTmp || deviceListTmp.isEmpty()) {
			gwShare_msg = bio.getMsg();
			deviceList=null;
		} else {
			faultList = deviceListTmp.get(deviceListTmp.size() - 1).get("faultMap");
			this.deviceList = new ArrayList<HashMap<String, String>>();
			for (int i = 0; i < deviceListTmp.size() - 1; i++) {
				HashMap<String, String> map = deviceListTmp.get(i);
				this.deviceList.add(map);
			}
		}

		total = deviceList == null ? 0 : deviceList.size();
		return "shareList0";
	}
	
	/**
	 * 执行后台
	 */
	public String doConfigAll() 
	{
		UserRes curUser = (UserRes) session.get("curUser");
		userId = curUser.getUser().getId();
		ajax = StringUtil.getStringValue(bio.doConfigAll(taskid, userId));
		return "ajax";
	}
	
	/**
	 * 下载模板
	 */
	public String downModle()
	{
		logger.debug("语音割接参数XLSX模板downModle()方法入口");
		// 文件路径
		String lipossHome = "";
		InputStream fis=null;
		OutputStream os=null;
		try
		{
			String a = FileUploadAction.class.getResource("/").getPath();
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		
			// 下载xls模板
			String filepath = lipossHome + "/fileModledata/" + "语音割接参数XLSX模板.xlsx";
			
			logger.warn("download({})", filepath);
			// path是指欲下载的文件的路径
			File file = new File(filepath);
			// 取得文件名
			String filename = file.getName();
			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(filepath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			fis=null;
			
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
			response.addHeader("Content-Length", "" + file.length());
			os = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			os.write(buffer);
			os.flush();
			os.close();
			os=null;
		}catch(Exception e){
			logger.error("语音割接参数XLSX模板downModle()方法错误:{}",ExceptionUtils.getStackTrace(e));
		}finally{
			try {
				if(fis!=null){
					fis.close();
					fis=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(os!=null){
					os.close();
					os=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String downModleTH()
	{
		logger.debug("语音割接参数XLSX模板downModle()方法入口");
		// 文件路径
		String lipossHome = "";
		InputStream fis=null;
		OutputStream os=null;
		try
		{
			String a = FileUploadAction.class.getResource("/").getPath();
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		
			// 下载xls模板
			String filepath = lipossHome + "/fileModledata/" + "语音割接参数XLSX通化模板.xlsx";
			
			logger.warn("download({})", filepath);
			// path是指欲下载的文件的路径
			File file = new File(filepath);
			// 取得文件名
			String filename = file.getName();
			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(filepath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			fis=null;
			
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
			response.addHeader("Content-Length", "" + file.length());
			os = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			os.write(buffer);
			os.flush();
			os.close();
			os=null;
		}catch(Exception e){
			logger.error("语音割接参数XLSX模板downModle()方法错误:{}",ExceptionUtils.getStackTrace(e));
		}finally{
			try {
				if(fis!=null){
					fis.close();
					fis=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				if(os!=null){
					os.close();
					os=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public VoipChangeConfigBio getBio() {
		return bio;
	}

	public void setBio(VoipChangeConfigBio bio) {
		this.bio = bio;
	}

	public String getGwShare_fileName() {
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getGwShare_cityId() {
		return gwShare_cityId;
	}

	public void setGwShare_cityId(String gwShare_cityId) {
		this.gwShare_cityId = gwShare_cityId;
	}

	public String getGwShare_queryField() {
		return gwShare_queryField;
	}

	public void setGwShare_queryField(String gwShare_queryField) {
		this.gwShare_queryField = gwShare_queryField;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getGwShare_queryParam() {
		return gwShare_queryParam;
	}

	public void setGwShare_queryParam(String gwShare_queryParam) {
		this.gwShare_queryParam = gwShare_queryParam;
	}

	public String getGwShare_msg() {
		return gwShare_msg;
	}

	public void setGwShare_msg(String gwShare_msg) {
		this.gwShare_msg = gwShare_msg;
	}

	public String getFaultList() {
		return faultList;
	}

	public void setFaultList(String faultList) {
		this.faultList = faultList;
	}

	public List<HashMap<String, String>> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<HashMap<String, String>> deviceList) {
		this.deviceList = deviceList;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public void setServletResponse(HttpServletResponse response){
		this.response = response;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getGwShare_thpage() {
		return gwShare_thpage;
	}

	public void setGwShare_thpage(String gwShare_thpage) {
		this.gwShare_thpage = gwShare_thpage;
	}
}
