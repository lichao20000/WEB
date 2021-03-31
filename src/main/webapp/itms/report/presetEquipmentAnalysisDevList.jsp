<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>

<script type="text/javascript">
	function ListToExcel(cityId, startOpenDate, endOpenDate, vendorId, modelId,
			status) {
		var time = $("input[@name='time']").val();
		var page = "<s:url value='/itms/report/presetEquipmentAnalysisQuery!getDevExcel.action'/>?"
				+ "city_id=" + cityId;
		+"&startOpenDate=" + startOpenDate + "&endOpenDate=" + endOpenDate
				+ "&vendorId=" + vendorId + "&modelId=" + modelId + "&status="
				+ status;
		document.all("childFrm").src = page;
	}
	function DetailDevice(device_id) {
		var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
		window
				.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

	//�鿴itms�û���ص���Ϣ
	function itmsUserInfo(user_id) {
		var strpage = "<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id="
				+ user_id;
		window
				.open(strpage, "",
						"left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
	}
</script>

<table class="listtable">

	<caption>Ԥ���豸�������ݷ����굥</caption>
	<thead>
		<tr>
			<th>OUI</th>
			<th>�豸���к�</th>
			<th>����</th>
			<th>�豸�ͺ�</th>
			<th>����</th>
			<th>����ʱ��</th>
			<th>�豸MAC</th>
			<th>����״̬</th>
			<th>ʹ��״̬</th>
			<th>�ն�ע��ʱ��</th>
			<th>���һ�θ���ʱ��</th>
			<th>LOID</th>
			<th>����汾</th>
			<th>Ӳ���汾</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devList.size()>0">
			<s:iterator value="devList">
				<tr>
					<td><s:property value="oui" /></td>
					<td><s:property value="device_serialnumber" /></td>
					<td><s:property value="vendor_name" /></td>
					<td><s:property value="model_name" /></td>
					<td><s:property value="city_name" /></td>
					<td><s:property value="buy_time" /></td>
					<td><s:property value="cpe_mac" /></td>
					<td>�ɹ�</td>
					<td><s:property value="status" /></td>
					<td><s:property value="complete_time" /></td>
					<td><s:property value="cpe_currentupdatetime" /></td>
					<td><s:property value="device_id_ex" /></td>
					<td><s:property value="softwareversion" /></td>
					<td><s:property value="hardwareversion" /></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="14">ϵͳû����ص��豸��Ϣ!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="14"><span style="float: right;"> <lk:pages
						url="/itms/report/presetEquipmentAnalysisQuery!getDevListForWbdTerminal.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
			</span></td>
		</tr>
		<tr>
			<td colspan="14" align="right"><input type="hidden" name="time"
				id="time" value="<s:property value='time' />" /> <IMG
				SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="city_id"/>','<s:property value="startOpenDate"/>','<s:property value="endOpenDate"/>','<s:property value="vendorId"/>','<s:property value="modelId"/>','<s:property value="status"/>')"></td>
		</tr>

		<TR>
			<TD align="center" colspan="14">
				<button onclick="javascript:window.close();">&nbsp;��
					��&nbsp;</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="14"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
