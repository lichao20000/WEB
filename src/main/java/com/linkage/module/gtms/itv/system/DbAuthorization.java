/*
 * @(#)DbAuthorization.java 1.00 1/21/2006
 *
 * Copyright 2005 联创科技.版权所有
 */

package com.linkage.module.gtms.itv.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.Encoder;
import com.linkage.litms.common.util.MD5;
import com.linkage.litms.common.util.SkinUtils;
import com.linkage.litms.system.Area;
import com.linkage.litms.system.Authorization;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserManager;
import com.linkage.litms.system.UserMap;
import com.linkage.litms.system.UserNotFoundException;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.system.dbimpl.AreaSyb;
import com.linkage.litms.system.dbimpl.DbUserManager;
import com.linkage.litms.system.dbimpl.DbUserRes;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.systemlog.core.SystemLog;
import com.linkage.system.systemlog.core.SystemLogBean;
import com.linkage.system.systemlog.core.SystemLogModuleCons;
import com.linkage.system.utils.database.DBUtil;

/**
 * 数据库实现系统注册用户登录认证授权接口
 *
 * @author yuht
 * @version 1.00, 1/21/2006
 * @since Liposs 2.1
 */
public class DbAuthorization implements Authorization
{

	private static Logger log = Logger.getLogger(DbAuthorization.class);
	private HttpServletRequest req = null;
	private HttpServletResponse rep = null;
	// 验证类型缺省1-网管系统认证；2-RADIUS
	private static int CHECK_TYPE = 1;
	private String CHECK_USER = "select acc_password, acc_pwd_time, loginmaxtrynum, lockedtime, "
			+ " limitdate, configip, isexclusive, acc_validatemonth, is_unique "
			+ " from tab_accounts where acc_loginname=? "
			+ " and acc_oid in(select acc_oid from tab_acc_area where area_id = ?)";

	private String CHECK_AREA = "select loginmaxtrynum, lockedtime from tab_accounts where acc_loginname=? "
		+ "and acc_password = ?";
	//查询用户是否登陆过系统
	private String CHECK_USER_LOGIN = "select count(*) as num from tab_oper_log where acc_oid = ? ";
	/**
	 * 获取所有区域信息
	 */
	private String ALLAREAS = "select area_id,area_name from tab_area";
	// private String CHECK_PASSWORD = "select acc_pwd_time form tab_accounts
	// where acc_loginname=? ";
	private String account = null;
	private String password = null;
	private long area_id;
	//add by yangdingtao
	private String userip = null;

	private PrepareSQL pSQL = null;
	// 用于记录用户创建密码或上次更新密码的时间
	private String createPasswordTime = null;
	private String acc_first_pwd = null;
	private String acc_second_pwd = null;
	// 最大登录失败次数
	private String loginmaxtrynum = null;
	// 登录失败锁定时间，单位为分
	private String lockedtime = null;
	// 登录时间范围
	private String limitdate = null;
	// 允许登录IP
	private String configip = null;
	// 是否允许重复登录
	private boolean isexclusive = false;
	// 帐号有效期，单位为月
	private String acc_validatemonth = null;
	/**
	 * 存储域名与域ID关联
	 */
	private static Map AreaInfoMap = new HashMap();

	/**
	 * 带参数构造函数
	 *
	 * @param _req
	 *            HttpServletRequest对象
	 * @param _rep
	 *            HttpServletResponse对象
	 */
	public DbAuthorization(HttpServletRequest _req, HttpServletResponse _rep)
	{
		this.req = _req;
		this.rep = _rep;
		if (pSQL == null)
		{
			pSQL = new PrepareSQL();
		}
		// 验证类型：缺省1-网管系统认证；2-RADIUS
		String s_check_type = LipossGlobals.getLipossProperty("auth.checktype");
		if (s_check_type != null)
		{
			CHECK_TYPE = Integer.parseInt(s_check_type);
			s_check_type = null;
		}
	}

