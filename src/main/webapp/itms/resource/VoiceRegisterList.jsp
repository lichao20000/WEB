<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����ע���ѯ</title>
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
	
</script>
</head>
<body>
<table class="listtable">
	<thead>
		<tr>
			<th>�豸�ͺ�</th>
			<th>LOID</th>
			<th>�豸���к�</th>
			<th>�ն�����</th>
			<th>�����˿��Ƿ�����</th>
			<th>�����˿ں���</th>
			<th>����ע��ɹ�״̬</th>
			<th>����ע��ʧ��ԭ��</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="voiceDeviceList.size()>0">
			<s:iterator value="voiceDeviceList">
				<tr>
					<td>
						<s:property value="device_model" />
					</td>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="device_serialnumber" />
					</td>
					<td>
						<s:property value="device_type" />
					</td>
					<td>
						�˿�[<s:property value="line_id" />]:
						<s:property value="enabled" />
					</td>
					<td>
						<s:property value="voip_phone" />
					</td>
					<td>
						<s:property value="status" />
					</td>
					<td>
						<s:property value="reason" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=8>
					û�в�ѯ������Ҫ��������Ϣ
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan=8>
				<span style="float: right;"> <lk:pages
						url="/itms/resource/VoiceRegisterQuery!VoiceRegisterQueryInfo.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>
<%@ include file="/foot.jsp"%>
