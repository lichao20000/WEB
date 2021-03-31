<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ʧ����ϸ�����ѯ</title>
<%
	/**
	 * ʧ����ϸ�����ѯ
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-09-08
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
		parent.dyniframesize();
	});
	
	function ToExcel(){
			parent.ToExcel();
	}
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<thead>
		<tr>
			<th>��������</th>
			<th>�豸�ͺ�</th>
			<th>�豸���к�</th>
			<th>�豸�߼���</th>
			<th>�·�ʱ��</th>
			<th>��������</th>
			<th>������ϸ����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="failInfoList!=null">
			<s:if test="failInfoList.size()>0">
				<s:iterator value="failInfoList">
						<tr>
							<td><s:property value="city_name" /></td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="device_id_ex" /></td>
							<td><s:property value="times" /></td>
							<td><s:property value="fault_desc" /></td>
							<td><s:property value="fault_reason" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>ϵͳû�и��û����·�ʧ����Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="9" align="right">
		 	<lk:pages
				url="/gtms/report/configFail!queryConfigInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>

		<tr>
			<td colspan="9" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>