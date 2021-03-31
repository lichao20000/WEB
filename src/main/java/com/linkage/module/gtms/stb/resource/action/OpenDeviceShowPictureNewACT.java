package com.linkage.module.gtms.stb.resource.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.OpenDeviceShowPictureNewBIO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-2-26
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class OpenDeviceShowPictureNewACT extends splitPageAction implements SessionAware,ServletRequestAware
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(OpenDeviceShowPictureNewACT.class);
	private OpenDeviceShowPictureNewBIO bio;
	private List tasklist;
	// 查询开始时间
	private String startTime;
	// 查询结束时间
	private String endTime;
	/** 任务id */
	private String taskId = "";
	/** 任务名 */
	private String taskName = "";
	//定制人
	private String acc_loginname="";
	private String status;
	private String ajax;
	private List taskResultList = null;
	private List<Map<String, String>> taskDetailMap = null;
	private String statu;
	private List statushow = new ArrayList();
	
	private Map session;
	private HttpServletRequest request; 
	private String view_url = "";
	private static String delTaskName = "点击删除任务按钮";
	private static String checkTaskName = "点击审核任务按钮";
	
	private File bootFile;
	private File startFile;
	private File authFile;
	private String bootFileName;
	private String startFileName;
	private String authFileName;
	public String initImport()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		String cityId = StringUtil.getStringValue(curUser.getCityId());
		tasklist=bio.getOrderTaskList(curPage_splitPage, num_splitPage, startTime, endTime, cityId, taskName, acc_loginname);
		maxPage_splitPage=bio.countOrderTask(curPage_splitPage, num_splitPage, startTime, endTime, cityId, taskName, acc_loginname);
		return "initImport";
		
	}
	
	public String init()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		String cityId = StringUtil.getStringValue(curUser.getCityId());
		if(statu.equals("1"))
		{
			tasklist=bio.getOrderTaskList1(curPage_splitPage, num_splitPage, startTime, endTime, cityId, taskName, acc_loginname);
			maxPage_splitPage=bio.countOrderTask1(curPage_splitPage, num_splitPage, startTime, endTime, cityId, taskName, acc_loginname);
			statushow.add("1");
		}else
		{
			tasklist=bio.getOrderTaskList(curPage_splitPage, num_splitPage, startTime, endTime, cityId, taskName, acc_loginname);
			maxPage_splitPage=bio.countOrderTask(curPage_splitPage, num_splitPage, startTime, endTime, cityId, taskName, acc_loginname);
		}
		return "init";
	}
	
	
	
	
	public String modPic()
	{
		
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		long currTime = new Date().getTime() / 1000L;
		ajax=bio.OpenDeviceShowPicConfig(StringUtil.getLongValue(taskId), currTime, acc_oid, bootFile, startFile, authFile, bootFileName,startFileName,authFileName);
		return "ajax";
	}
	
	
	
	public String validateCurUser() {
		return "validateCurUser";
	}
	public String getShowPictureConfigResult()
	{
		taskResultList = bio.getTaskResult(taskId,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countDeviceTask(curPage_splitPage, num_splitPage, taskId);
		return "taskResult";
	}
	
	public String getTaskDetail()
	{
		taskDetailMap=bio.getTaskDetail(taskId);
		return "taskDetail";
	}
	/**
	 * 删除
	 * @return
	 */
	public String doDelete(){
		logger.warn("doDelete()");
		addItemLog(delTaskName+"("+taskId+")");
	    ajax = bio.doDelete(taskId);
		return "ajax";
	}
	/**
	 * 修改任务状态
	 * @return
	 */
	public String updatestatus()
	{
		logger.warn("doCheck()");
		addItemLog(checkTaskName+"("+taskId+"),状态"+status);
		ajax=bio.updatestatus(status, taskId);
		return "ajax";
	}

	private void addItemLog(String operaName)
	{
		UserRes curUser = (UserRes) session.get("curUser");
		// 对于session失效的情况，不做日志处理。struts2拦截器会要求用户重新登录。
		if (curUser != null)
		{
			User user = curUser.getUser();
			bio.recordOperLog(operaName,user,request.getRemoteAddr(),request.getRemoteHost());
		}
	}
	
	public InputStream showView() throws IOException{
		
		 //获取文件流
		logger.warn("view_url="+view_url);
        String path = view_url.substring(0, view_url.lastIndexOf("/"));
        String fileName = view_url.substring(view_url.lastIndexOf("/")+1);
        logger.warn("view_url="+view_url+",path="+path+",fileName="+fileName);
        view_url = fileName;
        return new FileInputStream(new File(path,fileName));
	}
	
	public OpenDeviceShowPictureNewBIO getBio()
	{
		return bio;
	}

	
	public void setBio(OpenDeviceShowPictureNewBIO bio)
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

	
	public String getAcc_loginname()
	{
		return acc_loginname;
	}

	
	public void setAcc_loginname(String acc_loginname)
	{
		this.acc_loginname = acc_loginname;
	}

	
	public String getStatus()
	{
		return status;
	}

	
	public void setStatus(String status)
	{
		this.status = status;
	}

	
	public String getAjax()
	{
		return ajax;
	}

	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	
	public List getTaskResultList()
	{
		return taskResultList;
	}

	
	public void setTaskResultList(List taskResultList)
	{
		this.taskResultList = taskResultList;
	}

	
	public List<Map<String, String>> getTaskDetailMap()
	{
		return taskDetailMap;
	}

	
	public void setTaskDetailMap(List<Map<String, String>> taskDetailMap)
	{
		this.taskDetailMap = taskDetailMap;
	}

	
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
	public String getStatu()
	{
		return statu;
	}
	
	public void setStatu(String statu)
	{
		this.statu = statu;
	}
	
	public List getStatushow()
	{
		return statushow;
	}
	
	public void setStatushow(List statushow)
	{
		this.statushow = statushow;
	}
	
	public Map getSession()
	{
		return session;
	}

	@Override
	public void setSession(Map session)
	{
		this.session = session;
	}		
	
	@Override
	public void setServletRequest(HttpServletRequest request) {  
		 // request的setter方法  
		this.request = request;  
	}  
	public HttpServletRequest getServletRequest() {  
		return  this.request;
	}

	
	public String getView_url()
	{
		return view_url;
	}

	
	public void setView_url(String view_url)
	{
		this.view_url = view_url;
	}

	
	public File getBootFile()
	{
		return bootFile;
	}

	
	public void setBootFile(File bootFile)
	{
		this.bootFile = bootFile;
	}

	
	public File getStartFile()
	{
		return startFile;
	}

	
	public void setStartFile(File startFile)
	{
		this.startFile = startFile;
	}

	
	public File getAuthFile()
	{
		return authFile;
	}

	
	public void setAuthFile(File authFile)
	{
		this.authFile = authFile;
	}

	
	public String getBootFileName()
	{
		return bootFileName;
	}

	
	public void setBootFileName(String bootFileName)
	{
		this.bootFileName = bootFileName;
	}

	
	public String getStartFileName()
	{
		return startFileName;
	}

	
	public void setStartFileName(String startFileName)
	{
		this.startFileName = startFileName;
	}

	
	public String getAuthFileName()
	{
		return authFileName;
	}

	
	public void setAuthFileName(String authFileName)
	{
		this.authFileName = authFileName;
	} 
	
}
