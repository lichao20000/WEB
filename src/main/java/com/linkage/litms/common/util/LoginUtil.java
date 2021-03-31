
package com.linkage.litms.common.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;

/**
 * <pre>
 * ITMS WEB报表系统拆分 登录帮组类
 * 用于将用户名密码加解密操作
 * </pre>
 * 
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2014-3-24
 * @category com.linkage.litms.common.util
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class LoginUtil
{

	private static Logger loger = LoggerFactory.getLogger(LoginUtil.class);
	/**
	 * 根据系统参数配置，判断当前系统是否为报表系统
	 */
	private static boolean isReportSystem = "3".equals(LipossGlobals
			.getLipossProperty("ClusterMode.mode"));

	public static String getAccount(HttpServletRequest request)
	{
		String account = null;
		//报表系统或者新web跳转到老web得请求
		if (isReportSystem || (!StringUtil.IsEmpty(request.getQueryString()) && request.getQueryString().contains("requestParam")))
		{
			// 如果是报表系统，首先从业务系统获取登录账号
			String requestParam = request.getParameter("requestParam");
			account = decodeUser(requestParam)[0];
		}
		if (account == null)
		{
			account = request.getParameter("acc_yhm");
		}
		
		//单点登陆的时候，如果已经登陆，
		//在error.jsp设置的用户名为acc_loginname，未避免影响其他选项，增加为空判断
		if (account == null) 
		{
			account = request.getParameter("acc_loginname");
		}
		return account;
	}

	public static String getPassword(HttpServletRequest request)
	{
		String password = null;
		if (isReportSystem || (!StringUtil.IsEmpty(request.getQueryString()) && request.getQueryString().contains("requestParam")))
		{
			// 如果是报表系统，首先从业务系统获取登录账号
			String requestParam = request.getParameter("requestParam");
			password = decodeUser(requestParam)[1];
		}
		if (password == null)
		{
			password = request.getParameter("acc_mm");
		}
		//单点登陆的时候，如果已经登陆，
		//在error.jsp设置的用户名为acc_password，未避免影响其他选项，增加为空判断
		if (password == null)
		{
			password = request.getParameter("acc_password");
		}
		return password;
	}

	public static String getAreaName(HttpServletRequest request)
	{
		String areaName = null;
		if (isReportSystem || (!StringUtil.IsEmpty(request.getQueryString()) && request.getQueryString().contains("requestParam")))
		{
			String requestParam = request.getParameter("requestParam");
			areaName = decodeUser(requestParam)[2];
		}
		if (areaName == null)
		{
			areaName = request.getParameter("area_name");
		}
		return areaName;
	}

	/**
	 * <pre>
	 * 是否需要验证校验码
	 * 如果当前系统是报表系统，并且是业务子系统重定向登录，则不需要校验验证码
	 * </pre>
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isCheckCode(HttpServletRequest request)
	{
		if (isReportSystem || (!StringUtil.IsEmpty(request.getQueryString()) && request.getQueryString().contains("requestParam")))
		{
			String requestParam = request.getParameter("requestParam");
			if (!StringUtil.IsEmpty(requestParam))
			{
				return false;
			}
		}
		return true;
	}

	public static boolean validateCode(HttpServletRequest request)
	{
		if (isCheckCode(request))
		{
			// 获取登录页面参数
			String userCheckCode = request.getParameter("checkCode");
			String account = request.getParameter("acc_yhm");
			// 自动化测试用户万能验证码
			if("itmstest".equals(account)&&"asia".equals(userCheckCode)){
				return true;
			}
			
			String sysCheckCode="";
			String per_mobile="";
			if(Global.NMGDX.equals(Global.instAreaShortName)) {
				per_mobile=request.getAttribute("per_mobile")==null?"":request.getAttribute("per_mobile").toString();
				sysCheckCode = (String) SkinUtils.getSession(request, "checkCode-"+per_mobile);
			}else {
				sysCheckCode = (String) SkinUtils.getSession(request, "checkCode");
			}
			
			if (StringUtil.IsEmpty(userCheckCode) || StringUtil.IsEmpty(sysCheckCode)) {
				loger.warn("userCheckCode[{}] or sysCheckCode[{}] is null", userCheckCode, sysCheckCode);
				return false;
			}
			
			//有效期校验
			if(Global.NMGDX.equals(Global.instAreaShortName)) {
				String ValidTime = (String) SkinUtils.getSession(request, "ValidTime");
				long nowTime =System.currentTimeMillis();
				long ValidTime1 = StringUtil.getLongValue(ValidTime);
				if(nowTime-ValidTime1>1000*60*5){
					SkinUtils.removeSession(request,"checkCode-"+per_mobile);
					loger.warn("验证码超过5分钟有效期");
					return false;
				}
			}
			
			return sysCheckCode.toLowerCase().equals(userCheckCode.toLowerCase());
		}
		else
		{
			return true;
		}
	}

	/**
	 * <pre>
	 * 校验referer请求头是否合法。
	 * 由于重定向到报表子系统是直接访问checkuser.jsp，此时该请求头肯定为null。
	 * 该方法安全性也不是很好，以后需要优化。
	 * <pre>
	 */
	public static boolean validateReferer(HttpServletRequest request)
	{
		if (isReportSystem || (!StringUtil.IsEmpty(request.getQueryString()) && request.getQueryString().contains("requestParam")))
		{
			return true;
		}
		//防止CSRF攻击 addby os_hanzz
		String randTxt = StringUtil.getStringValue(request.getSession().getAttribute("randTxt"));
		String randTxt_form = request.getParameter("randSesion");
		if(!randTxt.equals(randTxt_form)){
			return false;
		}
		String refererURL = request.getHeader("referer");
		return !(refererURL == null || refererURL.equals("null"));
	}

	/**
	 * 将用户名和密码信息加密。 只是使用Base64加密
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public static String encodeUser(String account, String password, String areaName)
	{
		// 由于登录页面，已经将密码进行BASE64加密后传递，所以这个地方也是用BASE64加密，保证一致
		String input = account + "&" + Base64.encode(password) + "&" + areaName;
		if(LipossGlobals.inArea(Global.HBLT)){
			input = account + "&" + MD5.getMD5(Base64.encode(password)) + "&" + areaName;
		}
		return Base64.encode(input);
	}

	public static String encodeUserForNewEdit(String account, String password,
			String areaName) {
	 	// 由于登录页面，已经将密码进行BASE64加密后传递，所以这个地方也是用BASE64加密，保证一致
		String tmp = Base64.encode(password);
		// 页面传送，先base64，然后MD5，这里保持一致
		tmp = MD5.getMD5(tmp);
		String input = account + "&" +tmp + "&" + areaName;
	 	return Base64.encode(input);
	}
	
	
	/**
	 * 将用户名和密码加密信息解密。 result[0]表示用户名 result[1]表示密码 result[2]表示登录域
	 * 
	 * @param encodeInput
	 * @return never return null
	 */
	public static String[] decodeUser(String encodeInput)
	{
		if (StringUtil.IsEmpty(encodeInput))
		{
			return new String[3];
		}
		String input = Base64.decode(encodeInput);
		if (StringUtil.IsEmpty(input))
		{
			return new String[3];
		}
		String[] rts = input.split("&");
		return new String[] { getIndexValue(rts, 0), getIndexValue(rts, 1),
				getIndexValue(rts, 2) };
	}

	public static String getIndexValue(String[] rts, int index)
	{
		if (rts == null)
		{
			return null;
		}
		return rts.length > index ? rts[index] : null;
	}
}
