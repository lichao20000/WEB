package com.linkage.module.itms.service.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.BssSheetByHand4SDLTBIO;
import com.linkage.module.itms.service.obj.SheetObj;

/**
 * 山东联通RMS平台家庭网关宽带手工工单
 * @author wanghong5
 * 2015-12-30
 *
 */
public class BssSheetByHand4SDLTACT implements SessionAware
{
	public static Logger logger = LoggerFactory.getLogger(BssSheetByHand4SDLTACT.class);
	private SheetObj obj;
	private String ajax = null;
	private Map session;
	private List<Map<String, String>> cityList = null;
	//终端规格
	private List<HashMap<String,String>> specIdList=null;
	
	private BssSheetByHand4SDLTBIO bio;

	public String execute()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		specIdList=bio.getSpecId();
		return "success";
	}

	public String checkLoid()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId=curUser.getCityId();
		if("00".equals(cityId)){
			cityList=null;
		}else{
			cityList = CityDAO.getAllNextCityListByCityPid(cityId);
		}
		
		obj = bio.checkLoid(obj.getLoid(),obj.getUserType(),cityList);
		ajax = "000";
		if (null != obj)
		{
			ajax = obj.toString();
		}
		return "ajax";
	}

	public String doBusiness()
	{
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

	public BssSheetByHand4SDLTBIO getBio()
	{
		return bio;
	}

	public void setBio(BssSheetByHand4SDLTBIO bio)
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

	public List<HashMap<String,String>> getSpecIdList() {
		return specIdList;
	}

	public void setSpecIdList(List<HashMap<String,String>> specIdList) {
		this.specIdList = specIdList;
	}

}
