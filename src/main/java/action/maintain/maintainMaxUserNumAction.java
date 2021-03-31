package action.maintain;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import bio.maintain.mtMaxUserNumBIO;
import dao.maintain.mtMaxUserNumDAO;

public class maintainMaxUserNumAction implements ServletRequestAware {

	// request取登陆帐号使用
	private HttpServletRequest request;
	
	private mtMaxUserNumDAO mtMUNDao;
	
	private mtMaxUserNumBIO mtMUNBio;
	
	private String ajax;
	
	private String devIds;
	
	private String deviceId;
	
	private String mode;
	
	private String totalNum;
	
	private String fromDB;
	
	
	public String execute(){
		return "success";
	}

	public String getMaxUserNum() {
		if (null == devIds) {
			return "input";
		}
		String[] devIdsArr = devIds.split("\\|");
		
		ajax = mtMUNBio.getMaxUserNum(devIdsArr, fromDB);
		
		return "ajax";
	}
	

	public String editMUN() {
		
		ajax = mtMUNBio.editMUN(deviceId, mode, totalNum);
		
		return "ajax";
	}
	
	public String setParams() {
		
		ajax = mtMUNBio.setParams(deviceId, mode, totalNum);
		
		return "ajax";
	}
	
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	public void setMtMUNDao(mtMaxUserNumDAO mtMUNDao) {
		this.mtMUNDao = mtMUNDao;
	}

	public void setMtMUNBio(mtMaxUserNumBIO mtMUNBio) {
		this.mtMUNBio = mtMUNBio;
	}

	public String getDevIds() {
		return devIds;
	}

	public void setDevIds(String devIds) {
		this.devIds = devIds;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public String getFromDB() {
		return fromDB;
	}

	public void setFromDB(String fromDB) {
		this.fromDB = fromDB;
	}

	
	
}















