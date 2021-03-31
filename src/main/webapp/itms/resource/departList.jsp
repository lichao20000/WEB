<%@  page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���Ų�ѯ�б�</title>
<link rel="stylesheet" href="<s:url value='/css3/c_table.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css3/global.css'/>" type="text/css">
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript" src="<s:url value='/Js/jQuerySplitPage-linkage.js'/>"></script>

<script type="text/javascript">
$(function(){
	parent.dyniframesize();
});
</script>
</head>

<body>
	<table class="listtable">
		<caption>��ѯ���</caption>
		<thead>
			<tr>
				<th width="20%">����</th>
				<th width="25%">����</th>
				<th width="15%">����ʱ��</th>
				<th width="20%">������</th>
				<th width="20%">����</th>
			</tr>
		</thead>
		<tbody>
		
			<s:if test="departList!=null">
				<s:if test="departList.size()>0">
					<s:iterator value="departList">
						<tr>
							<td><s:property value="depart_name" /></td>
							<td><s:property value="depart_desc" /></td>
							<td style="text-align:center;"><s:property value="depart_time" /></td>
							<td style="text-align:center;"><s:property value="acc_loginname" /></td>
							<td style="text-align:center;">
								<a href="javascript:parent.delDeaprt('<s:property value="depart_id" />')">ɾ��</a>|
								<a href="javascript:parent.editDeaprt('<s:property value="depart_id" />',
																	'<s:property value="depart_name" />',
																	'<s:property value="depart_desc" />')">�༭</a>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=11>û�в�ѯ������豸��</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>û�в�ѯ������豸��</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="11" align="right">
					<lk:pages url="/itms/resource/departManage!queryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true"  />
				</td>
			</tr>
		</tfoot>

	</table>
</body>
</html>