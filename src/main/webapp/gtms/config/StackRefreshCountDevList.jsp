<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<%
 String type = request.getParameter("type");
 String starttime = request.getParameter("starttime");
 String endtime = request.getParameter("endtime");
 %> 
<script type="text/javascript">

function ListToExcel(cityId,status,type,starttime,endtime) {
	
	var page="<s:url value='/gtms/config/stackRefreshCount!getDevExcel.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime=" +status
		+ "&endtime=" +status
		+ "&status=" +status
		+ "&type=" +type;
	document.all("childFrm").src=page;
}

//�鿴itms�û���ص���Ϣ
function devInfo(device_id){
	var strpage="<s:url value='/Resource/DeviceShow.jsp'/>?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
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
				����
			</th>
			<th>
				�ͺ�
			</th>
			<th>
				�汾
			</th>
			<th>
				�豸���к�
			</th>
			<ms:inArea areaCode="zj_lt" notInMode="false">
				<th>LOID</th>
				<th>����˺�</th>
			</ms:inArea>
			<th>
				IP����
			</th>
			<th>
				ʧ��ԭ��
			</th>
			<th>
				����
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="vendor_add" />
					</td>
					<td>
						<s:property value="device_model" />
					</td>
					<td>
						<s:property value="softwareversion" />
					</td>
					<td>
						<s:property value="device" />
					</td>
					<ms:inArea areaCode="zj_lt" notInMode="false">
					<td>
						<s:property value="loid" />
					</td>
					<td>	
						<s:property value="username" />
					</td>
					</ms:inArea>
					<td>
						<s:property value="loopback_ip" />
					</td>
					<td>
						<s:property value="fault_desc" />
					</td>
					<td>
						<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="��ϸ��Ϣ"
							onclick="devInfo('<s:property value="device_id"/>')"
							style="cursor: hand">
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=10>
					ϵͳû����ص��û���Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="10">
				<span style="float: right;"> <lk:pages
						url="/gtms/config/stackRefreshCount!getDev.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
				<s:if test='#session.isReport=="1"'>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="status"/>','<s:property value="type"/>','<s:property value="starttime"/>','<s:property value="endtime"/>')">
				</s:if>
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
		<td colspan="10">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
