<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<%
	/**
	 *  Ԥ��Ԥ�޸澯��Ϣ
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2014-02-18
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
		$("#trData", parent.document).hide();
		$("#btn", parent.document).attr('disabled', false);
		parent.dyniframesize();
	});
	function queryDetail(taskid) {
		var url = "<s:url value='/ids/httpCustomMadeQuery!getTaskDevList.action'/>?taskId="
				+ taskid;
		window
				.open(url, "",
						"left=60,top=60,width=920,height=500,resizable=yes,scrollbars=yes");

	}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
				<th width="14%">��������</th>
				<th width="14%">������</th>
				<th width="14%">����ʱ��</th>
				<th width="14%">url</th>
				<th width="14%">���Խ���ļ���</th>
				<th width="14%">�������ȼ�</th>
				<th width="14%">����</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="list!=null">
				<s:if test="list.size()>0">
					<s:iterator value="list">
						<tr id="<s:property value="task_id" />">
							<td align="center"><s:property value="task_name" /></td>
							<td align="center"><s:property value="acc_loginname" /></td>
							<td align="center"><s:property value="add_time" /></td>
							<td align="center"><s:property value="url" /></td>
							<td align="center"><s:property value="file_name" /></td>
							<td align="center"><s:property value="level_report" /></td>
							<td align="center"><input type="button" name="btn"
								value="�鿴��ϸ"
								onclick="queryDetail('<s:property value="task_id" />')" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=7>ϵͳû��ƥ�䵽��Ӧ��Ϣ!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7>ϵͳû��ƥ�䵽��Ӧ��Ϣ!</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="7" align="right"><span style="float: right"><lk:pages
							url="/ids/httpCustomMadeQuery!queryTask.action" styleClass=""
							showType="" isGoTo="true" changeNum="true" /></span></td>
			</tr>
			<tr STYLE="display: none">
				<td colspan="7"><iframe id="childFrm" src=""></iframe></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>