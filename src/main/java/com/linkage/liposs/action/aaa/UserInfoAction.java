package com.linkage.liposs.action.aaa;

import java.util.List;
import java.util.Map;

import com.linkage.liposs.dao.aaa.UserInfoDAO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 查询、显示AAA帐号信息功能的action
 * @author 陈仲民  2007-12-03
 * @version 1.0
 */

public class UserInfoAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -299455794392864552L;

	//生效时间
	private String eff_date = "";
	
	//失效时间
	private String exp_date = "";
	
	//3A帐号
	private String user_name = "";
	
	//创建者
	private String creator = "";
	
	//DAO类
	private UserInfoDAO userInstance = null;
	
	//用户信息数据
	private List<Map> userData = null;

	//需要删除的用户名
	private String delUser = "";
	
	//帐号基本信息
	private List<Map> basicUserInfo = null;
	
	//帐号权限信息
	private List<Map> privilegInfo = null;
	
	//帐号关联设备信息
	private List<Map> deviceInfo = null;

	public void setDelUser(String delUser) {
		this.delUser = delUser;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public void setEff_date(String eff_date) {
		this.eff_date = eff_date;
	}

	public void setExp_date(String exp_date) {
		this.exp_date = exp_date;
	}
	
	public List<Map> getUserData() {
		return userData;
	}

	public void setUserInstance(UserInfoDAO userInstance) {
		this.userInstance = userInstance;
	}

	public String execute() throws Exception {
		
		return super.execute();
	}
	
	/**
	 * 查询用户信息
	 * @return
	 * @throws Exception
	 */
	public String getUserInfo() throws Exception{
		
		userData = userInstance.getUserInfo(eff_date, exp_date, user_name, creator);
		
		return "userList";
	}
	
	/**
	 * 删除AAA认证帐号
	 * @return
	 * @throws Exception
	 */
	public String delUserInfo() throws Exception{
		
		userInstance.delUserInfo(delUser);
		userData = userInstance.getUserInfo(eff_date, exp_date, user_name, creator);
		
		return "userList";
	}
	
	/**
	 * 显示帐号的详细信息
	 * @return
	 * @throws Exception
	 */
	public String showDetail() throws Exception{
		
		basicUserInfo = userInstance.getUserInfo("", "", user_name, "");
		privilegInfo = userInstance.getPrivileg(user_name);
		deviceInfo = userInstance.getDevice(user_name);
		
		return "detailInfo";
	}
	
	/**
	 * 显示帐号的权限信息
	 * @return
	 * @throws Exception
	 */
	public String showPrivilege() throws Exception{
		
		basicUserInfo = userInstance.getUserInfo("", "", user_name, "");
		privilegInfo = userInstance.getPrivileg(user_name);
		
		return "showPrivilege";
	}

	public List<Map> getBasicUserInfo() {
		return basicUserInfo;
	}

	public List<Map> getDeviceInfo() {
		return deviceInfo;
	}

	public List<Map> getPrivilegInfo() {
		return privilegInfo;
	}
}
