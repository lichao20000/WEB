<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ÿ����װ�豸����</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<% 
request.setCharacterEncoding("GBK");

String gw_type = request.getParameter("gw_type");
%>
<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});

	function ToExcel() {
		var a = $("#bind_total").val();
		if(a > 100000){
			alert("���ݳ���10w��������������������ѡ���С��ʱ��Σ�");
			return;
		}else{
			parent.excelToFile();
		}
	}
</script>
</head>
<body>
	<input type="hidden" id="bind_total" value="<s:property value="total"/>" />
	<table class="listtable" id="listTable">
	<%if(!"1".equals(gw_type)){ %>
	<caption>������è��¼ </caption>
	<%}else{ %>	
	<caption>������è��¼ </caption>
	<%}%>	
	<thead>
		<tr>
			<th align="center">ҵ���˺�</th>
			<th align="center">�ɳ���</th>
			<th align="center">���ͺ�</th>
			<th align="center">��SN</th>
			<th align="center">��mac��ַ</th>
			
			<th align="center">�³���</th>
			<th align="center">���ͺ�</th>
			<th align="center">��SN</th>
			<th align="center">��mac��ַ</th>
			 
			<th align="center">��ʱ��</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deviceList!=null">
			<s:if test="deviceList.size()>0">
				<s:iterator value="deviceList">
						<tr>
							<td class=column1><s:property value="username"/></td>
							<td class=column1><s:property value="old_vendor_name"/></td>
							<td class=column1><s:property value="old_device_model"/></td>
							<td class=column1><s:property value="old_devsn"/></td>
							<td class=column1><s:property value="old_mac"/></td>
							
							<td class=column1><s:property value="vendor_name"/></td>
							<td class=column1><s:property value="device_model"/></td>
							<td class=column1><s:property value="new_devsn"/></td>
							<td class=column1><s:property value="new_mac"/></td>
							
							<td class=column1><s:property value="binddate"/></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=14>û�и������豸��Ϣ</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=14>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>

		<tfoot>
			<tr>
				<td colspan="14" align="right"><lk:pages
						url="/gtms/stb/resource/stbBindChage!queryTabBindList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

			<tr>
				<td colspan="14" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()">(������ҹ��22:00֮�����!)</td>
			</tr>
		</tfoot>
	</table>
</body>
</html>