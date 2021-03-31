/**
 * 
 */

package com.linkage.module.itms.resource.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.itms.resource.bio.DeviceTypeInfoBIO;
import com.linkage.module.itms.resource.bio.DeviceTypeInfoExpBIO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;

/**
 * @author wuchao设备版本查询操作类
 */
public class DeviceTypeInfoExpACT extends splitPageAction implements SessionAware,ServletRequestAware
{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DeviceTypeInfoExpACT.class);
	/** 查询的所有数据列表 */
	
	
	// request取登陆帐号使用
	private HttpServletRequest request;
	private List<Map> deviceList;
	private DeviceTypeInfoExpBIO bio;
	private Map session;
	/** 厂家 */
	private int vendor = -1;
	/** 设备型号 */
	private int device_model = -1;
	/** 硬件版本 */
	private String hard_version;
	/** 软件型号 */
	private String soft_version;
	/** 是否审核 1是经过审核,0未审核 */
	private int is_check = -1;
	/** 设备类型 1是e8-b,2是机卡一体e8-c,3是机卡分离型 e8-c*/
	private int rela_dev_type = -1;
	/** 上行方式ID */
	private String typeId = null;
	/** 特定编号 */
	private String speversion;
	private String ajax;
	private long deviceTypeId;
	private int deleteID;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	/** 端口信息 */
	private String portInfo;
	/** 设备支持的协议*/
	private String servertype;
	/** 上行方式*/
	private int access_style_relay_id;
	/**设备Ip支持方式 ipv4 或ipv4和ipv6*/
	private String ipType;
	/**是否为规范版本*/
	private String isNormal;
	/**系统类型 1ITMS，2BBMS*/
	private String gw_type;
	/**终端规格*/
	private int spec_id;
	
	private long specId;
	
	private long detailSpecId;

	private List<Map<String, String>> specList = null;
	
	private String gwShare_fileName;
	
	private String gwShare_queryType;
	
	private String[] title;
	
	private String[] column;
	
	private List data = null;
	
	String fileName = null;

	//设备类型
	private List<Map<String, String>> devTypeMap;
	public String queryDeviceList()
	{ 
		devTypeMap = bio.getGwDevType();
		specList = bio.querySpecList();
		if("1".equals(gw_type)){
			return "itms";
		}
		else{
			return "bbms";
		}
		
	}
    public String dealTime(String time){
    	SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date str=new Date();
		try
		{
			str = date.parse(time);
		}
		catch (ParseException e)
		{
			logger.warn("选择开始或者结束的时间格式不对:"+time);
		}
		return str.getTime()/1000+"";
    }
    
    
	public String queryList()
	{
		if (startTime != null && !"".equals(startTime))
		{
			startTime=dealTime(startTime);
		}
		if (endTime != null && !"".equals(endTime))
		{
			endTime=dealTime(endTime);
		}
		deviceList = bio.queryDeviceList(vendor, device_model, hard_version,
				soft_version, is_check, rela_dev_type, curPage_splitPage, num_splitPage,startTime,endTime,access_style_relay_id,spec_id);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "queryList";
	}
	
	/**
	 * 下载获取设备版本详细信息
	 * 
	 * @return
	 */
	public String exportBatchExcelList() {
		logger.debug("exportBatchExcelList()");
		title = new String[] { "设备厂商", "设备型号", "特定版本","硬件版本","软件版本","是否审核","设备类型","上行方式", "终端规格" };
		column = new String[] { "vendor", "device_model", "specversion", "hard_version","soft_version","is_check",
				"rela_dev_type","access_style_relay_id","spec_name"};
	    fileName = "deviceVersionDetail";
		data = bio.queryExeclDeviceList(vendor, device_model, hard_version,soft_version, is_check, rela_dev_type,access_style_relay_id,spec_id );
		logger.warn("data="+data);
		return "excel";
	}
	
	public String batchImport()
	{
		logger.warn("batchImport init gwShare_fileName="+gwShare_fileName);
		List<String> elsData = new ArrayList<String>();
		//批量导入
		if (!StringUtil.IsEmpty(gwShare_fileName))
		{
			//批量文件导入
			String fileName_ = gwShare_fileName.substring(
					gwShare_fileName.length() - 3, gwShare_fileName.length());
			logger.warn("fileName_=="+fileName_);
			try {
				elsData = getImportDataByXLS(gwShare_fileName);
			} catch (FileNotFoundException e) {
				logger.warn("{}文件没找到!", fileName_);
				this.ajax = "文件没找到!";
				return "ajax";
			} catch (IOException e) {
				e.printStackTrace();
				logger.warn("ioe="+e.getMessage());
				logger.warn("{}文件读取出错！", fileName_);
				this.ajax = "{}文件读取出错！";
				return "ajax";
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("e="+e.getMessage());
				logger.warn("{}文件解析出错！", fileName_);
				this.ajax = "文件解析出错！";
				return "ajax";
			}
			ajax = bio.batchImportDevice(elsData);
		}
		return "ajax";
	}
	
	public String getFilePath() {
		//获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径",lipossHome);
		return lipossHome + "/temp/";
	}
	
	public List<String> getImportDataByXLS(String fileName) throws BiffException, IOException{
		logger.warn("getImportDataByXLS{}",fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath()+fileName);
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(f));
		// 获得sheet
		HSSFSheet sheet = workbook.getSheetAt(0);
		// 获得sheet的总行数
		int rowCount = sheet.getLastRowNum();
		logger.warn("@@rowCount="+rowCount);
		// 循环解析每一行，第一行不取
		for (int i = 1; i <= rowCount; i++) {
			// 获得行对象
			String vendor = "";
			String device_model = "";
			String speversion = "";
			String hard_version = "";
			String soft_version = "";
			String is_check = "";
			String rela_dev_type = "";
			String access_style_relay_id = "";
			String spec_name = "";
			
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell = row.getCell((short) 0);
			if (null != cell.getRichStringCellValue()) {
				vendor = StringUtil.getStringValue(cell.getRichStringCellValue());
				logger.warn("@@vendor="+vendor);
			}else{
				logger.warn("导入设备厂商有空值！");
			}
			HSSFCell cell1 = row.getCell((short) 1);
			if (null != cell1.getRichStringCellValue()) {
				device_model = StringUtil.getStringValue(cell1
						.getRichStringCellValue());
				logger.warn("@@device_model="+device_model);

			}else{
				logger.warn("导入设备型号有空值！");
			}
			HSSFCell cell2 = row.getCell((short) 2);
			if (null != cell2.getRichStringCellValue()) {
				speversion = StringUtil.getStringValue(cell2
						.getRichStringCellValue());
				logger.warn("@@speversion="+speversion);

			}else{
				logger.warn("导入特定版本有空值！");
			}
			HSSFCell cell3 = row.getCell((short) 3);
			if (null != cell2.getRichStringCellValue()) {
				hard_version = StringUtil.getStringValue(cell3
						.getRichStringCellValue());
				logger.warn("@@hard_version="+hard_version);
			}else{
				logger.warn("导入硬件版本有空值！");
			}
			HSSFCell cell4 = row.getCell((short) 4);
			if (null != cell4.getRichStringCellValue()) {
				soft_version = StringUtil.getStringValue(cell4
						.getRichStringCellValue());
				logger.warn("@@soft_version="+soft_version);
			}else{
				logger.warn("导入软件版本有空值！");
			}
			HSSFCell cell5 = row.getCell((short) 5);
			if (null != cell5.getRichStringCellValue()) {
				is_check = StringUtil.getStringValue(cell5
						.getRichStringCellValue());
				logger.warn("@@is_check="+is_check);

			}else{
				logger.warn("导入是否审核有空值！");
			}
			HSSFCell cell6 = row.getCell((short) 6);
			if (null != cell6.getRichStringCellValue()) {
				rela_dev_type = StringUtil.getStringValue(cell6
						.getRichStringCellValue());
				logger.warn("@@rela_dev_type="+rela_dev_type);
			}else{
				logger.warn("导入设备类型有空值！");
			}
			HSSFCell cell7 = row.getCell((short) 7);
			if (null != cell7.getRichStringCellValue()) {
				access_style_relay_id = StringUtil.getStringValue(cell7
						.getRichStringCellValue());
				logger.warn("@@access_style_relay_id="+access_style_relay_id);
			}else{
				logger.warn("导入上行方式有空值！");
			}
			HSSFCell cell8 = row.getCell((short) 8);
			if (null != cell8.getRichStringCellValue()) {
				spec_name = StringUtil.getStringValue(cell8
						.getRichStringCellValue());
				logger.warn("@@spec_name="+spec_name);
			}else{
				logger.warn("导入终端规格有空值！");
			}
			//初始化
			DeviceTypeUtil.init();
			logger.warn(vendor+";"+device_model+";"+speversion+";"+hard_version+";"+soft_version+";"+is_check+";"+rela_dev_type+";"+access_style_relay_id+";"+spec_name);
			logger.warn("#################"+DeviceTypeUtil.accessMap.get("e8-b"));
			list.add(DeviceTypeUtil.vendorMap.get(vendor) + ";" + DeviceTypeUtil.deviceModelMap.get(device_model)+ ";" +speversion + ";" + hard_version+ ";" + soft_version+ ";" + DeviceTypeUtil.checkMap.get(is_check)+ ";" +  DeviceTypeUtil.devTypeMap.get(rela_dev_type)+ ";" + DeviceTypeUtil.accessMap.get(access_style_relay_id)+ ";" + DeviceTypeUtil.specMap.get(spec_name));
			logger.warn("@@@@@list="+list);

		}
		f.delete();
		f = null;
		return list;
}
	
	public String getPortAndType(){
       ajax=bio.getPortAndType(deviceTypeId);
      return "ajax";
    }
	
	public String queryDetail(){
		logger.warn("!!detailSpecId= " +detailSpecId);
		deviceList = bio.queryDeviceDetail(deviceTypeId,detailSpecId);

		return "detail";
}

	public String deleteDevice()
	{
		logger.debug("into deleteDevice()");
		// 可以删除
		try
		{
			bio.deleteDevice(gw_type,deleteID);
			ajax = "1";
		}
		catch (Exception e)
		{
			ajax = "-1";
		}
		return "ajax";
	}

	/**
	 * add devicetype
	 * 
	 * @return
	 */
	public String addDevType()
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// 定制人
		long acc_oid = curUser.getUser().getId();

		if (deviceTypeId == -1)
		{
			ajax = bio.addDevTypeInfo(gw_type,vendor, device_model, speversion, hard_version,
					soft_version, is_check, rela_dev_type, typeId,portInfo,servertype,acc_oid,ipType,specId);
		}
		else   
		{
			ajax = bio.updateDevTypeInfo(gw_type,deviceTypeId, vendor, device_model, speversion,
										hard_version, soft_version, is_check, rela_dev_type, 
										typeId,portInfo,servertype,acc_oid,ipType,isNormal,specId);
		}
		return "ajax";
	}
	
	

	
	/**
	 * update is_check
	 * 
	 * @return
	 */
	public String updateIsCheck()
	{
		logger.debug("info updateIsCheck()");
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
	public String isNormalVersion(){
		logger.debug("isNormalVersion()");
		ajax = bio.isNormalVersion(device_model)+"";
		return "ajax";
	}
	public String getTypeNameList()
	{
		logger.debug("getTypeNameList()");
		ajax = bio.getTypeNameList(typeId);
		return "ajax";
	}

	/** getters and setters **/
	public DeviceTypeInfoExpBIO getBio()
	{
		return bio;
	}

	public void setBio(DeviceTypeInfoExpBIO bio)
	{
		this.bio = bio;
	}

	public int getDevice_model()
	{
		return device_model;
	}

	public void setDevice_model(int device_model)
	{
		this.device_model = device_model;
	}

	public List<Map> getDeviceList()
	{
		return deviceList;
	}

	public void setDeviceList(List<Map> deviceList)
	{
		this.deviceList = deviceList;
	}

	public String getHard_version()
	{
		return hard_version;
	}

	public void setHard_version(String hard_version)
	{
		try{
			this.hard_version = java.net.URLDecoder.decode(hard_version, "UTF-8");
			} catch (Exception e){
			this.hard_version = hard_version;
			}
	}

	public int getIs_check()
	{
		return is_check;
	}

	public void setIs_check(int is_check)
	{
		this.is_check = is_check;
	}

	public int getRela_dev_type()
	{
		return rela_dev_type;
	}

	public void setRela_dev_type(int rela_dev_type)
	{
		this.rela_dev_type = rela_dev_type;
	}

	public String getSoft_version()
	{
		return soft_version;
	}

	public void setSoft_version(String soft_version)
	{
		try{
			this.soft_version = java.net.URLDecoder.decode(soft_version, "UTF-8");
			} catch (Exception e){
			this.soft_version = soft_version;
			}
	}

	public int getVendor()
	{
		return vendor;
	}

	public void setVendor(int vendor)
	{
		this.vendor = vendor;
	}

	public Map getSession()
	{
		return session;
	}

	public String getSpeversion()
	{
		return speversion;
	}

	public void setSpeversion(String speversion)
	{
		try{
			this.speversion = java.net.URLDecoder.decode(speversion, "UTF-8");
			} catch (Exception e){
			this.speversion = speversion;
			}
	}

	public int getDeleteID()
	{
		return deleteID;
	}

	public void setDeleteID(int deleteID)
	{
		this.deleteID = deleteID;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public long getDeviceTypeId()
	{
		return deviceTypeId;
	}

	public void setDeviceTypeId(long deviceTypeId)
	{
		this.deviceTypeId = deviceTypeId;
	}

	public String getTypeId()
	{
		return typeId;
	}

	public void setTypeId(String typeId)
	{
		this.typeId = typeId;
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

	public void setSession(Map session)
	{
		this.session = session;
	}
	
	
	
	public String getPortInfo()
	{
		return portInfo;
	}
	
	public void setPortInfo(String portInfo)
	{
		try{
			this.portInfo = java.net.URLDecoder.decode(portInfo, "UTF-8");
			} catch (Exception e){
			this.portInfo = portInfo;
			}
	}
	
	
	
	public String getServertype()
	{
		return servertype;
	}

	public void setServertype(String servertype)
	{
		this.servertype = servertype;
	}
	
	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
		
	}
	
	public int getAccess_style_relay_id()
	{
		return access_style_relay_id;
	}
	
	public void setAccess_style_relay_id(int accessStyleRelayId)
	{
		access_style_relay_id = accessStyleRelayId;
	}
	public String getGw_type()
	{
		return gw_type;
	}
	
	public void setGw_type(String gwType)
	{
		gw_type = gwType;
	}
	public String getIpType() {
		return ipType;
	}
	public void setIpType(String ipType) {
		this.ipType = ipType;
	}
	public String getIsNormal() {
		return isNormal;
	}
	public void setIsNormal(String isNormal) {
		this.isNormal = isNormal;
	}
	public int getSpec_id() {
		return spec_id;
	}
	public void setSpec_id(int specId) {
		spec_id = specId;
	}
	public List<Map<String, String>> getSpecList() {
		return specList;
	}
	public List<Map<String, String>> getDevTypeMap() {
		return devTypeMap;
	}
	public void setDevTypeMap(List<Map<String, String>> devTypeMap) {
		this.devTypeMap = devTypeMap;
	}
	public void setSpecList(List<Map<String, String>> specList) {
		this.specList = specList;
	}
	public long getSpecId() {
		return specId;
	}
	public void setSpecId(long specId) {
		this.specId = specId;
	}
	public long getDetailSpecId() {
		return detailSpecId;
	}
	public void setDetailSpecId(long detailSpecId) {
		this.detailSpecId = detailSpecId;
	}
	public String getGwShare_fileName() {
		return gwShare_fileName;
	}
	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}
	public String getGwShare_queryType() {
		return gwShare_queryType;
	}
	public void setGwShare_queryType(String gwShare_queryType) {
		this.gwShare_queryType = gwShare_queryType;
	}
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}
	public String[] getTitle() {
		return title;
	}
	public void setTitle(String[] title) {
		this.title = title;
	}
	public String[] getColumn() {
		return column;
	}
	public void setColumn(String[] column) {
		this.column = column;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
