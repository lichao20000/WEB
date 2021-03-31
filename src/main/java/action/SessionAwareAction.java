package action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linkage.litms.system.User;
import com.linkage.litms.system.dbimpl.DbUserRes;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 从session中获取登陆用户信息的类，使用的action需要继承该类
 * 
 * @author 陈仲民（5243）
 * @since 2008-01-16
 * @version v1.0
 * @category
 * 
 */

public class SessionAwareAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8120666015792956478L;

	private DbUserRes user = null;

	/**
	 * 初始化
	 */
	private void init() {
		Map a = ActionContext.getContext().getSession();
		user = (DbUserRes) a.get("curUser");
	}

	/**
	 * 获取当前登陆用户的user对象
	 * 
	 * @return User对象
	 */
	public User getUser() {
		if (user == null) {
			init();
		}

		return user.getUser();
	}

	/**
	 * 获取用户可以访问的设备资源范围
	 * 
	 * @return 设备资源列表 （tab_res_area中的res_id）
	 */
	public List getUserDevRes() {
		if (user == null) {
			init();
		}

		return user.getUserDevRes();
	}

	/**
	 * 根据用户对象获取可以访问的设备资源范围
	 * 
	 * @param curUser
	 *            用户对象
	 * @return 设备资源列表 （tab_res_area中的res_id）
	 */
	public List getUserDevRes(User curUser) {
		if (user == null) {
			init();
		}

		return user.getUserDevRes(curUser);
	}

	/**
	 * 获取当前用户功能模块访问权限（根据角色判断）
	 * 
	 * @return 功能模块访问权限列表（tp_menu中的menu_key）
	 */
	public List getUserPermissions() {
		if (user == null) {
			init();
		}

		return user.getUserPermissions();
	}

	/**
	 * 根据当前用户对象获取可以访问的VPN设备资源范围
	 * 
	 * @return VPN设备资源列表 （tab_res_area中的res_id）
	 */
	public List getUserVpnRes() {
		if (user == null) {
			init();
		}

		return user.getUserVpnRes();
	}

	/**
	 * 根据当前用户对象获取可以访问的主机设备资源范围
	 * 
	 * @return 主机设备资源列表 （tab_res_area中的res_id）
	 */
	public List getHostRes() {
		if (user == null) {
			init();
		}

		return user.getHostRes();
	}

	/**
	 * 根据当前用户对象获取可以访问的采集机资源范围
	 * 
	 * @return 采集机资源列表 （tab_res_area中的res_id）
	 */
	public List getGatherList() {

		if (user == null) {
			init();
		}

		return user.getUserProcesses();

	}

	/**
	 * 获取当前登陆用户的属地
	 * 
	 * @return 登陆用户的属地id
	 */
	public String getCityId() {
		if (user == null) {
			init();
		}

		return user.getCityId();
	}

	/**
	 * 获取当前登陆用户的域id
	 * 
	 * @return 登陆用户的域id
	 */
	public long getAreaId() {
		if (user == null) {
			init();
		}

		return user.getAreaId();
	}

	/**
	 * 获取登陆用户的帐号
	 * 
	 * @return 用户的登陆帐号
	 */
	public String getAccounts() {

		if (user == null) {
			init();
		}

		return user.getUser().getAccount();

	}

	/**
	 * 获取登陆用户的密码
	 * 
	 * @return 用户的登陆密码
	 */
	public String getPasswd() {
		if (user == null) {
			init();
		}

		return user.getUser().getPasswd();
	}

	/**
	 * 获取用户最后一次登陆时间
	 * 
	 * @return 最后登陆时间
	 */
	public Date getLastLoginDate() {
		if (user == null) {
			init();
		}

		return user.getUser().getLastLoginDate();
	}

	/**
	 * 获得当前用户的角色数组，一个用户可以对应多个角色。
	 * 
	 * @return 用户对应的角色数组
	 */
	public long[] getRole_Id() {
		if (user == null) {
			init();
		}

		return user.getUser().getRole_Id();
	}

	/**
	 * 获取登陆用户的资料信息
	 * 
	 * @return 返回用户资料信息，存放在Map中
	 */
	public Map getUserInfo() {
		if (user == null) {
			init();
		}

		return user.getUser().getUserInfo();
	}
}
