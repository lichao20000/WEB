<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务查询</title>
<%
	/**
	 * e8-c业务查询
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2013-08-15
	 * @category
	 */
	 String startOpenDate=request.getParameter("startOpenDate");
	 String endOpenDate=request.getParameter("endOpenDate");
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
function ListToExcel(city_id,spec_id) {
	var _spec_id=$("input[@name='spec_id']").val();
	var startOpenDate = <%=startOpenDate%>;
	var endOpenDate = <%=endOpenDate%>;
	alert(city_id+" "+spec_id+" "+startOpenDate+" "+endOpenDate);
		var page="<s:url value='/itms/resource/UserSpecTerminals!getExcel.action'/>?"
			+ "&spec_id=" + spec_id
			+ "&city_id=" + city_id
			+ "&startOpenDate=" + startOpenDate
			+ "&endOpenDate=" + endOpenDate;
		document.all("childFrm").src=page;
	
	
}
</script>
</head>
<body>
	<input type="hidden" name="spec_id"
		value="<s:property value="spec_id" />" />
	<input type="hidden" name="startOpenDate"
		value="<s:property value="startOpenDate" />" />
	<input type="hidden" name="endOpenDate"
		value="<s:property value="endOpenDate" />" />
	<input type="hidden" name="city_id"
		value="<s:property value="city_id" />" />
	<table class="listtable" id="listTable">
		<caption>详细信息</caption>
		<thead>
			<tr>
				<th width="25%" align="center">用户LOID</th>
				<th width="25%" align="center">设备序列号</th>
				<th width="25%" align="center">用户规格</th>
				<th width="25%" align="center">设备规格</th>
			</tr>
		</thead>
		<tbody>

			<s:if test="list!=null">
				<s:if test="list.size()>0">
					<s:iterator value="list">
						<tr>
							<td align="center"><s:property value="loid" /></td>
							<td align="center"><s:property value="serial" /></td>
							<td align="center"><s:property value="cusSpec" /></td>
							<td align="center"><s:property value="terSpec" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=4>暂无报表信息</td>
					</tr>
				</s:else>

			</s:if>
		</tbody>
		<tfoot>
			<s:if test="list!=null">
				<s:if test="list.size()>0">
					<tr>
						<td colspan=4><span style="float: right;"> <lk:pages
									url="/itms/resource/UserSpecTerminals!getTerminalSpecList.action"
									styleClass="" showType="" isGoTo="true" changeNum="true" />
						</span> <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0'
							ALT='导出列表' style='cursor: hand' onclick="ListToExcel('<s:property value="city_id"/>','<s:property value="spec_id"/>')">
						</td>
					</tr>

				</s:if>
			</s:if>
			<TR>
				<TD align="center" colspan=4>
					<button onclick="javascript:window.close();">&nbsp;关
						闭&nbsp;</button>
				</TD>
			</TR>
			<tr STYLE="display: none">
				<td colspan="8"><iframe id="childFrm" src=""></iframe></td>
			</tr>
		</tfoot>
	</table>
</body>
<script>
	

	
</script>

</html>