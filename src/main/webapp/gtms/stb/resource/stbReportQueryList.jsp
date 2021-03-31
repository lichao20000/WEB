<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%String absPath=request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��ѯ���</title>
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
	 
	function ToExcel()
	{
		var sure = window.confirm("1.��֧�ִ�����������,�����WEB�쳣!\n2.��Ҫ����,�������ڲ���!\n�Ƿ��������");
		if(!sure)
		{
			return;
		}
		var form = parent.document.getElementById("gwShare_selectForm");
		form.action="<s:url value='/gtms/stb/resource/stbReport!getExcel.action'/>";
		form.submit();
	} 
	</script>

</head>
<body>
	<table class="listtable" id="listTable">
		<caption>��ѯ���</caption>
		<thead>
			<tr>
				<th>�豸����</th>
				<th>�ͺ�</th>
				<th>����汾</th>
				<!-- <th>ITMS����ʱ��</th> -->
				<th>����</th>
				<th>�豸���к�</th>
				<th>ҵ���˺�</th>
				<th>MAC</th>
				<th>�豸IP</th>
				<th>����ϱ�ʱ��</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="data!=null">
				<s:if test="data.size()>0">
					<s:iterator value="data">
						<tr>
							<td><s:property value="vendor_name" /></td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="softwareversion" /></td>
							<td><s:property value="city_name" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="serv_account" /></td>
							<td><s:property value="cpe_mac" /></td>
							<td><s:property value="loopback_ip" /></td>
							<td><s:property value="complete_time" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=9>ϵͳ��û�в�ѯ����Ҫ����Ϣ!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>ϵͳ��û�в�ѯ����Ҫ����Ϣ!</td>
				</tr>
			</s:else>
			<tr bgcolor="#FFFFFF">
				<td colspan="9" align="left"><IMG SRC="/itms/images/excel.gif"
					BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()"></td>
			</tr>
		</tbody>
		<tfoot>
			<tr bgcolor="#FFFFFF">

				<td colspan="9" align="right"><lk:pages
						url="/gtms/stb/resource/stbReport!query.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
			<tr STYLE="display: none">
				<td><iframe id="childFrm" src=""></iframe></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>