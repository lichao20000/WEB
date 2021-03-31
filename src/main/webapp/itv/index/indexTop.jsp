<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * 登录成功后的上边抬头页面
 * @author 王志猛(5194)
 * @version 1.0
 * @since 2008-8-14 下午03:53:30
 *
 * 版权：南京联创科技 网管科技部
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>流量行为分析抬头页面</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript">
//返回首页
function gohome()
{
	top.location.href="<s:url value="/lims/index.jsp"/>";
}
//重新登陆
function relogin()
{
	top.location.href="<s:url value="/FBA/FBA/login!relogin.action"/>";
}
//退出系统
function loginout()
{
	top.location.href="<s:url value="/FBA/FBA/login!loginOut.action"/>";
}
</script>
</head>

<body>
<form name="frm" action="">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<input type="hidden" name="test" value="test">
	<tr>
		<td background="<s:url value="/images/title_back.jpg"/>">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="462" height="44"><img
					src="<s:url value="/images/title_aof.jpg"/>" width="462"
					height="44"></td>
				<td>
				<div align="right">
				<table width="300" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><a href="#" onclick="gohome()"><img
							src="<s:url value="/images/button_home.gif"/>" width="81"
							height="22" border="0"></a></td>
						<td><a href="#" onclick="relogin()"><img
							src="<s:url value="/images/button_return.gif"/>" width="81"
							height="22" border="0"></a></td>
						<td><a href="#" onclick="loginout()"><img
							src="<s:url value="/images/button_out.gif"/>" width="81"
							height="22" border="0"></a></td>
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
		<td nowrap background="<s:url value="/images/title2_back2.jpg"/>">
		<table width="98%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="780" height="28" nowrap>
					<div align="left" class="text_bblue">
						&nbsp;&nbsp;<img src="<s:url value="/images/odong21_30.gif"/>" width="18" height="18">&nbsp;&nbsp;
						<span id="bar"></span>
					</div>
				</td>
				<td nowrap>
				<div align="right" class="text_bblue">用户:<s:property
					value="userName" /> 域:<s:property value="area" /></div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>