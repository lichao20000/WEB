/**
 * 
 */

package com.linkage.module.gtms.stb.diagnostic.action;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.diagnostic.serv.StbDeviceBatchRebootBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;


/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class StbDeviceBatchRebootACT extends splitPageAction implements ServletRequestAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(StbDeviceBatchRebootACT.class);
	@SuppressWarnings("unused")
	private HttpServletRequest request = null;
	StbDeviceBatchRebootBIO stbBio = null;
	// device ID
	private String deviceId = null;
	private String rebootFlag = null;
	private String restoreFlag = null;
	/** 1:ITMS 2:BBMS 4:STB */
	private String gw_type = null;
	private String ajax = null;
	/** 下级属地 */
	private String cityIds = "";
	/** 属地 */
	private String cityId = "";
	/** 初始化 */
	private List<Map<String, String>> cityList = null;
	/** 定制方式：1:属地定制，2:导入定制，*/
	private String taskType;
	/** 任务名称 */
	private String taskName;
	/** 重启时间 */
	private String startTime;
	/** 上传业务用户文件 */
	private File uploadCustomer;
	/** 上传业务用户文件名 */
	private String uploadFileName4Customer;
	/** 任务定制信息 */
	private List tasklist;
	/** 查询任务名称 */
	private String taskNameQ;
	/** 查询任务ID */
	private String taskIdQ;
	/** 设备重启结果信息 */
	private List devRestartResList;
	/** 删除任务ID */
	private String taskIdD;
	/** 是否删除任务 */
	private String delete;
	

	/**
	 * 机顶盒任务定制
	 * @return
	 */
	public String importConfig()
	{
		logger.warn("importConfig(cityId=[{}],[taskName=[{}],[cityIds=[{}])", new Object[] {cityId, taskName, cityIds});
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		long currTime = new Date().getTime() / 1000L;
		long taskId = currTime;
		long taskTime = currTime;
		if(cityIds != null && !"".equals(cityIds)){
			cityId = cityIds;
		}
		ajax = stbBio.importConfig(taskId,cityId,taskName,acc_oid,taskTime,uploadCustomer,uploadFileName4Customer,taskType,startTime);
		return "ajax";
	}
	
	
	/**
	 * @author 
	 * @return
	 */
	public String initBatchConfig()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getUser().getCityId());
		if(null!=cityList){
			for(Map<String,String> map : cityList){
				if(map.containsValue("省中心")){
					cityList.remove(map);
					break;
				}
			}
		}
		tasklist = stbBio.getStbBatchRebootTask(curPage_splitPage, num_splitPage, taskNameQ);
		maxPage_splitPage = stbBio.countStbBatchRebootTask(curPage_splitPage, num_splitPage, taskNameQ);
		return "initStbDeviceBatchReboot";
	}
	
	/**
	 * 根据cityId查询下级属地
	 * 
	 * @return
	 */
	public String getNextCity()
	{
		ajax = stbBio.getNextCity(cityId);
		return "ajax";
	}
	
	/**
	 * 根据taskIdQ查询设备重启信息
	 * @return
	 */
	public String queryRestartDev()
	{
		devRestartResList = stbBio.getStbBatchRestartDev(curPage_splitPage, num_splitPage, taskIdQ);
		maxPage_splitPage = stbBio.countStbBatchRestartDev(curPage_splitPage, num_splitPage, taskIdQ);
		return "queryRestartDev";
	}	
	
	/**
	 * @author 
	 * @return
	 */
	public String deleteTask()
	{
		if("yes".equals(delete) && !StringUtil.IsEmpty(taskIdD)){
			int[] delRes = stbBio.deleteTask(taskIdD);
			if (null != delRes && delRes.length > 0) {
				logger.warn("删除任务[{}]成功",taskIdD);
			} else {
				logger.warn("删除任务[{}]失败",taskIdD);
			}
		}
		return initBatchConfig();
	}
	
	/**
	 * execute
	 */
	public String execute() throws Exception
	{
		logger.debug("execute()");
		return "success";
	}

	public String reboot()
	{
		logger.debug("reboot()");
		this.rebootFlag = stbBio.reboot(deviceId, gw_type);
		return "reboot";
	}

	/**
	 * 恢复出厂设置
	 * 
	 * @return
	 */
	public String restore()
	{
		restoreFlag = stbBio.restore(deviceId, gw_type);
		return "restore";
	}

	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId()
	{
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}


	/**
	 * @return the rebootFlag
	 */
	public String getRebootFlag()
	{
		return rebootFlag;
	}

	/**
	 * @param rebootFlag
	 *            the rebootFlag to set
	 */
	public void setRebootFlag(String rebootFlag)
	{
		this.rebootFlag = rebootFlag;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getRestoreFlag()
	{
		return restoreFlag;
	}

	public void setRestoreFlag(String restoreFlag)
	{
		this.restoreFlag = restoreFlag;
	}

	
	public String getGw_type() {
		return gw_type;
	}

	
	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public StbDeviceBatchRebootBIO getStbBio() {
		return stbBio;
	}

	public void setStbBio(StbDeviceBatchRebootBIO stbBio) {
		this.stbBio = stbBio;
	}

	public String getCityIds() {
		return cityIds;
	}

	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}


	public String getTaskType() {
		return taskType;
	}


	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}


	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public File getUploadCustomer() {
		return uploadCustomer;
	}

	public void setUploadCustomer(File uploadCustomer) {
		this.uploadCustomer = uploadCustomer;
	}

	public String getUploadFileName4Customer() {
		return uploadFileName4Customer;
	}

	public void setUploadFileName4Customer(String uploadFileName4Customer) {
		this.uploadFileName4Customer = uploadFileName4Customer;
	}

	public List getTasklist() {
		return tasklist;
	}

	public void setTasklist(List tasklist) {
		this.tasklist = tasklist;
	}

	public String getTaskNameQ() {
		return taskNameQ;
	}

	public void setTaskNameQ(String taskNameQ) {
		this.taskNameQ = taskNameQ;
	}

	public String getTaskIdQ() {
		return taskIdQ;
	}

	public void setTaskIdQ(String taskIdQ) {
		this.taskIdQ = taskIdQ;
	}

	public List getDevRestartResList() {
		return devRestartResList;
	}

	public void setDevRestartResList(List devRestartResList) {
		this.devRestartResList = devRestartResList;
	}

	public String getTaskIdD() {
		return taskIdD;
	}

	public void setTaskIdD(String taskIdD) {
		this.taskIdD = taskIdD;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}
	
}
