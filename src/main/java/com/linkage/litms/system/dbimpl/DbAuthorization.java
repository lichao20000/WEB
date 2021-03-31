/*
 * @(#)DbAuthorization.java	1.00 1/21/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.system.dbimpl;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.LoginUtil;
import com.linkage.litms.common.util.MD5;
import com.linkage.litms.common.util.SkinUtils;
import com.linkage.litms.system.*;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 数据库实现系统注册用户登录认证授权接口
 * 
 * @author yuht
 * @version 1.00, 1/21/2006
 * @since Liposs 2.1
 */
public class DbAuthorization implements Authorization {

	/**
	 * 根据系统参数配置，判断当前系统是否为报表系统
	 */
	private static boolean isReportSystem = "3".equals(LipossGlobals
			.getLipossProperty("ClusterMode.mode"));
	/** logger */
	private static Logger loger = LoggerFactory
			.getLogger(DbAuthorization.class);

	private HttpServletRequest req = null;

	private HttpServletResponse rep = null;

	// 验证类型缺省1-网管系统认证；2-RADIUS
	private static int CHECK_TYPE = 1;

	private String CHECK_USER = "select * from tab_accounts where acc_loginname=? "
			+ "and acc_oid in(select acc_oid from tab_acc_area where area_id=?)";
	
	//查询用户是否登陆过系统 
	private String CHECK_USER_LOGIN = "select count(1) as num from tab_oper_log where acc_oid = ? ";
	
	// 用于验证：密码中必须含大小写字母、数字、特殊字符四类字符中的三类。
	private static String validpattrns[] = new String[] { ".*[\\d].*",
			".*[a-z].*", ".*[A-Z].*",
			".*[`~!@#$^&*()_=%|{}':;',\\[\\].<>/?\\\\].*" };

	// 用于验证：密码中必须含大小写字母、数字、特殊字符四类字符中的三类。
	private static String validkeyboard[] = new String[] { "1qaz", "2wsx",
			"3edc", "4rfv", "5tgb", "6yhn", "7ujm", "8ik,", "9ol.", "0p;/" };

	// private String CHECK_PASSWORD = "select acc_pwd_time form tab_accounts
	// where acc_loginname=? ";

	private String account = null;

	private String password = null;

	private String createPasswordTime = null;

	private String clientIPs = null;

	private String lockedtime = null;

	private String loginmaxtrynum = null;

	private String free_time = null;

	private String acc_validatemonth = null;

	private long area_id;

	private long acc_oid;

	private PrepareSQL pSQL = null;

	/**
	 * 带参数构造函数
	 * 
	 * @param _req
	 *            HttpServletRequest对象
	 * @param _rep
	 *            HttpServletResponse对象
	 */
	public DbAuthorization(HttpServletRequest _req, HttpServletResponse _rep) {

		this.req = _req;
		this.rep = _rep;
		if (pSQL == null)
			pSQL = new PrepareSQL();
		String s_check_type = LipossGlobals.getLipossProperty("auth.checktype");
		if (s_check_type != null)
			CHECK_TYPE = Integer.parseInt(s_check_type);
	}

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
	private boolean checkUserToDb(String _account, String _password,
			long area_id) {
		loger.debug("checkUserToDb({},{},{})", new Object[] { _account,
				_password, area_id });

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			CHECK_USER = "select acc_password, acc_pwd_time, acc_login_ip, lockedtime, loginmaxtrynum, " +
					"maxidletimebeforelogout, acc_validatemonth, acc_oid " +
					"from tab_accounts where acc_loginname=? "
					+ "and acc_oid in (select acc_oid from tab_acc_area where area_id=?)";
		}

		boolean result = false;
		pSQL.setSQL(CHECK_USER);
		pSQL.setString(1, _account);
		pSQL.setLong(2, area_id);

