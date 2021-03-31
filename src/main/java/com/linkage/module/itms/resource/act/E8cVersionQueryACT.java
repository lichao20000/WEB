
package com.linkage.module.itms.resource.act;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.resource.bio.E8cVersionQueryBIO;

@SuppressWarnings("rawtypes")
public class E8cVersionQueryACT extends splitPageAction implements SessionAware,
		ServletRequestAware
{

	private static final long serialVersionUID = -7787526862261263533L;
	private static Logger logger = LoggerFactory
			.getLogger(E8cVersionQueryACT.class);
	private E8cVersionQueryBIO bio;
	private HttpServletRequest request;
	private Map session;
	// 厂商名称集合
	private List<Map<String, Object>> vendorList;
	// 设备型号集合
	private List<Map<String, Object>> devModelList;
	// 终端规格集合
	private List<Map<String, Object>> specList;
	// 厂商id
	private String vendor_id = "";
	// 设备型号
	private String device_model = "";
	// 设备类型
	private String device_type = "";
	// 设备类型集合
	private List<Map<String, String>> devTypeMap;
	// 终端规格
	private String spec_id = "";
	// 开始时间
	private String starttime = "";
	// 结束时间
	private String endtime = "";
	// 需要导出的结果集，必须是处理过的可以直接显示的结果
	private List<Map> data;
	// 需要导出的列名，对应data中的键值
	private String[] column;
	// 显示在导出文档上的列名
	private String[] title;
	// 导出的文件名（不包括后缀名）
	private String fileName;
	private String ajax = "";
	// 查询结果列表
	private List<Map> list = null;
	// 详细页面列表
	private List<Map> deviceList;
	// 详细页面设备类型
	private long deviceTypeId;
	// 详细页面终端规格
	private long detailSpecId;
	// 操作类型
	private String operType;
	// 操作人
	private long acc_id;
	// 服务器路径集合
	private List<Map<String, Object>> dirList;
	// 软件版本集合
	private List<Map<String, Object>> softVersionList;
	// 获取服务器路径
	private String dir_url;
	// 上传文件名
	private String uploadFileName;
	private File myFile;
	private String fullFileName;
	// 软件版本
	private String software_version;
	// 查询记录结果
	private List<Map> rlist = null;
	private String urlParameter;
	private File file1;
	private String resp;
	// 设备类型
	private String devicetype_id;
	// 服务器地址
	private String fileserver;
	// 下载路径
	private String filePath;
	private HttpServletResponse response;
	private String uploadName;

	/*
	 * 查询初始化
	 */
	public String init()
	{
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		vendorList = bio.getVendorList();
		devTypeMap = bio.getGwDevType();
		specList = bio.querySpecList();
		return "init";
	}

	/*
	 * 查询设备型号
	 */
	@SuppressWarnings("unchecked")
	public String getDevModelSelectList()
	{
		// 根据厂商获取型号
		devModelList = bio.getDeviceModel(vendor_id);
		ajax = bio.getSelectOptiones(devModelList, "device_model_id", "device_model");
		return "ajax";
	}

	/*
	 * e8-c规范版本结果查询
	 */
	public String getE8cVersionList()
	{
		if ("1".equals(operType))
		{
			list = bio.getE8cVersionList(curPage_splitPage, num_splitPage, vendor_id,
					device_model, device_type, spec_id, starttime, endtime);
			maxPage_splitPage = bio.getE8cVersionCount(curPage_splitPage, num_splitPage,
					vendor_id, device_model, device_type, spec_id, starttime, endtime);
		}
		else
		{
			list = bio.getE8cVersionOperList(curPage_splitPage, num_splitPage, vendor_id,
					device_model, device_type, spec_id, starttime, endtime);
			maxPage_splitPage = bio.getE8cVersionOperCount(curPage_splitPage,
					num_splitPage, vendor_id, device_model, device_type, spec_id,
					starttime, endtime);
		}
		if ("1".equals(operType))
		{
			return "queryList";
		}
		else if ("2".equals(operType))
		{
			return "checkList";
		}
		else
		{
			return "downList";
		}
	}

	/*
	 * 下载e8-c规范版本入库
	 */
	public String downE8cVersionFile()
	{
		String operType = "下载";
		DateTimeUtil dt = new DateTimeUtil();
		String downTime = dt.getLongDate();
		UserRes curUser = (UserRes) session.get("curUser");
		acc_id = curUser.getUser().getId();
		bio.saveDownE8cVersionRecord(devicetype_id, operType, acc_id, downTime,
				fullFileName);
		return "ajax";
	}

	/*
	 * 获取详细信息
	 */
	public String getDetailInfo()
	{
		deviceList = bio.getDetailInfo(deviceTypeId, detailSpecId);
		return "detail";
	}

	/*
	 * 改变审核状态
	 */
	public String updateIsCheck()
	{
		try
		{
			bio.updateIsCheck(deviceTypeId);
			ajax = "1";
		}
		catch (Exception e)
		{
			ajax = "-1";
		}
		return "ajax";
	}

	/*
	 * 上传文件初始化
	 */
	public String uploadinit()
	{
		vendorList = bio.getVendorList();
		return "uploadinit";
	}

	/*
	 * 获取软件版本
	 */
	public String getsoftVersionSelectList()
	{
		softVersionList = bio.getsoftVersion(device_model);
		ajax = bio.getSelectOptiones(softVersionList, "devicetype_id", "softwareversion");
		return "ajax";
	}

	/*
	 * 上传文件
	 */
	public String uploadLocalFile()
	{
		resp = bio.uploadLocalFile(urlParameter, resp, file1, uploadName, fileserver);
		String flag = resp.split("#")[0];
		if ("1".equals(flag))
		{
			String operType = "上传";
			DateTimeUtil dt = new DateTimeUtil();
			String uploadTime = dt.getLongDate();
			UserRes curUser = (UserRes) session.get("curUser");
			acc_id = curUser.getUser().getId();
			bio.saveUploadRecord(devicetype_id, operType, acc_id, uploadTime,
					fullFileName);
			bio.saveUploadFile(urlParameter, devicetype_id, fileserver);
		}
		return "response";
	}

	/*
	 * 文件操作页面初始化
	 */
	public String fileRecordinit()
	{
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		vendorList = bio.getVendorList();
		dirList = bio.getResourcePath();
		return "fileRecordinit";
	}

	/*
	 * 查询文件操作记录
	 */
	public String getFileOperRecordList()
	{
		rlist = bio.getFileOperRecordList(curPage_splitPage, num_splitPage,
				devicetype_id, operType, starttime, endtime);
		maxPage_splitPage = bio.getFileOperRecordCount(curPage_splitPage, num_splitPage,
				devicetype_id, operType, starttime, endtime);
		return "fileRecordlist";
	}

	/*
	 * 检验上传文件名称是否存在
	 */
	public String checkFillName()
	{
		ajax = bio.checkFillName(uploadFileName);
		return "ajax";
	}

	/*
	 * 检验版本是否已上传
	 */
	public String checkSoftVersion()
	{
		ajax = bio.checkSoftVersion(devicetype_id);
		return "ajax";
	}

	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public E8cVersionQueryBIO getBio()
	{
		return bio;
	}

	public void setBio(E8cVersionQueryBIO bio)
	{
		this.bio = bio;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public List<Map<String, Object>> getVendorList()
	{
		return vendorList;
	}

	public void setVendorList(List<Map<String, Object>> vendorList)
	{
		this.vendorList = vendorList;
	}

	public List<Map<String, Object>> getDevModelList()
	{
		return devModelList;
	}

	public void setDevModelList(List<Map<String, Object>> devModelList)
	{
		this.devModelList = devModelList;
	}

	public String getVendor_id()
	{
		return vendor_id;
	}

	public void setVendor_id(String vendor_id)
	{
		this.vendor_id = vendor_id;
	}

	public List<Map> getData()
	{
		return data;
	}

	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public String[] getColumn()
	{
		return column;
	}

	public void setColumn(String[] column)
	{
		this.column = column;
	}

	public String[] getTitle()
	{
		return title;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public List<Map> getList()
	{
		return list;
	}

	public void setList(List<Map> list)
	{
		this.list = list;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public static Logger getLogger()
	{
		return logger;
	}

	public String getDevice_model()
	{
		return device_model;
	}

	public void setDevice_model(String device_model)
	{
		this.device_model = device_model;
	}

	public String getDevice_type()
	{
		return device_type;
	}

	public void setDevice_type(String device_type)
	{
		this.device_type = device_type;
	}

	public String getSpec_id()
	{
		return spec_id;
	}

	public void setSpec_id(String spec_id)
	{
		this.spec_id = spec_id;
	}

	public String getStarttime()
	{
		return starttime;
	}

	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	public List<Map<String, Object>> getSpecList()
	{
		return specList;
	}

	public void setSpecList(List<Map<String, Object>> specList)
	{
		this.specList = specList;
	}

	public List<Map> getDeviceList()
	{
		return deviceList;
	}

	public void setDeviceList(List<Map> deviceList)
	{
		this.deviceList = deviceList;
	}

	public long getDeviceTypeId()
	{
		return deviceTypeId;
	}

	public void setDeviceTypeId(long deviceTypeId)
	{
		this.deviceTypeId = deviceTypeId;
	}

	public long getDetailSpecId()
	{
		return detailSpecId;
	}

	public void setDetailSpecId(long detailSpecId)
	{
		this.detailSpecId = detailSpecId;
	}

	public List<Map<String, String>> getDevTypeMap()
	{
		return devTypeMap;
	}

	public void setDevTypeMap(List<Map<String, String>> devTypeMap)
	{
		this.devTypeMap = devTypeMap;
	}

	public String getOperType()
	{
		return operType;
	}

	public void setOperType(String operType)
	{
		this.operType = operType;
	}

	public long getAcc_id()
	{
		return acc_id;
	}

	public void setAcc_id(long acc_id)
	{
		this.acc_id = acc_id;
	}

	public List<Map<String, Object>> getDirList()
	{
		return dirList;
	}

	public void setDirList(List<Map<String, Object>> dirList)
	{
		this.dirList = dirList;
	}

	public List<Map<String, Object>> getSoftVersionList()
	{
		return softVersionList;
	}

	public void setSoftVersionList(List<Map<String, Object>> softVersionList)
	{
		this.softVersionList = softVersionList;
	}

	public String getDir_url()
	{
		return dir_url;
	}

	public void setDir_url(String dir_url)
	{
		this.dir_url = dir_url;
	}

	public String getUploadFileName()
	{
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName)
	{
		this.uploadFileName = uploadFileName;
	}

	public File getMyFile()
	{
		return myFile;
	}

	public void setMyFile(File myFile)
	{
		this.myFile = myFile;
	}

	public String getFullFileName()
	{
		return fullFileName;
	}

	public void setFullFileName(String fullFileName)
	{
		this.fullFileName = fullFileName;
	}

	public String getSoftware_version()
	{
		return software_version;
	}

	public void setSoftware_version(String software_version)
	{
		this.software_version = software_version;
	}

	public List<Map> getRlist()
	{
		return rlist;
	}

	public void setRlist(List<Map> rlist)
	{
		this.rlist = rlist;
	}

	public String getUrlParameter()
	{
		return urlParameter;
	}

	public void setUrlParameter(String urlParameter)
	{
		this.urlParameter = urlParameter;
	}

	public File getFile1()
	{
		return file1;
	}

	public void setFile1(File file1)
	{
		this.file1 = file1;
	}

	public String getDevicetype_id()
	{
		return devicetype_id;
	}

	public void setDevicetype_id(String devicetype_id)
	{
		this.devicetype_id = devicetype_id;
	}

	public String getFileserver()
	{
		return fileserver;
	}

	public void setFileserver(String fileserver)
	{
		this.fileserver = fileserver;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public String getResp()
	{
		return resp;
	}

	public void setResp(String resp)
	{
		this.resp = resp;
	}

	public HttpServletResponse getResponse()
	{
		return response;
	}

	public void setResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	public String getUploadName()
	{
		return uploadName;
	}

	public void setUploadName(String uploadName)
	{
		this.uploadName = uploadName;
	}
}