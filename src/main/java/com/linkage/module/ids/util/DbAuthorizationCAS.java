package com.linkage.module.ids.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.SkinUtils;
import com.linkage.litms.system.Area;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserManager;
import com.linkage.litms.system.UserNotFoundException;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.system.dbimpl.AreaSyb;
import com.linkage.litms.system.dbimpl.DbAuthorization;
import com.linkage.litms.system.dbimpl.DbUserManager;
import com.linkage.litms.system.dbimpl.DbUserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;

public class DbAuthorizationCAS  {

	public DbAuthorizationCAS(HttpServletRequest _req, HttpServletResponse _rep) {
		req = _req;
		rep =_rep;
	}

	/** logger */
	private static Logger loger = LoggerFactory
			.getLogger(DbAuthorization.class);

	private HttpServletRequest req = null;

	private HttpServletResponse rep = null;


	private String CHECK_USER = "select acc_oid from tab_accounts where acc_loginname=? "
			+ "and acc_oid in(select acc_oid from tab_acc_area where area_id=?)";

	// private String CHECK_PASSWORD = "select acc_pwd_time form tab_accounts
	// where acc_loginname=? ";

	private String account = null;

	private long area_id;
	
	private long acc_oid;

	private PrepareSQL pSQL = null;

	/**
	 * 通过账号、密码、域信息到数据验证用户是否合法
	 * 
	 * @param _account
	 *            账号字符串
	 * @param _password
	 *            密码字符串
	 * @param area_id
	 *            域ID
	 * @return 用户合法返回true，反之false
	 */
	private boolean checkUserToDb(String _account,long area_id) {
		loger.debug("checkUserToDb({},{},{})", new Object[] { _account,
				area_id});
		pSQL = new PrepareSQL();
		boolean result = false;
		pSQL.setSQL(CHECK_USER);
		pSQL.setString(1, _account);
		pSQL.setLong(2, area_id);

		Map fields = DataSetBean.getRecord(pSQL.getSQL());
		if (fields != null) {
			acc_oid = StringUtil.getLongValue(fields.get("acc_oid"));
		}
		return result;
	}

	/**
	 * 认证成功后，加载用户的相关资源到Session中，其session名字为：curUser
	 * 
	 */
	public void initUserRes() {
		loger.debug("initUserRes()");
		UserManager userMgr = new DbUserManager();
		String acc_loginname = (String)req.getAttribute("acc_loginname");
		String s_area_name   = (String)req.getAttribute("area_name");

		if (s_area_name != null){
			SkinUtils.setCookie(rep, "areaName", s_area_name);
			Area area = new AreaSyb(s_area_name);
			int i_area_id = area.getAreaId();
			area_id = (long) i_area_id;
		}
		checkUserToDb(acc_loginname,area_id);
		User user = null;
		try {
			if(acc_oid != 0){
				user = userMgr.getUser(acc_oid);
				// 江苏电信智能网管与itms单点登录用,当tab_accounts表中没有   去tab_excel_syn_accounts寻找数据
			} else if (Global.JSDX.equals(Global.instAreaShortName))
			{
				user = userMgr.getUser(acc_loginname, area_id, true);
			}
			
			UserRes userRes = new DbUserRes(user);
			SkinUtils.setSession(req, "curUser", userRes);
			List list = userRes.getUserPermissions();
			String s_per_liposs = null;
			for (int i = 0; i < list.size(); i++) {
				if (s_per_liposs == null)
					s_per_liposs = (String) list.get(i);
				else
					s_per_liposs += "#" + (String) list.get(i);
			}
			if (s_per_liposs == null)
				s_per_liposs = "";
			SkinUtils.setSession(req, "ldims", s_per_liposs);
		} catch (UserNotFoundException e) {
			loger.error("can't found this user: {}", account);
		}
	}

	public void logout() {
		loger.debug("logout()");

		HttpSession session = req.getSession();
		session.removeAttribute("curUser");
	}
}
