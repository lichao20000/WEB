<%@page import="com.linkage.litms.common.util.LoginUtil"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.util.SkinUtils"%>
<%@ page import="com.linkage.litms.system.UserMap"%>
<%@ page import="com.linkage.litms.system.UserRes"%>
<%@ page import="com.linkage.litms.common.util.Encoder"%>
<%@ page import="com.linkage.litms.system.dbimpl.LogItem"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@ page import="com.linkage.litms.system.dbimpl.DbAuthorization"%>
<%@ page import="com.linkage.litms.common.util.LoginUtil"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Date"%>
<LINK REL="stylesheet" HREF="./css/css_green.css" TYPE="text/css">
<script language="JavaScript" src="Js/tiaoZhuan.js"></script>

<%--
	由于将业务子系统与报表子系统分别部署，从业务子系统跳转到报表子系统时，不需要重复登录。
	增加业务逻辑：如果当前配置系统为报表子系统，并且从request获取加密的用户信息存在，
	则使用解密的用户名和密码，并屏蔽输入码校验。其他情况，跳转到登录页面。
	fangchao 2014-03-24
 --%>
<%
	String InstArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
	request.setCharacterEncoding("GBK");
	session.removeAttribute("nDay");
	if (!LoginUtil.validateReferer(request))
	//防止非法提交登录消息
	{
		response.sendRedirect("login_new.jsp");
	}

	//首先将原session中的数据转移至一临时map中  
    Map<String,Object> tempMap = new HashMap();  
    Enumeration<String> sessionNames = session.getAttributeNames();  
    while(sessionNames.hasMoreElements()){  
        String sessionName = sessionNames.nextElement();  
        tempMap.put(sessionName, session.getAttribute(sessionName));  
    }  
    //注销原session，为的是重置sessionId  
    session = request.getSession(false);
	  if ( session != null ) {
		  session.invalidate(); // 废弃旧的 session
	  }
	  session = request.getSession(); 
    //将临时map中的数据转移至新session  
    for(Map.Entry<String, Object> entry : tempMap.entrySet()){  
        session.setAttribute(entry.getKey(), entry.getValue());  
    }

	// 是否将已登录的帐号剔除，1：是  0：否
	String deleFlag = request.getParameter("deleFlag");
	//获取登录页面参数
	String account = LoginUtil.getAccount(request);
	boolean flg = LoginUtil.validateCode(request);
	int nDay = 0;
	String tipMsg = "";
	String descMsg = "";
	//验证码正确的情况
	if (flg)
	{
		// AHDX_ITMS-REQ-20190620YQW-001（web用户改造）超过三个月的用户添加进锁定列表
		if ("ah_dx".equals(InstArea)) {
			UserMap.getInstance().addLockOutdatedUser();
		}
		//判断用户是否处于锁定时间
		flg = UserMap.getInstance().checkIsLockUser(account);
		String desc = "";
		//用户是锁定用户
		if (flg)
		{
			desc = "处于锁定时间段内";
			LogItem.getInstance().writeLoginLog(request, Encoder.toISO(desc));
			if ("ah_dx".equals(InstArea)) { 
				response.sendRedirect("error.jsp?errid=15");
			}else if("gs_dx".equals(InstArea)){
				// 甘肃 显示剩余多久解锁
				long allowLoginTime = Long.parseLong(String.valueOf(UserMap.getInstance().getLockUsers().get(account)));
				allowLoginTime = allowLoginTime - new Date().getTime()/1000;	
				response.sendRedirect("error.jsp?errid=16&allowLoginTime="+allowLoginTime);
			}else {
				response.sendRedirect("error.jsp?errid=13");
			}
		}
		else
		{
			DbAuthorization auth = new DbAuthorization(request, response);
			//用户登录鉴权
			if (auth.login())
			{
				//密码的复杂度，大写字母、小写字母、数字、特殊字符（包括：!@#$%^&*_），至少需满足三种
				/* if(!auth.checkStrongValid(request)){
					desc="旧密码不是加强类型，请修改密码！";
					descMsg = "旧密码不是加强类型";
					LogItem.getInstance().writeLoginLog(request,Encoder.toISO(desc));
				}
				//不得包含键盘上任意连续的四个字符
				else if(!auth.checkKeyBoardValid(request)){
					desc="旧密码包含键盘上任意连续的四个字符";
					descMsg = "旧密码包含键盘上任意连续的四个字符";
					LogItem.getInstance().writeLoginLog(request,Encoder.toISO(desc));
				}
				//密码中不能包含用户名的形似变化
				else if(!auth.checkLikeNameValid(request)){
					desc="旧密码中包含用户名的形似变化";
					descMsg = "旧密码中包含用户名的形似变化";
					LogItem.getInstance().writeLoginLog(request,Encoder.toISO(desc));
				}
				//密码过期
				else */if (auth.checkPasswordUsedTime())
				{
					desc = "密码过期";
					LogItem.getInstance().writeLoginLog(request,
							Encoder.toISO(desc));
				}
				//ip地址不合法
				else if (!auth.checkClientIP(request.getRemoteHost()))
				{
					desc = "IP不合法";
					LogItem.getInstance().writeLoginLog(request,
							Encoder.toISO(desc));
					auth.dealErrorLogin();
					response.sendRedirect("error.jsp?errid=12");
				}
				else if (!"admin".equals(account) && auth.checkPasswordValidity())
				{
					desc = "密码过期，请修改密码！";
					LogItem.getInstance().writeLoginLog(request,
							Encoder.toISO(desc));
				}
				else
				{
					if ("ah_dx".equals(InstArea) || "jx_dx".equals(InstArea) || "hlj_dx".equals(InstArea))
					{
						//boolean isFirstLogin = auth.checkUserLogin(request);
						boolean isDefaultKey = auth.isDefaultKey();
						if (("jx_dx".equals(InstArea) || "hlj_dx".equals(InstArea)) &&  isDefaultKey)
						{
							LogItem.getInstance().addLoginLog(request, 0);
						}
						else
						{
							String isUnique = auth.checkIsUnique(); // 确认当前帐号是否被设置为唯一登录
							// 1 表示该帐号被设置为唯一登录，0 表示该帐号没有被设置为唯一登录
							if ("1".equals(isUnique))
							{
								// 当前帐号已在其他地方登录，需要询问是否需要将已经登录的帐号剔除
								if (null != UserMap.getInstance()
										.getOnlineUserMap().get(account))
								{
									// 1 表示剔除已经在其他地方登录的帐号
									if ("1".equals(deleFlag))
									{
										 // 将account用户踢出系统
										UserMap.getInstance()
												.destroySessionByUserName(account); 
										UserMap.getInstance().addOnlineSession(
												account, session);
										session.setAttribute(
												"isReport",
												LipossGlobals
														.getLipossProperty("isReport"));
										session.setAttribute("IsLogin", "true");//标示登陆成功
										response.sendRedirect("index.jsp");
										session.removeAttribute("errorLogin");
										LogItem.getInstance().addLoginLog(
												request, 1); 
									}
									else
									{
										desc = "当前帐号已在其他地方登录";
										String errUrl = "error.jsp?errid=14&acc_loginname="
												+ request.getParameter("acc_yhm")
												+ "&acc_password="
												+ request.getParameter("acc_mm")
                                                + "&area_name="
                                                + request
                                                .getParameter("area_name")
												+ "&checkCode="
												+ request
														.getParameter("checkCode");
										response.sendRedirect(errUrl);
									}
								}
								// 当前帐号没有在其他地方被登录，直接登录
								else
								{
									UserMap.getInstance().addOnlineSession(
											account, session);
									session.setAttribute(
											"isReport",
											LipossGlobals
													.getLipossProperty("isReport"));
									session.setAttribute("IsLogin", "true");//标示登陆成功
									response.sendRedirect("index.jsp");
									session.removeAttribute("errorLogin");
									LogItem.getInstance().addLoginLog(request, 1);
								}
							}
							// 非单点登录
							else
							{
								UserMap.getInstance().addOnlineSession(account,
										session);
								session.setAttribute("isReport", LipossGlobals
										.getLipossProperty("isReport"));
								session.setAttribute("IsLogin", "true");//标示登陆成功
								response.sendRedirect("index.jsp");
								session.removeAttribute("errorLogin");
								LogItem.getInstance().addLoginLog(request, 1);
							}
						}
					}
					else
					{
						//前一个星期提示修改密码，前三天提示了，然后跳转到改密码页面
						nDay = auth.tipPasswordUsedWeek();
						if (nDay > 3 && nDay <= 7)
						{
							UserMap.getInstance().addOnlineSession(account,
									session);
							session.setAttribute("isReport",
									LipossGlobals.getLipossProperty("isReport"));
							session.setAttribute("IsLogin", "true");//标示登陆成功
							response.sendRedirect("index.jsp");
							session.removeAttribute("errorLogin");
							LogItem.getInstance().addLoginLog(request, 1);
						}
						else if (nDay <= 3 && nDay > 0)
						{
							tipMsg = "alert('您的密码还有" + nDay + "天过期')";
							//response.sendRedirect("system/OuterModifyPwdForm.jsp");
						}
						else
						{
							UserMap.getInstance().addOnlineSession(account,
									session);
							session.setAttribute("isReport",
									LipossGlobals.getLipossProperty("isReport"));
							session.setAttribute("IsLogin", "true");//标示登陆成功
							response.sendRedirect("index.jsp");
							session.removeAttribute("errorLogin");
							LogItem.getInstance().addLoginLog(request, 1);
						}
					}
				}
				session.setAttribute("nDay", nDay);
			}
			//用户名、密码、域不匹配
			else
			{
				desc = "认证失败";
				LogItem.getInstance().writeLoginLog(request, Encoder.toISO(desc));
				auth.dealErrorLogin();
				
				if("gs_dx".equals(InstArea)){ // 甘肃
					// 用户名或密码不匹配！
					response.sendRedirect("error.jsp?errid=17");
				}else{
					response.sendRedirect("error.jsp?errid=1");
				}
				
			}
		}
	}
	else
	{
		//附加码不正确	
		response.sendRedirect("error.jsp?errid=7");
	}
%>

<html>
<SCRIPT LANGUAGE="JavaScript">
window.onload=function(){ 
	<%=tipMsg%>

}


</SCRIPT>
<body>
	<script type="text/javascript">
		var host = window.location.host;
		var href = window.location.href;
		var pathName = window.location.pathname;
		var path = pathName.substring(0,pathName.lastIndexOf("/"));
		var url = "http://" + host + path + "/system/OuterModifyPwdForm.jsp";
		if(href.indexOf("https") != -1)
		{
			url = "https://" + host + path + "/system/OuterModifyPwdForm.jsp";
		}
		
		<%if (!"".equals(descMsg))
			{%>
			goTips(5,url,
	<%="'" + descMsg + "'"%>);
		<%}
			else
			{%>
			go(5,url);
		<%}%>
	</script>
</body>
</html>


<!-- 测试权限Session： -->

<br>
<div style="overflow: auto; width: 600; height: 500"><%=session.getValue("ldims")%></div>