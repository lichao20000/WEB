
package com.linkage.module.gtms.stb.config.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.config.bio.BatchCustomNodeConfigBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class BatchCustomNodeConfigACT extends splitPageAction
{

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(BatchCustomNodeConfigACT.class);
	/** 任务名 */
	private String taskName = "";
	/** 厂商 */
	private String vendorId = "";
	/** 型号 */
	private String deviceModelIds = "";
	/** 软件版本 */
	private String deviceTypeIds = "";
	/** 属地 */
	private String cityId = "";
	/** 是否启用IP地址段 */
	private String ipCheck = "1";
	/** 是否启用MAC地址段 */
	private String macCheck = "1";
	/** IP地址段 */
	private String ipSG = "";
	/** MAC地址段 */
	private String macSG = "";
	/** 手工输入的用户信息 */
	private String custSG = "";
	/** 节点路径 */
	private String paramNodePath = "";
	/** 节点值 */
	private String paramValue = "";
	/** 节点类型 */
	private String paramType = "";
	private String ajax = "";
	private InputStream exportExcelStream;
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
	private BatchCustomNodeConfigBIO bio;
	// 页面分类 1.单台操作 2.批量操作
	private String flag;
	private String param;
	private String deviceIds;
	private String type;

	/**
	 * @author zhangsibei 2013年12月20日 初始化批量配置节点页面
	 * @return
	 */
	public String initBatchConfig()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		this.cityList = CityDAO.getNextCityListByCityPid(curUser.getUser().getCityId());
		this.vendorList = bio.getVendorList();
		if(LipossGlobals.inArea(Global.JXDX)){
			if("1".equals(type)){
				return "initBatchConfigNode4XJ_Ipoe";
			}else{
				return "initBatchConfigNode4XJ";
			}
		}
		return "initBatchConfigNode";
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
	 * @author zhangsibei 2013年12月20日 批量参数配置
	 * @return
	 */
	public String importConfig()
	{
		logger.warn("importConfig({},{},{},{},{},{})", new Object[] { cityId, vendorId,
				taskName, deviceModelIds, deviceTypeIds, paramNodePath });
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		long currTime = new Date().getTime() / 1000L;
		long taskId = currTime;
		long addTime = currTime;
		if (btnValue4IP.equals("byBatchIP"))
		{
			ipSG = getIPString();
		}
		if (btnValue4MAC.equals("byBatchMAC"))
		{
			macSG = getMACString();
		}
		
		logger.warn("btnValue4Customer:[{}]", btnValue4Customer);
		
		
		if("byBatchCust".equals(btnValue4Customer)){
			custSG = "byBatchCust";
		}else{
			
		}
		logger.warn("vendorId:[{}]",vendorId);
		logger.warn("deviceModelIds:[{}]",deviceModelIds);
		logger.warn("deviceTypeIds:[{}]",deviceTypeIds);
		
		
		ajax = bio.batchNodeConfig(taskId, cityId, vendorId, ipCheck, macCheck, taskName,
				acc_oid, addTime, ipSG, macSG,custSG,uploadCustomer,uploadFileName4Customer, deviceModelIds, deviceTypeIds,
				paramNodePath, paramValue, paramType, type);
		return "ajax";
	}

	/**
	 * 解析导入文件
	 * 
	 * @return
	 */
	public String getIPString()
	{
		logger.warn("getIPList");
		String targetDirectory = ServletActionContext.getServletContext().getRealPath(
				"/upload");
		return bio.getIPString(targetDirectory, uploadFileName4IP, uploadIP);
	}

	/**
	 * 解析导入文件
	 * 
	 * @return
	 */
	public String getMACString()
	{
		logger.warn("getMACList");
		String targetDirectory = ServletActionContext.getServletContext().getRealPath(
				"/upload");
		return bio.getIPString(targetDirectory, uploadFileName4MAC, uploadMAC);
	}

	/**
	 * 下载模版
	 * 
	 * @return
	 */
	public String downloadTemplate()
	{
		return "toExport";
	}

	public String getTaskName()
	{
		return taskName;
	}

	/**
	 * ITV零配置新增节点页面初始化
	 * 
	 * @return String
	 */
	public String initCfgByITV()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		this.cityList = CityDAO.getNextCityListByCityPid(curUser.getUser().getCityId());
		this.vendorList = bio.getVendorList();
		return "initBatchCfgByITV";
	}

	/**
	 * ITV零配置新增节点
	 * 
	 * @return
	 */
	public String doConfigByITV()
	{
		if ("1".equals(flag))
		{
			try
			{
				// 业务id
				String serviceId = "7001";
				String[] paramNodePaths = paramNodePath.split(",");
				String[] paramValues = paramValue.split(",");
				String[] paramTypes = paramType.split(",");
				int len = paramNodePaths.length;
				String[] paramArr = new String[len - 1];
				for (int i = 0; i < len - 1; i++)
				{
					paramArr[i] = paramNodePaths[i + 1] + "ailk!@#" + paramValues[i + 1]
							+ "ailk!@#" + paramTypes[i + 1];
				}
				if (true == StringUtil.IsEmpty(deviceIds))
				{
					logger.debug("任务中没有设备");
					ajax = "任务中没有设备";
				}
				// 直接传deviceId数组调配置模块接口
				else if (!"0".equals(deviceIds))
				{
					logger.warn("批量参数配置小于50，直接传deviceId数组调配置模块接口");
					String[] deviceId_array = null;
					deviceId_array = deviceIds.split(",");
					ajax = bio.singleNodeConfigByITV(deviceId_array, serviceId, paramArr);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.warn("Exception---" + e.getMessage());
				return "ajax";
			}
			return "ajax";
		}
		else
		{
			UserRes curUser = WebUtil.getCurrentUser();
			long acc_oid = curUser.getUser().getId();
			long currTime = new Date().getTime() / 1000L;
			long taskId = currTime;
			long addTime = currTime;
			ajax = bio.batchNodeConfigByITV(taskId, cityId, vendorId, taskName, acc_oid,
					addTime, deviceModelIds, deviceTypeIds, paramNodePath, paramValue,
					paramType);
			return "result";
		}
	}

	/**
	 * 查询策略表中未作的数量，超过一定数值就不做了
	 * 
	 * @return String
	 */
	public String queryUndoNum()
	{
		logger.debug("queryUndoNum()");
		ajax = String.valueOf(bio.queryUndoNum());
		return "ajax";
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

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public List getVendorList()
	{
		return vendorList;
	}

	public void setVendorList(List vendorList)
	{
		this.vendorList = vendorList;
	}

	public void setBio(BatchCustomNodeConfigBIO bio)
	{
		this.bio = bio;
	}

	public String getDeviceTypeIds()
	{
		return deviceTypeIds;
	}

	public void setDeviceTypeIds(String deviceTypeIds)
	{
		this.deviceTypeIds = deviceTypeIds;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
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

	public String getParamNodePath()
	{
		return paramNodePath;
	}

	public void setParamNodePath(String paramNodePath)
	{
		this.paramNodePath = paramNodePath;
	}

	public String getParamValue()
	{
		return paramValue;
	}

	public void setParamValue(String paramValue)
	{
		this.paramValue = paramValue;
	}

	public String getParamType()
	{
		return paramType;
	}

	public void setParamType(String paramType)
	{
		this.paramType = paramType;
	}

	public File getUploadMAC()
	{
		return uploadMAC;
	}

	public void setUploadMAC(File uploadMAC)
	{
		this.uploadMAC = uploadMAC;
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

	public String getFlag()
	{
		return flag;
	}

	public void setFlag(String flag)
	{
		this.flag = flag;
	}

	public void setUploadFileName4IP(String uploadFileName4IP)
	{
		byte[] tempName;
		String newName = "";
		try
		{
			tempName = uploadFileName4IP.getBytes("GBK");
			newName = new String(tempName, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		this.uploadFileName4IP = newName;
	}

	public String getUploadFileName4MAC()
	{
		return uploadFileName4MAC;
	}

	public void setUploadFileName4MAC(String uploadFileName4MAC)
	{
		byte[] tempName;
		String newName = "";
		try
		{
			tempName = uploadFileName4MAC.getBytes("GBK");
			newName = new String(tempName, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		this.uploadFileName4MAC = newName;
	}

	public void setExportExcelStream(InputStream exportExcelStream)
	{
		this.exportExcelStream = exportExcelStream;
	}

	// 下载模版
	public InputStream getExportExcelStream()
	{
		FileInputStream fio = null;
		String path = ServletActionContext.getServletContext().getRealPath("/");
		String separa = System.getProperty("file.separator");
		String filePath = path + "stb" + separa + "config" + separa
				+ "batchConfigTemplate.xls";
		try
		{
			fio = new FileInputStream(filePath);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			logger.warn("读取文件异常", e);
		}
		return fio;
	}
	
	/**
	 * @author chenxj
	 * @return
	 */
	  public String initForEdition()
	  {
	    UserRes curUser = WebUtil.getCurrentUser();
	    this.cityList = CityDAO.getNextCityListByCityPid(curUser.getUser().getCityId());
	    this.vendorList = this.bio.getVendorList();
	    return "initForEdition";
	  }

	public String getParam()
	{
		return param;
	}

	public void setParam(String param)
	{
		this.param = param;
	}

	public String getDeviceIds()
	{
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
	}

	public String getCustSG() {
		return custSG;
	}

	public void setCustSG(String custSG) {
		this.custSG = custSG;
	}

	public String getBtnValue4Customer() {
		return btnValue4Customer;
	}

	public void setBtnValue4Customer(String btnValue4Customer) {
		this.btnValue4Customer = btnValue4Customer;
	}

	public File getUploadCustomer() {
		return uploadCustomer;
	}

	public void setUploadCustomer(File uploadCustomer) {
		this.uploadCustomer = uploadCustomer;
	}

	public String getUploadFileName4Customer() {
		return uploadFileName4Customer;
	}

	public void setUploadFileName4Customer(String uploadFileName4Customer) {
		this.uploadFileName4Customer = uploadFileName4Customer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
