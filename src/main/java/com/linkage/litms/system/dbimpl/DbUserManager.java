/*
 * @(#)DbUserManager.java	1.00 1/20/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.system.dbimpl;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.Base64;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.MD5;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.*;
import com.linkage.module.gwms.Global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 用数据库方式实现系统集中管理用户接口
 * 
 * @author yuht
 * @version 1.00, 1/20/2006
 * @see UserManager
 * @since Liposs 2.1
 */
public class DbUserManager implements UserManager {
	private static Logger m_logger = LoggerFactory
			.getLogger(DbUserManager.class);

	/** 删除用户账号信息  */
	private static String DELETE_USER = "delete from tab_accounts where acc_oid=?";

	/** 删除用户资料信息 */
	private static String DELETE_USERINFO = "delete from tab_persons where per_acc_oid=?";

	/** 解除用户关联角色信息 */
	private static String DELETE_USER_ROLE = "delete from tab_acc_role where acc_oid=?";

	/** 解除用户关联域信息 */
	private static String DELETE_USER_AREA = "delete from tab_acc_area where acc_oid=?";

	/** 查询用户 */
	private static String LOAD_ALLUSER = "select b.creator,b.acc_loginname,a.per_acc_oid,"
			+ "a.per_name,a.per_email,a.per_dep_oid from tab_persons a,tab_accounts b "
			+ "where a.per_acc_oid=b.acc_oid and <!-- Filter --> order by a.per_acc_oid";

	/** 解除用户关联角色信息 */
	private static String PASSWORD_PATTRN = ".*[\u4E00-\u9FA5|[\uFE30-\uFFA0]].*";

	private PrepareSQL pSql = null;

	private String pageBar = null;

	/** 用于验证：密码中必须含大小写字母、数字、特殊字符四类字符中的三类。 */
	private static String[] validpattrns = new String[] { ".*[\\d].*",
			".*[a-z].*", ".*[A-Z].*",
			".*[`~!@#$^&*()_=%|{}':;',\\[\\].<>/?\\\\].*" };

	/** 用于验证：密码不得包含键盘上任意连续的四个字符。*/
	private static String[] validkeyboard = new String[] { "1qaz", "2wsx",
			"3edc", "4rfv", "5tgb", "6yhn", "7ujm", "8ik,", "9ol.", "0p;/" };
	
	/** 内蒙古电信用于验证：密码不得包含部门省市名称等*/
	private static String[] InvalidProvencekeyboard = new String[] { "itms",
				"wangyunbu", "wyb", "noc", "nmg", "neimenggu", "nmdx",
				"neimengdianxin,", "dx", "dianxin", "wumeng", "humeng", "baotou",
				"wuhai", "tongliao", "chifeng", "eeduosi", "bameng", "ximeng",
				"xinganmeng", "ameng", "huhehaote", "hhht", "eeds", "xam" };

	/** 操作 */
	private static final String DELETE = "delete";
	private static final String MODIFY_PWD = "modifypwd";
	private static final String ADD = "add";
	
	
	public DbUserManager() {
		if (pSql == null) {
			pSql = new PrepareSQL();
		}
	}
	
	@Override
	public User createUser(String account, String passwd, long creator,
			long roleId, long areaId) throws UserAlreadyExistsException {
		User newUser = null;
		try {
			// 判断用户是否已经存在
			getUser(account, areaId);

			throw new UserAlreadyExistsException();
		} catch (UserNotFoundException unfe) {
			// 创建一个新用户
			newUser = new DbUser(account, passwd, creator, roleId, areaId);
		}
		return newUser;
	}

	@Override
	public User createUser(String account, String passwd, long creator,
			long roleId, long areaId, Map map)
			throws UserAlreadyExistsException {
		User newUser = null;
		try {
			// 判断用户是否已经存在
			getUser(account, areaId);

			throw new UserAlreadyExistsException();
		} catch (UserNotFoundException unfe) {
			// 创建一个新用户
			newUser = new DbUser(account, passwd, creator, roleId, areaId,
					map);
		}
		return newUser;
	}

	@Override
	public User getUser(long accOid) throws UserNotFoundException {
		User user = new DbUser(accOid);
		return user;
	}

	@Override
	public User getUser(String account, long areaId)
			throws UserNotFoundException {
		User user = new DbUser(account, areaId);

		return user;
	}

	@Override
	public User getUser(String account, long areaId, boolean cas)
			throws UserNotFoundException {
		User user = new DbUser(account, areaId, cas);

		return user;
	}

	@Override
	public void deleteUser(User user) {
		long accOid = user.getId();
		deleteUser(accOid);
	}

	@Override
	public void deleteUser(long accOid) {
		String[] arrSql = new String[4];
		pSql.setSQL(DELETE_USER);
		pSql.setLong(1, accOid);
		arrSql[0] = pSql.getSQL();
		pSql.setSQL(DELETE_USERINFO);
		pSql.setLong(1, accOid);
		arrSql[1] = pSql.getSQL();
		pSql.setSQL(DELETE_USER_ROLE);
		pSql.setLong(1, accOid);
		arrSql[2] = pSql.getSQL();
		pSql.setSQL(DELETE_USER_AREA);
		pSql.setLong(1, accOid);
		arrSql[3] = pSql.getSQL();

		DataSetBean.doBatch(arrSql);
	}

