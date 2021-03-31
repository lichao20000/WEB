
package com.linkage.module.gwms.resource.act;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.resource.bio.CountDeviceBIO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2012-3-6 上午09:10:58
 * @category com.linkage.module.gwms.resource.act
 * @copyright 南京联创科技 网管科技部
 */
public class CountDeviceACT extends splitPageAction implements SessionAware
{

	private static Logger logger = LoggerFactory.getLogger(CountDeviceACT.class);
	private CountDeviceBIO bio;
	private Map session;
	// private String cityId;
	private List<Map> cityList;
	private String vendor;
	private String devicemodel;
	private String devicetype;
	private String rela_dev_type;
	private String is_check;
	private String startTime;
	private String endTime;
	private String protocol2;
	private String protocol1;
	private String protocol0;
	/**
	 * 查询结果的标题
	 */
	private List<String> titleList = null;
	/**
	 * 查询的结果
	 */
	private List dataList = null;
	private String cityId;
	private List detailDataList = null;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private String excel;
	private List<String> totalNumList = null;
	private String filterDevicetype;
	private String queryTime;

	public String execute()
	{
		// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(calendar.getTimeInMillis() - 24 * 3600 * 1000);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		String second1 = "";
		if (second < 10)
		{
			second1 += "0" + second;
		}
		else
		{
			second1 = "" + second;
		}
		String minute1 = "";
		if (minute < 10)
		{
			minute1 += "0" + minute;
		}
		else
		{
			minute1 += "" + minute;
		}
		queryTime = "" + year + "-" + month + "-" + day + " " + hour + ":" + minute1
				+ ":" + second1;
		return "success";
	}

	public String countDevice()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		// userCityId="0200";
		cityList = bio.queryCityName(userCityId);
		// List cityTempList = bio.getCityList(cityId);
		this.titleList = new ArrayList<String>();
		this.titleList.add("厂商");
		this.titleList.add("设备型号");
		this.titleList.add("软件版本(硬件版本)");
		this.titleList.add("语音协议");
		this.titleList.add("版本首次上线时间");
		this.titleList.add("是否规范");
		for (int i = 0; i < cityList.size(); i++)
		{
			this.titleList.add(String.valueOf(((Map) cityList.get(i)).get("city_name")));
		}
		// if(cityId.equals(userCityId)){
		this.titleList.add("小计");
		// }
		List<String> list = bio.filterDevice(userCityId, vendor, devicemodel, devicetype,
				protocol2, protocol1, protocol0);
		List<Map> devicetypeList = bio.getDevicetype(list, is_check, rela_dev_type,
				startTime, endTime);
		// getData(startTime,endTime,cityTempList,cityId,userCityId);
		this.dataList = bio.getData(devicetypeList, cityList, userCityId);
		this.totalNumList = bio.getEndAll(dataList, cityList, devicetypeList);
		// logger.warn("excel:"+excel);
		if ("excel".equals(excel))
		{
			return "resultExcel";
		}
		return "result";
	}

	public String queryDetail()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		detailDataList = bio.queryDetail(userCityId, devicetype, cityId,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getCountDevice(userCityId, devicetype, cityId,
				curPage_splitPage, num_splitPage);
		return "detail";
	}

	public String toExcel()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		detailDataList = bio.queryDetailForExcel(userCityId, devicetype, cityId);
		String excelCol = "vendor_add#device_model#softwareversion#city_name#device_serialnumber";
		String excelTitle = "设备厂商#设备型号#软件版本#属地#设备序列号";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "checkWork";
		data = detailDataList;
		return "excel";
	}

	public String queryDetailAll()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		detailDataList = bio.queryDetailAll(userCityId, filterDevicetype, cityId,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryDetailAllCount(userCityId, filterDevicetype, cityId,
				curPage_splitPage, num_splitPage);
		return "detailAll";
	}

	public String toExcelAll()
	{
		logger.warn("toExcelAll");
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		detailDataList = bio.queryDetailForExcelAll(userCityId, filterDevicetype, cityId);
		String excelCol = "vendor_add#device_model#softwareversion#city_name#device_serialnumber";
		String excelTitle = "设备厂商#设备型号#软件版本#属地#设备序列号";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "checkWork";
		data = detailDataList;
		return "excel";
	}

	public List<Map> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map> cityList)
	{
		this.cityList = cityList;
	}

	public CountDeviceBIO getBio()
	{
		return bio;
	}

	public void setBio(CountDeviceBIO bio)
	{
		this.bio = bio;
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public String getVendor()
	{
		return vendor;
	}

	public void setVendor(String vendor)
	{
		this.vendor = vendor;
	}

	public String getDevicemodel()
	{
		return devicemodel;
	}

	public void setDevicemodel(String devicemodel)
	{
		this.devicemodel = devicemodel;
	}

	public String getDevicetype()
	{
		return devicetype;
	}

	public void setDevicetype(String devicetype)
	{
		this.devicetype = devicetype;
	}

	public String getRela_dev_type()
	{
		return rela_dev_type;
	}

	public void setRela_dev_type(String relaDevType)
	{
		rela_dev_type = relaDevType;
	}

	public String getIs_check()
	{
		return is_check;
	}

	public void setIs_check(String isCheck)
	{
		is_check = isCheck;
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

	public String getProtocol2()
	{
		return protocol2;
	}

	public void setProtocol2(String protocol2)
	{
		this.protocol2 = protocol2;
	}

	public String getProtocol1()
	{
		return protocol1;
	}

	public void setProtocol1(String protocol1)
	{
		this.protocol1 = protocol1;
	}

	public String getProtocol0()
	{
		return protocol0;
	}

	public void setProtocol0(String protocol0)
	{
		this.protocol0 = protocol0;
	}

	public List<String> getTitleList()
	{
		return titleList;
	}

	public void setTitleList(List<String> titleList)
	{
		this.titleList = titleList;
	}

	public List getDataList()
	{
		return dataList;
	}

	public void setDataList(List dataList)
	{
		this.dataList = dataList;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public List getDetailDataList()
	{
		return detailDataList;
	}

	public void setDetailDataList(List detailDataList)
	{
		this.detailDataList = detailDataList;
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

	public String getExcel()
	{
		return excel;
	}

	public void setExcel(String excel)
	{
		this.excel = excel;
	}

	public List<String> getTotalNumList()
	{
		return totalNumList;
	}

	public void setTotalNumList(List<String> totalNumList)
	{
		this.totalNumList = totalNumList;
	}

	public String getFilterDevicetype()
	{
		return filterDevicetype;
	}

	public void setFilterDevicetype(String filterDevicetype)
	{
		this.filterDevicetype = filterDevicetype;
	}

	public String getQueryTime()
	{
		return queryTime;
	}

	public void setQueryTime(String queryTime)
	{
		this.queryTime = queryTime;
	}
}
