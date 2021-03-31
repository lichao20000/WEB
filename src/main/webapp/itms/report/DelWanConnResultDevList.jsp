<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">
function ListToExcel(cityId,starttime1,endtime1,status,
		resultId,isMgr,isSoftUp,vendorId,deviceModelId) 
{
	var page="<s:url value='/itms/report/delWanConnReportACT!getDevExcel.action'/>?"
				+ "cityId=" + cityId 
				+ "&starttime1=" +starttime1
				+ "&endtime1=" +endtime1
				+ "&status=" +status;
	document.all("childFrm").src=page;
}

//�鿴itms�û���ص���Ϣ
function devInfo(device_id)
{
	var strpage="<s:url value='/Resource/DeviceShow.jsp'/>?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
</script>

<table class="listtable">
	<caption>�豸�б�</caption>
	<thead>
		<tr>
			<th>����</th>
			<th>LOID</th>
			<th>ҵ������</th>
			<th>vlan ID</th>
			<th>�豸���к�</th>
			<th>�������</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devList.size()>0">
			<s:iterator value="devList">
				<tr>
					<td><s:property value="city_name" /></td>
					<td><s:property value="loid" /></td>
					<td><s:property value="serv_type" /></td>
					<td><s:property value="vlanid" /></td>
					<td><s:property value="device_serialnumber" /></td>
					<td><s:property value="res_desc" /></td>
					<td>
						<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="��ϸ��Ϣ"
							onclick="devInfo('<s:property value="device_id"/>')" style="cursor: hand">
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=8>
					ϵͳû����ص��û���Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="8">
				<span style="float: right;">
					<lk:pages url="/itms/report/delWanConnReportACT!getDev.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> 
				</span>
				<s:if test='#session.isReport=="1"'>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand' onclick="ListToExcel('<s:property value="cityId"/>',
																	'<s:property value="starttime1"/>',
																	'<s:property value="endtime1"/>',
																	'<s:property value="status"/>')">
				</s:if>
			</td>
		</tr>
		<TR>
			<TD align="center" colspan="8">
				<button onclick="javascript:window.close();">&nbsp;�� ��&nbsp;</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="5">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
