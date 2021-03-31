package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.PicTaskQueryBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.litms.common.util.Base64;
import com.linkage.litms.common.util.MD5;

/**
 * @author wuchao(工号) added by zhangsibei
 * @version 1.0
 * @since 2012-3-30 上午09:20:21
 * @category com.linkage.module.lims.stb.resource.act
 * @copyright 南京联创科技 网管科技部
 */
public class PicTaskQueryACT extends splitPageAction
{

	private static final long serialVersionUID = 3947403319365083327L;
	private String priority;
	private String status;
	private String cityId;
	private String groupId;
	private String tradeId;
	private String transactionId;
	private String reason;
	private String taskId;
	private List<Map<String, String>> cityList;
	//类别（启动，开机，认证）
	private String sortName;
	//hdorsd(hd 高清，sd标清)
	private String hdorsd;
	private PicTaskQueryBIO bio;
	private List<Map> resultList;
	private String ajax;
	private Map<String, String> detailMap;
	private String username;
	private String password;
	private Map<String, String> resultMap;
	private List<Map<String, String>> groupList;
	private List<Map>  resultDetailList;
	private String updateStatus;
	
	private String infoQueryType ;
	public String init() {
		UserRes curUser = WebUtil.getCurrentUser();
		this.cityList = CityDAO.getNextCityListByCityPid(curUser.getUser().getCityId());
		this.groupList = bio.getAlllGroupName();
		return "init";
	}

	public String execute() {
		resultList = bio.query(priority, status, cityId, groupId, tradeId,transactionId,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getCount(priority, status, cityId, groupId,tradeId,transactionId,
				 curPage_splitPage, num_splitPage);
		return "list";
	}

	/**
	 * 更新策略的状态
	 * 
	 * @return
	 */
	public String updateStrategyStatus() {
		UserRes curUser = WebUtil.getCurrentUser();
		ajax = bio.updateStatus(taskId, transactionId, status, reason, curUser
				.getUser().getId());
		return "ajax";
	}
	/**
	 * 设备升级的详细结果
	 * @return
	 */
	public String getSoftUpDetail(){
		resultDetailList = bio.getSoftUpDetail(taskId,updateStatus,curPage_splitPage,num_splitPage,sortName,hdorsd);
		maxPage_splitPage = bio.getSoftCount(taskId,updateStatus,curPage_splitPage,num_splitPage,sortName,hdorsd);
		return "detailResult";
	}
	public String deleteStrategy() {
		ajax = bio.deleteStrategy(taskId);
		return "ajax";
	}

	public String detail() {
		detailMap = bio.detail(taskId);
		return "detail";
	}

	public String result() {
		resultMap = bio.result(taskId);
		return "result";
	}

	public String validateCurUser() {
		return "validateCurUser";
	}

	public String validateUser() {
		UserRes curUser = WebUtil.getCurrentUser();
		//宁夏数据库密码已加密。故需要将界面传过来的密码加密处理之后再校验。
		if(LipossGlobals.inArea(Global.NXDX))
		{
			//MD5.getMD5(encodeBase64(defaultKey));
			password = MD5.getMD5(Base64.encode(password));
		}
		if (curUser.getUser().getAccount().equals(username)
				&& curUser.getUser().getPasswd().equals(password)) {
			ajax = "1";
		} else {
			ajax = "0";
		}
		return "ajax";
	}

	public String countOperate() {
		ajax = bio.getCountOperate(taskId);
		return "ajax";
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public PicTaskQueryBIO getBio() {
		return bio;
	}

	public void setBio(PicTaskQueryBIO bio) {
		this.bio = bio;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public List<Map> getResultList() {
		return resultList;
	}

	public void setResultList(List<Map> resultList) {
		this.resultList = resultList;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Map<String, String> getDetailMap() {
		return detailMap;
	}

	public void setDetailMap(Map<String, String> detailMap) {
		this.detailMap = detailMap;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String, String> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, String> resultMap) {
		this.resultMap = resultMap;
	}

	public List<Map<String, String>> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Map<String, String>> groupList) {
		this.groupList = groupList;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		try {
			this.reason = java.net.URLDecoder.decode(reason, "UTF-8");
		} catch (Exception e) {
			this.reason = reason;
		}
	}
	public void setResultDetailList(List<Map> resultDetailList) {
		this.resultDetailList = resultDetailList;
	}

	public String getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}

	public List<Map> getResultDetailList() {
		return resultDetailList;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public void setHdorsd(String hdorsd) {
		this.hdorsd = hdorsd;
	}

	public String getInfoQueryType() {
		return infoQueryType;
	}

	public void setInfoQueryType(String infoQueryType) {
		this.infoQueryType = infoQueryType;
	}
	
}
