/*
 * @(#)DbUserManager.java	1.00 1/20/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.system.dbimpl;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
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
 * 山东单独出来一个针对页面修改密码的情况
 *
 * created by lingmin on 2020/5/11
 *
 */
public class DbUserManagerSD implements UserManager {
	private static Logger m_logger = LoggerFactory
			.getLogger(DbUserManagerSD.class);

	// 用于验证：密码中必须含大小写字母、数字、特殊字符四类字符中的三类。
	private static String validpattrns[] = new String[] { ".*[\\d].*",
			".*[a-z].*", ".*[A-Z].*",
			".*[`~!@#$^&*()_=%|{}':;',\\[\\].<>/?\\\\].*" };

	// 用于验证：密码不得包含键盘上任意连续的四个字符。
	private static String validkeyboard[] = new String[] { "1qaz", "2wsx",
			"3edc", "4rfv", "5tgb", "6yhn", "7ujm", "8ik,", "9ol.", "0p;/" };

	/**
	 * 根据上一界面提交参数处理用户数据
	 *
	 * @param request
	 * @return 处理的消息
	 * @modify 2006-6-30 wangfeng
	 */
	public String userInfoDoAct(HttpServletRequest request) {
		//做CSRF攻击排除
		if(!validCSRF(request)){
			return "非法请求！";
		}

		String strMsg = "";
		String strAction = request.getParameter("action");
		String acc_oid = "";
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long user_id = user.getId();
		String sqlStr = "";
		if (strAction.equals("modifypwd")) {
			// 修改密码操作
			ArrayList InvalidateList = getInvalidatePwd();
			acc_oid = request.getParameter("acc_oid");
			String old_pwd = request.getParameter("old_pwd");
			String new_pwd = request.getParameter("new_pwd");
			m_logger.warn("new_pwd:{},old_pwd:{}",new_pwd,old_pwd);
			old_pwd = decode(old_pwd);
			new_pwd = decode(new_pwd);
			m_logger.warn("new_pwd:{},old_pwd:{}",new_pwd,old_pwd);
			if (!checkChinaCharacter(new_pwd)) {
				strMsg = "密码内容不能包含中文字符";
			}else if (!checkStrongValid(new_pwd)) {
				strMsg = "密码必须含大小写字母、数字、特殊字符四类字符中的三类。";
				if (Global.HBLT.equals(Global.instAreaShortName)){
					strMsg = "密码必须含大小写字母、数字。";
				}
			} else if ((new_pwd.toUpperCase()).indexOf(acc_oid.toUpperCase()) != -1) {// 判断密码中是否出现了用户名
				strMsg = "您设置的密码中出现了用户名，请重新设置";
			} else if (!checkAllowModify(user_id, new_pwd)) {
				strMsg = "您设置的密码长度太短了，请重新设置";
			} else if (new_pwd.matches("[0-9]+")) {
				strMsg = "您设置的密码太过单一，请重新设置";
			} else if (InvalidateList.contains(new_pwd)) {
				strMsg = "您设置的密码太过简单，请重新设置";
			} else if (!checkPasswordExisted(acc_oid, new_pwd)){
				strMsg = "您设置的密码和前两次有重复，请重新设置！";
			}else {
				Map fields = DataSetBean
						.getRecord("select * from tab_accounts where acc_loginname='"
								+ acc_oid
								+ "' and acc_password='"
								+ old_pwd
								+ "'");
				// 判断旧密码是否正确
				if (fields != null) {
					Date now = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String tmp_now = formatter.format(now);
					sqlStr = "update tab_accounts set acc_password='"
							+ new_pwd + "',acc_pwd_time='" + tmp_now
							+ "' where acc_loginname='" + acc_oid
							+ "' and acc_password='" + old_pwd + "'";
				} else {
					strMsg = "旧密码不正确，修改失败";
				}
			}
		}
		if (!StringUtil.IsEmpty(sqlStr)) {
			PrepareSQL psql = new PrepareSQL(sqlStr);
			psql.getSQL();
			int[] iCode = DataSetBean.doBatch(sqlStr);
			if (iCode != null && iCode.length > 0) {
				strMsg = "操作成功！";
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

	@Override
	public User createUser(String account, String passwd, long creator, long role_id, long area_id) throws UserAlreadyExistsException {
		return null;
	}

	@Override
	public User createUser(String account, String passwd, long creator, long role_id, long area_id, Map _map) throws UserAlreadyExistsException {
		return null;
	}

	@Override
	public User getUser(long acc_oid) throws UserNotFoundException {
		return null;
	}

	@Override
	public User getUser(String account, long area_id) throws UserNotFoundException {
		return null;
	}

	@Override
	public User getUser(String account, long area_id, boolean cas) throws UserNotFoundException {
		return null;
	}

	@Override
	public void deleteUser(User user) {

	}

	@Override
	public void deleteUser(long acc_oid) {

	}

	@Override
	public Cursor users(int startIndex, int numResults) {
		return null;
	}

	@Override
	public Cursor users(Map _map) {
		return null;
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

	// 中文汉字
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

	// 中文字符
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

	// 密码的复杂度校验：大写字母、小写字母、数字、特殊字符（包括：!@#$%^&*_），至少需满足三种
	public static boolean checkStrongValid(String new_pwd) {

		// HBLT-RMS-20200420-LH-001河北联通服务器弱口令整改需求
		if (Global.HBLT.equals(Global.instAreaShortName)) {
			validpattrns = new String[] { ".*[\\d].*", ".*[a-z].*", ".*[A-Z].*"};
		}
		boolean res = true;
		int patSize = 0;
		for (String regStr : validpattrns) {
			if (Pattern.matches(regStr, new_pwd)) {
				patSize++;
			}
		}
		if (patSize < 3) {
			res = false;
		}
		return res;
	}

	// 不得包含键盘上任意连续的四个字符
	public static boolean checkKeyBoardValid(String new_pwd) {
		boolean res = true;
		List<String> keyBoardStr = new ArrayList<String>();
		String[] keyBoardLine = new String[] { "qwertyuiop[]\\", "asdfghjkl;'",
				"zxcvbnm,./", "`1234567890-=" };

		// 先将pwd转换成小写,进行字符转换预检，比如!转变为1进行检测，包括!1,@2,#3,$4{[,}]……
		new_pwd = new_pwd.toLowerCase().replaceAll("~", "`")
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
		String reverse_Pwd = new StringBuffer(new_pwd).reverse().toString();

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
			if (new_pwd.indexOf(string) != -1) {
				return false;
			}
			// 反向校验
			if (reverse_Pwd.indexOf(string) != -1) {
				return false;
			}
		}

		return res;
	}

	// 密码中不能包含用户名的形似变化
	public static boolean checkLikeNameValid(String new_pwd, String username) {
		// 形似变化字符数组
		final String validlikename[] = new String[] { "a|@", "f|t", "J|L",
				"m|n", "U|V", "p|q", "P|R", "c|C|\\(", "s|S|\\$", "0|o|O|Q",
				"1|l|i|!", "b|d|6", "2|z|Z", "g|y|9" };
		// 替换后字符
		final String replacename[] = new String[] { "a", "f", "J", "m", "U",
				"p", "P", "c", "s", "0", "1", "b", "2", "g" };

		boolean res = true;
		int i = 0;
		// 替换所有形变字符
		for (String regStr : validlikename) {
			username = username.replaceAll(regStr, replacename[i]);
			new_pwd = new_pwd.replaceAll(regStr, replacename[i]);
			i++;
		}
		// 判断替换字符后的密码是否包含替换字符后的用户名
		if (new_pwd.indexOf(username) != -1) {
			res = false;
		}
		return res;
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
	private boolean checkAllowModify(long userID, String pwdStr) {
		boolean result = true;
		String sql = "select passwordminlenth from tab_accounts where acc_oid ="
				+ userID;
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
		if (Global.AHDX.equals(Global.instAreaShortName)) {
			if (pwdLength == null | pwdLength.length() < 9) {
				pwdLength = "9";
			}
		} else if (Global.CQDX.equals(Global.instAreaShortName)) {
			if (pwdLength == null | pwdLength.length() < 8) {
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

	private boolean checkPasswordExisted(String _s1, String _s2) {
		boolean IsExist = true;
		String strSQL_modify = null;
		String strSQL_update = null;
		String acc_oid = _s1;
		String new_pwd = _s2;
		strSQL_modify = "select acc_first_pwd,acc_second_pwd from tab_accounts where acc_loginname ='" + acc_oid + "'";
		PrepareSQL psql = new PrepareSQL(strSQL_modify);
		psql.getSQL();
		Map fields_tmp = DataSetBean.getRecord(strSQL_modify);
		String acc_first_pwd = (String)fields_tmp.get("acc_first_pwd");
		String acc_second_pwd = (String)fields_tmp.get("acc_second_pwd");
		if (acc_first_pwd.equals(new_pwd) || acc_second_pwd.equals(new_pwd)) {
			IsExist = false;
		} else {
			Date now = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String tmp_now = formatter.format(now);
			strSQL_update = "update tab_accounts set acc_first_pwd='" + acc_second_pwd + "' , acc_second_pwd='" + new_pwd + "' , acc_pwd_time='" + tmp_now + "' where acc_loginname ='" + acc_oid + "'";
			psql = new PrepareSQL(strSQL_update);
			psql.getSQL();
			int[] iCode = DataSetBean.doBatch(strSQL_update);
			if (iCode != null && iCode.length > 0) {
				m_logger.warn("密码记录更新成功！");
			} else {
				m_logger.warn("密码记录更新失败！");
			}
		}
		return IsExist;
	}

	/**
	 * 山东漏洞检测 修复密码修改页面CSRF攻击
	 * @param request
	 * @return
	 */
	private boolean validCSRF(HttpServletRequest request)
	{
		m_logger.warn("begin validCSRF:{}",request);
		//1、校验referer
		String validRefererUrl = LipossGlobals.getLipossProperty("validRefererUrl");
		String refererURL = request.getHeader("referer");
		m_logger.warn("validCSRF with validRefererUrl:{},refererURL:{}",validRefererUrl,refererURL);
		if(StringUtil.IsEmpty(refererURL) || refererURL.equals("null")
				|| (!StringUtil.IsEmpty(validRefererUrl) && !refererURL.contains(validRefererUrl))){
			return false;
		}
		//2、校验登录时生成的token值与页面传来的是否一致
		String randTxt = StringUtil.getStringValue(request.getSession().getAttribute("randTxt"));
		String randTxt_form = request.getParameter("randSesion");
		m_logger.warn("validCSRF with randTxt:{},randTxt_form:{}",randTxt,randTxt_form);
		return randTxt.equals(randTxt_form);
	}

}
