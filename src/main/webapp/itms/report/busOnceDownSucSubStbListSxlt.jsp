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
function ListToExcel(cityId,starttime1,endtime1,open_status) {
	var page="<s:url value='/itms/report/busOnceDownSucACTSxlt!getServInfoStbExcel.action'/>?"
		+ "&cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&open_status=" +open_status;
	document.all("childFrm").src=page;
}

</script>

<table class="listtable">
	<caption>
		设备列表
	</caption>
	<thead>
		<tr>
			<th>
				设备序列号
			</th>
			<th>
				唯一标识
			</th>
			<th>
				受理时间
			</th>
			<th>
				业务名称
			</th>
			<th>
				业务账号
			</th>
			<th>
				业务开通状态
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="servInfoList.size()>0">
			<s:iterator value="servInfoList">
				<tr>
					<td>
						<s:property value="deviceSerialnumber" />
					</td>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="dealdate" />
					</td>
					<td>
						<s:property value="servType" />
					</td>
					<td>
						<s:property value="username" />
					</td>
					<td>
						<s:property value="openStatus" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=10>
					系统没有相关的业务信息!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="10">
				<span style="float: right;"> <lk:pages
						url="/itms/report/busOnceDownSucACTSxlt!getServInfoStbDetail.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>',
						                     '<s:property value="endtime1"/>','<s:property value="open_status"/>')">
			</td>
		</tr>


		<TR>
			<TD align="center" colspan="10">
				<button onclick="javascript:window.close();">
					&nbsp;关 闭&nbsp;
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