	@Override
	public Cursor users(int startIndex, int numResults) {
		QueryPage qryp = new QueryPage();
		qryp.initPage(LOAD_ALLUSER, startIndex, numResults);
		pageBar = qryp.getPageBar();
		PrepareSQL psql = new PrepareSQL(LOAD_ALLUSER);
		psql.getSQL();
		return DataSetBean.getCursor(LOAD_ALLUSER, startIndex, numResults);
	}

	@Override
	public Cursor users(Map map) {

		return null;
	}

	/**
	 * 用户列表分页Banner条
	 * 
	 * @return 分页HTML 字符串
	 */
	public String getPageBar() {
		return pageBar;
	}

	/**
	 * 判断密码的历史长度
	 * 
	 * @param s1
	 *            用户登录名
	 * @param s2
	 *            用户新设密码
	 * @return 是否符合密码长度
	 */
	public boolean checkPasswordExisted(String s1, String s2) {
		return checkPasswordExistedStronger(s1, s2);
	}

	/**
	 * 判断是否和之前五次设置的密码相同
	 * 
	 * @param _s1
	 *            用户登录名
	 * @param _s2
	 *            用户新设密码
	 * @return 是否不存有前五次的密码和本次相同 true表示不存在，false表示存在
	 */
	public boolean checkPasswordExistedStronger(String s1, String s2) {
		
		int fieldsTmpListSize = 5;
		boolean isExist = true;
		String hispwdsSql = null;
		String accLoginname = s1;
		String newPwd = s2;
		String getAccIdSql = "select acc_oid from tab_accounts where acc_loginname='"
				+ accLoginname + "'";
		Map res = DataSetBean.getRecord(getAccIdSql);
		long accOid = Long.parseLong((String) res.get("acc_oid"));
		hispwdsSql = "select acc_oid,hispwd,histime from tab_acc_historypwd where acc_oid = (select "
				+ "acc_oid from tab_accounts where acc_loginname = ?)"
				+ "  order by histime asc";
		List<Map> fieldsTmpList = DataSetBean.executeQuery(hispwdsSql,
				new String[] { accLoginname });
		List<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = new PrepareSQL();
		if (null != fieldsTmpList && fieldsTmpList.size() > 0) {
			String temPwd = "";
			for (Map tem : fieldsTmpList) {
				temPwd = (String) tem.get("hispwd");
				if (newPwd.equals(temPwd)) {
					isExist = false;
					return isExist;
				}
			}
			if (fieldsTmpList.size() == fieldsTmpListSize) {
				// 删除最早的
				psql.setSQL("delete from tab_acc_historypwd where histime= ?");
				psql.setLong(
						1,
						StringUtil.getLongValue(fieldsTmpList.get(0).get(
								"histime")));
				sqlList.add(psql.getSQL());
			}
		}
		// 添加最新的
		String insertNewHis = "insert into tab_acc_historypwd values(?,?,?)";
		psql = new PrepareSQL(insertNewHis);
		psql.setLong(1, accOid);
		psql.setString(2, newPwd);
		psql.setLong(3, System.currentTimeMillis());
		sqlList.add(psql.getSQL());
		DataSetBean.doBatch(sqlList.toArray());
		return isExist;
	}

	/**
	 * 校验修改密码是否符合要求
	 * 
	 * @param userID
	 *            创建者的帐号ID
	 * @param pwdStr
	 *            修改后的密码
	 * @return true：允许修改，false：不允许修改
	 */
	private boolean checkAllowModify(long userId, String pwdStr) {
		boolean result = true;
		String sql = "select passwordminlenth from tab_accounts where acc_oid ="
				+ userId;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		HashMap user = DataSetBean.getRecord(sql);
		if (null != user) {
			String pwdLength = (String) user.get("passwordminlenth");
			result = checkPwdLength(pwdLength, pwdStr);
		}
		return result;
	}

	/**
	 * 校验密码有没有过短
	 * 
	 * @param pwdLength
	 *            要求的密码最短长度
	 * @param pwdStr
	 *            密码
	 * @return true:密码符合要求，false:密码不符合要求
	 */
	private boolean checkPwdLength(String pwdLength, String pwdStr) {
		boolean result = true;
		int pwdLength9 = 9;
		int pwdLength8 = 8;
		if (LipossGlobals.isAhDx()) {
			if (pwdLength == null || pwdLength.length() < pwdLength9) {
				pwdLength = "9";
			}
		} else if (LipossGlobals.isCqDx()) {
			if (pwdLength == null || pwdLength.length() < pwdLength8) {
				pwdLength = "8";
			}
		}
		else if (LipossGlobals.isHnLt()) {
			if (pwdLength == null || pwdLength.length() < pwdLength8) {
				pwdLength = "8";
			}
		}
		if (!"".equals(pwdLength)) {
			int minPwdLength = Integer.parseInt(pwdLength);
			if (null != pwdStr && pwdStr.length() < minPwdLength) {
				result = false;
			}
		}
		return result;
	}