	/**
	 * 存储域名与域ID关联
	 */
	private synchronized void getAreaNameToAreaId()
	{
		// clear all key-value
		AreaInfoMap.clear();
		pSQL.setSQL(ALLAREAS);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map field = cursor.getNext();
		while (field != null)
		{
			AreaInfoMap.put(field.get("area_name"), field.get("area_id"));
			field = cursor.getNext();
		}
		// clear
		field = null;
		cursor = null;
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
	private boolean checkUserToDb(String _account, String _password, long area_id)
	{
		log.info("checkUserToDb begin");
		boolean result = false;
		pSQL.setSQL(CHECK_USER);
		pSQL.setString(1, _account);
		pSQL.setLong(2, area_id);
		System.out.println("@@@@@@@@@@ pSQL.getSQL()=" + pSQL.getSQL());
		Map fields = DataSetBean.getRecord(pSQL.getSQL());
		if (fields != null)
		{
			String tmp = (String) fields.get("acc_password");
//			log.info("tmp=======" + tmp);
			// 判断是否需要加密
			if ("true".equals(LipossGlobals.getLipossProperty("hasEncoder")))
			{
				/**
				 *  新建用户，或修改密码时，配置文件有配置项询问是否需要加密，如果true，则存入数据库的密码是经过加密的
				 */
				tmp = Encoder.getFromBase64(tmp);
			}
			createPasswordTime = (String) fields.get("acc_pwd_time");
//			log.info("tmp=======" + tmp);
//			log.info("_password=======" + _password);
			loginmaxtrynum = (String) fields.get("loginmaxtrynum");
			log.info("loginmaxtrynum=======" + loginmaxtrynum);
			lockedtime = (String) fields.get("lockedtime");

			/**
			 * 此处是否需要解密，需要结合数据库中的密码，及实际情况而定
			 *
			 * 目前前台传输过来的密码是经过加密的，所以此处比对密码时，需要将前台传输过来的密码进行解密
			 */
			if (Encoder.getFromBase64(_password).equals(tmp))
			{
				limitdate = (String) fields.get("limitdate");
				configip = (String) fields.get("configip");
				log.info("checkUserToDb_isexclusive:" + fields.get("isexclusive"));
				if (null != fields.get("isexclusive")
						&& !((String) fields.get("isexclusive")).equals("")
						&& 1 == Integer.parseInt((String) fields.get("isexclusive")))
				{
					isexclusive = true;
				}
				acc_validatemonth = (String) fields.get("acc_validatemonth");
				result = true;
			}
			// clear
			tmp = null;
		}
		// clear
		fields = null;
		log.info("checkUserToDb end");
		return result;
	}

	/**
	 * 验证当前登录用户是否在有效时间范围内
	 *
	 * @param username
	 */
	public boolean checkTime(String username)
	{
		log.info("start checkTime " + username);
		// 验证时间是否在有效范围内
		if (null != this.limitdate && !"".equals(this.limitdate.trim()))
		{
			// 分解时间字符串，并判断时间是否合法
			String[] timeArr = this.limitdate.split("-");
			DateTimeUtil dt = new DateTimeUtil();
			long nowLongTime = dt.getLongTime();
			if (timeArr != null && timeArr.length == 2)
			{
				String minTime = dt.getDate() + " " + timeArr[0];
				String maxTime = dt.getDate() + " " + timeArr[1];
				// 允许的开始时间和结束时间
				DateTimeUtil minDate = new DateTimeUtil(minTime);
				DateTimeUtil maxDate = new DateTimeUtil(maxTime);
				long minLongTime = minDate.getLongTime();
				long maxLongTime = maxDate.getLongTime();
				// 判断当前时间是否在有效时间的最大和最小值之间
				if (nowLongTime >= minLongTime && nowLongTime <= maxLongTime)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				// 配置信息不正确，则认为所有时间有效
				return true;
			}
		}
		else
		{
			// 没有配置时间则默认所有时间都有效
			return true;
		}
	}

	/**
	 * 验证当前登录用户IP是否在指定的网段内
	 *
	 * @param username
	 */
	public boolean checkIP(String username, String remoteIP)
	{
		log.info("start checkIP " + username + "    " + remoteIP);
		// 验证ip是否在指定网段内
		if (this.configip != null && !"".equals(this.configip.trim()))
		{
			// 分解ip网段字符串，并判断ip是否合法
			String[] ipArr = this.configip.split(",");
			if (ipArr != null && ipArr.length > 0)
			{
				// ip合法标志
				boolean flag = false;
				for (int i = 0; i < ipArr.length; i++)
				{
					flag = compareIP(remoteIP, ipArr[i]);
					// 只要有一个网段符合条件则退出循环
					if (flag)
					{
						break;
					}
				}
				return flag;
			}
			else
			{
				// 配置信息不正确，则检查通过
				return true;
			}
		}
		else
		{
			// 没有配置网段则默认所有ip都有效
			return true;
		}
	}

	/**
	 * 比较remoteIP是否在ip的范围内，若在则返回true，否则返回false
	 *
	 * @param remoteIP
	 * @param ip
	 */
	private boolean compareIP(String remoteIP, String ip)
	{
		// 将ip按点号分开，逐一比较
		String[] remoteIPArr = remoteIP.split("\\.");
		String[] ipArr = ip.split("\\.");
		if (remoteIPArr != null && remoteIPArr.length > 3 && ipArr != null
				&& ipArr.length > 3)
		{
			boolean flag = false;
			// 对ip的每一位进行判断，若某一位不符则返回false
			if ("*".equals(ipArr[0]) || remoteIPArr[0].equals(ipArr[0]))
			{
				if ("*".equals(ipArr[1]) || remoteIPArr[1].equals(ipArr[1]))
				{
					if ("*".equals(ipArr[2]) || remoteIPArr[2].equals(ipArr[2]))
					{
						if ("*".equals(ipArr[3]) || remoteIPArr[3].equals(ipArr[3]))
						{
							flag = true;
						}
						else
						{
							flag = false;
						}
					}
					else
					{
						flag = false;
					}
				}
				else
				{
					flag = false;
				}
			}
			else
			{
				flag = false;
			}
			// 返回比较结果
			return flag;
		}
		else
		{
			// 获取的ip或配置的ip不正确则返回false
			return false;
		}
	}

	/**
	 * 验证当前登录用户是否唯一登录
	 *
	 * @param username
	 */
	public void checkExclusive(String username)
	{
		log.info("start checkExclusive  " + username + "   " + this.isexclusive);
		// 不允许重复登录,则把系统已登录用户失效
		if (this.isexclusive)
		{
			UserMap.getInstance().destroySessionByUserName(username);
		}
	}

	/**
	 * 处理登录失败的情况
	 */
	public void dealErrorLogin()
	{
	    log.info("dealErrorLogin begin");
		// 该用户设置了登录失败最大次数
		if (null != loginmaxtrynum && !"".equals(loginmaxtrynum.trim()))
		{
			log.info("null != loginmaxtrynum ");
			HttpSession session = req.getSession();
			// 会话中第一次登录,失败次数为1
			int errorLogin = 1;
			int errorIpLogin = 1;
			int maxErrorLogin = Integer.parseInt(loginmaxtrynum);
			/*Object errorLoginObj = session.getAttribute(account);
//		    userip = req.getRemoteAddr();
			userip = getRemoteAddr(req);
		    log.info("userip======" + userip);
			Object errorLoginIpObj = session.getAttribute(userip);
			if (null != errorLoginObj)
			{
				errorLogin = Integer.parseInt((String) errorLoginObj);
				errorLogin++;
			}
			if (null != errorLoginIpObj)
			{
				errorIpLogin = Integer.parseInt((String) errorLoginIpObj);
				errorIpLogin++;
			}*/
			Object errorLoginObj = session.getAttribute("errorLogin");
			if (null != errorLoginObj) {
				errorLogin = Integer.parseInt((String) errorLoginObj);
				errorLogin++;
				log.warn("errorLogin={"+errorLogin+"}");
			} else {
				log.warn("null == errorLoginObj");
			}

			log.info("account = " + account + ", errorLogin num =====" + errorLogin);
			//log.info("userip = " + userip + ", errorIpLogin num =====" + errorIpLogin);

			// 用户连续登录失败次数达到用户的最大登录失败次数，要锁定时间
			if (errorLogin >= maxErrorLogin)
			{
				errorLogin = 0;
				// 有设置锁定时间的情况
				if (null != lockedtime && !"".equals(lockedtime.trim()))
				{
					int tempLockedTime = Integer.parseInt(lockedtime);
					long allowLoginTime = new Date().getTime() / 1000 + tempLockedTime
							* 60;
					// 用个全局变量来做锁定用户
					UserMap.getInstance().addLockUser(account, allowLoginTime);
				}
			}

			/*// 相同用户IP连续登录失败次数达到用户的最大登录失败次数，要锁定时间
			if (errorIpLogin >= maxErrorLogin)
			{
				errorIpLogin = 0;
				// 有设置锁定时间的情况
				if (null != lockedtime && !"".equals(lockedtime.trim()))
				{
					int tempLockedTime = Integer.parseInt(lockedtime);
					long allowLoginTime = new Date().getTime() / 1000 + tempLockedTime
							* 60;
					// 用个全局变量来做锁定用户
					UserIpMap.getInstance().addLockUserIp(userip, allowLoginTime);
				}

				//把账号和IP的对应关系放入
				UserIpMap.getInstance().addlockUserMap(account, userip);

			}*/

			session.setAttribute(account, String.valueOf(errorLogin));
			//session.setAttribute(userip, String.valueOf(errorIpLogin));
		}
		log.info("dealErrorLogin end");
	}


	/**
	 * 服务端在反向代理软件中获取客户端源客户端IP地址
	 * @param request
	 * @return
	 */
	public String getRemoteAddr(HttpServletRequest request) {

		String result = request.getHeader("x-forwarded-for");

		if (request == null || result.length() == 0 || "unknown".equalsIgnoreCase(result)) {
			result = request.getHeader("Proxy-Client-IP");
		}

		if (request == null || result.length() == 0 || "unknown".equalsIgnoreCase(result)) {
			result = request.getHeader("WL-Proxy-Client-IP");
		}

		if (request == null || result.length() == 0 || "unknown".equalsIgnoreCase(result)) {
			result = request.getRemoteAddr();
		}

		return result;
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
	private boolean checkUserToRadius(String _account, String _password, long area_id)
	{
		boolean result = false;
		return result;
	}

	/**
	 * 认证成功后，加载用户的相关资源到Session中，其session名字为：curUser
	 */
	public void initUserRes()
	{
		UserManager userMgr = new DbUserManager();
		try
		{
			account = req.getAttribute("acc_loginname").toString();
			String s_area_name = req.getParameter("area_name");
			area_id = Long.parseLong(String.valueOf(AreaInfoMap.get(s_area_name)));
			//
			s_area_name = null;
			User user = userMgr.getUser(account, area_id);
			UserRes userRes = new DbUserRes(user);
			SkinUtils.setSession(req, "curUser", userRes);
			List list = userRes.getUserPermissions();
			String s_per_liposs = null;
			if (list != null)
			{
				for (int i = 0; i < list.size(); i++)
				{
					if (s_per_liposs == null)
						s_per_liposs = (String) list.get(i);
					else
						s_per_liposs += "#" + (String) list.get(i);
				}
			}
			if (s_per_liposs == null)
			{
				s_per_liposs = "";
			}
			SkinUtils.setSession(req, "ldims", s_per_liposs);
			try
			{
				String remoteip = req.getRemoteAddr();
				String hostname = req.getRemoteHost();
				java.util.Date now = new java.util.Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String tmp_now = formatter.format(now);
				String strSQL = "insert into logintrace (loginname,logintime,remoteip,remotename) values ('"
						+ user.getAccount()
						+ "','"
						+ tmp_now
						+ "','"
						+ remoteip
						+ "','"
						+ hostname + " ')";
				pSQL.setSQL(strSQL);
				DataSetBean.executeUpdate(pSQL.getSQL());
			}
			catch (Exception exception)
			{
			}
		}
		catch (UserNotFoundException e)
		{
			log.error("can't found this user: " + account);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.linkage.liposs.system.Authorization#login()
	 */
	public boolean login()
	{
		log.info("begin login");
		boolean result = false;
		boolean isPassworld = false;
		account = req.getAttribute("acc_loginname").toString();
		password = req.getAttribute("acc_password").toString();
		String s_area_name = req.getParameter("area_name");
		// if area_name is null
		if (s_area_name == null)
		{
			return result;
		}
		if (AreaInfoMap.get(s_area_name) == null)
		{
			this.getAreaNameToAreaId();
			if (AreaInfoMap.get(s_area_name) == null)
			{
				return result;
			}
		}
		SkinUtils.setCookie(rep, "areaName", s_area_name);
		area_id = Long.parseLong((String) AreaInfoMap.get(s_area_name));
		if (CHECK_TYPE == 1)
		{
			result = checkUserToDb(account, password, area_id);
		}
		else
		{
			result = checkUserToRadius(account, password, area_id);
		}
		// if (result){
		// initUserRes();
		// }
		isPassworld = checkPasswordUsedTime();
		System.out.println("======>" + result + isPassworld + (result && !isPassworld));
		/** 如果身份验证通过并且密码没有过期则加载相关用户资料 */
//		if (result && !isPassworld)
		/*if (result)
		{
			initUserRes();
		}*/
		/*
		 * 记录登陆日志
		 */
		SystemLogBean sysBean = new SystemLogBean();
		sysBean.setAccountName(account);
		sysBean.setAreaId(area_id);
		sysBean.setIpAddr(req.getRemoteAddr());
		if (result)
		{// 登陆成功
			SystemLog.success(sysBean, SystemLogModuleCons.MODULE_LOGIN, "登陆成功");
		}
		else
		{// 登陆失败
			SystemLog.fail(sysBean, SystemLogModuleCons.MODULE_LOGIN, "登陆失败");
		}
		log.info("end login");
		return result;
	}

	public void logout()
	{
		req.getSession().removeAttribute("curUser");
	}

	/**
	 * 判断密码有效期是否过期
	 *
	 * @return 过期true,反之false add by wangfeng 2006-12-15 修改密码策略
	 */
	public boolean checkPasswordUsedTime()
	{
		boolean isDateOff = false;
		DateTimeUtil createPasswdDateUtil = null;
		// 初始化当前的时间
		DateTimeUtil nowDateUtil = new DateTimeUtil();
		// 获得用户上次创建或更改密码的时间
		if (createPasswordTime != null)
		{
			createPasswdDateUtil = new DateTimeUtil(this.createPasswordTime);
		}
		else
		{
			createPasswdDateUtil = new DateTimeUtil();
		}
		// 需要控制帐号有效期
		if (null != LipossGlobals.getLipossProperty("auth.checkAccountTime")
				&& "true".equalsIgnoreCase(LipossGlobals
						.getLipossProperty("auth.checkAccountTime")))
		{
			// 密码有效期设置了数值就进行控制
			if (null != this.acc_validatemonth
					&& !"".equals(this.acc_validatemonth.trim()))
			{
				createPasswdDateUtil.getNextMonth(Integer
						.parseInt(this.acc_validatemonth));
				if (nowDateUtil.getLongTime() > createPasswdDateUtil.getLongTime())
				{
					isDateOff = true;
				}
			}
			else
			{
				isDateOff = true;
			}
		}
		else
		{
			// 获得用户上次创建或更改密码的时间往后偏移数个月的时间（具体数字见配置文件lims_cfg.xml中的EffectiveDate）
			createPasswdDateUtil.getNextMonth(Integer.valueOf(
					LipossGlobals.getLipossProperty("auth.EffectiveDate")).intValue());
			// 判断当前时间是否过期
			if (nowDateUtil.getLongTime() > createPasswdDateUtil.getLongTime())
			{
				isDateOff = true;
			}
		}
		// 对象回收
		nowDateUtil = null;
		createPasswdDateUtil = null;
		return isDateOff;
	}

	/**
	 * 获取登陆用户的密码还有多少天过期
	 *
	 * @return int 天数 add by wangfeng 2006-12-15 修改密码策略
	 */
	public int getCheckDay()
	{
		int day = 0;
		DateTimeUtil dateNow = null;
		String strNextThreeMonth = null;
		String strNowTime = null;
		dateNow = new DateTimeUtil(createPasswordTime);
		dateNow.getNextMonth(3);
		strNextThreeMonth = dateNow.getDate();
		dateNow = new DateTimeUtil();
		strNowTime = dateNow.getDate();
		day = DateTimeUtil.nDaysBetweenTwoDate(strNowTime, strNextThreeMonth);
		// 对象回收
		dateNow = null;
		strNextThreeMonth = null;
		strNowTime = null;
		return day;
	}

	/**
	 * 获取登陆用户的密码还有多少天过期
	 *
	 * @return int 天数 add by wangfeng 2006-12-15 修改密码策略
	 */
	public static int getCheckDay(String account)
	{
		int day = 0;
		String cptime = null;
		DateTimeUtil dateNow = null;
		String strNextThreeMonth = null;
		String strNowTime = null;
		String strSQL = "select acc_pwd_time from tab_accounts where acc_loginname='"
				+ account + "'";
		PrepareSQL psql = new PrepareSQL(strSQL);
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map filed = cursor.getNext();
		if (filed != null)
		{
			cptime = (String) filed.get("acc_pwd_time");
		}
		if (cptime != null)
		{
			dateNow = new DateTimeUtil(cptime);
		}
		else
		{
			dateNow = new DateTimeUtil();
		}
		dateNow.getNextMonth(3);
		strNextThreeMonth = dateNow.getDate();
		dateNow = new DateTimeUtil();
		strNowTime = dateNow.getDate();
		day = DateTimeUtil.nDaysBetweenTwoDate(strNowTime, strNextThreeMonth);
		// 对象回收
		dateNow = null;
		strNextThreeMonth = null;
		strNowTime = null;
		return day;
	}

	public String getPassword()
	{
		return password;
	}

	public String getCreatePasswordTime()
	{
		return createPasswordTime;
	}

	/**
	 * 获取用户初始页面 add by wangfeng 2007-4-24 获取用户初始页面
	 */
	public String getIntPage()
	{
		String intPage = null;
		String username = req.getAttribute("acc_loginname").toString();
		String strSQL = "select init_page from tab_accounts where acc_loginname = '"
				+ username + "'";
		pSQL.setSQL(strSQL);
		Map fields = DataSetBean.getRecord(pSQL.getSQL());
		if (fields != null)
		{
			intPage = (String) fields.get("init_page");
		}
		return intPage;
	}

	/**
	 * 密码到期后更新密码 add by wangfeng 2007-7-12
	 */
	public String modifyPassWorld()
	{
		String strSQL = "";
		String strMsg = "";
		account = req.getParameter("loginname");
		String s_area_name = req.getParameter("area_name");
		SkinUtils.setCookie(rep, "areaName", s_area_name);
		Area area = new AreaSyb(s_area_name);
		int i_area_id = area.getAreaId();
		area_id = (long) i_area_id;
		// 修改密码操作
		String acc_oid = req.getParameter("loginname");
		String old_pwd = req.getParameter("old_pwd");
		String new_pwd = req.getParameter("new_pwd");
		// 判断密码中是否出现了用户名
		if (new_pwd.indexOf(acc_oid) != -1)
		{
			strSQL = "";
			strMsg = "您设置的密码中出现了用户名，请重新设置";
		}
		else
		{
			// 判断是否需要加密
			if ("true".equals(LipossGlobals.getLipossProperty("hasEncoder")))
			{
				old_pwd = Encoder.getBase64(old_pwd);
				new_pwd = Encoder.getBase64(new_pwd);
			}
			// 判断密码的历史长度是否为3
			if (checkPasswordExisted(acc_oid, new_pwd))
			{
				strSQL = "select acc_oid from tab_accounts where acc_loginname='" + acc_oid
						+ "' and acc_password='" + old_pwd + "'";
				pSQL.setSQL(strSQL);
				Map fields = DataSetBean.getRecord(pSQL.getSQL());
				// 判断旧密码是否正确
				if (fields != null)
				{
					// 获得当前时间
					Date now = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					int dbtype = DBUtil.getDbType();
					String tmp_now = formatter.format(now);
					strSQL = "update tab_accounts set acc_password='"
							+ new_pwd
							+ "',acc_first_pwd='"
							+ acc_second_pwd
							+ "' , acc_second_pwd='"
							+ new_pwd
							+ "' , acc_pwd_time="
							+ (dbtype == 2 ? ("to_date('" + tmp_now + "','yyyy-mm-dd hh24：mi：ss')")
									: "'" + tmp_now + "'") + " where acc_loginname='"
							+ acc_oid + "' and acc_password='" + old_pwd + "'";
				}
				else
				{
					strSQL = "";
					strMsg = "旧密码不正确，修改失败";
				}
			}
			else
			{
				strSQL = "";
				strMsg = "您设置的密码和前两次有重复，请重新设置！";
			}
		}
		System.out.println(strSQL);
		if (!strSQL.equals(""))
		{
			pSQL.setSQL(strSQL);
			int iCode[] = DataSetBean.doBatch(pSQL.getSQL());
			if (iCode != null && iCode.length > 0)
			{
				initUserRes();
				strMsg = "密码更新成功！";
			}
			else
			{
				strMsg = "密码更新失败！";
			}
		}
		return strMsg;
	}

	/**
	 * 判断密码的历史长度
	 *
	 * @param _s1
	 *            用户登录名
	 * @param _s2
	 *            用户新设密码
	 * @return 是否符合密码长度 add by wangfeng 2006-12-15 修改密码策略
	 */
	public boolean checkPasswordExisted(String _s1, String _s2)
	{
		boolean IsExist = true;
		String strSQL_modify = null;
		String acc_oid = _s1;
		String new_pwd = _s2;
		strSQL_modify = "select acc_first_pwd,acc_second_pwd from tab_accounts where acc_loginname ='"
				+ acc_oid + "'";
		pSQL.setSQL(strSQL_modify);
		Map fields_tmp = DataSetBean.getRecord(pSQL.getSQL());
		acc_first_pwd = (String) fields_tmp.get("acc_first_pwd");
		acc_second_pwd = (String) fields_tmp.get("acc_second_pwd");
		if ((acc_first_pwd.equals(new_pwd)) || (acc_second_pwd.equals(new_pwd)))
		{
			IsExist = false;
		}
		else
		{
			IsExist = true;
		}
		return IsExist;
	}

	public static boolean CheckNumAndLetter(String b)
	{
		b = b.toLowerCase();
		String a1 = b;
		boolean b1 = isContain(a1, "([a-z]+)");
		boolean b2 = isContain(a1, "([0-9]+)");
		return b1 && b2;
	}

	public static boolean isContain(String a, String b)
	{
		Pattern ms = Pattern.compile(b, 2);
		Matcher m = ms.matcher(a);
		return m.find();
	}

	public static String getlogininfo(String loginname)
	{
		String logininfo = null;
		String sql = "select loginname,logintime,remoteip from logintrace where loginname='"
				+ loginname + "' order by logintime desc";
		PrepareSQL pSql = new PrepareSQL(sql);
		Cursor cursor = DataSetBean.getCursor(pSql.getSQL());
		Map fields = cursor.getNext();
		if (fields != null)
			fields = cursor.getNext();
		if (fields != null)
		{
			DateTimeUtil dtUtil = new DateTimeUtil((String) fields.get("logintime"));
			SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			logininfo = "您最近一次登录时间：" + fields.get("logintime") + " 当时登录ip:"
					+ fields.get("remoteip");
		}
		return logininfo;
	}

	/**
	 * 判断用户名是否存在
	 *
	 * @return 存在返回true ，不存在返回false
	 */
	public boolean checkUserName()
	{
		account = req.getAttribute("acc_loginname").toString();
		String sql = "select acc_oid from tab_accounts where acc_loginname='" + account + "'";
		log.info("checkUserName sql="+sql);
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 根据用户名判断填写的域名是否正确 liuht 66360 add
	 */
	public boolean loginArea()
	{
		 account = req.getAttribute("acc_loginname").toString();
		String s_area_name = req.getParameter("area_name");
		//add by yangdingtao
		// 此处需要到数据库查询，由于前台将密码加密了，所以此处需要解密    add by zhangchy 20130805
		password = Encoder.getFromBase64(req.getAttribute("acc_password").toString());

		pSQL.setSQL(CHECK_AREA);
		pSQL.setString(1, account);
		pSQL.setString(2, password);
		System.out.println("@@@@@@@@@@ pSQL.getSQL()=" + pSQL.getSQL());
		Map fieldsArea = DataSetBean.getRecord(pSQL.getSQL());
		if (fieldsArea != null)
		{
			loginmaxtrynum = (String) fieldsArea.get("loginmaxtrynum");
			log.info("loginmaxtrynum=======" + loginmaxtrynum);
			lockedtime = (String) fieldsArea.get("lockedtime");
			log.info("lockedtime=======" + lockedtime);
		}
		//add by yangdingtao

		if (s_area_name == null)
		{
			return false;
		}
		if (AreaInfoMap.get(s_area_name) == null)
		{
			this.getAreaNameToAreaId();
			if (AreaInfoMap.get(s_area_name) == null)
			{
				return false;
			}
		}
		long area_id = Long.parseLong((String) AreaInfoMap.get(s_area_name));// 域名所对应的long值
		String sql = "select a.acc_oid from tab_accounts a,tab_acc_area b where a.acc_loginname='" + account+ "' and a.acc_oid=b.acc_oid and b.area_id=" + area_id;
		pSQL.setSQL(sql);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map fields = cursor.getNext();
		if (fields != null)
		{
			return true;
		}
		else
		{
			return false;
		}
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
		pSQL.setSQL(CHECK_USER_LOGIN);
		pSQL.setLong(1, acc_oid);
		Map fields = DataSetBean.getRecord(pSQL.getSQL());
		if(null != fields)
		{
			String loginNum = StringUtil.getStringValue(fields, "num");
			if(!"0".equals(loginNum))
			{
				log.warn("该用户非第一次登陆系统。");
				return false;
			}

		}
		log.warn("该用户第一次登陆系统。");
		return true;
	}

	/***
	 * 检测密码是否是默认密码
	 */

	public boolean isDefaultKey()
	{
		String defaultKey = LipossGlobals.getLipossProperty("DefaultKey");
		if(Encoder.getFromBase64(password).equals(defaultKey))
		{
			log.warn("该密码是默认密码，需要修改！");
			return true;
		}
		log.warn("该密码非默认密码，不需要修改！");
		return false;
	}

	/**
	 * 确认当前登录帐号是否设置为唯一登录 add by zhangchy 2013-01-05
	 *
	 * @return
	 */
	public String checkIsUnique() {

		log.debug("checkIsUnique");
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(CHECK_USER);
		psql.setString(1, account);
		psql.setLong(2, area_id);

		Map fields = DataSetBean.getRecord(psql.getSQL());
		if (fields != null) {

			String tmp = (String) fields.get("acc_password");
			String isUnique = StringUtil.getStringValue(fields, "is_unique");

			if (Encoder.getFromBase64(password).equals(tmp)) {
				return isUnique;
			} else {
				return "";
			}
		} else {
			return "";
		}
	}
}
