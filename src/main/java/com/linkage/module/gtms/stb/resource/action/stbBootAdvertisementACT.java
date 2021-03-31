package com.linkage.module.gtms.stb.resource.action;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.stbBootAdvertisementBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-2-11
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class stbBootAdvertisementACT extends splitPageAction  implements SessionAware,ServletRequestAware
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(stbBootAdvertisementACT.class);
	/**
	 * ************************定制*********************************
	 */
	/** 任务id */
	private String taskId = "";
	/** 任务名 */
	private String taskName = "";
	/** 厂商 */
	private String vendorId = "";
	/** 型号 */
	private String deviceModelIds = "";
	/** 软件版本 */
	private String deviceTypeIds = "";
	/** 下级属地 */
	private String cityIds = "";
	/** 属地 */
	private String cityId = "";
	/** 是否启用IP地址段 */
	private String ipCheck = "1";
	/** 是否启用MAC地址段 */
	private String macCheck = "1";
	/** 是否加入业务用户维度(当业务帐号不需要从WEB这边导入数据库时，这个条件已经没有用) */
	private String custCheck="";
	/** IP地址段 */
	private String ipSG = "";
	/** MAC地址段 */
	private String macSG = "";
	/** 手工输入的用户信息 */
	private String custSG = "";
	/** 节点路径 */
	private String ajax = "";
	
	private InputStream exportExcelStream;
	
	private InputStream exportCustStream ;
	/** 文件路径 */
	private String filePath = "";
	/** 实际上传文件 */
	private File uploadIP;
	/** 上传文件名 */
	private String uploadFileName4IP;
	/** 实际上传文件 */
	private File uploadMAC;
	/** 上传MAC地址段文件名 */
	private String uploadFileName4MAC;
	/** 上传业务用户文件 */
	private File uploadCustomer;
	/** 上传业务用户文件名 */
	private String uploadFileName4Customer;
	
	private File bootFile;
	private File startFile;
	private File authFile;
	
	private String bootFileName;
	private String startFileName;
	private String authFileName;
	
	
	/** 导入or手输IP byBatchIP：导入 */
	private String btnValue4IP;
	/** 导入or手输MAC byBatchMAC 导入 */
	private String btnValue4MAC;
	/** 导入or手输MAC byBatchCustomer 导入 */
	private String btnValue4Customer;
	
	/** 初始化 */
	private List<Map<String, String>> cityList = null;
	/** 厂商 */
	private List vendorList;
	/**任务失效时间*/
	private String Invalidtime="";
	/**优先级*/
	private String priority="";
	/**分组id*/
	private String groupid="";
	private String groupids="";
	/**重庆City*/
	private String cqCityIds="";
	/**重庆cqVendorIds*/
	private String cqVendorIds="";
	/**
	 * *************************查询********************************
	 */
	// 导出数据
	private String[] title;
	private String[] column;
	private String fileName = "";
	private List data = null;
	
	// 查询开始时间
	private String startTime;
	// 查询结束时间
	private String endTime;
	//定制人
	private String accName;
	
	private List tasklist;
	private List taskResultList = null;
	private List ipList = null;
	private List macList = null;
	private Map<String, String> taskDetailMap = null;
	
	
	private stbBootAdvertisementBIO bio;
	private String isLocked;
	private String endtime;
	private String gwShare_fileName;
	private static String addTaskName = "点击定制任务按钮";
	private static String delTaskName = "点击删除任务按钮";
	private static String checkTaskName = "点击审核任务按钮";
	
	private Map session;
	private HttpServletRequest request; 
	/**
	 * 初始化页面
	 * @return
	 */
	public String init()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		this.cityList = CityDAO.getNextCityListByCityPid(curUser.getUser().getCityId());
		this.vendorList = bio.getVendorList();
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 240 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		return "init";
	}
	/**
	 * 根据cityId查询下级属地
	 * 
	 * @return
	 */
	public String getNextCity()
	{
		ajax = bio.getNextCity(cityId);
		return "ajax";
	}
	/**
	 * 根据型号查询年版本
	 * 
	 * @return
	 */
	public String getSoftVersion()
	{
		ajax = bio.getSoftVersion(deviceModelIds);
		return "ajax";
	}
	/**
	 * 根据vendorId查询型号
	 * 
	 * @return
	 */
	public String getDeviceModel()
	{
		ajax = bio.getDeviceModel(vendorId);
		return "ajax";
	}
	/**
	 * 查询所有分组
	 * @return
	 */
	public String getGroupId()
	{
		ajax=bio.getGroupId();
		return "ajax";
	}
	/**
	 * 机顶盒任务定制
	 * @return
	 */
	public String importConfig()
	{
		logger.warn("importConfig(cityId[{}],vendorId[{}],taskName[{}],deviceModelIds[{}],deviceTypeIds[{}],bootFile[{}],startFile[{}],authFile[{}],bootFileName[{}],startFileName[{}]" +
				",authFileName[{}],isLocked[{}],groupids[{}],filePath[{}],priority[{}],Invalidtime[{}],cqCityIds[{}],cqVendorIds[{}])",new Object[]{cityId,vendorId,taskName,deviceModelIds,deviceTypeIds,bootFile,startFile,authFile,bootFileName,startFileName
				,authFileName,isLocked,groupids,filePath,priority,Invalidtime,cqCityIds,cqVendorIds});
		
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		long currTime = new Date().getTime() / 1000L;
		long taskId = currTime;
		long addTime = currTime;
		addItemLog(addTaskName+"("+taskId+")");
		
		if(Global.CQDX.equals(Global.instAreaShortName)){
			//重庆的属地和厂商已经变为复选框可多选了 yaoli 20181018
			cityId = cqCityIds; 
			vendorId = cqVendorIds;
			logger.warn("cityId[{}],vendorId[{}]",new Object[]{cityId,vendorId});
		} 
		if(StringUtil.IsEmpty(cityIds)||cityIds.equals("-1"))
		{
			ajax = bio.OpenDeviceShowPicConfig(taskId, cityId, vendorId, taskName, acc_oid, addTime, deviceModelIds, deviceTypeIds, bootFile, startFile, authFile, bootFileName, startFileName,
					authFileName, isLocked, groupids, filePath, priority, Invalidtime);
		}else
		{
			ajax = bio.OpenDeviceShowPicConfig(taskId, cityIds, vendorId, taskName, acc_oid, addTime, deviceModelIds, deviceTypeIds, bootFile, startFile, authFile, bootFileName, startFileName,
					authFileName, isLocked, groupids, filePath, priority, Invalidtime);
		}
		
		return "ajax";
	}
	
	
	private void addItemLog(String operaName)
	{
		UserRes curUser = (UserRes) session.get("curUser");
		// 对于session失效的情况，不做日志处理。struts2拦截器会要求用户重新登录。
		if (curUser != null)
		{
			User user = curUser.getUser();
			bio.recordOperLog(operaName,user,request.getRemoteAddr(),request.getRemoteHost());
		}
	}
	
	
	public String getTaskId()
	{
		return taskId;
	}
	
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
	
	public String getTaskName()
	{
		return taskName;
	}
	
	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}
	
	public String getVendorId()
	{
		return vendorId;
	}
	
	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}
	
	public String getDeviceModelIds()
	{
		return deviceModelIds;
	}
	
	public void setDeviceModelIds(String deviceModelIds)
	{
		this.deviceModelIds = deviceModelIds;
	}
	
	public String getDeviceTypeIds()
	{
		return deviceTypeIds;
	}
	
	public void setDeviceTypeIds(String deviceTypeIds)
	{
		this.deviceTypeIds = deviceTypeIds;
	}
	
	public String getCityIds()
	{
		return cityIds;
	}
	
	public void setCityIds(String cityIds)
	{
		this.cityIds = cityIds;
	}
	
	public String getCityId()
	{
		return cityId;
	}
	
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}
	
	public String getIpCheck()
	{
		return ipCheck;
	}
	
	public void setIpCheck(String ipCheck)
	{
		this.ipCheck = ipCheck;
	}
	
	public String getMacCheck()
	{
		return macCheck;
	}
	
	public void setMacCheck(String macCheck)
	{
		this.macCheck = macCheck;
	}
	
	public String getCustCheck()
	{
		return custCheck;
	}
	
	public void setCustCheck(String custCheck)
	{
		this.custCheck = custCheck;
	}
	
	public String getIpSG()
	{
		return ipSG;
	}
	
	public void setIpSG(String ipSG)
	{
		this.ipSG = ipSG;
	}
	
	public String getMacSG()
	{
		return macSG;
	}
	
	public void setMacSG(String macSG)
	{
		this.macSG = macSG;
	}
	
	public String getCustSG()
	{
		return custSG;
	}
	
	public void setCustSG(String custSG)
	{
		this.custSG = custSG;
	}
	
	public String getAjax()
	{
		return ajax;
	}
	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	
	public InputStream getExportExcelStream()
	{
		return exportExcelStream;
	}
	
	public void setExportExcelStream(InputStream exportExcelStream)
	{
		this.exportExcelStream = exportExcelStream;
	}
	
	public InputStream getExportCustStream()
	{
		return exportCustStream;
	}
	
	public void setExportCustStream(InputStream exportCustStream)
	{
		this.exportCustStream = exportCustStream;
	}
	
	public String getFilePath()
	{
		return filePath;
	}
	
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	
	public File getUploadIP()
	{
		return uploadIP;
	}
	
	public void setUploadIP(File uploadIP)
	{
		this.uploadIP = uploadIP;
	}
	
	public String getUploadFileName4IP()
	{
		return uploadFileName4IP;
	}
	
	public void setUploadFileName4IP(String uploadFileName4IP)
	{
		this.uploadFileName4IP = uploadFileName4IP;
	}
	
	public File getUploadMAC()
	{
		return uploadMAC;
	}
	
	public void setUploadMAC(File uploadMAC)
	{
		this.uploadMAC = uploadMAC;
	}
	
	public String getUploadFileName4MAC()
	{
		return uploadFileName4MAC;
	}
	
	public void setUploadFileName4MAC(String uploadFileName4MAC)
	{
		this.uploadFileName4MAC = uploadFileName4MAC;
	}
	
	public File getUploadCustomer()
	{
		return uploadCustomer;
	}
	
	public void setUploadCustomer(File uploadCustomer)
	{
		this.uploadCustomer = uploadCustomer;
	}
	
	public String getUploadFileName4Customer()
	{
		return uploadFileName4Customer;
	}
	
	public void setUploadFileName4Customer(String uploadFileName4Customer)
	{
		this.uploadFileName4Customer = uploadFileName4Customer;
	}
	
	public File getBootFile()
	{
		return bootFile;
	}
	
	public void setBootFile(File bootFile)
	{
		this.bootFile = bootFile;
	}
	
	public File getStartFile()
	{
		return startFile;
	}
	
	public void setStartFile(File startFile)
	{
		this.startFile = startFile;
	}
	
	public File getAuthFile()
	{
		return authFile;
	}
	
	public void setAuthFile(File authFile)
	{
		this.authFile = authFile;
	}
	
	public String getBootFileName()
	{
		return bootFileName;
	}
	
	public void setBootFileName(String bootFileName)
	{
		this.bootFileName = bootFileName;
	}
	
	public String getStartFileName()
	{
		return startFileName;
	}
	
	public void setStartFileName(String startFileName)
	{
		this.startFileName = startFileName;
	}
	
	public String getAuthFileName()
	{
		return authFileName;
	}
	
	public void setAuthFileName(String authFileName)
	{
		this.authFileName = authFileName;
	}
	
	public String getBtnValue4IP()
	{
		return btnValue4IP;
	}
	
	public void setBtnValue4IP(String btnValue4IP)
	{
		this.btnValue4IP = btnValue4IP;
	}
	
	public String getBtnValue4MAC()
	{
		return btnValue4MAC;
	}
	
	public void setBtnValue4MAC(String btnValue4MAC)
	{
		this.btnValue4MAC = btnValue4MAC;
	}
	
	public String getBtnValue4Customer()
	{
		return btnValue4Customer;
	}
	
	public void setBtnValue4Customer(String btnValue4Customer)
	{
		this.btnValue4Customer = btnValue4Customer;
	}
	
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}
	
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}
	
	public List getVendorList()
	{
		return vendorList;
	}
	
	public void setVendorList(List vendorList)
	{
		this.vendorList = vendorList;
	}
	
	public String getInvalidtime()
	{
		return Invalidtime;
	}
	
	public void setInvalidtime(String invalidtime)
	{
		Invalidtime = invalidtime;
	}
	
	public String getPriority()
	{
		return priority;
	}
	
	public void setPriority(String priority)
	{
		this.priority = priority;
	}
	
	public String getGroupid()
	{
		return groupid;
	}
	
	public void setGroupid(String groupid)
	{
		this.groupid = groupid;
	}
	
	public String[] getTitle()
	{
		return title;
	}
	
	public void setTitle(String[] title)
	{
		this.title = title;
	}
	
	public String[] getColumn()
	{
		return column;
	}
	
	public void setColumn(String[] column)
	{
		this.column = column;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public List getData()
	{
		return data;
	}
	
	public void setData(List data)
	{
		this.data = data;
	}
	
	public String getStartTime()
	{
		return startTime;
	}
	
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
	
	public String getEndTime()
	{
		return endTime;
	}
	
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	
	public String getAccName()
	{
		return accName;
	}
	
	public void setAccName(String accName)
	{
		this.accName = accName;
	}
	
	public List getTasklist()
	{
		return tasklist;
	}
	
	public void setTasklist(List tasklist)
	{
		this.tasklist = tasklist;
	}
	
	public List getTaskResultList()
	{
		return taskResultList;
	}
	
	public void setTaskResultList(List taskResultList)
	{
		this.taskResultList = taskResultList;
	}
	
	public List getIpList()
	{
		return ipList;
	}
	
	public void setIpList(List ipList)
	{
		this.ipList = ipList;
	}
	
	public List getMacList()
	{
		return macList;
	}
	
	public void setMacList(List macList)
	{
		this.macList = macList;
	}
	
	public Map<String, String> getTaskDetailMap()
	{
		return taskDetailMap;
	}
	
	public void setTaskDetailMap(Map<String, String> taskDetailMap)
	{
		this.taskDetailMap = taskDetailMap;
	}
	
	public String getIsLocked()
	{
		return isLocked;
	}
	
	public void setIsLocked(String isLocked)
	{
		this.isLocked = isLocked;
	}
	
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	
	public stbBootAdvertisementBIO getBio()
	{
		return bio;
	}

	
	public void setBio(stbBootAdvertisementBIO bio)
	{
		this.bio = bio;
	}
	
	public String getGroupids()
	{
		return groupids;
	}
	
	public void setGroupids(String groupids)
	{
		this.groupids = groupids;
	}
	
	public String getEndtime()
	{
		return endtime;
	}
	
	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}
	
	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}
	
	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}
	
	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}		
	
	public void setServletRequest(HttpServletRequest request) {  
		 // request的setter方法  
		this.request = request;  
	}  
	public HttpServletRequest getServletRequest() {  
		return  this.request;
	}
	public String getCqCityIds()
	{
		return cqCityIds;
	}
	public void setCqCityIds(String cqCityIds)
	{
		this.cqCityIds = cqCityIds;
	}
	public String getCqVendorIds()
	{
		return cqVendorIds;
	}
	public void setCqVendorIds(String cqVendorIds)
	{
		this.cqVendorIds = cqVendorIds;
	} 
}
