<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css"/>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css"/>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>������ʧ���豸��ϸ</title>
<script type="text/javascript">
	var starttime = "<s:property value='starttime'/>";
	var endtime = "<s:property value='endtime'/>";

	function configDetail(deviceId,cityId,reasonId) {
		var url = "<s:url value='/gtms/stb/resource/zeroConfigHistory!doDeviceZeroHistoryQuery.action'/>?deviceId=" + deviceId
			+ "&starttime=" + starttime + "&endtime=" + endtime + "&failReasonMark=true&reasonId=" + reasonId + "&ctiyId=" + cityId;
		window.open(url, "","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}
	function ToExcel(reasonId,cityId,starttime,endtime) {
		var page="<s:url value='/gtms/stb/resource/zeroConfigFailReason!zeroConfigFaileDeviceExcel.action'/>?"
			+ "reasonId=" + reasonId + "&cityId=" + cityId
			+ "&starttime=" + starttime + "&endtime=" + endtime;
		document.all("childFrm").src=page;
	}
</script>
</head>
<body>

	<table width="100%" class="listtable" align="center"
		style="margin-top: 10px;">
		<caption>�豸��ϸ</caption>
		<thead>
			<tr>
				<th class="title_1">����</th>
				<th class="title_1">����</th>
				<th class="title_1">�豸�ͺ�</th>
				<th class="title_1">����汾</th>
				<th class="title_1">�豸���к�</th>
				<th class="title_1">MAC</th>
				<th class="title_1">ҵ���˺�</th>
				<th class="title_1">�豸IP</th>
				<th class="title_1">�鿴��ʷ������ϸ</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="data.size()>0">
				<s:iterator var="list" value="data">
					<tr>
						<td><s:property value="#list.city_name" /></td>
						<td><s:property value="#list.vendor_name" /></td>
						<td><s:property value="#list.device_model" /></td>
						<td><s:property value="#list.softwareversion" /></td>
						<td><s:property value="#list.device_serialnumber" /></td>
						<td><s:property value="#list.cpe_mac" /></td>
						<td><s:property value="#list.serv_account" /></td>
						<td><s:property value="#list.loopback_ip" /></td>
						<td align="center"><a href="javascript:void();" onclick="configDetail('<s:property value="#list.device_id"/>','<s:property value="#list.city_id"/>','<s:property value="reasonId"/>')">�鿴��ϸ</a></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="9">
						<div style="text-align: center">��ѯ������</div>
					</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<s:if test="data.size()>0">
				<tr>

					<td class="foot" colspan="9">
					    <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER="0" ALT="�����б�" style="cursor:hand;"
							 onclick="ToExcel('<s:property value="reasonId"/>','<s:property value="cityId"/>','<s:property value="starttime"/>','<s:property value="endtime"/>')"/>
						<div style="float: right;">
							<lk:pages url="/gtms/stb/resource/zeroConfigFailReason!queryZeroConfigFailReasonDetail.action" isGoTo="true"/>
						</div>
					</td>
				</tr>
			</s:if>
		</tfoot>
		<tr STYLE="display: none">
			<td colspan="12">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</table>
</body>
</html>
