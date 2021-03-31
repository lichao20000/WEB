package com.linkage.module.itms.service.act;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.BssSheetByHand4JXLTBIO;
import com.linkage.module.itms.service.obj.SheetObj;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class BssSheetByHand4JXLTACT implements SessionAware
{
	public static Logger logger = LoggerFactory.getLogger(BssSheetByHand4JXLTACT.class);
	private SheetObj obj;
	private String ajax = null;
	private String voipProtocol = null;
	private Map session;
	private List<Map<String, String>> cityList = null;
	// 区分BBMS/ITMS
	private String gw_type;
	private BssSheetByHand4JXLTBIO bio;

	
	public String init()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		if(Global.ZJLT.equals(Global.instAreaShortName))
		{
			return "zjlt_success";
		}
		else
		{
			return "success";
		}

	}

	public String checkLoid()
	{
		logger.warn("BssSheetByHand4JXLTACT.checkLoid({})",obj.getLoid());
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
		logger.warn("BssSheetByHand4JXLTACT.doBusiness({})",obj.getLoid());
		UserRes curUser = (UserRes) session.get("curUser");
		ajax = bio.doBusiness(obj,curUser);
		return "ajax";
	}
	
	
	public String delHvoipSheet()
	{
		logger.warn("BssSheetByHand4JXLTACT.delHvoipSheet({})",obj.getLoid());
		ajax = bio.delH248VoipSheetResult(obj);
		return "ajax";
	}
	
	public String delNetSheet()
	{
		logger.warn("BssSheetByHand4JXLTACT.delNetSheet({})",obj.getLoid());
		ajax = bio.delNetSheetResult(obj);
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

	public BssSheetByHand4JXLTBIO getBio(){
		return bio;
	}

	public void setBio(BssSheetByHand4JXLTBIO bio){
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
