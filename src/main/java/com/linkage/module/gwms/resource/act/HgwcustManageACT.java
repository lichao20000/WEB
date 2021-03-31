
package com.linkage.module.gwms.resource.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.dao.tabquery.HgwCustDAO;
import com.linkage.module.gwms.obj.tabquery.HgwCustObj;
import com.linkage.module.gwms.resource.bio.HgwcustManageBIO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.service.WanTypeUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Jason(3412)
 * @date 2009-9-27
 */
public class HgwcustManageACT extends ActionSupport implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(HgwcustManageACT.class);
	// 用户ID
	private String userId;
	// 用户账号
	private String username;
	// 用户密码
	private String passwd;
	// 接入方式
	private String accessType;
	// PVC
	private String vpi;
	private String vci;
	// Vlan
	private String vlanid;
	// 上网方式
	private String netType;
	// 静态IP时的参数
	private String ip;
	private String mask;
	private String gateway;
	private String dns;
	// 属地，局向，小区，绑定电话
	private String cityId;
	private String officeId;
	private String zoneId;
	private String bindphone;
	// 最大上下行速率
	private String maxUpSpeed;
	private String maxDownSpeed;
	// 用户实名，最大上网数
	private String realname;
	private String maxUserNum;
	// 联系人姓名，电话，手机，email
	private String linkman;
	private String linkphone;
	private String mobile;
	private String email;
	private String linkaddress;
	// 证件类型，证件号码
	private String credType;
	private String credno;
	// 受理时间
	private String dealdate;
	// 属地List
	private List cityList;
	// 局向List
	private List<Map<String, String>> officeList;
	// 小区List
	private List zoneList;
	// 页面返回的MAP信息
	private Map hgwCustMap;
	// Session
	private Map session;
	// ajax
	private String ajax;
	private HgwcustManageBIO hgwcustManageBio;
	// '1'表示添加，'2'表示编辑
	private String isAdd;
	// 保存结果, 1表示成功，-1表示失败
	private String saveResult;
	// 局向名称
	private String officeName;
	// 网关类型名字集合
	private List typeNameList;
	// 网关类型名字
	private String typeName;
	private String gw_type;

	/**
	 * 家庭网关用户添加初始化页面
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-27
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		String userCityId = curUser.getCityId();
		cityList = CityDAO.getAllNextCityListByCityPid(userCityId);
		typeNameList = hgwcustManageBio.getTypeNameList();
		Map<String, String> tmap = new HashMap<String, String>();
		if (false == StringUtil.IsEmpty(userId))
		{
			// 表示有用户ID传过来，初始化编辑页面p
			logger.debug("初始化编辑页面");
			HgwCustObj hgwCustObj = hgwcustManageBio.getHgwcust(userId,gw_type);
			if(null==hgwCustObj){
				hgwCustObj = new HgwCustObj();
			}
			typeName=StringUtil.getStringValue(hgwCustObj.getTypeName());
			username = StringUtil.getStringValue(hgwCustObj.getUsername());
			passwd = StringUtil.getStringValue(hgwCustObj.getPasswd());
			netType = StringUtil.getStringValue(WanTypeUtil.getNetType(hgwCustObj
					.getWanType()));
			accessType = StringUtil.getStringValue(WanTypeUtil.getAccessType(hgwCustObj
					.getWanType()));
			vpi = StringUtil.getStringValue(hgwCustObj.getVpiid());
			vci = StringUtil.getStringValue(hgwCustObj.getVciid());
			vlanid = StringUtil.getStringValue(hgwCustObj.getVlanid());
			ip = StringUtil.getStringValue(hgwCustObj.getIp());
			mask = StringUtil.getStringValue(hgwCustObj.getIp());
			gateway = StringUtil.getStringValue(hgwCustObj.getGateway());
			dns = StringUtil.getStringValue(hgwCustObj.getDns());
			realname = StringUtil.getStringValue(hgwCustObj.getRealname());
			bindphone = StringUtil.getStringValue(hgwCustObj.getTelepone());
			linkman = StringUtil.getStringValue(hgwCustObj.getLinkman());
			linkphone = StringUtil.getStringValue(hgwCustObj.getLinkphone());
			email = StringUtil.getStringValue(hgwCustObj.getEmail());
			linkaddress = StringUtil.getStringValue(hgwCustObj.getAddress());
			mobile = StringUtil.getStringValue(hgwCustObj.getMobile());
			cityId = StringUtil.getStringValue(hgwCustObj.getCityId());
			officeId = StringUtil.getStringValue(hgwCustObj.getOfficeId());
			zoneId = StringUtil.getStringValue(hgwCustObj.getZoneId());
			credno = StringUtil.getStringValue(hgwCustObj.getCredno());
			maxUpSpeed = StringUtil.getStringValue(hgwCustObj.getUpSpeed());
			maxDownSpeed = StringUtil.getStringValue(hgwCustObj.getDownSpeed());
			maxUserNum = StringUtil.getStringValue(hgwCustObj.getMaxUserNum());
			dealdate = new DateTimeUtil(
					StringUtil.getLongValue(hgwCustObj.getDealdate()) * 1000)
					.getLongDate();
			// officeList = hgwcustManageBio.getOfficeById(officeId);
			isAdd = "2";
		}
		else
		{
			logger.debug("初始化添加页面");
			isAdd = "1";
			
		}
		// if (officeList == null || officeList.size() < 1) {
		// tmap.put("office_id", "xuanze");
		// tmap.put("office_name", "==请根据局向查询来匹配==");
		// officeList = new ArrayList<Map<String, String>>();
		// officeList.add(tmap);
		// }
		return "init";
	}

	/**
	 * 保存用户操作
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-28
	 * @return String
	 */
	public String saveHgwcust()
	{
		logger.debug("saveHgwcust()");
		HgwCustObj hgwCustObj = new HgwCustObj();
		long longUserId = -1L;
		hgwCustObj.setUsername(username);
		hgwCustObj.setWanType(WanTypeUtil.getWanType(StringUtil
				.getIntegerValue(accessType), StringUtil.getIntegerValue(netType)));
		hgwCustObj.setCityId(cityId);
		hgwCustObj.setAddress(linkaddress);
		hgwCustObj.setCredno(credno);
		hgwCustObj.setDealdate(StringUtil.getLongValue(dealdate));
		hgwCustObj.setDownSpeed(StringUtil.getLongValue(maxDownSpeed));
		hgwCustObj.setUpSpeed(StringUtil.getLongValue(maxUpSpeed));
		hgwCustObj.setEmail(email);
		hgwCustObj.setLinkman(linkman);
		hgwCustObj.setLinkphone(linkphone);
		hgwCustObj.setMaxUserNum(StringUtil.getIntegerValue(maxUserNum));
		hgwCustObj.setMobile(mobile);
		hgwCustObj.setOfficeId(officeId);
		hgwCustObj.setPasswd(passwd);
		hgwCustObj.setRealname(realname);
		hgwCustObj.setTelepone(bindphone);
		hgwCustObj.setVciid(vci);
		hgwCustObj.setVpiid(vpi);
		hgwCustObj.setVlanid(vlanid);
		hgwCustObj.setZoneId(zoneId);
		hgwCustObj.setIp(ip);
		hgwCustObj.setMask(mask);
		hgwCustObj.setGateway(gateway);
		hgwCustObj.setDns(dns);
		hgwCustObj.setTypeName(typeName);
		int iret = -1;
		if (StringUtil.IsEmpty(userId))
		{
			// 表示为添加提交
			longUserId = HgwCustDAO.generateUserId();
			hgwCustObj.setUserId(StringUtil.getStringValue(longUserId));
			iret = 1;
			isAdd = "1";
		}
		else
		{
			// 编辑提交
			hgwCustObj.setUserId(userId);
			iret = 2;
			isAdd = "2";
		}
		iret = hgwcustManageBio.manageHgwcust(hgwCustObj, iret);
		if (iret > 0)
		{
			logger.debug("manager hgwcustomer success");
		}
		else
		{
			logger.debug("manager hgwcustomer failture");
		}
		if (iret > 0)
		{
			iret = 1;
		}
		else
		{
			iret = -1;
		}
		saveResult = String.valueOf(iret);
		return "saveOk";
	}

	/**
	 * 检查家庭网关用户存在性检查
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-28
	 * @return String
	 */
	public String checkUser()
	{
		logger.debug("checkUser()");
		int iuser = hgwcustManageBio.checkUser(username);
		ajax = StringUtil.getStringValue(iuser);
		return "ajax";
	}

	/**
	 * 根据属地获取局向的下拉列表
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-28
	 * @return String
	 */
	public String getOfficeListByName()
	{
		logger.debug("getOfficeListByName()");
		officeList = hgwcustManageBio.getOfficeListByName(cityId, officeName);
		ajax = hgwcustManageBio.getHtmlSelect(officeList, "officeId", "office_id",
				"office_name", null);
		// ajax =
		// "<s:select list='officeList' listKey='office_id' listValue='office_name' name='offiveId' cssClass='bk'></s:select>&nbsp;<font color='#FF0000'>*</font>";
		return "ajax";
	}

	/** getter,setter methods */
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		try
		{
			this.username = java.net.URLDecoder.decode(username, "UTF-8");
		}
		catch (Exception e)
		{
			this.username = username;
		}
	}

	public String getPasswd()
	{
		return passwd;
	}

	public void setPasswd(String passwd)
	{
		this.passwd = passwd;
	}

	public String getAccessType()
	{
		return accessType;
	}

	public void setAccessType(String accessType)
	{
		this.accessType = accessType;
	}

	public String getVpi()
	{
		return vpi;
	}

	public void setVpi(String vpi)
	{
		this.vpi = vpi;
	}

	public String getVci()
	{
		return vci;
	}

	public void setVci(String vci)
	{
		this.vci = vci;
	}

	public String getVlanid()
	{
		return vlanid;
	}

	public void setVlanid(String vlanid)
	{
		this.vlanid = vlanid;
	}

	public String getNetType()
	{
		return netType;
	}

	public void setNetType(String netType)
	{
		this.netType = netType;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getMask()
	{
		return mask;
	}

	public void setMask(String mask)
	{
		this.mask = mask;
	}

	public String getGateway()
	{
		return gateway;
	}

	public void setGateway(String gateway)
	{
		this.gateway = gateway;
	}

	public String getDns()
	{
		return dns;
	}

	public void setDns(String dns)
	{
		this.dns = dns;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getOfficeId()
	{
		return officeId;
	}

	public void setOfficeId(String officeId)
	{
		this.officeId = officeId;
	}

	public String getZoneId()
	{
		return zoneId;
	}

	public void setZoneId(String zoneId)
	{
		this.zoneId = zoneId;
	}

	public String getRealname()
	{
		return realname;
	}

	public void setRealname(String realname)
	{
		this.realname = realname;
	}

	public String getMaxUserNum()
	{
		return maxUserNum;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public void setBindphone(String bindphone)
	{
		this.bindphone = bindphone;
	}

	public void setMaxUpSpeed(String maxUpSpeed)
	{
		this.maxUpSpeed = maxUpSpeed;
	}

	public void setMaxDownSpeed(String maxDownSpeed)
	{
		this.maxDownSpeed = maxDownSpeed;
	}

	public void setLinkman(String linkman)
	{
		this.linkman = linkman;
	}

	public void setLinkphone(String linkphone)
	{
		this.linkphone = linkphone;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public void setLinkaddress(String linkaddress)
	{
		this.linkaddress = linkaddress;
	}

	public void setCredType(String credType)
	{
		this.credType = credType;
	}

	public void setCredno(String credno)
	{
		this.credno = credno;
	}

	public void setMaxUserNum(String maxUserNum)
	{
		this.maxUserNum = maxUserNum;
	}

	public List getCityList()
	{
		return cityList;
	}

	public String getAjax()
	{
		return ajax;
	}

	public String getDealdate()
	{
		return dealdate;
	}

	public void setDealdate(String dealdate)
	{
		this.dealdate = dealdate;
	}

	public Map getHgwCustMap()
	{
		return hgwCustMap;
	}

	public void setHgwcustManageBio(HgwcustManageBIO hgwcustManageBio)
	{
		this.hgwcustManageBio = hgwcustManageBio;
	}

	@Override
	public void setSession(Map arg0)
	{
		session = arg0;
	}

	public String getIsAdd()
	{
		return isAdd;
	}

	public void setIsAdd(String isAdd)
	{
		this.isAdd = isAdd;
	}

	public String getSaveResult()
	{
		return saveResult;
	}

	public String getUserId()
	{
		return userId;
	}

	public String getBindphone()
	{
		return bindphone;
	}

	public String getMaxUpSpeed()
	{
		return maxUpSpeed;
	}

	public String getMaxDownSpeed()
	{
		return maxDownSpeed;
	}

	public String getLinkman()
	{
		return linkman;
	}

	public String getLinkphone()
	{
		return linkphone;
	}

	public String getMobile()
	{
		return mobile;
	}

	public String getEmail()
	{
		return email;
	}

	public String getLinkaddress()
	{
		return linkaddress;
	}

	public String getCredType()
	{
		return credType;
	}

	public String getCredno()
	{
		return credno;
	}

	public List getZoneList()
	{
		return zoneList;
	}

	/**
	 * @return the officeList
	 */
	public List<Map<String, String>> getOfficeList()
	{
		return officeList;
	}

	/**
	 * @param officeList
	 *            the officeList to set
	 */
	public void setOfficeList(List<Map<String, String>> officeList)
	{
		this.officeList = officeList;
	}

	/**
	 * @return the officeName
	 */
	public String getOfficeName()
	{
		return officeName;
	}

	/**
	 * @param officeName
	 *            the officeName to set
	 */
	public void setOfficeName(String officeName)
	{
		try
		{
			this.officeName = java.net.URLDecoder.decode(officeName, "UTF-8");
		}
		catch (Exception e)
		{
			this.officeName = officeName;
		}
	}
	
	public String getGw_type()
	{
		return gw_type;
	}

	
	public void setGw_type(String gwType)
	{
		gw_type = gwType;
	}

	public String getTypeName()
	{
		return typeName;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public List getTypeNameList()
	{
		return typeNameList;
	}

	public void setTypeNameList(List typeNameList)
	{
		this.typeNameList = typeNameList;
	}
}
