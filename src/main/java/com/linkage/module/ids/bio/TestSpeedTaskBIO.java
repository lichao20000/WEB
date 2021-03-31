package com.linkage.module.ids.bio;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.ids.dao.TestSpeedTaskDAO;

public class TestSpeedTaskBIO {
	private static Logger logger = LoggerFactory
			.getLogger(TestSpeedTaskBIO.class);

	private TestSpeedTaskDAO dao;

	public String saveTestSpeedTask(long taskId, String taskName,
			long acc_oid, long addTime, String accountFilePath,File uploadCustomer,
			String uploadFileName4Customer,String acc_cityId) {
		
		    logger.warn("saveTestSpeedTask(taskId={},taskName={},acc_oid={},addTime={},accountFilePath={})",
		    		new Object[]{taskId,taskName,acc_oid,addTime,accountFilePath});
			
			String filePath="";
			if (null != uploadCustomer && uploadCustomer.isFile() && uploadCustomer.length()>0) {
				//如果是导入帐号，将导入帐号的文件放到指定的目录底下
				 String targetDirectory="";
				 filePath = "/accountFile";
				 String targetFileName ="";
				 HttpServletRequest request = null;
				 try {
				 //将文件存放到指定的路径中
				 request = ServletActionContext.getRequest();
				 targetDirectory = ServletActionContext.getServletContext().getRealPath(filePath);
				 //由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
		         targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS() +"_"+ uploadFileName4Customer;
		         File target = new File(targetDirectory, targetFileName);
				 FileUtils.copyFile(uploadCustomer, target);
				} catch (IOException e) {
					logger.error("批量导入升级，上传文件时出错");
				}
		        filePath = request.getScheme()+"://"+request.getLocalAddr()+":"+request.getServerPort() + request.getContextPath() +filePath +"/"+targetFileName;
				}
			int ier = dao.saveTestSpeedTask(taskId, taskName, acc_oid, addTime,filePath,acc_cityId);
		 if (ier == 1)
		{
			return "任务定制成功!";
		}
		else
		{
			return "任务定制失败！";
		}
	}
	
	public List getOrderTaskList(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName) {
		
		return dao.getOrderTaskList(curPage_splitPage, num_splitPage,
				 startTime, endTime, cityId, taskName,acc_oid,accName);
	}
	
	public int countOrderTask(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName) {
		// TODO Auto-generated method stub
		return dao.countOrderTask(curPage_splitPage,
				num_splitPage,startTime, endTime, cityId, taskName,acc_oid,accName);
	}
	
	public List getTaskResult(String taskId ,int curPage_splitPage,int num_splitPage) {
		return dao.getTaskResult(taskId,curPage_splitPage, num_splitPage);
	}
	
	public int countDeviceTask(int curPage_splitPage, int num_splitPage,String taskId) {
		return dao.countTaskResult(curPage_splitPage, num_splitPage, taskId);
	}
	
	public String doDelete(String taskId) {
		// TODO Auto-generated method stub
		return dao.doDelete(taskId);
	}
	
	public TestSpeedTaskDAO getDao() {
		return dao;
	}

	public void setDao(TestSpeedTaskDAO dao) {
		this.dao = dao;
	}
}
