<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%String absPath=request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>查询结果</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	 
	function ToExcel()
	{
		var sure = window.confirm("1.不支持大数据量导出,会造成WEB异常!\n2.如要导出,请晚上在操作!\n是否继续导出");
		if(!sure)
		{
			return;
		}
		var form = parent.document.getElementById("gwShare_selectForm");
		form.action="<s:url value='/gtms/stb/resource/stbReport!getExcel.action'/>";
		form.submit();
	} 
	</script>

</head>
<body>
	<table class="listtable" id="listTable">
		<caption>查询结果</caption>
		<thead>
			<tr>
				<th>设备厂商</th>
				<th>型号</th>
				<th>软件版本</th>
				<!-- <th>ITMS接收时间</th> -->
				<th>属地</th>
				<th>设备序列号</th>
				<th>业务账号</th>
				<th>MAC</th>
				<th>设备IP</th>
				<th>最近上报时间</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="data!=null">
				<s:if test="data.size()>0">
					<s:iterator value="data">
						<tr>
							<td><s:property value="vendor_name" /></td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="softwareversion" /></td>
							<td><s:property value="city_name" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="serv_account" /></td>
							<td><s:property value="cpe_mac" /></td>
							<td><s:property value="loopback_ip" /></td>
							<td><s:property value="complete_time" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=9>系统中没有查询出需要的信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>系统中没有查询出需要的信息!</td>
				</tr>
			</s:else>
			<tr bgcolor="#FFFFFF">
				<td colspan="9" align="left"><IMG SRC="/itms/images/excel.gif"
					BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()"></td>
			</tr>
		</tbody>
		<tfoot>
			<tr bgcolor="#FFFFFF">

				<td colspan="9" align="right"><lk:pages
						url="/gtms/stb/resource/stbReport!query.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
			<tr STYLE="display: none">
				<td><iframe id="childFrm" src=""></iframe></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>