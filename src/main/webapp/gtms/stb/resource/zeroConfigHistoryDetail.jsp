<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>�������豸���̸���</title>
</head>
<body>

	<table width="100%" class="listtable" align="center"
		style="margin-top: 10px;">
		<caption>�豸��ϸ���̸���</caption>
		<thead>
			<tr>
				<th class="title_1">���̱��</th>
				<th class="title_1">�󶨷�ʽ</th>
				<th class="title_1">��ʼʱ��</th>
				<th class="title_1">����ʱ��</th>
				<th class="title_1">���</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="data.size()>0">
				<s:iterator var="list" value="data">
					<tr>
						<td><s:property value="#list.buss_id" /></td>
						<td><s:property value="#list.bind_way" /></td>
						<td><s:property value="#list.start_time" /></td>
						<td><s:property value="#list.fail_time" /></td>
						<td><s:property value="#list.fail_reason_id" />,
						<s:property value="#list.reason_desc" />,<s:property value="#list.return_value" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="5">
						<div style="text-align: center">��ѯ������</div>
					</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<s:if test="data.size()>0">
				<tr>

					<td class="foot" colspan="5">
						<div style="float: right">
							<lk:pages url="/gtms/stb/resource/zeroConfigHistory!doDeviceZeroHistoryQuery.action"
								isGoTo="true"/>
						</div>
					</td>
				</tr>
			</s:if>
		</tfoot>
	</table>
</body>
</html>
