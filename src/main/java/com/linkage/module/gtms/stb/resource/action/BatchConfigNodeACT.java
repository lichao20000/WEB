
package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.obj.tr069.stbBatchConTask;
import com.linkage.module.gtms.stb.resource.serv.BatchConfigNodeBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.itms.service.obj.MQPublisher;

public class BatchConfigNodeACT extends splitPageAction
{

	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BatchConfigNodeACT.class);
	private String accoid; 
	private String areaId;
	private String strategyName;
	private String ajax;
	private BatchConfigNodeBIO bio;
	private String taskId;
	private List tasklist;
	// 查询开始时间
	private String startTime;
	// 查询结束时间
	private String endTime;
	private String taskName;
	private String queryVaild;
	private String operate;
	private List taskResultList = null;
	private List ipList = null;
	private List macList = null;
	private List paramList = null;
	private Map<String, String> taskDetailMap = null;

	public String getOperate()
	{
		return operate;
	}

	public void setOperate(String operate)
	{
		this.operate = operate;
	}

	// 导出数据
	private String[] title;
	private String[] column;
	private String fileName = "";
	private List data = null;
	// 批量软件升级mq
//	public static final String MQ_CON_TASK_ENAB = LipossGlobals
//			.getLipossProperty("stbBatchCon.enab");
//	public static final String MQ_CON_TASK_URL = LipossGlobals
//			.getLipossProperty("stbBatchCon.url");
//	public static final String MQ_CON_TASK_TOPIC = LipossGlobals
//			.getLipossProperty("stbBatchCon.topic");

	/**
	 * 批量导入升级初始化页面(有大的改动)
	 * 
	 * @author chendong
	 * @date April 22, 2013
	 * @param
	 * @return String
	 */
	public String initImport()
	{
		logger.debug("initImportSoft");
		UserRes curUser = WebUtil.getCurrentUser();
		accoid = StringUtil.getStringValue(curUser.getUser().getId());
		areaId = StringUtil.getStringValue(curUser.getAreaId());
		String cityId = StringUtil.getStringValue(curUser.getCityId());
		tasklist = bio.getBatchConfigNodeTask(curPage_splitPage, num_splitPage,
				queryVaild, startTime, endTime, cityId, taskName);
		maxPage_splitPage = bio.countBatchConfigNodeTask(curPage_splitPage,
				num_splitPage, queryVaild, startTime, endTime, cityId, taskName);
		return "initImport";
	}

	public String sendTask()
	{
		String message = "-1,删除任务操作失败";
		int result = 0;
		stbBatchConTask addobj = new stbBatchConTask("stbBatchConTask", taskId, operate);
		//生效
		if ("1".equals(operate))
		{
			result = bio.updateTaskStatus(taskId, "1");
		}
		//失效
		else if ("2".equals(operate))
		{
			if(LipossGlobals.inArea(Global.JXDX)){
				result = bio.updateTaskStatus(taskId, "-1");
			}else{
				result = bio.updateTaskStatus(taskId, "0");
			}
		}
		//删除
		else if ("3".equals(operate))
		{
			bio.deleteTask(taskId);
		}
		try
		{
//			logger.warn("MQ_CON_TASK_URL=" + MQ_CON_TASK_URL);
//			logger.warn("MQ_CON_TASK_TOPIC=" + MQ_CON_TASK_TOPIC);
//			MQPublisher publisher = new MQPublisher("1", MQ_CON_TASK_URL,
//					MQ_CON_TASK_TOPIC);
			MQPublisher publisher = new MQPublisher("stbBatchCon.task");
			publisher.publishMQ(addobj);
			message = "1,操作成功！";
		}
		catch (Exception e)
		{
			logger.error(" MQ消息发布失败， mesg({})", e.getMessage());
			message = "0,操作失败！";
		}
		
		ajax = message;
		return "ajax";
	}

	/**
	 * 查看结果
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getBatchConfigResult()
	{
		taskResultList = bio.getTaskResult(taskId);
		logger.warn("taskResultList=" + taskResultList);
		return "taskresult";
	}

	/**
	 * 查看详细
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getBatchConfigDetail()
	{
		taskDetailMap = bio.getTaskDetail(taskId);
		ipList = bio.getIpList(taskId);
		macList = bio.getMacList(taskId);
		paramList = bio.getParamList(taskId);
		logger.warn("taskDetailMap=" + taskDetailMap);
		return "taskdetail";
	}

	public String validateCurUser()
	{
		return "validateCurUser";
	}

	public String getAccoid()
	{
		return accoid;
	}

	public void setAccoid(String accoid)
	{
		this.accoid = accoid;
	}

	public String getAreaId()
	{
		return areaId;
	}

	public void setAreaId(String areaId)
	{
		this.areaId = areaId;
	}

	public String getStrategyName()
	{
		return strategyName;
	}

	public void setStrategyName(String strategyName)
	{
		this.strategyName = strategyName;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public BatchConfigNodeBIO getBio()
	{
		return bio;
	}

	public void setBio(BatchConfigNodeBIO bio)
	{
		this.bio = bio;
	}

	public List getTasklist()
	{
		return tasklist;
	}

	public void setTasklist(List tasklist)
	{
		this.tasklist = tasklist;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	public String getQueryVaild()
	{
		return queryVaild;
	}

	public void setQueryVaild(String queryVaild)
	{
		this.queryVaild = queryVaild;
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

	public List getData()
	{
		return data;
	}

	public void setData(List data)
	{
		this.data = data;
	}

	public Map<String, String> getTaskDetailMap()
	{
		return taskDetailMap;
	}

	public void setTaskDetailMap(Map<String, String> taskDetailMap)
	{
		this.taskDetailMap = taskDetailMap;
	}

	public List getTaskResultList()
	{
		return taskResultList;
	}

	public void setTaskResultList(List taskResultList)
	{
		this.taskResultList = taskResultList;
	}

	public List getIpList()
	{
		return ipList;
	}

	public void setIpList(List ipList)
	{
		this.ipList = ipList;
	}

	public List getMacList()
	{
		return macList;
	}

	public void setMacList(List macList)
	{
		this.macList = macList;
	}

	public List getParamList()
	{
		return paramList;
	}

	public void setParamList(List paramList)
	{
		this.paramList = paramList;
	}
}
