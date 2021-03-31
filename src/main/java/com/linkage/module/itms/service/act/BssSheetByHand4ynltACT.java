
package com.linkage.module.itms.service.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.BssSheetByHand4ynltBIO;
import com.linkage.module.itms.service.obj.SheetObj;

public class BssSheetByHand4ynltACT implements SessionAware
{

	public static Logger logger = LoggerFactory
			.getLogger(BssSheetByHand4ynltACT.class);
	private SheetObj obj;
	private String ajax = null;
	private String voipProtocol = null;
	private Map session;
	private List<Map<String, String>> cityList = null;
	private List<Map<String, String>> specList = null;
	
	// 区分BBMS/ITMS
	private String gw_type;

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	
	
	private BssSheetByHand4ynltBIO bio;

	public String execute()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		
		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		specList = bio.getSpec();
		return "success";
	}

	public String checkLoid()
	{
		logger.debug("checkLoid()");
		UserRes curUser = (UserRes) session.get("curUser");

		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		
		obj = bio.checkLoid(obj.getLoid(),obj.getUserType());
		if (null != obj)
		{
			ajax = obj.toString();
		}
		else 
		{
			ajax = "000";
		}
		return "ajax";
	}
	
	public String changeVoipPort()
	{
		logger.debug("BssSheetByHandACT==>changeVoipPort()");
		ajax = bio.getServInfo(obj.getLoid(),obj.getUserType(),obj.getSipVoipPort());
		return "ajax";
	}

	public String checkCustomerId()
	{
		logger.debug("checkLoid()");
		UserRes curUser = (UserRes) session.get("curUser");
//		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		// TODO
		ajax = bio.checkCustomerId(obj.getCustomerId());
		return "ajax";
	}

	public String doBusiness()
	{
		logger.debug("BssSheetByHandACT==>doBusiness()");
		UserRes curUser = (UserRes) session.get("curUser");
		ajax = bio.doBusiness(obj,curUser);
		return "ajax";
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public BssSheetByHand4ynltBIO getBio()
	{
		return bio;
	}

	public void setBio(BssSheetByHand4ynltBIO bio)
	{
		this.bio = bio;
	}

	public SheetObj getObj()
	{
		return obj;
	}

	public void setObj(SheetObj obj)
	{
		this.obj = obj;
	}

	public String getVoipProtocol()
	{
		return voipProtocol;
	}

	public void setVoipProtocol(String voipProtocol)
	{
		this.voipProtocol = voipProtocol;
	}

	public List<Map<String, String>> getSpecList()
	{
		return specList;
	}

	public void setSpecList(List<Map<String, String>> specList)
	{
		this.specList = specList;
	}
}
