package action.bbms;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;

import dao.bbms.ExceptionDeviceListDAO;

/**
 * 异常设备处理
 *
 * @author 王志猛(5194) tel：12345678
 * @version 1.0
 * @since 2008-6-11
 * @category action.bbms 版权：南京联创科技 网管科技部
 *
 */
public class ExceptionDeviceAction extends ActionSupport implements SessionAware
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1715765641467447733L;
	private String startDate;
	private String endDate;
	private List<Map> expDevList;// 设备资源列表
	private ExceptionDeviceListDAO edDAo;// 查询dao
	private int viewStaus = 0;// 查看处理信息还是处理异常设备 0是处理 1是查看
	private String device_id;// 设备id
	private String ajax;
	private Map session;
	private String doinfo;
	private Map viewInfo;
	private long exception_time;//异常发生时间
	private int dealstatus=0;//处理状态
	public String execute() throws Exception
	{
		return SUCCESS;
	}
	/**
	 * 异常设备列表
	 *
	 * @return
	 * @throws Exception
	 */
	public String ExpDevList() throws Exception
	{
		expDevList = edDAo.getExpDevList(startDate, endDate,dealstatus);
		return "devList";
	}
	public String expDev() throws Exception
	{
		if (viewStaus == 1)
			{
				viewInfo = edDAo.viewExpDev(device_id,exception_time);
			}
		return "expDev";
	}
	public String doExpDev() throws Exception
	{
		UserRes curUser = (UserRes) session.get("curUser");
		ajax = edDAo.doExpDev(device_id, doinfo, curUser.getUser().getId(),exception_time) ? "success"
				: "fail";
		return "doExpDev";
	}
	public String getStartDate()
	{
		return startDate;
	}
	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}
	public String getEndDate()
	{
		return endDate;
	}
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}
	public List<Map> getExpDevList()
	{
		return expDevList;
	}
	public ExceptionDeviceListDAO getEdDAo()
	{
		return edDAo;
	}
	public void setEdDAo(ExceptionDeviceListDAO edDAo)
	{
		this.edDAo = edDAo;
	}
	public int getViewStaus()
	{
		return viewStaus;
	}
	public void setViewStaus(int viewStaus)
	{
		this.viewStaus = viewStaus;
	}
	public String getDevice_id()
	{
		return device_id;
	}
	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}
	public String getAjax()
	{
		return ajax;
	}
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	public void setSession(Map session)
	{
		this.session = session;
	}
	public Map getViewInfo()
	{
		return viewInfo;
	}
	public String getDoinfo()
	{
		return doinfo;
	}
	public void setDoinfo(String doinfo)
	{
		try
			{
				this.doinfo = java.net.URLDecoder.decode(doinfo, "UTF-8");
			} catch (Exception e)
			{
				this.doinfo = doinfo;
			}
	}
	public long getException_time()
	{
		return exception_time;
	}
	public void setException_time(long exception_time)
	{
		this.exception_time = exception_time;
	}
	public void setDealstatus(int dealstatus)
	{
		this.dealstatus = dealstatus;
	}
	public int getDealstatus()
	{
		return dealstatus;
	}
}
