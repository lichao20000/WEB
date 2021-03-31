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
<%@ page import="com.linkage.commons.db.PrepareSQL"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<LINK REL="stylesheet" HREF="./css/css_green.css" TYPE="text/css">
<script language="JavaScript" src="Js/tiaoZhuan.js"></script>

<%--
	���ڽ�ҵ����ϵͳ�뱨����ϵͳ�ֱ��𣬴�ҵ����ϵͳ��ת��������ϵͳʱ������Ҫ�ظ���¼��
	����ҵ���߼��������ǰ����ϵͳΪ������ϵͳ�����Ҵ�request��ȡ���ܵ��û���Ϣ���ڣ�
	��ʹ�ý��ܵ��û��������룬������������У�顣�����������ת����¼ҳ�档
	fangchao 2014-03-24
 --%>
<%
	String InstArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
	request.setCharacterEncoding("GBK");
	session.removeAttribute("nDay");
	//��ֹ�Ƿ��ύ��¼��Ϣ
	if (!LoginUtil.validateReferer(request))
	{
		response.sendRedirect("login.jsp");
		return;
	}

	// ����ʡ�� ���ܻᱨCannot create a session after the response has been commited
	// ���ֻ�����½�
    if("xj_dx".equals(InstArea)){
    	//���Ƚ�ԭsession�е�����ת����һ��ʱmap��  
        Map<String,Object> tempMap = new HashMap();  
        Enumeration<String> sessionNames = session.getAttributeNames();  
        while(sessionNames.hasMoreElements()){  
            String sessionName = sessionNames.nextElement();  
            tempMap.put(sessionName, session.getAttribute(sessionName));  
        }  
        //ע��ԭsession��Ϊ��������sessionId  
        session = request.getSession(false);
    	  if ( session != null ) {
    		  session.invalidate(); // �����ɵ� session
    	  }
    	  session = request.getSession(); 
        //����ʱmap�е�����ת������session  
        for(Map.Entry<String, Object> entry : tempMap.entrySet()){  
            session.setAttribute(entry.getKey(), entry.getValue());  
        }
    }
    
	// �Ƿ��ѵ�¼���ʺ��޳���1����  0����
	String deleFlag = request.getParameter("deleFlag");
	//��ȡ��¼ҳ�����
	String account = LoginUtil.getAccount(request);
	String per_mobile="";
	if("nmg_dx".equals(InstArea)) {
		PrepareSQL psql = new PrepareSQL();
		psql.append(" select b.per_mobile from tab_accounts a ,tab_persons b where    ");
		psql.append(" a.acc_oid=b.per_acc_oid and a.acc_loginname=? ");
		psql.setString(1, account);
		Map fields = DataSetBean.getRecord(psql.getSQL());
		if (fields != null) {
			per_mobile = (String) fields.get("per_mobile");
			request.setAttribute("per_mobile", per_mobile);
		}
	}
	boolean flg = LoginUtil.validateCode(request);
	int nDay = 0;
	String tipMsg = "";
	String descMsg = "";
	//��֤����ȷ�����
	if (flg)
	{
		// AHDX_ITMS-REQ-20190620YQW-001��web�û����죩���������µ��û����ӽ������б�
		if ("ah_dx".equals(InstArea)) {
 			UserMap.getInstance().addLockOutdatedUser();
		}
		//�ж��û��Ƿ�������ʱ��
		flg = UserMap.getInstance().checkIsLockUser(account);
 		String desc = "";
		//�û��������û�
		if (flg)
		{
			desc = "��������ʱ�����";
			LogItem.getInstance().writeLoginLog(request, Encoder.toISO(desc));
			if ("ah_dx".equals(InstArea)) {
				//AHDX_ITMS-REQ-20190924YQW-001��web�û���¼���죩���������µ������޸�����
				descMsg = "���Ѵ���������δ��½ʹ��";
				//response.sendRedirect("error.jsp?errid=15");
			}
			else {
				response.sendRedirect("error.jsp?errid=13");
			}
		}
		else
		{
			DbAuthorization auth = new DbAuthorization(request, response);
			//�û���¼��Ȩ
			if (auth.login())
			{
				//���ɹŵ�¼У��ɹ���ʧЧ��֤��
				if("nmg_dx".equals(InstArea)) {
					session.removeAttribute("checkCode-"+per_mobile);
				}
				if ("sd_lt".equals(InstArea)) {
                    session.removeAttribute("checkCode");
                }
				//����ĸ��Ӷȣ���д��ĸ��Сд��ĸ�����֡������ַ���������!@#$%^&*_������������������
				/* if(!auth.checkStrongValid(request)){
					desc="�����벻�Ǽ�ǿ���ͣ����޸����룡";
					descMsg = "�����벻�Ǽ�ǿ����";
					LogItem.getInstance().writeLoginLog(request,Encoder.toISO(desc));
				}
				//���ð��������������������ĸ��ַ�
				else if(!auth.checkKeyBoardValid(request)){
					desc="��������������������������ĸ��ַ�";
					descMsg = "��������������������������ĸ��ַ�";
					LogItem.getInstance().writeLoginLog(request,Encoder.toISO(desc));
				}
				//�����в��ܰ����û��������Ʊ仯
				else if(!auth.checkLikeNameValid(request)){
					desc="�������а����û��������Ʊ仯";
					descMsg = "�������а����û��������Ʊ仯";
					LogItem.getInstance().writeLoginLog(request,Encoder.toISO(desc));
				}
				//�������
				else */if (auth.checkPasswordUsedTime())
				{
					desc = "�������";
					LogItem.getInstance().writeLoginLog(request,
							Encoder.toISO(desc));
					//����session����ֹ�����޸�����ҳ�������ҳ��ͬʱ��DbUserManager.java �޸�����ɹ���ʧЧsession
					session.setAttribute("passwordexpire", "true");
				}
				//ip��ַ���Ϸ�
				else if (!auth.checkClientIP(request.getRemoteHost()))
				{
					desc = "IP���Ϸ�";
					LogItem.getInstance().writeLoginLog(request,
							Encoder.toISO(desc));
					auth.dealErrorLogin();
					response.sendRedirect("error.jsp?errid=12");
				}
				// HBLT-RMS-20200420-LH-001�ӱ���ͨ��������������������
				else if ("hb_lt".equals(InstArea) && !auth.checkStrongValid(request)) {
					desc = "����Ϊ�����룬���޸ģ�";
					descMsg = desc;
					LogItem.getInstance().writeLoginLog(request,
							Encoder.toISO(desc));
					//����session����ֹ�����޸�����ҳ�������ҳ��ͬʱ��DbUserManager.java �޸�����ɹ���ʧЧsession
					session.setAttribute("passwordexpire", "true");
				}
				else if (!"admin".equals(account) && auth.checkPasswordValidity())
				{
					desc = "������ڣ����޸����룡";
					LogItem.getInstance().writeLoginLog(request,
							Encoder.toISO(desc));
					//����session����ֹ�����޸�����ҳ�������ҳ��ͬʱ��DbUserManager.java �޸�����ɹ���ʧЧsession
					session.setAttribute("passwordexpire", "true");
				}else if("sd_lt".equals(InstArea) && !auth.checkStrongValid(request)){
					desc="�����벻�Ǽ�ǿ���ͣ����޸����룡";
					LogItem.getInstance().writeLoginLog(request,Encoder.toISO(desc));
					session.setAttribute("passwordexpire", "true");
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
							String isUnique = auth.checkIsUnique(); // ȷ�ϵ�ǰ�ʺ��Ƿ�����ΪΨһ��¼
							// 1 ��ʾ���ʺű�����ΪΨһ��¼��0 ��ʾ���ʺ�û�б�����ΪΨһ��¼
							if ("1".equals(isUnique))
							{
								// ��ǰ�ʺ����������ط���¼����Ҫѯ���Ƿ���Ҫ���Ѿ���¼���ʺ��޳�
								if (null != UserMap.getInstance()
										.getOnlineUserMap().get(account))
								{
									// 1 ��ʾ�޳��Ѿ��������ط���¼���ʺ�
									if ("1".equals(deleFlag))
									{
										 // ��account�û��߳�ϵͳ
										UserMap.getInstance()
												.destroySessionByUserName(account); 
										UserMap.getInstance().addOnlineSession(
												account, session);
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
									else
									{
										desc = "��ǰ�ʺ����������ط���¼";
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
								// ��ǰ�ʺ�û���������ط�����¼��ֱ�ӵ�¼
								else
								{
									UserMap.getInstance().addOnlineSession(
											account, session);
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
							// �ǵ����¼
							else
							{
								UserMap.getInstance().addOnlineSession(account,
										session);
								session.setAttribute("isReport", LipossGlobals
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
						//ǰһ��������ʾ�޸����룬ǰ������ʾ�ˣ�Ȼ����ת��������ҳ��
						nDay = auth.tipPasswordUsedWeek();
						if (nDay > 3 && nDay <= 7)
						{
							UserMap.getInstance().addOnlineSession(account,
									session);
							session.setAttribute("isReport",
									LipossGlobals.getLipossProperty("isReport"));
							session.setAttribute("IsLogin", "true");//��ʾ��½�ɹ�
							response.sendRedirect("index.jsp");
							session.removeAttribute("errorLogin");
							LogItem.getInstance().addLoginLog(request, 1);
						}
						else if (nDay <= 3 && nDay > 0)
						{
							tipMsg = "alert('�������뻹��" + nDay + "�����')";
							descMsg="�������뻹��" + nDay + "�����";
							//response.sendRedirect("system/OuterModifyPwdForm.jsp");
						}
						else
						{
							UserMap.getInstance().addOnlineSession(account,
									session);
							session.setAttribute("isReport",
									LipossGlobals.getLipossProperty("isReport"));
							session.setAttribute("IsLogin", "true");//��ʾ��½�ɹ�
							response.sendRedirect("index.jsp");
							session.removeAttribute("errorLogin");
							LogItem.getInstance().addLoginLog(request, 1);
						}
					}
				}
				session.setAttribute("nDay", nDay);
			}
			//�û��������롢��ƥ��
			else
			{
				desc = "��֤ʧ��";
				LogItem.getInstance().writeLoginLog(request, Encoder.toISO(desc));
				auth.dealErrorLogin();
				response.sendRedirect("error.jsp?errid=1");
			}
		}
	}
	else
	{
		//�����벻��ȷ	
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
			goTips(5,url,<%="'" + descMsg + "'"%>);
		<%}
			else
			{%>
			go(5,url);
		<%}%>
	</script>
</body>
</html>


<!-- ����Ȩ��Session�� -->

<br>
<div style="overflow: auto; width: 600; height: 500"><%=session.getValue("ldims")%></div>