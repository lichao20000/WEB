<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��·�ӻ����������б�</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script language="JavaScript">
	function openHgw(oltip,ponid,startTime,endTime) {
		var page = "<s:url value='ids/DegradationPathsQuery!getDegradationPathsInfo.action'/>?"
				+ "&oltip=" + oltip + "&ponid=" + ponid+ "&startTime=" + startTime+ "&endTime=" + endTime;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>
</head>
<body>
	<table class="listtable">
		<caption>��·�ӻ�����������ѯ���</caption>
		<thead>
			<tr>
				<th>����</th>
				<th>������</th>
				<th>���յ�ַ</th>
				<th>OLT����</th>
				<th>OLTIP</th>
				<th>PON�˿�</th>
				<th>�ӻ���¼</th>
			</tr>
		</thead>

		<tbody>
			<s:if test="dataList.size()>0">
				<s:iterator value="dataList">
					<tr>
						<td align="center"><s:property value="area_name" /></td>
						<td align="center"><s:property value="subarea_name" /></td>
						<td align="center"><s:property value="site_name" /></td>
						<td align="center"><s:property value="olt_name" /></td>
						<td align="center"><s:property value="olt_ip" /></td>
						<td align="center"><s:property value="pon_id" /></td>
						<td align="center"><a
							href="javascript:openHgw('<s:property value="olt_ip"/>','<s:property value="pon_id" />','<s:property value="startTime" />','<s:property value="endTime" />')">
								<s:property value="cnt" />
						</a></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tfoot>
					<tr>
						<td align="left" colspan="7">û�й�·�ӻ���������</td>
					</tr>
				</tfoot>
			</s:else>
		</tbody>
		<tfoot>
		</tfoot>
		<tr>
			<td colspan="7" align="right"><lk:pages
					url="/ids/DegradationPathsQuery!getDegradationPaths.action"
					styleClass="" showType="" isGoTo="true" changeNum="true" /></td>

		</tr>
	</table>
</body>
</html>