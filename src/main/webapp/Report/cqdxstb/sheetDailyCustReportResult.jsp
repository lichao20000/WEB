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
			<s:if test="servTypeId == 1">设备类型</s:if>
			<s:elseif test="servTypeId == 2">区域</s:elseif>
			<s:elseif test="servTypeId == 3">终端模式</s:elseif>
			<s:elseif test="servTypeId == 4">业务类型</s:elseif>
			<s:elseif test="servTypeId == 5">用户类型</s:elseif>
			<s:else>条件</s:else>
				/日期
			</th>
			<s:iterator value="dateList">
			<th>
				<s:property />
			</th>
			</s:iterator>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="statsReportList">
			<tr>
				<td>
					<s:property value="city_name"  />
				</td>
				<s:if test="datenum>=1">
					<td>
						<s:property value="date0"/>
					</td>
				</s:if>
				<s:if test="datenum>=2">
					<td>
						<s:property value="date1"/>
					</td>
				</s:if>
				<s:if test="datenum>=3">
					<td>
						<s:property value="date2"/>
					</td>
				</s:if>
				<s:if test="datenum>=4">
					<td>
						<s:property value="date3"/>
					</td>
				</s:if>
				<s:if test="datenum>=5">
					<td>
						<s:property value="date4"/>
					</td>
				</s:if>
				<s:if test="datenum>=6">
					<td>
						<s:property value="date5"/>
					</td>
				</s:if>
				<s:if test="datenum>=7">
					<td>
						<s:property value="date6"/>
					</td>
				</s:if>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="13">
				<%-- <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel()"> --%>
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="13">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>
