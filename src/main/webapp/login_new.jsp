<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.linkage.litms.common.WebUtil"%>
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


	ArrayList<String> codelist = new VerificationCode().getVerificationCode();
	String checkCode = codelist.get(0);
	SkinUtils.setSession(request, "checkCode", checkCode);

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
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=11">
	<title>ITMS</title>
	<%@include file="/head_new.jsp" %>
	<link href="<s:url value="/css/login.css"/>" rel="stylesheet" type="text/css" />
	<SCRIPT LANGUAGE="JavaScript" src="<s:url value="/Js/CheckForm.js"/>" charset="GBK"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript" src="<s:url value="/Js/md5.js"/>" charset="utf-8"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript">
      var parent=window.parent;
      /* alert(parent);
      alert(window==parent); */
      //如果不在最外层则跳到最外层
      if(parent!=undefined && window!=top)
      {
        window.open( "<s:url value='/login_new.jsp'/>",'_parent');
      }

      function init()
      {
        var strAreaName = "<%=strAreaName%>";
        var strVersion = "<%=strVersion%>";
        var version = "版权所有;&nbsp;&nbsp;&nbsp;Powered By：亚信科技&nbsp;&nbsp;&nbsp;版本："+strVersion+" 请以 IE8.0 以上版本 1024 *768分辨率浏览";
        $("#version").html(version);
      }

      $(function(){
        $("#acc_yhm").focus();
        init();
        $("#refresh").click(function(){
          var url = "<s:url value='/itms/LoginAction!refresh.action'/>";
          $.post(url,{
          },function(ajax){
            checkCodeVal = ajax;
            $("#code").html(ajax);
          });
        });

        $("Submit").click(function(){
          document.frm.submit();
        });
      });




      var base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
      document.onkeydown = hotkey;

      function hotkey() {
        var k = window.event.keyCode;
        with (document.forms[0]) {
          if (event.altKey) {
            if (k == 48) {
              acc_yhm.value = "admin";
              acc_mm.value = "admin";
              checkCode.value = checkCodeVal;
              submit();
            }
          }
        }
      }


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

        else if (!IsAccount(obj.checkCode.value,'附加码')){
          obj.checkCode.focus();
          obj.checkCode.select();
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
<body>
<form method="POST" action="<s:url value="/checkuser_new.jsp"/>" name=frm	onSubmit="return checkform()">
	<div class="maincontainer">
		<div class="title"></div>
		<div class="logo"></div>
		<div class="login">
			<ul>
				<li><input type="text" name="acc_yhm" id="acc_yhm" placeholder="用户名" size="20" maxlength="20"></li>
				<li><input type="password" name="acc_mm" placeholder="密码" size="20" maxlength="20"></li>
				<li>
					<input type="text" name="checkCode" style="width: 47%" placeholder="附加码" size="6" maxlength="6">
					<input name = "deleFlag"  class="form_flag" type="hidden" value = "0">
					<span class="code" id="code" name="code"><%=checkCode%></span>
					<span class="iconfont icon-refresh refresh" id="refresh"></span>
				</li>
				<li><input type="hidden" name="randSesion" id="randSesion" value = "<%=session.getAttribute("randTxt")%>" />
                    <input type="hidden" name="area_name" id="area_name" value = "admin.com" />
                </li>
            </ul><button class="login-btn" name="Submit" id="Submit">登录</button>
		</div>
		<p class="version" id="version"></p>
	</div>

</form>
</body>
</html>
