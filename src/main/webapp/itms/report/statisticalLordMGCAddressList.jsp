<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	request.setCharacterEncoding("GBK");
	response.setContentType("Application/msexcel");
	response.setHeader("Content-disposition",
			"attachment; filename=MGC.xls");
%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">

function ToExcel(city_id) {
	var page = "<s:url value='/itms/report/statisticalLordMGCAddressQuery!getExcel.action'/>?"
		+ "city_id=" + city_id;
	document.all("childFrm").src = page;
}


	function openDevForWbdTerminal(city_id, prox_serv) {
		var page = "<s:url value='/itms/report/statisticalLordMGCAddressQuery!getDevListForWbdTerminal.action'/>?"
				+ "city_id=" + city_id + "&prox_serv=" + prox_serv;
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>
<table class="listtable" id="listTable">
	<caption>主用MGC统计信息</caption>
	<thead>
		<s:property value="htmlList" escapeHtml="false" />
	</thead>
	<tbody>
		<s:property value="dataList" escapeHtml="false" />
	</tbody>
	<tr STYLE="display: none">
		<td colspan="2"><iframe id="childFrm" src=""></iframe></td>
	</tr>
	<tfoot>
		<tr>
			<TD class=column colspan="<s:property value="columSize" />" height="30"><a
							href="statisticalLordMGCAddressList_excel.jsp?city_id=<s:property value="city_id" />"
							alt="导出当前页数据到Excel中">&nbsp;&nbsp;&nbsp;导出到Excel</a></TD>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td ><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>
