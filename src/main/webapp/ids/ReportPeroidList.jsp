<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<script type="text/javascript">
		function openHgw(taskId){
			var page="<s:url value='/ids/reportPeroid!getDevInfo.action'/>?taskId=" + taskId ;
			window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
		}
	</script>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>�ϱ����ڱ��������Ϣ�б�</title>

		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
	</head>
	<body>
		<table class="listtable">
			<caption>
				������Ϣ��ѯ���
			</caption>
			<thead>
				<tr>
					<th> ������ </th>
					<th> ����ʱ�� </th>
					<th> �ϱ����� </th>
					<th> Ŀ������ </th>
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
								<s:property value="reporttimelist" />
							</td>
							<td align="center">
								<s:property value="targettimelist" />
							</td>
							<td align="center">
								<a href="javascript:openHgw('<s:property value="taskId"/>')">
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
			<tr>
				<td colspan="8" align="right">
			 	<lk:pages
					url="/ids/reportPeroid!getTaskInfo.action"
					styleClass="" showType="" isGoTo="true" changeNum="true" />
				</td>

			</tr>
			<tr>
				<td colspan="8" ><IMG SRC="/itms/images/excel.gif"
					BORDER=0  ALT="�����б�" style="cursor: hand" onclick="javaScript:ToExcel();">
				</td>
			</tr>
		</table>
	</body>
</html>
