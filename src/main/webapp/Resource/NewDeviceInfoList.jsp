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
		parent.ToExcel();
	}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
	<%if(!"1".equals(gw_type)){ %>
	<caption>ÿ����װ�����б��� </caption>
	<%}else{ %>	
	<caption>ÿ����װ��è����  </caption>
	<%}%>	
	<thead>
		<tr>
			<th>�˺�</th>
			<th>�豸���к�</th>
			<th>�ͺ�</th>
			<th>����</th>
			<th>���һ������ʱ��</th>
			<th>��ʱ��</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="newDevMap!=null">
			<s:if test="newDevMap.size()>0">
				<s:iterator value="newDevMap">
						<tr>
							<td>
								<s:property value="username" />
							</td>
							<td>
								<s:property value="device_serialnumber" />
							</td>
							<td>
								<s:property value="device_model" />
							</td>
							<td>
								<s:property value="vendor_name" />
							</td>
							<td>
								<s:property value="cpe_currentupdatetime" />
							</td>
							<td>
								<s:property value="binddate" />
							</td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=5>û����װ���豸��Ϣ</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=5>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>

		<tfoot>
			<tr>
				<td colspan="11" align="right"><lk:pages
						url="/itms/resource/NewDeviceQuery!NewDeviceQueryInfo.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

			<tr>
				<td colspan="11" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()"></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>