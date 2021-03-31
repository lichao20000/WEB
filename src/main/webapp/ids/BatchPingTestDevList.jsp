<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<%
	/**
	 *  Ԥ��Ԥ�޸澯��Ϣ
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2014-02-18
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
$(function() {
	$("#trData",parent.document).hide();
	$("#btn",parent.document).attr('disabled',false);
	parent.dyniframesize();
});
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
				<th width="10%">��������</th>
				<th width="10%">������</th>
				<th width="10%">������</th>
				<th width="10%">����</th>
				<th width="10%">�豸����</th>
				<th width="10%">�ͺ�</th>
				<th width="15%">�豸���к�</th>
				<th width="10%">Ӳ���汾</th>
				<th width="10%">����汾</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="list!=null">
				<s:if test="list.size()>0">
					<s:iterator value="list">
						<tr>
							<td align="center"><s:property value="task_name" /></td>
							<td align="center"><s:property value="task_id" /></td>
							<td align="center"><s:property value="acc_loginname" /></td>
							<td align="center"><s:property value="city_name" /></td>
							<td align="center"><s:property value="vendor_name" /></td>
							<td align="center"><s:property value="device_model" /></td>
							<td align="center"><s:property value="device_serialnumber" /></td>
							<td align="center"><s:property value="hardwareversion" /></td>
							<td align="center"><s:property value="softwareversion" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=9>ϵͳû��ƥ�䵽��Ӧ��Ϣ!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>ϵͳû��ƥ�䵽��Ӧ��Ϣ!</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="9" align="right"><lk:pages
						url="/ids/batchPingTest!getTaskDevList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

		</tfoot>
	</table>
</body>
</html>