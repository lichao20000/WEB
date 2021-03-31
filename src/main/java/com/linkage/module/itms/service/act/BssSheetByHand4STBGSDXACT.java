
package com.linkage.module.itms.service.act;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.linkage.module.itms.service.obj.SheetObj4GSStb;
import com.opensymphony.xwork2.ModelDriven;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.BssSheetByHand4STBGSDXBIO;

/**
 * 甘肃电信机顶盒手工工单
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2021年1月27日
 * @category com.linkage.module.itms.service.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BssSheetByHand4STBGSDXACT implements SessionAware, RequestAware,
		ModelDriven<SheetObj4GSStb>
{

	public static Logger logger = LoggerFactory
			.getLogger(BssSheetByHand4STBGSDXACT.class);
	private String ajax = null;
	private Map session;
	private Map<String,Object> request;
	private List<Map<String, String>> cityList = null;
	private String cityId = "";
	private BssSheetByHand4STBGSDXBIO bio;
	private SheetObj4GSStb obj = new SheetObj4GSStb();
	private String curSession = "curUser";

	public String execute()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) this.session.get(curSession);
		this.cityId = curUser.getCityId();
		this.cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		return "success_stb";
	}

	public String checkStbServAccount()
	{
		logger.debug("checkStbServAccount()");
		UserRes curUser = (UserRes) this.session.get(curSession);
		this.cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		this.obj = this.bio.checkStbServAccount(this.obj.getUserID());
		if (this.obj != null)
		{
			this.ajax = JSON.toJSONString(this.obj);
		}
		else
		{
			this.ajax = "000";
		}
		return "ajax";
	}

	/**
	 * 机顶盒开户业务
	 * 
	 * @return
	 */
	public String stbDoBusiness()
	{
		logger.debug("BssSheetByHandACT==>stbDoBusiness()");
		UserRes curUser = (UserRes) this.session.get(curSession);
		this.ajax = this.bio.stbDoBusiness(this.obj, curUser);
		return "ajax";
	}

	@Override
	public SheetObj4GSStb getModel()
	{
		return obj;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public String getAjax()
	{
		return this.ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public List<Map<String, String>> getCityList()
	{
		return this.cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public BssSheetByHand4STBGSDXBIO getBio()
	{
		return bio;
	}

	public void setBio(BssSheetByHand4STBGSDXBIO bio)
	{
		this.bio = bio;
	}

	public String getCityId()
	{
		return this.cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	@Override
	public void setRequest(Map<String, Object> request)
	{
		this.request = request;
	}
	
	public Map<String, Object> getRequest()
	{
		return this.request;
	}
	  
}
