<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>查看告警过滤规则详情</title>
		<%
			/**
			 * WebTopo实时告警牌告警模板详情展示页面
			 * <li>REQ: GZDX-REQ-20080402-ZYX-001
			 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
			 *
			 * @author	段光锐
			 * @version 1.0
			 * @since	2008-4-11
			 * @category	WebTopo/实时告警牌/告警规则
			 *
			 */
		%>
		<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
		<script language="javascript" type="text/javascript"><!--
		$(function(){
			$("#goBackButton").click(function (){goBack();});
		});
		function goBack(){
			window.location="<s:url value='/webtopo/warnRuleDef.action'/>";
		}
		--></script>
	</head>
	<body>
		<form name="frm" method ="post">
		<br>
			<table width="90%" height="30" border=0 align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
				<tr>
					<td width="162" align="center"  class="title_bigwhite">查看告警过滤规则</td>
					<td>
					当前查看模板名称:<font color="red"><s:property value="ruleName"/></font> 所属用户:<font color="red"><s:property value="userName"/></font>
					</td>
				</tr>
			</table>
			<table width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table" bgcolor="#000000">
				<tr>
					<th nowrap>优先级</th>
					<th>规则内容</th>
					<th nowrap>是否启用</th>
				</tr>
				<s:iterator value="resultList" var="rt"  status="status">
				<tr>
					<td class="column text"  align="center"><s:property value="#rt.rule_priority"/></td>
					<td class="column text"><s:property value="#rt.rule_content"/></td>
					<td class="column text"  align="center" nowrap>
						<s:if test="#rt.rule_invocation == 1">启用</s:if>
						<s:else>禁用</s:else>
					</td>
				</tr>
				</s:iterator>
				<tr>
					<td colspan="3" class="foot" align="left">
						<input type="button" id="goBackButton" title="返回告警过滤规则模板页面" class="jianbian" value="返  回" />
						<s:if test="resultList==null || resultList.size==0">
							<span style="width:20%;"></span><font color="red">未定义告警规则详情</font>
						</s:if>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
