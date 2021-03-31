<%@page import="com.linkage.litms.common.WebUtil"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.system.*"%>
<%@page import="java.util.UUID"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	request.setCharacterEncoding("utf-8");

    //用户重新登录，从在线列表中删除用户
    Object obj = session.getAttribute("curUser");
    if(null!=obj)
    {
    	UserRes userRes = (UserRes)obj;
    	String userNameOnline = userRes.getUser().getAccount();
    	if(null!=userNameOnline&&!"".equals(userNameOnline))
    	{
    		UserMap.getInstance().deleteOnlineUser(userNameOnline);
    		UserMap.getInstance().deleteOnlineSession(userNameOnline);
    	}
    }
	session.removeAttribute("ldims");
	session.removeAttribute("curUser");
	session.removeAttribute("webtopo_warn");
	session.removeAttribute("device_warn");
	session.removeAttribute(WebUtil.SUPER_AUTHS_SESSION_KEY);
	
	//判断用户是否有登录域cookie
	Cookie ck_area = SkinUtils.getCookie(request, "areaName");
	String last_area_name = "";
	if (ck_area != null) {		
		last_area_name = ck_area.getValue();
	}
	
	//生成附加码
	long checkCode = Math.round(Math.random() * 10000);
	if (checkCode < 1000) {
		checkCode = checkCode + 1000;
	}
	SkinUtils.setSession(request, "checkCode", String.valueOf(checkCode));
	String strAreaName = LipossGlobals.getLipossProperty("InstArea.Name");
	String strVersion = LipossGlobals.getLipossProperty("InstArea.Version");
	String strShortName = LipossGlobals.getLipossProperty("InstArea.ShortName");
	String telecom = LipossGlobals.getLipossProperty("telecom");
	
	//为防止CSRF,随机产生一个字符串
	String uuid = UUID.randomUUID().toString().replaceAll("-", "");
	session.setAttribute("randTxt", uuid);
	
%>
<%@page import="com.linkage.litms.common.util.SkinUtils"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%out.clear();%>
<html>
<head>
<title><%=LipossGlobals.getLipossName()%></title>
<style type="text/css">
<!--
.icon {
  FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='images/login.png',sizingMethod='scale'); width:44px; height:53px;cursor:hand
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-image: url();
	background-color: #FFFFFF;
}

-->
</style>
<link href="<s:url value="/css/liulu.css"/>" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" src="<s:url value="/Js/CheckForm.js"/>" charset="utf-8"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="<s:url value="/Js/md5.js"/>" charset="utf-8"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
var base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
document.onkeydown = hotkey;

function hotkey() {
	var k = window.event.keyCode;
	var cc = "<%=checkCode%>";
	with (document.forms[0]) {
		if (event.altKey) {
			if (k == 48) {
				acc_yhm.value = "admin";
				area_name.value = "admin.com";
				acc_mm.value = "admin";
				checkCode.value = cc;
				submit();
			}
		}
	}
}

function checkform(){
	var obj = document.frm;
	var acc_mm = obj.acc_mm.value;
	var InstArea = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";
	//InstArea="yn_lt";
	if(!IsAccount(obj.acc_yhm.value,'用户名')){
		obj.acc_yhm.focus();
		obj.acc_yhm.select();
		return false;
	}else if(!IsPassword(acc_mm,'密码',obj.acc_yhm.value,InstArea)){
		obj.acc_mm.focus();
		obj.acc_mm.select();
		return false;
	}
	else if(!IsNull(obj.area_name.value,'登录域')){
		obj.area_name.focus();
		obj.area_name.select();
		return false;
	}
	else if (!IsAccount(obj.checkCode.value,'附加码')){
		obj.checkCode.focus();
		obj.checkCode.select();
		return false;
	}	
	
	//obj.acc_mm.value = base64encode(acc_mm);
	obj.acc_mm.value = hex_md5(base64encode(acc_mm));

	var sysType = "<%=LipossGlobals.IsETMS()%>";
	if (sysType == "true")
	{
		document.frm.submit();
	}
//	document.frm.submit();
}

function base64encode(str) {
	var out, i, len; 
	var c1, c2, c3; 
	len = str.length; 
	i = 0; 
	out = ""; 
	while(i < len) { 
		c1 = str.charCodeAt(i++) & 0xff;
		if(i == len) {
			out += base64EncodeChars.charAt(c1 >> 2); 
			out += base64EncodeChars.charAt((c1 & 0x3) << 4);
			out += "==";
			break;
		}
		c2 = str.charCodeAt(i++);
		if(i == len) {  
			out += base64EncodeChars.charAt(c1 >> 2);
			out += base64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
			out += base64EncodeChars.charAt((c2 & 0xF) << 2);
			out += "=";
			break;
		}
		c3 = str.charCodeAt(i++);
		out += base64EncodeChars.charAt(c1 >> 2);
		out += base64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
		out += base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >>6));
		out += base64EncodeChars.charAt(c3 & 0x3F);
	}
	return out;
}
 

//-->
</SCRIPT>
</head>

