
package com.linkage.module.itms.report.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.UserDeviceReportBIO;
import com.linkage.system.extend.struts.splitpage.SplitPageAction;
import com.opensymphony.xwork2.ActionContext;

/**
 * 用户信息统计报表
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-7-19
 * @category com.linkage.module.itms.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class UserDeviceReportACT extends SplitPageAction
{

	private static final long serialVersionUID = 4177141199898437677L;
	private UserDeviceReportBIO userDeviceReportBIO;
	private String startTime;
	private String endTime;
	/**
	 * 属地查询条件
	 */
	private String cityId;
	/**
	 * 页面展示属地下拉框列表
	 */
	private List<Map<String, String>> cityList;
	/**
	 * 列表集合,用户页面展示和Excel导出
	 */
	private List<Map> data;
	// 导出文件列标题
	private String[] title = new String[] { "属地", "E8-B用户数", "EB-C用户数", "政企融合网关用户数",
			"E8-B绑定设备用户数", "EB-C绑定设备用户数", "政企融合网关绑定设备用户数" };
	// 导出文件列
	private String[] column = new String[] { "city_name", "e8b_count", "e8c_count",
			"all_e8c_count", "bind_e8b_count", "bind_e8c_count", "bind_all_e8c_count" };
	// 导出文件名
	private String fileName = "UserDeviceReport";;

	/**
	 * 跳转到报表统计页面的初始化方法
	 * 
	 * @return
	 */
	public String init()
	{
		UserRes currentUser = (UserRes) ActionContext.getContext().getSession()
				.get("curUser");
		cityId = currentUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(cityId);
		startTime = timeOfMonthFirstDay();
		endTime = timeOfNextDay();
		return "query";
	}

	/**
	 * 根据查询条件统计各地市的用户数和绑定设备用户数
	 * 
	 * @return
	 */
	public String queryUserDevice()
	{
		if (StringUtil.IsEmpty(cityId))
		{
			UserRes currentUser = (UserRes) ActionContext.getContext().getSession()
					.get("curUser");
			cityId = currentUser.getCityId();
		}
		data = userDeviceReportBIO.queryUserDevice(cityId, transTimeInSecond(startTime),
				transTimeInSecond(endTime));
		return "list";
	}

	/**
	 * Excel导出
	 * 
	 * @return
	 */
	public String exportUserDevice()
	{
		if (StringUtil.IsEmpty(cityId))
		{
			UserRes currentUser = (UserRes) ActionContext.getContext().getSession()
					.get("curUser");
			cityId = currentUser.getCityId();
		}
		data = userDeviceReportBIO.queryUserDevice(cityId, transTimeInSecond(startTime),
				transTimeInSecond(endTime));
		return "excel";
	}

	/**
	 * 获取当前月的第一天，格式为 yyyy-MM-01 00:00:00
	 * @return 当前月的第一天
	 */
	private String timeOfMonthFirstDay()
	{
		Calendar time = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
		time.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(time.getTime()) + " 00:00:00";
	}

	/**
	 * 获取当前时间的第二天零点时间，格式为 yyyy-MM-dd 00:00:00
	 * @return 当前时间的第二天零点时间
	 */
	private String timeOfNextDay()
	{
		Calendar time = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
		time.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH) + 1);
		return new SimpleDateFormat("yyyy-MM-dd").format(time.getTime()) + " 00:00:00";
	}

	/**
	 * 将输入的字符串时间转化为以秒为单位的时间格式
	 * @param inputTime 字符串时间，格式为yyyy-MM-dd HH:mm:ss
	 * @return 返回字符串时间的秒单位
	 */
	private String transTimeInSecond(String inputTime)
	{
		if (StringUtil.IsEmpty(inputTime))
		{
			return null;
		}
		DateTimeUtil dt = new DateTimeUtil(inputTime);
		return String.valueOf(dt.getLongTime());
	}

	public UserDeviceReportBIO getUserDeviceReportBIO()
	{
		return userDeviceReportBIO;
	}

	public void setUserDeviceReportBIO(UserDeviceReportBIO userDeviceReportBIO)
	{
		this.userDeviceReportBIO = userDeviceReportBIO;
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

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public List<Map> getData()
	{
		return data;
	}

	public void setData(List<Map> data)
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
}