		Map fields = DataSetBean.getRecord(pSQL.getSQL());
		if (fields != null) {
			String tmp = "";
			// NXDX-REQ-ITMS-20190617-LX-001(对数据库里的用户密码进行加密保存) 数据库里是密文，不用再加密
			if (LipossGlobals.inArea(Global.NXDX) || LipossGlobals.inArea(Global.AHDX) ||LipossGlobals.inArea(Global.HNLT) ) {
				tmp = (String) fields.get("acc_password");
			}
			else {
				// 页面传送，先base64，然后MD5，这里保持一致
				tmp = MD5.getMD5(encodeBase64((String) fields.get("acc_password")));
			}
			createPasswordTime = (String) fields.get("acc_pwd_time");
			clientIPs = (String) fields.get("acc_login_ip");
			if ("".equals(clientIPs)) {
				clientIPs = null;
			}
			lockedtime = (String) fields.get("lockedtime");
			if ("".equals(lockedtime)) {
				lockedtime = null;
			}
			loginmaxtrynum = (String) fields.get("loginmaxtrynum");
			if ("".equals(loginmaxtrynum)) {
				loginmaxtrynum = null;
			}
			free_time = (String) fields.get("maxidletimebeforelogout");
			if ("".equals(free_time)) {
				free_time = null;
			}
			acc_validatemonth = (String) fields.get("acc_validatemonth");
			if ("".equals(acc_validatemonth)) {
				acc_validatemonth = null;
			}
			loger.warn("checkUserToDb=========({},{})", new Object[] {
					_password, tmp });
			acc_oid = StringUtil.getLongValue(fields.get("acc_oid"));
			if (_password.equals(tmp)) {
				result = true;
			}

		}

		// 如果loginmaxtrynum大于5或者为空,强制设置的值为5
		if (StringUtil.IsEmpty(loginmaxtrynum)
				|| Integer.parseInt(loginmaxtrynum) > 5) {
			loginmaxtrynum = "5";
		}
		// 如果锁定时间为空,强制设置为锁定半小时
		if (StringUtil.IsEmpty(lockedtime)) {
			lockedtime = "1800";
		}

		loger.debug("loginmaxtrynum{}", loginmaxtrynum);

