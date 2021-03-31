
package com.linkage.module.itms.resource.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.act.PVCReportACT;
import com.linkage.module.itms.resource.bio.LogRestartQueryBIO;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2016-11-30
 * @category com.linkage.module.itms.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class LogRestartQueryACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(PVCReportACT.class);
	private List<Map> data;
	private LogRestartQueryBIO itms_restartBIO;
	private Map session;
	// 业务信息表
	private List<Map> LogRestartQuerderive;
	// 属地
	private String cityId;
	// 开始时间
	private String starttime;
	// 结束时间
	private String endtime;
	// 文件名
	private String fileName;
	// 执行时间
	private String update_time;

	public String getUpdate_time()
	{
		return update_time;
	}

	public void setUpdate_time(String update_time)
	{
		this.update_time = update_time;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导入时间
	private String add_time;
	// 批次
	private String task_name;
	// 重启成功数
	private String doneNum;
	// 重启未执行数
	private String unDoneNum;
	// 代替status
	private String type;
	// 重启失败数
	private String failNum;
	// 总数
	private String totalNum;

	public String getTotalNum()
	{
		return totalNum;
	}

	public void setTotalNum(String totalNum)
	{
		this.totalNum = totalNum;
	}

	public String getUnDoneNum()
	{
		return unDoneNum;
	}

	public void setUnDoneNum(String unDoneNum)
	{
		this.unDoneNum = unDoneNum;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getFailNum()
	{
		return failNum;
	}

	public void setFailNum(String failNum)
	{
		this.failNum = failNum;
	}

	public String getDoneNum()
	{
		return doneNum;
	}

	public void setDoneNum(String doneNum)
	{
		this.doneNum = doneNum;
	}

	// 属地列表
	private List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();

	/**
	 * 初始查询
	 * 
	 * @return
	 */
	public String countITMS()
	{
		logger.warn("countITMS()");
		this.setTime();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		title = new String[] { "导入日期", "批次", "重启成功数", "重启失败数", "未执行数", "总数" };
		column = new String[] { "add_time", "task_name", "doneNum", "failNum",
				"unDoneNum", "totalNum" };
		data = itms_restartBIO.countITMS(cityId, starttime, endtime, curPage_splitPage,
				num_splitPage,acc_oid);
		maxPage_splitPage = itms_restartBIO.getMaxPage_splitPage();
		return "list";
	}

	/**
	 * 页面初始化
	 * 
	 * @return
	 */
	public String init()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		return "init";
	}

	/**
	 * 初始查询导出
	 * 
	 * @return
	 */
	public String toExcel()
	{
		this.setTime();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		data = itms_restartBIO.countITMSExcel(cityId, starttime, endtime,acc_oid);
		logger.warn("data=" + data);
		title = new String[6];
		title[0] = "导入日期";
		title[1] = "批次";
		title[2] = "重启成功数";
		title[3] = "重启失败数";
		title[4] = "未执行数";
		title[5] = "总数";
		column = new String[6];
		column[0] = "add_time";
		column[1] = "task_name";
		column[2] = "doneNum";
		column[3] = "failNum";
		column[4] = "unDoneNum";
		column[5] = "totalNum";
		fileName = "批量重启查询";
		return "excel";
	}

	/**
	 * 未执行
	 * 
	 * @return
	 */
	public String unexecuteExcel()
	{
		this.setTime();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		data = itms_restartBIO.unexecutederive(unDoneNum, add_time, task_name, type,acc_oid);
		title = new String[6];
		title[0] = "属地";
		title[1] = "厂家";
		title[2] = "型号";
		title[3] = "软件版本";
		title[4] = "硬件版本";
		title[5] = "特别版本";
		column = new String[6];
		column[0] = "city_name";
		column[1] = "vendor_name";
		column[2] = "device_model";
		column[3] = "softwareversion";
		column[4] = "hardwareversion";
		column[5] = "specversion";
		fileName = "重启未执行查询";
		return "excel";
	}

	/**
	 * 失败导出
	 * 
	 * @return
	 */
	public String fialExcel()
	{
		this.setTime();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		data = itms_restartBIO.FailQuertDerive(failNum, add_time, task_name, type,acc_oid);
		title = new String[8];
		title[0] = "属地";
		title[1] = "厂家";
		title[2] = "型号";
		title[3] = "软件版本";
		title[4] = "硬件版本";
		title[5] = "特别版本";
		title[6] = "执行时间";
		title[7] = "失败原因";
		column = new String[8];
		column[0] = "city_name";
		column[1] = "vendor_name";
		column[2] = "device_model";
		column[3] = "softwareversion";
		column[4] = "hardwareversion";
		column[5] = "specversion";
		column[6] = "update_time";
		column[7] = "fail_desc";
		fileName = "重启失败查询";
		return "excel";
	}

	/**
	 * 成功导出
	 * 
	 * @return
	 */
	public String successExcel()
	{
		this.setTime();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		data = itms_restartBIO.successQueryDerive(add_time, task_name, doneNum, type,acc_oid);
		title = new String[7];
		title[0] = "属地";
		title[1] = "厂家";
		title[2] = "型号";
		title[3] = "软件版本";
		title[4] = "硬件版本";
		title[5] = "特别版本";
		title[6] = "执行时间";
		column = new String[7];
		column[0] = "city_name";
		column[1] = "vendor_name";
		column[2] = "device_model";
		column[3] = "softwareversion";
		column[4] = "hardwareversion";
		column[5] = "specversion";
		column[6] = "update_time";
		fileName = "重启成功查询";
		return "excel";
	}

	/**
	 * 总数导出
	 * 
	 * @return
	 */
	public String sunExcel()
	{
		this.setTime();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		data = itms_restartBIO.sunQueryDrive(add_time, task_name,acc_oid);
		title = new String[5];
		title[0] = "时间";
		title[1] = "重启成功数";
		title[2] = "未执行数号";
		title[3] = "未成功数";
		title[4] = "总数";
		column = new String[5];
		column[0] = "update_time";
		column[1] = "doneNum";
		column[2] = "unDoneNum";
		column[3] = "failNum";
		column[4] = "totalNum";
		fileName = "重启总数查询";
		return "excel";
	}
	/**
	 * 明细导出
	 */
	public String Detailed()
	{
		this.setTime();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		data = itms_restartBIO.sunQueryDrive(add_time, task_name,acc_oid);
		title = new String[10];
		title[0] = "属地";
		title[1] = "厂家";
		title[2] = "型号";
		title[3] = "设备序列号";
		title[4] = "LOID";
		title[5] = "软件版本";
		title[6] = "硬件版本";
		title[7] = "特别版本";
		title[8] = "执行时间";
		title[9] ="失败原因";
		column = new String[8];
		column[0] = "city_name";
		column[1] = "vendor_name";
		column[2] = "device_model";
		column[3] = "device_serialnumberb";
		column[4] = "username";
		column[5] = "softwareversion";
		column[6] = "hardwareversion";
		column[7] = "specversion";
		column[8] = "update_time";
		column[9] = "fail_desc";
	
		fileName = "明细查询";
		return "excel";
	}
	/**
	 * 成功数查询
	 * 
	 * @return
	 */
	public String success()
	{
		this.time();
		logger.warn("success===========");
		this.setTime();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		data = itms_restartBIO.SuccessQuery(add_time, task_name, doneNum, type,
				curPage_splitPage, num_splitPage,acc_oid);
		logger.warn("success=====>方法====data===>"+data);
		maxPage_splitPage = itms_restartBIO.getMaxPage_splitPage();
		return "success";
	}

	/**
	 * 失败重启查询
	 * 
	 * @return
	 */
	public String file()
	{
		this.time();
		logger.warn("file==========");
		this.setTime();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		data = itms_restartBIO.FailQuery(failNum, add_time, task_name, type,
				curPage_splitPage, num_splitPage,acc_oid);
		maxPage_splitPage = itms_restartBIO.getMaxPage_splitPage();
		return "file";
	}

	/**
	 * 未执行重启查询
	 * 
	 * @return
	 */
	public String unDoneNum()
	{
		this.time();
		logger.warn("unDoneNum");
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		data = itms_restartBIO.unexecute(unDoneNum, add_time, task_name, type,
				curPage_splitPage, num_splitPage,acc_oid);
		maxPage_splitPage = itms_restartBIO.getMaxPage_splitPage();
		logger.warn("data=======" + data);
		return "unexecute";
	}

	/**
	 * 总数查询
	 */
	public String sumQuery()
	{
		this.time();
		logger.warn("add_time=" + add_time);
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		data = itms_restartBIO.sunQuery(add_time, task_name, curPage_splitPage,
				num_splitPage,acc_oid);
		logger.warn("2222222222222222222");
		logger.warn("data==============" + data);
		maxPage_splitPage = itms_restartBIO.getMaxPage_splitPage();
		return "tote";
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
			starttime = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime))
		{
			endtime = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endtime = String.valueOf(dt.getLongTime());
		}
	}

	private void time()
	{
		DateTimeUtil dt = null;
		if (add_time == null || "".equals(add_time))
		{
			add_time = null;
		}
		else
		{
			dt = new DateTimeUtil(add_time);
			add_time = String.valueOf(dt.getLongTime());
		}
	}

	public void TIME()
	{
		DateTimeUtil dt = null;
		dt = new DateTimeUtil(update_time);
		add_time = String.valueOf(dt.getLongTime());
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
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

	public static Logger getLogger()
	{
		return logger;
	}

	public List<Map> getData()
	{
		return data;
	}

	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public LogRestartQueryBIO getItms_restartBIO()
	{
		return itms_restartBIO;
	}

	public void setItms_restartBIO(LogRestartQueryBIO itms_restartBIO)
	{
		this.itms_restartBIO = itms_restartBIO;
	}

	public String getAdd_time()
	{
		return add_time;
	}

	public void setAdd_time(String add_time)
	{
		this.add_time = add_time;
	}

	public String getTask_name()
	{
		return task_name;
	}

	public void setTask_name(String task_name)
	{
		this.task_name = task_name;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public List<Map> getLogRestartQuerderive()
	{
		return LogRestartQuerderive;
	}

	public void setLogRestartQuerderive(List<Map> logRestartQuerderive)
	{
		LogRestartQuerderive = logRestartQuerderive;
	}
}
