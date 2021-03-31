
package com.linkage.module.itms.service.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.BssSheetByHand4AHBIO;
import com.linkage.module.itms.service.obj.SheetObj;

public class BssSheetByHand4AHACT implements SessionAware
{

	public static Logger logger = LoggerFactory
			.getLogger(BssSheetByHand4AHACT.class);
	private SheetObj obj;
	private String ajax = null;
	private String voipProtocol = null;
	private Map session;
	private List<Map<String, String>> cityList = null;
	private List<Map<String, String>> lineList = null;
	private String lineId="";
	// 区分BBMS/ITMS
	private String gw_type;
	private String line="";
	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	
	
	private BssSheetByHand4AHBIO bio;

	public String execute()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		return "success";
	}

	public String checkLoid()
	{
		logger.debug("checkLoid()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		// TODO
		obj = bio.checkLoid(obj.getLoid(),obj.getUserType());
		if (null != obj)
		{
			ajax = obj.toString();
//			logger.info("obj.toString:"+ajax);
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
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		// TODO
		ajax = bio.checkCustomerId(obj.getCustomerId());
		return "ajax";
	}

	public String doBusiness()
	{
		logger.debug("BssSheetByHandACT==>doBusiness()");
		UserRes curUser = (UserRes) session.get("curUser");
		ajax = bio.doBusiness(obj,curUser,obj.getUserType());
		return "ajax";
	}
	
	public String deleteVoipPort()
	{
		logger.debug("deleteVoipPort()");
		UserRes curUser = (UserRes) session.get("curUser");
		ajax = bio.deleteVoipPort(obj,curUser);
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

	public BssSheetByHand4AHBIO getBio()
	{
		return bio;
	}

	public void setBio(BssSheetByHand4AHBIO bio)
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

	
	public List<Map<String, String>> getLineList()
	{
		return lineList;
	}

	
	public void setLineList(List<Map<String, String>> lineList)
	{
		this.lineList = lineList;
	}

	
	public String getLineId()
	{
		return lineId;
	}

	
	public void setLineId(String lineId)
	{
		this.lineId = lineId;
	}

	
	public String getLine()
	{
		return line;
	}

	
	public void setLine(String line)
	{
		this.line = line;
	}


	
}
