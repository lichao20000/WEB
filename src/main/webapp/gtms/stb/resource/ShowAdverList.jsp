<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������·������ѯ</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	$(function() {
		parent.document.selectForm.queryButton.disabled = false;
		parent.dyniframesize();
	})
	
	function getTaskDetail(taskId) {
		var page = "<s:url value='/gtms/stb/resource/showAdver!getShowAdverDetail.action'/>?taskId="+taskId;
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
	};

</SCRIPT>

</head>
<body>
	<table width="98%" class="listtable" align="center">
		<thead>
			<tr>
				<th align="center" width="8.3%">����</th>
				<th align="center" width="8.3%">����</th>
				<th align="center" width="8.3%">�豸�ͺ�</th>
				<th align="center" width="8.3%">����汾</th>
				<th align="center" width="12.3%">�豸���к�</th>
				<th align="center" width="8.3%">MAC</th>
				<th align="center" width="8.3%">ҵ���˺�</th>
				<th align="center" width="8.3%">�豸IP</th>
				<th align="center" width="8.3%">���ý��</th>
				<th align="center" width="12.6%">����ʱ��</th>
				<th align="center" width="8.3%">ִ�в�������</th>
			</tr>
		</thead>
		<s:if test="tasklist!=null">
			<s:if test="tasklist.size()>0">
				<tbody>
					<s:iterator value="tasklist">
						<tr>
							<td align="center"><s:property value="cityName" /></td>
							<td align="center"><s:property value="vendorName" /></td>
							<td align="center"><s:property value="deviceModel" /></td>
							<td align="center"><s:property value="deviceTypeName" /></td>
							<td align="center"><s:property value="deviceSerialNumber" /></td>
							<td align="center"><s:property value="cpe_mac" /></td>
							<td align="center"><s:property value="servAccount" /></td>
							<td align="center"><s:property value="loopback_ip" /></td>
							<td align="center"><s:property value="result" /></td>
							<td align="center"><s:property value="update_time" /></td>
							<td align="center"><a href="javascript:void(0);"
								onclick="getTaskDetail('<s:property value="task_id" />')"><s:property
										value="task_name" /></a></td>
						</tr>
					</s:iterator>
				</tbody>
				<tfoot>
					<tr bgcolor="#FFFFFF">
						<td colspan="11" align="right"><lk:pages
								url="/gtms/stb/resource/showAdver!adverResultList.action"
								styleClass="" showType="" isGoTo="true" changeNum="false" /></td>
					</tr>
				</tfoot>
			</s:if>
			<s:else>
				<tbody>
					<TR>
						<TD colspan="11"><font color="red">û�ж��ƵĿ�������·����</font></TD>
					</TR>
				</tbody>
			</s:else>
		</s:if>
	</table>
</body>