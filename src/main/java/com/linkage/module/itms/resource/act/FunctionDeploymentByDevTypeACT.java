
package com.linkage.module.itms.resource.act;

import java.text.SimpleDateFormat;
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
import com.linkage.module.itms.resource.bio.FunctionDeploymentByDevTypeBIO;
import action.splitpage.splitPageAction;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-12-9
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class FunctionDeploymentByDevTypeACT extends splitPageAction implements
		SessionAware, ServletRequestAware
{

	private static Logger logger = LoggerFactory
			.getLogger(FunctionDeploymentByDevTypeACT.class);
	private Map session;
	private HttpServletRequest request;
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	// 厂商文件列表
	private Map<String, String> vendorMap;
	// 功能
	private String gn;
	// 厂商
	private String vendorId;
	// 型号
	private String modelId;
	private String ajax;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	// 统计信息
	private List<Map> deployList;
	// 统计详细明细
	private List<Map> deployDevTypeList;
	private FunctionDeploymentByDevTypeBIO bio;

	/**
	 * 初始化页面数据
	 */
	@Override
	public String execute() throws Exception
	{
		logger.debug("FunctionDeploymentByDevTypeACT=>execute()");
		endOpenDate = getEndDate();
		vendorMap = bio.getVendor();
		return "init";
	}

	/**
	 * 查询新增功能部署报表设备型号情况
	 * 
	 * @return
	 */
	public String quertFunctionDeployByDevType()
	{
		logger.debug("FunctionDeploymentByDevTypeACT=>quertFunctionDeployByDevTypeList()");
		this.setTime();
		deployList = bio
				.quertFunctionDeployByDevType(vendorId, modelId, endOpenDate1, gn);
		return "list";
	}

	/**
	 * 导出新增功能部署报表设备型号情况
	 * 
	 * @return
	 */
	public String quertFunctionDeployByDevTypeExcel()
	{
		logger.debug("FunctionDeploymentByDevTypeACT=>quertFunctionDeployByDevTypeList()");
		this.setTime();
		deployList = bio
				.quertFunctionDeployByDevType(vendorId, modelId, endOpenDate1, gn);
		String excelCol = "modelType#deploy_total";
		String excelTitle = "设备型号#已开通终端数";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "deployTypeListTotal";
		data = deployList;
		return "excel";
	}

	/**
	 * 新增功能部署报表设备型号情况明细页面
	 * 
	 * @return
	 */
	public String quertFunctionDeployByDevTypeList()
	{
		logger.debug("FunctionDeploymentByDevTypeACT=>quertFunctionDeployByDevTypeList()");
		this.setTime();
		deployDevTypeList = bio.quertFunctionDeployByDevTypeList(vendorId, modelId,
				endOpenDate1, gn, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countQuertFunctionDeployByDevTypeList(vendorId, modelId,
				endOpenDate1, gn, curPage_splitPage, num_splitPage);
		return "devlist";
	}

	/**
	 * 导出新增功能部署报表设备型号情况明细页面
	 * 
	 * @return
	 */
	public String quertFunctionDeployByDevTypeListExcel()
	{
		logger.debug("FunctionDeploymentByDevTypeACT=>quertFunctionDeployByDevTypeListExcel()");
		this.setTime();
		deployDevTypeList = bio.excelQuertFunctionDeployByDevTypeList(vendorId, modelId,
				gn, endOpenDate1);
		String excelCol = "city_name#loid#device_serialnumber#start_time#status#timelist#device_model";
		String excelTitle = "区域#LOID#设备序列号#部署时间#开通状态#采样周期#设备型号";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "deployDevTypeList";
		data = deployDevTypeList;
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
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	@Override
	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	public String getEndOpenDate()
	{
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}

	public String getEndOpenDate1()
	{
		return endOpenDate1;
	}

	public void setEndOpenDate1(String endOpenDate1)
	{
		this.endOpenDate1 = endOpenDate1;
	}

	public Map<String, String> getVendorMap()
	{
		return vendorMap;
	}

	public void setVendorMap(Map<String, String> vendorMap)
	{
		this.vendorMap = vendorMap;
	}

	public String getGn()
	{
		return gn;
	}

	public void setGn(String gn)
	{
		this.gn = gn;
	}

	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getModelId()
	{
		return modelId;
	}

	public void setModelId(String modelId)
	{
		this.modelId = modelId;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
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

	public FunctionDeploymentByDevTypeBIO getBio()
	{
		return bio;
	}

	public void setBio(FunctionDeploymentByDevTypeBIO bio)
	{
		this.bio = bio;
	}

	public List<Map> getDeployDevTypeList()
	{
		return deployDevTypeList;
	}

	public void setDeployDevTypeList(List<Map> deployDevTypeList)
	{
		this.deployDevTypeList = deployDevTypeList;
	}
}
