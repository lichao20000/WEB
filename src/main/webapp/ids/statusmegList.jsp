<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<script type="text/javascript" src="../Js/jquery.js"/></script>
<html>
	<script type="text/javascript">
		$(function() {
		parent.dyniframesize();
	});
		function openHgw(taskId){
			var page="<s:url value='/ids/idsStatusQuery!getDevInfo.action'/>?taskId=" + taskId ;
			window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
		}
function warnMsg(){
	$("#trData",parent.document).css('display','none');
}
	</script>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>״̬��Ϣ��������Ϣ�б�</title>

		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
	</head>
	<body onload="warnMsg()">
	<s:if test="ajax!='���Ƴɹ�'">
	<font <s:if test="ajax.contains('���ζ����豸�������ɳ���1000��')">style="font-weight: 600;color: red;"</s:if>><s:property value="ajax"/></font>
	</s:if>
	<s:else>
		<table width="98%" border=0 cellspacing=1 cellpadding=0 class="listtable">
			<caption>
				������Ϣ��ѯ���
			</caption>
			<thead>
				<tr>
					<th> ������ </th>
					<th> ����ʱ�� </th>
					<th> ������ر� </th>
					<th> �ϱ����� </th>
					<th> �ļ��ϴ�����</th>
					<th> �˿� </th>
					<th> ��ز��� </th>
					<th> �豸�� </th>
				</tr>
			</thead>

			<tbody>
				<s:if test="IdsList.size()>0">
					<s:iterator value="IdsList" var="map1">
						<tr>
							<td align="center">
								<s:property value="acc_loginname" />
							</td>
							<td align="center">
								<s:property value="add_time" />
							</td>
							<td align="center">
								<s:if test="enable==1">����</s:if>
								<s:if test="enable==0">�ر�</s:if>
							</td>
							<td align="center">
								<s:property value="timelist" />
							</td>
							<td align="center">
								<s:property value="serverurl" />
							</td>
							<td align="center">
								<s:property value="tftp_port" />
							</td>
							<td align="center">
								<s:property value="paralist" />
							</td>
							<td align="center">
								<a href="javascript:openHgw('<s:property value="task_id"/>')">
									<s:property value="devCount" /> </a>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tfoot>
						<tr>
							<td align="left" colspan="8">
								û�ж�����Ϣ
							</td>
						</tr>
					</tfoot>
				</s:else>
			</tbody>
			<tfoot>
			</tfoot>
		</table>
		</s:else>
		<br/>
		<s:if test="ajax.contains('���ζ����豸�������ɳ���1000��')">
		<table width="98%" border=0 cellspacing=1 cellpadding=0 class="listtable">
			<caption>
				������Ϣ��ѯ���
			</caption>
			<thead>
				<tr>
					<th> ������ </th>
					<th> ����ʱ�� </th>
					<th> ������ر� </th>
					<th> �ϱ����� </th>
					<th> �ļ��ϴ�����</th>
					<th> �˿� </th>
					<th> ��ز��� </th>
					<th> �豸�� </th>
				</tr>
			</thead>

			<tbody>
				<s:if test="IdsList.size()>0">
					<s:iterator value="IdsList" var="map1">
						<tr>
							<td align="center">
								<s:property value="acc_loginname" />
							</td>
							<td align="center">
								<s:property value="add_time" />
							</td>
							<td align="center">
								<s:if test="enable==1">����</s:if>
								<s:if test="enable==0">�ر�</s:if>
							</td>
							<td align="center">
								<s:property value="timelist" />
							</td>
							<td align="center">
								<s:property value="serverurl" />
							</td>
							<td align="center">
								<s:property value="tftp_port" />
							</td>
							<td align="center">
								<s:property value="paralist" />
							</td>
							<td align="center">
								<a href="javascript:openHgw('<s:property value="task_id"/>')">
									<s:property value="devCount" /> </a>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tfoot>
						<tr>
							<td align="left" colspan="8">
								û�ж�����Ϣ
							</td>
						</tr>
					</tfoot>
				</s:else>
			</tbody>
			<tfoot>
			</tfoot>
		</table>
		</s:if>
	</body>
</html>
