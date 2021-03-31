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


function toExcel(cityId) {
	var page="<s:url value='/gwms/report/MismathSpeed!toChangedExcel.action'/>?"+ "cityId=" + cityId;
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		��ͥ�����ն����ʲ�ƥ�����޸��ն�����
	</caption>
	<thead>
		<tr>
			<th>�ֹ�˾</th>
			<th>����</th>
			<th>ǩԼ����</th>
			<th>loid</th>
			<th>����˺�</th>
			<th>�ͺ�</th>
			<th>Ӳ���汾</th>
			<th>����汾</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devList.size()>0">
			<s:iterator value="devList">
				<tr>
					<td>
						<s:property value="city" />
					</td>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="downlink" />
					</td>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="net_user" />
					</td>
					<td>
						<s:property value="device_model" />
					</td>
					<td>
						<s:property value="hardwareversion" />
					</td>
					<td>
						 <s:property value="softwareversion" />
					</td>
					<td>
						 <s:property value="oui_sn" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="9">
					ϵͳû����ص��豸��Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9">
				<span style="float: right;"> <lk:pages
						url="/gwms/report/MismathSpeed!queryChangedDetail.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="toExcel('<s:property value="cityId"/>')">
			</td>
		</tr>


		<TR>
			<TD align="center" colspan="9">
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
