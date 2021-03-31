<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
	
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
			<th>
			<s:if test="servTypeId == 1">终端类型</s:if>
			<s:elseif test="servTypeId == 2">区域</s:elseif>
			<s:else> </s:else>
			</th>
			<th>
				设备终端数
			</th>
			<th>
				在线终端数
			</th>
			<th>
				开通业务量
			</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="statsReportList">
			<tr>
				<td>
					<s:property value="name"  />
				</td>
				<td>
					<%-- <a href="javascript:getDetail('<s:property value="cityId"/>','0');">
						<s:property value="notNum" />
					</a> --%>
					<s:property value="allnum" />
				</td>
				<td>
						<s:property value="onnum" />
				</td>
				<td>
						<s:property value="servnum" />
				</td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="4">
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
