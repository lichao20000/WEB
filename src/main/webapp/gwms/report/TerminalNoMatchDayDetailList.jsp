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

//�ն˲�ƥ�䵼��
function ListToExcel(cityId) {
	var page="<s:url value='/gwms/report/terminalNoMatchReport!getNoMatchDayCompltExcel.action'/>?"+ "cityId=" + cityId;
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
				Ӫҵ��
			</th>
			<th>
				LOID
			</th>
			<th>
				����˺�
			</th>
			<th>
				����
			</th>
			<th>
				�ͺ�
			</th>
			<th>
				�Ƿ�֧��ǧ��
			</th>
			<th>
				�Ƿ�����������
			</th>
			<th>
				��ע
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
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="pppoe_name" />
					</td>
					<td>
						<s:property value="vendor_add" />
					</td>
					<td>
						<s:property value="device_model" />
					</td>
					<td>
						<s:property value="gigabit_port" />
					</td>
					<td>
						<s:property value="device_version_type" />
					</td>
					<td>
						<s:property value="remark" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="8">
					ϵͳû����ص��豸��Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="8">
				<span style="float: right;"> <lk:pages
						url="/gwms/report/terminalNoMatchReport!getNoMatchDayDetailList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>')">
			</td>
		</tr>


		<TR>
			<TD align="center" colspan="8">
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
