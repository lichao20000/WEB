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
	function ListToExcel(cityId, starttime1, endtime1, vendor, devicemodel,
			status, resultId) {
		alert(status);
		alert(resultId);
		var page = "<s:url value='/itms/resource/DPIQuery!queryDetailExcel.action'/>?"
				+ "starttime="
				+ starttime1
				+ "&endtime="
				+ endtime1
				+ "&cityId="
				+ cityId
				+ "&vendor="
				+ vendor
				+ "&devicemodel="
				+ devicemodel + "&status=" + status + "&resultId=" + resultId;
		document.all("childFrm").src = page;
	}

	//�鿴itms�û���ص���Ϣ
	function devInfo(device_id) {
		var strpage = "<s:url value='/Resource/DeviceShow.jsp'/>?device_id="
				+ device_id;
		window
				.open(strpage, "",
						"left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
	}
</script>

<table class="listtable">
	<caption>�豸�б�</caption>
	<thead>
		<tr>
			<th>�豸����</th>
			<th>�ͺ�</th>
			<th>����汾</th>
			<th>����</th>
			<th>�豸���к�</th>
			<th>���ý��</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td><s:property value="vendor_id" /></td>
					<td><s:property value="device_model_id" /></td>
					<td><s:property value="softwareversion" /></td>
					<td><s:property value="city_name" /></td>
					<td><s:property value="device_serialnumber" /></td>
					<td><s:property value="fault_desc" /></td>
					<td><IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
						ALT="��ϸ��Ϣ" onclick="devInfo('<s:property value="device_id"/>')"
						style="cursor: hand"></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>ϵͳû����ص��û���Ϣ!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7"><span style="float: right;"> <lk:pages
						url="/itms/resource/DPIQuery!queryDetail.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
			</span> <s:if test='#session.isReport=="1"'>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="vendor"/>','<s:property value="devicemodel"/>','<s:property value="status"/>','<s:property value="resultId"/>')">
				</s:if></td>
		</tr>


		<TR>
			<TD align="center" colspan="7">
				<button onclick="javascript:window.close();">&nbsp;��
					��&nbsp;</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="7"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>