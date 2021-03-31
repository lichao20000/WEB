package action.report;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.dbimpl.DbUserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.opensymphony.xwork2.ActionSupport;

import dao.report.PerformanceReportDAO;

public class PerformanceReportAction extends ActionSupport implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7887727106993029804L;
	//dao
	private PerformanceReportDAO performance;
	//属地列表
	private List cityList;
	// 服务器的会话对象
	private Map session; 
	//时间
	private String day;
	//属地
	private String city_id;
	//ajax
	private String ajax;
	//性能报表结果
	private String reportData;
	//统计类型
	private String[] reportType;
	//统计设备
	private String[] devList;
	//报表类型 0:日 1:周 2:月
	private String type;
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getReportData()
	{
		return reportData;
	}
	public void setReportData(String reportData)
	{
		this.reportData = reportData;
	}
	public String[] getReportType()
	{
		return reportType;
	}
	public void setReportType(String[] reportType)
	{
		this.reportType = reportType;
	}
	public String[] getDevList()
	{
		return devList;
	}
	public void setDevList(String[] devList)
	{
		this.devList = devList;
	}
	public String getDay()
	{
		return day;
	}
	public void setDay(String day)
	{
		this.day = day;
	}
	public String getCity_id()
	{
		return city_id;
	}
	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}
	public String getAjax()
	{
		return ajax;
	}
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	public List getCityList()
	{
		return cityList;
	}
	public void setCityList(List cityList)
	{
		this.cityList = cityList;
	}
	public void setPerformance(PerformanceReportDAO performance)
	{
		this.performance = performance;
	}
	public void setSession(Map session)
	{
		this.session = session;
	}
	/**
	 * 初始化界面
	 */
	public String execute() throws Exception
	{
		// 取得session中的user属性
		DbUserRes dbUserRes = (DbUserRes) session.get("curUser");
		User user = dbUserRes.getUser();
		cityList = CityDAO.getAllNextCityListByCityPid(user.getCityId());
		day = new DateTimeUtil().getDate();
		return SUCCESS;
	}
	/**
	 * 获取性能报表
	 * @return
	 * @throws Exception
	 */
	public String getPerReport() throws Exception
	{
		reportData = performance.getPerReportData(day, reportType, devList, type);
		return "list";
	}
	/**
	 * 查询属地下的设备信息
	 * @return
	 * @throws Exception
	 */
	public String getCityDev() throws Exception
	{
		ajax = performance.getDevByCity(city_id);
		return "ajax";
	}
}
