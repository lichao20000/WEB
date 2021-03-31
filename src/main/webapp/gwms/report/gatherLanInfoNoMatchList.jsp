<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">

function ListToExcel(cityId,startOpenDate,endOpenDate) {
	var page="<s:url value='/gwms/report/lanGatherInfo!exportDetail.action'/>?"
		+ "&cityId=" + cityId
		+ "&startOpenDate=" +startOpenDate
		+ "&endOpenDate=" +endOpenDate
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		GE�ڲ�ƥ����ϸ�嵥�б�
	</caption>
	<thead>
		<tr>
			<th>
				����
			</th>
			<th>
				LOID
			</th>
			<th>
				����˺�
			</th>
			<th>
				�ͺ�
			</th>
			<th>
				����
			</th>
			<th>
				ƥ��˿ڵ�����
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="noMatchList.size()>0">
			<s:iterator value="noMatchList">
				<tr>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="username" />
					</td>
					<td>
						<s:property value="devicemodel" />
					</td>
					<td>
						<s:property value="vendorName" />
					</td>
					<td>
						<s:property value="speed" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>
					ϵͳû����ص��豸��Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6">
				<span style="float: right;"> <lk:pages
						url="/gwms/report/lanGatherInfo!queryDetail.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="startOpenDate"/>','<s:property value="endOpenDate"/>')">

			</td>
		</tr>


		<TR>
			<TD align="center" colspan="6">
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
