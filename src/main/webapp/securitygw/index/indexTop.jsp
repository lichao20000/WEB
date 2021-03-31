<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%--
/**
 * 该jsp是为了安全网关使用的
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2008-03-26
 * @category security
 *
 *
 */
--%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>The top of index for chinese</title>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT type="text/javascript">
function goLocation(to)
{
	var url = "<s:url value="/webtopo/GetTopoTree.action"/>"+"?type=0/0";
	goMenu(url);

	parent.bottom.all.rightbottom.src=to;
}
function goMenu(to)
{
parent.bottom.all.leftbottom.src=to;
parent.bottom.cols="215px,15px,*";
}
//显示告警页面
function showAlarm(url)
{
	parent.bottom.all.rightbottom.src=url;
}
function getCount()
{
var url ="<s:url value="/model_vip/Top!ajaxCount.action"/>"+"?ajax=true"
$.ajax({ type: "POST",
			url: url,
			success:
				function(data)
				{
					var res = data.replace(/(^\s*)|(\s*$)/g,"");
					if(res=="authError")
					{
						parent.bottom.all.rightbottom.contentWindow.alert("您的会话已经失效，请重新登录\nSorry,your session is invalid,please relogin!");
						<s:url var="loginout" value="/model_vip/GoPosition.action" includeParams="all">
							<s:param name="position">
								<s:url value="/public/Login.action?lout=1" />
							</s:param>
						</s:url>
					top.window.location="<s:property value="%{loginout}"/>";
					}else
					{
					$("#serWarnCnt").text(res.split("*")[0]);
					$("#norWarnCnt").text(res.split("*")[1]);
					$("#proWarnCnt").text(res.split("*")[2]);
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
$(function(){
		window.setInterval(getCount,15000);
		$("td[@name='tab']").hover(function(){
			$(this).addClass("sec_buttonover").removeClass("sec_button");
			},
			function(){
				if($(this).attr("class").indexOf("default")==-1)
				{
					$(this).addClass("sec_button").removeClass("sec_buttonover");
				}
			});
		$("td[@name='tab']").click(function(){
				$("td[@name='tab']").removeClass("sec_buttonover sec_button default").addClass("sec_button");
				$(this).addClass("sec_buttonover default").removeClass("sec_button");
			});
});
</SCRIPT>
<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet"
	type="text/css">
<style type="text/css">
<!--
@import url("<s:url value="/model_vip/css/css_ico.css"/>");
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td background="<s:url value="/model_vip/images/title_back.jpg"/>">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<s:if test="#session['WW_TRANS_I18N_LOCALE']==null">
					<td width="462" height="44"><img src="./images/security_title.jpg"
						width="462" height="44"></td>
				</s:if>
				<s:elseif
					test="#session['WW_TRANS_I18N_LOCALE'].toString()=='zh_CN'">
					<td width="462" height="44"><img src="./images/security_title.jpg"
						width="462" height="44"></td>
				</s:elseif>
				<s:elseif
					test="#session['WW_TRANS_I18N_LOCALE'].toString()=='en_US'">
					<td width="462" height="44"><img src="./images/security_title.jpg"
						width="462" height="44"></td>
				</s:elseif>
				<td>
				<div align="right">
				<table width="300" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<s:if test="#session['WW_TRANS_I18N_LOCALE']==null">
							<s:url id="goindex" value="/model_vip/GoPosition.action"
								includeParams="all">
								<s:param name="position">
									<s:url value="/securitygw/index.jsp" />
								</s:param>
							</s:url>
							<td><s:a href="%{goindex}">
								<img src="<s:url value="/model_vip/images/button_home.gif"/>"
									width="81" height="22" border="0">
							</s:a></td>
							<s:url id="loginout" value="/model_vip/GoPosition.action"
								includeParams="all">
								<s:param name="position">
									<s:url value="/public/Login.action?lout=1" />
								</s:param>
							</s:url>
							<td><s:a href="%{loginout}">
								<img src="<s:url value="/model_vip/images/button_return.gif"/>"
									width="81" height="22" border="0">
							</s:a></td>
							<td><a href="javascript:this.parent.close();"><img
								src="<s:url value="/model_vip/images/button_out.gif"/>"
								width="81" height="22" border="0"></a></td>
						</s:if>
						<s:elseif
							test="#session['WW_TRANS_I18N_LOCALE'].toString()=='zh_CN'">
							<s:url id="goindex" value="/model_vip/GoPosition.action"
								includeParams="all">
								<s:param name="position">
									<s:url value="/model_vip/index.jsp" />
								</s:param>
							</s:url>
							<td><s:a href="%{goindex}">
								<img src="<s:url value="/model_vip/images/button_home.gif"/>"
									width="81" height="22" border="0">
							</s:a></td>
							<s:url id="loginout" value="/model_vip/GoPosition.action"
								includeParams="all">
								<s:param name="position">
									<s:url value="/public/Login.action?lout=1" />
								</s:param>
							</s:url>
							<td><s:a href="%{loginout}">
								<img src="<s:url value="/model_vip/images/button_return.gif"/>"
									width="81" height="22" border="0">
							</s:a></td>
							<td><a href="javascript:this.parent.close()"><img
								src="<s:url value="/model_vip/images/button_out.gif"/>"
								width="81" height="22" border="0"></a></td>
						</s:elseif>
						<s:elseif
							test="#session['WW_TRANS_I18N_LOCALE'].toString()=='en_US'">
							<s:url id="goindex" value="/model_vip/GoPosition.action"
								includeParams="all">
								<s:param name="position">
									<s:url value="/securitygw/index.jsp" />
								</s:param>
							</s:url>
							<td><s:a href="%{goindex}">
								<img src="<s:url value="/model_vip/images/button_home_e.gif"/>"
									width="81" height="22" border="0">
							</s:a></td>
							<s:url id="loginout" value="/model_vip/GoPosition.action"
								includeParams="all">
								<s:param name="position">
									<s:url value="/public/Login.action?lout=1" />
								</s:param>
							</s:url>
							<td><s:a href="%{loginout}">
								<img
									src="<s:url value="/model_vip/images/button_return_e.gif"/>"
									width="81" height="22" border="0">
							</s:a></td>
							<td><a href="javascript:this.parent.close()"><img
								src="<s:url value="/model_vip/images/button_out_e.gif"/>"
								width="81" height="22" border="0"></a></td>
						</s:elseif>
						<td>&nbsp;</td>
					</tr>
				</table>
				</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td nowrap
			background="<s:url value="/model_vip/images/title2_back2.jpg"/>">
		<table width="98%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="724" height="28" valign="bottom"
					background="<s:url value="/model_vip/images/title2_back.jpg"/>">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="232">
						<table width="208" height="20" border="0" cellpadding="0"
							cellspacing="0">
							<tr>
								<td name="tab" width="52" class="sec_button"
									onclick="goMenu('<s:url value="/model_vip/GetMenu.action"/>')"><img
									src="<s:url value="/securitygw/images/arrow02.gif"/>" width="3" height="5">菜单</td>
								<td name="tab" width="52" class="sec_button"
									onclick="goLocation('../webtopo/main_vip.jsp')"><img
									src="<s:url value="/securitygw/images/arrow02.gif"/>" width="3" height="5">网络</td>
								<td name="tab" width="52" class="sec_button"
									onclick="goMenu('<s:url value="/model_vip/SDAMenu.action"/>')"><img
									src="<s:url value="/securitygw/images/arrow02.gif"/>" width="3" height="5">机房</td>
								<td name="tab" width="52" class="sec_buttonover default"
									onclick="goMenu('<s:url value="/model_vip/SGWMenu.action"/>')"><img
									src="<s:url value="/securitygw/images/arrow02.gif"/>" width="3" height="5">安全</td>
							</tr>
						</table>
						</td>
						<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="80" class="text_white"><s:text name="CURRWARN"></s:text>：</td>
								<td class="back_red"><a class="white" href="javascript:showAlarm('<s:url value="/work/realTimeAlarmShow.jsp"><s:param name="alrf" value="4"/></s:url>');"><s:text name="GRAVE_ALARM" />:<span
									id="serWarnCnt"><s:property value="serWarnCnt" /></span></a></td>
								<td width="8">&nbsp;</td>
								<td class="back_org"><a class="white" href="javascript:showAlarm('<s:url value="/work/realTimeAlarmShow.jsp"><s:param name="alrf" value="2"/></s:url>');"><s:text name="GENERAL_ALARM" />:<span
									id="norWarnCnt"><s:property value="norWarnCnt" /></span></a></td>
								<td width="8">&nbsp;</td>
								<td class="back_yel"><a class="black" href="javascript:showAlarm('<s:url value="/work/realTimeAlarmShow.jsp"><s:param name="alrf" value="1"/></s:url>');"><s:text name="HINT_ALARM" />:<span
									id="proWarnCnt"><s:property value="proWarnCnt" /></span></a></td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
				<td nowrap>
				<div align="right" class="text_bblue"><s:text name="USER" />:<s:property
					value="userName" /> <s:text name="AREA" />:<s:property
					value="area" /> <s:text name="DEV_CNT" />:<s:property
					value="deviceCnt" /></div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>
