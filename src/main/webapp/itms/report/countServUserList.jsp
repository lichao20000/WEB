<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title> </title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});
	
	function DetailToExcel(city_id,starttime,endtime,specId,is_active,serv_type_id) {
		var page="<s:url value='/itms/report/countServ!getDetailsExcel.action'/>?"
			+ "city_id="+city_id
			+ "&starttime=" + starttime
			+ "&endtime=" + endtime
			+ "&specId=" + specId
			+ "&is_active=" + is_active
			+ "&serv_type_id=" + serv_type_id;
		document.all("childFrm").src=page;
		
	}
</script>

</head>

<body>
	<table class="listtable">
		<caption>
			统计结果
		</caption>
		<thead>
			<tr>
				<th>设备序列号</th>
				<th>LOID</th>
				<th>受理时间</th>
				<th>业务名称</th>
				<th>业务账号</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="details_list">
				<tr bgcolor="#FFFFFF" style="vertical-align:middle; text-align:center;" >
					<td class=column1><s:property value="deviceNum"/></td>
					<td class=column1><s:property value="LOID"/></td>
					<td class=column1><s:property value="dealdate"/></td>
					<td class=column1><s:property value="operationName"/></td>
					<td class=column1><s:property value="operationNum"/></td>
				</tr>
			</s:iterator>
		</tbody>
		<tr>
			<td colspan="9" align="right">
				<lk:pages url="/itms/report/countServ!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
		<tfoot>
			<tr>
				<td colspan="9">
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表s'
						style='cursor: hand' 
						onclick="DetailToExcel('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="specId"/>','<s:property value="is_active"/>','<s:property value="serv_type_id"/>')">
				</td>
			</tr>
		</tfoot>
		<tr STYLE="display: none">
			<td colspan="9">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</table>
</body>
</html>