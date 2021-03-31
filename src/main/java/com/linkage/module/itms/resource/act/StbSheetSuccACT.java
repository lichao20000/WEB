
package com.linkage.module.itms.resource.act;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.resource.bio.StbSheetSuccBIO;

public class StbSheetSuccACT extends splitPageAction implements SessionAware,
		ServletRequestAware
{

	// 序列化
	private static final long serialVersionUID = 1L;
	// 日志
	private static Logger logger = LoggerFactory.getLogger(StbSheetSuccACT.class);
	// request取登陆帐号使用
	private HttpServletRequest request;
	// SESSION
	private Map<String, Object> session;
	// 开始时间
	private String starttime;
	// 结束时间
	private String endtime;
	// 返回类型
	private String returnType;
	// 报表结果数据
	private List<Map<String, Object>> data = null;
	// 业务逻辑处理
	private StbSheetSuccBIO bio;
	// 导出文件列标题
	private String[] title = null;
	// 导出文件列
	private String[] column = null;
	// 导出文件名
	private String fileName = null;

	/*
	 * 初始化页面
	 */
	public String init()
	{
		logger.debug("StbSheetSuccACT=>init()");
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate(); // 获取当前时间
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000); // 精确到当前时间的23点59分59秒
		endtime = dt.getLongDate();
		starttime = dt.getFirtDayOfMonth(); // 获取开始时间，为当月时间的第一天
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil((start_time) * 1000);
		starttime = dt.getLongDate();
		return "init";
	}

	/*
	 * 展示根据属地统计情况
	 */
	public String getCountData() throws ParseException
	{
		logger.debug("StbSheetSuccACT=>getCountData()");
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		data = bio.getCountData(userCityId, starttime, endtime);
		return "list";
	}

	/*
	 * 导出统计列表页面
	 */
	public String queryDataForExcel()
	{
		logger.debug("StbSheetSuccACT=>queryDataForExcel()");
		title = new String[] { "属地", "FTTH成功数", "FTTH总数", "FTTH成功率", "FTTB成功数", "FTTB总数",
				"FTTB成功率", "LAN成功数", "LAN总数", "LAN成功率", "HGW成功数", "HGW总数", "HGW成功率",
				"总工单成功数", "总工单总数", "总工单成功率" };
		column = new String[] { "cityName", "FTTHSuccNum", "FTTHTotalNum",
				"FTTHSuccRate", "FTTBSuccNum", "FTTBTotalNum", "FTTBSuccRate",
				"LANSuccNum", "LANTotalNum", "LANSuccRate", "HGWSuccNum", "HGWTotalNum",
				"HGWSuccRate", "totalSuccNum", "totalNum", "totalSuccRate" };
		fileName = "机顶盒工单成功率统计";
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		data = bio.getCountData(userCityId, starttime, endtime);
		return "excel";
	}

	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public Map<String, Object> getSession()
	{
		return session;
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

	public String getReturnType()
	{
		return returnType;
	}

	public void setReturnType(String returnType)
	{
		this.returnType = returnType;
	}

	public List<Map<String, Object>> getData()
	{
		return data;
	}

	public void setData(List<Map<String, Object>> data)
	{
		this.data = data;
	}

	public void setBio(StbSheetSuccBIO bio)
	{
		this.bio = bio;
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
