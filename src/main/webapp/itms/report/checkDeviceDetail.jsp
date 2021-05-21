<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags" %>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">
function ListToExcel(cityId,checkState) {
	var page="<s:url value='/itms/report/checkDeviceACT!getServInfoExcel.action'/>?"
		+ "&cityId=" + cityId 
		+ "&checkState=" +checkState;
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		�豸�б�
	</caption>
	<thead>
		<tr>
			<th>
				����
			</th>
			<th>
				�ͺ�
			</th>
			<th>
				����汾
			</th>
			<th>
				����
			</th>
			<th>
				�豸���к�
			</th>
			<th>
				IP
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="servInfoList.size()>0">
			<s:iterator value="servInfoList">
				<tr>
					<td>
						<s:property value="vendor_name" />
					</td>
					<td>
						<s:property value="device_model" />
					</td>
					<td>
						<s:property value="softwareversion" />
					</td>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="device_serialnumber" />
					</td>
					<td>
						<s:property value="loopback_ip" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=10>
					ϵͳû����ص�ҵ����Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="10">
				<span style="float: right;"> <lk:pages
						url="/itms/report/checkDeviceACT!getServInfoDetail.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="checkState"/>')">
			</td>
		</tr>


		<TR>
			<TD align="center" colspan="10">
				<button onclick="javascript:window.close();">
					&nbsp;�� ��&nbsp;
				</button>
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