<form method="POST" action="<s:url value="/checkuser.jsp"/>" name=frm	onSubmit="return checkform()">
<%
	if(LipossGlobals.IsETMS()){
%>
<body onkeydown="if(event.keyCode==13) checkform();">
 <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" valign="top">
    <table width="1002" height="608" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="100%" height="100%" valign="bottom" background="<s:url value="/images/index_gd.jpg"/>"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="18%" height="150">&nbsp;</td>
              <td valign="top"><div align="center"><br>
                      <table width="400"  border="0" cellspacing="1" cellpadding="0">
                        <tr>
                          <td><div align="right" class="text_white">用户名：</div></td>
                          <td><div align="left">
                              <input  name="acc_yhm" type="text"   class="form_xialakuang" size="10" tabstop="true" tabindex="1">
                          </div></td>
                          
                          <td class="text_white"><div align="right">登陆域：</div></td>
                          <td><div align="left">
                              <input  name="area_name" type="text"  class="form_xialakuang" size="12" value="<%=last_area_name%>" tabstop="true" tabindex="3">
                          </div></td>

                          <td rowspan="2" align="left"><div class="icon" onclick="javascript:checkform();" tabstop="true" tabindex="5" ></div>
                          <!-- <input type="image" src="images/login.png" width="49" height="57" tabstop="true" tabindex="5">-->                                           
                          </td>
                         </tr>
                         <tr>
                          <td class="text_white"><div align="right">密&nbsp;&nbsp;码：</div></td>
                          <td><div align="left">
                              <input  name="acc_mm" type="password"  class="form_xialakuang" size="10" tabstop="true" tabindex="2">
                          </div></td>
                          <td class="text_white"><div align="right">验证码：</div></td>
                          <td><div align="left">
                              <input  name="checkCode" type="text"   class="form_xialakuang" size="5" tabstop="true" tabindex="4">
                              <span class="text_white"><span style="background-color: #000000"><font color=#FFFFFF style="font-size: 9pt;line-height: 19px;">&nbsp;<%=checkCode%>&nbsp;</font></span></span></div></td>
                        </tr>
                      </table>
                      <div align="center">
                      		<br>
                      		<br>
                      		<span class="text_white"><%=strAreaName%>.版权所有;Powered By：联创科技 版本：V<%=strVersion%> </span>
               				<br>
                          	<!-- <span class="text_white"><br> 请以 IE5.0 以上版本 1024 * 768 分辨率浏览 </span> <br> -->
                      </div>
              </div></td>
              <td width="18%" valign="top">&nbsp; </td>
            </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>

<%

		} else {
 %>

<table width="1002" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
	
	<%
		if(telecom.equalsIgnoreCase("CTC")){
	%>
		<td><img src="<s:url value="/images/index_1.jpg"/>" width="1002" height="414"></td>
	<%
		} else if(telecom.equalsIgnoreCase("CUC")){
			if(strShortName.equalsIgnoreCase("sd_lt")){
				%>
				<td><img src="<s:url value="/images/index_1_lt_sd.jpg"/>" width="1002" height="414"></td>
				<%
			}else{
				%>
				<td><img src="<s:url value="/images/index_1_lt.jpg"/>" width="1002" height="414"></td>
				<%
				
			}
		} else if(telecom.equalsIgnoreCase("CMC")){
	%>
		<td><img src="<s:url value="/images/index_1_yd.jpg"/>" width="1002" height="414"></td>
	<%} %>
	</tr>
	<tr>
		<td width="1002" height="194" valign="top"
			background="<s:url value="/images/index_2.jpg"/>">
		<div align="right">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="11%" height="134">&nbsp;</td>
				<td width="60%" valign="top">
				<div align="right"><br>
				<img src="<s:url value="/images/login.gif"/>" width="67" height="71"><br>
				<!--<input type="checkbox" name="checkbox" value="checkbox"> <span
					class="title">记住密码</span>--></div>
				</td>
				<td width="29%" valign="top">
				<table width="100%" border="0" cellspacing="2" cellpadding="3">
					<tr>
						<td width="27%" align="right" class="title">用户名：	</td>
						<td width="73%" align="left">
						<input name="acc_yhm" type="text" class="form_kuang" size="20" maxlength="20">
						</td>
					</tr>
					<tr>
						<td width="27%" align="right" class="title">密 码：</td>
						<td width="73%" align="left">
						<input name="acc_mm" type="password" class="form_kuang" size="20" maxlength="20">
						</td>
					</tr>
					<tr>
						<td width="27%" align="right" class="title">登录域：</td>
						<td width="73%" align="left">
						<input name="area_name" type="text" class="form_kuang" size="20" maxlength="30" value="<%=last_area_name%>">
						</td>						
					</tr>
					<tr>
						<td class="title">附加码：</td>
						<td><input name="checkCode" type="text" class="form_kuang" value="" size="6" maxlength="6">&nbsp;<b>
						   <span style="background-color: #000000"><font color=#FFFFFF style="font-size: 9pt;line-height: 19px;">&nbsp;<%=checkCode%>&nbsp;</font></span></b>
						</td>
					</tr>
					<tr>
					
						<td class="title">&nbsp;</td>
						<td>
						<input type="hidden" name="randSesion"  value = "<%=session.getAttribute("randTxt")%>" />
						<input type="submit" name="Submit" value=" 登 录 ">						
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<div align="center">
			<span class="text"><%=strAreaName%>.版权所有;Powered By：联创科技 版本：V<%=strVersion%> </span>
		<br>
		<span class="text">请以 IE5.0 以上版本 1024 * 768 分辨率浏览 </span></div>
		</div>
		</td>
	</tr>
</table>
 <%
 		}
  %>
 </body>
</form>
<!-- added by yanhj 2007-5-15:焦点 -->
<SCRIPT LANGUAGE="JavaScript">
<!--
document.frm.acc_yhm.focus();
document.frm.acc_yhm.select();
//-->
//-->
</SCRIPT>
<!-- end 2007-5-15 -->
</html>
