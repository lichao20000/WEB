<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.module.gtms.itv.system.UserMap" %>
<%@page import="com.linkage.module.gtms.itv.system.UserIpMap" %>
<%@page import="com.linkage.litms.common.util.SkinUtils" %>
<%@page import="com.linkage.module.gtms.itv.system.DbAuthorization" %>
<%@page import="com.linkage.litms.LipossGlobals" %>
<%@ page import="com.linkage.litms.system.dbimpl.LogItem"%>

<LINK REL="stylesheet" HREF="./css/css_blue.css" TYPE="text/css">
<script language="JavaScript" src="../Js/tiaoZhuan.js"></script>
<%
    request.setCharacterEncoding("GBK");

    //防止非法提交登录消息
    String strRefererURL = request.getHeader("referer");
    //登陆用户帐号
    String str_username = request.getParameter("acc_loginname");
	//登陆密码
	String str_password = request.getParameter("acc_password");
	// 是否将已登录的帐号剔除，1：是  0：否
	String deleFlag = request.getParameter("deleFlag");
	//解码
	// 此处如果解码，那么放入session中的密码将是明文，不安全    --注释 by zhangchy 2013805
	//str_username = Encoder.getFromBase64(str_username);
	//str_password = Encoder.getFromBase64(str_password);
	request.setAttribute("acc_loginname", str_username);
	request.setAttribute("acc_password", str_password);
    
    //域名
    String s_area_name = request.getParameter("area_name");

    if (strRefererURL == null || strRefererURL.equals("null")) {
		response.sendRedirect("./login.jsp");
    }

    //获取登录页面参数
    String userCheckCode = request.getParameter("checkCode");
    boolean flg = false;
    boolean flgUserIp = false;
    String tipMsg = "";
    String descMsg = "";

    //判断附加码是否正确
    String sysCheckCode = (String) SkinUtils.getSession(request, "checkCode");
    if (sysCheckCode != null && sysCheckCode.equals(userCheckCode)) {
		sysCheckCode = null;
		userCheckCode = null;
		flg = true;
    }

    if (flg) {
		//判断用户是否处于锁定时间
		//String userName = request.getParameter("acc_loginname");
		flg = UserMap.getInstance().checkIsLockUser(str_username);
		
		String userip =  request.getRemoteAddr();   // 不使用第三方软件转发时，IP获取，使用此段代码
		/** 此段代码只适合使用第三方软件转发的时候使用	begin add by zhangchy 2013-08-15 */
		/**
		String userip = request.getHeader("x-forwarded-for");
		if (request == null || userip.length() == 0 || "unknown".equalsIgnoreCase(userip)) {
			userip = request.getHeader("Proxy-Client-IP");
		}
		
		if (request == null || userip.length() == 0 || "unknown".equalsIgnoreCase(userip)) {
			userip = request.getHeader("WL-Proxy-Client-IP");
		}
		
		if (request == null || userip.length() == 0 || "unknown".equalsIgnoreCase(userip)) {
			userip = request.getRemoteAddr();
		}
		*/
		/** 此段代码只适合使用第三方软件转发的时候使用	end add by zhangchy 2013-08-15 */
		
		flgUserIp = UserIpMap.getInstance().checkIsLockUserIp(userip);

		//用户是锁定用户
		if (flg) {
		    response.sendRedirect("error.jsp?errid=14&acc_loginname=" + str_username);
		}
		else if(flgUserIp)
		{
		    response.sendRedirect("error.jsp?errid=17");
		}
		else {
		    DbAuthorization auth = new DbAuthorization(request, response);

		    /****************************************************************************/
             //liuht 6360 add
             //验证用户名是否存在
             boolean isExeUserName=auth.checkUserName();
             String loginBz="";//用来判断登录提示信息的标志,0:用户名错误  1:域名错误 2:密码错误
             if(!isExeUserName){
                 response.sendRedirect("login.jsp?loginBz=0" );
                 return;
             }else if(!auth.loginArea()){//判断用户对应的域名是否正确
            	 System.out.println("auth.loginArea");
            	 auth.dealErrorLogin();
            	 response.sendRedirect("login.jsp?loginBz=1" );
                 return;
             }else if(!auth.login()){//判断密码是否正确
            	 System.out.println("auth.login");
            	 auth.dealErrorLogin();
            	 response.sendRedirect("login.jsp?loginBz=2" );
                 return;
             }
            /****************************************************************************/ 
            
		    if (true) {//因为上面auth.login()已经为true了,此处条件可以直接写成true

		    System.out.println("next fun checkPasswordUsedTime");
			//检验帐号有效期 	admin帐号不做有效期判断，永久有效
			if (auth.checkPasswordUsedTime() && !"admin".equals(str_username)) {
				//帐号过期，就不给用了
			    if (null != LipossGlobals.getLipossProperty("auth.checkAccountTime")
				    && "true".equalsIgnoreCase(LipossGlobals
					    .getLipossProperty("auth.checkAccountTime"))) {
				auth.dealErrorLogin();
				response.sendRedirect("error.jsp?errid=11&acc_loginname=" + str_username);
			    }
			    //帐号过期允许重新修改密码来使用
			    else {
					auth.dealErrorLogin();
					/* response.sendRedirect("itv/OuterModifyPwdForm.jsp?loginname=" + str_username + "&area_name="
					+ s_area_name); */
			    }
			} else {
			    String remoteIP = request.getRemoteAddr(); // 不使用第三方软件转发时，IP获取，使用此段代码
				boolean isDefaultKey = auth.isDefaultKey();
			    //session.setAttribute("remoteIP",remoteIP);
				/** 此段代码只适合使用第三方软件转发的时候使用	begin add by zhangchy 2013-08-15 */
			    /**
				String remoteIP = request.getHeader("x-forwarded-for");
				if (request == null || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
					remoteIP = request.getHeader("Proxy-Client-IP");
				}
				
				if (request == null || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
					remoteIP = request.getHeader("WL-Proxy-Client-IP");
				}
				
				if (request == null || remoteIP.length() == 0 || "unknown".equalsIgnoreCase(remoteIP)) {
					remoteIP = request.getRemoteAddr();
				}
				*/
				/** 此段代码只适合使用第三方软件转发的时候使用	end add by zhangchy 2013-08-15 */

			    //验证登录时间是否合法
			    if (!auth.checkTime(str_username)) {
					auth.dealErrorLogin();
					response.sendRedirect("error.jsp?errid=12&acc_loginname=" + str_username);
			    }
			    //验证登录ip是否合法
			    else if (!auth.checkIP(str_username, remoteIP)) {
					auth.dealErrorLogin();
					response.sendRedirect("error.jsp?errid=13&acc_loginname=" + str_username);
			    } else {
			    	
			    	if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) )
			    	{
			    		if((isDefaultKey))
			    		{
			    			LogItem.getInstance().addLoginLog(request, 0);	
			    		}
			    		else
			    		{
			    			String isUnique = auth.checkIsUnique(); // 确认当前帐号是否被设置为唯一登录
							// 1 表示该帐号被设置为唯一登录，0 表示该帐号没有被设置为唯一登录
							if ("1".equals(isUnique))
							{
									// 1 表示剔除已经在其他地方登录的帐号
									if ("1".equals(deleFlag))
									{
										auth.initUserRes();
										 // 将account用户踢出系统
										UserMap.getInstance()
												.destroySessionByUserName(str_username); 
										UserMap.getInstance().addOnlineSession(
												str_username, session);
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
									
								
								// 当前帐号没有在其他地方被登录，直接登录
								else
								{
									auth.initUserRes();
									UserMap.getInstance().addOnlineSession(
											str_username, session);
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
			    			else
			    			{
			    				auth.initUserRes();
								UserMap.getInstance().addOnlineSession(
										str_username, session);
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
			    	}
			    	else
			    	{
			    		auth.initUserRes();
						//判断用户是否不允许重复登录，如是，则把之前登录用户都取消
						auth.checkExclusive(str_username);
						session.setAttribute("IsLogin", "true");//标示登陆成功
						UserMap.getInstance().addOnlineSession(str_username, session);
						response.sendRedirect("index.jsp");
						//向session写入信息
						session.setAttribute("rand", "rand");
			    	}

    }

			 }
		    } else {
			//江苏电信特殊功能，登录失败后判断是用户名错误还是密码错误
			if ("js_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {
			    auth.dealErrorLogin();
			    if (auth.checkUserName()) {
				response.sendRedirect("error.jsp?errid=15&acc_loginname=" + str_username);
			    } else {
				response.sendRedirect("error.jsp?errid=16&acc_loginname=" + str_username);
			    }
			} else {
			    auth.dealErrorLogin();
			    response.sendRedirect("error.jsp?errid=1&acc_loginname=" + str_username);
			}
		    }
		}

    } else {
		//附加码不正确
		response.sendRedirect("./error.jsp?errid=7&acc_loginname=" + str_username);
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
		var url = "http://" + host + path + "/OuterModifyPwdForm.jsp";
		if(href.indexOf("https") != -1)
		{
			url = "https://" + host + path + "/OuterModifyPwdForm.jsp";
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