<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>
<%
 String gwType = "1";
 String do_type = "2";
 String type = request.getParameter("type");
 String typeName = "";
 if("1".equals(type)){
	 typeName = "删除";
}else if("2".equals(type)){
	 typeName = "新增";
}else{
	// response.sendRedirect("../../login.jsp");
 }
 %> 
<script language="JavaScript">

function ToExcel(cityId) {
	var type1 = document.getElementById("type").value;
	var page="<s:url value='/gwms/config/setMulticastBatch!getInfoExcel.action'/>?"
		+ "type=" +type1;
	document.all("childFrm").src=page;
}

function openDev(cityId,status){
	var type1 = document.getElementById("type").value;
	var page="<s:url value='/gwms/config/setMulticastBatch!getDev.action'/>?"
		+ "cityId=" + cityId 
		+"&status=" + status
		+ "&type=" +type1;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

</script>

<br>
<TABLE>
	<%if("1".equals(type)){ %>
	<input type=hidden id = "type" name="type" value="1"/>
	<%} else{%>
	<input type=hidden id = "type" name="type" value="2"/>
	<% }%>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
					修改IPTV组播参数统计
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
				<table class="querytable">
					<tr>
					<th colspan=4>
					修改IPTV组播参数统计
					</th>
					</tr>
					
<table class="listtable">
	<caption>
		统计结果
	</caption>
	<thead>
		<tr>
			<th rowspan="2">
				属地
			</th>
			<th rowspan="2">
				总配置数
			</th>
			<th rowspan="2">
				成功
			</th>
			<th rowspan="2">
				未触发
			</th>
			<th colspan="2">
				失败
			</th>
		</tr>	
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','');">
							<s:property value="city_name" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','');">
							<s:property value="allup" /> </a>

					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','11');">
							<s:property value="successnum" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','2');">
							<s:property value="noupnum" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','3');">
							<s:property value="failnum" /> </a>
					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="cityId"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="7">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>
					
</table>
		</td>
	</tr>

</TABLE>

<%@ include file="/foot.jsp"%>
