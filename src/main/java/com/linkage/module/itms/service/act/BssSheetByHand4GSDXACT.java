package com.linkage.module.itms.service.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.BssSheetByHand4GSDXBIO;
import com.linkage.module.itms.service.obj.SheetObj;

public class BssSheetByHand4GSDXACT implements SessionAware
{
	public static Logger logger = LoggerFactory.getLogger(BssSheetByHand4GSDXACT.class);
	private SheetObj obj;
	private String ajax = null;
	private String voipProtocol = null;
	private Map session;
	private List<Map<String, String>> cityList = null;
	// 区分BBMS/ITMS
	private String gw_type;
	private BssSheetByHand4GSDXBIO bio;

	
	public String init()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		return "success";
	}

	public String checkLoid()
	{
		logger.warn("BssSheetByHand4HBACT.checkLoid({})",obj.getLoid());
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		obj = bio.checkLoid(obj.getLoid(),obj.getUserType());
		if (null != obj){
			ajax = obj.toString();
		}else{
			ajax = "000";
		}
		return "ajax";
	}
	

	public String doBusiness()
	{
		logger.warn("BssSheetByHand4HBACT.doBusiness({})",obj.getLoid());
		UserRes curUser = (UserRes) session.get("curUser");
		SheetObj sheetObj = bio.checkLoid(obj.getLoid(),obj.getUserType());
		if (!StringUtil.IsEmpty(obj.getSipVoipPwd()) && obj.getSipVoipPwd().equals("******")) {
			obj.setSipVoipPwd(sheetObj.getSipVoipPwd());
		}
		if (!StringUtil.IsEmpty(obj.getWifiPassword()) && obj.getWifiPassword().equals("******")) {
			obj.setWifiPassword(sheetObj.getWifiPassword());
		}
		ajax = bio.doBusiness(obj,curUser);
		return "ajax";
	}
	
	public String queryNetParam()
	{
		ajax = bio.queryNetParam(obj);
		return "ajax";
	}
	
	public String delSipSheet()
	{
		logger.warn("BssSheetByHand4HBACT.delSipSheet({})",obj.getLoid());
		ajax = bio.delSipVoipSheetResult(obj);
		return "ajax";
	}
	
	public String delIptvSheet()
	{
		logger.warn("BssSheetByHand4HBACT.delIptvSheet({})",obj.getLoid());
		ajax = bio.delIptvSheetResult(obj);
		return "ajax";
	}
	

	public String delHvoipSheet()
	{
		logger.warn("BssSheetByHand4HBACT.delHvoipSheet({})",obj.getLoid());
		ajax = bio.delH248VoipSheetResult(obj);
		return "ajax";
	}
	
	public String delNetSheet()
	{
		logger.warn("BssSheetByHand4HBACT.delNetSheet({})",obj.getLoid());
		ajax = bio.delNetSheetResult(obj);
		return "ajax";
	}
	public String delWifiSheet()
	{
		logger.warn("BssSheetByHand4HBACT.delWifiSheet({})",obj.getLoid());
		ajax = bio.delWifiSheetResult(obj);
		return "ajax";
	}

	public String delCloudNetSheet()
	{
		logger.warn("BssSheetByHand4HBACT.delCloudNetSheet({})",obj.getLoid());
		ajax = bio.delCloudNetSheetResult(obj);
		return "ajax";
	}


	public String delUserSheet()
	{
		logger.warn("BssSheetByHand4HBACT.delUserSheet({})",obj.getLoid());
		ajax = bio.delUserSheetResult(obj);
		return "ajax";
	}
	
	
	public Map getSession(){
		return session;
	}

	public void setSession(Map session){
		this.session = session;
	}

	public String getAjax(){
		return ajax;
	}

	public void setAjax(String ajax){
		this.ajax = ajax;
	}

	public List<Map<String, String>> getCityList(){
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList){
		this.cityList = cityList;
	}

	public BssSheetByHand4GSDXBIO getBio(){
		return bio;
	}

	public void setBio(BssSheetByHand4GSDXBIO bio){
		this.bio = bio;
	}

	public SheetObj getObj(){
		return obj;
	}

	public void setObj(SheetObj obj){
		this.obj = obj;
	}
	
	public String getVoipProtocol(){
		return voipProtocol;
	}
	
	public void setVoipProtocol(String voipProtocol){
		this.voipProtocol = voipProtocol;
	}
	
	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
}