		return result;
	}

	/**
	 * 判断密码有效期是否过期
	 * 
	 * @return 过期true,反之false
	 */
	public boolean checkPasswordUsedTime() {
		loger.debug("checkPasswordUsedTime()");

		boolean isDateOff = false;//
		if (StringUtil.IsEmpty(acc_validatemonth)
				|| Integer.parseInt(acc_validatemonth) > 3) {
			// return false;
			// 如果没有设置有效期，或者有效期大于三个月，强制按三个月算
			acc_validatemonth = "3";
		}

		// init
		DateTimeUtil nowDateUtil = new DateTimeUtil();// now
		DateTimeUtil createPasswdDateUtil = new DateTimeUtil(createPasswordTime);// password
		// create
		// time
		createPasswdDateUtil.getNextMonth(Integer.parseInt(acc_validatemonth));// offset
																				// 帐号有效期
		// time
		// 3
		// months

		if (nowDateUtil.getLongTime() > createPasswdDateUtil.getLongTime()) {
			isDateOff = true;
		}

		// clear
		nowDateUtil = null;
		createPasswdDateUtil = null;

		return isDateOff;
	}

	/**
	 * 判断密码有效期前一个星期提示
	 * 
	 * @return 过期true,反之false
	 */
	public int tipPasswordUsedWeek() {
		loger.debug("tipPasswordUsedWeek()");

		int nDay = 0;//
		if (StringUtil.IsEmpty(acc_validatemonth)
				|| Integer.parseInt(acc_validatemonth) > 3) {
			// return false;
			// 如果没有设置有效期，或者有效期大于三个月，强制按三个月算
			acc_validatemonth = "3";
		}

		// init
		DateTimeUtil nowDateUtil = new DateTimeUtil();// now
		DateTimeUtil createPasswdDateUtil = new DateTimeUtil(createPasswordTime);// password
		// create
		// time
		createPasswdDateUtil.getNextMonth(Integer.parseInt(acc_validatemonth));// offset
																				// 帐号有效期
		// time
		// day

		nDay = (int) ((createPasswdDateUtil.getLongTime() - nowDateUtil
				.getLongTime()) / (24 * 60 * 60));

		// clear
		nowDateUtil = null;
		createPasswdDateUtil = null;

		return nDay;
	}

	/**
	 * 检查用户登录IP是否合法
	 * 
	 * @param IP
	 * @return
	 */
	public boolean checkClientIP(String IP) {
		loger.debug("checkClientIP({})", IP);

		if (null != clientIPs) {
			if (null == IP) {
				return false;
			}
			String[] IPS = clientIPs.split(",");
			for (int i = 0; i < IPS.length; i++) {
				if (IPS[i].split("\\.").length == 3) {
					IP = IP.substring(0, IP.lastIndexOf("."));
				}
				if (IPS[i].equals(IP) || IPS[i].equals(IP + ".")) {
					return true;
				}
			}
			return false;
		}
		// 没有设置IP，则不做IP校验
		else {
			return true;
		}
	}

	/**
	 * 密码是否过期，如果过期，提示修改密码，暂定admin密码不存在失效的情况
	 * 此方法与checkPasswordUsedTime()方法不同，checkPasswordUsedTime()用来判断登录帐号是否过期
	 * 
	 * @return
	 */
	public boolean checkPasswordValidity() {

		loger.debug("DbAuthorization==>checkPasswordValidity()");

		boolean isDateOff = false;

		DateTimeUtil nowDateUtil = new DateTimeUtil(); // 系统当前时间

		DateTimeUtil createPasswdDateUtil = new DateTimeUtil(createPasswordTime);// password

		// 如果没有设置过期时间，或者过期时间大于三个月，强制按三个月算
		int pwdValidity = 0;
		if (StringUtil.IsEmpty(LipossGlobals
				.getLipossProperty("changePwd.pwdValidity"))
				|| Integer.parseInt(LipossGlobals
						.getLipossProperty("changePwd.pwdValidity")) > 3) {
			pwdValidity = 3;
		} else {
			pwdValidity = Integer.parseInt(LipossGlobals
					.getLipossProperty("changePwd.pwdValidity"));
		}
		createPasswdDateUtil.getNextMonth(pwdValidity);

		if (nowDateUtil.getLongTime() > createPasswdDateUtil.getLongTime()
				&& !"admin".equals(account)) {
			isDateOff = true;
		}

		// clear
		nowDateUtil = null;
		createPasswdDateUtil = null;

		return isDateOff;
	}

	/**
	 * 处理登录失败的情况
	 * 
	 */
	public void dealErrorLogin() {
		loger.debug("dealErrorLogin()");

		// 该用户设置了登录失败最大次数
		if (null != loginmaxtrynum) {
			HttpSession session = req.getSession();
			// 会话中第一次登录,失败次数为1
			int errorLogin = 1;
			int maxErrorLogin = Integer.parseInt(loginmaxtrynum);
			loger.debug("maxErrorLogin={}", maxErrorLogin);
			Object errorLoginObj = session.getAttribute("errorLogin");
			if (null != errorLoginObj) {
				errorLogin = Integer.parseInt((String) errorLoginObj);
				errorLogin++;
				loger.warn("errorLogin={}", errorLogin);
			} else {
				loger.warn("null == errorLoginObj");
			}
			// 用户连续登录失败次数达到用户的最大登录失败次数，要锁定时间
			if (errorLogin >= maxErrorLogin) {
				loger.warn("errorLogin >= maxErrorLogin");
				errorLogin = 0;
				// 有设置锁定时间的情况
				if (null != lockedtime) {
					int tempLockedTime = Integer.parseInt(lockedtime);
					long allowLoginTime = new Date().getTime() / 1000
							+ tempLockedTime;
					// 用个全局变量来做锁定用户
					UserMap.getInstance().addLockUser(account, allowLoginTime);
				}
			}

			session.setAttribute("errorLogin", String.valueOf(errorLogin));
			loger.warn("errorLogin={}", errorLogin);
		}

	}

	/**
	 * 通过账号、密码、域信息发送AAA服务器认证用户是否合法
	 * 
	 * @param _account
	 *            账号字符串
	 * @param _password
	 *            密码字符串
	 * @param area_id
	 *            域ID
	 * @return 用户合法返回true，反之false
	 */
	private boolean checkUserToRadius(String _account, String _password,
			long area_id) {
		loger.debug("checkUserToRadius({},{},{})", new Object[] { _account,
				_password, area_id });

		boolean result = false;
		return result;
	}

	/**
	 * 认证成功后，加载用户的相关资源到Session中，其session名字为：curUser
	 * 
	 */
	private void initUserRes() {
		loger.debug("initUserRes()");
		UserManager userMgr = new DbUserManager();

		try {
			User user = userMgr.getUser(acc_oid);
			UserRes userRes = new DbUserRes(user);
			SkinUtils.setSession(req, "curUser", userRes);
			long roleid = userRes.getUser().getRoleId();
			HashMap<String, String> illegalUrl = initItem(roleid);
			HashMap<String, String> legalUrl = initItemLegal(roleid);
			SkinUtils.setSession(req, "illegalUrl", illegalUrl);
			SkinUtils.setSession(req, "legalUrl", legalUrl);
			if (null != free_time) {
				int sessionOut = Integer.parseInt(free_time);
				SkinUtils.setSessionTimeOut(req, sessionOut);
			}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkage.litms.system.Authorization#login()
	 */
	public boolean login() {

		boolean result = false;
		
		account = LoginUtil.getAccount(req);
		loger.warn("account"+account);
		password = LoginUtil.getPassword(req);
		loger.warn("password"+password);
		if (isReportSystem){
			password=MD5.getMD5(password);
		}
		String s_area_name = LoginUtil.getAreaName(req);
		loger.warn("s_area_name"+s_area_name);
		if (s_area_name == null)
			return result;
		SkinUtils.setCookie(rep, "areaName", s_area_name);
		Area area = new AreaSyb(s_area_name);
		int i_area_id = area.getAreaId();
		area_id = (long) i_area_id;
		if (CHECK_TYPE == 1){
			result = checkUserToDb(account, password, area_id);
		}
		else {
			result = checkUserToRadius(account, password, area_id);
		}
		
		if (result) {
			initUserRes();
		}
		return result;
	}

	public void logout() {
		loger.debug("logout()");

		HttpSession session = req.getSession();
		session.removeAttribute("curUser");
	}

	/**
	 * Encodes a String as a base64 String.
	 * 
	 * @param data
	 *            a String to encode.
	 * @return a base64 encoded String.
	 */
	public static String encodeBase64(String data) {
		return encodeBase64(data.getBytes());
	}

	/**
	 * Encodes a byte array into a base64 String.
	 * 
	 * @param data
	 *            a byte array to encode.
	 * @return a base64 encode String.
	 */
	public static String encodeBase64(byte[] data) {
		final String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "abcdefghijklmnopqrstuvwxyz" + "0123456789+/";
		final int fillchar = '=';
		int c;
		int len = data.length;
		StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
		for (int i = 0; i < len; ++i) {
			c = (data[i] >> 2) & 0x3f;
			ret.append(cvt.charAt(c));
			c = (data[i] << 4) & 0x3f;
			if (++i < len)
				c |= (data[i] >> 4) & 0x0f;

			ret.append(cvt.charAt(c));
			if (i < len) {
				c = (data[i] << 2) & 0x3f;
				if (++i < len)
					c |= (data[i] >> 6) & 0x03;

				ret.append(cvt.charAt(c));
			} else {
				++i;
				ret.append((char) fillchar);
			}

			if (i < len) {
				c = data[i] & 0x3f;
				ret.append(cvt.charAt(c));
			} else {
				ret.append((char) fillchar);
			}
		}
		return ret.toString();
	}

	/**
	 * 确认当前登录帐号是否设置为唯一登录 add by zhangchy 2013-01-05
	 * 
	 * @return
	 */
	public String checkIsUnique() {

		loger.debug("checkIsUnique");
		PrepareSQL psql = new PrepareSQL();

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			CHECK_USER = "select acc_password, is_unique " +
					"from tab_accounts where acc_loginname=? "
					+ "and acc_oid in (select acc_oid from tab_acc_area where area_id=?)";
		}

		psql.setSQL(CHECK_USER);
		psql.setString(1, account);
		psql.setLong(2, area_id);

		Map fields = DataSetBean.getRecord(psql.getSQL());
		if (fields != null) {

			String tmp = MD5.getMD5(encodeBase64((String) fields.get("acc_password")));
			String isUnique = StringUtil.getStringValue(fields, "is_unique");

			if (password.equals(tmp)) {
				return isUnique;
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	/**
	 * 对登陆密码进行强验证 add by zhangchy 2013-01-05
	 * 
	 * @return
	 */
	public boolean checkStrongValid(HttpServletRequest request) {
		boolean res = true;
		int patSize = 0;

		account = LoginUtil.getAccount(request);
		String s_area_name = LoginUtil.getAreaName(request);

		Area area = new AreaSyb(s_area_name);
		int i_area_id = area.getAreaId();

		area_id = (long) i_area_id;

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			CHECK_USER = "select acc_password " +
					"from tab_accounts where acc_loginname=? "
					+ "and acc_oid in (select acc_oid from tab_acc_area where area_id=?)";
		}

		// 查询用户表信息，获取密码字段
		pSQL.setSQL(CHECK_USER);
		pSQL.setString(1, account);
		pSQL.setLong(2, area_id);

		Map fields = DataSetBean.getRecord(pSQL.getSQL());
		if (fields != null) {
			password = (String) fields.get("acc_password");
			if ("".equals(password)) {
				password = null;
			}
		}
		// HBLT-RMS-20200420-LH-001	服务器弱口令整改需求
		if (LipossGlobals.inArea(Global.HBLT)) {
			if (StringUtil.IsEmpty(password) || password.length() < 8) {
				return false;
			}
			//validpattrns = new String[] { ".*[\\d].*", ".*[a-z].*", ".*[A-Z].*"};
		}
		
		for (String regStr : validpattrns) {
			if (Pattern.matches(regStr, password)) {
				patSize++;
			}
		}
		// 是否含大小写字母、数字、特殊字符四类字符中的三类
		if (patSize < 3) {
			res = false;
		}
		return res;
	}

	/**
	 * 对登陆密码进行验证不得包含键盘上任意连续的四个字符 add by zhangchy 2013-01-05
	 * 
	 * @return
	 */
	public boolean checkKeyBoardValid(HttpServletRequest request) {
		boolean res = true;
		List<String> keyBoardStr = new ArrayList<String>();

		String[] keyBoardLine = new String[] { "qwertyuiop[]\\", "asdfghjkl;'",
				"zxcvbnm,./", "`1234567890-=" };

		account = LoginUtil.getAccount(request);
		String s_area_name = LoginUtil.getAreaName(request);

		Area area = new AreaSyb(s_area_name);
		int i_area_id = area.getAreaId();

		area_id = (long) i_area_id;

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			CHECK_USER = "select acc_password " +
					"from tab_accounts where acc_loginname=? "
					+ "and acc_oid in (select acc_oid from tab_acc_area where area_id=?)";
		}

		// 查询用户表信息，获取密码字段
		pSQL.setSQL(CHECK_USER);
		pSQL.setString(1, account);
		pSQL.setLong(2, area_id);

		Map fields = DataSetBean.getRecord(pSQL.getSQL());
		if (fields != null) {
			password = (String) fields.get("acc_password");
			if ("".equals(password)) {
				password = null;
			}
		}
		// 先将pwd转换成小写,进行字符转换预检，比如!转变为1进行检测，包括!1,@2,#3,$4{[,}]……
		password = password.toLowerCase().replaceAll("~", "`")
				.replaceAll("!", "1").replaceAll("@", "2").replaceAll("#", "3")
				.replaceAll("\\$", "4").replaceAll("%", "5")
				.replaceAll("\\^", "6").replaceAll("&", "7")
				.replaceAll("\\*", "8").replaceAll("\\(", "9")
				.replaceAll("_", "-").replaceAll("\\+", "=")
				.replaceAll("\\)", "0").replaceAll("<", ",")
				.replaceAll(">", ".").replaceAll(":", ";")
				.replaceAll("\\?", "/").replaceAll("\\{", "[")
				.replaceAll("\\}", "]").replaceAll("\\|", "\\\\")
				.replaceAll("\"", "'");
		// 反转pwd校验
		String reverse_Pwd = new StringBuffer(password).reverse().toString();
		// 键盘横向校验格式
		for (int i = 0; i < keyBoardLine.length; i++) {
			char[] a = keyBoardLine[i].toCharArray();
			for (int j = 0; j < a.length - 3; j++) {
				String x = String.valueOf(a[j]) + String.valueOf(a[j + 1])
						+ String.valueOf(a[j + 2]) + String.valueOf(a[j + 3]);
				keyBoardStr.add(x);
			}
		}
		// 键盘纵向校验格式
		for (String regStr : validkeyboard) {
			keyBoardStr.add(regStr);
		}
		for (String string : keyBoardStr) {
			// 正向校验
			if (password.indexOf(string) != -1) {
				return false;
			}
			// 反向校验
			if (reverse_Pwd.indexOf(string) != -1) {
				return false;
			}
		}
		return res;
	}

	/**
	 * 对登陆密码进行验证不能包含用户名的形似变化 add by zhangchy 2013-01-05
	 * 
	 * @return
	 */
	public boolean checkLikeNameValid(HttpServletRequest request) {
		// 形似变化字符数组
		final String validlikename[] = new String[] { "a|@", "f|t", "J|L",
				"m|n", "U|V", "p|q", "P|R", "c|C|\\(", "s|S|\\$", "0|o|O|Q",
				"1|l|i|!", "b|d|6", "2|z|Z", "g|y|9" };
		// 替换后字符
		final String replacename[] = new String[] { "a", "f", "J", "m", "U",
				"p", "P", "c", "s", "0", "1", "b", "2", "g" };

		boolean res = true;
		int i = 0;

		account = LoginUtil.getAccount(request);
		String s_area_name = LoginUtil.getAreaName(request);

		Area area = new AreaSyb(s_area_name);
		int i_area_id = area.getAreaId();

		area_id = (long) i_area_id;

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			CHECK_USER = "select acc_password " +
					"from tab_accounts where acc_loginname=? "
					+ "and acc_oid in (select acc_oid from tab_acc_area where area_id=?)";
		}

		// 查询用户表信息，获取密码字段
		pSQL.setSQL(CHECK_USER);
		pSQL.setString(1, account);
		pSQL.setLong(2, area_id);

		Map fields = DataSetBean.getRecord(pSQL.getSQL());
		if (fields != null) {
			password = (String) fields.get("acc_password");
			if ("".equals(password)) {
				password = null;
			}
		}

		// 替换所有形变字符
		for (String regStr : validlikename) {
			account = account.replaceAll(regStr, replacename[i]);
			password = password.replaceAll(regStr, replacename[i]);
			i++;
		}
		// 判断替换字符后的密码是否包含替换字符后的用户名
		if (password.indexOf(account) != -1) {
			res = false;
		}
		return res;
	}
	/***
	 * 检测用户是否是第一次登陆系统
	 */
	public boolean checkUserLogin(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long acc_oid = user.getId();

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			CHECK_USER_LOGIN = "select count(*) as num from tab_oper_log where acc_oid = ? ";
		}
		pSQL.setSQL(CHECK_USER_LOGIN);
		pSQL.setLong(1, acc_oid);
		Map fields = DataSetBean.getRecord(pSQL.getSQL());
		if(null != fields)
		{
			String loginNum = StringUtil.getStringValue(fields, "num");
			if(!"0".equals(loginNum))
			{
				loger.warn("该用户非第一次登陆系统。");
				return false;
			}
			
		}
		loger.warn("该用户第一次登陆系统。");
		return true;
	}
	
	/***
	 * 检测密码是否是默认密码
	 */
	
	public boolean isDefaultKey()
	{
		String defaultKey = LipossGlobals.getLipossProperty("DefaultKey");
		if (StringUtil.IsEmpty(defaultKey)) {
			return true;
		}
		defaultKey = MD5.getMD5(encodeBase64(defaultKey));
		if(password.equals(defaultKey))
		{
			loger.warn("该密码是默认密码，需要修改！");
			return true;
		}
		loger.warn("该密码非默认密码，不需要修改！");
		return false;
	}
	private HashMap<String,String> initItemLegal(long roleId)
	{
		HashMap itemMap = new HashMap<String, String>();
		String sql = "select item_url,item_id from tab_item where item_id in (select item_id from tab_item_role where role_id="+roleId+")";
		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(sql);
		if (list != null && !list.isEmpty())
		{
			StringBuilder url = new StringBuilder("");
			String urlStr = "";
			for (int i = 0; i < list.size(); i++)
			{
				Map<String, String> item = list.get(i);
				url = new StringBuilder(item.get("item_url"));
				while (url.indexOf("/") == 0)
				{
					url.deleteCharAt(0);
				}

				urlStr = url.toString();
				if(url.indexOf("?")!=-1){
					urlStr = urlStr.substring(0, url.indexOf("?"));
				}
				itemMap.put(urlStr, item.get("item_id"));
			}
		}
		return itemMap;
	}

	private HashMap<String,String> initItem(long roleId)
	{
		HashMap itemMap = new HashMap<String, String>();
		String sql = "select item_url,item_id from tab_item where item_id not in (select item_id from tab_item_role where role_id="+roleId+")";
		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(sql);
		if (list != null && !list.isEmpty())
		{
			StringBuilder url = new StringBuilder("");
			String urlStr = "";
			for (int i = 0; i < list.size(); i++)
			{
				Map<String, String> item = list.get(i);
				url = new StringBuilder(item.get("item_url"));
				while (url.indexOf("/") == 0)
				{
					url.deleteCharAt(0);
				}

				urlStr = url.toString();
				if(url.indexOf("?")!=-1){
					urlStr = urlStr.substring(0, url.indexOf("?"));
				}
				itemMap.put(urlStr, item.get("item_id"));
			}
		}
		return itemMap;
	}
}
