
package com.linkage.module.gtms.stb.resource.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.userMessageManageBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-1-30
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class userMessageManageACT extends splitPageAction implements SessionAware,ServletRequestAware
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(userMessageManageACT.class);
	private userMessageManageBIO bio;
	private Map session;
	//属地
	private String gwShare_cityId;
	//下级属地
	private String citynext;
	// 开始时间
	private String starttime;
	// 结束时间
	private String endtime;
	// 属地列表
	private List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();
	// 业务账号
	private String servaccount="";
	// 平台类型
	private String platformType="";
	// MAC地址
	private String MAC="";
	// 用户分组
	private String userGroupID="";
	// 接入方式
	private String stbaccessStyle="";
	private String ajax;
	// 鉴权账号
	private String authUser="";
	// 鉴权密码
	private String authPwd="";
	// 业务类型
	private String servTypeId="";
	// 操作类型
	private String operateId="";
	// IPTV业务绑定手机号码
	private String iptvBindPhone="";
	// IPTV网络接入帐号
	private String stbuser="";
	// IPTV网络接入密码
	private String stbpwd="";
	// IPTV业务密码
	private String servpwd="";
	// 上行接入方式
	private String stbuptyle="";
	private List<Map> platformTypeList=new  ArrayList<Map>();
	// 结果集
	private List<Map> date = new ArrayList<Map>();
	private String dealDate="";
	private  List<Map> userGroupIDList=new  ArrayList<Map>();
	private String PcityIdByCityId="";
	/** 权限控制* */
	private String showType="";
	private Map<String,String> map=new HashMap<String, String>();
	
	private HttpServletRequest request;
	private String instArea=Global.instAreaShortName;
	
	public String init()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		map.put("gwtype", showType);
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		platformTypeList=bio.queryplatform();
		userGroupIDList=bio.queryCustomerGroup();
		return "init";
	}
	/**
	 * 查询属地
	 * 
	 * @return
	 */
	public String getCityNextChild() {
		logger.debug("GwDeviceQueryACT=>getCity()");
		if (null == this.gwShare_cityId) {
			UserRes curUser = (UserRes) session.get("curUser");
			this.gwShare_cityId = curUser.getCityId();
		}
		this.ajax = bio.getCity(gwShare_cityId);
		return "ajax";
	}
	public String getCityNextChild1() {
		UserRes curUser = (UserRes) session.get("curUser");
		gwShare_cityId = curUser.getCityId();
		this.ajax = bio.getCity(gwShare_cityId);
		return "ajax";
	}
	
	public String test()
	{
		ajax=bio.test(gwShare_cityId);
		return "ajax";
	}
	public String test1()
	{
		ajax=bio.test(citynext);
		return "ajax";
	}
	public String getAllPcityIdByCityId()
	{
		this.ajax = bio.getAllPcityIdByCityId(PcityIdByCityId);
		return "ajax";
	}
	/**
	 * 查询下级属地
	 * 
	 * @return
	 */
	public String getCityNext() {
		logger.debug("GwDeviceQueryACT=>getCity()");
		this.ajax = bio.getCity(citynext);
		return "ajax";
	}
	public String getplatformType()
	{
		ajax=bio.getplatformType();
		return "ajax";
	}
	public String getuserGroupID()
	{
		ajax=bio.getuserGroupID();
		return "ajax";
	}
	/**
	 * 查询
	 * @return
	 */
	public String query()
	{
		this.setTime();
		String city_id="";
		if(!gwShare_cityId.equals("-1")&&!citynext.equals("-1"))
		{
			city_id=citynext;
		}else if(!gwShare_cityId.equals("-1"))
		{
			city_id=gwShare_cityId;
		}else
		{
			UserRes curUser = (UserRes) session.get("curUser");
			city_id=curUser.getCityId();
		}
		map.put("gwtype", showType);
		date = bio.queryList(city_id, starttime, endtime, servaccount, platformType, MAC,
				userGroupID, stbaccessStyle, iptvBindPhone,curPage_splitPage, num_splitPage,stbuser);
		maxPage_splitPage=bio.getMaxPage_splitPage();
		return "list";
	}

	public String time()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		platformTypeList=bio.queryplatform();
		userGroupIDList=bio.queryCustomerGroup();
		return "add";
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime))
		{
			starttime = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime))
		{
			endtime = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endtime = String.valueOf(dt.getLongTime());
		}
	}
	
	/**
	 * 添加
	 * @return
 	 */
	public String add()
	{
		//this.setTime();
		String city_id="";
		if(!gwShare_cityId.equals("-1")&&!citynext.equals("-1"))
		{
			city_id=citynext;
		}else if(!gwShare_cityId.equals("-1"))
		{
			city_id=gwShare_cityId;
		}
		
		Map<String,String> customerMap=null;
		if(Global.HNLT.equals(instArea)){
			customerMap=bio.getCustomerInfo(servaccount);
		}
		
		ajax = bio.adduserMessage(authUser, authPwd, endtime, platformType, userGroupID,
					iptvBindPhone, city_id, stbuser, stbpwd, MAC, stbaccessStyle, servaccount,
					servpwd, stbuptyle);
		
		if(Global.HNLT.equals(instArea)){
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			String user_ip=request.getRemoteHost();
			bio.addLogInsert(customerMap,user_id,user_ip,ajax, platformType, userGroupID,
					iptvBindPhone, city_id, stbuser, stbpwd, MAC, servaccount,servpwd);
		}
		
		return "ajax";
	}
	
	/**
 	* 修改
 	* @return
 	*/
	public String updateuserMessage()
	{
		String city_id="";
		if(!gwShare_cityId.equals("-1")&&!citynext.equals("-1"))
		{
			city_id=citynext;
		}else if(!gwShare_cityId.equals("-1"))
		{
			city_id=gwShare_cityId;
		}else if(gwShare_cityId.equals("-1")&&!citynext.equals("-1"))
		{
			city_id=citynext;
		}
		String stbuptyle1="";
		if(stbuptyle.equals("1")){
			stbuptyle1="FTTH";
		}else if(stbuptyle.equals("2"))
		{
			stbuptyle1="FTTB";
		}else if(stbuptyle.equals("3"))
		{
			stbuptyle1="LAN";
		}else if(stbuptyle.equals("4"))
		{
			stbuptyle1="HGW";
		}
		
		Map<String,String> customerMap=null;
		if(Global.HNLT.equals(instArea)){
			customerMap=bio.getCustomerInfo(servaccount);
		}
		
		ajax=bio.updateuserMessage(authUser, authPwd, platformType, userGroupID, iptvBindPhone, city_id, stbuser, stbpwd, MAC, stbaccessStyle, servaccount, servpwd, stbuptyle1);
		
		if(Global.HNLT.equals(instArea)){
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			String user_ip=request.getRemoteHost();
			bio.addLogUpdate(customerMap,user_id,user_ip,ajax,authUser, authPwd, platformType, userGroupID, iptvBindPhone, city_id, stbuser, stbpwd, MAC, stbaccessStyle, servaccount, servpwd, stbuptyle1);
		}
		return "ajax";
	}
	
	/**
	 * 查看详细信息
	 */
	public String queryLook()
	{
		date = bio.queryList1(gwShare_cityId, starttime, endtime, servaccount, platformType, MAC,
				userGroupID, stbaccessStyle,iptvBindPhone, curPage_splitPage, num_splitPage);
		maxPage_splitPage=bio.getMaxPage_splitPage();
		return "look";
	}
	
	/**
	 * 根据servaccount去查询所有信息
	 */
	public String queryservaccount()
	{
		date = bio.queryList1(gwShare_cityId, starttime, endtime, servaccount, platformType, MAC,
				userGroupID, stbaccessStyle,iptvBindPhone, curPage_splitPage, num_splitPage);
		maxPage_splitPage=bio.getMaxPage_splitPage();
		if(Global.HNLT.equals(instArea)){
			return "showhnlt";
		}
		return "show";
	}
	
	/**
	 * 删除
	 * @return
	 */
	public String deleteuserMessage()
	{
		Map<String,String> customerMap=null;
		if(Global.HNLT.equals(instArea)){
			customerMap=bio.getCustomerInfo(servaccount);
		}
		
		ajax=bio.deleteuserMessage(gwShare_cityId, starttime, endtime, servaccount, platformType, MAC, userGroupID, stbaccessStyle, iptvBindPhone,curPage_splitPage, num_splitPage);
		if(Global.HNLT.equals(instArea)){
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			String user_ip=request.getRemoteHost();
			bio.addLogDelete(customerMap,user_id,user_ip,ajax,servaccount);
		}
		return "ajax";
	}
	
	public String cleanAccountPwd(){
		Map<String,String> customerMap=null;
		if(Global.HNLT.equals(instArea)){
			customerMap=bio.getCustomerInfo(servaccount);
		}
		ajax=bio.cleanAccountPwd(servaccount);
		if(Global.HNLT.equals(instArea)){
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			String user_ip=request.getRemoteHost();
			bio.addLogcleanAccountPwd(customerMap,user_id,user_ip,ajax,servaccount);
		}
		return "ajax";
	}
	
	/**
	 * 时间转化
	 */
	private void setTime1()
	{
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (dealDate == null || "".equals(dealDate))
		{
			dealDate = null;
		}
		else
		{
			dt = new DateTimeUtil(dealDate);
			dealDate = String.valueOf(dt.getLongTime());
		}
	}
	public userMessageManageBIO getBio()
	{
		return bio;
	}

	public void setBio(userMessageManageBIO bio)
	{
		this.bio = bio;
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}
	public String getDealDate()
	{
		return dealDate;
	}
	
	public void setDealDate(String dealDate)
	{
		this.dealDate = dealDate;
	}
	public String getStarttime()
	{
		return starttime;
	}

	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public String getServaccount()
	{
		return servaccount;
	}

	public void setServaccount(String servaccount)
	{
		this.servaccount = servaccount;
	}

	public String getPlatformType()
	{
		return platformType;
	}

	public void setPlatformType(String platformType)
	{
		this.platformType = platformType;
	}

	public String getMAC()
	{
		return MAC;
	}

	public void setMAC(String mAC)
	{
		MAC = mAC;
	}

	public String getUserGroupID()
	{
		return userGroupID;
	}

	public void setUserGroupID(String userGroupID)
	{
		this.userGroupID = userGroupID;
	}

	public String getStbaccessStyle()
	{
		return stbaccessStyle;
	}

	public void setStbaccessStyle(String stbaccessStyle)
	{
		this.stbaccessStyle = stbaccessStyle;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getAuthUser()
	{
		return authUser;
	}

	public void setAuthUser(String authUser)
	{
		this.authUser = authUser;
	}

	public String getAuthPwd()
	{
		return authPwd;
	}

	public void setAuthPwd(String authPwd)
	{
		this.authPwd = authPwd;
	}

	public String getServTypeId()
	{
		return servTypeId;
	}

	public void setServTypeId(String servTypeId)
	{
		this.servTypeId = servTypeId;
	}

	public String getOperateId()
	{
		return operateId;
	}

	public void setOperateId(String operateId)
	{
		this.operateId = operateId;
	}

	public String getIptvBindPhone()
	{
		return iptvBindPhone;
	}

	public void setIptvBindPhone(String iptvBindPhone)
	{
		this.iptvBindPhone = iptvBindPhone;
	}

	public String getStbuser()
	{
		return stbuser;
	}

	public void setStbuser(String stbuser)
	{
		this.stbuser = stbuser;
	}

	public String getStbpwd()
	{
		return stbpwd;
	}

	public void setStbpwd(String stbpwd)
	{
		this.stbpwd = stbpwd;
	}

	public String getServpwd()
	{
		return servpwd;
	}

	public void setServpwd(String servpwd)
	{
		this.servpwd = servpwd;
	}

	public String getStbuptyle()
	{
		return stbuptyle;
	}

	public void setStbuptyle(String stbuptyle)
	{
		this.stbuptyle = stbuptyle;
	}

	public List<Map> getDate()
	{
		return date;
	}

	public void setDate(List<Map> date)
	{
		this.date = date;
	}
	
	public List<Map> getPlatformTypeList()
	{
		return platformTypeList;
	}
	
	public void setPlatformTypeList(List<Map> platformTypeList)
	{
		this.platformTypeList = platformTypeList;
	}
	
	public List<Map> getUserGroupIDList()
	{
		return userGroupIDList;
	}
	
	public void setUserGroupIDList(List<Map> userGroupIDList)
	{
		this.userGroupIDList = userGroupIDList;
	}
	
	public String getGwShare_cityId()
	{
		return gwShare_cityId;
	}
	
	public void setGwShare_cityId(String gwShare_cityId)
	{
		this.gwShare_cityId = gwShare_cityId;
	}
	
	public String getCitynext()
	{
		return citynext;
	}
	
	public void setCitynext(String citynext)
	{
		this.citynext = citynext;
	}
	
	public String getPcityIdByCityId()
	{
		return PcityIdByCityId;
	}
	
	public void setPcityIdByCityId(String pcityIdByCityId)
	{
		PcityIdByCityId = pcityIdByCityId;
	}
	
	public String getShowType()
	{
		return showType;
	}
	
	public void setShowType(String showType)
	{
		this.showType = showType;
	}
	public Map<String, String> getMap()
	{
		return map;
	}
	
	public void setMap(Map<String, String> map)
	{
		this.map = map;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	
}
