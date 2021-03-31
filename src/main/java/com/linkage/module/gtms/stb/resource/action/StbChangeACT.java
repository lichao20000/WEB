package com.linkage.module.gtms.stb.resource.action;

import action.splitpage.splitPageAction;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.StbChangeBIO;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 机顶盒更换
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-2-1
 */
public class StbChangeACT extends splitPageAction implements SessionAware,ServletRequestAware
{

	/** 序列化 */
	private static final long serialVersionUID = 1L;
	/** 属地ID */
	private String cityId = "";
	/** 厂商ID */
	/** 设备mac地址 */
	private String deviceMac = "";
	/** oldMac */
	private String oldMac = "";
	/** oui */
	private String servAccount = "";
	private String ajax = "";
	/** 查询总数 */
	private int queryCount;
	private StbChangeBIO bio;
	/** 用户列表 */
	private List<Map<String, String>> userList  = new ArrayList<Map<String, String>>();
	@SuppressWarnings("rawtypes")
	private Map session;

	private HttpServletRequest request;
	
	
	/**
	 * 更换机顶盒
	 * @param
	 * @return
	 */
	public String getMacList() {
		UserRes curUser = (UserRes) session.get("curUser");
		this.cityId = curUser.getCityId();
		userList = bio.getMac(servAccount, deviceMac,cityId);
//		maxPage_splitPage = bio.countMac(curPage_splitPage, num_splitPage,servAccount, deviceMac);
//		queryCount = bio.getQueryCount();
		return "queryList";
	}
	
	/**
	 * 更换机顶盒
	 * @param
	 * @return
	 */
	public String change() {
		UserRes curUser = (UserRes) session.get("curUser");
		String grid =curUser.getCityId();
		String loginAccountName = curUser.getUser().getAccount();
		Map<String,String> map=bio.queryCustomer(cityId,servAccount,deviceMac, oldMac);
		if(null == map || map.size() == 0 )
		{
			ajax = "请检查业务账号和mac地址是否存在或绑定";
			return "ajax";
		}
		ajax = bio.changeStbMac(cityId, servAccount, deviceMac, oldMac,grid,loginAccountName);
		
		long  user_id=((UserRes) session.get("curUser")).getUser().getId();
		String user_ip=request.getRemoteHost();
		bio.addLogUnBind(user_id,user_ip,cityId, servAccount,deviceMac,ajax,map);
		return "ajax";
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getDeviceMac() {
		return deviceMac;
	}

	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}

	public String getOldMac() {
		return oldMac;
	}

	public void setOldMac(String oldMac) {
		this.oldMac = oldMac;
	}

	public String getServAccount() {
		return servAccount;
	}

	public void setServAccount(String servAccount) {
		this.servAccount = servAccount;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}


	public StbChangeBIO getBio() {
		return bio;
	}

	public void setBio(StbChangeBIO bio) {
		this.bio = bio;
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

	public List<Map<String, String>> getUserList() {
		return userList;
	}

	public void setUserList(List<Map<String, String>> userList) {
		this.userList = userList;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
}
