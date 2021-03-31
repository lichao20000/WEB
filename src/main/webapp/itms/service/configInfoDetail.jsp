<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ��������ϸ��Ϣ</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/commFunction.js"/>"></script>
<script type="text/javascript">
	function decodePassword() {
		var isReturn = false;
		var configXml = $("td[@id='temp']").text();
		var pwdStart = configXml.indexOf("<Password>");
		if (pwdStart > 0) {
			var pwdEnd = configXml.indexOf("</Password>");
			if (pwdEnd > pwdStart) {
				var password = configXml.substring(pwdStart + 10, pwdEnd);
				if (password) {
					isReturn = true;
					var url = "configInfoDetailOper.jsp?password=" + password;
					document.all("childFrm").src = url;
				}
			}
		}
		if (!isReturn) {
			$("td[@id='td_password']").text("����Ϊ��");
		}

		<%-- ��¼��־�ӳ�2��ִ�У���Ȼ�������������֮ǰ��ȡ������Ϊ�� --%>
		setTimeout(logSuperAuth, 2000);
	}

	<%-- 2.�û�������������İ�ť�����¼����Ȩ����־ --%>
	function logSuperAuth(){
		superAuthLog('ShowNetSrcPwd',
				'�鿴[<s:property value="deviceSN" />]�豸�ĳ�������['+ $("td[@id='td_password']").text() +']');
	}

</script>

</head>
<body>
<%  if(LipossGlobals.isGSDX()||LipossGlobals.isSXLT()) {%>
<div>
	<table class="listtable">
		<TR>
			<th colspan="4">�豸 <s:property value="deviceSN" /> ��������
			</th>
		</tr>
		<s:if test="configDetailList!=null">
			<s:if test="configDetailList.size()>0">
				<s:iterator value="configDetailList">
					<TR>
						<TD class=column width="15%" align='right'>����ID</TD>
						<TD width="35%"><s:property value="id" /></TD>
						<TD class=column width="15%" align='right'>����ID</TD>
						<TD width="35%"><s:property value="sheet_id" /></TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>ҵ��ID</TD>
						<TD width="85%" colspan="3"><s:property value="service_id" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>�����ϸ����</TD>
						<TD width="85%" colspan="3"><s:property value="fault_desc" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>����ԭ��</TD>
						<TD width="85%" colspan="3"><s:property value="fault_reason" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>���Բ�����Ϣ</TD>
						<TD width="85%" colspan="3" id="temp"><s:property
								value="sheet_para" escapeHtml="false" /></TD>
					</TR>
					<%-- Ŀǰ���ĵ�չʾ�������ǣ�����+���+�г���Ȩ�� --%>
					<ms:inArea areaCode="hb_dx">
					<s:if test="servTypeId=='10'">
					<ms:hasAuth authCode="ShowNetSrcPwd">
					<tr id="tr_password">
						<TD class=column width="15%" align='right'>
							<button onclick="decodePassword()">&nbsp;��������&nbsp;</button>
						</TD>
						<TD width="85%" colspan="3" id="td_password"></TD>
						</td>
					</tr>
					</ms:hasAuth>
					</s:if>
					</ms:inArea>
					<tr>
						<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>ϵͳû��������Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>ϵͳû��������Ϣ!</td>
			</tr>
		</s:else>
	</table>
	<table class="listtable" >
		<s:if test="doServStatusList!=null">
		<thead>
			<tr>
				<th>ҵ���������</th>
				<th>ҵ�����ȡֵ</th>
				<th>����</th>
				<th>ִ�н��</th>
			</tr>
		</thead>
			<s:if test="doServStatusList.size()>0">
				<s:iterator value="doServStatusList">
					<tr>
						<td><s:property value="name" /></td>
						<td style="word-break:break-all" width="400px" ><s:property value="value" /></td>
						<td><s:property value="type"/></td>
						<td><s:property value="result" /></td>
					</tr>
				</s:iterator>
			</s:if>
		</s:if>
	</table>
</div>
<%} else {%>
<table class="querytable">
		<TR>
			<th colspan="4">�豸 <s:property value="deviceSN" /> ��������
			</th>
		</tr>
		<s:if test="configDetailList!=null">
			<s:if test="configDetailList.size()>0">
				<s:iterator value="configDetailList">
					<TR>
						<TD class=column width="15%" align='right'>����ID</TD>
						<TD width="35%"><s:property value="id" /></TD>
						<TD class=column width="15%" align='right'>����ID</TD>
						<TD width="35%"><s:property value="sheet_id" /></TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>ҵ��ID</TD>
						<TD width="85%" colspan="3"><s:property value="service_id" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>�����ϸ����</TD>
						<TD width="85%" colspan="3"><s:property value="fault_desc" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>����ԭ��</TD>
						<TD width="85%" colspan="3"><s:property value="fault_reason" />
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>���Բ�����Ϣ</TD>
						<TD width="85%" colspan="3" id="temp"><s:property
								value="sheet_para" escapeHtml="false" /></TD>
					</TR>
					<%-- Ŀǰ���ĵ�չʾ�������ǣ�����+���+�г���Ȩ�� --%>
					<ms:inArea areaCode="hb_dx">
					<s:if test="servTypeId=='10'">
					<ms:hasAuth authCode="ShowNetSrcPwd">
					<tr id="tr_password">
						<TD class=column width="15%" align='right'>
							<button onclick="decodePassword()">&nbsp;��������&nbsp;</button>
						</TD>
						<TD width="85%" colspan="3" id="td_password"></TD>
						</td>
					</tr>
					</ms:hasAuth>
					</s:if>
					</ms:inArea>
					<tr>
						<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>ϵͳû��������Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>ϵͳû��������Ϣ!</td>
			</tr>
		</s:else>
	</table>
  <%} %>
</body>
