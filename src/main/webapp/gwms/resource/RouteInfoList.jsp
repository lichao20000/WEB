<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>·����Ϣ��ѯ</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	
	$(function(){
		parent.$("input[@name='gwShare_queryButton']").attr("disabled",false);
		parent.dyniframesize();
	});

	function ToExcel(){
		
	//	var form =$('#form', window.parent.document);
	//	var mainForm=parent.document.getElementById("form");
		var mainForm = window.parent.document.getElementById("mainForm");
		mainForm.action="<s:url value="/gwms/resource/routeInfoQuery!toExcel.action"/>";
		mainForm.submit();
	}
</script>

</head>

<body>
<table class="listtable">
	<caption>��ѯ���</caption>
	<thead>
		<tr>
			<th>����</th>
			<th>����</th>
			<th>����</th>
			<th>�ͺ�</th>
			<th>����汾</th>
			<th>Ӳ���汾</th>
			<th>�豸���к�</th>
			<th>�߼�ID</th>
			<th>����˺�</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="routeInfoList!=null">
			<s:if test="routeInfoList.size()>0">
				<s:iterator value="routeInfoList">
					<tr>
						<td align="center"><s:property value="cityName" /></td>
						<td align="center"><s:property value="city_name" /></td>
						<td align="center"><s:property value="vendor_add" /></td>
						<td align="center"><s:property value="device_model" /></td>
						<td align="center"><s:property value="softwareversion" /></td>
						<td align="center"><s:property value="hardwareversion" /></td>
						<td align="center"><s:property value="device_serialnumber" /></td>
						<td align="center"><s:property value="loid" /></td>
						<td align="center"><s:property value="kdname" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>û�в�ѯ����ذ汾��</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9>û�в�ѯ����ذ汾��</td>
			</tr>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="9" align="right">
					<IMG SRC="/itms/images/excel.gif" BORDER='0' ALT='�����б�'
					style='cursor: hand'
					onclick="ToExcel()">
					<lk:pages url="/gwms/resource/routeInfoQuery!queryRouteInfo.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
		</tfoot>
	</tbody>

	

</table>
</body>
</html>