
package com.linkage.module.itms.resource.act;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.resource.bio.SoftUpgradRecordQueryBIO;

/**
 * @author yinlei3 (73167)
 * @version 1.0
 * @since 2015年6月2日
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class SoftUpgradRecordQueryACT extends splitPageAction implements
		ServletRequestAware, ServletResponseAware
{

	/** 序列化 */
	private static final long serialVersionUID = 1L;
	/** 日志 */
	private static Logger logger = LoggerFactory
			.getLogger(SoftUpgradRecordQueryACT.class);
	/** 终端厂家 */
	private String vendor = "";
	/** 终端型号 */
	private String device_model = "";
	/** 升级开始起始时间 */
	private String starttime = "";
	/** 升级开始截止时间 */
	private String endtime = "";
	/** 升级记录Id */
	private String recordId = "";
	/** 新增-终端厂家 */
	private String vendor_add = "";
	/** 新增-终端型号 */
	private String device_model_add = "";
	/** 新增 -现有版本 */
	private String currentVersion_add = "";
	/** 新增-目标版本 */
	private String targetVersion_add = "";
	/** 新增-升级范围 */
	private String upgradeRange_add = "";
	/** 新增-终端数量 */
	private String deviceCount_add = "";
	/** 新增-升级原因 */
	private String upgradeReason_add = "";
	/** 新增-升级方式 */
	private String upgradeMethod_add = "";
	/** 新增-升级开始时间 */
	private String starttime_add = "";
	/** 新增-升级结束时间 */
	private String endtime_add = "";
	/** 新增-终端厂家联系方式 */
	private String contactWay_add = "";
	/** 新增-文件路径 **/
	private File file_path_add = null;
	/** 软件升级记录查询 用service */
	private SoftUpgradRecordQueryBIO bio;
	/** 节点路径 */
	private String ajax = "";
	/** 初始化结果 */
	private final String INITR_RESULT = "queryForm";
	/** 查询列表结果 */
	private final String QUERY_RESULT = "queryList";
	/** 修改列表结果 */
	private final String MODIFY_RESULT = "modifyForm";
	/** DB变更结果 */
	private final String DB_RESULT = "dbResultForm";
	/** ajax */
	private final String AJAX_RESULT = "ajax";
	/** 存放在WEB工程的路径 **/
	public static String SYSTEM_PATH = "dev-version-file" + File.separator;
	/** 软件升级记录List */
	@SuppressWarnings("rawtypes")
	private List<Map> softUpgradRecordList = null;
	/** 单条记录结果map */
	@SuppressWarnings("rawtypes")
	private Map recordMap = null;
	/** HttpServletRequest */
	private HttpServletRequest request;
	/** HttpServletResponse */
	private HttpServletResponse response;
	/** 保存到WEB系统里面文件名 */
	private String fileName = "";
	/** 修改时保存到WEB系统里面文件名 */
	private String fileName_modify = "";
	/** 公告标题 */
	private String title = "";
	/** 公告内容 */
	private String content = "";

	/**
	 * 初始化查询页面
	 * 
	 * @return 初始化结果
	 */
	public String init()
	{
		return INITR_RESULT;
	}

	/**
	 * 查询设备厂商
	 * 
	 * @return 设备厂商
	 */
	public String getVendors()
	{
		logger.debug("SoftUpgradRecordQueryACT=>getVendors()");
		this.ajax = bio.getVendors();
		return AJAX_RESULT;
	}

	/**
	 * 查询设备型号
	 * 
	 * @param vendor
	 *            设备Id
	 * @return 设备型号
	 */
	public String getDeviceModels()
	{
		logger.debug("SoftUpgradRecordQueryACT=>getDeviceModels()");
		this.ajax = bio.getDeviceModels(vendor);
		return AJAX_RESULT;
	}

	/**
	 * 查询设备版本
	 * 
	 * @return
	 */
	public String getDevicetypes()
	{
		logger.debug("SoftUpgradRecordQueryACT=>getDevicetypes()");
		this.ajax = bio.getDevicetypes(device_model_add);
		return AJAX_RESULT;
	}

	/**
	 * 查询列表
	 * 
	 * @return
	 */
	public String getSoftUpgradRecordQuery()
	{
		logger.debug("SoftUpgradRecordQueryACT-> getSoftUpgradRecordQuery");
		// 时间转化
		starttime = setTime(starttime);
		endtime = setTime(endtime);
		softUpgradRecordList = bio.getSoftUpgradRecordQuery(vendor, device_model,
				starttime, endtime, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getCountSoftUpgradRecordQuery(vendor, device_model,
				starttime, endtime, curPage_splitPage, num_splitPage);
		return QUERY_RESULT;
	}

	/**
	 * 修改软件升级记录
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String modifyByRecordId()
	{
		logger.debug("SoftUpgradRecordQueryACT-> modifyByRecordId");
		// 时间转换
		starttime_add = setTime(starttime_add);
		endtime_add = setTime(endtime_add);
		ServletRequest servletRequest = (ServletRequest) request;
		// 是否需要上传文件
		boolean needUploadFile = file_path_add == null ? false : true;
		String dbResult = "";
		if (needUploadFile)
		{
			// 保存的路径
			String storePath = servletRequest.getRealPath("/") + SYSTEM_PATH;
			// 若原有记录有附件，删除原有附件   删除结果忽略
			if (!StringUtil.IsEmpty(fileName))
			{
				bio.deleteFile(storePath + fileName);
			}
			// 上传文件结果
			String fileResult = bio.saveFile(file_path_add, storePath, fileName_modify);
			// 更新数据库
			dbResult = bio.modifyByRecordId(recordId, targetVersion_add,
					upgradeRange_add, deviceCount_add, upgradeReason_add,
					upgradeMethod_add, starttime_add, endtime_add, contactWay_add,
					fileName_modify);
			// 拼接结果页面展示
			ajax = dbResult + "|" + fileResult;
		}
		else
		{
			dbResult = bio.modifyByRecordId(recordId, targetVersion_add,
					upgradeRange_add, deviceCount_add, upgradeReason_add,
					upgradeMethod_add, starttime_add, endtime_add, contactWay_add,
					fileName);
			// 拼接结果页面展示,-1标示不需要上传文件
			ajax = dbResult + "|" + "-1";
		}
		return DB_RESULT;
	}

	/**
	 * 删除所选记录
	 * 
	 * @return 删除结果
	 */
	@SuppressWarnings("deprecation")
	public String deleRecordByRecordId()
	{
		logger.debug("SoftUpgradRecordQueryACT-> deleRecordByRecordId()");
		String dbResult = "";
		// 有附件
		if (!StringUtil.IsEmpty(fileName))
		{
			// 删除DB数据
			dbResult = StringUtil.getStringValue(bio.deleRecordByRecordId(recordId));
			ServletRequest servletRequest = (ServletRequest) request;
			String storePath = servletRequest.getRealPath("/") + SYSTEM_PATH;
			// 删除文件结果
			String fileResult = bio.deleteFile(storePath + fileName);
			// 拼接结果页面展示
			ajax = dbResult + "|" + fileResult;
		}
		else
		{
			dbResult = StringUtil.getStringValue(bio.deleRecordByRecordId(recordId));
			// 拼接结果页面展示,-1标示不需要删除文件
			ajax = dbResult + "|" + "-1";
		}
		return AJAX_RESULT;
	}

	/**
	 * 新增软件升级记录
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String addSoftUpgradRecord()
	{
		logger.debug("SoftUpgradRecordQueryACT-> addSoftUpgradRecord()");

		// 插入公告
		if (!(StringUtil.IsEmpty(title)) && !(StringUtil.IsEmpty(content)))
		{
			String broadReslut = bio.insertBroadInfo(title, content);
			// 提醒插入公告失败 不插入软件升级记录,界面重新输入
			if (!"1".equals(broadReslut))
			{
				ajax = "3" + "|" + "-1";
				return DB_RESULT;
			}
		}
		// 时间转换
		starttime_add = setTime(starttime_add);
		endtime_add = setTime(endtime_add);
		ServletRequest servletRequest = (ServletRequest) request;
		// 是否需要上传文件
		boolean needUploadFile = file_path_add == null ? false : true;
		String dbResult = "";
		if (needUploadFile)
		{
			// 保存的路径
			String storePath = servletRequest.getRealPath("/") + SYSTEM_PATH;
			// 上传文件结果
			String fileResult = bio.saveFile(file_path_add, storePath, fileName);
			// 插入数据库
			dbResult = bio.addSoftUpgradRecord(vendor_add, device_model_add,
					currentVersion_add, targetVersion_add, upgradeRange_add,
					deviceCount_add, upgradeReason_add, upgradeMethod_add, starttime_add,
					endtime_add, contactWay_add, fileName);
			// 拼接结果页面展示
			ajax = dbResult + "|" + fileResult;
		}
		else
		{
			dbResult = bio.addSoftUpgradRecord(vendor_add, device_model_add,
					currentVersion_add, targetVersion_add, upgradeRange_add,
					deviceCount_add, upgradeReason_add, upgradeMethod_add, starttime_add,
					endtime_add, contactWay_add, "");
			// 拼接结果页面展示,-1标示不需要上传文件
			ajax = dbResult + "|" + "-1";
		}
		return DB_RESULT;
	}

	/**
	 * 根据recordId查询记录
	 * 
	 * @return 查询结果map
	 */
	public String findRecordByRecordId()
	{
		logger.debug("SoftUpgradRecordQueryACT-> findRecordByRecordId()");
		recordMap = bio.findRecordByRecordId(recordId);
		return MODIFY_RESULT;
	}

	/**
	 * 下载版本文件
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String download()
	{
		// 文件路径
		ServletRequest servletRequest = (ServletRequest) request;
		String storePath = servletRequest.getRealPath("/") + SYSTEM_PATH;
		bio.download(storePath + fileName, response);
		return null;
	}

	/**
	 * 时间转化
	 */
	private String setTime(String time)
	{
		logger.debug("setTime()" + time);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (!StringUtil.IsEmpty(time))
		{
			dt = new DateTimeUtil(time);
			time = StringUtil.getStringValue(dt.getLongTime());
			return time;
		}
		else
		{
			return "";
		}
	}

	public String getVendor()
	{
		return vendor;
	}

	public void setVendor(String vendor)
	{
		this.vendor = vendor;
	}

	public String getDevice_model()
	{
		return device_model;
	}

	public void setDevice_model(String device_model)
	{
		this.device_model = device_model;
	}

	public String getStarttime()
	{
		return starttime;
	}

	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	public String getVendor_add()
	{
		return vendor_add;
	}

	public void setVendor_add(String vendor_add)
	{
		this.vendor_add = vendor_add;
	}

	public String getDevice_model_add()
	{
		return device_model_add;
	}

	public void setDevice_model_add(String device_model_add)
	{
		this.device_model_add = device_model_add;
	}

	public String getCurrentVersion_add()
	{
		return currentVersion_add;
	}

	public void setCurrentVersion_add(String currentVersion_add)
	{
		this.currentVersion_add = currentVersion_add;
	}

	public String getTargetVersion_add()
	{
		return targetVersion_add;
	}

	public void setTargetVersion_add(String targetVersion_add)
	{
		this.targetVersion_add = targetVersion_add;
	}

	public String getUpgradeRange_add()
	{
		return upgradeRange_add;
	}

	public void setUpgradeRange_add(String upgradeRange_add)
	{
		this.upgradeRange_add = upgradeRange_add;
	}

	public String getDeviceCount_add()
	{
		return deviceCount_add;
	}

	public void setDeviceCount_add(String deviceCount_add)
	{
		this.deviceCount_add = deviceCount_add;
	}

	public String getUpgradeReason_add()
	{
		return upgradeReason_add;
	}

	public void setUpgradeReason_add(String upgradeReason_add)
	{
		this.upgradeReason_add = upgradeReason_add;
	}

	public String getUpgradeMethod_add()
	{
		return upgradeMethod_add;
	}

	public void setUpgradeMethod_add(String upgradeMethod_add)
	{
		this.upgradeMethod_add = upgradeMethod_add;
	}

	public String getStarttime_add()
	{
		return starttime_add;
	}

	public void setStarttime_add(String starttime_add)
	{
		this.starttime_add = starttime_add;
	}

	public String getEndtime_add()
	{
		return endtime_add;
	}

	public void setEndtime_add(String endtime_add)
	{
		this.endtime_add = endtime_add;
	}

	public String getContactWay_add()
	{
		return contactWay_add;
	}

	public void setContactWay_add(String contactWay_add)
	{
		this.contactWay_add = contactWay_add;
	}

	public File getFile_path_add()
	{
		return file_path_add;
	}

	public void setFile_path_add(File file_path_add)
	{
		this.file_path_add = file_path_add;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public void setBio(SoftUpgradRecordQueryBIO bio)
	{
		this.bio = bio;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getSoftUpgradRecordList()
	{
		return softUpgradRecordList;
	}

	@SuppressWarnings("rawtypes")
	public void setSoftUpgradRecordList(List<Map> softUpgradRecordList)
	{
		this.softUpgradRecordList = softUpgradRecordList;
	}

	public String getRecordId()
	{
		return recordId;
	}

	public void setRecordId(String recordId)
	{
		this.recordId = recordId;
	}

	@SuppressWarnings("rawtypes")
	public Map getRecordMap()
	{
		return recordMap;
	}

	@SuppressWarnings("rawtypes")
	public void setRecordMap(Map recordMap)
	{
		this.recordMap = recordMap;
	}

	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileName_modify()
	{
		return fileName_modify;
	}

	public void setFileName_modify(String fileName_modify)
	{
		this.fileName_modify = fileName_modify;
	}

	
	public String getTitle()
	{
		return title;
	}

	
	public String getContent()
	{
		return content;
	}

	
	public void setTitle(String title)
	{
		this.title = title;
	}

	
	public void setContent(String content)
	{
		this.content = content;
	}
	
}
