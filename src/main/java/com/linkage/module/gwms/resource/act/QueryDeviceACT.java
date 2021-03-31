
package com.linkage.module.gwms.resource.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.bio.QueryDeviceBIO;

/**
 * @author 王森博
 * @date 2009-11-16
 */
public class QueryDeviceACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(QueryDeviceACT.class);
	// session
	private Map session;
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	// 导入的资源类型 1：家庭网关 2：企业网关
	private String infoType = "1";
	/** 开始时间 */
	private String starttime = "";
	/** 开始时间 */
	private String starttime1 = "";
	/** 结束时间 */
	private String endtime = "";
	/** 结束时间 */
	private String endtime1 = "";
	/** 绑定状态 */
	private String bindState = "1";
	/** 时间类型 */
	private String timeType;
	/** 设备序列号 */
	private String device_serialnumber = "";
	/** IP地址 */
	private String loopback_ip = "";
	/** 确认状态 */
	private String device_status = "";
	/** 设备逻辑SN **/
	private String device_logicsn = "";
	/** 设备ID **/
	private String deviceId = "";
	
	/** BIO */
	private QueryDeviceBIO queryDeviceBio;

	/**
	 * 是否查询(特用于菜单进入的时候不触发查询) by zhangcong 2011-06-02
	 */
	private String no_query;
	
	private  String  gw_type;
	private int total;
	
	
	
	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * 查询设备信息
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String execute() throws Exception
	{
		return "list";
	}
	/**
	 * 查询设备信息
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String init() throws Exception
	{
		logger.debug("execute()");
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		  }
		if(!StringUtil.IsEmpty(device_serialnumber)){
			device_serialnumber = device_serialnumber.trim();
		}
        this.setTime();
		title = queryDeviceBio.getTitle(infoType, bindState);
		UserRes curUser = (UserRes) session.get("curUser");
		data = queryDeviceBio.getDeviceList(infoType, curPage_splitPage, num_splitPage,
				curUser, bindState, timeType, starttime1, endtime1, device_serialnumber,
				loopback_ip, device_logicsn, device_status);
		maxPage_splitPage = queryDeviceBio.getDeviceCount(infoType, curPage_splitPage,
				num_splitPage, curUser, bindState, timeType, starttime1, endtime1,
				device_serialnumber, loopback_ip, device_logicsn, device_status);
		return "devlist";
	}

	/**
	 * 查询设备信息跳页
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String goPage() throws Exception
	{
		logger.debug("goPage()");
		logger.warn("gw_type:"+gw_type);
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			device_serialnumber = device_serialnumber.trim();
		}
		this.setTime();
		title = queryDeviceBio.getTitle(infoType, bindState);
		UserRes curUser = (UserRes) session.get("curUser");
		data = queryDeviceBio.getDeviceList(infoType, curPage_splitPage, num_splitPage,
				curUser, bindState, timeType, starttime1, endtime1, device_serialnumber,
				loopback_ip, device_logicsn, device_status);
		maxPage_splitPage = queryDeviceBio.getDeviceCount(infoType, curPage_splitPage,
				num_splitPage, curUser, bindState, timeType, starttime1, endtime1,
				device_serialnumber, loopback_ip, device_logicsn, device_status);
		return "devlist";
	}
	/**
	 * 查询导入预置设备信息
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String impInitDevice() throws Exception
	{
		logger.debug("impInitDevice()");
		//infoType = "" + LipossGlobals.SystemType();
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		this.setTime();
        title = queryDeviceBio.getTitle(infoType, "1");
        if(!StringUtil.IsEmpty(device_serialnumber)){
        	device_serialnumber = device_serialnumber.trim();
        }
		
		// 判断是否是菜单进入，是则不查询 by 张聪 2011-07-05
		String query = getNo_query();
		if (query == null || query.trim().equalsIgnoreCase("")) 
		{
			UserRes curUser = (UserRes) session.get("curUser");
			data = queryDeviceBio.getImpInitDeviceList(curPage_splitPage, num_splitPage, device_serialnumber);
			maxPage_splitPage = queryDeviceBio.getImpInitDeviceCount(curPage_splitPage, num_splitPage, device_serialnumber);
		}else
		{
			//从菜单进入则直接置空结果集,避免查询时间太长，客户体验差by 张聪 2011-07-05
			data = new ArrayList<Map>();
			maxPage_splitPage = -99;
		}
		return "impInitDevice";
	}
	public String gopageImpInitDevice() throws Exception
	{
		logger.debug("impInitDevice()");
		//infoType = "" + LipossGlobals.SystemType();
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		this.setTime();
        title = queryDeviceBio.getTitle(infoType, "1");
        if(!StringUtil.IsEmpty(device_serialnumber))
        {
        	device_serialnumber = device_serialnumber.trim();
		}
		UserRes curUser = (UserRes) session.get("curUser");
		data = queryDeviceBio.getImpInitDeviceList(curPage_splitPage, num_splitPage,
				device_serialnumber);
		maxPage_splitPage = queryDeviceBio.getImpInitDeviceCount(curPage_splitPage,
				num_splitPage, device_serialnumber);
		return "impInitDevice";
	}
	
	/**
	 * 查询未确认设备信息
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String unconfirmDevice() throws Exception
	{
		logger.debug("execute()");
		//infoType = "" + LipossGlobals.SystemType();
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		this.setTime();
        title = queryDeviceBio.getTitle(infoType, "1");
        if(!StringUtil.IsEmpty(device_serialnumber)){
        	device_serialnumber = device_serialnumber.trim();
        }
		
		// 判断是否是菜单进入，是则不查询 by 张聪 2011-07-05
		String query = getNo_query();
		if (query == null || query.trim().equalsIgnoreCase("")) 
		{
			UserRes curUser = (UserRes) session.get("curUser");
			data = queryDeviceBio.getDeviceList(infoType, curPage_splitPage, num_splitPage,
					curUser, "0", timeType, starttime1, endtime1, device_serialnumber,
					loopback_ip, device_logicsn, "0");
			maxPage_splitPage = queryDeviceBio.getDeviceCount(infoType, curPage_splitPage,
					num_splitPage, curUser, "0", timeType, starttime1, endtime1,
					device_serialnumber, loopback_ip, device_logicsn, "0");
		   this.total = queryDeviceBio.getTotal();
		}else
		{
			//从菜单进入则直接置空结果集,避免查询时间太长，客户体验差by 张聪 2011-07-05
			data = new ArrayList<Map>();
			maxPage_splitPage = -99;
		}
		return "unconfirmDevice";
	}

	/**
	 * 查询未确认设备信息跳页
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String gopageUnconfirmDevice() throws Exception
	{
		logger.debug("goPage()");
		//infoType = "" + LipossGlobals.SystemType();
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			device_serialnumber = device_serialnumber.trim();
		}
		this.setTime();
		title = queryDeviceBio.getTitle(infoType, "1");
		UserRes curUser = (UserRes) session.get("curUser");
		data = queryDeviceBio.getDeviceList(infoType, curPage_splitPage, num_splitPage,
				curUser, "0", timeType, starttime1, endtime1, device_serialnumber,
				loopback_ip, device_logicsn, "0");
		maxPage_splitPage = queryDeviceBio.getDeviceCount(infoType, curPage_splitPage,
				num_splitPage, curUser, "0", timeType, starttime1, endtime1,
				device_serialnumber, loopback_ip, device_logicsn, "0");
		this.total = queryDeviceBio.getTotal();
		return "unconfirmDevice";
	}
	
	/**
	 * 导出未确认设备列表
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String getInfoExcelUnconfirmDevice() throws Exception
	{
		logger.debug("getInfoExcel()");
		//infoType = "" + LipossGlobals.SystemType();
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			device_serialnumber = device_serialnumber.trim();
		}
		this.setTime();
		title = queryDeviceBio.getTitle(infoType, "1");
		UserRes curUser = (UserRes) session.get("curUser");
		data = queryDeviceBio.getDeviceExcel(infoType, curUser, "0", timeType,
				starttime1, endtime1, device_serialnumber, loopback_ip, device_logicsn, "0");
		column = queryDeviceBio.getColumn(infoType, "1");
		fileName = "deviceInfo";
		return "excel";
	}


	/**
	 * 导出设备列表
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String getInfoExcel() throws Exception
	{
		logger.debug("getInfoExcel()");
		//infoType = "" + LipossGlobals.SystemType();
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			device_serialnumber = device_serialnumber.trim();
		}
		this.setTime();
		title = queryDeviceBio.getTitle(infoType, bindState);
		UserRes curUser = (UserRes) session.get("curUser");
		data = queryDeviceBio.getDeviceExcel(infoType, curUser, bindState, timeType,
				starttime1, endtime1, device_serialnumber, loopback_ip, device_logicsn, device_status);
		column = queryDeviceBio.getColumn(infoType, bindState);
		fileName = "deviceInfo";
		return "excel";
	}
	
	/**
	 * 设备列表
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String deviceAll() throws Exception
	{
		logger.debug("execute()");
		//infoType = "" + LipossGlobals.SystemType();
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			device_serialnumber = device_serialnumber.trim();
		}
		this.setTime();
		title = queryDeviceBio.getTitle(infoType, "3");
		UserRes curUser = (UserRes) session.get("curUser");

		// 判断是否是菜单进入，是则不查询 by 张聪 2011-06-02
		String query = getNo_query();
        if (query == null || query.trim().equalsIgnoreCase("")) 
		{
			data = queryDeviceBio.getDeviceList(infoType, curPage_splitPage,
					num_splitPage, curUser, "0", timeType, starttime1,
					endtime1, device_serialnumber, loopback_ip, device_logicsn,"1");
			maxPage_splitPage = queryDeviceBio
					.getDeviceCount(infoType, curPage_splitPage, num_splitPage,
							curUser, "0", timeType, starttime1, endtime1,
							device_serialnumber, loopback_ip, device_logicsn, "1");
			this.total = queryDeviceBio.getTotal();
		}else
		{
			data = new ArrayList<Map>();
			total = 0;
			maxPage_splitPage = 0;
		}
		return "deviceAll";
	}

	/**
	 * 设备列表跳页
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String gopageDeviceAll() throws Exception
	{
		logger.debug("goPage()");
		//infoType = "" + LipossGlobals.SystemType();
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			device_serialnumber = device_serialnumber.trim();
		}
		this.setTime();
		if(LipossGlobals.inArea(Global.NXLT)){
			title = queryDeviceBio.getTitle(infoType, "3");
		}else{
			title = queryDeviceBio.getTitle(infoType, "1");
		}
		
		UserRes curUser = (UserRes) session.get("curUser");
		data = queryDeviceBio.getDeviceList(infoType, curPage_splitPage, num_splitPage,
				curUser, "0", timeType, starttime1, endtime1, device_serialnumber,
				loopback_ip, device_logicsn, "1");
		maxPage_splitPage = queryDeviceBio.getDeviceCount(infoType, curPage_splitPage,
				num_splitPage, curUser, "0", timeType, starttime1, endtime1,
				device_serialnumber, loopback_ip, device_logicsn, "1");
		this.total = queryDeviceBio.getTotal();
		return "deviceAll";
	}
	
	
	
	
	/**
	 * 设备列表
	 * 
	 * add by zhangchy 2012-12-27
	 * 
	 * 根据安徽电信要求，将设备操作列表的查询条件，
	 * 改为工程中封装好了的设备查询/gwms/share/gwShareDeviceQuery.jsp
	 * 
	 * 为了不在原有的函数上修改，所以重新定义了一个函数，供其使用
	 * 
	 */
	public String deviceOperateByDeviceId() throws Exception
	{
		logger.debug("deviceOperateByDeviceId()");
		
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		if(LipossGlobals.inArea(Global.NXLT))
		{
			title = new String[8];
			title[0] = "属地";
			title[1] = "设备厂商";
			title[2] = "型号";
			title[3] = "软件版本";
			title[4] = "设备序列号";
			title[5] = "LOID";
			title[6] = "域名或IP";
			title[7] = "上报时间";
		}
		else
		{
			title = new String[7];
			title[0] = "属地";
			title[1] = "设备厂商";
			title[2] = "型号";
			title[3] = "设备序列号";
			title[4] = "设备逻辑SN";
			title[5] = "域名或IP";
			title[6] = "上报时间";
		}

		
//		// 判断是否是菜单进入
//		String query = getNo_query();
//		
//		if (query == null || query.trim().equalsIgnoreCase("")) 
//		{
			data = queryDeviceBio.getDeviceListByDeviceId(infoType, deviceId, curPage_splitPage,
					num_splitPage);
			maxPage_splitPage = queryDeviceBio.getDeviceCountByDeviceId(infoType, deviceId,
					curPage_splitPage, num_splitPage);
//		}else
//		{
//			data = null;
//			maxPage_splitPage = 0;
//		}
		return "deviceOperate2";
	}




	public String deviceOperateByDeviceIdForSxlt() throws Exception
	{
		logger.warn("deviceOperateByDeviceIdForSxlt()");

		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;
		}
		title = new String[7];
		title[0] = "属地";
		title[1] = "设备厂商";
		title[2] = "型号";
		title[3] = "软件版本";
		title[4] = "设备序列号";
		title[5] = "域名或IP";
		title[6] = "上报时间";
		data = queryDeviceBio.getDeviceListByDeviceIdForSxlt(infoType, deviceId, curPage_splitPage,
				num_splitPage);
		maxPage_splitPage = queryDeviceBio.getDeviceCountByDeviceId(infoType, deviceId,
				curPage_splitPage, num_splitPage);
		return "deviceOperateForSxlt";
	}
	
	
	/**
	 * 设备列表
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String deviceOperate() throws Exception
	{
		logger.debug("execute()");
		//infoType = "" + LipossGlobals.SystemType();
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		
		this.setTime();
		title = new String[7];
		title[0] = "属地";
		title[1] = "设备厂商";
		title[2] = "型号";
		title[3] = "设备序列号";
		title[4] = "设备逻辑SN";
		title[5] = "域名或IP";
		title[6] = "上报时间";
		//title = queryDeviceBio.getTitle(infoType, "1");
		UserRes curUser = (UserRes) session.get("curUser");
		
		// 判断是否是菜单进入，是则不查询 by 张聪 2011-06-02
		String query = getNo_query();
		if(!StringUtil.IsEmpty(device_serialnumber)){
			device_serialnumber = device_serialnumber.trim();
		}
		if (query == null || query.trim().equalsIgnoreCase("")) 
		{
			data = queryDeviceBio.getDeviceList(infoType, curPage_splitPage,
					num_splitPage, curUser, "0", timeType, starttime1,
					endtime1, device_serialnumber, loopback_ip, device_logicsn, "1");
			maxPage_splitPage = queryDeviceBio
					.getDeviceCount(infoType, curPage_splitPage, num_splitPage,
							curUser, "0", timeType, starttime1, endtime1,
							device_serialnumber, loopback_ip, device_logicsn, "1");
		}else
		{
			data = null;
			maxPage_splitPage = 0;
		}
		return "deviceOperate";
	}

	/**
	 * 设备列表跳页
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String gopageDeviceOperate() throws Exception
	{
		logger.debug("goPage()");
		//infoType = "" + LipossGlobals.SystemType();
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			device_serialnumber = device_serialnumber.trim();
		}
		
		this.setTime();
		title = new String[7];
		title[0] = "属地";
		title[1] = "设备厂商";
		title[2] = "型号";
		title[3] = "设备序列号";
		title[4] = "设备逻辑SN";
		title[5] = "域名或IP";
		title[6] = "上报时间";
		//title = queryDeviceBio.getTitle(infoType, "1");
		UserRes curUser = (UserRes) session.get("curUser");
		data = queryDeviceBio.getDeviceList(infoType, curPage_splitPage, num_splitPage,
				curUser, "0", timeType, starttime1, endtime1, device_serialnumber,
				loopback_ip, device_logicsn, "1");
		maxPage_splitPage = queryDeviceBio.getDeviceCount(infoType, curPage_splitPage,
				num_splitPage, curUser, "0", timeType, starttime1, endtime1,
				device_serialnumber, loopback_ip, device_logicsn, "1");
		return "deviceOperate";
	}
	
	/**
	 * 导出设备列表
	 * 
	 * @author wangsenbo
	 * @date Nov 16, 2009
	 * @return String
	 */
	public String getInfoExcelDevice() throws Exception
	{
		logger.debug("getInfoExcel()");
		//infoType = "" + LipossGlobals.SystemType();
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			device_serialnumber = device_serialnumber.trim();
		}
		this.setTime();
		UserRes curUser = (UserRes) session.get("curUser");

		if(LipossGlobals.inArea(Global.NXLT)){
			title = queryDeviceBio.getTitle(infoType, "3");
			column = queryDeviceBio.getColumn(infoType, "3");
		}else{
			title = queryDeviceBio.getTitle(infoType, "1");
			column = queryDeviceBio.getColumn(infoType, "1");
		}
		data = queryDeviceBio.getDeviceExcel(infoType, curUser, "0", timeType,
				starttime1, endtime1, device_serialnumber, loopback_ip, device_logicsn, "1");
		fileName = "deviceInfo";
		return "excel";
	}
	
	public String getInfoExcelDevice2() throws Exception
	{
		logger.debug("getInfoExcel()");
	//	infoType = "" + LipossGlobals.SystemType();
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			device_serialnumber = device_serialnumber.trim();
		}
		this.setTime();
		//title = queryDeviceBio.getTitle(infoType, "1");
		title = new String[7];
		title[0] = "属地";
		title[1] = "设备厂商";
		title[2] = "型号";
		title[3] = "设备序列号";
		title[4] = "设备逻辑SN";
		title[5] = "域名或IP";
		title[6] = "上报时间";
		UserRes curUser = (UserRes) session.get("curUser");
		data = queryDeviceBio.getDeviceExcel(infoType, curUser, "0", timeType,
				starttime1, endtime1, device_serialnumber, loopback_ip, device_logicsn, "1");
		//column = queryDeviceBio.getColumn(infoType, "1");
		column = new String[7];
		column[0] = "city_name";
		column[1] = "vendor_add";
		column[2] = "device_model";
		column[3] = "device";
		column[4] = "device_id_ex";
		column[5] = "loopback_ip";
		column[6] = "complete_time";
		fileName = "deviceInfo";
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

	public String getInfoType()
	{
		logger.debug("getInfoType()");
	//	infoType = "" + LipossGlobals.SystemType();
		if(null !=gw_type  &&  !"".equals(gw_type) && !"null".equals(gw_type)){
			infoType=gw_type;	
		}
		return infoType;
	}

	/**
	 * @return the data
	 */
	public List<Map> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn()
	{
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the starttime
	 */
	public String getStarttime()
	{
		return starttime;
	}

	/**
	 * @param starttime
	 *            the starttime to set
	 */
	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	/**
	 * @return the starttime1
	 */
	public String getStarttime1()
	{
		return starttime1;
	}

	/**
	 * @param starttime1
	 *            the starttime1 to set
	 */
	public void setStarttime1(String starttime1)
	{
		this.starttime1 = starttime1;
	}

	/**
	 * @return the endtime
	 */
	public String getEndtime()
	{
		return endtime;
	}

	/**
	 * @param endtime
	 *            the endtime to set
	 */
	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	/**
	 * @return the endtime1
	 */
	public String getEndtime1()
	{
		return endtime1;
	}

	/**
	 * @param endtime1
	 *            the endtime1 to set
	 */
	public void setEndtime1(String endtime1)
	{
		this.endtime1 = endtime1;
	}

	/**
	 * @return the bindState
	 */
	public String getBindState()
	{
		return bindState;
	}

	/**
	 * @param bindState
	 *            the bindState to set
	 */
	public void setBindState(String bindState)
	{
		this.bindState = bindState;
	}

	/**
	 * @return the timeType
	 */
	public String getTimeType()
	{
		return timeType;
	}

	/**
	 * @param timeType
	 *            the timeType to set
	 */
	public void setTimeType(String timeType)
	{
		this.timeType = timeType;
	}

	/**
	 * @return the queryDeviceBio
	 */
	public QueryDeviceBIO getQueryDeviceBio()
	{
		return queryDeviceBio;
	}

	/**
	 * @param queryDeviceBio
	 *            the queryDeviceBio to set
	 */
	public void setQueryDeviceBio(QueryDeviceBIO queryDeviceBio)
	{
		this.queryDeviceBio = queryDeviceBio;
	}

	/**
	 * @param infoType
	 *            the infoType to set
	 */
	public void setInfoType(String infoType)
	{
		this.infoType = infoType;
	}

	/**
	 * @return the device_serialnumber
	 */
	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}

	/**
	 * @param device_serialnumber
	 *            the device_serialnumber to set
	 */
	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
	}

	/**
	 * @return the loopback_ip
	 */
	public String getLoopback_ip()
	{
		return loopback_ip;
	}

	public String getDevice_logicsn() {
		return device_logicsn;
	}

	public void setDevice_logicsn(String device_logicsn) {
		this.device_logicsn = device_logicsn;
	}

	/**
	 * @param loopback_ip
	 *            the loopback_ip to set
	 */
	public void setLoopback_ip(String loopback_ip)
	{
		this.loopback_ip = loopback_ip;
	}

	/**
	 * @return the device_status
	 */
	public String getDevice_status()
	{
		return device_status;
	}

	/**
	 * @param device_status
	 *            the device_status to set
	 */
	public void setDevice_status(String device_status)
	{
		this.device_status = device_status;
	}
	
    public String getGw_type()
	{
		return gw_type;
	}

	
	public void setGw_type(String gwType)
	{
		gw_type = gwType;
	}

	public String getNo_query() 
	{
		return no_query;
	}

	public void setNo_query(String no_query) 
	{
		this.no_query = no_query;
	}

	
	public String getDeviceId() {
		return deviceId;
	}

	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}
}
