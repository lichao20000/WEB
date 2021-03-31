<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="../css3/c_table.css" type="text/css">
<link rel="stylesheet" href="../css3/global.css" type="text/css">
<link rel="stylesheet" href="../css/tab.css" type="text/css">
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
$(function() {
	$("#trData",parent.document).hide();
	$("#btn",parent.document).attr('disabled',false);
	parent.dyniframesize();
});
function openCus(cityId,starttime,endtime,temperature,bais_current,vottage){
	var page="<s:url value='/ids/deviceTVB!queryByTVBList.action'/>?"
		+"starttime="+starttime
		+ "&endtime=" + endtime
		+ "&cityId=" + cityId
		+ "&temperature=" + temperature
		+ "&bais_current=" + bais_current
		+ "&vottage=" + vottage;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}
function ToExcel(cityId,starttime,endtime,temperature,bais_current,vottage){
	var page="<s:url value='/ids/deviceTVB!queryByTVBExcel.action'/>?"
		+"starttime="+starttime
		+ "&endtime=" + endtime
		+ "&cityId=" + cityId
		+ "&temperature=" + temperature
		+ "&bais_current=" + bais_current
		+ "&vottage=" + vottage;
	document.all("childFrm").src = page;
}
</script>
<table class="listtable">
	<caption>
		终端物理状态统计情况
	</caption>
	<thead>
		<tr>
			<th width="50%">
				区域
			</th>
			<th width="50%">
				终端数
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="list.size()>0">
			<s:iterator value="list">
				<tr>
					<td>
							<s:property value="cityName" />
					</td>
					<td>
						<a
							href="javascript:openCus('<s:property value="cityId"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="temperature"/>','<s:property value="bais_current"/>','<s:property value="vottage"/>');">
							<s:property value="total" /> </a>

					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="2">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="temperature"/>','<s:property value="bais_current"/>','<s:property value="vottage"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="2">
			<iframe id="childFrm" name="childFrm" src=""></iframe>
		</td>
	</tr>

</table>

