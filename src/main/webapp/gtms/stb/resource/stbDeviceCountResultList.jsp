<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>全省IPTV用户终端分布及活跃率情况结果</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
		parent.isShowButton(true);
		parent.isCountDesc("");
	});
	
	function toExcel()
	{
		var is_exp=$("input[@name='is_exp']").val();
		if(is_exp==0){
			$("input[@name='is_exp']").val("1");
			parent.query('exp',$("input[@name='queryTime']").val());
		}else{
			alert("报表正在导出中，请勿重复操作！");
		}
	}
</script>
</head>

<body>
<input type='hidden' name="is_exp" value="0" />
<input type='hidden' name="queryTime" value="<s:property value='queryTime' />" />
<table>
<tr>
	<td>
		<table class="listtable">
			<caption>全省IPTV用户终端分布及活跃率情况</caption>
			<thead>
				<tr>
					<th>单位</th>
					<th>终端总数</th>
					<th>其中4K机顶盒</th>
					<th>其中非4K机顶盒</th>
					<th>已部署探针</th>
					<th>探针部署率</th>
					<th>活跃数</th>
					<th>活跃率</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="data!=null && data.size()>0">
					<s:iterator value="data">
						<tr>
							<td><s:property value="city_name" /></td>
							<td><s:property value="all_num" /></td>
							<td><s:property value="Y_4K_num" /></td>
							<td><s:property value="N_4K_num" /></td>
							<td><s:property value="is_probe_num" /></td>
							<td><s:property value="probe_rate" /></td>
							<td><s:property value="month_active_num" /></td>
							<td><s:property value="month_active_rate" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=8>没有统计到数据！</td>
					</tr>
				</s:else>
			</tbody>
			<s:if test="data!=null && data.size()>0">
			<tfoot>
				<tr>
					<td colspan="8">
						<img src="<s:url value="/images/excel.gif"/>" border='0' alt='导出列表'
							style='cursor: hand' onclick="toExcel()">
					</td>
				</tr>
			</tfoot>
			</s:if>
			<tr STYLE="display: none">
				<td colspan="8">
					<iframe id="childFrm" src=""></iframe>
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>
</body>
</html>