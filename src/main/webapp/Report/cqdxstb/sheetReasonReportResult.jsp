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
				属地/原因
			</th>
			<th>
				操作成功
			</th>
			<th>
				LOID不能为空
			</th>
			<th>
				work_asgn_id为空
			</th>
			<th>
				无对应的用户信息
			</th>
			<th>
				Order_Remark为空
			</th>
			<th>
				受理编号为空
			</th>
			<th>
				受理日期为空
			</th>
			<th>
				业务账号或业务密码为空
			</th>
			<th>
				设备ID为空
			</th>
			<th>
				STBID为空或非32位
			</th>
			<th>
				设备接入类型为空
			</th>
			<th>
				上门方式为空
			</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="statsReportList">
			<tr>
				<td>
					<s:property value="city_name"  />
				</td>
				<td>
					<s:property value="r0" />
				</td>
				<td>
					<s:property value="r03" />
				</td>
				<td>
					<s:property value="r02" />
				</td>
				<td>
					<s:property value="r25" />
				</td>
				<td>
					<s:property value="r32" />
				</td>
				<td >
					<s:property value="r33" />
				</td>
				<td >
					<s:property value="r34" />
				</td>
				<td >
					<s:property value="r45" />
				</td>
				<td >
					<s:property value="r47" />
				</td>
				<td >
					<s:property value="r48" />
				</td>
				<td >
					<s:property value="r49" />
				</td>
				<td >
					<s:property value="r50" />
				</td>
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