	/** 密码的复杂度校验：大写字母、小写字母、数字、特殊字符（包括：!@#$%^&*_），至少需满足三种 */
	public static boolean checkStrongValid(String newPwd) {
		
		// HBLT-RMS-20200420-LH-001河北联通服务器弱口令整改需求
		// HBLT-RMS-20200605-LH-001服务器弱口令整改需求  需要将 特殊字符加入 注释河北判断，走统一逻辑。
		/*if ("hb_lt".equals(Global.instAreaShortName)) {
			validpattrns = new String[] { ".*[\\d].*", ".*[a-z].*", ".*[A-Z].*"};
		}*/
		boolean res = true;
		int patSize = 0;
		for (String regStr : validpattrns) {
			if (Pattern.matches(regStr, newPwd)) {
				patSize++;
			}
		}
		int validsize =3;
		if(LipossGlobals.isJlLt()){
			validsize=4;
		}
		if (patSize < validsize) {
			res = false;
		}
		return res;
	}

	/** 不得包含键盘上任意连续的四个字符 */
	public static boolean checkKeyBoardValid(String newPwd) {
		boolean res = true;
		List<String> keyBoardStr = new ArrayList<String>();
		String[] keyBoardLine = new String[] { "qwertyuiop[]\\", "asdfghjkl;'",
				"zxcvbnm,./", "`1234567890-=" };

		// 先将pwd转换成小写,进行字符转换预检，比如!转变为1进行检测，包括!1,@2,#3,$4{[,}]……
		newPwd = newPwd.toLowerCase().replaceAll("~", "`")
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
		String reversePwd = new StringBuffer(newPwd).reverse().toString();

		int keyBoardLineLess = 3;
		
		// 键盘横向校验格式
		for (int i = 0; i < keyBoardLine.length; i++) {
			char[] a = keyBoardLine[i].toCharArray();
			for (int j = 0; j < a.length - keyBoardLineLess; j++) {
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
			if (newPwd.indexOf(string) != -1) {
				return false;
			}
			// 反向校验
			if (reversePwd.indexOf(string) != -1) {
				return false;
			}
		}

		return res;
	}

	/**  密码中不能包含用户名的形似变化 */
	public static boolean checkLikeNameValid(String newPwd, String username) {
		// 形似变化字符数组
		final String[] validlikename = new String[] { "a|@", "f|t", "J|L",
				"m|n", "U|V", "p|q", "P|R", "c|C|\\(", "s|S|\\$", "0|o|O|Q",
				"1|l|i|!", "b|d|6", "2|z|Z", "g|y|9" };
		// 替换后字符
		final String[] replacename = new String[] { "a", "f", "J", "m", "U",
				"p", "P", "c", "s", "0", "1", "b", "2", "g" };

		boolean res = true;
		int i = 0;
		// 替换所有形变字符
		for (String regStr : validlikename) {
			username = username.replaceAll(regStr, replacename[i]);
			newPwd = newPwd.replaceAll(regStr, replacename[i]);
			i++;
		}
		// 判断替换字符后的密码是否包含替换字符后的用户名
		if (newPwd.indexOf(username) != -1) {
			res = false;
		}
		return res;
	}

	protected static boolean checkChinaCharacter(String str) {
		boolean res = true;
		str = str.replaceAll("\\|", "");
		char[] strChars = str.toCharArray();
		for (char c : strChars) {
			if (isChineseByBlock(c) || isChinesePuctuation(c)) {
				res = false;
			}
		}
		// if (Pattern.matches(PASSWORD_PATTRN, str)) {
		// res = false;
		// }
		return res;
	}

	/**
	 * 内蒙古电信，要求密码不能包含省市名称
	 * @param str
	 * @return
	 */
	protected static boolean checkProvinceCharacter(String str) {
		boolean res = true;
		str = str.toLowerCase();
		for (String province : InvalidProvencekeyboard) {
			if (str.contains(province)) {
				res = false;
			}
		}	
		return res;
	}
	
		/** 不得包含键盘上任意连续的两个字符*/
		public static boolean checkKeyBoardValid4NMG(String newPwd) {
			boolean res = true;
			List<String> keyBoardStr = new ArrayList<String>();
			String[] keyBoardLine = new String[] { "qwertyuiop[]\\", "asdfghjkl;'",
					"zxcvbnm,./", "`1234567890-=", "abcdefghigklmnopqrstuvwxyz"};

			// 先将pwd转换成小写,进行字符转换预检，比如!转变为1进行检测，包括!1,@2,#3,$4{[,}]……
			newPwd = newPwd.toLowerCase().replaceAll("~", "`")
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
			String reversePwd = new StringBuffer(newPwd).reverse().toString();

			// 键盘横向校验格式
			for (int i = 0; i < keyBoardLine.length; i++) {
				char[] a = keyBoardLine[i].toCharArray();
				for (int j = 0; j < a.length - 1; j++) {
					String x = String.valueOf(a[j]) + String.valueOf(a[j + 1]);
					keyBoardStr.add(x);
				}
			}
			// 键盘纵向校验格式
			for (String regStr : validkeyboard) {
				keyBoardStr.add(regStr);
			}
			for (String string : keyBoardStr) {
				// 正向校验
				if (newPwd.indexOf(string) != -1) {
					return false;
				}
				// 反向校验
				if (reversePwd.indexOf(string) != -1) {
					return false;
				}
			}

			return res;
		}
		
		/**
		 * 内蒙古电信，不能出现同一字符排序连续出现两次以上
		 * @param str
		 * @return
		 */
		protected static boolean contChars (String str) {
			boolean res = true;  
			String pw = str.toLowerCase();
			for(int i=0;i<pw.length()-1;i++)
			{
				//取前一位字符
				char befChar = pw.charAt(i);
				//取后一位字符
				char aftChar = pw.charAt(i+1);  
				if(befChar == aftChar)
				{
					res = false;
					return res;
				}
			}
			return res;
		}	
	/**
	 * 根据上一界面提交参数处理用户数据
	 * 
	 * @param request
	 * @return 处理的消息
	 * @modify 2006-6-30 wangfeng
	 */
	public String userInfoDoAct(HttpServletRequest request) {
		String model = LipossGlobals.getLipossProperty("SetupModel");
		String instArea = Global.instAreaShortName;
		
		List<String> strSql = new ArrayList<String>();
		String strMsg = "";
		String strAction = request.getParameter("action");
		String accOid = "";
		HttpSession session = request.getSession();
		// String creator = (String)session.getAttribute("username");
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		String creator = String.valueOf(user.getId());
		long userId = user.getId();

		String matche = "[0-9]+";
		// 删除操作
		if (strAction.equals(this.DELETE)) { 
			accOid = request.getParameter("acc_oid");

			strSql.add("delete from tab_persons where per_acc_oid=" + accOid);
			// _sql.append("delete from tab_persons where per_accOid=" +
			// accOid);
			strSql.add("delete from tab_accounts where acc_oid=" + accOid);
			strSql.add("delete from tab_acc_role where acc_oid=" + accOid);
			strSql.add("delete from tab_acc_group where acc_oid=" + accOid);
			strSql.add("delete from tab_acc_area where acc_oid=" + accOid);
			strSql.add("update tab_role set acc_oid=" + creator
					+ " where acc_oid=" + accOid);

		} else if (strAction.equals(this.MODIFY_PWD)) {
			// 修改密码操作
			ArrayList invaliDateList = getInvalidatePwd();
			accOid = request.getParameter("acc_oid");
			
			if(LipossGlobals.isCqDx()) {
				// 为防止页面直接拼接accOid, 导致越权修改密码
				accOid = user.getAccount();
			}
			String oldPwd = request.getParameter("old_pwd");
			String newPwd = request.getParameter("new_pwd");
			m_logger.warn("newPwd:{},old_pwd:{}",newPwd,oldPwd);
			/*old_pwd = decode(old_pwd);
			newPwd = decode(newPwd);*/
			m_logger.warn("newPwd:{},old_pwd:{}",newPwd,oldPwd);
			if (!checkChinaCharacter(newPwd)) {
				strSql.clear();
				strMsg = "密码内容不能包含中文字符";
			}else if( LipossGlobals.isNmgDx() && !checkProvinceCharacter(newPwd))
			{
				strSql.clear();
				strMsg = "密码不能包含省市地域名称";
			}else if( LipossGlobals.isNmgDx() && !checkKeyBoardValid4NMG(newPwd))
			{
				strSql.clear();
					strMsg = "密码不能包含键盘及数字字母排序超过两位";
			}else if( LipossGlobals.isNmgDx() && !contChars(newPwd))
			{
				strSql.clear();
					strMsg = "密码不能出现同一字符排序连续出现两次以上";
			}else if (!checkStrongValid(newPwd)) {
				strSql.clear();
				strMsg = "密码必须含大小写字母、数字、特殊字符四类字符中的三类。";
				if (LipossGlobals.isHbLt()){
					strMsg = "密码必须含大小写字母、数字。";
				}
				if (LipossGlobals.isJlLt()){
					strMsg = "密码必须含大小写字母、数字、特殊字符四类字符。";
				}
				// 判断密码中是否出现了用户名
			} else if (!checkKeyBoardValid(newPwd)) {
				strSql.clear();
				strMsg = "密码中不得包含键盘上任意连续的四个字符，请重新设置";
				// 判断密码中是否出现了用户名
			} else if ((newPwd.toUpperCase()).indexOf(accOid.toUpperCase()) != -1) {
				strSql.clear();
				strMsg = "您设置的密码中出现了用户名，请重新设置";
			} else if (!checkLikeNameValid(newPwd, accOid)) {
				strSql.clear();
				strMsg = "您设置的密码中包含用户名的形似变化，请重新设置";
			} else if (!checkAllowModify(userId, newPwd)) {
				strSql.clear();
				strMsg = "您设置的密码长度太短了，请重新设置";
			} else if (newPwd.matches(matche)) {
				strSql.clear();
				strMsg = "您设置的密码太过单一，请重新设置";
			} else if (invaliDateList.contains(newPwd)) {
				strSql.clear();
				strMsg = "您设置的密码太过简单，请重新设置";
			} else {
				// NXDX-REQ-ITMS-20190617-LX-001(对数据库里的用户密码进行加密保存) 
				if (LipossGlobals.isNxDx() || LipossGlobals.isAhDx() || LipossGlobals.isHnLt() ) {
					oldPwd = MD5.getMD5(Base64.encode(oldPwd));
					newPwd = MD5.getMD5(Base64.encode(newPwd));
				}
				Map fields = DataSetBean
						.getRecord("select * from tab_accounts where acc_loginname='"
								+ accOid
								+ "' and acc_password='"
								+ oldPwd
								+ "'");
				// 判断旧密码是否正确
				if (fields != null) {
					// 判断和历史5个密码是否有重复
					if (checkPasswordExisted(accOid, newPwd)) {
						Date now = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						String tmpNow = formatter.format(now);
						strSql.add("update tab_accounts set acc_password='"
								+ newPwd + "',acc_pwd_time='" + tmpNow
								+ "' where acc_loginname='" + accOid
								+ "' and acc_password='" + oldPwd + "'");

					} else {
						strSql.clear();
						strMsg = "该密码最近几次设置过，请重新设置！";
					}
				} else {
					strSql.clear();
					strMsg = "旧密码不正确，修改失败";
				}
			}
		} else {
			// 增加了是否唯一登录(is_unique)字段
			String isUnique = request.getParameter("isUnique") == null ? "0"
					: request.getParameter("isUnique");
			String accLoginname = request.getParameter("acc_loginname");
			String accPassword = request.getParameter("acc_password");
			String perName = request.getParameter("per_name");
			// String per_identificationnumber =
			// request.getParameter("per_identificationnumber");
			// modify by lizhaojun 2006-12-08
			String perCity = "";
			if (LipossGlobals.isCqDx()) {
				perCity = request.getParameter("cityIds");
			} else {
				perCity = request.getParameter("per_city");
			}
			// String cityIds = request.getParameter("cityIds");
			// String per_category = request.getParameter("per_category");
			// String per_allowassignment =
			// request.getParameter("per_allowassignment");
			// String per_allowemail = request.getParameter("per_allowemail");
			String perGender = request.getParameter("per_gender");
			String perEmail = request.getParameter("per_email");
			if (perEmail != null) {
				perEmail = perEmail.trim();
			}
			String perTitle = request.getParameter("per_title");
			String perDepOid = request.getParameter("per_dep_oid");
			String perJobtitle = request.getParameter("per_jobtitle");
			String perPhone = request.getParameter("per_phone");
			if (perPhone != null) {
				perPhone = perPhone.trim();
			}
			String perMobile = request.getParameter("per_mobile");
			if (perMobile != null) {
				perMobile = perMobile.trim();
			}
			String perLastname = request.getParameter("per_lastname");
			String perBirthdate = request.getParameter("per_birthdate");
			String perSearchcode = request.getParameter("per_searchcode");
			String perRemark = request.getParameter("per_remark");
			String roleOids = request.getParameter("gr_oid");
			String groupOids = request.getParameter("gg_oid");
			String areaId = request.getParameter("area_id");
			String customerid = request.getParameter("customerid");
			String loginError = request.getParameter("login_error");
			String lockTime = request.getParameter("lock_time");
			String loginFree = request.getParameter("login_free");
			String minPasswdlength = request.getParameter("min_passwdlength");
			String userIps = request.getParameter("user_ips");
			String accValidatemonth = request
					.getParameter("acc_validatemonth");

			// 帐号有效期
			if ("".equals(accValidatemonth)) {
				accValidatemonth = null;
			}

			// 允许用户登录失败的次数
			if ("".equals(loginError)) {
				loginError = null;
			}

			// 用户登录失败锁定的时间
			if ("".equals(lockTime)) {
				lockTime = null;
			}

			// 允许用户的最大可空闲的时间
			if ("".equals(loginFree)) {
				loginFree = null;
			}

			// 用户密码长度
			if ("".equals(minPasswdlength)) {
				minPasswdlength = null;
			}

			// 用户登录IP范围
			if ("".equals(userIps)) {
				userIps = null;
			} else {
				userIps = "'" + userIps + "'";
			}

			// 生日
			if (!perBirthdate.equals("")) {
				perBirthdate = "'" + perBirthdate + "'";
			} else {
				perBirthdate = null;
			}
			
			String modelVipColumn = "model_vip";

			if (strAction.equals(this.ADD)) {
				// 增加操作
				ArrayList invaliDateList = getInvalidatePwd();
				Object pwdLength = user.getMinPwdLength();
				// 判断密码中是否出现了用户名
				if ((accPassword.toUpperCase()).indexOf(accLoginname
						.toUpperCase()) != -1) {
					strSql.clear();
					strMsg = "您设置的密码中出现了用户名，请重新设置";
				} else if (!checkChinaCharacter(accPassword)) {
					strSql.clear();
					strMsg = "密码内容不能包含中文字符";
				} else if( LipossGlobals.isNmgDx() && !checkProvinceCharacter(accPassword))
				{
					strSql.clear();
					strMsg = "密码不能包含省市地域名称";
				}else if( LipossGlobals.isNmgDx() && !contChars(accPassword))
				{
						strSql.clear();
						strMsg = "密码不能出现同一字符排序连续出现两次以上";
				}else if( LipossGlobals.isNmgDx() && !checkKeyBoardValid4NMG(accPassword))
				{
						strSql.clear();
						strMsg = "密码不能包含键盘及数字字母排序超过两位";    
				}else if (!checkStrongValid(accPassword)) {
					strSql.clear();
					strMsg = "密码必须含大小写字母、数字、特殊字符四类字符中的三类。";
					// HBLT-RMS-20200605-LH-001服务器弱口令整改需求  需要将 特殊字符加入 注释河北判断，走统一逻辑。
					/*if ("hb_lt".equals(Global.instAreaShortName)){
						strMsg = "密码必须含大小写字母、数字。";
					}*/
					if (LipossGlobals.isJlLt()){
						strMsg = "密码必须含大小写字母、数字、特殊字符四类字符。";
					}
					// 判断密码中是否出现了用户名
				} else if (!checkKeyBoardValid(accPassword)) {
					strSql.clear();
					strMsg = "密码中不得包含键盘上任意连续的四个字符，请重新设置";
				} else if (!checkLikeNameValid(accPassword, accLoginname)) {
					strSql.clear();
					strMsg = "密码中不能包含用户名的形似变化，请重新设置";
				} else if (!checkPwdLength(minPasswdlength, accPassword)) {
					strSql.clear();
					strMsg = "你设置的密码长度太短了，请重新设置";
				} else if (invaliDateList.contains(accPassword)) {
					strSql.clear();
					strMsg = "你设置的密码太过简单，请重新设置";
				} else {
					
					// NXDX-REQ-ITMS-20190617-LX-001(对数据库里的用户密码进行加密保存) 
					if (LipossGlobals.isNxDx() || LipossGlobals.isAhDx() || LipossGlobals.isHnLt()) {
						accPassword = MD5.getMD5(Base64.encode(accPassword));
					}
					else {
						// 密码存在英文单引号，则增加oralce单引号转义
						accPassword = accPassword.replaceAll("'", "''");
					}
					// 查询此acc_loginname是否已入库
					Map fields = DataSetBean
							.getRecord("select * from tab_accounts where acc_loginname='"
									+ accLoginname + "'");
					if (fields != null) {
						strSql.clear();
						strMsg = "数据库已存在此记录，请返回重试！";
					} else {
						long maxAccOid = DataSetBean.getMaxId("tab_accounts",
								"acc_oid");

						// 获得当前时间，将密码创建时间入库。
						Date now = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						String tmpNow = formatter.format(now);
						DateTimeUtil createPasswdDateUtil = new DateTimeUtil(tmpNow);
						String tmpFourMonth = createPasswdDateUtil.getNextDateTime("month", -4);
						strSql.add("insert into tab_accounts (acc_oid,acc_loginname,acc_password,creator,creationdate,acc_pwd_time,acc_second_pwd,loginmaxtrynum,lockedtime,maxidletimebeforelogout,passwordminlenth,acc_login_ip,acc_validatemonth,is_unique) values ("
								+ maxAccOid
								+ ",'"
								+ accLoginname
								+ "','"
								+ accPassword
								+ "',"
								+ creator
								+ " , '"
								+ tmpNow
								+ "','"
								+ tmpFourMonth
								+ "','"
								+ accPassword
								+ "',"
								+ loginError
								+ ","
								+ lockTime
								+ ","
								+ loginFree
								+ ","
								+ minPasswdlength
								+ ","
								+ userIps
								+ ","
								+ accValidatemonth + ",'" + isUnique + "')");// 增加了是否唯一登录（is_unique）字段
						strSql.add("insert into tab_acc_historypwd values("
								+ maxAccOid + ",'" + accPassword + "',"
								+ System.currentTimeMillis() + ")");
						if (model == null) {
							strSql.add("insert into tab_persons (per_acc_oid,per_searchcode,"
									+ "per_name,per_lastname,per_gender,per_title,per_jobtitle,per_phone,per_mobile,per_email,per_dep_oid,per_remark,per_birthdate,per_city) values ("
									+ maxAccOid
									+ ",'"
									+ perSearchcode
									+ "','"
									+ perName
									+ "','"
									+ perLastname
									+ "','"
									+ perGender
									+ "','"
									+ perTitle
									+ "','"
									+ perJobtitle
									+ "','"
									+ perPhone
									+ "','"
									+ perMobile
									+ "','"
									+ perEmail
									+ "',"
									+ perDepOid
									+ ",'"
									+ perRemark
									+ "',"
									+ perBirthdate
									+ ",'"
									+ perCity
									+ "')");
						} else if (model.compareTo(modelVipColumn) == 0) {
							strSql.add("insert into tab_persons (per_acc_oid,per_searchcode,"
									+ "per_name,per_lastname,per_gender,per_title,per_jobtitle,per_phone,per_mobile,per_email,per_dep_oid,per_remark,per_birthdate,per_city,per_customid) values ("
									+ maxAccOid
									+ ",'"
									+ perSearchcode
									+ "','"
									+ perName
									+ "','"
									+ perLastname
									+ "','"
									+ perGender
									+ "','"
									+ perTitle
									+ "','"
									+ perJobtitle
									+ "','"
									+ perPhone
									+ "','"
									+ perMobile
									+ "','"
									+ perEmail
									+ "',"
									+ perDepOid
									+ ",'"
									+ perRemark
									+ "',"
									+ perBirthdate
									+ ",'"
									+ perCity
									+ "','" + customerid + "')");
						}
						strSql.add("insert into tab_acc_area (area_id,acc_oid) values ("
								+ areaId + "," + maxAccOid + ")");
						for (String sql : strSql) {
							sql = StringUtils.replace(sql, ",,", ",null,");
							sql = StringUtils.replace(sql, ",,", ",null,");
							sql = StringUtils.replace(sql, ",)", ",null)");
						}

						accOid = String.valueOf(maxAccOid);
					}
				}
				invaliDateList = null;
			} else {
				accOid = request.getParameter("acc_oid");
				if (model == null) {
					strSql.add("update tab_persons set per_searchcode='"
							+ perSearchcode + "',per_name='" + perName
							+ "',per_lastname='" + perLastname
							+ "',per_gender='" + perGender + "',per_title='"
							+ perTitle + "',per_jobtitle='" + perJobtitle
							+ "',per_phone='" + perPhone + "',per_mobile='"
							+ perMobile + "',per_email='" + perEmail
							+ "',per_dep_oid=" + perDepOid + ",per_remark='"
							+ perRemark + "',per_birthdate=" + perBirthdate
							+ ",per_city='" + perCity + "' where per_acc_oid="
							+ accOid);
				}
				// begin modified by w5221 家庭网关中不考虑大客户
				/*
				 * else if (model.compareTo("model_vip") == 0) { strSql =
				 * "update tab_persons set per_searchcode='" + per_searchcode +
				 * "',per_name='" + per_name + "',per_lastname='" + per_lastname
				 * + "',per_gender='" + per_gender + "',per_title='" + per_title
				 * + "',per_jobtitle='" + per_jobtitle + "',per_phone='" +
				 * per_phone + "',per_mobile='" + per_mobile + "',per_email='" +
				 * per_email + "',per_dep_oid=" + per_dep_oid + ",per_remark='"
				 * + per_remark + "',per_birthdate=" + per_birthdate +
				 * ",per_city='" + per_city + "',per_customid='" + customerid +
				 * "' where per_accOid=" + accOid; }
				 */
				strSql.add("update tab_accounts set loginmaxtrynum="
						+ loginError + ",lockedtime=" + lockTime
						+ ", is_unique='" + isUnique + "'"
						+ ",maxidletimebeforelogout=" + loginFree
						+ ",passwordminlenth=" + minPasswdlength
						+ ",acc_login_ip=" + userIps + ",acc_validatemonth="
						+ accValidatemonth + " where acc_oid=" + accOid);
				// end modified by w5221 家庭网关中不考虑大客户
				strSql.add("delete from tab_acc_area where acc_oid=" + accOid);
				strSql.add("insert into tab_acc_area (area_id,acc_oid) values ("
						+ areaId + "," + accOid + ")");

				for (String sql : strSql) {
					sql = StringUtils.replace(sql, "=,", "=null,");
					sql = StringUtils.replace(sql, "= where", "=null where");
				}
			}

			if (strSql.size()>0) {
				strSql.add("delete from tab_acc_role where acc_oid=" + accOid);
				strSql.add("delete from tab_acc_group where acc_oid=" + accOid);
				String[] arrRoleOid = roleOids.split(",");
				for (int i = 0; i < arrRoleOid.length; i++) {
					strSql.add("insert into tab_acc_role (acc_oid,role_id) values ("
							+ accOid + "," + arrRoleOid[i] + ")");
				}
				String[] arrGroupOid = groupOids.split(",");
				for (int i = 0; i < arrGroupOid.length; i++) {
					strSql.add("insert into tab_acc_group (acc_oid,group_oid) values ("
							+ accOid + "," + arrGroupOid[i] + ")");
				}
			}

		}

		// PrepareSQL psql = new PrepareSQL(strSql);
		// psql.getSQL();
		if (strSql.size() > 0) {
			int iCode[] = DataSetBean.doBatchList(strSql);
			if (iCode != null && iCode.length > 0) {
				strMsg = "操作成功！";
				try {
					session.removeAttribute("passwordexpire");
				} catch (Exception e) {
				}
			} else if (StringUtil.IsEmpty(strMsg)) {
				strMsg = "操作失败，请返回重试或稍后再试！";
			}
		}

		return strMsg;
	}

	/**
	 * 根据上一界面提交参数处理用户数据
	 * 
	 * @param request
	 * @return 处理的消息
	 * @modify 2006-6-30 wangfeng
	 */
	public String modifyPwdOutOfSys(HttpServletRequest request) {
		String strSql = null;
		String strMsg = "";
		String accOidColumn = "acc_oid";
		String strAction = request.getParameter("action");
		String accOid = request.getParameter(accOidColumn);
		PrepareSQL psql = new PrepareSQL();
		// String creator = (String)session.getAttribute("username");

		String sql = "select * from tab_accounts where acc_loginname= ? ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select acc_oid from tab_accounts where acc_loginname= ? ";
		}
		psql.setSQL(sql);
		psql.setString(1, accOid);
		Map user = DBOperation.getRecord(psql.getSQL());

		if (null == user || StringUtil.IsEmpty((String) user.get(accOidColumn))) {
			strSql = "";
			strMsg = "用户'" + accOid + "'不存在！";
			return strMsg;
		}
		long userId = StringUtil.getLongValue(user.get("acc_oid"));
		// 修改密码操作
		ArrayList invaliDateList = getInvalidatePwd();
		String oldPwd = request.getParameter("old_pwd");
		String newPwd = request.getParameter("new_pwd");

		String matche = "[0-9]+";
		
		if (!checkChinaCharacter(newPwd)) {
			strSql = "";
			strMsg = "密码内容不能包含中文字符";
		} else if (!checkStrongValid(newPwd)) {
			strSql = "";
			strMsg = "密码必须含大小写字母、数字、特殊字符四类字符中的三类。";
			if (LipossGlobals.isJlLt()){
				strMsg = "密码必须含大小写字母、数字、特殊字符四类字符。";
			}
			// 判断密码中是否出现了用户名
		} else if (!checkKeyBoardValid(newPwd)) {
			strSql = "";
			strMsg = "密码中不得包含键盘上任意连续的四个字符，请重新设置";
			// 判断密码中是否出现了用户名
		} else if (newPwd.indexOf(accOid) != -1) {
			strSql = "";
			strMsg = "您设置的密码中出现了用户名，请重新设置";
		} else if (!checkLikeNameValid(newPwd, accOid)) {
			strSql = "";
			strMsg = "密码中不能包含用户名的形似变化，请重新设置";
		} else if (!checkAllowModify(userId, newPwd)) {
			strSql = "";
			strMsg = "您设置的密码长度太短了，请重新设置";
		} else if (newPwd.matches(matche)) {
			strSql = "";
			strMsg = "您设置的密码太过单一，请重新设置";
		} else if (invaliDateList.contains(newPwd)) {
			strSql = "";
			strMsg = "您设置的密码太过简单，请重新设置";
		} else {
			// NXDX-REQ-ITMS-20190617-LX-001(对数据库里的用户密码进行加密保存) 
			if (LipossGlobals.isNxDx() || LipossGlobals.isAhDx() || LipossGlobals.isHbLt()) {
				oldPwd = MD5.getMD5(Base64.encode(oldPwd));
				newPwd = MD5.getMD5(Base64.encode(newPwd));
			}

			strSql = "select * from tab_accounts where acc_loginname='"
					+ accOid + "' and acc_password='" + oldPwd + "'";
			Map fields = DataSetBean.getRecord(strSql);
			// 判断旧密码是否正确
			if (fields != null) {
				// 判断和历史5个密码是否有重复
				if (checkPasswordExistedStronger(accOid, newPwd)) {
					Date now = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String tmpNow = formatter.format(now);
					strSql = "update tab_accounts set acc_password='" + newPwd
							+ "',acc_pwd_time='" + tmpNow
							+ "' where acc_loginname='" + accOid
							+ "' and acc_password='" + oldPwd + "'";

				} else {
					strSql = "";
					strMsg = "该密码最近几次设置过，请重新设置！";
				}
			} else {
				strSql = "";
				strMsg = "旧密码不正确，修改失败";
			}
		}

		psql.getSQL();
		if (!strSql.equals("")) {
			int iCode[] = DataSetBean.doBatch(strSql);
			if (iCode != null && iCode.length > 0) {
				strMsg = "操作成功！";
				HttpSession session = request.getSession();
				try {
					session.removeAttribute("passwordexpire");
					
					//AHDX_ITMS-REQ-20190924YQW-001（web用户登录改造）密码修改成功后，移除锁定map
					if(LipossGlobals.isAhDx()){
						UserMap.getInstance().deleteLockUserMap(accOid);
					}
				} catch (Exception e) {
				}
			} else {
				strMsg = "操作失败，请返回重试或稍后再试！";
			}
		}

		return strMsg;
	}

