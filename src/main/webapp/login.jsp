<%@page import="com.linkage.litms.common.WebUtil"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.system.*"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.UUID"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	request.setCharacterEncoding("utf-8");

    //用户重新登录，从在线列表中删除用户
    Object obj = session.getAttribute("curUser");
    if(null!=obj)
    {
    	UserRes userRes = (UserRes)obj;
    	String onlineUserName = userRes.getUser().getAccount();
    	if(null!=onlineUserName&&!"".equals(onlineUserName))
    	{
    		UserMap.getInstance().deleteOnlineUser(onlineUserName);
    		UserMap.getInstance().deleteOnlineSession(onlineUserName);
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
	
	String strAreaName = LipossGlobals.getLipossProperty("InstArea.Name");
	String strVersion = LipossGlobals.getLipossProperty("InstArea.Version");
	String strShortName = LipossGlobals.getLipossProperty("InstArea.ShortName");
	String telecom = LipossGlobals.getLipossProperty("telecom");
	
	ArrayList<String> codelist = new VerificationCode().getVerificationCode();
	String checkCode = codelist.get(0);
	//内蒙古电信用短信验证码，不加入session
	if(!"nmg_dx".equals(strShortName) && !"sd_lt".equals(strShortName)){
		SkinUtils.setSession(request, "checkCode", checkCode);
	}
	
	
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
	/* background-image: url(); */
	background-color: #FFFFFF;
}
	.get_vod{
	border:1px solid #ccc;
	border-radius: 4px;
	color: #050001;
	background: #f4d773;
	height: 19px; 
	text-align: center;
	font-size: 11px;
	font-weight: bold;
	cursor: pointer;
}
-->
</style>
<link href="<s:url value="/css/liulu.css"/>" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" src="<s:url value="/Js/CheckForm.js"/>" charset="GBK"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="<s:url value="/Js/md5.js"/>" charset="utf-8"></SCRIPT>
<script LANGUAGE="JavaScript" src="<s:url value='/Js/jquery-1.11.3.min.js'/>"></script>
<script type="text/javascript">
  $(function(){
    $("#refresh").click(function(){
      var url = "<s:url value='/itms/LoginAction!refresh.action'/>";
      $.post(url,{
      },function(ajax){
        checkCodeVal = ajax;
        $("#check-code").html(ajax);
      });
    });
  });
  
  function getCheckCode() {
	var obj = document.frm;//from表单
	var username=obj.acc_yhm.value;
	if(username.length==0){
		alert('请输入用户名后再获取');
		return;
	}
	var url = "<s:url value='/itms/LoginAction!getMessageCode.action'/>";
	$.post(url,{
		username:username
	},function(ajax){
		var data = ajax.split('##')
	     if(data[0]==1){
				alert(data[1]);
				var ltime=60;
				var timel=setInterval(function(){
					ltime=ltime-1;
					$("#get_yzm_btn").val("("+ltime+"s)重新获取");
					$("#get_yzm_btn").attr("disabled","disabled");
					if(ltime==0){
						$("#get_yzm_btn").val("获取短信码");
						$("#get_yzm_btn").removeAttr("disabled");
						clearInterval(timel);
					}
				},1000);
			}else{
				alert(data[1]);
			}
	   });
	}
</script>
<SCRIPT LANGUAGE="JavaScript">
var base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
//监听键盘的
function checkform(){
	var obj = document.frm;//from表单
	var acc_mm = obj.acc_mm.value;//获取密码
	//var abc = obj.deleFlag.value;
	var InstArea = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";
	//InstArea="yn_lt";
	if(!IsAccount(obj.acc_yhm.value,'用户名')){
		obj.acc_yhm.focus();
		obj.acc_yhm.select();
		return false;
	}
	else if(!IsPassword(obj.acc_mm.value,'密码',obj.acc_yhm.value,InstArea)){
		obj.area_name.focus();
		obj.area_name.select();
		return false;
	}
	else if(!IsNull(obj.area_name.value,'登录域')){
		obj.area_name.focus();
		obj.area_name.select();
		return false;
	}
	if (InstArea=="nmg_dx") {
		if (!IsAccount(obj.checkCode.value,'短信码')){
			obj.checkCode.focus();
			obj.checkCode.select();
			return false;
		}	
	}else {
		if (!IsAccount(obj.checkCode.value,'附加码')){
			obj.checkCode.focus();
			obj.checkCode.select();
			return false;
		}
		if (InstArea=="sd_lt")
		{
            var checkCode = obj.checkCode.value;
            obj.checkCode.value = base64encode(checkCode)
		}

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
                          <td>
                          	<div align="left">
                              <input  name="acc_yhm" type="text"  class="form_xialakuang" size="10" tabstop="true" tabindex="1">
                         	 </div>
                          </td>
                          <%if (!LipossGlobals.inArea("hn_lt") && !LipossGlobals.inArea("sx_lt")) { %>
                          <td class="text_white"><div align="right">登陆域：</div></td>
                          <td>
                          	<div align="left">
                              <input  name="area_name" type="text"  class="form_xialakuang" size="12" value="<%=last_area_name%>" tabstop="true" tabindex="3">
                          	</div>
                          </td>
                          <%}else{%>
							<input name="area_name" type="hidden"  hidden="hidden"  value="admin.com">
						<%} %>

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
                      		<span class="text_white"><%=strAreaName%>.版权所有;Powered By：亚信科技 版本：V<%=strVersion%> </span>
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
		if(telecom.equals("CTC")){
	%>
		<td><img src="<s:url value="/images/index_1.jpg"/>" width="1002" height="414"></td>
	<%
		} else if(telecom.equals("CUC")){
			if(strShortName.equals("sd_lt")){
				%>
				<td><img src="<s:url value="/images/index_1_lt_sd.jpg"/>" width="1002" height="414"></td>
				<%
			}else{
				%>
				<td><img src="<s:url value="/images/index_1_lt.jpg"/>" width="1002" height="414"></td>
				<%
				
			}
		} else if(telecom.equals("CMC")){
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
					<%
						if(strShortName.equals("jx_dx")){
					%>
						<input name = "deleFlag"  class="form_flag" type="hidden" value = "1">
					<%
						}else{
					%>
						<input name = "deleFlag"  class="form_flag" type="hidden" value = "0">
					<%
						}
					%>
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
					<%if (!LipossGlobals.inArea("hn_lt") && !LipossGlobals.inArea("sx_lt")) { %>	
						<tr>
							<td width="27%" align="right" class="title">登陆域：</td>
							<td width="73%" align="left">
							<input name="area_name" type="text" class="form_kuang" size="20" maxlength="30" value="<%=last_area_name%>">
							</td>						
						</tr>
					<%}else{%>
						<input name="area_name" type="hidden"  hidden="hidden"  value="admin.com">
					<%} %>
					
					<%if (LipossGlobals.inArea("hn_lt") || LipossGlobals.inArea("cq_dx")) { %>
					<tr>
						<td class="title">附加码：</td>
						<td><input name="checkCode" type="text" class="form_kuang" value="" size="6" maxlength="6">
	   						<img id="img" src="<%=codelist.get(1)%>" style="position:absolute;padding: 0px;margin: 0px;"/>
						</td>
					</tr>
					<% }
					else if (LipossGlobals.inArea("sx_lt")){%>
					<tr>
						<td class="title">附加码：</td>
						<td>
						<input name="checkCode" type="text" class="form_kuang" value="" size="6" maxlength="6">&nbsp;<b>
						   <span style="cursor: pointer;background-color: #000000" id="refresh"><font color=#FFFFFF style="font-size: 9pt;line-height: 19px;" id = "check-code">&nbsp;<%=checkCode%>&nbsp;</font></span></b>
						</td>
					</tr>
					<% }
					else if (LipossGlobals.inArea("nmg_dx")){%>
					<tr>
						<td width="27%" align="right" class="title">短信码：</td>
						<td width="73%" align="left">
						   <input style="width:22%" name="checkCode" type="text" class="form_kuang" value="" size="6" maxlength="6">
						   <input style="width:39%" type="button" class="get_vod"  id="get_yzm_btn" onclick="getCheckCode()" value="获取短信码" />
						</td>
					</tr>
					<% }
					else if(LipossGlobals.inArea("sd_lt")){%>
					<tr>
						<td class="title">附加码：</td>
						<td>
                            <div>
                                <input name="checkCode" type="text" class="form_kuang" value="" size="6" maxlength="6">
                                <img style = "position:relative;top:5px"id="vCode" src="validatecode.jsp"  onclick="javascript:document.getElementById('vCode').src='validatecode.jsp?'+Math.random();" />
                            </div>
						</td>
					</tr>
					<% }
					else
					{
					%>
					<tr>
                        <td class="title">附加码：</td>
                        <td><input name="checkCode" type="text" class="form_kuang" value="" size="6" maxlength="6">&nbsp;<b>
                            <span style="background-color: #000000" ><font color=#FFFFFF style="font-size: 9pt;line-height: 19px;">&nbsp;<%=checkCode%>&nbsp;</font></span></b>
                        </td>
                    </tr>
                    <% } %>
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
			<span class="text"><%=strAreaName%>.版权所有;Powered By：亚信科技 版本：V<%=strVersion%> </span>
		<br>
		<!-- <span class="text">请以 IE5.0 以上版本 1024 * 768 分辨率浏览 </span> -->
		</div>
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
