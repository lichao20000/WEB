package com.linkage.module.gtms.stb.resource.serv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.resource.dao.BatchSendUserInfoDAO;
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gwms.util.StringUtil;

public class BatchSendUserInfoBIO {
	private static Logger logger = LoggerFactory
			.getLogger(BatchSendUserInfoBIO.class);

	private BatchSendUserInfoDAO dao;

	public BatchSendUserInfoDAO getDao() {
		return dao;
	}

	public void setDao(BatchSendUserInfoDAO dao) {
		this.dao = dao;
	}

	public String saveBatchSendUserInfoTask(long taskId, String taskName, long acc_oid,
			long addTime, String accountFilePath, File uploadCustomer,
			String uploadFileName4Customer, String acc_cityId, String configType) {

		logger.warn(
				"saveBatchSendUserInfoTask(taskId={},taskName={},acc_oid={},addTime={},accountFilePath={},configType={})",
				new Object[] { taskId, taskName, acc_oid, addTime, accountFilePath, configType });
		String filePath = "";
		if (null != uploadCustomer && uploadCustomer.isFile()
				&& uploadCustomer.length() > 0) {
			// 如果是导入帐号，将导入帐号的文件放到指定的目录底下
			String targetDirectory = "";
			filePath = "/accountFile";
			String targetFileName = "";
			HttpServletRequest request = null;
			try {
				// 将文件存放到指定的路径中
				request = ServletActionContext.getRequest();
				targetDirectory = ServletActionContext.getServletContext()
						.getRealPath(filePath);
				// 由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
				targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS() + "_"
						+ uploadFileName4Customer;
				File target = new File(targetDirectory, targetFileName);
				FileUtils.copyFile(uploadCustomer, target);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("批量导入升级，上传文件时出错");
			}
			filePath = "http://" + request.getLocalAddr() + ":"
					+ request.getServerPort() + request.getContextPath()
					+ filePath + "/" + targetFileName;
		}
		int ier = dao.saveBatchSendUserInfoTask(taskId, taskName, acc_oid, addTime,
				filePath, acc_cityId, configType);
		if (ier == 1) {
			return "任务定制成功!";
		} else {
			return "任务定制失败！";
		}
	}

	public List getOrderTaskList(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName,String isActive) {
		
		return dao.getOrderTaskList(curPage_splitPage, num_splitPage,
				 startTime, endTime, cityId, taskName,acc_oid,accName,isActive);
	}

	public int countOrderTask(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String cityId, String taskName,long acc_oid,String accName,String isActive) {
		return dao.countOrderTask(curPage_splitPage,
				num_splitPage,startTime, endTime, cityId, taskName,acc_oid,accName,isActive);
	}

	public List getTaskResult(String taskId ,int curPage_splitPage,int num_splitPage) {
		return dao.getTaskResult(taskId,curPage_splitPage, num_splitPage);
	}

	public int countDeviceTask(int curPage_splitPage, int num_splitPage,String taskId) {
		return dao.countTaskResult(curPage_splitPage, num_splitPage, taskId);
	}

	public String doDelete(String taskId) {
		return dao.doDelete(taskId);
	}
	
	public String beNotActived(String taskId) {
		return dao.beNotActived(taskId);
	}
	
	public Map<String, String> getTaskDetail(String taskId) {
		Map<String,String> taskMap = dao.getTaskDetail(taskId);
		return taskMap;
	}

}
