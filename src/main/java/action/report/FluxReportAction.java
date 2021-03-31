package action.report;

import com.linkage.litms.common.util.DateTimeUtil;
import com.opensymphony.xwork2.ActionSupport;

import dao.report.FluxReportDAO;

/**
 * 设备流量统计功能
 * @author czm
 * @since 2008-07-14
 * @version 1.0
 * 
 */
public class FluxReportAction extends ActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3009534032570345107L;
	//dao
	private FluxReportDAO fluxReport;
	//厂商列表
	private String vendorList;
	//厂商编号
	private String vendor_id;
	//设备型号编号
	private String device_model_id;
	//设备编号
	private String device_id;
	//ajax
	private String ajax;
	//统计时间
	private String day;
	//统计类型
	private String[] kind;
	//统计的设备端口
	private String devicePortList;
	//统计数据
	private String msg;
	//设备端口信息描述
	private String devicePortListInfo;
	public String getDevicePortListInfo()
	{
		return devicePortListInfo;
	}
	public void setDevicePortListInfo(String devicePortListInfo)
	{
		this.devicePortListInfo = devicePortListInfo;
	}
	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	public String[] getKind()
	{
		return kind;
	}
	public void setKind(String[] kind)
	{
		this.kind = kind;
	}
	public String getDay()
	{
		return day;
	}
	public void setDay(String day)
	{
		this.day = day;
	}
	public String getDevicePortList()
	{
		return devicePortList;
	}
	public void setDevicePortList(String devicePortList)
	{
		this.devicePortList = devicePortList;
	}
	public String getAjax()
	{
		return ajax;
	}
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	public String getVendor_id()
	{
		return vendor_id;
	}
	public void setVendor_id(String vendor_id)
	{
		this.vendor_id = vendor_id;
	}

	public String getDevice_model_id() {
		return device_model_id;
	}
	
	public void setDevice_model_id(String device_model_id) {
		this.device_model_id = device_model_id;
	}
	
	public String getDevice_id()
	{
		return device_id;
	}
	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}
	public String getVendorList()
	{
		return vendorList;
	}
	public void setVendorList(String vendorList)
	{
		this.vendorList = vendorList;
	}
	public void setFluxReport(FluxReportDAO fluxReport)
	{
		this.fluxReport = fluxReport;
	}

	/**
	 * 
	 */
	public String execute() throws Exception
	{
		vendorList = fluxReport.getVendorList();
		day = new DateTimeUtil().getNextDate("day", -1);
		return SUCCESS;
	}
	
	/**
	 * 查询统计数据
	 * @return
	 * @throws Exception
	 */
	public String getFluxReportInfo() throws Exception
	{
		msg = fluxReport.getFluxReportInfo(day, devicePortList, devicePortListInfo, kind);
		return "data";
	}
	/**
	 * 查询对应厂商的设备型号
	 * @return
	 * @throws Exception
	 */
	public String getDeviceModelInfo() throws Exception
	{
		ajax = fluxReport.getModelList(vendor_id);
		return "ajax";
	}
	/**
	 * 查询对应设备型号下的所有设备
	 * @return
	 * @throws Exception
	 */
	public String getDeviceInfo() throws Exception
	{
		ajax = fluxReport.getDeviceList(vendor_id, device_model_id);
		return "ajax";
	}
	/**
	 * 查询设备的端口信息
	 * @return
	 * @throws Exception
	 */
	public String getDevicePortInfo() throws Exception
	{
		ajax = fluxReport.getPortInfo(device_id);
		return "ajax";
	}
}
