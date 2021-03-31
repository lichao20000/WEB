<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.system.UserRes" %>
<%@page import="com.linkage.module.gtms.itv.system.UserMap" %>
<%@page import="com.linkage.litms.common.util.SkinUtils" %>
<%@page import="com.linkage.litms.LipossGlobals" %>
<%
	request.setCharacterEncoding("GBK");
	//�û����µ�¼���������б���ɾ���û�
	Object obj = session.getAttribute("curUser");
	if (null != obj) {
		UserRes userRes = (UserRes) obj;
		String onlineUserName = userRes.getUser().getAccount();
		if (null != onlineUserName && !"".equals(onlineUserName)) {
			UserMap.getInstance().deleteOnlineSession(onlineUserName);
		}
	}
	session.removeAttribute("ldims");
	session.removeAttribute("curUser");
	//Add By Hmc 2006-11-23 ɾ���豸�澯session
	session.removeAttribute("webtopo_warn");
	session.removeAttribute("device_warn");
	session.removeAttribute("IsLogin");
	//���ɸ�����
	long checkCode = Math.round(Math.random() * 10000);
	if (checkCode < 1000) {
		checkCode = checkCode + 1000;
	}
	//SkinUtils.removeSession(request, "checkCode");
	SkinUtils.setSession(request, "checkCode",
			String.valueOf(checkCode));
	//�жϰ�װ������ʾ��ͬ��ͼƬ
	String strArea = LipossGlobals
			.getLipossProperty("InstArea.ShortName");
	String strAreaName = LipossGlobals
			.getLipossProperty("InstArea.Name");
	String strVersion = LipossGlobals
			.getLipossProperty("InstArea.Version");
	String index_img = "itv/images/index_";
	if (strArea != null) {
		index_img += strArea + ".jpg";
	} else {
		index_img = "itv/images/index.jpg";
	}
	if (strAreaName == null) {
		strAreaName = "�����Ƽ�";
	}
	//�ж��û��Ƿ��е�¼��cookie
	Cookie ck_area = SkinUtils.getCookie(request, "areaName");
	String last_area_name = "";
	if (ck_area != null) {
		last_area_name = ck_area.getValue();
	}
	//�����ǵ�¼��ʾ��Ϣ������� liuht 66360 add
	String loginMsg = "";
	String loginname = request.getParameter("loginBz");//loginBz������checkuser.jsp��
	if ("0".equals(loginname)) {
		loginMsg = "�û�������!";
	} else if ("1".equals(loginname)) {
		loginMsg = "��������!";
	} else if ("2".equals(loginname)) {
		loginMsg = "�������!";
	} else {
		loginMsg = "";
	}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><%=LipossGlobals.getLipossName()%></title>
<link href="./css/logon.css" rel="stylesheet" type="text/css" />
<!--[if IE 6]>
<script src="js/DD_belatedPNG.js"></script>
<script>
  DD_belatedPNG.fix('.light-bg,.log-main,background');
</script>
<![endif]-->
<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="../Js/base64.js"></script>
<SCRIPT LANGUAGE="JavaScript">
<!--
function checkform(){
	
	var obj = document.frm;
	var acc_password = obj.acc_password.value;
	var acc_loginname = obj.acc_loginname.value;
	
	if(!IsAccount(obj.acc_loginname.value,'�û���')){
		obj.acc_loginname.focus();
		obj.acc_loginname.select();
		return false;
    }else if(obj.acc_password.value==""){
		alert("���벻��Ϊ��");
		obj.acc_password.focus();
		obj.acc_password.select();
		return false;
	}else if(obj.acc_password.value.length<6){
		alert("���볤������Ϊ6λ");
		obj.acc_password.focus();
		obj.acc_password.select();
		return false;
	}else if(!IsNull(obj.area_name.value,'��¼��')){
		obj.area_name.focus();
		obj.area_name.select();
		return false;
	}else if (!IsAccount(obj.checkCode.value,'������')){
		obj.checkCode.focus();
		obj.checkCode.select();
		return false;
	}else{
		var b64 = new Base64(); 
		
		obj.acc_password.value = b64.encode(acc_password);
		   
		return true;
	}
}

window.onload= function (){
	document.frm.acc_loginname.focus();
}
//-->
</SCRIPT>
</head>

<body class="log-bg">
	<div class="log-main">
		<form method="POST" action="./checkuser.jsp" name=frm
			onSubmit="return checkform()" target="_top">
			<table class="log-box">
						<%
							if(strArea.equals("jx_dx")){
						%>
							<tr>
							<td>
							<input name = "deleFlag"  class="form_flag" type="hidden" value = "1">
							</td>
							</tr>
						<%
							}else{
						%>
							<tr>
							<td>
							<input name = "deleFlag"  class="form_flag" type="hidden" value = "0">
							</td>
							</tr>
						<%
							}
						%>
				<tr>
					<td>�û�����</td>
					<td>
					  <!--input name="acc_loginname_temp" id="acc_loginname_temp" type="text" class="log-intxt" /-->
					  <input name="acc_loginname" type="text" class="log-intxt" size="20" maxlength="20" >
					</td>
				</tr>  
				<tr>
					<td>�� �룺</td>
					<td>
					     <!-- input name="acc_password_temp" id="acc_password_temp" type="password" class="log-intxt" /-->
						<input name="acc_password" type="password" class="log-intxt" size="20" maxlength="20">
						</td>
				</tr>
				<tr>
					<td>��¼��</td>
					<td><input name="area_name" type="text" class="log-intxt"
						value="<%=last_area_name%>" /></td>
				</tr>
				<tr>
					<td class="title">�����룺</td>
					<td><input name="checkCode" type="text"
						style="margin-left: 4px;" class="form_kuang" value="" size="4"
						maxlength="4">&nbsp;<b><span
							style="background-color: #000000"><font color=#FFFFFF>&nbsp;<%=checkCode%>&nbsp;
							</font></span></b></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><span style="color: red"><%=loginMsg%></span></td>
				</tr>
			</table>
			<input name="" type="submit" class="logon-btn" value="��¼" />
		</form>
		<span class="copyright">�й����Ž�����˾ ��Ȩ���� All rights reserved@2014</span>
	</div>
</body>
</html>
