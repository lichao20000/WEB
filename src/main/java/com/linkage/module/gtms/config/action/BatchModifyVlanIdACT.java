package com.linkage.module.gtms.config.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;




import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.BatchModifyVlanidBIO;
import com.linkage.module.gtms.config.serv.StackRefreshToolsBIO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Administrator
 * 
 */
public class BatchModifyVlanIdACT extends splitPageAction implements
		SessionAware {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(BatchModifyVlanIdACT.class);
	// Session
	private Map<String, Object> session;
	private String gwShare_fileName = "";
	private BatchModifyVlanidBIO bio;
	private String ajax = "";
	private long userId;
	// 属地
	private String cityId = "00";
	private String city_id;
	private String wanBus ;
	private String iptvBus;
	private String voipBus;
	
	/**
	 * 配置
	 * 
	 * @return
	 * @throws IOException
	 */
	public String doConfig() throws IOException {
		logger.warn("BatchModifyVlanIdACT.doConfig()");
		
		long todayCount = bio.getTodayCount();
		if (100000 < todayCount) {
			ajax = "今日执行用户数已满，不能超过10万个";
			return "ajax";
		}
		
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		
		if (StringUtil.IsEmpty(gwShare_fileName) || gwShare_fileName.length() < 4) {
			ajax =  "上传的文件名不正确！";
			return "ajax";
		}
		String fileName_ = gwShare_fileName.substring(gwShare_fileName.length() - 3, gwShare_fileName.length());
		if (!"xls".equals(fileName_) && !"txt".equals(fileName_)) {
			ajax = "上传的文件格式不正确！";
			return "ajax";
		}
		int count = 0;
		try {
			if ("txt".equals(fileName_)) {
				count = bio.getImportDataByTXT4JX(gwShare_fileName);
			} else {
				count = bio.getImportDataByXLS4JX(gwShare_fileName);
			}
		} catch (FileNotFoundException e) {
			logger.warn("{}文件没找到！", gwShare_fileName);
			ajax = "文件没找到！";
			return "ajax";
		} catch (IOException e) {
			logger.warn("{}文件解析出错！", gwShare_fileName);
			ajax = "文件解析出错！";
			return "ajax";
		} catch (Exception e) {
			logger.warn("{}文件解析出错！", gwShare_fileName);
			ajax = "文件解析出错！";
			return "ajax";
		}

		if (count > 30000){
			ajax = "文件行数不要超过30000行";
			return "ajax";
		}
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String filePath = "http://" + request.getLocalAddr() + ":" + request.getServerPort() + "/itms/temp/" + gwShare_fileName;
		logger.warn("上传文件路径[{}]", filePath);
		
		ajax = bio.doConfig4JX(curUser, filePath,wanBus,iptvBus,voipBus);

		return "ajax";
	}
 
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getGwShare_fileName() {
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	 
	public BatchModifyVlanidBIO getBio()
	{
		return bio;
	}

	public void setBio(BatchModifyVlanidBIO bio)
	{
		this.bio = bio;
	}
	public String getWanBus()
	{
		return wanBus;
	}

	
	public void setWanBus(String wanBus)
	{
		this.wanBus = wanBus;
	}

	
	public String getIptvBus()
	{
		return iptvBus;
	}

	
	public void setIptvBus(String iptvBus)
	{
		this.iptvBus = iptvBus;
	}

	
	public String getVoipBus()
	{
		return voipBus;
	}

	
	public void setVoipBus(String voipBus)
	{
		this.voipBus = voipBus;
	}
}
