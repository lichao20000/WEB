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
<title>�淶�汾��ѯ</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	
	$(function(){
		parent.dyniframesize();
	});
function ToExcel(){
		
	//	var form =$('#form', window.parent.document);
	//	var mainForm=parent.document.getElementById("form");
		var mainForm = window.parent.document.getElementById("mainForm");
		mainForm.action="<s:url value="/gtms/resource/deviceVersionInfo!toExcel.action"/>";
		mainForm.submit();
		mainForm.action="";
		
	}
</script>

</head>

<body>
<table class="listtable">
	<caption>��ѯ���</caption>
	<thead>
		<tr>
			<th>�豸����</th>
			<th>�豸�ͺ�</th>
			<th>����汾</th>
			<th>�豸�ն˹��</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deviceVersionList!=null">
			<s:if test="deviceVersionList.size()>0">
				<s:iterator value="deviceVersionList">
					<tr>
						<td><s:property value="vendor_add" /></td>
						<td><s:property value="device_model" /></td>
						<td><s:property value="softwareversion" /></td>
						<td><s:property value="spec_name"/></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>û�в�ѯ����ذ汾��</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>û�в�ѯ����ذ汾��</td>
			</tr>
		</s:else>
	</tbody>

	<tfoot>
		<tr>
			<td colspan="4" align="right">
				<IMG SRC="/itms/images/excel.gif" BORDER='0' ALT='�����б�'
					style='cursor: hand'
					onclick="ToExcel()">
				<lk:pages
				url="/gtms/resource/deviceVersionInfo!queryList.action" styleClass=""
				showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
		
	</tfoot>

</table>
</body>
</html>