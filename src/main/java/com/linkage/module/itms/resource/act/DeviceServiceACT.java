
package com.linkage.module.itms.resource.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.resource.bio.DeviceServiceBIO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-9-9
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DeviceServiceACT extends splitPageAction implements ServletRequestAware,
		SessionAware
{

	private static Logger logger = LoggerFactory.getLogger(DeviceOUIInfoACT.class);
	private HttpServletRequest request;
	private Map session;
	private DeviceServiceBIO bio;
	private String ajax;
	// 开始时间
	private String startOpenDate = "";
	// 开始时间
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	// 厂商
	private String vendorId;
	// 型号
	private String modelId;
	// 版本
	private String deviceTypeId;
	// 设备类型
	private String rela_dev_type_id;
	private String city_id;
	// gw_type 1:家庭网关设备 2:企业网关设备
	private String gw_type;
	// 厂商文件列表
	private Map<String,String> vendorMap;
	// 设备列表
	private List<Map> deviceList;
	// 导出表
	private String tempTable;
	// 查看版本明细
	private String isflag;
	// 导出统计信息
	private String isprt;
	// 厂商id
	private String vid;
	// 型号id
	private String mid;
	// 版本id
	private String did;
	// 软件版本
	private String softwareversion;
	// 硬件版本
	private String hardwareversion;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）

	@Override
	public String execute() throws Exception
	{
		logger.debug("DeviceServiceACT=>execute()");
		vendorMap = bio.getVendor();
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "init";
	}

	/**
	 * 过滤操作
	 * 
	 * @return
	 */
	public String deviceServiceInfo()
	{
		logger.debug("DeviceServiceACT=>deviceServiceInfo()");
		this.setTime();
		UserRes curUser = (UserRes) session.get("curUser");
		String cityid = curUser.getCityId();
		ajax = bio.getHtmlDeviceByEdition(startOpenDate1, endOpenDate1, vendorId,
				modelId, deviceTypeId, rela_dev_type_id, gw_type, cityid, isprt);
		return "ajax";
	}

	/**
	 * 软件版本明细操作
	 * 
	 * @return
	 */
	public String detailVendor()
	{
		logger.debug("DeviceServiceACT=>detailVendor()");
		this.setTime();
		UserRes curUser = (UserRes) session.get("curUser");
		String cityid = curUser.getCityId();
		ajax = bio.detailVendor(startOpenDate1, endOpenDate1, vendorId, modelId,
				softwareversion, rela_dev_type_id, gw_type, cityid, isprt);
		return "ajax";
	}

	/**
	 * 导出过滤信息和导出软件版本明细信息
	 * 
	 * @return
	 */
	public String excelDevice()
	{
		logger.debug("DeviceServiceACT=>excelDevice()");
		this.setTime();
		UserRes curUser = (UserRes) session.get("curUser");
		String cityid = curUser.getCityId();
		if ("1".equals(isflag) && "1".equals(isprt))
		{
			tempTable = bio.detailVendor(startOpenDate1, endOpenDate1, vid, mid,
					softwareversion, rela_dev_type_id, gw_type, cityid, isprt);
		}
		else
		{
			tempTable = bio.getHtmlDeviceByEdition(startOpenDate1, endOpenDate1,
					vendorId, modelId, deviceTypeId, rela_dev_type_id, gw_type, cityid,
					isprt);
		}
		request.setAttribute("tempTable", tempTable);
		return "excelDevice";
	}

	/**
	 * 设备信息明细查询
	 * 
	 * @return
	 */
	public String DeviceVendorList()
	{
		logger.debug("DeviceServiceACT=>DeviceVendorList()");
		this.setTime();
		deviceList = bio.deviceVendorList(startOpenDate1, endOpenDate1, vendorId,
				softwareversion, hardwareversion, gw_type, city_id, curPage_splitPage,
				num_splitPage);
		maxPage_splitPage = bio.countDeviceVendorList(startOpenDate1, endOpenDate1,
				vendorId, softwareversion, hardwareversion, gw_type, city_id,
				curPage_splitPage, num_splitPage);
		return "devicelist";
	}

	/**
	 * 导出设备信息excel操作
	 * 
	 * @return
	 */
	public String excelDeviceListByEdition()
	{
		logger.debug("DeviceServiceACT=>excelDeviceListByEdition()");
		this.setTime();
		deviceList = bio.excelDeviceVendorList(startOpenDate1, endOpenDate1, vendorId,
				softwareversion, hardwareversion, gw_type, city_id);
		String excelCol = "vendor#devicemodel#softwareversion#city_name#device_serialnumber#area_name";
		String excelTitle = "设备厂商#型号#软件版本#属地#设备序列号#管理域";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "deviceList";
		data = deviceList;
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + startOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startOpenDate == null || "".equals(startOpenDate))
		{
			startOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate1 = String.valueOf(dt.getLongTime());
		}
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			endOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate1 = String.valueOf(dt.getLongTime());
		}
	}

	// 当前年的1月1号
	private String getStartDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	public void setServletRequest(HttpServletRequest req)
	{
		this.request = req;
	}

	public DeviceServiceBIO getBio()
	{
		return bio;
	}

	public void setBio(DeviceServiceBIO bio)
	{
		this.bio = bio;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getStartOpenDate()
	{
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate)
	{
		this.startOpenDate = startOpenDate;
	}

	public String getEndOpenDate()
	{
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}


	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getVid()
	{
		return vid;
	}

	public void setVid(String vid)
	{
		this.vid = vid;
	}

	public String getHardwareversion()
	{
		return hardwareversion;
	}

	public void setHardwareversion(String hardwareversion)
	{
		this.hardwareversion = hardwareversion;
	}

	public String getMid()
	{
		return mid;
	}

	public String getSoftwareversion()
	{
		return softwareversion;
	}

	public void setSoftwareversion(String softwareversion)
	{
		this.softwareversion = softwareversion;
	}

	public void setMid(String mid)
	{
		this.mid = mid;
	}

	public String getDid()
	{
		return did;
	}

	public void setDid(String did)
	{
		this.did = did;
	}

	public String getIsflag()
	{
		return isflag;
	}

	public void setIsflag(String isflag)
	{
		this.isflag = isflag;
	}

	public String getIsprt()
	{
		return isprt;
	}

	public void setIsprt(String isprt)
	{
		this.isprt = isprt;
	}

	public String getModelId()
	{
		return modelId;
	}

	public String getTempTable()
	{
		return tempTable;
	}

	public void setTempTable(String tempTable)
	{
		this.tempTable = tempTable;
	}

	public void setModelId(String modelId)
	{
		this.modelId = modelId;
	}

	public String getDeviceTypeId()
	{
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId)
	{
		this.deviceTypeId = deviceTypeId;
	}

	public String getRela_dev_type_id()
	{
		return rela_dev_type_id;
	}

	public void setRela_dev_type_id(String rela_dev_type_id)
	{
		this.rela_dev_type_id = rela_dev_type_id;
	}

	public String getGw_type()
	{
		return gw_type;
	}

	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	public String getCity_id()
	{
		return city_id;
	}

	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	public List<Map> getDeviceList()
	{
		return deviceList;
	}

	public void setDeviceList(List<Map> deviceList)
	{
		this.deviceList = deviceList;
	}

	
	public Map<String, String> getVendorMap()
	{
		return vendorMap;
	}

	
	public void setVendorMap(Map<String, String> vendorMap)
	{
		this.vendorMap = vendorMap;
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
}
