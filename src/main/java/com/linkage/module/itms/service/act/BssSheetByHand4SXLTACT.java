
package com.linkage.module.itms.service.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.linkage.module.itms.service.obj.SXLTSheetObj;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.BssSheetByHand4SXLTBIO;
import com.linkage.module.itms.service.obj.SXLTSheetObj;

/**
 * 山西联通手工工单
 * 
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-8-23
 * @category com.linkage.module.itms.service.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BssSheetByHand4SXLTACT implements SessionAware,RequestAware, ModelDriven<SXLTSheetObj>
{

	public static Logger logger = LoggerFactory
			.getLogger(BssSheetByHand4HBLTACT.class);
//	private SXLTSheetObj obj;
	private String ajax = null;
	private String voipProtocol = null;
	private Map session;
	private Map request;
	private List<Map<String, String>> cityList = null;
	private String cityId = "";
	private String gw_type;
	private String loid = "";
	private List<String> netUsernameList = new ArrayList<String>();
	private BssSheetByHand4SXLTBIO bio;

    private SXLTSheetObj obj = new SXLTSheetObj();

    @Override
    public SXLTSheetObj getModel() {
        return obj;
    }
	public String getGw_type()
	{
		return this.gw_type;
	}

	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	public String execute()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) this.session.get("curUser");
		this.cityId = curUser.getCityId();
		this.loid = ((String) this.request.get("loid"));
		this.cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		return "success";
	}

	public String execute_stb()
	{
		logger.debug("execute_stb()");
		UserRes curUser = (UserRes) this.session.get("curUser");
		this.cityId = curUser.getCityId();
		this.loid = ((String) this.request.get("loid"));
		this.cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		return "success_stb";
	}


	public String checkLoidNew()
	{
		logger.warn("checkLoidNew()");
		UserRes curUser = (UserRes) this.session.get("curUser");
		this.cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		String loid = this.obj.getLoid();
		String userType = this.obj.getUserType();
		HashMap hashMap = this.bio.checkLoidNew(loid, userType);
		if (hashMap != null && hashMap.size() > 0)
		{
			this.ajax = JSON.toJSONString(hashMap);
		}
		else
		{
			this.ajax = "000";
		}
		return "ajax";
	}

	public String checkStbLoid()
	{
		logger.debug("checkStbLoid()");
		UserRes curUser = (UserRes) this.session.get("curUser");
		this.cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		this.obj = this.bio.checkStbLoid(this.obj.getLoid());
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

	public String checkCustomerId()
	{
		logger.debug("checkLoid()");
		UserRes curUser = (UserRes) this.session.get("curUser");
		this.cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		this.ajax = this.bio.checkCustomerId(this.obj.getCustomerId());
		return "ajax";
	}

	public String doBusiness()
	{
		logger.debug("BssSheetByHandACT==>doBusiness()");
		UserRes curUser = (UserRes) this.session.get("curUser");
		this.ajax = this.bio.doBusiness(this.obj, curUser);
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
		UserRes curUser = (UserRes) this.session.get("curUser");
		this.ajax = this.bio.stbDoBusiness(this.obj, curUser);
		return "ajax";
	}

	public String delBusiness()
	{
		logger.debug("BssSheetByHandACT==>delBusiness()");
		UserRes curUser = (UserRes) this.session.get("curUser");
		this.ajax = this.bio.delBusiness(this.obj, curUser);
		return "ajax";
	}

	public String delStbBusiness()
	{
		logger.debug("BssSheetByHandACT==>delStbBusiness()");
		UserRes curUser = (UserRes) this.session.get("curUser");
		this.ajax = this.bio.delStbBusiness(this.obj, curUser);
		return "ajax";
	}

	public Map getSession()
	{
		return this.session;
	}

	@Override
    public void setSession(Map session)
	{
		this.session = session;
	}

	public Map getRequest()
	{
		return this.request;
	}

	@Override
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

	public BssSheetByHand4SXLTBIO getBio()
	{
		return bio;
	}

	public void setBio(BssSheetByHand4SXLTBIO bio)
	{
		this.bio = bio;
	}

	/*public SXLTSheetObj getObj()
	{
		return this.obj;
	}

	public void setObj(SXLTSheetObj obj)
	{
		this.obj = obj;
	}*/

	public String getVoipProtocol()
	{
		return this.voipProtocol;
	}

	public void setVoipProtocol(String voipProtocol)
	{
		this.voipProtocol = voipProtocol;
	}

	public String getLoid()
	{
		return this.loid;
	}

	public void setLoid(String loid)
	{
		this.loid = loid;
	}

	public String getCityId()
	{
		return this.cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	
	public List<String> getNetUsernameList()
	{
		return netUsernameList;
	}

	
	public void setNetUsernameList(List<String> netUsernameList)
	{
		this.netUsernameList = netUsernameList;
	}

}
