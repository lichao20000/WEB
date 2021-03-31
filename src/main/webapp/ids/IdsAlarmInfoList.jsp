<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Ԥ��Ԥ�޸澯��Ϣ</title>
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
		parent.dyniframesize();
	});

	function ToExcel() {
		parent.ToExcel();
	}
</script>
</head>
<body>
	<table class="listtable" id="listTable" style="width: 1400px">
		<caption>Ԥ��Ԥ�޸澯��Ϣ</caption>
		<thead>
			<tr>
				<th>����</th>
				<th>LOID</th>
				<th>�豸��ʶ</th>
				<th>�澯���</th>
				<th>�澯����</th>
				<th>�澯����</th>
				<th>�澯��Ϣ</th>
				<th>�澯��ʼʱ��</th>
				<th>���һ���ϱ�ʱ��</th>
				<!-- <th>�澯����</th> -->
				<%--th>�澯Ԥ���</th>
			<th>�澯���</th--%>
				<th>�澯���ʱ��</th>
				<th>�澯��ʱ</th>
				<!-- <th>�Ƿ��ɵ�</th>
				<th>�ɵ�����</th> -->
			<th>OLT����</th>
			<th>OLTIP</th>
			<th>PON��</th>
			<%--<th>ONUID</th> --%>
			</tr>
		</thead>
		<tbody>
			<s:if test="alarmList!=null">
				<s:if test="alarmList.size()>0">
					<s:iterator value="alarmList">
						<tr>
							<td><s:property value="city_name" /></td>
							<td><s:property value="loid" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="alarm_type" /></td>
							<td><s:property value="alarm_name" /></td>
							<td><s:property value="alarm_code" /></td>

							<td><s:property value="alarm_content" /></td>
							<td><s:property value="first_up_time" /></td>
							<td><s:property value="last_up_time" /></td>

							<%-- <td><s:property value="alarm_count" /></td> --%>
							<%--td><s:property value="is_pre_release" /></td>
							<td><s:property value="is_release" /></td--%>
							<td><s:property value="release_time" /></td>
							<td><s:property value="duration_time" /></td>
							<%-- <td><s:property value="is_send_sheet" /></td>
							<td><s:property value="send_sheet_obj" /></td>
							--%>
							<td><s:property value="olt_name" /></td>
							<td><s:property value="olt_ip" /></td>
							<td><s:property value="pon_intf" /></td>
							<%--<td><s:property value="onu_id" /></td>--%> 

						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=18>ϵͳû��Ԥ��Ԥ�޸澯��Ϣ!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=18>ϵͳû�д��û�!</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="18" align="right"><lk:pages
						url="/ids/IdsAlarmInfo!getIdsarmInfoList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

			<tr>
				<td colspan="18" align="right"><IMG
					SRC="/itms/images/excel.gif" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()"></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>