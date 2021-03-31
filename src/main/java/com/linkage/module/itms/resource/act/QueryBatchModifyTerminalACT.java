
package com.linkage.module.itms.resource.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.resource.bio.QueryBatchModifyTerminalBIO;

import action.splitpage.splitPageAction;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-3-30
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class QueryBatchModifyTerminalACT extends splitPageAction implements SessionAware,ServletRequestAware
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(QueryBatchModifyTerminalACT.class);
	// 开始时间
	private String starttime;
	// 结束时间
	private String endtime;
	// 任务名称
	private String taskName;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	//导出文件名称
	private String fileName;
	
	// 导入时间
	private String add_time;
	// 下发成功数
	private String doneNum;
	// 未触发数
	private String unDoneNum;
	// 未成功数
	private String failNum;
	// 总数
	private String totalNum;
	private List<Map> date;
	private List<Map> data;
	private QueryBatchModifyTerminalBIO bio;
	private String task_name;
	//替代status
	private String type;
	/**
	 * 账号类型：1.用户账号 2.设备序列号
	 */
	private String account_type;
	private String serv_account;
	

	/**
	 * 批量修改终端接入数查询
	 * @return
	 */
	public String query()
	{
		this.setTime();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		logger.warn("QueryBatchModifyTerminalACT=======>query()====方法入口");
		date = bio.query(starttime, endtime, taskName, curPage_splitPage, num_splitPage,acc_oid);
		logger.warn("date=======> "+date);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		logger.warn("maxPage_splitPage======>"+maxPage_splitPage);
		logger.warn("QueryBatchModifyTerminalACT=======>query()====方法出口");
		return "init";
	}
	/**
	 * 点击未触发数查询
	 */
	public String unDoneNumQuery()
	{
		logger.warn("unDoneNumQuery=====>方法入口");
		this.Time();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		date=bio.unDoneNumQuery( type, add_time, curPage_splitPage, num_splitPage,acc_oid,task_name);
		maxPage_splitPage=bio.getMaxPage_splitPage();
		logger.warn("unDoneNumQuery=====>方法出口");
		return "undone";
	}
	/**
	 * 未触发导出
	 * @return
	 */
	public String unexecuteExcel()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
	logger.warn("未触发导出");
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
		fileName = "查询终端接入数未执行查询";
		data=bio.unDoneNumDerive( type, add_time,acc_oid,task_name);
		return "excel";
	}
	/**
	 * 点击未成功数查询
	 */
	public String failNumQuery()
	{
		this.Time();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		logger.warn(" failNumQuery====>方法中的acc_oid参数====>"+acc_oid);
		date=bio.failNumQuery( type, add_time, curPage_splitPage, num_splitPage,acc_oid,task_name);
		maxPage_splitPage=bio.getMaxPage_splitPage();
		return "fail";
	}
	/**
	 * 失败导出
	 * @return
	 */
	public String fialExcel()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		logger.warn("失败导出");
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
		fileName = "查询终端接入数失败查询";
		data=bio.failNumDerive(type, add_time,acc_oid,task_name);
		return "excel";
	}
	/**
	 * 点击下发成功数查询
	 */
	public String doneNumQuery()
	{
		logger.warn("doneNumQuery方法入口======>");
		this.Time();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		date=bio.doneNumQuery( type, add_time, curPage_splitPage, num_splitPage,acc_oid,task_name);
		maxPage_splitPage=bio.getMaxPage_splitPage();
		logger.warn("doneNumQuery方法入口======>");
		return "success";
	}
	/**
	 * 成功导出
	 * @return
	 */
	public String successExcel()
	{
		logger.warn("成功导出");
		logger.warn("成功导出 时间"+add_time);
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
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
		fileName = "查询终端接入数成功查询";
		data = bio.doneNumDerive(type, add_time,acc_oid,task_name);
		return "excel";
	}
	/**
	 * 总数导出
	 * 
	 * @return
	 */
	public String sunExcel()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		
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
		fileName = "查询终端接入数总数查询";
		data = bio.sunQueryDrive(add_time, task_name,acc_oid);
		return "excel";
	}
	/**
	 * 明细导出
	 */
	public String Detailed()
	{
		this.Time();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		
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
		data = bio.sunQueryDrive(add_time, task_name,acc_oid);
		return "excel";
	}
	/**
	 * 总数查询
	 */
	public String sumQuery()
	{
		this.Time();
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		date = bio.sunQuery(add_time, task_name, curPage_splitPage,
				num_splitPage,acc_oid);
		logger.warn("date=========>"+date);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "total";
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
	/**
	 * 转换add_time
	 * @return
	 */
	public void Time()
	{
		DateTimeUtil dt = null;// 定义DateTimeUtil
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

	
	public String getTask_name()
	{
		return task_name;
	}
	
	public void setTask_name(String task_name)
	{
		this.task_name = task_name;
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

	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
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


	
	public String getAdd_time()
	{
		return add_time;
	}
	
	public void setAdd_time(String add_time)
	{
		this.add_time = add_time;
	}
	public String getDoneNum()
	{
		return doneNum;
	}

	public void setDoneNum(String doneNum)
	{
		this.doneNum = doneNum;
	}

	public String getUnDoneNum()
	{
		return unDoneNum;
	}

	public void setUnDoneNum(String unDoneNum)
	{
		this.unDoneNum = unDoneNum;
	}

	public String getFailNum()
	{
		return failNum;
	}

	public void setFailNum(String failNum)
	{
		this.failNum = failNum;
	}

	public String getTotalNum()
	{
		return totalNum;
	}

	public void setTotalNum(String totalNum)
	{
		this.totalNum = totalNum;
	}

	public List<Map> getDate()
	{
		return date;
	}

	public void setDate(List<Map> date)
	{
		this.date = date;
	}

	public QueryBatchModifyTerminalBIO getBio()
	{
		return bio;
	}

	public void setBio(QueryBatchModifyTerminalBIO bio)
	{
		this.bio = bio;
	}

	public String getAccount_type()
	{
		return account_type;
	}

	public void setAccount_type(String account_type)
	{
		this.account_type = account_type;
	}

	public String getServ_account()
	{
		return serv_account;
	}

	public void setServ_account(String serv_account)
	{
		this.serv_account = serv_account;
	}

	
	public String getType()
	{
		return type;
	}

	
	public void setType(String type)
	{
		this.type = type;
	}

	
	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	@Override
	public void setSession(Map<String, Object> session)
	{
		// TODO Auto-generated method stub
	}
	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		// TODO Auto-generated method stub
		
	}
	
	public List<Map> getData()
	{
		return data;
	}
	
	public void setData(List<Map> data)
	{
		this.data = data;
	}
	
}
