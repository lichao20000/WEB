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

    //��ֹ�Ƿ��ύ��¼��Ϣ
    String strRefererURL = request.getHeader("referer");
    //��½�û��ʺ�
    String str_username = request.getParameter("acc_loginname");
	//��½����
	String str_password = request.getParameter("acc_password");
	// �Ƿ��ѵ�¼���ʺ��޳���1����  0����
	String deleFlag = request.getParameter("deleFlag");
	//����
	// �˴�������룬��ô����session�е����뽫�����ģ�����ȫ    --ע�� by zhangchy 2013805
	//str_username = Encoder.getFromBase64(str_username);
	//str_password = Encoder.getFromBase64(str_password);
	request.setAttribute("acc_loginname", str_username);
	request.setAttribute("acc_password", str_password);
    
    //����
    String s_area_name = request.getParameter("area_name");

    if (strRefererURL == null || strRefererURL.equals("null")) {
		response.sendRedirect("./login.jsp");
    }

    //��ȡ��¼ҳ�����
    String userCheckCode = request.getParameter("checkCode");
    boolean flg = false;
    boolean flgUserIp = false;
    String tipMsg = "";
    String descMsg = "";

    //�жϸ������Ƿ���ȷ
    String sysCheckCode = (String) SkinUtils.getSession(request, "checkCode");
    if (sysCheckCode != null && sysCheckCode.equals(userCheckCode)) {
		sysCheckCode = null;
		userCheckCode = null;
		flg = true;
    }

    if (flg) {
		//�ж��û��Ƿ�������ʱ��
		//String userName = request.getParameter("acc_loginname");
		flg = UserMap.getInstance().checkIsLockUser(str_username);
		
		String userip =  request.getRemoteAddr();   // ��ʹ�õ��������ת��ʱ��IP��ȡ��ʹ�ô˶δ���
		/** �˶δ���ֻ�ʺ�ʹ�õ��������ת����ʱ��ʹ��	begin add by zhangchy 2013-08-15 */
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
		/** �˶δ���ֻ�ʺ�ʹ�õ��������ת����ʱ��ʹ��	end add by zhangchy 2013-08-15 */
		
		flgUserIp = UserIpMap.getInstance().checkIsLockUserIp(userip);

		//�û��������û�
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
             //��֤�û����Ƿ����
             boolean isExeUserName=auth.checkUserName();
             String loginBz="";//�����жϵ�¼��ʾ��Ϣ�ı�־,0:�û�������  1:�������� 2:�������
             if(!isExeUserName){
                 response.sendRedirect("login.jsp?loginBz=0" );
                 return;
             }else if(!auth.loginArea()){//�ж��û���Ӧ�������Ƿ���ȷ
            	 System.out.println("auth.loginArea");
            	 auth.dealErrorLogin();
            	 response.sendRedirect("login.jsp?loginBz=1" );
                 return;
             }else if(!auth.login()){//�ж������Ƿ���ȷ
            	 System.out.println("auth.login");
            	 auth.dealErrorLogin();
            	 response.sendRedirect("login.jsp?loginBz=2" );
                 return;
             }
            /****************************************************************************/ 
            
		    if (true) {//��Ϊ����auth.login()�Ѿ�Ϊtrue��,�˴���������ֱ��д��true

		    System.out.println("next fun checkPasswordUsedTime");
			//�����ʺ���Ч�� 	admin�ʺŲ�����Ч���жϣ�������Ч
			if (auth.checkPasswordUsedTime() && !"admin".equals(str_username)) {
				//�ʺŹ��ڣ��Ͳ�������
			    if (null != LipossGlobals.getLipossProperty("auth.checkAccountTime")
				    && "true".equalsIgnoreCase(LipossGlobals
					    .getLipossProperty("auth.checkAccountTime"))) {
				auth.dealErrorLogin();
				response.sendRedirect("error.jsp?errid=11&acc_loginname=" + str_username);
			    }
			    //�ʺŹ������������޸�������ʹ��
			    else {
					auth.dealErrorLogin();
					/* response.sendRedirect("itv/OuterModifyPwdForm.jsp?loginname=" + str_username + "&area_name="
					+ s_area_name); */
			    }
			} else {
			    String remoteIP = request.getRemoteAddr(); // ��ʹ�õ��������ת��ʱ��IP��ȡ��ʹ�ô˶δ���
				boolean isDefaultKey = auth.isDefaultKey();
			    //session.setAttribute("remoteIP",remoteIP);
				/** �˶δ���ֻ�ʺ�ʹ�õ��������ת����ʱ��ʹ��	begin add by zhangchy 2013-08-15 */
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
				/** �˶δ���ֻ�ʺ�ʹ�õ��������ת����ʱ��ʹ��	end add by zhangchy 2013-08-15 */

			    //��֤��¼ʱ���Ƿ�Ϸ�
			    if (!auth.checkTime(str_username)) {
					auth.dealErrorLogin();
					response.sendRedirect("error.jsp?errid=12&acc_loginname=" + str_username);
			    }
			    //��֤��¼ip�Ƿ�Ϸ�
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
			    			String isUnique = auth.checkIsUnique(); // ȷ�ϵ�ǰ�ʺ��Ƿ�����ΪΨһ��¼
							// 1 ��ʾ���ʺű�����ΪΨһ��¼��0 ��ʾ���ʺ�û�б�����ΪΨһ��¼
							if ("1".equals(isUnique))
							{
									// 1 ��ʾ�޳��Ѿ��������ط���¼���ʺ�
									if ("1".equals(deleFlag))
									{
										auth.initUserRes();
										 // ��account�û��߳�ϵͳ
										UserMap.getInstance()
												.destroySessionByUserName(str_username); 
										UserMap.getInstance().addOnlineSession(
												str_username, session);
										session.setAttribute(
												"isReport",
												LipossGlobals
														.getLipossProperty("isReport"));
										session.setAttribute("IsLogin", "true");//��ʾ��½�ɹ�
										response.sendRedirect("index.jsp");
										session.removeAttribute("errorLogin");
										LogItem.getInstance().addLoginLog(
												request, 1); 
									}
									
								
								// ��ǰ�ʺ�û���������ط�����¼��ֱ�ӵ�¼
								else
								{
									auth.initUserRes();
									UserMap.getInstance().addOnlineSession(
											str_username, session);
									session.setAttribute(
											"isReport",
											LipossGlobals
													.getLipossProperty("isReport"));
									session.setAttribute("IsLogin", "true");//��ʾ��½�ɹ�
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
								session.setAttribute("IsLogin", "true");//��ʾ��½�ɹ�
								response.sendRedirect("index.jsp");
								session.removeAttribute("errorLogin");
								LogItem.getInstance().addLoginLog(request, 1);
			    			}
			    		}
			    	}
			    	else
			    	{
			    		auth.initUserRes();
						//�ж��û��Ƿ������ظ���¼�����ǣ����֮ǰ��¼�û���ȡ��
						auth.checkExclusive(str_username);
						session.setAttribute("IsLogin", "true");//��ʾ��½�ɹ�
						UserMap.getInstance().addOnlineSession(str_username, session);
						response.sendRedirect("index.jsp");
						//��sessionд����Ϣ
						session.setAttribute("rand", "rand");
			    	}

    }

			 }
		    } else {
			//���յ������⹦�ܣ���¼ʧ�ܺ��ж����û����������������
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
		//�����벻��ȷ
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