
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
import com.linkage.module.itms.resource.bio.DeviceTypeVoiceQueryBIO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-29
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DeviceTypeVoiceQueryACT extends splitPageAction implements
		ServletRequestAware, SessionAware
{

	private static Logger logger = LoggerFactory
			.getLogger(DeviceTypeVoiceQueryACT.class);
	private HttpServletRequest request;
	private Map session;
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
	
	private String numInfo;
	// 厂商文件列表
	private Map<String, String> vendorMap;
	// 型号统计信息
	private List<Map> deviceVoiceList;
	private List<Map> deviceList;
	private DeviceTypeVoiceQueryBIO bio;
	private String ajax;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）

	@Override
	public String execute() throws Exception
	{
		logger.debug("DeviceServiceACT=>execute()");
		vendorMap = bio.getVendor();
		endOpenDate = getEndDate();
		return "init";
	}

	public String deviceVoiceInfo()
	{
		this.setTime();
		deviceVoiceList = bio.deviceVoiceInfo(vendorId, modelId, startOpenDate1,
				endOpenDate1);
		return "list";
	}

	public String deviceVoiceListInfo()
	{
		this.setTime();
		deviceList = bio.voiceDeviceQueryInfo(vendorId, modelId, startOpenDate1,
				endOpenDate1,numInfo, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countVoiceDeviceQueryInfo(vendorId, modelId,
				startOpenDate1, endOpenDate1,numInfo, curPage_splitPage, num_splitPage);
		return "devicelist";
	}

	public String deviceVoiceExcel()
	{
		this.setTime();
		deviceVoiceList = bio.deviceVoiceInfo(vendorId, modelId, startOpenDate1,
				endOpenDate1);
		String excelCol = "modelType#lineOneNum#lineOneNoNum#lineTwoNum#lineTwoNoNum#lineOneTwoNoNum";
		String excelTitle = "设备型号#语音端口1总数#语音端口1未启用总数#语音端口2总数#语音端口2未启用总数#语音端口1和语音端口2同时未启用总数";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "deviceVoiceList";
		data = deviceVoiceList;
		return "excel";
	}

	public String voiceDeviceQueryExcel()
	{
		this.setTime();
		deviceList = bio.voiceDeviceQueryExcel(vendorId, modelId, startOpenDate1,
				endOpenDate1,numInfo);
		String excelCol = "device_model#loid#device_serialnumber#device_type#enabled#voip_phone#status#reason";
		String excelTitle = "设备型号#LOID#终端序列号#终端型号#语音端口是否启用#语音端口号码#语音注册成功状态#语音注册失败原因";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "deviceList";
		data = deviceList;
		return "excel";
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM", Locale.US);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + endOpenDate);
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			startOpenDate1 = null;
			endOpenDate1 = null;
		}
		else
		{
			String start = endOpenDate + "-01 00:00:00";
			DateTimeUtil st = new DateTimeUtil(start);
			startOpenDate1 = String.valueOf(st.getLongTime());
			String end = st.getLastDayOfMonth() + " 23:59:59";
			DateTimeUtil et = new DateTimeUtil(end);
			endOpenDate1 = String.valueOf(et.getLongTime());
			logger.warn("开始时间：" + start + "  结束时间：" + end);
		}
	}

	@Override
	public void setSession(Map<String, Object> session)
	{
		// TODO Auto-generated method stub
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		// TODO Auto-generated method stub
		this.request = request;
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

	public String getModelId()
	{
		return modelId;
	}

	public void setModelId(String modelId)
	{
		this.modelId = modelId;
	}

	public Map<String, String> getVendorMap()
	{
		return vendorMap;
	}

	public void setVendorMap(Map<String, String> vendorMap)
	{
		this.vendorMap = vendorMap;
	}

	public DeviceTypeVoiceQueryBIO getBio()
	{
		return bio;
	}

	public void setBio(DeviceTypeVoiceQueryBIO bio)
	{
		this.bio = bio;
	}

	public String getAjax()
	{
		return ajax;
	}

	
	public String getNumInfo()
	{
		return numInfo;
	}

	
	public void setNumInfo(String numInfo)
	{
		this.numInfo = numInfo;
	}

	public List<Map> getDeviceVoiceList()
	{
		return deviceVoiceList;
	}

	public void setDeviceVoiceList(List<Map> deviceVoiceList)
	{
		this.deviceVoiceList = deviceVoiceList;
	}

	public List<Map> getDeviceList()
	{
		return deviceList;
	}

	public void setDeviceList(List<Map> deviceList)
	{
		this.deviceList = deviceList;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
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
