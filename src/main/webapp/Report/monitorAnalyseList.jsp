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
		parent.dyniframesize();
		indexType= <s:property value="indexType" />;
	});
	var indexType;
	function ToExcel(){
		
		var mainForm = window.parent.document.getElementById("selectform");
		mainForm.action="<s:url value='/gwms/report/monitorAnalyse!download.action'/>";
		mainForm.submit();
	}
</script>

</head>

<body>
<table class="listtable">
	<caption>��ѯ���</caption>
	<thead>
		<tr>
			<s:if test="indexType==4">
				<th>������</th>
				<th>������</th>
				<th>ʱ��</th>
			</s:if>
			<s:else>
			
			<s:if test="indexType==2||indexType==3">
			<th>�豸DeviceId</th>
			<th>�豸���к�</th>
			<th>����</th>
			<th>�ͺ�</th>
			</s:if>
			<s:else>
			<th>�豸IP</th>
			</s:else>
			<th>����</th>
			<th>����</th>
			</s:else>
	</tr>
	</thead>
	<tbody>
		<s:if test="monitorInfoList!=null">
			<s:if test="monitorInfoList.size()>0">
				<s:iterator value="monitorInfoList">
					<tr>
						<s:if test="indexType==4">
							<td align="center"><s:property value="instance" /></td>
							<td align="center"><s:property value="value1" /></td>
							<td align="center"><s:property value="gettime" /></td>
						</s:if>
						<s:else>
							<td align="center"><s:property value="device_message" /></td>
						<s:if test="indexType==2||indexType==3">
							<td align="center"><s:property value="device_serialnumber" /></td>
							<td align="center"><s:property value="vendor_add" /></td>
							<td align="center"><s:property value="device_model" /></td>
						</s:if>
							<td align="center"><s:property value="report_times" /></td>
							<td align="center"><s:property value="count_day" /></td>
						</s:else>
				</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td id ="warnMsg_a"colspan=6>û�в�ѯITMS��ط�����Ϣ��</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td id ="warnMsg_b" colspan=6>��ѯITMS��ط�����Ϣ�쳣��</td>
			</tr>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="6" align="right">
					<IMG SRC="/itms/images/excel.gif" BORDER='0' ALT='�����б�'
					style='cursor: hand'
					onclick="ToExcel()">
					
					<lk:pages url="/gwms/report/monitorAnalyse!queryMonitorAnalyse.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
				
			</tr>
			
		</tfoot>
	</tbody>

	

</table>
</body>
</html>