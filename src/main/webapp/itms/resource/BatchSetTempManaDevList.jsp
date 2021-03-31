<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<%--
	/**
	 * �淶�汾��ѯ�б�ҳ��
	 *
	 * @author ����(����) Tel:�绰
	 * @version 1.0
	 * @since ${date} ${time}
	 * 
	 * <br>��Ȩ���Ͼ������Ƽ� ���ܿƼ���
	 * 
	 */
 --%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸�б�</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	function ToExcel() {
		var countNum='<s:property value="countNum" />';
		if(countNum>100000){
			var choice = window.confirm("������������10W,�����鵼��(ȷ������)");
			if(choice==true){
				var task_id='<s:property value="task_id" />';
				var type='<s:property value="type" />';
				var page="<s:url value='/itms/resource/batchSetTempManaACT!queryDevListExcel.action'/>"+"?task_id="+task_id+"&&type="+type;
				document.all("childFrm").src=page;
			}
			else{
				return false;
			}
		}
		else{
			var task_id='<s:property value="task_id" />';
			var city_id='<s:property value="city_id" />';
			var type='<s:property value="type" />';
			var page="<s:url value='/itms/resource/batchSetTempManaACT!queryDevListExcel.action'/>"+"?task_id="+task_id+"&&type="+type;
			document.all("childFrm").src=page;
		}
	}
	

</script>

</head>

<body >
<table class="listtable">
	<caption>��ѯ���</caption>
	<thead>
		<tr>
			<th>OUI</th>
			<th>�豸���к�</th>
			<th>IP��ַ</th>
			<th>����</th>
			<th>�ͺ�</th>
			<th>����汾</th>
			<th>����</th>
			<th>���</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="taskList!=null">
			<s:if test="taskList.size()>0">
				<s:iterator value="taskList">
					<tr>
						<td align="center"><s:property value="oui" /></td>
						<td align="center"><s:property value="device_serialnumber" /></td>v
						<td align="center"><s:property value="loopback_ip" /></td>
						<td align="center"><s:property value="vendor_id" /></td>
						<td align="center"><s:property value="device_model_id" /></td>
						<td align="center"><s:property value="version" /></td>
						<td align="center"><s:property value="city_name" /></td>
						<td align="center"><s:property value="status" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>û�в�ѯ�������Ϣ��</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>û�в�ѯ�������Ϣ��</td>
			</tr>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="10" align="right">
					<lk:pages url="/itms/resource/batchSetTempManaACT!queryDevList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
			<tr>
				<td colspan="10" align="left">
					<IMG
					SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()">����
				</td>
			</tr>
			<tr STYLE="display: none">
				<td>
					<iframe id="childFrm" src=""></iframe>
				</td>
			</tr>
		</tfoot>
	</tbody>

	

</table>
</body>
</html>