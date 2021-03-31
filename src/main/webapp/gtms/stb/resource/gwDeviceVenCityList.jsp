<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import= "java.util.*"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<%
List<String> titleList = (List<String>)request.getAttribute("titleList");
List<String> cityShow = (List<String>)request.getAttribute("cityShow");
List<Map<String,String>> statsReportList = (List<Map<String,String>>)request.getAttribute("statsReportList");
%>	
<script>
$(function() {
		parent.dyniframesize();
		$("#querybtn",parent.document).attr("disabled","");
});
	
var deviceType;
var startOpenDate;
var endOpenDate;
var servTypeId;

function getDetail(city_id, open_status)
{
	var page = "<s:url value='/itms/report/sheetStatsReport!getDetailReport.action'/>?cityId="+city_id+"&openStatus="+open_status;
	
	// 进一步获取父页面的几个查询条件
	getCondition();	
	page += "&deviceType=" + deviceType + "&startOpenDate=" + startOpenDate + "&endOpenDate=" + endOpenDate + "&servTypeId=" + servTypeId;
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}

function ToExcel()
{
	var form = parent.document.getElementsByName("selectForm")[0];
	form.action = "<s:url value='/itms/report/sheetStatsReport!getExcel.action'/>";
	form.target = "_self";
	form.submit();
}

// 获取父页面的查询条件
function getCondition()
{
	deviceType = window.parent.document.getElementsByName("deviceType")[0].value;
	startOpenDate = window.parent.document.getElementsByName("startOpenDate")[0].value;
	endOpenDate = window.parent.document.getElementsByName("endOpenDate")[0].value;
	servTypeId = window.parent.document.getElementsByName("servTypeId")[0].value;
}
</script>
<table class="listtable">
	<caption>
		查询结果
	</caption>
	<thead>
		<tr>
			<%
			for(int i=0;i<titleList.size();i++){
			%>
			<th>
			<%=titleList.get(i)%>
			</th>
			<%} %>
			<th>小计</th>
		</tr>
	</thead>
	<tbody>
		<%
		if(statsReportList.size()>0){
		for(int i=0;i<statsReportList.size()-1;i++){
			Map<String,String> one = statsReportList.get(i);
			String vendor = one.get("vendor");
			String model = one.get("model");
			String version = one.get("version");
		%>
			<tr>
				<td>
					<%=vendor %>
				</td>
				<td>
					<%=model %>
				</td>
				<td>
					<%=version %>
				</td>
				<%for(int j=0;j<cityShow.size();j++){ 
				String key = cityShow.get(j);%>
				<td>
						<%=one.get(key) %>
				</td>
				<%} %>
				<td>
						<%=one.get("sum") %>
				</td>
			</tr>
		<% }
		Map<String,String> one1 = statsReportList.get(statsReportList.size()-1);
		%>
			<tr>
				
				<td colspan="3" align="right">
					小计
				</td>
				<%for(int j=0;j<cityShow.size();j++){ 
				String key = cityShow.get(j);%>
				<td>
						<%=one1.get(key) %>
				</td>
				<%} %>
				<td>
						<%=one1.get("sum") %>
				</td>
			
			</tr>
		<%
		}
		else{%>
			<tr>
			<td colspan="100">
			没有查询到相关结果！
			</td>
			</tr>
		<%}%>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="100">
				<%-- <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel()"> --%>
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="4">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>
