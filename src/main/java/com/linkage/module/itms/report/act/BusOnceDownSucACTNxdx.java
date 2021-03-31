
package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.report.bio.BusOnceDownSucBIONxdx;

/**
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-8-6
 * @category com.linkage.module.itms.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BusOnceDownSucACTNxdx extends splitPageAction implements SessionAware
{

	private static Logger logger = LoggerFactory
			.getLogger(BusOnceDownSucACTNxdx.class);
	private Map session;
	/** 属地 */
	private String cityId;
	/** 开始时间 */
	private String starttime;
	/** 结束时间 */
	private String endtime;
	/** 开始时间转化后 */
	private String starttime1;
	/** 结束时间转化后 */
	private String endtime1;
	/** 业务类型Id */
	private String servTypeId;
	/** 业务信息信息 */
	private List<Map> servInfoList;
	/** 导出数据 */
	private List<Map<String, Object>> data;
	/** 导出文件列标题 */
	private String[] title;
	/** 导出文件列 */
	private String[] column;
	/** 导出文件名 */
	private String fileName;
	private String gwType;
	/** 厂商id */
	private String gwShare_vendorId;
	/** 型号id */
	private String gwShare_deviceModelId;
	/** 版本id */
	private String gwShare_devicetypeId;
	/** 厂商名称 */
	private String vendorName;
	/** 软件版本 */
	private String softwareversion;
	/** 硬件版本 */
	private String hardwareversion;
	/** 型号 */
	private String deviceModel;
	private BusOnceDownSucBIONxdx busOnceDownSucBIO;

	/**
	 * 查询成功率
	 * 
	 * @return
	 */
	public String queryList()
	{
		logger.debug("queryList({},{},{})", new Object[] { cityId, starttime, endtime });
		logger.warn("查询成功率:starttime=" + starttime + "   endtime=" + endtime
				+ "  cityId=" + cityId);
		this.setTime();
		if ("4".equals(gwType))
		{
			data = busOnceDownSucBIO
					.queryStbDataList(cityId, starttime1, endtime1, gwType,
							gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId);
			return "stblist";
		}
		else
		{
			data = busOnceDownSucBIO.queryDataList(cityId, starttime1, endtime1, gwType,
					gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId);
			return "list";
		}
	}

	/**
	 * 导出查询的数据
	 * 
	 * @return
	 */
	public String getAllResultExcel()
	{
		logger.debug("getAllResultExcel({},{},{})", new Object[] { cityId, starttime1,
				endtime1 });
		fileName = "业务一次下发成功率统计";
		if ("4".equals(gwType))
		{
			title = new String[] { "本地网", "机顶盒下发成功率" };
			column = new String[] { "cityName", "totalSucRate" };
			data = busOnceDownSucBIO
					.queryStbDataList(cityId, starttime1, endtime1, gwType,
							gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId);
		}
		else
		{
			title = new String[] { "本地网", "宽带一次下发成功率", "IPTV一次下发成功率", "voip一次下发成功率",
					"总下发成功率" };
			column = new String[] { "cityName", "broadbandSucRate", "iptvSucRate",
					"voipSucRate", "totalSucRate" };
			data = busOnceDownSucBIO.queryDataList(cityId, starttime1, endtime1, gwType,
					gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId);
		}
		return "excel";
	}

	/**
	 * 获取业务信息的详细信息
	 * 
	 * @return
	 */
	public String getServInfoDetail()
	{
		logger.debug("getServInfoDetail({},{},{},{})", new Object[] { cityId, starttime1,
				endtime1, servTypeId });
		if ("4".equals(gwType))
		{
			servInfoList = busOnceDownSucBIO.getStbServInfoDetail(cityId, starttime1,
					endtime1, servTypeId, curPage_splitPage, num_splitPage, gwType,
					gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId);
			maxPage_splitPage = busOnceDownSucBIO.getServInfoCount(cityId, starttime1,
					endtime1, servTypeId, curPage_splitPage, num_splitPage, gwType,
					gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId);
			return "serInfoStbList";
		}
		else
		{
			servInfoList = busOnceDownSucBIO.getServInfoDetail(cityId, starttime1,
					endtime1, servTypeId, curPage_splitPage, num_splitPage, gwType,
					gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId);
			maxPage_splitPage = busOnceDownSucBIO.getServInfoCount(cityId, starttime1,
					endtime1, servTypeId, curPage_splitPage, num_splitPage, gwType,
					gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId);
			return "serInfoList";
		}
	}

	/**
	 * 导出业务信息详细信息
	 * 
	 * @return
	 */
	public String getServInfoExcel()
	{
		logger.debug("getServInfoExcel({},{},{},{})", new Object[] { cityId, starttime1,
				endtime1, servTypeId });
		fileName = "busOnceDownSuccessRate";
		if ("4".equals(gwType))
		{
			title = new String[] { "设备序列号", "受理时间", "业务账号", "业务开通状态" };
			column = new String[] { "deviceSerialnumber", "dealdate", "username",
					"openStatus" };
			data = busOnceDownSucBIO.getStbServInfoExcel(cityId, starttime1, endtime1,
					servTypeId, gwType, gwShare_vendorId, gwShare_deviceModelId,
					gwShare_devicetypeId);
		}
		else
		{
			title = new String[] { "设备序列号", "逻辑SN", "受理时间", "业务名称", "业务账号", "业务开通状态" };
			column = new String[] { "deviceSerialnumber", "logicSN", "dealdate",
					"servType", "username", "openStatus" };
			data = busOnceDownSucBIO.getServInfoExcel(cityId, starttime1, endtime1,
					servTypeId, gwType, gwShare_vendorId, gwShare_deviceModelId,
					gwShare_devicetypeId);
		}
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime))
		{
			starttime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime))
		{
			endtime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
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

	public String getStarttime1()
	{
		return starttime1;
	}

	public void setStarttime1(String starttime1)
	{
		this.starttime1 = starttime1;
	}

	public String getEndtime1()
	{
		return endtime1;
	}

	public void setEndtime1(String endtime1)
	{
		this.endtime1 = endtime1;
	}

	public List<Map<String, Object>> getData()
	{
		return data;
	}

	public void setData(List<Map<String, Object>> data)
	{
		this.data = data;
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

	public String getGwType()
	{
		return gwType;
	}

	public void setGwType(String gwType)
	{
		this.gwType = gwType;
	}

	public String getServTypeId()
	{
		return servTypeId;
	}

	public void setServTypeId(String servTypeId)
	{
		this.servTypeId = servTypeId;
	}

	public List<Map> getServInfoList()
	{
		return servInfoList;
	}

	public void setServInfoList(List<Map> servInfoList)
	{
		this.servInfoList = servInfoList;
	}

	public BusOnceDownSucBIONxdx getBusOnceDownSucBIO()
	{
		return busOnceDownSucBIO;
	}

	public void setBusOnceDownSucBIO(BusOnceDownSucBIONxdx busOnceDownSucBIO)
	{
		this.busOnceDownSucBIO = busOnceDownSucBIO;
	}

	public String getGwShare_vendorId()
	{
		return gwShare_vendorId;
	}

	public void setGwShare_vendorId(String gwShare_vendorId)
	{
		this.gwShare_vendorId = gwShare_vendorId;
	}

	public String getGwShare_deviceModelId()
	{
		return gwShare_deviceModelId;
	}

	public void setGwShare_deviceModelId(String gwShare_deviceModelId)
	{
		this.gwShare_deviceModelId = gwShare_deviceModelId;
	}

	public String getGwShare_devicetypeId()
	{
		return gwShare_devicetypeId;
	}

	public void setGwShare_devicetypeId(String gwShare_devicetypeId)
	{
		this.gwShare_devicetypeId = gwShare_devicetypeId;
	}

	public String getVendorName()
	{
		return vendorName;
	}

	public void setVendorName(String vendorName)
	{
		this.vendorName = vendorName;
	}

	public String getSoftwareversion()
	{
		return softwareversion;
	}

	public void setSoftwareversion(String softwareversion)
	{
		this.softwareversion = softwareversion;
	}

	public String getHardwareversion()
	{
		return hardwareversion;
	}

	public void setHardwareversion(String hardwareversion)
	{
		this.hardwareversion = hardwareversion;
	}

	public String getDeviceModel()
	{
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel)
	{
		this.deviceModel = deviceModel;
	}
}
