<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>�鿴�澯���˹�������</title>
		<%
			/**
			 * WebTopoʵʱ�澯�Ƹ澯ģ������չʾҳ��
			 * <li>REQ: GZDX-REQ-20080402-ZYX-001
			 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
			 *
			 * @author	�ι���
			 * @version 1.0
			 * @since	2008-4-11
			 * @category	WebTopo/ʵʱ�澯��/�澯����
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
					<td width="162" align="center"  class="title_bigwhite">�鿴�澯���˹���</td>
					<td>
					��ǰ�鿴ģ������:<font color="red"><s:property value="ruleName"/></font> �����û�:<font color="red"><s:property value="userName"/></font>
					</td>
				</tr>
			</table>
			<table width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table" bgcolor="#000000">
				<tr>
					<th nowrap>���ȼ�</th>
					<th>��������</th>
					<th nowrap>�Ƿ�����</th>
				</tr>
				<s:iterator value="resultList" var="rt"  status="status">
				<tr>
					<td class="column text"  align="center"><s:property value="#rt.rule_priority"/></td>
					<td class="column text"><s:property value="#rt.rule_content"/></td>
					<td class="column text"  align="center" nowrap>
						<s:if test="#rt.rule_invocation == 1">����</s:if>
						<s:else>����</s:else>
					</td>
				</tr>
				</s:iterator>
				<tr>
					<td colspan="3" class="foot" align="left">
						<input type="button" id="goBackButton" title="���ظ澯���˹���ģ��ҳ��" class="jianbian" value="��  ��" />
						<s:if test="resultList==null || resultList.size==0">
							<span style="width:20%;"></span><font color="red">δ����澯��������</font>
						</s:if>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