	/**
	 * 装载表tab_common_passwd中的非法密码
	 * 
	 * @return
	 */
	private ArrayList getInvalidatePwd() {
		ArrayList list = new ArrayList();
		String pwd = "";
		String sql = "select distinct(password) password from tab_common_passwd";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		while (null != fields) {
			pwd = (String) fields.get("password");
			// pwd都为空的情况
			if (!"".equals(pwd)) {
				list.add(pwd);
			}

			fields = cursor.getNext();
		}

		fields = null;
		cursor = null;
		return list;
	}
	
	
		/** 中文汉字 */
		private static boolean isChineseByBlock(char c) {
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
			if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
					|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
					|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT) {
				return true;
			}
			return false;
		}

		/** 中文字符 */
		private static boolean isChinesePuctuation(char c) {
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
			if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
					|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
					|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
					|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS) {
				return true;
			}
			return false;
		}

	public static String decode(String str) {
		byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
				60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
				10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
				-1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
				38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
				-1, -1 };

		byte[] data = str.getBytes();
		int len = data.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
		int i = 0;
		int b1, b2, b3, b4;

		while (i < len) {
			do {
				b1 = base64DecodeChars[data[i++]];
			} while (i < len && b1 == -1);
			if (b1 == -1) {
				break;
			}

			do {
				b2 = base64DecodeChars[data[i++]];
			} while (i < len && b2 == -1);
			if (b2 == -1) {
				break;
			}
			buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

			do {
				b3 = data[i++];
				if (b3 == 61) {
					return new String(buf.toByteArray());
				}
				b3 = base64DecodeChars[b3];
			} while (i < len && b3 == -1);
			if (b3 == -1) {
				break;
			}
			buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

			do {
				b4 = data[i++];
				if (b4 == 61) {
					return new String(buf.toByteArray());
				}
				b4 = base64DecodeChars[b4];
			} while (i < len && b4 == -1);
			if (b4 == -1) {
				break;
			}
			buf.write((int) (((b3 & 0x03) << 6) | b4));
		}
		return new String(buf.toByteArray());
	}


	
	public static void main(String[] args) {
		// now
		DateTimeUtil nowDateUtil = new DateTimeUtil();
		// password
		DateTimeUtil createPasswdDateUtil = new DateTimeUtil("2020-01-05 21:19:52");
		// create
		// time
		createPasswdDateUtil.getNextMonth(Integer.parseInt("3"));
		int nDay = (int) ((createPasswdDateUtil.getLongTime() - nowDateUtil
				.getLongTime()) / (24 * 60 * 60));
		System.out.println(nDay);
	}
	
}
