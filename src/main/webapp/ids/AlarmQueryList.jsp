<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Ԥ��Ԥ�޸澯��ѯ������Ϣ</title>
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

	function del(id) {
		parent.del(id);
	}
	function edit(id, alarm_name, alarm_code, alarm_level, alarm_period,
			alarm_count, rx_power, temperature, delay, loss_pp, send_sheet_obj) {
		parent.edit(id, alarm_name, alarm_code, alarm_level, alarm_period,
				alarm_count, rx_power, temperature, delay, loss_pp,
				send_sheet_obj);
	}

	function ToExcel() {
		parent.ToExcel();
	}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>Ԥ��Ԥ�޸澯��ѯ������Ϣ</caption>
		<thead>
			<tr>
				<th>�澯����</th>
				<th>�澯����</th>
				<th>�澯����</th>
				<th>N��Сʱ</th>
				<th>M��</th>
				<th>�⹦�ʣ�С�ڣ�</th>
				<th>�¶�</th>
				<th>ʱ�ӣ����ڣ�</th>
				<th>�����ʣ����ڣ�</th>
				<th>�ɵ�����</th>
				<th>����</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="alarmList!=null">
				<s:if test="alarmList.size()>0">
					<s:iterator value="alarmList">
						<tr>
							<td><s:property value="alarm_name" /></td>
							<td><s:property value="alarm_code" /></td>
							<td><s:property value="alarm_level" /></td>
							<td><s:property value="alarm_period" /></td>
							<td><s:property value="alarm_count" /></td>
							<td><s:property value="rx_power" /></td>
							<td><s:property value="temperature" /></td>
							<td><s:property value="delay" /></td>
							<td><s:property value="loss_pp" /></td>
							<td><s:property value="send_sheet_obj" /></td>
							<td><IMG SRC="/itms/images/edit.gif" BORDER='0' ALT='�༭'
								onclick="edit('<s:property value="id" />','<s:property value="alarm_name" />','<s:property value="alarm_code" />','<s:property value="alarm_level" />',
								'<s:property value="alarm_period" />','<s:property value="alarm_count" />','<s:property value="rx_power" />',
								'<s:property value="temperature" />','<s:property value="delay" />','<s:property value="loss_pp" />','<s:property value="send_sheet_obj" />')"
								style='cursor: hand'><IMG SRC="/itms/images/del.gif"
								BORDER='0' ALT='ɾ��' onclick="del('<s:property value="id" />')"
								style='cursor: hand'></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=11>ϵͳû��Ԥ��Ԥ�޸澯��ѯ������Ϣ!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>ϵͳû�д��û�!</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="11" align="right"><lk:pages
						url="/ids/AlarmQuery!getIdsarmInfoList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

			<tr>
				<td colspan="11" align="right"><IMG
					SRC="/itms/images/excel.gif" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()"></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>