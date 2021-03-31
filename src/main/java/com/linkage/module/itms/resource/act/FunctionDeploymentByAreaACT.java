
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

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.FunctionDeploymentByAreaBIO;
import action.splitpage.splitPageAction;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-12-9
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class FunctionDeploymentByAreaACT extends splitPageAction implements SessionAware,
		ServletRequestAware
{

	private static Logger logger = LoggerFactory
			.getLogger(FunctionDeploymentByAreaACT.class);
	private FunctionDeploymentByAreaBIO bio;
	private HttpServletRequest request;
	private Map session;
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	// 属地
	private String city_id = null;
	// 属地列表
	private List<Map<String, String>> cityList = null;
	// 功能
	private String gn;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private List<Map> deployList = null;
	private List<Map> deployDevList = null;

	/**
	 * 初始化页面数据
	 */
	@Override
	public String execute() throws Exception
	{
		logger.debug("FunctionDeploymentByAreaACT=>execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		endOpenDate = getEndDate();
		return "init";
	}

	/**
	 * 查询新增功能部署报表区域情况
	 * 
	 * @return
	 */
	public String quertFunctionDeployByArea()
	{
		logger.debug("FunctionDeploymentByAreaACT=>quertFunctionDeployByArea()");
		this.setTime();
		deployList = bio.quertFunctionDeployByArea(city_id, gn, endOpenDate1);
		return "list";
	}

	/**
	 * 导出新增功能部署报表区域情况
	 * 
	 * @return
	 */
	public String quertFunctionDeployByAreaExcel()
	{
		logger.debug("FunctionDeploymentByAreaACT=>quertFunctionDeployByAreaExcel()");
		this.setTime();
		deployList = bio.quertFunctionDeployByArea(city_id, gn, endOpenDate1);
		String excelCol = "city_name#deploy_total";
		String excelTitle = "区域#已开通终端数";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "deployListTotal";
		data = deployList;
		return "excel";
	}

	/**
	 * 查询新增功能部署报表区域情况明细信息
	 * 
	 * @return
	 */
	public String quertFunctionDeployByAreaDev()
	{
		logger.debug("FunctionDeploymentByAreaACT=>quertFunctionDeployByAreaDev()");
		this.setTime();
		deployDevList = bio.quertFunctionDeployByAreaList(city_id, gn, endOpenDate1,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countQuertFunctionDeployByAreaList(city_id, gn,
				endOpenDate1, curPage_splitPage, num_splitPage);
		return "devlist";
	}

	/**
	 * 导出新增功能部署报表区域情况明细信息
	 * 
	 * @return
	 */
	public String quertFunctionDeployByAreaDevExcel()
	{
		logger.debug("FunctionDeploymentByAreaACT=>quertFunctionDeployByAreaDevExcel()");
		this.setTime();
		deployDevList = bio.excelQuertFunctionDeployByAreaList(city_id, gn, endOpenDate1);
		String excelCol = "city_name#loid#device_serialnumber#start_time#status#timelist#device_model";
		String excelTitle = "区域#LOID#设备序列号#部署时间#开通状态#采样周期#设备型号";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "deployDevList";
		data = deployDevList;
		return "excel";
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		// now.set(Calendar.HOUR_OF_DAY, 23);
		// now.set(Calendar.MINUTE, 59);
		// now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + endOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
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

	@Override
	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public FunctionDeploymentByAreaBIO getBio()
	{
		return bio;
	}

	public void setBio(FunctionDeploymentByAreaBIO bio)
	{
		this.bio = bio;
	}

	public String getEndOpenDate()
	{
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}

	public String getCity_id()
	{
		return city_id;
	}

	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public String getGn()
	{
		return gn;
	}

	public void setGn(String gn)
	{
		this.gn = gn;
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

	public List<Map> getDeployList()
	{
		return deployList;
	}

	public void setDeployList(List<Map> deployList)
	{
		this.deployList = deployList;
	}

	public List<Map> getDeployDevList()
	{
		return deployDevList;
	}

	public void setDeployDevList(List<Map> deployDevList)
	{
		this.deployDevList = deployDevList;
	}
}
