package com.linkage.module.itms.service.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.BssSheetByHand4NXLTBIO;
import com.linkage.module.itms.service.obj.SheetObj;

public class BssSheetByHand4NXLTACT
  implements SessionAware, RequestAware
{
  public static Logger logger = LoggerFactory.getLogger(BssSheetByHand4NXLTACT.class);
  private SheetObj obj;
  private String ajax = null;
  private String voipProtocol = null;
  private Map session;
  private Map request;
  private List<Map<String, String>> cityList = null;
  private List<HashMap<String,String>> specIdList=null;
  private String cityId = "";
  private String gw_type;
  private String loid = "";
  private BssSheetByHand4NXLTBIO bio;

  public String getGw_type()
  {
    return this.gw_type;
  }

  public void setGw_type(String gw_type) {
    this.gw_type = gw_type;
  }

  public String execute()
  {
    logger.debug("execute()");
    UserRes curUser = (UserRes)this.session.get("curUser");
    this.cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
    setSpecIdList(bio.getSpecId());
    return "success";
  }
  

  public String checkLoid()
  {
    logger.debug("checkLoid()");
    UserRes curUser = (UserRes)this.session.get("curUser");

    this.cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());

    this.obj = this.bio.checkLoid(this.obj.getLoid(), this.obj.getUserType());

    if (this.obj != null)
    {
      this.ajax = this.obj.toString();
    }
    else
    {
      this.ajax = "000";
    }
    return "ajax";
  }

  public String changeVoipPort()
  {
    logger.debug("BssSheetByHandACT==>changeVoipPort()");
    this.ajax = this.bio.getServInfo(this.obj.getLoid(), this.obj.getUserType(), this.obj.getSipVoipPort());
    return "ajax";
  }

  public String doBusiness()
  {
    logger.debug("BssSheetByHandACT==>doBusiness()");
    UserRes curUser = (UserRes)this.session.get("curUser");
    this.ajax = this.bio.doBusiness(this.obj, curUser);
    return "ajax";
  }
   

  public String delBusiness()
  {
    logger.debug("BssSheetByHandACT==>delBusiness()");
    UserRes curUser = (UserRes)this.session.get("curUser");
    this.ajax = this.bio.delBusiness(this.obj, curUser);
    return "ajax";
  }

  public Map getSession()
  {
    return this.session;
  }

  public void setSession(Map session)
  {
    this.session = session;
  }

  public Map getRequest()
  {
    return this.request;
  }

  public void setRequest(Map request)
  {
    this.request = request;
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

  public BssSheetByHand4NXLTBIO getBio()
  {
    return this.bio;
  }

  public void setBio(BssSheetByHand4NXLTBIO bio)
  {
    this.bio = bio;
  }

  public SheetObj getObj()
  {
    return this.obj;
  }

  public void setObj(SheetObj obj)
  {
    this.obj = obj;
  }

  public String getVoipProtocol()
  {
    return this.voipProtocol;
  }

  public void setVoipProtocol(String voipProtocol)
  {
    this.voipProtocol = voipProtocol;
  }

  public String getLoid() {
    return this.loid;
  }

  public void setLoid(String loid) {
    this.loid = loid;
  }

  public String getCityId() {
    return this.cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

public List<HashMap<String,String>> getSpecIdList()
{
	return specIdList;
}

public void setSpecIdList(List<HashMap<String,String>> specIdList)
{
	this.specIdList = specIdList;
}
}