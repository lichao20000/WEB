<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%--
/**
 * ip��ַ�����ip����Ĺ���action
 * 
 * @author ��־��(5194) tel:13701409234
 * @version 1.0
 * @since 2007-11-12
 * @category ipmg
 */
--%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>for ip assgin</title>
</head>
<body>
<s:action name="getSubDetail" namespace="/hgwipMgSys" executeResult="true"
	id="detail">
	<s:param name="attr" value="attr"></s:param>
</s:action>
<s:if test="purForNet!=0">
<div style="margin-left: 1%;width: 98%;color: red;margin-top: 10px;font-size: x-large;">��������;������:<s:property value="purpose1Name"/>|<s:property value="purpose2Name"/>|<s:property value="purpose3Name"/></div>
</s:if>
<s:else>
	<p style="margin-left: 1%;"><a
		href="<s:url value="/hgwipMgSys/unAssignIP!assingIP.action"><s:param name="act">cut</s:param><s:param name="attr" value="attr"></s:param></s:url>">��������</a><span
		style="width: 30px;"></span><a
		href="<s:url value="/hgwipMgSys/unAssignIP!assingIP.action"><s:param name="act">give</s:param>
	<s:param name="attr" value="attr"></s:param><s:param value="#detail.subnetDetail.totaladdr" name="addrnum"/>
	</s:url>">����IP��ַ</a></p>
</s:else>
</body>
</html>
<%@ include file="../foot.jsp"%>