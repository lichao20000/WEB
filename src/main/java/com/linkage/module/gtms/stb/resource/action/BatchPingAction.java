package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.RemoteShellExecutor;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.BatchPingBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings("rawtypes")
public class BatchPingAction extends splitPageAction implements ServletResponseAware {
	
	/**
	 *  serial
	 */
	private static final long serialVersionUID = 1L;

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BatchPingAction.class);

	BatchPingBIO bpBIO = null;
	
	private String ajax = null;
	
	private List dataList = null;

	private String cityId = "";

	private String deviceIp = null;
	
	private String result = "";
	
	private List<Map<String,String>> cityList = null;
	
	/** 开始时间 */
	private String starttime = "";
	/** 结束时间 */
	private String endtime = "";
	
	/**
	 * 初始化页面数据
	 * @return
	 * @throws Exception
	 */
	public String InitData() throws Exception {
		logger.debug("InitData()");
		UserRes curUser = WebUtil.getCurrentUser();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		return "init";
	}

	/**
	 * 执行批量ping脚本
	 * @throws Exception
	 */
	public String pingCmd() throws Exception {
		String ip = LipossGlobals.getLipossProperty("bathPing.ip");
		String user = LipossGlobals.getLipossProperty("bathPing.user");
		String pswd = LipossGlobals.getLipossProperty("bathPing.pswd");
		String cmds = LipossGlobals.getLipossProperty("bathPing.cmds");
		RemoteShellExecutor executor = new RemoteShellExecutor(ip, user, pswd);
		int retcode = executor.exec(cmds);
		logger.warn("cmds ret is:" + retcode);
		if (retcode == 0) {
			ajax = "执行脚本成功！";
		}
		else {
			ajax = "执行脚本失败，请联系管理员！";
		}
		return "ajax";
	}
	
	/**
	 * 查询数据
	 * @return
	 */
	public String queryDataList() {
		logger.debug("BatchPingAction=>queryDataList()");
		UserRes curUser = WebUtil.getCurrentUser();
		if(StringUtil.IsEmpty(cityId)){
			cityId = curUser.getCityId();
		}else{
			cityId = cityId.trim();
		}
		dataList = bpBIO.getDataList(curPage_splitPage, num_splitPage, cityId, deviceIp, result, starttime, endtime);
		int total = bpBIO.getDataCount(cityId, deviceIp, result, starttime, endtime);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "list";
	}
	
	
	// 导出文件标题
	private String[] title;
	// 导出文件列名
	private String[] column;
	// 数据集
	private List data;
	// 导出文件名
	private String fileName;

	/**
	 * 导出
	 * @return
	 */
	public String exportExcel() {
		title = new String[] {
				"设备ip", "设备名称", "属地", "结果", "结果描述",
				"操作时间", "成功数", "失败数", "丢包率", "最小响应时间(ms)", 
				"平均响应时间(ms)", "最大响应时间(ms)"
				};
		column = new String[]{
				"device_ip", "device_name", "city_name", "result", "result_desc",
				"operate_time", "succes_num", "fail_num", "packet_loss_rate", 
				"min_response_time", "avg_response_time", "max_response_time"
				};
		logger.debug("BatchPingAction=>exportExcel()");
		UserRes curUser = WebUtil.getCurrentUser();
		if(StringUtil.IsEmpty(cityId)){
			cityId = curUser.getCityId();
		}else{
			cityId = cityId.trim();
		}
		data = bpBIO.getDataList(-1, 0, cityId, deviceIp, result, starttime, endtime);
		fileName = "batchPingResult";
		return "excel";
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	
	public BatchPingBIO getBpBIO() {
		return bpBIO;
	}

	public void setBpBIO(BatchPingBIO bpBIO) {
		this.bpBIO = bpBIO;
	}

	public List getDataList() {
		return dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
//		this.response = response;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
}
