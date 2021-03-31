package com.linkage.module.gwms.report.act;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.report.bio.BindDevTypeStatBIO;
import com.linkage.module.gwms.report.dao.BindDevTypeStatDAO;

import action.splitpage.splitPageAction;

/**
 * 
 * @author yaoli (Ailk No.)XJDX-ITMS-20180228-LJ-001(绑定终端版本统计报表)
 * @version 1.0
 * @since 2019年3月4日
 * @category com.linkage.module.gwms.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BindDevTypeStatACT extends splitPageAction implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6970503478175814011L;
	
	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(BindDevTypeStatACT.class);
	private  BindDevTypeStatBIO bindDevTypeStatBIO;
	private Map session;
	
	private String vendorId = null;
	private String deviceModelId = null;
	private String starttime = null;
	private String endtime = null;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private List<Map> bindDevTypeList = null; //绑定终端版本结果集
	private String bindDevTable = null;
	
	public String init(){
		initTime();
		return "init";
	}
	
	public String queryBindDevTypeList(){
		logger.debug("queryBindDevTypeList");
		UserRes user = (UserRes) session.get("curUser");
		setTime();
		bindDevTable = bindDevTypeStatBIO.queryBindDevTypeList(user,vendorId, deviceModelId, starttime, endtime);
		HttpServletRequest request = null;
		try
		{
			request = ServletActionContext.getRequest();
			request.setCharacterEncoding("gbk");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		HttpSession session = request.getSession(); 
		session.setAttribute("bindDevTable", bindDevTable);
		return "bindList";
	}
	
	public void initTime(){
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();  // 获取当前时间
		
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000); // 精确到当前时间的23点59分59秒
		endtime = dt.getLongDate();
		
		int year = dt.getYear();
		int month = dt.getMonth()-1;
		int day = dt.getDay();
		
		dt = new DateTimeUtil(year+"-"+month+"-"+day);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil((start_time) * 1000);
		starttime = dt.getLongDate();
	}
	
	private void setTime(){
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime)){
			starttime = null;
		}else{
			dt = new DateTimeUtil(starttime);
			starttime = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime)){
			endtime = null;
		}else{
			dt = new DateTimeUtil(endtime);
			endtime = String.valueOf(dt.getLongTime());
		}
	}
	@Override
	public void setSession(Map session)
	{
		this.session = session;
		
	}
	
	public Map getSession()
	{
		return session;
	}

	public String getVendorId()
	{
		return vendorId;
	}

	
	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	
	public String getDeviceModelId()
	{
		return deviceModelId;
	}

	
	public void setDeviceModelId(String deviceModelId)
	{
		this.deviceModelId = deviceModelId;
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
	
	public List<Map> getBindDevTypeList()
	{
		return bindDevTypeList;
	}

	public void setBindDevTypeList(List<Map> bindDevTypeList)
	{
		this.bindDevTypeList = bindDevTypeList;
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

	public BindDevTypeStatBIO getBindDevTypeStatBIO()
	{
		return bindDevTypeStatBIO;
	}

	public void setBindDevTypeStatBIO(BindDevTypeStatBIO bindDevTypeStatBIO)
	{
		this.bindDevTypeStatBIO = bindDevTypeStatBIO;
	}

	public String getBindDevTable()
	{
		return bindDevTable;
	}

	public void setBindDevTable(String bindDevTable)
	{
		this.bindDevTable = bindDevTable;
	}
}
