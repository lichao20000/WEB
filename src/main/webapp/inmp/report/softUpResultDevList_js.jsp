<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="../../css/inmp/css3/c_table.css" type="text/css">
<link rel="stylesheet" href="../../css/inmp/css3/global.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/jquery.js"></SCRIPT>
<script type="text/javascript" src="../../Js/inmp/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/edittable.js"></SCRIPT>
<script type="text/javascript">

function ListToExcel(cityId,starttime1,endtime1,status,resultId,isMgr,isSoftUp) {
	var page="<s:url value='/inmp/report/softUpResult!getDevExcel.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&status=" +status
		+ "&resultId=" +resultId
		+ "&isSoftUp=" +isSoftUp
		+ "&isMgr=" +isMgr;
	document.all("childFrm").src=page;
}

//�鿴itms�û���ص���Ϣ
function devInfo(device_id){
	var strpage="<s:url value='/inmp/resource/DeviceShow.jsp'/>?device_id=" + device_id;
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
			<th>
				IP����
			</th>
			<th>
				����
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devList.size()>0">
			<s:iterator value="devList">
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
					<td>
						<s:property value="loopback_ip" />
					</td>
					<td>
						<IMG SRC="../../images/inmp/view.gif" BORDER="0" ALT="��ϸ��Ϣ"
							onclick="devInfo('<s:property value="device_id"/>')"
							style="cursor: hand">
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>
					ϵͳû����ص��û���Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7">
				<span style="float: right;"> <lk:pages
						url="/inmp/report/softUpResult!getDev.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
				<s:if test='#session.isReport=="1"'>
					<IMG SRC="../../images/inmp/excel.gif" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="status"/>','<s:property value="resultId"/>','<s:property value="isMgr"/>','<s:property value="isSoftUp"/>')">
				</s:if>
			</td>
		</tr>


		<TR>
			<TD align="center" colspan="7">
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

<%@ include file="../foot.jsp"%>